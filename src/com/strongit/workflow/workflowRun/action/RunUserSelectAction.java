package com.strongit.workflow.workflowRun.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.uupInterface.UupUtil;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 流程运行态人员选择处理Action
 * @author       喻斌
 * @company      Strongit Ltd. (C) copyright
 * @date         Dec 15, 2008  4:14:59 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.workflow.workflowRun.action.UserSelectAction
 */
@ParentPackage("default")
@Results( { @Result(name = "assign", value = "/WEB-INF/jsp/workflowRun/action/task_assign_select.jsp", type = ServletDispatcherResult.class),
			@Result(name = "reassign", value = "/WEB-INF/jsp/workflowRun/action/task_reassign_select.jsp", type = ServletDispatcherResult.class),
			@Result(name = "default", value = "/WEB-INF/jsp/workflowRun/action/task_per_choosetree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "user", value = "/WEB-INF/jsp/workflowRun/action/task_per_choosetree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "preactor", value = "/WEB-INF/jsp/workflowRun/action/task_preactor_choosetree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "buttonSelectperson", value = "/WEB-INF/jsp/address/addressOrg-buttonSelectperson.jsp", type = ServletDispatcherResult.class),
			@Result(name = "other", value = "/WEB-INF/jsp/workflowRun/action/task_other_choosetree.jsp", type = ServletDispatcherResult.class)})
public class RunUserSelectAction extends BaseActionSupport<TwfBaseProcessfile> {

	//分派类型：1.任务分配 2.任务重新指派
	private String dispatch;
	
	private String needReturn;
	
	private String isSelectOtherActors;
	
	private String maxTaskActors;
	
	private String preActor;
	
	private String transitionId;//迁移线id
	
	/**
	 * 用户已选择的数据，修改选择人员时使用<br>
	 * <p>数据结构:</p>
	 * <p>用户1ID|用户1名称,用户2ID|用户2名称,...,用户NID|用户N名称
	 */
	private String taskActors;
	
	//具体树形页面展现类型:1.默认 2.人员 3.前步处理人 4.其他人员
	private String setType;
	
	private String nodeId;
	
	private String taskId;

	private List orgList;
	
	private List userList;
	
	@Autowired protected IUserService userService;//统一用户服务
	
	@Autowired private IWorkflowService workflowService;
	
	TwfBaseProcessfile model;
	
	private ITaskService manager;
	
	private IProcessDefinitionService definitionService;
	private String workflowName;
	
	private UupUtil uupBroker;
	
	private String topTreeNodeId = "0";

	private String allowChangeMainActor;//"1":标识主办变更
	/**
	 * @roseuid 493692F5002E
	 */
	public RunUserSelectAction() {

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
	public void setManager(ITaskService aManager) {
		manager = aManager;
	}
	
	@Autowired
	public void setDefinitionService(IProcessDefinitionService definitionService) {
		this.definitionService = definitionService;
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
	public String input() throws Exception{
		
		/**
		 * 字符转码（参数由JavaScript传入）
		 */
		if(taskActors != null && !"".equals(taskActors)){
			//this.setTaskActors(new String(taskActors.getBytes("ISO-8859-1"), "UTF-8"));
			this.setTaskActors(URLDecoder.decode(taskActors, "utf-8"));
		}
		
		if(!"0".equals(nodeId)){
			TwfBaseNodesetting nodeSetting = definitionService.getNodesettingByNodeId(nodeId);
			this.setMaxTaskActors(nodeSetting.getNsTaskMaxactors());
			this.setIsSelectOtherActors(nodeSetting.getNsTaskCanSelOther());
		}
		if(getRequest().getParameter("selectPersonType") != null){//开启按钮型人员选择模式
			String selectPersonType = getRequest().getParameter("selectPersonType") ;
			if("buttonType".equals(selectPersonType)){
				return "buttonSelectperson";
			}
		}
		if("assign".equals(this.dispatch)){
			if(workflowName != null && workflowName.length() > 0) {
				workflowName = URLDecoder.decode(workflowName, "utf-8");
				List<TwfBaseProcessfile> list = manager.getDataByHql("from TwfBaseProcessfile t where t.pfName = ?", new Object[]{workflowName});
				TwfBaseProcessfile processFile = list.get(0);
				workflowName = processFile.getRest1();				
			}
			return "assign";
		}else if("reassign".equals(this.dispatch)){
			return "reassign";
		}else{
			return INPUT;
		}
	}
	
	/**
	 * 具体树形展现页面分派
	 * @author  喻斌
	 * @date    Dec 15, 2008  4:33:44 PM
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String chooseTree() throws Exception{
	    if("default".equals(setType)){
	    	orgList = uupBroker.getAllOrgList();
	    	return "default";
	    }else if("user".equals(setType)){
	    	orgList = new ArrayList<String[]>(0);
	    	Set<String> orgList2 = new HashSet<String>(0);
	    	Set<String> orgList3 = new HashSet<String>(0);
	    	Set<String> removeOrgList = new HashSet<String>(0);
	    	List<String[]> tempOrgLst = null;
	    	if(workflowName != null && workflowName.length() > 0) {
				if(workflowName != null && !"".equals(workflowName)) {
					if("true".equals(workflowName)) {//全局流程
						String parentOrgId;
						List<String[]> returnList = new ArrayList<String[]>();
						List<TUumsBaseOrg> orgList = userService.getAllOrgInfo();
						for(TUumsBaseOrg org : orgList){
							/**
							 * 如果此组织机构已是最顶级，则其父级Id为”0“
							 */
							parentOrgId = org.getOrgParentId();
							if(parentOrgId == null || "".equals(parentOrgId)) {
								parentOrgId = "0";
							}
							/*parentOrgId = this.getParentDepart(org.getOrgId());
							if(org.getOrgId().equals(parentOrgId)){
								parentOrgId = "0";
							}*/
							returnList.add(new String[]{org.getOrgId(), parentOrgId, org.getOrgName()});
						}
						tempOrgLst = returnList;
					}
				}	
			}
	    	if(tempOrgLst == null) {//非全局流程
	    		tempOrgLst = uupBroker.getAllOrgList();
	    	}
	    	//final Map<String,Long> userMap = new HashMap<String, Long>();
	    	/*userList = manager.getToSelectActorsForTasknode(nodeId, taskId);
	    	for(int i=0;i<userList.size();i++){
	    		String userId=((String[])userList.get(i))[0];
	    		if(userId!=null){
	    			User user = userService.getUserInfoByUserId(userId);
	    			if(!userMap.containsKey(userId)) {
						if(user != null) {
							userMap.put(userId, user.getUserSequence());
						}
					}
	    		}
	    	}
	    	Collections.sort(userList, new PersonComparator<String[]>(SortConst.SORT_ASC,0,userMap));*/
	    	//改成支持迁移线上选择人员的功能 dengzc 2011年9月27日17:56:01
	    	if("0".equals(transitionId)) {
	    		transitionId = null;
	    	}
	    	userList = workflowService.getTaskActorsByTask(nodeId, taskId, transitionId);
	    	String currentUserId=userService.getCurrentUser().getUserId();//获取当前用户ID
	    	for(int i=0;i<userList.size();i++){
	    		if(userList.get(i) == null){//modify  yanjian 2011-10-29 11:20 userList.get(i)为空处理
	    			continue;
	    		}
	    		String[] o = ((String[])userList.get(i));
	    		if(((String[])userList.get(i))[0]==null){
	    			System.out.println("该机构不存在可选的人员，机构ID为：【"+o[2]+"】");
	    			userList.remove(i);
    				i--;
	    			continue;
	    		}
	    		String userId=o[0];
	    		if(this.dispatch!=null){
	    			if(this.dispatch.equals("reassign")){
		    			if(userId.equals(currentUserId)){//去除当前用户的信息
		    				userList.remove(i);
		    				i--;
		    				continue;
		    			}
		    		}
	    		}
	    	}
	    	// -------------------- End -----------------------------
	    	/*输出排序信息
	    	for(int i=0;i<userList.size();i++){
	    		String key=((String[])userList.get(i))[0];
	    		System.out.println("userId:"+key+",userSequence:"+userMap.get(key));
	    	}*/
	    	Set<String> tempOrgLst2 = new HashSet<String>(0);
	    	for(int i=0; i<userList.size(); i++){
	    		tempOrgLst2.add(((String[])userList.get(i))[2]);
	    	}
	    	String[] temp;
	    	String temp2 = null;
	    	Iterator<String> it1;
	    	for(int i=0; i<tempOrgLst2.size(); i++){
	    		it1 = tempOrgLst2.iterator();
	    		for(int k=0; k<=i; k++){
	    			temp2 = it1.next();
	    		}
	    		for(int j=0; j<tempOrgLst.size(); j++){
	    			temp = (String[])tempOrgLst.get(j);
	    			if(temp2.equals(temp[0])){
	    				//if(orgList.isEmpty() || topTreeNodeId.compareTo(temp[1]) > 0){
	    				//	topTreeNodeId = temp[1];
	    				//}
	    				orgList.add(temp);
	    				
	    				orgList2.add(temp[1]);
	    				orgList3.add(temp[0]);
	    				tempOrgLst2.remove(temp[0]);
	    				removeOrgList.add(temp[0]);
	    				if(!"0".equals(temp[1]) && !removeOrgList.contains(temp[1])){
	    					tempOrgLst2.add(temp[1]);
	    				}
	    				i = -1;
	    				break;
	    			}
	    		}
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
	    	
	    	tempOrgLst = null;
	    	tempOrgLst2 = null;
	    	orgList2 = null;
	    	orgList3 = null;
	    	
	    	Collections.sort(orgList,new Comparator(){
				public int compare(Object arg0, Object arg1) {
					String[] org1 =(String[]) arg0;
					String[] org2 =(String[]) arg1;
					Long key1;
					if(org1!=null && org1[0]!=null){
						key1 = userService.getDepartmentByOrgId(org1[0]).getOrgSequence();
					}else{
						key1 = Long.MAX_VALUE;
					}
					Long key2;
					if(org2!=null && org2[0] != null){
						key2 = userService.getDepartmentByOrgId(org2[0]).getOrgSequence();
					}else{
						key2 = Long.MAX_VALUE;
					}
					return key1.compareTo(key2); 
				}
	    	});
	    	
	    	return "user";
	    }else if("preactor".equals(setType)){
	    	//orgList = uupBroker.getAllOrgList();
	    	return "preactor";
	    }else if("other".equals(setType)){
	    	orgList = uupBroker.getAllOrgUserList();
	    	Set<String> orgList2 = new HashSet<String>(0);
	    	Set<String> orgList3 = new HashSet<String>(0);
	    	Set<String> removeOrgList = new HashSet<String>(0);
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
	    	return "other";
	    }else{
	    	return "default";
	    }
	}
	
	public ITaskService getManager() {
		return manager;
	}

	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public List getOrgList() {
		return orgList;
	}

	public void setOrgList(List orgList) {
		this.orgList = orgList;
	}

	public UupUtil getUupBroker() {
		return uupBroker;
	}

	@Autowired
	public void setUupBroker(UupUtil uupBroker) {
		this.uupBroker = uupBroker;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getSetType() {
		return setType;
	}

	public void setSetType(String setType) {
		this.setType = setType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List getUserList() {
		return userList;
	}

	public void setUserList(List userList) {
		this.userList = userList;
	}

	public String getNeedReturn() {
		return needReturn;
	}

	public void setNeedReturn(String needReturn) {
		this.needReturn = needReturn;
	}

	public String getPreActor() {
		return preActor;
	}

	public void setPreActor(String preActor) {
		this.preActor = preActor;
	}

	public String getTaskActors() {
		return taskActors;
	}

	public void setTaskActors(String taskActors) {
		this.taskActors = taskActors;
	}

	public String getIsSelectOtherActors() {
		return isSelectOtherActors;
	}

	public void setIsSelectOtherActors(String isSelectOtherActors) {
		this.isSelectOtherActors = isSelectOtherActors;
	}

	public String getMaxTaskActors() {
		return maxTaskActors;
	}

	public void setMaxTaskActors(String maxTaskActors) {
		this.maxTaskActors = maxTaskActors;
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

	public String getTransitionId() {
		return transitionId;
	}

	public void setTransitionId(String transitionId) {
		this.transitionId = transitionId;
	}
	
	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getAllowChangeMainActor() {
		return allowChangeMainActor;
	}

	public void setAllowChangeMainActor(String allowChangeMainActor) {
		this.allowChangeMainActor = allowChangeMainActor;
	}

}
