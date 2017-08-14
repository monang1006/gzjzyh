package com.strongit.oa.attendance.arrange;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAttendRegulation;
import com.strongit.oa.bo.ToaAttendSchedGroup;
import com.strongit.oa.bo.ToaAttendSchedule;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Nov 10, 2009 4:48:08 PM
 * @version  2.0.4
 * @comment  轮班规则manager
 */
@Service
@Transactional
public class RegManager {
	
	private GenericDAOHibernate<ToaAttendRegulation, String> regDao;
	private SchedulesManager scheManager;	//班次Manager
	private ScheGroupManager groupManager;	//班组manager
	private final static String ZERO="0";

	public RegManager() {

	}
	
	/*
	 * 
	 * Description:根据班次ID获取班次轮班规则
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 10, 2009 5:04:31 PM
	 */
	public ToaAttendRegulation getRegulationByScheId(String scheId)throws SystemException,ServiceException{
		try{
			String hql="from ToaAttendRegulation t where t.toaAttendSchedule.schedulesId=?";
			List<ToaAttendRegulation> list=regDao.find(hql, scheId);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}else{
				return null;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"查找班次的轮班规则对象"});
		}
	}
	
	/*
	 * 
	 * Description:保存班次的轮班规则
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 11, 2009 3:28:21 PM
	 * 2010-04-09彭小青修改
	 */
	public String saveRegulation(ToaAttendRegulation reg,String scheId)throws SystemException,ServiceException{
		String msg="";
		try{
			if("".equals(reg.getRegId())){
				reg.setRegId(null);
			}
			ToaAttendSchedule schedules=scheManager.getSchedulesById(scheId);//获取班次对象
			reg.setToaAttendSchedule(schedules);
			if(reg.getRepeatWay()!=null&&reg.getRepeatWay().equals("0")){//按天
				reg.setWeekDays("");
				reg.setMonthDays("");
				reg.setIsContainLastDay("");
				//if(schedules.getSchedulesType().equals("0")){//如果不是休息班
					List<ToaAttendRegulation> reglist=this.getOtherRegOfGroup(schedules);//获取除当前轮班规则外的班次的轮班规则
					for(ToaAttendRegulation scheReg:reglist){	//循环
						scheReg.setRepeatStime(reg.getRepeatStime());//重新设置轮班开始时间
						scheReg.setRepeatCycle(reg.getRepeatCycle());//重新设置轮班周期
					}
					regDao.save(reglist);
				//}
			}else if(reg.getRepeatWay()!=null&&reg.getRepeatWay().equals("1")){//按周
				reg.setContiguoousDays(null);
				reg.setRepeatStime(null);
				reg.setMonthDays("");
				reg.setIsContainLastDay("");
			}else if(reg.getRepeatWay()!=null&&reg.getRepeatWay().equals("2")){//按月
				reg.setWeekDays("");
				reg.setContiguoousDays(null);
				reg.setRepeatStime(null);
			}
			regDao.save(reg);
		}catch(ServiceException e){
			msg="保存轮班规则失败！";
			throw new ServiceException(MessagesConst.save_error,new Object[]{"保存轮班规则"});
		}
		return msg;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 20, 2009 10:59:19 AM
	 * Desc:    获取一个班组中除当前班次外的其他班次的轮班规则列表
	 * param: 2010-04-09彭小青修改 (去掉了条件and t2.schedulesType<>?)
	 */
	public List<ToaAttendRegulation> getOtherRegOfGroup(ToaAttendSchedule schedules){
		try{
			String groupId=schedules.getToaAttendSchedGroup().getGroupId();
			String hql="select t3 from ToaAttendSchedGroup t,ToaAttendSchedule t2,ToaAttendRegulation t3 " +
					"where t.groupId=? and t.groupId=t2.toaAttendSchedGroup.groupId  " +
					"and t3.toaAttendSchedule.schedulesId=t2.schedulesId " +
					"and t2.schedulesId<>? ";
			List<ToaAttendRegulation> list=regDao.find(hql, groupId,schedules.getSchedulesId());
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 20, 2009 11:07:48 AM
	 * Desc:    获取某个班组中班次的所有轮班规则列表
	 * param:
	 */
	public List<ToaAttendRegulation> getAllRegOfGroup(String groupId){
		try{
			String hql="select t3 from ToaAttendSchedGroup t,ToaAttendSchedule t2,ToaAttendRegulation t3 " +
					"where t.groupId=? and t.groupId=t2.toaAttendSchedGroup.groupId  " +
					"and t3.toaAttendSchedule.schedulesId=t2.schedulesId ";
			List<ToaAttendRegulation> list=regDao.find(hql, groupId);
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 20, 2009 11:02:04 AM
	 * Desc:	班别下是否有班次，班次是否都设置了轮班规则
	 * param:
	 */
	public String isHasSetAllRegOfGroup(String groupId)throws SystemException,ServiceException{
		try{
			List<ToaAttendRegulation> list=this.getAllRegOfGroup(groupId);		//获取该班组下班次的所有轮班规则列表
			ToaAttendSchedGroup group=groupManager.getScheGroupById(groupId);	//根据班组ID获取班组对象
			if(group.getToaAttendSchedules()!=null&&group.getToaAttendSchedules().size()>0){//如果有班次
				if(list==null||group.getToaAttendSchedules().size()!=list.size()){	//如果没有轮班规则或则存在班次没有设置轮班规则
					return ZERO+ZERO;
				}else{
					return ZERO+ZERO+ZERO;
				}
			}else{
				return ZERO;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"保存轮班规则"});
		}
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		regDao = new GenericDAOHibernate<ToaAttendRegulation, String>(sessionFactory,ToaAttendRegulation.class);
	}
	
	@Autowired
	public void setScheManager(SchedulesManager scheManager) {
		this.scheManager = scheManager;
	}
	
	@Autowired
	public void setGroupManager(ScheGroupManager groupManager) {
		this.groupManager = groupManager;
	}

}
