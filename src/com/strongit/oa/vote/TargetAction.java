package com.strongit.oa.vote;

import java.util.ArrayList;
import java.util.List;
import com.strongit.oa.vote.util.VoteConst;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOaVote;
import com.strongit.oa.bo.TOaVoteTarget;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author piyu
 * Jun 18, 2010
 *
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "target.action", type = ServletActionRedirectResult.class) })
public class TargetAction extends BaseActionSupport {
     
	private TOaVoteTarget target=new TOaVoteTarget();
	private Page<TOaVoteTarget> page = new Page<TOaVoteTarget>(FlexTableTag.MAX_ROWS,true);
	private String vid;
	private String userids;
	private String usernames;
	private String mobiles;
	private String tids ;
	private VoteManager votemanager ;
	private TargetManager tgmanager ;
	/**
	 * 设置调查对象
	 */
	public String setTarget()throws Exception {
		tgmanager.addTargets(votemanager.getVote(vid),userids,usernames,mobiles);
		return this.renderHtml("<script language='javascript'>window.close()</script>");
	}
	
	public String delTarget()throws Exception{
		tgmanager.delTargets(tids);
		return list();
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
	 * 分页查询调查问卷下的所有调查对象
	 */
	@Override
	public String list() throws Exception {
		StringBuffer hql=new StringBuffer("from TOaVoteTarget target where 1=1 ");
		List<Object>params=new ArrayList<Object>();
		if(vid!=null){
			hql.append(" and target.vid=?");
			params.add(vid);
		}
		hql.append(" order by target.tid desc ");
		page=tgmanager.getTargetPage(page, hql.toString(), params.toArray(new Object[params.size()]));
		
		if(page.getTotalCount()<0){
			page.setTotalCount(0);
			page.setPageNo(0);
		}
		return "list";
	}
	/**
	 * 加入调查对象前，获取已经设置的调查对象
	 */
    public String init_setTarget(){
    	List<TOaVoteTarget> list_target=tgmanager.getAllTargetByVid(vid);
    	
    	StringBuffer  array_userids=new StringBuffer();
    	StringBuffer  array_usernames=new StringBuffer();
    	StringBuffer  array_mobiles=new StringBuffer();
    	for(TOaVoteTarget target:list_target){
    		if(target.getUserid()!=null){
    			array_userids.append(target.getUserid());
    			array_userids.append(",");
    		}
    	    if(target.getUsername()!=null){
    	    	array_usernames.append(target.getUsername());
    	    	array_usernames.append(",");
    	    }
    	    if(target.getMobile()!=null){
    	    	array_mobiles.append(target.getMobile());
    	    	array_mobiles.append(",");
    		}
    	}
    	if(array_userids.length()>0){
    		userids=array_userids.substring(0, array_userids.lastIndexOf(","));
    	}
    	if(array_usernames.length()>0){
    		usernames=array_usernames.substring(0, array_usernames.lastIndexOf(","));
    	}
    	if(array_mobiles.length()>0){
    		mobiles=array_mobiles.substring(0, array_mobiles.lastIndexOf(","));
    	}
    	
    	TOaVote vote=votemanager.getVote(vid);
    	if(VoteConst.vote_type_page.equals(vote.getType())){
    		// 网页参与
    		
    		return "addpage";
    	}else{
    		//手机短信参与
    		
    		return "addsms";
    	}
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

	public TOaVoteTarget getTarget() {
		return target;
	}

	public void setTarget(TOaVoteTarget target) {
		this.target = target;
	}

	public Page<TOaVoteTarget> getPage() {
		return page;
	}

	public void setPage(Page<TOaVoteTarget> page) {
		this.page = page;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getUserids() {
		return userids;
	}

	public void setUserids(String userids) {
		this.userids = userids;
	}

	public String getUsernames() {
		return usernames;
	}

	public void setUsernames(String usernames) {
		this.usernames = usernames;
	}

	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}

	public String getTids() {
		return tids;
	}

	public void setTids(String tids) {
		this.tids = tids;
	}

	public TargetManager getTgmanager() {
		return tgmanager;
	}
	@Autowired
	public void setTgmanager(TargetManager tgmanager) {
		this.tgmanager = tgmanager;
	}

	public VoteManager getVotemanager() {
		return votemanager;
	}
	@Autowired
	public void setVotemanager(VoteManager votemanager) {
		this.votemanager = votemanager;
	}

}
