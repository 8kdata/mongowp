/*
 * MongoWP - MongoWP: Bson
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
package com.eightkdata.mongowp.bson;

/**
 *
 */
public interface BsonTimestamp extends BsonValue<BsonTimestamp> {

    int getSecondsSinceEpoch();

    int getOrdinal();

    /**
     * Two BsonTimestap are equal if their secondsSinceEpoch and ordinal
     * properties are equal.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj);

    /**
     * The hashCode of a BsonTimestamp is <code>getSecondsSinceEpoch() &lt;&lt; 4 | (getOrdinal() &amp; 0xf)</code>.
     * @return
     */
    @Override
    public int hashCode();

    public default long toRawData() {
        return getSecondsSinceEpoch() << 32 + getOrdinal();
    }
}
