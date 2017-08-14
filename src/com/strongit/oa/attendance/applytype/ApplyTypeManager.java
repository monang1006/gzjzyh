package com.strongit.oa.attendance.applytype;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.bo.ToaAttendType;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
/**
 * 申请单类型manager
 * @author 胡丽丽
 * @createdate:2009-11-06
 *
 */
@Service
@Transactional
public class ApplyTypeManager {

	private GenericDAOHibernate<ToaAttendType, String> attleTypeDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
    {
		attleTypeDao = new GenericDAOHibernate<ToaAttendType, java.lang.String>(sessionFactory,
		 ToaAttendType.class);
	}
	
	/**
	 * 保存申请类型
	 * @author 胡丽丽
	 * @createdate:2009-11-06
	 * @param model
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveAttendType(ToaAttendType model)throws SystemException,ServiceException{
		try {
			attleTypeDao.save(model);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] {"申请类型保存出错"});
		}
	}
	/**
	 *  启用或禁止
	 *  @author 胡丽丽
	 *  @createdate:2009-11-16
	 * @param isenable
	 * @param typeids
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void updateTypeByIsEnable(String isenable,String[] typeids) throws SystemException,ServiceException{
		try {
			String ids="";
			for(int i=0;i<typeids.length;i++){
			   ids=ids+",'"+typeids[i]+"'";
			}
			ids=ids.substring(1);
			String hql="update ToaAttendType t set t.isEnable=? where t.typeId in ("+ids+")";
			Query query=attleTypeDao.createQuery(hql,isenable);
			query.executeUpdate();
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] {"申请类型启用或禁用出错！"});
		}
	}
	/**
	 * 删除申请类型
	 * @author 胡丽丽
	 * @createdate:2009-11-06
	 * @param typeId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteById(String typeId)throws SystemException,ServiceException{
		try {
			attleTypeDao.delete(typeId);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] {"申请类型删除出错"});
		}
	}
	/**
	 * 根据ID查询申请单类型
	 * @author 胡丽丽
	 * @createdate:2009-11-06
	 * @param typeid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaAttendType getAttendTypeByID(String typeid)throws SystemException,ServiceException{
		try {
			return attleTypeDao.get(typeid);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"根据ID查询申请类型出错"});
		}
	}
	/**
	 * 查询所有申请单类型
	 * @author 胡丽丽
	 * @createdate:2009-11-06
	 * @param typeid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaAttendType> getAllAttendType()throws SystemException,ServiceException{
		try {
			String hql="from ToaAttendType t  order by t.typeCreateDate desc";
			return attleTypeDao.find(hql);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查询所有申请类型出错"});
		}
	}
	/**
	 * 查询所有申请单类型
	 * @author 胡丽丽
	 * @createdate:2009-11-06
	 * @param typeid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaAttendType> getAllAttendType( Page<ToaAttendType>  page)throws SystemException,ServiceException{
		try {
			String hql="from ToaAttendType t order by t.typeCreateDate desc";
			return attleTypeDao.find(page,hql);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"查询所有申请类型出错"});
		}
	}
	/**
	 * 查询是申请类型的类型
	 * @param applyType
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaAttendType> getAttendTypeByIsApplyType(String applyType)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaAttendType t where t.isApplyType=? order by t.typeCreateDate desc";
			return attleTypeDao.find(hql, applyType);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "查询所有申请类型出错" });
		}
	}
	/**
	 * 查询是申请类型的类型
	 * @param applyType
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaAttendType> getAttendTypeByIsApplyType(Page<ToaAttendType> page,String applyType)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaAttendType t where t.isApplyType=? order by t.typeCreateDate desc";
			return attleTypeDao.find(page,hql, applyType);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "查询所有申请类型出错" });
		}
	}
	
	public Page<ToaAttendType> search(Page<ToaAttendType> page,ToaAttendType model)throws SystemException, ServiceException {
		String hql = "from ToaAttendType t where t.isApplyType=? ";
		List<Object> list=new ArrayList<Object>();
		list.add("0");
		//申请类型名称
		if(model.getTypeName()!=null&&!"".equals(model.getTypeName())){
			hql=hql+" and t.typeName like ?";
			list.add("%"+model.getTypeName()+"%");
		}
		//缺勤
		if(model.getIsAbsence()!=null&&!"".equals(model.getIsAbsence())){
			hql=hql+" and t.isAbsence=?";
			list.add(model.getIsAbsence());
		}
		//是否启用
		if(model.getIsEnable()!=null&&!"".equals(model.getIsEnable())&&!", ".equals(model.getIsEnable())){
			hql=hql+" and t.isEnable=?";
			list.add(model.getIsEnable());
		}
		//是否是系统
		if(model.getIsSystem()!=null&&!"".equals(model.getIsSystem())){
			hql=hql+" and t.isSystem=?";
			list.add(model.getIsSystem());
		}
		//是否能补填申请单
		if(model.getCanRewriter()!=null&&!"".equals(model.getCanRewriter())){
			hql=hql+" and t.canRewriter=?";
			list.add(model.getCanRewriter());
		}
		//创建时间
		if(model.getTypeCreateDate()!=null){
			hql=hql+" and t.typeCreateDate>=?";
			list.add(model.getTypeCreateDate());
		}
		hql=hql+" order by t.typeCreateDate desc";
		Object[] obj=new Object[list.size()];
		for(int i=0;i<list.size();i++){
			obj[i]=list.get(i);
		}
		return attleTypeDao.find(page,hql, obj);
	}
}
