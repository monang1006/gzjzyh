package com.strongit.oa.outlink;

import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaOutlink;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * <p>Title: OutLinkAction.java</p>
 * <p>Description: 外部链接对应Action类</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2010-03-22 21:59:43
 * @version  1.0
 */

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/outLink.action") })
public class OutLinkAction extends BaseActionSupport<ToaOutlink>{

	private static final long serialVersionUID = -2218947290264667678L;
	
	@Autowired private OutlinkManager outLinkManager;
	
	private Page<ToaOutlink> page = new Page<ToaOutlink>(FlexTableTag.MAX_ROWS, true);
	
	private String id;
	
	private String des;
	
	private String address;
	
	private String type;
	
	private Date beginDate;
	
	private Date endDate;
	
	ToaOutlink model = new ToaOutlink();

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		if(id!=null&&!"".equals(id)){
			if(outLinkManager.delObjects(id)){
				return this.renderText("true");
			}else{
				return this.renderText("false");
			}
		}else{
			return this.renderText("false");
		}
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return "input";
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		Date d1 = new Date();
		if(endDate != null){
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_MONTH, 1); // 加一天
			d1 = cal.getTime();
		}
		page = outLinkManager.getPageList(page, des, beginDate, d1);
		return "list";
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		if(id!=null&&!"".equals(id)){
			model.setOutlinkId(id);
		}
		Date date = new Date();
		model.setOutlinkDes(des);
		model.setOutlinkAddress(address);
		model.setOutlinkDate(date);
		model.setOutlinkType(type);
		if(outLinkManager.saveObj(model)){
			return this.renderText("true");
		}else{
			return this.renderText("false");
		}
	}
	
	public String edit() throws Exception{
		model = outLinkManager.getObjectById(id);
		return "change";
	}
	
	public String getUrl() throws Exception{
		return null;
	}

	public ToaOutlink getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public void setModel(ToaOutlink model) {
		this.model = model;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Page<ToaOutlink> getPage() {
		return page;
	}

	public void setPage(Page<ToaOutlink> page) {
		this.page = page;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDes() {
		return des;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
