/*
 * MongoWP - Mongo Server: Wire Protocol Layer
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
package com.eightkdata.mongowp.server.decoder;

import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.messages.request.RequestMessage;
import com.eightkdata.mongowp.exceptions.InvalidNamespaceException;
import com.eightkdata.mongowp.exceptions.MongoException;
import io.netty.buffer.ByteBuf;

/**
 *
 */
public interface MessageDecoder<T extends RequestMessage> {
    /**
     * Decodes a message from a ByteBuf, positioned just before the body's content beginning
     * @param buffer
     * @param requestBaseMessage
     * @return
     * @throws MongoException If it was impossible to decode the message
     * @throws InvalidNamespaceException If the message expected a namespace but
     *                                   an invalid namespace is provided
     *
     */
    public T decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws MongoException, InvalidNamespaceException;
}
