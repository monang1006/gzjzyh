package com.strongit.xxbs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.entity.TInfoBaseAdoption;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.service.IAdoptionService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class AdoptionService implements IAdoptionService
{
	private GenericDAOHibernate<TInfoBaseAdoption, String> adoptionDao;
	private GenericDAOHibernate<TInfoBasePublish, String> submitDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		adoptionDao = new GenericDAOHibernate<TInfoBaseAdoption, String>(
				sessionFactory, TInfoBaseAdoption.class);
		submitDao = new GenericDAOHibernate<TInfoBasePublish, String>(
				sessionFactory, TInfoBasePublish.class);
	}

	public TInfoBaseAdoption getAdoption(String adoId) throws ServiceException,
			SystemException, DAOException
	{
		return adoptionDao.get(adoId);
	}

	public List<TInfoBaseAdoption> getAdoptions() throws ServiceException,
			SystemException, DAOException
	{
		return adoptionDao.findAll();
	}

	public Page<TInfoBaseAdoption> getAdoptions(Page<TInfoBaseAdoption> page)
			throws ServiceException, SystemException, DAOException
	{
		return adoptionDao.findAll(page);
	}

	@Transactional(readOnly = false)
	public void saveAdoption(TInfoBaseAdoption adoption) throws ServiceException,
			SystemException, DAOException
	{
		adoptionDao.save(adoption);
		TInfoBasePublish publish = submitDao.get(adoption.getPubId());
		publish.setPubUseStatus(Publish.USED);
		if(adoption.getAdoInstructionScore() != null 
				&& adoption.getAdoInstructionScore().intValue() >= 0)
		{
			publish.setPubIsInstruction(Publish.INSTRUCTION);
		}
		submitDao.save(publish);	
	}

	@Transactional(readOnly = false)
	public void deleteAdoption(String adoId) throws ServiceException,
			SystemException, DAOException
	{
		adoptionDao.delete(adoId);
	}

	public TInfoBaseAdoption findAdoptionByPubId(String pubId)
			throws ServiceException, SystemException, DAOException
	{
		List<TInfoBaseAdoption> lists =
				adoptionDao.findByProperty("pubId", pubId);
		TInfoBaseAdoption adoption = null;
		if(lists.size() == 1)
		{
			adoption = lists.get(0);
		}
		return adoption;
	}

	public String[] findAdoptionByJvId(String jvId) throws ServiceException,
			SystemException, DAOException
	{
		List<TInfoBaseAdoption> lists =
				adoptionDao.findByProperty("jvId", jvId);
		
		String[] jvIds = null;
		if(lists != null)
		{
			jvIds = new String[lists.size()];
			int i = 0;
			for(TInfoBaseAdoption one : lists)
			{
				jvIds[i] = one.getPubId();
				i++;
			}
		}

		return jvIds;
	}

	public Map<String, TInfoBaseAdoption> getAdos() throws DAOException,
			SystemException, ServiceException
	{
		Map<String, TInfoBaseAdoption> rets =
				new HashMap<String, TInfoBaseAdoption>();
		List<TInfoBaseAdoption> lists = adoptionDao.findAll();
		for(TInfoBaseAdoption one : lists)
		{
			rets.put(one.getPubId(), one);
		}
		return rets;
	}

}
