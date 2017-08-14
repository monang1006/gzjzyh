/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: 
 * modifyer:	luosy
 * Version: V1.0
 * Description：车辆申请信息管理
 */
package com.strongit.oa.car;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaCar;
import com.strongit.oa.bo.ToaCarApplicant;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.ToaWorkForm;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.dict.IDictService;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * @author       吴为胜
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年4月18日10:31:52
 * @version      1.0.0.0
 * @comment      车辆申请Action
 */
/**
 * 
 * 郑志斌  2010、3、23 
 * 添加 private IDictService dictService;  //字典项类
 */

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "carApply.action", type = ServletActionRedirectResult.class) })
public class CarApplyAction extends AbstractBaseWorkflowAction<ToaCarApplicant> {

	private String applicantId; //申请单ID
	private Map applyStstusMap = new HashMap(); //申请状态
	private Map carStstusMap = new HashMap(); //车辆状态
	private Map carTypeMap = new HashMap(); //车辆类型
	private Map applyConfrimMap = new HashMap(); //申请登记状态
	
	private String termStart;   //日程开始时间
	private String termEnd;   //日程结束时间
//	 指定查看日期
	private String setDate;    //指定查看日期
//	视图类型  
	private String inputType;  //视图类型 （日、周、月）
    //	查询用字段
	private String applierId; //申请人ID
	private String applier; //申请人姓名
	private String carId;   //车辆Id
	private String carno;   //车牌号
	private Date applytime;  //申请时间
	private String destination; //目的地

	private String applystatus; //申请状态
	private String caruser; //车辆使用者
	private String needdriver;//是否需要司机 (1：自驾)
	
	private String applystatus2; //申请状态2 用于查看
	private String stime;   //开始时间
	private String etime;   //结束时间
	private String carstatus;  //车辆状态
	private String cartype;  //车辆类型
	
	private List carTypeList;//车辆类型列表
	private List carStatusList;//车辆状态列表
	private List carList;//车辆列表
	
	private Page<ToaCarApplicant> page = new Page<ToaCarApplicant>(FlexTableTag.MAX_ROWS, true);
	private ToaCarApplicant model = new ToaCarApplicant();
	private ToaCar carmodel=new ToaCar(); //车辆模型
	@Autowired CarApplyManager carApplyManager;
	private CarManager carManager;
	
	private IDictService dictService;  //字典项类
	
	/**
	 * 驳回，回退,回退上一步标识
	 */
	private String returnFlag;  //返回标志
	
	public CarApplyAction(){
		applyStstusMap.put(null, "选择状态");
		applyStstusMap.put("0", "待提交");
		applyStstusMap.put("1", "审批中");
		applyStstusMap.put("3", "已批准");
		applyStstusMap.put("4", "已撤销");
		
		applyConfrimMap.put(null, "未登记");
		applyConfrimMap.put("", "未登记");
		applyConfrimMap.put(ToaCarApplicant.CARAPPLY_NOTCONFIRM, "未登记");
		applyConfrimMap.put(ToaCarApplicant.CARAPPLY_ISCONFIRM, "已登记");
		
	}
	
	@Override
	public String delete() throws Exception {
		carApplyManager.deleteCars(applicantId);
		addActionMessage("删除成功");
		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/car/carApply.action';</script>");
		// return RELOAD;
	}

	@Override
	public String input() throws Exception {
		prepareModel();
		ToaCar toaCar=carManager.getCarInfo(carId);
		model.setToaCar(toaCar);
		model.setApplierId(super.adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserId());
		model.setApplier(super.adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserName());
		if (termStart!="" && termStart!=null){
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			model.setStarttime(format.parse(termStart));
			model.setEndtime(format.parse(termEnd));
			}	
		return INPUT;
	}
	
