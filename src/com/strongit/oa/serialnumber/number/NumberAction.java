/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-7-10
 * Autour: hull
 * Version: V1.0
 * Description： 文号action
 */
package com.strongit.oa.serialnumber.number;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSerialnumberNumber;
import com.strongit.oa.bo.ToaSerialnumberRegulation;
import com.strongit.oa.bo.ToaSerialnumberSort;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.serialnumber.regulation.RegulationManager;
import com.strongit.oa.serialnumber.sort.SortManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 文号管理action
 * @author 胡丽丽
 * @date:2009-12-05
 *
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "number.action", type = ServletActionRedirectResult.class) })
public class NumberAction extends BaseActionSupport {

	Page<ToaSerialnumberNumber> page = new Page<ToaSerialnumberNumber>(FlexTableTag.MAX_ROWS, true);

	/** 文号manager*/
	private NumberManager numberManager;

	private ToaSerialnumberNumber model = new ToaSerialnumberNumber();

	/** 文号规则manager*/
	private RegulationManager regulationManager;

	/** 文号规则列表*/
	private List<ToaSerialnumberRegulation> regulationList;

	/** 文号ID*/
	private String numberId;

	/** 文件类型Manager*/
	private SortManager sortManager;

	/** 文件类型列表*/
	private List<ToaSerialnumberSort> sortList;

	/** 使用的规则*/
	private String chooseRegulation;

	/** 文号*/
	private String numberNo;

	/** 文号代字*/
	private String abbreviation;

	/** 规则ID*/
	private String regulationId;

	/** 自定义规则*/
	private String rule;

	/** 自定义段落*/
	private String paragraph;

	/** 用户IUserService*/
	IUserService userService;
	
	/** 分隔符*/
	private String division;
	
	/** 文号生成时间*/
	private Date numberTime;
	
	private String doinput;
	
	private String disLogo;
	
	private String dictcode;
	
	private static final String ONE="1";
	private static final String TWO="2";
	private static final String THREE="3";
	private static final String FOUR="4";
	private List<ToaSerialnumberNumber> makeList;//预留文号列表
	private List<ToaSerialnumberNumber> recycleList;//回收文号列表
	
	private String regulationSort;			//文号规则类型：

	public List<ToaSerialnumberNumber> getMakeList() {
		return makeList;
	}

	public void setMakeList(List<ToaSerialnumberNumber> makeList) {
		this.makeList = makeList;
	}

	public List<ToaSerialnumberNumber> getRecycleList() {
		return recycleList;
	}

	public void setRecycleList(List<ToaSerialnumberNumber> recycleList) {
		this.recycleList = recycleList;
	}

	
	public String getDisLogo() {
		return disLogo;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}

	/**
	 * 删除文号
	 */
	@Override
	public String delete() throws Exception {
		if (numberId != null && !"".equals(numberId)) {
			numberManager.deleteNumber(numberId);
		}
		return RELOAD;
	}

	/**
	 * 添加文号初始化
	 */
	@Override
	public String input() throws Exception {
		regulationList = regulationManager
		.getRegulationByUnits(getUserDepartment());
		sortList = sortManager.getAllSort();
		return INPUT;
	}
	/**
	 * 添加文号初始化
	 */
	public String show() throws Exception {
		makeList=numberManager.getMakeNumber(getUserDepartment(),getYearTime());
		recycleList=numberManager.getRecycleNumber(getUserDepartment(),getYearTime());
//		regulationList = regulationManager.getRegulationByUnits(getUserDepartment());
		if(regulationSort!=null&&!regulationSort.equals("")){			
			regulationList=regulationManager.getRegulationListByRegulationsort(regulationSort);
		}else{
			
			regulationList = regulationManager.getAllRegulation();
		}
			
		sortList = sortManager.getAllSort();
		return "show";
	}

	/**
	 * 获取文号列表
	 */
	@Override
	public String list() throws Exception {
		if(disLogo!=null&&!"".equals(disLogo)){//判断是否多条搜索
			page=numberManager.getNumberByProperty(page, model);
		}else{
			page = numberManager.getAllNumber(page);
		}
		if(page.getResult()==null){
			page.setTotalCount(0);
		}
		return SUCCESS;
	}

	/**
	 * BO初始化
	 */
	@Override
	protected void prepareModel() throws Exception {
		if (numberId == null || "".equals(numberId)) {//判断numberId是否为空
			model = new ToaSerialnumberNumber();
		} else {
			model = numberManager.getNumberById(numberId);
		}

	}

	/**
	 * 获取当前用户所在部门ID
	 * 
	 * @return
	 */
	private String getUserDepartment() {
		String userid = userService.getCurrentUser().getUserId();
		return userService.getUserDepartmentByUserId(userid).getOrgId();

	}

