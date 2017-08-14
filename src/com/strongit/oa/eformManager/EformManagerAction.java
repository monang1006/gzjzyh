/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息附件 manager类
 */
package com.strongit.oa.eformManager;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;


import com.strongit.oa.bo.TEFormTemplate;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.IUserService;

import com.strongit.oa.formManager.IFormManagerService;
import com.strongit.oa.infoManager.IInfoManagerService;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;

import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 处理消息文件夹类
 * @Create Date: 2009-12-7
 * @author luosy
 * @version 1.0
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( {
	@Result(name = "formdesigner",value = "/WEB-INF/jsp/eformManager/eformManager-formdesigner.jsp", type = ServletDispatcherResult.class),
	@Result(name = BaseActionSupport.RELOAD, value = "eFormManager.action", type = ServletActionRedirectResult.class) })
public class EformManagerAction extends BaseActionSupport {

	private Page<TEFormTemplate> page = new Page<TEFormTemplate>(FlexTableTag.MAX_ROWS, true);

	private String id;
	
	private String title;
	
	private TEFormTemplate model = new TEFormTemplate();

	private EformManagerManager manager;
	
	private IFormManagerService formManagerService;		//表单管理接口
	
	private IInfoManagerService infoManagerService;		//信息项管理接口

	private HashMap<String, String> typemap = new HashMap<String, String>();
	
	private String searchTitle;
	
	private File file;
	
	private IEFormService eFormService;
	
	private String orgName=null;
	
	private String orgCode=null;
	
	private List<TUumsBaseOrg> orgList;
	
	private int topOrgcodelength;//构造组织机构树时顶级节点的编码长度
	
	private String codeType;
	
	private String operating="";//(用户当前操作)："add"-新建，"edit"-修改，"view"-查看,"open"-打开
	/**
	 * 郑志斌 添加 2010 12 14 
	 */
	private String reportEform;								//在定义报表时，跳转到选择表单页面
	//申仪玲 2011 11 6
	private IProcessDefinitionService processService;      //流程设计接口
	
	private String superCode;

	public String getSuperCode() {
		return superCode;
	}

	public void setSuperCode(String superCode) {
		this.superCode = superCode;
	}

	@Autowired
	public void setProcessService(IProcessDefinitionService processService) {
		this.processService = processService;
	}
	
	@Autowired
	IUserService userService;
	
	@Autowired
	public void setManager(EformManagerManager manager) {
		this.manager = manager;
	}

	public TEFormTemplate getModel() {
		return model;
	}
	public void setModel(TEFormTemplate model) {
		this.model = model;
	}

	
	////////////////////////////// 以下为action跳转方法
	/**
	 * @roseuid 496AFCED000F
	 */
	public EformManagerAction() {
		typemap.put("SF", "启动表单");
		typemap.put("QF", "查询表单");
		typemap.put("VF", "展现表单");
		typemap.put(null, "");
	}

	protected void prepareModel() {

	}

	/**
	 * author:luosy
	 * description:	跳转到添加页面
	 * modifyer:
	 * description:
	 * @return
	 */
	public String input() throws Exception {
		if(model==null){
			model=new TEFormTemplate();
		}
		if(null!=id&&!"".equals(id)){
			model = manager.getFormTemplateInfo(id);
			orgCode=model.getOrgCode();
		}else{
			orgCode=userService.getDepartmentByOrgId(userService.getCurrentUser().getOrgId())
							   .getOrgSyscode();
		}
		if(orgCode!=null && !"0".equals(orgCode)){
			orgName=userService.getOrgInfoBySyscode(orgCode).getOrgName();
		}
		model.setOrgCode(orgCode);
		return "input";
	}

	/**
	 * 编辑的时候，判断当前表单是否已经使用
	 * @author zhengzb
	 * @desc 
	 * 2010-11-30 下午12:07:39 
	 * @return
	 * @throws Exception
	 */
	public String isHasEdit() throws Exception{
		Boolean boo=manager.isHasEFormByworkflow(id);
		if(boo){
			model = manager.get(id);
			return renderHtml(model.getTitle());					//表单已经使用
		}else{
			return renderHtml( "0");					
		}
	}
	
