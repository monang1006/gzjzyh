/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理日程"活动分类"跳转类
 */
package com.strongit.oa.calendar;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaCalendarActivity;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 处理"日程_活动分类"类
 * @Create Date: 2009-2-12
 * @author luosy
 * @version 1.0
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "calendarActivity.action", type = ServletActionRedirectResult.class) })
public class CalendarActivityAction extends BaseActionSupport<ToaCalendarActivity> {

	private Page<ToaCalendarActivity> page = new Page<ToaCalendarActivity>(FlexTableTag.MAX_ROWS, true);
	
	private ToaCalendarActivity model = new ToaCalendarActivity();
	
	private CalendarActivityManager manager;
	
	private String activityId;

	private List<ToaCalendarActivity> activityList;
	
	public CalendarActivityAction() {

	}

	@Autowired
	public void setManager(CalendarActivityManager manager) {
		this.manager = manager;
	}

	public Page<ToaCalendarActivity> getPage() {
		return page;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public void setModel(ToaCalendarActivity model) {
		this.model = model;
	}
	
	/**
	 * author:luosy
	 * description: 删除日程分类
	 * modifyer:
	 * description:
	 * @return
	 */
	public String delete() throws Exception {
		String str = manager.deleteMore(activityId);
		return renderText(str);
	}

	public String input() throws Exception {
		return "input";
	}

	@Override
	public String list() throws Exception {
		activityList = manager.getAllActivity("");
		return SUCCESS;
	}

	protected void prepareModel() throws Exception {
		if(activityId!=null&&!"".equals(activityId)){
			model = manager.getActivityById(activityId);
		}
	}
	
	/**
	 * author:luosy
	 * description: ajax添加新活动分类
	 * modifyer:
	 * description:
	 * @return
	 */
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		try {
			if(!"".equals(model.getActivityName())&&null!=model.getActivityName()){
				model.setActivityName(java.net.URLDecoder.decode(model
						.getActivityName(), "utf-8"));
			}
			if(!"".equals(model.getActivityRemark())&&null!=model.getActivityRemark()){
				model.setActivityRemark(java.net.URLDecoder.decode(model
						.getActivityRemark(), "utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(activityId!=null&&!"".equals(activityId)){//编辑
			
			ToaCalendarActivity activ = manager.saveActivity(model);
			//AJAX返回值 分类ID
			return renderText(activ.getActivityId()+","+activ.getActivityName());
		}else{//添加
			if(manager.isActivityExist(model.getActivityName())){
				return renderText("exist");
			}else{
				ToaCalendarActivity activ = manager.saveActivity(model);
				//AJAX返回值 分类ID
				return renderText(activ.getActivityId()+","+activ.getActivityName());
			}
		}
	}
	
	/**
	 * author:luosy
	 * description: 表单添加新活动分类
	 * modifyer:
	 * description:
	 * @return
	 */
	public String saveForm() throws Exception{
		manager.saveActivity(model);
		return "input";
	}

	public ToaCalendarActivity getModel() {
		return model;
	}

	public List<ToaCalendarActivity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<ToaCalendarActivity> activityList) {
		this.activityList = activityList;
	}
	

}
