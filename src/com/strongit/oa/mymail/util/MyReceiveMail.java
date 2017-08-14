package com.strongit.oa.mymail.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.IMAPInputStream;


public class MyReceiveMail {
	
	private static StringBuffer bodyText=new StringBuffer();
	
	public static void handle(Message msg) throws Exception{
        System.out.println("邮件主题:" + deCodeSubject(msg.getHeader("Subject")));
        System.out.println("发送至:"+MimeUtility.decodeText(msg.getHeader("To")[0]));
        System.out.println("MessageID:"+((MimeMessage)msg).getMessageID());
        try{
	        System.out.println("邮件作者:" + deCode(msg.getFrom()[0].toString()));
	        System.out.println("发送日期:" + msg.getSentDate());
        }catch(Exception e){
	        System.out.println("邮件作者:" + deCodeSubject(msg.getHeader("From")));
	        System.out.println("发送日期:" + deCodeSubject(msg.getHeader("Date")));
        }
	}
	
    private static void saveAttach(BodyPart part) throws Exception {
    	String temp=null;
    	if(part.getFileName()==null){
    		
    	}else{
    		temp = part.getFileName();
	        System.out.println("#################################未转码之前"+temp);
	        String s = deCode(temp);
	        String fileName = s==null?temp:s;
	        System.out.println("-------------------有附件:" + fileName);
	
	        InputStream in = part.getInputStream();
	        FileOutputStream writer = new FileOutputStream(new File("c:\\tmp"+ "\\"+fileName));
	        byte[] content = new byte[255];
	        int read = 0;
	        while ((read = in.read(content)) != -1) {
	            writer.write(content);
	        }
	        writer.close();
	        in.close();
    	}
    }
    
    public static String deCodeReceiver(String[] strs){
    	String receiver="";
    	try{
    		for(int i=0;i<strs.length;i++){
    			System.out.println(strs[i]);
    		}
    	}catch(Exception e){
    		
    	}
    	return receiver;
    }
    
    public static String deCodeSubject(String [] strs){
    	String subject="";
    	try{
	        for (String str : strs){
	            subject = new String(str.getBytes("ISO8859_1"), "GBK");
	        }
	        subject = MimeUtility.decodeText(subject);//base64解码
	        
    	}catch(Exception e){
    		
    	}
    	return subject;
    }
    
    public static String deCode(String str) throws Exception{
    	String returnStr;
    	if(str.matches("=\\?[\\w-]+\\?[bBqQ]\\?.*?")){
    		returnStr=MimeUtility.decodeText(str);
    	}else{
	    	try{
	    		returnStr=new String(str.getBytes("ISO8859_1"),"GBK");
	    	}catch(Exception e){
	    		returnStr=null;
	    	}
    	}
    	return returnStr;
    }
    