	/**
	 * 
	 * author:
	 * description: 跳转到编辑页面
	 * modifyer:luosy
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		prepareModel();
		return "edit";
	}
	
	/**
	 * 查看审请单
	 * @return
	 * @throws Exception
	 */
	public String view() throws Exception {
		    prepareModel();
			if ('0'==model.getApplystatus().charAt(0)) { 
			    this.setApplystatus2("待提交");  
			}
			if ('1'==model.getApplystatus().charAt(0)) { 
			    this.setApplystatus2("审批中");  
			}
			if ('3'==model.getApplystatus().charAt(0)) { 
			    this.setApplystatus2("已批准");  
			}
			if ('4'==model.getApplystatus().charAt(0)) { 
			    this.setApplystatus2("已撤销");  
			}
		return "view";
	}

	/**
	 * author:luosy
	 * description:当前用户的申请列表(车辆申请)
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	@Override
	public String list() throws Exception {
		model.setApplierId(super.adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserId());
		try {
			if(model.getCaruser()!=null&&!"".equals(model.getCaruser())){
				model.setCaruser(URLDecoder.decode(model.getCaruser(), "utf-8"));
			}
			if(model.getDestination()!=null&&!"".equals(model.getDestination())){
				model.setDestination(URLDecoder.decode(model.getDestination(), "utf-8"));
			}

			if(model.getToaCar()!=null&&model.getToaCar().getCarno()!=null&&!"".equals(model.getToaCar().getCarno())){
				model.getToaCar().setCarno(URLDecoder.decode(model.getToaCar().getCarno(), "utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		page=carApplyManager.queryCarApplicants(page, model,carId);
		return SUCCESS;
	}
   
	/**
	 * author: 
	 * description: 车辆 carno 审批情况列表(车辆申请-->申请-->列表)
	 * modifyer:luosy
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String list1() throws Exception {
		applystatus = model.getApplystatus();
		if (applystatus=="" || applystatus==null){
			model.setApplystatus("3");   //设置默认申请状态为已批准
		}
		try {
			if(model.getApplier()!=null&&!"".equals(model.getApplier())){
				model.setApplier(URLDecoder.decode(model.getApplier(), "utf-8"));
			}
			if(model.getDestination()!=null&&!"".equals(model.getDestination())){
				model.setDestination(URLDecoder.decode(model.getDestination(), "utf-8"));
			}

			if(model.getToaCar()!=null&&model.getToaCar().getCarno()!=null&&!"".equals(model.getToaCar().getCarno())){
				model.getToaCar().setCarno(URLDecoder.decode(model.getToaCar().getCarno(), "utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(carno!=null&&!"".equals(carno)){
			carno = URLDecoder.decode(carno, "utf-8");
		}
		page=carApplyManager.queryCarApplicants(page, model,carId);
		return "list1";
	}
	
	/**
	 * author: 
	 * description: 车辆申请查询列表(车辆申请查询)
	 * modifyer:luosy
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String list2() throws Exception {
		try {
			if(model.getCaruser()!=null&&!"".equals(model.getCaruser())){
				model.setCaruser(URLDecoder.decode(model.getCaruser(), "utf-8"));
			}
			if(model.getDestination()!=null&&!"".equals(model.getDestination())){
				model.setDestination(URLDecoder.decode(model.getDestination(), "utf-8"));
			}

			if(model.getToaCar()!=null&&model.getToaCar().getCarno()!=null&&!"".equals(model.getToaCar().getCarno())){
				model.getToaCar().setCarno(URLDecoder.decode(model.getToaCar().getCarno(), "utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		applystatus  = model.getApplystatus();
		page=carApplyManager.queryCarApplicants(page, model,carId);
		return "list2";
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if (applicantId != null) {
			model = carApplyManager.getCarApplyInfo(applicantId);
		} else {
			model = new ToaCarApplicant();
		}
	}

	@Override
	public String save() throws Exception {
		if ("".equals(model.getApplicantId())) {
			model.setApplicantId(null);
		}
		java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date beginTime=df.parse(stime);
		Date endTime=df.parse(etime);
		model.setStarttime(beginTime);
		model.setEndtime(endTime);
		Date applytime=new Date();
		model.setApplytime(applytime);
		if (model.getNeeddriver()=="" || model.getNeeddriver()==null){model.setNeeddriver("0");}  //需要配司机
		model.setApplystatus("0");

		//分级信息
		TUumsBaseOrg org=adapterBaseWorkflowManager.getUserService().getSupOrgByUserIdByHa(model.getApplierId());
		model.setOrgCode(org.getSupOrgCode());
		model.setOrgId(org.getOrgId());
		
		carApplyManager.saveCarApplyInfo(model);
		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/car/carApply.action';</script>");
	}

	//编辑车辆申请后保存
	public String editsave() throws Exception {
		if ("".equals(model.getApplicantId())) {
			model.setApplicantId(null);
		}
		java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date beginTime=df.parse(stime);
		Date endTime=df.parse(etime);
		model.setStarttime(beginTime);
		model.setEndtime(endTime);
		if (model.getNeeddriver()=="" || model.getNeeddriver()==null){model.setNeeddriver("0");} //需要配司机
		model.setApplystatus("0");
		carApplyManager.saveCarApplyInfo(model);
		return renderHtml("<script> window.dialogArguments.location='"
				+ getRequest().getContextPath()
				+ "/car/carApply.action'; window.close();</script>");
	}
	
     //	审批时，编辑车辆申请后保存
	public String approvalsave() throws Exception {
		if ("".equals(model.getApplicantId())) {
			model.setApplicantId(null);
		}
		java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date beginTime=df.parse(stime);
		Date endTime=df.parse(etime);
		model.setStarttime(beginTime);
		model.setEndtime(endTime);	
		carApplyManager.saveCarApplyInfo(model);
		return renderHtml("<script> window.location='"
				+ getRequest().getContextPath()
				+ "/car/carApply!form.action?taskId="+taskId+
                     				"&instanceId="+instanceId+"'; window.close();</script>");
	}
	/**
	 * 展现当前车辆日程用车情况
	 * @return
	 */
	public String selectCalender(){
		if (carId==null || carId==""){
			if (carManager.getCars().size()!=0){
			carId=carManager.getCars().get(0).getCarId();
			carno=carManager.getCars().get(0).getCarno();
			}
		}else{
		   this.setCarno(carManager.getCarInfo(carId).getCarno());
		}
		
		if("".equals(inputType)||null==inputType){
			inputType="day";
		}
		if("".equals(setDate)||null==setDate){
			Calendar now = Calendar.getInstance();
			setDate=String.valueOf(now.get(Calendar.YEAR))+"-"+String.valueOf(now.get(Calendar.MONTH)+1)+"-"+String.valueOf(now.get(Calendar.DAY_OF_MONTH));
		}
		return "selectcalendar";
	}
	
