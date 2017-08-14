package com.strongit.oa.relat;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaWorkForm;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class WorkFormManager {

	private GenericDAOHibernate<ToaWorkForm, String> workformDao;

	@SuppressWarnings("unchecked")
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.workformDao = new GenericDAOHibernate(sessionFactory,
				ToaWorkForm.class);
	}

	/**
	 * 添加挂接表单或展现表单
	 * @param workForm
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void addWorkForm(ToaWorkForm workForm) throws SystemException,
			ServiceException {
		try {
			this.workformDao.save(workForm);
		} catch (ServiceException e) {
			throw new ServiceException("find.error",
					new Object[] { "添加流程表单关联" });
		}
	}


	/**
	 * 通过工作流程ID读取流程与表单的挂接或展现关系
	 * @param id
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public ToaWorkForm getWorkForm(String id) throws SystemException,
			ServiceException {
		try {
			ToaWorkForm workForm = null;
			String hql = " from ToaWorkForm t where t.pfId ='" + id + "'";
			List<ToaWorkForm> list = this.workformDao.find(hql);
			if (list.size() != 0) {
				workForm = (ToaWorkForm) list.get(0);
			}

			return workForm;
		} catch (ServiceException e) {
			throw new ServiceException("find.error",
					new Object[] { "添加流程表单关联" });
		}
	}

	/**
	 * 根据流程定义名称获取流程表单挂接对象
	 * @author:邓志城
	 * @date:2009-6-9 下午07:06:31
	 * @param workFlowName 流程定义名称
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public ToaWorkForm getWorkFormByWorkFlowName(String workFlowName)throws SystemException,ServiceException{
		String hql = "from ToaWorkForm t where t.pfName='"+workFlowName+"'";
		List<ToaWorkForm> lst = workformDao.find(hql);
		if(lst.size()>0){
			return lst.get(0);
		}
		return null;
	}
	
}
