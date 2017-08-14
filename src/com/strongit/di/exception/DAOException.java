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

public class DAOException
    extends BaseException {

  private Throwable rootCause;
  private String errCode;

  public DAOException() {
  }

  public DAOException(String code, String message) {
    super(message);
    this.errCode = code;
  }

  public DAOException(String code, Throwable t) {
    super(getAllMessage(t));
    this.errCode = code;
    this.rootCause = t;
  }

  static private SQLException getInheritSQLException(Throwable t) {
    SQLException ex = null;

    while (t != null) {
      if (t instanceof SQLException) {
        ex = (SQLException) t;
        break;
      }
      else {
        t = t.getCause();
      }
    }

    return ex;
  }

  static private String getAllMessage(Throwable t) {
    String message = null;

    if (t != null) {
      if (! (t instanceof SQLException)) {
        message = t.getMessage();
      }
      SQLException ex = getInheritSQLException(t);
      if (ex != null) {
        if (message != null) {
          message += "\r\n" + ex.getMessage();
        }
        else {
          message = ex.getMessage();
        }
        String nextMessage = getAllMessage(ex.getNextException());
        if (nextMessage != null) {
          message += "\r\n" + nextMessage;
        }
      }
    }

    return message;
  }

  public DAOException(String code, String message, Throwable t) {
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