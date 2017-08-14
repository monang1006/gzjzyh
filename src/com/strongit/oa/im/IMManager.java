package com.strongit.oa.im;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.BaseComponent;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.model.Task;
import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.im.service.AbstractBaseService;
import com.strongit.oa.util.DateCountUtil;
import com.strongit.oa.util.PropertiesUtil;
import com.strongit.oa.common.user.util.Md5;
import com.strongmvc.exception.SystemException;
import com.strongmvc.service.ServiceLocator;

@Service("iMManager")
public class IMManager implements IMMessageService {

	private Log logger = LogFactory.getLog(IMManager.class);

	@Autowired
	IUserService userService;// 注入统一用户接口

	BaseComponent baseComponent;

	/**
	 * 在构造方法中实例化处理类.
	 * 
	 */
	public IMManager() {
		baseComponent = (BaseComponent) ServiceLocator
				.getService("baseComponent");
	}

	/**
	 * 在Spring上下文中得到所有即时通讯软件服务类.
	 * 
	 * @author:邓志城
	 * @date:2010-6-4 上午10:21:09
	 * @return
	 */
	public List<AbstractBaseService> findAllImService() {
		ApplicationContext cxt = baseComponent.getApplicationContext();
		String[] beanNames = cxt.getBeanNamesForType(AbstractBaseService.class);
		List<AbstractBaseService> baseServiceList = new ArrayList<AbstractBaseService>();
		for (String beanName : beanNames) {
			AbstractBaseService bean = (AbstractBaseService) cxt
					.getBean(beanName);
			baseServiceList.add(bean);
		}
		return baseServiceList;
	}

	/**
	 * 发送即时消息
	 * 
	 * @param String
	 *            message 消息内容
	 * @param String
	 *            receiveId 接收人ID，多个接收人以逗号隔开
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public String sendIM(String message, String receiveId, Task task)
			throws Exception {
		List receiver = new ArrayList();
		// add by yangwg
		if (receiveId != null && receiveId.length() > 0) {
			String receiveIdArray[] = receiveId.split(",");
			for (String userId : receiveIdArray) {
				receiver.add(userId);
			}
		}
		//del by yangwg
		// receiver.add(receiveId);
		return sendIMMessage(message, receiver, task);
	}
	
	
	
	
	/**
	 * 对外发送即时消息接口
	 * @author:邓志城
	 * @author:刘皙
	 * @date:2009-5-18 下午03:15:59
	 * @param message 消息内容
	 * @param receivers 消息接受者
	 * @param task
	 * <P>默认是当前用户发送,如果当前用户不存在，则有系统管理员发送</P>
	 * 	 * @return 
	 * 	<P>0:发送成功；-1：软件未启用；-2：异常</P>
	 * @throws Exception
	 */
	public String sendIMMessage(String message,List<String> receivers,Task task)throws Exception{
		String ret = "0";
		try {
			if(isEnabled()){
//				message = message==null?"":message.replaceAll("\\s", "");
				message = message==null?"":message;
				String sender = userService.getCurrentUser().getUserLoginname(); 
				String pwd = userService.getCurrentUser().getUserExpresslyPassword();
				if(pwd == null) {
					pwd = PropertiesUtil.getProperty("im.system.message.sender.pwd");
				}
				//DesUtil des = new DesUtil(DateCountUtil.getNowCompactDatetime());
				Md5 md5 = new Md5();
				for(String recv:receivers){
					User user = userService.getUserInfoByUserId(recv);
					String userLoginName = user.getUserLoginname();
					//String md5Pwd = user.getUserPassword();
					//String desPwd = des.encry(ignoreValidate);
					/*if(!sender.equals(userLoginName)){//发送即时消息不允许发送给自己
						//先验证接收人是否存在
						if(isUserExist(userLoginName)){ 
							Cache.getService().sendMessage(sender, userLoginName, message,pwd);				
						}
					}*/
					String desPwd = md5.getMD5ofStr(DateCountUtil.serialVersionUID);
					if(task == null) {
						task = new Task();
					}
					task.setPassword(pwd);
					task.setDesOfPassword(desPwd);
					task.setUserName(user.getUserName());
					task.setUserLoginName(userLoginName);
					Cache.getService().sendMessage(sender, userLoginName, message, task);				
				}
			}else{
				ret = "-1";
				logger.error("im软件未启用.");
			}
			
		} catch (Exception e) {
			logger.error("发送即时消息出错",e);
			ret = "-2";
		}
		return ret ;
	}
	
