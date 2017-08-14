package com.strongit.workflow.workflowDesign.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.common.UupExtInterface;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Position;
import com.strongit.oa.util.TempPo;
import com.strongit.workflow.bo.TwfBaseActorSetting;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.uupInterface.IUupInterface;
import com.strongit.workflow.uupInterface.UupInterface;
import com.strongit.workflow.uupInterface.UupUtil;
import com.strongit.workflow.workflowDesign.util.GenerateSelectTree;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongit.workflow.workflowInterface.IWorkflowConfigService;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 人员选择界面处理Action
 * @author       喻斌
 * @company      Strongit Ltd. (C) copyright
 * @date         Dec 12, 2008  1:21:36 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.workflow.workflowDesign.action.UserSelectAction
 */
@ParentPackage("default")
@Results( { @Result(name = "processOwner", value = "/WEB-INF/jsp/workflowDesign/action/process_owner_select.jsp", type = ServletDispatcherResult.class),
			@Result(name = "reAssign", value = "/WEB-INF/jsp/workflowDesign/action/node_reassign_select.jsp", type = ServletDispatcherResult.class),
			@Result(name = "taskAssign", value = "/WEB-INF/jsp/workflowDesign/action/node_person_select.jsp", type = ServletDispatcherResult.class),
			@Result(name = "org", value = "/WEB-INF/jsp/workflowDesign/action/process_org_choosetree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "corg", value = "/WEB-INF/jsp/workflowDesign/action/process_corg_choosetree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "organization", value = "/WEB-INF/jsp/workflowDesign/action/process_organization_choosetree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "pos", value = "/WEB-INF/jsp/workflowDesign/action/node_pos_choosetree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "per", value = "/WEB-INF/jsp/workflowDesign/action/node_per_choosetree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "rel", value = "/WEB-INF/jsp/workflowDesign/action/node_relative_choosetree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "gpost", value = "/WEB-INF/jsp/workflowDesign/action/node_gpost_choosetree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "assignUserList", value = "/WEB-INF/jsp/workinspect/worksend/workSend-assignUserList.jsp", type = ServletDispatcherResult.class)})
public class UserSelectAction extends BaseActionSupport<TwfBaseProcessfile> {
	
	//分派类型
	private String dispatch;
	
	private String actorSettings;//保存某个config关联的人员选择配置信息
	
	//具体树形页面展现类型
	private String flag;
	
	//流程定义时的权限控制类型：1.启动者 2.设计者 3.管理者
	private String pageType;

	private List orgList;
	
	TwfBaseProcessfile model;
	
	private IProcessDefinitionService manager;
	
	//邓志城 修改于2010年6月20日17:08:10，注入名称和oa中的某个名称冲突
	@Autowired IWorkflowConfigService configManager;

	@Autowired IUserService userService ;//added by dengzc 2010年7月15日15:37:51
	
	@Autowired IUupInterface uupInterface;
	
	@Autowired 
	private UupExtInterface uupExtInterface;
	
	private String topTreeNodeId = "0";
	
	private String type;

	private String prefix;//树结构信息的前缀
	/**
	 * @roseuid 493692F5002E
	 */
	public UserSelectAction() {

	}
	
	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public TwfBaseProcessfile getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(IProcessDefinitionService aManager) {
		manager = aManager;
	}
	
	/**
	 * @return java.lang.String
	 * @roseuid 4936902101B5
	 */
	@Override
	public String list() {
		return SUCCESS;
	}

	/**
	 * @roseuid 49369077005D
	 */
	@Override
	public void prepareModel() {
		
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936910B034B
	 */
	@Override
	public String save() throws Exception {
		return RELOAD;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936911402EE
	 */
	@Override
	public String delete() throws Exception {
		return RELOAD;
	}

	/**
	 * 人员选择页面路径分派
	 */
	@Override
	public String input() {
		String pfId = getRequest().getParameter("pfId");
		if(pfId != null && !"".equals(pfId)) {
			TwfBaseProcessfile processFile = manager.getProcessfileByPfId(pfId);
			type = processFile.getRest1();
			if(type != null && !"".equals(type)) {
				if("true".equals(type)) {
					type = "1";
				}
			}
		}
		List<TwfBaseActorSetting> lst = configManager.getActorSettingsByConfigClass(this.dispatch, "1");
		StringBuffer json = new StringBuffer("");
		String isActiveactor = getRequest().getParameter("isActiveactor");//是否是动态选择处理人
		if(!lst.isEmpty()){
			for(TwfBaseActorSetting as : lst){
				
				// add "&& null!=isActiveactor"  modify by luosy 2014-01-28
				if(!"1".equals(isActiveactor) && null!=isActiveactor) {
					if(as.getAsPrefix().equals("r")) {//机构选择人员
						continue;
					}
				}
				if(prefix != null && !prefix.equals("") && !as.getAsPrefix().equals(prefix)){//modify 严建 2011-12-18 11:30 选择显示指定类别前缀的树结构
					continue;
				}
				json.append(",{")
					.append("name:'").append(as.getAsName()).append("',")
					.append("alias:'").append(as.getAsAlias()).append("',")
					.append("url:'").append(this.getRequest().getContextPath() + as.getAsActionUrl()).append("',")
					.append("prefix:'").append(as.getAsPrefix()).append("'}");
			}
		}
		
		this.actorSettings = "".equals(json.toString()) ? "[]" : "[" + json.toString().substring(1) + "]";
		return INPUT;
		
		//if("processOwner".equals(this.dispatch)){
		//	return "processOwner";
		//}else if("reAssign".equals(this.dispatch)){
		//	return "reAssign";
		//}else if("taskAssign".equals(this.dispatch)){
		//	return "taskAssign";
		//}else{
		//	return INPUT;
		//}
	}
	public String assignUserList() throws Exception {
		String root = getRequest().getContextPath();
		this.actorSettings = "[" +
				//"{name:'机构选择',alias:'org',url:'"+root+"/workflowDesign/action/userSelect!chooseTree.action?flag=org',prefix:'o'}" +
				//",{name:'机构负责人',alias:'corg',url:'/OA5.0/workflowDesign/action/userSelect!chooseTree.action?flag=corg',prefix:'c'}" +
				//",{name:'岗位选择',alias:'post',url:'/OA5.0/workflowDesign/action/userSelect!chooseTree.action?flag=pos',prefix:'p'}" +
				"{name:'人员选择',alias:'person',url:'"+root+"/workflowDesign/action/userSelect!chooseTree.action?flag=per',prefix:'u'}" +
				//",{name:'其他选择',alias:'other',url:'/OA5.0/workflowDesign/action/userSelect!chooseTree.action?flag=rel',prefix:'s'}" +
				//",{name:'全局岗位',alias:'gpost',url:'/OA5.0/workflowDesign/action/userSelect!chooseTree.action?flag=gpost',prefix:'g'}" +
				"]";
		return "assignUserList";
	}
	
	/**
	 * 具体树形展现页面分派
	 * @author  喻斌
	 * @date    Dec 12, 2008  3:08:45 PM
	 * @return
	 * @throws Exception
	 */
	public String chooseTree() throws Exception{
		if(dispatch==null){
			dispatch="";
		}
	    if("org".equals(flag)){
	    	//增加对全局流程的支持 dengzc 2011年6月3日15:27:39
	    	String type = getRequest().getParameter("type");
	    	if("1".equals(type) && dispatch.equals("ag")) {//全局 动态选择处理人 
	    		orgList = uupExtInterface.getAllOrgUserListForGlobalWorkflow();	 
	    	} else {
	    		orgList = uupInterface.getAllOrgUserList();	    		
	    	}
	    	//--------------------End---------------------
	    	Set<String> orgList2 = new HashSet<String>(0);
	    	Set<String> orgList3 = new HashSet<String>(0);
	    	Object[] temp;
	    	for(int i=0; i<orgList.size(); i++){
	    		temp=(Object[])orgList.get(i);
	    		orgList2.add(String.valueOf(temp[1]));
	    		orgList3.add(String.valueOf(temp[0]));
	    	}
	    	String orgId1=null,orgId2=null;
	    	boolean containsFlag;
	    	for(int i=0; i<orgList2.size(); i++){
	    		orgId1 = orgList2.iterator().next();
	    		containsFlag = false;
	    		Iterator<String> it=orgList3.iterator();
	    		while(it.hasNext()){
	    			orgId2=it.next();
	    			if(orgId1.equals(orgId2)){
	    				containsFlag = true;
	    				break;
	    			}
	    		}

	    		if(containsFlag){
    				orgList2.remove(orgId1);
    				orgList3.remove(orgId2);
	    			i=-1;
	    		}else{
	    			topTreeNodeId=orgId1;
	    			break;
	    		}
	    	}
	    	
	    	orgList2 = null;
	    	orgList3 = null;
	    	return "org";
	    }else if("corg".equals(flag)){
	    	//增加对全局流程的支持 dengzc 2011年6月3日15:27:39
	    	String type = getRequest().getParameter("type");
	    	if("1".equals(type) && dispatch.equals("ag")) {//全局 动态选择处理人 
	    		orgList = uupExtInterface.getAllOrgUserListForGlobalWorkflow();	 
	    	} else {
	    		orgList = uupInterface.getAllOrgUserList();	    		
	    	}
	    	//--------------------End---------------------
	    	Set<String> orgList2 = new HashSet<String>(0);
	    	Set<String> orgList3 = new HashSet<String>(0);
	    	Object[] temp;
	    	for(int i=0; i<orgList.size(); i++){
	    		temp=(Object[])orgList.get(i);
	    		orgList2.add(String.valueOf(temp[1]));
	    		orgList3.add(String.valueOf(temp[0]));
	    	}
	    	String orgId1=null,orgId2=null;
	    	boolean containsFlag;
	    	for(int i=0; i<orgList2.size(); i++){
	    		orgId1 = orgList2.iterator().next();
	    		containsFlag = false;
	    		Iterator<String> it=orgList3.iterator();
	    		while(it.hasNext()){
	    			orgId2=it.next();
	    			if(orgId1.equals(orgId2)){
	    				containsFlag = true;
	    				break;
	    			}
	    		}

	    		if(containsFlag){
    				orgList2.remove(orgId1);
    				orgList3.remove(orgId2);
	    			i=-1;
	    		}else{
	    			topTreeNodeId=orgId1;
	    			break;
	    		}
	    	}
	    	orgList2 = null;
	    	orgList3 = null;
	    	return "corg";
	    } else if("organization".equals(flag)) {
//	    	增加对全局流程的支持 dengzc 2011年6月3日15:27:39
	    	String type = getRequest().getParameter("type");
	    	if("1".equals(type) && dispatch.equals("ag")) {
	    		orgList = uupExtInterface.getAllOrgUserListForGlobalWorkflow();	 
	    	} else {
	    		orgList = uupInterface.getAllOrgUserList();	    		
	    	}
	    	//--------------------End---------------------
	    	Set<String> orgList2 = new HashSet<String>(0);
	    	Set<String> orgList3 = new HashSet<String>(0);
	    	Object[] temp;
	    	for(int i=0; i<orgList.size(); i++){
	    		temp=(Object[])orgList.get(i);
	    		orgList2.add(String.valueOf(temp[1]));
	    		orgList3.add(String.valueOf(temp[0]));
	    	}
	    	String orgId1=null,orgId2=null;
	    	boolean containsFlag;
	    	for(int i=0; i<orgList2.size(); i++){
	    		orgId1 = orgList2.iterator().next();
	    		containsFlag = false;
	    		Iterator<String> it=orgList3.iterator();
	    		while(it.hasNext()){
	    			orgId2=it.next();
	    			if(orgId1.equals(orgId2)){
	    				containsFlag = true;
	    				break;
	    			}
	    		}

	    		if(containsFlag){
    				orgList2.remove(orgId1);
    				orgList3.remove(orgId2);
	    			i=-1;
	    		}else{
	    			topTreeNodeId=orgId1;
	    			break;
	    		}
	    	}
	    	orgList2 = null;
	    	orgList3 = null;
	    	return "organization";
	    } else if("pos".equals(flag)){
	    	//增加对全局流程的支持 dengzc 2011年6月3日15:27:39
	    	String type = getRequest().getParameter("type");
	    	if("1".equals(type) && dispatch.equals("ag")) {
	    		orgList = uupExtInterface.getAllOrgPostListForGlobalWorkflow();	 
	    	} else {
	    		orgList = uupInterface.getAllOrgPostList();  		
	    	}
	    	//--------------------End---------------------
	    	Set<String> orgList2 = new HashSet<String>(0);
	    	Set<String> orgList3 = new HashSet<String>(0);
	    	Object[] temp;
	    	for(int i=0; i<orgList.size(); i++){
	    		temp=(Object[])orgList.get(i);
	    		orgList2.add(String.valueOf(temp[1]));
	    		orgList3.add(String.valueOf(temp[0]));
	    	}
	    	String orgId1=null,orgId2=null;
	    	boolean containsFlag;
	    	for(int i=0; i<orgList2.size(); i++){
	    		orgId1 = orgList2.iterator().next();
	    		containsFlag = false;
	    		Iterator<String> it=orgList3.iterator();
	    		while(it.hasNext()){
	    			orgId2=it.next();
	    			if(orgId1.equals(orgId2)){
	    				containsFlag = true;
	    				break;
	    			}
	    		}

	    		if(containsFlag){
    				orgList2.remove(orgId1);
    				orgList3.remove(orgId2);
	    			i=-1;
	    		}else{
	    			topTreeNodeId=orgId1;
	    			break;
	    		}
	    	}
	    	orgList2 = null;
	    	orgList3 = null;
	    	//List<TempPo> trees = GenerateSelectTree.generateOrgUserTree(orgList,flag);
	    	//getRequest().setAttribute("data", trees);
	    	return "pos";
	    }else if("per".equals(flag)){
	    	//增加对全局流程的支持 dengzc 2011年6月3日15:27:39
	    	String type = getRequest().getParameter("type");
	    	if("1".equals(type) && dispatch.equals("ag")) {
	    		orgList = uupExtInterface.getAllOrgUserListForGlobalWorkflow();	 
	    	}
	    	else {
	    		orgList = uupInterface.getAllOrgUserList();	    		
	    	}
	    	//List<TempPo> trees = GenerateSelectTree.generateOrgUserTree(orgList,flag);
	    	//getRequest().setAttribute("data", trees);
	    	//--------------------End---------------------
	    	/*Set<String> orgList2 = new HashSet<String>(0);
	    	Set<String> orgList3 = new HashSet<String>(0);
	    	Object[] temp;
	    	for(int i=0; i<orgList.size(); i++){
	    		temp=(Object[])orgList.get(i);
	    		orgList2.add(String.valueOf(temp[1]));
	    		orgList3.add(String.valueOf(temp[0]));
	    	}
	    	String orgId1=null,orgId2=null;
	    	boolean containsFlag;
	    	for(int i=0; i<orgList2.size(); i++){
	    		orgId1 = orgList2.iterator().next();
	    		containsFlag = false;
	    		Iterator<String> it=orgList3.iterator();
	    		while(it.hasNext()){
	    			orgId2=it.next();
	    			if(orgId1.equals(orgId2)){
	    				containsFlag = true;
	    				break;
	    			}
	    		}

	    		if(containsFlag){
    				orgList2.remove(orgId1);
    				orgList3.remove(orgId2);
	    			i=-1;
	    		}else{
	    			topTreeNodeId=orgId1;
	    			break;
	    		}
	    	}
	    	orgList2 = null;
	    	orgList3 = null;*/
	    	return "per";
	    }else if("rel".equals(flag)){
	    	//orgList = uupBroker.getAllOrgUserList();
	    	return "rel";
	    }else if("gpost".equals(flag)){//added by dengzc 2010年7月15日15:14:03
	    								//全局岗位选择人员
	    	List<Position> postList = userService.getPostList();
	    	List<TempPo> list = new ArrayList<TempPo>();
	    	for(Position position : postList){
	    		TempPo po = new TempPo();
	    		po.setId("g"+position.getPostId());
	    		po.setName(position.getPostName());
	    		po.setParentId("0");
	    		list.add(po);
	    	}
	    	getRequest().setAttribute("list", list);
	    	return "gpost";
	    }else{
	    	return "org";
	    }
	}
	
	public IProcessDefinitionService getManager() {
		return manager;
	}

	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public List getOrgList() {
		return orgList;
	}

	public void setOrgList(List orgList) {
		this.orgList = orgList;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	/**
	 * @return the topTreeNodeId
	 */
	public String getTopTreeNodeId() {
		return topTreeNodeId;
	}

	/**
	 * @param topTreeNodeId the topTreeNodeId to set
	 */
	public void setTopTreeNodeId(String topTreeNodeId) {
		this.topTreeNodeId = topTreeNodeId;
	}

	/**
	 * @return the actorSettings
	 */
	public String getActorSettings() {
		return actorSettings;
	}

	/**
	 * @param actorSettings the actorSettings to set
	 */
	public void setActorSettings(String actorSettings) {
		this.actorSettings = actorSettings;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
