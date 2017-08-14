package com.strongit.oa.personnel.deploymanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.PersonDeployInfo;
import com.strongit.oa.bo.ToaPersonDeploy;
import com.strongit.oa.infotable.IInfoTableService;
import com.strongit.tag.web.grid.stronger.FlexTableTag;

import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class DeploymanageAction extends BaseActionSupport {

	private Page<ToaPersonDeploy> page = new Page<ToaPersonDeploy>(FlexTableTag.MAX_ROWS, true);

	private Deploymanager deployManager;
	private List<ToaPersonDeploy> typeList;
	private List<PersonDeployInfo> infoList;
	private ToaPersonDeploy model = new ToaPersonDeploy();

	private String deployId;

	private Map veternMap = new HashMap();

	private Map activeMap = new HashMap();


	private String pName;

	private String pisveteran;

	private String pIsactiv;
	
	private String struct;
	
	private List dataRowTitle;

	private IInfoTableService manager;//信息项接口


	private DeploymanageAction() {
		veternMap.put("0", "未转入");
		veternMap.put("1", "转入");

		activeMap.put("0", "未启用");
		activeMap.put("1", "启用");

	}


	public String getPIsactiv() {
		return pIsactiv;
	}


	public void setPIsactiv(String isactiv) {
		pIsactiv = isactiv;
	}


	public String getPisveteran() {
		return pisveteran;
	}


	public void setPisveteran(String pisveteran) {
		this.pisveteran = pisveteran;
	}


	public String getPName() {
		return pName;
	}


	public void setPName(String name) {
		pName = name;
	}


	public Deploymanager getDeployManager() {
		return deployManager;
	}

	@Autowired
	public void setDeployManager(Deploymanager deployManager) {
		this.deployManager = deployManager;
	}

	@Override
	public String delete() throws Exception {
		try {
			deployManager.deletePersonDeploy(deployId);
			
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
		this.prepareModel();
		return INPUT;
	}

	@Override
	public String list() throws Exception {

		page = deployManager.queryDeploy(page,pName,pisveteran,
				pIsactiv);
		return SUCCESS;
	}

	public String deploy() {
		infoList=deployManager.getDeployeInfos();
		return "deploy";
	}
	@Override
	protected void prepareModel() throws Exception {
		if (deployId != null) {
			model = deployManager.getOnePersonDeploy(deployId);
		} else {
			model = new ToaPersonDeploy();
		}
	}

	@Override
	public String save() throws Exception {
		if ("".equals(model.getPdepId())) {
			model.setPdepId(null);
		}
		deployManager.saveToaPersonDeploy(model);

		return renderHtml("<script>window.dialogArguments.location='"
				+ getRequest().getContextPath()
				+ "/personnel/deploymanage/deploymanage.action'; window.close();</script>");
	}
	
	/**
	 * 获取信息项列表
	 * 
	 * @return java.lang.String
	 * @roseuid 49479C710177
	 */
	public String rowlist() throws Exception{
		dataRowTitle = manager.getSelectItems(struct);
		return "rowlist";
	}

	public ToaPersonDeploy getModel() {
		return model;
	}

	public Page<ToaPersonDeploy> getPage() {
		return page;
	}

	public void setPage(Page<ToaPersonDeploy> page) {
		this.page = page;
	}

	public Map getActiveMap() {
		return activeMap;
	}

	public void setActiveMap(Map activeMap) {
		this.activeMap = activeMap;
	}

	public Map getVeternMap() {
		return veternMap;
	}

	public void setVeternMap(Map veternMap) {
		this.veternMap = veternMap;
	}

	public void setModel(ToaPersonDeploy model) {
		this.model = model;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public List<PersonDeployInfo> getInfoList() {
		return infoList;
	}

	public void setInfoList(List<PersonDeployInfo> infoList) {
		this.infoList = infoList;
	}

	public List<ToaPersonDeploy> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<ToaPersonDeploy> typeList) {
		this.typeList = typeList;
	}

	public List getDataRowTitle() {
		return dataRowTitle;
	}

	public void setDataRowTitle(List dataRowTitle) {
		this.dataRowTitle = dataRowTitle;
	}

	public IInfoTableService getManager() {
		return manager;
	}
	@Autowired
	public void setManager(IInfoTableService manager) {
		this.manager = manager;
	}

	public String getStruct() {
		return struct;
	}

	public void setStruct(String struct) {
		this.struct = struct;
	}

}
