/*
 * MongoWP
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.abst.AbstractBsonDouble;

/**
 *
 */
public class PrimitiveBsonDouble extends AbstractBsonDouble {

  private static final long serialVersionUID = -8649710470577957984L;

  private static final PrimitiveBsonDouble ZERO = new PrimitiveBsonDouble(0);
  private static final PrimitiveBsonDouble ONE = new PrimitiveBsonDouble(1);

  private final double value;

  private PrimitiveBsonDouble(double value) {
    this.value = value;
  }

  public static PrimitiveBsonDouble newInstance(double value) {
    if (value == 0) {
      return ZERO;
    }
    if (value == 1) {
      return ONE;
    }
    return new PrimitiveBsonDouble(value);
  }

  @Override
  public double doubleValue() {
    return value;
  }

  @Override
  public Double getValue() {
    return value;
  }

}
