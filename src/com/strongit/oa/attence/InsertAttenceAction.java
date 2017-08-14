package com.strongit.oa.attence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class InsertAttenceAction extends QuartzJobBean {

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
			String userName=user.getUserName();
			ToaAttendanceRecord record=new ToaAttendanceRecord();
			record.setUserId(userID);
			record.setUserName(userName);
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			Date currentDate = null;
			try {
				currentDate = dateFormat.parse(dateFormat.format(new Date()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			record.setTime(currentDate);
			record.setNowoke(7);//默认旷工7小时
			record.setType(0);
			attenceRecordManager.save(record);
			
			ToaLog log = new ToaLog();
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("{初始化}[" +currentDate+ userName + "]的考勤信息");// 日志信息
			myLogManager.saveObj(log);
		}
	}


}
