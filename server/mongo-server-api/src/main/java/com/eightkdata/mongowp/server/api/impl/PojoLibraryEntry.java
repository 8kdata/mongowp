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
 * along with mongo-server-api. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandsLibrary.LibraryEntry;

/**
 *
 */
public class PojoLibraryEntry implements LibraryEntry {

    private final String alias;
    private final Command<?, ?> command;

    public PojoLibraryEntry(String alias, Command<?, ?> command) {
        this.alias = alias;
        this.command = command;
    }

    @Override
    public Command getCommand() {
        return command;
    }

    @Override
    public String getAlias() {
        return alias;
    }

}
