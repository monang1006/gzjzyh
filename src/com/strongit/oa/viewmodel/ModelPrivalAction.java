package com.strongit.oa.viewmodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaForamula;
import com.strongit.oa.bo.ToaForamulaRelation;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Role;
import com.strongit.oa.common.user.model.User;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.workflow.uupInterface.IUupInterface;
import com.strongmvc.webapp.action.BaseActionSupport;

//@ParentPackage("default")
//@Results( { @Result(name = BaseActionSupport.RELOAD, value = "viewmodel/modelPrival.action") })

@ParentPackage("default")
@Results( { @Result(name = "select", value = "/WEB-INF/jsp/viewmodel/modelPrival-select.jsp", type = ServletDispatcherResult.class),
			@Result(name = "org", value = "/WEB-INF/jsp/viewmodel/modelPrival-orgtree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "pos", value = "/WEB-INF/jsp/viewmodel/modelPrival-postree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "per", value = "/WEB-INF/jsp/viewmodel/modelPrival-pertree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "role", value = "/WEB-INF/jsp/viewmodel/modelPrival-roletree.jsp", type = ServletDispatcherResult.class),
			@Result(name = BaseActionSupport.RELOAD, value = "/viewmodel/modelPrival.action", type = ServletActionRedirectResult.class)})

public class ModelPrivalAction extends BaseActionSupport {

	private List orgList;
	
	@Autowired IUupInterface uupInterface;
	
	@Autowired IUserService userService ;
	
	private IModelPrivalService privalService;				//门户权限接口
	
	private ViewModelManager viewModelManager;				//首页模块MANAGER
	
	private ModelPrivalManager manager;					
	
	private String selectData;								//要保存的权限数据
	
	private String topTreeNodeId = "0";
	
	private List<ToaForamulaRelation> list=new ArrayList<ToaForamulaRelation>();
	
	private String foramulaId;								//首页模块ID
	
	private String selectedData;							//获取设置权限信息字符串			
	
	private String privilsortId;							
	
	private String privalSort;							//权限类型
	
	//	具体树形页面展现类型
	private String flag;
	
	@Override
	public String delete() throws Exception {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO 自动生成方法存根

	}
	
	public String selectPerson()throws Exception{
		privalSort="modelPrival";
		selectedData=manager.getSelectedDateByForamulaId(foramulaId);
		return "select";
	}

	@Override
	public String save() throws Exception {
		
		if(foramulaId!=null&&!foramulaId.equals("")){
			manager.deleteByForamulaId(foramulaId);
			if(selectData!=null&&!selectData.equals("")&&selectData.length()>1){
				
				list=manager.getSelectDataBySplit(selectData);
				manager.saveList(list);
			}
			 return renderHtml("sucess");
		}else{
			 return renderHtml("erroe");
		}
		 
		
	  
	}


	
	public String chooseTree() throws Exception{
		try{
		 if("org".equals(flag)){												//机构
			 orgList = privalService.getAllOrgList();   //原来的接口
			 Map<String,String> orgIdMapPid = new LinkedHashMap<String,String>();
			 Object[] temp;
			 for(int i=0; i<orgList.size(); i++){
				 temp=(Object[])orgList.get(i);
				 orgIdMapPid.put(String.valueOf(temp[0]), String.valueOf(temp[1]));
			 }
			 if(!orgIdMapPid.isEmpty()){
				 Set<String> keySet = orgIdMapPid.keySet();
				 for(String orgId:keySet){
					 String pid = orgIdMapPid.get(orgId);
					 if(!orgIdMapPid.containsKey(pid)){
						 topTreeNodeId = pid;
						 break;
					 }
				 }
			 }
			 
		//	 orgList = uupInterface.getAllOrgUserList();	 
//		    	Set<String> orgList2 = new HashSet<String>(0);
//		    	Set<String> orgList3 = new HashSet<String>(0);
//		    	Object[] temp;
//		    	for(int i=0; i<orgList.size(); i++){
//		    		temp=(Object[])orgList.get(i);
//		    		orgList2.add(String.valueOf(temp[1]));
//		    		orgList3.add(String.valueOf(temp[0]));
//		    	}
//		    	String orgId1=null,orgId2=null;
//		    	boolean containsFlag;
//		    	for(int i=0; i<orgList2.size(); i++){
//		    		orgId1 = orgList2.iterator().next();
//		    		containsFlag = false;
//		    		Iterator<String> it=orgList3.iterator();
//		    		while(it.hasNext()){
//		    			orgId2=it.next();
//		    			if(orgId1.equals(orgId2)){
//		    				containsFlag = true;
//		    				break;
//		    			}
//		    		}
//
//		    		if(containsFlag){
//	    				orgList2.remove(orgId1);
//	    				orgList3.remove(orgId2);
//		    			i=-1;
//		    		}else{
//		    			topTreeNodeId=orgId1;
//		    			break;
//		    		}
//		    	}
//		    	
//		    	orgList2 = null;
//		    	orgList3 = null;
		    	return "org";
		    }else if("pos".equals(flag)){														//岗位
		    	orgList = privalService.getAllOrgPostList();
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
		    	return "pos";
		    }else if ("role".equals(flag)) {
		    	orgList=privalService.getAllOrgRoleList();					//获取已经启用的角色
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
		    	return "role";
			}else if("per".equals(flag)){										//人员选择
		    	orgList = privalService.getAllOrgUserList();
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
		    	return "per";
		    }else{
		    	return "org";
		    }
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "org";
		 
	}
	
	/**
	/**
	 * 判断当前选择人员是否为非门户模型
	 * @author qibh
	 * @desc 
	 * 2012-03-27 下午19:13:46 
	 * @return   返回：true 是非门户，  返回： false 为门户用户
	 * @return
	 * @throws Exception
	 */
	public String isNotDoor() throws Exception{
		User user = userService.getCurrentUser();
		String userId=user.getUserId();
		String boo=manager.getForamulaIdByUserId(userId);
		if(boo!=null&&!boo.equals("")){
			return renderHtml("sucess");
		}else{
			return renderHtml("error");
		}
	    
	}
	
	/**
	/**
	 * 判断当前选择人员是否可以添加权限
	 * @author zhengzb
	 * @desc 
	 * 2011-1-20 下午05:13:46 
	 * @return   返回：true 可以添加权限，  返回： false 不可以添加 
	 * @return
	 * @throws Exception
	 */
	public String isHasSelect() throws Exception{
		Boolean boo=manager.isHasSelect(privilsortId, foramulaId);
		if(boo){
			return renderHtml("sucess");
		}else{
			return renderHtml("error");
		}
	    
	}
	/**
	/**
	 * 替换视图
	 * @author xush
	 * @desc 
	 * 12/11/2013 3:33 PM
	 * @return   删除原有视图设置
	 * @return
	 * @throws Exception
	 */
	public void replace() throws Exception{
		 manager.updateView(privilsortId, foramulaId);
		
	}
	/**
	 *设置权限时，判断当前首页模块是否已经生成预览
	 * @author zhengzb
	 * @desc 
	 * 2011-1-21 上午11:52:18 
	 * @return
	 * @throws Exception
	 */
	public String isHasCreatPage()throws Exception{
		ToaForamula foramula=viewModelManager.getObjById(foramulaId);
		if(foramula!=null&&foramula.getIsCreatePage()!=null&&foramula.getIsCreatePage().equals("1")){
			return renderHtml("sucess");
		}else{
			return renderHtml(foramula.getForamulaDec());
		}
	}
	
	public Object getModel() {  
		// TODO 自动生成方法存根
		return null;
	}

	public List getOrgList() {
		return orgList;
	}

	public void setOrgList(List orgList) {
		this.orgList = orgList;
	}

	public String getTopTreeNodeId() {
		return topTreeNodeId;
	}

	public void setTopTreeNodeId(String topTreeNodeId) {
		this.topTreeNodeId = topTreeNodeId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public IModelPrivalService getPrivalService() {
		return privalService;
	}

	@Autowired
	public void setPrivalService(IModelPrivalService privalService) {
		this.privalService = privalService;
	}

	public String getSelectData() {
		return selectData;
	}

	public void setSelectData(String selectData) {
		this.selectData = selectData;
	}

	public ModelPrivalManager getManager() {
		return manager;
	}

	@Autowired
	public void setManager(ModelPrivalManager manager) {
		this.manager = manager;
	}

	public String getForamulaId() {
		return foramulaId;
	}

	public void setForamulaId(String foramulaId) {
		this.foramulaId = foramulaId;
	}

	public String getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(String selectedData) {
		this.selectedData = selectedData;
	}

	public String getPrivilsortId() {
		return privilsortId;
	}

	public void setPrivilsortId(String privilsortId) {
		this.privilsortId = privilsortId;
	}

	public ViewModelManager getViewModelManager() {
		return viewModelManager;
	}

	@Autowired
	public void setViewModelManager(ViewModelManager viewModelManager) {
		this.viewModelManager = viewModelManager;
	}

	public String getPrivalSort() {
		return privalSort;
	}

	public void setPrivalSort(String privalSort) {
		this.privalSort = privalSort;
	}

}
