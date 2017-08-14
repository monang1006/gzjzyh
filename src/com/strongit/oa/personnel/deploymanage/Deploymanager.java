package com.strongit.oa.personnel.deploymanage;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.PersonDeployInfo;
import com.strongit.oa.bo.ToaPersonDeploy;
import com.strongit.oa.util.MessagesConst;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class Deploymanager {
	private GenericDAOHibernate<ToaPersonDeploy, String> deployDao = null;
	private GenericDAOHibernate<PersonDeployInfo, String> infoDao = null;
	public Deploymanager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		deployDao = new GenericDAOHibernate<ToaPersonDeploy, String>(
				sessionFactory, ToaPersonDeploy.class);
		infoDao = new GenericDAOHibernate<PersonDeployInfo, String>(
				sessionFactory, PersonDeployInfo.class);

	}

	/**
	 * 得到一页调配类别对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-9-14 下午03:28:06
	 * @param page
	 * @param pdepId
	 * @param pdepName
	 * @param pdepIsveteran
	 * @param pdepIsactiv
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaPersonDeploy> queryDeploy(Page<ToaPersonDeploy> page,
		 String pdepName, String pdepIsveteran,
			String pdepIsactiv) throws DAOException, SystemException,
			ServiceException {
		StringBuffer queryStr = new StringBuffer(
				"from ToaPersonDeploy t where 1=1");
		Object[] values = new Object[0];

		if (pdepName != null && !"".equals(pdepName)) {
			queryStr.append(" and t.pdepName like '%" + pdepName + "%'");
		}
		if (pdepIsveteran != null && !"".equals(pdepIsveteran)) {
			queryStr.append(" and t.pdepIsveteran like '%" + pdepIsveteran
					+ "%'");
		}
		if (pdepIsactiv != null && !"".equals(pdepIsactiv)) {
			queryStr.append(" and t.pdepIsactiv like '%" + pdepIsactiv + "%'");

		}
		return deployDao.find(page, queryStr.toString(), values);

	}

	public ToaPersonDeploy getOnePersonDeploy(String deployId) {
		return deployDao.get(deployId);
	}
	
	public List<ToaPersonDeploy> getDeployes() throws DAOException,
	SystemException, ServiceException {
      final String hql = "from ToaPersonDeploy t where t.pdepIsactiv=1";
        return deployDao.find(hql);
}
	
	public List<PersonDeployInfo> getDeployeInfos() throws DAOException,
	SystemException, ServiceException {
      final String hql = "from PersonDeployInfo where 1=1";
        return infoDao.find(hql);
	}

	@Transactional(readOnly = false)
	public void saveToaPersonDeploy(ToaPersonDeploy toSort)
			throws DAOException, SystemException, ServiceException {
		deployDao.save(toSort);
	}
	

	@Transactional(readOnly = false)
	public void deletePersonDeploy(String sortIds) throws DAOException,
			SystemException, ServiceException {
		String[] ids = sortIds.split(",");
		deployDao.delete(ids);
	}
	
	/*
	 * 
	 * Description:保存人员调配信息记录
	 * param: 
	 * @author 	 彭小青
	 * @date 	 Oct 15, 2009 10:33:09 AM
	 */
	public void saveDeployInfo(PersonDeployInfo deployInfo) throws SystemException, ServiceException {
		try{
			infoDao.save(deployInfo);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存调配信息"}); 
		}
	}
	
	/*
	 * 
	 * Description:获取人员调配记录列表
	 * param: 
	 * @author 	 彭小青
	 * @date 	 Oct 19, 2009 5:18:35 PM
	 */
	public List<PersonDeployInfo> getPerDeployList(PersonDeployInfo model,String personId)throws SystemException, ServiceException{
		try{
			List<Object> objlist = new ArrayList<Object>();
		 	String hql = "from PersonDeployInfo t where t.personId=?";
		 	objlist.add(personId);
		 	if(model.getExchangeTime()!= null&&!"".equals(model.getExchangeTime())) {//调配开始日期
				hql = hql + " and t.exchangeTime >= ?";
				objlist.add(model.getExchangeTime());
			}
		 	if(model.getLastTime()!= null&&!"".equals(model.getLastTime())) {//调配结束日期
				hql = hql + " and t.exchangeTime <= ?";
				model.getLastTime().setHours(23);
				model.getLastTime().setMinutes(59);
				model.getLastTime().setSeconds(59);
				objlist.add(model.getLastTime());
			}
			if(model.getDeployInfo()!=null&&model.getDeployInfo().getPdepId()!= null&&!"".equals(model.getDeployInfo().getPdepId())) {//调配类别
				hql = hql + " and t.deployInfo.pdepId = ?";
				objlist.add(model.getDeployInfo().getPdepId());
			}
		 	if(model.getOldInfos()!= null&&!"".equals(model.getOldInfos())) {//调配前信息
				hql = hql + " and t.oldInfos like ?";
				objlist.add("%"+model.getOldInfos()+"%");
			}
			if(model.getNewInfos()!= null&&!"".equals(model.getNewInfos())) {//调配后信息
				hql = hql + " and t.newInfos like ?";
				objlist.add("%"+model.getNewInfos()+"%");
			}
			if(model.getExchangeWhy()!= null&&!"".equals(model.getExchangeWhy())) {//调配原因
				hql = hql + " and t.exchangeWhy like ?";
				objlist.add("%"+model.getExchangeWhy()+"%");
			}
			Object[] objs = new Object[objlist.size()];
			for (int i = 0; i < objlist.size(); i++) {
				objs[i] = objlist.get(i);
			}	
			return infoDao.find(hql,objs);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取人员调配记录列表"}); 
		}
	}
	
	/*
	 * 
	 * Description:根据调配记录ID获取调配对象
	 * param: 
	 * @author 	 彭小青
	 * @date 	 Oct 20, 2009 10:27:05 AM
	 */
	public PersonDeployInfo getDeployInfo(String id){
		PersonDeployInfo info=null;
		try{
			info=infoDao.get(id);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据调配记录ID获取调配对象"}); 
		}
		return info;
	}
	
	/*
	 * 
	 * Description:删除调配记录
	 * param: 
	 * @author 	 彭小青
	 * @date 	 Oct 20, 2009 10:48:51 AM
	 */
	public void deleteDeployInfo(String ids){
		try{
			String[] idarray=ids.split(",");
			for(int i=0;idarray!=null&&i<idarray.length;i++){
				PersonDeployInfo info=infoDao.get(idarray[i]);
				infoDao.delete(info);
			}
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除调配记录"}); 
		}
	}

}
