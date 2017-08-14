package com.strongit.di.util;

import com.strongit.di.exception.SystemException;
import java.net.*;
import java.util.*;

/**
 *
 * <p>Title: Strong Data Interchange System</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007 Jiang Xi Strong Co. Ltd.</p>
 * <p>Company: </p>
 * @author minhongbin@hotmail.com
 * @version 1.0
 */
public class Utils {

  public Utils() {
  }

  /**
   * 得到主机的IPs
   * @return
   * @throws CAException
   */
  public static String getHostIPs() throws SystemException {
    StringBuffer sb = new StringBuffer();

    try {
      InetAddress[] ia = InetAddress.getAllByName(InetAddress.getLocalHost().
                                                  getHostName());
      if (ia != null && ia.length > 0) {
        for (int i = 0; i < ia.length; i++) {
          if (i != 0) {
            sb.append(",");
          }
          sb.append(ia[i].getHostAddress());
        }
      }
    }
    catch (UnknownHostException ex) {
      throw new SystemException("Utils.getHostIPs.UnknownHostException", ex);
    }

    return sb.toString();
  }

  /**
   * 分割连接字符串
   * @param ljzfc - 连接字符串
   * @param key - Key
   * @param separator - 分割符号
   * @return
   */
  public static String split(String ljzfc, String key, String separator) {
    String str = null;

    if (ljzfc != null && key != null && ljzfc.length() > key.length()) {
      int ii = ljzfc.indexOf(key);
      if (ii >= 0) {
        int li = ljzfc.indexOf(separator, ii);
        if (li > 0) {
          str = ljzfc.substring(ii, li);
        }
        else {
          str = ljzfc.substring(ii);
        }
        str = str.substring(key.length());
      }
    }

    return str;
  }

  public static String replace(String str, String str1, String str2) {

    str2 = str2 == null ? "" : str2;
    if (str == null || str1 == null) {
      return str;
    }

    List lst = new ArrayList();
    int i = 0;
    do {
      i = str.indexOf(str1, i);
      if (i < 0) {
        break;
      }
      if (i > 0) {
        lst.add(str.substring(0, i));
      }
      lst.add(str2); ;
      str = str.substring(i + str1.length());
      i = 1;
    }
    while (true);
    lst.add(str);

    StringBuffer sb = new StringBuffer(512);
    for (int l = 0; l < lst.size(); l++) {
      sb.append( (String) lst.get(l));
    }

    return sb.toString();
  }

}