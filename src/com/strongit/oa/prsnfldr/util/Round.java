package com.strongit.oa.prsnfldr.util;

import java.math.BigDecimal;

public class Round {

	/**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
	public static double round(double v, int scale){
	   if (scale < 0){
	            throw new IllegalArgumentException(
	                    "The scale must be a positive integer or zero");
	   }
	   BigDecimal b = new BigDecimal(Double.toString(v));
	   BigDecimal one = new BigDecimal("1");
	   return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(round(18.456, 1));


	}

}
