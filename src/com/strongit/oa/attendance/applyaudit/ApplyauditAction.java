package com.strongit.oa.attendance.applyaudit;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.attendance.apply.ApplyManager;
import com.strongit.oa.attendance.applytype.ApplyTypeManager;
import com.strongit.oa.bo.ToaAttenApply;
import com.strongit.oa.bo.ToaAttendCancle;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.parameter.OtherParameter;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.oa.work.WorkManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;

public class ApplyauditAction extends AbstractBaseWorkflowAction {
	
	private Page pageflow=new Page(FlexTableTag.MAX_ROWS, true);			//工作流取出数据分页
	private WorkManager workmanager;
	private ApplyManager applyManager;
	@Autowired private ApplyTypeManager typeManager;	//申请类型服务类
	@Autowired private IUserService usmanager;			//用户接口  
	@Autowired private PersonManager personManager;		//人事人员接口
	private ToaAttenApply model = new ToaAttenApply();	//已送审申请单
	private ToaAttendCancle canc=new ToaAttendCancle();	//销假单
	private String cancleid;
	private String userName;
	private String userId;
	private String workId;		//	 工作流处理
	private String applyId;		// 申请ID
	private String ids;
 

	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public WorkManager getWorkmanager() {
		return workmanager;
	}
	@Autowired
	public void setWorkmanager(WorkManager workmanager) {
		this.workmanager = workmanager;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}
	
	@Override
	public String list() throws Exception {
		return null;
	}
	
	/**
	 * 销假单展现列表
	 * @return
	 * @throws Exception
	 */
	public String canclelist() throws Exception{
		pageflow=getManager().getTodoWorks(pageflow,getWorkflowType(),businessName,userName,startDate,endDate,isBackSpace);
		return "canclelist";
	}

	@Override
	protected void prepareModel() throws Exception {
		
	}

	@Override
	public String save() throws Exception {
		return null;
	}
	
