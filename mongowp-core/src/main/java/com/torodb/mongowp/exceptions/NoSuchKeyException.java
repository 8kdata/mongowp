/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torodb.mongowp.exceptions;

import com.torodb.mongowp.ErrorCode;

/**
 *
 */
public class NoSuchKeyException extends MongoException {

  private static final long serialVersionUID = 1L;
  private final String key;

  public NoSuchKeyException(String key) {
    super(ErrorCode.NO_SUCH_KEY, key);
    this.key = key;
  }

  public NoSuchKeyException(String key, String customMessage) {
    super(customMessage, ErrorCode.NO_SUCH_KEY);
    this.key = key;
  }

  public String getKey() {
    return key;
  }

}
