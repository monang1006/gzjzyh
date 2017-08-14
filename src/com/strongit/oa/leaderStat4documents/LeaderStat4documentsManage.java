package com.strongit.oa.leaderStat4documents;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDraftRecvdoc;
import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.docmonitor.vo.DocMonitorParamter;
import com.strongit.oa.docmonitor.vo.DocMonitorVo;
import com.strongit.oa.senddocRegistSta.SendDocRegistStaManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.work.manager.WorkExtendManager;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class LeaderStat4documentsManage extends BaseManager{
	
	private GenericDAOHibernate<ToaDraftRecvdoc, String> ReceiveRegistDAO;
	@Autowired
	protected AdapterBaseWorkflowManager adapterBaseWorkflowManager;
	/** 用户管理Service接口*/
	private IUserService userservice;
	@Autowired 
	protected IUserService userService;//统一用户服务
	@Autowired
	private ICustomUserService customUserService;
	@Autowired
	private WorkExtendManager workExtendManager;
	private IProcessDefinitionService iProcessDefinitionService;
	private SessionFactory sessionFactory;
	@Autowired
	private SendDocRegistStaManager sendDocRegistStaManager;
	/**
	 * 已登记/已分发的文件
	 * @return
	 */
	public String getReceiveRegistNum(String param){
		Object[] toSelectItems = {"processInstanceId"};
		List sItems = Arrays.asList(toSelectItems);		
		Map<Object, Object> orderMap = new HashMap<Object, Object>();
		orderMap.put("processStartDate", "0");
		IWorkflowService iWorkflowService = adapterBaseWorkflowManager.getWorkflowService();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processName", param);
		paramsMap.put("processSuspend", "0");	// 1表示废除
		int numByDay = getReceiveRegistByDay(iWorkflowService, sItems, orderMap, paramsMap);
		int numByWeek = getReceiveRegistByWeek(iWorkflowService, sItems, orderMap, paramsMap);
		int numByMonth = getReceiveRegistByMonth(iWorkflowService, sItems, orderMap, paramsMap);
		int numByYear = getReceiveRegistByYear(iWorkflowService, sItems, orderMap, paramsMap);
		String numTotal = numByDay + ";" + numByWeek + ";" + numByMonth + ";" + numByYear ;
		return numTotal;
	}
	
	/**
	 * 领导首页分管处室统计(首页顶部)
	 * @return String 格式: "处室名称,收文登记数-已分办文件数-待签收数,自办文数(办结)-收文数(办结)-发文数(办结),自办文数(在办)-收文数(在办)-发文数(在办),发文登记文件数(已编号)-发文登记文件数(未编号),处室id;  ...(同左)"
	 */
	public String getLeaderManageOrgs(){
		List<TempPo> orgList = getOrgList("leaderManage");
		Map<String, Object> sdrsMap = sendDocRegistStaManager.getRegistDocsNumByWeek(orgList);
		String totalStr = "";
		//如果分管领导所分管处室多于3个 则不查询
		if(orgList.size()>3){
			orgList = null;
		}
		for(TempPo tempPo : orgList){
			Date[] objs = new Date[]{startDateFormat(new Date()), endDateFormat(new Date())};
			
			//办结
			String sqlBase = "select count(p.id_) as total, p.name_ as p_name  from t_uums_base_user u, t_uums_base_org o, jbpm_processinstance p, T_MAINA_CORCONFING mc" +
					" where 1=1 and p.id_=mc.processinstance_id and u.org_id = o.org_id and p.start_user_id_ = u.user_id and p.name_ in ('处室收文办理','省政府发文流程','办公厅发文流程','自办文办理','登记办文')" +
					" and p.END_ is not null and p.ISSUSPENDED_=0 and o.org_id = '" + tempPo.getId() + "'";
			
			String sql = sqlBase + " and p.END_>? and p.END_<?  group by p.name_";
			objs = getDateParamByWeek(objs);
			Map<String, Integer> processedWeekMap = getCount(sql, objs);
			String processedCount = processedCountFormat(processedWeekMap);
			
			//在办
			String sqlBase2 = "select count(p.id_) as total, p.name_ as p_name  from t_uums_base_user u, t_uums_base_org o, jbpm_processinstance p, T_MAINA_CORCONFING mc" +
			" where 1=1 and p.id_=mc.processinstance_id and u.org_id = o.org_id and p.start_user_id_ = u.user_id and p.name_ in ('处室收文办理','省政府发文流程','办公厅发文流程','自办文办理','登记办文')" +
			" and p.END_ is null and p.ISSUSPENDED_=0 and o.org_id = '" + tempPo.getId() + "'";
			sql = sqlBase2 + " group by p.name_";
			Map<String, Integer> processingWeekMap = getCount(sql, null);
			String processingCount = processedCountFormat(processingWeekMap);
			
			//收文			
			String sqlBase3 = "select count(p.id_) as total, p.name_ as p_name  from t_uums_base_user u, t_uums_base_org o, jbpm_processinstance p, T_MAINA_CORCONFING mc" +
			" where 1=1 and p.id_=mc.processinstance_id and u.org_id = o.org_id and p.start_user_id_ = u.user_id and p.name_ in ('收文办件登记','处室收文办理')" +
			" and p.START_USER_ID_ is not null and p.ISSUSPENDED_=0 and o.org_id = '" + tempPo.getId() + "'";
			sql = sqlBase3 + " and p.START_>? and p.START_<?  group by p.name_";
			Map<String, Integer> receiveWeekMap = getCount(sql, objs);
			String receiveCount = (receiveWeekMap.get("收文办件登记")==null?"0":receiveWeekMap.get("收文办件登记")) + "-"
								+ (receiveWeekMap.get("处室收文办理")==null?"0":receiveWeekMap.get("处室收文办理"));
			String sqlBase4 = "select count(*) from jbpm_processinstance p,jbpm_taskinstance t where p.ID_= t.PROCINST_ and t.END_ is null and t.NAME_ like '%"+tempPo.getName()+"签收%' ";
			receiveCount = receiveCount + "-" + recDocQuery(sqlBase4);
			String orgStr = tempPo.getName() + "," + receiveCount + "," + processedCount + "," + processingCount + "," + sdrsMap.get(tempPo.getId()) + "," + tempPo.getId();
			totalStr = totalStr + ";" + orgStr;
		}
		return totalStr.substring(1);
	}
	
	public String recDocQuery(String sql){
		String count = "" ;
		PreparedStatement psmt = null;
		try {
			psmt = this.getConnection().prepareStatement(sql);
			ResultSet result = psmt.executeQuery();
			if(result.next()){
				count = result.getInt(1) + "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(psmt != null){
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return count;
	}
	
	/**
	 * 获得上周开始结束时间
	 * @param objs
	 * @return
	 */
	public Date[] getDateParamByWeek(Date[] objs){
		Date now = new Date();
		now.setTime(now.getTime()-7*24*60*60*1000); //上周
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
			now.setTime(now.getTime()-24*60*60*1000);
			calendar.setTime(now);
		}
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date date = calendar.getTime();
		objs[0] = startDateFormat(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
		date = calendar.getTime();
		objs[1] = endDateFormat(date);
		return objs;
	}
	
	/**
	 * 办结文件统计(自办文 收文 发文)  分处室
	 * @return	List<Map<String, Integer>> 
	 */
	public List<Map<String, Integer>> getProcessedCountByOrg(){
		List<Map<String, Integer>> totalMap = new ArrayList<Map<String, Integer>>();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");		
		String sqlStart = "select pi.total, org.org_id from t_uums_base_org org,(select count(p.id_) as total," +
						" o.org_id as org_id from t_uums_base_user u, t_uums_base_org o, jbpm_processinstance p, T_MAINA_CORCONFING mc where 1=1 and p.id_=mc.processinstance_id" +
						" and u.org_id = o.org_id and p.start_user_id_ = u.user_id and p.name_ in ('处室收文办理','省政府发文流程','办公厅发文流程','自办文办理','登记办文')" +
						" and p.END_ is not null and p.ISSUSPENDED_=0";		
		String sqlEnd = " group by o.org_id) pi where 1=1 and pi.org_id(+) = org.org_id and org.rest1='0' order by org.org_sequence";
		
		//by day
		Date[] objs = new Date[]{startDateFormat(new Date()), endDateFormat(new Date())};
		Map<String, Integer> dayMap = getCount(sqlStart + " and p.START_>to_date('" + sdf2.format(startDateFormat(new Date()))
										+ "','yyyy/mm/dd HH24:mi:ss') and p.START_<to_date('" + sdf2.format(endDateFormat(new Date()))
										+ "','yyyy/mm/dd HH24:mi:ss')" + sqlEnd, null);

		
		String sql = sqlStart + " and p.END_>? and p.END_<?" + sqlEnd;
		//by week
		Date now = new Date();
		now.setTime(now.getTime()-7*24*60*60*1000); //上周
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
			now.setTime(now.getTime()-24*60*60*1000);
			calendar.setTime(now);
		}
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date date = calendar.getTime();
		objs[0] = startDateFormat(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
		date = calendar.getTime();
		objs[1] = endDateFormat(date);
		Map<String, Integer> weekMap = getCount(sql, objs);
		//by month
		Date monthDate = new Date();
		calendar.setTime(monthDate);
		calendar.set(Calendar.DATE,1);
		Date dateM = calendar.getTime();
		objs[0] = startDateFormat(dateM);
		calendar.add(Calendar.MONDAY, 1);
		calendar.add(Calendar.DATE, -1);
		dateM = calendar.getTime();
		objs[1] = endDateFormat(dateM);
		Map<String, Integer> monthMap = getCount(sql, objs);
		//by year
		Date yearDate = new Date();
		calendar.setTime(yearDate);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		Date dateY = calendar.getTime();
		objs[0] = startDateFormat(dateY);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String end = calendar.get(Calendar.YEAR)+"-12-31";
		try {
			dateY = sdf.parse(end);
			objs[1] = endDateFormat(dateY);
			Map<String, Integer> yearMap = getCount(sql, objs);
			totalMap.add(dayMap);
			totalMap.add(weekMap);
			totalMap.add(monthMap);
			totalMap.add(yearMap);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return totalMap;
	}
	
	/**
	 * 在办文件统计(自办文 收文 发文)  分处室
	 * @return
	 */
	public Map<String, Integer> getProcessingCountByOrg(){
		String sqlStart = "select pi.total, org.org_id from t_uums_base_org org,(select count(p.id_) as total," +
		" o.org_id as org_id from t_uums_base_user u, t_uums_base_org o, jbpm_processinstance p, T_MAINA_CORCONFING mc where 1=1 and p.id_=mc.processinstance_id" +
		" and u.org_id = o.org_id and p.start_user_id_ = u.user_id and p.name_ in ('处室收文办理','省政府发文流程','办公厅发文流程','自办文办理','登记办文')" +
		" and p.END_ is null and p.ISSUSPENDED_=0";		
		String sqlEnd = " group by o.org_id) pi where 1=1 and pi.org_id(+) = org.org_id and org.rest1='0' order by org.org_sequence";
		Map<String, Integer> countMap = getCount(sqlStart + sqlEnd, null);
		return countMap;
	}
	
	/**
	 * 收文登记统计
	 * @return String 格式:"x-x,x-x,x-x,x-x"
	 */
	public String getReceiveCount(){
		String total = "";
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String sqlBase = "select count(p.ID_), p.NAME_ from JBPM_PROCESSINSTANCE p, T_MAINA_CORCONFING mc where 1=1 and p.id_=mc.processinstance_id and p.START_USER_ID_ is not null" +
						" and p.ISSUSPENDED_=0 and (p.NAME_='收文办件登记' or p.NAME_='处室收文办理')";	//发起人为null和废除 不统计
		
		//by day
		Date[] objs = new Date[]{startDateFormat(new Date()), endDateFormat(new Date())};
		Map<String, Integer> dayMap = getCount(sqlBase + " and p.START_>to_date('" + sdf2.format(startDateFormat(new Date()))
										+ "','yyyy/mm/dd HH24:mi:ss') and p.START_<to_date('" + sdf2.format(endDateFormat(new Date()))
										+ "','yyyy/mm/dd HH24:mi:ss') group by p.NAME_", null);
		String dayCount = (dayMap.get("收文办件登记")==null?"0":dayMap.get("收文办件登记")) +
							"-" + (dayMap.get("处室收文办理")==null?"0":dayMap.get("处室收文办理"));
		
		String sql = sqlBase + " and p.START_>? and p.START_<? group by p.NAME_";
		//by week
		Date now = new Date();
		now.setTime(now.getTime()-7*24*60*60*1000); //上周
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
			now.setTime(now.getTime()-24*60*60*1000);
			calendar.setTime(now);
		}
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date date = calendar.getTime();
		objs[0] = startDateFormat(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
		date = calendar.getTime();
		objs[1] = endDateFormat(date);
		Map<String, Integer> weekMap = getCount(sql, objs);
		String weekCount = (weekMap.get("收文办件登记")==null?"0":weekMap.get("收文办件登记")) + "-"
							+ (weekMap.get("处室收文办理")==null?"0":weekMap.get("处室收文办理"));
		//by month
		Date monthDate = new Date();
		calendar.setTime(monthDate);
		calendar.set(Calendar.DATE,1);
		Date dateM = calendar.getTime();
		objs[0] = startDateFormat(dateM);
		calendar.add(Calendar.MONDAY, 1);
		calendar.add(Calendar.DATE, -1);
		dateM = calendar.getTime();
		objs[1] = endDateFormat(dateM);
		Map<String, Integer> monthMap = getCount(sql, objs);
		String monthCount = (monthMap.get("收文办件登记")==null?"0":monthMap.get("收文办件登记")) + "-"
							+ (monthMap.get("处室收文办理")==null?"0":monthMap.get("处室收文办理"));
		//by year
		Date yearDate = new Date();
		calendar.setTime(yearDate);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		Date dateY = calendar.getTime();
		objs[0] = startDateFormat(dateY);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String end = calendar.get(Calendar.YEAR)+"-12-31";
		try {
			dateY = sdf.parse(end);
			objs[1] = endDateFormat(dateY);
			Map<String, Integer> yearMap = getCount(sql, objs);
			String yearCount = (yearMap.get("收文办件登记")==null?"0":yearMap.get("收文办件登记")) + "-"
								+ (yearMap.get("处室收文办理")==null?"0":yearMap.get("处室收文办理"));
			total = dayCount + "," + weekCount + "," + monthCount + "," + yearCount;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return total;
	}
	
	/**
	 * 在办文件统计(自办文 收文 发文)
	 * @return
	 */
	public String getProcessingCount(){
		String sql = "select count(p.ID_), p.NAME_ from JBPM_PROCESSINSTANCE p, T_MAINA_CORCONFING mc where 1=1 and p.id_=mc.processinstance_id" +
					 " and p.START_USER_ID_ is not null and p.END_ is null and p.ISSUSPENDED_=0" +
					 " and p.NAME_ in ('处室收文办理','省政府发文流程','办公厅发文流程','自办文办理','登记办文') group by p.NAME_";	//发起人为null和废除 不统计
		Map<String, Integer> countMap = getCount(sql, null);
		Integer sendCount = (countMap.get("省政府发文流程")==null?new Integer(0):countMap.get("省政府发文流程"))
							+ (countMap.get("办公厅发文流程")==null?new Integer(0):countMap.get("办公厅发文流程"));
		Integer selfCount = (countMap.get("自办文办理")==null?new Integer(0):countMap.get("自办文办理"))
							+ (countMap.get("登记办文")==null?new Integer(0):countMap.get("登记办文"));
		String count = selfCount + "-" + (countMap.get("处室收文办理")==null?"0":countMap.get("处室收文办理")) + "-" + sendCount;
		return count;
	}
	
	/**
	 * 办结文件统计(自办文 收文 发文)
	 * @return
	 */
	public String getProcessedCount(){
		String total = "";
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String sqlBase = "select count(p.ID_), p.NAME_ from JBPM_PROCESSINSTANCE p, T_MAINA_CORCONFING mc where 1=1 and p.id_=mc.processinstance_id" +
						 " and p.START_USER_ID_ is not null and p.END_ is not null and p.ISSUSPENDED_=0" +
						 " and p.NAME_ in ('处室收文办理','省政府发文流程','办公厅发文流程','自办文办理','登记办文')";	//发起人为null和废除 不统计
		//by day
		Date[] objs = new Date[]{startDateFormat(new Date()), endDateFormat(new Date())};		
		Map<String, Integer> dayMap = getCount(sqlBase + " and p.END_>to_date('" + sdf2.format(startDateFormat(new Date()))
										+ "','yyyy/mm/dd HH24:mi:ss') and p.END_<to_date('" + sdf2.format(endDateFormat(new Date()))
										+ "','yyyy/mm/dd HH24:mi:ss') group by p.NAME_", null);
		String dayCount = processedCountFormat(dayMap);
		
		String sql = sqlBase + " and p.END_>? and p.END_<? group by p.NAME_";
		//by week
		Date now = new Date();
		now.setTime(now.getTime()-7*24*60*60*1000); //上周
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
			now.setTime(now.getTime()-24*60*60*1000);
			calendar.setTime(now);
		}
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date date = calendar.getTime();
		objs[0] = startDateFormat(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
		date = calendar.getTime();
		objs[1] = endDateFormat(date);
		Map<String, Integer> weekMap = getCount(sql, objs);
		String weekCount = processedCountFormat(weekMap);
		
		//by month
		Date monthDate = new Date();
		calendar.setTime(monthDate);
		calendar.set(Calendar.DATE,1);
		Date dateM = calendar.getTime();
		objs[0] = startDateFormat(dateM);
		calendar.add(Calendar.MONDAY, 1);
		calendar.add(Calendar.DATE, -1);
		dateM = calendar.getTime();
		objs[1] = endDateFormat(dateM);
		Map<String, Integer> monthMap = getCount(sql, objs);
		String monthCount = processedCountFormat(monthMap);
		
		//by year
		Date yearDate = new Date();
		calendar.setTime(yearDate);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		Date dateY = calendar.getTime();
		objs[0] = startDateFormat(dateY);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String end = calendar.get(Calendar.YEAR)+"-12-31";
		try {
			dateY = sdf.parse(end);
			objs[1] = endDateFormat(dateY);
			Map<String, Integer> yearMap = getCount(sql, objs);
			String yearCount = processedCountFormat(yearMap);
			total = dayCount + "," + weekCount + "," + monthCount + "," + yearCount;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return total;
	}	
	
	/**
	 * 办结文件统计结果转换为String
	 * @param map
	 * @return
	 */
	public String processedCountFormat(Map<String, Integer> map){
		Integer sendCount = (map.get("省政府发文流程")==null?new Integer(0):map.get("省政府发文流程"))
							+ (map.get("办公厅发文流程")==null?new Integer(0):map.get("办公厅发文流程"));
		Integer selfCount = (map.get("自办文办理")==null?new Integer(0):map.get("自办文办理"))
							+ (map.get("登记办文")==null?new Integer(0):map.get("登记办文"));
		String count = selfCount + "-" + (map.get("处室收文办理")==null?"0":map.get("处室收文办理")) + "-" + sendCount;
		return count;
	}
	
	/**
	 * 根据sql语句得到记录数
	 * @param sql
	 * @return
	 */
	public Map<String, Integer> getCount(String sql, Date[] dates){
		Map<String, Integer> resultMap = new LinkedHashMap<String, Integer>();
		PreparedStatement psmt = null;
		try {
			psmt = this.getConnection().prepareStatement(sql);
			int length = (dates==null?0:dates.length);	
			for(int i=0; i<length; i++){
				Date dd = dates[i];
				java.sql.Date sqlDate = new java.sql.Date(dd.getTime());
				psmt.setDate(i+1, sqlDate);
			}
			ResultSet result = psmt.executeQuery();
			while(result.next()){
				resultMap.put(result.getString(2), new Integer(result.getInt(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(psmt != null){
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * 按天查询
	 * @param iWorkflowService
	 * @param sItems
	 * @param orderMap
	 * @return
	 */
	public int getReceiveRegistByDay(IWorkflowService iWorkflowService, List sItems, Map orderMap, Map<String, Object> paramsMap){
		Date now = new Date();
		startDateFormat(now);
		Date now2 = new Date();
		endDateFormat(now2);
		paramsMap.put("processStartDateStart", now);
		paramsMap.put("processStartDateEnd", now2);
		List<Object[]> list = iWorkflowService.getProcessInstanceByConditionForList(sItems, paramsMap, orderMap);
		return list.size();
	}
	
	/**
	 * 按周查询
	 * @param iWorkflowService
	 * @param sItems
	 * @param orderMap
	 * @return
	 */
	public int getReceiveRegistByWeek(IWorkflowService iWorkflowService, List sItems, Map orderMap, Map<String, Object> paramsMap){
		Date now = new Date();
		now.setTime(now.getTime()-7*24*60*60*1000); //上周
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
			now.setTime(now.getTime()-24*60*60*1000);
			calendar.setTime(now);
		}
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date date = calendar.getTime();
		startDateFormat(date);
		paramsMap.put("processStartDateStart", date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
		date = calendar.getTime();
		endDateFormat(date);
		paramsMap.put("processStartDateEnd", date);		
		List<Object[]> list = iWorkflowService.getProcessInstanceByConditionForList(sItems, paramsMap, orderMap);
		return list.size();
	}
	
	/**
	 * 按月查询
	 * @param iWorkflowService
	 * @param sItems
	 * @param orderMap
	 * @return
	 */
	public int getReceiveRegistByMonth(IWorkflowService iWorkflowService, List sItems, Map orderMap, Map<String, Object> paramsMap){
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.set(Calendar.DATE,1);
		Date date = calendar.getTime();
		startDateFormat(date);
		paramsMap.put("processStartDateStart", date);
		calendar.add(Calendar.MONDAY, 1);
		calendar.add(Calendar.DATE, -1);
		date = calendar.getTime();
		endDateFormat(date);
		paramsMap.put("processStartDateEnd", date);
		List<Object[]> list = iWorkflowService.getProcessInstanceByConditionForList(sItems, paramsMap, orderMap);
		return list.size();
	}
	
	/**
	 * 按年查询
	 * @param iWorkflowService
	 * @param sItems
	 * @param orderMap
	 * @return
	 */
	public int getReceiveRegistByYear(IWorkflowService iWorkflowService, List sItems, Map orderMap, Map<String, Object> paramsMap){
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		int numByYear = 0;
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		Date date = calendar.getTime();
		startDateFormat(date);
		paramsMap.put("processStartDateStart", date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String end = calendar.get(Calendar.YEAR)+"-12-31";
		try {
			date = sdf.parse(end);
			endDateFormat(date);
			paramsMap.put("processStartDateEnd", date);
			List<Object[]> list = iWorkflowService.getProcessInstanceByConditionForList(sItems, paramsMap, orderMap);
			numByYear = list.size();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return numByYear;
	}
	
	/**
	 * 设置查询开始时间
	 * @param date
	 * @return
	 */
	public Date startDateFormat(Date date){
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}
	
	/**
	 * 设置查询结束时间
	 * @param date
	 * @return
	 */
	public Date endDateFormat(Date date){
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		return date;
	}
	
	/**
	 * author:zhangli 
	 * description:获取所有处室 
	 * modifyer: 
	 * description:
	 * 
	 * @return User 用户对象
	 */
	public List<TempPo> getOrgList(String moduletype) throws SystemException,ServiceException{
		try{
			List<Organization> orglist = new ArrayList<Organization>();
			List<TempPo> newList = new ArrayList<TempPo>();
			List<String> syscodeList = new ArrayList<String>();
//			String idddd = userservice.getCurrentUser().getUserId();
//			if ("pige".equals(moduletype)) {// 如果为处室管理员，只能查看对应处室档案
//				orglist = userservice.getOrgAndChildOrgByCurrentUser();	
//			}else if("wpige".equals(moduletype)){
//				orglist = userservice.getCurrentUserOrgAndDept();
//			}
//			else{
//				boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
//				User user=userService.getCurrentUser();
//				if(!isView){
//					Organization org=userservice.getDepartmentByOrgId(user.getOrgId());
//					orglist.add(org);
//				}
//				else{
//					orglist=userservice.getOrgAndChildOrgByCurrentUser();					
//				}
//			}
			orglist = userservice.getOrgAndChildOrgByUserId("6006800BBDF034DFE040007F01001D9F");//超级管理员  任何用户都能看到所有部门
			
			if(orglist==null||orglist.size()<=0)
				return new ArrayList<TempPo>();
			
			for(int i=0;i<orglist.size();i++){
				Organization org = orglist.get(i);
				syscodeList.add(org.getOrgSyscode());
			}
			for(int i=0;i<orglist.size();i++){
				TempPo po = new TempPo();
				po.setType("org");
				Organization org = orglist.get(i);
				if(org.getRest1()!=null && "0".equals(org.getRest1())){								
					if(moduletype!=null && "leaderManage".equals(moduletype)){		//分管领导所分管部门
						String curUserId = userservice.getCurrentUser().getUserId();
						String leader = org.getRest2();
						if(leader!=null&&!"".equals(leader)){
							String[] strl = leader.split(",");	//一个部门可能有多个分管领导
							for(int j=0; j<strl.length; j++){
								leader = strl[j].substring(1);
								if(curUserId.equals(leader)){
									String orgSysCode = org.getOrgSyscode();
									Organization porg = userservice.getParentDepartmentBySyscode(orgSysCode);
									String porgsyscode = porg.getOrgSyscode();
									if(porg==null||orgSysCode.equals(porgsyscode)||!syscodeList.contains(porgsyscode))
											po.setParentId(null);
									else
										po.setParentId(porg.getOrgId());
									po.setId(org.getOrgId());
									po.setCodeId(orgSysCode);
									po.setName(org.getOrgName());
									newList.add(po);
								}
							}
						}
					}else{
						String orgSysCode = org.getOrgSyscode();
						Organization porg = userservice.getParentDepartmentBySyscode(orgSysCode);
						String porgsyscode = porg.getOrgSyscode();
						if(porg==null||orgSysCode.equals(porgsyscode)||!syscodeList.contains(porgsyscode))
								po.setParentId(null);
						else
							po.setParentId(porg.getOrgId());
						po.setId(org.getOrgId());
						po.setCodeId(orgSysCode);
						po.setName(org.getOrgName());
						newList.add(po);
					}
				}
			}
			return newList;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"当前用户"});
		}
	}
	
	/**
	 * 根据处室和查询粒度查询出收文登记数
	 * @param orgList
	 * @param selectType	0:按日 1:按周 2:按月 3:按年
	 * @return
	 */
	public Map<String, Object> getReceiveRegistBySelectType(List<TempPo> orgList, String selectType, DocMonitorParamter param, String workflowName){		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		param = new DocMonitorParamter();
		param.setWorkflowName(workflowName);
		param.setIsSuspended("0");
		param = setSelectTimeParam(selectType, param);
		if(orgList!=null && !orgList.isEmpty()){
			for(TempPo tempPo : orgList){
				List<String> userIds = customUserService.getAllUserIdByOrgId(tempPo.getId());
				if(userIds != null && !userIds.isEmpty()){
					param.setUserIds(userIds);
					Page<DocMonitorVo> page = new Page<DocMonitorVo>(1,true);
					page = workExtendManager.getProcessedWorks(page, param);
					map.put(tempPo.getId(), page.getTotalCount());
				}else{
					map.put(tempPo.getId(), 0);
				}
			}
		}
		return map;
	}
	
	/**
	 * 根据查询粒度 流程名 部门 查询返回page
	 * @param orgId
	 * @param workflowName
	 * @param selectType
	 * @return
	 */
	public Page<DocMonitorVo> getReceiveRegistByOrg(String orgId, String workflowName, String selectType){
		
//		Object[] toSelectItems = {"processInstanceId", "processMainFormBusinessId"};
//		List sItems = Arrays.asList(toSelectItems);		
//		Map<String, String> orderMap = new HashMap<String, String>();
//		orderMap.put("processStartDate", "0");
//		IWorkflowService iWorkflowService = adapterBaseWorkflowManager.getWorkflowService();
//		Map<String, Object> paramsMap = new HashMap<String, Object>();
//		paramsMap.put("processName", workflowName);
//		paramsMap.put("processSuspend", "0");
//		List<Object[]> list = iWorkflowService.getProcessInstanceByConditionForList(sItems, paramsMap, orderMap);
//		String ss = "";
//		for(Object[] obj : list){
//			String formId = (String)obj[1];
//			formId = formId.substring(formId.lastIndexOf(";")+1);
//			ss = ss + ",'" + formId + "'";
//		}
//		Page<ToaDraftRecvdoc> recivedocPage = new Page<ToaDraftRecvdoc>(20,true);
//		ss = "from ToaDraftRecvdoc t where t.oarecvdocid in (" + ss.substring(1) + ")";
//		System.out.println(ss);
//		recivedocPage = ReceiveRegistDAO.find(recivedocPage, ss, null);
////		List iii = ReceiveRegistDAO.findAll();
////		System.out.println("page count ======= "+recivedocPage.getTotalCount());
//		System.out.println("----=-=-=-=-=----------");
		
		
		Page<DocMonitorVo> page = new Page<DocMonitorVo>(20,true);
		DocMonitorParamter param = new DocMonitorParamter();
		param.setWorkflowName(workflowName);
		param.setIsSuspended("0");
		param = setSelectTimeParam(selectType, param);
		if(orgId!=null && !"".equals(orgId)){
			List<String> userIds = customUserService.getAllUserIdByOrgId(orgId);
			if(userIds != null && !userIds.isEmpty()){
				param.setUserIds(userIds);
				page = workExtendManager.getProcessedWorks(page, param);
			}
		}else{
			List<Object[]> users = customUserService.getAllUserInfo();
			List<String> userIds = new ArrayList<String>();
			for(Object[] obj : users){
				userIds.add(obj[0].toString());
			}
			param.setUserIds(userIds);
			page = workExtendManager.getProcessedWorks(page, param);
		}
		return page;
	}
	
	/**
	 * 根据查询粒度，向DocMonitorParamter参数中追加时间段查询
	 * @param selectType 查询粒度 0:当天 1:本周 2:当月 3:本年
	 * @param param
	 * @return
	 */
	public DocMonitorParamter setSelectTimeParam(String selectType, DocMonitorParamter param){
		Date sDate = new Date();
		Date eDate = new Date();
		Calendar calendar = Calendar.getInstance();
		if(selectType!=null){
			if("0".equals(selectType)){
				startDateFormat(sDate);
				endDateFormat(eDate);
			}else if("1".equals(selectType)){
				sDate.setTime(sDate.getTime()-7*24*60*60*1000); //上周
				calendar.setTime(sDate);
				if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
					sDate.setTime(sDate.getTime()-24*60*60*1000);
					calendar.setTime(sDate);
				}
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				sDate = calendar.getTime();
				startDateFormat(sDate);
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
				eDate = calendar.getTime();
				endDateFormat(eDate);
			}else if("2".equals(selectType)){
				calendar.setTime(sDate);
				calendar.set(Calendar.DATE,1);
				sDate = calendar.getTime();
				startDateFormat(sDate);
				calendar.add(Calendar.MONDAY, 1);
				calendar.add(Calendar.DATE, -1);
				eDate = calendar.getTime();
				endDateFormat(eDate);
			}else if("3".equals(selectType)){
				calendar.setTime(sDate);
				calendar.set(Calendar.DAY_OF_YEAR, 1);
				sDate = calendar.getTime();
				startDateFormat(sDate);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String end = calendar.get(Calendar.YEAR)+"-12-31";
				try {
					eDate = sdf.parse(end);
					endDateFormat(eDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			param.setStartDate(sDate);
			param.setEndDate(eDate);
		}	
		return param;
	}
	
	/**
	 * author:zhangli
	 * description:注册用户Service接口
	 * @param archivefilemanager 用户Service接口
	 */
	@Autowired
	public void setUserservice(IUserService userservice) throws SystemException,ServiceException{
		this.userservice = userservice;
	}

	public IProcessDefinitionService getIProcessDefinitionService() {
		return iProcessDefinitionService;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		ReceiveRegistDAO = new GenericDAOHibernate<ToaDraftRecvdoc, String>(
				sessionFactory, ToaDraftRecvdoc.class);
		this.sessionFactory = sessionFactory;
	}
	
}