	/**
	 * 转交下一步处理：销假单和请假单审都是走这个方法
	 * @return
	 */
	public String nextstep() throws Exception {
		try {
			businessName =URLDecoder.decode(businessName , "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ideaLst = getManager().getCurrentUserDictItem(getDictType());	//获取审批意见列表
		ToaSysmanageDict dict = getManager().getDict(getDictType());	//得到发文字典类主键
		if(dict != null){
			dictId = dict.getDictCode();
		}
		return "nextstep";
	}

	/**
	 * 表单提交下一步：销假单和请假单审都是走这个方法
	 *@author 胡丽丽
	 *@date 3010-03-22
	 * @return
	 */
	public String handleNextStep() {
		try{
			//将提醒(方式|内容)存储在session中
			ActionContext cxt = ActionContext.getContext();
			cxt.getSession().put("remindType", remindType);
			cxt.getSession().put("handlerMes", handlerMes);
			cxt.getSession().put("moduleType", getModuleType());//调用消息模块的类型
			cxt.getSession().put("formData", fullFormData);		//存储电子表单数据，用于归档至档案中心.
			String isNewForm = "";
			String[] info = getManager().getFormIdAndBussinessIdByTaskId(taskId);
			bussinessId = info[0];
			if ("0".equals(bussinessId)) {
				isNewForm = "1";
			} else {
				isNewForm = "0";
			}		
			if(suggestion!=null){
				suggestion = suggestion.replaceAll("\\r\\n", "");//处理审批意见有回车换行的情况		
			}
			String curUserId = adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserId();
			 OtherParameter otherparameter = initOtherParameter();
			if ("".equals(strTaskActors)) {
				getManager().handleWorkflowNextStep(taskId,transitionName,"",isNewForm,formId,bussinessId,suggestion,curUserId,null,formData,otherparameter);
			} else {
				getManager().handleWorkflowNextStep(taskId,transitionName,"",isNewForm,formId,bussinessId,suggestion,curUserId,strTaskActors.split(","),formData,otherparameter);
			}	
			//在这里保存审批意见
			getManager().saveIdea(dictId, suggestion);
			//获取申请单
			if(bussinessId!=null && !bussinessId.equals("")){
				//判断是否是申请单
				if("APPLY_ID".equals(bussinessId.split(";")[1])){
					model=applyManager.getAttenApply(bussinessId.split(";")[2]);
					model.setApplyTypeName(typeManager.getAttendTypeByID(model.getApplyTypeId()).getTypeName());
					applyManager.updateAttenApply(model);
				}
			}
			return renderHtml("<script>window.returnValue ='OK'; window.close();</script>");//提交至流程后关闭窗口
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("提交工作流异常，异常信息：" + e.getMessage());
			return renderHtml("<script>window.returnValue ='NO'; window.close();</script>");//提交至流程后关闭窗口
		}
	}
	
	/**
	 * 销假表单提交下一步(取消)
	 *@author 胡丽丽
	 *@date 3010-03-22
	 * @return
	 * @throws Exception
	 */
	public String cnhandleNextStep() throws Exception {
		String isNewForm = "1"; // 当前节点是否是新的表单
		String returnNodeId = "";
		try {
			/*if ("".equals(strTaskActors)) {
				workmanager.handleWorkflowNextStep(taskId, transitionName,
						returnNodeId, isNewForm, "0", cancleid, suggestion,
						null, concurrentTrans);
			} else {
				workmanager.handleWorkflowNextStep(taskId, transitionName,
						returnNodeId, isNewForm, "0", cancleid, suggestion,
						strTaskActors.split(","), concurrentTrans);
			}*/
			//在这里保存审批意见
			getManager().saveIdea(dictId, suggestion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return renderHtml("<script>window.returnValue ='OK'; window.close();</script>");
	}

	/**	
	 * 查看申请单
	 * @author 蒋国斌
	 * @date 2009-11-23 下午02:37:21 
	 * @return
	 * @修改时间 2010-03-16
	 */
	public String docView(){
		try {
			businessName =URLDecoder.decode(businessName , "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(StringUtils.hasLength(taskId)){ 
			String[] info = workmanager.getFormIdAndBussinessIdByTaskId(taskId);
			workId=info[0];
			formId = info[1];
			String[] strBussinessId = workId.split(";");
			tableName = strBussinessId[0];
			pkFieldName = strBussinessId[1];
			pkFieldValue =  strBussinessId[2];
			ids = strBussinessId[2];
		}
		return "docView";
	}

	/**	
	 * 查看销假单
	 *@author 胡丽丽
	 *@date 2010-03-15 下午02:37:21 
	 * @return
	 */
	public String candocView(){
		try {
			businessName =URLDecoder.decode(businessName , "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(StringUtils.hasLength(taskId)){ 
			String userid=usmanager.getCurrentUser().getUserId();
			ToaBasePerson person=personManager.getPersonByUumsPerId(userid);
			String[] info = workmanager.getFormIdAndBussinessIdByTaskId(taskId);
			workId=info[0];
			formId = info[1];
			String[] strBussinessId = workId.split(";");
			tableName = strBussinessId[0];
			pkFieldName = strBussinessId[1];
			pkFieldValue =  strBussinessId[2];
			ids = strBussinessId[2];
			canc=applyManager.getToaAttendCancle(ids);
			//获取申请单原因：销假审核时，如果用户修改了销假时间，电子表单上要判断销假时间段是否在请假时间段内
			model=canc.getToaAttenApply();
		}
		return "docView";
	}

	public Object getModel() {
		return this.model;
	}
	
	public String getApplyId() {
		return applyId;
	}
	
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public void setModel(ToaAttenApply model) {
		this.model = model;
	}
	
	public ApplyManager getApplyManager() {
		return applyManager;
	}
	
	@Autowired
	public void setApplyManager(ApplyManager applyManager) {
		this.applyManager = applyManager;
	}

	public String getWorkId() {
		return workId;
	}
	
	public void setWorkId(String workId) {
		this.workId = workId;
	}
	
	public String getIds() {
		return ids;
	}
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	public Page getPageflow() {
		return pageflow;
	}
	
	public void setPageflow(Page pageflow) {
		this.pageflow = pageflow;
	}
	
	public String getCancleid() {
		return cancleid;
	}
	
	public void setCancleid(String cancleid) {
		this.cancleid = cancleid;
	}
	
	@Override
	protected String getDictType() {
		return null;
	}
	
	@Override
	protected BaseManager getManager() {
		return this.applyManager;
	}
	
	@Override
	protected String getModuleType() {
		return null;
	}
	
	@Override
	protected String getWorkflowType() {
		return workflowType;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
