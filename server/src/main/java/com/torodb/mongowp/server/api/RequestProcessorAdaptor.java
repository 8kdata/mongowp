/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torodb.mongowp.server.api;

import com.torodb.mongowp.MongoConstants;
import com.torodb.mongowp.Status;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.utils.DefaultBsonValues;
import com.torodb.mongowp.commands.Command;
import com.torodb.mongowp.commands.CommandLibrary.LibraryEntry;
import com.torodb.mongowp.commands.Connection;
import com.torodb.mongowp.commands.ErrorHandler;
import com.torodb.mongowp.commands.MarshalException;
import com.torodb.mongowp.commands.Request;
import com.torodb.mongowp.commands.Request.ExternalClientInfo;
import com.torodb.mongowp.commands.SafeRequestProcessor;
import com.torodb.mongowp.commands.pojos.QueryRequest;
import com.torodb.mongowp.exceptions.CommandNotFoundException;
import com.torodb.mongowp.exceptions.FailedToParseException;
import com.torodb.mongowp.exceptions.MongoException;
import com.torodb.mongowp.exceptions.UnauthorizedException;
import com.torodb.mongowp.fields.DoubleField;
import com.torodb.mongowp.fields.IntField;
import com.torodb.mongowp.fields.StringField;
import com.torodb.mongowp.messages.request.DeleteMessage;
import com.torodb.mongowp.messages.request.GetMoreMessage;
import com.torodb.mongowp.messages.request.InsertMessage;
import com.torodb.mongowp.messages.request.KillCursorsMessage;
import com.torodb.mongowp.messages.request.QueryMessage;
import com.torodb.mongowp.messages.request.QueryMessage.QueryOptions;
import com.torodb.mongowp.messages.request.RequestOpCode;
import com.torodb.mongowp.messages.request.UpdateMessage;
import com.torodb.mongowp.messages.response.ReplyMessage;
import com.torodb.mongowp.server.callback.MessageReplier;
import com.torodb.mongowp.server.callback.RequestProcessor;
import com.torodb.mongowp.utils.BsonDocumentBuilder;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 *
 */
public class RequestProcessorAdaptor<C extends Connection> implements RequestProcessor {

  public final AttributeKey<C> connection = AttributeKey.valueOf(
      RequestProcessorAdaptor.class.getCanonicalName() + ".connection");

  public static final String QUERY_MESSAGE_COMMAND_COLLECTION = "$cmd";
  public static final String QUERY_MESSAGE_ADMIN_DATABASE = "admin";
  public static final IntField ERR_CODE = new IntField("code");
  public static final StringField ERR_MSG_FIELD = new StringField("errmsg");
  public static final DoubleField OK_FIELD = new DoubleField("ok");

  private final SafeRequestProcessor<C> safeRequestProcessor;
  private final ErrorHandler errorHandler;

  @Inject
  public RequestProcessorAdaptor(
      SafeRequestProcessor<C> safeRequestProcessor,
      ErrorHandler errorHandler) {
    this.safeRequestProcessor = safeRequestProcessor;
    this.errorHandler = errorHandler;
  }

  @Nonnull
  protected Connection getConnection(AttributeMap attMap) {
    return attMap.attr(connection).get();
  }

  @Nonnull
  protected C getConnection(MessageReplier messageReplier) {
    return messageReplier.getAttributeMap().attr(connection).get();
  }

  @Override
  public void onChannelActive(AttributeMap attMap) {
    C newConnection = safeRequestProcessor.openConnection();
    Connection oldConnection = attMap.attr(connection).setIfAbsent(
        newConnection
    );
    if (oldConnection != null) {
      throw new IllegalArgumentException("A connection with id "
          + oldConnection.getConnectionId() + " was stored before "
          + "channel became active!");
    }
  }

  @Override
  public void onChannelInactive(AttributeMap attMap) {
    C connection = attMap.attr(this.connection).getAndRemove();
    if (connection != null) {
      connection.close();
    }
  }

