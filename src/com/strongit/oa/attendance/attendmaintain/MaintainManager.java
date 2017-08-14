package com.strongit.oa.attendance.attendmaintain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attendance.apply.ApplyManager;
import com.strongit.oa.attendance.applytype.ApplyTypeManager;
import com.strongit.oa.attendance.arrange.ScheGroupManager;
import com.strongit.oa.attendance.holiday.HolidayManager;
import com.strongit.oa.attendance.register.RegisterManager;
import com.strongit.oa.bo.ToaAttenApply;
import com.strongit.oa.bo.ToaAttendCancle;
import com.strongit.oa.bo.ToaAttendHoliday;
import com.strongit.oa.bo.ToaAttendRecord;
import com.strongit.oa.bo.ToaAttendSchedGroup;
import com.strongit.oa.bo.ToaAttendSchedule;
import com.strongit.oa.bo.ToaAttendType;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Nov 25, 2009 10:21:02 AM
 * @version  2.0.4
 * @comment  考勤维护Manager
 */
@Service
@Transactional
public class MaintainManager {
	
	private GenericDAOHibernate<ToaAttendRecord, String> registerDao;
	@Autowired private HolidayManager holidayManager;
	@Autowired private ApplyManager applyManager;
	@Autowired private RegisterManager registerManager;
	@Autowired private ApplyTypeManager typeManager;
	@Autowired private ScheGroupManager groupManager;		//班组manager
	private final static String jiaban="40288227278ecc0401278f1a2cf80001";//申请类型为加班
	private final static String FDJ="法定假";
	private final static String XIUXI="休息";
	private final static String NOTCALABSENCE="1";	//不统计为缺勤
	private final static String ISCAL="1";			//已计算
	private final static String ZERO="0";		
	private final static String ONE="1";
	private final static String FIRSTTIMEOFDAY="00:00";
	private final static String LASTTIMEOFDAY="23:59";
	
	
	public MaintainManager() {

	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		registerDao = new GenericDAOHibernate<ToaAttendRecord, String>(sessionFactory,ToaAttendRecord.class);
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 25, 2009 4:02:58 PM
	 * Desc:	计算考勤
	 * param:
	 */
	public String calAttend(Date startDate,Date endDate,List<ToaBasePerson> userlist,String flag)throws SystemException,ServiceException{
		String msg="考勤计算成功!";
		Long s=new Date().getTime();
		try{
			System.out.println("考勤计算开始!");
			List<ToaAttendRecord> attendList=new ArrayList<ToaAttendRecord>();
			List<ToaAttendHoliday> holidayList=holidayManager.getHolidayByStimeAndEtime(startDate, endDate);//获取有效的的法定假日列表
			if(holidayList!=null&&holidayList.size()>0){//判断是否有法定假日
				this.calHolidayRecordOfAllPerson(attendList,holidayList,userlist,startDate, endDate);	//计算所有人员的法定假日考勤
			}
			registerDao.save(attendList);
			registerDao.flush();
			registerDao.clear();
			List<ToaAttenApply> applyList=new ArrayList<ToaAttenApply>();//获取计算时间段内的申请单list
			for(ToaBasePerson person:userlist){	//循环人员
				applyList=applyManager.getApplyList(person, startDate, endDate);//获取某人员考勤计算时间段内的申请单列表
				this.calRecordOfApply(applyList, person,startDate,endDate);		//计算某个人某段时间的考勤（请假或加班）	
			}
			registerDao.flush();
			registerDao.clear();
			attendList=new ArrayList<ToaAttendRecord>();
			this.calAbsenceRecordOfAllPerson(flag,attendList,userlist,startDate, endDate);
			
			//更新计算状态为未计算的考勤记录为已计算
			List<ToaAttendRecord> list=this.getRecordOfNotCal(startDate, endDate,userlist);
			ToaAttendRecord temp;
			for(int i=0;list!=null&&i<list.size();i++){
				temp=list.get(i);
				temp.setIsCalcu(ISCAL);
			}
			registerDao.update(list);
		}catch(ServiceException e){
			msg="考勤计算失败!";
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"calAttend(计算考勤)"});
		}catch(Exception e){
			msg="考勤计算失败!";
			e.printStackTrace();
		}
		Long e=new Date().getTime();
		System.out.println("考勤计算结束!用时:"+(e-s));
		return msg;
	}
	
	/*
	 * @author: 彭小青
	 * @date: 	Nov 27, 2009 5:43:16 PM
	 * Desc:	计算某个人某段时间的考勤（请假或加班）
	 * param:
	 */
	public void calRecordOfApply(List<ToaAttenApply> applyList,ToaBasePerson person,Date startDate,Date endDate)throws SystemException,ServiceException{
		try{	
			List<Object[]> actualApply=this.getActualApplyTime(applyList,startDate,endDate);//获取实际的请假时间
			List<ToaAttendSchedule> scheList;	//班次列表
			Object[] dateArr;	//实际请假时间数组
			Calendar c1;		//申请开始时间
			Calendar c2;		//申请结束时间
			Calendar c3=Calendar.getInstance();
			Calendar tempStart=Calendar.getInstance();
			for(int j=0;actualApply!=null&&j<actualApply.size();j++){	//循环实际的申请时间列表
				dateArr=actualApply.get(j);
				c1=this.getDay((Date)dateArr[0],"yyyy-MM-dd HH:mm:ss");	//申请开始时间
				c2=this.getDay((Date)dateArr[1],"yyyy-MM-dd HH:mm:ss");	//申请结束时间
				tempStart.set(Calendar.YEAR, c1.get(Calendar.YEAR));	//临时变量，设置年月日时分秒毫秒
				tempStart.set(Calendar.MONTH, c1.get(Calendar.MONTH));
				tempStart.set(Calendar.DATE, c1.get(Calendar.DATE));
				tempStart.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
				tempStart.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
				tempStart.set(Calendar.SECOND,c1.get(Calendar.SECOND));
				tempStart.set(Calendar.MILLISECOND,c1.get(Calendar.MILLISECOND));
				while(tempStart.before(c2)){//循环申请开始时间到结束时间
					scheList=registerManager.getSchedulesByUser(person.getPersonid(),registerManager.getDateByString(tempStart.getTime(), "yyyy-MM-dd"));//获取班次列表
					if(tempStart.get(Calendar.YEAR)==c2.get(Calendar.YEAR)&&tempStart.get(Calendar.MONTH)==c2.get(Calendar.MONTH)
							&&tempStart.get(Calendar.DATE)==c2.get(Calendar.DATE)){//如果开始时间和结束时间是同一天
						c3=c2;	
					}else{		//如申请开始时间和申请结束时间不是同一天
						c3.set(Calendar.YEAR, tempStart.get(Calendar.YEAR));
						c3.set(Calendar.MONTH, tempStart.get(Calendar.MONTH));
						c3.set(Calendar.DATE , tempStart.get(Calendar.DATE ));
						c3.set(Calendar.HOUR_OF_DAY, 23);
						c3.set(Calendar.MINUTE, 59);
						c3.set(Calendar.SECOND, 59);	
					}
					this.saveApplyRecordOfEveryday(scheList,person,dateArr[2].toString(), tempStart, c3);
					tempStart.add(Calendar.DAY_OF_YEAR, 1);//申请开始时间加1天
					tempStart.set(Calendar.HOUR_OF_DAY, 0);
					tempStart.set(Calendar.MINUTE, 0);
					tempStart.set(Calendar.SECOND,0);
				}
			}		
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"calRecordOfApply(计算某个人某段时间的考勤)"});
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 27, 2009 3:06:49 PM
	 * Desc:	根据申请开始时间和申请结束时间，销假开始时间和销假结束时间获取实际的请假时间段
	 * param:
	 * 说明：该方法主要是将申请时间段和对应销假时间段进行比较
	 * 如果申请单没有销假单，则该申请的确切申请时间就是该申请的开始时间和结束时间
	 * 如果有该申请有销假的话，那么要计算真正请假的时间
	 * 1、如果销假开始时间早于申请开始时间，那么看该申请的销假结束结束时间是否早于申请结束时间，如果早于的话并且相差时间大于30分钟，
	 * 那么记录销假结束时间到申请结束时间段为真正请假时间，否则视为该申请已经销假
	 * 2、如果销假开始时间晚于申请开始时间，那么看申请的销假开始时间和申请开始时间相差时间是大于30分钟，如果大于那么记录申请开始时间和销假开始时间段为请假时间，
	 * 如果小于则此段时间不视为请假时间。接下来看销假结束时间和申请结束时间的对比，看销假结束结束时间是否早于申请结束时间，如果早于的话并且相差时间大于30分钟，
	 * 那么记录销假结束时间到申请结束时间段为真正请假时间，否则视为该申请已经销假
	 * 
	 */
	public List<Object[]> getActualApplyTime(List<ToaAttenApply> applyList,Date startDate,Date endDate){
		try{
			int minutes=0;
			Date applySTime;
			Date applyETime;
			Date cancleSTime;
			Date cancleEtime;
			ToaAttenApply apply;		//申请单对象
			ToaAttendCancle cancle;		//销假单对象
			List<ToaAttendCancle> cancleList=new ArrayList<ToaAttendCancle>();	//销假单list
			List<Object[]> list=new ArrayList<Object[]>();	//实际申请时间list
			Object[] object;
			for(int j=0;j<applyList.size();j++){//循环申请单
				apply=applyList.get(j);
				applySTime=apply.getApplyStime().after(startDate)?apply.getApplyStime():startDate;	//申请开始时间
				applyETime=apply.getApplyEtime().before(endDate)?apply.getApplyEtime():endDate;		//申请结束时间
				cancleList=applyManager.getAuditAttenCancles(apply.getApplyId());
				if(cancleList!=null&&cancleList.size()>0){// 如果该申请单有对应的销假单
					cancle=cancleList.get(0);		
					cancleSTime=cancle.getCancleStime();	//销假开始时间
					cancleEtime=cancle.getCancleEtime();	//销假结束时间
					object=new Object[3];
					if(cancleSTime.after(applySTime)){	//如果销假开始时间晚于申请开始时间
						minutes=this.getMinutsBetweenTwoDate(applySTime, cancleSTime);//获取申请开始时间和销假开始时间分钟数
						if(minutes<30){//如果间隔小于30分钟
							object[0]=cancleEtime;	//设置销假结束时间
						}else{//大于30分钟，则申请开始时间和销假开始时间也算是请假时间
							object[0]=applySTime;
							object[1]=cancleSTime;
							object[2]=apply.getApplyTypeId();
							list.add(object);
							object=new Object[3];
							object[0]=cancleEtime;	//设置销假结束时间
						}
					}else{	
						object[0]=cancleEtime;	
					}
					if(cancleEtime.before(applyETime)){//如果销假结束时间早于申请结束时间
						minutes=this.getMinutsBetweenTwoDate(cancleEtime, applyETime);//获取销假结束时间和申请结束时间分钟数
						if(minutes>30){//如果间隔大于30分钟
							object[1]=applyETime;
							object[2]=apply.getApplyTypeId();
							list.add(object);
						}
					}
				}else{	//如果没有对应的销假单，则实际申请时间就是该申请单的申请时间
					object=new Object[3];
					object[0]=applySTime;
					object[1]=applyETime;
					object[2]=apply.getApplyTypeId();
					list.add(object);
				}		
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 28, 2009 11:41:04 AM
	 * Desc:    计算某个人每一天的考勤（请假或加班）
	 * param:	scheList 	班次列表
	 * param:	personId 	人员ID
	 * param:	applyTypeId 申请类型ID
	 * param:	c1 			申请开始时间
	 * param:	c2 			申请结束时间
	 * 
	 */
	public void saveApplyRecordOfEveryday(List<ToaAttendSchedule> scheList,ToaBasePerson person,String applyTypeId,Calendar c1,Calendar c2)throws SystemException,ServiceException{
		try{	
			SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
			Date date=registerManager.getDateByString(c1.getTime(), "yyyy-MM-dd");//申请时间格式化
			ToaAttendType type=typeManager.getAttendTypeByID(applyTypeId);	//根据类型ID获取申请类型对象
			String personId=person.getPersonid();
			List<ToaAttendRecord> recordList;
			ToaAttendRecord record;		//考勤明细对象
			ToaAttendSchedule schedule;	//班次对象
			String regStart;			//规定上班时间
			String regend;				//规定下班时间	
			boolean flag=true;			//当天即要上班又要加班的情况
			String isJump="";
			boolean isContain=false;
			boolean isContain1=false;
			boolean isContain2=false;
			if(scheList!=null&&scheList.size()>0){	//有班次：1、各种请假 2、当天有班次，但当天却是法定假日，并且当天也加班了
				String applyStart=sdf.format(c1.getTime());	//请假开始时间
				String applyEnd=sdf.format(c2.getTime());	//请假结束时间
				String[] result=new String[2];				//存放计算后的申请开始时间和申请结束时间
				recordList=registerManager.getRegisterRecord(personId,date,date);		//获取请假时间当天的考勤记录列表
				if(recordList!=null&&recordList.size()>0){	//如果申请时间有考勤记录
					int k=recordList.size();
					List<ToaAttendRecord> list=this.getHolidayRecord(date, personId);	//获取当天为法定假的考勤明细记录
					if(list!=null&&list.size()>0){					//如果当天存在法定假日
						this.delHolidayRecord(date, personId);		//删除法定假日
						if(k==1)	//如果考勤记录只有一条（法定假日），按班次添加考勤记录
							registerManager.saveAttendRecord(scheList,person,date,ISCAL);//根据班次新增当天的考勤记录
					}
				}else{
					registerManager.saveAttendRecord(scheList,person,date,ISCAL);		 //根据班次新增当天的考勤记录
				}
				for(int i=0;i<scheList.size();i++){			//循环班次列表
					schedule=scheList.get(i);				//班次对象
					regStart=schedule.getWorkStime();		//规定上班时间
					regend=schedule.getWorkEtime();			//规定下班时间	
					isJump=schedule.getJumpDay();
					if(regStart==null||regend==null){	//如果规定上班时间或规定下班时间为空
						if(applyTypeId.equals(jiaban)){	//如果是加班
							regStart=applyStart;
							regend=applyEnd;
						}else{
							continue;
						}
					}
					if(ONE.equals(isJump)){	//如果是跨天
						isContain=(applyStart.compareTo(LASTTIMEOFDAY)<0&&applyEnd.compareTo(regStart)>0)||applyStart.compareTo(regend)<0&&applyEnd.compareTo(FIRSTTIMEOFDAY)>0;
						if(applyStart.compareTo(regend)<0&&applyEnd.compareTo(FIRSTTIMEOFDAY)>0){
							isContain1=applyStart.compareTo(FIRSTTIMEOFDAY)<0;
						}else{
							isContain1=applyStart.compareTo(regStart)<0;
						}
						if(applyStart.compareTo(LASTTIMEOFDAY)<0&&applyEnd.compareTo(regStart)>0){
							isContain2=applyEnd.compareTo(LASTTIMEOFDAY)>0;
						}else{
							isContain2=applyEnd.compareTo(regend)>0;
						}
					}else{	//不是跨天
						isContain=applyStart.compareTo(regend)<0&&applyEnd.compareTo(regStart)>0;
						isContain1=applyStart.compareTo(regStart)<0;
						isContain2=applyEnd.compareTo(regend)>0;
					}
					if(isContain){//如果请假开始时间小于规定下班时间并且请假结束时间必须大于规定上班时间,即申请时间段和上班时间段有交叉	
						flag=false;
						if(isContain1){	//请假开始时间小于规定上班时间	
							result[0]=regStart;	
						}else{
							result[0]=applyStart;	
						}
						if(isContain2){		//申请结束时间大于等于规定下班时间
							result[1]=regend;
						}else{
							result[1]=applyEnd;
						}	
						record=registerManager.getUnitRecordByCon(personId, date, schedule.getSchedulesId());		//获取对应的考勤明细记录
						record.setAbsenceHours(registerManager.getHoursBetweenTwoDate(result[0], result[1], ZERO));
						record.setAttendanceTypeId(type.getTypeId());
						record.setAttendanceType(type.getTypeName());
						record.setIsCalcuAbsence(type.getIsAbsence());
						registerManager.saveAttendRecord(record);
					    if(result[1]==applyEnd){
					    	break;
					    }else{
					    	result=new String[2];
					    }
					}
				}
				if(flag){	//当天即要上班又要加班的情况
					this.delJiabanRecord(date, personId);	//删除计算当天加班的记录
					record=new ToaAttendRecord();
					record.setUserId(personId);			
					record.setUserName(person.getPersonName());		
					record.setOrgId(person.getBaseOrg().getOrgid());
					record.setOrgName(person.getBaseOrg().getOrgName()); 
					record.setAttendTime(date);					
					record.setShouldAttendHours(new BigDecimal(0));
					record.setAbsenceHours(registerManager.getHoursBetweenTwoDate(applyStart,applyEnd, ZERO));	
					record.setAttendanceTypeId(type.getTypeId());
					record.setAttendanceType(type.getTypeName());
					record.setIsCalcuAbsence(type.getIsAbsence());
					record.setIsCalcu(ISCAL);
					registerManager.saveAttendRecord(record);
				}
			}else{	//当天没有班次，但加班了。
				//this.delHolidayRecord(date, personId);	//删除计算当天法定假的记录
				//this.delJiabanRecord(date, personId);	//删除计算当天加班的记录
				this.delRecord(date, personId);
				record=new ToaAttendRecord();
				record.setUserId(personId);			
				record.setUserName(person.getPersonName());		
				record.setOrgId(person.getBaseOrg().getOrgid());
				record.setOrgName(person.getBaseOrg().getOrgName());
				record.setAttendTime(date);					
				record.setShouldAttendHours(new BigDecimal(0));
				String applyStart=sdf.format(c1.getTime());	//请假开始时间
				String applyEnd=sdf.format(c2.getTime());	//请假结束时间
				record.setAbsenceHours(registerManager.getHoursBetweenTwoDate(applyStart,applyEnd, ZERO));	
				record.setAttendanceTypeId(type.getTypeId());
				record.setAttendanceType(type.getTypeName());
				record.setIsCalcuAbsence(type.getIsAbsence());
				record.setIsCalcu(ISCAL);
				registerManager.saveAttendRecord(record);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"saveApplyRecordOfEveryday(计算某个人每一天的考勤（请假或加班）)"});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 28, 2009 10:39:54 AM
	 * Desc:	将班次组合成数组
	 * param:
	 */
	public String[][] getScheduleArr(List<ToaAttendSchedule> scheList){
		String[][] schedules=new String[2][];
		try{
			if(scheList!=null&&scheList.size()>0){
				for(int i=0;i<scheList.size();i++){
					ToaAttendSchedule sche=scheList.get(0);
					schedules[0][i]=sche.getWorkStime();
					schedules[1][i]=sche.getWorkEtime();
				}	
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return schedules;
	}

	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 27, 2009 3:24:48 PM
	 * Desc:   获取两个时间之间的分钟数
	 * param:  startTime 开始时间
	 * param:  endTime   结束时间
	 */
	public Integer getMinutsBetweenTwoDate(Date startTime,Date endTime){
		long minutes=0;
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.format(startTime);
		Calendar c1=df.getCalendar();	//开始时间
		SimpleDateFormat df2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df2.format(endTime);
		Calendar c2=df2.getCalendar();	//结束时间
		long start=c1.getTimeInMillis();		//开始时间毫秒数
		long end=c2.getTimeInMillis();	//结束时间毫秒数
		minutes=(end-start)/60000;//登记时间减去规定时间，如果为负数，说明早退或者旷工了
		return (int)minutes;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 27, 2009 5:12:30 PM
	 * Desc:	日期转换
	 * param:
	 */
	public Calendar getDay(Date date,String formate){
		try{
			SimpleDateFormat df=new SimpleDateFormat(formate);
			df.format(date);
			Calendar c1=df.getCalendar();	//规定时间
			return c1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 28, 2009 6:46:47 PM
	 * Desc:	计算所有人员的法定假日考勤
	 * param:	holidayList 法定假日信息列表
	 * param:  	personIds 	人员ID
	 * param： 	startDate	考勤计算开始时间
	 * param：   endDate   	考勤计算结束时间
	 * 算法：假日假日是5.1-5.3
	 * 1、法定假日的有效时间是：2008.1.1-没有结束时间
	 * 	  计算的时间段：2008.1.1-2010.1.1
	 *    生成的法定假日记录为：2008.5.1-2008.5.3  2009.5.1-2009.5.3
	 * 2、法定假日的有效时间是：2008.5.2-没有结束时间
	 *    计算的时间段：2008.1.1-2010.1.1
	 *    生成的法定假日记录为：2008.5.2-2008.5.3  2009.5.1-2009.5.3
	 * 3、法定假日的有效时间是：2008.1.1-没有结束时间
	 *    计算的时间段：2008.5.2-2010.1.1
	 *    生成的法定假日记录为：2008.5.2-2008.5.3  2009.5.1-2009.5.3
	 * 4、法定假日的有效时间是：2008.1.1-2008.12.31
	 *    计算的时间段：2008.1.1-2010.1.1
	 *    生成的法定假日记录为：2008.5.1-2008.5.3 
	 * 5、法定假日的有效时间是：2008.1.1-2008.12.31
	 *    计算的时间段：2008.5.2-2010.1.1
	 *    生成的法定假日记录为：2008.5.2-2008.5.3 
	 * 6、法定假日的有效时间是：2008.1.1-2009.5.2
	 *    计算的时间段：2008.5.2-2010.1.1
	 *    生成的法定假日记录为：2008.5.2-2008.5.3  2009.5.1-2009.5.2  
	 */
	public void calHolidayRecordOfAllPerson(List<ToaAttendRecord> attendList,List<ToaAttendHoliday> holidayList,List<ToaBasePerson> userList,Date startDate,Date endDate)throws SystemException,ServiceException{
		try{	
			String date="2009-01-01";
			SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
			sdf1.parse(date);
			Calendar startCal=sdf1.getCalendar();  	 //开始时间	
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
			sdf2.parse(date);
			Calendar endCal=sdf2.getCalendar();		//结束时间
			SimpleDateFormat sdf4=new SimpleDateFormat("yyyy-MM-dd");	
			sdf4.parse(date);
			Calendar tempCalendar=sdf4.getCalendar();//结束时间临时变量
			Object[] obj;
			List<Object[]> actualHoliday=this.getActualHoliday(holidayList, startDate, endDate);
			for(int i=0;i<actualHoliday.size();i++){//循环组合的法定假日
				obj=actualHoliday.get(i);
				startCal.set(Calendar.YEAR, ((Calendar)obj[0]).get(Calendar.YEAR));		
				startCal.set(Calendar.MONTH, ((Calendar)obj[0]).get(Calendar.MONTH));	
				startCal.set(Calendar.DATE, ((Calendar)obj[0]).get(Calendar.DATE));	
				
				endCal.set(Calendar.YEAR, ((Calendar)obj[1]).get(Calendar.YEAR));		
				endCal.set(Calendar.MONTH, ((Calendar)obj[1]).get(Calendar.MONTH));	
				endCal.set(Calendar.DATE, ((Calendar)obj[1]).get(Calendar.DATE));
				
				while(!startCal.after(endCal)){	//循环考勤计算时间（按年）
					if(startCal.get(Calendar.YEAR)==endCal.get(Calendar.YEAR)){//如果计算考勤开始时间的年份等于计算考勤结束时间的年份
						tempCalendar.set(Calendar.YEAR, endCal.get(Calendar.YEAR));
						tempCalendar.set(Calendar.MONTH, endCal.get(Calendar.MONTH));
						tempCalendar.set(Calendar.DATE, endCal.get(Calendar.DATE));
					}else{	
						tempCalendar.set(Calendar.YEAR,startCal.get(Calendar.YEAR));
						tempCalendar.set(Calendar.MONTH, 11);	//11表示的是12月份
						tempCalendar.set(Calendar.DATE ,31);		
					}
					this.calHolidayRecord(attendList,startCal, tempCalendar, obj[2].toString(), obj[3].toString(), userList);
					startCal.add(Calendar.YEAR, 1);		//加一年	
					startCal.set(Calendar.MONTH, 0);	//0表示的是1月份
					startCal.set(Calendar.DATE, 1);
				}	
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"calHolidayRecordOfAllPerson(计算所有人员的法定假日考勤)"});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Dec 1, 2009 2:55:51 PM
	 * Desc:	根据计算开始时间和结束时间来获取确切的法定假日有效时间
	 * param:   holidayList 法定假日列表
	 * param:	startDate 计算开始时间
	 * param:	endDate   计算结束时间
	 */
	public List<Object[]>  getActualHoliday(List<ToaAttendHoliday> holidayList,Date startDate,Date endDate){
		try{
			SimpleDateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
			df1.format(startDate);		//考勤计算开始时间
			Calendar startCal=df1.getCalendar();
			
			SimpleDateFormat df2=new SimpleDateFormat("yyyy-MM-dd");
			df2.format(endDate);		//考勤计算结束时间
			Calendar endCal=df2.getCalendar();
			
			SimpleDateFormat df3;
			SimpleDateFormat df4;
			
			Calendar enableTime;	
			Calendar disableTime;		
			
			ToaAttendHoliday holiday;	//法定假日对象
			Object[] obj;
			List<Object[]> list=new ArrayList<Object[]>();
			for(int i=0;i<holidayList.size();i++){//循环法定假日列表	
				holiday=(ToaAttendHoliday)holidayList.get(i);
				obj=new Object[4];
				df3=new SimpleDateFormat("yyyy-MM-dd");
				df4=new SimpleDateFormat("yyyy-MM-dd");
				df3.format(holiday.getHenableTime());
				enableTime=df3.getCalendar();		//假日的有效时间
				if(startCal.after(enableTime)){		//如果计算开始时间在假日的有效时间之后
					obj[0]=startCal;
				}else{
					obj[0]=enableTime;
				}
				if(holiday.getHdisableTime()!=null){	//如果失效时间不为空
					df4.format(holiday.getHdisableTime());
					disableTime=df4.getCalendar();		//假日的失效时间
					if(disableTime.after(endCal)){		//如果假日的失效时间在考勤计算结束时间之后
						obj[1]=endCal;
					}else{
						obj[1]=disableTime;
					}
				}else{
					obj[1]=endCal;
				}
				obj[2]=holiday.getHolidayStime();
				obj[3]=holiday.getHolidayEtime();
				list.add(obj);
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 30, 2009 9:49:09 AM
	 * Desc:	
	 * param:   startCal 		计算开始时间
	 * param: 	tempCalendar	计算结束时间
	 * param: 	start			法定假日开始时间
	 * param: 	end				法定假日结束时间
	 * param: 	personIds		所有人员ID
	 */
	public void calHolidayRecord(List<ToaAttendRecord> attendList,Calendar startCal,Calendar tempCalendar,String start,String end,List<ToaBasePerson> userList)throws SystemException,ServiceException{
		try{
			Date date;
			ToaAttendRecord record;
			List<ToaAttendRecord> recordList;		//考勤信息列表
				
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			sdf.parse("2009-01-01");
			Calendar tempStartCal=sdf.getCalendar();//临时变量
			
			//组装法定假日开始时间（加上年份）
			SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
			if(start.substring(0,start.indexOf("-")).length()!=4){	
				start="2009-"+start;
			}
			sdf1.parse(start);
			Calendar c1=sdf1.getCalendar();			
			c1.set(Calendar.YEAR, startCal.get(Calendar.YEAR));
			
			//组装法定假日结束时间
			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
			if(end.substring(0,end.indexOf("-")).length()!=4){
				end="2009-"+end;
			}
			sdf2.parse(end);			
			Calendar c2=sdf2.getCalendar();			
			c2.set(Calendar.YEAR, startCal.get(Calendar.YEAR));
		
			if(startCal.after(c2)||tempCalendar.before(c1)){	//如果计算开始时间晚于法定假日结束时间,或者计算结束时间早于假日开始时间	
			}else{
				if(startCal.after(c1)){		//如果考勤计算的开始时间在法定假日的开始时间之后
					c1.set(Calendar.MONTH, startCal.get(Calendar.MONTH));	
					c1.set(Calendar.DAY_OF_MONTH, startCal.get(Calendar.DAY_OF_MONTH));
				}
				if(tempCalendar.before(c2)){//如果考勤计算的结束时间在法定假日结束时间之前
					c2.set(Calendar.MONTH, tempCalendar.get(Calendar.MONTH));	
					c2.set(Calendar.DAY_OF_MONTH, tempCalendar.get(Calendar.DAY_OF_MONTH));
				}
				for(ToaBasePerson person:userList){//循环人员
					tempStartCal.set(Calendar.YEAR,c1.get(Calendar.YEAR));
					tempStartCal.set(Calendar.MONTH, c1.get(Calendar.MONTH));
					tempStartCal.set(Calendar.DAY_OF_MONTH, c1.get(Calendar.DAY_OF_MONTH));
					while(!tempStartCal.after(c2)){	//循环法定假日
						date=registerManager.getDateByString(tempStartCal.getTime(), "yyyy-MM-dd");		//时间格式化
						recordList=registerManager.getRecordByUserId(person.getPersonid(), date, date);	//查找当天的考勤记录
						if(recordList!=null&&recordList.size()>0){		//如果有考勤记录,不做任何处理，重新计算的时候也不做任何处理	
						}else{	
							record=new ToaAttendRecord();
							record.setUserId(person.getPersonid());
							record.setUserName(person.getPersonName());
							record.setOrgId(person.getBaseOrg().getOrgid());
							record.setOrgName(person.getBaseOrg().getOrgName());
							record.setShouldAttendHours(new BigDecimal(0));
							record.setAttendTime(date);
							record.setAttendanceType(FDJ);
							record.setIsCalcuAbsence(NOTCALABSENCE);
							record.setIsCalcu(ISCAL);
							attendList.add(record);
						}
						tempStartCal.add(Calendar.DAY_OF_MONTH, 1);	//增加一天
					}
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"calHolidayRecord(保存法定假日考勤)"});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 29, 2009 4:51:07 PM
	 * Desc:	删除某一天加班考勤明细
	 * param:
	 */
	public void delJiabanRecord(Date attendTime,String personId){
		try{
			String hql="from ToaAttendRecord t where t.attendTime=? and t.userId=? and (t.attendanceTypeId is null or t.attendanceTypeId=?)";
			List<ToaAttendRecord> recordList=registerDao.find(hql, attendTime,personId,jiaban);
			if(recordList!=null&&recordList.size()>0){
				registerDao.delete(recordList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 29, 2009 4:51:07 PM
	 * Desc:	删除某一天假日考勤明细
	 * param:
	 */
	public void delHolidayRecord(Date attendTime,String personId){
		try{
			String hql="from ToaAttendRecord t where t.attendTime=? and t.userId=? and t.attendanceType='"+FDJ+"'";
			List<ToaAttendRecord> recordList=registerDao.find(hql, attendTime,personId);
			if(recordList!=null&&recordList.size()>0){
				registerDao.delete(recordList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 29, 2009 4:51:07 PM
	 * Desc:	删除某一天考勤明细
	 * param:
	 */
	public void delRecord(Date attendTime,String personId){
		try{
			String hql="from ToaAttendRecord t where t.attendTime=? and t.userId=?";
			List<ToaAttendRecord> recordList=registerDao.find(hql, attendTime,personId);
			if(recordList!=null&&recordList.size()>0){
				registerDao.delete(recordList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Dec 2, 2009 9:12:46 AM
	 * Desc:
	 * param:
	 */
	public List<ToaAttendRecord> getHolidayRecord(Date attendTime,String personId){
		try{
			String hql="from ToaAttendRecord t where t.attendTime=? and t.userId=? and t.attendanceType='"+FDJ+"'";
			List<ToaAttendRecord> recordList=registerDao.find(hql, attendTime,personId);
			return recordList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 
	 * Description:获取计算状态为未计算的考勤记录
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 10, 2010 5:06:57 PM
	 */
	public List<ToaAttendRecord> getRecordOfNotCal(Date startDate,Date endDate,List<ToaBasePerson> userlist)throws SystemException,ServiceException{
		try{
			String ids="";
			for(ToaBasePerson person:userlist){
				ids+=",'"+person.getPersonid()+"'";
			}
			ids=ids.substring(1);
			String hql="from ToaAttendRecord t where t.attendTime>=? and t.attendTime<=? and t.userId in ("+ids+") and t.isCalcu<>? ";
			List<ToaAttendRecord> recordList=registerDao.find(hql,startDate,endDate,ISCAL);
			return recordList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"getRecordOfNotCal(获取计算状态为未计算的考勤记录)"});
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 29, 2009 10:40:26 PM
	 * Desc:	填充考勤计算时间段内没有考勤记录的考勤明细信息
	 * param:  	personIds 人员ID
	 * param： 	startDate 考勤计算开始时间
	 * param：   endDate   考勤计算结束时间
	 */
	public void calAbsenceRecordOfAllPerson(String flag,List<ToaAttendRecord> attendList,List<ToaBasePerson> personList,Date startDate,Date endDate)throws SystemException,ServiceException{
		try{
			SimpleDateFormat sdf1=new SimpleDateFormat();
			sdf1.format(startDate);
			Calendar startCalendar=sdf1.getCalendar();
			
			SimpleDateFormat sdf2=new SimpleDateFormat();
			sdf2.format(endDate);
			Calendar endCalendar=sdf2.getCalendar();
			
			SimpleDateFormat sdf3=new SimpleDateFormat();
			sdf3.format(startDate);
			Calendar temp=sdf3.getCalendar();			//临时变量，用于累加
	
			int count=0;
			String personId;
			Date date;	
			ToaAttendRecord record;
			List<ToaAttendSchedule> schedule;
			List<ToaAttendSchedGroup> groupList;
			Map<String,Integer> map=new HashMap<String,Integer>();	
			map=registerManager.getAttendRecordCount(personList, startDate, endDate,flag);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date calStime=registerManager.getDateByString(startCalendar.getTime(), "yyyy-MM-dd");	//计算开始时间
			Date calEtime=registerManager.getDateByString(endCalendar.getTime(), "yyyy-MM-dd");		//计算结束时间
			
			for(ToaBasePerson person:personList){
				attendList=new ArrayList<ToaAttendRecord>();
				personId=person.getPersonid();
				System.out.println(personId);
				groupList=groupManager.getEffectiveGroupByUserAndTime(personId,calStime,calEtime);	//获取人员在考勤计算时间内的有效班次
				if(groupList==null||groupList.size()==0){	//如果该人员该时间段没有排班，不进行下面的计算
					continue;
				}
				temp.set(Calendar.YEAR,startCalendar.get(Calendar.YEAR));
				temp.set(Calendar.MONTH,startCalendar.get(Calendar.MONTH));
				temp.set(Calendar.DATE,startCalendar.get(Calendar.DATE));
				while(!temp.after(endCalendar)){	//循环考勤计算时间段
					date=registerManager.getDateByString(temp.getTime(), "yyyy-MM-dd");
					if(map!=null){	
						count=map.get(personId+sdf.format(date))==null?0:map.get(personId+sdf.format(date));
					}
					if(count>0){//如果找到了考勤明细记录
					}else{//如果没找到了考勤明细记录
						schedule=registerManager.getSchedulesByUser(groupList,personId,date);//查找班次
						if(schedule!=null&&schedule.size()>0){		 //找到班次,这种情况是当某个人员当天一天都没登记的情况，算旷工
							registerManager.saveAttendRecord(schedule, person, date, ISCAL);
						}else{	//没找到班次	
							record=new ToaAttendRecord();
							record.setUserId(personId);
							record.setUserName(person.getPersonName());
							record.setOrgId(person.getBaseOrg().getOrgid());
							record.setOrgName(person.getBaseOrg().getOrgName());
							record.setShouldAttendHours(new BigDecimal(0));
							record.setAttendTime(date);
							record.setAttendanceType(XIUXI);
							record.setIsCalcuAbsence(NOTCALABSENCE);
							record.setIsCalcu(ISCAL);
							registerDao.save(record);
						}
					}
					temp.add(Calendar.DATE, 1);
				}
				registerDao.flush (); 
				registerDao.clear (); 
			}	
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"calAbsenceRecordOfAllPerson(填充考勤计算时间段内没有考勤记录的考勤明细信息)"});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
