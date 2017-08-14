package com.strongit.doc.monidoc;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.doc.bo.TdocSend;
import com.strongit.doc.bo.TdocStampLog;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 盖章日志管理Action.
 * @author 
 * @company  Strongit Ltd. (C) copyright
 * @date  下午04:52:02
 * @version  2.0.2.3
 * @classpath com.strongit.oa.bookmark.BookMarkAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "moniDoc.action", type = ServletActionRedirectResult.class) })
public class MoniDocAction extends BaseActionSupport {
 
	TdocStampLog model = new TdocStampLog();						//定义MODEL对象.
	
	Page<TdocStampLog> page = new Page<TdocStampLog>(20,true);	//定义列表分页对象	 
	
	@Autowired MoniDocManager manager ;						//注入服务类对象
	private Date startDate	;//开始时间
	private Date endDate	;//结束时间 
	

	@Autowired
	public MoniDocAction() { 
		
		final Calendar ts = Calendar.getInstance();
		endDate = ts.getTime();
 
		ts.set(Calendar.MONTH,ts.getTime().getMonth()-1); 
		startDate = ts.getTime();
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 标签分页列表.
	 */
	@Override
	public String list() throws Exception {
		
//		try {
//			if(model.getDesc() != null){
//				model.setDesc(URLDecoder.decode(model.getDesc(),"utf-8"));
//			}
//		} catch (UnsupportedEncodingException e) {
//			logger.error("转码异常",e);
//		}
		page = manager.getStampLogList(page, model,startDate,endDate);
		return "list" ;
	}

	@Override
	protected void prepareModel() throws Exception {
	}

	/**
	 * 保存
	 */
	@Override
	public String save() throws Exception {
		if(model.getStampLogId()!=null && "".equals(model.getStampLogId())){
			model.setStampLogId(null);
		}
		manager.save(model);
		return RELOAD;
	}

	/**
	 *  保存记录.
	 * @author:
	 * @date: 
	 * @return
	 */
	public void doSave() {
		String ret = "0";
		try{ 
			manager.save(model);				//保存对象
		}catch(Exception e){
			logger.error("保存出错。",e);
			ret = "-1";
		}
		this.renderText(ret);
	}

	
	public TdocStampLog getModel() {
		return model ;
	}

	public Page<TdocStampLog> getPage() {
		return page;
	}

	public void setPage(Page<TdocStampLog> page) {
		this.page = page;
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

}
