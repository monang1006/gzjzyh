package com.strongit.oa.webservice.verificationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.strongit.oa.bo.TUumsUserandip;
import com.strongit.oa.bo.ToaIMEI;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.privilmanage.IPrivilManager;
import com.strongit.uums.usermanage.IUserManager;
import com.strongit.uums.util.Const;
import com.strongit.uums.util.WebserviceResponseStrUtil;
import com.strongit.uums.webservice.AuthenticationHandler;
import com.strongit.uums.webservice.UserInfoForService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.service.ServiceLocator;

public class AuthentiUpdatecationService extends ServletEndpointSupport {
	/**
	 * 人员管理类
	 * @see UserManager
	 */
	private IUserManager userManager = null;
	
	protected IUserManager getUserManager(){
		if(userManager == null){
			userManager = (IUserManager)ServiceLocator.getService("userManager");
		}
		return userManager;
	}
	
	/**
	 * 权限管理类
	 * @see BasePrivilManager
	 */
	private IPrivilManager privilManager = null;
	
	protected IPrivilManager getPrivilManager(){
		if(privilManager == null){
			privilManager = (IPrivilManager)ServiceLocator.getService("basePrivilManager");
		}
		return privilManager;
	}

	private SystemsetManager systemsetManager = null;
	
	protected SystemsetManager getSystemsetManager(){
		if(systemsetManager == null){
			systemsetManager = (SystemsetManager)ServiceLocator.getService("systemsetManager");
		}
		return systemsetManager;
	}
	
	
	/**
	 * 根据用户名和密码进行用户登录验证服务
	 * @author 喻斌
	 * @date May 8, 2009 3:36:42 PM
	 * @param userLoginName -用户登录名
	 * @param password -用户密码
	 * 		  String IMEI
	 * 			  设备唯一标示的id
	 * 		  String deviceType
	 * 			  设备类型 0:android 1:iphone 2:ipad
	 * @return 根据接口规范构造的登录用户信息XML字符串
	 */
	public String userLogin(String userLoginName, String password,String IMEI,String deviceType){
		try{
			TUumsBaseUser userInfo = getUserManager().getUserInfoByLoginname(userLoginName);
			MessageContext mc = MessageContext.getCurrentContext();
			HttpServletRequest req = (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
			String ip = req.getHeader("x-forwarded-for");
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = req.getHeader("Proxy-Client-IP");
	        }
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = req.getHeader("WL-Proxy-Client-IP");
	        }
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = req.getRemoteAddr();
	        }
			if(userInfo == null){
		        TUumsUserandip tuip = new TUumsUserandip(null,ip,new Date(),IMEI,"0","用户数据不存在",deviceType);
				getSystemsetManager().save(tuip);
				return WebserviceResponseStrUtil.getInstance().createFailResponse("用户数据不存在");
			} else if (Const.IS_YES.equals(userInfo.getUserIsdel())) {
				TUumsUserandip tuip = new TUumsUserandip(userInfo.getUserId(),ip,new Date(),IMEI,"0",userLoginName + "已经被删除!",deviceType);
				getSystemsetManager().save(tuip);
				return WebserviceResponseStrUtil.getInstance().createFailResponse(userLoginName + "已经被删除!");
			} else if (Const.IS_NO.equals(userInfo.getUserIsactive())) {
//				return WebserviceResponseStrUtil.getInstance().createFailResponse(userLoginName + "已经被禁用!");
				TUumsUserandip tuip = new TUumsUserandip(userInfo.getUserId(),ip,new Date(),IMEI,"0","对不起，您的账号未被启用!",deviceType);
				getSystemsetManager().save(tuip);
				return WebserviceResponseStrUtil.getInstance().createFailResponse("对不起，您的账号未被启用!");
			/*} else if (userInfo.getUserIsSupManager() == null
						|| Const.IS_NO.equals(userInfo.getUserIsSupManager())) {
				if (userInfo.getBaseSysmanagers() == null
						|| userInfo.getBaseSysmanagers().size() == 0) {
					return WebserviceResponseStrUtil.getInstance().createFailResponse(userLoginName
							+ "不具有管理员以上权限,无法登录本系统!");
				}*/
			} else if(!password.equals(userInfo.getUserPassword())){
				TUumsUserandip tuip = new TUumsUserandip(userInfo.getUserId(),ip,new Date(),IMEI,"0",userLoginName+ "用户密码错误!",deviceType);
				getSystemsetManager().save(tuip);
				return WebserviceResponseStrUtil.getInstance().createFailResponse(userLoginName
							+ "用户密码错误!");
			} else {
				List<String> authsList = new ArrayList<String>();

				/**
				 * 获取当前登录用户的所有权限
				 */
				List<TUumsBasePrivil> privils = getPrivilManager().getPrivilInfosByUserLoginName(userInfo.getUserLoginname(),
						Const.IS_YES, Const.IS_YES, Const.IS_YES);
				if (privils != null) {
					for (TUumsBasePrivil privil : privils) {
						/**
						 * 唯一确定一个权限的编码为：权限所属系统编码-权限编码
						 * 未启用权限过滤
						 */
						if(Const.IS_YES.equals(privil.getPrivilIsactive())){
							authsList.add(privil.getBasePrivilType().getBaseSystem().getSysSyscode()
									+ "-" + privil.getPrivilSyscode());
						}
					}
				}
				/**
				 * 判断登入用户是否是手机端登入用户
				 * 查询用户id与手机imei码中间表  有数据则登入进去否者返回错误
				 */
				String tempImie = "0";
				/**
				 * 开关设置 如果值为1则不进行iemi验证 如果为0则iemi验证开启
				 */
				Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("webserviceaddress.properties"));
				String isClose=prop.getProperty("mobile_imei_valid_close");
				if(!"1".equals(isClose)){
					List<ToaIMEI> t = getSystemsetManager().findIMEIList(userInfo.getUserId(), "", "", "");
					if(t==null||t.size()==0){
						TUumsUserandip tuip = new TUumsUserandip(userInfo.getUserId(),ip,new Date(),IMEI,"0","该手机未在系统中与本用户关联，关联后才能登入！",deviceType);
						getSystemsetManager().save(tuip);
						return WebserviceResponseStrUtil.getInstance().createFailResponse("该手机未在系统中与本用户关联，关联后才能登入！");
					}else{
						for(ToaIMEI tti : t){
							if(tti.getIemiCode().equals(IMEI)){
								tempImie = "1";
								break;
							}
						}
					}
					if("0".equals(tempImie)){
						TUumsUserandip tuip = new TUumsUserandip(userInfo.getUserId(),ip,new Date(),IMEI,"0","该手机未在系统中与本用户关联，关联后才能登入！",deviceType);
						getSystemsetManager().save(tuip);
						return WebserviceResponseStrUtil.getInstance().createFailResponse("该手机未在系统中与本用户关联，关联后才能登入！");
					}
				}
				//将本次ip存入数据库
				TUumsUserandip tuip = new TUumsUserandip(userInfo.getUserId(),ip,new Date(),IMEI,"1","登入成功！",deviceType);
				getSystemsetManager().save(tuip);
				
