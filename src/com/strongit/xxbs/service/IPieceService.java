package com.strongit.xxbs.service;

import java.util.List;

import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.entity.TInfoBasePiece;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IPieceService
{
	public TInfoBasePiece getPiece(String pieceId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBasePiece> getPieces()
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBasePiece> getPieces(Page<TInfoBasePiece> page,String orgId,String flag)
			throws ServiceException,SystemException, DAOException;
	
	public void savePiece(TInfoBasePiece piece)
			throws ServiceException,SystemException, DAOException;
	
	public String deletePiece(String pieceId)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBasePiece> getPiecesByTitle(Page<TInfoBasePiece> page,String title,String orgId,String flag)
	throws ServiceException,SystemException, DAOException;
	
}
