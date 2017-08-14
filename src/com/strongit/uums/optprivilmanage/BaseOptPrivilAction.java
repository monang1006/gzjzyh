package com.strongit.uums.optprivilmanage;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseSystem;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.util.PropertiesUtil;

import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "baseOptPrivil!input.action", type = ServletActionRedirectResult.class),
		@Result(name = "mytree", value = "baseOptPrivil!priviltree.action", type = ServletActionRedirectResult.class),
		@Result(name = "insert", value = "/WEB-INF/jsp/optprivilmanage/baseOptPrivil-insert.jsp", type = ServletDispatcherResult.class),
		@Result(name = "init",value = "/WEB-INF/jsp/usermanage/usermanage-init.jsp",type = ServletDispatcherResult.class),
		@Result(name = "setoptprivil",value = "/WEB-INF/jsp/usermanage/usermanage-setoptprivil.jsp",type = ServletDispatcherResult.class),
		@Result(name = "noset",value = "/WEB-INF/jsp/usermanage/usermanage-noset.jsp",type = ServletDispatcherResult.class),
		@Result(name = "copyoper",value = "/WEB-INF/jsp/usermanage/usermanage-copyoper.jsp",type = ServletDispatcherResult.class)})
public class BaseOptPrivilAction extends BaseActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3252633443486404004L;

	private Page<ToaUumsBaseOperationPrivil> page = new Page<ToaUumsBaseOperationPrivil>(FlexTableTag.MAX_ROWS, true);

	private String privilId;//权限ID

	private String codeType;

	private String code;

	private String systemId;
	
	private List<Organization> orgList;
	
	private String orgId;
	
	private List<User> userList;
	
	/**
	 * 操作权限列表
	 */
	private List<ToaUumsBaseOperationPrivil> optPrivilLst = new ArrayList<ToaUumsBaseOperationPrivil>();

	private List<ToaUumsBaseOperationPrivil> privilList;

	private ToaUumsBaseOperationPrivil model = new ToaUumsBaseOperationPrivil();

	private BaseOptPrivilManager manager;

	@Autowired
	private IUserService userService;

	private List<TUumsBaseSystem> systemList;

	private String orgcode;

	private String message;

	// 是否将下级权限设置为启用状态标识
	private String childTogether;

	// 权限系统编码
	private String privilSyscode;
	
	private String userId;//用户ID
	
	private String copytoid;//要复制到的ID
	
	private String initPrivil = "";
	
	/**
	 * 岗位ID（id1,id2...）
	 */
	private String postId;
	
	public String getPrivilSyscode() {
		return privilSyscode;
	}

	public void setPrivilSyscode(String privilSyscode) {
		this.privilSyscode = privilSyscode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public Page<ToaUumsBaseOperationPrivil> getPage() {
		return page;
	}

	public BaseOptPrivilAction() {
	}

	@Override
	public String delete() throws Exception {
		/*try {
			manager.deletePrivils(privilId);
			addActionMessage("删除成功");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		return renderHtml("<script>parent.PrivilTree.location='"
				+ getRequest().getContextPath()
				+ "/optprivilmanage/baseOptPrivil!priviltree.action';parent.PrivilContent.location='"
				+ getRequest().getContextPath()
				+ "/optprivilmanage/baseOptPrivil!insert.action'</script>");*/
		return null;

	}

	@Override
	public String input() throws Exception {
		prepareModel();
		return INPUT;
	}

	/*public String insert() throws Exception {
		UserInfo currentUser = (UserInfo) this.getUserDetails();
		//当前用户为超级管理员或管理员身份
		if ("1".equals(currentUser.getUserIsSupManager()) || "1".equals(manager.isUserManager(currentUser.getUserId()))) {
			prepareModel();
			String nextCode = manager.getNextPrivilCode(code);
			if (nextCode.equals(code) && privilId != null && !"".equals(privilId)) {
				return this
						.renderHtml("<script>alert('权限已达最底层，无法添加！');location='"
								+ this.getRequest().getContextPath()
								+ "/optprivilmanage/baseOptPrivil!input.action?privilId="
								+ privilId + "'</script>");
			}
			model.setPrivilSyscode(nextCode);
			return "insert";
		} else {
			return "canntinsert";
		}
	}*/

	/**
	 * author:dengzc
	 * description:展现操作权限树
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String priviltree() throws Exception {
		try {
			privilList = manager.getCurrentUserPrivilLst(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		codeType = PropertiesUtil.getCodeRule(GlobalBaseData.RULE_CODE_PRIVIL);
		return "priviltree";
	}

	@Override
	public String list() throws Exception {
		//page = manager.queryPrivils(page);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		/*if (privilId != null) {
			model = manager.getPrivil(privilId);
		} else {
			model = new ToaUumsBaseOperationPrivil();
			String nextSequence = manager.getNextSequenceCode();
			model.setPrivilSequence(nextSequence);
		}
		systemList = systemManager.getAllSystems();*/
	}

	@Override
	public String save() throws Exception {

		/*if ("".equals(model.getPrivilId())) {
			model.setPrivilId(null);
		}
		String returnMessage = manager.savePrivil(model, childTogether);

		//Added By dengzc 加入更新权限缓存的代码
		if(model.getPrivilId() != null && PrivilHelper.checkHas(model.getPrivilId())){//若权限缓存中存在指定的权限key值,这更新缓存中对应的缓存对象
			PrivilHelper.addPrivil(model.getPrivilId(),model);
		}
		addActionMessage(returnMessage);*/
		return renderHtml("<script>parent.PrivilTree.location='"
				+ getRequest().getContextPath()
				+ "/optprivilmanage/baseOptPrivil!priviltree.action';window.location='"
				+ getRequest().getContextPath()
				+ "/optprivilmanage/baseOptPrivil!insert.action'</script>");

	}

	public ToaUumsBaseOperationPrivil getModel() {

		return model;
	}

	/**
	 * 权限上移
	 * 
	 * @author 喻斌
	 * @date Feb 11, 2009 9:49:47 AM
	 * @return
	 * @throws Exception
	 */
	/*public String moveUp() throws Exception {
		prepareModel();
		String mes = manager.movePrivilPositionInTree(model,
				GlobalBaseData.MOVE_UP_IN_TREE);
		if (!"".equals(mes)) {
			this.addActionMessage(mes);
			this.message = mes;
		}
		return priviltree();
	}
*/
	/**
	 * 权限下移
	 * 
	 * @author 喻斌
	 * @date Feb 11, 2009 9:50:00 AM
	 * @return
	 * @throws Exception
	 */
	/*public String moveDown() throws Exception {
		prepareModel();
		String mes = manager.movePrivilPositionInTree(model,
				GlobalBaseData.MOVE_DOWN_IN_TREE);
		if (!"".equals(mes)) {
			this.addActionMessage(mes);
			this.message = mes;
		}
		return priviltree();
	}*/

	/**
	 * 检验编码合法性
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-25 下午02:17:49
	 * @return String
	 * @throws Exception
	 */
	/*public String compareCode() throws Exception {
		boolean flag = manager.compareCode(orgcode);
		boolean iserr = manager.checkCode(orgcode);
		boolean isParentExist = manager.checkParentIsExist(orgcode, privilId);
		message = "111";
		
		if(!flag){
			message = "000";// 编码不唯一
		}else if(!iserr){
			message = "333";// 编码不符合规则
		}else if(!isParentExist){
			message = "222";// 该编码的父级不存在
		}
		return this.renderText(message);
	}*/


	/**
	 * 设置操作权限
	 * @author 邓志城
	 * @return
	 */
	/*public String setOptPrivil() {
		manager.setOptPrivil(userId, privilId);
		addActionMessage("设置权限成功");
		return "init";
	}*/

	
	/**
	 * 复制操作权限
	 * @author:邓志城
	 * @date:2009-8-3 下午06:01:45
	 * @return
	 */
	/*public String setCopyOper(){
		manager.copyUserOperPrivil(userId, copytoid);
		addActionMessage("复制操作权限成功");
		return "init";
	}
	
	*//**
	 * 为岗位设置操作权限
	 * @author:邓志城
	 * @date:2009-5-14 上午10:23:40
	 *//*
	public String setPostPrivil()throws Exception{
		manager.setPostOptPrivil(postId, privilId);
		return renderHtml("<script>window.dialogArguments.location='"+getRequest().getContextPath()+"/postmanage/postContent!list.action';window.close();</script>");
	}*/

	/**
	 * 展现操作权限树(启用状态的权限),用于给用户赋权
	 * @author 邓志城
	 */
