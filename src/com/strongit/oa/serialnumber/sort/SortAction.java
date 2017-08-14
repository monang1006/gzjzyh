/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-7-09
 * Autour: hull
 * Version: V1.0
 * Description： 文号类型action
 */
package com.strongit.oa.serialnumber.sort;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaArchiveSort;
import com.strongit.oa.bo.ToaSerialnumberSort;
import com.strongit.oa.serialnumber.regulation.RegulationManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * 文号类型action
 * @author 胡丽丽
 * @date:2009-12-05
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "sort.action", type = ServletActionRedirectResult.class) })
public class SortAction extends BaseActionSupport {
	
	/** 文号类型BO*/
	private ToaSerialnumberSort model=new ToaSerialnumberSort();
	
	private Page<ToaSerialnumberSort> page=new Page<ToaSerialnumberSort>(FlexTableTag.MAX_ROWS,true);
	
	/** 文号类型Manager*/
	private SortManager sortManager;
	
	/** 文号类型ID*/
	private String sortId;
	
	/** 公文代字*/
	private String  sortAbbreviation;

	/** 文号类型名称*/
	private String sortName;
	
	/** 添加时间*/
	private Date sortTime;
	
	/** 文号规则manager*/
	private RegulationManager regulationManager;
	
	@Autowired
	public void setRegulationManager(RegulationManager regulationManager) {
		this.regulationManager = regulationManager;
	}


	@Override
	public String delete() throws Exception {
		if(sortId!=null&&!"".equals(sortId)){// 判断ID是否为空
		    /**拆分文号类型ID*/
			String[] id=sortId.split(",");
			for(int i=0;i<id.length;i++){//循环删除文号类型
				int count=regulationManager.getIsSortId(id[i]);
				if(count>0){
					String msg="当前你所要删除的文号类型，已经被文号规则中引用，不能删除 ，请先删除包括“当前文号类型”的“文号规则”！";
					StringBuffer returnhtml = new StringBuffer(
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
					returnhtml.append("alert('")
						.append(msg)
						.append("');");
					returnhtml.append(" location='")
					.append(getRequest().getContextPath())
					.append("/serialnumber/sort/sort.action")
					.append("';</script>");
				return	renderHtml(returnhtml.toString());
				}
			}
			sortManager.deleteSort(sortId);
		}
		return RELOAD;
	}

	/**
	 * 添加和修改初始化
	 */
	@Override
	public String input() throws Exception {
		prepareModel();
		int count=regulationManager.getIsSortId(sortId);
		if(count>0){
			String msg="当前所要编辑的文号类型，已经被文号规则中引用，不能编辑；请先修改包含【当前文号类型】的【文号规则】！！";
			StringBuffer returnhtml = new StringBuffer(
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
			returnhtml.append("alert('")
				.append(msg)
				.append("');");
			returnhtml.append(" location='")
			.append(getRequest().getContextPath())
			.append("/serialnumber/sort/sort.action")
			.append("';</script>");
		return	renderHtml(returnhtml.toString());
		}else {
			
			return INPUT;
		}
		
	}

	/**
	 * 获取文号类型列表
	 */
	@Override
	public String list() throws Exception {
		//判断是否是多条件搜索
		if(sortName!=null||sortAbbreviation!=null||sortTime!=null){//判断文号类型名称，文号代字，编辑时间是否不为空
			if(sortName!=null&&!"".equals(sortName)){//判断要搜索的文号名称是否为空
				model.setSortName(sortName);
			}
			if(sortAbbreviation!=null&&!"".equals(sortAbbreviation)){//判断要搜索的文号代字是否为空
				model.setSortAbbreviation(sortAbbreviation);
			}
			if(sortTime!=null&&!"".equals(sortTime)){//判读要搜索编辑时间是否为空
				model.setSortTime(sortTime);
			}
			page=sortManager.getSortsByProperty(page, model);
		}else{
			page=sortManager.getAllSort(page);
		}
		if(page.getResult()==null){
			page.setTotalCount(0);
		}
		return SUCCESS;
	}

	/**
	 * 初始化BO
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(sortId==null||"".equals(sortId)){//判断ID是否为空
			model=new ToaSerialnumberSort();
		}else{
			model=sortManager.getSortById(sortId);
		}

	}

	/**
	 * 保存文号类型
	 */
	@Override
	public String save() throws Exception {
		if(model.getSortTime()==null||"".equals(model.getSortTime())){//判断时间是否为空
			Date time=new Date();
			model.setSortTime(time);
		}
		if("".equals(model.getSortId())){
			model.setSortId(null);
		}
		sortManager.saveSort(model);
		return RELOAD;
	}

	/**
	 * 查询文号代字
	 * @return
	 * @throws Exception
	 */
	public String select() throws Exception{
		StringBuffer strb=new StringBuffer();
		if(sortId!=null&&!"".equals(sortId)){
			model=sortManager.getSortById(sortId);
		}
		sortName=URLDecoder.decode(sortName, "utf-8");
		
		List<String> list=null;
		if(model.getSortAbbreviation()!=null){
			list=this.getSplit(model.getSortAbbreviation());
			
		}
		strb.append("<option value=''>请选择文号代字</option>");
		if(list!=null){
			for(String str:list){
				if(str.equals(sortName)){
					strb.append("<option  selected='selected'  value='"+str+"'>"+str+"</option>");
				}else
				strb.append("<option value='"+str+"'>"+str+"</option>");
			}
		}
		
		return renderHtml(strb.toString());
	
	}
	
	/**
	 * 分割字符串
	 * @param temp
	 * @return
	 */
	private List<String> getSplit(String temp){
		List<String> list=new ArrayList<String>();
		String[] str= temp.split(",");
		for(int i=0;i<str.length;i++){
			if(str[i]!=null&&!"".equals(str[i])){
				list.add(str[i]);
			}
		}
		return list;
	}
	
	public String IsSortName() throws Exception{
		StringBuffer returnhtml=new StringBuffer();
		if(sortName != null && !"".equals(sortName)){//文号规则名	
			int result=sortManager.getIsSortName(sortName);
			if(result>0){
				if(sortId!=null&&!sortId.equals("")){//修改文号类型时，判断文号类型名是否相同
					ToaSerialnumberSort sortModel=sortManager.getSortById(sortId);
					if(sortModel.getSortName()!=null&&sortModel.getSortName().equals(sortName)){
						returnhtml.append("1");
					}else {
						returnhtml.append("0");//当前文号名存在
					}
				}else{					
					returnhtml.append("0");//当前文号名存在
				}
			}
			else{
				returnhtml.append("1");
			}
		}
		else {
			returnhtml.append("1");
		}
		return renderHtml(returnhtml.toString());
	}
	
	public ToaSerialnumberSort getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public Page<ToaSerialnumberSort> getPage() {
		return page;
	}

	public void setPage(Page<ToaSerialnumberSort> page) {
		this.page = page;
	}

	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

    @Autowired
	public void setSortManager(SortManager sortManager) {
		this.sortManager = sortManager;
	}

    public String getSortAbbreviation() {
		return sortAbbreviation;
	}

	public void setSortAbbreviation(String sortAbbreviation) {
		this.sortAbbreviation = sortAbbreviation;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public Date getSortTime() {
		return sortTime;
	}

	public void setSortTime(Date sortTime) {
		this.sortTime = sortTime;
	}
}
