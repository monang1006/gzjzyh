package com.strongit.di.util;

/**
 * 字节Buffer
 * <p>Title: Strong Data Interchange System</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007 Jiang Xi Strong Co. Ltd. </p>
 * <p>Company: </p>
 * @author minhongbin@hotmail.com
 * @version 1.0
 */
public class ByteBuffer {

  private byte buff[];
  private int count;

  private void init() {
    this.buff = new byte[1024];
    this.count = 0;
  }

  private void expand(int size) {
    if (this.buff.length - this.count < size) {
      byte newbuff[] = new byte[Math.max(this.buff.length << 1,
                                         this.buff.length + size)];
      System.arraycopy(this.buff, 0, newbuff, 0, this.count);
      this.buff = newbuff;
    }
  }

  public ByteBuffer() {
    this.init();
  }

  public ByteBuffer(byte b) {
    this.init();
    this.append(b);
  }

  public ByteBuffer(byte buf[]) {
    this.init();
    this.append(buf);
  }

  public synchronized void append(byte b) {
    this.expand(1);
    this.buff[this.count] = b;
    this.count += 1;
  }

  public synchronized void append(byte buf[]) {
    if (buf != null && buf.length > 0) {
      this.expand(buf.length);
      System.arraycopy(buf, 0, this.buff, this.count, buf.length);
      this.count += buf.length;
    }
  }

  public synchronized void append(byte buf[], int bufOff, int bufLen) {
    if (buf == null) {
      return;
    }
    if ( (bufOff < 0) || (bufOff > buf.length) || (bufLen < 0) ||
        ( (bufOff + bufLen) > buf.length) || ( (bufOff + bufLen) < 0)) {
      throw new IndexOutOfBoundsException();
    }
    this.expand(bufLen);
    System.arraycopy(buf, bufOff, this.buff, this.count, bufLen);
    this.count += bufLen;
  }

  public synchronized byte read(int off) {
    if (off < 0 || off > this.count) {
      throw new IndexOutOfBoundsException();
    }
    return this.buff[off];
  }

  public synchronized byte[] read(int off, int len) {
    if (off < 0 || (off + len > this.count)) {
      throw new IndexOutOfBoundsException();
    }

    byte buf[] = new byte[len];
    System.arraycopy(this.buff, off, buf, 0, len);

    return buf;
  }

  public synchronized byte[] get() {
    byte buf[] = new byte[this.count];
    System.arraycopy(this.buff, 0, buf, 0, this.count);
    this.buff = buf;
    return this.buff;
  }

  public synchronized int count() {
    return this.count;
  }

  public synchronized void set(byte b, int off) {
    if (off < 0 || off > this.count) {
      throw new IndexOutOfBoundsException();
    }
    this.buff[off] = b;
  }

  public synchronized void set(byte[] buf, int off) {
    if (buf == null) {
      return;
    }
    if (off < 0 || off > this.count) {
      throw new IndexOutOfBoundsException();
    }
    System.arraycopy(buf, 0, this.buff, off, buf.length);
  }

  public synchronized void set(byte[] buf, int bufOff, int bufLen, int off) {
    if (buf == null) {
      return;
    }
    if ( (bufOff < 0) || (bufOff > buf.length) || (bufLen < 0) ||
        ( (bufOff + bufLen) > buf.length) || ( (bufOff + bufLen) < 0) ||
        (bufLen + off > this.count)) {
      throw new IndexOutOfBoundsException();
    }
    System.arraycopy(buf, bufOff, this.buff, off, bufLen);
  }

  /**
   * front cut
   * @param len
   */
  public synchronized void fcut(int len) {
    if (len > this.count) {
      return;
    }
    int newcount = this.count - len;
    if (newcount > 0) {
      byte newbuff[] = new byte[this.buff.length - len];
      System.arraycopy(this.buff, len, newbuff, 0, newcount);
      this.buff = newbuff;
      this.count = newcount;
    }
    else {
      this.clear();
    }
  }

  /**
   * back cut
   * @param len
   */
  public synchronized void bcut(int len) {
    if (len > this.count) {
      return;
    }
    int newcount = this.count - len;
    if (newcount > 0) {
      byte newbuff[] = new byte[this.buff.length - len];
      System.arraycopy(this.buff, 0, newbuff, 0, newcount);
      this.buff = newbuff;
      this.count = newcount;
    }
    else {
      this.clear();
    }
  }

  public synchronized void clear() {
    if (this.count > 0) {
      this.init();
    }
  }

}