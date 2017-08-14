/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-22 16:00
 * Autour: lanlc
 * Version: V1.0
 * Description：公文模板类别manager类
 */
package com.strongit.oa.doctemplate.doctempType;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDoctemplateGroup;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class DocTempTypeManager {
	private GenericDAOHibernate<ToaDoctemplateGroup,java.lang.String> templateDao;
	/**
	 * author:lanlc
	 * description:无参构造方法
	 * modifyer:
	 */
	public DocTempTypeManager(){}
	
	/**
	 * author:lanlc
	 * description:注入sessionFactory
	 * modifyer:
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		templateDao = new GenericDAOHibernate<ToaDoctemplateGroup,java.lang.String>(sessionFactory,ToaDoctemplateGroup.class);
	}
	
	private IUserService userService;
	
	/**
	 * author:lanlc
	 * description:获取指定的公文模板类别
	 * modifyer:
	 * @param docgroupId
	 * @return
	 */
	public ToaDoctemplateGroup getTemplateType(String docgroupId)throws SystemException,ServiceException{
		try{
			return templateDao.get(docgroupId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取公文模板类别"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:删除公文模板类别
	 * modifyer:
	 * @param docgroupId
	 */
	public void delTemplateType(String docgroupId)throws SystemException,ServiceException{
		try{
			templateDao.delete(docgroupId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除公文模板类别"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:保存公文模板
	 * modifyer:
	 * @param model
	 */
	public void saveTemplateType(ToaDoctemplateGroup model)throws SystemException,ServiceException{
		try{
			User user=userService.getCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			if(org!=null){
				model.setDocgroupOrgId(org.getOrgId());
				model.setDocgroupOrgCode(org.getSupOrgCode());
			}
			
			templateDao.save(model);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存公文模板类别"});
		}
	}

	/**
	 * author:lanlc
	 * description:获取所有父节点为0的公文模板类别
	 * modifyer:
	 * @return
	 */
	public List docTempTypeList()throws SystemException,ServiceException{
		try{
			String hql="from ToaDoctemplateGroup t where t.docgroupParentId=?";
			if(userService.isViewSetOpen()) {
				User user=userService.getCurrentUser();
				TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
				boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
				String orgStr="";
				
				if(isView){
					orgStr=" and t.docgroupOrgCode like '"+org.getSupOrgCode()+"%'";		
				}else {
					orgStr=" and t.docgroupOrgId  = '"+org.getOrgId()+"'";
				}
				
				hql=hql+orgStr;				
			}
			Object[] values={"0"}; 
			List docTempTypeList = templateDao.find(hql, values);
			return docTempTypeList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取父节点公文模板类别"});
		}
	}
	
	public List docTempWordTypeList()throws SystemException,ServiceException{
		try{
			String hql="from ToaDoctemplateGroup t where (t.docgroupType=0 or t.docgroupType is null) and t.docgroupParentId=?";
			Object[] values={"0"}; 
			if(userService.isViewSetOpen()) {
				User user=userService.getCurrentUser();
				TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
				boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
				String orgStr="";
				
				if(isView){
					orgStr=" and t.docgroupOrgCode like '"+org.getSupOrgCode()+"%'";		
				}else {
					orgStr=" and t.docgroupOrgId  = '"+org.getOrgId()+"'";
				}
				
				hql=hql+orgStr;				
			}
			
			List docTempTypeList = templateDao.find(hql, values);
			return docTempTypeList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取父节点公文Word模板类别"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:获取公文模板类别下的子类别
	 * modifyer:
	 * @param ParentId
	 * @return
	 */
	public List docTempSubTypeList(String parentId)throws SystemException,ServiceException{
		try{
			String hql="from ToaDoctemplateGroup t where t.docgroupParentId=?";
			Object[] values={parentId};
			if(userService.isViewSetOpen()) {
				User user=userService.getCurrentUser();
				TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
				boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
				String orgStr="";
				
				if(isView){
					orgStr=" and t.docgroupOrgCode like '"+org.getSupOrgCode()+"%'";		
				}else {
					orgStr=" and t.docgroupOrgId  = '"+org.getOrgId()+"'";
				}
				
				hql=hql+orgStr;				
			}
			
			return templateDao.find(hql, values);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取子节点公文模板类别"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:获取公文模板类别名称
	 * modifyer:
	 * @param docgroupId
	 * @return
	 */
	public String doctempTypeName(String docgroupId)throws SystemException,ServiceException{
		try{
			ToaDoctemplateGroup doctemptype=getTemplateType(docgroupId);
			return doctemptype.getDocgroupName();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取公文模板类别名称"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:获取所有的公文模板类别
	 * modifyer:
	 * @return
	 */
	public List<ToaDoctemplateGroup> getAllTypeTemplate()throws SystemException,ServiceException{
		try{
			return templateDao.findAll();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取所有的公文模板类别"});
		}
	}
	
	/**
	 * 根据父节点ID，判断该节点下是否还存在子节点
	 * @author zhengzb
	 * @desc 
	 * 2010-6-17 下午05:04:58 
	 * @param docParentId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List getTypeByParentId(String docParentId)throws SystemException,ServiceException{
		try{
			String hql="from ToaDoctemplateGroup t where t.docgroupParentId=?";
			Object[] values={docParentId};
			if(userService.isViewSetOpen()) {
				User user=userService.getCurrentUser();
				TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
				boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
				String orgStr="";
				
				if(isView){
					orgStr=" and t.docgroupOrgCode like '"+org.getSupOrgCode()+"%'";		
				}else {
					orgStr=" and t.docgroupOrgId  = '"+org.getOrgId()+"'";
				}
				
				hql=hql+orgStr;				
			}
			
			List typeByTypeList = templateDao.find(hql.toString(), values);
			return typeByTypeList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取类别下的公文模板"});
		}
	}
	
	/**
	 * 根据模板类型，获取模板类
	 * @author zhengzb
	 * @desc 
	 * 2010-6-21 上午10:41:36 
	 * @param docgroupType  模板类型
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List getTemplateByType(String docgroupType) throws SystemException,ServiceException{
		try {
			String hql="from ToaDoctemplateGroup t where t.docgroupType=?";
			Object[] values={docgroupType};
			if(userService.isViewSetOpen()) {
				User user=userService.getCurrentUser();
				TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
				boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
				String orgStr="";
				
				if(isView){
					orgStr=" and t.docgroupOrgCode like '"+org.getSupOrgCode()+"%'";		
				}else {
					orgStr=" and t.docgroupOrgId  = '"+org.getOrgId()+"'";
				}
				
				hql=hql+orgStr;				
			}
			
			List typeByTypeList = templateDao.find(hql.toString(), values);
			return typeByTypeList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取模板类型的公文模板"});
		}
	}

	public IUserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
}
