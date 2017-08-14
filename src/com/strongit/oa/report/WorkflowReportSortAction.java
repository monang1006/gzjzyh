package com.strongit.oa.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaReportDefinition;
import com.strongit.oa.bo.ToaReportSort;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "workflowReportSort.action", type = ServletActionRedirectResult.class) })
public class WorkflowReportSortAction extends BaseActionSupport {
	
	private Page<ToaReportSort> page=new Page<ToaReportSort>(FlexTableTag.MAX_ROWS,true);
	
	private ToaReportSort model=new ToaReportSort();
	
	private WorkflowReportSortManager manager;
	
	private ReportDefineManager reportDefineManager;			//定义报表MANAGER
	
	private IUserService userService;							//统一用户接口
	
	private String sortId;
	
	private String sortName;
	
	private String checkSort;						//定义报表时，选择报表类型

	@Override
	public String delete() throws Exception {
		String [] sortIds=sortId.split(",");
		for(int i=0;i<sortIds.length;i++){
			manager.delReoprtSortById(sortIds[i]);
		}
		return RELOAD;
	}

	@Override
	public String input() throws Exception {
		
		return "input";
	}

	@Override
	public String list() throws Exception {
		page=manager.getPage(page, model);
		if(checkSort!=null&&!checkSort.equals("")){
			return "checkSort";
		}else {
			return SUCCESS;
		}
		
	}

	@Override
	protected void prepareModel() throws Exception {
		if(sortId!=null&&!sortId.equals("")){
			model=manager.getReportSortById(sortId);
		}else {
			model=new ToaReportSort();
		}

	}

	@Override
	public String save() throws Exception {
		 String msg="保存失败！";
		if(model.getSortId()!=null&&"".equals(model.getSortId())){
			model.setSortId(null);
		}
		 User user=userService.getCurrentUser();
		 TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(user.getUserId());
		 if(org!=null){
			 model.setSortOrgId(org.getOrgId());
			 model.setSortOrgCode(org.getSupOrgCode());
		 }
		manager.save(model);
		 msg="保存成功!";
		 addActionMessage(msg);
		 StringBuffer returnhtml=new StringBuffer("");
		 returnhtml.append(
			"<script> var scriptroot = '")
			.append(getRequest().getContextPath())
			.append("'</script>")
			.append("<SCRIPT src='")
			.append(getRequest().getContextPath())
			.append(
					"/common/js/commontab/workservice.js'>")
			.append("</SCRIPT>")
			.append("<SCRIPT src='")
			.append(getRequest().getContextPath())
			.append(
					"/common/js/commontab/service.js'>")
			.append("</SCRIPT>")
			.append("<script>");
			
			returnhtml.append("window.dialogArguments.window.location = window.dialogArguments.window.location; self.close();")
			.append("</script>");
			
			
	   return renderHtml(returnhtml.toString());
	}

	/**
	 * 判断报表类型名是否存在
	 * @author zhengzb
	 * @desc 
	 * 2010-12-17 上午10:35:35 
	 * @return
	 * @throws Exception
	 */
	public String isHasSortName() throws Exception {
		boolean boo=manager.isHasSortName(sortName,sortId);
		if(boo){
			return renderText("1");
		}else {
			return renderText("0");
		}
	}
	
	//判断报表类型是否被引用
	public String isHasDelete()throws Exception{
		String [] sortIds=sortId.split(",");
		String sortName="";
		for(int i=0;i<sortIds.length;i++){
			List<ToaReportDefinition>rdList=reportDefineManager.getDefinitionListBySortId(sortIds[i]);
			if(rdList!=null&&rdList.size()>0){
				model=manager.getReportSortById(sortIds[i]);
				sortName+=","+model.getSortName();
			}
		}
		if(sortName.length()>1){
			sortName=sortName.substring(1);
			return renderText(sortName);
		}else {
			return renderText("1");
		}
	}
	
	
	public ToaReportSort getModel() {
		// TODO 自动生成方法存根
		return model;
	}

	public Page<ToaReportSort> getPage() {
		return page;
	}

	public void setPage(Page<ToaReportSort> page) {
		this.page = page;
	}

	public WorkflowReportSortManager getManager() {
		return manager;
	}
	@Autowired
	public void setManager(WorkflowReportSortManager manager) {
		this.manager = manager;
	}

	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	public String getCheckSort() {
		return checkSort;
	}

	public void setCheckSort(String checkSort) {
		this.checkSort = checkSort;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public ReportDefineManager getReportDefineManager() {
		return reportDefineManager;
	}

	@Autowired
	public void setReportDefineManager(ReportDefineManager reportDefineManager) {
		this.reportDefineManager = reportDefineManager;
	}

	public IUserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	
}

