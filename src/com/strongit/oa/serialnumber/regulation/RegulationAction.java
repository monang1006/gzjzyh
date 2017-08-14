/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-7-09
 * Autour: hull
 * Version: V1.0
 * Description： 文号规则action
 */
package com.strongit.oa.serialnumber.regulation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSerialnumberRegulation;
import com.strongit.oa.bo.ToaSerialnumberSort;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.serialnumber.sort.SortManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 文号规则action
 * @author hull
 * @date:2009-12-05
 *
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "regulation.action", type = ServletActionRedirectResult.class) })
public class RegulationAction extends BaseActionSupport {

	private Page<ToaSerialnumberRegulation> page=new Page<ToaSerialnumberRegulation>(FlexTableTag.MAX_ROWS,true);

	/** 序列规则Manager*/
	private RegulationManager regulationManager;

	/** 序列规则BO*/
	private ToaSerialnumberRegulation model=new ToaSerialnumberRegulation();

	/** 序列规则ID*/
	private String regulationId;

	/** 文号类型列表*/
	private List<ToaSerialnumberSort> sortList;

	/** 文号类型Manager*/
	private SortManager sortManager;

	/** 段落*/
	private String paragraph;
	
	private String regulationSort;			//文号规则类型：

	/** 文件年份*/
	private String ruleYear;

	/** 文号类型*/
	private String ruleNumber;

	/** 单位代字*/
	private String abbreviate;

	/** 字母缩写*/
	private String letter;

	/** 文号类型ID*/
	private String sortId;

	/** 两段*/
	private static final String TWO="2";

	/** 三段*/
	private static final String THREE="3";

	/** 四段*/
	private static final String FOUR="4";

	/** 用户IUserService*/
	IUserService userService;

	/** 规则名称*/
	private String regulationName;

	/** 规则创建人*/
	private String regulationUser;

	/** 创建时间*/
	private Date regulationTime;

	/** 规则段落*/
	private String regulationParagraph;

	
	/**
	 * 根据ID删除序列规则
	 */
	@Override
	public String delete() throws Exception {
		if(regulationId!=null&&!"".equals(regulationId)){
			regulationManager.deleteRegulation(regulationId);
		}
		return RELOAD;
	}

	/**
	 * 添加和修改页面初始化
	 */
	@Override
	public String input() throws Exception {
		prepareModel();
		sortList=sortManager.getAllSort();
		return INPUT;
	}

	/**
	 * 获取序列规则列表
	 */
	@Override
	public String list() throws Exception {
		if (regulationName != null || regulationParagraph != null
				|| regulationUser != null || regulationTime != null) {// 判读是否走了搜索
			if (regulationName != null && !"".equals(regulationName)) {// 规则名称是否为空
				model.setRegulationName(regulationName);
			}
			if (regulationParagraph != null && !"".equals(regulationParagraph)) {// 规则段落是否为空
				model.setRegulationParagraph(regulationParagraph);
			}
			if (regulationUser != null && !"".equals(regulationUser)) {// 创建人是否为空
				model.setRegulationUser(regulationUser);
			}
			if (regulationTime != null) {// 创建时间是否为空
				model.setRegulationTime(regulationTime);
			}
			if(regulationSort!=null&&!regulationSort.equals("")){
				model.setRegulationSort(regulationSort);
			}
			page = regulationManager.getRegulationByProperty(page, model);
		} else {
			page = regulationManager.getAllRegulation(page);
		}
		if(page.getResult()==null){
			page.setTotalCount(0);
		}
		return SUCCESS;
	}

