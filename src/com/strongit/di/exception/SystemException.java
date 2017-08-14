package com.strongit.di.exception;

import java.sql.SQLException;

/**
 * <p>Title: Hotel System</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) GNT Corp. 2007</p>
 * <p>Company: </p>
 * @author minhongbin@hotmail.com
 * @version 2.0
 */

public class SystemException
    extends BaseException {

  private Throwable rootCause;
  private String errCode;

  public SystemException() {
  }

  public SystemException(String code, String message) {
    super(message);
    this.errCode = code;
  }

  public SystemException(String code, Throwable t) {
    super(getAllMessage(t));
    this.errCode = code;
    this.rootCause = t;
  }

  static private String getAllMessage(Throwable t) {
    String message = null;
    if (t != null) {
      message = t.getMessage();
      if (t instanceof SQLException) {
        SQLException ex = (SQLException) t;
        String nextMessage = getAllMessage(ex.getNextException());
        if (nextMessage != null) {
          message += "\r\n" + nextMessage;
        }
      }
    }
    return message;
  }

  public SystemException(String code, String message, Throwable t) {
    super(message + "\r\n" + getAllMessage(t));
    this.errCode = code;
    this.rootCause = t;
  }

  public String getCode() {
    return this.errCode;
  }

  public Throwable getRootCause() {
    return this.rootCause;
  }

}