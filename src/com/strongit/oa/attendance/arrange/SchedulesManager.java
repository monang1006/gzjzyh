package com.strongit.oa.attendance.arrange;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAttendRegulation;
import com.strongit.oa.bo.ToaAttendSchedule;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Nov 9, 2009 8:45:19 AM
 * @version  2.0.4
 * @comment  班次manager
 */
@Service
@Transactional
public class SchedulesManager {
	
	private GenericDAOHibernate<ToaAttendSchedule, String> schedulesDao;

	public SchedulesManager() {

	}

	/*
	 * Description:根据查询条件查询班次信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 9, 2009 8:45:01 AM
	 */
	@Transactional(readOnly = true)
	public Page<ToaAttendSchedule> getSchedules(Page<ToaAttendSchedule> page,ToaAttendSchedule obj,String groupId)throws SystemException,ServiceException{
		try{
			List<Object> objlist = new ArrayList<Object>();
			StringBuffer hql=new StringBuffer("from ToaAttendSchedule t where 1=1");
			if(groupId!=null){
				hql.append(" and t.toaAttendSchedGroup.groupId=?");
				objlist.add(groupId);
			}
			if(obj.getSchedulesName()!=null&&!"".equals(obj.getSchedulesName())){
				hql.append(" and t.schedulesName like ?");
				objlist.add("%"+obj.getSchedulesName()+"%");
			}
			if(obj.getWorkStime()!=null&&!"".equals(obj.getWorkStime())){
				hql.append(" and t.workStime >= ?");
				objlist.add(obj.getWorkStime());
			}
			if(obj.getWorkEtime()!=null&&!"".equals(obj.getWorkEtime())){
				hql.append(" and t.workEtime <=?");
				objlist.add(obj.getWorkEtime());
			}
			hql.append(" order by t.schedulesOrder asc,t.operateDate desc");
			
			Object[] objs=new Object[objlist.size()];
		    for(int i=0;i<objlist.size();i++){
		    	objs[i]=objlist.get(i);
		    }
		    
			page= schedulesDao.find(page,hql.toString(),objs);
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"班次信息列表"});
		}	
	}
	

	/*
	 * Description:根据主键ID获取班次对象
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 9, 2009 8:44:43 AM
	 */
	public ToaAttendSchedule getSchedulesById(String schedulesId)throws SystemException,ServiceException{
		try{
			String hql="from ToaAttendSchedule t where t.schedulesId=?";
			List<ToaAttendSchedule> list=schedulesDao.find(hql, schedulesId);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}else{
				return null;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"查找班次对象"});
		}
	}
	
	/*
	 * Description:根据班次ID删除班次对象
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 9, 2009 8:45:52 AM
	 */	
	public String deleteSchedules(String schedulesId)throws SystemException,ServiceException{
		String msg="";
		try{
			schedulesDao.delete(schedulesId);
		}catch(ServiceException e){
			msg="删除班次失败！";
			throw new ServiceException(MessagesConst.del_error,new Object[]{"删除班次对象"});
		}
		return msg;
	}
	
	/*
	 * Description:保存班组信息
	 * param: 	   
	 * @author 	    彭小青
	 * @date 	    Nov 9, 2009 8:47:00 AM
	 */
	public String saveSchedules(ToaAttendSchedule obj)throws SystemException,ServiceException{
		String msg="";
		try{	
			obj.setOperateDate(new Date());
			schedulesDao.save(obj);
		}catch(ServiceException e){
			msg="保存班次失败！";
			throw new ServiceException(MessagesConst.save_error,new Object[]{"保存班次对象"});
		}
		return msg;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 19, 2009 7:17:24 PM
	 * Desc:    构造班次和该班次对应的天数的一个二维数组
	 * param:
	 */
	public String[][] getSchedulesByGroupId(String groupId)throws SystemException,ServiceException{
		try{
			String arr[][]=new String[2][4];
			String hql="from ToaAttendSchedule t where t.toaAttendSchedGroup.groupId=? order by t.schedulesOrder";
			List<ToaAttendSchedule> list=schedulesDao.find(hql, groupId);
			ToaAttendRegulation reg=null;//轮班规则
			int k=0;
			for(ToaAttendSchedule schedule:list){
				arr[0][k]=schedule.getSchedulesId();	//设置班次
				arr[1][k]="0";
				Set regSet=schedule.getToaAttendRegulations();	//获取轮班规则集合
				Iterator regit=regSet.iterator();				//遍历轮班规则
				if(regit.hasNext()){
					reg=(ToaAttendRegulation)regit.next();			//获取第一个轮班规则
					arr[1][k]=reg.getContiguoousDays().toString();	//设置班次对应的天数
				}	
				k++;
			}
			return arr;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"构造班次和该班次对应的天数的一个二维数组"});
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 23, 2009 4:34:49 PM
	 * Desc:	获取班组下的班次信息列表
	 * param:
	 */
	public List<ToaAttendSchedule> getSchedulelistByGroupId(String groupId)throws SystemException,ServiceException{
		try{
			String hql="from ToaAttendSchedule t where t.toaAttendSchedGroup.groupId=? order by t.schedulesOrder,t.workStime";
			List<ToaAttendSchedule> list=schedulesDao.find(hql, groupId);
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"获取班组下的班次信息列表"});
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		schedulesDao = new GenericDAOHibernate<ToaAttendSchedule, String>(sessionFactory,ToaAttendSchedule.class);
	}
}
