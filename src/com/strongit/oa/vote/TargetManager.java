package com.strongit.oa.vote;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TOaVote;
import com.strongit.oa.bo.TOaVoteTarget;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author piyu
 * Jun 18, 2010
 *
 */
@Service
@Transactional
public class TargetManager {
	private GenericDAOHibernate<TOaVoteTarget, String> targetDao;
	protected static final Log logger = LogFactory.getLog(TargetManager.class);
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.targetDao = new GenericDAOHibernate(sessionFactory,
				TOaVoteTarget.class);
	}
	/**
	 * 判断用户或手机号是否是调查对象
	 */
	@Transactional(readOnly=true)
	public boolean isTarget(String vid,String userid,String mobile){
		StringBuffer hql=new StringBuffer("select count(*) from TOaVoteTarget target where target.vid=? ");
		Object[]params=new Object[2];
		params[0]=vid;
		if(userid!=null&&userid.length()>0){
			hql.append(" and target.userid=? ");
			params[1]=userid;
		}else{
			hql.append(" and target.mobile=? ");
			params[1]=mobile;
		}
		
		try {
			if(Integer.parseInt(targetDao.createQuery(hql.toString(),params).list().get(0).toString())>0){
				return true;
			}else{		
				return false;
			}
		} catch (Exception e) {
			logger.error("查询调查对象",e);
			throw new ServiceException("find.error", new Object[] { "查询调查对象" });
		}
	}
	/**
	 * 取出问卷下的所有调查对象
	 */
	@Transactional(readOnly=true)
	public List<TOaVoteTarget> getAllTargetByVid(String vid){
		List<TOaVoteTarget> result=null; 
		String hql="from TOaVoteTarget target where target.vid=? ";
		Object[]params=new Object[]{vid};
		try {
			result= targetDao.createQuery(hql,params).list();
		}  catch (Exception e) {
			logger.error("查询调查对象",e);
			throw new ServiceException("find.error", new Object[] { "查询调查对象" });
		}
		return result ;
		
	}
	/**
	 * 批量加入调查对象
	 */
	public void addTargets(TOaVote vote,String userids,String usernames,String mobiles){
		TOaVoteTarget target=null;
		if(userids==null&&mobiles==null){
			return ;
		}else{
			if(vote.getVid()==null){
				return ;
			}
			//删除问卷的所有调查对象
			delTargetByVid(vote.getVid());
		}
		
		if(userids!=null&&userids.trim().length()>0){
			//页面类型
			String []tmpids=userids.split(",");
			String []tmpname=usernames.split(",");
		    for(int i=0;i<tmpids.length;i++){
		    	if(tmpids[i].trim().length()<1){
		    		continue;
		    	}
		    	target=new TOaVoteTarget();
		    	target.setVid(vote.getVid());
		    	target.setUserid(tmpids[i].trim());
		    	target.setUsername(tmpname[i].trim());
		    	this.addTarget(target);
		    }
		}
		if(mobiles!=null&&mobiles.trim().length()>0){
			//手机短信类型，可以设置用户和手机号
			String []tmpmobiles=mobiles.split(",");
			 for(int i=0;i<tmpmobiles.length;i++){
				 if(tmpmobiles[i].trim().length()<1){
					 continue;
				 }
				 target=new TOaVoteTarget();
				 target.setVid(vote.getVid());
				 target.setMobile(tmpmobiles[i].trim());
				 this.addTarget(target);
			 }
		}
	}
	/**
	 * 加入一个调查对象
	 */
	public void addTarget(TOaVoteTarget target){
		try {
			targetDao.insert(target);
		} catch (Exception e) {
			logger.error("添加调查对象",e);
			throw new ServiceException("find.error", new Object[] { "添加调查对象" });
		}
	}
	/**
	 * 按问卷ID删除调查对象
	 */
	public void delTargetByVid(String vid){
		try {
			String hql="delete from TOaVoteTarget target where target.vid=?";
			Object []params=new Object[]{vid};
			targetDao.createQuery(hql,params).executeUpdate();
		} catch (Exception e) {
			logger.error("删除调查对象",e);
			throw new ServiceException("find.error", new Object[] { "删除调查对象" });
		}
	}
	
	/**
	 * 按主键批量删除调查对象
	 */
	public void delTargets(String tids){
		if(tids==null){
			return ;
		}
		try {
			targetDao.delete(tids.split(","));
		} catch (Exception e) {
			logger.error("删除调查对象",e);
			throw new ServiceException("find.error", new Object[] { "删除调查对象" });
		}
	}
	/**
	 * 分页查询调查对象
	 */
	public Page<TOaVoteTarget> getTargetPage(Page<TOaVoteTarget> page, String hql ,Object[]params)  throws SystemException,ServiceException { 
		try {
			page=targetDao.find(page, hql, params);
		} catch (Exception e) {
			logger.error("调查对象查询",e);
			throw new ServiceException("find.error", new Object[] { "调查对象查询" });
		}
		return page;
	} 
}