	/**
	 * 查看该车辆的基本信息和已批准用车信息
	 * @return
	 * @throws Exception
	 */
	public String carchaxun() throws Exception {
		carTypeList = dictService.getItemsByDictValue("CARTYPE");	
		carStatusList = dictService.getItemsByDictValue("CARSTATUS");
		carmodel=carManager.getCarInfo(carId);
		
		ToaSysmanageDictitem toas = null;
		for(Iterator it = carStatusList.iterator(); it.hasNext();) {
			toas = (ToaSysmanageDictitem) it.next();
			if (toas.getDictItemValue().charAt(0)==carmodel.getStatus().charAt(0)) {
			    this.setCarstatus(toas.getDictItemShortdesc());
				break;
			}
		}
		for(Iterator it = carTypeList.iterator(); it.hasNext();) {
			toas = (ToaSysmanageDictitem) it.next();
			if (toas.getDictItemValue().charAt(0)==carmodel.getCartype().charAt(0)) {
				this.setCartype(toas.getDictItemShortdesc());
				break;
			}
		}
	   	if (applystatus=="" || applystatus==null){
			applystatus="3";                    //applystatus="3" 表示申请状态为已批准
		}
	   	page=carApplyManager.queryCarApplicants(page, model,carId);
//	       page=carApplyManager.queryCarApplicants(page, applierId,applier,caruser, carId, carno, destination, starttime,endtime,applystatus);
			return "carchaxun";
		}
	
