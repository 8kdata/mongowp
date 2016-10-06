
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.IntField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.DropIndexesCommand.DropIndexesArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.DropIndexesCommand.DropIndexesResult;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;

/**
 *
 */
public class DropIndexesCommand extends AbstractCommand<DropIndexesArgument, DropIndexesResult> {

    private final static String COMMAND_NAME = "dropIndexes";
    
    public static final DropIndexesCommand INSTANCE = new DropIndexesCommand();

    private DropIndexesCommand() {
        super(COMMAND_NAME);
    }

    protected DropIndexesCommand(String commandName) {
        super(commandName);
    }

    @Override
    public Class<? extends DropIndexesArgument> getArgClass() {
        return DropIndexesArgument.class;
    }

    @Override
    public DropIndexesArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        return DropIndexesArgument.unmarshall(requestDoc, getCommandName());
    }

    @Override
    public BsonDocument marshallArg(DropIndexesArgument request) {
        return request.marshall();
    }

    @Override
    public Class<? extends DropIndexesResult> getResultClass() {
        return DropIndexesResult.class;
    }

    @Override
    public BsonDocument marshallResult(DropIndexesResult reply) {
        return reply.marshall();
    }

    @Override
    public DropIndexesResult unmarshallResult(BsonDocument replyDoc) throws TypesMismatchException, NoSuchKeyException {
        return DropIndexesResult.unmarshall(replyDoc);
    }

    public static class DropIndexesArgument {
        private final static StringField COLLECTION_FIELD = new StringField(COMMAND_NAME);
        private final static StringField INDEX_FIELD = new StringField("index");

        private final String collection;
        private final String indexToDrop;
        private final boolean dropAllIndexes;

        public DropIndexesArgument(String collection, String indexToDrop) {
            this.collection = collection;
            this.indexToDrop = indexToDrop;
            dropAllIndexes = indexToDrop.equals("*");
        }

        public String getCollection() {
            return collection;
        }

        public String getIndexToDrop() {
            return indexToDrop;
        }
        
        public boolean isDropAllIndexes() {
            return dropAllIndexes;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(COLLECTION_FIELD, collection)
                    .append(INDEX_FIELD, indexToDrop)
                    .build();
        }

        private static DropIndexesArgument unmarshall(BsonDocument requestDoc, String commandName)
                throws TypesMismatchException, NoSuchKeyException, BadValueException {
            String collection = BsonReaderTool.getString(requestDoc, commandName);
            String indexToDrop = BsonReaderTool.getString(requestDoc, INDEX_FIELD);
            return new DropIndexesArgument(collection, indexToDrop);
        }
    }

    public static class DropIndexesResult {
        private static final IntField N_INDEXES_WAS_FIELD = new IntField("nIndexesWas");

        private final int nIndexesWas;

        public DropIndexesResult(
                int nIndexesWas) {
            this.nIndexesWas = nIndexesWas;
        }

        public int getNIndexesWas() {
            return nIndexesWas;
        }

        private static DropIndexesResult unmarshall(BsonDocument replyDoc) throws TypesMismatchException, NoSuchKeyException {
            int nIndexesWas = BsonReaderTool.getInteger(replyDoc, N_INDEXES_WAS_FIELD);
            return new DropIndexesResult(nIndexesWas);
        }

        private BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();
            builder.append(N_INDEXES_WAS_FIELD, nIndexesWas);
            return builder.build();
        }


    }

}
