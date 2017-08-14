package com.strongit.oa.desktop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaPortalPrival;
import com.strongit.oa.viewmodel.IModelPrivalService;
import com.strongmvc.webapp.action.BaseActionSupport;


@ParentPackage("default")
@Results( { @Result(name = "select", value = "/WEB-INF/jsp/viewmodel/modelPrival-select.jsp", type = ServletDispatcherResult.class),
			@Result(name = "org", value = "/WEB-INF/jsp/viewmodel/modelPrival-orgtree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "pos", value = "/WEB-INF/jsp/viewmodel/modelPrival-postree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "per", value = "/WEB-INF/jsp/viewmodel/modelPrival-pertree.jsp", type = ServletDispatcherResult.class),
			@Result(name = "role", value = "/WEB-INF/jsp/viewmodel/modelPrival-roletree.jsp", type = ServletDispatcherResult.class),
			@Result(name = BaseActionSupport.RELOAD, value = "/desktop/protalPrival.action", type = ServletActionRedirectResult.class)})
public class ProtalPrivalAction extends BaseActionSupport {
	
	private ProtalPrivalManager  manager;
	
	private IModelPrivalService privalService;				//门户权限接口
	
	private String selectedData;										//获取已设置权限信息字符串	
	
	private String portalId;											//门户ID
	
	private String selectData;											//要保存的权限数据
	
	private List orgList;
	
	private String topTreeNodeId = "0";
	
	private String privalId;										//权限ID
	
	//	具体树形页面展现类型
	private String flag;
	
	private String privalSort;									
	
	private List<ToaPortalPrival> list=new ArrayList<ToaPortalPrival>();
	
	
	public String selectPerson()throws Exception{
		privalSort="protalPrival";
		selectedData=manager.getSelectedDateByPortalId(portalId);
		return "select";
	}
	

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

	@Override
	public String save() throws Exception {
		
		if(portalId!=null&&!portalId.equals("")){
			manager.deleteByPortalId(portalId);
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
		    	orgList = privalService.getAllOrgList();
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
	 * 判断当前选择人员是否可以添加权限
	 * @author zhengzb
	 * @desc 
	 * 2011-1-20 下午05:13:46 
	 * @return   返回：true 可以添加权限，  返回： false 不可以添加 
	 * @return
	 * @throws Exception
	 */
	public String isHasSelect() throws Exception{
		Boolean boo=manager.isHasSelect(privalId, portalId);
		if(boo){
			return renderHtml("sucess");
		}else{
			return renderHtml("error");
		}
	    
	}
	
	
	public Object getModel() {
		// TODO 自动生成方法存根
		return null;
	}

	public ProtalPrivalManager getManager() {
		return manager;
	}

	@Autowired
	public void setManager(ProtalPrivalManager manager) {
		this.manager = manager;
	}


	public String getSelectedData() {
		return selectedData;
	}


	public void setSelectedData(String selectedData) {
		this.selectedData = selectedData;
	}


	public String getPortalId() {
		return portalId;
	}


	public void setPortalId(String portalId) {
		this.portalId = portalId;
	}


	public String getSelectData() {
		return selectData;
	}


	public void setSelectData(String selectData) {
		this.selectData = selectData;
	}


	public List<ToaPortalPrival> getList() {
		return list;
	}


	public void setList(List<ToaPortalPrival> list) {
		this.list = list;
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


	public IModelPrivalService getPrivalService() {
		return privalService;
	}


	@Autowired
	public void setPrivalService(IModelPrivalService privalService) {
		this.privalService = privalService;
	}


	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}


	public String getPrivalId() {
		return privalId;
	}


	public void setPrivalId(String privalId) {
		this.privalId = privalId;
	}


	public String getPrivalSort() {
		return privalSort;
	}


	public void setPrivalSort(String privalSort) {
		this.privalSort = privalSort;
	}

}
