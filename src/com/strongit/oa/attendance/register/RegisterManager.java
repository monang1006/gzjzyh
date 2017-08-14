package com.strongit.oa.attendance.register;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attendance.arrange.ScheGroupManager;
import com.strongit.oa.attendance.arrange.SchedulesManager;
import com.strongit.oa.attendance.holiday.HolidayManager;
import com.strongit.oa.bo.ToaAttendArrange;
import com.strongit.oa.bo.ToaAttendRecord;
import com.strongit.oa.bo.ToaAttendRegulation;
import com.strongit.oa.bo.ToaAttendSchedGroup;
import com.strongit.oa.bo.ToaAttendSchedule;
import com.strongit.oa.bo.ToaAttendTemp;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Nov 16, 2009 11:16:46 AM
 * @version  2.0.4
 * @comment  考勤登记Manager
 */
@Service
@Transactional
public class RegisterManager {
	
	private GenericDAOHibernate<ToaAttendRecord, String> registerDao;
	private ScheGroupManager manager;		//班组manager
	private SchedulesManager scheManager;	//班次Manager
	private PersonManager perManager;		//人员manager	
	private HolidayManager holidayManager;	//假日manager
	private final static String KUANGGONGID="402882cc250536640125053991710001";//申请类型为旷工
	private final static String FDJ="法定假";
	private final static String XIUXI="休息";
	private final static String KUANGGONG="旷工";
	private final static String NOTCALABSENCE="1";	//不统计为缺勤
	private final static String ISCAL="1";			//已计算
	private final static String ZERO="0";		
	private final static String ONE="1";
	private final static String TWO="2";
	
