package com.strongit.oa.workinspect.worktype;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOsWorkType;
import com.strongit.oa.bo.TOsWorktask;
import com.strongit.oa.bo.TOsWorktaskSend;
import com.strongit.oa.workinspect.worksend.IWorkSendService;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.security.UserInfo;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "workType.action", type = ServletActionRedirectResult.class) })
public class WorkTypeAction extends BaseActionSupport<TOsWorkType>{

	
	@Autowired
	private WorkTypeManager manager;
	private String worktypeId;
	private String worktypeValue;// 字典值
	private String worktypeName;// 名称
	private String worktypeDemo;// 备注
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Page<TOsWorkType> page = new Page<TOsWorkType>(15, true);
	
	private TOsWorkType model = new TOsWorkType();
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		if(worktypeId!=null){
		manager.delete(worktypeId);
		}
		return RELOAD;
	}

	/**
	 * 新增分类
	 * @author: qibh
	 *@return
	 *@throws Exception
	 * @created: 2013-6-3 上午03:04:25
	 * @version :5.0
	 */
	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		if(worktypeId!=null){
			model=manager.fingById(worktypeId);
		}
		return INPUT;
	}

	public String getWorktypeId() {
		return worktypeId;
	}

	public void setWorktypeId(String worktypeId) {
		this.worktypeId = worktypeId;
	}

	public String getWorktypeValue() {
		return worktypeValue;
	}

	public void setWorktypeValue(String worktypeValue) {
		this.worktypeValue = worktypeValue;
	}

	public String getWorktypeName() {
		return worktypeName;
	}

	public void setWorktypeName(String worktypeName) {
		this.worktypeName = worktypeName;
	}

	/**
	 * 获取所有分类
	 * @author: qibh
	 *@return
	 *@throws Exception
	 * @created: 2013-6-3 上午03:04:25
	 * @version :5.0
	 */
	public String list() throws Exception {
		manager.getAllType(page,model);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public String edit() throws Exception{
		return "edit";
    }
	/**
	 * 保存工作分类
	 * @author: qibh
	 *@return
	 *@throws Exception
	 * @created: 2013-6-3 上午03:04:25
	 * @version :5.0
	 */
	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		if(worktypeId!=null){
			manager.updateType(model);
		}else{
			manager.saveType(this.model);
		}
		return renderHtml("<script>window.returnValue='OK';window.close();</script>");
	}
	public TOsWorkType getModel() {
		return this.model;
	}

	public Page<TOsWorkType> getPage() {
		return page;
	}

	public void setPage(Page<TOsWorkType> page) {
		this.page = page;
	}
	
}