package com.strongit.oa.mymail.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
/**
 * 邮件发送类解决了发送无附件邮件被服务器解析成带附件邮件的问题
 * @author yuhz
 * @version 1.0
 */
public class WriteMail {   
    //定义发件人、收件人、SMTP服务器、用户名、密码、主题、内容等
    private String displayName;					//发件人别名   
    private List<String> to;   					//接受人员列表
    private List<String> cc;					//抄送人员列表
    private List<String> bcc;					//暗送人员列表
    private String from;   				  		//发送人员	
    private String smtpServer;   				//smtp服务器
    private String username;   					//用户名
    private String password;   					//密码
    private String subject;   					//主题
    private String content;   					//正文
    private boolean ifAuth; 					//服务器是否要身份认证   
    private String filename="";
    private boolean readBack=false;				//已读回执
    private Vector file = new Vector(); 		//用于保存发送附件的文件名的集合   
    private String isSSL;						//是否进行SSL加密
    private String port;						//服务器端口
    
    
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:38:05 AM
     * @desc: 设置暗送地址
     * @param bcc	暗送人员列表
     * @return
     */
	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}

	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200910:38:40 AM
	 * @desc: 设置抄送人员列表
	 * @param cc	抄送人员列表
	 * @return
	 */
	public void setCc(List<String> cc) {
		this.cc = cc;
	}
      
      
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:39:34 AM
     * @desc: 设置接收服务器配置
     * @param smtpServer 接收服务器配置 
     * @return
     */   
    public void setSmtpServer(String smtpServer){   
        this.smtpServer=smtpServer;   
    }   
      
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:40:14 AM
     * @desc: 设置发送邮件地址
     * @param from 发送人员地址
     * @return
     */
    public void setFrom(String from){   
        this.from=from;   
    }   
    
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:40:44 AM
     * @desc: 设置邮件显示名称
     * @param displayName  邮件显示名称
     * @return
     */
    public void setDisplayName(String displayName){   
        this.displayName=displayName;   
    }   
      
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:41:10 AM
     * @desc: 设置发送身份验证类是否需要身份验证
     * @param ifAuth
     * @return
     */
    public void setIfAuth(boolean ifAuth){   
        this.ifAuth=ifAuth;   
    }   
      
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:41:35 AM
     * @desc: 设置Email的用户名
     * @param username   用户名
     * @return
     */
    public void setUserName(String username){   
        this.username=username;   
    }   
      
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:41:57 AM
     * @desc: 设置Email的用户的密码
     * @param password 密码
     * @return
     */
    public void setPassword(String password){   
        this.password=password;   
    }   
      
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:42:19 AM
     * @desc: 设置接收邮件人的邮件地址列表
     * @param to 邮件地址列表
     * @return
     */
    public void setTo(List<String> to){   
        this.to=to;   
    }   
      
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:43:02 AM
     * @desc: 设置邮件主题
     * @param subject  邮件主题
     * @return
     */
    public void setSubject(String subject){   
        this.subject=subject;   
    }   
      
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:43:20 AM
     * @desc: 设置邮件内容
     * @param content 邮件内容
     * @return
     */
    public void setContent(String content){   
        this.content=content;   
    }   
      
    /**  
     * 该方法用于收集附件名  
     */   
    public void addAttachfile(String fname){   
        file.addElement(fname);   
    }   
      
    public WriteMail(){   
          
    }   
      
    /**
     * @author：yuhz
     * @time：Feb 14, 200910:43:20 AM
     * @desc:初始化SMTP服务器地址、发送者E-mail地址、用户名、密码、接收者、主题、内容  
     * @param smtpServer			接收邮件服务器
     * @param from					发送者邮件
     * @param displayName			显示名称
     * @param username				账户名
     * @param password				密码
     * @param to					接收邮件人员列表
     * @param cc					抄送人员列表
     * @param bcc					密送人员列表
     * @param subject				主题
     * @param content				内容
     * @param isSSL					是否采用SSL加密
     * @param port					端口
     */
    public WriteMail(String smtpServer,String from,String displayName,String username,String password,List<String> to,List<String> cc,List<String> bcc,String subject,String content,String isSSL,String port){   
        this.smtpServer=smtpServer;   
        this.from=from;   
        this.displayName=displayName;   
        this.ifAuth=true;   
        this.username=username;   
        this.password=password;   
        this.to=to;   
        this.cc=cc;
        this.bcc=bcc;
        this.subject=subject;   
        this.content=content;
        this.isSSL=isSSL;
        this.port=port;
    }   
      
    /**
     * @author：yuhz
     * @time：Feb 14, 200910:43:20 AM
     * @desc:初始化SMTP服务器地址、发送者E-mail地址、接收者、主题、内容    
     * @param smtpServer		发送服务器
     * @param from				发送人邮件
     * @param displayName		显示名称
     * @param to				接受人邮件地址列表
     * @param subject			主题
     * @param content			内容
     */
    
    public WriteMail(String smtpServer,String from,String displayName,List<String> to,String subject,String content){   
        this.smtpServer=smtpServer;   
        this.from=from;   
        this.displayName=displayName;   
        this.ifAuth=false;   
        this.to=to;
        this.cc=cc;
        this.bcc=bcc;
        this.subject=subject;   
        this.content=content;   
    }  
    
    /**
     * @author：yuhz
     * @time：Feb 14, 200910:43:20 AM
     * @desc: 进行发送和抄送暗送的人员列表的设置
     * @param msg
     * @param recipientType
     * @param recipients
     * @throws Exception
     */
    
    private void setMailRecipients(Message msg, RecipientType recipientType, List recipients) throws Exception   
    {   
        //构建收信人地址数组。   
        InternetAddress[] internetAddresses = new InternetAddress[recipients.size()];   
        //遍历所有收信人。   
        for(int i = 0; i < recipients.size(); i++)   
        {   
            //构建收信人地址对象。   
            internetAddresses[i] = new InternetAddress(recipients.get(i).toString());   
        }   
        //设置收信人。   
        msg.setRecipients(recipientType, internetAddresses);   
    }  
   
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:45:05 AM
     * @desc: 发送邮件主作用类
     * @return
     * @throws UnsupportedEncodingException
     * @return
     */
    public HashMap send() throws UnsupportedEncodingException{   
        HashMap map=new HashMap();   
        map.put("state", "success");   
        String message="邮件发送成功！";   
        Session session=null;   
        Properties props = null;

        if(isSSL==null||"0".equals(isSSL)){
        	props=System.getProperties();
        	if(props.get("mail.smtp.socketFactory.class")!=null){
        		props.remove("mail.smtp.socketFactory.class");
        	}
        	if(props.get("mail.smtp.socketFactory.fallback")!=null){
        		props.remove("mail.smtp.socketFactory.fallback");
        	}
        	if(props.get("mail.smtp.socketFactory.port")!=null){
        		props.remove("mail.smtp.socketFactory.port");
        	}
        	Security.removeProvider(new com.sun.net.ssl.internal.ssl.Provider().getName());
            props.put("mail.smtp.host", smtpServer);
            props.setProperty("mail.smtp.port", this.port);
        }else{																						//设置SSL验证
        	  Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        	  final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        	  // Get a Properties object
        	  props = System.getProperties();
        	  props.setProperty("mail.smtp.host", "smtp.gmail.com");
        	  props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        	  props.setProperty("mail.smtp.socketFactory.fallback", "false");
        	  props.setProperty("mail.smtp.port", port);
        	  props.setProperty("mail.smtp.socketFactory.port", port);
        	  props.put("mail.smtp.auth", "true");
        }
        if(ifAuth){ 																				//服务器需要身份认证   
            props.put("mail.smtp.auth","true");      
            SmtpAuth smtpAuth=new SmtpAuth(username,password);   
            try{
            	session=Session.getInstance(props, smtpAuth); 
            }catch(Exception e){
            	e.printStackTrace();
            }
        }else{   
            props.put("mail.smtp.auth","false");   
            session=Session.getInstance(props, null);   
        }   
        //session.setDebug(true);   																//设置邮件控制台信息
        Transport trans = null;     
        try {   
            Message msg = new MimeMessage(session);    
            try{   
                Address from_address = new InternetAddress(from, displayName);   
                msg.setFrom(from_address);   
            }catch(java.io.UnsupportedEncodingException e){   
                e.printStackTrace();   
            }   
//            InternetAddress[] address={new InternetAddress(to)};  
            try {
            	if(to!=null&&to.size()!=0){
            		this.setMailRecipients(msg, RecipientType.TO, to);
            	}
            	if(cc!=null&&cc.size()!=0){
            		this.setMailRecipients(msg, RecipientType.CC, cc);
            	}
            	if(bcc!=null&&bcc.size()!=0){
            		this.setMailRecipients(msg, RecipientType.BCC, bcc);
            	}
            	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//            msg.setRecipients(Message.RecipientType.TO,address);   
            msg.setSubject(subject);

            if(!file.isEmpty()){//有附件   
                Multipart mp = new MimeMultipart();   
                BodyPart mbp = new MimeBodyPart();   
                mbp.setContent(content.toString(), "text/html;charset=utf-8");   
                mp.addBodyPart(mbp);     
                Enumeration efile=file.elements();   
                while(efile.hasMoreElements()){    
                    mbp=new MimeBodyPart();
                    FileHelper temp=(FileHelper)efile.nextElement();								//选择出每一个附件名   
                    FileDataSource fds=new FileDataSource(temp.getFile()); 							//得到数据源   
                    mbp.setDataHandler(new DataHandler(fds)); 										//得到附件本身并至入BodyPart   
                    mbp.setFileName(MimeUtility.encodeText(temp.getFileName()));					//得到文件名同样至入BodyPart   
                    mp.addBodyPart(mbp);   
                }     
                file.removeAllElements();
                msg.setContent(mp); 																//Multipart加入到信件	
            }else{
            	if(content!=null){
            		msg.setContent(content.toString(), "text/html;charset=utf-8");
            	}else{
            		msg.setContent("","text/html;charset=utf-8");
            	}
            }
            		 															   
            msg.setSentDate(new Date());     														//设置信件头的发送日期  
            msg.setHeader("X-Mailer", "StrongOA2.0 javamail webmail");
            if(readBack){
            	msg.setHeader("Disposition-Notification-To",from);
            }
            
            //msg.addHeader("Return-Receipt-To", "dengzc@strongit.com.cn");
            //msg.setHeader("Return-Receipt-To","dengzc@strongit.com.cn");
            //发送信件   
            msg.saveChanges();
            trans = session.getTransport("smtp");   
            trans.connect(smtpServer, username, password);   
            trans.sendMessage(msg, msg.getAllRecipients());   
            trans.close();   
        }catch(AuthenticationFailedException e){      
             map.put("state", "failed");   
             message="邮件发送失败！错误原因：\n"+"身份验证错误!";   
             e.printStackTrace();    
        }catch (MessagingException e) {   
             message="邮件发送失败！错误原因：\n"+e.getMessage();   
             map.put("state", "failed");   
             e.printStackTrace();   
             Exception ex = null;   
             if ((ex = e.getNextException()) != null) {   
                 System.out.println(ex.toString());   
                 ex.printStackTrace();   
             }    
        }catch (Exception e) {   
            message="邮件发送失败！错误原因 未知";   
            map.put("state", "failed");   
            e.printStackTrace();   
            System.out.println(message);   
       }   
        //System.out.println("\n提示信息:"+message);   
        //props=null;
        map.put("message", message);   
        return map;   
    }   
    public static void main(String args[]) throws UnsupportedEncodingException{
    	List<String> list=new ArrayList<String>();
    	List<String> cc=new ArrayList<String>();
    	List<String> bcc=new ArrayList<String>();
//    	try {
//			cc.add(MimeUtility.encodeText("丫头")+"<yhz19840818@yahoo.com.cn>");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try{
//			bcc.add(MimeUtility.encodeText("yuhz")+"<yuhz@strongit.com.cn>");
//			bcc.add(MimeUtility.encodeText("丫头")+"<yhz19840818@yahoo.com.cn>");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	try {
			list.add(MimeUtility.encodeText("yhz")+"<yuhz@strongit.com.cn>");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	WriteMail writeMail=new WriteMail("mail.strongit.com.cn","yuhz@strongit.com.cn","邮件测试","yuhz","111",list,cc,bcc,"邮件测试","邮件测试","0","25");
//   	writeMail.username="dengzc";
//    	writeMail.password="112526";
//    	writeMail.setCc(cc);
//    	writeMail.setBcc(bcc);
//    	writeMail.file.add("c:\\mailBox-input.jsp");
    	HashMap map=writeMail.send();
    	if("failed".equals(map.get("state"))){
    		//System.out.println(map.get("message"));
    	}else{
    		//System.out.println("邮件发送成功");
    	}
    }

	public void setIsSSL(String isSSL) {
		this.isSSL = isSSL;
	}

	public void setFile(Vector file) {
		this.file = file;
	}

	public void setReadBack(boolean readBack) {
		this.readBack = readBack;
	}

      
}   