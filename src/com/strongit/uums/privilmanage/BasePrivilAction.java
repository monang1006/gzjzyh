package com.strongit.uums.privilmanage;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.uums.bo.TUumsBasePrivilType;
import com.strongit.uums.bo.TUumsBaseSystem;
import com.strongit.oa.common.user.IUserService;

import com.strongit.uums.security.UserInfo;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "basePrivil!input.action", type = ServletActionRedirectResult.class),
		@Result(name = "mytree", value = "basePrivil!priviltree.action", type = ServletActionRedirectResult.class),
		@Result(name = "insert", value = "/WEB-INF/jsp/privilmanage/basePrivil-insert.jsp", type = ServletDispatcherResult.class) })
public class BasePrivilAction extends BaseActionSupport {
	private Page<TUumsBasePrivil> page = new Page<TUumsBasePrivil>(FlexTableTag.MAX_ROWS, true);

	private String privilId;

	private String codeType;

	private String code;

	private String systemId;

	private List<TUumsBasePrivil> privilList;

	private TUumsBasePrivil model = new TUumsBasePrivil();
	
	//资源权限类型model类
	private TUumsBasePrivilType privilType = new TUumsBasePrivilType();

	@Autowired IUserService userService;

	private List<TUumsBaseSystem> systemList;

	private String orgcode;

	private String message;

	// 是否将下级权限设置为启用状态标识
	private String childTogether;

	// 权限系统编码
	private String privilSyscode;
	
	//资源类型select元素列表
	private List<PrivilTypeSelectItemBean> typeLst;
	
	//资源类型Id
	private String typeId;
	
	//资源视图切换标识：systemTree; typeTree
	private String viewChangeFlag;

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

	public Page<TUumsBasePrivil> getPage() {

		return page;
	}

	public BasePrivilAction() {
		
	}

