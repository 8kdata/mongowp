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

package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.server.api.CommandResult;
import com.eightkdata.mongowp.server.callback.WriteOpResult;

import javax.annotation.Nonnull;

/**
 *
 */
public class WriteCommandResult<R> implements CommandResult<R> {

  @Nonnull
  private final R result;
  private final WriteOpResult writeOpResult;

  public WriteCommandResult(@Nonnull R result, @Nonnull WriteOpResult writeOpResult) {
    this.result = result;
    this.writeOpResult = writeOpResult;
  }

  @Nonnull
  @Override
  public WriteOpResult getWriteOpResult() {
    return writeOpResult;
  }

  @Override
  @Nonnull
  public R getResult() {
    return result;
  }

}