  @Override
  public void queryMessage(QueryMessage queryMessage, MessageReplier messageReplier) throws
      MongoException {
    C connection = getConnection(messageReplier);

    if (QUERY_MESSAGE_COMMAND_COLLECTION.equals(queryMessage.getCollection())) {
      executeCommand(connection, queryMessage, messageReplier);
    } else {
      QueryRequest.Builder requestBuilder = new QueryRequest.Builder(
          queryMessage.getDatabase(),
          queryMessage.getCollection()
      );
      QueryOptions queryOptions = queryMessage.getQueryOptions();
      requestBuilder.setCollection(queryMessage.getCollection())
          .setQuery(queryMessage.getQuery())
          .setProjection(null)
          .setNumberToSkip(queryMessage.getNumberToSkip())
          .setLimit(queryMessage.getNumberToReturn())
          .setAwaitData(queryOptions.isAwaitData())
          .setExhaust(queryOptions.isExhaust())
          .setNoCursorTimeout(queryOptions.isNoCursorTimeout())
          .setOplogReplay(queryOptions.isOplogReplay())
          .setPartial(queryOptions.isPartial())
          .setSlaveOk(queryOptions.isSlaveOk())
          .setTailable(queryOptions.isTailable());

      if (requestBuilder.getLimit() < 0) {
        requestBuilder.setAutoclose(true);
        requestBuilder.setLimit(-requestBuilder.getLimit());
      } else if (requestBuilder.getLimit() == 1) {
        requestBuilder.setAutoclose(true);
      }

      ReplyMessage reply = safeRequestProcessor.query(
          connection,
          new Request(
              queryMessage.getDatabase(),
              new ExternalClientInfo(queryMessage.getClientAddress(), queryMessage.getClientPort()),
              requestBuilder.isSlaveOk(),
              null //Set the requested timeout
          ),
          messageReplier.getRequestId(),
          requestBuilder.build()
      );
      messageReplier.replyMessage(reply);
    }

  }

  @SuppressWarnings("unchecked")
  private void executeCommand(
      C connection,
      QueryMessage queryMessage,
      MessageReplier messageReplier) throws MongoException {
    BsonDocument document = queryMessage.getQuery();
    LibraryEntry libraryEntry = safeRequestProcessor.getCommandsLibrary().find(document);
    Command command;
    if (libraryEntry == null) {
      command = null;
    } else {
      command = libraryEntry.getCommand();
    }
    if (command == null) {
      if (document.isEmpty()) {
        throw new CommandNotFoundException("Empty document query");
      }
      String firstKey = document.iterator().next().getKey();
      throw new CommandNotFoundException(firstKey);
    }

    if (command.isAdminOnly()) {
      if (!QUERY_MESSAGE_ADMIN_DATABASE.equals(queryMessage.getDatabase())) {
        throw new UnauthorizedException(
            command.getCommandName() + "may only be run "
            + "against the admin database."
        );
      }
    }

    Object arg = command.unmarshallArg(document, libraryEntry.getAlias());

    Request request = new Request(
        queryMessage.getDatabase(),
        new ExternalClientInfo(queryMessage.getClientAddress(), queryMessage.getClientPort()),
        queryMessage.getQueryOptions().isSlaveOk(),
        null //Set the requested timeout
    );
    Status<?> reply = safeRequestProcessor.execute(request, command, arg, connection);

    BsonDocument bson;
    if (reply.isOk()) {
      try {
        bson = command.marshallResult(reply.getResult());
        if (bson == null) {
          bson = DefaultBsonValues.EMPTY_DOC;
        } else {
          if (!bson.containsKey(OK_FIELD.getFieldName())) {
            bson = new BsonDocumentBuilder(bson)
                .append(OK_FIELD, MongoConstants.OK)
                .build();
          }
        }
      } catch (MarshalException ex) {
        throw new FailedToParseException(ex.getLocalizedMessage());
      }
    } else {
      bson = new BsonDocumentBuilder()
          .append(ERR_CODE, reply.getErrorCode().getErrorCode())
          .append(ERR_MSG_FIELD, reply.getErrorMsg())
          .append(OK_FIELD, MongoConstants.KO)
          .build();
    }

    messageReplier.replyMessageNoCursor(bson);
  }

