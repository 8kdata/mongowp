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

package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class NotMasterNoSlaveOkCodeException extends MongoException {

  private static final long serialVersionUID = 1L;

  public NotMasterNoSlaveOkCodeException(String customMessage) {
    super(customMessage, ErrorCode.NOT_MASTER_NO_SLAVE_OK_CODE);
  }

  public NotMasterNoSlaveOkCodeException(String customMessage, Throwable cause) {
    super(customMessage, cause, ErrorCode.NOT_MASTER_NO_SLAVE_OK_CODE);
  }

  public NotMasterNoSlaveOkCodeException() {
    super(ErrorCode.NOT_MASTER_NO_SLAVE_OK_CODE);
  }

  public NotMasterNoSlaveOkCodeException(Throwable cause) {
    super(cause, ErrorCode.NOT_MASTER_NO_SLAVE_OK_CODE);
  }
}
