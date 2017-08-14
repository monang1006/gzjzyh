package com.strongit.di.util;

import com.strongit.di.exception.SystemException;
import java.util.zip.*;
import java.io.*;

/**
 * ZIP压缩器
 * <p>Title: Strong Data Interchange System</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007 Jiang Xi Strong Co. Ltd. </p>
 * <p>Company: </p>
 * @author minhongbin@hotmail.com
 * @version 1.0
 */
public class ZIPCompressor {

  public ZIPCompressor() {
  }

  /**
   * 压缩
   * @param input
   * @return
   */
  static public byte[] compress(byte[] input) throws SystemException {

    byte comp[] = null;

    try {
      Deflater compressor = new Deflater();
      compressor.setLevel(Deflater.BEST_COMPRESSION);
      compressor.setInput(input);
      compressor.finish();
      ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
      byte[] buf = new byte[1024];
      while (!compressor.finished()) {
        int count = compressor.deflate(buf);
        bos.write(buf, 0, count);
      }
      bos.close();
      comp = bos.toByteArray();
    }
    catch (IOException ex) {
      throw new SystemException("ZIPCompressor.compress.IOException", ex);
    }
    catch (Exception ex) {
      throw new SystemException("ZIPCompressor.compress.Exception", ex);
    }

    return comp;
  }

  /**
   * 解压
   * @param input
   * @return
   */
  static public byte[] decompress(byte[] input) throws SystemException {

    byte[] decomp = null;

    try {
      Inflater decompressor = new Inflater();
      decompressor.setInput(input);
      ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

      byte[] buf = new byte[1024];
      while (!decompressor.finished()) {
        int count = decompressor.inflate(buf);
        bos.write(buf, 0, count);
      }
      bos.close();
      decomp = bos.toByteArray();
    }
    catch (DataFormatException ex) {
      throw new SystemException(
          "ZIPCompressor.decompress.DataFormatException", ex);
    }
    catch (IOException ex) {
      throw new SystemException("ZIPCompressor.decompress.IOException", ex);
    }
    catch (Exception ex) {
      throw new SystemException("ZIPCompressor.decompress.Exception", ex);
    }

    return decomp;
  }

}