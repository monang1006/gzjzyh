package com.strongit.oa.attendance.register;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaAttendRecord;
import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.personnel.personorg.PersonOrgManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * 考勤明细action
 * @author 胡丽丽
 * @date:2009-12-02
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "record.action", type = ServletActionRedirectResult.class) })
public class RecordAction extends BaseActionSupport {

	private Page<ToaAttendRecord> page=new Page<ToaAttendRecord>(FlexTableTag.MAX_ROWS,true);
	private Page<MyRecord> recordpage=new Page<MyRecord>(FlexTableTag.MAX_ROWS,true);
	/** 考勤明细BO*/
	private ToaAttendRecord model=new ToaAttendRecord();
	/** 考勤明细ID*/
	private String attendId;
	/** 考勤明细manager*/
	private RegisterManager manager;
	/** 部门ID*/
	private String orgId;
    /** 考勤明细列表*/
	private List<ToaAttendRecord> reList;
	private String codeType;
    /** 机构列表*/
    private List<ToaBaseOrg> orgList;
	/** 机构manager*/
	private PersonOrgManager orgmanager;
	/** 考勤明细合计后的总数据*/
	private List<MyRecord> recordList;
	private String disLogo; //搜索
	private int pagesize;   //每页行数
	private int pages;		//总页数
	private int newpage;	//当前页
	private int begin;		//开始行
	private int end;		//结束行
	private int bancisize;
	private String userName;//用户姓名
	private Date beginTime;	//开始时间
	private Date endTime;	//结束时间
	private String isGather;//是否汇总信息查看明细
	private String userId;	//用户ID
	private String begindate;//开始时间
	private String enddate;	//结束时间

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsGather() {
		return isGather;
	}

	public void setIsGather(String isGather) {
		this.isGather = isGather;
	}

	public int getBancisize() {
		return bancisize;
	}

	public void setBiancisize(int bancisize) {
		this.bancisize = bancisize;
	}

	public String getDisLogo() {
		return disLogo;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}

	public Page<ToaAttendRecord> getPage() {
		return page;
	}

	public void setPage(Page<ToaAttendRecord> page) {
		this.page = page;
	}

	public String getAttendId() {
		return attendId;
	}

	public void setAttendId(String attendId) {
		this.attendId = attendId;
	}

