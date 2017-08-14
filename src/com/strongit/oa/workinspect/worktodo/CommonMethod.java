package com.strongit.oa.workinspect.worktodo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * Copyright : Jiang Xi Strong Co. Ltd.<br>
 * All right reserved.<br>
 * Jul 30, 2009<br>
 * Author: dengyc<br>
 * Version: V1.0<br>
 * Description：CommonMethod.java
 */
public class CommonMethod {

	/**
	 * 在参数前后加百分号
	 * 
	 * @param value
	 * @return String
	 */
	public static String addPercentSign(String value) {
		return "%" + value + "%";
	}

	/**
	 * 在参数前加百分号
	 * 
	 * @param value
	 * @return String
	 */
	public static String addPrePercentSign(String value) {
		return "%" + value;
	}

	/**
	 * 在参数后加百分号
	 * 
	 * @param value
	 * @return String
	 */
	public static String addSufcentSign(String value) {
		return value + "%";
	}

	/**
	 * 分段构造in语句
	 * 
	 * @param inSql
	 *            需要分段的数据数组
	 * @param field
	 *            字段的名称 sql类似于id 而HQL类似于{id}
	 * @param increment
	 *            一个in里的数据大小，如果小于等于0或者大于等于1000则取默认值100
	 * @return String
	 */
	public static String getInSql(String[] inSql, String field, int increment) {
		String sql = "(";
		if (inSql == null || inSql.length == 0) {
			sql += "1=1";
		}
		else {
			if (increment <= 0 || increment >= 1000) {
				increment = 100;
			}
			int groupNum = inSql.length / increment;
			// 分组处理
			if (groupNum == 0) {
				// 不足一个increment
				// **id in(
				sql += field;
				sql += " in(";
				for (int i = 0; i < inSql.length; i++) {
					sql += inSql[i];
					sql += ",";
				}
				sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
				sql += ")";
				// )
			}
			else {
				// 至少有一个increment,分两种情况:一种是恰好被increment整除，另一种是不能被整除
				if (groupNum * increment == inSql.length) {
					// 能被整除
					for (int i = 0; i < groupNum; i++) {
						if (i == 0) {// 第一个increment
							// **id in(
							sql += field;
							sql += " in(";
							for (int j = 0; j < increment; j++) {
								sql += inSql[j];
								sql += ",";
							}
							sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
							sql += ")";
							// )
						}
						else {// 其它的increment
							// or **id in(
							sql += " or ";
							sql += field;
							sql += " in(";
							for (int j = 0; j < increment; j++) {
								sql += inSql[i * increment + j];
								sql += ",";
							}
							sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
							sql += ")";
							// )
						}
					}
				}
				else {
					// 不能被整除,那就要把最后那个increment找出来单独处理
					for (int i = 0; i <= groupNum; i++) {
						if (i == 0) {// 第一个increment
							// **id in(
							sql += field;
							sql += " in(";
							for (int j = 0; j < increment; j++) {
								sql += inSql[j];
								sql += ",";
							}
							sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
							sql += ")";
							// )
						}
						else {// 其它的increment，注意单独处理最后一个increment
							// or **id in(
							if (i == groupNum) {
								// 最后一个increment单独处理
								sql += " or ";
								sql += field;
								sql += " in(";
								for (int j = groupNum * increment; j < inSql.length; j++) {
									sql += inSql[j];
									sql += ",";
								}
								sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
								sql += ")";
							}
							else {
								sql += " or ";
								sql += field;
								sql += " in(";
								for (int j = 0; j < increment; j++) {
									sql += inSql[i * increment + j];
									sql += ",";
								}
								sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
								sql += ")";
							}
							// )
						}
					}
				}
			}
		}
		sql += ")";
		return sql;
	}

