package com.strongit.oa.sms.util;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2012-12-18
 * Autour: luosy
 * Version: V1.0
 * Description： 
 */
public class RuleTemplate {
	public String con;
	/*public static void main(String[] ogs){
		RuleTemplate b=new RuleTemplate();
		int a=b.SMSchannel("18909873642");
		System.out.println(a);
	}*/
	/**
	 * author:luosy
	 * description:
	 * modifyer:
	 * description:
	 * @param mobile
	 * @return
	 */
	public int SMSchannel(String mobile){
		UtilXML obj=new UtilXML();
		String str=obj.isOpen();
		List chinaMobile  = Arrays.asList(str.split(",")) ; 
		int i=getMobileType(mobile);
		int ir;
		if(i==1){
			con="移动手机号码";
		}else if(i==2){
			con="联通手机号码";
		}else if(i==3){
			con="电信手机号码";
		}
		
		switch(i)
		{
		   case 1:  //移动
			   
			   if(chinaMobile.contains("chinamobile")){
				   ir=1;
			   }else if(chinaMobile.contains("chinamobile2")){
				   ir=10;
			   }else{
				   if(chinaMobile.contains("gsmmodem")){
					   ir=4;
				   }else{
					   ir=5;
				   }
			   }
			   break;
		   case 2:  //联通
			   
			   if(chinaMobile.contains("chinaunicom")){
				   ir=2;
			   }else{
				   if(chinaMobile.contains("gsmmodem")){
					   ir=4;
				   }else{
					   ir=5;
				   }
			   }
			   break;
		   case 3:  //电信
			   
			   if(chinaMobile.contains("chinatelecommunications")){
				   ir= 3;
			   }else{
				   if(chinaMobile.contains("gsmmodem")){
					   ir=4;
				   }else{
					   ir=5;
				   }
			   }
			   break;
		   default:
			   if(chinaMobile.contains("gsmmodem")){
				   ir=4;
			   }else{
				   ir=5;
			   }
		}
		return ir;
	}
	
	
	/**  
     * 判断号码是联通，移动，电信中的哪个, 
     * 在使用本方法前，请先验证号码的合法性 规则：前三位为130-133 联通 ；前三位为135-139或前四位为1340-1348 移动； 其它的应该为电信 
     * @param mobile要判断的号码 
     * @return 返回相应类型：1代表移动；2代表联通；3代表电信 
     */ 
    public static int getMobileType(String mobile) { 
    	if(mobile.length()<=3){
    		return 199;
    	}
     if(mobile.startsWith("0") || mobile.startsWith("+860")){ 
      mobile = mobile.substring(mobile.indexOf("0") + 1, mobile.length()); 
     } 
     InputStream in = SmsSenderManager.class.getClassLoader().getResourceAsStream(   
     "phoneTemplate.properties");   
	 Properties p = new Properties();   
	 try {   
	   p.load(in);   
	 }   
	 catch (IOException ie) {   
	   ie.printStackTrace();   
	 }   
	 
     List chinaMobile  = Arrays.asList(p.getProperty("ChinaMobile").split(",")) ; 
     List chinaUnicom = Arrays.asList(p.getProperty("ChinaUnicom").split(",")) ; 
     List chinaTelecommunications = Arrays.asList(p.getProperty("ChinaTelecommunications").split(",")) ; 

        boolean bolChinaMobile  = (chinaMobile.contains(mobile.substring(0,3))) ; 
        boolean bolChinaUnicom = (chinaUnicom.contains(mobile.substring(0,3))) ; 
        boolean bolChinaTelecommunications = (chinaTelecommunications.contains(mobile.substring(0,3))) ; 

        if (bolChinaMobile) 
            return 1  ;// 移动 
        if (bolChinaUnicom) 
            return 2 ; //联通 
        if(bolChinaTelecommunications)
        return 3 ; //为电信 
        return 4;  //短信猫
    }

    /**
     * author:luosy
     * description:
     * modifyer:
     * description:
     * @param orgs
     */
    public static void main(String[] orgs){
    	RuleTemplate obj=new RuleTemplate();
    	obj.SMSchannel("15270858776");
    }
}
