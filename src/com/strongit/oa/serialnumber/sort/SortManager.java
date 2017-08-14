/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-7-09
 * Autour: hull
 * Version: V2.0.3
 * Description： 文号类型manager
 */
package com.strongit.oa.serialnumber.sort;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSerialnumberSort;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;


@Service
@Transactional
public class SortManager {

	private GenericDAOHibernate<ToaSerialnumberSort, String> sortDAO;
	
	/**
	 * setSessionFactory
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		sortDAO=new GenericDAOHibernate<ToaSerialnumberSort, String>(
				sessionFactory,ToaSerialnumberSort.class);
	}
	
	/**
	 * 添加文号类型
	 * @param sort
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveSort(ToaSerialnumberSort sort)throws SystemException,
	ServiceException {
		try {
			sortDAO.save(sort);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "添加文号类型Manager" });
		}
	}
	
	/**
	 * 根据ID删除文号类型
	 * @param sortId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteSort(String sortId) throws SystemException,ServiceException{
		try {
		    /**拆分文号类型ID*/
			String[] id=sortId.split(",");
			for(int i=0;i<id.length;i++){//循环删除文号类型
			sortDAO.delete(id[i]);
			}
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"删除文号类型Manager"});
		}
		
	}
	
	/**
	 * 根据ID查询文号类型
	 * @param sortId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaSerialnumberSort getSortById(String sortId)throws SystemException,ServiceException{
		try {
			return sortDAO.get(sortId);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据ID查找文号类型"});
		}
	}
	
	/**
	 * 查询所有文号类型
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaSerialnumberSort> getAllSort(Page<ToaSerialnumberSort> page)throws SystemException,ServiceException{
		try {
			return sortDAO.findAll(page);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"查找所有文号类型"});
		}
	}
	
	/**
	 * 查询所有文号类型列表
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaSerialnumberSort> getAllSort()throws SystemException,ServiceException{
		try {
			return sortDAO.findAll();
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"查找所有文号类型"});
		}
	}
	
	/**
	 * 根据搜索文号类型列表
	 * @param page
	 * @param model
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaSerialnumberSort> getSortsByProperty(Page<ToaSerialnumberSort> page ,ToaSerialnumberSort model)throws SystemException,ServiceException{
		try {
			String hql="from ToaSerialnumberSort t  ";
			List<Object> list=new ArrayList<Object>();
			
			if(model.getSortName()!=null&&!"".equals(model.getSortName())){//判读文号类型是否为空
				hql=hql+" where t.sortName like ?";
				list.add("%"+model.getSortName()+"%");
				
				if(model.getSortAbbreviation()!=null&&!"".equals(model.getSortAbbreviation())){//文号代字是否为空
					hql=hql+" and t.sortAbbreviation like ?";
					list.add("%"+model.getSortAbbreviation()+"%");
					
				}
				if(model.getSortTime()!=null){//编辑时间是否为空
					hql=hql+" and t.sortTime>=?";
					list.add(model.getSortTime());
				}
			}else if(model.getSortAbbreviation()!=null&&!"".equals(model.getSortAbbreviation())){//文号类型名称为空时，判断文号代字是否为空
				hql=hql+" where t.sortAbbreviation like ?";
				list.add("%"+model.getSortAbbreviation()+"%");
			
				if(model.getSortTime()!=null){//时间是否为空
					hql=hql+" and t.sortTime>=?";
					list.add(model.getSortTime());
				}
			}else if(model.getSortTime()!=null){//时间是否为空
				hql=hql+" where t.sortTime>=?";
				list.add(model.getSortTime());
			}
			Object[] obj=new Object[list.size()];
			for(int i=0;i<list.size();i++){
				obj[i]=list.get(i);
			}
			return sortDAO.find(page, hql, obj);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"查找所有文号类型"});
		}
	}
	
	/**
	 * 
	 * @author zhengzb
	 * @desc 根据文号类型名，判断当前所添加的文号类型名，是否存在
	 * 2010-5-11 上午11:44:27 
	 * @param regulationName 文号名
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getIsSortName(String regulationName) throws SystemException,ServiceException {
		try {
			String hql="select count(*) from ToaSerialnumberSort t  ";
			hql=hql+" where t.sortName like ?";					
		return Integer.parseInt(sortDAO.findUnique(hql,regulationName).toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据文号规则名称查询文号规则是否存在"});	
		}
	}
}
