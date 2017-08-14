package com.strongit.oa.personnel.trainingmanage;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaTrainColumn;
import com.strongit.oa.bo.ToaTrainInfo;
import com.strongit.oa.bo.ToaTrainRecord;
import com.strongit.oa.bo.ToaTraininfoAttach;
import com.strongit.oa.bo.ToaVeteranRegard;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongit.oa.bo.ToaBasePerson;

@Service
@Transactional
public class Trainingmanager {
	private GenericDAOHibernate<ToaTrainInfo, String> trainDao = null;
	private GenericDAOHibernate<ToaTrainRecord, String> recordDao = null;
	private GenericDAOHibernate<ToaTraininfoAttach, String> attachDao = null;
	private GenericDAOHibernate<ToaTrainColumn, String> columnDAO = null;
	
	private PersonManager perManager;
	public PersonManager getPerManager() {
		return perManager;
	}
	@Autowired
	public void setPerManager(PersonManager perManager) {
		this.perManager = perManager;
	}

	public Trainingmanager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		trainDao = new GenericDAOHibernate<ToaTrainInfo, String>(
				sessionFactory, ToaTrainInfo.class);
		recordDao = new GenericDAOHibernate<ToaTrainRecord, String>(
				sessionFactory, ToaTrainRecord.class);
		attachDao = new GenericDAOHibernate<ToaTraininfoAttach, String>(
				sessionFactory, ToaTraininfoAttach.class);
		columnDAO = new GenericDAOHibernate<ToaTrainColumn, String>(
				sessionFactory, ToaTrainColumn.class);
	}
	
	
	public List getParentColumn() throws SystemException, ServiceException {
		try {
			String hql = "from ToaTrainColumn t where t.clumnParent=?";
			Object[] values = { "0" }; //设置栏目父节点为0
			List parentColumnList = columnDAO.find(hql, values);
			return parentColumnList;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取父节点栏目" });
		}
	}
	
public List getSubColumn(String parentId,OALogInfo ... loginfos) throws SystemException,
	ServiceException {
try {
	String hql = "from ToaTrainColumn t where t.clumnParent=?";
	Object[] values = { parentId };
	return columnDAO.find(hql, values);
} catch (ServiceException e) {
	throw new ServiceException(MessagesConst.find_error,
			new Object[] { "获取子节点栏目" });
}
}
public void saveColumn(ToaTrainColumn model,OALogInfo ... loginfos) throws SystemException,
ServiceException {
try {
     columnDAO.save(model);
} catch (ServiceException e) {
throw new ServiceException(MessagesConst.save_error,
		new Object[] { "保存栏目" });
}
}

public String getClumnName(String clumnId,OALogInfo ... loginfos) throws SystemException,
ServiceException {
try {
	ToaTrainColumn column = getColumn(clumnId);
return column.getClumnName();
} catch (ServiceException e) {
throw new ServiceException(MessagesConst.find_error,
		new Object[] { "获取栏目名称" });
}
}

public ToaTrainColumn getColumn(String clumnId,OALogInfo ... loginfos)
throws SystemException, ServiceException {
try {
return columnDAO.get(clumnId);
} catch (ServiceException e) {
throw new ServiceException(MessagesConst.find_error,
		new Object[] { "栏目管理对象" });
}
}
public void delColumn(String clumnId,OALogInfo ... loginfos) throws SystemException,
ServiceException {
try {
columnDAO.delete(clumnId);
} catch (ServiceException e) {
throw new ServiceException(MessagesConst.del_error,
		new Object[] { "删除栏目" });
}
}

public boolean columnArticl(String clumnId,OALogInfo ... loginfos)throws SystemException,
ServiceException {
try {
String hql = "from ToaTrainInfo t where t.toaTrainColumn.clumnId=?";
Object[] values = { clumnId };
List columnArticlList = trainDao.find(hql, values);
if (columnArticlList.size() > 0) { //栏目下存在培训
	return true;
} else {
	return false;
}
} catch (ServiceException e) {
throw new ServiceException(MessagesConst.find_error,
		new Object[] { "判断栏目下是否有培训" });
}
}

	@Transactional(readOnly = false)
	public void saveTrainattach(ToaTraininfoAttach attach) throws DAOException,
			SystemException, ServiceException {
		attachDao.save(attach);
	}
	
	@Transactional(readOnly = false)
	public void deleteAttaches(String sortIds) throws DAOException,
			SystemException, ServiceException {
		String[] ids = sortIds.split(",");
		attachDao.delete(ids);
	}
	