	/**
	 * 对外发送即时消息接口
	 * @author:邓志城
	 * @author:刘皙
	 * @date:2009-5-18 下午03:15:59
	 * @param message 消息内容
	 * @param receivers 消息接受者
	 * @param task
	 * @param SenderID 发送者ID
	 * <P>默认是当前用户发送,如果当前用户不存在，则有系统管理员发送</P>
	 * 	 * @return 
	 * 	<P>0:发送成功；-1：软件未启用；-2：异常</P>
	 * @throws Exception
	 */
	public String sendIMMessage(String message,List<String> receivers,Task task,String SenderID)throws Exception{
		String ret = "0";
		try {
			if(isEnabled()){
//				message = message==null?"":message.replaceAll("\\s", "");
				message = message==null?"":message;
				User senderUser = userService.getUserInfoByUserId(SenderID); 
				String sender = senderUser.getUserLoginname(); 
				String pwd = senderUser.getUserExpresslyPassword();
				if(pwd == null) {
					pwd = PropertiesUtil.getProperty("im.system.message.sender.pwd");
				}
				//DesUtil des = new DesUtil(DateCountUtil.getNowCompactDatetime());
				Md5 md5 = new Md5();
				for(String recv:receivers){
					User user = userService.getUserInfoByUserId(recv);
					String userLoginName = user.getUserLoginname();
					//String md5Pwd = user.getUserPassword();
					//String desPwd = des.encry(ignoreValidate);
					/*if(!sender.equals(userLoginName)){//发送即时消息不允许发送给自己
						//先验证接收人是否存在
						if(isUserExist(userLoginName)){ 
							Cache.getService().sendMessage(sender, userLoginName, message,pwd);				
						}
					}*/
					String desPwd = md5.getMD5ofStr(DateCountUtil.serialVersionUID);
					if(task == null) {
						task = new Task();
					}
					task.setPassword(pwd);
					task.setDesOfPassword(desPwd);
					task.setUserName(user.getUserName());
					task.setUserLoginName(userLoginName);
					Cache.getService().sendMessage(sender, userLoginName, message, task);				
				}
			}else{
				ret = "-1";
				logger.error("im软件未启用.");
			}
			
		} catch (Exception e) {
			logger.error("发送即时消息出错",e);
			ret = "-2";
		}
		return ret ;
	}
	
	

	/**
	 * 对外发送即时消息接口
	 * @author:邓志城
	 * @date:2009-5-18 下午03:15:59
	 * @param message 消息内容
	 * @param receiveId 消息接受者</BR>
	 * <P>默认是当前用户发送,如果当前用户不存在，则有系统管理员发送</P>
	 * 	 * @return 
	 * 	<P>0:发送成功；-1：软件未启用；-2：异常</P>
	 * @throws Exception
	 */
	public String sendIMMessageBySender(String message,List<String> receivers,Task task,String userId)throws Exception{
		String ret = "0";
		try {
			if(isEnabled()){
//				message = message==null?"":message.replaceAll("\\s", "");
				String sender ="";
				String pwd ="";
				User senduser=new User();
				if(userId!=null && !"".equals(userId)){
					senduser= userService.getUserInfoByUserId(userId);
				}
				if (senduser!=null){
					sender = senduser.getUserLoginname();
					pwd = senduser.getUserExpresslyPassword();
				}
				
				if(pwd == null) {
					pwd = PropertiesUtil.getProperty("im.system.message.sender.pwd");
				}
	
				//DesUtil des = new DesUtil(DateCountUtil.getNowCompactDatetime());
				Md5 md5 = new Md5();
				for(String recv:receivers){
					User user = userService.getUserInfoByUserId(recv);
					String userLoginName = user.getUserLoginname();
					//String md5Pwd = user.getUserPassword();
					//String desPwd = des.encry(ignoreValidate);
					/*if(!sender.equals(userLoginName)){//发送即时消息不允许发送给自己
						//先验证接收人是否存在
						if(isUserExist(userLoginName)){ 
							Cache.getService().sendMessage(sender, userLoginName, message,pwd);				
						}
					}*/
					String desPwd = md5.getMD5ofStr(DateCountUtil.serialVersionUID);
					if(task != null) {
						task.setPassword(pwd);
						task.setDesOfPassword(desPwd);
						task.setUserName(user.getUserName());
						task.setUserLoginName(userLoginName);
					}
					Cache.getService().sendMessage(sender, userLoginName, message,task);				
				}
			}else{
				ret = "-1";
				logger.error("im软件未启用.");
			}
			
		} catch (Exception e) {
			logger.error("发送即时消息出错",e);
			ret = "-2";
		}
		return ret ;
	}

	
	/**
	 * 用户数据同步到即时通讯软件
	 * 
	 * @author:邓志城
	 * @date:2010-6-2 上午09:03:29
	 * @return
	 */
	public String oa2im() {
		return Cache.getService().oa2im();
	}

	/**
	 * 返回即时通讯软件是否启用的标示.
	 * 
	 * @author:邓志城
	 * @date:2010-6-2 上午08:58:22
	 * @return
	 * @throws Exception
	 */
	public boolean isEnabled() throws Exception {
		return Cache.getService().isEnabled();
	}