	@Autowired
	public void setManager(RegisterManager manager) {
		this.manager = manager;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    /**
     * 个人某时间段的考勤明细信息
     * @author Administrator胡丽丽
     * @date:2009-12-02
     * @return
     * @throws Exception
     */
	public String userRecordList()throws Exception {
		try {
			if(newpage==0){//是否首次加载
				newpage=1;
			}
			if(pagesize==0){//设置默认每页显示行数
				pagesize=20;
			}
			if("".equals(begindate)||begindate==null){
				begindate=gettime("yyyy-MM-dd",beginTime);//开始时间
			}
			if("".equals(enddate)||enddate==null){
				enddate=gettime("yyyy-MM-dd",endTime);//结束时间
			}
			begin=(newpage-1)*pagesize;//开始
			end=begin+pagesize-1;//结束行
			reList=manager.getRecordByUserId( userId, beginTime, endTime);
			if(reList!=null&&reList.size()>0){//判断LIST是否为空
			    bancisize=manager.getCountBanciByUser(userId);//查询每天上多少个班次
			}else{
				bancisize=0;
			}
			getRequest().setAttribute("bancisize",bancisize);
			recordZuhe();
			//计算总行数
			pages=recordList.size()%pagesize==0?recordList.size()/pagesize:recordList.size()/pagesize+1;
			//当前页是否大于总页数 是：当前页赋为首页
			if(newpage>pages){
				newpage=1;
			}
			getRequest().setAttribute("count",recordList.size());
			recordpage.setResult(recordList);
			recordpage.setTotalCount(recordList.size());
			List<Object> list=null;
			int j=0;
			for(MyRecord rec:recordList){
				if(rec.getWorkList().size()<bancisize){
					j=rec.getWorkList().size();
					list=rec.getWorkList();
					for(int i=0;i<bancisize-j;i++){
						list.add("");
					}
				}
			}
			return "showgather";
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "noUserRecord";
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
     * 考勤明细信息
     * @author Administrator胡丽丽
     * @date:2009-12-02
     * @return
     * @throws Exception
     */
	@Override
	public String list() throws Exception {
		try {
			if(newpage==0){//是否首次加载
				newpage=1;
			}
			if(pagesize==0){//设置默认每页显示行数
				pagesize=20;
			}
			begin=(newpage-1)*pagesize;//开始行
			end=begin+pagesize-1;//结束行
			if(disLogo!=null&&!"".equals(disLogo)){//是否是搜索
				reList=manager.getRecordByOrgId(orgId,userName,beginTime,endTime);
			}else{
				Date date=new Date();
				String month=getStringBydate(date, "MM");
				String year=getStringBydate(date, "yyyy");
				beginTime=getdatetime(year+"-"+month+"-01");
				endTime=date;
				reList=manager.getRecordByOrgId(orgId,userName,getdatetime(year+"-"+month+"-01"),date);
			}
			if(reList==null||reList.size()==0){//是否为空
				bancisize=0;
			}else{
				bancisize=manager.getCountBanci(orgId);
			}
			getRequest().setAttribute("bancisize",bancisize);
			recordZuhe();
			//计算总行数
			pages=recordList.size()%pagesize==0?recordList.size()/pagesize:recordList.size()/pagesize+1;
			getRequest().setAttribute("count",recordList.size());
			recordpage.setResult(recordList);
			recordpage.setTotalCount(recordList.size());
			List<Object> list=null;
			int j=0;//班趟数量
			for(MyRecord rec:recordList){
				if(rec.getWorkList().size()<bancisize){
					j=rec.getWorkList().size();
					list=rec.getWorkList();
					for(int i=0;i<bancisize-j;i++){
						list.add("");
					}
				}
			}
			return SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "norecord";
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}
	/**
     * 机构树
     * @author Administrator胡丽丽
     * @date:2009-12-02
     * @return
     * @throws Exception
     */
	public String orgtree()throws Exception{
		try {
			codeType = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		//	orgList = orgManager.getOrgsByIsdel("0");
			orgList = orgmanager.getOrgsByIsdel("0");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "orgtree";
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * 根据日期计算考勤明细列表
     * @author Administrator胡丽丽
     * @date:2009-12-02
     * @return
     * @throws Exception
     */
	public void recordZuhe(){
		Map<String,Date> map=new HashMap<String, Date>();//开始计算的人员
		recordList=new ArrayList<MyRecord>();//考勤明细
		if(recordList==null||recordList.size()==0){//判断是否为空
			List<Object> obj=new ArrayList<Object>();
			MyRecord myreco=null;
			for(ToaAttendRecord record:reList){
				//该人员的这一天是否已经开始计算 是：true 否：false
				if(map.get(record.getUserId())!=null&&(record.getAttendTime()==map.get(record.getUserId())||record.getAttendTime().equals(map.get(record.getUserId())))){
					String time="";
					if(record.getMregisterTime()!=null){//上班时间
						time=gettime("MM/dd HH:mm",record.getMregisterTime())+"-";
					}
					if(record.getAregisterTime()!=null){//下班时间
					    time=time+	"-"+gettime("MM/dd HH:mm",record.getAregisterTime());
					}
					myreco.setAttendId(myreco.getAttendId()+","+record.getAttendId());
					//迟到
					if(record.getAttendLaterTime()!=null){
					myreco.setAttendLaterTime(myreco.getAttendLaterTime()+record.getAttendLaterTime().intValue());
					}
					//早退
					if(record.getAttendEarlyTime()!=null){
					myreco.setAttendEarlyTime(record.getAttendEarlyTime().intValue()+myreco.getAttendEarlyTime());
					}
					//是否计算缺勤
					if("0".equals(record.getIsCalcuAbsence())){
					myreco.setAbsenceHours(BigDecimal.valueOf(record.getAbsenceHours().floatValue()+myreco.getAbsenceHours().floatValue()).setScale(1,   BigDecimal.ROUND_HALF_UP));
					}
					//申请类型是否为空
					if(record.getAttendanceType()!=null&&!"".equals(record.getAttendanceType())){
					myreco.setAttendanceType(myreco.getAttendanceType()+","+record.getAttendanceType());
					}
					if(record.getAttendanceTypeId()!=null&&!"".equals(record.getAttendanceTypeId())){//类型ID是否为空
					myreco.setAttendanceTypeId(myreco.getAttendanceTypeId()+","+record.getAttendanceTypeId());
					}
					//描述
				    if(record.getAttendDesc()!=null&&!"".equals(record.getAttendDesc())){
					myreco.setAttendDesc(myreco.getAttendDesc()+","+record.getAttendDesc());
				    }
				    if(record.getIsCalcu()!=null&&"0".equals(record.getIsCalcu())){//是否计算
				    	myreco.setIsCalcu(record.getIsCalcu());
				    }
				    //应出勤
				    if(record.getShouldAttendHours()!=null&&!"".equals(record.getShouldAttendHours())){
				    	myreco.setShouldAttendHours(BigDecimal.valueOf(record.getShouldAttendHours().floatValue()+myreco.getShouldAttendHours().floatValue()).setScale(1,   BigDecimal.ROUND_HALF_UP));
					}
				    obj.add(time);
					myreco.setWorkList(obj);
				}else{
					myreco=new MyRecord();
					if("0".equals(record.getIsCalcuAbsence())){
						myreco.setAbsenceHours(record.getAbsenceHours());//缺勤时间
					}else{
						myreco.setAbsenceHours(BigDecimal.valueOf(0));
					}
					if(record.getAttendanceType()!=null&&!"".equals(record.getAttendanceType())){//类型名称
						myreco.setAttendanceType(record.getAttendanceType());
					}else{
						myreco.setAttendanceType("");
					}
					if(record.getAttendanceTypeId()!=null&&!"".equals(record.getAttendanceTypeId())){//类型ID是否为空
						myreco.setAttendanceTypeId(record.getAttendanceTypeId());
					}else{
						myreco.setAttendanceTypeId("");
					}
					if(record.getAttendDesc()!=null&&!"".equals(record.getAttendDesc())){//说明描述是否为空
					myreco.setAttendDesc(record.getAttendDesc());
					}else{
						myreco.setAttendDesc("");
					}
					if(record.getAttendEarlyTime()!=null){
					  myreco.setAttendEarlyTime(record.getAttendEarlyTime().intValue());
					}
					//迟到
					if(record.getAttendLaterTime()!=null){
					  myreco.setAttendLaterTime(record.getAttendLaterTime().intValue());
					}
					myreco.setAttendTime(gettime("yyyy-MM-dd",record.getAttendTime()));
					myreco.setIsCalcu(record.getIsCalcu());
					myreco.setIsCalcuAbsence(record.getIsCalcuAbsence());
					myreco.setOrgId(record.getOrgId());
					myreco.setOrgName(record.getOrgName());
					myreco.setShouldAttendHours(record.getShouldAttendHours());//应出勤
					myreco.setUserId(record.getUserId());
					myreco.setUserName(record.getUserName());
					myreco.setAttendId(record.getAttendId());
					obj=new ArrayList<Object>();
					String time="";
					if(record.getMregisterTime()!=null){
						time=gettime("MM/dd HH:mm",record.getMregisterTime())+"-";
					}
					if(record.getAregisterTime()!=null){
					    time=time+	"-"+gettime("MM/dd HH:mm",record.getAregisterTime());
					}
					obj.add(time);
					myreco.setWorkList(obj);
					recordList.add(myreco);
					map.put(record.getUserId(), record.getAttendTime());
				}
			}
		}
	}
	/**
	 * 格式化时间
	 * @author 胡丽丽
	 * @date:2009-11-19
	 * @param time
	 * @return
	 */
	private String gettime(String temp,Date time){
		SimpleDateFormat df = new SimpleDateFormat(temp);//设置日期格式  
		String time1=df.format(time);
		return time1;
	}
	public ToaAttendRecord getModel() {
		// TODO Auto-generated method stub
		return model;
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

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public List<MyRecord> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<MyRecord> recordList) {
		this.recordList = recordList;
	}

	public List<ToaAttendRecord> getReList() {
		return reList;
	}

	public void setReList(List<ToaAttendRecord> reList) {
		this.reList = reList;
	}

	public Page<MyRecord> getRecordpage() {
		return recordpage;
	}

	public void setRecordpage(Page<MyRecord> recordpage) {
		this.recordpage = recordpage;
	}

	@Autowired
	public void setOrgmanager(PersonOrgManager orgmanager) {
		this.orgmanager = orgmanager;
	}

	public List<ToaBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<ToaBaseOrg> orgList) {
		this.orgList = orgList;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getNewpage() {
		return newpage;
	}

	public void setNewpage(int newpage) {
		this.newpage = newpage;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}


}
