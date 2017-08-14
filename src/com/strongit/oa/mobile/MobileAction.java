package com.strongit.oa.mobile;


import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.im.bigant.BigAntBaseService;
import com.strongit.oa.sms.IsmsService;
import com.strongit.oa.util.GlobalBaseData;
import com.strongmvc.exception.SystemException;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 大蚂蚁中集成短信功能支持.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-6-28 上午11:02:39
 * @version  2.0.2.3
 * @classpath com.strongit.oa.mobile.MobileAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "mobile.action", type = ServletActionRedirectResult.class) })
public class MobileAction extends BaseActionSupport {

	private String userName;	//发送者登录名
	
	private String userRealName ;//用户姓名
	
	private String selectName;	//接收者登录名
	
	private String telephone;	//接收者电话号码
	
	private IUserService user;	//统一用户接口
	
	private IsmsService manager;//短信发送接口
	
	@Autowired BigAntBaseService bigAnt ;//大蚂蚁服务类.
	
	private String content;		//短信内容
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSelectName() {
		return selectName;
	}

	public void setSelectName(String selectName) {
		this.selectName = selectName;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 转到发送手机短信页面
	 * 
	 */
	@Override
	public String input() throws Exception {
		if(userName != null && !"".equals(userName)){
			/*User selectUser = user.getUserInfoByLoginName(selectName);
			HttpServletRequest req = ServletActionContext.getRequest();
			req.setAttribute("userName", userName);
			telephone = selectUser.getUserTel();*/
		}
		if(userName == null || "".equals(userName)){
			logger.error("参数userName不能为空。");
			throw new SystemException("参数userName不能为空。");
		}
		String userInfo = bigAnt.getUserInfo(selectName);
		JSONObject objInfo = JSONObject.fromObject(userInfo);
		userRealName = objInfo.getString("col_Name");
		telephone = objInfo.getString("col_Mobile");
		return "send";
	}
	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {

	}

	@Override
	public String save() throws Exception {
		return null;
	}
	
	public String send() throws Exception{
		try {
			if(userName != null && !"".equals(userName)){
			    User sendUser = user.getUserInfoByLoginName(userName);
			    if(sendUser.getUserId() == null){
			    	sendUser = user.getUserInfoByLoginName(userName.toLowerCase());
			    }
				manager.sendSmsByAnt( sendUser.getUserId(),telephone ,content, GlobalBaseData.SMSCODE_BIGANT);
				return this.renderText("true");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("大蚂蚁发送短信",e);
		}
		return this.renderText("false");
	}

	public Object getModel() {
		return null;
	}
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	@Autowired
	public void setManager(IsmsService manager) {
		this.manager = manager;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
}
