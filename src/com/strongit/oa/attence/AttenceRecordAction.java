package com.strongit.oa.attence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaAttendanceRecord;
import com.strongit.oa.bo.ToaAttendanceTime;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.service.UserService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author 汤世风
 * @company Strongit Ltd. (C) copyright
 * @date 2012年9月17日
 * @comment 考勤时间设定Action
 */

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "attenceRecord.action", type = ServletActionRedirectResult.class) })
public class AttenceRecordAction extends BaseActionSupport {

	private ToaAttendanceRecord model = new ToaAttendanceRecord();
	private ToaAttendanceTime attendanceTime = new ToaAttendanceTime();
	private AttenceRecordManager attenceRecordManager;
	@Autowired
	private AttenceTimeManager attenceTimeManager;

	private Date startTime;
	private Date endTime;
	private String userID;
	private String remarks;
	private Page<ToaAttendanceRecord> page = new Page<ToaAttendanceRecord>(5,
			true);

	// 分页设置
	public Integer curpage;
	public Integer unitpage = 10;

	private UserService userService;

	public AttenceRecordAction() {

	}

	public ToaAttendanceRecord getModel() {
		return model;
	}

	public void setModel(ToaAttendanceRecord model) {
		this.model = model;
	}

	public AttenceRecordManager getAttenceRecordManager() {
		return attenceRecordManager;
	}