	/**
	 * 合同报表过滤部门对应物品权限
	 * <br>
	 * creator: 胡志文 <br>
	 * create date: 2011-1-12
	 * @param inSql
	 * @param field
	 * @param increment
	 * @return
	 */
	public static String getInSqlContract(String[] inSql, int increment) {
		String sql = "(";
		if (inSql == null || inSql.length == 0) {
			sql += "1=1";
		}
		else {
			if (increment <= 0 || increment >= 1000) {
				increment = 100;
			}
			int groupNum = inSql.length / increment;
			// 分组处理
			if (groupNum == 0) {
				// 不足一个increment
				// **id in(
				sql += "contractId in (select contract.contractId from ContractDetail where 1=1 and goods ";
				sql += " in(";
				for (int i = 0; i < inSql.length; i++) {
					sql += inSql[i];
					sql += ",";
				}
				sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
				sql += "))";
				// )
			}
			else {
				// 至少有一个increment,分两种情况:一种是恰好被increment整除，另一种是不能被整除
				if (groupNum * increment == inSql.length) {
					// 能被整除
					for (int i = 0; i < groupNum; i++) {
						if (i == 0) {// 第一个increment
							// **id in(
							sql += "contractId in (select contract.contractId from ContractDetail where 1=1 and goods ";
							sql += " in(";
							for (int j = 0; j < increment; j++) {
								sql += inSql[j];
								sql += ",";
							}
							sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
							sql += "))";
							// )
						}
						else {// 其它的increment
							// or **id in(
							sql += " or ";
							sql += "contractId in (select contract.contractId from ContractDetail where 1=1 and goods ";
							sql += " in(";
							for (int j = 0; j < increment; j++) {
								sql += inSql[i * increment + j];
								sql += ",";
							}
							sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
							sql += "))";
							// )
						}
					}
				}
				else {
					// 不能被整除,那就要把最后那个increment找出来单独处理
					for (int i = 0; i <= groupNum; i++) {
						if (i == 0) {// 第一个increment
							// **id in(
							sql += "contractId in (select contract.contractId from ContractDetail where 1=1 and goods ";
							sql += " in(";
							for (int j = 0; j < increment; j++) {
								sql += inSql[j];
								sql += ",";
							}
							sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
							sql += "))";
							// )
						}
						else {// 其它的increment，注意单独处理最后一个increment
							// or **id in(
							if (i == groupNum) {
								// 最后一个increment单独处理
								sql += " or ";
								sql += "contractId in (select contract.contractId from ContractDetail where 1=1 and goods ";
								sql += " in(";
								for (int j = groupNum * increment; j < inSql.length; j++) {
									sql += inSql[j];
									sql += ",";
								}
								sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
								sql += "))";
							}
							else {
								sql += " or ";
								sql += "contractId in (select contract.contractId from ContractDetail where 1=1 and goods ";
								sql += " in(";
								for (int j = 0; j < increment; j++) {
									sql += inSql[i * increment + j];
									sql += ",";
								}
								sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
								sql += "))";
							}
							// )
						}
					}
				}
			}
		}
		sql += ")";
		return sql;
	}

