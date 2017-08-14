package com.strongit.xxbs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.entity.TInfoBasePiece;
import com.strongit.xxbs.service.IAppointToService;
import com.strongit.xxbs.service.IBulletinService;
import com.strongit.xxbs.service.IPieceService;
import com.strongit.xxbs.service.IPublishService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class PieceService implements IPieceService
{
	private GenericDAOHibernate<TInfoBasePiece, String> pieceDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		pieceDao = new GenericDAOHibernate<TInfoBasePiece, String>(
				sessionFactory, TInfoBasePiece.class);
	}

	public TInfoBasePiece getPiece(String pieceId) throws ServiceException,
			SystemException, DAOException
	{
		return pieceDao.get(pieceId);
	}

	public List<TInfoBasePiece> getPieces() throws ServiceException,
			SystemException, DAOException
	{
		return pieceDao.findAll();
	}

	public Page<TInfoBasePiece> getPieces(Page<TInfoBasePiece> page,String orgId,String flag)
			throws ServiceException, SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		Criterion cr2 = Restrictions.eq("pieceFlag", flag);
		crs.add(cr2);
		if(!"".equals(orgId)&&orgId!=null){
		Criterion cr1 = Restrictions.eq("orgId", orgId);
		crs.add(cr1);
		return pieceDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
		}
		else{
			return pieceDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
		}
	}

	@Transactional(readOnly = false)
	public void savePiece(TInfoBasePiece piece)
			throws ServiceException, SystemException, DAOException
	{
		pieceDao.save(piece);
	}

	@Transactional(readOnly = false)
	public String deletePiece(String pieceId) throws ServiceException,
			SystemException, DAOException
	{
		
		String flag = null;
		if(!pieceId.equals(null)){
		String a[] = pieceId.split(",");
		for(int i=0;i<a.length;i++){
			pieceDao.delete(a[i]);
			flag = "success";
		} 
		}
		return flag;
	}

	public Page<TInfoBasePiece> getPiecesByTitle(Page<TInfoBasePiece> page,String title,String orgId,String flag)
			throws ServiceException, SystemException, DAOException {
		List<Criterion> crs = new ArrayList<Criterion>();
		Criterion cr = Restrictions.like("pieceTitle", "%"+title+"%");
		crs.add(cr);
		Criterion cr2 = Restrictions.eq("pieceFlag", flag);
		crs.add(cr2);
		if(!"".equals(orgId))
		{
		Criterion cr1 = Restrictions.eq("orgId", orgId);
		crs.add(cr1);
		}
		return pieceDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
	}


}
