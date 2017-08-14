package com.strongit.oa.vote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TOaVote;
import com.strongit.oa.bo.TOaVoteQuestion;
import com.strongit.oa.bo.TOaVoteTarget;
import com.strongit.oa.smsplatform.SmsPlatformManager;
import com.strongit.oa.vote.util.VoteConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author piyu
 * Jun 10, 2010
 *
 */
@Service
@Transactional
public class VoteManager {
	private GenericDAOHibernate<TOaVote, String> voteDao;
	private GenericDAOHibernate<TOaVoteQuestion, String> qtDao;
	private GenericDAOHibernate<TOaVoteTarget, String> targetDao;
	@Autowired private SmsPlatformManager smsPlatformManager;//自增码同步
	protected static final Log logger = LogFactory.getLog(VoteManager.class);
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.voteDao = new GenericDAOHibernate(sessionFactory,TOaVote.class);
		this.qtDao = new GenericDAOHibernate(sessionFactory,TOaVoteQuestion.class);
		this.targetDao = new GenericDAOHibernate(sessionFactory,TOaVoteTarget.class);
	}
	
	/**
	 * 获得问题唯一编号
	 */
	@Transactional(readOnly=true)
	public synchronized String getCode(String moduleCode){
		return smsPlatformManager.getFullCode(moduleCode) ;
	}
	/**
	 * 问题唯一编号自增
	 */
	@Transactional(readOnly=true)
	public synchronized void updateCode(String moduleCode){
		smsPlatformManager.updateMsgCode(moduleCode);
	}
	/**
	 * 将所有过期问卷的状态，设置为过期
	 */
	@Transactional(readOnly=true)
	public void checkDate(){
		try {
			Query query=voteDao.createQuery("update TOaVote vote set vote.state=:state where vote.endDate<(sysdate-1)");
			
			query.setParameter("state",VoteConst.vote_gq);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("投票过期",e);
			throw new ServiceException("find.error", new Object[] { "投票过期" });
		}
	}
	
	/**
	 * 分页查询调查问卷
	 */
	@Transactional(readOnly=true)
	public Page<TOaVote> getVotePage(Page<TOaVote> page, String hql ,Object[]params)  throws SystemException,ServiceException { 
		try {
			checkDate();
			page=voteDao.find(page, hql, params);
		} catch (Exception e) {
			logger.error("投票查询",e);
			throw new ServiceException("find.error", new Object[] { "投票查询" });
		}
		return page;
	}
	/**
	 * 按主键获取问卷对象
	 */
	@Transactional(readOnly=true)
	public TOaVote getVote(String vid){
		TOaVote vote=null;
		try {
			checkDate();
			vote=voteDao.get(vid);
		} catch (Exception e) {
			logger.error("投票查询",e);
			throw new ServiceException("find.error", new Object[] { "投票查询" });
		}		
		return vote ;
	}
	/**
	 * 新建问卷
	 */
	public void addVote(TOaVote vote){
		try {
			voteDao.insert(vote);
		} catch (Exception e) {
			logger.error("投票新建",e);
			throw new ServiceException("find.error", new Object[] { "投票新建" });
		}
	}
	/**
	 * 删除问卷
	 */	
	public void delVote(String vids){
		try {
			String hql="delete from TOaVoteQuestion qt where qt.vote.vid in (:myvids)";
			Query query=qtDao.createQuery(hql);
			query.setParameterList("myvids",vids.split(","));
			query.executeUpdate();
			
			hql="delete from TOaVoteTarget target where target.vid in (:myvids)";
			query=qtDao.createQuery(hql);
			query.setParameterList("myvids",vids.split(","));
			query.executeUpdate();
			
			String[] ids = vids.split(",");
			voteDao.delete(ids);
						
		} catch (Exception e) {
			logger.error("投票删除",e);
			throw new ServiceException("find.error", new Object[] { "投票删除" });
		}
	}
	/**
	 * 更新问卷
	 */
	public void updateVote(TOaVote vote){
		try {
			voteDao.update(vote);
		} catch (Exception e) {
			logger.error("投票更新",e);
			throw new ServiceException("find.error", new Object[] { "投票更新" });
		}
	}
	/**
	 * 激活问卷
	 */
	public void enableVote(String vids){
		changeVote(vids,VoteConst.vote_yjh);
	}
	/**
	 * 冻结问卷
	 */
	public void disableVote(String vids){
		changeVote(vids,VoteConst.vote_wjh);
	}
	/**
	 * 激活、冻结问卷
	 */
	private void changeVote(String vids,String state){
		try {
			Query query=voteDao.createQuery("update TOaVote vote set vote.state=:state where vote.vid in (:vids)");
			query.setParameter("state", state);
			query.setParameterList("vids",vids.split(","));
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("投票激活冻结",e);
			throw new ServiceException("find.error", new Object[] { "投票激活冻结" });
		}
	}
}