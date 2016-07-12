/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with v3m0. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.authentication;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.*;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.EmptyCommandArgumentMarshaller;
import com.eightkdata.mongowp.server.api.MarshalException;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;

/**
 *
 */
public class GetNonceCommand extends AbstractCommand<Empty, String>{

    public static final GetNonceCommand INSTANCE = new GetNonceCommand();
    private static final String COMMAND_NAME = "getnonce";
    private static final StringField NONCE_FIELD = new StringField("nonce");

    private GetNonceCommand() {
        super(COMMAND_NAME);
    }

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public Class<? extends Empty> getArgClass() {
        return Empty.class;
    }

    @Override
    public Empty unmarshallArg(BsonDocument requestDoc) throws BadValueException,
            TypesMismatchException, NoSuchKeyException, FailedToParseException {
        return Empty.getInstance();
    }

    @Override
    public BsonDocument marshallArg(Empty request) throws MarshalException {
        return EmptyCommandArgumentMarshaller.marshallEmptyArgument(this);
    }

    @Override
    public Class<? extends String> getResultClass() {
        return String.class;
    }

    @Override
    public String unmarshallResult(BsonDocument resultDoc) throws BadValueException,
            TypesMismatchException, NoSuchKeyException, FailedToParseException, MongoException {
        return BsonReaderTool.getString(resultDoc, NONCE_FIELD);
    }

    @Override
    public BsonDocument marshallResult(String result) throws MarshalException {
        return new BsonDocumentBuilder()
                .append(NONCE_FIELD, result)
                .build();
    }


}