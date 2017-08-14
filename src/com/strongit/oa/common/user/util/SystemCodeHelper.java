package com.strongit.oa.common.user.util;

/**
 * @author       喻斌
 * @company      Strongit Ltd. (C) copyright
 * @date         Jan 6, 2009  7:06:00 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.uums.util.SystemCodeHelper
 * @comment		 系统编码辅助类
 */
public class SystemCodeHelper {
  /**
   * 根据系统编码、编码规则和默认上级编码得到上级编码
   * @author  喻斌
   * @date    Jan 6, 2009  7:23:36 PM
   * @param code
   * @param ruleCode
   * @param defaultParentCode：默认上级编码，当code没有上级编码时返回的默认上级编码，若不设置则系统默认为“0”
   * @return
   */
  public static String getFatherCode(String code, String ruleCode, String defaultParentCode) {
		int length1 = 0;
		int length2 = 0;
		int codeLength = code.length();
		String fatherCode;
		if(defaultParentCode != null && !"".equals(defaultParentCode)){
			fatherCode = new String(defaultParentCode);
		}else{
			fatherCode = new String("0");
		}
		for (int i = 0; i < ruleCode.length(); i++) {
			length1 = length1 + Integer.parseInt(ruleCode.substring(i, i+1));
			if (i > 0) {
				length2 = length2 + Integer.parseInt(ruleCode.substring(i-1, i));
			}
			if (codeLength == length1) {
				if (length2 != 0) {
					fatherCode = code.substring(0, length2);
					break;
				}
			}
		}
		return fatherCode;
  }
}
