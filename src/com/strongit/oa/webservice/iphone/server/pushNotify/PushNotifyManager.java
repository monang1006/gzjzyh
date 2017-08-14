/**  
 * @title: pushNotifyManager.java
 * @package com.strongit.oa.webservice.iphone.server.pushNotify
 * @description: TODO
 * @author  hecj
 * @date Jan 14, 2014 8:47:16 PM
 */

package com.strongit.oa.webservice.iphone.server.pushNotify;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaPushNotify;
import com.strongit.oa.bo.ToaPrintSet;
import com.strongit.oa.bo.ToaPushNotifyCloseMessage;
import com.strongit.oa.bo.ToaPushNotifyToken;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.smsplatform.SmsPlatformManager;
import com.strongit.utils.StringUtil;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * @classname: pushNotifyManager
 * @author hecj
 * @date Jan 14, 2014 8:47:16 PM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.webservice.iphone.server.pushNotify
 * @update
 */
@Service
@Transactional
public class PushNotifyManager {
	/**
	 * 对象注入
	 */
	private GenericDAOHibernate<ToaPushNotify, String> pushDao;
	private GenericDAOHibernate<ToaPushNotifyToken, String> tokenDao;
	private GenericDAOHibernate<ToaPushNotifyCloseMessage, String> closeDao;
	// 短信通道的模块对应manager
	@Autowired
	private SmsPlatformManager platformManager;
	@Autowired
	private PushNotify pushNotify;

	/**
	 * 常用变量,推送信息功能里i4含义不是领导日程模块时就需要同步修改 例如我设定i4对应的是领导日程模块 i1待办事宜 i2公文管理 i3内部邮件
	 * i4领导日程
	 * i5通知公告
	 */
	public static final String PUSH_MODULE_NO_TODO = "i1";
	public static final String PUSH_MODULE_NO_DOCTODO = "i2";
	public static final String PUSH_MODULE_NO_MESSAGE = "i3";
	public static final String PUSH_MODULE_NO_CALENDAR = "i4";
	public static final String PUSH_MODULE_NO_ARTICLES = "i5";

