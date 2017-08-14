package com.strongit.oa.mylog;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaLog;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * 日志管理action
 * @author 胡丽丽
 * @date 2009-12-15
 *
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "myLog.action", type = ServletActionRedirectResult.class) })
public class MyLogAction extends BaseActionSupport {

	private Page<ToaLog> page=new Page<ToaLog>(FlexTableTag.MAX_ROWS,true);
	private ToaLog model=new ToaLog();//日志BO
	private String logId;//日志ID
	private String disLogo;//搜索
	private MyLogManager manager;
	public Page<ToaLog> getPage() {
		return page;
	}

	public void setPage(Page<ToaLog> page) {
		this.page = page;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getDisLogo() {
		return disLogo;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}

	@Autowired
	public void setManager(MyLogManager manager) {
		this.manager = manager;
	}

	/**
	 * 逻辑删除日志
	 * @author 胡丽丽
	 * @date 2009-12-15
	 */
	@Override
	public String delete() throws Exception {
		String[] ids=logId.split(",");
		for(int i=0;i<ids.length;i++){
			if(ids[i]!=null&&!"".equals(ids[i])){
				manager.deleteObj(ids[i]);
			}
		}
		return RELOAD;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return "show";
	}

	@Override
	public String list() throws Exception {
		if(disLogo!=null&&!"".equals(disLogo)){
			page=manager.search(page, model);
		}else{
			page=manager.getAllToaLog(page);
		}
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if(logId!=null&&!"".equals(logId)){
			model=manager.getToaLogByID(logId);
		}else{
			model=new ToaLog();
		}

	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public ToaLog getModel() {
		// TODO Auto-generated method stub
		return model;
	}

}
