package com.strongit.oa.autoencoder.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.common.user.IUserService;
import com.strongit.uums.bo.TUumsBaseOrg;

/**
 * <p>
 * Title: NumberAnalysis.java
 * </p>
 * <p>
 * Description: 编号XML解析类，通过解析出来的XML信息进行编号的生成
 * </p>
 * <p>
 * Strongit Ltd. (C) copyright 2009
 * </p>
 * <p>
 * Company: Strong
 * </p>
 * 
 * @author 于宏洲
 * @date Oct 12, 2010
 * @version 1.0
 */
public class NumberAnalysis {

	private final static Logger log = Logger.getLogger(NumberAnalysis.class);

	private String xmlStr;

	private Document numberRule;

	private boolean longYearDif = false;

	private boolean shorYearDif = false;

	private boolean monthDif = false;

	private boolean dayDif = false;

	/**
	 * 构造方法
	 * 
	 * @param xmlStr
	 */
	public NumberAnalysis(String xmlStr) {
		this.xmlStr = xmlStr;
		try {
			numberRule = DocumentHelper.parseText(xmlStr);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			numberRule = null;
		}
	}

	/**
	 * @author:于宏洲
	 * @des :获取所有的xml元素
	 * @param path
	 * @return
	 * @date :Oct 12, 2010
	 */
	private List<Element> getAllType(String path) {
		List<Element> list = numberRule.selectNodes(path);
		return list;
	}

