package com.strongit.oa.sms.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.dom4j.DocumentException;


/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2012-12-18
 * Autour: luosy
 * Version: V1.0
 * Description： 
 */
public class SmsSenderManager {

	/**
	 * author:luosy
	 * description:
	 * modifyer:
	 * description:
	 * @param PhoneNombers
	 * @param Content
	 * @return
	 */
	public boolean sendAllSMS(String PhoneNombers, String Content) {
		// TODO Auto-generated method stub

		return false;
	}

	/**
	 * author:luosy
	 * description:
	 * modifyer:
	 * description:
	 * @param PhoneNomber
	 * @param Content
	 * @return
	 * @throws DocumentException
	 */
	public String sendSMS(String PhoneNomber, String Content) throws DocumentException {
		// TODO Auto-generated method stub
		RuleTemplate t=new RuleTemplate();
		int rule=t.SMSchannel(PhoneNomber);

		if(rule==1){//中国移动号码 走移动网关
		//	SMSendEntry obj=new SMSendEntry();
		//	int s=obj.SMSSingleSend(PhoneNomber,Content);
			return "移动网关发出："+PhoneNomber+":"+t.con;
		}else if(rule==2){//没有联通网关
			return "联通网关发出："+PhoneNomber+":"+t.con;
		}else if(rule==3){//没有电信网关
			return "电信网关发出："+PhoneNomber+":"+t.con;
		}else if(rule==4){//短信猫网关
			return "短信猫网关发出："+PhoneNomber+":"+t.con;
		}else{
			return "什么网关都没有找到，发送失败";
		}
	}
	
	

    public static void main(String[] org){
    	//System.out.println(getMobileType("15270858776"));
    }
}
