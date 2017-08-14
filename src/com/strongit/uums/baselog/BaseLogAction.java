/*******************************************************************************
 * file name:BaseLogAction.java<br>
 * version: <br>
 * description:  <br>
 * copyright: <br>
 * //////////////////////////////////////////////////////// <br>
 * creator: 胡志文 <br>
 * create date: Feb 23, 2012 <br>
 ******************************************************************************/
package com.strongit.uums.baselog;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseLog;
import com.strongit.oa.common.user.IUserService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "baseLog.action", type = ServletActionRedirectResult.class) })
public class BaseLogAction extends BaseActionSupport<TUumsBaseLog>{
	
	private Page<TUumsBaseLog> page = new Page<TUumsBaseLog>(FlexTableTag.MAX_ROWS, true);
	
	@Autowired
	private IUserService userService;
	
	private TUumsBaseLog baseLog;
	
	private String logId;
	
	private Map resultMap = new HashMap();
	
	/*
	 * 查询相关属性
	 */
	private String username;// 操作用户

	private String desc;// 操作描述
	
	private String result;//操作结果

	public Page<TUumsBaseLog> getPage() {
		return page;
	}

	public void setPage(Page<TUumsBaseLog> page) {
		this.page = page;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		page = userService.queryBaseLog(page, username, desc, result);
		
		resultMap.put("1", "成功");
		resultMap.put("0", "失败");
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String view() throws Exception{
		baseLog = userService.getLogInfoById(logId);
		if("1".equals(baseLog.getLogOpresult())){
			result = "成功";
		}else if("0".equals(baseLog.getLogOpresult())){
			result = "失败";
		}
		return "view";
	}

	public TUumsBaseLog getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public TUumsBaseLog getBaseLog() {
		return baseLog;
	}

	public void setBaseLog(TUumsBaseLog baseLog) {
		this.baseLog = baseLog;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public Map getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map resultMap) {
		this.resultMap = resultMap;
	}

}