	public RegisterManager() {

	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		registerDao = new GenericDAOHibernate<ToaAttendRecord, String>(sessionFactory,ToaAttendRecord.class);
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 16, 2009 11:13:19 AM
	 * Desc:	根据主键获取考勤明细对象
	 * param:
	 */
	public ToaAttendRecord getAttendRecordById(String attendId)throws SystemException,ServiceException{
		try{
			String hql="from ToaAttendRecord t where t.attendId=?";
			List<ToaAttendRecord> list=registerDao.find(hql, attendId);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}else{
				return null;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"根据主键获取考勤明细对象"});
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 17, 2009 9:46:29 AM
	 * Desc:    根据查询条件查询出符合条件的考勤明细
	 * param:
	 */
	public List<ToaAttendRecord> getAttendRecordByCon(ToaAttendRecord recorde)throws SystemException,ServiceException{
		try{
			List<Object> objlist = new ArrayList<Object>();
			StringBuffer hql=new StringBuffer("from ToaAttendRecord t where t.userId=? ");
			objlist.add(recorde.getUserId());
			if(recorde.getAttendTime()!=null){
				hql.append(" and t.attendTime=?");
				objlist.add(recorde.getAttendTime());
			}
			Object[] objs = new Object[objlist.size()];
			for (int i = 0; i < objlist.size(); i++) {
				objs[i] = objlist.get(i);
			}	
			hql.append(" order by t.mregulationTime");
			List<ToaAttendRecord> list= registerDao.find(hql.toString(),objs);
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"根据查询条件查询出符合条件的考勤明细"});
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 17, 2009 10:58:29 AM
	 * Desc:    构造登记信息列表
	 * param:
	 */
	public List<ToaAttendTemp> buildRegistList(HttpServletRequest request,User user)throws SystemException,ServiceException{
		String msg="";
		List<ToaAttendTemp> list=new ArrayList<ToaAttendTemp>();	
		try{	
			Date date=this.getDateByString(new Date(),"yyyy-MM-dd");//格式化日期
			ToaAttendRecord recorde=new ToaAttendRecord();
			recorde.setAttendTime(date);
			ToaBasePerson person=perManager.getPersonByUumsPerId(user.getUserId());	//根据统一用户ID获取对应人事的人员ID
			String personId="";
			if(person!=null){	//如果该人员已导入人事中
				personId=person.getPersonid();
				recorde.setUserId(personId);
			}else{
				msg="当前用户还未导入到人事系统中！";
				request.setAttribute("msg", msg);
				return null;
			}	
			List<ToaAttendRecord> attendlist=this.getAttendRecordByCon(recorde);	//获取该人员当天的考勤明细列表
			List<ToaAttendSchedule> schedules=null;
			if(attendlist!=null&&attendlist.size()>0){	//当前用户有当天的考勤
				if(holidayManager.islegalHoliday()){	//当天是法定假日,之所以要在这里再判断一次法定假日，是因为法定假日不需要展现考勤登记列表
					msg="当天为法定假日,不需上下班登记！";
				}else{
					schedules=this.getSchedulesByAttendence(attendlist);	//获取当前用户当天的班次信息列表
				}
			}else{										
				if(holidayManager.islegalHoliday()){	//当天是法定假日
					this.saveAttendRecord(null,person,date,ZERO);			
					msg="当天为法定假日,不需上下班登记！";
				}else{
					schedules=this.getCurrentSchedulesByUser(personId,date);//获取当前用户当天的班次信息列表
					if(schedules!=null&&schedules.size()>0){	
						this.saveAttendRecord(schedules,person,date,ZERO);	//按班次保存当天的考勤明细
					}else {	
						msg="当天没有班次！";
					}	
				}
			}
			list=this.buildRegistList(schedules,person,date); 	//构造考勤登记信息列表
			request.setAttribute("msg", msg);
			request.setAttribute("list",list);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"构造登记信息列表"});
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 16, 2009 11:26:13 AM
	 * Desc:    获取当前用户某天的班次信息列表
	 * param:
	 */
	public List<ToaAttendSchedule> getSchedulesByUser(String personId,Date now)throws SystemException,ServiceException{
		List<ToaAttendSchedule> schelist=new ArrayList<ToaAttendSchedule>();
		try{
			List<ToaAttendSchedGroup> list=manager.getEffectiveGroupByUser(personId,now);	//获取人员的有效班组列表
			Set scheduleSet;			//班次列表
			Set regSet;					//轮班规则集合
			ToaAttendSchedule schedule;
			ToaAttendRegulation reg=null;	//轮班规则
			ToaAttendArrange arrange;		//排班
			for(ToaAttendSchedGroup group:list){	//循环班组列表
				scheduleSet=group.getToaAttendSchedules();	//获取班组的班次信息列表
				Iterator scheduleIt=scheduleSet.iterator();	//遍历轮班规则
				arrange=manager.getArrangeListByGroupOrUser(group.getGroupId(), personId, null).get(0);
				while(scheduleIt.hasNext()){
					schedule=(ToaAttendSchedule)scheduleIt.next();	//获取轮班规则集合
					regSet=schedule.getToaAttendRegulations();
					Iterator regit=regSet.iterator();			//遍历轮班规则
					if(regit.hasNext()){
						reg=(ToaAttendRegulation)regit.next();	//获取第一个轮班规则
					}
					if(reg!=null&&this.isIdentical(reg,arrange,schedule,now)){	//不倒班如果当天上符合这个轮班规则,则该人员当天上这趟班
						schelist.add(schedule);	
					}
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"获取当前用户当天的班次信息列表"});
		}
		return schelist;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Dec 3, 2009 11:53:52 AM
	 * Desc:
	 * param:
	 * 说明：先查找当天当前用户所在有效班组，循环班组，获取班组的班次信息，再循环班次列表
	 * ，查找班次对应的轮班规则，如果当天在轮班规则时间范围内，那么当前用户需上该班次的班
	 */
	public List<ToaAttendSchedule> getCurrentSchedulesByUser(String personId,Date now)throws SystemException,ServiceException{
		List<ToaAttendSchedule> schelist=new ArrayList<ToaAttendSchedule>();
		try{
			List<ToaAttendSchedGroup> list=manager.getEffectiveGroupByUser(personId,now);	//获取人员的有效班组列表
			List<ToaAttendSchedule>  schedulelist;	//班次列表
			ToaAttendRegulation reg=null;			//轮班规则
			ToaAttendArrange arrange;				//排班
			Set regSet;								//轮班规则集合
			for(ToaAttendSchedGroup group:list){	//循环班组列表
				schedulelist=scheManager.getSchedulelistByGroupId(group.getGroupId());	//获取班组的班次信息列表
				arrange=manager.getArrangeListByGroupOrUser(group.getGroupId(), personId, null).get(0);
				for(ToaAttendSchedule schedule:schedulelist){
					regSet=schedule.getToaAttendRegulations();	//获取轮班规则集合
					Iterator regit=regSet.iterator();			//遍历轮班规则
					if(regit.hasNext()){
						reg=(ToaAttendRegulation)regit.next();	//获取第一个轮班规则
					}
					if(reg!=null&&this.isIdentical(reg,arrange,schedule,now)){		//不倒班如果当天上符合这个轮班规则,则该人员当天上这趟班
						schelist.add(schedule);	
					}
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"获取当前用户当天的班次信息列表"});
		}
		return schelist;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 16, 2009 4:47:35 PM
	 * Desc:	判断某天是否符合轮班规则
	 * param:
	 */
	public boolean isIdentical(ToaAttendRegulation reg,ToaAttendArrange arrange,ToaAttendSchedule schedule,Date now){
		String rangeWay=reg.getRangeWay();//规则重复范围方式
		if(rangeWay.equals("0")){	//按时间段
			if(now.before(reg.getRegStime())){	//当天早于规则重复开始时间
				return false;
			}else if(reg.getRegEtime()!=null&&now.after(reg.getRegEtime())){//当天晚于规则重复结束时间
				return false;
			}
		}else{						//按月份			
			int month=now.getMonth()+1;
			if(reg.getRegSmonth()<=reg.getRegEmonth()){//如果起始月份小于等于结束月份
				if(month<reg.getRegSmonth()){		//当天早于规则重复开始月份
					return false;
				}else if(month>reg.getRegEmonth()){	//当天晚于规则重复结束月份
					return false;
				}
			}else{
				if((month>=reg.getRegSmonth()&&month<=12)||(month>=1&&month<=reg.getRegEmonth())){			
				}else{
					return false;
				}
			}
		}
		String repeatWay=reg.getRepeatWay();	//规则重复方式
		if(repeatWay.equals(ZERO)){		//按日，一定是倒班
			if(!isIdenticalByDay(now,reg, arrange, schedule)){
				return false;
			}
		}else if(repeatWay.equals(ONE)){//按周
			if(!isIdenticalByWeek(now,reg.getWeekDays())){
				return false;
			}
		}else if(repeatWay.equals(TWO)){//按月
			if(!isIdenticalByMonth(now,reg.getMonthDays(),reg.getIsContainLastDay())){
				return false;
			}	
		}
		return true;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 19, 2009 7:40:45 PM
	 * Desc:    规则重复方式按日时，判断某天是否在设定的星期中
	 * param:
	 */
	public boolean isIdenticalByDay(Date now,ToaAttendRegulation reg,ToaAttendArrange arrange,ToaAttendSchedule schedule){
		Date repeatStime=reg.getRepeatStime();//轮班开始时间
		Integer cycle=reg.getRepeatCycle();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		sdf.format(repeatStime);
		Calendar repeat=sdf.getCalendar();
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
		sdf1.format(now);
		Calendar now1=sdf1.getCalendar();	
		if(now1.before(repeat)){	//如果当天早于规则开始时间
			return false;
		}	
		Integer days=(now1.get(Calendar.DAY_OF_YEAR)-repeat.get(Calendar.DAY_OF_YEAR))%cycle+1;//计算当天是该周期的第几天
		Integer count=0;		//统计天数
		String scheduleId="";	//按轮班规则计算出的班次ID
		String[][] arr=scheManager.getSchedulesByGroupId(arrange.getGroupId());//2行n列的数组,第一行存放班次ID,第二行存放连续天数，每一列都是对应的，即班次对应天数
		int k=0;		//起始班次所在数组的列数
		for(int i=0;i<arr[0].length;i++){//循环列
			if(arr[0][i].equals(arrange.getStartSchedules())){//如果是起始班次
				k=i;	//得到起始班次所在数组的列数
				break;
			}
		}
		for(int j=k;j<arr[1].length;j++){		//从起始班次所在列开始循环
			count+=Integer.parseInt(arr[1][j]);	//获取对应的天数并相加
			if(count>=days){		
				scheduleId=arr[0][j];
				break;
			}
			if(j==arr[1].length-1){//如果该列是最后一列
				j=0;//重新循环
			}
		}
		if(scheduleId.equals(schedule.getSchedulesId())){//如果按轮班规则计算出的班次和该班次相同
			return true;
		}else{
			return false;
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 17, 2009 9:21:12 AM
	 * Desc:    规则重复方式按周时，判断某天是否在设定的星期中
	 * param:
	 */
	public boolean isIdenticalByWeek(Date now,String weekDays){
		Calendar cal=Calendar.getInstance();
        cal.setTime(now);          
        int w=cal.get(Calendar.DAY_OF_WEEK)-1;
        if(w==0)w=7;
        if(weekDays.indexOf(String.valueOf(w))==-1){		//如果当天不在设定的星期里，则返回false
        	return false;
        }else{
        	return true;
        }
	} 
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 17, 2009 9:24:25 AM
	 * Desc:    规则重复方式按月时，判断某天是否在设定的号中 monthDays的数据如1,3,5-7 
	 * param:
	 */
	public boolean isIdenticalByMonth(Date now,String monthDays,String isContainLastDay){
		int day=now.getDay();
		Calendar cal = Calendar.getInstance();   
		int  maxDate = cal.getActualMaximum(Calendar.DATE);   
        String[] monthDays1=monthDays.split(",");
        for(String setday:monthDays1){	//循环设定的号
        	if(setday.indexOf("-")!=-1){//如果存在‘-’
        		String[] setdays=setday.split("-");
        		if(day>=Integer.parseInt(setdays[0])&&day<=Integer.parseInt(setdays[1])){//判断当天是否在这个号之间
        			return true;
        		}
        	}else{
        		if(day==Integer.parseInt(setday)){
        			return true;
        		}
        	}
        }
        if(isContainLastDay.equals(ONE)&&maxDate==day){
        	return true;
        }
        return false;
	} 

	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 17, 2009 3:01:41 PM
	 * Desc:    判断当前用户的班次是否有冲突
	 * param:
	 */
	public boolean isConflictSchedules(List<ToaAttendSchedule> schedules){
		
		return false;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 16, 2009 11:13:19 AM
	 * Desc:	保存考勤记录
	 * param:
	 */
	public void saveAttendRecord(List<ToaAttendSchedule> schedules,ToaBasePerson person,Date whichDay,String flag)throws SystemException,ServiceException{
		try{
			ToaAttendSchedule schedule;	//班次对象
			if(schedules!=null&&schedules.size()>0){//如果当天为正常上班
				for(int i=0;i<schedules.size();i++){//循环班次列表
					schedule=schedules.get(i);
					String workstime= schedule.getWorkStime();		//上班时间
					String worketime=schedule.getWorkEtime();		//下班时间
					ToaAttendRecord record=new ToaAttendRecord();	//新增一个对象
					record.setUserId(person.getPersonid());			//设置当前用户ID
					record.setUserName(person.getPersonName());		//设置当前用户名
					record.setOrgId(person.getBaseOrg().getOrgid());//机构ID
					record.setOrgName(person.getBaseOrg().getOrgName());	//机构名称
					record.setSchedulesId(schedule.getSchedulesId());		//班次ID
					record.setSchedulesName(schedule.getSchedulesName());	//班次名称
					record.setAttendTime(whichDay);				//出勤日期
					record.setMregulationTime(workstime);	//规定上班时间
					record.setAregulationTime(worketime);	//规定下班时间
					if(schedule.getSchedulesType()!=null&&schedule.getSchedulesType().equals(ONE)){//如果是休息班
						record.setAttendanceType(XIUXI);
						record.setIsCalcuAbsence(ONE);
					}else{		//如果不是休息班
						BigDecimal absenceHours=this.getHoursBetweenTwoDate(workstime, worketime,schedule.getJumpDay());//班次时数
						record.setShouldAttendHours(absenceHours);	//应出勤时数
						record.setAbsenceHours(absenceHours);		//缺勤时数
						record.setAttendanceTypeId(KUANGGONGID);
						record.setAttendanceType(KUANGGONG);
						record.setIsCalcuAbsence(ZERO);
					}
					record.setIsCalcu(flag);	//是否计算 0：没有计算，1：已计算
					registerDao.save(record);
				}
			}else{		//如果为法定假日
				ToaAttendRecord record=new ToaAttendRecord();
				record.setUserId(person.getPersonid());
				record.setUserName(person.getPersonName());
				record.setOrgId(person.getBaseOrg().getOrgid());
				record.setOrgName(person.getBaseOrg().getOrgName());
				record.setShouldAttendHours(new BigDecimal(0));
				record.setAttendTime(whichDay);
				record.setAttendanceType(FDJ);
				record.setIsCalcuAbsence(ONE);
				record.setIsCalcu(flag);
				registerDao.save(record);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,new Object[]{"保存考勤记录"});
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 17, 2009 5:29:58 PM
	 * Desc:    获取两个时间的小时数
	 * param:  startStr 	上班时间
	 * param:  endStr 		下班时间
	 * param:  isJumpDay 	该班次是否跨天
	 */
	public BigDecimal getHoursBetweenTwoDate(String startStr,String endStr,String isJumpDay){
		String[] startStrArr=startStr.split(":"); //上班时间
		String[] endStrArr=endStr.split(":");	  //下班时间
		Calendar c1=Calendar.getInstance();	
		c1.setTime(new Date());
		c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startStrArr[0]));
		c1.set(Calendar.MINUTE, Integer.parseInt(startStrArr[1]));
		c1.set(Calendar.SECOND, 0);
		Calendar c2=Calendar.getInstance();
		c2.setTime(new Date());
		if(isJumpDay!=null&&isJumpDay.equals(ONE)){//如果跨天了
			c2.add(Calendar.DAY_OF_YEAR, 1);
		}
		c2.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endStrArr[0]));
		c2.set(Calendar.MINUTE, Integer.parseInt(endStrArr[1]));
		c1.set(Calendar.SECOND, 0);
		long sdates=c1.getTimeInMillis();
		long edates=c2.getTimeInMillis();
		float result =(float)(edates-sdates)/(float)3600000;
		return new BigDecimal(Math.round(result*10)/10.0);
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 17, 2009 6:56:29 PM
	 * Desc:    构造考勤登记信息列表
	 * param:
	 */
	public List<ToaAttendTemp> buildRegistList(List<ToaAttendSchedule> schedules,ToaBasePerson person,Date now){
		List<ToaAttendTemp> list=new ArrayList<ToaAttendTemp> ();
		try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			sdf.format(now);
			Calendar c=sdf.getCalendar();
			c.add(Calendar.DATE, -1);
			Date lastDay=c.getTime();	//当天的前一天
			
			ToaAttendSchedule schedule;	//班次对象
			ToaAttendTemp attendTemp;	//考勤登记临时对象
			ToaAttendRecord record;		//考勤明细对象
			String personId=person.getPersonid();	//人员ID
			String isjump="";
			int k=0;
			
			List lastDayRecord=this.getRecordByTimeAndUser(personId, lastDay, lastDay);	//获取前一天的考勤记录
			List<ToaAttendSchedule> tempScheList=new ArrayList<ToaAttendSchedule>();		
			//处理前一天的考勤
			if(lastDayRecord!=null&&lastDayRecord.size()>0){			//如果考勤表中有前一天考勤记录	
				tempScheList=this.getJumpDaySchedule(personId,lastDay);	//获取前一天的跨天的班次列表(从考勤记录表里找)
			}else{		
				List<ToaAttendSchedule> lastDaySchedules=this.getCurrentSchedulesByUser(personId, lastDay);	//获取前一天的班次列表
				for(int j=0;lastDaySchedules!=null&&j<lastDaySchedules.size();j++){		//循环班次列表
					schedule=lastDaySchedules.get(j);
					isjump=schedule.getJumpDay();
					if(isjump!=null&&isjump.equals("1")){	//已跨天
						tempScheList.add(schedule);
					}
				}
				if(tempScheList.size()>0){
					this.saveAttendRecord(tempScheList, person, lastDay, ZERO);
				}
			}		
			for(int x=0;x<tempScheList.size();x++){		//循环前一天跨天的班次列表
				schedule=tempScheList.get(x);			//获得班次对象
				record=this.getUnitRecordByCon(personId, lastDay, schedule.getSchedulesId());	//根据人员，出勤日期，班次ID获取考勤明细记录	
				k+=1;
				attendTemp=new ToaAttendTemp();	//新增一个考勤登记临时对象
				attendTemp.setScheduleId(record.getAttendId()+"|"+schedule.getRegisterStimeOff()+ "|"+schedule.getRegisterEtimeOff()+"|"+TWO+"|"+schedule.getWorkEtime());
				attendTemp.setUserId(personId);
				attendTemp.setSequencecode("第"+k+"次登记");
				attendTemp.setAttendDate(now);
				attendTemp.setAttendType("下班登记");
				attendTemp.setRegisterStime(schedule.getRegisterStimeOff());
				attendTemp.setRegisterEtime(schedule.getRegisterEtimeOff());
				attendTemp.setRegulationTime(schedule.getWorkEtime());
				attendTemp.setRegisterTime(record.getAregisterTime());
				list.add(attendTemp);
			}
			
			
			//处理当天的考勤
			for(int i=0;schedules!=null&&i<schedules.size();i++){	//循环班次
				schedule=schedules.get(i);			//获得班次对象
				isjump=schedule.getJumpDay();		//是否跨天标识
				record=this.getUnitRecordByCon(personId, now, schedule.getSchedulesId());//根据人员，出勤日期，班次ID获取考勤明细记录
				if(record!=null){	//存在考勤明细
					k+=1;
					attendTemp=new ToaAttendTemp();		//新增一个考勤登记临时对象
					attendTemp.setScheduleId(record.getAttendId()+"|"+schedule.getRegisterStimeOn()+ "|"+schedule.getRegisterEtimeOn()+"|"+ONE+"|"+schedule.getWorkStime());
					attendTemp.setUserId(personId);								//人员ID
					attendTemp.setSequencecode("第"+k+"次登记");					//登记序号
					attendTemp.setAttendDate(now);								//出勤日期
					attendTemp.setAttendType("上班登记");							//登记类型
					attendTemp.setRegisterStime(schedule.getRegisterStimeOn());	//登记开始时间
					attendTemp.setRegisterEtime(schedule.getRegisterEtimeOn());	//登记结束时间
					attendTemp.setRegulationTime(schedule.getWorkStime());		//规定登记时间点
					attendTemp.setRegisterTime(record.getMregisterTime());		//登记时间	
					list.add(attendTemp);
				}	
				if(record!=null&&(isjump==null||"".equals(isjump)||"0".equals(isjump))){//存在考勤明细并且当天该班次没有跨天
					k+=1;
					attendTemp=new ToaAttendTemp();	//新增一个考勤登记临时对象
					attendTemp.setScheduleId(record.getAttendId()+"|"+schedule.getRegisterStimeOff()+ "|"+schedule.getRegisterEtimeOff()+"|"+TWO+"|"+schedule.getWorkEtime());
					attendTemp.setUserId(personId);
					attendTemp.setSequencecode("第"+k+"次登记");
					attendTemp.setAttendDate(now);
					attendTemp.setAttendType("下班登记");
					attendTemp.setRegisterStime(schedule.getRegisterStimeOff());
					attendTemp.setRegisterEtime(schedule.getRegisterEtimeOff());
					attendTemp.setRegulationTime(schedule.getWorkEtime());
					attendTemp.setRegisterTime(record.getAregisterTime());
					list.add(attendTemp);
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 17, 2009 10:19:59 PM
	 * Desc:	日期的格式化处理
	 * param:
	 */
	public Date getDateByString(Date date,String formatstr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatstr);
        String dateStr=sdf.format(date);
        try {
            return sdf.parse(dateStr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 17, 2009 10:35:04 PM
	 * Desc:	根据排班情况获取班次列表
	 * param:
	 */
	public List<ToaAttendSchedule> getSchedulesByAttendence(List<ToaAttendRecord> list){
		List<ToaAttendSchedule> scheList=new ArrayList<ToaAttendSchedule>();
		try{
			ToaAttendSchedule schedule;		//班次对象
			for(int i=0;i<list.size();i++){	//循环考勤明细列表
				schedule=scheManager.getSchedulesById(list.get(i).getSchedulesId());	//根据班次ID获取班次对象
				scheList.add(schedule);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return scheList;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 18, 2009 10:34:48 AM
	 * Desc:	登记考勤
	 * param:
	 */
	public String register(String attendId,String attendType,String registerTime)throws SystemException,ServiceException{
		try{
			ToaAttendRecord record=this.getAttendRecordById(attendId);
			ToaAttendSchedule schedule=scheManager.getSchedulesById(record.getSchedulesId());
			SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String flag;
			if(schedule.getSchedulesType()!=null&&schedule.getSchedulesType().equals(ONE)){//如果是休息班，只记录登记时间
				if(attendType.equals(ONE)){//上班
					record.setMregisterTime(format.parse(registerTime));
				}else{						//下班
					record.setAregisterTime(format.parse(registerTime));
				}
				this.saveAttendRecord(record);
				return null;
			}
			if(attendType.equals(ONE)){//如果为上班登记
				record.setMregisterTime(format.parse(registerTime));
				flag=this.calAbesenceStatus(schedule,registerTime,attendType);	//获取上班出勤状态
				if(flag!=null&&flag.equals("later")){		//如果为迟到
					Integer laterTime=this.getMinutsBetweenTwoDate(schedule.getWorkStime(), registerTime,attendType,schedule.getJumpDay());	//计算迟到分钟数
					record.setAttendLaterTime(-laterTime);	//设置迟到时间
				}	
			}else{	//如果为下班登记
				record.setAregisterTime(format.parse(registerTime));
				Date mregisterDate=record.getMregisterTime();	
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(mregisterDate!=null){	//如果登记了对应的上班时间
					flag=this.calAbesenceStatus(schedule,sdf.format(mregisterDate),ONE);		//获取上班出勤状态
					if(flag!=null&&flag.equals("skip")){			//上班登记超过了旷工时间，不做处理
						
					}else{
						flag=this.calAbesenceStatus(schedule,registerTime,attendType);//获取下班出勤状态
						if(flag!=null&&flag.equals("skip")){		//如果旷工了
							record.setAttendLaterTime(0);			//清空迟到时间
						}else if(flag!=null&&flag.equals("early")){	//如果早退了
							Integer earlyTime=this.getMinutsBetweenTwoDate(schedule.getWorkStime(),registerTime,attendType,schedule.getJumpDay());//计算早退分钟数
							record.setAttendLaterTime(-earlyTime);	  //设置早退时间
							record.setAbsenceHours(new BigDecimal(0));//清空旷工时间
							record.setIsCalcuAbsence(ZERO);			  //设置不统计缺勤
							record.setAttendanceTypeId(null);
							record.setAttendanceType(null);
						}else{//正常
							record.setAbsenceHours(new BigDecimal(0));
							record.setIsCalcuAbsence(ZERO);
							record.setAttendanceTypeId(null);
							record.setAttendanceType(null);
						}		
					}
				}
			}
			this.saveAttendRecord(record);
		} catch (ParseException e) {
			e.printStackTrace();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,new Object[]{"登记考勤"});
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 18, 2009 10:38:04 AM
	 * Desc:    根据人员ID，出勤时间，班次ID获取考勤明细记录
	 * param:
	 */
	public ToaAttendRecord getUnitRecordByCon(String personId,Date attendDate,String scheduleId)throws SystemException,ServiceException{
		try{
			String hql=" from ToaAttendRecord t where t.userId=? and t.attendTime=? and t.schedulesId=?";
			List<ToaAttendRecord> list=registerDao.find(hql, personId,attendDate,scheduleId);
			if(list!=null&&list.size()>0){
				return list.get(0);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"根据人员ID，出勤时间，班次ID获取考勤明细记录"});
		}
		return null;
	}
	
	/**
	 * 添加情况说明
	 * @author 胡丽丽
	 * @date:2009-11-18
	 * @param id 考勤明细ID
	 * @param desc  情况说明
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveDescByID(String id,String desc)throws SystemException,ServiceException{
		try {
			String hql="update ToaAttendRecord t set t.attendDesc=? where t.attendId=?";
			Query query=registerDao.createQuery(hql,desc,id);
			query.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,new Object[]{"添加情况说明出错!"});
		}
	}
	
	/**
	 * 根据部门查看考勤明细
	 * @author 胡丽丽
	 * @date:2009-11-18
	 * @param page 分页对象
	 * @param orgId 部门ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaAttendRecord> getRecordByOrgId(String orgId) throws SystemException,ServiceException{
		List<ToaAttendRecord> list=new ArrayList<ToaAttendRecord>();
		try {
			String hql="from ToaAttendRecord t where t.orgId=? order by t.userName,t.attendTime desc";
			list=registerDao.find(hql,orgId);
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据部门查看考勤明细出错！"});
		}
		return list;
	}
	/**
	 * 根据部门和条件搜索查看考勤明细
	 * @author 胡丽丽
	 * @date:2009-11-18
	 * @param page 分页对象
	 * @param orgId 部门ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaAttendRecord> getRecordByreport(String orgId,Date begintime ,Date endtime) throws SystemException,ServiceException{
		List<ToaAttendRecord> list=new ArrayList<ToaAttendRecord>();
		try {
			List<Object> objlist=new ArrayList<Object>();
			String hql="from ToaAttendRecord t where ";
				hql=hql+"  t.attendTime>=?";
				objlist.add(begintime);
			if(endtime!=null){//结束时间
				hql=hql+" and t.attendTime<=?";
				objlist.add(endtime);
			}
			if(orgId!=null&&!"".equals(orgId)){
				hql=hql+" and t.orgId=? ";
				objlist.add(orgId);
			}
			hql=hql+"  order by t.orgId,t.userId,t.attendTime ";
			Object[] obj=new Object[objlist.size()];
			for(int i=0;i<objlist.size();i++){
				obj[i]=objlist.get(i);
			}
			list=registerDao.find(hql,obj);
			
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据部门查看考勤明细出错！"});
		}
		return list;
	}
	/**
	 * 查看考勤明细
	 * @author 胡丽丽
	 * @date:2009-11-18
	 * @param page 分页对象
	 * @param orgId 部门ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaAttendRecord> getRecordByOrgId() throws SystemException,ServiceException{
		List<ToaAttendRecord> list=new ArrayList<ToaAttendRecord>();
		try {
			String hql="from ToaAttendRecord t  order by t.userName,t.attendTime";
			list=registerDao.find(hql);
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据部门查看考勤明细出错！"});
		}
		return list;
	}
	
	/**
	 * 根据部门和条件搜索查看考勤明细
	 * @author 胡丽丽
	 * @date:2009-11-18
	 * @param page 分页对象
	 * @param orgId 部门ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaAttendRecord> getRecordByOrgId(String orgId,String userName,Date  begintime,Date endtime) throws SystemException,ServiceException{
		List<ToaAttendRecord> list=new ArrayList<ToaAttendRecord>();
		try {
			List<Object> objlist=new ArrayList<Object>();
			String hql="from ToaAttendRecord t where t.orgId=? ";
			objlist.add(orgId);
			if(userName!=null&&!"".equals(userName.trim())){//用户姓名
				hql=hql+" and t.userName like ?";
				objlist.add("%"+userName+"%");
			}
			if(begintime!=null){//开始时间
				hql=hql+" and t.attendTime>=?";
				objlist.add(begintime);
			}
			if(endtime!=null){//结束时间
				hql=hql+" and t.attendTime<=?";
				objlist.add(endtime);
			}
			hql=hql+"  order by t.userName,t.attendTime desc";
			Object[] obj=new Object[objlist.size()];
			for(int i=0;i<objlist.size();i++){
				obj[i]=objlist.get(i);
			}
			list=registerDao.find(hql,obj);
			
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据部门查看考勤明细出错！"});
		}
		return list;
	}
	
	/**
	 * 根据用户ID和考勤汇总时间段查看考勤明细
	 * @author 胡丽丽
	 * @date:2009-11-18
	 * @param page 分页对象
	 * @param orgId 部门ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaAttendRecord> getRecordByUserId(String userid,Date  begintime,Date endtime) throws SystemException,ServiceException{
		List<ToaAttendRecord> list=new ArrayList<ToaAttendRecord>();
		try {
			List<Object> objlist=new ArrayList<Object>();
			String hql="from ToaAttendRecord t where";
			     hql=hql+" t.userId = ?";
				objlist.add(userid);
			if(begintime!=null){//开始时间
				hql=hql+" and t.attendTime>=?";
				objlist.add(begintime);
			}
			if(endtime!=null){//结束时间
				hql=hql+" and t.attendTime<=?";
				objlist.add(endtime);
			}
			hql=hql+"  order by t.attendTime desc,t.userId desc";
			Object[] obj=new Object[objlist.size()];
			for(int i=0;i<objlist.size();i++){
				obj[i]=objlist.get(i);
			}
			list=registerDao.find(hql,obj);
			
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据部门查看考勤明细出错！"});
		}
		return list;
	}
	
	/**
	 * 根据部门查看考勤明细
	 * @author 胡丽丽
	 * @date:2009-11-18
	 * @param page 分页对象
	 * @param orgId 部门ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaAttendRecord> search(Page<ToaAttendRecord> page,String orgId) throws SystemException,ServiceException{
		try {
			String hql="from ToaAttendRecord t where t.orgId=? order by t.attendTime,t.userId desc";
			
			page=registerDao.find(page, hql,orgId);
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据部门查看考勤明细出错！"});
		}
		return page;
	}
	
	/**
	 * 查看某个部门里最多上了多少个班次
	 * @author hull
	 * @param orgid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getCountBanci(String orgid)throws SystemException,ServiceException{
		try {
			String hql="select  max(count(t.userId)) from ToaAttendRecord t where t.orgId=? group by  t.userId ,t.attendTime";
			Query query=registerDao.createQuery(hql, orgid);
			List<Object> list=query.list();
			int count=Integer.parseInt(list.get(0).toString());
			
			return count;
		}catch(Exception e1){
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查看某个部门里最多上了多少个班次出错！"});
		}
	}
	/**
	 * 查看最多上了多少个班次
	 * @author hull
	 * @param orgid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getCountBanci()throws SystemException,ServiceException{
		try {
			String hql="select  max(count(t.userId)) from ToaAttendRecord t  group by  t.userId ,t.attendTime";
			Query query=registerDao.createQuery(hql);
			List<Object> list=query.list();
			int count=Integer.parseInt(list.get(0).toString());
			
			return count;
		}catch(Exception e1){
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查看某个部门里最多上了多少个班次出错！"});
		}
	}
	
	/**
	 * 查看某个人最多上了多少个班次
	 * @author hull
	 * @param orgid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getCountBanciByUser(String userid)throws SystemException,ServiceException{
		try {
			String hql="select  max(count(t.schedulesId)) from ToaAttendRecord t where t.userId=? group by t.userId ,t.attendTime";
			Query query=registerDao.createQuery(hql, userid);
			List<Object> list=query.list();
			int count=Integer.parseInt(list.get(0).toString());
			
			return count;
		}catch(Exception e1){
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查看某个部门里最多上了多少个班次出错！"});
		}
	}
	
	/**
	 * * 根据用户和汇总时间段查看考勤明细
	 * @author 胡丽丽
	 * @date:2009-11-25
	 * @param userId  用户ID
	 * @param beginTime  开始时间
	 * @param endTime  结束时间
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaAttendRecord> getRecordByTimeAndUser(String userId,
			Date beginTime, Date endTime) throws SystemException,
			ServiceException {
		try {
			String[] ids = userId.split(",");
			String userids = "";
			for (int i = 0; i < ids.length; i++) {
				userids = userids + ",'" + ids[i] + "'";
			}
			userids = userids.substring(1);
			String hql = "from ToaAttendRecord t where t.userId in("
					+ userids
					+ ") and t.attendTime>=? and t.attendTime<=?  order by t.userId , t.attendTime desc";
			return registerDao.find(hql, beginTime, endTime);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据用户和汇总时间段查看考勤明细出错！" });
		}
	}
	
	/**
	 * 根据用户和时间段查看某个申请类型占用多少时间
	 * @author 胡丽丽
	 * @date:2009-11-25
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @param property
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<Object> getTimesByProperty(String userId,
			Date beginTime, Date endTime)throws SystemException,
			ServiceException {
		try {
			String[] ids = userId.split(",");
			String userids = "";
			for (int i = 0; i < ids.length; i++) {
				userids = userids + ",'" + ids[i] + "'";
			}
			userids = userids.substring(1);
			String hql = "select  t.userId, t.attendanceType,sum(t.absenceHours)  from ToaAttendRecord t where t.userId in ("+userids
			+") and t.attendTime>=? and t.attendTime<=?  group by t.userId,t.attendanceType";
			List<Object> list= registerDao.find(hql, beginTime,endTime);
			return list;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据用户和时间段查看某个申请类型占用多少时间出错！" });
		} 
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 24, 2009 2:00:15 PM
	 * Desc:	查看某人某段时间内最多的班次数
	 * param:
	 */
	public int getMaxBanci(String personId,Date startDate,Date endDate)throws SystemException,ServiceException{
		try {
			List<Object> paraList=new ArrayList<Object>();
			String hql="select  max(count(t.userId)) from ToaAttendRecord t where t.attendTime>=? " +
					"and t.attendTime<=? ";
			paraList.add(startDate);
			paraList.add(endDate);
			if(personId!=null){
				hql+=" and t.userId=? ";
				paraList.add(personId);
			}
			hql+=" group by  t.userId ,t.attendTime";
			Object[] objs = new Object[paraList.size()];
			for (int i = 0; i < paraList.size(); i++) {
				objs[i] = paraList.get(i);
			}	
			Query query=registerDao.createQuery(hql,objs);
			List<Object> list=query.list();
			if(list!=null&&list.get(0)!=null){
				return Integer.parseInt(list.get(0).toString());
			}
			return 0;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查看某人某段时间内最多的班次数！"});
		}
	}

	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 18, 2009 11:44:54 AM
	 * Desc:	保存考勤明细记录
	 * param:
	 */
	public void saveAttendRecord(ToaAttendRecord record)throws SystemException,ServiceException{
		try{
			registerDao.save(record);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,new Object[]{"保存考勤明细记录"});
		}
	} 
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 18, 2009 11:50:18 AM
	 * Desc:    将登记时间和规定时间相比，判断是迟到还是早退还是旷工
	 * param: 
	 * return:  
	 */
	public String calAbesenceStatus(ToaAttendSchedule schedule,String registerDateStr,String attendType){
		try{	
			String regulationDateStr;
			Integer skiptime=schedule.getSkipTime();
			Integer latertime=schedule.getLaterTime();
			Integer earlytime=schedule.getEarlyTime();
			Integer gapMinutes;
			if(attendType.equals(ONE)){//如果是上班
				regulationDateStr=schedule.getWorkStime();//获取上班规定登记时间
				gapMinutes=this.getMinutsBetweenTwoDate(regulationDateStr, registerDateStr,attendType,schedule.getJumpDay());
				if(String.valueOf(gapMinutes).indexOf("-")!=-1){//如果计算出来的分钟数为负数，说明迟到或者旷工了
					gapMinutes=-gapMinutes;			//将负数转正数
					if(gapMinutes>skiptime){		//如果晚登记时间大于旷工时间
						return "skip";
					}else if(gapMinutes>latertime){	//如果晚登记时间大于迟到时间
						return "later";
					}
				}else{	//如果计算出来的分钟数为正数，说明正常
					return null;
				}				
			}else{						//如果是下班
				regulationDateStr=schedule.getWorkEtime();//获取下班规定登记时间
				gapMinutes=this.getMinutsBetweenTwoDate(regulationDateStr,registerDateStr,attendType,schedule.getJumpDay());
				if(String.valueOf(gapMinutes).indexOf("-")!=-1){//如果计算出来的分钟数为负数，说明早退或者旷工了
					gapMinutes=-gapMinutes;			//将负数转正数
					if(gapMinutes>skiptime){		//如果早登记时间大于旷工时间
						return "skip";
					}else if(gapMinutes>earlytime){	//如果早登记时间大于迟到时间
						return "early";
					}
				}else{	//如果计算出来的分钟数为正数，说明正常
					return null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * @author: 彭小青
	 * @date: 	Nov 18, 2009 3:01:43 PM
	 * Desc:
	 * param:regDateStr   规定时间 格式为时分
	 * param:registerDateStr 登记时间 格式为年月日 时分秒
	 */
	public boolean isRegular(String regDateStr,String registerDateStr,String flag){
		try {
			String[] regTimeStrArr=regDateStr.split(":"); 
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.parse(registerDateStr);
			Calendar c1=df.getCalendar();	//规定时间
			c1.set(Calendar.HOUR_OF_DAY,Integer.parseInt(regTimeStrArr[0]));
			c1.set(Calendar.MINUTE,Integer.parseInt(regTimeStrArr[1]));
			SimpleDateFormat df1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df1.parse(registerDateStr);
			Calendar c2=df1.getCalendar();	//登记时间
			if(flag.equals(ONE)){	//如果是上班
				if(c2.after(c1)){	//登记时间晚于规定时间
					return true;
				}else{
					return false;
				}
			}else{		//如果是下班
				if(c1.after(c2)){	//规定时间晚于登记时间
					return true;
				}else{
					return false;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 18, 2009 1:52:30 PM
	 * Desc:   获取规定时间和登记时间之间的分钟数
	 * param:  regDateStr 		规定时间 格式为时分
	 * param:  registerDateStr  登记时间，格式为年月日时分秒
	 * param:  flag 			上下班标识
	 * param:  isJumpDate		该班次是否跨天 
	 */
	public Integer getMinutsBetweenTwoDate(String regDateStr,String registerDateStr,String flag,String isJumpDate){
		try {
			long minutes=0;
			String[] regTimeStrArr=regDateStr.split(":"); 	//规定时间 格式为时分
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.parse(registerDateStr);
			Calendar c1=df.getCalendar();	//规定时间
			if(isJumpDate!=null&&isJumpDate.equals(ONE)&&flag.equals(TWO)){//如果该班次跨天了，且是登记下班
				c1.add(Calendar.DAY_OF_YEAR, 1);
			}
			c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(regTimeStrArr[0]));
			c1.set(Calendar.MINUTE, Integer.parseInt(regTimeStrArr[1]));
			SimpleDateFormat df1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df1.parse(registerDateStr);
			Calendar c2=df1.getCalendar();//登记时间
			long regMillis=c1.getTimeInMillis();		//规定时间毫秒数
			long registerMillis=c2.getTimeInMillis();	//登记时间毫秒数
			if(flag.equals(ONE)){//如果是上班
				minutes=(regMillis-registerMillis)/60000;//规定时间减去登记时间，如果为负数，说明迟到或者旷工了
			}else{		//如果下班
				minutes=(registerMillis-regMillis)/60000;//登记时间减去规定时间，如果为负数，说明早退或者旷工了
			}
			return (int)minutes;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 24, 2009 11:44:33 AM
	 * Desc:	重新构建上下班记录列表
	 * param:
	 */
	public List<Object[]> getRegisterRecordList(HttpServletRequest request,User user,Date startDate,Date endDate)throws SystemException,ServiceException{
		try{
			List<ToaAttendRecord> registerList=null;
			
			String personId=null;
			if(user!=null){
				ToaBasePerson person=perManager.getPersonByUumsPerId(user.getUserId());//根据统一用户ID获取对应人事的人员ID
				if(person!=null){
					personId=person.getPersonid();
					registerList=this.getRegisterRecord(personId, startDate, endDate);
				}else{
					return null;
				}
			}else{
				registerList=this.getRegisterRecord(personId, startDate, endDate);
			}
			List<Object[]> detailList=new ArrayList<Object[]>();
			Object[] object = null;
			Object[] preobj=null;
			ToaAttendRecord record1=null;
			ToaAttendRecord record2=null;  
			int k=0;
			String desc="";
			int num=this.getMaxBanci(personId, startDate, endDate);//获取人员某段时间内最多的班次数
			request.setAttribute("maxNum", String.valueOf(num));
			for(int i=0;i<registerList.size();i++){	//循环考勤登记信息列表
			   if(i>0){//如果是第一条以后的数据
				   record1=(ToaAttendRecord)registerList.get(i-1);	//获取前一条记录
			   }
			   record2=(ToaAttendRecord)registerList.get(i); 		//获取循环的当前的记录
			   
			   //是否是该人员当天的登记信息
			   if(i>0&&record1.getUserId().equals(record2.getUserId())&&record1.getAttendTime().getTime()==record2.getAttendTime().getTime()){//是
				    k=k+2;
			   }else{
				   //新增一个
				   if(preobj!=null)
					   detailList.add(preobj);
				   object=new Object[3+num*2];			//创建数组对象
				   object[0]=record2.getAttendTime();  	//出勤时间
				   k=0;	//重置累加器
				   desc="";
			   }   
			   object[1+k]=record2.getMregisterTime();
			   object[1+k+1]=record2.getAregisterTime();
			   if(record2.getAttendDesc()!=null){
				   desc+="【"+record2.getAttendDesc()+"】";
			   }
			   object[1+num*2]=desc;
			   preobj=object;
			   if(i==registerList.size()-1){	//如果是最后一条记录
				   detailList.add(object);
			   }
			}
		   return detailList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{""});
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 24, 2009 11:48:45 AM
	 * Desc:	按人员、出勤日期排序，获取一段时间内的考勤明细列表
	 * param:
	 */
	public List<ToaAttendRecord> getRegisterRecord(String personId,Date startDate,Date EndDate){
		try{
			List<Object> objlist = new ArrayList<Object>();
			String hql="from ToaAttendRecord t where t.attendTime>=? and t.attendTime<=?";
			objlist.add(startDate);
			objlist.add(EndDate);
			if(personId!=null){
				hql+=" and t.userId=?";
				objlist.add(personId);;
			}
			hql+=" order by t.userId ,t.attendTime";
			Object[] objs = new Object[objlist.size()];
			for (int i = 0; i < objlist.size(); i++) {
				objs[i] = objlist.get(i);
			}	
			return registerDao.find(hql, objs);
		}catch(Exception e1){
			e1.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"按人员、出勤日期排序，获取一段时间内的考勤明细列表"});	
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Dec 15, 2009 5:04:11 PM
	 * Desc:	获取某天已跨天的考勤明细记录
	 * param:
	 */
	public List<ToaAttendSchedule> getJumpDaySchedule(String personId,Date attendDate)throws SystemException,ServiceException{
		try{
			String hql="select s from ToaAttendRecord t,ToaAttendSchedule s where t.userId=? and t.attendTime=? and t.schedulesId=s.schedulesId and s.jumpDay=?";
			List<ToaAttendSchedule> list=registerDao.find(hql, personId,attendDate,ONE);
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"根据人员ID，出勤时间，班次ID获取考勤明细记录"});
		}
	}
	
	/*
	 * 
	 * Description:查找人员的考勤记录数
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 12, 2010 3:59:32 PM
	 */
	public Map<String,Integer> getAttendRecordCount(List<ToaBasePerson> personList,Date stime,Date etime,String flag)throws SystemException,ServiceException{
		try{
			String hql=" select t.userId,t.attendTime,count(t.attendId) from ToaAttendRecord t where t.attendTime>=? and t.attendTime<=? ";
			String personid="";
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String date="";
			if(flag!=null&&ONE.equals(flag)){	//手动计算
				for(ToaBasePerson person:personList){
					personid+=",'"+person.getPersonid()+"'";
				}
				personid=personid.substring(1);
				hql+=" and t.userId in ("+personid+")";
			}
			hql+=" group by t.userId,t.attendTime";
			List<Object[]> list=registerDao.find(hql,stime,etime);
			if(list!=null&&list.size()>0){
				Object[] record;
				Map<String,Integer> map=new HashMap<String,Integer>();
				for(int j=0;j<list.size();j++){
					record=list.get(j);
					if(record[1]!=null)
						date=sdf.format(record[1]);
					map.put(record[0].toString()+date,Integer.parseInt(record[2].toString()));
				}
				return map;
			}
			return null;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"查找人员的考勤记录数"});
		}
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 12, 2010 8:19:38 PM
	 */
	public List<ToaAttendSchedule> getSchedulesByUser(List<ToaAttendSchedGroup> list,String personId,Date now)throws SystemException,ServiceException{
		List<ToaAttendSchedule> schelist=new ArrayList<ToaAttendSchedule>();
		try{
			Set scheduleSet;				//班次集合
			Set regSet;						//轮班规则集合
			ToaAttendSchedule schedule;		//班次对象
			ToaAttendRegulation reg=null;	//轮班规则对象
			ToaAttendArrange arrange=null;	//排班对象
			Date groupStime;	//班组有效时间
			Date groupEtime;	//班次有效时间
			for(ToaAttendSchedGroup group:list){	//循环班组列表
				groupStime=group.getGroupStime();
				groupEtime=group.getGroupEtime();
				if(now.before(groupStime)||(groupEtime!=null&&now.after(groupEtime))){
					continue;
				}
				scheduleSet=group.getToaAttendSchedules();	//获取班组的班次信息列表
				Iterator scheduleIt=scheduleSet.iterator();	//遍历轮班规则
				arrange=manager.getArrangeListByGroupOrUser(group.getGroupId(), personId, null).get(0);
				while(scheduleIt.hasNext()){
					schedule=(ToaAttendSchedule)scheduleIt.next();	//获取轮班规则集合
					regSet=schedule.getToaAttendRegulations();
					Iterator regit=regSet.iterator();			//遍历轮班规则
					if(regit.hasNext()){
						reg=(ToaAttendRegulation)regit.next();	//获取第一个轮班规则
					}	
					if(reg!=null&&this.isIdentical(reg,arrange,schedule,now)){	//不倒班如果当天上符合这个轮班规则,则该人员当天上这趟班
						schelist.add(schedule);	
					}
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"获取当前用户当天的班次信息列表"});
		}
		return schelist;
	}
	
	@Autowired
	public void setManager(ScheGroupManager manager) {
		this.manager = manager;
	}
	
	@Autowired
	public void setPerManager(PersonManager perManager) {
		this.perManager = perManager;
	}

	@Autowired
	public void setHolidayManager(HolidayManager holidayManager) {
		this.holidayManager = holidayManager;
	}
	
	@Autowired
	public void setScheManager(SchedulesManager scheManager) {
		this.scheManager = scheManager;
	}
}
