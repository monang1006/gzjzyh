package com.strongit.oa.infoManager.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaSysmanagePmanager;
import com.strongit.oa.infoManager.IInfoManagerService;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
@OALogger
public class InfoManagerService implements IInfoManagerService {

	private GenericDAOHibernate<ToaSysmanagePmanager, java.lang.String>infoManagerDao;
	
	 @Autowired
	 public void setSessionFactory(SessionFactory sessionFactory) 
	 {
		 infoManagerDao = new GenericDAOHibernate<ToaSysmanagePmanager, java.lang.String>(sessionFactory,
				 ToaSysmanagePmanager.class);
	 }
	
	public void deleteByInfoManagerCode(String infoManagerCode,
			OALogInfo... loginfos) throws SystemException, ServiceException {
		try {
			infoManagerDao.delete(infoManagerCode);
		} catch (Exception e) {
			 throw new ServiceException(MessagesConst.save_error,               
						new Object[] {"删除信息项使用记录"});
		}

	}

	public void deleteByList(List<ToaSysmanagePmanager> pmModeList,OALogInfo ...logInfos)throws SystemException,ServiceException{
		try {
			infoManagerDao.delete(pmModeList);
		} catch (Exception e) {
			 throw new ServiceException(MessagesConst.save_error,               
						new Object[] {"删除List信息项使用记录"});
		}
	}
	
	public List<ToaSysmanagePmanager> getListByQuery(String infoItemId,
			String infoBusinessId, OALogInfo... loginfos)
			throws SystemException, ServiceException {
		try {
			List<Object> list =new ArrayList<Object>();
			String hql =" from ToaSysmanagePmanager t where 1=1";
			if(infoItemId!=null&&!infoItemId.equals("")){
				hql=hql+" and t.infoItemId = ? ";
				list.add(infoItemId);
			}
			if (infoBusinessId!=null&&!infoBusinessId.equals("")) {
				hql=hql+" and t.infoBusinessId like ? ";
				list.add("%"+infoBusinessId+"%");
			}
			if(list.size()>0){
				Object[] obj=new Object[list.size()];
				for(int i=0;i<list.size();i++){
					obj[i]=list.get(i);
				}
				return infoManagerDao.find(hql.toString(), obj);
			}else {
				return infoManagerDao.find(hql.toString());
			}
			
			
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据多条件查询信息项使用列表"});
		}
	}

	public ToaSysmanagePmanager getPmanagerModel(String infoManagerCode,
			OALogInfo... loginfos) throws SystemException, ServiceException {
		try {
			return infoManagerDao.get(infoManagerCode);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据主键查询信息项使用对象"});
		}
	}

	public void saveInfoManager(ToaSysmanagePmanager infoManagerModel,
			OALogInfo... loginfos) throws SystemException, ServiceException {
		try {
			infoManagerDao.save(infoManagerModel);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"保存信息项使用对象"});
		}

	}
	
	/**
	 * 根据infoItemId查询信息集或信息项，是否使用
	 * @author zhengzb
	 * @desc 
	 * 2010-11-30 上午10:05:12 
	 * @param infoItemId    “表名”或“表名+$+字段名"
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int isHasInfoFormUsed(String infoItemId) throws SystemException,ServiceException{
		try {
			String hql="select count(*) from ToaSysmanagePmanager t where 1=1";
			hql=hql+" and t.infoItemId=?";

			int num=Integer.parseInt(infoManagerDao.findUnique(hql, infoItemId).toString());
			return num;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查询信息集或信息项是否使用"});	
		}
	}

}