	/**
	 * author:luosy
	 * description:点击下个月的图标 载入下个月的指定车辆的车辆安排情况
	 * modifyer:wuws
	 * description:
	 * @return
	 * @throws ParseException 
	 */
	public String changeCalendar() throws ParseException{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar startCal = Calendar.getInstance();
		if(!"".equals(termStart)&&null!=termStart){
			startCal.setTime(format.parse(termStart));
		}
		Calendar endCal = Calendar.getInstance();
		if(!"".equals(termEnd)&&null!=termEnd){
			endCal.setTime(format.parse(termEnd));
		}
		List calList = null;
		boolean canEdit = false;
		calList=carApplyManager.queryCarApplicantsList(startCal, endCal,carId);
		JSONArray array = carApplyManager.makeCalListToJSONArray(calList, canEdit);
		return renderText(array.toString());
	}
	
	
	/**
	 * 流程开始向导，获车辆申请流程
	 * @return
	 */
	public String wizard() {
		String[] strBussinessID = bussinessId.split(";");
		//HttpServletRequest request=ServletActionContext.getRequest();
		//HttpSession session=request .getSession();
		//session.setAttribute("bussinessId", bussinessId);//session传申请人姓名
		
		this.setFormId("0");
		//获取申请单
		 model = carApplyManager.getCarApplyInfo(strBussinessID[2]);
		String processTypeId=CarConst.CAR_WORKFLOW_TYPE_ID;   //车辆申请流程类型ID
		//获取工作流
		workflows = carApplyManager.getProcessOwnedByProcessType(processTypeId);
		return "towizard";
	}
	
	/**
	 * 申请提交,启动车辆申请工作流程
	 */
	public String handleWorkflow() {
		this.setFormId("0");
		String desti="";
		String[] strBussinessId = bussinessId.split(";");
		model = carApplyManager.getCarApplyInfo(strBussinessId[2]);
		if (model.getDestination().length()>30) {
			 desti=model.getDestination().substring(0, 30);
		}else{
			 desti=model.getDestination();
		}
//		处理审批意见有回车换行的情况
		if(suggestion!=null){
			suggestion = suggestion.replaceAll("\\r\\n", "");			
		}
		if ("".equals(strTaskActors)) {
			carApplyManager.handleWorkflow(formId, workflowName, 
					bussinessId, 
					"申请人："+model.getApplier()+"；用车人："+model.getCaruser()+"； 目的地： "+desti,
					null,
					transitionName,
					concurrentTrans,
					suggestion);
		} else {
			carApplyManager.handleWorkflow(formId, workflowName, 
					bussinessId, 
					"申请人："+model.getApplier()+"；用车人："+model.getCaruser()+"； 目的地： "+desti, 
					strTaskActors.split(","),
					transitionName,
					concurrentTrans,
					suggestion);
		}	
		model.setApplystatus("1");
		carApplyManager.saveCarApplyInfo(model);
		return renderHtml("<script>window.returnValue ='OK'; window.close();</script>");
	}
	
	/**
	 * 显示车辆申请单页面
	 * @return String
	 */
	public String form() throws Exception{
		carList=carManager.getCars();
		this.setFormId("0");
		userName = super.adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserName();
		if (bussinessId != null && !"".equals(bussinessId)) { 
			//草拟车辆申请提交时获取申请对象
			String[] strBussinessId = bussinessId.split(";");
			applicantId = strBussinessId[2];
			model = carApplyManager.getCarApplyInfo(applicantId);
		} else {
			//通过任务ID获取表单ID和业务ID,业务ID用于获取申请单对象
			String[] info = carApplyManager.getFormIdAndBussinessIdByTaskId(taskId);
			bussinessId = info[0];
			String[] strBussinessId = bussinessId.split(";");
			applicantId = strBussinessId[2];
			model = carApplyManager.getCarApplyInfo(applicantId);
			
			formId = info[1];
			carApplyManager.signForTask(taskId, "1");
		}
		
		returnFlag = carApplyManager.checkCanReturn(taskId);
		return "form";
	}
	
