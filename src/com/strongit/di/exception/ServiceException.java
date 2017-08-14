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

public class ServiceException
    extends BaseException {

  private Throwable rootCause;
  private String key;
  private boolean isMessage = true;

  public ServiceException() {
  }

  public ServiceException(String key) {
    this.key = key;
  }

  public ServiceException(String key, boolean isMessage) {
    this.key = key;
    this.isMessage = isMessage;
  }

  public ServiceException(String key, String message) {
    super(message);
    this.key = key;
  }

  public ServiceException(String key, String message, boolean isMessage) {
    super(message);
    this.key = key;
    this.isMessage = isMessage;
  }

  public ServiceException(String key, Throwable t) {
    this.key = key;
    this.rootCause = t;
    this.isMessage = false;
  }

  public ServiceException(String key, Throwable t, boolean isMessage) {
    this.key = key;
    this.rootCause = t;
    this.isMessage = isMessage;
  }

  public Throwable getRootCause() {
    return this.rootCause;
  }

  public String getKey() {
    return key;
  }

  public boolean isMessage() {
    return isMessage;
  }

}