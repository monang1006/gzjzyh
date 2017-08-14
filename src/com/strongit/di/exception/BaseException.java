package com.strongit.di.exception;

/**
 *
 * <p>Title: Strong Data Interchange System</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007 Jiang Xi Strong Co. Ltd. </p>
 * <p>Company: </p>
 * @author minhongbin@hotmail.com
 * @version 1.0
 */
public class BaseException
    extends Exception {

  public BaseException() {
  }

  public BaseException(String message) {
    super(message);
  }

  public BaseException(Throwable t) {
    super(t);
  }

  public BaseException(String message, Throwable t) {
    super(message, t);
  }

}