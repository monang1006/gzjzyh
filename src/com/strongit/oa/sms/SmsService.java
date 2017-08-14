/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：手机短信模块对外接口实现
 */
package com.strongit.oa.sms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.bo.ToaSms;
import com.strongit.oa.bo.ToaSmsCommConf;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.smscontrol.IsmscontrolService;
import com.strongit.oa.smsplatform.SmsPlatformManager;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 处理短信模块对外接口
 * @Create Date: 2009-2-13
 * @author luosy
 * @version 1.0
 */
@Service
public class SmsService implements IsmsService{
	
	private SmsManager smsManager;
	private IsmsEngineService smsEngine;
	private SmsPlatformManager smsPlatform;
	private IUserService userService;
	private IsmscontrolService smsControl;
	private SmsConfManager smsConf;
	private MyInfoManager myInfoManager;
	private SystemsetManager sysManager;

	@Autowired
	public void setSysManager(SystemsetManager sysManager) {
		this.sysManager = sysManager;
	}

	@Autowired
	public void setMyInfoManager(MyInfoManager myInfoManager) {
		this.myInfoManager = myInfoManager;
	}

	@Autowired
	public void setSmsManager(SmsManager smsManager) {
		this.smsManager = smsManager;
	}

	@Autowired
	public void setSmsEngine(IsmsEngineService smsEngine) {
		this.smsEngine = smsEngine;
	}
	
