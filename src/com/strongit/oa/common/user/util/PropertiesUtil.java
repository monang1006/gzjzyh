package com.strongit.oa.common.user.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
  public static String getCodeRule(String type) throws IOException {
    Properties properties = new Properties();
    InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream("codeRule.properties");
    try {
      properties.load(is);
    }
    catch (IOException e) {
      throw e;
    }
    String codeRule = properties.getProperty(type);
    if (codeRule == null || "".equals(codeRule)) {
      codeRule = "0";
    }
    return codeRule;
  }
}
