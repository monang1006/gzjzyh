/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息 action跳转类
 */
package com.strongit.oa.reply_message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.bo.ToaMobileReplyMessage;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.bo.ToaSms;
import com.strongit.oa.bo.ToaSmsRep;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.message.MessageManager;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.reply_message.vo.MsgReceiver;
import com.strongit.oa.sms.SmsManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "messageReply.action", type = ServletActionRedirectResult.class) })
public class MessageReplyAction extends BaseActionSupport {

	private Page<ToaMessage> page = new Page<ToaMessage>(FlexTableTag.MAX_ROWS, true);

	private String messageId;
	
	private String bussinessId;
	
	private String moduleId;
	
	private String replyMessageId;
	
	private String unreplyPerson;
	
	private int readMsgNum;
	
	private int noReadMsgNum;
	
	private int hasreply;
	
	private int unreply;
	
	private List<ToaMobileReplyMessage> replyList;
	
	private List<MsgReceiver> receiverList;
	
	private List messageList;

	private ToaMessage message = new ToaMessage();
	
	private ToaMobileReplyMessage model = new ToaMobileReplyMessage();

	private MessageManager messagemanager;
	
	private MessageReplyManager manager;
	
	private MyInfoManager infoManager;						//个人信息manager
	
	private SmsManager smsManager;
	
	private IUserService user;
	
	private List<ToaSms> smsList;
	
	private ToaSmsRep smsrep = new ToaSmsRep();
	
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	
	@Autowired
	public void setInfoManager(MyInfoManager infoManager) {
		this.infoManager = infoManager;
	}