	/**
	 * 回退到指定步
	 * @return
	 */
	public String  backToPreDefNode() throws Exception{
		String isNewForm = "";
		String returnNodeId = getRequest().getParameter("returnNode");
	
		if ("".equals(applicantId)) {
			isNewForm = "1";
		} else {
			isNewForm = "0";
		}		

//		处理审批意见有回车换行的情况
		if(suggestion!=null){
			suggestion = suggestion.replaceAll("\\r\\n", "");			
		}
		
		String curUserId = adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserId();
		carApplyManager.handleWorkflowNextStep(taskId, 
                    WorkflowConst.WORKFLOW_TRANSITION_HUITUI, 
                    returnNodeId, 
                    isNewForm, 
                    formId, 
                    bussinessId, 
                    suggestion, 
                    curUserId,
                    null);
		
		return super.todo(); //返回到待办列表页面
	}
	
	
	/**
	 * author:luosy
	 * description:  车辆使用登记列表（取出所有已经审批完成的申请，在列表上可以登记使用费用等情况）
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String confirmList() throws Exception{
		try {
			if(model.getCaruser()!=null&&!"".equals(model.getCaruser())){
				model.setCaruser(URLDecoder.decode(model.getCaruser(), "utf-8"));
			}
			if(model.getDestination()!=null&&!"".equals(model.getDestination())){
				model.setDestination(URLDecoder.decode(model.getDestination(), "utf-8"));
			}
			if(model.getToaCar()!=null&&model.getToaCar().getCarno()!=null&&!"".equals(model.getToaCar().getCarno())){
				model.getToaCar().setCarno(URLDecoder.decode(model.getToaCar().getCarno(), "utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		model.setApplystatus("3");
		model.setApplierId("");
		page=carApplyManager.queryCarApplicants(page, model,carId);
		
		return "confirmList";
	}
	
	/**
	 * author:luosy
	 * description:  登记车辆使用情况
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String confirm() throws Exception{
		this.prepareModel();
		if ('0'==model.getApplystatus().charAt(0)) {
		    this.setApplystatus2("待提交");  
		}
		if ('1'==model.getApplystatus().charAt(0)) {
		    this.setApplystatus2("审批中");  
		}
		if ('3'==model.getApplystatus().charAt(0)) {
		    this.setApplystatus2("已批准");  
		}
		if ('4'==model.getApplystatus().charAt(0)) {
		    this.setApplystatus2("已撤销");  
		}
		
		//初始化 驾驶员
		if(model.getDriver()==null||"".equals(model.getDriver())){
			model.setDriver(model.getApplier());
		}
		//初始化 用车处室/单位
		if(model.getUserDepart()==null||"".equals(model.getUserDepart())){
			Organization Applier = adapterBaseWorkflowManager.getUserService().getUserDepartmentByUserId(model.getApplierId());
			if(null!=Applier){
				model.setUserDepart(Applier.getOrgName());
			}
		}
		//初始化 发车地点
		if(model.getLocalPosition()==null||"".equals(model.getLocalPosition())){
			model.setLocalPosition("");
		}
		//初始化 里程数
		if(model.getDistance()==null||"".equals(model.getDistance())){
			model.setDistance("0");
		}
		//初始化 汽油费
		if(model.getGasCost()==null||"".equals(model.getGasCost())){
			model.setGasCost("0");
		}
		//初始化 路桥费
		if(model.getBridgeCost()==null||"".equals(model.getBridgeCost())){
			model.setBridgeCost("0");
		}
		//初始化 停车费
		if(model.getPullCost()==null||"".equals(model.getPullCost())){
			model.setPullCost("0");
		}
		//初始化 洗车费
		if(model.getCleanCost()==null||"".equals(model.getCleanCost())){
			model.setCleanCost("0");
		}
		//初始化 其他费用
		if(model.getOtherCost()==null||"".equals(model.getOtherCost())){
			model.setOtherCost("0");
		}
		//初始化 费用说明
		if(model.getCostNotes()==null||"".equals(model.getCostNotes())){
			model.setCostNotes("");
		}
		return "confirm";
	}
	
	/**
	 * author:luosy
	 * description:  保存登记车辆使用情况
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String confirmSave() throws Exception{
		try{
			model.setIsConfirm(ToaCarApplicant.CARAPPLY_ISCONFIRM);
			carApplyManager.saveCarApplyInfo(model);
			addActionMessage("success");
			return "confirm";
		}catch(Exception e){
			addActionMessage("error");
			return "confirm";
		}
	}
	
	public ToaCarApplicant getModel() {
		return model;
	}

	public void setModel(ToaCarApplicant model) {
		this.model = model;
	}

	public Page<ToaCarApplicant> getPage() {
		return page;
	}

	public void setPage(Page<ToaCarApplicant> page) {
		this.page = page;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}

	public String getApplierId() {
		return applierId;
	}

	public void setApplierId(String applierId) {
		this.applierId = applierId;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getApplystatus() {
		return applystatus;
	}

	public void setApplystatus(String applystatus) {
		this.applystatus = applystatus;
	}

	public Map getApplyStstusMap() {
		return applyStstusMap;
	}

	public void setApplyStstusMap(Map applyStstusMap) {
		this.applyStstusMap = applyStstusMap;
	}

	public Date getApplytime() {
		return applytime;
	}

	public void setApplytime(Date applytime) {
		this.applytime = applytime;
	}

	public String getCarno() {
		return carno;
	}

	public void setCarno(String carno) {
		this.carno = carno;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public CarManager getCarManager() {
		return carManager;
	}
	@Autowired
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public ToaCar getCarmodel() {
		return carmodel;
	}

	public void setCarmodel(ToaCar carmodel) {
		this.carmodel = carmodel;
	}

	public List getCarStatusList() {
		return carStatusList;
	}

	public List getCarTypeList() {
		return carTypeList;
	}

	public Map getCarStstusMap() {
		return carStstusMap;
	}

	public void setCarStstusMap(Map carStstusMap) {
		this.carStstusMap = carStstusMap;
	}

	public Map getCarTypeMap() {
		return carTypeMap;
	}

	public void setCarTypeMap(Map carTypeMap) {
		this.carTypeMap = carTypeMap;
	}

	public String getTermEnd() {
		return termEnd;
	}

	public void setTermEnd(String termEnd) {
		this.termEnd = termEnd;
	}

	public String getTermStart() {
		return termStart;
	}

	public void setTermStart(String termStart) {
		this.termStart = termStart;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getSetDate() {
		return setDate;
	}

	public void setSetDate(String setDate) {
		this.setDate = setDate;
	}

	public String getCaruser() {
		return caruser;
	}

	public void setCaruser(String caruser) {
		this.caruser = caruser;
	}

	public String getNeeddriver() {
		return needdriver;
	}

	public void setNeeddriver(String needdriver) {
		this.needdriver = needdriver;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getApplystatus2() {
		return applystatus2;
	}

	public void setApplystatus2(String applystatus2) {
		this.applystatus2 = applystatus2;
	}

	public List getCarList() {
		return carList;
	}

	public void setCarList(List carList) {
		this.carList = carList;
	}

	public String getCarstatus() {
		return carstatus;
	}

	public void setCarstatus(String carstatus) {
		this.carstatus = carstatus;
	}

	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	@Override
	protected BaseManager getManager() {
		return this.carApplyManager;
	}

	@Override
	protected String getModuleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWorkflowType() {
		return CarConst.CAR_WORKFLOW_TYPE_ID;
	}

	@Override
	protected String getDictType() {
		return WorkFlowTypeConst.CARIDEA;
	}
	
	//郑志斌添加 
	@Autowired
	public void setDictService(IDictService dictService) {
		this.dictService = dictService;
	}

	public Map getApplyConfrimMap() {
		return applyConfrimMap;
	}

	public void setApplyConfrimMap(Map applyConfrimMap) {
		this.applyConfrimMap = applyConfrimMap;
	}


}
