/*
 * MongoWP - MongoWP: Core
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
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © 2014 8Kdata (www.8kdata.com)
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
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © 2014 8Kdata (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © ${project.inceptionYear} 8Kdata (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © ${project.inceptionYear} ${owner} (${email})
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.utils;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.annotations.Material;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.*;
import com.eightkdata.mongowp.bson.impl.InstantBsonDateTime;
import com.eightkdata.mongowp.bson.impl.LongBsonDateTime;
import com.eightkdata.mongowp.fields.*;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.net.HostAndPort;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.eightkdata.mongowp.bson.utils.DefaultBsonValues.*;

/**
 *
 */
public class BsonDocumentBuilder {

    private final LinkedHashMap<String, BsonValue<?>> map;
    private boolean built;

    public BsonDocumentBuilder() {
        this.map = new LinkedHashMap<>();
        built = false;
    }

    public BsonDocumentBuilder(int initialCapacity) {
        this.map = new LinkedHashMap<>(initialCapacity);
        built = false;
    }

    public BsonDocumentBuilder(BsonDocument doc) {
        this.map = new LinkedHashMap<>(doc.size());
        for (Entry<?> entry : doc) {
            map.put(entry.getKey(), entry.getValue());
        }
        built = false;
    }

    public boolean containsField(@Nonnull BsonField<?,?> field) {
        Preconditions.checkState(!built);
        return map.containsKey(field.getFieldName());
    }

    public BsonDocumentBuilder copy(@Nonnull BsonDocument otherDoc) {
        Preconditions.checkState(!built);
        for (Entry<?> entrySet : otherDoc) {
            map.put(entrySet.getKey(), entrySet.getValue());
        }
        return this;
    }

    public BsonDocumentBuilder appendUnsafe(String fieldName, @Nullable BsonValue value) {
        Preconditions.checkState(!built);
        if (value == null) {
            map.put(fieldName, NULL);
            return this;
        }
        map.put(fieldName, value);
        return this;
    }

    public <JV> BsonDocumentBuilder append(BsonField<JV, BsonValue<JV>> field, @Nullable BsonValue<JV> value) {
        Preconditions.checkState(!built);
        if (value == null) {
            map.put(field.getFieldName(), NULL);
            return this;
        }
        map.put(field.getFieldName(), value);
        return this;
    }

    public <T> BsonDocumentBuilder append(BsonField<T, BsonValue<T>> field, T value, Function<T, BsonValue<T>> translator) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(field.getFieldName(), translator.apply(value));
        return this;
    }

    public BsonDocumentBuilder append(BooleanField field, boolean value) {
        Preconditions.checkState(!built);
        map.put(field.getFieldName(), newBoolean(value));
        return this;
    }

    public BsonDocumentBuilder appendNumber(NumberField<?> field, Number value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(field.getFieldName(), toBsonNumber(value));
        return this;
    }

    private BsonNumber toBsonNumber(Number number) {
        if (number instanceof Double || number instanceof Float) {
            return newDouble(number.doubleValue());
        }
        if (number instanceof Long) {
            long longValue = number.longValue();
            if (longValue <= Integer.MAX_VALUE && longValue >= Integer.MAX_VALUE) {
                return newInt((int) longValue);
            }
            return newLong(longValue);
        }
        if (number instanceof Integer || number instanceof Byte || number instanceof Short) {
            return newInt(number.intValue());
        }
        throw new IllegalArgumentException("Numbers of class " + number.getClass() + " are not supported");
    }

    public BsonDocumentBuilder appendNumber(NumberField<?> field, int value) {
        Preconditions.checkState(!built);
        map.put(field.getFieldName(), newInt(value));
        return this;
    }

    public BsonDocumentBuilder appendNumber(NumberField<?> field, long value) {
        Preconditions.checkState(!built);
        if (value < Integer.MAX_VALUE && value > Integer.MIN_VALUE) {
            map.put(field.getFieldName(), newInt((int) value));
        }
        else {
            map.put(field.getFieldName(), newLong(value));
        }
        return this;
    }

    public BsonDocumentBuilder append(IntField field, int value) {
        Preconditions.checkState(!built);
        map.put(field.getFieldName(), newInt(value));
        return this;
    }

    public BsonDocumentBuilder append(LongField field, long value) {
        Preconditions.checkState(!built);
        map.put(field.getFieldName(), newLong(value));
        return this;
    }

    public BsonDocumentBuilder append(StringField field, String value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(field.getFieldName(), newString(value));
        return this;
    }

    public BsonDocumentBuilder append(DoubleField field, double value) {
        Preconditions.checkState(!built);
        map.put(field.getFieldName(), newDouble(value));
        return this;
    }

    public BsonDocumentBuilder append(DateTimeField field, Instant value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(field.getFieldName(), new InstantBsonDateTime(value));
        return this;
    }

    /**
     *
     * @param field
     * @param value millis since Epoch
     * @return
     */
    public BsonDocumentBuilder appendInstant(DateTimeField field, long value) {
        Preconditions.checkState(!built);
        map.put(field.getFieldName(), new LongBsonDateTime(value));
        return this;
    }

    public BsonDocumentBuilder append(TimestampField field, OpTime value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(
                field.getFieldName(),
                value.getTimestamp()
        );
        return this;
    }

    public BsonDocumentBuilder append(TimestampField field, BsonTimestamp value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(
                field.getFieldName(),
                value
        );
        return this;
    }

    public BsonDocumentBuilder append(ArrayField field, List<BsonValue<?>> list) {
        return append(field, newArray(list));
    }

    public BsonDocumentBuilder append(ArrayField field, BsonArray value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(field.getFieldName(), value);
        return this;
    }

    public BsonDocumentBuilder append(DocField field, BsonDocument value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(field.getFieldName(), value);
        return this;
    }

    public BsonDocumentBuilder append(DocField field, BsonDocumentBuilder value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(field.getFieldName(), value.build());
        return this;
    }

    public BsonDocumentBuilder append(HostAndPortField field, HostAndPort value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(field.getFieldName(), newString(value.toString()));
        return this;
    }

    public BsonDocumentBuilder append(HostAndPortField field, String value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(field.getFieldName(), newString(value));
        return this;
    }

    public BsonDocumentBuilder append(ObjectIdField field, BsonObjectId value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return appendNull(field);
        }
        map.put(field.getFieldName(), value);
        return this;
    }

    public BsonDocumentBuilder appendNull(BsonField<?, ?> field) {
        Preconditions.checkState(!built);
        map.put(field.getFieldName(), NULL);
        return this;
    }

    @Material
    public BsonDocument build() {
        built = true;
        return newDocument(map);
    }

}
