package com.strongit.oa.vote;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TOaVoteStyle;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 样式 处理类
 * @Create Date: 2009-8-8
 * @version 1.0
 */
@Service
@Transactional
public class VoteStyleManager {

	private GenericDAOHibernate<TOaVoteStyle, String> styleDao;
	protected static final Log logger = LogFactory.getLog(VoteStyleManager.class);
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.styleDao = new GenericDAOHibernate(sessionFactory,	TOaVoteStyle.class);
	}

	@Transactional(readOnly=true)
	public Page<TOaVoteStyle> getStylePages(
			Page<TOaVoteStyle> page) throws SystemException,
			ServiceException {
		String hql = "from TOaVoteStyle t order by t.styleId DESC";
		page = this.styleDao.find(page, hql, new Object[0]);
		return page;
	}

	@Transactional(readOnly=true)
	public List<TOaVoteStyle> getStylePages() throws SystemException,
			ServiceException {
		String hql = "from TOaVoteStyle t order by t.styleId DESC";

		try {
			return this.styleDao.find(hql, new Object[0]);
		} catch (ServiceException e) {
			logger.error("投票调查",e);
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}

	}
    /**
     * 读取样式对象
     * @param styleId
     * @return
     * @throws SystemException
     * @throws ServiceException
     */
	@Transactional(readOnly=true)
	public TOaVoteStyle getStyle(String styleId) throws SystemException,
			ServiceException {
		try {
			return ((TOaVoteStyle) this.styleDao.get(styleId));
		} catch (ServiceException e) {
			logger.error("投票调查",e);
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}

	public void addStyle(TOaVoteStyle style) throws SystemException,
			ServiceException {
		this.styleDao.save(style);
	}
    /**
     * 删除
     * @param styleId
     * @throws SystemException
     * @throws ServiceException
     */
	public void delStyle(String styleId) throws SystemException,
			ServiceException {
		try {
			String[] ids = styleId.split(",");

			for (int i = 0; i < ids.length; ++i) {
				this.styleDao.delete(ids[i]);
			}
			String hql="update TOaVote vote set vote.styleId=null where vote.styleId=? ";
			Object []params=new Object[]{styleId};
			styleDao.createQuery(hql, params).executeUpdate();
		} catch (ServiceException e) {
			throw new ServiceException("delete.error", new Object[] { "投票删除" });
		} catch (SystemException e) {
			throw new ServiceException("delete.error", new Object[] { "投票删除" });
		} catch (Exception e) {
			logger.error("投票调查",e);
			throw new ServiceException("delete.error", new Object[] { "投票删除" });
		}
	}
    /**
     * 修改
     * @param survey
     * @throws SystemException
     * @throws ServiceException
     */
	public void editStyle(TOaVoteStyle survey) throws SystemException,
			ServiceException {
		this.styleDao.update(survey);
	}
	/**
	 * 是否允许删除模板
	 * 如果问卷引用了模板，不允许删除 
	 */
	@Transactional(readOnly=true)
	public boolean canDel(String styleId){
		boolean candel=false;
		String hql="select count(*) from TOaVote vote where vote.styleId=? ";
		Object []params=new Object[]{styleId};
		
		try {
			List result=styleDao.createQuery(hql,params).list();
			if(result!=null&&result.size()>0){
				if(Integer.parseInt(result.get(0).toString())>0){
					candel=false;
				}else{
					candel=true;
				}
			}
		} catch (Exception e) {
			logger.error("删除模板",e);
			throw new ServiceException("find.error", new Object[] { "删除模板" });
		}
		return candel ;
	}
}