	/**
	 * 分段构造in语句,并把数据用单引号引起
	 * 
	 * @param inSql
	 *            需要分段的数据数组
	 * @param field
	 *            字段的名称 sql类似于id 而HQL类似于{id}
	 * @param increment
	 *            一个in里的数据大小，如果小于等于0或者大于等于1000则取默认值100
	 * @return String
	 */
	public static String getInSqlWithString(String[] inSql, String field,
			int increment) {
		String sql = "(";
		if (inSql == null || inSql.length == 0) {
			sql += "1=1";
		}
		else {
			if (increment <= 0 || increment >= 1000) {
				increment = 100;
			}
			int groupNum = inSql.length / increment;
			// 分组处理
			if (groupNum == 0) {
				// 不足一个increment
				// **id in(
				sql += field;
				sql += " in(";
				for (int i = 0; i < inSql.length; i++) {
					sql += "'";
					sql += inSql[i];
					sql += "'";
					sql += ",";
				}
				sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
				sql += ")";
				// )
			}
			else {
				// 至少有一个increment,分两种情况:一种是恰好被increment整除，另一种是不能被整除
				if (groupNum * increment == inSql.length) {
					// 能被整除
					for (int i = 0; i < groupNum; i++) {
						if (i == 0) {// 第一个increment
							// **id in(
							sql += field;
							sql += " in(";
							for (int j = 0; j < increment; j++) {
								sql += "'";
								sql += inSql[j];
								sql += "'";
								sql += ",";
							}
							sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
							sql += ")";
							// )
						}
						else {// 其它的increment
							// or **id in(
							sql += " or ";
							sql += field;
							sql += " in(";
							for (int j = 0; j < increment; j++) {
								sql += "'";
								sql += inSql[i * increment + j];
								sql += "'";
								sql += ",";
							}
							sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
							sql += ")";
							// )
						}
					}
				}
				else {
					// 不能被整除,那就要把最后那个increment找出来单独处理
					for (int i = 0; i <= groupNum; i++) {
						if (i == 0) {// 第一个increment
							// **id in(
							sql += field;
							sql += " in(";
							for (int j = 0; j < increment; j++) {
								sql += "'";
								sql += inSql[j];
								sql += "'";
								sql += ",";
							}
							sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
							sql += ")";
							// )
						}
						else {// 其它的increment，注意单独处理最后一个increment
							// or **id in(
							if (i == groupNum) {
								// 最后一个increment单独处理
								sql += " or ";
								sql += field;
								sql += " in(";
								for (int j = groupNum * increment; j < inSql.length; j++) {
									sql += "'";
									sql += inSql[j];
									sql += "'";
									sql += ",";
								}
								sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
								sql += ")";
							}
							else {
								sql += " or ";
								sql += field;
								sql += " in(";
								for (int j = 0; j < increment; j++) {
									sql += "'";
									sql += inSql[i * increment + j];
									sql += "'";
									sql += ",";
								}
								sql = sql.substring(0, sql.length() - 1);// 去掉最后一个逗号
								sql += ")";
							}
							// )
						}
					}
				}
			}
		}
		sql += ")";
		return sql;
	}