				UserInfoForService user = new UserInfoForService();

				/**
				 * 将用户当前用户信息保存到安全上下文中，以方便系统获取
				 */
				user.setUserLoginname(userInfo.getUserLoginname());
				user.setRest1(userInfo.getRest1());
				user.setRest2(userInfo.getRest2());
				user.setRest3(userInfo.getRest3());
				user.setRest4(userInfo.getRest4());
				user.setUserId(userInfo.getUserId());
				user.setUserEmail(userInfo.getUserEmail());
				user.setUserIsactive(userInfo.getUserIsactive());
				user.setUserName(userInfo.getUserName());
				user.setUserSyscode(userInfo.getUserSyscode());
				user.setUserAddr(userInfo.getUserAddr());
				user.setUserTel(userInfo.getUserTel());
				user.setUserPubkey(userInfo.getUserPubkey());
				user.setUserUsbkey(userInfo.getUserUsbkey());
				user.setUserIsSupManager(userInfo.getUserIsSupManager());
				user.setAuthorities(authsList);
				user.setUserPassword(userInfo.getUserPassword());
				
				user.setSessionId(req.getSession().getId());
				
				//AuthenticationHandler.addUserInfo(user.getSessionId(), user);
				
				AuthenticationHandler.addUserInfo(user.getSessionId(), user);
				
