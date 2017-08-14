package com.strongit.oa.report;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaReportShowitem;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
@OALogger
public class ReportShowitemManager {
	
	private GenericDAOHibernate<ToaReportShowitem, java.lang.String>showitemDao;
	
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		showitemDao=new GenericDAOHibernate<ToaReportShowitem, String>(session,ToaReportShowitem.class);
	}
	/*
	 * 
	 * 保存报表显示项
	 */
	public String save(ToaReportShowitem model) throws SystemException,ServiceException{
		try {
			showitemDao.save(model);
			return model.getShowitemId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"保存报表显示项"}); 
		}
	}
	/*
	 * 保存列表显示项
	 */
	public void saveList(List<ToaReportShowitem> list)throws SystemException,ServiceException{
		try {
			showitemDao.save(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"保存报表显示项"}); 
		}
	}
	
	/**
	 * 删除报表显示项"
	 * @author zhengzb
	 * @desc 
	 * 2010-12-22 下午05:58:06 
	 * @param showitemId     报表显示项ID
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByShowitemId(String showitemId)throws SystemException,ServiceException{
		try {
			showitemDao.delete(showitemId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"删除报表显示项"}); 
		}
	}
	
	/**
	 * 删除显示项
	 * @author zhengzb
	 * @desc 
	 * 2010-12-22 下午12:18:02 
	 * @param showitemList
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteList(List<ToaReportShowitem> showitemList)throws SystemException,ServiceException{
		try {
			showitemDao.delete(showitemList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除显示项"}); 
		}
	}
	
	
	/**
	 * 根据定义报表ID查询显示项列表
	 * @author zhengzb
	 * @desc 
	 * 2010-12-22 下午03:20:04 
	 * @param definitionId			定义报表ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaReportShowitem> getListByDefinitionId(String definitionId)throws SystemException,ServiceException{
		try {
			String hql =" from ToaReportShowitem t where t.toaReportDefinition.definitionId = ? order by t.showitemSequence asc ";
			
			List<ToaReportShowitem>list=showitemDao.find(hql.toString(),definitionId);
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"根据定义报表ID查询显示项列表！"}); 
		}
	}
	
}