	/**
	 * 通用返回对象分页方法
	 * 
	 * @param page
	 * @param btList
	 * @param name
	 * @return
	 */
	public static Page returnObjectPage(Connection con, Page page, String sql) {
		Statement st = null;
		ResultSet rs = null;
		try {
			List btList = new ArrayList();
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			rs.last();
			int count = rs.getRow();
			rs.first();
			if (count != 0) {
				int n = (page.getPageNo() - 1) * page.getPageSize();// 计算已显示出的记录数
				int pn = n + page.getPageSize();// 新一页数据长度
				// 如果为最后一页
				if (count < pn) {
					pn = count;
				}
				rs.absolute(n + 1);
				Object[] obj;
				for (int i = n + 1; i < pn + 1; i++) {
					obj = new Object[columnCount];
					for (int k = 0; k < columnCount; k++) {
						obj[k] = rs.getObject(k + 1);
					}
					btList.add(obj);
					rs.next();
				}
			}
			/**
			 * int recordsPerPage = page.getPageSize();//获取每页记录条数 int forSize =
			 * count/recordsPerPage;//计算总页数
			 * 
			 * if(forSize==0){ page.setTotalCount(1); //throw new
			 * Exception("nothing"); }else{ int c = forSize*recordsPerPage; if(c<count){
			 * forSize = forSize + 1; }
			 * page.setTotalPage(forSize);//将算出的总页数设置到page中 }
			 */
			page.setTotalCount(count);// 设置总的记录长度
			page.setResult(btList);// 设置内容列表
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return page; // 返回分页对象
	}

	/**
	 * Description: 将Date格式的日期转换成字符串
	 * 
	 * @param date
	 *            要转换的日期对象
	 * @param format
	 *            要转换的日期格式，如"yyyy-MM-dd"
	 * @return String
	 */
	public static String formatDate(Date date, String format) {
		if (format == null || "".equals(format)) {
			format = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		if (date == null) {
			date = new Date();
		}
		String resultDate = sdf.format(date);

		return resultDate;
	}

	/**
	 * 保留double数字精度
	 * 
	 * @param douNum
	 * @param bitNum
	 *            精度
	 * @return 字符串
	 * @author capuchin
	 * @time 2010-4-27 14:47:16
	 */
	public static Double getFormatDouble(double douNum, int bitNum) {
		// double douNum = Double.parseDouble(strNum);
		String format = "0";
		if (bitNum <= 0) {
			format = "0";
		}
		else {
			format += ".";
			for (int i = 0; i < bitNum; i++) {
				format += "0";
			}
		}
		DecimalFormat f = new DecimalFormat(format);
		String tmp = f.format(douNum);
		return Double.valueOf(tmp);
	}

	// ***********************参数效验 Add By LeungJun********************
	/**
	 * 身份证校验位
	 */
	public static String[] CHECK_DIGIT = { "1", "0", "X", "9", "8", "7", "6",
			"5", "4", "3", "2" };
	/**
	 * 身份证加权因子
	 */
	public static int[] gene = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
			4, 2, 1 };

	/**
	 * 获得一个字符串的长度，一个汉字按2个长度算
	 * 
	 * @param str
	 * @return
	 */
	public static int getLengthAsLetter(String str) {
		int len = 0;
		int strLength = str.length();
		int code;
		for (int i = 0; i < strLength; i++) {
			code = str.charAt(i);
			if (code > 0 && code < 257) {
				len++;
			}
			else {
				len += 2;
			}
		}
		return len;
	}

	/**
	 * 判断是否为email
	 * 
	 * @param email
	 * @return 是email返回true; 不是email返回false
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.equals(""))
			return false;
		Pattern pattern = Pattern
				.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		Matcher isMail = pattern.matcher(email);
		return isMail.matches();

	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param email
	 * @return 是数字返回true; 不是数字返回false
	 */
	public static boolean isNum(String num) {
		if (num == null || num.equals(""))
			return false;
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(num);
		return isNum.matches();
	}

	/**
	 * 判断一个字符串是否为合法的字符
	 * 
	 * @param temp
	 * @return
	 */
	public static boolean isLegalityLetter(String temp) {
		if (temp == null)
			return false;
		if (temp.equals(""))
			return true;
		Pattern pattern = Pattern.compile("^\\w+$");
		Matcher isLegalityLetter = pattern.matcher(temp);
		return isLegalityLetter.matches();

	}

	/**
	 * 获得某个日期的月上减去monthCount个月
	 * 
	 * @param date
	 * @param monthCount
	 * @return
	 */
	public static Date parseByMonthCount(Date date, Integer monthCount) {
		if (date == null)
			return null;
		if (monthCount == null)
			return null;

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.MONTH, monthCount);
		return calendar.getTime();
	}

	/**
	 * 判断是否为用户名
	 * 
	 * @param lName
	 * @return
	 */
	public static boolean isLoginName(String lName) {
		if (lName == null || lName.equals(""))
			return false;
		Pattern pattern = Pattern.compile("^[\\w@\\.-]{8,26}$");
		Matcher isLoginName = pattern.matcher(lName);
		return isLoginName.matches();
	}

	/**
	 * 判断是否为手机号
	 */
	public static boolean isMobile(String mo) {
		if (mo == null || mo.equals(""))
			return false;
		Pattern pattern = Pattern.compile("^1[35]\\d{9}$");
		Matcher isMobile = pattern.matcher(mo);
		return isMobile.matches();
	}