public List<ToaTraininfoAttach> getTrainAttaches(String trainId)
	throws DAOException, SystemException, ServiceException {
    final String hql = "from ToaTraininfoAttach as t where t.traininfo.trainId =?";
    return attachDao.find(hql, trainId);
}

public ToaTraininfoAttach getTrainAttache(String id) throws DAOException,
SystemException, ServiceException {

return attachDao.get(id);
}

	/**
	 * 得到一页一个老干部的慰问信息情况
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
	
	public Page<ToaVeteranRegard> queryVeteranRegard(Page<ToaVeteranRegard> page,
			String personId, String verePersons, String vereTopic,
			Date vereTime) throws DAOException, SystemException,
			ServiceException {
		StringBuffer queryStr = new StringBuffer(
				"from ToaVeteranRegard t where 1=1");
		Object[] values = new Object[1];
		if (personId != null && !"".equals(personId)) {
			queryStr.append(" and t.toaBaseVeteran.personId ='" + personId + "'");
		}

		if (verePersons != null && !"".equals(verePersons)) {
			queryStr.append(" and t.verePersons like '%" + verePersons + "%'");
		}
		if (vereTopic != null && !"".equals(vereTopic)) {
			queryStr.append(" and t.vereTopic like '%" + vereTopic
					+ "%'");
		}
		if (vereTime != null && !"".equals(vereTime)) {
			queryStr.append(" and t.vereTime<=?");
			values[0] =vereTime;
		} else {
			queryStr.append(" and 1=?");
			values[0] = 1;
		}
		queryStr.append(" order by t.vereTime desc");
		return regardDao.find(page, queryStr.toString(), values);

	}
	 */
	/**
	 * 得到一页未删除的老干部对象
	 *@author 蒋国斌
	 *@date 2009-9-22 下午02:59:32 
	 * @param page
	 * @param personName
	 * @param personPset
	 * @param personSax
	 * @param personLevel
	 * @param healthState
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaTrainInfo> queryTrainInfoPage(Page<ToaTrainInfo> page,
			String trainTopic, String trainCommpany, String trainType,
			Date sDate,Date eDate,String clumnId) throws DAOException, SystemException,
			ServiceException {
		StringBuffer queryStr = new StringBuffer(
				"from ToaTrainInfo t where 1=1");
		Object[] values = new Object[2];
		
		if (clumnId != null && !"".equals(clumnId)) {
			queryStr.append(" and t.toaTrainColumn.clumnId like'" + clumnId + "%'");
		}
		if (trainTopic != null && !"".equals(trainTopic)) {
			queryStr.append(" and t.trainTopic like'%" + trainTopic + "%'");
		}

		if (trainCommpany != null && !"".equals(trainCommpany)) {
			queryStr.append(" and t.trainCommpany like '%" + trainCommpany + "%'");
		}
		if (trainType != null && !"".equals(trainType)) {
			queryStr.append(" and t.trainType like '%" + trainType
					+ "%'");
		}
		if (sDate != null && !"".equals(sDate)) {
			queryStr.append(" and t.trainStartdate>=?");
			values[0] = sDate;
		} else {
			queryStr.append(" and 1=?");
			values[0] = 1;
		}
		if (eDate != null && !"".equals(eDate)) {
			queryStr.append(" and t.trainEnddate<=?");
			values[1] =eDate;
		} else {
			queryStr.append(" and 1=?");
			values[1] = 1;
		}
		queryStr.append(" order by t.trainStartdate desc");
		return trainDao.find(page, queryStr.toString(), values);

	}
	
	public Page<ToaTrainInfo> queryTrainInfoPageByperson(Page<ToaTrainInfo> page,
			String personName) throws DAOException, SystemException,
			ServiceException {
		List<ToaTrainRecord> list=this.getTrainRecordsByName(personName);
		List<ToaTrainInfo> train=new ArrayList();
		if(list!=null && list.size()!=0){
			for (Iterator it = list.iterator();it.hasNext();) {
				ToaTrainRecord record=(ToaTrainRecord)it.next();
				train.add(record.getToaTrainInfo());
			}
			//page.setResult(train);
			//page.setTotalCount(list.size());
		}
		page=ListUtils.splitList2Page(page,train);
		return page;
	}
	
	public Page<ToaTrainInfo> queryTrainInfoPageByOrg(Page<ToaTrainInfo> page,
			String orgid) throws DAOException, SystemException,
			ServiceException {
		List<ToaBasePerson> pelist=perManager.getPersonByOrg(orgid);
		List<ToaTrainInfo> train=new ArrayList();
		if(pelist!=null && pelist.size()!=0){
			
			for (Iterator it = pelist.iterator();it.hasNext();) {
				ToaBasePerson pe=(ToaBasePerson)it.next();
				String personName=pe.getPersonName();
				List<ToaTrainRecord> list=this.getTrainRecordsByName(personName);
				
				if(list!=null && list.size()!=0){
					for (Iterator te = list.iterator();te.hasNext();) {
						ToaTrainRecord record=(ToaTrainRecord)te.next();
						if(!train.contains(record.getToaTrainInfo())){
							train.add(record.getToaTrainInfo());
						}
						
					}
					
				}			
			}
				
		}
		page=ListUtils.splitList2Page(page,train);
		
		return page;
	}

	public ToaTrainInfo getOneTrainInfo(String veteranId) {
		return trainDao.get(veteranId);
	}
	
	public List<ToaTrainInfo> getTrainInfos() throws DAOException,
	SystemException, ServiceException {
      final String hql = "from ToaTrainInfo t where 1=1";
        return trainDao.find(hql);
}
	

	@Transactional(readOnly = false)
	public void saveToaTrainInfo(ToaTrainInfo toSort)
			throws DAOException, SystemException, ServiceException {
		trainDao.save(toSort);
	}
	/**
	 * 保存人员培训信息记录
	 *@author 蒋国斌
	 *@date 2009-9-27 上午10:10:16 
	 * @param toSort
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveToaTrainRecord(ToaTrainRecord trainRecord)
			throws DAOException, SystemException, ServiceException {
		recordDao.save(trainRecord);
	}
	
	/**  根据一个人员ID得到他的所有培训信息
	 *@author 蒋国斌
	 *@date 2009-9-22 下午03:17:08 
	 * @param personid
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */

