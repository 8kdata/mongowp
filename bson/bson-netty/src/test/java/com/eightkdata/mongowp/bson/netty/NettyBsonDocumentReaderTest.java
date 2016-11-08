/*
 * MongoWP - MongoWP: Bson Netty
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.eightkdata.mongowp.bson.netty;

import com.eightkdata.mongowp.bson.netty.pool.AlwaysFalseStringPool;
import com.eightkdata.mongowp.bson.org.bson.utils.MongoBsonTranslator;
import com.eightkdata.mongowp.bson.utils.BsonDocumentReader.AllocationType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Collection;
import org.bson.BsonDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author gortiz
 */
@RunWith(Parameterized.class)
public class NettyBsonDocumentReaderTest {

    private final ByteBuf byteBuf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

    private static final NettyStringReader STRING_READER = new PooledNettyStringReader(AlwaysFalseStringPool.getInstance());
    private final NettyBsonDocumentReader reader = new NettyBsonDocumentReader(
            new DefaultNettyBsonLowLevelReader(STRING_READER),
            new OffHeapNettyBsonLowLevelReader(STRING_READER),
            new OffHeapValuesNettyBsonLowLevelReader(STRING_READER)
    );

    @Parameters(name = "{0}")
    public static Collection<Object[]> documents() throws IOException {
        return MongoDocumentProvider.readTestDocuments();
    }

    @Parameter(0)
    public String name;

    @Parameter(1)
    public BsonDocument mongoDoc;

    @Before
    public void setUp() {
        byteBuf.clear();
    }

    private void test(AllocationType allocationType) throws NettyBsonReaderException, IOException {
        assert mongoDoc != null : "A null document parameter has been injected";

        MongoBsonUtils.write(mongoDoc, byteBuf);

        com.eightkdata.mongowp.bson.BsonDocument wpDocument
                = reader.readDocument(allocationType, byteBuf);

        BsonDocument written = MongoBsonTranslator.translate(wpDocument);

        assertEquals(mongoDoc, written);

    }

    @Test
    public void readHeapTest() throws NettyBsonReaderException, IOException {
        test(AllocationType.HEAP);
    }

    @Test
    public void readOffHeapTest() throws NettyBsonReaderException, IOException {
        test(AllocationType.OFFHEAP);
    }
    
    @Test
    public void readOffHeapValuesTest() throws NettyBsonReaderException, IOException {
        test(AllocationType.OFFHEAP_VALUES);
    }
}
