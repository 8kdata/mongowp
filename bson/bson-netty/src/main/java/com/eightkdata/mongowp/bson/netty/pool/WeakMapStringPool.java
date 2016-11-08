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
package com.eightkdata.mongowp.bson.netty.pool;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import io.netty.buffer.ByteBuf;

/**
 *
 */
public class WeakMapStringPool extends StringPool {
    private final Interner<String> interner;

    public WeakMapStringPool(StringPoolPolicy heuristic) {
        super(heuristic);
        this.interner = Interners.newWeakInterner();
    }

    @Override
    protected String retrieveFromPool(ByteBuf stringBuf) {
        return interner.intern(getString(stringBuf));
    }

}
