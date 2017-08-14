package com.strongit.oa.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaReportDefinition;
import com.strongit.oa.bo.ToaReportPrivilset;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;


@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "reportPrivilset.action", type = ServletActionRedirectResult.class) })
public class ReportPrivilsetAction extends BaseActionSupport {
	
	private ReportPrivilsetManager manager;
	
	private Page<ToaReportPrivilset> page=new Page<ToaReportPrivilset>(FlexTableTag.MAX_ROWS,true);
	
	private List<ToaReportPrivilset> psList;
	
	private String privilsetId;
	
	private String definitionId; //报表定义ID
	
	private String definitionName; //报表定义ID
	
	private String privilsetTypeName;

	private String privilsetTypeId;
	
	private String privilsetTypeFlag;
	
	private String privilsetLevel;
	
	private ToaReportPrivilset model=new ToaReportPrivilset();
	
	@Override
	public String delete() throws Exception {
		try{
			
			String[] privilsetIds=privilsetId.split(",");
			for(int i=0;i<privilsetIds.length;i++){
				manager.delete(privilsetIds[i]);
			}
			
			return renderText("succ");
		}catch(Exception e){
			return renderText("fail");
		}
	}

	@Override
	public String input() throws Exception {
		// TODO 自动生成方法存根
		return "input";
	}

	@Override
	public String list() throws Exception {
		List<ToaReportPrivilset> psList=manager.getPrivisetListByDefinitionId(definitionId);
		definitionName = manager.getDefinitionByDefinitionId(definitionId).getDefinitionName();
		this.getRequest().setAttribute("psList", psList);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(model==null){
			model = new ToaReportPrivilset();
		}
	}

	@Override
	public String save() throws Exception {
		ToaReportDefinition toaReportDefinition = manager.getDefinitionByDefinitionId(definitionId);
		
		model.setPrivilsetLevel(privilsetLevel);
		model.setPrivilsetTypeflag(privilsetTypeFlag);
		model.setPrivilsetTypeid(privilsetTypeId);
		model.setPrivilsetTypename(privilsetTypeName);
		model.setToaReportDefinition(toaReportDefinition);
		
		String ret = manager.save(model);
		
		return renderText(ret);
	}

	public String privil() throws Exception {
		
		return "inputPrivil";
	}
	
	public String showTreeWithCheckbox() throws Exception  {
		List<TempPo> list = new ArrayList<TempPo>();
		if("1".equals(privilsetTypeFlag)){//部门
			list = manager.getOrgTreeList();
			privilsetTypeName = "部门";
		}else if("2".equals(privilsetTypeFlag)){//角色
			list = manager.getRoleTreeList();
			privilsetTypeName = "角色";
		}
		if(!list.isEmpty()){
			TempPo firstPo = list.get(0);
			getRequest().setAttribute("root", firstPo.getName());
		}else{
			getRequest().setAttribute("root", "未找到记录");
		}
		getRequest().setAttribute("list",list);
		return "choosetree";
	}
	
	public ToaReportPrivilset getModel() {
		// TODO 自动生成方法存根
		return null;
	}

	public String getPrivilsetId() {
		return privilsetId;
	}

	public void setPrivilsetId(String privilsetId) {
		this.privilsetId = privilsetId;
	}

	@Autowired
	public void setManager(ReportPrivilsetManager manager) {
		this.manager = manager;
	}

	public Page<ToaReportPrivilset> getPage() {
		return page;
	}

	public void setPage(Page<ToaReportPrivilset> page) {
		this.page = page;
	}

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public String getPrivilsetLevel() {
		return privilsetLevel;
	}

	public void setPrivilsetLevel(String privilsetLevel) {
		this.privilsetLevel = privilsetLevel;
	}

	public String getPrivilsetTypeFlag() {
		return privilsetTypeFlag;
	}

	public void setPrivilsetTypeFlag(String privilsetTypeFlag) {
		this.privilsetTypeFlag = privilsetTypeFlag;
	}

	public String getPrivilsetTypeId() {
		return privilsetTypeId;
	}

	public void setPrivilsetTypeId(String privilsetTypeId) {
		this.privilsetTypeId = privilsetTypeId;
	}

	public String getPrivilsetTypeName() {
		return privilsetTypeName;
	}

	public void setPrivilsetTypeName(String privilsetTypeName) {
		this.privilsetTypeName = privilsetTypeName;
	}

	public String getDefinitionName() {
		return definitionName;
	}

	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	public List<ToaReportPrivilset> getPsList() {
		return psList;
	}


}
