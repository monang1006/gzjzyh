package com.strongit.oa.webservice.iphone.server.iphoneMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.bo.ToaMessageFolder;
import com.strongit.oa.ipadoa.util.WorkflowForIpadService;
import com.strongit.oa.message.MessageFolderManager;
import com.strongit.oa.message.MessageManager;
import com.strongit.oa.util.Dom4jUtil;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;

/**
 * @author shenyl
 *
 */
public class iphoneMessageWebService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static String STATUS_SUC = "1";// 返回成功状态
	private static String STATUS_FAIL = "0";// 返回失败状态

	private ToaMessage model = new ToaMessage();
	private List<ToaAttachment> attachMentList; // 附件列表
	private Set toaMessageAttaches;
	List<String[]> result = new ArrayList<String[]>();
    
	private MessageManager manager;
	private MessageFolderManager msgForlderManager;
	WorkflowForIpadService workflowForIpadService;
	
	 IAttachmentService attachmentManager;

	public iphoneMessageWebService() {
		manager = (MessageManager) ServiceLocator.getService("messageManager");
		msgForlderManager = (MessageFolderManager) ServiceLocator.getService("messageFolderManager");
		workflowForIpadService = (WorkflowForIpadService) ServiceLocator.getService("workflowForIpadService");
		attachmentManager = (IAttachmentService) ServiceLocator.getService("attachmentManager");
	}
	
	private Date parseDate(String dateStr) throws ParseException,SystemException {
		   try {
			  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      return dateFormat.parse(dateStr);
		   } catch (ParseException e) {
		     throw e;
		   } catch (Exception e) {
			   throw new SystemException(e);
		   }
		}
	


	/**获取收件箱邮件列表
	 * @param userId
	 * @param beginDate
	 * @param endDate
	 * @param titleName
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public String getMsgReceived(String userId,String beginDate, String endDate, String titleName,
			String pageSize, String pageNo) throws DAOException, ServiceException, SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			if (pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if (pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize), true);
			page.setPageNo(Integer.parseInt(pageNo));

			List<String[]> result = new ArrayList<String[]>();

			String folderId = msgForlderManager.getMsgFolderByName("收件箱", userId).getMsgFolderId();
			Date beginDateL = null;
			Date endDateL = null;
			if(beginDate != null && !"".equals(beginDate)){				
				beginDateL = parseDate(beginDate);
			}
			if(endDate != null && !"".equals(endDate)){
				endDateL = parseDate(endDate);
			}
			Page pageMessage = manager.getMsgByFolderId(page, folderId, beginDateL, endDateL,"", titleName);

			List messageList = pageMessage.getResult();

			if (messageList != null && !messageList.isEmpty()) {
				for (Object message : messageList) {

					// msg.msgId,msg.msgTitle,msg.isRead,msg.msgDate,msg.msgSize,msg.msgSender,msg.msgPri,msg.isHasAttr,msg.msgReceiver
					Object[] objs = (Object[]) message;

					String[] msgId = new String[2];
					msgId[0] = "String";
					msgId[1] = objs[0].toString();

					String[] msgTitle = new String[2];
					msgTitle[0] = "string";
					msgTitle[1] = objs[1].toString();

					String[] isRead = new String[2];
					isRead[0] = "string";
					isRead[1] = objs[2].toString();

					String[] msgDate = new String[2];
					msgDate[0] = "date";
					msgDate[1] = objs[3].toString();
					msgDate[1] =msgDate[1].substring(0, msgDate[1].length()-2);

					String[] msgSender = new String[2];
					msgSender[0] = "string";
					msgSender[1] = objs[5].toString();

					String[] isHasAttr = new String[2];
					isHasAttr[0] = "string";
					//可能没有保存是否存在附件
					if(objs[7] == null){
						objs[7] = "0";
					}
					isHasAttr[1] = objs[7].toString();

//					String[] msgReceiver = new String[2];
//					msgReceiver[0] = "string";
//					msgReceiver[1] = objs[8].toString();
					
					result.add(msgId);
					result.add(msgTitle);
					result.add(isRead);
					result.add(isHasAttr);

					result.add(msgSender);
					result.add(msgDate);

				}
			}

			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 6, String.valueOf(pageMessage.getTotalCount()),
					String.valueOf(pageMessage.getTotalPages()));
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发邮件发生数据级异常:" + ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发邮件发生服务级异常:" + ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发邮件发生系统级异常:" +ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发邮件发生未知异常:" + ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}
	
	/**获取发件箱邮件列表
	 * @param userId
	 * @param beginDate
	 * @param endDate
	 * @param titleName
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public String getMsgSended(String userId,String beginDate, String endDate, String titleName,
			String pageSize, String pageNo) throws DAOException, ServiceException, SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			if (pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if (pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize), true);
			page.setPageNo(Integer.parseInt(pageNo));

			List<String[]> result = new ArrayList<String[]>();

			String folderId = msgForlderManager.getMsgFolderByName("发件箱", userId).getMsgFolderId();
			Date beginDateL = null;
			Date endDateL = null;
			if(beginDate != null && !"".equals(beginDate)){				
				beginDateL = parseDate(beginDate);
			}
			if(endDate != null && !"".equals(endDate)){
				endDateL = parseDate(endDate);
			}
			Page pageMessage = manager.getMsgByFolderId(page, folderId, beginDateL, endDateL,"", titleName);

			List messageList = pageMessage.getResult();

			if (messageList != null && !messageList.isEmpty()) {
				for (Object message : messageList) {

					// msg.msgId,msg.msgTitle,msg.isRead,msg.msgDate,msg.msgSize,msg.msgSender,msg.msgPri,msg.isHasAttr,msg.msgReceiver
					Object[] objs = (Object[]) message;

					String[] msgId = new String[2];
					msgId[0] = "String";
					msgId[1] = objs[0].toString();

					String[] msgTitle = new String[2];
					msgTitle[0] = "string";
					msgTitle[1] = objs[1].toString();

					String[] isRead = new String[2];
					isRead[0] = "string";
					isRead[1] = objs[2].toString();

					String[] msgDate = new String[2];
					msgDate[0] = "date";
					msgDate[1] = objs[3].toString();
					msgDate[1] =msgDate[1].substring(0, msgDate[1].length()-2);

//					String[] msgSender = new String[2];
//					msgSender[0] = "string";
//					msgSender[1] = objs[5].toString();

					String[] isHasAttr = new String[2];
					isHasAttr[0] = "string";
					//可能没有保存是否存在附件
					if(objs[7] == null){
						objs[7] = "0";
					}
					isHasAttr[1] = objs[7].toString();

					String[] msgReceiver = new String[2];
					msgReceiver[0] = "string";
					msgReceiver[1] = objs[8].toString();
					
					result.add(msgId);
					result.add(msgTitle);
					result.add(isRead);
					result.add(isHasAttr);

					result.add(msgReceiver);
					result.add(msgDate);

				}
			}

			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 6, String.valueOf(pageMessage.getTotalCount()),
					String.valueOf(pageMessage.getTotalPages()));
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发邮件发生数据级异常:" + ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发邮件发生服务级异常:" + ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发邮件发生系统级异常:" +ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取收发邮件发生未知异常:" + ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}

	/**
	 * @param String
	 *            msgId 邮件id
	 */
	public String getMessageInfo(String msgId) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();

		try {
			model = manager.getMessageById(msgId);
			if(model==null){
				throw new Exception("邮件已不存在!");
			}
			if (model != null || !model.equals("")) {

				// 邮件发送时间msgDate
				String[] msgDate = new String[2];
				msgDate[0] = "string";
				msgDate[1] = model.getMsgDate().toString();

				// <item type="string"
				// value="超级管理员&lt;id:6006800BBDF034DFE040007F01001D9F&gt;"/>
				String tempSender = model.getMsgSender();
				String[] sender = new String[2];
				sender = tempSender.split("<id:");
				// 发件人ID
				String[] msgSenderId = new String[2];
				msgSenderId[0] = "string";
				msgSenderId[1] = sender[1].split(">")[0];

				// 发件人msgSender
				String[] msgSender = new String[2];
				msgSender[0] = "string";
				msgSender[1] = sender[0];

				// <item type="string"
				// value="超级管理员&lt;id:6006800BBDF034DFE040007F01001D9F&gt;"/>
				// 收件人msgReceiver
				String tempReceiver = model.getMsgReceiver();
				String[] receivers = tempReceiver.split(",");
				// 收件人ID
				String[] msgReceiverId = new String[2];
				// 收件人名称msgReceiver
				String[] msgReceiver = new String[2];
				msgReceiverId[0] = "string";
				String recevierIds = "";
				String recevierNames = "";
				msgReceiver[0] = "string";
				for (int i = 0; i < receivers.length; i++) {
					String receiver = receivers[i];
					String [] idAndName = receiver.split("<id:");
					recevierNames += "," + idAndName[0];
					recevierIds += "," + idAndName[1].substring(0, idAndName[1].length()-1);
					
					
				}

				msgReceiverId[1] = recevierIds.substring(1);
				msgReceiver[1] = recevierNames.substring(1);

				// 标题msgTitle
				String[] msgTitle = new String[2];
				msgTitle[0] = "string";
				msgTitle[1] = model.getMsgTitle();

				// Base64格式的正文内容
				BASE64Encoder encoder = new BASE64Encoder();
				String[] msgContent = new String[2];
				msgContent[0] = "string";
				msgContent[1] = model.getMsgContent()==null?"":model.getMsgContent();
				//变换字体大小
				if(msgContent[1] != null && !"".equals(msgContent[1])){					
					msgContent[1]=msgContent[1].replaceAll("size=&quot;.*?&quot", "size=&quot;5&quot");
					if(msgContent[1].split("amp;").length>2){
						msgContent[1]=msgContent[1].replaceAll("&amp;", "&");
					}
				}
				if(!"".equals(msgContent[1])){
					msgContent[1] = encoder.encode(msgContent[1].getBytes("utf-8"));
				}
				



				// 附件类 toaMessageAttaches
				attachMentList = manager.getAttachMent(model.getMsgId());// 附件列表
				List<String[]> attachs = new ArrayList<String[]>();
				if (attachMentList != null || attachMentList.isEmpty()) {
					ToaAttachment tmpAtt=null;
					for (ToaAttachment att : attachMentList) {
						if(att==null){
							continue;
						}
						// 附件记录id
						String[] toaMessageAttachesid = new String[2];
						toaMessageAttachesid[0] = "string";
						toaMessageAttachesid[1] = att.getAttachId();

						// 附件名称
						String[] toaMessageAttachesidname = new String[2];
						toaMessageAttachesidname[0] = "string";
						toaMessageAttachesidname[1] = att.getAttachName();
						
						
						tmpAtt=attachmentManager.getAttachmentById(att.getAttachId());
						// 附件大小
						String[] attSize = new String[2];
						attSize[0] = "string";
						attSize[1] = tmpAtt.getAttachCon()==null?"0":String.valueOf(tmpAtt.getAttachCon().length);
						
						attachs.add(toaMessageAttachesid);
						attachs.add(toaMessageAttachesidname);
						attachs.add(attSize);
					}
				}

				String[] systemFlag=new String[2];
				systemFlag[0]="string";
				systemFlag[1]="system".equals(sender[1].split(">")[0])?"1":"0";
					
				result.add(msgDate);
				result.add(msgSenderId);
				result.add(msgSender);
				result.add(msgReceiverId);
				result.add(msgReceiver);
				result.add(msgTitle);
				result.add(msgContent);
				result.add(systemFlag);
				
				//处理需要回执消息的邮件
				String parentMsgId = model.getParentMsgId();
				//发件箱中与该条消息相对应的记录
				if(parentMsgId != null && !"".equals(parentMsgId)){					
					ToaMessage parentMsg = manager.getMessageById(parentMsgId);
					
					if(parentMsg != null){
						String isReturnBack = model.getIsReturnBack();
						String isSendBack = parentMsg.getIsSendBack();
						if("1".equals(isSendBack) && (isReturnBack == null || "0".equals(isReturnBack))){
							//表示该邮件需要回执消息并且还没有发送回执消息
							//邮件发送回执消息
							manager.receipt(model);
							//修改邮件是否已发送回执消息的状态
							model.setIsReturnBack("1");							
						}	
						
					}
					
				}				
				// 查询了此邮件，是否读取状态，设置为读取状态
				model.setIsRead("1");
				manager.saveMessage(model);
				
				ret = dom.createXmlMessageDetail(STATUS_SUC, null, result, attachs,3);
			}
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取邮件详细信息发生数据级异常:" + ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取邮件详细信息发生服务级异常:" + ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取邮件详细信息发生系统级异常:" + ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取邮件详细信息发生未知异常:" + ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}
	
	
	

	/**
	 * @param id
	 * @return
	 */
	public String loadMessageAttachment(String id) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			ToaAttachment toaAttachMent=attachmentManager.getAttachmentById(id);

 			if (toaAttachMent == null || toaAttachMent.getAttachCon() == null) {
				//throw new SystemException("附件'" + id + "'不存在！");
 				throw new SystemException("附件不存在！");
			}else{
				long length_byte = toaAttachMent.getAttachCon().length;
			    if (length_byte >= 1024) {
					double length_k = ((double) length_byte) / 1024;
					if (length_k >= 6144) {
						ret = dom.createItemResponseData(STATUS_FAIL, "内部邮件附件大小超过6M,请在PC端查看附件！", null, null);
						logger.info(ret);
						return ret;
					} 
				}
			}
			BASE64Encoder encoder = new BASE64Encoder();
			String content = toaAttachMent.getAttachCon()==null?"":encoder.encode(toaAttachMent.getAttachCon());
			
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", content);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取邮件附件发生异常:" + ex.getMessage(), null, null);
		}
		return ret;
	}
	/**
	 * 
	 * 
	 * @description
	 *
	 * @author  hecj
	 * @date    May 8, 2014 12:25:33 PM
	 * @param   isReply
	 * 				回复标示
	 * 				1：回复 0:转发
	 * @return  String
	 * @throws
	 */
	public String quickSend(String userId, String msgId, String MsgReceiverId, String MsgTitle, String quicklyre,String isReply) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			// String content=filterManager.filterPhrase(quicklyre,
			// FILTER_PHRASE_ID);
			// String title = filterManager.filterPhrase(MsgTitle,
			// FILTER_PHRASE_ID);
			if(msgId!=null&&!"".equals(msgId)&&!"null".equals(msgId)){
				ToaMessage msg=manager.getMessageById(msgId);
				if(msg==null){
					return dom.createItemResponseData(STATUS_FAIL, "邮件发送或转发操作发生未知异常:邮件已被删除" , null, null);
				}
			}
			model.setMsgTitle(MsgTitle);
			BASE64Decoder decode=new BASE64Decoder();
			quicklyre=new String(decode.decodeBuffer(quicklyre),"utf-8");
			model.setMsgContent(HtmlUtils.htmlEscape(quicklyre));

			// 处理用户id格式user.getCurrentUser().getUserId()
			String sender = manager.formatUserId(userId);
			String recvUser = manager.formatUserId(MsgReceiverId);

			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);
			String res = null;
			
			res = manager.saveMessageExt(msgId, model,isReply);

			if ("1".equals(res)) {
				String[] str = new String[2];
				str[0] = "string";
				str[1] = res;
				ret = dom.createXmlMessage(STATUS_SUC, null, str);
			}
		} catch (DAOException ex) {
			ret = dom
					.createItemResponseData(STATUS_FAIL, "邮件发送或转发操作发生数据级异常:" + ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom
					.createItemResponseData(STATUS_FAIL, "邮件发送或转发操作发生服务级异常:" + ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom
					.createItemResponseData(STATUS_FAIL, "邮件发送或转发操作发生系统级异常:" + ex.getMessage(), null, null);
		} catch (Exception ex) {
			ex.printStackTrace();
			ret = dom.createItemResponseData(STATUS_FAIL, "邮件发送或转发操作发生未知异常:" + ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}

	public String quickReply(String userId, String msgId, String MsgReceiverId, String MsgTitle, String quicklyre) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			// String content=filterManager.filterPhrase(quicklyre,
			// FILTER_PHRASE_ID);
			// String title = filterManager.filterPhrase(MsgTitle,
			// FILTER_PHRASE_ID);
			if(msgId!=null&&!"".equals(msgId)&&!"null".equals(msgId)){
				ToaMessage msg=manager.getMessageById(msgId);
				if(msg==null){
					return dom.createItemResponseData(STATUS_FAIL, "邮件发送或转发操作发生未知异常:邮件已被删除" , null, null);
				}
			}
			model.setMsgTitle(MsgTitle);
			
			BASE64Decoder base64 = new BASE64Decoder();
			quicklyre  = new String(base64.decodeBuffer(quicklyre),"utf-8");
			model.setMsgContent(HtmlUtils.htmlEscape(quicklyre));

			// 处理用户id格式user.getCurrentUser().getUserId()
			String sender = manager.formatUserId(userId);
			String recvUser = manager.formatUserId(MsgReceiverId);

			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);
			String res = manager.replyMessage(userId, msgId, model);
			if (res != null || !"".equals(res)) {
				String[] str = new String[2];
				str[0] = "string";
				str[1] = res;
				ret = dom.createXmlMessage(STATUS_SUC, null, str);
			}
		} catch (DAOException ex) {
			ret = dom
					.createItemResponseData(STATUS_FAIL, "邮件发送或转发操作发生数据级异常:" + ex.getMessage(), null, null);
		} catch (ServiceException ex) {
			ret = dom
					.createItemResponseData(STATUS_FAIL, "邮件发送或转发操作发生服务级异常:" + ex.getMessage(), null, null);
		} catch (SystemException ex) {
			ret = dom
					.createItemResponseData(STATUS_FAIL, "邮件发送或转发操作发生系统级异常:" + ex.getMessage(), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "邮件发送或转发操作发生未知异常:" + ex.getMessage(), null, null);
		}
		logger.info(ret);
		return ret;
	}
	
	/**description: 删除消息:移动到垃圾箱
	 * @param userId
	 * @param msgId
	 * @return
	 */
	public String delete(String userId,String msgId){		
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			
			ToaMessage msg = manager.getMessageById(msgId);
			ToaMessageFolder folder = msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_RUBBISH,userId);
			msg.setToaMessageFolder(folder);
			manager.saveMessage(msg);			
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", "success");
		} catch (Exception e) {
			e.printStackTrace();
			ret = dom.createItemResponseData(STATUS_FAIL, "删除邮件发生异常:" +e.getMessage(), null, null);
		}
		return ret;
	}
	
	/**
	 * description: 删除消息:彻底删除
	 * @param userId
	 * @param msgId
	 * @return
	 */
	public String realdelete(String userId,String msgId){		
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			
			ToaMessage msg = manager.getMessageById(msgId);
			if(msg==null){
				throw new Exception("邮件已不存在!");
			}
			manager.delMessage(msg);
			
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", "success");
		} catch (Exception e) {
			e.printStackTrace();
			ret = dom.createItemResponseData(STATUS_FAIL, "删除邮件发生异常:" +e.getMessage(), null, null);
		}
		return ret;
	}
		
}
