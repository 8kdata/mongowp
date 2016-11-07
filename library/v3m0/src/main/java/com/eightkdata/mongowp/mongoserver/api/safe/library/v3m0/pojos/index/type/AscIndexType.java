/*
 *     This file is part of ToroDB.
 *
 *     ToroDB is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     ToroDB is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with ToroDB. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */

package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.index.type;

import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;

public class AscIndexType extends AbstractIndexType {

    public static final AscIndexType INSTANCE = new AscIndexType();
    
    private AscIndexType() {
        super(DefaultBsonValues.newInt(1));
    }

    @Override
    public boolean equalsToBsonValue(BsonValue<?> bsonValue) {
        return sameNumber(bsonValue);
    }

    @Override
    public <Arg, Result> Result accept(IndexTypeVisitor<Arg, Result> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}