  @Override
  public void getMore(GetMoreMessage getMoreMessage, MessageReplier messageReplier) {
    C connection = getConnection(messageReplier);
    try {
      Request req = new Request(
          getMoreMessage.getDatabase(),
          new ExternalClientInfo(getMoreMessage.getClientAddress(), getMoreMessage.getRequestId()),
          true,
          null //Set the requested timeout
      );

      ReplyMessage reply = safeRequestProcessor.getMore(connection, req, messageReplier
          .getRequestId(), getMoreMessage);
      messageReplier.replyMessage(reply);
    } catch (MongoException ex) {
      errorHandler.handleMongodbException(connection, messageReplier.getRequestId(), false, ex);
    }
  }

  @Override
  public void killCursors(KillCursorsMessage killCursorsMessage, MessageReplier messageReplier) {
    C connection = getConnection(messageReplier);
    try {
      Request req = new Request(
          "admin", //an arbitary database
          new ExternalClientInfo(killCursorsMessage.getClientAddress(), killCursorsMessage
              .getRequestId()),
          true,
          null //Set the requested timeout
      );
      safeRequestProcessor.killCursors(connection, req, killCursorsMessage);
    } catch (MongoException ex) {
      errorHandler.handleMongodbException(connection, messageReplier.getRequestId(), false, ex);
    }
  }

  @Override
  public void insert(InsertMessage insertMessage, MessageReplier messageReplier) {
    C connection = getConnection(messageReplier);
    try {
      Request req = new Request(
          insertMessage.getDatabase(),
          new ExternalClientInfo(insertMessage.getClientAddress(), insertMessage.getRequestId()),
          false,
          null //Set the requested timeout
      );
      safeRequestProcessor.insert(connection, req, insertMessage);
    } catch (MongoException ex) {
      errorHandler.handleMongodbException(connection, messageReplier.getRequestId(), false, ex);
    }
  }

  @Override
  public void update(UpdateMessage updateMessage, MessageReplier messageReplier) {
    C connection = getConnection(messageReplier);
    try {
      Request req = new Request(
          updateMessage.getDatabase(),
          new ExternalClientInfo(updateMessage.getClientAddress(), updateMessage.getRequestId()),
          false,
          null //Set the requested timeout
      );
      safeRequestProcessor.update(connection, req, updateMessage);
    } catch (MongoException ex) {
      errorHandler.handleMongodbException(connection, messageReplier.getRequestId(), false, ex);
    }
  }

  @Override
  public void delete(DeleteMessage deleteMessage, MessageReplier messageReplier) {
    C connection = getConnection(messageReplier);
    try {
      Request req = new Request(
          deleteMessage.getDatabase(),
          new ExternalClientInfo(deleteMessage.getClientAddress(), deleteMessage.getRequestId()),
          false,
          null //Set the requested timeout
      );
      safeRequestProcessor.delete(connection, req, deleteMessage);
    } catch (MongoException ex) {
      errorHandler.handleMongodbException(connection, messageReplier.getRequestId(), false, ex);
    }
  }

  @Override
  public boolean handleError(RequestOpCode requestOpCode, MessageReplier messageReplier,
      Throwable throwable) {
    Connection connection = getConnection(messageReplier);

    ReplyMessage handleMongodbException;
    if (throwable instanceof MongoException) {
      handleMongodbException = errorHandler.handleMongodbException(connection,
          messageReplier.getRequestId(),
          requestOpCode.canReply(),
          (MongoException) throwable
      );
    } else {
      handleMongodbException = errorHandler.handleUnexpectedError(
          connection,
          messageReplier.getRequestId(),
          requestOpCode.canReply(),
          throwable
      );
    }
    if (requestOpCode.canReply() && handleMongodbException != null) {
      messageReplier.replyMessage(handleMongodbException);
    }
    return true;
  }
}
