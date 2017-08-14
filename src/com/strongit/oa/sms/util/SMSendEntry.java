/*package com.strongit.oa.sms.util;

import com.huawei.api.*;
import com.huawei.api.smsend.system.SMLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.dom4j.DocumentException;


*//**
 * <p>Title:MAS JAVA API����ʾ����� </p>
 * <p>Description:SMEntry ��JAVA API�ṩ�����һ��ӿ�</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company:  </p>
 * @author wx
 * @version 1.0
 *//*
public class SMSendEntry
{
	//String DB_IP=null;
	//String DB_UserName = null;
	//String DB_PWord =null;
	//String MAS_UserName=null;
   // String MAS_PWord=null;
    
    String sourceAddr=null;
    String ServiceID=null;
    String feeType = null;
    String feeCode = null;
    
    *//**
     * ��½MAS��ݿ��MASϵͳ
     * ��У�飩
     * �����Ƕ��ŷ��Ͳ���������Ƚ�����һ������
     *//*
    public int test_initLogin(PhoneConfig p)
    {
        // ����API�����ļ��Ĵ��·����Ⱥ�������ļ�·���Ĵ�ŵ�ַ	
       SMAPIConfig.setConfigFilePath("config");
//      wx add ���Ż���ѡ�� by 2009-01-11 begin
        try
        {
            *//**
             * �ӿڲ����ʼ��
             *//*
        	//��ӡ��ǰ�û�Ŀ¼
        	//System.out.println(System.getProperty("user.dir"));
            // ��ݿ��½����
            //String DB_IP = "jdbc:microsoft:sqlserver://168.1.11.248:1433;SelectMethod=cursor;DatabaseName=DB_CustomSMS";
            //String DB_IP = "168.1.11.248";
            //String DB_UserName = "CustomSMS";
            //String DB_PWord = "SqlMsde@InfoxEie2000";
            // MAS��½����
            // String MAS_UserName =cfg.getValue("MAS_UserName").trim();
            // String MAS_PWord = cfg.getValue("MAS_PWord").trim();
            //String MAS_UserName = "0001";
            //String MAS_PWord = "1";

            *//** ����}���ʼ���������ͨ��������½ӿ�ʵ�֣�
             *  SMEntry.init(DB_IP,DB_UserName,DB_PWord,MAS_UserName,MAS_PWord);
             *//*

            // �������ݿ�l�ӵĳ�ʼ������ ���Ի�����1
            SMEntry.init(p.getChinamobile_dbip(), p.getChinamobile_dbusername(),p.getChinamobile_dbpword());
            // ��ɵ�½MAS��Ȩ��У����� ���Ի�����2
            SMEntry.logIn(p.getChinamobile_masusername(), p.getChinamobile_maspword());
            return 1;
            
           //SMEntry.init(DB_IP,DB_UserName,DB_PWord,MAS_UserName,MAS_PWord);
        }
        catch (SMException ex)
        {
            SMLog.fatal(ex.getErrorDesc(),ex);
            System.out.println("______________DXM��ʼ��ʧ�ܣ�________________");
            return 0;
        }

    }
    
    //����è��ȡ����������
    
//     public static void get_DxmSl(String _Dxmtel,String _Content) {
//    	 //���Ȼ�ȡ��ݿ�l��
//    	 test_SendShorMessage("SendSms","����",String "Content");
//    	 
//    	 
//     }

   
    
    public  int SMSSingleSend(String _PhoneNomber, String _Content) throws DocumentException
    {
        // ִ�ж����ύ
        //test_SendShortMessage("15807956170", "6��F71187����������ң������ͺţ�SQR7080��1�֣���ͨ���ݷ־ֹ��վ2�ų��=�Ƿ���ʱ������Ƿ��·��13000Բ��");
    	UtilXML obj=new UtilXML();
    	List<PhoneConfig> alllist=new ArrayList<PhoneConfig>(0);
    	alllist=obj.findAllConfig();
		for(int i=0;i<alllist.size();i++){
			PhoneConfig s=new PhoneConfig();
			s=alllist.get(i);
			if(s.getChinamobile_type().equals("chinamobile")){
				// ��ʼ��
		        test_initLogin(s);
			}
		}
    
        //���7��Ͷ���
        return(SendShortMessage(_PhoneNomber,_Content));
     
    }


    *//**
     * ִ�з��͵���
     * @param PhoneNomber String  ����Ŀ�ĵ�ַ���ֻ���룩
     * @param Content String      ��������
     * @return int ������ֵ����0ʱ����ʾ����0����ţ�������ֵ����1ʱ����ʾ����һ����ųɹ���
                   �����쳣����ʾ�������ʧ�ܣ�����ʧ��ԭ������쳣�б?
     *//*
    public int SendShortMessage(String PhoneNomber, String Content)
    {
        *//**
         * �������������μ�JAVA API��˵���ĵ�
         *//*
        Date atTime = new Date();
        //String sourceAddr = "1860";
        //String sourceAddr = "1065730101680001";
      //  String sourceAddr = "106573010168";
        int needStateReport = 0;
        //String ServiceID = "API";
        //String feeType = "02";//01
        //String feeCode = "10";//0
        //String feeType = "01";
        //String feeCode = "20";

        try
        {
			*//**
             * ��8�������ŷ��ͽӿ�(����һ��10�����Ķ��ŵ����ӿڣ�������ͬ��
             * ��Ҫ��Ϊ�˼���ǰ�ڵİ汾�����Ժ���)
             *//*
       //     return  SMEntry.submitShortMessage(atTime, sourceAddr, PhoneNomber, Content, needStateReport,ServiceID, feeType, feeCode);
           return 1;
            
             * 	atTime�����Ͷ��ŵ�ʱ�䡣(Java.util.Date)
				sourceAddr�����Ͷ��ŵ�Դ��ַ��
				destAddr�����Ͷ��ŵ�Ŀ�ĵ�ַ��
				content���������ݡ�
				needStateReport�����͸ö����Ƿ���Ҫ״̬���档��ע��ʹ��״̬�������ȷ�϶Է��Ƿ�һ���յ������ò������ʹ��}��ֵ��0����ʾ����Ҫ״̬���棬1����ʾ��Ҫ״̬���档
				serviceID��ҵ�����͡�ҵ�����ͽ�������Ӫ�̶˶Զ��Ž��мƷ�ʱʹ�ã��ò����ܳ���10���ַ�
				feeType���ʷ����͡��ò���ֻ�������¼���ֵ����01����ʾ���û���ѣ���02����ʾ���û�������ȡ��Ϣ�ѣ������շѽ��>���һ�����03����ʾ���û���������ȡ��Ϣ�ѡ�
				feeCode���ʷѴ��롣�ò�����5��һ������ʾ�ö��ŵ���Ϣ�ѣ�ע���Է�Ϊ��λ�����ò����ܳ�������ַ�

             
        }
        
        catch (Exception ex)
        {
            SMLog.error("submit sms failed.",ex);
            System.out.println("____________dxfsfailed�������ŷ���ʧ�ܣ�____________________");
            return 0;
        }

      
    }
  

    *//**
     * ʾ�� 2
     * ����Ⱥ���������
     * [Ⱥ�������ļ�·��Ĭ�� = System.getProperty("user.dir")���������ֳ��Ŀ������������У�]
        ������SMAPIConfig.setConfigFilePath(String path)���������ļ���Ⱥ�������ļ��Ĵ��·
        ������ô�����û����õ�·��Ϊ׼
     * ���Ⱥ���������հѺ����ļ���֣���������ݿ���һ����Ϣ��һ���¼
     *//*
    public  void test_SMSMultiSend()
    {
        try
        {
            *//**
             * �������������μ�JAVA API��˵���ĵ�
             *//*
            SMEntry.submitShortMessageMulti(new Date(), "1065730101680001", "mutilmobile.txt", "����Ⱥ������,API���óɹ�", 0, "123456", "01", "20");
            System.out.println("Ⱥ�������ѷ���,API���óɹ���");
        }
        catch (Exception ex)
        {
            SMLog.error("submit multisend sms failed.",ex);
            System.out.println("Ⱥ�����ŷ���ʧ�ܣ�");
        }

    }
    

    
}


*/