package com.strongit.oa.attendance.gather;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.attendance.register.RegisterManager;
import com.strongit.oa.bo.ToaAttendRecord;
import com.strongit.oa.bo.ToaAttendStatistic;
import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.parameter.OtherParameter;
import com.strongit.oa.infotable.IInfoTableService;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.personnel.personorg.PersonOrgManager;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Nov 24, 2009 8:55:51 PM
 * @version  2.0.4
 * @comment
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/attendance/gather/gather.action", type = ServletActionRedirectResult.class) })
public class GatherAction extends AbstractBaseWorkflowAction {

	private static final long serialVersionUID = 1L;
	private final static String ZERO="0";
	private final static String ONE="1";
	private final static String infoSetCode="40280cc22526195301252649bd340001";
	private Page<List> infopage = new Page<List>(FlexTableTag.MAX_ROWS, true);
	private ToaAttendStatistic model=new ToaAttendStatistic();//汇总考勤BO对象
	private InfoSetManager infoManager;						//信息集manager
	private InfoItemManager itemManager;					//信息项manager
	private IInfoTableService infoTable;					//信息集服务接口
	private PersonOrgManager orgmanager;					//机构编制manager
	private IInfoTableService manager;
	private List<ToaBaseOrg> orgList;
	private List<ToaSysmanageProperty> columnList;			//信息项列表
	private String pkey;
	private String codeType;	
	private String infoItems;
	private String orgId;									//机构ID				
	private Date beginTime;//汇总出勤开始时间
	private Date endTime;//汇总出勤结束时间
	private String userIds;//人员ID
	private String userNames;//人员名称
	private RegisterManager registerManager;
	private Map<String, BigDecimal> recordMap=new HashMap<String, BigDecimal>();
	private GatherManager gatherManager;

