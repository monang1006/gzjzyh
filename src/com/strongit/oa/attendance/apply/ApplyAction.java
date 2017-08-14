package com.strongit.oa.attendance.apply;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.attendance.applytype.ApplyTypeManager;
import com.strongit.oa.bo.ToaAttenApply;
import com.strongit.oa.bo.ToaAttendCancle;
import com.strongit.oa.bo.ToaAttendType;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.VoFormDataBean;
import com.strongit.oa.common.service.parameter.OtherParameter;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.oa.work.WorkManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
/**
 * 申请单ACTION
 * @date 2010-03-15
 * @author hull
 *
 */
public class ApplyAction extends AbstractBaseWorkflowAction<ToaAttenApply> {
	private String applyId;								//申请表单ID
	private String cancleid;							//销假表单ID
	private String ids;
	private Page<ToaAttenApply> page = new Page<ToaAttenApply>(FlexTableTag.MAX_ROWS, true);			//申请单PAGE对象
	private Page<EForm> EFpage = new Page<EForm>(FlexTableTag.MAX_ROWS, false);//分页对象,每页20条,支持自动获取总记录数
	private Page<ToaAttendCancle> canclepage = new Page<ToaAttendCancle>(FlexTableTag.MAX_ROWS, true);	//销假单PAGE对象
	private ToaAttenApply model = new ToaAttenApply();	//申请单BO
	private ToaAttendCancle canc=new ToaAttendCancle();	//销假单BO
	private ApplyManager applyManager;
	private IUserService usmanager;						//用户接口  
	private PersonManager personManager;				//人事人员接口
	private WorkManager workmanager;
	private ApplyTypeManager typeManager;				//申请类型服务类
	private List<ToaAttendType> typeList;				//申请类型列表
	private List<EForm> formList;
	private List ideaLst;
	private String applyer;
	private String applyType;							//申请类型
	private Date cancleStime;							//开始时间
	private Date cancleEtime;							//结束时间
	private String status;								//申请单状态
	private String formName;//表单模板列表查询
	private String workId;								//生成的表单ID
    private Date applyDate;								//申请时间
    private String userId;
    private String apptype;								//要返回的页面
	private String isbutian;
	private static final String APPLYFORM ="14";		//申请单流程类型ID
	private static final String CANCLEFORM="15";		//销假单流程类型ID
	private static final String YES="0";		    	//是
	private static final String NO="1";		    		//否
	private static final String TWO="2";		    	//销假单流程类型ID
	private static final String THREE="3";		    	//销假单流程类型ID
	
	@Autowired
	private IEFormService eformService;
	
