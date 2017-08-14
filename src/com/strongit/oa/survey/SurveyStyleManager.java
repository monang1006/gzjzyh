package com.strongit.oa.survey;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSurveytableStyle;
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
public class SurveyStyleManager {

	private GenericDAOHibernate<ToaSurveytableStyle, String> styleDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.styleDao = new GenericDAOHibernate(sessionFactory,
				ToaSurveytableStyle.class);
	}

	public Page<ToaSurveytableStyle> getStylePages(
			Page<ToaSurveytableStyle> page) throws SystemException,
			ServiceException {
		String hql = "from ToaSurveytableStyle t order by t.styleId DESC";
		page = this.styleDao.find(page, hql, new Object[0]);
		return page;
	}

	public List<ToaSurveytableStyle> getStylePages() throws SystemException,
			ServiceException {
		String hql = "from ToaSurveytableStyle t order by t.styleId DESC";

		try {
			return this.styleDao.find(hql, new Object[0]);
		} catch (ServiceException e) {
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
	public ToaSurveytableStyle getStyle(String styleId) throws SystemException,
			ServiceException {
		try {
			return ((ToaSurveytableStyle) this.styleDao.get(styleId));
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "投票调查" });
		}
	}

	public void addStyle(ToaSurveytableStyle style) throws SystemException,
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
			e.printStackTrace();
			throw new ServiceException("delete.error", new Object[] { "投票删除" });
		}
	}
    /**
     * 修改
     * @param survey
     * @throws SystemException
     * @throws ServiceException
     */
	public void editStyle(ToaSurveytableStyle survey) throws SystemException,
			ServiceException {
		this.styleDao.update(survey);
	}

}
