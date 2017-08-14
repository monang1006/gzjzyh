package com.strongit.oa.autoencoder;

import java.net.URLDecoder;
import java.util.Date;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaCodemanage;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "codemanage.action", type = ServletActionRedirectResult.class) })
public class CodemanageAction extends BaseActionSupport<ToaCodemanage>{

	private static final long serialVersionUID = 3632209395283884209L;
	
	@Autowired private CodemanageManager codemanageManager;
	
	private String codeName;
	
	private Date starttime;
	
	private Date endtime;
	
	private String state;
	
	private String ids;
	
	private Page<ToaCodemanage> page = new Page<ToaCodemanage>(FlexTableTag.MAX_ROWS, true);

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		if(codemanageManager.delObj(ids)){
			return this.renderText("true");
		}else{
			return this.renderText("false");
		}
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		if(codeName!=null){
			codeName = URLDecoder.decode(codeName,"utf-8");
		}
		page = codemanageManager.getPageList(page, codeName, starttime, endtime, state,null);
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

	public ToaCodemanage getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<ToaCodemanage> getPage() {
		return page;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		if(endtime==null){
			
		}else{
			endtime.setHours(23);
			endtime.setMinutes(59);
			endtime.setSeconds(59);
		}
		this.endtime = endtime;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}
