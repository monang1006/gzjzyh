package com.strongit.oa.util;

import org.apache.axis.utils.JavaUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongmvc.exception.ServiceException;

/**
 * <p>Title: LogAop.java</p>
 * <p>Description: AOP日志信息处理核心类</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2009-11-23 18:39:16
 * @classpath    com.strongit.oa.util.LogAop
 * @version  1.0
 */
@Service
@Aspect
public class LogAop {
	
	private static final Logger log = Logger.getLogger(LogAop.class);
	
	private MyLogManager myLogManager;
	
	private IUserService user;
	
	@Autowired
	public void setMyLogManager(MyLogManager myLogManager) {
		this.myLogManager = myLogManager;
	}
	
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}

	
	@Around("@within(com.strongit.oa.util.annotation.OALogger)")
	public Object joinPointAccess(ProceedingJoinPoint inter) throws Throwable{
		
		log.info("当前运行类："+inter.getTarget().getClass().getName());
		Object obj = null;
		Object arg[] = inter.getArgs();
		
		try{
			obj = inter.proceed();
		}catch(RuntimeException e){
			log.error("发生异常", e);
			for(int i=0;i<arg.length;i++){
				if(arg[i] instanceof OALogInfo[]){
					OALogInfo[] temp = (OALogInfo[])arg[i];
					if(temp.length!=0){
						//log.info("日志信息:"+temp[0].getLogInfo());
						String eStr = JavaUtils.stackToString(e);
						if(eStr.length() >= 1000) {
							eStr = eStr.substring(0, 1000);
						}
						temp[0].setLogInfo(temp[0].getLogInfo()+"@该操作出现异常，异常信息：" + eStr);
						ToaLog log = mapToObj(temp[0]);				//进行对象映射
						if(log!=null){
							myLogManager.saveObj(log);				//进行日志信息持久化
						}else{
																	
						}
					}
				}
			}
			throw e;
		}
		for(int i=0;i<arg.length;i++){
			if(arg[i] instanceof OALogInfo[]){
				OALogInfo[] temp = (OALogInfo[])arg[i];
				if(temp.length!=0){
					//log.info("日志信息:"+temp[0].getLogInfo());
					ToaLog log = mapToObj(temp[0]);				//进行对象映射
					if(log!=null){
						myLogManager.saveObj(log);				//进行日志信息持久化
					}else{
																
					}
				}
			}
		}

		return obj;
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-23 18:58:36
	 * @des 	将传输类转化为对应的BO    
	 * @return  ToaLog
	 */
	private ToaLog mapToObj(OALogInfo oaLogInfo){
		if(oaLogInfo == null){
			return null;
		}else{
			ToaLog log = new ToaLog();
			log.setLogInfo(oaLogInfo.getLogInfo());
			log.setLogModule(oaLogInfo.getLogModule());
			log.setLogState(oaLogInfo.getLogState());		
			if(oaLogInfo.getOpeIp()!=null){
				log.setOpeIp(oaLogInfo.getOpeIp());
			}else{
				try {
					log.setOpeIp(ServletActionContext.getRequest().getRemoteAddr());
				} catch (Exception e) {
					log.setOpeIp(null);
				}			
			}
			log.setOpeTime(oaLogInfo.getOpeTime());
			if(oaLogInfo.getOpeUser() != null){
				log.setOpeUser(oaLogInfo.getOpeUser());
			}else{
				try {
					log.setOpeUser(user.getCurrentUser().getUserName());
				} catch (Exception e) {
					log.setOpeUser(user.getUserNameByUserId(IUserService.SYSTEM_ACCOUNT));
				}
			}
			return log;
		}
	}

}
