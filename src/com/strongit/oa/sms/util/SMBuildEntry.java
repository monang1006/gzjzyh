package com.strongit.oa.sms.util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.huawei.eie.api.sm.DBSMProxy;
import com.huawei.eie.api.sm.SmReceiveBean;
import com.huawei.eie.api.sm.SmSendResultBean;
import com.huawei.eie.api.sm.SmSendBean;
import com.huawei.eie.api.sm.imp.SmApiLogger;

/**
 * <p>Title: mas</p>
 *
 * <p>Description: masproject</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: www.huawei.com</p>
 *
 * @author www.huawei
 * @version 1.0
 */
/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2012-12-18
 * Autour: luosy
 * Version: V1.0
 * Description： 
 */
public class SMBuildEntry
{
  private static long SendTime = 0;
    private static long ReceiveTime = 0;
    public static FileWriter fw = null;
    public static PrintWriter out = null;


    /** Creates a new instance of Main */
    public SMBuildEntry() {
    }

    public static final void testSendMessage(String phone,String content) throws Exception
    {
    	try{
        DBSMProxy smproxy = createProxy(null);
        testSend(phone,content,smproxy); //发送消息测试接口。
//        testGetReceivedSm(smproxy,40000); //接收消息测试接口。
//        testQuerysmResult(smproxy,40000);//查询消息测试接口。

        //退出登陆。
        smproxy.logout();

        //销毁连接。
        smproxy.destroy();
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
     }

     public static final DBSMProxy createProxy(DBSMProxy smproxy) throws Exception
     {
    	 try{
//       if (smproxy == null)
//       {
         smproxy = new DBSMProxy();
//       }
//            smproxy.initConn("smApiConf.xml");
//        try
//        {
//            smproxy.login("admin","Mas12345*");
//        }
//        catch (Exception ex)
//        {
//            smproxy.login("admin","Mas12345*");
//            ex.printStackTrace();
//        }
         
         Map paras = new HashMap();
         //访问sqlserver数据库时的参数设置  内部测试专用
//         paras.put("driver", "com.microsoft.jdbc.sqlserver.SQLServerDriver");
//         //paras.put("url","jdbc:microsoft:sqlserver://10.70.117.82;DatabaseName=db_customsms");
//         //paras.put("user","customsms");
//         //paras.put("password","123456");
//         paras.put("url","jdbc:microsoft:sqlserver://192.168.2.178:1433;SelectMethod=cursor;DatabaseName=DB_CustomSMS");
//         paras.put("user","sa");
//         paras.put("password","sa");
         
         
         //运管环境
//         paras.put("driver", "com.microsoft.jdbc.sqlserver.SQLServerDriver");
//         paras.put("url","jdbc:microsoft:sqlserver://168.1.11.248:1433;SelectMethod=cursor;DatabaseName=DB_CustomSMS");
//         paras.put("user","CustomSMS");
//         paras.put("password","SqlMsde@InfoxEie2000");
         

//       //抚州市
//	       paras.put("driver", "com.microsoft.jdbc.sqlserver.SQLServerDriver");
//	       paras.put("url","jdbc:microsoft:sqlserver://211.141.113.130:1393;SelectMethod=cursor;DatabaseName=db_customsms");
//	       paras.put("user","customsms");
//	       paras.put("password","SqlMsde@InfoxEie2000");
         
         
         //访问db2数据库时的参数设置   真实环境测试
        // paras.put("driver", "com.ibm.db2.jcc.DB2Driver");
        // paras.put("url","jdbc:db2://192.168.2.248:50110/MASDB");
        // paras.put("user","db2inst1");
        // paras.put("password","Y4yhl9T");
         
//       //抚州市
//          paras.put("driver", "com.ibm.db2.jcc.DB2Driver");
//          paras.put("url","jdbc:db2://211.141.113.130:1393/customsms");
//          paras.put("user","db_customsms");
//          paras.put("password","SqlMsde@InfoxEie2000");
       
         
         
         //    以下示例提供了3种初始化连接的方法。r
         smproxy.initConn(paras);//通过上面设定的参数初始化。2
      //   smproxy.login("admin", "Y4yhl9T#");
       //  smproxy.login("oa","jstoA123#");
         smproxy.login("9999","Mas9999!");
        return smproxy;
    	 }catch(Exception ex){
    		 ex.printStackTrace();
    		 return null;
    	 }
     }

     /**
     * 查询上行消息代码样例。
     * @param smproxy DBSMProxy
     * @param count int
     * @throws Exception
     */
     public static final void testGetReceivedSm(String phone,String content,DBSMProxy smproxy) throws Exception
     {
        SmReceiveBean[] beans = null;
        //循环接收测试短信
        SmApiLogger.getLogger().info("===========Receive Start==========");
        long firstTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int index = 0; index < 1; index++)
        {
             beans = smproxy.getReceivedSms(2,null,null,null,null);
            
             if (beans.length != 0)
             {
                 SmApiLogger.getLogger().info("get received sms from MAS,SourceAddr = "+ beans[0].getSmOrgAddr() +", MsgContent = "+beans[0].getSmMsgContent());
                 SmApiLogger.getLogger().info("输出时间: "+sdf.format(beans[0].getSmRecvTime()));
                 beans[0] = null;
             }
        }

        long lastTime = System.currentTimeMillis();
        ReceiveTime = lastTime - firstTime;
        SmApiLogger.getLogger().info(" Received:" + 1 + " records,consumed " + (lastTime - firstTime) + " ms");

        SmApiLogger.getLogger().info("===========Receive End==========");
     }