	/**
	 * 保存生成序列号
	 */
	@Override
	public String save() throws Exception {
		User user=userService.getCurrentUser();
		//创建文号生成类
		GenerateNum generate = new GenerateNum();
		
		String temp = "";//
		String year = "";
		if (chooseRegulation == ONE || ONE.equals(chooseRegulation)) {// 判断是否使用系统规则
			int num = numberManager.getCount(user.getOrgId(),abbreviation,dictcode,year);// 获取序列号个数
			while (true) {// 生成唯一的序列号
				num = num + 1;
				temp = generate.getNum(num,"");// 获取数字
				year = "〔" + getYearTime() + "〕";
				if (division != null && !"".equals(division)) {//判断分割符是否为空
					numberNo = year + division + temp;
				} else {
					numberNo = year + temp;
				}
				int sum = numberManager.getCountByNo(numberNo,user.getOrgId());
				if (sum == 0) {
					break;
				}
			}

		} else if (chooseRegulation == TWO || TWO.equals(chooseRegulation)) {// 使用单位公用规则
			//获取规则
			ToaSerialnumberRegulation reg = regulationManager
					.getRegulationByID(regulationId);
			if (reg.getRegulationParagraph() == TWO
					|| TWO.equals(reg.getRegulationParagraph())) {//是否生成2段
				numberNo = generate.generateTwo(reg, numberManager, division,user.getOrgId(),dictcode);
			} else if (reg.getRegulationParagraph() == THREE
					|| THREE.equals(reg.getRegulationParagraph())) {//是否是3段
				numberNo = generate.generateThree(reg, numberManager,
						abbreviation, division,user.getOrgId(),dictcode);
			} else if (reg.getRegulationParagraph() == FOUR
					|| FOUR.equals(reg.getRegulationParagraph())) {//是否4段
				numberNo = generate.generateFour(reg, numberManager,
						abbreviation, division,user.getOrgId(),dictcode);
			}
		} else if (chooseRegulation ==THREE || THREE.equals(chooseRegulation)) {// 自定义规则
			ToaSerialnumberRegulation reg = new ToaSerialnumberRegulation();//实例化一个规则对象
			reg.setRegulationRule(rule);
			reg.setRegulationParagraph(paragraph);
			if (reg.getRegulationParagraph() == TWO
					|| TWO.equals(reg.getRegulationParagraph())) {//生成2段
				numberNo = generate.generateTwo(reg, numberManager, division,user.getOrgId(),dictcode);
			} else if (reg.getRegulationParagraph() == THREE
					|| THREE.equals(reg.getRegulationParagraph())) {//生成3段
				numberNo = generate.generateThree(reg, numberManager,
						abbreviation, division,user.getOrgId(),dictcode);
			} else if (reg.getRegulationParagraph() == FOUR
					|| FOUR.equals(reg.getRegulationParagraph())) {//生成4段
				numberNo = generate.generateFour(reg, numberManager,
						abbreviation, division,user.getOrgId(),dictcode);
			}
		}
		if (numberNo == null || "".equals(numberNo)) {// 判断文号是否生成
			return renderHtml("<script>alert('文号生成失败！');</script>");
		} else {
			model.setNumberNo(numberNo);
			model.setNumberTime(new Date());
			
			model.setUserId(user.getUserId());
			model.setUserName(user.getUserName());
			model.setOrgId(user.getOrgId());
			model.setOrgName(userService.getDepartmentByOrgId(user.getOrgId()).getOrgName());
			//numberManager.saveNumber(model);
			return renderHtml(numberNo);
		}
	}


	/**
	 * 获取当年年份
	 * 
	 * @return
	 */
	private String getYearTime() {
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy");

		return format.format(time);
	}

