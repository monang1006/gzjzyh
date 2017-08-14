package com.strongit.oa.webservice.server.oaMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import sun.misc.BASE64Encoder;


import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.bo.ToaMessageFolder;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.ipadoa.util.WorkflowForIpadService;
import com.strongit.oa.message.MessageFolderManager;
import com.strongit.oa.message.MessageManager;
import com.strongit.oa.util.Dom4jUtil;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;

public class oaMessageWebService {
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

	// @Autowired
	private IUserService userService;

	public oaMessageWebService() {
		manager = (MessageManager) ServiceLocator.getService("messageManager");
		msgForlderManager = (MessageFolderManager) ServiceLocator.getService("messageFolderManager");
		workflowForIpadService = (WorkflowForIpadService) ServiceLocator.getService("workflowForIpadService");
		attachmentManager = (IAttachmentService) ServiceLocator.getService("attachmentManager");
		userService = (IUserService) ServiceLocator.getService("userService");
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
	

	/**
	@Description: TODO() 
	@author penghj    
	@date Feb 17, 2012 9:35:58 AM 
	@param userId
	@param folderdName
	@param beginDate
	@param endDate
	@param name
	@param pageSize
	@param pageNo
	@return
	@throws DAOException
	@throws ServiceException
	@throws SystemException
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
			Page pageMessage = manager.getMsgByFolderId(page, folderId, beginDateL, endDateL,null ,titleName);

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
	
	/**
	@Description: 发件箱接口 
	@author shenyl  
	@date 2012/08/07 17:20 
	@param userId
	@param folderdName
	@param beginDate
	@param endDate
	@param name
	@param pageSize
	@param pageNo
	@return
	@throws DAOException
	@throws ServiceException
	@throws SystemException
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
			Page pageMessage = manager.getMsgByFolderId(page, folderId, beginDateL, endDateL,null, titleName);

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
				String[] msgContent = new String[2];
				msgContent[0] = "string";
				msgContent[1] = model.getMsgContent();
				
				//变换字体大小
				if(msgContent[1] != null && !"".equals(msgContent[1])){					
					msgContent[1]=msgContent[1].replaceAll("size=&quot;.*?&quot", "size=&quot;5&quot") ;      
				}



				// 附件类 toaMessageAttaches
				attachMentList = manager.getAttachMent(model.getMsgId());// 附件列表
				List<String[]> attachs = new ArrayList<String[]>();
				if (attachMentList != null || attachMentList.isEmpty()) {

					for (ToaAttachment att : attachMentList) {
						// 附件记录id
						String[] toaMessageAttachesid = new String[2];
						toaMessageAttachesid[0] = "string";
						toaMessageAttachesid[1] = att.getAttachId();

						// 附件名称
						String[] toaMessageAttachesidname = new String[2];
						toaMessageAttachesidname[0] = "string";
						toaMessageAttachesidname[1] = att.getAttachName();
						attachs.add(toaMessageAttachesid);
						attachs.add(toaMessageAttachesidname);
					}
				}

				result.add(msgDate);
				result.add(msgSenderId);
				result.add(msgSender);
				result.add(msgReceiverId);
				result.add(msgReceiver);
				result.add(msgTitle);
				result.add(msgContent);
				
				
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
				
				ret = dom.createXmlMessageDetail(STATUS_SUC, null, result, attachs);
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
	@Description: TODO() 
	@author penghj    
	@date Mar 4, 2012 2:08:37 PM 
	@param id
	@return
	 */
	public String loadMessageAttachment(String id) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			ToaAttachment toaAttachMent=attachmentManager.getAttachmentById(id);

 			if (toaAttachMent == null || toaAttachMent.getAttachCon() == null) {
				throw new SystemException("附件'" + id + "'不存在！");
			}
			BASE64Encoder encoder = new BASE64Encoder();
			String content = encoder.encode(toaAttachMent.getAttachCon());
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", content);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取邮件附件发生异常:" + ex.getMessage(), null, null);
		}
		return ret;
	}

	public String quickSend(String userId, String msgId, String MsgReceiverId, String MsgTitle, String quicklyre) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			// String content=filterManager.filterPhrase(quicklyre,
			// FILTER_PHRASE_ID);
			// String title = filterManager.filterPhrase(MsgTitle,
			// FILTER_PHRASE_ID);
			model.setMsgTitle(MsgTitle);
			model.setMsgContent(HtmlUtils.htmlEscape(quicklyre));

			// 处理用户id格式user.getCurrentUser().getUserId()
			String sender = manager.formatUserId(userId);
			String recvUser = manager.formatUserId(MsgReceiverId);

			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);
			String res = null;
			
			res = manager.saveMessage(msgId, model);

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
			model.setMsgTitle(MsgTitle);
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

	public String getOrgInfo(String userId, String parentSysCode) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();

		try {
			List<TUumsBaseOrg> orgs = workflowForIpadService.getOrgInfo(userId, parentSysCode);
			if(!orgs.isEmpty()){
				for (TUumsBaseOrg org : orgs) {
					String[] str = new String[5];
					str[0] = "0";
					str[1] = org.getOrgId();
					str[2] = org.getOrgName();
					str[3] = org.getOrgSyscode();
					str[4] = parentSysCode;
					result.add(str);
				}
			}
			
			List<TUumsBaseUser> users = workflowForIpadService.getSystemAddressUsers(userId, parentSysCode);
			
			if(!users.isEmpty()){
				for (TUumsBaseUser user : users) {
					String[] str = new String[5];
					str[0] = "1";
					str[1] = user.getUserId();
					str[2] = user.getUserName();
					str[3] = "";
					str[4] = parentSysCode;

					result.add(str);
				}
			}
			ret = dom.createXmlOrgInfo(STATUS_SUC, null, result);
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取部门机构发生异常:" +e.getMessage(), null, null);
		}

		return ret;
	}
	
	
	/**获取与当前节点(当前节点可能是组织机构也可能是人员)的父级同级别的组织机构和人员列表
	 * @param 当前用户Id
	 * @param type 0代表部门，1代表人员
	 * @param orgSysCode(部门编码或者是人员Id)
	 * @return
	 */
	public String getParentOrgInfo(String userId,String type,String id){
		
		String parentSysCode = "";	
		if("0".equals(type)){
			TUumsBaseOrg org = userService.getParentOrgByOrgSyscode(id);
			
			if(org == null){//最顶级节点
				
			}else{		
				TUumsBaseOrg parentOrg = userService.getParentOrgByOrgSyscode(org.getOrgSyscode());
				if(parentOrg == null){
					
				}else{
					parentSysCode = parentOrg.getOrgSyscode();
				}
			}	
		}else{
			TUumsBaseOrg org = userService.getUserDepartmentByUserId(id);
			parentSysCode = org.getSupOrgCode();
		}
		
		return getOrgInfo(userId, parentSysCode);
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
			manager.delMessage(msg);
			
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", "success");
		} catch (Exception e) {
			e.printStackTrace();
			ret = dom.createItemResponseData(STATUS_FAIL, "删除邮件发生异常:" +e.getMessage(), null, null);
		}
		return ret;
	}
	
	
}