	/**
	 * @author:于宏洲
	 * @des :根据元素类型进行对应的节点值处理
	 * @param el
	 * @param id
	 * @return
	 * @date :Oct 12, 2010
	 */
	@SuppressWarnings("unchecked")
	private String dealElement(Element el, String orgId, String[] myIds,
			Object[]... id) {
		String reVal = null;
		String type = el.getName();
		if ("String".equals(type)) { // 字符类型
			reVal = el.attributeValue("Value");
		} else if ("Variant".equals(type)) { // 变量类型
			String vType = el.attributeValue("Type");
			if ("Data LongYear".equals(vType)) {
				NumberTime numberTime = new NumberTime(NumberTime.longYear);
				reVal = numberTime.getNumberTime();
			} else if ("Data ShortYear".equals(vType)) {
				NumberTime numberTime = new NumberTime(NumberTime.shortYear);
				reVal = numberTime.getNumberTime();
			} else if ("Data Month".equals(vType)) {
				NumberTime numberTime = new NumberTime(NumberTime.month);
				reVal = numberTime.getNumberTime();
				if(reVal.length()==1){
					reVal = "0" + reVal;
				}
			} else if ("Data Day".equals(vType)) {
				NumberTime numberTime = new NumberTime(NumberTime.day);
				reVal = numberTime.getNumberTime();
				if(reVal.length()==1){
					reVal = "0" + reVal;
				}
			} else if ("Array".equals(vType)) {
				if (id != null && id.length != 0) {
					reVal = getArrayInfo(el, (String) id[0][0]);
				}
			} else if ("OrgName".equals(vType)) {
				reVal = getOrgNames(el, orgId).attributeValue("rname");

			}
		} else if ("Sequence".equals(type)) { // 序列号类型
			List<Element> list = numberRule.selectNodes("/AutoCode/Sequence");
			if (list.size() == 0) {
				log.error("当前XML文件中未找到SEQUENCE");
				return null;
			}
			Element seqEl = list.get(0);
			String start = seqEl.attributeValue("Start"); // 开始字符
			String end = seqEl.attributeValue("End"); // 结束数字
			String step = seqEl.attributeValue("Step"); // 步长
			String display = seqEl.attributeValue("Display"); // 数字展现类型
			String fill = seqEl.attributeValue("Fill"); // 是否用其他字符填充0：不进行填充、1：进行填充
			String fillChar = seqEl.attributeValue("FillChar"); // 填充的字符串
			String nowValue = seqEl.attributeValue("NowValue"); // 当前值
			String year = seqEl.attributeValue("year"); // 最后一个问号所在年份
			if (!isNumberic(start) || !isNumberic(end) || !isNumberic(step)
					|| !isNumberic(nowValue)) {
				reVal = "@错误:numberic@";
			} else {
				long startNum = Long.parseLong(start);
				long endNum = Long.parseLong(end);
				long stepNum = Long.parseLong(step);
				long nowNum = Long.parseLong(nowValue);
				if (chargeHasArray()) { // 如果存在变量组合
					List<Element> tempList = numberRule
							.selectNodes("/AutoCode/Variant[@Type='Array']");
					Element var = tempList.get(0);
					if (var == null) { // 当前的变量组合不存在
						reVal = "@错误:noarray@";
					} else {
						String numDiffrent = var.attributeValue("NumDiffrent");
						if ("false".equals(numDiffrent)) {
							String nowYear = new NumberTime(NumberTime.longYear)
									.getNumberTime();
							if (this.longYearDif || this.shorYearDif) {
								if (nowYear.equals(year)) { // 当前年份不变
									if (nowNum + stepNum > endNum) { // 当前值超过最大值
										reVal = "@错误:outOf@";
									} else {
										reVal = String
												.valueOf(nowNum + stepNum);
									}
								} else {
									reVal = start;
								}
							} else {
								if (nowYear.equals(year)) { // 当前年份不变
									if (nowNum + stepNum > endNum) { // 当前值超过最大值
										reVal = "@错误:outOf@";
									} else {
										reVal = String
												.valueOf(nowNum + stepNum);
									}
								} else {
									reVal = start;
								}
							}
						} else {
							Element nowEl = getArrayParent(var, myIds[0]); // 获得当前的变量数组中的选项
							String mstart = nowEl.attributeValue("mstart"); // 变量中设置的开始字符
							String mend = nowEl.attributeValue("mend"); // 变量中设置的结束字符
							String mstep = nowEl.attributeValue("mstep"); // 变量中设置的步阶字符
							String mNowValue = nowEl
									.attributeValue("mNowValue"); // 当前变量值
							String mNowYear = nowEl.attributeValue("nowyear"); // 最后一个文号生成所在年份
							if (!isNumberic(mstart) || !isNumberic(mend)
									|| !isNumberic(mstep)
									|| !isNumberic(mNowValue)) {
								reVal = "@错误:变量缺少开始值步长值结束值或者当前值@";
							} else {
								String nowYear = new NumberTime(
										NumberTime.longYear).getNumberTime();
								long mstartNum = Long.parseLong(mstart);
								long mendNum = Long.parseLong(mend);
								long mstepNum = Long.parseLong(mstep);
								long mnowNum = Long.parseLong(mNowValue);
								if (this.longYearDif || this.shorYearDif) {
									if (nowYear.equals(mNowYear)) {
										if (mnowNum + mstepNum > mendNum) {
											reVal = "@错误:outOf@";
										} else {
											reVal = String.valueOf(mnowNum
													+ mstepNum);
										}
									} else {
										reVal = mstart;
									}
								} else {
									if (mnowNum + mstepNum > mendNum) {
										reVal = "@错误:outOf@";
									} else {
										reVal = String.valueOf(mnowNum
												+ mstepNum);
									}
								}
							}
						}
					}
				} else if (chargeHasOrgName()) {// 如果存在部门规则
					List<Element> orgList = numberRule
							.selectNodes("/AutoCode/Variant[@Type='OrgName']");
					Element var = orgList.get(0);
					if (var == null) { // 不存在当前用户的部门规则
						reVal = "@错误:该部门无文号规则@";
					} else {
						String numDiffrent = var.attributeValue("NumDiffrent");
						if ("false".equals(numDiffrent)) {
							String nowYear = new NumberTime(NumberTime.longYear)
									.getNumberTime();
							if (this.longYearDif || this.shorYearDif) {
								if (nowYear.equals(year)) { // 当前年份不变
									if (nowNum + stepNum > endNum) { // 当前值超过最大值
										reVal = "@错误:outOf@";
									} else {
										reVal = String
												.valueOf(nowNum + stepNum);
									}
								} else {
									reVal = start;
								}
							} else {
								if (nowYear.equals(year)) { // 当前年份不变
									if (nowNum + stepNum > endNum) { // 当前值超过最大值
										reVal = "@错误:outOf@";
									} else {
										reVal = String
												.valueOf(nowNum + stepNum);
									}
								} else {
									reVal = start;
								}
							}
						} else {
							Element orgItem = getOrgNames(var, orgId);
							String rstart = orgItem.attributeValue("rstart"); // 变量中设置的开始字符
							String rend = orgItem.attributeValue("rend"); // 变量中设置的结束字符
							String rstep = orgItem.attributeValue("rstep"); // 变量中设置的步阶字符
							String rnow = orgItem.attributeValue("rnow"); // 当前变量值
							String rNowYear = orgItem.attributeValue("nowYear"); // 最后一个文号生成所在年份
							if (!isNumberic(rstart) || !isNumberic(rend)
									|| !isNumberic(rstep) || !isNumberic(rnow)) {
								reVal = "@错误:变量缺少开始值,步长值,结束值,或者当前值@";
							} else {
								String nowYear = new NumberTime(
										NumberTime.longYear).getNumberTime();
								long rstartNum = Long.parseLong(rstart);
								long rendNum = Long.parseLong(rend);
								long rstepNum = Long.parseLong(rstep);
								long rnowNum = Long.parseLong(rnow);
								if (this.longYearDif || this.shorYearDif) {
									if (nowYear.equals(rNowYear)) {
										if (rnowNum + rstepNum > rendNum) {
											reVal = "@错误:outOf@";
										} else {
											reVal = String.valueOf(rnowNum
													+ rstepNum);
										}
									} else {
										reVal = rstart;
									}
								} else {
									if (rnowNum + rstepNum > rendNum) {
										reVal = "@错误:outOf@";
									} else {
										reVal = String.valueOf(rnowNum
												+ rstepNum);
									}
								}
							}
						}
					}
				}

				else {
					if (nowNum + stepNum > endNum) { // 当前值超过最大值
						reVal = "@错误:编号超过结束值@";
					} else {
						reVal = String.valueOf(nowNum + stepNum);
					}
				}
				if (reVal == null || reVal.indexOf("@错误") == 0) {

				} else {

					// 进行转码工作将编号进行对应的转换
					if ("中文数字".equals(display)) {
						reVal = new NumberUtil(NumberUtil.chinese)
								.getNumberByType(reVal);
					} else if ("大写中文数字".equals(display)) {
						reVal = new NumberUtil(NumberUtil.bigNumber)
								.getNumberByType(reVal);
					}

					// 进行字符填补工作
					if ("1".equals(fill)) {
						int nowSize = reVal.length();
						if (fillChar == null || "".equals(fillChar)) {

						} else {
							for (int i = 0; i < end.length() - nowSize; i++) {
								reVal = fillChar + reVal;
							}
						}
					}
				}
			}
		}
		return reVal;
	}

