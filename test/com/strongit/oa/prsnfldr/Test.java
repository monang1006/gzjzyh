package com.strongit.oa.prsnfldr;

import java.util.Date;

public class Test {

	/**
	 * @author:luosy
	 * @description:
	 * @date : 2012-4-5
	 * @modifyer:
	 * @description:
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String strTemp = "2012-04-04 17:28:00";
			Object[] obj = {strTemp};
			System.out.println("obj[0]:"+obj[0]);
			Date da = (Date) obj[0];
//			Date da = new Date(strTemp)
			System.out.println("da:"+da);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
