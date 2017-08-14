package com.strongit.oa.common.business.processurgency;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TProcessUrgency;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 
 * 流程催办记录manager
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 18, 2012 1:25:21 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.business.processurgency.ProcessUrgencyManager
 */
@Service
@Transactional
@OALogger
public class ProcessUrgencyManager {
	private GenericDAOHibernate<TProcessUrgency, java.lang.String> dao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 注入SessionFactory
	 * 
	 * @param session
	 *            会话对象.
	 */
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		dao = new GenericDAOHibernate<TProcessUrgency, java.lang.String>(
				session, TProcessUrgency.class);
	}

	private void save(TProcessUrgency model, OALogInfo... infos) {
		logger.info("保存催办记录");
		dao.save(model);
	}

	/**
	 * 保存或者跟新催办记录
	 * 
	 * @author 严建
	 * @param model
	 * @param infos
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime May 18, 2012 1:31:54 PM
	 */
	public void updateOrSaveModel(TProcessUrgency model, OALogInfo... infos)
			throws DAOException, ServiceException, SystemException {
		try {
			save(model);// 保存
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 根据流程实例id获取催办记录列表,按催办时间降序排序
	 * 
	 * @description
	 * @author 严建
	 * @param pid
	 * @return
	 * @createTime May 18, 2012 1:33:17 PM
	 */
	@SuppressWarnings("unchecked")
	public List<TProcessUrgency> getProcessUrgencyListByPid(String pid) {
		StringBuilder hql = new StringBuilder(
				"from TProcessUrgency t where t.processInstanceId = ? order by t.urgencyDate desc");
		return (List<TProcessUrgency>) dao.find(hql.toString(), new Long(pid));
	}
	
}
