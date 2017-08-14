package com.strongit.oa.senddocRegistSta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSendDocRegist;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.senddocRegist.SendDocRegistManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class SendDocRegistStaManager {
	
	private GenericDAOHibernate<ToaSendDocRegist, String> registDocDAO;
	private SessionFactory sessionFactory;
	private SendDocRegistManager sendDocRegistManager;
	private Date date;
	private Calendar calendar;
	/** 用户管理Service接口*/
	private IUserService userservice;
	@Autowired 
	protected IUserService userService;//统一用户服务
	
	public SendDocRegistStaManager(){
		date = new Date();
		calendar = Calendar.getInstance();
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		registDocDAO = new GenericDAOHibernate<ToaSendDocRegist, String>(
				sessionFactory, ToaSendDocRegist.class);
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * 按查询粒度取出发文登记数
	 * @return
	 */
	public String getRegistDocsNum(){
		List<TempPo> orgs = getOrgList("wjtj");
		String sqlPart = "";
		if(!orgs.isEmpty()){	
			for(TempPo org : orgs){
				String orgId = org.getId();
				sqlPart = sqlPart + ",'" + orgId + "'";
			}
			sqlPart = " and t.toRoom in (" + sqlPart.substring(1) + ")";
		}else{
			sqlPart = " and t.toRoom in ('" + sqlPart + "')";
		}
		String sql = "select count(*) from ToaSendDocRegist t where 1=1 and t.receiveTime >= ? and ? >= t.receiveTime";
		sql = sql + sqlPart;
		Session session = sessionFactory.getCurrentSession();
		Object numByDay =  getRegistDocsByDay(session, sql, null);
		Object numByWeek = getRegistDocsByWeek(session, calendar, sql, null);
		Object numByMonth = getRegistDocsByMonth(session, calendar, sql, null);	
		Object numByYear = getRegistDocsByYear(session, calendar, sql, null);
		String numTotal = numByDay + ";" + numByWeek + ";" + numByMonth + ";" + numByYear ;
		return numTotal;
	}
	
	/**
	 * 按查询粒度取出发文登记数(无权限控制) 发文登记是否已编号
	 * @return
	 */
	public String getRegistDocsNumWithOutRight(){
		String sql = "select count(*) from ToaSendDocRegist t where 1=1 and t.docCode is not null and t.receiveTime >= ? and ? >= t.receiveTime";//已编号
		Session session = sessionFactory.getCurrentSession();
		//已编号
		Object numByDay =  getRegistDocsByDay(session, sql, null);
		Object numByWeek = getRegistDocsByWeek(session, calendar, sql, null);
		Object numByMonth = getRegistDocsByMonth(session, calendar, sql, null);	
		Object numByYear = getRegistDocsByYear(session, calendar, sql, null);
		//未编号
		sql = "select count(*) from ToaSendDocRegist t where 1=1 and t.docCode is null and t.receiveTime >= ? and ? >= t.receiveTime";//未编号
		Object numByDay2 =  getRegistDocsByDay(session, sql, null);
		Object numByWeek2 = getRegistDocsByWeek(session, calendar, sql, null);
		Object numByMonth2 = getRegistDocsByMonth(session, calendar, sql, null);	
		Object numByYear2 = getRegistDocsByYear(session, calendar, sql, null);
		String numTotal = numByDay + "-" + numByDay2 + ";"
						+ numByWeek + "-" + numByWeek2 + ";"
						+ numByMonth + "-" + numByMonth2 + ";"
						+ numByYear + "-" + numByYear2 ;
		return numTotal;
	}
	
	/**
	 * 查询本周已编号及未编号发文登记数(根据处室)
	 * @param orgList
	 * @return
	 */
	public Map<String, Object> getRegistDocsNumByWeek(List<TempPo> orgList){
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for(TempPo tempPo : orgList){
			String sql = "select count(*) from ToaSendDocRegist t where 1=1 and t.docCode is not null and t.receiveTime >= ? and ? >= t.receiveTime and t.toRoom = ?";
			Session session = sessionFactory.getCurrentSession();
			Object numWithCode = getRegistDocsByWeek(session, calendar, sql, tempPo.getId());
			sql = "select count(*) from ToaSendDocRegist t where 1=1 and t.docCode is null and t.receiveTime >= ? and ? >= t.receiveTime and t.toRoom = ?";
			Object numWithoutCode = getRegistDocsByWeek(session, calendar, sql, tempPo.getId());
			map.put(tempPo.getId(), numWithCode + "-" + numWithoutCode);
		}
		return map;
	}
	
	/**
	 * 查询当天发文登记记录
	 * @param session
	 * @param sql
	 * @param param
	 * @return
	 */
	public Object getRegistDocsByDay(Session session, String sql, String param){
		Query query = session.createQuery(sql);
		Date startDate = new Date();
		startDate(startDate);
		query.setParameter(0, startDate);
		Date endDate = new Date();
		endDate(endDate);
		query.setParameter(1, endDate);
		if(param!=null && !"".equals(param)){
			query.setParameter(2, param);
		}
		return query.uniqueResult();
	}
	
	/**
	 * 查询本周发文登记记录
	 * @param calendar
	 * @param query
	 * @return
	 */
	public Object getRegistDocsByWeek(Session session, Calendar calendar, String sql, String param){
		Query query = session.createQuery(sql);
		date = new Date();
		date.setTime(date.getTime()-7*24*60*60*1000);//上周
		calendar.setTime(date);
		if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
			date.setTime(date.getTime()-24*60*60*1000);
			calendar.setTime(date);
		}
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date date = calendar.getTime();
		startDate(date);
		query.setParameter(0, date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
		date = calendar.getTime();
		endDate(date);
		query.setParameter(1, date);
		if(param!=null && !"".equals(param)){
			query.setParameter(2, param);
		}
		return query.uniqueResult();
	}
	
	/**
	 * 查询当月发文登记记录
	 * @param calendar
	 * @param query
	 * @return
	 */
	public Object getRegistDocsByMonth(Session session, Calendar calendar, String sql, String param){
		Query query = session.createQuery(sql);
		calendar.setTime(date);
		calendar.set(Calendar.DATE,1);
		Date date = calendar.getTime();
		startDate(date);
		query.setParameter(0, date);
		calendar.add(Calendar.MONDAY, 1);
		calendar.add(Calendar.DATE, -1);
		date = calendar.getTime();
		endDate(date);
		query.setParameter(1, date);
		if(param!=null && !"".equals(param)){
			query.setParameter(2, param);
		}
		return query.uniqueResult();
	}
	
	/**
	 * 查询今年发文登记记录
	 * @param calendar
	 * @param query
	 * @return
	 */
	public Object getRegistDocsByYear(Session session, Calendar calendar, String sql, String param){
		Query query = session.createQuery(sql);
		calendar.setTime(date);
		Object numByYear = null;
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		Date date = calendar.getTime();
		startDate(date);
		query.setParameter(0, date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String end = calendar.get(Calendar.YEAR)+"-12-31";
		try {
			date = sdf.parse(end);
			endDate(date);
			query.setParameter(1, date);
			if(param!=null && !"".equals(param)){
				query.setParameter(2, param);
			}
			numByYear =  query.uniqueResult();
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
	public Date startDate(Date date){
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
	public Date endDate(Date date){
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
	public List getOrgList(String moduletype) throws SystemException,ServiceException{
		try{
			List<Organization> orglist = new ArrayList();
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
					if(moduletype!=null && "wjtj".equals(moduletype)){		//分管领导所分管部门
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
	 * 根据处室和查询粒度查询出发文登记数
	 * @param orgList
	 * @param selectType	0:按日 1:按周 2:按月 3:按年
	 * @return
	 */
	public Map<String, Object> getRegistDocsBySelectType(List<TempPo> orgList, String selectType){
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String sql = "select count(*) from ToaSendDocRegist t where 1=1 and t.receiveTime >= ? and ? >= t.receiveTime and t.toRoom = ?";
		Object num = null;	//存放结果数
		Session session = sessionFactory.getCurrentSession();
		calendar.setTime(date);
		if(selectType!=null){
			if("0".equals(selectType)){
				for(TempPo tempPo : orgList){				
					num = getRegistDocsByDay(session, sql, tempPo.getId());
					map.put(tempPo.getId(), num);				
				}
			}else if("1".equals(selectType)){
				for(TempPo tempPo : orgList){
					num = getRegistDocsByWeek(session, calendar, sql, tempPo.getId());
					map.put(tempPo.getId(), num);
				}
			}else if("2".equals(selectType)){
				for(TempPo tempPo : orgList){
					num = getRegistDocsByMonth(session, calendar, sql, tempPo.getId());
					map.put(tempPo.getId(), num);
				}
			}else if("3".equals(selectType)){
				for(TempPo tempPo : orgList){
					num = getRegistDocsByYear(session, calendar, sql, tempPo.getId());
					map.put(tempPo.getId(), num);
				}
			}
		}				
		return map;
	}
	
	public Page<ToaSendDocRegist> getRegistDocsBySearch(Page<ToaSendDocRegist> page, String searchDocTitle, String searchSend, String searchRoom, 
			String searchDocCode, String searchSecret, Date searchStaTime, Date searchEndTime,
			Date searchSendStaTime, Date searchSendEndTime){
		Object[] obj = new Object[10];
		int i = 0;
		StringBuffer hqlSb = new StringBuffer();
		hqlSb.append("from ToaSendDocRegist t where 1=1");
		if(searchDocTitle!=null && !"".equals(searchDocTitle)){
			hqlSb.append(" and t.docTitle like ?");
			if(searchDocTitle.indexOf("%")>-1){						//处理%号
				searchDocTitle = searchDocTitle.replaceAll("%", "/%");
				hqlSb.append(" ESCAPE '/'");
			}
			obj[i] = "%" + searchDocTitle + "%";
			i++;
		}
		if(searchSend!=null && !"".equals(searchSend)){
			hqlSb.append(" and t.send like ?");
			if(searchSend.indexOf("%")>-1){
				searchSend = searchSend.replace("%", "/%");
				hqlSb.append(" ESCAPE '/'");
			}
			obj[i] = "%" + searchSend + "%";
			i++;
		}
		if(searchRoom!=null && !"".equals(searchRoom)){
			hqlSb.append(" and t.toRoomName like ?");
			obj[i] = "%" + searchRoom + "%";
			i++;
		}else{
			List<TempPo> orgs = getOrgList("wjtj");
			String sqlPart = "";
			if(!orgs.isEmpty()){	
				for(TempPo org : orgs){
					String orgId = org.getId();
					sqlPart = sqlPart + ",'" + orgId + "'";
				}
				sqlPart = " and t.toRoom in (" + sqlPart.substring(1) + ")";
			}else{
				sqlPart = " and t.toRoom in ('" + sqlPart + "')";
			}
			hqlSb.append(sqlPart);
		}
		if(searchDocCode!=null && !"".equals(searchDocCode)){
			hqlSb.append(" and t.docCode like ?");
			if(searchDocCode.indexOf("%")>-1){
				searchDocCode = searchDocCode.replace("%", "/%");
				hqlSb.append(" ESCAPE '/'");
			}
			obj[i] = "%" + searchDocCode + "%";
			i++;
		}
		if(searchSecret!=null && !"".equals(searchSecret)){
			hqlSb.append(" and t.secret like ?");
			obj[i] = "%" + searchSecret + "%";
			i++;
		}
		if(searchStaTime!=null && !"".equals(searchStaTime)){
			searchStaTime.setHours(0);
			searchStaTime.setMinutes(0);
			searchStaTime.setSeconds(0);
			hqlSb.append(" and t.receiveTime >= ?");
			obj[i] = searchStaTime;
			i++;
		}
		if(searchEndTime!=null && !"".equals(searchEndTime)){
			searchEndTime.setHours(23);
			searchEndTime.setMinutes(59);
			searchEndTime.setSeconds(59);
			hqlSb.append(" and ? >= t.receiveTime");
			obj[i] = searchEndTime;
			i++;
		}
		if(searchSendStaTime!=null && !"".equals(searchSendStaTime)){
			searchSendStaTime.setHours(0);
			searchSendStaTime.setMinutes(0);
			searchSendStaTime.setSeconds(0);
			hqlSb.append(" and t.sendTime >= ?");
			obj[i] = searchSendStaTime;
			i++;
		}
		if(searchSendEndTime!=null && !"".equals(searchSendEndTime)){
			searchSendEndTime.setHours(23);
			searchSendEndTime.setMinutes(59);
			searchSendEndTime.setSeconds(59);
			hqlSb.append(" and ? >= t.sendTime");
			obj[i] = searchSendEndTime;
			i++;
		}
		hqlSb.append(" order by t.receiveTime DESC ");
		String hql = hqlSb.toString();
		Object[] param = new Object[i];
		for (int k = 0, t = 0; k < obj.length; k++) {
			if (obj[k] != null) {
				param[t] = obj[k];
				t++;
			}
		}		
		return registDocDAO.find(page, hql, param);
	}
	
	public Page<ToaSendDocRegist> getRegistDocs(Page<ToaSendDocRegist> page, String selectType, String roomId){
		Date sDate = new Date();
		Date eDate = new Date();
		if("0".equals(selectType)){
			startDate(sDate);
			endDate(eDate);
		}else if("1".equals(selectType)){
			calendar.setTime(sDate);
			if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
				date.setTime(date.getTime()-24*60*60*1000);
				calendar.setTime(date);
			}
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			sDate = calendar.getTime();
			startDate(sDate);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
			eDate = calendar.getTime();
			endDate(eDate);
		}else if("2".equals(selectType)){
			calendar.setTime(sDate);
			calendar.set(Calendar.DATE,1);
			sDate = calendar.getTime();
			startDate(sDate);
			calendar.add(Calendar.MONDAY, 1);
			calendar.add(Calendar.DATE, -1);
			eDate = calendar.getTime();
			endDate(eDate);
		}else if("3".equals(selectType)){
			calendar.setTime(sDate);
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			sDate = calendar.getTime();
			startDate(sDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String end = calendar.get(Calendar.YEAR)+"-12-31";
			try {
				eDate = sdf.parse(end);
				endDate(eDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}		
		}		
		Object[] obj = new Object[4];
		int i = 0;
		StringBuffer hqlSb = new StringBuffer();
		hqlSb.append("from ToaSendDocRegist t where 1=1");
		if(sDate!=null && !"".equals(sDate)){
			hqlSb.append(" and t.receiveTime >= ?");
			obj[i] = sDate;
			i++;
		}
		if(eDate!=null && !"".equals(eDate)){		
			hqlSb.append(" and ? >= t.receiveTime");
			obj[i] = eDate;
			i++;
		}
		if(roomId!=null && !"".equals(roomId)){
			hqlSb.append(" and t.toRoom like ?");
			obj[i] = "%" + roomId + "%";
			i++;
		}else{
			List<TempPo> orgs = getOrgList("wjtj");
			String sqlPart = "";
			if(!orgs.isEmpty()){	
				for(TempPo org : orgs){
					String orgId = org.getId();
					sqlPart = sqlPart + ",'" + orgId + "'";
				}
				sqlPart = " and t.toRoom in (" + sqlPart.substring(1) + ")";
			}else{
				sqlPart = " and t.toRoom in ('" + sqlPart + "')";
			}
			hqlSb.append(sqlPart);
		}
		hqlSb.append(" order by t.receiveTime DESC ");
		String hql = hqlSb.toString();
		Object[] param = new Object[i];
		for (int k = 0, t = 0; k < obj.length; k++) {
			if (obj[k] != null) {
				param[t] = obj[k];
				t++;
			}
		}		
		return registDocDAO.find(page, hql, param);
	}
	
	public String getRoomNameByRoomId(String roomId){
		return userservice.getOrgInfoByOrgId(roomId).getOrgName();
	}
	
	public List<TempPo> orgtree(String moduletype) throws Exception{
		return sendDocRegistManager.getOrgList(moduletype);
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
	
	public SendDocRegistManager getSendDocRegistManager() {
		return sendDocRegistManager;
	}

	@Autowired
	public void setSendDocRegistManager(SendDocRegistManager sendDocRegistManager) {
		this.sendDocRegistManager = sendDocRegistManager;
	}
}
