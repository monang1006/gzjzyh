/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-7-10
 * Autour: hull
 * Version: V1.0
 * Description： 文号生成类
 */
package com.strongit.oa.serialnumber.number;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSerialnumberRegulation;

public class GenerateNum {

	/** 文号*/
	private String numberNo;

	/** 文号manager*/
	private NumberManager numberManager;
	
	private static final String ONE="1";
	private static final String TWO="2";
	private static final String THREE="3";
	private static final String FOUR="4";

	/**
	 * 获取年份段落
	 * 
	 * @param value
	 * @return
	 */
	private String getYear(String value) {
		String year = "";
		if (value == ONE || ONE.equals(value)) {// 是否是当年
			year = "〔" + getYearTime() + "〕";
		} else if (value == TWO || TWO.equals(value)) {// 是否是去年
			year = "〔" + (Integer.parseInt(getYearTime()) - 1) + "〕";
		} else {
			year = "〔" + (Integer.parseInt(getYearTime()) - 2) + "〕";
		}
		return year;
	}

	/**
	 * 获取数字文号
	 * 
	 * @param num
	 * @return
	 */
	public String getNum(int num,String type) {
		String temp = String.valueOf(num);
		temp = "" + temp;
		if(THREE.equals(type)){
			temp=temp+"号";
		}
		return temp;
	}

	/**
	 * 获取字母文号
	 * 
	 * @return
	 */
	private String getChars(String type) {
		char[] letters = new char[26];
		char first = 'A';
		for (int i = 0; i < letters.length; i++) {
			letters[i] = first++;
		}
		Random subr = new Random();

		char[] str = new char[4];
		for (int i = 0; i < 4; i++) {
			char randomChar = letters[subr.nextInt(27)];
			str[i] = randomChar;
		}
		if(FOUR.equals(type)){
			return String.valueOf(str)+"号";
		}else{
			return String.valueOf(str);
		}
		
	}

	/**
	 * 产生一个两段组成的文号
	 * 
	 * @param reg
	 */
	public String generateTwo(ToaSerialnumberRegulation reg,
			NumberManager manager, String division,String orgid,String dictcode) {
		List<String> list = getSplit(reg.getRegulationRule());
		numberManager = manager;
		String temp = "";//
		String year = "";
		year = getYear(list.get(0));// 获取年份
		int num = numberManager.getCount(orgid,"","",year);// 获取序列号个数
		if (list.get(1) == ONE || ONE.equals(list.get(1))||THREE.equals(list.get(1))) {
			while (true) {// 生成唯一的序列号
				num++;
				temp = getNum(num,list.get(1));// 获取数字
				numberNo = year + division + temp;
				int sum = numberManager.getCountByNo(numberNo,orgid);
				if (sum == 0) {
					break;
				}
			}
		} else if(list.get(1)==TWO||TWO.equals(list.get(1))){
			while (true) {
				temp = getChars(list.get(1));
				numberNo = year + division + temp;
				int sum = numberManager.getCountByNo(numberNo,orgid);
				if (sum == 0) {
					break;
				}
			}
		}
		return numberNo;
	}

	/**
	 * 产生一个有三段组成的文号
	 * 
	 * @param reg
	 * @param manager
	 * @param abbreviation
	 * @return
	 */
	public String generateThree(ToaSerialnumberRegulation reg,
			NumberManager manager, String abbreviation, String division,String orgid,String dictcode) {
		List<String> list = getSplit(reg.getRegulationRule());
		numberManager = manager;
		String temp = "";//
		String year = "";
		year = getYear(list.get(1));// 获取年份
		int num =numberManager.getCount(orgid,abbreviation,dictcode,getYearTime());// 获取序列号个数
		if (abbreviation == null || "".equals(abbreviation)) {// 公文代字是否为空
			abbreviation = list.get(0);
		}
		if (list.get(2) == ONE || ONE.equals(list.get(2))||THREE.equals(list.get(2))) {// 是否是数字递增
			while (true) {// 生成唯一的序列号
				num++;
				temp = getNum(num,list.get(2));// 获取数字
				numberNo = abbreviation + division + year + division + temp;
				int sum = numberManager.getCountByNo(numberNo,orgid);
				if (sum == 0) {//没有找到重复的文号
					break;
				}
			}
		} else {
			while (true) {
				temp = getChars(list.get(2));
				numberNo = abbreviation + division + year + division + temp;
				int sum = numberManager.getCountByNo(numberNo,orgid);
				if (sum == 0) {//没有找到重复的文号
					break;
				}
			}
		}
		return numberNo;
	}

	/**
	 * 产生一个有四段组成的文号
	 * 
	 * @param reg
	 * @param manager
	 * @param abbreviation
	 * @return
	 */
	public String generateFour(ToaSerialnumberRegulation reg,
			NumberManager manager, String abbreviation, String division,String orgid,String dictcode) {
		List<String> list = getSplit(reg.getRegulationRule());
		numberManager = manager;
		String temp = "";//
		String year = "";
		year = getYear(list.get(2));// 获取年份
		int num =  numberManager.getCount(orgid,abbreviation,dictcode,getYearTime());// 获取序列号个数
		if (abbreviation == null || "".equals(abbreviation)) {// 公文代字是否为空
			abbreviation = list.get(0);
		}
		if (list.get(3) == ONE || ONE.equals(list.get(3))||THREE.equals(list.get(3))) {// 判断是否数字递增
			while (true) {// 生成唯一的序列号
				num++;
				temp = getNum(num,list.get(3));// 获取数字
				numberNo = abbreviation + division + list.get(1) + division
						+ year + division + temp;
				int sum = numberManager.getCountByNo(numberNo,orgid);
				if (sum == 0) {
					break;
				}
			}
		} else {
			while (true) {// 生成唯一的序列号
				temp = getChars(list.get(3));// 获取字母
				numberNo = abbreviation + division + list.get(1) + division
						+ year + division + temp;
				int sum = numberManager.getCountByNo(numberNo,orgid);
				if (sum == 0) {
					break;
				}
			}
		}
		return numberNo;
	}

	/**
	 * 获取当年年份
	 * 
	 * @return
	 */
	public String getYearTime() {
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy");

		return format.format(time);
	}

	/**
	 * 拆分字符串
	 * 
	 * @param temp
	 * @return
	 */
	public List<String> getSplit(String temp) {
		List<String> list = new ArrayList<String>();
		String[] str = temp.split(",");
		for (int i = 0; i < str.length; i++) {
			if (str[i] != null && !"".equals(str[i])) {
				list.add(str[i]);
			}
		}
		return list;
	}


	@Autowired
	public void setNumberManager(NumberManager numberManager) {
		this.numberManager = numberManager;
	}

	public String getNumberNo() {
		return numberNo;
	}

	public void setNumberNo(String numberNo) {
		this.numberNo = numberNo;
	}

}
