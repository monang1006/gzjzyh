package com.strongit.xxbs.mail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;

import org.springframework.context.ApplicationContext;

import com.strongit.oa.common.user.util.Md5;
import com.strongit.xxbs.dto.MailInfo;
import com.strongit.xxbs.service.IMemberService;
import com.strongit.xxbs.service.IPublishService;
import com.strongit.xxbs.service.IUnsaveMailService;
import com.strongmvc.util.SpringContextUtil;

public class MailReceive
{
	
	public static void receive()
    {
		Properties props = new Properties();  
		//props.setProperty("mail.smtp.host", "smtp.163.com");
		//props.setProperty("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props); 
		
		String path = MailReceive.class.getResource("/").getPath();
		String file = path + "xxbs.mail.properties";
		
		Properties mail = new Properties();
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(file);
			mail.load(fis);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fis.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		String url = mail.getProperty("pop3.url");
		String strPort = mail.getProperty("pop3.port");
		Integer port = new Integer(strPort);
		String username = mail.getProperty("mail.username");
		String password = mail.getProperty("mail.password");
		String isdelete = mail.getProperty("mail.isdelete");
		Boolean isDelete = new Boolean(isdelete);
		String daynum = mail.getProperty("mail.receive.daynum");
		int dayNum = Integer.valueOf("-"+daynum).intValue();
				
		URLName urlname = new URLName("pop3", url, port.intValue(), null,
				username, password);
		Store store = null;
		Folder folder = null;
		try
		{
			store = session.getStore(urlname);
		}
		catch (NoSuchProviderException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			store.connect();
			folder = store.getFolder("INBOX");  
			folder.open(Folder.READ_WRITE);
			
			//POP3Folder inbox = (POP3Folder) folder;
			Folder inbox = folder;
				        
			Calendar now = Calendar.getInstance();
	        now.setTime(new Date());
	        now.add(Calendar.DATE, dayNum);
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
	        Date beginDate = now.getTime();
	        
	        SearchTerm st = new SentDateTerm(ComparisonTerm.GE, beginDate);
	        Message[] messages = inbox.search(st);
	        int messageCount = messages.length;
	        System.out.println("本次邮件收取数："+messageCount);  
	        
	        

        	ApplicationContext ctx = SpringContextUtil.getApplicationContext();

	        for (int i = 0; i < messageCount; i++)
	        {  
	            MimeMessage message = (MimeMessage) messages[i];
	            
	            if(isDelete)
	            {
		            message.setFlag(Flags.Flag.DELETED, true);
	            }
	            
	            InternetAddress[] address = (InternetAddress[]) message.getFrom();
	            
	            //System.out.println("地址：" + MimeUtility.decodeText(address[0].toString()));
	            //System.out.println("发送时间：" + message.getSentDate());  
	            //System.out.println("内容：" + message.getContent());
	            
	            
	            String subject = message.getSubject();
	            
	            String charset = "";
	            Boolean isReDeal = false;
	            String tmp = subject.toUpperCase();
	            if(tmp.contains("GB2312"))
	            {
	            	isReDeal = true;
	            	charset = "GB2312";
	            }
	            else if(tmp.contains("=?GBK?Q?"))
	            {
	            	isReDeal = true;
	            	charset = "GBK";
	            }
	            else if(tmp.contains("=?GB18030?Q?"))
	            {
	            	isReDeal = true;
	            	charset = "GB18030";
	            }
	            else if(tmp.contains("=?UTF-8?Q?"))
	            {
	            	isReDeal = true;
	            	charset = "UTF-8";
	            }

	            if(isReDeal)
	            {	            	
	            	subject = "";
	            	Enumeration e = message.getAllHeaders();
	            	while(e.hasMoreElements())
	            	{
	            		Header header = (Header) e.nextElement();
	            		if(header.getName().equalsIgnoreCase("subject"))
	            		{
	            			if("".equals(subject))
	            			{
		            			subject = header.getValue();
	            			}
	            			else
	            			{
	            				subject = header.getValue() + subject;
	            			}
	            		}
	            		if(header.getName().equalsIgnoreCase(header.getValue()))
	            		{
	            			if("".equals(subject))
	            			{
		            			subject = header.getValue();
	            			}
	            			else
	            			{
	            				subject = subject + header.getValue();
	            			}
	            		}
	            	}
	            	
	            	subject = subject.replace("\r", "");
	            	subject = subject.replace("\n", "");
	            	subject = subject.replace(" ", "");
	            	subject = subject.replace(charset, "");
	            	subject = subject.replace(charset.toLowerCase(), "");
	            	subject = subject.replace("?", "");
	            	subject = subject.replace("Q", "");
	            	subject = subject.replace("q", "");
	            	if(subject.startsWith("="))
	            	{
	            		subject = subject.substring(1);
	            	}
	            	while(subject.contains("=="))
	            	{
	            		subject = subject.replace("==", "=");
	            	}
	            	if(subject.endsWith("="))
	            	{
	            		subject = subject.substring(0, subject.length()-1);
	            	}
	            	subject = subject.trim();
	            	subject = "=?"+charset+"?Q?" +subject+"?=";

	            	subject = MimeUtility.decodeText(subject);
	            }
	            subject = subject.replace("\"", "”");
	            if(subject.length() == 0)
	            	subject = "无主题";
	            System.out.println("主题：" + subject);  
	            
	            Date sentDate = message.getSentDate();
	            String mailUrl = MimeUtility.decodeText(address[0].toString());
	            

	            
	            String content = "";
	            Object ct = message.getContent();
	            
	            if(ct instanceof Multipart)
	            {
	            	Multipart mm = (Multipart) ct;
	            	for(int count=0;count<mm.getCount(); count++)
	            	{
		            	BodyPart bp = mm.getBodyPart(count);		            	
		            	if(bp.getContentType().contains("text/plain"))
		            	{
		            		//System.out.println(bp.getContentType());
			            	content = (String) bp.getContent();
		            	}
	            	}
	            }
	            else
	            {
		            content = (String) message.getContent();
	            }
	            
	            content = content.replace("<", "＜");
	            content = content.replace(">", "＞");
	            
	            mailUrl = mailUrl.toLowerCase();
	            int p1 = mailUrl.indexOf("<");
	            int p2 = 0;
	            if(p1 != -1)
	            {
	            	p2 = mailUrl.indexOf(">");
	            	mailUrl = mailUrl.substring(p1+1, p2);
	            }
	            
	            
	            //String mailUid = inbox.getUID(message);
	            String mailUid = "";
	            mailUid = subject + sentDate.toString() + mailUrl + content;
	            
	            mailUid = (new Md5()).getMD5ofStr(mailUid);
	            
	            MailInfo info = new MailInfo();
	            info.setSubject(subject);
	            info.setDate(sentDate);
	            info.setMailUrl(mailUrl);
	            info.setMailUid(mailUid);
	            info.setContent(content);
	            
	            IMemberService memberService = (IMemberService) ctx.getBean("memberService");
	            String orgId = memberService.getOrgIdByMail(mailUrl);
	            String userId = memberService.getUserIdByMail(mailUrl);
	            
	            //若存在用户和机构，则保存邮件信息
	            if(!"".equals(orgId) && !"".equals(userId))
	            {
		            info.setOrgId(orgId);
		            info.setUserId(userId);
		            
		            IPublishService pub = (IPublishService) ctx.getBean("publishService");
		            pub.saveMailInfo(info);
	            }
	            else
	            {
	            	IUnsaveMailService um = (IUnsaveMailService) ctx.getBean("unsaveMailService");
	            	um.saveMailInfo(info);
	            }
	            
	        }
		}
		catch (MessagingException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				folder.close(true);
				store.close();
			}
			catch (MessagingException e)
			{
				e.printStackTrace();
			}
		}
    }
}