/*	public String getOptPrivil() throws Exception{
		User user = manager.getUserInfoByUserId(userId);
		if (GlobalBaseData.IS_YES.equals(user.getUserIsdel())) {
			addActionMessage("用户已经删除不能操作");
			return "noset";
		}
		List<String> lst = manager.getUserPrivilLst(userId);
		if(lst!=null && lst.size()>0){
			for(String privil:lst){
				initPrivil += "," + privil;
			}
			if (initPrivil.length() > 0) {
				initPrivil = initPrivil.substring(1);
			}
		}
		optPrivilLst = manager.getCurrentUserPrivilLst(true);//true：启用状态的权限
		codeType = PropertiesUtil.getCodeRule(GlobalBaseData.RULE_CODE_PRIVIL);
		String rootPrivilInfo = manager.getRootPrivil(userId);
		getRequest().setAttribute("rootPrivil", rootPrivilInfo);
		return "setoptprivil";
	}*/

	/**
	 * 异步加载操作权限
	 * @author:邓志城
	 * @date:2009-9-11 下午06:52:10
	 * @return
	 * @throws Exception
	 */
/*	public String getChildOptPrivil() throws Exception {
		try{
			String html = manager.getChildPrivil(userId, code);
			this.renderHtml(html);
		}catch(Exception e){
			e.printStackTrace();
			LogPrintStackUtil.logExceptionInfo(e,"加载权限信息出错！");
			this.renderHtml("加载权限信息出错！");
		}
		return null;
	}*/
		
	
	@Autowired
	public void setManager(BaseOptPrivilManager manager) {
		this.manager = manager;
	}

	public String getPrivilId() {
		return privilId;
	}

	public void setPrivilId(String privilId) {
		this.privilId = privilId;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public List<ToaUumsBaseOperationPrivil> getPrivilList() {
		return privilList;
	}

	public void setPrivilList(List<ToaUumsBaseOperationPrivil> privilList) {
		this.privilList = privilList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<TUumsBaseSystem> getSystemList() {
		return systemList;
	}

	public void setSystemList(List<TUumsBaseSystem> systemList) {
		this.systemList = systemList;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getChildTogether() {
		return childTogether;
	}

	public void setChildTogether(String childTogether) {
		this.childTogether = childTogether;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCopytoid() {
		return copytoid;
	}

	public void setCopytoid(String copytoid) {
		this.copytoid = copytoid;
	}

	public String getInitPrivil() {
		return initPrivil;
	}

	public void setInitPrivil(String initPrivil) {
		this.initPrivil = initPrivil;
	}

	public List<ToaUumsBaseOperationPrivil> getOptPrivilLst() {
		return optPrivilLst;
	}

	public List<Organization> getOrgList() {
		return orgList;
	}

	public List<User> getUserList() {
		return userList;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
