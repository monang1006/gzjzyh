package com.strongit.oa.attendance.autoset;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attendance.attendmaintain.MaintainManager;
import com.strongit.oa.attendance.gather.GatherManager;
import com.strongit.oa.attendance.register.RegisterManager;
import com.strongit.oa.bo.ToaAttendPlan;
import com.strongit.oa.bo.ToaAttendRecord;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.personnel.baseperson.PersonManager;

/**
 * 自动执行计划service
 * @author 胡丽丽
 *
 */
@Service
@Transactional
public class PlanService {
	private List<ToaBasePerson> userlist=new ArrayList<ToaBasePerson>();
	private Map<String, BigDecimal> recordMap=new HashMap<String, BigDecimal>();
	private List<ToaSysmanageProperty> columnList;		//信息项列表
	@Autowired private GatherManager gatherManager;		//汇总MANAGER
	@Autowired private RegisterManager registerManager;	//考勤计算manager
	@Autowired private InfoItemManager itemManager;
	@Autowired private PersonManager personManager;		//人事人员manager
	@Autowired private MaintainManager mainManager;		//考勤维护Manager
	@Autowired private PlanManager planManager;			//考勤执行计划MANAGER
	private Date beginTime;			//汇总出勤开始时间
	private Date endTime;			//汇总出勤结束时间
	private String userIds="";		//人员ID
   
	
	/**
	 * 
	 * @return
	 */
	public Date getRunDate(){
		ToaAttendPlan plan=planManager.getPlan();
	    String plantime=	plan.getPlanTime();
	    Date newdate=new Date();
	    plantime=getStringBydate(newdate, "yyyy-MM-dd")+" "+plantime;
	    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	    try {
			Date date=df.parse(plantime);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 当前时间和系统设置开始时间、结束时间的先后顺序
	 * @author hull
	 * @date 2010-03-22
	 * @param plan
	 * @return
	 */
	public boolean afterDate(ToaAttendPlan plan,Date now){
		//判断当前时间是否在开始时间之后，结束时间之前
		if(!now.before(plan.getPlanStime())&&(plan.getPlanEtime()==null||!now.after(plan.getPlanEtime()))){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 执行考勤计算汇总计划
	 * @author hull
	 * @date 2010-03-19
	 */
	public void runSave(){
		ToaAttendPlan plan=planManager.getPlan();
		Date now =new Date();		//当前时间
		String nowStr=this.getStringBydate(now, "yyyy-MM-dd");
		now=this.getdatetime(nowStr);
		if(plan.getPlanId()==null||"".equals(plan.getPlanId())){	//是否设置了计划

		}else{
			if("1".equals(plan.getPlanFrequency())){	//判断是否自动计算考勤
				if(afterDate(plan,now)){	//当前时间是否在开始时间和结束时间段内
					if(isAccord(plan.getPlanStime())){	//是否到了计算时间点 是：计算 否：不进行计算
						this.cal(plan); 	//计算考勤
					}
				}
				if("1".equals(plan.getIsAuto())){		//是否自动汇总考勤	
					//if(isPlantime(plan)){				//是否到了计算时间点 是：汇总 否：不进行汇总
						Date planLtime=plan.getPlanLtime();	//最后一次计算考勤时间
						if(planLtime!=null){
							planLtime.setHours(0);
							planLtime.setMinutes(0);
							planLtime.setSeconds(0);
							long nowtime=now.getTime();
							long lasttime=planLtime.getTime();
							int intervalTime=1;					//默认隔一天汇总
							if(plan.getIntervalTime()!=null){	//获取间隔时间
								intervalTime=plan.getIntervalTime().intValue();
							}
							int day=(int)(nowtime-lasttime)/1000/60/60/24;
							if(day==intervalTime){
								this.save(plan);
							}
						}
					//}
				}
			}
		}
	}

	
	/*
	 * 
	 * Description:判断当天是否是计算考勤的时间
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 13, 2010 9:38:28 AM
	 */
	public boolean isAccord(Date stime){
		String s=this.getStringBydate(stime, "dd");
		String now=this.getStringBydate(new Date(), "dd");
		if(s.equals(now)){
			return true;
		}else{
			return false;
		}
		
	}
	/**
	 * 获取要查询的人员缺勤详细情况
	 * @author 胡丽丽
	 * @date:2010-03-18
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
	 * 获取所有用户ID
	 * @author 胡丽丽
	 * @date 2010-03-18
	 */
	private void getAllUserId(){
		userIds="";
		userlist=personManager.getAllPerson();
		for(ToaBasePerson user:userlist){
			if(user.getPersonid()!=null&&!"".equals(user.getPersonid())){
				userIds=userIds+","+user.getPersonid();
			}
		}
		userIds=userIds.substring(1);
	}
	
	/**
	 * 保存汇总
	 * @author 胡丽丽
	 * @date:2010-03-18
	 */
	public void save(ToaAttendPlan plan) {
		try {
			getAllUserId();//获取所有用户
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String planLtime=sdf.format(plan.getPlanLtime());	//格式化最后一次计算考勤时间

			SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
			sdf1.parse(planLtime);
			Calendar begin=sdf1.getCalendar();
			begin.add(Calendar.MONTH, -1);
			beginTime=begin.getTime();

			SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
			sdf2.parse(planLtime);
			Calendar end=sdf2.getCalendar();
			end.add(Calendar.DATE, -1);
			endTime=end.getTime();

			this.getTypeRecord();
			int sizeuser = userIds.split(",").length;
			List<ToaAttendRecord> recordlist = registerManager.getRecordByTimeAndUser(userIds, beginTime, endTime);
			if (recordlist != null && recordlist.size() > 0) {
				columnList = itemManager.getAllCreatedItemsByValue("T_OA_ATTEND_STATISTICS");
				gatherManager.save(recordlist, recordMap, beginTime, endTime,sizeuser);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算考勤
	 * @author hull
	 * @date 2010-03-18
	 */
	public void cal(ToaAttendPlan plan){
		//获取所有用户
		getAllUserId();
		//计算考勤
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		sdf.format(new Date());
		Calendar calStime=sdf.getCalendar();

		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
		sdf2.format(new Date());
		Calendar calEtime=sdf2.getCalendar();

		calStime.add(Calendar.MONTH, -1);
		calEtime.add(Calendar.DATE,-1);
		
		mainManager.calAttend(calStime.getTime(),calEtime.getTime(),userlist, "0");
		//修改最后计算时间
		plan.setPlanLtime(new Date());
		planManager.save(plan);
	}
	
	/**
	 * 当前月份是否是大月：是true 否false
	 * @param month
	 * @return
	 */
	public boolean getIsBigMonth(String month){
		if("02".equals(month)&&"04".equals(month)&&"06".equals(month)&&"09".equals(month)&&"11".equals(month)){
			return false;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断当前年份是否是闰年 是true 否false
	 * @param year
	 * @return
	 */
	public boolean getIsLeapyear(int year){
		if(year%4==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 把字符串转换成日期类型
	 * @author  hull
	 * @date 2010-03-19
	 * @param time
	 * @return Date
	 */
	private Date getdatetime(String time){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date=df.parse(time);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 把日期转换成字符串
	 * @param date
	 * @param temp
	 * @return
	 */
	private String getStringBydate(Date date,String temp){
		SimpleDateFormat df=new SimpleDateFormat(temp);
		return df.format(date);
	}
	
	/**
	 * 判断当前时间是否是执行考勤自动计算时间点
	 * @author hull
	 * @date 2010-03-19
	 * @param plan
	 * @return 是：true 否：false
	 */
	private boolean isPlantime(ToaAttendPlan plan){
		SimpleDateFormat df=new SimpleDateFormat("HH:mm");
		String newtime=df.format(new Date());
		if(newtime.equals(plan.getPlanTime())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断距离上次计算时间是否有一个月,或计算日期是考勤设置开始日期点
	 * @param planLtime
	 * @return
	 */
	private boolean isMonth(ToaAttendPlan plan){
		Date planLtime=plan.getPlanLtime();
		String day=getStringBydate(new Date(), "dd");//当前日
		if(planLtime==null){//判断上次计算日期是否为空
			if(plan.getPlanStime()!=null){//开始时间是否为空
				String sday=getStringBydate(plan.getPlanStime(), "dd");//开始日
				if(sday.equals(day)){
					return true;
				}
			}else{
				return false;
			}
		}
		String ltime=getStringBydate(planLtime, "yyyy-MM");//上次计算时间
		String newtime=getStringBydate(new Date(), "yyyy-MM");//当前时间
		String oldday=getStringBydate(planLtime, "dd");//上次计算日
	    long ldate= planLtime.getTime();
	    long newdate=(new Date()).getTime();
		if(ltime.equals(newtime)){//是否在同一年月
			return false;
		}else if(newdate>ldate&&day.equals(oldday)){//当前月份是否大于上次计算时间月份
			return true;
		}else{
			return false;
		}
	}
}