	private boolean isNumberic(String numStr) {
		try {
			Long.parseLong(numStr);
			return true;
		} catch (Exception e) {
			log.error("number:" + numStr + e);
			return false;
		}
	}

	/**
	 * @author:于宏洲
	 * @des :获取指定ID的内容的值信息
	 * @param el
	 * @param id
	 * @return
	 * @date :Oct 12, 2010
	 */
	@SuppressWarnings("unchecked")
	private String getArrayInfo(Element el, String id) {
		// List<Element> list=el.selectNodes("ArrayItem");
		Document doc;
		try {
			doc = DocumentHelper.parseText(el.asXML());
			// System.out.println(el.asXML());
			List<Element> list = doc.selectNodes("/Variant/ArrayItem[@Id=" + id
					+ "]");
			if (list == null || list.size() == 0) {
				return null;
			} else {
				return list.get(0).attributeValue("Value");
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			log.error(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Element getArrayParent(Element el, String id) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(el.asXML());
			List<Element> list = doc.selectNodes("/Variant/ArrayItem[@Id=" + id
					+ "]");
			if (list == null || list.size() == 0) {
				return null;
			} else {
				return list.get(0);
			}
		} catch (DocumentException e) {
			log.error(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Element getOrgNames(Element el, String orgId) {

		Document doc;
		try {
			doc = DocumentHelper.parseText(el.asXML());
			List<Element> list = doc.selectNodes("/Variant/orgItem");
			if (list.size() > 0 && list != null) {
				for (int i = 0; i < list.size(); i++) {
					Element e = list.get(i);
					if (e.attributeValue("orgId").equals(orgId)) {
						return e;
					}
				}
			}
		} catch (DocumentException e) {
			log.error(e);
		}
		return null;
	}

	/**
	 * @author:于宏洲
	 * @des :根据用户传入的ID获取编号信息
	 * @param ids
	 * @return
	 * @date :Oct 12, 2010
	 */
	public String getMyNumber(String orgId, String[]... ids) {
		String serNumber = "";
		List<Element> list = numberRule.selectNodes("/AutoCode/*");
		List<Element> tempList = numberRule
				.selectNodes("/AutoCode/Variant[@Type='Array']");
		List<Element> orgList = numberRule
				.selectNodes("/AutoCode/Variant[@Type='OrgName']");
		System.out.println(tempList.size());
		List<Element> chooseList = numberRule
				.selectNodes("/AutoCode/Variant[(@Type='Data LongYear') or (@Type='Data ShortYear') or (@Type='Data Month') or (@Type='Data Day')]");
		int nowPos = 0;
		Object[][] obj = null;
		if (ids.length != 0) {
			if (tempList.size() != ids[0].length) {
				if (ids.length == 0)
					return "@error:arrayerror@";
			} else {
				// if(ids.length!=0){
				obj = new Object[ids[0].length][2];
				for (int i = 0; i < ids[0].length; i++) {
					obj[i][0] = ids[0][i];
					obj[i][1] = tempList.get(i);
					System.out.println(((Element) obj[i][1]).asXML());
				}
			}
		}

		for (int i = 0; i < chooseList.size(); i++) {
			Element el = chooseList.get(i);
			String type = el.attributeValue("Type");
			if ("true".equals(el.attributeValue("NumDiffrent"))) {
				if ("Data LongYear".equals(type)) {
					longYearDif = true;
				}
				if ("Data ShortYear".equals(type)) {
					shorYearDif = true;
				}
				if ("Data Month".equals(type)) {
					monthDif = true;
				}
				if ("Data Day".equals(type)) {
					dayDif = true;
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			Element el = list.get(i);
			if (ids == null || ids.length == 0) {
				serNumber = serNumber + dealElement(el, orgId, null);
			} else {
				serNumber = serNumber
						+ dealElement(el, orgId, ids[0], obj[nowPos]);
			}
			if ("Variant".equals(el.getName())) {
				if ("Array".equals(el.attributeValue("Type"))) {
					if (nowPos < tempList.size() - 1) {
						nowPos++;
					}
				} else if ("OrgName".equals(el.attributeValue("OrgName"))) {
					if (nowPos < tempList.size() - 1) {
						nowPos++;
					}
				}
			}

		}
		return serNumber;
	}

	@SuppressWarnings( { "unchecked" })
	public String updateXmlByInfo(String orgId, String... ids) {
		
		//判断Sequence中的NowValue值是否已经改变了，原则上生成一次编码最多只能更新一次Sequence中的NowValue值       
		//shenyl 20120706
		String flag = "false";
		String charge = "";
		// 更新变量:固定格式
		List<Element> chargeList = numberRule
				.selectNodes("/AutoCode/Variant[@Type='Array']");				
		if (chargeList.size() > 0 && flag.equals("false")) {
			charge = chargeList.get(0).attributeValue("NumDiffrent");
			if (ids.length == 0 || "false".equals(charge)) {
				List<Element> seqList = numberRule
						.selectNodes("/AutoCode/Sequence");
				if (seqList.size() == 0) {

				} else {
					String start = seqList.get(0).attributeValue("Start");
					String end = seqList.get(0).attributeValue("End");
					String step = seqList.get(0).attributeValue("Step");
					String nowValue = seqList.get(0).attributeValue("NowValue");
					String year = seqList.get(0).attributeValue("year");
					if (!isNumberic(start) || !isNumberic(end)
							|| !isNumberic(step) || !isNumberic(nowValue)) {
						return null;
					} else {
						String nowYear = new NumberTime(NumberTime.longYear)
								.getNumberTime();
						if (nowYear.equals(year)) {
							if (Long.parseLong(nowValue) + Long.parseLong(step) <= Long
									.parseLong(end)) {
								seqList.get(0).addAttribute(
										"NowValue",
										String.valueOf(Long.parseLong(nowValue)
												+ Long.parseLong(step)));
								seqList.get(0).addAttribute(
										"year",
										new NumberTime(NumberTime.longYear)
												.getNumberTime());
								flag = "true";
							} else {
								//return null;
							}
						} else {
							seqList.get(0).addAttribute("NowValue",
									String.valueOf(Long.parseLong(start)));
							seqList.get(0).addAttribute(
									"year",
									new NumberTime(NumberTime.longYear)
											.getNumberTime());
							flag = "true";
						}
					}
				}
			} else {
				List<Element> arrayList = numberRule
						.selectNodes("/AutoCode/Variant/ArrayItem[@Id="
								+ ids[0] + "]");
				if (arrayList.size() == 0) {
					return null;
				} else {
					String mstart = arrayList.get(0).attributeValue("mstart");
					String mend = arrayList.get(0).attributeValue("mend");
					String mstep = arrayList.get(0).attributeValue("mstep");
					String mNowValue = arrayList.get(0).attributeValue(
							"mNowValue");
					String nowyear = arrayList.get(0).attributeValue("nowyear");
					if (!isNumberic(mstart) || !isNumberic(mend)
							|| !isNumberic(mstep) || !isNumberic(mNowValue)) {
						return null;
					} else {
						String nowYear = new NumberTime(NumberTime.longYear)
								.getNumberTime();
						if (nowYear.equals(nowyear)) {
							if (Long.parseLong(mNowValue)
									+ Long.parseLong(mstep) <= Long
									.parseLong(mend)) {
								arrayList.get(0).addAttribute(
										"mNowValue",
										String.valueOf(Long
												.parseLong(mNowValue)
												+ Long.parseLong(mstep)));
								arrayList.get(0).addAttribute(
										"nowyear",
										new NumberTime(NumberTime.longYear)
												.getNumberTime());
								flag = "true";
							} else {
								return null;
							}
						} else {
							arrayList.get(0).addAttribute("mNowValue",
									String.valueOf(Long.parseLong(mstart)));
							arrayList.get(0).addAttribute(
									"nowyear",
									new NumberTime(NumberTime.longYear)
											.getNumberTime());
							 flag = "true";
						}
					}

				}
			}
		}

		// 更新变量 :部门文号
		List<Element> orgList = numberRule
				.selectNodes("/AutoCode/Variant[@Type='OrgName']");
		//Element cha = null;
		if (orgList.size() > 0 && flag.equals("false")) {
			//cha = orgList.get(0);
			charge = orgList.get(0).attributeValue("NumDiffrent");
			if ("false".equals(charge)) {
				List<Element> seqList = numberRule
						.selectNodes("/AutoCode/Sequence");
				if (seqList.size() == 0) {
					return null;
				} else {
					String start = seqList.get(0).attributeValue("Start");
					String end = seqList.get(0).attributeValue("End");
					String step = seqList.get(0).attributeValue("Step");
					String nowValue = seqList.get(0).attributeValue("NowValue");
					String year = seqList.get(0).attributeValue("year");
					if (!isNumberic(start) || !isNumberic(end)
							|| !isNumberic(step) || !isNumberic(nowValue)) {
						return null;
					} else {
						String nowYear = new NumberTime(NumberTime.longYear)
								.getNumberTime();
						if (nowYear.equals(year)) {
							if (Long.parseLong(nowValue) + Long.parseLong(step) <= Long
									.parseLong(end)) {
								seqList.get(0).addAttribute(
										"NowValue",
										String.valueOf(Long.parseLong(nowValue)
												+ Long.parseLong(step)));
								seqList.get(0).addAttribute(
										"year",
										new NumberTime(NumberTime.longYear)
												.getNumberTime());
								flag = "true";
							} else {
								//return null;
							}
						} else {
							seqList.get(0).addAttribute("NowValue",
									String.valueOf(Long.parseLong(start)));
							seqList.get(0).addAttribute(
									"year",
									new NumberTime(NumberTime.longYear)
											.getNumberTime());
							flag = "true";
						}
					}
				}
			} else {
				List<Element> list = numberRule
						.selectNodes("/AutoCode/Variant/orgItem");
				if (list.size() > 0 && list != null) {
					for (int i = 0; i < list.size(); i++) {
						Element orgItem = list.get(i);
						if (orgItem.attributeValue("orgId").equals(orgId)) {
							String rstart = orgItem.attributeValue("rstart"); // 变量中设置的开始字符
							String rend = orgItem.attributeValue("rend"); // 变量中设置的结束字符
							String rstep = orgItem.attributeValue("rstep"); // 变量中设置的步阶字符
							String rnow = orgItem.attributeValue("rnow"); // 当前变量值
							String rNowYear = orgItem.attributeValue("nowYear"); // 最后一个文号生成所在年份

							if (!isNumberic(rstart) || !isNumberic(rend)
									|| !isNumberic(rstep) || !isNumberic(rnow)) {
								return null;
							} else {
								String nowYear = new NumberTime(
										NumberTime.longYear).getNumberTime();
								if (nowYear.equals(rNowYear)) {
									if (Long.parseLong(rnow)
											+ Long.parseLong(rstep) <= Long
											.parseLong(rend)) {
										orgItem.addAttribute("rnow",String.valueOf(Long.parseLong(rnow)
																		+ Long.parseLong(rstep)));
										flag = "true";
									} else {
										return null;
									}
								} else {
									orgItem.addAttribute("rnow", String
											.valueOf(Long.parseLong(rstart)));
									orgItem.addAttribute("nowYear",
											new NumberTime(NumberTime.longYear).getNumberTime());
									flag = "true";
								}
							}

						}
					}

				}

			}
		}
		//变量：当前时间
		List<Element> chooseList = numberRule.selectNodes("/AutoCode/Variant[(@Type='Data LongYear') or " +
				"(@Type='Data ShortYear') or (@Type='Data Month') or (@Type='Data Day')]");
		if (orgList.size() > 0 && flag.equals("false")) {
			charge = chooseList.get(0).attributeValue("NumDiffrent");
			if ("false".equals(charge)&& flag.equals("false")) {
				List<Element> seqList = numberRule.selectNodes("/AutoCode/Sequence");
				if (seqList.size() == 0) {
					return null;
				} else {
					String start = seqList.get(0).attributeValue("Start");
					String end = seqList.get(0).attributeValue("End");
					String step = seqList.get(0).attributeValue("Step");
					String nowValue = seqList.get(0).attributeValue("NowValue");
					String year = seqList.get(0).attributeValue("year");
					if (!isNumberic(start) || !isNumberic(end)
							|| !isNumberic(step) || !isNumberic(nowValue)) {
						return null;
					} else {
						String nowYear = new NumberTime(NumberTime.longYear)
								.getNumberTime();
						if (nowYear.equals(year)) {
							if (Long.parseLong(nowValue) + Long.parseLong(step) <= Long
									.parseLong(end)) {
								seqList.get(0).addAttribute(
										"NowValue",
										String.valueOf(Long.parseLong(nowValue)
												+ Long.parseLong(step)));
								seqList.get(0).addAttribute(
										"year",
										new NumberTime(NumberTime.longYear)
												.getNumberTime());
								flag = "true";
							} else {
								//return null;
							}
						} else {
							seqList.get(0).addAttribute("NowValue",
									String.valueOf(Long.parseLong(start)));
							seqList.get(0).addAttribute(
									"year",
									new NumberTime(NumberTime.longYear)
											.getNumberTime());
							flag = "true";
						}
					}
				}
			}
		}
		
		//不存在变量
		if (flag.equals("false")) {
			List<Element> seqList = numberRule.selectNodes("/AutoCode/Sequence");
			if (seqList.size() == 0) {
				return null;
			} else {
				String start = seqList.get(0).attributeValue("Start");
				String end = seqList.get(0).attributeValue("End");
				String step = seqList.get(0).attributeValue("Step");
				String nowValue = seqList.get(0).attributeValue("NowValue");
				String year = seqList.get(0).attributeValue("year");
				if (!isNumberic(start) || !isNumberic(end)
						|| !isNumberic(step) || !isNumberic(nowValue)) {
					return null;
				} else {
					String nowYear = new NumberTime(NumberTime.longYear)
							.getNumberTime();
					if (nowYear.equals(year)) {
						if (Long.parseLong(nowValue) + Long.parseLong(step) <= Long
								.parseLong(end)) {
							seqList.get(0).addAttribute(
									"NowValue",
									String.valueOf(Long.parseLong(nowValue)
											+ Long.parseLong(step)));
							seqList.get(0).addAttribute(
									"year",
									new NumberTime(NumberTime.longYear)
											.getNumberTime());
							flag = "true";
						} else {
							//return null;
						}
					} else {
						seqList.get(0).addAttribute("NowValue",
								String.valueOf(Long.parseLong(start)));
						seqList.get(0).addAttribute(
								"year",
								new NumberTime(NumberTime.longYear)
										.getNumberTime());
						flag = "true";
					}
				}
			}
		}
		
		System.out.println(dealXmlInfo(numberRule.asXML()));
		return dealXmlInfo(numberRule.asXML());
	}

	private String dealXmlInfo(String xml) {
		if (xml.indexOf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>") == -1) {
			return null;
		} else {
			return xml.substring("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					.length(), xml.length());
		}
	}

	private boolean chargeHasArray() {
		boolean var = false;
		List<Element> list = numberRule
				.selectNodes("/AutoCode/Variant/ArrayItem");
		if (list.size() != 0) {
			var = true;
		}
		return var;
	}

	private boolean chargeHasOrgName() {
		boolean var = false;
		List<Element> list = numberRule
				.selectNodes("/AutoCode/Variant/orgItem");
		if (list.size() != 0) {
			var = true;
		}
		return var;

	}

	public static void main(String args[]) {
		String xml = "<AutoCode><Sequence Start=\"100000000000\" End=\"1\" Step=\"1\" Display=\"数字\" Fill=\"0\" FillChar=\"1\" NowValue=\"0\" year=\"2010\"/><String Value=\"s\"/></AutoCode>";
		NumberAnalysis analysis = new NumberAnalysis(xml);
		String[] ids = new String[1];
		ids[0] = "1";
		// System.out.println(analysis.getMyNumber(orgId));
	}

}
