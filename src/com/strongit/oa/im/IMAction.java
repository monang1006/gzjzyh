package com.strongit.oa.im;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.Cookie;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.ui.rememberme.AbstractRememberMeServices;
import org.springframework.util.StringUtils;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.strongit.oa.bo.ToaImConfig;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.im.config.IMConfigManager;
import com.strongit.oa.im.service.AbstractBaseService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.util.PathUtil;
import com.strongmvc.exception.SystemException;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date 2009年2月18日13:37:49
 * @version 1.0.0.0
 * @comment IMAction
 */
@ParentPackage("default")
public class IMAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3206429541559630489L;
	private IMManager manager;
	
	private String content;// 发送的消息内容
	private String receiveId;// 接收人id,多个接收人以“,”隔开
	private String deptId;
	private String type; // 大蚂蚁中关系表中用到

	private String userName;// 登录rtx的用户名、密码
	private String password;

	private String clientport;// RTX客户端登录端口

	private String rtxStart;// 登录时是否启动rtx

	private String isPop;// 登录时是否启动rtx

	public ToaImConfig model = new ToaImConfig();

	public List<Object[]> baseServiceInfo = new ArrayList<Object[]>();

	@Autowired
	IUserService userService;

	@Autowired
	IMConfigManager iMConfigManager;
	private MyLogManager myLogManager;

	@Override
	public String delete() throws Exception {
		return null;
	}

	/**
	 * 转到发送即时消息页面
	 */
	@Override
	public String input() throws Exception {
		return "input";
	}

	/**
	 * 获取IM软件下的部门及用户
	 */
	@Override
	public String list() throws Exception {
		this.logger.info("-------------------列出IM的组织构架啦");
		try {
			if (manager.isEnabled()) {
				Object[] objs = manager.imTreeInfo(deptId);
//				Object[] objs = rtxManager.imTreeInfo(deptId);
				
				if (objs != null && objs.length >= 2) {
					getRequest().setAttribute("orgList", objs[0]);
					getRequest().setAttribute("hasChild", objs[1]);
				}
			} else {
				this.logger.info("未启用RTX，无法列出子部门或用户");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error(e.getMessage(), e);
			System.out.println(e.getMessage());
		}
		return "synuser";
	}

	/**
	 * author:dengzc description:异步加载RTX中的树 modifyer: description:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String synajaxtree() throws Exception {
		this.logger.info("----------->deptId:" + deptId);
		try {
			renderHtml(manager.imUserInfo(deptId, type));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * 用户数据同步到即时通讯软件
	 * 
	 * @author:邓志城
	 * @date:2010-6-2 上午09:03:29
	 * @return
	 */
	public String oa2im() throws Exception {
		try {
			manager.oa2im();
			renderText("ok");
			//日志信息
			String ip = getRequest().getRemoteAddr();
			String logInfo = "";
			logInfo = "同步即时通讯";					
			myLogManager.addLog(logInfo, ip);
		} catch (Exception e) {
			renderText("error");
		}
		return null;
	}

	/**
	 * author:dengzc description:获取并检查rtx登录信息 modifyer: description:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initLoginRtx() throws Exception {
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		User user = userService.getCurrentUser();
		if (userName == null || "".equals(userName)) {
			userName = user.getUserLoginname();
		}
		try {
			// 先判断当前登录用户在RTX中是否存在
//			boolean isExist  = rtxManager.isUserExist(userName);
			boolean isExist = manager.isUserExist(userName);
			int iState = manager.getUserStatus(userName);

// boolean isExist = true;
			if (!(iState == 1)) {
				if (isExist) {
					// exist
					ToaImConfig config = Cache.get();
					String serverIP = config.getImconfigIp();
					String serverPort = config.getImconfigClientPort();
					String sessionKey = manager.getSessionKey(userName);
					obj.put("status", "ok");
					obj.put("ip", serverIP);
					obj.put("port", serverPort);
					obj.put("sessionkey", sessionKey);
					obj.put("userName", userName);
					array.add(obj);
					renderText(array.toString());
					logger.info("得到用户信息：" + array.toString());
				} else {
					obj.put("status", "no");
					obj.put("userName", userName);
					array.add(obj);
					renderText(array.toString());
				}

			}
			
		} catch (Exception e) {
			obj.put("status", "error");e.printStackTrace();
			array.add(obj);
			renderText(array.toString());
		}
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {

	}

	/**
	 * author:dengzc description:发送即时消息 modifyer: description:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String sendIM() throws Exception {
		if (receiveId == null) {
			throw new SystemException("param receiveId is null.");
		}
		List<String> receiver = Arrays.asList(receiveId.split(","));
		String ret = manager.sendIMMessage(content, receiver, null);
		JSONObject jsobj = new JSONObject();
		jsobj.put("result", ret);
		return renderText(ret);
	}

	/**
	 * 读取IM服务器配置参数
	 * 
	 * @author:邓志城
	 * @date:2010-6-1 下午04:19:49
	 * @return
	 * @throws Exception
	 */
	public String serverConfig() throws Exception {
		model = iMConfigManager.getConfig();
		// 加载资源文件
		String resourceName = ServletActionContext.getServletContext()
				.getInitParameter("messageResource");
		LocalizedTextUtil.addDefaultResourceBundle(resourceName);
		logger.info("load resource file " + resourceName);
		List<AbstractBaseService> baseServiceList = manager.findAllImService();
		for (AbstractBaseService baseService : baseServiceList) {
			String name = baseService.getClass().getName();
			logger.info("find imService:" + baseService);
			String value = getText(name);
			logger.info("im's name is : " + value);
			baseServiceInfo.add(new String[] { name, value });
		}
		// add by yangwg
		baseServiceInfo.add(new String[] {
				"com.strongit.oa.im.rtx.RTX2010BaseService", "RTX2010版" });
		return "config";
	}

	/**
	 * 得到即时通讯软件配置信息.
	 * 
	 * @author:邓志城
	 * @date:2010-6-1 下午05:29:53
	 * @return
	 * @throws Exception
	 */
	public String findServerConfigBySyn() throws Exception {
		try {
			model = Cache.get();
			JSONObject obj = new JSONObject();
			obj.put("ip", model.getImconfigIp());
			obj.put("port", model.getImconfigPort());
			obj.put("state", model.getImconfigState());
			renderText(obj.toString());
		} catch (Exception e) {
			renderText("error");
		}
		return null;
	}

	/**
	 * 更新即时通讯软件配置
	 * 
	 * @author:邓志城
	 * @date:2010-6-1 下午04:42:53
	 * @return "0":成功；“1”：操作失败.
	 * @throws Exception
	 */
	public String saveServerConfig() throws Exception {
		String ret = "";
		try {
			iMConfigManager.updateConfig(model);
			Cache.put(model);

			// 处理轮询器
			if (Cache.getService().isLoopEnabled()) {
				PathUtil.getIMListener().start();
			} else {
				PathUtil.getIMListener().destory();
			}
			ret = "0";
		} catch (Exception e) {
			logger.error("saveServeConfig()", e);
			ret = "1";
		}
		return renderText(ret);
	}

	/**
	 * 校验是否启动了即时通讯软件.
	 * <P>
	 * 若启动了，则将启动状态和登录名写入SESSION
	 * </P>
	 * 
	 * @author:邓志城
	 * @date:2010-6-1 下午04:13:14
	 * @return
	 * @throws Exception
	 * 
	 * 增加Cooike记录用户名功能 2010年9月6日9:43:55
	 */
	public String checkImStart() throws Exception {
		try {
			getRequest().getSession().setAttribute("rtxStart", rtxStart);
			getRequest().getSession().setAttribute("rtxLoginName", userName);
			getRequest().getSession().setAttribute("imEnabled", String.valueOf(manager.isEnabled()));
			getRequest().getSession().setAttribute("isPop", isPop);
			getRequest().getSession().setAttribute("ExpresslyPassword", password);// 保存用户密码明文,用于发送大蚂蚁即时消息.
			// Linux 系统部署，需要修改获取SessionKey 方式
			String sessionKey = "";
			try {
				sessionKey = manager.getSessionKey(userName);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
 			getRequest().getSession().setAttribute("sessionKey", sessionKey);
			renderText("ok");
			// 增加Cooike记录用户名功能
			String cookieValue = userName;// encodeCookie(userName);
			cookieValue = URLEncoder.encode(cookieValue, "utf-8");
			Cookie cookie = new Cookie(
					AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY,
					cookieValue);
			String maxAge = getText(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
			if (maxAge != null
					&& !AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY
							.equals(maxAge)) {
				Integer IntMaxAge = Integer.parseInt(maxAge);
				cookie.setMaxAge(IntMaxAge);
				logger.info("有效期为:" + IntMaxAge + "秒.");
			} else {
				cookie.setMaxAge(7 * 24 * 60 * 60);
			}
			cookie.setPath(StringUtils.hasLength(getRequest()
							.getContextPath()) ? getRequest().getContextPath()
							: "/");
			getResponse().addCookie(cookie);
			logger.info("Added remember-me cookie for user '" + userName
					+ "', expiry: '" + new Date(7 * 24 * 60 * 60) + "'");
		} catch (Exception e) {
			logger.error("checkImStart()", e);
			getRequest().getSession().removeAttribute("rtxStart");
			getRequest().getSession().removeAttribute("rtxLoginName");
			getRequest().getSession().removeAttribute("isPop");
			getRequest().getSession().removeAttribute("imEnabled");
			getRequest().getSession().removeAttribute("ExpresslyPassword");
			renderText("error");
			logger.info("Cancelling cookie");
			Cookie cookie = new Cookie(
					AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY,
					null);
			cookie.setMaxAge(0);
			cookie.setPath(StringUtils.hasLength(getRequest()
							.getContextPath()) ? getRequest().getContextPath()
							: "/");
			getResponse().addCookie(cookie);
		}
		return null;
	}

	/**
	 * @author:luosy
	 * @description:	根据用户名密码检查用户是否可用
	 * @date : 2011-11-27
	 * @modifyer:
	 * @description: 
	 * @return
	 * @throws Exception
	 */
	public String checkUserActive() throws Exception {
		
		//MD5计算
		String password = getRequest().getParameter("password");
		String loginName = getRequest().getParameter("username");
		
		try{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			char[] charArray = password.toCharArray();
			byte[] byteArray = new byte[charArray.length];
			for (int i=0; i<charArray.length; i++){
				byteArray[i] = (byte) charArray[i];
			}
			byte[] md5Bytes = md5.digest(byteArray);
			StringBuffer hexValue = new StringBuffer();
			for (int i=0; i<md5Bytes.length; i++){
				int val = ((int) md5Bytes[i] ) & 0xff; 
				if (val < 16) hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}
			
			String passwordMd5 = hexValue.toString();
			
			User user= userService.getUserInfoByLoginName(loginName);
			if(user!=null){
				String userPsd = user.getUserPassword();
				String userisActive = user.getUserIsactive();
				String userisDel = user.getUserIsdel();
				
				if("0".equals(userisDel)&&"1".equals(userisActive)&&passwordMd5.equals(userPsd)){
					return renderText("begin[1]end");
				}
			}
		}catch (Exception e){
		}
		return renderText("begin[0]end");
	}
	
	@Override
	public String save() throws Exception {
		return null;
	}

	public Object getModel() {
		return model;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getReceiveId() {
		return receiveId;
	}

	@Autowired
	public void setManager(IMManager manager) {
		this.manager = manager;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRtxStart() {
		return rtxStart;
	}

	public void setRtxStart(String rtxStart) {
		this.rtxStart = rtxStart;
	}

	public String getClientport() {
		return clientport;
	}

	public void setClientport(String clientport) {
		this.clientport = clientport;
	}

	public String getIsPop() {
		return isPop;
	}

	public void setIsPop(String isPop) {
		this.isPop = isPop;
	}

	public List<Object[]> getBaseServiceInfo() {
		return baseServiceInfo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MyLogManager getMyLogManager() {
		return myLogManager;
	}

	public void setMyLogManager(MyLogManager myLogManager) {
		this.myLogManager = myLogManager;
	}

}
