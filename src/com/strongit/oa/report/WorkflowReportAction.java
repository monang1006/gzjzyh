package com.strongit.oa.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaReportDefinition;
import com.strongit.oa.bo.ToaReportPrivilset;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "workflowReport.action", type = ServletActionRedirectResult.class) })
public class WorkflowReportAction extends BaseActionSupport {
	
	
	
	protected Map<String, List<Object[]>>	reportMap;				//定义报表集合
	
	protected List<Object[]> reportSortList ;						//报表类型列表
	
	private ReportDefineManager reportDefineManager;
	
	private ReportPrivilsetManager privilsetManager;				//权限
	
	@Autowired  IUserService userService ;//统一用户服务

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
		User userInfo = userService.getCurrentUser();
		List<Object[]> objList=privilsetManager.getReportPrivilsetByUserId(userInfo.getUserId());							//获取当前用户查询定义报表权限
		List<Object[]> creatList=reportDefineManager.getListByUserId(userInfo.getUserId());												//根据用户ID查询创建定义报表LIST
//		List<ToaReportDefinition> rdList=reportDefineManager.getAllList();
		Map<String, Object[]>rdMap=new HashMap<String, Object[]>();
		
		if(objList!=null&&!objList.isEmpty()){
			for(Object[] obj:objList){				
					rdMap.put(obj[3].toString(), obj);
			}
		}
		if(creatList!=null&&!creatList.isEmpty()){
			for(Object[] obj:creatList){				
					rdMap.put(obj[3].toString(), obj);
			}
		}
		List list = new LinkedList();
		if(rdMap!=null&&rdMap.size()>0){
			
			for(Iterator it=rdMap.keySet().iterator();it.hasNext();){
				String keyId=it.next().toString();
				Object[] obj=rdMap.get(keyId);
				list.add(obj);
			}
			if(list != null && !list.isEmpty()) {
				reportMap = new HashMap<String, List<Object[]>>(list.size());
				reportSortList = new ArrayList<Object[]>(list.size());
				List<String> workflowTpeIdList = new LinkedList<String>();
				for(int i=0;i<list.size();i++){
					Object[] workflowInfo = (Object[])list.get(i);
					if(!workflowTpeIdList.contains(workflowInfo[1].toString())) {
						workflowTpeIdList.add(workflowInfo[1].toString());
						reportSortList.add(new Object[]{workflowInfo[1],workflowInfo[2]});					
					}
					if(!reportMap.containsKey(workflowInfo[1].toString())){
						List<Object[]> workflowList = new ArrayList<Object[]>(1);
						workflowList.add(new Object[]{workflowInfo[0],workflowInfo[3]});
						reportMap.put(workflowInfo[1].toString(), workflowList);
					} else {
						reportMap.get(workflowInfo[1].toString()).add(new Object[]{workflowInfo[0],workflowInfo[3]});
					}
				}
			}
		}
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO 自动生成方法存根

	}

	@Override
	public String save() throws Exception {
		// TODO 自动生成方法存根
		return null;
	}

	public Object getModel() {
		// TODO 自动生成方法存根
		return null;
	}

	public List<Object[]> getReportSortList() {
		return reportSortList;
	}

	public void setReportSortList(List<Object[]> reportSortList) {
		this.reportSortList = reportSortList;
	}

	public ReportDefineManager getReportDefineManager() {
		return reportDefineManager;
	}

	@Autowired
	public void setReportDefineManager(ReportDefineManager reportDefineManager) {
		this.reportDefineManager = reportDefineManager;
	}

	public Map<String, List<Object[]>> getReportMap() {
		return reportMap;
	}

	public void setReportMap(Map<String, List<Object[]>> reportMap) {
		this.reportMap = reportMap;
	}

	public ReportPrivilsetManager getPrivilsetManager() {
		return privilsetManager;
	}

	@Autowired
	public void setPrivilsetManager(ReportPrivilsetManager privilsetManager) {
		this.privilsetManager = privilsetManager;
	}

}