	/**
	 * 用户是否存在
	 * 
	 * @param userLoginName
	 * @return
	 * @throws SystemException
	 */
	public boolean isUserExist(String userLoginName) throws Exception {
		try {
			return Cache.getService().isUserExist(userLoginName);
		} catch (Exception e) {
			throw new SystemException();
		}
	}
	
	public int getUserStatus(String userLoginName) throws Exception {
		try {
			return Cache.getService().getUserStatus(userLoginName);
		} catch (Exception e) {
			throw new SystemException();
		}
	}

	/**
	 * 获取用户的sessionKey，用于登陆RTX
	 * 
	 * @param userLoginName
	 * @return
	 * @throws SystemException
	 */
	public String getSessionKey(String userLoginName) throws Exception {
		try {
			return Cache.getService().getSessionKey(userLoginName);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取用户简单信息
	 * 
	 * @param userLoginName
	 * @return "userRTXNo:用户RTX编号,userName:用户名"
	 * @throws SystemException
	 */
	public String getUserInfo(String userLoginName) throws Exception {
		try {
			return Cache.getService().getUserInfo(userLoginName);
		} catch (Exception e) {
			throw new SystemException("获取人员简单信息失败.");
		}
	}

	/**
	 * 添加用户
	 * 
	 * @param deptId
	 * @param loginName
	 * @param password
	 * @param realName
	 * @param mobile
	 * @return
	 * @throws SystemException
	 */
	public String addUser(String deptId, String loginName, String password,
			String realName, String mobile) throws Exception {
		try {
			return Cache.getService().addUser(deptId, loginName, password,
					realName, mobile);
		} catch (Exception e) {
			throw new SystemException("添加用户到IM软件失败");
		}
	}

	/**
	 * 添加部门
	 * 
	 * @param deptId
	 * @param parentDeptId
	 * @param deptName
	 * @param deptDesc
	 * @return
	 * @throws SystemException
	 */
	public String addDept(String deptId, String parentDeptId, String deptName,
			String deptDesc) throws Exception {
		try {
			return Cache.getService().addDept(deptId, parentDeptId, deptName,
					deptDesc);
		} catch (Exception e) {
			throw new SystemException("添加IM部门失败");
		}

	}

	/**
	 * 删除用户
	 * 
	 * @param userLoginName
	 * @return
	 * @throws SystemException
	 */
	public String delUser(String userLoginName) throws Exception {
		try {
			return Cache.getService().delUser(userLoginName);
		} catch (Exception e) {
			throw new SystemException("删除IM用户失败");
		}
	}

	/**
	 * 删除部门
	 * 
	 * @param deptId
	 * @return
	 * @throws SystemException
	 */
	public String delDept(String deptId, String isDel) throws Exception {
		try {
			return Cache.getService().delDept(deptId, isDel);
		} catch (Exception e) {
			throw new SystemException("删除IM部门失败");
		}
	}

	/**
	 * author:dengzc description:发送消息提醒 modifyer: description:
	 * 
	 * @param message
	 *            String 消息内容
	 * @param receiveId
	 *            String 接收人ID,多个接收人以逗号隔开
	 * @param type
	 *            String 0:普通消息 1:紧急消息
	 * @param title
	 *            String 消息标题
	 * @param delayTime
	 *            String 显示停留时间(毫秒) 0:为永久停留(用户关闭时才关闭)
	 * @return String 0:操作成功 非0:操作不成功
	 * @throws Exception
	 */
	public String sendNotify(String message, String receiveId, String type,
			String title, String delayTime) throws Exception {
		try {
			String[] receiveIds = receiveId.split(",");
			String ret = "";
			for (int i = 0; i < receiveIds.length; i++) {
				ret = Cache.getService().sendNotify(
						title,
						message,
						userService.getUserInfoByUserId(receiveIds[i])
								.getUserLoginname());
			}
			return ret;
		} catch (Exception e) {
			throw new SystemException("发送消息提醒出错", e);
		}
	}

	/**
	 * 获取IM组织机构树信息
	 * <P>
	 * 机构树,从根节点开始遍历,读取每个机构下是否存在子机构信息.
	 * </P>
	 * 
	 * @author:邓志城
	 * @date:2010-6-2 上午09:37:52
	 * @param deptId
	 * @return
	 */
	public Object[] imTreeInfo(String deptId) {
		return Cache.getService().imTreeInfo(deptId);
	}

	/**
	 * 获取IM组织机构人员信息.
	 * 
	 * @author:邓志城
	 * @date:2010-6-2 上午09:45:50
	 * @param deptId
	 *            部门id
	 * @return 人员信息.
	 */
	public String imUserInfo(String deptId, Object... objects) {
		return Cache.getService().imUserInfo(deptId, objects);
	}

}
