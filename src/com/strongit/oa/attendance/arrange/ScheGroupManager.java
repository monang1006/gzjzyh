package com.strongit.oa.attendance.arrange;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAttendArrange;
import com.strongit.oa.bo.ToaAttendSchedGroup;
import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.bo.ToaPersonPrivil;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.oa.personnel.personorg.PersonOrgManager;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Nov 6, 2009 10:04:02 AM
 * @version  2.0.4
 * @comment  班组manager
 */
@Service
@Transactional
public class ScheGroupManager {
	
	private GenericDAOHibernate<ToaAttendSchedGroup, String> scheGroupDao;	//班组DAO
	private GenericDAOHibernate<ToaAttendArrange, String> arrangeDao;		//排班DAO
	 private GenericDAOHibernate<ToaPersonPrivil, java.lang.String> personPrivilDao;
	@Autowired private PersonManager manager;			//人员manager
	@Autowired private PersonOrgManager orgManager;	//机构manager
	private final static String ZERO="0";
	private final static String TXRY="ff8080812015288501201792ab090061"; //退休人员

	public ScheGroupManager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		scheGroupDao = new GenericDAOHibernate<ToaAttendSchedGroup, String>(sessionFactory,ToaAttendSchedGroup.class);
		arrangeDao = new GenericDAOHibernate<ToaAttendArrange, String>(sessionFactory,ToaAttendArrange.class);
	    personPrivilDao=new GenericDAOHibernate<ToaPersonPrivil, java.lang.String>(sessionFactory,ToaPersonPrivil.class);
	}
	
	/*
	 * Description:根据查询条件查询班组信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 6, 2009 10:57:06 AM
	 */
	@Transactional(readOnly = true)
	public Page<ToaAttendSchedGroup> getScheGroup(Page<ToaAttendSchedGroup> page,ToaAttendSchedGroup obj)throws SystemException,ServiceException{
		try{
			List<Object> objlist = new ArrayList<Object>();
			StringBuffer hql=new StringBuffer("from ToaAttendSchedGroup t where 1=1");
			if(obj.getGroupName()!=null&&!"".equals(obj.getGroupName())){			//班组名称
				hql.append(" and t.groupName like ?");
				objlist.add("%"+obj.getGroupName()+"%");
			}
			if(obj.getLogo()!=null&&!"".equals(obj.getLogo())){						//是否需倒班
				hql.append(" and t.logo = ?");
				objlist.add(obj.getLogo());
			}
			if(obj.getGroupStime()!=null&&!"".equals(obj.getGroupStime())){			//有效时间
				hql.append(" and t.groupStime >= ?");
				objlist.add(obj.getGroupStime());
			}
			if(obj.getGroupEtime()!=null&&!"".equals(obj.getGroupEtime())){			//失效时间
				hql.append(" and t.groupEtime <= ?");
				obj.getGroupEtime().setHours(23);
				obj.getGroupEtime().setMinutes(59);
				obj.getGroupEtime().setSeconds(59);
				objlist.add(obj.getGroupEtime());
			}
			if(obj.getGroupDesc()!=null&&!"".equals(obj.getGroupDesc())){			//备注
				hql.append(" and t.groupDesc like ?");
				objlist.add("%"+obj.getGroupDesc()+"%");
			}
			hql.append(" order by t.operateDate desc");
			
			Object[] objs=new Object[objlist.size()];
		    for(int i=0;i<objlist.size();i++){
		    	objs[i]=objlist.get(i);
		    }
		    
			page= scheGroupDao.find(page,hql.toString(),objs);
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"班组信息列表"});
		}	
	}
	
	/*
	 * Description:根据主键ID获取班组对象
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 6, 2009 3:15:08 PM
	 */
	public ToaAttendSchedGroup getScheGroupById(String groupId)throws SystemException,ServiceException{
		try{
			String hql="from ToaAttendSchedGroup t where t.groupId=?";
			List<ToaAttendSchedGroup> list=scheGroupDao.find(hql, groupId);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}else{
				return null;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"查找班组对象"});
		}
	}
	
	/*
	 * Description:根据班组ID删除班组对象
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 6, 2009 3:23:27 PM
	 */
	public String deleteScheGroup(String groupId)throws SystemException,ServiceException{
		String msg="";
		try{
			scheGroupDao.delete(groupId);
			this.delArrangeListByGroupId(groupId,null);
		}catch(ServiceException e){
			msg="删除班组失败！";
			throw new ServiceException(MessagesConst.del_error,new Object[]{"删除班组对象"});
		}
		return msg;
	}
	
	/*
	 * Description:保存班组信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 6, 2009 4:22:48 PM
	 */
	public String saveScheGroup(ToaAttendSchedGroup obj)throws SystemException,ServiceException{
		String msg="";
		try{
			obj.setOperateDate(new Date());
			scheGroupDao.save(obj);
		}catch(ServiceException e){
			msg="保存班组失败！";
			throw new ServiceException(MessagesConst.save_error,new Object[]{"删除班组对象"});
		}
		return msg;
	}
	
	/*
	 * Description:查找该班组已排班的某个单位的人员列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 12, 2009 1:50:12 PM
	 */
	public void getArrangePerson(HttpServletRequest request,String orgId,String scheGroupId)throws SystemException, ServiceException {
		List<ToaBasePerson> arrangePerList = new ArrayList<ToaBasePerson>();
		List<ToaBasePerson> allPerList = new ArrayList<ToaBasePerson>();
		List<ToaBasePerson> notArrangePerList = new ArrayList<ToaBasePerson>();
		ToaBasePerson person;
		String personids="";
		try {
			if(orgId!=null&&!"".equals(orgId)){
				allPerList=manager.getEffectivPersonByOrg(orgId);//获取单位所有人员列表
				StringBuffer sql = new StringBuffer("");
				//构造sql语句，查询出该班组已排班的人员
				sql.append("select t.personid,t.person_name from T_OA_ATTEND_ARRANGE a,t_oa_base_person t ")
					.append(" where t.org_id ='")
					.append(orgId)
					.append("' and t.person_isdel='")
					.append(ZERO)
					.append("' and t.person_person_kind<>'")
					.append(TXRY)
					.append("' and a.user_id = t.personid ")
					.append("  and a.group_id='")
					.append(scheGroupId)
					.append("' order by t.person_name asc");
				ResultSet rs=scheGroupDao.executeJdbcQuery(sql.toString());
				if(rs!=null){
					while(rs.next()){
						person=new ToaBasePerson();
						person.setPersonid(rs.getString(1));
						person.setPersonName(rs.getString(2));
						arrangePerList.add(person);
						personids+=","+rs.getString(1);
					}
				}
				//获取没有排班的人员列表
				for(int i=0;allPerList!=null&&i<allPerList.size();i++){
					person=allPerList.get(i);
					if(personids.indexOf(person.getPersonid())==-1){
						notArrangePerList.add(person);
					}	
				}
				
			}
			request.setAttribute("arrangePerList", arrangePerList);
			request.setAttribute("notArrangePerList", notArrangePerList);
		}catch (SQLException e){
			e.printStackTrace();
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "查找该班组已排班的某个单位的人员列表" });
		}
	}
	
	/*
	 * Description:保存人员排班
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 13, 2009 10:45:22 AM
	 */
	public String saveArrange(String fld_str,String fld_str_pre,String groupId)throws SystemException, ServiceException{
		String msg="";
		try{
			if(!"".equals(fld_str_pre)){
				String fld_str_pres[]=fld_str_pre.split(",");		//可选人员
				for(int i=0;i<fld_str_pres.length;i++){
					this.delArrangeListByPG(fld_str_pres[i], groupId);
				}
			}
			if(!"".equals(fld_str)){
				String fld_strs[]=fld_str.split(",");				//已选人员
				for(int i=0;i<fld_strs.length;i++){
					this.saveArrangeListByPG(fld_strs[i], groupId,null);
				}
			}
		}catch(ServiceException e){
			msg="保存排班失败！";
			throw new ServiceException(MessagesConst.save_error,new Object[]{"保存排班失败"});
		}	
		return msg;
	}
	
	/*
	 * Description:根据人员ID和班组ID删除排班对象
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 13, 2009 11:36:07 AM
	 */
	public void delArrangeListByPG(String personId,String groupId)throws SystemException, ServiceException{
		try{
			List<ToaAttendArrange> list=this.getArrangeListByGroupOrUser(groupId, personId,null);
			arrangeDao.delete(list);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,new Object[]{"删除某人员在该班组的排班"});
		}	
	}
	
	/*
	 * Description:保存某人员在该班组的排班
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 13, 2009 11:48:02 AM
	 */
	public void saveArrangeListByPG(String personId,String groupId,String startSchedules)throws SystemException, ServiceException{
		try{
			List<ToaAttendArrange> list=null;
			if(startSchedules!=null){	//如果起始班次不为空
				this.delArrangeListByGroupIdAndUser(groupId, personId);//删除该人员在该班组的其他排班
			}else{					
				list=this.getArrangeListByGroupOrUser(groupId, personId,startSchedules);//获取该人员在该班组的排班，并且起始班次为startSchedules
			}
			if(list!=null&&list.size()>0){
				
			}else{	//如果没有找到该人员在该班组的排班，则新增排班信息
				ToaAttendArrange arrange=new ToaAttendArrange();
				arrange.setUserId(personId);
				arrange.setGroupId(groupId);
				arrange.setStartSchedules(startSchedules);
				arrangeDao.save(arrange);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,new Object[]{"删除某人员在该班组的排班"});
		}	
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 16, 2009 11:29:55 AM
	 * Desc:	获取某班组的排班信息列表或某人员的排班信息列表
	 * param:
	 */
	public List<ToaAttendArrange> getArrangeListByGroupOrUser(String groupId,String userId,String startSchedules)throws SystemException, ServiceException{
		try{
			String hql="from ToaAttendArrange  t where 1=1";
			if(groupId!=null&&!"".equals(groupId)){
				hql+=" and t.groupId='"+groupId+"'";
			}
			if(userId!=null&&!"".equals(userId)){
				hql+=" and t.userId='"+userId+"'";
			}
			if(startSchedules!=null&&!"".equals(startSchedules)){
				hql+=" and t.startSchedules='"+startSchedules+"'";
			}
			List<ToaAttendArrange> list=arrangeDao.find(hql);
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"获取排班信息列表"});
		}
	}
	
	/*
	 * Description:获取某个班组下所有已排班的人员id串
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 13, 2009 10:55:22 AM
	 */
	public String getArrangePerByGroup(String groupId,String startSchedules)throws SystemException, ServiceException{
		String personids="";
		try{
			StringBuffer sql = new StringBuffer("");
			//构造sql语句，查询出该班组已排班的人员
			sql.append("select t.personid,t.person_name from T_OA_ATTEND_ARRANGE a,t_oa_base_person t ")
			.append(" where t.person_isdel='")
			.append(ZERO)
			.append("' and (t.person_person_kind is null or ( ")
			.append(" t.person_person_kind is not null and t.person_person_kind<>'")
			.append(TXRY)
			.append("')) and a.user_id = t.personid ")
			.append("  and a.group_id='")
			.append(groupId)
			.append("'");
			if(startSchedules!=null&&!"".equals(startSchedules)){	//如果起始班次不为空
				sql.append(" and a.start_schedules='")
				.append(startSchedules)
				.append("'");
			}
			sql.append(" order by t.org_id, t.person_name");		//按机构和人员姓名排序
			ResultSet rs=scheGroupDao.executeJdbcQuery(sql.toString());
			if(rs!=null){
				while(rs.next()){
					personids+=","+rs.getString(1)+"|"+rs.getString(2);
				}
			}
			if(personids.length()>0){
				personids=personids.substring(1);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "查找某班组下所有已排班的人员" });
		}
		return personids;
	}
	
	/*
	 * Description:得到所有组织机构及其下人员信息列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 13, 2009 3:37:48 PM
	 */
	public List<Object[]> getAllOrgUserList(String orgId)throws SystemException, ServiceException{
		Long s=new Date().getTime();
		String parentOrgId;
		List<Object[]> returnList = new ArrayList<Object[]>();
		List<String[]> orgUserList;
		int index=0;
		int k=0;	//累加器
		try{
			List<ToaBaseOrg> orgList=null;
			List<ToaBasePerson> userList=new ArrayList<ToaBasePerson>();
			if(orgId!=null&&!"".equals(orgId)){
				orgList = orgManager.getOrgListByID(orgId);
				userList =manager.getEffectivPersonByOrg(orgId);
			}else{
				orgList = orgManager.getOrgsByIsdel("0");
				userList =this.getAllPersons();
			}
			if(orgList != null && !orgList.isEmpty()){
				for(ToaBaseOrg org : orgList){
					orgUserList = new ArrayList<String[]>();
					ToaBasePerson user;
					index=0;
					k=0;
					for(int i=0;i<userList.size();i++){
						user = userList.get(i);
						if(org.getOrgid().equals(user.getBaseOrg().getOrgid())){
							if(k==0){
								index=i;		
							}
							k++;
							orgUserList.add(new String[]{user.getPersonid(), user.getPersonName()});
							continue;
						}
					}
					while(k>0){
						userList.remove(index);
						k--;
					}
					/**
					 * 如果此组织机构已是最顶级，则其父级Id为”0“
					*/
					if(orgList.size()==1){
						parentOrgId = "0";
					}else{
						parentOrgId = this.getParentDepart(org.getOrgid(),org.getOrgSyscode());
						if(org.getOrgid().equals(parentOrgId)){
							parentOrgId = "0";
						}	
					}
					returnList.add(new Object[]{org.getOrgid(), parentOrgId
							, org.getOrgName(), orgUserList});
				}
			}
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"得到所有组织机构及其下人员信息列表"});
		}	
		Long e=new Date().getTime();
		System.out.println(e-s);
		return returnList;
	}
	
	/*
	 * Description:得到所有组织机构信息列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 14, 2009 10:40:48 AM
	 */
	public List<Object[]> getAllOrgList()throws SystemException, ServiceException{
		String parentOrgId;
		List<Object[]> returnList = new ArrayList<Object[]>();
		try{
			List<ToaBaseOrg> orgList = orgManager.getOrgsByIsdel("0");
			if(orgList != null && !orgList.isEmpty()){
				for(ToaBaseOrg org : orgList){
					parentOrgId = this.getParentDepart(org.getOrgid(),org.getOrgSyscode());
					if(org.getOrgid().equals(parentOrgId)){
						parentOrgId = "0";
					}			
					returnList.add(new Object[]{org.getOrgid(), parentOrgId
							, org.getOrgName()});
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"得到所有组织机构信息列表"});
		}	
		return returnList;
	}

	/*
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 13, 2009 3:06:18 PM
	 */
	public String getParentDepart(String orgId,String orgCode){
		ToaBaseOrg org =orgManager.getParentOrgByOrgSyscode(orgCode);
		if(org == null){
			return orgId;
		}else{
			return org.getOrgid();
		}
	}
	
	/*
	 * Description:保存人员排班信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 14, 2009 11:16:09 AM
	 */
	public String saveArrange1(String perOps,String groupId,String startSchedules)throws SystemException, ServiceException{
		String msg="";
		try{
			this.delArrangeListByGroupId(groupId,startSchedules);	//删除某班组的排班，或删除某班组中起始班次为某特定班次的排班信息
			if(perOps!=null&&!"".equals(perOps)){
				String perOps1[]=perOps.split(",");		//可选人员
				for(int i=0;i<perOps1.length;i++){
					this.saveArrangeListByPG(perOps1[i],groupId,startSchedules);	//保存最新的人员排班情况
				}
			}
		}catch(ServiceException e){
			msg="保存排班失败！";
			throw new ServiceException(MessagesConst.save_error,new Object[]{"保存人员排班"});
		}	
		return msg;
	}
	
	/*
	 * 
	 * Description:删除某班组中起始班次为某特定班次的排班信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 14, 2009 11:18:07 AM
	 */
	public void delArrangeListByGroupId(String groupId,String startSchedules)throws SystemException, ServiceException{
		try{
			List<ToaAttendArrange> list=this.getArrangeListByGroupOrUser(groupId, null, startSchedules);
			arrangeDao.delete(list);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,new Object[]{"删除某班组中起始班次为某特定班次的排班信息"});
		}	
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 16, 2009 3:36:51 PM
	 * Desc:    删除该人员在该班组的排班
	 * param:
	 */
	public void delArrangeListByGroupIdAndUser(String groupId,String userId)throws SystemException, ServiceException{
		try{
			List<ToaAttendArrange> list=this.getArrangeListByGroupOrUser(groupId, userId, null);
			arrangeDao.delete(list);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,new Object[]{"删除该人员在该班组的排班"});
		}	
	}

	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 16, 2009 11:29:55 AM
	 * Desc:	获取某人员的班组信息列表
	 * param:
	 */
	public List<ToaAttendSchedGroup> getEffectiveGroupByUser(String userId,Date date)throws SystemException, ServiceException{
 		List<ToaAttendSchedGroup> groupIdlist=new ArrayList<ToaAttendSchedGroup>();
		try{	
			String sql="select distinct(f) from ToaAttendArrange t,ToaAttendSchedGroup f" +
					" where f.groupId= t.groupId and t.userId=? and f.groupStime<=?" +
					" and (f.groupEtime is null or f.groupEtime>=?)";
			groupIdlist = scheGroupDao.find(sql,userId,date,date);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"获取某人员的班组信息列表"});
		}
		return groupIdlist;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Dec 15, 2009 3:17:38 PM
	 * Desc: 	当前用户当天上的班是否有倒班
	 * param:
	 */
	public boolean isAlternate(String userId,Date date)throws SystemException, ServiceException{
 		List<ToaAttendArrange> arrngelist=new ArrayList<ToaAttendArrange>();
		try{	
			String sql="select t from ToaAttendArrange t,ToaAttendSchedGroup f" +
					" where f.groupId= t.groupId and t.userId=? and f.groupStime<=?" +
					" and (f.groupEtime is null or f.groupEtime>=?) and t.startSchedules is not null";
			arrngelist = arrangeDao.find(sql,userId,date,date);
			if(arrngelist!=null&&arrngelist.size()>0){
				return true;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"获取某人员的班组信息列表"});
		}
		return false;
	}
	
	/*
	 * 
	 * Description:获取所有人员
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 10, 2010 2:16:57 PM
	 */
	public List<ToaBasePerson> getAllPersons()throws SystemException, ServiceException{
		List<ToaBasePerson> returnList = new ArrayList<ToaBasePerson>();
		try {
			String sql=" select t.personid, t.person_name,t.org_id from t_oa_base_person t " +
			"where (t.person_isdel is null or t.person_isdel = '0') " +
			"and (t.person_person_kind is null or t.person_person_kind <> 'ff8080812015288501201792ab090061')" +
			" order by t.org_id,t.person_name";
			ResultSet rs= arrangeDao.executeJdbcQuery(sql);
			if(rs!=null){
				ToaBasePerson person;
				ToaBaseOrg baseOrg;
				while(rs.next()){
					person=new ToaBasePerson();
					person.setPersonid(rs.getString(1));
					person.setPersonName(rs.getString(2));
					baseOrg=new ToaBaseOrg();
					baseOrg.setOrgid(rs.getString(3));
					person.setBaseOrg(baseOrg);
					returnList.add(person);
				}
			}
		}catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"获取所有人员"});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnList;
	}
	
	/*
	 * 
	 * Description:获取某个人计算时间段内有效的班组信息列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 12, 2010 8:33:03 PM
	 */
	public List<ToaAttendSchedGroup> getEffectiveGroupByUserAndTime(String userId,Date calStime,Date calEtime)throws SystemException, ServiceException{
 		List<ToaAttendSchedGroup> groupIdlist=new ArrayList<ToaAttendSchedGroup>();
		try{	
			String sql="select distinct(f) from ToaAttendArrange t,ToaAttendSchedGroup f" +
					" where f.groupId= t.groupId and t.userId=? and f.groupStime<=?" +
					" and (f.groupEtime is null or f.groupEtime>=?)";
			groupIdlist = scheGroupDao.find(sql,userId,calEtime,calStime);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"获取某个人计算时间段内有效的班组信息列表"});
		}
		return groupIdlist;
	}
	
	/*
	 * 
	 * Description:查找用户的人事人员权限
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 14, 2010 5:13:09 PM
	 */
	public ToaPersonPrivil getPersonPrivil(String userId)throws SystemException,ServiceException{
		ToaPersonPrivil privil=null;
		try{
			String hql=" from ToaPersonPrivil t where t.userId=?";
			List<ToaPersonPrivil> list=personPrivilDao.find(hql, userId);
			if(list!=null&&list.size()>0){
				privil=list.get(0);
			}
			return privil;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"getPersonPrivil+查找用户的人事人员权限"}); 
		}
	}
	
	
	/*
	 * 
	 * Description:保存用户的人事人员权限信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 14, 2010 5:07:19 PM
	 */
	public String savePersonPrivil(ToaPersonPrivil privil)throws SystemException,ServiceException{
		String msg="设置成功!";
		try{
			personPrivilDao.save(privil);
		}catch(Exception e){
			msg="设置失败！";
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"savePersonPrivil+保存用户的人事人员权限信息"}); 
		}
		return msg;
	}
}
