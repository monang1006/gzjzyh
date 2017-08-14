package com.strongit.oa.formManager.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSysmanageFormManager;
import com.strongit.oa.formManager.IFormManagerService;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
@OALogger
public class FormManagerService implements IFormManagerService {

	private GenericDAOHibernate<ToaSysmanageFormManager, java.lang.String>formManagerDao;
	
	 @Autowired
	 public void setSessionFactory(SessionFactory sessionFactory) 
	 {
		 formManagerDao = new GenericDAOHibernate<ToaSysmanageFormManager, java.lang.String>(sessionFactory,
				 ToaSysmanageFormManager.class);
	 }
	
	public void deleteByFormManagerCode(String formId, OALogInfo... loginfos)
			throws SystemException, ServiceException {
		try {
			formManagerDao.delete(formId);
		} catch (Exception e) {
			 throw new ServiceException(MessagesConst.save_error,               
						new Object[] {"删除信息项使用记录"});
		}

	}

	public void deleteByList(List<ToaSysmanageFormManager> fmMoList,OALogInfo ... loginfos)throws SystemException,ServiceException{
		try {
			formManagerDao.delete(fmMoList);
		} catch (Exception e) {
			 throw new ServiceException(MessagesConst.save_error,               
						new Object[] {"删除表单使用记录list"});
		}

	}
	
	
	public List<ToaSysmanageFormManager> getListByQuery(String formId,
			String formBusinessId, OALogInfo... loginfos)
			throws SystemException, ServiceException {
		try {
			List<Object> list =new ArrayList<Object>();
			String hql =" from ToaSysmanageFormManager t where 1=1";
			if(formId!=null&&!formId.equals("")){
				hql=hql+" and t.formId = ? ";
				list.add(formId);
			}
			if (formBusinessId!=null&&!formBusinessId.equals("")) {
				hql=hql+" and t.formBusinessId like ?";
				list.add("%"+formBusinessId+"%");
			}
			if(list.size()>0){
				Object[] obj=new Object[list.size()];
				for(int i=0;i<list.size();i++){
					obj[i]=list.get(i);
				}
				return formManagerDao.find(hql.toString(), obj);
			}else {
				return formManagerDao.find(hql.toString());
			}
			
			
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据多条件查询表单使用列表"});
		}
	}

	public ToaSysmanageFormManager getPmanagerModel(String formManagerCode,
			OALogInfo... loginfos) throws SystemException, ServiceException {
		try {
			return formManagerDao.get(formManagerCode);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据主键查询表单使用对象"});
		}
	}

	public void saveFormManager(ToaSysmanageFormManager FmModel,
			OALogInfo... loginfos) throws SystemException, ServiceException {
		try {
			formManagerDao.save(FmModel);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"保存表单使用对象"});
		}

	}
	
	/**
	 * 根据表单ID，查询表单中单表，使用记录
	 * @author zhengzb
	 * @desc 
	 * 2010-11-30 上午11:20:44 
	 * @param formId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int isHasEFormUsed(String formId)throws SystemException,ServiceException{
		try {
			String hql="select count(*) from ToaSysmanageFormManager t where 1=1";
			hql=hql+" and t.formId=?";

			int num=Integer.parseInt(formManagerDao.findUnique(hql, formId).toString());
			return num;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查询表单是否使用"});	
		}
	}

}