	@Autowired
	public void setAttenceRecordManager(
			AttenceRecordManager attenceRecordManager) {
		this.attenceRecordManager = attenceRecordManager;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		model=attenceRecordManager.getTodayAttenceInfo(userService.getCurrentUser().getUserId());
		attendanceTime = attenceTimeManager.getLastRecord();
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		// 默认查询当前月的考勤记录
		if (startTime == null && endTime == null) {
			Calendar now = Calendar.getInstance();
			Date date = new Date();
			// 本月1日
			now.setTime(date);
			now.set(Calendar.DATE, 1);
			startTime = now.getTime();
			// 本月末日
			now.setTime(date);
			now.add(Calendar.MONTH, 1);
			now.set(Calendar.DATE, 1);
			now.add(Calendar.DATE, -1);
			endTime = now.getTime();
		}
		String userID = userService.getCurrentUser().getUserId();

		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		page = attenceRecordManager.getAttenceList(page, userID, startTime,
				endTime);

		int totalRecord = page.getTotalCount();
		int totalPage = totalRecord % unitpage == 0 ? totalRecord / unitpage
				: totalRecord / unitpage + 1; // 计算总页数

		JSONObject jsonObj = new JSONObject();
		// 根据jqGrid对JSON的数据格式要求给jsonObj赋值
		jsonObj.put("curpage", curpage);// 当前页
		jsonObj.put("totalpages", totalPage);// 总页数
		jsonObj.put("totalrecords", totalRecord);// 总记录数

		// List<TInfoBasePublish> result = page.getResult();
		JSONArray rows = new JSONArray();
		if (page.getResult() != null && page.getResult().size() > 0) {
			List<ToaAttendanceRecord> attenceList = page.getResult();

			JSONObject obj = null;
			for (ToaAttendanceRecord r : attenceList) {
				obj = new JSONObject();
				obj.put("id", r.getId());
				obj.put("userName", r.getUserName());
				obj.put("time", "" + new SimpleDateFormat("yyyy-MM-dd").format(r.getTime()));

				if (r.getWorktime() != null && !"".equals(r.getWorktime())) {
					obj.put("worktime", " " + r.getWorktime());
				} else {
					obj.put("worktime", "");
				}

				if (r.getLeavetime() != null && !"".equals(r.getLeavetime())) {
					obj.put("leavetime", " " + r.getLeavetime());
				} else {
					obj.put("leavetime", "");
				}

				if (!"".equals(r.getLatetime())) {
					obj.put("latetime", " " + r.getLatetime());
				} else {
					obj.put("latetime", "");
				}

				if (!"".equals(r.getLeaveearly())) {
					obj.put("leaveearly", " " + r.getLeaveearly());
				} else {
					obj.put("leaveearly", "");
				}

				if (!"".equals(r.getNowoke())) {
					int t = r.getLatetime()+r.getLeaveearly();
//					obj.put("nowoke", " " + r.getNowoke());
					//矿工时间是迟到时间加早退时间所以
					//旷工时间如果大于工作时间则以工作时间显示旷工时间
					ToaAttendanceTime tadt = attenceTimeManager.getLastRecord();
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					Date wkt = sdf.parse(tadt.getWorktime());
					Date let = sdf.parse(tadt.getLeavetime());
					int s =(int) (let.getTime()-wkt.getTime())/3600000;
					if(s<t/60){
						obj.put("nowoke", " " + s);
					}else{
						obj.put("nowoke", " " + t/60);
					}
				} else {
					obj.put("nowoke", "");
				}

				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);

		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;

	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() throws Exception {
		String state = getRequest().getParameter("state");
		String userID = userService.getCurrentUser().getUserId();
		String userName = userService.getCurrentUser().getUserName();
		ToaAttendanceRecord record = attenceRecordManager
				.getTodayAttenceInfo(userID);
		ToaAttendanceTime attendanceTime = attenceTimeManager.getLastRecord();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Date currentDate = null;
		currentDate = dateFormat.parse(dateFormat.format(new Date()));
		Date workTimeDate = dateFormat.parse(attendanceTime.getWorktime());
		Date leaveTimeDate = dateFormat.parse(attendanceTime.getLeavetime());
		String currentTime = dateFormat.format(new Date());
		if(record==null){
			record = new ToaAttendanceRecord();
			record.setUserId(userID);
			record.setUserName(userName);
			SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");
			Date da = null;
			da = d.parse(d.format(new Date()));//util类型时间
			record.setTime(da);
		}
			// in表示签到
			if ("in".equals(state)) {
				record.setWorktime(currentTime);
				long between = (currentDate.getTime() - workTimeDate.getTime()) / 1000;// 除以1000是为了转换成秒
				long hour = between % (24 * 3600) / 3600;
				long minute = between % 3600 / 60;
				long latetime = hour * 60 + minute;// 迟到多少分钟
				if (latetime > 0) {
					record.setLatetime((int) latetime);
				} else {
					record.setLatetime(0);
				}
				if (remarks != null && !"".equals(remarks)) {
					record.setSremarks(remarks);
				}
				String sIP = getRequest().getRemoteAddr();
				record.setsIP(sIP);
			}
			// out表示签退
			if ("out".equals(state)) {
				record.setLeavetime(currentTime);
				long between = (leaveTimeDate.getTime() - currentDate.getTime()) / 1000;// 除以1000是为了转换成秒
				long hour = between % (24 * 3600) / 3600;
				long minute = between % 3600 / 60;

				long latetime = hour * 60 + minute;// 早退多少分钟
				if (latetime > 0) {
					record.setLeaveearly((int) latetime);
				} else {
					record.setLeaveearly(0);
				}
				if (remarks != null && !"".equals(remarks)) {
					record.setQremarks(remarks);
				}
				String qIP = getRequest().getRemoteAddr();
				record.setqIP(qIP);
			}
			attenceRecordManager.updateOrSave(record);

			String SUCCESS_AND_CLOSE ="<html><head><title>info</title></head><body><script type='text/javascript'>parent.window.returnValue='success';parent.window.close();</script></body><html>";

			getResponse().setContentType("text/html");
			getResponse().getWriter().write(SUCCESS_AND_CLOSE);
			
			return null;

	}
	
	
	public String showResult() throws Exception {
		// 默认查询当前月的考勤记录
		if (startTime == null && endTime == null) {
			Calendar now = Calendar.getInstance();
			Date date = new Date();
			// 本月1日
			now.setTime(date);
			now.set(Calendar.DATE, 1);
			startTime = now.getTime();
			// 本月末日
			now.setTime(date);
			now.add(Calendar.MONTH, 1);
			now.set(Calendar.DATE, 1);
			now.add(Calendar.DATE, -1);
			endTime = now.getTime();
		}
		
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		page = attenceRecordManager.getAttenceList(page, userID, startTime,
				endTime);

		int totalRecord = page.getTotalCount();
		int totalPage = totalRecord % unitpage == 0 ? totalRecord / unitpage
				: totalRecord / unitpage + 1; // 计算总页数

		JSONObject jsonObj = new JSONObject();
		// 根据jqGrid对JSON的数据格式要求给jsonObj赋值
		jsonObj.put("curpage", curpage);// 当前页
		jsonObj.put("totalpages", totalPage);// 总页数
		jsonObj.put("totalrecords", totalRecord);// 总记录数

		// List<TInfoBasePublish> result = page.getResult();
		JSONArray rows = new JSONArray();
		if (page.getResult() != null && page.getResult().size() > 0) {
			List<ToaAttendanceRecord> attenceList = page.getResult();
			int i = 1;
			JSONObject obj = null;
			for (ToaAttendanceRecord r : attenceList) {
				obj = new JSONObject();
				obj.put("id", r.getId());
				obj.put("userName", r.getUserName());
				obj.put("time", "" + new SimpleDateFormat("yyyy-MM-dd").format(r.getTime()));

				if (r.getWorktime() != null && !"".equals(r.getWorktime())) {
					obj.put("worktime", " " + r.getWorktime());
					i++;
				} else {
					obj.put("worktime", "");
				}

				if (r.getLeavetime() != null && !"".equals(r.getLeavetime())) {
					obj.put("leavetime", " " + r.getLeavetime());
					i++;
				} else {
					obj.put("leavetime", "");
				}

				if (!"".equals(r.getLatetime())) {
					obj.put("latetime", " " + r.getLatetime());
				} else {
					obj.put("latetime", "");
				}

				if (!"".equals(r.getLeaveearly())) {
					obj.put("leaveearly", " " + r.getLeaveearly());
				} else {
					obj.put("leaveearly", "");
				}

				if (!"".equals(r.getNowoke())) {
					int t = r.getLatetime()+r.getLeaveearly();
//					obj.put("nowoke", " " + r.getNowoke());
					ToaAttendanceTime tadt = attenceTimeManager.getLastRecord();
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					Date wkt = sdf.parse(tadt.getWorktime());
					Date let = sdf.parse(tadt.getLeavetime());
					int s =(int) (let.getTime()-wkt.getTime())/3600000;
					if(s<t/60){
						obj.put("nowoke", " " + s);
					}else{
						obj.put("nowoke", " " + t/60);
					}
					//当打卡时间以及签退时间都为null时  i为3
					if(i==3){
						if(r.getTime().getTime()-new Date().getTime()>=0){
							obj.put("nowoke", "0");
						}
					}
				} else {
					obj.put("nowoke", "");
				}

				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);

		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;

	}
	


	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Page<ToaAttendanceRecord> getPage() {
		return page;
	}

	public void setPage(Page<ToaAttendanceRecord> page) {
		this.page = page;
	}

	public Integer getCurpage() {
		return curpage;
	}

	public void setCurpage(Integer curpage) {
		this.curpage = curpage;
	}

	public Integer getUnitpage() {
		return unitpage;
	}

	public void setUnitpage(Integer unitpage) {
		this.unitpage = unitpage;
	}

	public ToaAttendanceTime getAttendanceTime() {
		return attendanceTime;
	}

	public void setAttendanceTime(ToaAttendanceTime attendanceTime) {
		this.attendanceTime = attendanceTime;
	}

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	
}