	@Autowired
	public void setManager(MessageReplyManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setMessagemanager(MessageManager messagemanager) {
		this.messagemanager = messagemanager;
	}

	@Autowired
	public void setSmsManager(SmsManager smsManager) {
		this.smsManager = smsManager;
	}
	
	public void setModel(ToaMobileReplyMessage model) {
		this.model = model;
	}

	public ToaMobileReplyMessage getModel() {
		return model;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page<ToaMessage> aPage) {
		page = aPage;
	}

	public MessageReplyAction() {

	}
	
	////////////////////////////// 以下为action跳转方法
	
	public String input() throws Exception {
		return null;
	}

	/**
	 * author:luosy
	 * description:	消息列表
	 * modifyer:
	 * description:
	 * @return
	 */
	public String list() throws Exception {
		return null;
	}

	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getReplyMessageId() {
		return replyMessageId;
	}

	public void setReplyMessageId(String replyMessageId) {
		this.replyMessageId = replyMessageId;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public String view() throws Exception {
		// TODO Auto-generated method stub
//		message = messagemanager.getMessageById(messageId);
//		unreplyPerson = message.getMsgReceiver();
//		replyList = manager.getReplayList(moduleId,message.getMessageCode());
//		ToaPersonalInfo person=new ToaPersonalInfo();
//		for(int i=0;i<replyList.size();i++){
//			ToaMobileReplyMessage rmsg = replyList.get(i);
//			System.out.print("phone is "+rmsg.getMobileNumber());
//			person = infoManager.getInfoByTelephoneNum(rmsg.getMobileNumber());
//			if(null!=person){
//				String userid = person.getUserId();
//				User toauser = user.getUserInfoByUserId(userid);
//				if(unreplyPerson.equals(toauser.getUserName()+"<id:"+userid+">"))
//					unreplyPerson = "";
//				else
//					unreplyPerson.replaceAll(toauser.getUserName()+"<id:"+userid+">"+",", "");
//			}
//		}
//		hasreply = replyList==null?0:replyList.size();
//		if(unreplyPerson.equals(",")||unreplyPerson.equals(""))
//			unreply = 0;
//		else{
//			String[] MsgReceiver = unreplyPerson.split(",");			
//			unreply = MsgReceiver.length;
//		}
		message = messagemanager.getMessageById(messageId);
		List arrList = messagemanager.getRecvMessage(messageId);
		receiverList = new ArrayList<MsgReceiver>();
		int hasread=0;
		for(int i=0;i<arrList.size();i++){
			Object[] temp=(Object[])arrList.get(i);
			MsgReceiver vo=new MsgReceiver();
			vo.setReceiverName(user.getUserNameByUserId(temp[0].toString()));
			vo.setIsRead(temp[1].toString());
			if("1".equals(vo.getIsRead())){
				hasread++;
			}
			receiverList.add(vo);
		}
		replyList=manager.getReplayList(message.getModuleCode(),message.getMessageCode());
		smsList=smsManager.getReceiverList(message.getModuleCode(), message.getMessageCode());
		ToaPersonalInfo person=new ToaPersonalInfo();
		unreplyPerson="";
		for(int i=0;i<smsList.size();i++){
			ToaSms rmsg = smsList.get(i);
//			System.out.print("phone is "+rmsg.getSmsRecver());
			if(rmsg.getSmsRecvnum()==null){
				unreplyPerson=unreplyPerson+"<font color=red  style=\"font-weight:bold\">"+rmsg.getSmsRecver()+"</font>,";
				continue;
			}else{
				person = infoManager.getInfoByTelephoneNum(rmsg.getSmsRecvnum());
			}
			if(null!=person){
				String userid = person.getUserId();
				User toauser = user.getUserInfoByUserId(userid);
				if(!isExists(rmsg.getSmsRecvnum())){
					if(toauser!=null){
						unreplyPerson=unreplyPerson+"<font color=red  style=\"font-weight:bold\">"+toauser.getUserName()+"</font>,";
					}else{
						unreplyPerson=unreplyPerson+"<font color=red  style=\"font-weight:bold\">"+rmsg.getSmsRecvnum()+"</font>,";
					}
				}
			}else{
				if(!isExists(rmsg.getSmsRecvnum())){
					unreplyPerson=unreplyPerson+"<font color=red  style=\"font-weight:bold\">"+rmsg.getSmsRecvnum()+"</font>,";
				}
			}
		}
		if(unreplyPerson!=null &&!"".equals(unreplyPerson)){
			unreplyPerson = unreplyPerson.substring(0,unreplyPerson.length()-1);
		}
		this.readMsgNum=hasread;
		this.noReadMsgNum=arrList.size()-hasread;
		hasreply=replyList.size();
		unreply=smsList.size()-replyList.size();
		return "view";
	}
	
	private boolean isExists(String telephoneNum){
		boolean exists=false;
		for(int i=0;i<replyList.size();i++){
			ToaMobileReplyMessage temp=replyList.get(i);
			String myTelNum=temp.getMobileNumber();
			if(telephoneNum.equals(myTelNum.substring(myTelNum.length()-11, myTelNum.length()))){
				exists=true;
				break;
			}
		}
		return exists;
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List getReplyList() {
		return replyList;
	}

	public List getMessageList() {
		return messageList;
	}

	public String getUnreplyPerson() {
		return unreplyPerson;
	}

	public void setUnreplyPerson(String unreplyPerson) {
		this.unreplyPerson = unreplyPerson;
	}

	public int getHasreply() {
		return hasreply;
	}

	public void setHasreply(int hasreply) {
		this.hasreply = hasreply;
	}

	public int getUnreply() {
		return unreply;
	}

	public void setUnreply(int unreply) {
		this.unreply = unreply;
	}

	public ToaMessage getMessage() {
		return message;
	}

	public void setMessage(ToaMessage message) {
		this.message = message;
	}

	public List getReceiverList() {
		return receiverList;
	}

	public void setReceiverList(List receiverList) {
		this.receiverList = receiverList;
	}

	public int getNoReadMsgNum() {
		return noReadMsgNum;
	}

	public int getReadMsgNum() {
		return readMsgNum;
	}



	/**
	 * @author:luosy
	 * @description:	短信回复统计界面
	 * @date : 2010-11-17
	 * @modifyer:
	 * @description:
	 * @return
	 */
	public String smsRepView(){
		
		smsrep = smsManager.getCodeByBussinessId(bussinessId);
		if(smsrep==null){
			return "smsReplyView";
		}
		replyList=manager.getReplayList(smsrep.getModelCode(),smsrep.getIncreaseCode());
		smsList=smsManager.getReceiverList(smsrep.getModelCode(),smsrep.getIncreaseCode());
		ToaPersonalInfo person=new ToaPersonalInfo();
		unreplyPerson="";
		for(int i=0;i<smsList.size();i++){
			ToaSms rmsg = smsList.get(i);
			if(rmsg.getSmsRecvnum()==null){
				unreplyPerson=unreplyPerson+"<font color=red  style=\"font-weight:bold\">"+rmsg.getSmsRecver()+"</font>,";
				continue;
			}else{
				person = infoManager.getInfoByTelephoneNum(rmsg.getSmsRecvnum());
			}
			if(null!=person){
				String userid = person.getUserId();
				User toauser = user.getUserInfoByUserId(userid);
				if(!isExists(rmsg.getSmsRecvnum())){
					if(toauser!=null){
						unreplyPerson=unreplyPerson+"<font color=red  style=\"font-weight:bold\">"+toauser.getUserName()+"</font>,";
					}else{
						unreplyPerson=unreplyPerson+"<font color=red  style=\"font-weight:bold\">"+rmsg.getSmsRecvnum()+"</font>,";
					}
				}
			}else{
				if(!isExists(rmsg.getSmsRecvnum())){
					unreplyPerson=unreplyPerson+"<font color=red  style=\"font-weight:bold\">"+rmsg.getSmsRecvnum()+"</font>,";
				}
			}
		}
		hasreply=replyList.size();
		unreply=smsList.size()-replyList.size();
		return "smsReplyView";
	}

	public String getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}

	public ToaSmsRep getSmsrep() {
		return smsrep;
	}

	public void setSmsrep(ToaSmsRep smsrep) {
		this.smsrep = smsrep;
	}
}
