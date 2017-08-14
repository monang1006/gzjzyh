package com.strongit.oa.personnel.veteranmanage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.PersonDeployInfo;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.bo.ToaBaseVeteran;
import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaPersonDeploy;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bo.ToaVeteranRegard;
import com.strongit.oa.dict.dictItem.DictItemManager;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class Veteranmanager {
	private GenericDAOHibernate<ToaBaseVeteran, String> veteranDao = null;
	private GenericDAOHibernate<ToaVeteranRegard, String> regardDao = null;
	
	private DictItemManager manager; // 字典项Manager
	
	public DictItemManager getManager() {
		return manager;
	}
	@Autowired
	public void setManager(DictItemManager manager) {
		this.manager = manager;
	}

	public Veteranmanager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		veteranDao = new GenericDAOHibernate<ToaBaseVeteran, String>(
				sessionFactory, ToaBaseVeteran.class);
		regardDao = new GenericDAOHibernate<ToaVeteranRegard, String>(
				sessionFactory, ToaVeteranRegard.class);
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
	 */
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
	public Page<ToaBaseVeteran> queryBaseVeteran(Page<ToaBaseVeteran> page,
			String personName, String personPset, String personSax,
			String personLevel,String healthState) throws DAOException, SystemException,
			ServiceException {
		StringBuffer queryStr = new StringBuffer(
				"from ToaBaseVeteran t where t.personIsdel=0");
		Object[] values = new Object[0];
		if (personName != null && !"".equals(personName)) {
			queryStr.append(" and t.personName like'%" + personName + "%'");
		}

		if (personPset != null && !"".equals(personPset)) {
			queryStr.append(" and t.personPset like '%" + personPset + "%'");
		}
		if (personSax != null && !"".equals(personSax)) {
			queryStr.append(" and t.personSax like '%" + personSax
					+ "%'");
		}
		if (personLevel != null && !"".equals(personLevel)) {
			queryStr.append(" and t.personTreatmentLevel like '%" + personLevel + "%'");
		}
		if (healthState != null && !"".equals(healthState)) {
			queryStr.append(" and t.personHealthState like '%" + healthState + "%'");
		}
		return veteranDao.find(page, queryStr.toString(), values);

	}

	public ToaBaseVeteran getOneVeteran(String veteranId) {
		return veteranDao.get(veteranId);
	}
	
	public List<ToaBaseVeteran> getVeterans() throws DAOException,
	SystemException, ServiceException {
      final String hql = "from ToaBaseVeteran t where t.personIsdel=0";
        return veteranDao.find(hql);
}
	public ToaVeteranRegard getOneVeteranRegard(String regardId) {
		return regardDao.get(regardId);
	}

	@Transactional(readOnly = false)
	public void saveToaBaseVeteran(ToaBaseVeteran toSort)
			throws DAOException, SystemException, ServiceException {
		    veteranDao.save(toSort);
	}

	@Transactional(readOnly = false)
	public void saveToaVeteranRegard(ToaVeteranRegard toSort)
			throws DAOException, SystemException, ServiceException {
		regardDao.save(toSort);
	}
	/**
	 * 根据一个老干部ID得到他的所有慰问信息
	 *@author 蒋国斌
	 *@date 2009-9-22 下午03:17:08 
	 * @param personid
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
public List<ToaVeteranRegard> getVeteranRegards(String personid) throws DAOException,
	SystemException, ServiceException {
      final String hql = "from ToaVeteranRegard t where t.toaBaseVeteran.personId ="+personid ;
        return regardDao.find(hql);
}
	/**
	 * 根据主键ID删除慰问信息(支持批量删除)
	 *@author 蒋国斌
	 *@date 2009-9-22 下午03:14:59 
	 * @param topicIds
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteRegards(String topicIds) throws DAOException,
			SystemException, ServiceException {
		String[] ids = topicIds.split(",");
		regardDao.delete(ids);
	}
	/**
	 * 删除一个老干部的所有的慰问信息
	 *@author 蒋国斌
	 *@date 2009-9-22 下午03:16:07 
	 * @param personId
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteOnePersonRegards(String personId) throws DAOException,
			SystemException, ServiceException {
		List<ToaVeteranRegard> ids=this.getVeteranRegards(personId);
		regardDao.delete(ids);
	}
	/**
	 * 将退休人员转入老干部表中
	 *@author 蒋国斌
	 *@date 2009-10-16 上午09:23:21 
	 * @param person
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws ParseException 
	 */
	@Transactional(readOnly = false)
	public void saveToaVeteranByPerson(ToaBasePerson person)
	throws DAOException, SystemException, ServiceException, ParseException {
		ToaBaseVeteran veter=new ToaBaseVeteran();
		veter.setPersonId(null);
		//veter.(person.getPersonid());
		
		veter.setBaseOrg(person.getBaseOrg());
		
		DateFormat  f=new SimpleDateFormat("yyyy-MM-dd");  
		if(person.getPersonBorn()!=null && !person.getPersonBorn().equals("")){
		  Date dd = f.parse(person.getPersonBorn());
		       veter.setPersonBorn(dd);
		}else{
			 veter.setPersonBorn(f.parse("1949-10-01"));
		}
		veter.setPersonHealthState("4028823923da6c310123da97dbd50002");
		veter.setPersonIsdel(person.getPersonIsdel());
		veter.setPersonName(person.getPersonName());
		if(person.getPersonNation()!=null && !person.getPersonNation().equals("")){
			veter.setPersonNation(person.getPersonNation());
		}else{
		     veter.setPersonNation("");
		}
		
		if(person.getPersonNativePlace()!=null && !person.getPersonNativePlace().equals("")){
			veter.setPersonNativeplace(person.getPersonNativePlace());
		}else{
			veter.setPersonNativeplace("");
		}
		
		veter.setPersonPersonKind(person.getPersonPersonKind());
		if(person.getPersonPset()!=null && !person.getPersonPset().equals("")){
		ToaSysmanageDictitem dictitem = (ToaSysmanageDictitem)manager.getDictItem(person.getPersonPset());
		         veter.setPersonPset(dictitem.getDictItemShortdesc());
		}else{
			 veter.setPersonPset("");
		}
		
		SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd");
		String aa=dateformat1.format(new Date());
		 Date xx = dateformat1.parse(aa);
		veter.setPersonRetireTime(xx);
		veter.setPersonSax(person.getPersonSax());
		veter.setPersonStatus(person.getPerson_status());
		
		this.saveToaBaseVeteran(veter);
		
}


}
