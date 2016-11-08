/*
 * MongoWP - Mongo Client: Driver Wrapper
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
package com.eightkdata.mongowp.client.wrapper;

import com.eightkdata.mongowp.client.core.MongoClientFactory;
import com.eightkdata.mongowp.client.core.UnreachableMongoServerException;
import com.google.common.net.HostAndPort;
import com.google.inject.Inject;

/**
 *
 */
public class MongoClientWrapperFactory implements MongoClientFactory {

    private final MongoClientConfiguration mongoClientConfiguration;

    @Inject
    public MongoClientWrapperFactory(MongoClientConfiguration mongoClientConfiguration) {
        this.mongoClientConfiguration = mongoClientConfiguration;
    }

    @Override
    public MongoClientWrapper createClient(HostAndPort address) throws UnreachableMongoServerException {
        return new MongoClientWrapper(mongoClientConfiguration.builder(address).build());
    }

}