    public String getApptype() {
		return apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	public String getUserId() {
	    return userId;
    }

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWorkId() {
		return workId;
	}

	public void setWorkId(String workId) {
		this.workId = workId;
	}

	public List<EForm> getFormList() {
		return formList;
	}

	public void setFormList(List<EForm> formList) {
		this.formList = formList;
	}

	/**
	 * 删除申请单
	 */
	@Override
	public String delete() throws Exception {
		try {
			applyManager.deleteApplys(ids);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return list();
	}
	/**
	 * 删除销假单
	 */
	public String deletecancle() throws Exception{
		try {
			applyManager.deleteAttendCancle(cancleid);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return canclelist();
	}

	/**
	 * 转到新建工作页面
	 */
	@Override
	public String input() throws Exception {
		if(bussinessId == null || "".equals(bussinessId)){//填写申请单
			String userid=usmanager.getCurrentUser().getUserId();
			ToaBasePerson person=personManager.getPersonByUumsPerId(userid);
			userId=person.getPersonid();
			userName=person.getPersonName();
		}else{											//修改申请单
			tableName = "T_OA_ATTEN_APPLY";
			pkFieldName = "APPLY_ID";
			pkFieldValue = bussinessId;
		}
		applyId=bussinessId;
		if (applyId != null) {
			model =applyManager.getAttenApply(applyId) ;
			String applyTypeId=model.getApplyTypeId();
			ToaAttendType type=typeManager.getAttendTypeByID(applyTypeId);
			if(type!=null){
				isbutian=type.getCanRewriter();
			}
		} else {
			model = new ToaAttenApply(); 
		}
		typeList=typeManager.getAllAttendType();
		return INPUT;
		
	}
	/**
	 * 人员是否在考勤管理系统里
	 * @return
	 */
	public String isPerson(){
		String userid=usmanager.getCurrentUser().getUserId();
		ToaBasePerson person=personManager.getPersonByUumsPerId(userid);
		if(person==null||person.getPersonid()==null){
			return renderHtml(YES);
		}else{
			if(bussinessId!=null&&!"".equals(bussinessId)){
				model =applyManager.getAttenApply(bussinessId) ;
				ToaAttendType type=typeManager.getAttendTypeByID(model.getApplyTypeId());
				if(type==null||type.getTypeId()==null){
					return renderHtml(TWO);
				}else if(NO.equals(type.getIsEnable())){
					return renderHtml("3");
				}else{
					return renderHtml("1");
				}
			}else{
				return renderHtml("1");
			}
		}
	}
	/**
	 * 填写销假申请单
	 * @author hull
	 * @return
	 * @throws Exception
	 */
	public String inputcanc() throws Exception{
		this.prepareCanc();
		if(bussinessId == null || "".equals(bussinessId)){//填写销假单
			String userid=usmanager.getCurrentUser().getUserId();
			ToaBasePerson person=personManager.getPersonByUumsPerId(userid);
			userId=person.getPersonid();
			userName=person.getPersonName();
		}else{											//修改销假单
			tableName = "T_OA_ATTEND_CANCLE";
			pkFieldName = "CANCLE_ID";
			pkFieldValue = bussinessId;
		}
		return "inputcanc";
	}

	/**
	 * 申请单列表
	 */
	@Override
	public String list() throws Exception {
		String userid=usmanager.getCurrentUser().getUserId();
		ToaBasePerson person=personManager.getPersonByUumsPerId(userid);
		if(person==null){
			page=applyManager.queryAttendApply(page, applyer, applyDate, applyType, startDate, endDate, status, "   ");
		}else{
			page=applyManager.queryAttendApply(page, applyer, applyDate, applyType, startDate, endDate, status, person.getPersonid());
		}
		typeList=typeManager.getAllAttendType();
		return SUCCESS;
	}
	/**
	 * 销假单列表
	 */
	public String canclelist() throws Exception {
		String userid=usmanager.getCurrentUser().getUserId();
		ToaBasePerson person=personManager.getPersonByUumsPerId(userid);
		if(person==null){
			canclepage=applyManager.queryAttendcancelApply(canclepage, applyType, startDate, endDate, cancleStime, cancleEtime, status,ids,"   ");
		}else{
			canclepage=applyManager.queryAttendcancelApply(canclepage, applyType, startDate, endDate, cancleStime, cancleEtime, status,ids,person.getPersonid());
		}
		typeList=typeManager.getAllAttendType();
		return "canclelist";
	}
	
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		formList = workmanager.getRelativeFormByProcessType(APPLYFORM);//获取考勤申请表单
		if(formList!=null && formList.size()!=0){//判断表单数量是否为空
		       EForm ef=formList.get(0);
		       //获取电子表单ID
		       formId=ef.getId().toString();
		}
	}
	/**
	 * 得到销假单对象
	 *@author 蒋国斌
	 *@date 2009-11-24 上午10:01:47 
	 * @throws Exception
	 */
	protected void prepareCanc() throws Exception {
		formList = workmanager.getRelativeFormByProcessType(CANCLEFORM);//获取销假申请表单
		if(formList!=null && formList.size()!=0){
			EForm ef=formList.get(0);
			formId=ef.getId().toString();
		}
		if (ids != null) {
			model =applyManager.getAttenApply(ids) ;
		}
		if (cancleid != null) {
			canc =applyManager.getToaAttendCancle(cancleid);
			model=canc.getToaAttenApply();
		} else {
			canc=new ToaAttendCancle(); 
		}

	}
	/**
	 * 判断当前申请单是否已经销假
	 *@author 蒋国斌
	 *@date 2009-11-25 上午10:08:30 
	 * @return
	 * @throws Exception
	 */
	public String checkisCancle() throws Exception{
		String userid=usmanager.getCurrentUser().getUserId();
		ToaBasePerson person=personManager.getPersonByUumsPerId(userid);
		if(person==null||person.getPersonid()==null){
			renderText(YES);
		}else if(applyManager.getAttendCanclebyApp(ids)==null){
			renderText("success");
		}
		else
			renderText("error");
		return null;
	}

	/**
	 * 保存表单数据
	 *@author 蒋国斌
	 *@date 2009-11-20 上午09:20:39 
	 * @return
	 */
	public InputStream saveFormData() {
		String ret = "0";
		InputStream responseData = null;
		try{
			VoFormDataBean bean = eformService.saveFormData(formData);
			ret = bean.getBusinessId();
			bean.deleteFile();
			if(ret!=null && !ret.equals("")){
				model=applyManager.getAttenApply(ret.split(";")[2]);
				model.setApplyState("0");
				model.setApplyTypeName(typeManager.getAttendTypeByID(model.getApplyTypeId()).getTypeName());
				applyManager.updateAttenApply(model);
			}
			responseData = new ByteArrayInputStream(ret.toString().getBytes("utf-8"));
			return responseData;
		}catch(DAOException ex){
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		}catch(SystemException ex) {
			throw ex;
		}catch(Exception e) {
			throw new SystemException(e);
		}
	}
	/**
	 * 保存申请单表单数据
	 *@author 蒋国斌
	 *@date 2009-11-20 上午09:31:48 
	 * @return
	 * @throws Exception
	 */
	public String eFormDateSave()throws Exception{
		String ret = "0";
		try{
			if(formData != null){
				VoFormDataBean bean = eformService.saveFormData(formData);
				workId = bean.getBusinessId();
				bean.deleteFile();
				if(workId!=null && !workId.equals("")){
					model=applyManager.getAttenApply(workId.split(";")[2]);
					model.setApplyState("0");
					model.setApplyTypeName(typeManager.getAttendTypeByID(model.getApplyTypeId()).getTypeName());
					applyManager.updateAttenApply(model);
				}
				renderText(workId);
			}else{
				ret = "-1";
			}
		}catch(Exception ex){
			logger.error("保存电子表单出现异常，异常信息：" + ex.getMessage());
			ret = "-2";
		}
		return this.renderText(ret);
	}
	/**
	 * 保存销毁单表单数据
	 * @author 胡丽丽
	 * @date 2010-03-13
	 * @return
	 * @throws Exception
	 */
	public String canFormDateSave()throws Exception{
		String ret = YES;
		try{
			if(formData != null){
				VoFormDataBean bean = eformService.saveFormData(formData);
				workId = bean.getBusinessId();
				bean.deleteFile();
				if(workId!=null && !workId.equals("")){
					canc=applyManager.getToaAttendCancle((workId.split(";")[2]));
					canc.setCancleState("0");
					applyManager.updateAttendCancle(canc);
					}
				renderText(workId);
			}else{
				ret = "-1";
			}
		}catch(Exception ex){
			logger.error("保存电子表单出现异常，异常信息：" + ex.getMessage());
			ret = "-2";
		}
		return this.renderText(ret);
	}

	/**
	 * 申请单提交工作流
	 */
	@SuppressWarnings("unchecked")
	public String handleWorkflow() {
		//将提醒(方式|内容)存储在session中
		ActionContext cxt = ActionContext.getContext();
		cxt.getSession().put("remindType", remindType);
		cxt.getSession().put("handlerMes", handlerMes);
		cxt.getSession().put("moduleType", getModuleType());//调用消息模块的类型
		cxt.getSession().put("formData", fullFormData);//存储电子表单数据，用于归档至档案中心.
		if(suggestion!= null){
			suggestion = suggestion.replaceAll("\\r\\n", "");////处理审批意见有回车换行的情况		
		}
		 OtherParameter otherparameter = initOtherParameter();
		if ("".equals(strTaskActors)) {
			getManager().handleWorkflow(formId, workflowName,bussinessId,businessName,null,transitionName,concurrentTrans,suggestion,formData,otherparameter);
		} else {
			getManager().handleWorkflow(formId, workflowName,bussinessId,businessName,strTaskActors.split(","),transitionName,concurrentTrans,suggestion,formData,otherparameter);
		}
//		在这里保存审批意见
		getManager().saveIdea(dictId, suggestion);
		//获取申请单
		if(bussinessId!=null && !bussinessId.equals("")){
			//判断是否是申请单
			if("APPLY_ID".equals(bussinessId.split(";")[1])){
				model=applyManager.getAttenApply(bussinessId.split(";")[2]);
				model.setApplyState(NO);
				model.setApplyTypeName(typeManager.getAttendTypeByID(model.getApplyTypeId()).getTypeName());
				applyManager.updateAttenApply(model);
			}else {//销假单
				canc=applyManager.getToaAttendCancle(bussinessId.split(";")[2]);
				canc.setCancleState(NO);
				applyManager.updateAttendCancle(canc);
			}
		}
		
		return renderHtml("<script>window.returnValue ='OK'; window.close();</script>");
	}
	
	public ToaAttenApply getModel() {
		// TODO Auto-generated method stub
		return this.model;
	}


	public ApplyManager getApplyManager() {
		return applyManager;
	}
	@Autowired
	public void setApplyManager(ApplyManager applyManager) {
		this.applyManager = applyManager;
	}

	public Page<ToaAttenApply> getPage() {
		return page;
	}

	public void setPage(Page<ToaAttenApply> page) {
		this.page = page;
	}

	public void setModel(ToaAttenApply model) {
		this.model = model;
	}

	public String getApplyer() {
		return applyer;
	}

	public void setApplyer(String applyer) {
		this.applyer = applyer;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCancleEtime() {
		return cancleEtime;
	}

	public void setCancleEtime(Date cancleEtime) {
		this.cancleEtime = cancleEtime;
	}

	public Page<ToaAttendCancle> getCanclepage() {
		return canclepage;
	}

	public void setCanclepage(Page<ToaAttendCancle> canclepage) {
		this.canclepage = canclepage;
	}

	public Date getCancleStime() {
		return cancleStime;
	}

	public void setCancleStime(Date cancleStime) {
		this.cancleStime = cancleStime;
	}

	public Page<EForm> getEFpage() {
		return EFpage;
	}

	public void setEFpage(Page<EForm> fpage) {
		EFpage = fpage;
	}

	public WorkManager getWorkmanager() {
		return workmanager;
	}
	@Autowired
	public void setWorkmanager(WorkManager workmanager) {
		this.workmanager = workmanager;
	}

	public IUserService getUsmanager() {
		return usmanager;
	}
	@Autowired
	public void setUsmanager(IUserService usmanager) {
		this.usmanager = usmanager;
	}

	public PersonManager getPersonManager() {
		return personManager;
	}
	@Autowired
	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public ApplyTypeManager getTypeManager() {
		return typeManager;
	}
	@Autowired
	public void setTypeManager(ApplyTypeManager typeManager) {
		this.typeManager = typeManager;
	}

	public List getIdeaLst() {
		return ideaLst;
	}

	public void setIdeaLst(List ideaLst) {
		this.ideaLst = ideaLst;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getCancleid() {
		return cancleid;
	}

	public void setCancleid(String cancleid) {
		this.cancleid = cancleid;
	}

	public ToaAttendCancle getCanc() {
		return canc;
	}

	public void setCanc(ToaAttendCancle canc) {
		this.canc = canc;
	}

	public List<ToaAttendType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<ToaAttendType> typeList) {
		this.typeList = typeList;
	}

	@Override
	protected String getDictType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseManager getManager() {
		// TODO Auto-generated method stub
		return this.applyManager;
	}

	@Override
	protected String getModuleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWorkflowType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getIsbutian() {
		return isbutian;
	}

	public void setIsbutian(String isbutian) {
		this.isbutian = isbutian;
	}

}
