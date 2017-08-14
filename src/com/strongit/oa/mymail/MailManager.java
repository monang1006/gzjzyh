package com.strongit.oa.mymail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaMail;
import com.strongit.oa.bo.ToaMailAttach;
import com.strongit.oa.bo.ToaMailBox;
import com.strongit.oa.bo.ToaMailFolder;
import com.strongit.oa.mymail.util.FileObj;
import com.strongit.oa.mymail.util.MailUtil;
import com.strongit.oa.mymail.util.MessagePo;
import com.strongmvc.exception.DAOException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 对邮件进行管理
 * @author Administrator
 * @version 1.0
 */
@Service
@Transactional
public class MailManager {
	private GenericDAOHibernate<ToaMail,String> mailDao;
	
//	String pSubject;										//邮件主题	
//	String pContent;										//邮件内容
//	Date   pSentDate;										//邮件发送日期
//	String pSenter;											//邮件发送者
//	String pMessageId;										//邮件ID
//	String pReceiver;										//接受人
//	String pSize;											//邮件大小
//	String hasAtt="0";										//是否存在邮件
//	List<FileObj> attList=new ArrayList<FileObj>();			//附件列表
	
	private MailFolderManager mailFolderManager;
	
	private IAttachmentService attachmentService;
	
	private MailAttManager mailAttManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		mailDao = new GenericDAOHibernate<ToaMail,String>(sessionFactory,ToaMail.class);
	}
	
	@Autowired
	public void setMailFolderManager(MailFolderManager mailFolderManager) {
		this.mailFolderManager = mailFolderManager;
	}
	
	@Autowired
	public void setAttachmentService(IAttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	@Autowired
	public void setMailAttManager(MailAttManager mailAttManager) {
		this.mailAttManager = mailAttManager;
	}

	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:21:45 AM
	 * @desc: 根据文件夹来获取该文件夹未读邮件
	 * @param folder 
	 * @return int
	 */
	public int getFolderNum(ToaMailFolder folder){
		String par[]={folder.getMailfolderId(),"0"};
		List<ToaMail> list=mailDao.find("select mail.mailId from ToaMail as mail where mail.toaMailFolder.mailfolderId=? and mail.mailIsRead=?", par);
		if(list==null){
			return 0;
		}else{
			return list.size();
		}
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:22:18 AM
	 * @desc: 根据邮箱获得邮箱内所有邮件的messageID
	 * @param mailBox
	 * @return List<String>
	 */
	public List<String> getMessageId(ToaMailBox mailBox){
		ToaMailBox[] param={mailBox};
		List<String> list=mailDao.find("select mail.mailMessageId from ToaMail as mail where mail.toaMailFolder.toaMailBox=?", param);
		return list;
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 14, 200911:22:44 AM
	 * @desc: 根据人员ID获得最新的邮件
	 * @param userid
	 * @return
	 * @throws DAOException
	 * @throws SQLException List<ToaMail>
	 */
	public List<ToaMail> getNewestMailByUser(String userid,Page<ToaMail> page) throws DAOException, SQLException{
//		List<ToaMail> list = new ArrayList<ToaMail>();
//		StringBuffer sql = new StringBuffer("select t.MAIL_ID,t.MAIL_SENDER,t.MAIL_TITLE,t.MAIL_SEND_DATE,t.MAIL_IS_READ")
//			.append(" from T_OA_MAIL t,T_OA_MAIL_BOX b,T_OA_MAIL_FOLDER f")
//			.append(" where f.MAILBOX_ID=b.MAILBOX_ID and t.MAILFOLDER_ID=f.MAILFOLDER_ID")
//			.append(" and b.USER_ID='").append(userid).append("'")
//			.append(" and f.MAILFOLDER_TYPE='1' order by t.MAIL_SEND_DATE desc");
//		ResultSet rs = mailDao.getConnection().prepareStatement(sql.toString()).executeQuery();
//		while(rs.next()){
//			ToaMail mail = new ToaMail();
//			mail.setMailId(rs.getString(1));
//			mail.setMailSender(rs.getString(2));
//			mail.setMailTitle(rs.getString(3));
//			mail.setMailSendDate(rs.getDate(4));
//			mail.setMailIsRead(rs.getString(5));
//			list.add(mail);
//		}
		Page<ToaMail> list=mailDao.find(page ,"from ToaMail as mail where mail.toaMailFolder.toaMailBox.userId=? and mail.toaMailFolder.mailfolderType=1 order by mail.mailSendDate desc", userid);
		return list.getResult();
	}
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200911:23:29 AM
	 * @desc: 获得邮箱中的邮件（查询）
	 * @param folder 邮箱文件夹
	 * @param page   翻页对象
	 * @param name	 查询的条件
	 * @param beginDate	开始时间
	 * @param endDate   结束时间
	 * @return
	 * @throws UnsupportedEncodingException Page<ToaMail>
	 */
	public Page<ToaMail> getBoxMail(ToaMailFolder folder,Page<ToaMail> page,String name,Date beginDate,Date endDate) throws UnsupportedEncodingException{
		Object[] obj=new Object[4];
		obj[0]=folder;
		int i=1;
		String hql="select mail.mailId,mail.mailTitle,mail.mailIsRead,mail.mailIsHasAtt,mail.mailPri,mail.mailSender,mail.mailSendDate,mail.mailSize,mail.mailReceiver from ToaMail as mail where mail.toaMailFolder=?";
		if(name==null||"".equals(name)||"请您输入邮件主题".equals(URLDecoder.decode(name, "utf-8"))){
			obj[1]=null;
		}else{
			hql=hql+" and mail.mailTitle like ?";
			obj[1]="%"+URLDecoder.decode(name, "utf-8")+"%";
			i++;
		}
		if(beginDate==null){
			obj[2]=null;
		}else{
			hql=hql+" and mail.mailSendDate >=?";
			obj[2]=beginDate;
			i++;
		}
		if(endDate==null){
			obj[3]=null;
		}else{
			hql=hql+" and mail.mailSendDate <=?";
			obj[3]=endDate;
			i++;
		}
		Object[] param=new Object[i];
		for(int k=0,t=0;k<obj.length;k++){
			if(obj[k]!=null){
				param[t]=obj[k];
				t++;
			}
		}
		return mailDao.find(page, hql+" order by mail.mailSendDate desc", param);
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:53:45 PM
	 * @desc: 获取邮箱中邮件的查询方法
	 * @param id  邮箱文件夹ID
	 * @param page 翻页对象
	 * @param name 查询邮件的名称
	 * @param beginDate 开始日期
	 * @param endDate   结束日期
	 * @return
	 * @throws UnsupportedEncodingException Page<ToaMail>
	 */
	public Page<ToaMail> getBoxMailById(String id,Page<ToaMail> page,String name,Date beginDate,Date endDate) throws UnsupportedEncodingException{
		Object[] obj=new Object[3];
		int i=0;
		String hql="select mail.mailId,mail.mailTitle,mail.mailIsRead,mail.mailIsHasAtt,mail.mailPri,mail.mailSender,mail.mailSendDate,mail.mailSize,mail.mailReceiver from ToaMail as mail where mail.toaMailFolder.mailfolderId='"+id+"'";
		if(name==null||"".equals(name)||"请您输入邮件主题".equals(URLDecoder.decode(name, "utf-8"))){
			obj[0]=null;
		}else{
			hql=hql+" and mail.mailTitle like ?";
			obj[0]="%"+URLDecoder.decode(name, "utf-8")+"%";
			i++;
		}
		if(beginDate==null){
			obj[1]=null;
		}else{
			hql=hql+" and mail.mailSendDate >=?";
			obj[1]=beginDate;
			i++;
		}
		if(endDate==null){
			obj[2]=null;
		}else{
			hql=hql+" and mail.mailSendDate <=?";
			obj[2]=endDate;
			i++;
		}
		Object[] param=new Object[i];
		for(int k=0,t=0;k<obj.length;k++){
			if(obj[k]!=null){
				param[t]=obj[k];
				t++;
			}
		}

		
		return mailDao.find(page, hql+" order by mail.mailSendDate desc", param);
	}

	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200911:24:45 AM
	 * @desc: 根据ID获得邮件对象
	 * @param id
	 * @return ToaMail
	 */
	@Transactional(readOnly=true)
	public ToaMail getObjById(String id){
		return mailDao.get(id);
	}
	
	public boolean changeState(String mailId){
		ToaMail mail=getObjById(mailId);
		mail.setMailIsRead("1");
		if(mail!=null){
			if(saveObj(mail)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
		
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200911:25:10 AM
	 * @desc: 根据邮件ID删除邮件对象
	 * @param mailId
	 * @return boolean
	 */
	public boolean delObjById(String mailId){
		try{
			if(mailId.indexOf(",")!=-1){
				String[] id=mailId.split(",");
				try{
					for(int i=0;i<id.length;i++){
						ToaMail mail=mailDao.get(id[i]);
						Set set=mail.getToaMailAttaches();
						if(set==null||set.isEmpty()){
							
						}else{
							List<ToaMailAttach> list=new ArrayList<ToaMailAttach>(set);
							for(int k=0;k<list.size();k++){
								attachmentService.deleteAttachment(list.get(k).getAttachId6());
							}
						}
						mailDao.delete(id[i]);
					}
					
				}catch(Exception e){
					e.printStackTrace();
					return false;
				}
			}else{
				ToaMail mail=mailDao.get(mailId);
				Set set=mail.getToaMailAttaches();
				if(set==null||set.isEmpty()){
					
				}else{
					List<ToaMailAttach> list=new ArrayList<ToaMailAttach>(set);
					for(int k=0;k<list.size();k++){
						attachmentService.deleteAttachment(list.get(k).getAttachId6());
					}
				}
				mailDao.delete(mailId);
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200911:26:08 AM
	 * @desc: 将邮件更换存储的文件夹
	 * @param mailId  所要操作的邮件ID
	 * @param folder  目标文件夹
	 * @return boolean
	 */
	public boolean changeFolder(String mailId,ToaMailFolder folder){
		try{
			if(mailId.indexOf(",")!=-1){
				String[] id=mailId.split(",");
				try{
					for(int i=0;i<id.length;i++){
						ToaMail mail=mailDao.get(id[i]);
						mail.setToaMailFolder(folder);
						mailDao.save(mail);
						mailDao.flush();
					}
				}catch(Exception e){
					e.printStackTrace();
					return false;
				}
			}else{
				ToaMail mail=mailDao.get(mailId);
				mail.setToaMailFolder(folder);
				mailDao.save(mail);
				mailDao.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:55:33 PM
	 * @desc: 将邮件更换邮件文件夹
	 * @param mailId
	 * @return boolean
	 */
	public boolean changeBox(String mailId){
		try{
			if(mailId.indexOf(",")!=-1){
				String[] id=mailId.split(",");
				try{
					for(int i=0;i<id.length;i++){
						ToaMail mail=mailDao.get(id[i]);
						ToaMailBox mailBox=(mail.getToaMailFolder()).getToaMailBox();
						ToaMailFolder mailFolder=mailFolderManager.getFolderByParent(mailBox.getMailboxId(), "4");
						mail.setToaMailFolder(mailFolder);
						mailDao.save(mail);
						mailDao.flush();							//保存过程中每一次进行session的刷新
					}
				}catch(Exception e){
					e.printStackTrace();
					return false;
				}
			}else{
				ToaMail mail=mailDao.get(mailId);
				ToaMailBox mailBox=(mail.getToaMailFolder()).getToaMailBox();
				ToaMailFolder mailFolder=mailFolderManager.getFolderByParent(mailBox.getMailboxId(), "4");
				mail.setToaMailFolder(mailFolder);
				mailDao.save(mail);
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:01:39 PM
	 * @desc: 进行保存邮件
	 * @param toaMail
	 * @return boolean
	 */
	public boolean saveObj(ToaMail toaMail){
		try{
			int china=0;
			mailDao.save(toaMail);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 保存邮件对象
	 * @param toaMail
	 * @return
	 */
	public ToaMail saveReObj(ToaMail toaMail){
		mailDao.save(toaMail);
		return toaMail;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:02:32 PM
	 * @desc: 对收取的邮件进行保存
	 * @param session  session对象
	 * @param mailBox  邮箱对象
	 * @param mailFolder  所在文件夹对象
	 * @return
	 * @throws Exception boolean
	 */
	
	public boolean recSaveMail(HttpSession session,ToaMailBox mailBox,ToaMailFolder mailFolder) throws Exception{
		List<String> mesList=getMessageId(mailBox);
		MailUtil mailUtil=new MailUtil();
		Store mailStore=mailUtil.getMailSession(mailBox);
		try {
			mailStore.connect();
			Folder inbox = mailStore.getDefaultFolder().getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			Message[] msg = inbox.getMessages();
			//System.out.println("共有"+msg.length+"封邮件");
			session.setAttribute("totalmail",msg.length);
			FetchProfile profile = new FetchProfile();
			profile.add(FetchProfile.Item.ENVELOPE);
			inbox.fetch(msg, profile);
			ToaMail saveMail=null;
			for(int i=0;i<msg.length;i++){
				try {				
					MessagePo myPo=handle(msg[i]);							//先进行邮件头判断在进行邮件体读取
					
				//	System.out.println("\n\n========"+i+"===邮件标题:"+ msg[i].getSubject()+"\n========"
				//			+i+"===邮件时间:"+msg[i].getReceivedDate()+"\n========"
				//			+i+"===邮件附件:"+msg[i].getSize()+"\n\n");
					if(!mesList.contains(myPo.getPMessageId())||myPo.getPMessageId()==null||"".equals(myPo.getPMessageId())){
						myPo=handleContent(msg[i],myPo);								
						saveMail=new ToaMail();
						saveMail.setMailMessageId(myPo.getPMessageId());
						saveMail.setMailTitle(myPo.getPSubject());
						saveMail.setMailSender(myPo.getPSenter());
						saveMail.setMailSendDate(myPo.getPSentDate());
						saveMail.setMailReceiver(myPo.getPReceiver());
						saveMail.setMailSize(myPo.getPSize());
						saveMail.setMailCon(myPo.getPContent());
						saveMail.setToaMailFolder(mailFolder);
						saveMail.setMailIsHasAtt(myPo.getHasAtt());
						myPo.setHasAtt("0");
						saveMail.setMailPri("0");
						saveMail.setMailIsRead("0");
						saveMail.setMailIsReply("0");
						if(saveObj(saveMail)){
							if(myPo.getAttList().size()!=0){
								Calendar cals = Calendar.getInstance();
								//System.out.println(attList.size());
								for(int k=0;k<myPo.getAttList().size();k++){
									FileObj obj=myPo.getAttList().get(k);
									byte[] fileBytes=obj.getFileByte();
									String fileName=obj.getFileName();
									String attachId = attachmentService.saveAttachment(fileName, fileBytes, cals.getTime(), "", "1", "注:邮件附件", "userId");
									if(mailAttManager.saveObj(attachId, saveMail)){
										if(k==myPo.getAttList().size()-1){					//所有附件存储完毕
											session.setAttribute("getstatus", i+1);
										}
									}else{
										session.setAttribute("totalmail", "-1");
									}
									if(k==myPo.getAttList().size()-1){						//所有附件存储完毕,执行附件list清空操作
										
									}
								}
							}else{
								session.setAttribute("getstatus", i+1);
							}
						}else{
							//保存失败就发消息给前台提示失败
							session.setAttribute("totalmail", "-1");
						}
	//					session.setAttribute("getstatus", i+1);
						saveMail=null;
					}else{
						session.setAttribute("getstatus", i+1);
						myPo.setHasAtt("0");									//将邮件附件表示还原
						//System.out.println("已经收取过的邮件");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			MailUtil.release(inbox, mailStore);						//释放邮件连接
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			//System.out.println(MailUtil.deCode(((e.getMessage()))));
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println(MailUtil.deCode(((e.getMessage()))));
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:04:51 PM
	 * @desc: 对邮件信息的解析
	 * @param msg  邮件Message对象
	 * @throws Exception void
	 */
	public MessagePo handle(Message msg) throws Exception{
		MessagePo po=new MessagePo();
		po.setPMessageId(((MimeMessage)msg).getMessageID());
		po.setPSize(String.valueOf(msg.getSize()/1024+3));
		po.setPSubject(MailUtil.deCodeSubject(msg.getHeader("Subject")));
		
	/*
		//是否回执
		try{
        	String ydhz = (MailUtil.deCodeSubject(msg.getHeader("Disposition-Notification-To")));
        	System.out.println("\n\n"+MailUtil.deCodeSubject(msg.getHeader("Subject"))+"\n已读回执:"+ydhz+"\n\n");
        }catch(Exception e){
        	System.out.println("\n\n"+MailUtil.deCodeSubject(msg.getHeader("Subject"))+"\n已读回执:null\n\n");
        }
	*/ 
		
        try{
        	po.setPReceiver(stringToString(msg.getHeader("To")[0],","));
        }catch(Exception e){
        	po.setPReceiver("");
        }
        try{
        	po.setPSenter(MailUtil.deSenderCode(msg.getFrom()[0].toString()));
        	po.setPSentDate(msg.getSentDate());
        }catch(Exception e){
        	po.setPSenter(MailUtil.deCodeSubject(msg.getHeader("From")));
        	String tempDate=MailUtil.deCodeSubject(msg.getHeader("Date"));
        	if(tempDate==null||"null".equals(tempDate)){
        		po.setPSentDate(null);
        	}else{
        		po.setPSentDate(new Date(tempDate));
        	}
        }
        return po;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:05:37 PM
	 * @desc: 对邮件正文的解析
	 * @param msg 邮件Message对象
	 * @throws Exception void
	 */
	public MessagePo handleContent(Message msg,MessagePo po)throws Exception{
        String disposition;
        BodyPart part;
        Object mp = msg.getContent();
        String contentType=msg.getContentType();
        
        if(mp instanceof Multipart){
        	int count=((Multipart)mp).getCount();
        	for(int i=0;i<count;i++){
        		part=((Multipart)mp).getBodyPart(i);
        		disposition = part.getDisposition();
        		if(disposition!= null&&disposition.equals(Part.ATTACHMENT)||(Part.INLINE).equals(disposition)){
        			//System.out.println("出现附件请您存储");
        			FileObj temp=saveAttach(part);
        			if(temp!=null){
        				po.getAttList().add(temp);
        			}
        			po.setHasAtt("1");
        		}else{
        			String transfere=MailUtil.deCodeSubject(msg.getHeader("Content-Transfer-Encoding"));
        			if(transfere==null||"".equals(transfere)||"base64".equalsIgnoreCase(transfere)||"quoted-printable".equalsIgnoreCase(transfere)||"7bit".equalsIgnoreCase(transfere)||"8bit".equalsIgnoreCase(transfere)){
        				try{
        					po=getMailContent((Part)part,contentType,count,po);
        				}catch(Exception e){
        					//解决垃圾邮件不能够读取的问题（原因不符合MIME规范 QQ）
        					po.setPContent(MailUtil.inputStream2String(((MimeBodyPart)part).getRawInputStream()));
        					//System.out.println(MailUtil.inputStream2String(((MimeBodyPart)part).getRawInputStream()));
        				}
        			}
        		}
        	}
        }else if(mp instanceof String){
        	if(contentType.indexOf("text/plain")>=0){
        		if(contentType.indexOf("gbk")>=0||contentType.indexOf("GBK")>=0||contentType.indexOf("gb2312")>=0||contentType.indexOf("GB2312")>=0||contentType.indexOf("UTF-8")>=0||contentType.indexOf("utf-8")>=0){
        			po.setPContent((String)mp);
        			//System.out.println((String)mp);
        		}else{
        			po.setPContent(new String(((String)mp).getBytes("ISO8859_1"),"GBK"));
        			//System.out.println(new String(((String)mp).getBytes("ISO8859_1"),"GBK"));
        		}
        	}else{
        		if(contentType.indexOf("us-ascii")>=0||contentType.indexOf("ISO8859_1")>=0||contentType.indexOf("iso8859_1")>=0){
        			po.setPContent(new String(((String)mp).getBytes("ISO8859_1"),"GBK"));
        			//System.out.println(new String(((String)mp).getBytes("ISO8859_1"),"GBK"));
        		}else{
        			po.setPContent(String.valueOf(mp));
        			//System.out.println(String.valueOf(mp));
        		}
        	}
        }
        return po;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:06:43 PM
	 * @desc: 邮件正文递归操作
	 * @param part
	 * @param type 邮件体的格式
	 * @param partCount 邮件体数
	 * @throws Exception void
	 */
	public MessagePo getMailContent(Part part,String type,int partCount,MessagePo po) throws Exception{
		String contenttype = part.getContentType();
		int nameindex=contenttype.indexOf("name");
		boolean conname=false;
		if(nameindex!=-1){
			conname=true;
		}
		if (part.isMimeType("text/plain") && !conname) {
			if(partCount!=1){
				if(type.indexOf("multipart/alternative")==-1){
					po.setPContent((String)part.getContent());
					//System.out.println(part.getContent());
				}
			}else{
				po.setPContent((String)part.getContent());
				//System.out.println(part.getContent());
			}
		}else if(part.isMimeType("text/html")&&!conname){
			po.setPContent((String)part.getContent());
			//System.out.println(part.getContent());
		}else if(part.isMimeType("multipart/*")){
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				po=getMailContent(multipart.getBodyPart(i),contenttype,counts,po);
			}
		}else if(part.isMimeType("message/rfc822")){
			po=getMailContent((Part) part.getContent(),contenttype,0,po);
		}else if(part.isMimeType("APPLICATION/OCTET-STREAM")){
			//System.out.println("附件进行存储");
			FileObj temp=saveAttach(part);
			if(temp!=null){
				po.getAttList().add(temp);
			}
			po.setHasAtt("1");
		}
		return po;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:08:04 PM
	 * @desc: 对邮件正文进行字符串的分割操作将Q编码转化为UTF-8编码
	 * @param content 邮件正文
	 * @param sign    邮件正文标识
	 * @return String
	 */
	public String stringToString(String content,String sign){
		String reStr="";
		if(content!=null&&!"".equals(content)){
			if(content.indexOf(",")!=-1){
				String temp[]=content.split(",");
				for(int i=0;i<temp.length;i++){
					if(i!=temp.length-1){
						reStr=reStr+q2utf(temp[i])+",";
					}else{
						reStr=reStr+q2utf(temp[i]);
					}
				}
			}else{
				reStr=q2utf(content);
			}
		}else{
			reStr="";
		}
		return reStr;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:09:17 PM
	 * @desc: 收件人中带有Q或者B编码的解码情况
	 * @param str   需要进行解码的字符串
	 * @return String
	 */
	public String q2utf(String str){
		if(str!=null&&str.indexOf("<")!=-1){						//如果存在"<"号
			if(str.indexOf("<")==0){								//如果"<"号在第一位不做任何处理
				return str;
			}else{													//如果"<"号不在第一位则转码
				try {
					String temp=str.substring(0, str.indexOf("<"));
					if(temp.indexOf("\"")!=-1){
						str=MimeUtility.decodeText(temp.substring(temp.indexOf("\"")+1,temp.lastIndexOf("\"")))+str.subSequence(str.indexOf("<"),str.length());
					}else{
						str=MimeUtility.decodeText(temp)+str.subSequence(str.indexOf("<"),str.length());
					}
					return str;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					return str;
				}
			}
		}else{														//不存在"<"号的话默认是没有Q或B编码的文字
			return str;
		}
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:09:35 PM
	 * @desc: 进行附件保存操作
	 * @param part
	 * @return
	 * @throws Exception FileObj
	 */
	private FileObj saveAttach(Part part) throws Exception{
		try{
			String temp = part.getFileName();
			String s = temp==null?null:MimeUtility.decodeText(temp);
			String fileName = s==null?temp:s;
			InputStream file = part.getInputStream();
			byte[] fileByte=getBytesFromIS(file);
			FileObj obj=new FileObj();
			obj.setFileByte(fileByte);
			obj.setFileName(fileName);
			if(fileName==null){
				return null;
			}
			//System.out.println("附件");
			return obj;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private byte[] getBytesFromIS(InputStream is) throws IOException{
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    int b=0;
	    while( (b = is.read())!=-1)
	        baos.write(b);
	    return baos.toByteArray();
	}

	public void flashSession(){
		mailDao.getSession().clear();
	}
}
