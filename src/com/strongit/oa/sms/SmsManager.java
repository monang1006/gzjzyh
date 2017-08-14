/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理手机短信manager类
 */
package com.strongit.oa.sms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongmvc.exception.AjaxException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaMsgStateMean;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.bo.ToaSms;
import com.strongit.oa.bo.ToaSmsRep;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.message.MessageStateMeanManager;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.sms.util.RuleTemplate;
import com.strongit.oa.sms.util.SMBuildEntry;
import com.strongit.oa.smsplatform.ModelStateMeanManager;
import com.strongit.oa.smsplatform.SmsPlatformManager;
import com.strongit.oa.updatebadwords.phrasefilter.IPhraseFilterManage;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;

/**
 * 处理手机短信manager类
 * @Create Date: 2009-2-13
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
@OALogger
public class SmsManager {
	private GenericDAOHibernate<ToaSms, java.lang.String> smsDao;

	private GenericDAOHibernate<ToaSmsRep, java.lang.String> smsRepDao;
	
	private IUserService userService;
	
	private IPhraseFilterManage filterManager;

	private IsmsEngineService smsEngineService;
	
	private MyInfoManager myInfoManager;
	
	private SmsPlatformManager smsPlatformManager;

	//手机回复意义manager类
	private ModelStateMeanManager stateMeanManager;

	//手机短信回复意义manager类
	private MessageStateMeanManager msgStateMeanManager;
	
	public SmsManager() {

	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setFilterManager(IPhraseFilterManage filterManager) {
		this.filterManager = filterManager;
	}
	
	@Autowired
	public void setMyInfoManager(MyInfoManager myInfoManager) {
		this.myInfoManager = myInfoManager;
	}
	
	@Autowired
	public void setSmsEngineService(IsmsEngineService smsEngineService) {
		this.smsEngineService = smsEngineService;
	}
	
	@Autowired
	public void setSmsPlatformManager(SmsPlatformManager smsPlatformManager) {
		this.smsPlatformManager = smsPlatformManager;
	}
	
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		smsDao = new GenericDAOHibernate<ToaSms, java.lang.String>(session,
				ToaSms.class);
		smsRepDao = new GenericDAOHibernate<ToaSmsRep, java.lang.String>(session,
				ToaSmsRep.class);
		
	}
	
	@Autowired
	public void setStateMeanManager(ModelStateMeanManager stateMeanManager) {
		this.stateMeanManager = stateMeanManager;
	}
	
	@Autowired
	public void setMsgStateMeanManager(MessageStateMeanManager msgStateMeanManager) {
		this.msgStateMeanManager = msgStateMeanManager;
	}
	
	/**
	 * author:luosy
	 * description:获取所有记录 按时间排序
	 * modifyer:
	 * description:
	 * @param page
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<ToaSms> getAllList(Page<ToaSms> page) throws SystemException,ServiceException{
		try{
			return smsDao.find(page, "from ToaSms t where t.smsIsdel=? order by t.smsSendTime desc ",ToaSms.SMS_NOTDEL);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信记录列表"});
		}
	}

	/**
	 * author:luosy
	 * description:获取对应模块编码的短信列表
	 * modifyer:
	 * description:
	 * @param modelCode 模块ID
	 * @param increaseCode 自增长编码
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ToaSms> getReceiverList(String modelCode,String increaseCode){
		return smsDao.find("from ToaSms t where t.modelCode=? and t.increaseCode=?", modelCode,increaseCode);
	}
	
	/**
	 * author:luosy
	 * description: 获取当前用户所发送的记录 按时间排序
	 * modifyer:
	 * description:
	 * @param page
	 * @return
	 */
	public Page<ToaSms> getListByUser(Page<ToaSms> page) throws SystemException,ServiceException{
		try{
			String user = userService.getCurrentUser().getUserId();
			return smsDao.find(page, "from ToaSms t where t.smsSendUser=? and t.smsIsdel=? order by t.smsSendTime desc ", user,ToaSms.SMS_NOTDEL);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信记录列表"});
		}
	}

	/**
	 * author:luosy
	 * description:根据ID获取记录
	 * modifyer:
	 * description:
	 * @param smsId
	 * @return
	 */
	public ToaSms getSmsById(String smsId) throws SystemException,ServiceException{
		try{
			return (ToaSms) smsDao.findById(smsId, true);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信记录"});
		}
	}

	/**
	 * author:luosy
	 * description: 发送手机短信 外部调用
	 * modifyer:
	 * description:
	 * @param flag 发送标识 system/person
	 * @param recvUser 接受者ID list
	 * @param smscontent 短信内容
	 * @return 发送结果
	 */
	public String sendSmsByImpl(String flag,List recvUser,String smsContent, String code)throws SystemException,ServiceException{
		try{
			//词语过滤
			smsContent=filterManager.filterPhrase(smsContent, "402882271e1f7caf011e1f818cc30003");
			
			ToaSms toasms = new ToaSms();
			toasms.setModelCode(code);
			ToaBussinessModulePara toabp = smsPlatformManager.getObjByCode(code);
			if(null!=toabp){
				toasms.setModelName(toabp.getBussinessModuleName());
			}
			
			//发送内容
			toasms.setSmsCon(smsContent);
			//发送者
			if(flag==null|"system".equals(flag)){
				toasms.setSmsSendUser("system");
				toasms.setSmsSenderName("系统发送");
			}else{
				String sendUserId = userService.getCurrentUser().getUserId();
				String smsSenderName = userService.getCurrentUser().getUserName();
				toasms.setSmsSendUser(sendUserId);
				toasms.setSmsSenderName(smsSenderName);
			}
			
			// 未指定发送时间 则即刻发送
			if (toasms.getSmsSendDelay() == null) {
				Calendar rightNow = Calendar.getInstance();
				toasms.setSmsSendTime(rightNow.getTime());
				toasms.setSmsState(ToaSms.SMS_UNSEND);// 未发送
				toasms.setSmsServerRet(ToaSms.SERVICE_RET_UNSEND);
			} else {
				toasms.setSmsSendTime(toasms.getSmsSendDelay());
				toasms.setSmsState(ToaSms.SMS_UNSEND);// 未发送
				toasms.setSmsServerRet(ToaSms.SERVICE_RET_UNSEND);
			}
			
//			ToaSms newSms =null;
			//保存记录到数据库
			for(int i=0;i<recvUser.size();i++){
				String recverId = recvUser.get(i).toString();
				
				ToaSms newSms = new ToaSms();
//				String recvNum = userService.getUserInfoByUserId(recverId).getUserTel() ;//接受号码
				ToaPersonalInfo personInfo = myInfoManager.getInfoByUserid(recverId);
				String recvNum="";
				if(!"".equals(personInfo)&&null!=personInfo){
					recvNum = myInfoManager.getInfoByUserid(recverId).getPrsnMobile1();
				}
				
				newSms.setSmsSendUser(toasms.getSmsSendUser());
				newSms.setSmsCon(toasms.getSmsCon());
				newSms.setSmsRecver(userService.getUserInfoByUserId(recverId).getUserName());
				newSms.setSmsRecvId(recverId);
				newSms.setSmsRecvnum(recvNum);
				newSms.setSmsSendTime(toasms.getSmsSendTime());
				newSms.setSmsSenderName(toasms.getSmsSenderName());
				newSms.setModelCode(toasms.getModelCode());
				newSms.setModelName(toasms.getModelName());
				
				newSms.setSmsState(toasms.getSmsState());
				newSms.setSmsServerRet(toasms.getSmsServerRet());
				newSms.setSmsIsdel(ToaSms.SMS_NOTDEL);
				newSms.setSmsId(null);
				
				//smsDao.save(newSms);
				this.saveSms(newSms);
				
			}
			
			return "";
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"短信发送"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"短信发送"});
		}
	}
	
	/**
	 * author:luosy
	 * description:调用短信猫发送短信
	 * modifyer:
	 * description:
	 * @param recvnum 手机号码
	 * @param smscontent 短信内容
	 * @return 发送回执（返回状态）
	 */
	public String sendSms(ToaSms sms) throws SystemException,ServiceException{
		try{
//		 调用短信发送
			String ret = "";
			try {
				ret = smsEngineService.sendSingleSms(sms);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ret;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"短信发送"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 保存短信记录 并调用短信发送接口
	 * modifyer:
	 * description:
	 * @param recvUserIds  手机短信接受者id 多个用逗号分隔
	 * @param toasms 短信记录对象
	 * @return
	 */
	public String saveSms(String recvUserIds,ToaSms toasms,String addNum, OALogInfo ... loginfos)throws SystemException,ServiceException{
		try{
//			词语过滤
			toasms.setSmsCon(filterManager.filterPhrase(toasms.getSmsCon(), "402882271e1f7caf011e1f818cc30003"));
			
			// 获得用户信息
			String sendUserId = userService.getCurrentUser().getUserId();
			String smsSenderName = userService.getCurrentUser().getUserName();
			toasms.setSmsSendUser(sendUserId);
			
			// 未指定发送时间 则即刻发送
			if (toasms.getSmsSendDelay() == null) {
				Calendar rightNow = Calendar.getInstance();
				toasms.setSmsSendTime(rightNow.getTime());
				toasms.setSmsState(ToaSms.SMS_UNSEND);// 未发送
				toasms.setSmsServerRet(ToaSms.SERVICE_RET_UNSEND);
			} else {
				toasms.setSmsSendTime(toasms.getSmsSendDelay());
				toasms.setSmsState(ToaSms.SMS_UNSEND);// 未发送
				toasms.setSmsServerRet(ToaSms.SERVICE_RET_UNSEND);
			}
			
//			保存系统人员记录到数据库
			if(!"".equals(recvUserIds)&&null!=recvUserIds){
				String temp[]=recvUserIds.split(",");
				ToaSms newSms = null;
				for(int i=0;i<temp.length;i++){
					newSms = new ToaSms();
//				String recvNum = userService.getUserInfoByUserId(temp[i]).getUserTel() ;
					//ToaPersonalInfo personInfo = myInfoManager.getInfoByUserid(temp[i]);
					User user = userService.getUserInfoByUserId(temp[i]);
					String recvNum="";
					
					/*if(!"".equals(personInfo)&&null!=personInfo){
						recvNum = myInfoManager.getInfoByUserid(temp[i]).getPrsnMobile1();
					}*/
					if(!"".equals(user)&&null!=user){
						//recvNum = myInfoManager.getInfoByUserid(temp[i]).getPrsnMobile1();
						recvNum = user.getRest2();//南航项目修改
					}
					
					newSms.setSmsCon(toasms.getSmsCon());
	/*短信前 加“尊敬的×××先生/女士”
					String recvSmsCon="";
					if(!"".equals(personInfo)&&null!=personInfo){
						recvNum = personInfo.getPrsnMobile1();
						
						String froCon = "尊敬的"+personInfo.getPrsnName();
						if("1".equals(personInfo.getPrsnSex())){
							froCon += "女士：";
						}else{
							froCon += "先生：";
						}
						recvSmsCon = froCon+toasms.getSmsCon();
					}else{
						recvSmsCon = toasms.getSmsCon();
					}
					newSms.setSmsCon(recvSmsCon);
	*/				
					
					newSms.setSmsRecver(userService.getUserInfoByUserId(temp[i]).getUserName());
					newSms.setSmsRecvId(temp[i]);
					newSms.setSmsRecvnum(recvNum);
					newSms.setSmsSendTime(toasms.getSmsSendTime());
					newSms.setSmsSendUser(toasms.getSmsSendUser());
					newSms.setSmsState(toasms.getSmsState());
					newSms.setSmsServerRet(toasms.getSmsServerRet());
					newSms.setIncreaseCode(toasms.getIncreaseCode());
					newSms.setModelCode(toasms.getModelCode());
					newSms.setSmsIsdel(ToaSms.SMS_NOTDEL);
					newSms.setModelName(toasms.getModelName());
					newSms.setSmsSenderName(smsSenderName);
					
					newSms.setSmsId(null);
					
					//smsDao.save(newSms);
					this.saveSms(newSms);
					
				}
			}
			
			//手录号码的短信发送
			if(!"".equals(addNum)&&null!=addNum){
				String[] num = addNum.split(",");
				ToaSms newSms = null;
				for(int i=0;i<num.length;i++){
					newSms = new ToaSms();
					
					newSms.setSmsCon(toasms.getSmsCon());
					newSms.setSmsRecver(num[i]);
					newSms.setSmsRecvnum(num[i]);
					newSms.setSmsSendTime(toasms.getSmsSendTime());
					newSms.setSmsSendUser(toasms.getSmsSendUser());
					newSms.setSmsState(toasms.getSmsState());
					newSms.setSmsServerRet(toasms.getSmsServerRet());
					newSms.setSmsIsdel(ToaSms.SMS_NOTDEL);
					newSms.setIncreaseCode(toasms.getIncreaseCode());
					newSms.setModelCode(toasms.getModelCode());
					newSms.setModelName(toasms.getModelName());
					newSms.setSmsSenderName(smsSenderName);
					newSms.setSmsId(null);
					
					//smsDao.save(newSms);
					this.saveSms(newSms);
					
				}
			}
			return "";
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"短信发送,保存"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"短信发送,保存"});
		}
	}

	/**
	 * author:luosy
	 * description:删除一条或多条记录
	 * modifyer:
	 * description:
	 * @param smsId
	 */
	public void delSms(ToaSms sms, OALogInfo ... loginfos) throws SystemException,ServiceException,AjaxException{
		try{
			sms.setSmsIsdel(ToaSms.SMS_DEL);
			smsDao.save(sms);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除短信记录"});
		}catch(AjaxException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除短信记录"});
		}
	}

	/**
	 * author:luosy
	 * description:更新短信对象
	 * modifyer:
	 * description:
	 * @param sms
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void updateSms(ToaSms sms)throws SystemException,ServiceException{
		try{
			smsDao.update(sms);
		}catch(ServiceException e){
		throw new ServiceException(MessagesConst.del_error,               
				new Object[] {"删除短信记录"});
		}
	}
	
	/**
	 * author:luosy
	 * description:按条件搜索所有记录
	 * modifyer:
	 * description:
	 * @param page
	 * @param toasms 
	 * @return page
	 */
	@Transactional(readOnly=true)
	public Page<ToaSms> searchList(Page<ToaSms> page, ToaSms toasms,String inputType) throws SystemException,ServiceException{
		try{
			StringBuffer hql = new StringBuffer();
			hql.append("from ToaSms t where t.smsIsdel=?");
			Object[] obj = new Object[10];
			int i = 1;
			obj[0] = ToaSms.SMS_NOTDEL;
			
			//判断是个人记录还是所有记录
			if("list".equals(inputType)){
				String UserId = userService.getCurrentUser().getUserId();
				hql.append(" and t.smsSendUser='"+UserId+"'");
			}
			
//			 业务类型
			String modelName = toasms.getModelName();
			if (!"".equals(modelName) && modelName != null) {
				hql.append(" and t.modelName like ? ");
				obj[i] = "%" + modelName + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			// 接收者
			String smsRecver = toasms.getSmsRecver();
			if (!"".equals(smsRecver) && smsRecver != null) {
				hql.append(" and t.smsRecver like ? ");
				obj[i] = "%" + smsRecver + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			// 接受者 手机号
			String smsRecvnum = toasms.getSmsRecvnum();
			if (!"".equals(smsRecvnum) && smsRecvnum != null) {
				hql.append(" and t.smsRecvnum like ? ");
				obj[i] = "%" + smsRecvnum + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			// 短信内容
			String smsCon = toasms.getSmsCon();
			if (!"".equals(smsCon) && smsCon != null) {
				hql.append(" and t.smsCon like ? ");
				obj[i] = "%" + smsCon + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			// 发送状态
			String smsState = toasms.getSmsState();
			if (!"".equals(smsState) && smsState != null) {
				hql.append(" and t.smsState like ? ");
				obj[i] = "%" + smsState + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			// 返回状态
			String smsServerRet = toasms.getSmsServerRet();
			if (!"".equals(smsServerRet) && smsServerRet != null) {
				hql.append(" and t.smsServerRet like ? ");
				obj[i] = "%" + smsServerRet + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			// 发送时间
			Date smsSendTime = toasms.getSmsSendTime();
			if (smsSendTime != null && !"".equals(smsSendTime)) {
				Calendar calendar =Calendar.getInstance();
				calendar.setTime(smsSendTime);
				calendar.set(Calendar.HOUR_OF_DAY,23);
				calendar.set(Calendar.MINUTE,59);
				hql.append(" and ( (t.smsSendTime >= ? and t.smsSendTime<= ?) or t.smsSendTime is null )");
				obj[i] = smsSendTime;
				i++;
				obj[i] = calendar.getTime();
				i++;
			} else {
				hql.append(" and 1= ? and 1=?");
				obj[i] = 1;
				i++;
				obj[i] = 1;
				i++;
			}
			
			//  发送人
			String smsSenderName = toasms.getSmsSenderName();
			if (!"".equals(smsSenderName) && smsSenderName != null) {
				hql.append(" and t.smsSenderName like ? ");
				obj[i] = "%" + smsSenderName + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			page = smsDao.find(page, hql.toString()+" order by t.smsSendTime desc ", obj);
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"搜索短信记录"});
		}
	}

	/**
	 * author:luosy
	 * description: 获取需要发送的短信列表
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public List<ToaSms> getUnSendedSmsList() throws SystemException,ServiceException{
		try{
//			return smsDao.find("from ToaSms as t where t.smsState=?  ",ToaSms.SMS_UNSEND);
			return smsDao.find("from ToaSms as t where t.smsState=? and t.smsSendTime<? and t.smsIsdel=?",ToaSms.SMS_UNSEND,new Date(),ToaSms.SMS_NOTDEL);
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未发送的短信"});
		}
		
	}
	
	/**
	 * @author 张磊
	 * @description: 获取当天已发送的短信列表
	 * @return List
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaSms> getSendedListByDate(Date date) throws SystemException,ServiceException{
		try {	//to_date(?,'yyyy-mm-dd hh24:mi;ss')
			return smsDao.find("from ToaSms as t where t.smsState=? and t.smsSendTime=? and t.smsIsdel=?",ToaSms.SMS_UNSEND, date, ToaSms.SMS_NOTDEL);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "查找未发送的短信" });
		}
		
	}

	/**
	 * @author:luosy
	 * @description:	设置超出发送范围的短信延时发送
	 * @date : 2010-11-1
	 * @modifyer:
	 * @description:
	 * @param list
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaSms> delaySendSms(List<ToaSms> list)throws SystemException,ServiceException{
		try{
			List<ToaSms> delayList = new ArrayList<ToaSms>();
			if(list.size()>80){
				delayList = list.subList(80, list.size());		//得到超出80条的短信
				list = list.subList(0, 80);					//返回要发送的80条短信
			}
			
			if(delayList!=null&&delayList.size()>0){
				for(int i=0;i<delayList.size();i++){
					ToaSms sms = delayList.get(i);
					
					Calendar cal = Calendar.getInstance();
//					cal.setTime(sms.getSmsSendTime());
					if(cal.getTime().getTime()>sms.getSmsSendTime().getTime()){
						cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)+1);	//延后1小时 用来发送短信
						cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+30);	//延后30分钟 短信猫休息
						sms.setSmsSendTime(cal.getTime());
						smsDao.save(sms);
						smsDao.flush();
						
					}
				}
			}
			return list;
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未发送的短信"});
		}
		
	}
	/**
	 * author:luosy
	 * description: 发送单条短信 根据短信猫配置的手机号码
	 * modifyer:
	 * description:
	 * @param smsNum 接受号码
	 * @param smsText 短信内容
	 * @param simNum	短信猫配置的手机号码
	 * @return 发送状态
	 * @throws Exception 
	 */
	public String sendSingleSmsBySimNum(String smsNum, String smsText, String simNum, OALogInfo ... loginfos)throws SystemException,ServiceException{
		try{
			return smsEngineService.sendSingleSmsBySimNum(smsNum, smsText, simNum);
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未发送的短信"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未发送的短信"});
		}
	}
	
	
	/**
	 * author:luosy
	 * description: 发送手机短信 webService调用
	 * modifyer:
	 * description:
	 * @param flag 发送标识 system/person
	 * @param recvUser 接受者ID list
	 * @param smscontent 短信内容
	 * @return 发送结果
	 */
	public String sendSmsByWS(String sender,String recvNum,String smsContent, String code)throws SystemException,ServiceException,Exception{
		try{
			//词语过滤
			smsContent=filterManager.filterPhrase(smsContent, "402882271e1f7caf011e1f818cc30003");
			//模块信息 
			ToaBussinessModulePara toabp = smsPlatformManager.getObjByCode(code);
			String modelName = "";
			if(null!=toabp){
				modelName =  toabp.getBussinessModuleName();
			}else{
				return "没有配置短信平台";
			}
			
			//发送用户
			User senderUser = userService.getUserInfoByUserId(sender);
			String senderName = "";
			if(null==senderUser || senderUser.getUserId()==null){
				senderName = sender;
			}else{
				senderName = senderUser.getUserId();
			}
			
			
//			收录号码的短信发送
			if(!"".equals(recvNum)&&null!=recvNum){
				String[] num = recvNum.split(",");
				ToaSms newSms = null;
				for(int i=0;i<num.length;i++){
					String userName = num[i];
					ToaPersonalInfo perInfo = myInfoManager.getInfoByTelephoneNum(num[i]);
					if(null!=perInfo){	//如果根据手机号码在系统中找的到用户
						userName = perInfo.getPrsnName(); //回复的用户名
    				}
					newSms = new ToaSms();
					newSms.setSmsCon(smsContent);
					newSms.setSmsRecver(userName);
					newSms.setSmsRecvnum(num[i]);
					newSms.setSmsSendTime(Calendar.getInstance().getTime());
					newSms.setSmsSendDelay(Calendar.getInstance().getTime());
					newSms.setSmsSendUser(sender);
					newSms.setSmsState(ToaSms.SMS_UNSEND);
					newSms.setSmsServerRet(ToaSms.SERVICE_RET_UNSEND);
					newSms.setSmsIsdel(ToaSms.SMS_NOTDEL);
					newSms.setIncreaseCode("");
					newSms.setModelCode(code);
					newSms.setModelName(modelName);
					newSms.setSmsSenderName(senderName);
					newSms.setSmsId(null);
					
					//smsDao.save(newSms);
					this.saveSms(newSms);
				}
			}else{
				return "短信接收号码为空";
			}
			
			return "success";
		}catch(Exception e){
			throw new Exception();
		}
	}
	
	
	/**
	 * author:luosy
	 * description:按条件搜索所有记录
	 * modifyer:
	 * description:
	 * @param page
	 * @param toasms 
	 * @return page
	 */
	@Transactional(readOnly = true)
	public List<ToaSms> getSmsListByWS(String sender, String recvNum, String smsCon
								, String moduleId) throws SystemException, ServiceException, Exception {
		try{
			StringBuffer hql = new StringBuffer();
			hql.append("from ToaSms t where t.smsIsdel=?");
			Object[] obj = new Object[5];
			int i = 1;
			obj[0] = ToaSms.SMS_NOTDEL;
			
			//模块id
			if (!"".equals(moduleId) && moduleId != null) {
				hql.append(" and t.modelCode like ? ");
				obj[i] = "%" + moduleId + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			// 接受者 手机号
			if (!"".equals(recvNum) && recvNum != null) {
				hql.append(" and t.smsRecvnum like ? ");
				obj[i] = "%" + recvNum + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			// 短信内容
			if (!"".equals(smsCon) && smsCon != null) {
				hql.append(" and t.smsCon like ? ");
				obj[i] = "%" + smsCon + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			//  发送人
			if (!"".equals(sender) && sender != null) {
				hql.append(" and t.smsSenderName like ? ");
				obj[i] = "%" + sender + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			return smsDao.find(hql.toString()+" order by t.smsSendTime desc ", obj);
		}catch(Exception e){
			throw new Exception();
		}
	}
	
	/**
	 * author:luosy
	 * description:获取短信平台信息  修改短信发送时间
	 * modifyer:
	 * description:
	 * @param code
	 * @param sendTime
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws Exception
	 */
	public ToaSms getSendTimeBySmsplatform(ToaSms toasms)throws SystemException, ServiceException, Exception {
		try{
			if("".equals(toasms.getModelCode())||null==toasms.getModelCode()){
				return toasms;
			}
			
			ToaBussinessModulePara bmp = smsPlatformManager.getObjByCode(toasms.getModelCode());
			if(!"1".equals(bmp.getIsEnable())){
				return toasms;
			}
//			定时发送
			String timingType = bmp.getTimingType();
			if("0".equals(timingType)){
				//每天定时发送
				Calendar now = Calendar.getInstance();
				Calendar sendDelay = Calendar.getInstance();
				sendDelay.setTime(bmp.getTimingDelay()); 
				sendDelay.set(Calendar.YEAR, now.get(Calendar.YEAR));
				sendDelay.set(Calendar.MONTH, now.get(Calendar.MONTH));
				sendDelay.set(Calendar.DATE, now.get(Calendar.DATE));
				
				if(sendDelay.getTime().getTime()<now.getTime().getTime()){
					sendDelay.set(Calendar.DATE, now.get(Calendar.DATE)+1);
				}
					
				toasms.setSmsSendTime(sendDelay.getTime());
			}else if("1".equals(timingType)){
				//每月定时发送
				Calendar now = Calendar.getInstance();
				Calendar sendDelay = Calendar.getInstance();
				sendDelay.setTime(bmp.getTimingDelay()); 
				sendDelay.set(Calendar.YEAR, now.get(Calendar.YEAR));
				sendDelay.set(Calendar.MONTH, now.get(Calendar.MONTH));
				
				if(sendDelay.getTime().getTime()<now.getTime().getTime()){
					sendDelay.set(Calendar.MONTH, now.get(Calendar.MONTH)+1);
				}
				
				toasms.setSmsSendTime(sendDelay.getTime());
			}
			return toasms;
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据短信平台调整短信发送时间"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据短信平台调整短信发送时间"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 选择短信通道发送短信
	 * modifyer:
	 * description:
	 * @param toasms
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws Exception
	 */
	public void saveSms(ToaSms toasms)throws SystemException, ServiceException, Exception {
		try{
			toasms = this.getSendTimeBySmsplatform(toasms);
			RuleTemplate t=new RuleTemplate();
			if(toasms.getSmsRecvnum()!=null){
				if(toasms.getSmsRecvnum().equals("")){
					toasms.setSmsState(ToaSms.SMS_SEND);
					toasms.setSmsServerRet(ToaSms.SERVICE_RET_FAILURE);
					smsDao.save(toasms);
				}else{
					int rule=t.SMSchannel(toasms.getSmsRecvnum());//getSmsRecver());
		
					if(rule==1){//中国移动号码 走移动网关
						try{
							SMBuildEntry.sendSMS(toasms.getSmsRecvnum(),toasms.getSmsCon());
							
								toasms.setSmsState(ToaSms.SMS_SEND);
								toasms.setSmsServerRet(ToaSms.SERVICE_RET_SUCCESS);
								smsDao.save(toasms);
							
							}catch(Exception ex){
								toasms.setSmsState(ToaSms.SMS_SEND);
								toasms.setSmsServerRet(ToaSms.SERVICE_RET_FAILURE);
								smsDao.save(toasms);
							}
							System.out.println("移动网关(建设厅)发出："+toasms.getSmsRecver()+":"+t.con);
					}else if(rule==10){
						try{
							SMBuildEntry.sendSMS(toasms.getSmsRecvnum(),toasms.getSmsCon());
						
							toasms.setSmsState(ToaSms.SMS_SEND);
							toasms.setSmsServerRet(ToaSms.SERVICE_RET_SUCCESS);
							smsDao.save(toasms);
						
						}catch(Exception ex){
							toasms.setSmsState(ToaSms.SMS_SEND);
							toasms.setSmsServerRet(ToaSms.SERVICE_RET_FAILURE);
							smsDao.save(toasms);
						}
						System.out.println("移动网关(建设厅)发出："+toasms.getSmsRecvnum()+":"+t.con);
					}else if(rule==2){//联通网关
						System.out.println("联通网关发出："+toasms.getSmsRecver()+":"+t.con);
					}else if(rule==3){//电信网关
						System.out.println("电信网关发出："+toasms.getSmsRecver()+":"+t.con);
					}else if(rule==4){//短信猫网关
						System.out.println("短信猫网关发出："+toasms.getSmsRecver()+":"+t.con);
						smsDao.save(toasms);
					}else{
						System.out.println("什么网关都没有找到，发送失败");
						toasms.setSmsState(ToaSms.SMS_SEND);
						toasms.setSmsServerRet(ToaSms.SERVICE_RET_FAILURE);
						smsDao.save(toasms);
					}
				}
			}else{
				toasms.setSmsState(ToaSms.SMS_SEND);
				toasms.setSmsServerRet(ToaSms.SERVICE_RET_FAILURE);
				smsDao.save(toasms);
			}
		}catch(Exception e){
			e.printStackTrace();
			//throw new Exception();
			toasms.setSmsState(ToaSms.SMS_SEND);
			toasms.setSmsServerRet(ToaSms.SERVICE_RET_FAILURE);
			smsDao.save(toasms);
		}
	}
	
	/**
	 * author:luosy
	 * description: 获取某个模块的短信默认回复答案
	 * modifyer:
	 * description:
	 * @param modulId 模块ID
	 * @return
	 */
	public List getModulStateMean(String modulId){
		return stateMeanManager.getObjsByPar(modulId);
	}
	
	/**
	 * author:luosy
	 * description: 获取短信回复答案
	 * modifyer:
	 * description:
	 * @param modulId 模块ID
	 * @return
	 */
	public List getStateMean(String modulId,String increaseCode){
		return msgStateMeanManager.getMeanListByModelCode(modulId,increaseCode);
	}
	
	/**
	 * author:luosy
	 * description:根据模块Id 获取该模块的配置信息
	 * modifyer:
	 * description:
	 * @param modulId 模块Id
	 * @return
	 */
	public ToaBussinessModulePara getModuleParaByCode(String modulId){
		return smsPlatformManager.getObjByCode(modulId);
	}
	
	/**
	 * @author:luosy
	 * @description: 给需要回复的短信添加模块编码及回复内容
	 * @date : 2010-11-12
	 * @modifyer:
	 * @description:
	 * @param model
	 * @param means
	 * @param moduleCode
	 * @return
	 */
	public synchronized ToaSms setSmsRep(ToaSms model,List means,String moduleCode)throws SystemException, ServiceException, Exception {
		try{
			//获取自增长
			String  increaseCode = smsPlatformManager.getFullCode(moduleCode);
			if("toolong".equals(increaseCode)){
				//return "toolong";
				System.out.println("超出短信自增长位数");
			}
			
			String smsCon = model.getSmsCon();
			StringBuffer sb = new StringBuffer();
			sb.append("");
			//保存消息对应的短信回复答案
			for(int i=0;i<means.size();i++){
				String mean = means.get(i).toString();
				if(!"".equals(mean)&&null!=mean){
					ToaMsgStateMean msm = new ToaMsgStateMean();
					msm.setMsgState(String.valueOf(i));
					msm.setMsgStateMean(mean);
					msm.setIncreaseCode(increaseCode);
					msm.setModelCode(moduleCode);
					msgStateMeanManager.saveMsgStateMean(msm);
					sb.append("").append(moduleCode).append(increaseCode).append(i).append(mean).append("，");
				}
			}
			//保存短信发送内容
			if(sb.toString().length()>1){
				String addCon = sb.toString().substring(0, sb.toString().length()-1);
				smsCon = smsCon+"\n回复:"+addCon+"";
				model.setSmsCon(smsCon);
				
				model.setIncreaseCode(increaseCode);
			}

			//自增长编码+1
			smsPlatformManager.updateMsgCode(moduleCode);
			return model;
			
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"给需要回复的短信添加模块编码及回复内容"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"给需要回复的短信添加模块编码及回复内容"});
		}
	}
	
	/**
	 * 判断是否是有效手机号码
	 * @date 2012-04-09 10:45
	 * @Author:hecj
	 * @param telephone
	 * @return true/false
	 */
	public boolean checkIsTel(String telephone){
		String reg="^\\d+$";
		Pattern pattern = Pattern.compile(reg);   
		if(telephone==null||"".equals(telephone)){
			return true;
		}
		String[] phones=telephone.split(",");
		if(phones==null||phones.length==0){
			phones=new String[1];
			phones[0]=telephone;
		}
		for(String phone:phones){
			if(phone==null||"".equals(phone)){
				return false;
			}
			Matcher matcher=pattern.matcher(phone);   
			boolean tem=matcher.matches();  
			if(!tem){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @author:luosy
	 * @description:  添加需要回复的短信 发送的记录
	 * @date : 2010-11-15
	 * @modifyer:
	 * @description:
	 * @param toasms
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws Exception
	 */
	public void saveSmsRep(ToaSms toasms)throws SystemException, ServiceException, Exception {
		try{
			// 未指定发送时间 则即刻发送
			if (toasms.getSmsSendDelay() == null) {
				Calendar rightNow = Calendar.getInstance();
				toasms.setSmsSendTime(rightNow.getTime());
			} else {
				toasms.setSmsSendTime(toasms.getSmsSendDelay());
			}
			
			ToaSmsRep smsrep = new ToaSmsRep();
			smsrep.setIncreaseCode(toasms.getIncreaseCode());
			smsrep.setModelCode(toasms.getModelCode());
			smsrep.setModelName(toasms.getModelName());
			smsrep.setSmsRepCon(toasms.getSmsCon());
			smsrep.setSmsRepSendTime(toasms.getSmsSendTime());
			smsrep.setSmsSenderName(userService.getCurrentUser().getUserName());
			smsrep.setSmsSendUser(userService.getCurrentUser().getUserId());

			smsRepDao.save(smsrep);
			
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"根据短信平台调整短信发送时间"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据短信平台调整短信发送时间"});
		}
	}
	
	
	public ToaSmsRep getCodeByBussinessId(String bussinessId) throws ServiceException{
		try{
			ToaSmsRep smsrep = smsRepDao.get(bussinessId);
			return smsrep;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存手机回复短信"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存手机回复短信"});
		}
		
	}
	
	/**
	 * author:luosy
	 * description:按条件搜索所有记录
	 * modifyer:
	 * description:
	 * @param page
	 * @param toasms 
	 * @return page
	 */
	@Transactional(readOnly=true)
	public Page<ToaSms> getRepList(Page<ToaSms> page, ToaSmsRep smsrep,String inputType) throws SystemException,ServiceException{
		try{
			StringBuffer hql = new StringBuffer();
			hql.append("select t.smsRepId, t.modelCode,t.increaseCode, t.smsRepCon,t.smsRepSendTime,t.smsSenderName,t.modelName from ToaSmsRep t where");
			Object[] obj = new Object[6];
			hql.append("  1=? ");
			obj[0]=1;
			int i = 1;
			
			//判断是个人记录还是所有记录
			if("list".equals(inputType)){
				String UserId = userService.getCurrentUser().getUserId();
				hql.append(" and  t.smsSendUser='"+UserId+"'");
			}
			
//			 业务类型
			String modelName = smsrep.getModelName();
			if (!"".equals(modelName) && modelName != null) {
				hql.append(" and t.modelName like ? ");
				obj[i] = "%" + modelName + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			// 接收者
			String smsSenderName = smsrep.getSmsSenderName();
			if (!"".equals(smsSenderName) && smsSenderName != null) {
				hql.append(" and t.smsSenderName like ? ");
				obj[i] = "%" + smsSenderName + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			// 短信内容
			String smsCon = smsrep.getSmsRepCon();
			if (!"".equals(smsCon) && smsCon != null) {
				hql.append(" and t.smsRepCon like ? ");
				obj[i] = "%" + smsCon + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			// 发送时间
			Date smsSendTime = smsrep.getSmsRepSendTime();
			if (smsSendTime != null && !"".equals(smsSendTime)) {
				Calendar calendar =Calendar.getInstance();
				calendar.setTime(smsSendTime);
				calendar.set(Calendar.HOUR_OF_DAY,23);
				calendar.set(Calendar.MINUTE,59);
				hql.append(" and ( (t.smsRepSendTime >= ? and t.smsRepSendTime<= ?) or t.smsRepSendTime is null )");
				obj[i] = smsSendTime;
				i++;
				obj[i] = calendar.getTime();
				i++;
			} else {
				hql.append(" and 1= ? and 1=?");
				obj[i] = 1;
				i++;
				obj[i] = 1;
				i++;
			}
			
			page = smsDao.find(page, hql.toString()+" order by t.smsRepSendTime desc ",obj);
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"搜索短信记录"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"搜索短信记录"});
		}
	}
}
