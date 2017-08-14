/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-19
 * Autour: zhangli
 * Version: V1.0
 * Description： 信息项分类管理MANAGER
 */
package com.strongit.oa.infotable.infotype;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongit.oa.bo.ToaSysmanagePropertype;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class InfoTypeManager {
	private GenericDAOHibernate<ToaSysmanagePropertype, java.lang.String> infoTypeDao;

	private InfoSetManager manager;

	@Autowired
	public void setManager(InfoSetManager manager) {
		this.manager = manager;
	}

	/**
	 * @roseuid 49479C940148
	 */
	public InfoTypeManager() {

	}

	/**
	 * 注册DAO
	 * 
	 * @param sessionFactory
	 * @roseuid 49464D3601BA
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		infoTypeDao = new GenericDAOHibernate<ToaSysmanagePropertype, java.lang.String>(
				sessionFactory, ToaSysmanagePropertype.class);
	}

	/**
	 * 根据信息集编号获取对应所有信息项分类列表
	 * 
	 * @param infoSetCode
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 49471EA903BE
	 */
	public List getAllTypes(String infoSetCode) 
			throws SystemException,ServiceException{
		try{
			return infoTypeDao
				.find(
						"from ToaSysmanagePropertype t where t.toaSysmanageStructure.infoSetCode=?  order by t.propertypeName asc ",
						infoSetCode);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项分类列表"});
		}
	}

	/**
	 * 根据信息集编号和相应查询条件获取对应所有信息项分类分页列表
	 * 
	 * @param infoSetCode
	 *            信息集编号
	 * @param page
	 *            分页对象
	 * @param infotype
	 *            信息项分类对象
	 * @return com.strongmvc.orm.hibernate.Page
	 * @roseuid 49471ED70302
	 */
	public Page<ToaSysmanagePropertype> getAllTypes(String infoSetCode,
			Page<ToaSysmanagePropertype> page, ToaSysmanagePropertype infotype) 
			throws SystemException,ServiceException{
		try{
			Object[] obj = new Object[1];
			StringBuffer hql = new StringBuffer(
					"from ToaSysmanagePropertype t where t.toaSysmanageStructure.infoSetCode=?");
			int i = 0;
			if (infotype.getPropertypeName() != null
					&& !infotype.getPropertypeName().equals("")) {
				if(infotype.getPropertypeName().indexOf("%")!=-1){
					String propertypeName=infotype.getPropertypeName();
					propertypeName=propertypeName.replaceAll("%","/%");
					hql.append("and t.propertypeName like '%" + propertypeName + "%' ESCAPE '/' ");
				}else if(infotype.getPropertypeName().indexOf("_")!=-1){
					String propertypeName=infotype.getPropertypeName();
					propertypeName=propertypeName.replaceAll("_","/_");
					hql.append("and t.propertypeName like '%" + propertypeName + "%' ESCAPE '/' ");
			    }else {
			    	hql.append(" and t.propertypeName like ?");
					obj[i] = "%" + infotype.getPropertypeName() + "%";
					i++;
				}
				
			}
           hql.append(" order by t.propertypeName asc ");
			if (i == 0) {
				page = infoTypeDao.find(page, hql.toString(), infoSetCode);
			} else if (i == 1) {
				page = infoTypeDao.find(page, hql.toString(), infoSetCode, obj[0]);
			}
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项分类分页列表"});
		}
	}

	/**
	 * 获取信息项分类对象
	 * 
	 * @param propertypeId
	 *            信息项分类编号
	 * @return com.strongit.oa.bo.ToaSysmanagePropertype
	 * @roseuid 49471F1E0024
	 */
	public ToaSysmanagePropertype getInfoType(String propertypeId) 
			throws SystemException,ServiceException{
		try{
			return infoTypeDao.get(propertypeId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项分类对象"});
		}
	}

	/**
	 * 获取信息项分类名称
	 * 
	 * @param propertypeId
	 *            信息项分类编号
	 * @return com.strongit.oa.bo.ToaSysmanagePropertype
	 * @roseuid 49471F1E0024
	 */
	public String getInfoTypeName(String propertypeId) 
			throws SystemException,ServiceException{
		try{
			ToaSysmanagePropertype property=this.getInfoType(propertypeId);
			if(property!=null){
				return property.getPropertypeName();
			}else{
				return null;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息项分类名称"});
		}
	}

	/**
	 * 保存信息项分类对象
	 * 
	 * @param infotype
	 * @roseuid 49471F500237
	 */
	public void saveInfoType(ToaSysmanagePropertype infotype) 
			throws SystemException,ServiceException{
		try{
			infoTypeDao.save(infotype);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"信息项分类对象"});
		}
	}

	/**
	 * 批量删除信息项分类对象
	 * 
	 * @param propertypeId
	 * @roseuid 49471F5900D0
	 */
	public void delInfoType(String propertypeId) 
			throws SystemException,ServiceException{
		try{
			String[] str = propertypeId.split(",");
			for (int i = 0; i < str.length; i++) {
				infoTypeDao.delete(str[i]);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"信息项分类对象"});
		}
	}

	/**
	 * 通过信息集编号获取信息集名称
	 * 
	 * @param infoSetCode
	 *            信息集编号
	 * @return java.util.List
	 * @roseuid 49464D780286
	 */
	public String getInfoSetName(String infoSetCode) 
			throws SystemException,ServiceException{
		try{
			return manager.getNameByCode(infoSetCode);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"信息集名称"});
		}
	}
	/**
	 * 验证信息集下的信息项分类名称重名情况
	 * @author:
	 * @date:
	 * @param propertypeName
	 * @return
	 */
	public boolean isInfoTypeUsed(String infoSetCode,String propertypeName) throws ServiceException{
		try{
			Assert.hasText(propertypeName, "propertypeName不能为空！");
			String hql = "select count(*) from ToaSysmanagePropertype t where t.toaSysmanageStructure.infoSetCode=? and t.propertypeName=?";
			Long count = infoTypeDao.findLong(hql, infoSetCode,propertypeName);
			if(count > 0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			LogPrintStackUtil.logException(e);
			LogPrintStackUtil.error("校验信息项分类名称失败！");
			throw new ServiceException("校验信息项分类名称失败！");
		}
	}
	
	
}