	@Autowired
	public void setSmsPlatform(SmsPlatformManager smsPlatform) {
		this.smsPlatform = smsPlatform;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setSmsControl(IsmscontrolService smsControl) {
		this.smsControl = smsControl;
	}
	
	@Autowired
	public void setSmsConf(SmsConfManager smsConf) {
		this.smsConf = smsConf;
	}
	/**
	 * author:luosy
	 * description: 提供给工作流的发送接口
	 * modifyer:
	 * description:
	 * @param flag 发送标识 system/person
	 * @param recvUser 接受者ID list
	 * @param smscontent 短信内容
	 * @return 发送结果
	 */
	public void sendSms(String flag,List recvUser,String smsContent )throws SystemException,ServiceException{
		try{
			if(this.isModuleParaOpen(GlobalBaseData.SMSCODE_WORKFLOW)){
				
				//工作流发送短信给领导时，同时发送给领导联系人
				List addUserIDs  = new ArrayList();
				String recverId = "";					
				String leaderConID = "";
				for(int i=0;i<recvUser.size();i++){
					recverId = recvUser.get(i).toString();					
					leaderConID = userService.getLDLxrId(recverId);
					if(null!=leaderConID && !"".equals(leaderConID)){
						leaderConID = leaderConID.substring(1,leaderConID.length());
						addUserIDs.add(leaderConID);
					}
				}
				recvUser.addAll(addUserIDs);
				smsManager.sendSmsByImpl(flag,recvUser, smsContent,GlobalBaseData.SMSCODE_WORKFLOW);
			}
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"短信发送"});
		}catch(Exception e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"短信发送"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 发送手机短信
	 * modifyer:
	 * description:
	 * @param flag 发送标识 system/person
	 * @param recvUser 接受者ID list
	 * @param smscontent 短信内容
	 * @param code 模块编码
	 * @return 发送结果
	 */
	public void sendSms(String flag,List recvUser,String smsContent,String code )throws SystemException,ServiceException{
		try{
			if(this.isModuleParaOpen(code)){
				smsManager.sendSmsByImpl(flag,recvUser, smsContent,code);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"短信发送"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"短信发送"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 提供给大蚂蚁的发送接口
	 * description: 提供给号码的发送接口 
	 * modifyer:
	 * description:
	 * @param ownerNum 接受人号码
	 * @param content 短信内容
	 * @param sender 发送人
	 * @param code   模块编码
	 * @return 发送结果
	 */
	public void sendSmsByAnt(String sender,String ownerNum,String content,String code){
		try{
			if(this.isModuleParaOpen(code)){
				smsManager.sendSmsByWS(sender,ownerNum,content, code);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"短信发送"});
		}catch(Exception e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"短信发送"});
		}
	}
	/**
	 * author:luosy
	 * description: 提供给消息模块的发送接口 
	 * modifyer:
	 * description:
	 * @param flag 发送标识 system/person
	 * @param recvUser 接受者ID list
	 * @param smscontent 短信内容
	 * @return 发送结果
	 */
	public void sendSmsByMessage(String ownerNum, String recvUser, ToaSms sms)throws SystemException,ServiceException{
		try{
			String modelCode =  sms.getModelCode();
			if(this.isModuleParaOpen(modelCode)){
				smsManager.saveSms(recvUser, sms, ownerNum, new OALogInfo("手机短信-消息模块-『发送』："+sms.getSmsCon()));
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"短信发送"});
		}
	}
	
	
	/**
	 * author:luosy
	 * description: 提供给号码的发送接口 
	 * modifyer:
	 * description:
	 * @param ownerNum 接受人号码
	 * @param content 短信内容
	 * @param sender 发送人
	 * @param code   模块编码
	 * @return 发送结果
	 */
	public void sendSmsByNum(String ownerNum, String content,String sender, String code)throws SystemException,ServiceException,Exception{
		try{
			if(this.isModuleParaOpen(code)){
				smsManager.sendSmsByWS(sender,ownerNum,content, code);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"短信发送"});
		}
	}
	/**
	 * author:luosy
	 * description:获取未发短信列表 
	 * modifyer:
	 * description:
	 * @return
	 */
	public List<ToaSms> getUnSendSmsList(){
		return smsManager.getUnSendedSmsList();
	}
	
	/**
	 * author:luosy
	 * description: 发送短信list
	 * modifyer:
	 * description:
	 * @param unsendlist
	 * @throws Exception
	 */
	public void sendSmsByEngine(List<ToaSms> unsendlist) throws Exception{
		try{
			if(unsendlist!=null){
				
				//短信数量过大时，将超出短信数量范围的短信发送时间延后
				//System.out.println("delaySendSms :"+unsendlist.size()+"(size)");
				//unsendlist = smsManager.delaySendSms(unsendlist);
				//System.out.println("send in this mins:"+unsendlist.size()+"(size)");
				
				if(unsendlist.size()>0){
					//发送短信
						Iterator<ToaSms> it = unsendlist.iterator();
						ToaSms sms = null;
						while (it.hasNext()) {
							sms = (ToaSms) it.next();
							smsEngine.sendSingleSms(sms);
							
						}
				}
				
				
			}
		}catch(Exception e){
			throw new Exception();
		}
	}
	
	/**
	 * author:luosy
	 * description:读取SIM卡中的短信
	 * modifyer:
	 * description:
	 * @throws Exception
	 */
	public void readSmsInSim()throws Exception {
		try{
			smsEngine.readSIMsms();
		}catch(Exception e){
			throw new Exception();
		}
	}
	
	/**
	 * author:luosy
	 * description:删除SIM卡中的短信
	 * modifyer:
	 * description:
	 * @throws Exception
	 */
	public void delSmsInSim()throws Exception {
		try{
			smsEngine.delSIMsms();
		}catch(Exception e){
			throw new Exception();
		}
	}
	
	
	/**
	 * author:luosy
	 * description:  该模块是否开启
	 * modifyer:
	 * description:
	 * @param code
	 * @return
	 */
	public boolean isModuleParaOpen(String code)throws SystemException,ServiceException{
		try{
			String able = smsPlatform.isModuleParaOpen(code);
			if("1".equals(able)){
				return true;
			}else{
				return false;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信模块开关状态"});
		}
	}

	/**
	 * author:luosy
	 * description:  短信模块是否可用(供提醒标签使用)
	 * modifyer:
	 * description:
	 * @param code
	 * @return true : 启用；false ：关闭
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean isSMSOpen(String code)throws SystemException,ServiceException{
		try{
			String able = smsPlatform.isModuleParaOpen(code);
			
			String userId = userService.getCurrentUser().getUserId();
			boolean SendAble = smsControl.hasSendRight(userId);
			if("1".equals(able)&&SendAble){
				return true;
			}else{
				return false;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信模块开关状态"});
		}
	}

	/**
	 * author:luosy
	 * description: 短信模块 获取手机卡号码
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getSmsModelNum()throws SystemException,ServiceException{
		try{
			ToaSmsCommConf conf = smsConf.getSmsConf();
			return conf.getSmscomSimnum();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信模块获取手机卡号码"});
		}catch(Exception e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信模块获取手机卡号码"});
		}
	}
	/**
	 * @author 张磊
	 * @create 2012-03-08
	 * @description 生日提醒功能
	 * @throws Exception
	 * */
	@SuppressWarnings({ "unchecked", "static-access" })
	public void birthReminder() throws Exception {
		ToaSystemset toaSys = sysManager.getSystemset();
		String isUsing = ""+toaSys.getIsBirthReminder();//是否启用生日提醒
		if (isUsing.endsWith("0")) {
			System.out.println("******不启用生日提醒功能******");
		} else if (isUsing.endsWith("1")) {
			Map m = new HashMap(); // 存放所有员工或者用户
			List l = new ArrayList(); // 存放当天生日的员工或者用户
			ToaPersonalInfo toaPersonalInfo = new ToaPersonalInfo();
			m = myInfoManager.getPersonalInfo();
			
			/*用于获取明天的日期*/
			Date date=new Date();//取时间
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
	        date=calendar.getTime(); //这个时间就是日期往后推一天的结果        
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String today = df.format(date);
			
			for (Iterator i = m.values().iterator(); i.hasNext();) {// 遍历出所有用户
				toaPersonalInfo = new ToaPersonalInfo();
				toaPersonalInfo = (ToaPersonalInfo) i.next();
				if (toaPersonalInfo.getPrsnBirthday() != null) {
					String birth = df.format(toaPersonalInfo.getPrsnBirthday());// 格式化生日
					if (birth.endsWith(today)) {
						l.add(toaPersonalInfo); // 将当天生日的员工添加到List
					}
				}
			}
			
			/*将信息发送时间转换为Date类型*/
			DateFormat gainTime = new SimpleDateFormat("HH:mm:ss");
			DateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			System.out.println(toaSys.getReminderDate());
			if(null==toaSys.getReminderDate()){
				return;
			}
			String timeValue = gainTime.format(toaSys.getReminderDate());
			Date sendTime = formatTime.parse(today+" "+timeValue);
			
			List<ToaSms> smsSize = smsManager.getSendedListByDate(sendTime);
			List l1 = new ArrayList(); // 存放当天发送过的号码以及内容
			for (int t = 0; t < smsSize.size(); t++) {
				ToaSms ts = smsSize.get(t);
				l1.add(ts.getSmsRecvnum());
				l1.add(ts.getSmsCon());
			}
//			System.out.println("*******将会有" + l.size() + "个人生日********");
			if (l.size() != 0) { // 判断是否有生日
				for (int j = 0; j < l.size(); j++) {
					String blessing = toaSys.getReminderText(); // 获取服务器配置的祝福语
					toaPersonalInfo = new ToaPersonalInfo();
					toaPersonalInfo = (ToaPersonalInfo) l.get(j);
					String tel = toaPersonalInfo.getPrsnMobile1();// 被发送者的手机号码
					String pos = toaPersonalInfo.getPosition(); // 被发送者的职务
					String name = toaPersonalInfo.getPrsnName();// 被发送者的姓名
					if (pos == "" || pos == null) { // 处级以上干部需显示职务
						blessing = blessing.replaceAll("\\@", name);
					} else {
						blessing = blessing.replaceAll("\\@", name.concat(pos));
					}
					ToaSms toa = new ToaSms();
					toa.setSmsCon(blessing); //信息内容
					toa.setSmsRecvnum(tel); // 号码
					toa.setSmsSendTime(sendTime);// 发送时间
					toa.setModelName("手机模块");
					toa.setModelCode("1");
					toa.setSmsIsdel(ToaSms.SMS_NOTDEL);
					toa.setSmsRecver(name);
					toa.setSmsSenderName("系统发送");
					toa.setSmsSendUser("system");
					toa.setSmsServerRet(ToaSms.SERVICE_RET_UNSEND);
					toa.setSmsState(ToaSms.SMS_UNSEND);
					if (smsSize.size() == 0) {
						smsManager.saveSms(toa);
					} else {
						if ((l1.contains(tel) && l1.contains(blessing)) == false) {
							smsManager.saveSms(toa);
						} else {
//							System.out.println("*******birthday SMS sended。。。。********");
						}
					}
				}
			}
		}
	}
	
	
}