	public static void getMailContent(Part part,String type,int partCount) throws Exception {
		String contenttype = part.getContentType();
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if (nameindex != -1)
			conname = true;
//		System.out.println("CONTENTTYPE: " + contenttype)
		
		if (part.isMimeType("text/plain") && !conname) {
			if(partCount!=1){
				if(type.indexOf("multipart/alternative")==-1){
					System.out.println(part.getContent());
				}
			}else{
				System.out.println(String.valueOf(part.getContent()).length());
			}
		} else if (part.isMimeType("text/html") && !conname) {
			System.out.println(part.getContent());
		}else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i),contenttype,counts);
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent(),contenttype,0);
		}else if(part.isMimeType("APPLICATION/OCTET-STREAM")){
			saveAttach((BodyPart)part);
		}else{
			
		}
	}
	
	public static void handleMultipart(Message msg) throws Exception{
        String disposition;
        BodyPart part;
        Object mp = msg.getContent(); 
        String contentType=msg.getContentType();
        if(mp instanceof Multipart){
        	 int mpCount = ((Multipart)mp).getCount();
             for (int i=0;i<mpCount;i++) {
             
                 part = ((Multipart)mp).getBodyPart(i);
                 disposition = part.getDisposition();
                 //System.out.println(disposition);
                 //判断是否有附件
                 if (disposition != null && disposition.equals(Part.ATTACHMENT))//||(Part.INLINE).equals(disposition)
                 {
                	 saveAttach(part);
                 } else {
                     //不是附件，就只显示文本内容
//                	 Enumeration test=msg.getAllHeaders();
//                	 
//                	 while(test.hasMoreElements()){
//                		 Header header = (Header) test.nextElement();
//                		 System.out.println(header.getName()+":"+header.getValue());
//                	 }
                	 String transfere=deCodeSubject(msg.getHeader("Content-Transfer-Encoding"));
                	 //System.out.println("%%%%%%%%%%%%%%%"+deCodeSubject(msg.getHeader("Content-Transfer-Encoding"))+"%%%%%%%%%");
                	 //System.out.println("%%%%%%%%%%%%%%%%%"+(part.getDataHandler()).getTransferDataFlavors().getTransferData("Content-Transfer-Encoding")+"%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                	 //if(deCodeSubject(msg.getHeader("Content-Transfer-Encoding"))==null||"".equalsIgnoreCase(deCodeSubject(msg.getHeader("Content-Transfer-Encoding")))||"base64".equalsIgnoreCase(deCodeSubject(msg.getHeader("Content-Transfer-Encoding")))||"7bit".equalsIgnoreCase(deCodeSubject(msg.getHeader("Content-Transfer-Encoding")))||"quoted-printable".equalsIgnoreCase(deCodeSubject(msg.getHeader("Content-Transfer-Encoding")))){
                	if(transfere==null||"".equals(transfere)||"base64".equalsIgnoreCase(transfere)||"quoted-printable".equalsIgnoreCase(transfere)||"7bit".equalsIgnoreCase(transfere)||"8bit".equalsIgnoreCase(transfere)){	
                	 	try{
                	 		getMailContent((Part)part,contentType,mpCount);
//                	 		System.out.println(bodyText.toString());
//                	 		bodyText=null;
//                	 		bodyText=new StringBuffer();
                	 	}catch(Exception e){
                	 		System.out.println(inputStream2String(((MimeBodyPart)part).getRawInputStream()));
//							e.printStackTrace();
                	 	}
                	 }else{
                		 System.out.println(new String(((String)(part.getContent())).getBytes("iso-8859-1"),"gb2312"));
                	 }
                	// System.out.println(new String( ((String)(part.getContent())).getBytes("iso-8859-1"),"gb2312"));
//                	Object obj;
//                	if(part instanceof IMAPBodyPart){
//                		part.writeTo(System.out);
//                		obj=((IMAPBodyPart)part).getContentType();
//                	}else{
//                		obj=part.getContent();
//                	}
//                	
//                    if(obj instanceof String){
//                    	System.out.println(String.valueOf(obj));
//                    }else if(obj instanceof IMAPInputStream){
//                    	System.out.println(inputStream2String(((IMAPInputStream)obj)));
//                    }
                 }
             }
        }else if(mp instanceof String){
        	if(contentType.indexOf("text/plain")>=0){
        		//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$:"+contentType);
        		if(contentType.indexOf("gbk")>=0||contentType.indexOf("GBK")>=0||contentType.indexOf("gb2312")>=0||contentType.indexOf("GB2312")>=0||contentType.indexOf("UTF-8")>=0||contentType.indexOf("utf-8")>=0){
        			System.out.println((String)mp);
        		}else{
        			System.out.println(new String(((String)mp).getBytes("ISO8859_1"),"GBK"));
        		}
        	}else{
        		if(contentType.indexOf("us-ascii")>=0||contentType.indexOf("ISO8859_1")>=0||contentType.indexOf("iso8859_1")>=0){
        			System.out.println(new String(((String)mp).getBytes("ISO8859_1"),"GBK"));
        		}else{
        			System.out.println(String.valueOf(mp));
        		}
        	}
        }else if(mp instanceof IMAPInputStream){
        	
        	System.out.println(inputStream2String(((IMAPInputStream)mp)));
        }
	}
	
	public static void receive(String username,String password,String server){
		try{
			boolean isSSL=false;
			Properties prop;
			if(isSSL){
				Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
				final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
				prop = System.getProperties();
				prop.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
				prop.setProperty("mail.pop3.socketFactory.fallback", "true");
				prop.setProperty("mail.pop3.port", "995");
				prop.setProperty("mail.pop3.socketFactory.port", "995");
			}else{
				prop = System.getProperties();
			}
			URLName urln = new URLName("pop3",server,110,null,username, password);
			Session session = Session.getInstance(prop, null);
			Store store = session.getStore(urln);
			store.connect();
			Folder inbox = store.getDefaultFolder().getFolder("INBOX");

			//设置inbox对象为只读，如果要接受完毕后删除邮箱中的邮件要设置为READ_WRITE
			inbox.open(Folder.READ_ONLY);
			Message[] msg = inbox.getMessages();
			System.out.println("共有"+msg.length+"封邮件");
			FetchProfile profile = new FetchProfile();
			profile.add(FetchProfile.Item.ENVELOPE);
			inbox.fetch(msg, profile);
			for(int i=0;i<msg.length;i++){
				if(i==5){
					System.out.println();
				}
				MyReceiveMail.handleMultipart(msg[i]);
				MyReceiveMail.handle(msg[i]);
				System.out.println("**************************接shou完毕第"+(i+1)+"封邮件**************************");
			}
			MyReceiveMail.release(inbox, store);
		}catch(Exception e){
			try {
				System.out.println(deCode(e.getMessage()));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public static void release(Folder box,Store store){
		try{

			if(box.isOpen()){
				box.close(true);
			}
			if(store.isConnected()){
				store.close();
			}
		}catch(Exception e){
			//这里可能会存在文件夹关闭异常IMAP协议;
			if(box.isOpen()){
				try {
					box.close(true);
				} catch (MessagingException d) {
					// TODO Auto-generated catch block
					d.printStackTrace();
				}
			}
			if(store.isConnected()){
				try{
					store.close();
				}catch(Exception d){
					d.printStackTrace();
				}
			}
		}finally{			
			box=null;
			store=null;
		}

	}
	
	public static void main(String args[]){
		MyReceiveMail.receive("dengzc", "112526", "mail.strongit.com.cn");
//		try {
//			getReceiver("\"=?GB2312?B?0869qMa9?=\" <youjp@strongit.com.cn>,"+
//"\"=?GB2312?B?wO6408n6?=\" <ligs@strongit.com.cn>,"+
//"\"=?GB2312?B?0c/Wvruq?=\" <yanzh@strongit.com.cn>,"+
//"\"=?GB2312?B?0+DL2M7E?=\" <yusw@strongit.com.cn>");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public static String getReceiver(String receiver) throws Exception{
		String returnStr="";
		if(receiver.indexOf(",")!=-1){
			String[] rec=receiver.split(",");
			for(int i=0;i<rec.length;i++){
				if(rec[i].indexOf("\"")!=-1){
					System.out.println(rec[i].lastIndexOf("\""));
					String temp=rec[i].substring((rec[i].indexOf("\"")+1),(rec[i].lastIndexOf("\"")));
					String tempLast=rec[i].substring(rec[i].lastIndexOf("\""),rec[i].length());
					returnStr=returnStr+MimeUtility.decodeText(temp)+tempLast;
				}else{
					returnStr=returnStr+rec[i];
				}
				if(i!=rec.length-1){
					returnStr=returnStr+",";
				}
			}
		}else{
			returnStr=receiver;
		}
		System.out.println(returnStr);
		return returnStr;
	}
	
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

}