	private String gatherId;//
	private String gatherdate;//汇总时间
	@Autowired
	public void setRegisterManager(RegisterManager registerManager) {
		this.registerManager = registerManager;
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
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			infoItems=this.getSelectRowValue();
			if(infoItems!=null&&!"".equals(infoItems)){	//选择列
				columnList=manager.getTableTitle(infoSetCode,infoItems);
			}
			else{
				columnList=itemManager.getCreatedItemsByValue("T_OA_ATTEND_STATISTICS");
			}
			if(columnList!=null&&columnList.size()>0){
				ToaSysmanageStructure struct=infoManager.getInfoSetByInfoSetName("T_OA_ATTEND_STATISTICS");
				pkey=struct.getInfoSetPkey();
				String infoSetCode=struct.getInfoSetCode();
				StringBuffer consql=new StringBuffer(" 1=1 ");
				if(orgId!=null&&!"".equals(orgId)){
					consql.append(" and ORG_ID='")
					.append(orgId)
					.append("'");
				}
				if(model.getUserName()!=null&&!"".equals(model.getUserName())){//汇总开始时间
					consql.append(" and USER_NAME like'%")
					.append(model.getUserName())
					.append("%'");
				}
				if(model.getStatisticsStime()!=null&&!"".equals(model.getStatisticsStime())){//汇总开始时间
					consql.append(" and to_char(STATISTICS_STIME,'yyyy-MM-dd') >='")
					.append(sdf.format(model.getStatisticsStime())).append("'");
				}
				if(model.getStatisticsEtime()!=null&&!"".equals(model.getStatisticsStime())){//汇总结束时间
					consql.append(" and to_char(STATISTICS_ETIME,'yyyy-MM-dd') <='")
					.append(sdf.format(model.getStatisticsEtime())).append("'");
				}
				if(model.getAutiState()!=null&&!"".equals(model.getAutiState())){//汇总结束时间
					consql.append(" and AUTI_STATE='")
					.append(model.getAutiState()).append("'");
				}else{
					consql.append(" and  (AUTI_STATE is null or AUTI_STATE='")
					.append(ZERO).append("') ");
				}
				infopage = infoTable.getTablePage(columnList, infoSetCode,consql.toString(), infopage);	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	 * 查看信息
	 * @author hull
	 * @date 2010-03-24
	 * @return
	 * @throws Exception
	 */
	public String gatherlist() throws Exception {
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			columnList=itemManager.getCreatedItemsByValue("T_OA_ATTEND_STATISTICS");
			if(columnList!=null&&columnList.size()>0){
				ToaSysmanageStructure struct=infoManager.getInfoSetByInfoSetName("T_OA_ATTEND_STATISTICS");
				pkey=struct.getInfoSetPkey();
				String infoSetCode=struct.getInfoSetCode();
				StringBuffer consql=new StringBuffer(" (AUTI_STATE is null or AUTI_STATE='")
				.append(ONE)
				.append("')");
				if(gatherId!=null&&!"".equals(gatherId)){
					consql.append(" and STATISTICS_ID='")
					.append(gatherId)
					.append("'");
				}
				
				infopage = infoTable.getTablePage(columnList, infoSetCode,consql.toString(), infopage);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "gatherlist";
	}

	@Override
	protected void prepareModel() throws Exception {

	}


	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 24, 2009 9:58:59 PM
	 * Desc:	展现机构树
	 * param:
	 */
	public String orgtree()throws Exception{
		try {
			codeType=PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
			orgList = orgmanager.getOrgsByIsdel("0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "orgtree";
	}

	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 25, 2009 9:33:38 AM
	 * Desc:	获取选择列信息
	 * param:
	 */
	public String getSelectRowValue()throws Exception {
		HttpServletResponse response=this.getResponse();
		HttpServletRequest request=this.getRequest();
		String cookieName="Sender"; 	//定义cookie的名字
		String svalues="";				//cookie的值
		Cookie cookies[]=request.getCookies();//获取所有的cookie对象
		for(int i=0;i<cookies.length;i++)
		{
			Cookie cookie=cookies[i];
			if(cookieName.equals(cookie.getName()))	//判断名为sender的cookie是否已经存在，如果存在则返回该cookie的值
			{
				svalues=cookie.getValue();
				break;
			}	
		}
		if(infoItems!=null&&!"".equals(infoItems)&&infoItems!="null")//如果值不为空，且没有存入cookie中,则保存到cookies中
		{	
			if("".equals(svalues))
			{
				Cookie cookie=new Cookie(cookieName,infoItems); //获取名为sender的cookie对象
				response.addCookie(cookie); 
			}
			else
			{
				Cookie oldcookie=new Cookie(cookieName,svalues);
				oldcookie.setMaxAge(0);
				Cookie cookie=new Cookie(cookieName,infoItems); //获取名为sender的cookie对象
				response.addCookie(cookie); 
			}
			return infoItems;//将该cookies对象保存到客户端
		}else if(!"".equals(svalues))
			return svalues;
		else	
			return null;
	}


	/**
	 * 初始化汇总页面
	 * @author 胡丽丽
	 * @date:2009-11-25
	 * @return
	 * @throws Exception
	 */
	public String statistic()throws Exception{
		return "statistic";
	}
	/**
	 * 获取要查询的人员考勤详细情况
	 * @author 胡丽丽
	 * @date:2009-11-28
	 */
	public void getTypeRecord(){
		List<Object> list=registerManager.getTimesByProperty(userIds, beginTime, endTime);
		String key="";
		for(Object obj:list){
			Object[] objs=(Object[])obj;
			if(objs[1]!=null){
				key=objs[0].toString()+"/"+objs[1].toString();
				if(objs[2]==null){
					recordMap.put(key, BigDecimal.valueOf(0));
				}else{
					recordMap.put(key, BigDecimal.valueOf(Float.parseFloat(objs[2].toString())));
				}
			}
		}
	}
	/**
	 * 保存汇总
	 * @author 胡丽丽
	 * @date:2009-11-28
	 */
	@Override
	public String save() throws Exception {
		this.getTypeRecord();
		String yesusesid="";//可以汇总的人员ID
		String nousername="";//这个时间段已经汇总过的人员名称
		int sizeuser = 0;
		String [] users=userIds.split(",");
		String[] names=userNames.split(",");
		//清除这段时间内已经汇总过的考勤
		for(int i=0;i<users.length;i++){
			if(gatherManager.selectCountByUserId(users[i],beginTime,endTime)==0){
				yesusesid=yesusesid+","+users[i];
			}else{
				nousername=nousername+","+names[i];
			}
		}
		if(!"".equals(yesusesid)){//判断是否有没有汇总过的用户
			yesusesid=yesusesid.substring(1);
			sizeuser = yesusesid.split(",").length;
			List<ToaAttendRecord> recordlist = registerManager.getRecordByTimeAndUser(yesusesid, beginTime, endTime);
			if (recordlist != null && recordlist.size() > 0) {
				ToaSysmanageStructure struct = infoManager.getInfoSetByInfoSetName("T_OA_ATTEND_STATISTICS");
				columnList = itemManager.getAllCreatedItemsByValue("T_OA_ATTEND_STATISTICS");
				gatherManager.save(recordlist, recordMap, beginTime, endTime,sizeuser);
			}else{
				if(!"".equals(nousername)){
					nousername=nousername.substring(1);
					return renderHtml("<script> alert('"+nousername+",这段时间内已经汇总过并且已提交，不需要再次汇总。其他人员没有没有考勤记录，无法汇总！'); window.dialogArguments.document.forms[0].submit();window.close();</script>");
				}else{
					return renderHtml("<script> alert('您选择要汇总的人员这段时间没有考勤记录，无法汇总！'); window.dialogArguments.document.forms[0].submit();window.close();</script>");
				}
			}
		}
		if(!"".equals(nousername)){
			nousername=nousername.substring(1);
			return renderHtml("<script> alert('"+nousername+",这段时间内已经汇总过了不需要再次汇总！'); window.dialogArguments.document.forms[0].submit();window.close();</script>");
		}
		return renderHtml("<script> window.dialogArguments.document.forms[0].submit();window.close();</script>");
	}
	/**
	 * 查看人员考勤明细
	 * @author 胡丽丽
	 * @date:2009-11-28
	 * @return
	 */
	public String showRecord(){
		Object[] objs=gatherManager.getGatherById(pkey);
		StringBuffer st=new StringBuffer("<script> window.location='"+getRequest().getContextPath());
		st.append("/attendance/register/record!userRecordList.action?userId="+objs[0]+"&beginTime="+objs[1]+"&endTime="+objs[2]+"&isGather=gather'</script>");
		return renderHtml(st.toString());
	}
	
	/**
	 * 提交流程
	 * @author:hull
	 * @date:2009-12-18 上午10:46:21
	 * @return
	 */
	public String wizard() throws Exception {
		SimpleDateFormat df=new SimpleDateFormat("yyyy年MM月dd日");
		bussinessId=gatherId;
		Object[] objs=gatherManager.getGatherById(gatherId);
		beginTime=(Date)objs[1];
		businessName=objs[3]+df.format((Date)objs[1])+"至"+df.format((Date)objs[2])+"考勤汇总情况";
		//获取审批意见列表
		// 查找流程类型对应的流程
		workflows = gatherManager.getProcessOwnedByProcessType("569983");
		ideaLst = getManager().getCurrentUserDictItem(getDictType());
		//得到发文字典类主键
		ToaSysmanageDict dict = getManager().getDict(getDictType());
		if(dict != null){
			dictId = dict.getDictCode();
		}
		return "wizard";
	}
	/**
	 * 提交数据至工作流
	 * @author:hull
	 * @date:2010-03-24
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String handleWorkflow() throws Exception {
		try{
			//将提醒(方式|内容)存储在session中
			ActionContext cxt = ActionContext.getContext();
			cxt.getSession().put("remindType", remindType);
			cxt.getSession().put("handlerMes", handlerMes);
			cxt.getSession().put("moduleType", getModuleType());//调用消息模块的类型
			if(suggestion!= null){
				suggestion = suggestion.replaceAll("\\r\\n", "");////处理审批意见有回车换行的情况		
			}
			 OtherParameter otherparameter = initOtherParameter();
			if ("".equals(strTaskActors)) {
				getManager().handleWorkflow("0", workflowName,bussinessId,businessName,null,transitionName,concurrentTrans,suggestion,formData,otherparameter);
			} else {
				getManager().handleWorkflow("0", workflowName,bussinessId,businessName,strTaskActors.split(","),transitionName,concurrentTrans,suggestion,formData,otherparameter);
			}
			//在这里保存审批意见
			getManager().saveIdea(dictId, suggestion);
			//修改审核状态
			if(bussinessId!=null&&!"".equals(bussinessId)){
				gatherManager.updateGatherAutistate(bussinessId, "1", new OALogInfo("送审考勤汇总记录"));
			}
			return renderHtml("<script>window.returnValue ='OK'; window.close();</script>");
		}catch(Exception e){e.printStackTrace();
			logger.error("提交工作流异常，异常信息：" + e.getMessage());
			return renderHtml("<script>window.returnValue ='NO'; window.close();</script>");
		}
	}
	/**
	 * 提交流程走向下一步
	 * 提交成功返回“OK”，提交失败返回“NO”
	 * @author:hull
	 * @date:2010-03-24
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String handleNextStep(){
		try{
			//将提醒(方式|内容)存储在session中
			ActionContext cxt = ActionContext.getContext();
			cxt.getSession().put("remindType", remindType);
			cxt.getSession().put("handlerMes", handlerMes);
			cxt.getSession().put("moduleType", getModuleType());//调用消息模块的类型
			String isNewForm = "";//当前节点
			
			String[] info = getManager().getFormIdAndBussinessIdByTaskId(taskId);
			bussinessId = info[0];
			if ("0".equals(bussinessId)) {//bussinessid只有在子流程的时候会为0
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
//			在这里保存审批意见
			getManager().saveIdea(dictId, suggestion);
			return renderHtml("<script>window.returnValue ='OK'; window.close();</script>");//提交至流程后关闭窗口
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("提交工作流异常，异常信息：" + e.getMessage());
			return renderHtml("<script>window.returnValue ='NO'; window.close();</script>");//提交至流程后关闭窗口
		}
	}
	/**
	 * 退回
	 * @author hull
	 * @date 2010-03-24
	 */
	public String back() throws Exception {
		String ret = "0";
		try{
			if(StringUtils.hasLength(taskId)){
				JSONObject jsonObj = JSONObject.fromObject(suggestion);
				String content = jsonObj.getString("suggestion");
				remindType = jsonObj.getString("remindType");
//				将提醒(方式|内容)存储在session中
				ActionContext cxt = ActionContext.getContext();
				cxt.getSession().put("remindType", remindType);
				cxt.getSession().put("handlerMes", content);
				cxt.getSession().put("moduleType", getModuleType());//调用消息模块的类型
				String returnNodeId = getRequest().getParameter("returnNodeId");
				String curUserId = adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserId();
				getManager().backSpace(taskId, returnNodeId, formId, suggestion,curUserId, formData);
			}else{
				ret = "-1";
			}
		}catch(SystemException e){
			logger.error(e.getMessage());
			ret = "-3";
		}catch(Exception ex){
			logger.error("退回任务时出现异常,异常信息：" + ex.getMessage());
			ret = "-2";
		}
		return this.renderText(ret);
	}
	/**
	 * 查询业务数据ID
	 * 
	 * @author 胡丽丽
	 * @date 2010-03-22
	 * @return
	 * @throws Exception
	 */
	public String displayview() throws Exception {
		String[] info = getManager().getFormIdAndBussinessIdByTaskId(taskId);
		String cid = info[0];
		return renderHtml(cid);
	}
	public ToaAttendStatistic getModel() {
		return model;
	}

	public void setModel(ToaAttendStatistic model) {
		this.model = model;
	}

	public List<ToaSysmanageProperty> getColumnList() {
		return columnList;
	}

	@Autowired
	public void setInfoManager(InfoSetManager infoManager) {
		this.infoManager = infoManager;
	}

	@Autowired
	public void setItemManager(InfoItemManager itemManager) {
		this.itemManager = itemManager;
	}

	public String getPkey() {
		return pkey;
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public Page<List> getInfopage() {
		return infopage;
	}

	public void setInfopage(Page<List> infopage) {
		this.infopage = infopage;
	}

	@Autowired
	public void setInfoTable(IInfoTableService infoTable) {
		this.infoTable = infoTable;
	}

	public List<ToaBaseOrg> getOrgList() {
		return orgList;
	}

	@Autowired
	public void setOrgmanager(PersonOrgManager orgmanager) {
		this.orgmanager = orgmanager;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCodeType() {
		return codeType;
	}

	public String getInfoItems() {
		return infoItems;
	}

	public void setInfoItems(String infoItems) {
		this.infoItems = infoItems;
	}

	@Autowired
	public void setManager(IInfoTableService manager) {
		this.manager = manager;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getUserNames() {
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

	public Map<String, BigDecimal> getRecordMap() {
		return recordMap;
	}

	public void setRecordMap(Map<String, BigDecimal> recordMap) {
		this.recordMap = recordMap;
	}

	@Autowired
	public void setGatherManager(GatherManager gatherManager) {
		this.gatherManager = gatherManager;
	}

	@Override
	protected String getDictType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseManager getManager() {
		// TODO Auto-generated method stub
		return this.gatherManager;
	}

	@Override
	protected String getModuleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWorkflowType() {
		// TODO Auto-generated method stub
		return "569983";
	}

	public String getGatherId() {
		return gatherId;
	}

	public void setGatherId(String gatherId) {
		this.gatherId = gatherId;
	}

	public String getGatherdate() {
		return gatherdate;
	}

	public void setGatherdate(String gatherdate) {
		this.gatherdate = gatherdate;
	}

}
