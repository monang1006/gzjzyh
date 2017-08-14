/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-22
 * Autour: lanlc
 * Version: V1.0
 * Description：公文文号历史记录action
 */

package com.strongit.oa.docnumber;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaDocnumberHistory;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;


@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "docNumberHistory.action", type = ServletActionRedirectResult.class) })
public class DocNumberHistoryAction extends BaseActionSupport<ToaDocnumberHistory> {

	private static final long serialVersionUID = -4875428408264914540L;
	
	private ToaDocnumberHistory model=new ToaDocnumberHistory();
	private Page<ToaDocnumberHistory> page = new Page<ToaDocnumberHistory>(FlexTableTag.MAX_ROWS, true);
	private String docnumberHistoryid;
	private String searchdocnhword;
	private DocNumberHistoryManager docNumberHistoryManager;
	
	public DocNumberHistoryAction(){
	}
	public ToaDocnumberHistory getModel() {
		return model;
	}

	public void setModel(ToaDocnumberHistory model) {
		this.model = model;
	}

	public Page getPage() {
		return page;
	}
	public String getDocnumberHistoryid() {
		return docnumberHistoryid;
	}

	public void setDocnumberHistoryid(String docnumberHistoryid) {
		this.docnumberHistoryid = docnumberHistoryid;
	}
	@Autowired
	public void setDocNumberHistoryManager(
			DocNumberHistoryManager docNumberHistoryManager) {
		this.docNumberHistoryManager = docNumberHistoryManager;
	}
	
	/**
	 * 删除公文文号记录
	 */
	@Override
	public String delete() throws Exception {
		try{
			docNumberHistoryManager.deleteDocnumberHistory(docnumberHistoryid);
			addActionMessage("删除公文文号成功");
		}catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			addActionMessage(e.getMessage());
		}
		return RELOAD;
	}

	@Override
	public String input() throws Exception {
		prepareModel();
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		page=docNumberHistoryManager.getdocNumberHistoryList(page,model,searchdocnhword);
		return SUCCESS;
	}
	
	
	@Override
	protected void prepareModel() throws Exception {
		if(docnumberHistoryid!=null){
			model = docNumberHistoryManager.getDocnumberHistory(docnumberHistoryid);
		}else{
			model = new ToaDocnumberHistory();
		}
		
	}
	
	/**
	 * 保存公文文号记录
	 */
	@Override
	public String save() throws Exception {
		if("".equals(model.getDocnumberHistoryid())){
			model.setDocnumberHistoryid(null);
		}
		docNumberHistoryManager.saveDocnumberHistory(model);
		return RELOAD;
	}
	
	/**
	 * 判断公文字号历史记录中是否已存在
	 * @param docnumber
	 * @return
	 */
	public boolean isExist(String docnumber)throws Exception{
		boolean isexist=docNumberHistoryManager.isExist(docnumber);
		return isexist;
	}
	public String getSearchdocnhword() {
		return searchdocnhword;
	}
	public void setSearchdocnhword(String searchdocnhword) {
		this.searchdocnhword = searchdocnhword;
	}
	

	

}