  /**
   * 查询消息发送结果，代码样例。
   *
   * @param smproxy DBSMProxy
   * @param count int
   * @throws Exception
   */
  public static final void testQuerysmResult(DBSMProxy smproxy,int count) throws Exception
      {
        //构造查询条件，入口参数详细说明参见上面函数说明。
      SmApiLogger.getLogger().info("=======query start========"+new java.util.Date());
        long firstTime = System.currentTimeMillis();
        for (int index = 0; index < count ; index++)
        {
          SmSendResultBean[] beans =
              smproxy.querySmsResult( -1
              , new java.util.Date(System.currentTimeMillis() - 86400000L)
              , new java.util.Date(System.currentTimeMillis())
              , "1860", "1399999");
          for (SmSendResultBean bean : beans)
          {
              SmApiLogger.getLogger().info("bean:" + bean.getSmMsgContent());
          }
        }
        long lastTime = System.currentTimeMillis();
        ReceiveTime = lastTime - firstTime;
        SmApiLogger.getLogger().info(" query:" + count + " times,consumed " + (lastTime - firstTime) + " ms");
        SmApiLogger.getLogger().info("query end"+new java.util.Date());
      }

      /**
       * 发送测试代码样例。
       * @param smproxy DBSMProxy
       * @param count int
       * @throws Exception
      */
      public static final void testSend(String phone,String content,DBSMProxy smproxy)throws Exception
      {
         SmSendBean bean = new SmSendBean();
         //ArrayList<String> arrs = new ArrayList();
         String[] addrs = new String[1];
         addrs[0] = phone;
         int[] ret = new int[1];
         //循环发送测试短信
         SmApiLogger.getLogger().info("===========Send Start==========");
         String ss = "===========Send Start==========";
         long firstTime = System.currentTimeMillis();

         for (int index = 0; index < 1; index++)
         {
            bean.setSmDestAddrs(addrs);
            bean.setSmMsgContent(content);
            bean.setSmSendTime(new Date());
            bean.setMsgFmt("15");
            bean.setSendMethodType(5);
            bean.setSmServiceId("EIE");
            bean.setAppendOperatorID(false);
            bean.setSuboperationType("5");
            bean.setOperationType("WAS");
            bean.setSendType(1);
       //     bean.setMsgFmt("0");
            //检测。
//            if (smproxy.isConnected())
//            {
            SmApiLogger.getLogger().info("................true");
              ret = smproxy.sendSm(bean);
              System.out.println(ret[0]);
//            }
//            else //循环等待。
//            {
//              while (!smproxy.isConnected())
//              {
//                System.out.println("................false");
//                Thread.currentThread().sleep(500);
//                try
//                {
//                  smproxy.destroy();
//                  createProxy(smproxy);
//                }
//                catch (Exception ex)
//                {
//
//                }
//              }
//            }
              SmApiLogger.getLogger().info("MAS Send Result " + ret[0]);
         }

         long lastTime = System.currentTimeMillis();
         SendTime = lastTime - firstTime;
         SmApiLogger.getLogger().info(" Sended:" + 1 + " records,consumed " + (lastTime - firstTime) + " ms");
         SmApiLogger.getLogger().info("===========Send End==========");
      }

     /**
     * 测试方法主入口。
     *
     * @param args String[]
     * @throws Exception
     */
   public static void main(String[] args)
      throws Exception
   {
       //File sendFile = FileUtil.newFileDelExit("testResult.txt");
       //SendCount = Integer.parseInt(args[0]);
       //ReceiveCount = Integer.parseInt(args[1]);

       FileWriter fw = new FileWriter("testResult.txt");
       PrintWriter out = new PrintWriter(fw);
//       testSendMessage("15270858776","A短信测试");
       testSendMessage("13767109565","A短信测试");
      System.out.println(" Send Use Time: " + SendTime + "; Receive Use Time: " + ReceiveTime);
      out.println(" Send Use Time: " + SendTime + "; Receive Use Time: " + ReceiveTime);
       out.close();
       fw.close();
       SmApiLogger.getLogger().info("====all test finished.press any key to exit.======");
       System.in.read();
   }
   
   public static void sendSMS(String phone,String content)
   throws Exception{
	   testSendMessage(phone,content);
   }

}