				return WebserviceResponseStrUtil.getInstance().createSuccessResponse(user, 3, null);
			}
		}catch(DAOException ex){
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_DAO_EXCEPTION);
		}catch(SystemException ex){
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_SYSTEM_EXCEPTION);
		}catch(ServiceException ex){
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_SERVICE_EXCEPTION);
		}catch(Exception ex){
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_ELSE_EXCEPTION);
		}
	}
	
	/**
	 * 判断当前传入用户是否拥有指定权限模块的权限
	 * @author 喻斌
	 * @date May 11, 2009 4:14:33 PM
	 * @param sessionId
	 *            -安全校验标识，目前只在Service Handler中使用（由于修改为SSL方式保证Web Service安全性，故该字段已经失效，但为了保证向下兼容，保留该字段，但已无作用）
	 * @param subSystemCode -系统在统一用户中的注册编号
	 * @param userLoginName -用户登录名
	 * @param privilModuleCode -权限模块编号
	 * @return 根据接口规范构造的XML字符串
	 */
	public String userModuleValidate(String sessionId, String subSystemCode, String userLoginName, String privilModuleCode){
		try{
			TUumsBaseUser userInfo = getUserManager().getUserInfoByLoginname(userLoginName);
			
			if(userInfo == null){
				return WebserviceResponseStrUtil.getInstance().createFailResponse("用户数据不存在");
			} else if (Const.IS_YES.equals(userInfo.getUserIsdel())) {
				return WebserviceResponseStrUtil.getInstance().createFailResponse(userLoginName + "已经被删除!");
			} else if (Const.IS_NO.equals(userInfo.getUserIsactive())) {
				return WebserviceResponseStrUtil.getInstance().createFailResponse(userLoginName + "已经被禁用!");
			} else {
				List<TUumsBasePrivil> privils = getPrivilManager().getPrivilInfosByUserLoginName(userInfo.getUserLoginname(),
						Const.IS_YES, Const.IS_YES, Const.IS_YES);
				if (privils != null) {
					for (TUumsBasePrivil privil : privils) {
						if(privilModuleCode.equals(privil.getPrivilSyscode()) && 
								subSystemCode.equals(privil.getBasePrivilType().getBaseSystem().getSysSyscode())){
							return WebserviceResponseStrUtil.getInstance().createSuccessResponse(1, 1, null);
						}
					}
				}
				return WebserviceResponseStrUtil.getInstance().createSuccessResponse(0, 1, null);
			}
		}catch(DAOException ex){
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_DAO_EXCEPTION);
		}catch(SystemException ex){
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_SYSTEM_EXCEPTION);
		}catch(ServiceException ex){
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_SERVICE_EXCEPTION);
		}catch(Exception ex){
			ex.printStackTrace();
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_ELSE_EXCEPTION);
		}
	}
	
	
	/**
	 * 登录用户登出统一用户系统
	 * @author 喻斌
	 * @date May 12, 2009 8:42:36 AM
	 * @deprecated 由于修改为SSL方式保证Web Service安全性，故该方法已失效
	 * @param sessionId
	 *            -安全校验标识，目前只在Service Handler中使用
	 * @return 根据接口规范构造的XML字符串
	 */
	public String userLogout(String sessionId,String userLoginName,String IMEI,String deviceType){
		try{
			/*Object sessionValidate = MessageContext
					.getCurrentContext().getSession().get(Const.CURRENT_SERVICE_VALIDITY);
			if(Const.IS_NO.equals(String.valueOf(sessionValidate))){
				return WebserviceResponseStrUtil.getInstance()
								.createFailResponse(Const.SERVICE_AUTHENTICATION_EXCEPTION);
			}
			AuthenticationHandler.removeUserInfo(sessionId);*/
			//将本次ip存入数据库
			MessageContext mc = MessageContext.getCurrentContext();
			HttpServletRequest req = (HttpServletRequest) mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
			String ip = req.getHeader("x-forwarded-for");
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = req.getHeader("Proxy-Client-IP");
	        }
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = req.getHeader("WL-Proxy-Client-IP");
	        }
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = req.getRemoteAddr();
	        }
			TUumsBaseUser userInfo = getUserManager().getUserInfoByLoginname(userLoginName);
			TUumsUserandip tuip = new TUumsUserandip(userInfo.getUserId(),ip,new Date(),IMEI,"1","退出系统！",deviceType);
			getSystemsetManager().save(tuip);
			return WebserviceResponseStrUtil.getInstance()
								.createSuccessResponse(null, 1, null);
		}catch(DAOException ex){
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_DAO_EXCEPTION);
		}catch(SystemException ex){
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_SYSTEM_EXCEPTION);
		}catch(ServiceException ex){
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_SERVICE_EXCEPTION);
		}catch(Exception ex){
			return WebserviceResponseStrUtil.getInstance().createFailResponse(Const.SERVICE_ELSE_EXCEPTION);
		}
	}
}
