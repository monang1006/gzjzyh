package com.strongit.oa.vote;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOaVoteLog;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author piyu
 * Jun 24, 2010
 * 问卷参与记录
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "voteLog.action", type = ServletActionRedirectResult.class) })
public class VoteLogAction extends BaseActionSupport {

	@Autowired private VoteLogManager votelogmanager;	
	private Page<TOaVoteLog> page = new Page(FlexTableTag.MAX_ROWS, true);
	private String vid ;
	/**
	 * 分页查询问卷参与记录
	 */
	@Override
	public String list() throws Exception {
		String hql="from TOaVoteLog votelog where votelog.vote.vid=? order by votelog.vote_date desc";
		Object[]params=new Object[]{vid};
		page=votelogmanager.getVoteLogPage(page, hql, params);
		return "list";
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

	

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<TOaVoteLog> getPage() {
		return page;
	}

	public void setPage(Page<TOaVoteLog> page) {
		this.page = page;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

}