public List<ToaTrainRecord> getTrainRecords(String personid) throws DAOException,
	SystemException, ServiceException {
      final String hql = "from ToaTrainRecord t where t.toaBasePerson.personid ="+personid ;
        return recordDao.find(hql);
}

public List<ToaTrainRecord> getTrainRecordsByName(String personname) throws DAOException,
SystemException, ServiceException {
  final String hql = "from ToaTrainRecord t where t.toaBasePerson.personName =?" ;
    return recordDao.find(hql,personname);
}

/**
 * 根据培训信息获得培训人员参加培训情况
 *@author 蒋国斌
 *@date 2009-10-17 上午09:47:16 
 * @param trainid
 * @return
 * @throws DAOException
 * @throws SystemException
 * @throws ServiceException
 */
public List<ToaTrainRecord> getTrainRecordsbyT(String trainid) throws DAOException,
SystemException, ServiceException {
  final String hql = "from ToaTrainRecord t where t.toaTrainInfo.trainId =?" ;
    return recordDao.find(hql,trainid);
}
	/**
	 * 根据主键ID删除慰问信息(支持批量删除)包括其下面的附件
	 *@author 蒋国斌
	 *@date 2009-9-22 下午03:14:59 
	 * @param topicIds
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteTrainInfo(String topicIds) throws DAOException,
			SystemException, ServiceException {
		String[] ids = topicIds.split(",");
		for(int i=0;i<ids.length;i++){
			this.deleteTrainRecords(ids[i]);
			List<ToaTraininfoAttach> list=this.getTrainAttaches(ids[i]);
			if(list.size()!=0 || list!=null){
				attachDao.delete(list);
			}	
			}
		trainDao.delete(ids);
	}
	
	/**
	 * 删除一个人员的所有的培训信息记录
	 *@author 蒋国斌
	 *@date 2009-9-27 下午03:16:07 
	 * @param personId
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	  */
	@Transactional(readOnly = false)
	public void deletePersonTrainRecords(String personId) throws DAOException,
			SystemException, ServiceException {
		List<ToaTrainRecord> ids=this.getTrainRecords(personId);
	     recordDao.delete(ids);
	}
	
	@Transactional(readOnly = false)
	public void deleteTrainRecords(String trainId) throws DAOException,
			SystemException, ServiceException {
		List<ToaTrainRecord> ids=this.getTrainRecordsbyT(trainId);
	     recordDao.delete(ids);
	}
	
	
}
