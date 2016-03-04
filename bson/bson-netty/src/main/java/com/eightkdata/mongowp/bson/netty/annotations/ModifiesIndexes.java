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
 * along with bson-netty. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.bson.netty.annotations;

import io.netty.buffer.ByteBuf;
import java.lang.annotation.*;

/**
 * This annotation is used to identify when the indexes of a {@link ByteBuf}
 * parameter will be modified by the method that recives the parameter or the
 * constructor (and the created object) that recives the parameter.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PARAMETER})
@Documented
public @interface ModifiesIndexes {
    boolean value() default true;
}
