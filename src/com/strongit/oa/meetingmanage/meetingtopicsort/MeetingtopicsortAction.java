package com.strongit.oa.meetingmanage.meetingtopicsort;



import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaMeetingTopicsort;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.meetingmanage.util.MeetingmanageConst;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;


@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "meetingtopicsort.action", type = ServletActionRedirectResult.class) })
public class MeetingtopicsortAction extends AbstractBaseWorkflowAction {
	
	private String sortId;//议题分类ID
	//查询用字段
	private String querysortId;//编号
	private String sortName;//名称
	private String sortDes;//描述
	private String proName;//流程名称
	private String sortstatus;//是否停用
	private List<String> processList; //流程信息集
	private Map disableMap = new HashMap(); 
	
	private Page<ToaMeetingTopicsort> page = new Page<ToaMeetingTopicsort>(FlexTableTag.MAX_ROWS, true);//议题分类PAGE对象

	private ToaMeetingTopicsort model = new ToaMeetingTopicsort();
	
	private MeetingtopicsortManager topicsortManager;
	
	public MeetingtopicsortAction(){
		disableMap.put("0","已停用");
		disableMap.put("1","已启用");
	}

	@Override
	public String delete() throws Exception {
		try {
			topicsortManager.deleteSorts(sortId);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			renderText("deletefalse");
		}

		renderText("deletetrue");
		 return null;

	}

	@Override
	public String input() throws Exception {
		prepareModel();
//		查找流程类型对应的流程
		processList = new ArrayList<String>();
	//	List<Object[]> pList = topicsortManager.getAllWorkflowTypeLst(String.valueOf(WorkFlowTypeConst.MEETING));//会议管理流程类型为5
		processList= topicsortManager.getAllWorkflowTypeLst(String.valueOf(WorkFlowTypeConst.MEETING));//会议管理流程类型为5
		
	//	if(pList.size()>0){
	//		for(int i=0;i<pList.size();i++){
	//			Object[] process = pList.get(i);
	//			String proName = (String) process[0]; //流程名称
	//			processList.add(proName);
	//		}
	//	}
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		page=topicsortManager.queryTopicsort(page, querysortId, sortName,proName,sortDes,sortstatus);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if (sortId != null) {
			model = topicsortManager.getTopicsort(sortId);
		} else {
			model = new ToaMeetingTopicsort();
		}

	}

	@Override
	public String save() throws Exception {
		if ("".equals(model.getTopicsortId())) {
			model.setTopicsortId(null);
		}
		model.setIsDisbled(MeetingmanageConst.NO_DISABLE);
		topicsortManager.saveTopicsort(model);
		
		return renderHtml("<script>window.dialogArguments.location='"
				+ getRequest().getContextPath()
				+ "/meetingmanage/meetingtopicsort/meetingtopicsort.action'; window.close();</script>");
		// return RELOAD;
	}
	/**
	 * 将不用的会议分类停用
	 *@author 蒋国斌
	 *@date 2009-5-6 上午10:51:00
	 */
 public String setDisabled(){
	 model=topicsortManager.getTopicsort(sortId);
	 model.setIsDisbled(MeetingmanageConst.IS_DISABLE);
	 topicsortManager.updateTopicsort(model);
	 
	 return renderHtml("<script> window.location='"
				+ getRequest().getContextPath()
				+ "/meetingmanage/meetingtopicsort/meetingtopicsort.action';</script>");
 }
 
 /**
	 * 将已经停用的会议分类恢复启用
	 *@author 蒋国斌
	 *@date 2009-5-14 上午10:51:00
	 */
public String setRevive(){
	 model=topicsortManager.getTopicsort(sortId);
	 model.setIsDisbled(MeetingmanageConst.NO_DISABLE);
	 topicsortManager.updateTopicsort(model);
	 
	 return renderHtml("<script>window.location='"
				+ getRequest().getContextPath()
				+ "/meetingmanage/meetingtopicsort/meetingtopicsort.action';</script>");
}
/**
 * 根据流程名称获得流程图
 *@author 蒋国斌
 *@date 2009-5-20 上午09:34:09 
 * @return
 */
public String pdimage(){
	if(!this.proName.equals("")){
		try{
			proName = URLDecoder.decode(proName, "utf-8");
		}catch(Exception e){
			
		}
		this.getRequest().setAttribute("proName", proName);
	}
	return "pdimage";
}
	public ToaMeetingTopicsort getModel() {
		// TODO Auto-generated method stub
		return this.model;
	}

	public Page<ToaMeetingTopicsort> getPage() {
		return page;
	}

	public void setPage(Page<ToaMeetingTopicsort> page) {
		this.page = page;
	}

	public MeetingtopicsortManager getTopicsortManager() {
		return topicsortManager;
	}
	@Autowired
	public void setTopicsortManager(MeetingtopicsortManager topicsortManager) {
		this.topicsortManager = topicsortManager;
	}

	public void setModel(ToaMeetingTopicsort model) {
		this.model = model;
	}

	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	public String getQuerysortId() {
		return querysortId;
	}

	public void setQuerysortId(String querysortId) {
		this.querysortId = querysortId;
	}

	public String getSortDes() {
		return sortDes;
	}

	public void setSortDes(String sortDes) {
		this.sortDes = sortDes;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public List<String> getProcessList() {
		return processList;
	}

	public void setProcessList(List<String> processList) {
		this.processList = processList;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getSortstatus() {
		return sortstatus;
	}

	public void setSortstatus(String sortstatus) {
		this.sortstatus = sortstatus;
	}

	public Map getDisableMap() {
		return disableMap;
	}

	public void setDisableMap(Map disableMap) {
		this.disableMap = disableMap;
	}

	@Override
	protected String getDictType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseManager getManager() {
		// TODO Auto-generated method stub
		return this.topicsortManager;
	}

	@Override
	protected String getModuleType() {
		// TODO Auto-generated method stub
		return String.valueOf(WorkFlowTypeConst.MEETING);
	}

	@Override
	protected String getWorkflowType() {
		// TODO Auto-generated method stub
	 return String.valueOf(WorkFlowTypeConst.MEETING);
	}


}
