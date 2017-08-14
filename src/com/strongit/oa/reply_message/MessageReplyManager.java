/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息业务类
 */
package com.strongit.oa.reply_message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.bo.ToaMobileReplyMessage;
import com.strongit.oa.bo.ToaMsgStateMean;
import com.strongit.oa.message.MessageManager;
import com.strongit.oa.message.MessageStateMeanManager;
import com.strongit.oa.sms.SmsManager;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.vote.AnswerManager;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
@Service
@Transactional
public class MessageReplyManager {
	
	private GenericDAOHibernate<ToaMobileReplyMessage, java.lang.String> messageDao;
	
	private MessageManager messageManager;
	
	private AnswerManager answerManager;

	private MessageStateMeanManager stateMeanManager;
	
	@Autowired private SmsManager smsManager;
	
	public MessageReplyManager() {

	}

	/**
	 * author:luosy
	 * description: 设置messageDao
	 * modifyer:
	 * description:
	 * @param session
	 */
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		messageDao = new GenericDAOHibernate<ToaMobileReplyMessage, java.lang.String>(
				session, ToaMobileReplyMessage.class);
	}
	
	@Autowired
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
	@Autowired
	public void setStateMeanManager(MessageStateMeanManager stateMeanManager) {
		this.stateMeanManager = stateMeanManager;
	}
	@Autowired
	public void setAnswerManager(AnswerManager answerManager) {
		this.answerManager = answerManager;
	}
	
	public List<ToaMobileReplyMessage> getReplayList(String module , String messageId){
		return messageDao.find("from ToaMobileReplyMessage t where t.module=? and t.messageId=?", module,messageId);
	}
	
	public void save(ToaMobileReplyMessage sms){
		messageDao.save(sms);
	}
	
	public boolean isSavedsms(String num,String moduleCode,String increaseCode){
		List l = messageDao.find("from ToaMobileReplyMessage t where t.mobileNumber=? and t.module=? and t.messageId=?", num,moduleCode,increaseCode);
		if(null!=l){
			if(l.size()>0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * author:luosy
	 * description:  保存用户回复的
	 * modifyer:
	 * description:
	 * @param phoneNum 手机号码
	 * @param repTime   回复时间
	 * @param moduleCode	模块编码
	 * @param increaseCode 自增码
	 * @param repCon	回复内容
	 * @param replyUser	回复用户
	 * @return  返回 false 表示短信未保存过，执行保存操作
	 * 		<br>  返回 true  表示短信已经保存过，不再执行保存操作
	 * @throws ServiceException
	 */
	public boolean saveReplySms(String phoneNum,Date repTime,String moduleCode,String increaseCode,String repCon,String replyUser) throws ServiceException{
		try{
			ToaMobileReplyMessage repSms = new ToaMobileReplyMessage();
			repSms.setMobileNumber(phoneNum);	//手机号码
			repSms.setReplyTime(repTime);	//回复时间
			repSms.setModule(moduleCode);	//模块ID
			repSms.setMessageId(increaseCode);//自增编码
			repSms.setReplyUser(replyUser);
			
			
			if(moduleCode.equals(GlobalBaseData.SMSCODE_MESSAGE)){ 				//我的消息模块
				if(this.isSavedsms(phoneNum,moduleCode,increaseCode)){
					return true;
				}
				ToaMessage message = messageManager.getMsgByModuleCode(moduleCode, increaseCode);
				if(null!=message){
					String smsRepCon = repCon;
					List means = stateMeanManager.getMeanListByMsgId(message.getMsgId());
					for (int j=0;j<means.size();j++){
						ToaMsgStateMean mean = (ToaMsgStateMean)means.get(j);
						if(repCon.equals(mean.getMsgState())){
							smsRepCon = mean.getMsgStateMean();
						}
					}
					if(!smsRepCon.equals(repCon)){
						repCon = smsRepCon;
					}
				}
			}else if(moduleCode.equals(GlobalBaseData.SMSCODE_SURVEY)){ 		//投票调查模块
				repCon = moduleCode+increaseCode+repCon;
				if(!answerManager.submitVoteBySMS(replyUser,phoneNum,repCon)){  //投票不成功
					return true;
				}
			}else{
				String smsRepCon = repCon;
				List means = stateMeanManager.getMeanListByModelCode(moduleCode, increaseCode);
				for (int j=0;j<means.size();j++){
					ToaMsgStateMean mean = (ToaMsgStateMean)means.get(j);
					if(repCon.equals(mean.getMsgState())){
						smsRepCon = mean.getMsgStateMean();
					}
				}
				if(!smsRepCon.equals(repCon)){
					repCon = smsRepCon;
				}
			}
			
			repSms.setReplyContent(repCon);
			messageDao.save(repSms);
			return false;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存手机回复短信"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存手机回复短信"});
		}
		
	}
	
}
