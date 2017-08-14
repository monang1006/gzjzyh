package com.strongit.di.util;

import java.net.InetAddress;
import java.io.Serializable;

/**
 * UUID生成器
 * <p>Title: Strong Data Interchange System</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007 Jiang Xi Strong Co. Ltd. </p>
 * <p>Company: </p>
 * @author minhongbin@hotmail.com
 * @version 1.0
 */
public class UUIDGenerator {

  private static final int IP;
  static {
    int ipadd;
    try {
      ipadd = toInt(InetAddress.getLocalHost().getAddress());
    }
    catch (Exception e) {
      ipadd = 0;
    }
    IP = ipadd;
  }

  private static short counter = (short) 0;
  private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

  public static int toInt(byte[] bytes) {
    int result = 0;
    for (int i = 0; i < 4; i++) {
      result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
    }
    return result;
  }

  public UUIDGenerator() {
  }

  /**
   * Unique across JVMs on this machine (unless they load this class
   * in the same quater second - very unlikely)
   */
  protected int getJVM() {
    return JVM;
  }

  /**
   * Unique in a millisecond for this JVM instance (unless there
   * are > Short.MAX_VALUE instances created in a millisecond)
   */
  protected short getCount() {
    synchronized (UUIDGenerator.class) {
      if (counter < 0) {
        counter = 0;
      }
      return counter++;
    }
  }

  /**
   * Unique in a local network
   */
  protected int getIP() {
    return IP;
  }

  /**
   * Unique down to millisecond
   */
  protected short getHiTime() {
    return (short) (System.currentTimeMillis() >>> 32);
  }

  protected int getLoTime() {
    return (int) System.currentTimeMillis();
  }

  private String sep = "";

  protected String format(int intval) {
    String formatted = Integer.toHexString(intval);
    StringBuffer buf = new StringBuffer("00000000");
    buf.replace(8 - formatted.length(), 8, formatted);
    return buf.toString();
  }

  protected String format(short shortval) {
    String formatted = Integer.toHexString(shortval);
    StringBuffer buf = new StringBuffer("0000");
    buf.replace(4 - formatted.length(), 4, formatted);
    return buf.toString();
  }

  public Serializable generate() {
    return new StringBuffer(36)
        .append(format(getIP())).append(sep)
        .append(format(getJVM())).append(sep)
        .append(format(getHiTime())).append(sep)
        .append(format(getLoTime())).append(sep)
        .append(format(getCount()))
        .toString();
  }

  public static void main(String[] args) throws Exception {
    UUIDGenerator gen = new UUIDGenerator();

    UUIDGenerator gen2 = new UUIDGenerator();

    for (int i = 0; i < 100; i++) {
      String id = (String) gen.generate();
      System.out.println(id + ": " + id.length());
      String id2 = (String) gen2.generate();
      System.out.println(id2 + ": " + id2.length());
    }
  }

}