	/**
	 * author:luosy
	 * description:	跳转到查看页面
	 * modifyer:
	 * description:
	 * @return
	 */
	public String view() throws Exception {
		if(null!=id&&!"".equals(id)){
			model = manager.get(id);
		}
		return "view";
	}
	
	/**
	 * 列表tab页
	 * @return
	 * @author  niwy
	 * @throws Exception
	 * @date  2013年12月30日9:28:31
	 */
	public String container() throws Exception{
		return "container";
	} 
	
	/**
	 * jsp表单列表
	 * @return
	 * @author 倪文英
	 * @date   2013年12月30日9:41:45
	 * @throws Exception
	 */
	public String jspList() throws Exception{
		return "jsp";
	}
	/**
	 *添加jsp
	 * @return
	 */
	public String add() throws Exception{
		return "add";
	}
	/**
	 * 删除jsp列表
	 * @return
	 * @throws Exception
	 */
	public String del() throws Exception{
		
		return null;
	}
	/**
	 * author:luosy
	 * description:	得到表单列表
	 * modifyer:
	 * description:
	 * @return
	 */
	public String list() throws Exception {
		try {
			if(model.getTitle()!=null&&!"".equals(model.getTitle())){
				/* modify xielei 2014-04-11 转义百分号*/
				model.setTitle(URLDecoder.decode(model.getTitle().replaceAll("%", "%25"), "utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(reportEform!=null&&!reportEform.equals("")){
			model.setType("SF");			
		}
		manager.searchEform(page, model);
		
		if(reportEform!=null&&!reportEform.equals("")){
			return "reportEform";
		}else {
			if(!operating.equals("")&operating.equals("open")){
				return "openlist";
			}else{
				return SUCCESS;
			}
		}
	}

	/**
	 * @author 严建
	 * @createTime Aug 26, 2011
	 * @description 获取组织机构树
	 */
	public String getOrgTree() throws IOException {
	
		if(userService.isSystemDataManager(userService.getCurrentUser().getUserId())) {
			orgList = userService.queryOrgs(Const.IS_NO);
		} else {
			//增加分级授权功能
			User userInfo = userService.getCurrentUser();
			orgList = userService.queryOrgsByHa(Const.IS_NO, userInfo.getOrgId());
		}
			
		
			/**
		UserInfo userInfo = userManager.getCurrentUserInfo();
		String userId = userInfo.getUserId();
		TUumsBaseOrg org=userManager.getSupOrgByUserIdByHa(userManager.getCurrentUserInfo().getUserId());
		if(userService.isSystemDataManager(userId)) {
			orgList = orgManager.getAllOrgInfos();			
		} else {
			orgList = orgManager.getAllOrgInfosByHa(userInfo.getOrgId());
		}
	**/	
		for(int i=0;i<orgList.size();i++){
			String isOrg=orgList.get(i).getIsOrg();
			if(isOrg!=null){
				if(!isOrg.equals("1")){
					orgList.remove(i);
					i--;
				}
			}
		}
		//superCode=org.getSupOrgCode();
		topOrgcodelength = userService.findTopOrgCodeLength();
		codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		return "checktree";
	}
	
	/**
	 * @author 严建
	 * @createTime Aug 26, 2011
	 * @description 获取模板信息
	 */
	public String getEFormTemplateInfo()throws Exception{
		if(model==null){
		}else{
			JSONObject json = new JSONObject();
			model = manager.getFormTemplateInfo(id);
			String title=model.getTitle();
			String type=model.getType();
			String orgCode=model.getOrgCode();
			json.put("title", title);					
			json.put("type", type);		
			json.put("orgCode", orgCode);	
			return this.renderText(json.toString());
		}
		
		return null;
	}
	
	/**
	 * 同步表单中的数据到表单使用记录中
	 * @author zhengzb
	 * @desc 
	 * 2010-11-20 上午10:17:23 
	 * @return
	 * @throws Exception
	 */
	
	public String dataEForm()throws Exception{
		try{
//			 对表单数据同步
			List<EForm> eformList = eFormService.getFormTemplateList(null);
			if(eformList!=null&&eformList.size()>0){
				//manager.SynInsertInfoForm(eformList);
			}
			renderText("ok");
		}catch(Exception e){
			
			e.printStackTrace();
			renderText("error");
		}
		return null;
	}
	
	
	/**
	* @method checkTitle
	* @author 申仪玲
	* @created 2011-9-21 上午09:37:35
	* @description 表单同名验证
	* @return String 返回类型
	*/
	public String checkTitle() throws Exception {
		
		if(null!=id&&!"".equals(id)){
			model = manager.get(id);
			if(title.equals(model.getTitle())){
				return renderHtml("true"); //说明是编辑表单,表单名没有变
			}else{				
				model = manager.getByTitle(title);
				if(model == null){
					return renderHtml("true");
				}else{
					return renderHtml("false");
				}				
			}
		}else{
			model = manager.getByTitle(title);
			if(model == null){
				return renderHtml("true");
			}else{
				return renderHtml("false");
			}		
			
		}
	}
	
	/**
	 * author:luosy
	 * description:	保存
	 * modifyer:
	 * description:
	 * @return
	 */
	public String save() throws Exception {
		if(file==null||file.length()==0){
			addActionMessage("error附件为空，请确认表单文件正确！");
		}else{
			try {
				
				manager.save(model);
				if(model!=null&&model.getId()!=null&&!model.getId().equals("")){
				}
				addActionMessage("success");
			} catch (Exception e) {
				
				addActionMessage("添加信息集使用记录出错，请做表单数据同步操作！");
			}
		}
		return "input";
	}

	/**
	 * author:luosy
	 * description:	删除
	 * modifyer:
	 * description:
	 * @return
	 */
	public String delete() throws Exception {
		try {
			boolean boo = true;
			boo= manager.isHasEFormByworkflow(id);
			
			//modify yanjian 2011-11-06 15:58 删除表单时，判断表单是否被流程草稿引用
			boolean isUsedByDraft = manager.isHasEFormByDraft(id);
			if(boo||isUsedByDraft){
				renderText("当前表单已使用，不能删除公共表单！");
			}else{				
				if("false".equals(manager.deleteById(id))){
					renderText("操作失败，不能删除公共表单");
				}
			}
			 
		} catch (Exception e) {
			e.printStackTrace();
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}
	
	/**
	 * author:shenyl
	 * description:  加载表单模板设计
	 * modifyer:
	 * description:
	 * @return
	 */
	
	public String formdesigner()throws Exception {
		
		if(null!=id&&!"".equals(id)){
			
			model = manager.get(id);
		}
		return "formdesigner";
	}


	/**
	 * author:luosy 2014-3-25
	 * description:	修改表单是否为全局表单
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String conf()throws Exception {
		String ret = "";
		if(null==orgCode||null==id){
			ret = "没有找到表单数据！";
		}else if("0".equals(orgCode)){
			String orgid = userService.getCurrentUser().getOrgId();
			TUumsBaseOrg org = userService.getOrgInfoByOrgId(orgid);
			orgCode = org.getSupOrgCode();
			manager.setFormOrgCode(id, orgCode);
		}else{
			manager.setFormOrgCode(id, "0");
		}
		
		return renderText(ret);
	}
	
	public Page<TEFormTemplate> getPage() {
		return page;
	}

	public HashMap<String, String> getTypemap() {
		return typemap;
	}

	public String getSearchTitle() {
		return searchTitle;
	}

	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Autowired
	public void setFormManagerService(IFormManagerService formManagerService) {
		this.formManagerService = formManagerService;
	}
	
	@Autowired
	public void setInfoManagerService(IInfoManagerService infoManagerService) {
		this.infoManagerService = infoManagerService;
	}

	@Autowired
	public void setEFormService(IEFormService formService) {
		eFormService = formService;
	}

	public String getReportEform() {
		return reportEform;
	}

	public void setReportEform(String reportEform) {
		this.reportEform = reportEform;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<TUumsBaseOrg> orgList) {
		this.orgList = orgList;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getOperating() {
		return operating;
	}

	public void setOperating(String operating) {
		this.operating = operating;
	}

	public int getTopOrgcodelength() {
		return topOrgcodelength;
	}

	public void setTopOrgcodelength(int topOrgcodelength) {
		this.topOrgcodelength = topOrgcodelength;
	}
	
}