	/**
	 * 判断是否为身份证号^(\d{15}|(\d{17}[xX\d]))$
	 */
	public static boolean isIdentityCard(String card) {
		// if(card==null||card.equals(""))return false;
		// if(card.length()!=15&&card.length()!=18)return false;
		// Pattern pattern=Pattern.compile("^(\\d{15}|(\\d{17}[xX\\d]))$");
		// Matcher isIdentityCard=pattern.matcher(card);
		// return isIdentityCard.matches();
		if (card == null || card.equals(""))
			return false;
		if (card.length() != 15 && card.length() != 18)
			return false;
		Pattern pattern = Pattern.compile("^(\\d{15}|(\\d{17}[xX\\d]))$");
		Matcher isIdentityCard = pattern.matcher(card);
		if (!isIdentityCard.matches())
			return false;
		if (card.length() == 18) {
			int yearPrefix = Integer.parseInt(card.substring(6, 8));
			if (yearPrefix < 19 || yearPrefix > 21)
				return false;// 出生日期必须大于1900年小于2100年
			int month = Integer.parseInt(card.substring(10, 12));
			if (month > 12 || month == 0)
				return false; // 验证月
			int day = Integer.parseInt(card.substring(12, 14));
			if (day > 31 || day == 0)
				return false; // 验证日
			String checkDigit = getCheckDigitFor18(card);
			if (checkDigit.equals("-1"))
				return false;
			if (checkDigit.equals(card.substring(17, 18).toUpperCase())) {
				return true;
			}
			else {
				return false;
			}
		}
		else if (card.length() == 15) {
			int month = Integer.parseInt(card.substring(8, 10));
			if (month > 12 || month == 0)
				return false; // 验证月
			int day = Integer.parseInt(card.substring(10, 12));
			if (day > 31 || day == 0)
				return false;
			return true;
		}
		return false;
	}

	private static String getCheckDigitFor18(String card) {
		if (card == null || card.equals(""))
			return "-1";
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			sum += Integer.parseInt(card.substring(i, i + 1)) * gene[i];
		}
		return CHECK_DIGIT[sum % 11];
	}

	/**
	 * 随机字符串生成
	 * 
	 * @return
	 */
	public static String getRandmStr(int length) {
		char[] tempCs = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
				'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's',
				'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b',
				'n', 'm' };
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(tempCs[Math.abs(random.nextInt()) % tempCs.length]);
		}
		return sb.toString();
	}

	// ***********************参数效验 Add By LeungJun********************

	/**
	 * 转换编码格式
	 * 
	 * @param param
	 *            入参
	 * @return
	 * @throws Exception
	 */
	public static String changeEncode(String param) throws Exception {
		if (param != null && !"".equals(param)) {
			return new String(param.getBytes("ISO-8859-1"), "utf-8");
		}
		else {
			return param;
		}
	}

	/**
	 * 将List<String>类型数据转换成String类型，并用逗号隔开
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static String ListChangtoString(List<String> list) throws Exception {
		StringBuffer codes = null;
		String code = null;
		if (list != null) {
			code = (String) list.get(0);
			codes = new StringBuffer(code);
			if (list.size() > 1) {
				for (int i = 1; i < list.size(); i++) {
					code = (String) list.get(i);
					codes.append(",");
					codes.append(code);
				}
			}
		}
		return codes.toString();
	}

	/**
	 * Description：从配置文件获取信息
	 * @param resourceName 配置文件名
	 * @return Properties
	 * @throws ServiceException
	 * @throws SystemException Properties
	 */
	public static Properties getProperties(String resourceName)
			throws ServiceException, SystemException {
		Properties prop = new Properties();
		InputStream in = CommonMethod.class.getResourceAsStream(resourceName);

		try {
			prop.load(in);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}

	/**
	 * Description：把字符串转换成带引号的形式，如"aa,bb"转换成"'aa','bb'"
	 * @param str 要转换的字符串
	 * @return String
	 */
	public static String changtoString(String str) {
		String result = null;
		StringBuffer buffer = new StringBuffer("");
		if (str != null && !"".equals(str)) {
			String[] strings = str.split(",");
			for (int i = 0; i < strings.length; i++) {
				buffer.append("'");
				buffer.append(strings[i]);
				buffer.append("'");
				buffer.append(",");
			}

			result = buffer.substring(0, buffer.length() - 1);
		}

		return result;
	}
	
}