	/**
	 * 拆分字符串
	 * 
	 * @param temp
	 * @return
	 */
	private List<String> getSplit(String temp) {
		List<String> list = new ArrayList<String>();
		String[] str = temp.split(",");
		for (int i = 0; i < str.length; i++) {
			if (str[i] != null && !"".equals(str[i])) {
				list.add(str[i]);
			}
		}
		return list;
	}
	/**
	 * 判断文号是否已经存在
	 */
	public String getIsNumber() {
		User user=userService.getCurrentUser();
		int count=numberManager.getIsNumberNo(numberNo, user.getOrgId());
		if(count>0){
			return renderHtml( "1");
		}else{
			if(!isLegal(numberNo)){//验证合法性
				return renderHtml( "2");
			}else{
				return renderHtml( "0");
			}
		}
	}
	/**
	 * 验证合法性
	 * @param number
	 * @return
	 */
	private boolean isLegal(String number){
		int begin=number.indexOf("[");
		int end=number.indexOf("]");
		if(begin>=0&&end>0&&end>begin){
			if(end-begin==5&&end<number.length()-1){
				String year=getYearByGuize(number);
				if(isNumeric(year)){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		return false;
	}
	/**
	 * 验证是否是数字
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str) 
	{ 
		Pattern pattern = Pattern.compile("[0-9]*"); 
		Matcher isNum = pattern.matcher(str); 
		if( !isNum.matches() ) 
		{ 
		return false; 
		} 
		return true; 
	}
	/**
	 * 保存已经生成的文号
	 * @author 胡丽丽
	 * @date 2010-01-13
	 * @return
	 */
	public String saveNumber(){
		User user=userService.getCurrentUser();
		Organization org=userService.getDepartmentByOrgId(user.getOrgId());
		if(!"undefined".equals(model.getNumberNo())&&!"".equals(model.getNumberNo())
				&&numberManager.getCountByNo(model.getNumberNo(), org.getOrgId())<=0){
			model.setNumberTime(new Date());
			model.setUserId(user.getUserId());
			model.setUserName(user.getUserName());
			model.setOrgId(org.getOrgId());
			model.setOrgName(org.getOrgName());
			model.setNumberAbbreviation(abbreviation);
			model.setNumberYear(getYearByGuize(model.getNumberNo()));
			model.setNumberState("0");
			numberManager.saveNumber(model);
		}else{
			numberManager.updateNumberState(model.getNumberNo(), "0", org.getOrgId(),org.getOrgName());
		}
		return renderHtml("");
	}
	/**
	 * 预留文号
	 * @author 胡丽丽
	 * @date 2010-01-14
	 * @return
	 */
	public String makeNumber(){
		User user=userService.getCurrentUser();
		Organization org=userService.getDepartmentByOrgId(user.getOrgId());
		if(numberManager.getCountByNo(model.getNumberNo(), org.getOrgId())<=0){
			model.setNumberTime(new Date());
			model.setUserId(user.getUserId());
			model.setUserName(user.getUserName());
			model.setOrgId(user.getOrgId());
			model.setOrgName(org.getOrgName());
			model.setNumberAbbreviation(abbreviation);
			try {
				model.setNumberYear(getYearByGuize(model.getNumberNo()));
			} catch (Exception e) {
				model.setNumberYear(null);
			}
			model.setNumberState("1");
			numberManager.saveNumber(model);
			return renderHtml("预留成功！");			
		}else{
			return renderHtml("当前所要预留的文号，已确定使用，不能预留！！！");	
		}		
	}
	
	/**
	 * 回收文号，只能回收本机构下的已经使用文号
	 * @return
	 */
	public String recycleNumber(){
		User user=userService.getCurrentUser();
		Organization org=userService.getDepartmentByOrgId(user.getOrgId());
		//if(numberManager.getAloneOrgCountByNo(model.getNumberNo(), user.getOrgId())<=0){ getCountByNo
		if(numberManager.getCountByNo(model.getNumberNo(), user.getOrgId())<=0){
			return renderHtml("0");//文号不存在
		}else{
			if(numberManager.getIsNumberNo(model.getNumberNo(),user.getOrgId())>0){
				int count=numberManager.updateNumberState(model.getNumberNo(), "2",user.getOrgId(),org.getOrgName());
				return renderHtml(count+"");
			}else{
				return renderHtml("false");
			}
		}
	}
	/**
	 * 获取文号年份
	 * @author 胡丽丽
	 * @date 2010-01-13
	 * @param num
	 * @return
	 */
	private String getYearByGuize(String num){
		String year=num.substring(num.indexOf("〔")+1,num.indexOf("〕"));
		return year;
	}
	public ToaSerialnumberNumber getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public Page<ToaSerialnumberNumber> getPage() {
		return page;
	}

	public void setPage(Page<ToaSerialnumberNumber> page) {
		this.page = page;
	}

	public List<ToaSerialnumberRegulation> getRegulationList() {
		return regulationList;
	}

	public void setRegulationList(List<ToaSerialnumberRegulation> regulationList) {
		this.regulationList = regulationList;
	}

	public String getNumberId() {
		return numberId;
	}

	public void setNumberId(String numberId) {
		this.numberId = numberId;
	}

	@Autowired
	public void setNumberManager(NumberManager numberManager) {
		this.numberManager = numberManager;
	}

	@Autowired
	public void setRegulationManager(RegulationManager regulationManager) {
		this.regulationManager = regulationManager;
	}

	public List<ToaSerialnumberSort> getSortList() {
		return sortList;
	}

	public void setSortList(List<ToaSerialnumberSort> sortList) {
		this.sortList = sortList;
	}

	@Autowired
	public void setSortManager(SortManager sortManager) {
		this.sortManager = sortManager;
	}
	
	public Date getNumberTime() {
		return numberTime;
	}

	public void setNumberTime(Date numberTime) {
		this.numberTime = numberTime;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getParagraph() {
		return paragraph;
	}

	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
	}

	public String getRegulationId() {
		return regulationId;
	}

	public void setRegulationId(String regulationId) {
		this.regulationId = regulationId;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getNumberNo() {
		return numberNo;
	}

	public void setNumberNo(String numberNo) {
		this.numberNo = numberNo;
	}

	public String getChooseRegulation() {
		return chooseRegulation;
	}

	public void setChooseRegulation(String chooseRegulation) {
		this.chooseRegulation = chooseRegulation;
	}

	public String getDoinput() {
		return doinput;
	}

	public void setDoinput(String doinput) {
		this.doinput = doinput;
	}

	public String getDictcode() {
		return dictcode;
	}

	public void setDictcode(String dictcode) {
		this.dictcode = dictcode;
	}

	public String getRegulationSort() {
		return regulationSort;
	}

	public void setRegulationSort(String regulationSort) {
		this.regulationSort = regulationSort;
	}


}
