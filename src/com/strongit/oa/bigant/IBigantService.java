package com.strongit.oa.bigant;

import java.util.List;

import com.strongit.oa.bo.ToaBigAntUser;
import com.strongit.oa.bo.ToaGroup;
import com.strongit.oa.bo.ToaView;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IBigantService {
	public List<ToaView> getAllDeparments()throws SystemException,ServiceException;
	public List<ToaBigAntUser> getUsersByOrgID(int Col_HsItemID,int Col_HsItemType)throws SystemException,ServiceException;
	public List<ToaGroup>  getGroupById(int Col_HsItemID,int Col_HsItemType)throws SystemException,ServiceException;
	
	public boolean isHasChild(int Col_HsItemID,int Col_HsItemType)throws SystemException,ServiceException;
	public boolean isHasView(ToaView model)throws SystemException,ServiceException;
	public boolean isHasGroup(ToaGroup model)throws SystemException,ServiceException;
	public boolean isHasUser(ToaBigAntUser model)throws SystemException,ServiceException;
	
	public boolean saveView(ToaView model)throws SystemException,ServiceException;
	public boolean saveGroup(ToaGroup model)throws SystemException,ServiceException;
	public boolean saveUser(ToaBigAntUser model)throws SystemException,ServiceException;
	
	public boolean saveViewAndGroup(ToaView parent,ToaGroup child)throws SystemException,ServiceException;
	public boolean saveViewAndUser(ToaView parent,ToaBigAntUser child)throws SystemException,ServiceException;
	public boolean saveGroupAndGroup(ToaGroup parent,ToaGroup child)throws SystemException,ServiceException;
	public boolean saveGroupAndUser(ToaGroup parent,ToaBigAntUser child)throws SystemException,ServiceException;
	public boolean deleteAll()throws SystemException,ServiceException;
}