	/**
	 * 对文号规则名，进行判断，是否含有重名
	 */
	public String IsRegulationName() throws Exception{
		StringBuffer returnhtml=new StringBuffer();
		if(regulationName != null && !"".equals(regulationName)&&(regulationId==null||"".equals(regulationId))){//文号规则名	
			int result=regulationManager.getIsRegulationName(regulationName);
			if(result>0){
				returnhtml.append("0");//当前文号名存在
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
	
	
	
	/**
	 * BO初始化
	 */
	@Override
	protected void prepareModel() throws Exception {
		if(regulationId==null||"".equals(regulationId)){
			model=new ToaSerialnumberRegulation();
		}else{
			model=regulationManager.getRegulationByID(regulationId);
			paragraph=model.getRegulationParagraph();

		}
	}

	/**
	 * 保存序列规则
	 */
	@Override
	public String save() throws Exception {
		String rule="";
		if("".equals(model.getRegulationId())){
			model.setRegulationId(null);
		}
		if(paragraph==TWO||TWO.equals(paragraph)){//判断是否是两段
			model.setRegulationParagraph(TWO);

			rule=rule+ruleYear+","+ruleNumber;
		}else if(paragraph==THREE||THREE.equals(paragraph)){//判断段落是否是三段
			model.setRegulationParagraph(THREE);
			rule=rule+abbreviate+","+ruleYear+","+ruleNumber;
		}else if(paragraph==FOUR||FOUR.equals(paragraph)){//是否四段
			model.setRegulationParagraph(FOUR);
			rule=rule+abbreviate+","+letter+","+ruleYear+","+ruleNumber;
		}
		if(sortId!=null&&!"".equals(sortId)){
			model.setSortId(sortId);
		}
		Date date=new Date();
		model.setRegulationTime(date);
		model.setRegulationRule(rule);
		model.setRegulationUser(getUser());
		model.setRegulationUnits(getUserDepartment());
		model.setRegulationSort(regulationSort);			//设置文号规则类型
		regulationManager.saveRegulation(model);
		return RELOAD;
	}

	/**
	 * 是否应用文件类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public String selectSort() throws Exception {
		prepareModel();
	//	StringBuffer isSort = new StringBuffer();
		StringBuffer str=new StringBuffer();
		if(model.getSortId()!=null&&!"".equals(model.getSortId())){//判断文号类型ID是否为空
			List<String> relist=getSplit(model.getRegulationRule());
			String daizi="";//代字
			ToaSerialnumberSort sort=sortManager.getSortById(model.getSortId());
			if(sort!=null){
				
				List<String> list=getSplit(sort.getSortAbbreviation());
				str.append("&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;<span>代字：</span>&nbsp; &nbsp; &nbsp;<select id='abbreviate' onChange='chooseabb()' name='abbreviate'>");
				if(THREE.equals(model.getRegulationParagraph())){
					daizi=relist.get(0);
				}else if(FOUR.equals(model.getRegulationParagraph())){
					daizi=relist.get(1);
				}
				
				for(String temp:list){
					if(temp.equals(daizi)){
						str.append("<option value='"+temp+"'  selected='selected'>"+temp+"</option>");
					}else{
						str.append("<option value='"+temp+"'>"+temp+"</option>");
					}
				}
				str.append("</select>");
			}
		}
		return renderHtml(str.toString());
	}

	/**
	 * 查询规则里包含的公文代字
	 * @return
	 * @throws Exception
	 */
	public String chooseAbbreviate()throws Exception{
		prepareModel();
		StringBuffer str=new StringBuffer();
		if(model.getSortId()!=null&&!"".equals(model.getSortId())){//判断文号类型ID是否为空
			ToaSerialnumberSort sort=sortManager.getSortById(model.getSortId());
			List<String> list=getSplit(sort.getSortAbbreviation());
			str.append("<hr><br><span>请选择公文代字：</span>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "+
			"&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<select id='abbreviate' onChange='chooseabb()' name='abbreviate'><option>请选择文号代字</option>");
			for(String temp:list){
				str.append("<option value='"+temp+"'>"+temp+"</option>");
			}
			str.append("</select>");
		}
		return renderHtml(str.toString());
	}
	/**
	 * 拆分字符串
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
	/**
	 * 获取用户名称
	 * 
	 * @return
	 */
	public String getUser() {
		return userService.getCurrentUser().getUserName();
	}

	/**
	 * 获取当前用户所在部门ID
	 * @return
	 */
	private String getUserDepartment(){
		String userid=userService.getCurrentUser().getUserId();
		return userService.getUserDepartmentByUserId(userid).getOrgId();

	}
	public ToaSerialnumberRegulation getModel() {

		return model;
	}

	public Page<ToaSerialnumberRegulation> getPage() {
		return page;
	}

	public void setPage(Page<ToaSerialnumberRegulation> page) {
		this.page = page;
	}

	@Autowired
	public void setRegulationManager(RegulationManager regulationManager) {
		this.regulationManager = regulationManager;
	}


	public String getRegulationId() {
		return regulationId;
	}

	public void setRegulationId(String regulationId) {
		this.regulationId = regulationId;
	}

	public List<ToaSerialnumberSort> getSortList() {
		return sortList;
	}

	public void setSortList(List<ToaSerialnumberSort> sortList) {
		this.sortList = sortList;
	}

	public String getAbbreviate() {
		return abbreviate;
	}

	public void setAbbreviate(String abbreviate) {
		this.abbreviate = abbreviate;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public String getRuleYear() {
		return ruleYear;
	}

	public void setRuleYear(String ruleYear) {
		this.ruleYear = ruleYear;
	}

	public String getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(String ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	public String getParagraph() {
		return paragraph;
	}

	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
	}

	@Autowired
	public void setSortManager(SortManager sortManager) {
		this.sortManager = sortManager;
	}

	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	public String getRegulationName() {
		return regulationName;
	}

	public void setRegulationName(String regulationName) {
		this.regulationName = regulationName;
	}

	public String getRegulationUser() {
		return regulationUser;
	}

	public void setRegulationUser(String regulationUser) {
		this.regulationUser = regulationUser;
	}

	public Date getRegulationTime() {
		return regulationTime;
	}

	public void setRegulationTime(Date regulationTime) {
		this.regulationTime = regulationTime;
	}

	public String getRegulationParagraph() {
		return regulationParagraph;
	}

	public void setRegulationParagraph(String regulationParagraph) {
		this.regulationParagraph = regulationParagraph;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String getRegulationSort() {
		return regulationSort;
	}

	public void setRegulationSort(String regulationSort) {
		this.regulationSort = regulationSort;
	}

}