	@Override
	public String delete() throws Exception {
		try {
			userService.deletePrivils(privilId);
			addActionMessage("删除成功");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		return renderHtml("<script>parent.PrivilTree.location='"
				+ getRequest().getContextPath()
				+ "/privilmanage/basePrivil!priviltree.action?viewChangeFlag=" + viewChangeFlag + "';parent.PrivilContent.location='"
				+ getRequest().getContextPath()
				+ "/privilmanage/basePrivil!insert.action?viewChangeFlag=" + viewChangeFlag + "';</script>");

	}

	@Override
	public String input() throws Exception {
		try {
			prepareModel();
			if(model.getBasePrivilType() != null){
				typeId = model.getBasePrivilType().getTypeId();
			}
			return INPUT;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public String insert() throws Exception {
		UserInfo currentUser = (UserInfo) this.getUserDetails();
		if (userService.checkUserIsManager(currentUser.getUserId())) {
			prepareModel();
			String nextCode = userService.getNextPrivilCode(code);
			if (nextCode.equals(code) && privilId != null && !"".equals(privilId)) {
				return this
						.renderHtml("<script>alert('权限已达最底层，无法添加！');location='"
								+ this.getRequest().getContextPath()
								+ "/privilmanage/basePrivil!input.action?viewChangeFlag='" + viewChangeFlag + "&privilId="
								+ privilId + "'</script>");
			}
			model.setPrivilSyscode(nextCode);
			return "insert";
		} else {
			return "canntinsert";
		}
	}

	public String priviltree() throws Exception {
		try {
			UserInfo user = userService.getCurrentUserInfo();
			privilList = userService.getPrivilInfosByUserLoginName(user.getUsername(), "2", "2", "2");
			systemList = userService.getCurrentUserSystems(Const.IS_YES);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL);
		if("typeTree".equals(viewChangeFlag)){
			return "priviltypetree";
		}else{
			return "priviltree";
		}
	}
	
	/**
	 * 切换资源权限添加功能视图
	 * @author 喻斌
	 * @date Jul 2, 2009 10:53:57 AM
	 * @return
	 * @throws Exception
	 */
	public String changeInsertView() throws Exception {
		systemList = userService.querySystems(Const.IS_YES);
		//初始化资源权限类型select元素集
		typeLst = new ArrayList<PrivilTypeSelectItemBean>(0);
		if(systemList != null){
			for(TUumsBaseSystem system : systemList){
				for(TUumsBasePrivilType type : system.getBasePrivilTypes()){
					typeLst.add(new PrivilTypeSelectItemBean(type.getTypeId(),
							type.getBaseSystem().getSysName() + " — " + type.getTypeName()));
				}
			}
		}
		if(model.getBasePrivilType() != null){
			typeId = model.getBasePrivilType().getTypeId();
		}
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		page = userService.queryPrivils(page, "%");
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (privilId != null && !"".equals(privilId)) {
			model = userService.getPrivilInfoByPrivilId(privilId);
		} else {
			model = new TUumsBasePrivil();
			Long nextSequence = userService.getNextPrivilSequenceCode();
			model.setPrivilSequence(nextSequence);
		}
		systemList = userService.querySystems(Const.IS_YES);
		//初始化资源权限类型select元素集
		typeLst = new ArrayList<PrivilTypeSelectItemBean>(0);
		if(systemList != null){
			for(TUumsBaseSystem system : systemList){
				for(TUumsBasePrivilType type : system.getBasePrivilTypes()){
					typeLst.add(new PrivilTypeSelectItemBean(type.getTypeId(),
							type.getBaseSystem().getSysName() + " — " + type.getTypeName()));
				}
			}
		}
	}

	@Override
	public String save() throws Exception {
		if ("".equals(model.getPrivilId())) {
			model.setPrivilId(null);
		}
		//更新资源权限类型信息
		if(typeId != null && !"".equals(typeId)){
			TUumsBasePrivilType privilType = userService.getPrivilTypeById(typeId);
			if(privilType != null){
				model.setBasePrivilType(privilType);
			}
		}
		String attr = model.getPrivilAttribute();
		//去掉回车换行
		if(attr != null && attr.length() > 0) {
			attr = attr.replaceAll("\r\n", "");
			model.setPrivilAttribute(attr);
		}
		userService.savePrivil(model, childTogether);

		//addActionMessage(returnMessage);
		return renderHtml("<script>parent.PrivilTree.location='"
				+ getRequest().getContextPath()
				+ "/privilmanage/basePrivil!priviltree.action?viewChangeFlag=" + viewChangeFlag + "';window.location='"
				+ getRequest().getContextPath()
				+ "/privilmanage/basePrivil!insert.action?viewChangeFlag=" + viewChangeFlag + "'</script>");

	}

	public TUumsBasePrivil getModel() {

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
	public String moveUp() throws Exception {
		prepareModel();
		String mes = null;
		if("typeTree".equals(viewChangeFlag)){
			mes = userService.movePrivilPositionInPrivilTypeTree(model,
				Const.MOVE_UP_IN_TREE);
		}else{
			mes = userService.movePrivilPositionInTree(model,
					Const.MOVE_UP_IN_TREE);
		}
		if (!"".equals(mes)) {
			this.addActionMessage(mes);
			this.message = mes;
		}
		return "move";
	}

	/**
	 * 权限下移
	 * 
	 * @author 喻斌
	 * @date Feb 11, 2009 9:50:00 AM
	 * @return
	 * @throws Exception
	 */
	public String moveDown() throws Exception {
		prepareModel();
		String mes = null;
		if("typeTree".equals(viewChangeFlag)){
			mes = userService.movePrivilPositionInPrivilTypeTree(model,
				Const.MOVE_DOWN_IN_TREE);
		}else{
			mes = userService.movePrivilPositionInTree(model,
					Const.MOVE_DOWN_IN_TREE);
		}
		if (!"".equals(mes)) {
			this.addActionMessage(mes);
			this.message = mes;
		}
		return "move";
	}

	/**
	 * 检验编码合法性
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-25 下午02:17:49
	 * @return String
	 * @throws Exception
	 */
	public String compareCode() throws Exception {
		message = "111";
		boolean iserr=userService.checkPrivilCode(orgcode);
		if(iserr){
			boolean flag=userService.comparePrivilCode(orgcode);
			if(flag){
				boolean isParentExist = userService.checkPrivilParentIsExist(orgcode, privilId);
				if(!isParentExist){
					message = "222";// 该编码的父级不存在
				}
			}else{
				message = "000";// 编码不唯一
			}
		}else{
			message = "333";// 编码不符合规则
		}
		return this.renderText(message);
	}

	/**
	 * 判断父级元素及所属系统是否被设置为未启用状态
	 * 
	 * @author 喻斌
	 * @date Apr 3, 2009 2:18:20 PM
	 * @return
	 * @throws Exception
	 */
	public String checkFatherIsdel() throws Exception {
		if (systemId != null && !"".equals(systemId)) {
			TUumsBaseSystem systemInfo = userService.getSystemBySystemId(systemId);
			if (Const.IS_NO.equals(systemInfo.getSysIsactive())) {
				return this.renderText("222");
			}
		}
		TUumsBasePrivil privilInfo = userService
				.getParentPrivilByPrivilSyscode(this.privilSyscode);
		if (privilInfo != null) {
			if (Const.IS_NO.equals(privilInfo.getPrivilIsactive())) {
				return this.renderText("111");
			}
		}
		return this.renderText("false");
	}
	
	/**
	 * 初始化资源权限类型model类信息
	 * @author 喻斌
	 * @date Jun 23, 2009 4:23:42 PM
	 * @return
	 * @throws Exception
	 */
	public String initPrivilType() throws Exception {
		if(typeId != null && !"".equals(typeId)){
			this.privilType = userService.getPrivilTypeById(typeId);
			systemId = privilType.getBaseSystem().getSysId();
		}else{
			this.privilType = new TUumsBasePrivilType();
			privilType.setTypeCategory("2");
			Long sequence = userService.getNextPrivilTypeSequence();
			privilType.setTypeSequence(sequence);
		}
		systemList = userService.getCurrentUserSystems(Const.IS_YES);
		return "priviltype";
	}
	
	/**
	 * 保存资源权限类型信息
	 * @author 喻斌
	 * @date Jun 23, 2009 6:03:58 PM
	 * @return
	 * @throws Exception
	 */
	public String savePrivilType() throws Exception {
		//新增
		if(typeId == null || "".equals(typeId)){
			if(systemId != null && !"".equals(systemId)){
				TUumsBaseSystem system = userService.getSystemBySystemId(systemId);
				if(system != null){
					privilType.setBaseSystem(system);
					userService.savePrivilType(privilType);
				}
			}
		//修改
		}else{
			TUumsBasePrivilType privilType1 = userService.getPrivilTypeById(typeId);
			if(privilType1 != null){
				if(systemId != null && !"".equals(systemId)){
					TUumsBaseSystem system = userService.getSystemBySystemId(systemId);
					if(system != null){
						privilType1.setBaseSystem(system);
					}
				}
				privilType1.setTypeCategory(privilType.getTypeCategory());
				privilType1.setTypeMemo(privilType.getTypeMemo());
				privilType1.setTypeName(privilType.getTypeName());
				privilType1.setTypeSequence(privilType.getTypeSequence());
				userService.savePrivilType(privilType1);
			}
		}
		return "savepriviltype";
	}
	
	/**
	 * 删除资源类型信息
	 * @author 喻斌
	 * @date Jun 24, 2009 9:42:45 AM
	 * @throws Exception
	 */
	public String delPrivilType() throws Exception {
		if(typeId != null && !"".equals(typeId)){
			userService.delPrivilTypes(typeId);
		}
		return this.priviltree();
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

	public List<TUumsBasePrivil> getPrivilList() {
		return privilList;
	}

	public void setPrivilList(List<TUumsBasePrivil> privilList) {
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

	public List<PrivilTypeSelectItemBean> getTypeLst() {
		return typeLst;
	}

	public void setTypeLst(List<PrivilTypeSelectItemBean> typeLst) {
		this.typeLst = typeLst;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public TUumsBasePrivilType getPrivilType() {
		return privilType;
	}

	public void setPrivilType(TUumsBasePrivilType privilType) {
		this.privilType = privilType;
	}

	public String getViewChangeFlag() {
		return viewChangeFlag;
	}

	public void setViewChangeFlag(String viewChangeFlag) {
		this.viewChangeFlag = viewChangeFlag;
	}
}