	public static final String WORKFLOW_TYPE_DOC = "2,3";

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.pushDao = new GenericDAOHibernate<ToaPushNotify, String>(
				sessionFactory, ToaPushNotify.class);
		this.tokenDao = new GenericDAOHibernate<ToaPushNotifyToken, String>(
				sessionFactory, ToaPushNotifyToken.class);
		this.closeDao = new GenericDAOHibernate<ToaPushNotifyCloseMessage, String>(
				sessionFactory, ToaPushNotifyCloseMessage.class);
	}

	/**
	 * 保存推送的消息
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Jan 14, 2014 8:54:02 PM
	 * @param
	 * @return void
	 * @throws Exception
	 * @throws
	 */
	public void saveNotity(String userId, String moduleNo, int count)
			throws SystemException {
		if (moduleNo != null && !"".equals(moduleNo)) {
			try {
				List<ToaPushNotify> lst = findNotifyByUserIdAndModuleNo(userId,
						moduleNo, "order by t.moduleNo");
				ToaPushNotify entity = null;
				if (lst != null && lst.size() == 1) {
					entity = lst.get(0);
				} else if (lst != null && lst.size() > 1) {
					throw new SystemException("记录不唯一,定位不到记录");
				}
				if (entity == null) {
					entity = new ToaPushNotify();
					entity.setPID(null);
					entity.setUserId(userId);
					entity.setModuleNo(moduleNo);
					entity.setMessageCount(count);
					pushDao.save(entity);
				} else {
					entity.setMessageCount(entity.getMessageCount() + count);
					pushDao.update(entity);
				}
				pushDao.flush();
			} catch (Exception e) {
				throw new SystemException("保存推送消息失败!");
			}
		}
	}
	/**
	 * 推送状态
	 * @description
	 *
	 * @author  hecj
	 * @date    Mar 23, 2014 7:00:50 PM
	 * @param   
	 * @return  boolean
	 * 			    true 推送
	 * 				false 不推送
	 * @throws
	 */
	public boolean getPushState(String userId,String moduleNo){
		List<ToaPushNotifyCloseMessage> lst= this.findModuleCloseByUserId(userId, moduleNo, null);
		if(lst!=null&&lst.size()>0){
			ToaPushNotifyCloseMessage msg=lst.get(0);
			if("0".equals(msg.getStateFlag())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 根据用户id以及模块编号获取推送的统计信息
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Jan 15, 2014 8:49:13 AM
	 * @param
	 * @return ToaIOSPushNotify
	 * @throws Exception
	 * @throws
	 */
	public List<ToaPushNotifyCloseMessage> findModuleCloseByUserId(String userId,
			String moduleNo, String orderBy) throws SystemException {
		StringBuilder sb = new StringBuilder();
		sb.append("from ToaPushNotifyCloseMessage t where 1=1 ");
		if (StringUtil.isNotEmpty(userId)) {
			sb.append(" and t.userId= '" + userId + "'");
		}
		if (StringUtil.isNotEmpty(moduleNo)) {
			sb.append(" and t.moduleNo= '" + moduleNo + "' ");
		}
		if (StringUtil.isNotEmpty(orderBy)) {
			sb.append(orderBy);
		}
		List<ToaPushNotifyCloseMessage> lst = closeDao.find(sb.toString());
		return lst;
	}
	
	/**
	 * 保存模块开关的设置信息
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Jan 14, 2014 8:54:02 PM
	 * @param
	 * @return void
	 * @throws Exception
	 * @throws
	 */
	public void saveModuleClose(String userId, String moduleNo, String flag)
			throws SystemException {
		if (moduleNo != null && !"".equals(moduleNo)) {
			try {
				List<ToaPushNotifyCloseMessage> lst = findModuleCloseByUserId(userId,
						moduleNo, "order by t.moduleNo");
				ToaPushNotifyCloseMessage entity = null;
				if (lst != null && lst.size() == 1) {
					entity = lst.get(0);
				} else if (lst != null && lst.size() > 1) {
					throw new SystemException("记录不唯一,定位不到记录");
				}
				if (entity == null) {
					entity = new ToaPushNotifyCloseMessage();
					entity.setPID(null);
					entity.setUserId(userId);
					entity.setModuleNo(moduleNo);
					entity.setStateFlag(flag);
					closeDao.save(entity);
				} else {
					entity.setStateFlag(flag);
					closeDao.update(entity);
				}
				closeDao.flush();
			} catch (Exception e) {
				throw new SystemException("保存推送消息失败!");
			}
		}
	}
	/**
	 * 在进入推送模块前最好先调用接口获取数据
	 * 获取模块的推送设置
	 * @description
	 *
	 * @author  hecj
	 * @date    Mar 25, 2014 11:08:30 AM
	 * @param   
	 * @return  String
	 * @throws
	 */
	public List getModuleClose(String userId){
		List<ToaPushNotifyCloseMessage> lst =new ArrayList<ToaPushNotifyCloseMessage>();
		if (userId != null && !"".equals(userId)) {
			try {
				lst = findModuleCloseByUserId(userId,null, "order by t.moduleNo");
				return lst;
			} catch (Exception e) {
				throw new SystemException("保存推送消息失败!");
			}
		}
		return lst;
	}
	
	/**
	 * 根据用户id以及模块编号获取推送的统计信息
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Jan 15, 2014 8:49:13 AM
	 * @param
	 * @return ToaIOSPushNotify
	 * @throws Exception
	 * @throws
	 */
	public List<ToaPushNotify> findNotifyByUserIdAndModuleNo(String userId,
			String moduleNo, String orderBy) throws SystemException {
		StringBuilder sb = new StringBuilder();
		sb.append("from ToaPushNotify t where 1=1 ");
		if (StringUtil.isNotEmpty(userId)) {
			sb.append(" and t.userId= '" + userId + "'");
		}
		if (StringUtil.isNotEmpty(moduleNo)) {
			sb.append(" and t.moduleNo= '" + moduleNo + "' ");
		}
		if (StringUtil.isNotEmpty(orderBy)) {
			sb.append(orderBy);
		}
		List<ToaPushNotify> lst = pushDao.find(sb.toString());
		return lst;
	}

	/**
	 * 根据用户id获取每个模块的推送消息总数
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Jan 15, 2014 8:49:13 AM
	 * @param
	 * @return ToaIOSPushNotify
	 * @throws Exception
	 * @throws
	 */
	public List getTotalEachModule(String userId) throws SystemException {
		StringBuilder sb = new StringBuilder();
		sb
				.append("select t.moduleNo,sum(messageCount) from ToaPushNotify t where 1=1 ");
		if (StringUtil.isNotEmpty(userId)) {
			sb.append(" and t.userId= '" + userId + "'");
		}
		sb
				.append(" group by t.moduleNo having sum(messageCount)>0 order by t.moduleNo");
		List lst = pushDao.find(sb.toString());
		return lst;
	}

	/**
	 * 根据模块获取所有的用户id，根据用户id获取所有的设备令牌id
	 * @description
	 *
	 * @author  hecj
	 * @date    Mar 24, 2014 4:44:11 PM
	 * @param   moduleNo
	 * 				模块标示 例如:i1,i2
	 * 			clientType
	 * 				移动客户端类型 0:ios 1:android
	 * @return  List
	 * @throws
	 */
	public List getTokenListEachModule(String moduleNo,String clientType) throws SystemException {
		StringBuilder sb = new StringBuilder();
		sb.append("select o.tokenId from ToaPushNotify t,ToaPushNotifyToken o where t.userId=o.userId ");
		if (StringUtil.isNotEmpty(moduleNo)) {
			sb.append(" and t.moduleNo= '" + moduleNo + "'");
		}
		if (StringUtil.isNotEmpty(clientType)) {
			sb.append(" and o.clientType= '" + clientType + "'");
		}
		List lst = pushDao.find(sb.toString());
		return lst;
	}
	
	/**
	 * 将模块标示与模块名称保存起来
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Mar 23, 2014 12:56:21 PM
	 * @param
	 * @return void
	 * @throws
	 */
	public HashMap<String, String> moduleNameMap() {
		/**
		 * 短信通道对应的模块表
		 */
		HashMap<String, String> nameMap = new HashMap<String, String>();
		List<ToaBussinessModulePara> moduleList = platformManager.getAllObj();
		if (moduleList != null && moduleList.size() > 0) {
			Iterator<ToaBussinessModulePara> it = moduleList.iterator();
			ToaBussinessModulePara para = null;
			while (it.hasNext()) {
				para = it.next();
				nameMap.put(para.getBussinessModuleCode(), para
						.getBussinessModuleName());
			}
		}
		return nameMap;
	}

	/**
	 * 根据用户id获取令牌列表
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Mar 23, 2014 1:15:16 PM
	 * @param
	 * @return List<String>
	 * @throws
	 */
	public List<ToaPushNotifyToken> getTokenListByUserId(String userId) {
		List<ToaPushNotifyToken> tokenList = new ArrayList<ToaPushNotifyToken>();
		StringBuilder hql = new StringBuilder(
				"from ToaPushNotifyToken t where 1=1 ");
		List<String> paramList = new LinkedList<String>();
		if (userId != null && !"".equals(userId)) {
			hql.append(" and t.userId=?");
			paramList.add(userId);
		}
		tokenList = tokenDao.find(hql.toString(), paramList.toArray());
		return tokenList;
	}

	/**
	 * 获取所有不重复的用户id并且这个用户的模块的推送消息数大于0
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Mar 23, 2014 1:15:16 PM
	 * @param
	 * @return List<String>
	 * @throws
	 */
	public List getAllTokenUserId() {
		List tokenList = new ArrayList();
		StringBuilder hql = new StringBuilder(
				"select distinct t.userId from ToaPushNotifyToken t,ToaPushNotify o where t.userId=o.userId and o.messageCount>0");
		tokenList = tokenDao.find(hql.toString());
		return tokenList;
	}

	/**
	 * 获取所有模块，并且模块对应推送数不为0
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Mar 23, 2014 1:15:16 PM
	 * @param
	 * @return List<String>
	 * @throws
	 */
	public List getAllModule() {
		List tokenList = new ArrayList();
		StringBuilder hql = new StringBuilder(
				"select distinct t.moduleNo from ToaPushNotify t where t.messageCount>0");
		tokenList = tokenDao.find(hql.toString());
		return tokenList;
	}
	
	/**
	 * 根据令牌id获取列表
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Mar 23, 2014 2:23:22 PM
	 * @param
	 * @return List<ToaPushNotifyToken>
	 * @throws
	 */
	public List<ToaPushNotifyToken> getTokenListByTokenId(String tokenId) {
		List<ToaPushNotifyToken> tokenList = new ArrayList<ToaPushNotifyToken>();
		StringBuilder hql = new StringBuilder(
				"from ToaPushNotifyToken t where 1=1 ");
		List<String> paramList = new LinkedList<String>();
		if (tokenId != null && !"".equals(tokenId)) {
			hql.append(" and t.tokenId=?");
			paramList.add(tokenId);
		}
		tokenList = tokenDao.find(hql.toString(), paramList.toArray());
		return tokenList;
	}

	/**
	 * 保存令牌与用户id的关系 约束 1、一个用户允许有多个令牌，也就是一个用户可以有多个手机 2、多个用户使用同一个令牌是不允许的
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Mar 23, 2014 2:19:35 PM
	 * @param
	 * @return void
	 * @throws
	 */
	public void saveTokenWithUserId(String tokenId, String userId,String clientType) {
		List<ToaPushNotifyToken> tokenList = this
				.getTokenListByTokenId(tokenId);
		if (tokenList != null && tokenList.size() > 0) {
			// 如果当前令牌id已存在,那么就不保存,只允许一个令牌对应一个用户id
			// 也不允许修改操作，如果令牌已经绑定了一个用户id，除非删除这个记录再新增绑定新用户id，否则这个令牌就不允许修改也不允许再添加这个令牌
		} else {
			ToaPushNotifyToken token = new ToaPushNotifyToken(tokenId, userId,clientType);
			tokenDao.save(token);
		}
	}

	/**
	 * 推送消息
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Mar 23, 2014 1:00:54 PM
	 * @param userId
	 *            用户id
	 * @return String
	 * @throws KeystoreException
	 * @throws CommunicationException
	 * @throws InvalidDeviceTokenFormatException
	 * @throws KeystoreException
	 * @throws CommunicationException
	 * @throws InvalidDeviceTokenFormatException
	 * @throws JSONException
	 * @throws JSONException
	 * @throws
	 */
	public String pushNotify(List moduleList) throws CommunicationException,
			KeystoreException, InvalidDeviceTokenFormatException, JSONException {
		String rtn = "0";
		if (StringUtil.isNotEmpty(pushNotifyWebService.certFileRealPath)) {
			if (moduleList != null && moduleList.size() > 0) {
				/**
				 * 遍历所有包含推送消息的模块
				 */
				for (Object moduleNo : moduleList) {
					/**
					 * ios推送
					 */
					pushNotify.pushIt(moduleNo.toString(), "0");
					/**
					 * android推送
					 */
					pushNotify.pushIt(moduleNo.toString(), "1");
					
					updateToaPushNotify(null,moduleNo.toString());
					//每次推送完一个模块的消息就退出，等下次轮训，ios推送服务器的间隔时间是5分钟
					break;
				}//end for
			}
		}
		return rtn;
	}

	/**
	 * 清空数据库中推送记录的信息 author taoji
	 * 
	 * @param userId
	 * @date 2014-3-7 上午09:13:21
	 */
	public void updateToaPushNotify(String userId) {
		StringBuilder sb = new StringBuilder();
		sb.append("from ToaPushNotify t where 1=1 ");
		sb.append(" and t.userId= '" + userId + "'");
		List<ToaPushNotify> lst = pushDao.find(sb.toString());
		for (ToaPushNotify t : lst) {
			t.setMessageCount(0);
			pushDao.save(t);
		}
	}
	
	/**
	 * 清空数据库中推送记录的信息 author taoji
	 * 
	 * @param userId
	 * @date 2014-3-7 上午09:13:21
	 * 
	 * update by hecj 2014-3-22 16:45
	 */
	public void updateToaPushNotify(String userId,String moduleNo) {
		StringBuilder sb = new StringBuilder();
		List paramList=new LinkedList();
		sb.append("from ToaPushNotify t where 1=1 ");
		if(StringUtil.isNotEmpty(userId)){
			sb.append(" and t.userId= ?");
			paramList.add(userId);
		}
		if(StringUtil.isNotEmpty(moduleNo)){
			sb.append(" and t.moduleNo= ?");
			paramList.add(moduleNo);
		}
		List<ToaPushNotify> lst = pushDao.find(sb.toString(),paramList.toArray());
		for (ToaPushNotify t : lst) {
			t.setMessageCount(0);
			pushDao.save(t);
		}
	}
}