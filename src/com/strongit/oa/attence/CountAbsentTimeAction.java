package com.strongit.oa.attence;

import java.util.Date;
import java.util.List;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.strongit.oa.bo.ToaAttendanceRecord;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.service.UserService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.attence.AttenceRecordManager;
//初始化所有人员的考勤信息
public class CountAbsentTimeAction extends QuartzJobBean {

	private UserService userService=null;
	private AttenceRecordManager attenceRecordManager=null;
	private MyLogManager myLogManager=null;
	
	
	public AttenceRecordManager getAttenceRecordManager() {
		return attenceRecordManager;
	}


	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public void setAttenceRecordManager(AttenceRecordManager attenceRecordManager) {
		this.attenceRecordManager = attenceRecordManager;
	}

	
	public MyLogManager getMyLogManager() {
		return myLogManager;
	}


	public void setMyLogManager(MyLogManager myLogManager) {
		this.myLogManager = myLogManager;
	}


	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		userService.setUserId(IUserService.SYSTEM_ACCOUNT);//因为userService是和当前登录用户绑定的，而这里没当前登录用户的概念，必须设置
		List<User> allUsers=userService.getAllUserInfo();
		for (int i = 0; i < allUsers.size(); i++) {
			User user=allUsers.get(i);
			String userID=user.getUserId();
			ToaAttendanceRecord record;
			try {
				record = attenceRecordManager.getTodayAttenceInfo(userID);
				if(record.getWorktime()!=null&&!"".equals(record.getWorktime())&&record.getLeavetime()!=null&&!"".equals(record.getLeavetime())){
					record.setNowoke(0);
					if(record.getLatetime()!=0&&record.getLeaveearly()!=0){
						record.setType(5);//5表示“迟到并早退”
					}else if(record.getLatetime()!=0){
						record.setType(1);//1表示“迟到”
					}else if(record.getLeaveearly()!=0){
						record.setType(2);//2表示“早退”
					}else {
						record.setType(6);//6表示“正常”
					}
				}else if(record.getWorktime()!=null&&!"".equals(record.getWorktime())){
					record.setType(4);//4表示“下班未打卡”,只要有一次未打卡，旷工时间就是默认为7，不设置
				}else if(record.getLeavetime()!=null&&!"".equals(record.getLeavetime())){
					record.setType(3);//3表示“上班未打卡”
				}
				attenceRecordManager.update(record);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ToaLog log = new ToaLog();
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("{更新}[" +new Date()+ user.getUserName() + "]的考勤信息");// 日志信息
			myLogManager.saveObj(log);
		}
	}


}
