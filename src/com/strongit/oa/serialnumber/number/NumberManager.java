/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-7-10
 * Autour: hull
 * Version: V1.0
 * Description： 文号manager
 */
package com.strongit.oa.serialnumber.number;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSerialnumberNumber;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class NumberManager {

	private GenericDAOHibernate<ToaSerialnumberNumber, String> numberDAO;

	/**
	 * setSessionFactory
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		numberDAO = new GenericDAOHibernate<ToaSerialnumberNumber, String>(
				sessionFactory, ToaSerialnumberNumber.class);
	}

	/**
	 * 保存序列
	 * @param number
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveNumber(ToaSerialnumberNumber number)
	throws SystemException, ServiceException {
		try {
			numberDAO.save(number);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"保存生成序列Manager"});
		}

	}

	/**
	 * 删除序列
	 * @param numberId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteNumber(String numberId) throws SystemException,
	ServiceException {
		try {
			String[] id=numberId.split(",");
			for(int i=0;i<id.length;i++){
				numberDAO.delete(id[i]);
			}
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"删除生成序列Manager"});
		}
	}

	/**
	 * 根据ID查询序列
	 * @param numberId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaSerialnumberNumber getNumberById(String numberId)
	throws SystemException, ServiceException {
		try {
			return numberDAO.get(numberId);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据ID查询序列Manager"});
		}
	}

	/**
	 * 查询所有序列
	 * @param page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaSerialnumberNumber> getAllNumber(
			Page<ToaSerialnumberNumber> page) throws SystemException,
			ServiceException {
		try {
			String hql="from ToaSerialnumberNumber t where t.numberState is not null  order by t.numberTime desc";
			return numberDAO.find(page,hql);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"查询序列列表Manager"});		}
	}

	/**
	 * 根据多条件查询
	 * @param page
	 * @param model
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaSerialnumberNumber> getNumberByProperty(
			Page<ToaSerialnumberNumber> page, ToaSerialnumberNumber model)
			throws SystemException, ServiceException {
		try {
			List<Object> list =new ArrayList<Object>();
			String hql = "from ToaSerialnumberNumber t  where t.numberState is not null";
			if (model.getNumberNo() != null && !"".equals(model.getNumberNo())) {//判断文号是否为空
				hql = hql + " and t.numberNo like ?";
				list.add("%"+model.getNumberNo()+"%");
				} 
			if(model.getOrgName()!=null&&!"".equals(model.getOrgName())){
				hql =hql + " and t.orgName like ?";
				list.add("%"+model.getOrgName()+"%");
			}
			if(model.getUserName()!=null&&!"".equals(model.getUserName())){
				hql=hql+" and t.userName like ?";
				list.add("%"+model.getUserName()+"%");
			}
			if (model.getNumberTime() != null) {//文号为空时创建时间是否为空
				hql = hql + " and t.numberTime>?";
				list.add(model.getNumberTime());
			}
			if (model.getNumberState() != null&&!model.getNumberState().equals("")) {//文号状态
				hql = hql + " and t.numberState=?";
				list.add(model.getNumberState());
			}
			hql=hql+" order by t.numberTime desc";
			if(list.size()>0){
				Object[] obj=new Object[list.size()];
				for(int i=0;i<list.size();i++){
					obj[i]=list.get(i);

				}
				return numberDAO.find(page, hql, obj);
			}
			return numberDAO.find(page, hql);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据多条件查询序列列表Manager"});		
		}
	}

	/**
	 * 格式化时间
	 */
	public String getDateTime(Date time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(time);
		return date;
	}


	/**
	 * 统计序列号个数
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getCount(String orgId,String abb,String dictcode,String year)throws SystemException,
	ServiceException {
		try {
//			String hql="select count(*) from ToaSerialnumberNumber t where t.orgId=?";
//			Object obj=numberDAO.findUnique(hql,orgid);
//			return Integer.parseInt(obj.toString());
			//根据科技厅需求修改
			String daizi="";
			if(abb!=null&&abb.length()>3&&!abb.equals("赣科发字")&&!abb.equals("赣科办字")){
				daizi=abb.substring(0,3);
			}
			String hql="select count(*) from ToaSerialnumberNumber t where 1=1";
			if(dictcode!=null&&!dictcode.equals("")){
				hql=hql+" and t.numberDict="+dictcode;
			}
			if(!daizi.equals("")&&daizi.length()>0){
				abb="'"+daizi+"%'";
				hql=hql+"  and t.numberAbbreviation like "+abb+" and  t.numberAbbreviation not in('赣科发字','赣科办字')";
			}else {
				hql=hql+"  and t.numberAbbreviation like '"+abb+"' ";
			}
			
			//hql=hql+" and t.orgId=? and t.numberYear=?";
			hql=hql+" and t.numberYear=?";
				
			
			int num=Integer.parseInt(numberDAO.findUnique(hql, year).toString());
			return num;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据多条件查询序列列表Manager"});		
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据多条件查询序列列表Manager"});		
		}
	}
	/**
	 * 查询所有机构下当前序列号是否已经存在
	 * @param numberNo
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getCountByNo(String numberNo, String orgid)
			throws SystemException, ServiceException {
		try {
			
			String hql = "select count(*) from ToaSerialnumberNumber t where t.numberNo=? ";
			if(orgid!=null&&!"".equals(orgid)){
				//hql=hql+" and t.orgId=?";
				
				return Integer.parseInt(numberDAO.findUnique(hql, numberNo).toString());			//, orgid
			}else{
			return Integer.parseInt(numberDAO.findUnique(hql, numberNo)
					.toString());
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据多条件查询序列列表Manager"});		
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据多条件查询序列列表Manager"});		
		}
	}
	
	
	/**
	 * 查询机构中当前序列号是否已经存在
	 * @param numberNo
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getAloneOrgCountByNo(String numberNo, String orgid)
			throws SystemException, ServiceException {
		try {
			
			String hql = "select count(*) from ToaSerialnumberNumber t where t.numberNo=? ";
			if(orgid!=null&&!"".equals(orgid)){
				hql=hql+" and t.orgId=?";
				
				return Integer.parseInt(numberDAO.findUnique(hql, numberNo, orgid)
						.toString());			//, orgid
			}else{
			return Integer.parseInt(numberDAO.findUnique(hql, numberNo)
					.toString());
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据多条件查询序列列表Manager"});		
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据多条件查询序列列表Manager"});		
		}
	}
	
	/**
	 * 查询用户所在机构预留文号
	 * @author 胡丽丽
	 * @date 2010-01-13
	 * @param orgid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaSerialnumberNumber> getMakeNumber(String orgid,String year)throws SystemException, ServiceException {
		try {
			String hql="from ToaSerialnumberNumber t where t.numberState='1'  and  t.orgId=? and t.numberYear=?";
			return numberDAO.find(hql,orgid,year);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"查询预留文号"});		
		}
	}
	/**
	 * 查询用户所在机构回收文号
	 * @author 胡丽丽
	 * @date 2010-01-13
	 * @param orgid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaSerialnumberNumber> getRecycleNumber(String orgid,String year)throws SystemException, ServiceException {
		try {
			String hql="from ToaSerialnumberNumber t where t.numberState='2' and  t.orgId=? and t.numberYear=?";
			return numberDAO.find(hql,orgid,year);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"查询回收文号"});		
		}
	}

	/**
	 * 修改文号状态
	 * @author 胡丽丽
	 * 
	 * @param numberNo
	 * @param state
	 * @param orgId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int updateNumberState(String numberNo,String state,String orgId,String orgName)throws SystemException,ServiceException{
		try {
			
			//String hql="update ToaSerialnumberNumber t set t.numberState=? where t.numberNo=? and t.orgId=?";
			String hql="from ToaSerialnumberNumber t  where t.numberNo like ? ";
			ToaSerialnumberNumber serialnumberNumber=(ToaSerialnumberNumber)numberDAO.find(hql,numberNo).get(0);
			int count=0;
			if(serialnumberNumber!=null&&serialnumberNumber.getNumberId().length()>2){
				serialnumberNumber.setNumberState(state);
				serialnumberNumber.setOrgId(orgId);
				serialnumberNumber.setOrgName(orgName);
				numberDAO.update(serialnumberNumber);
				count=1;
			}
//			Query query=numberDAO.createQuery(hql, state,numberNo,orgId);
//			int count=query.executeUpdate();
			return count;
		}  catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.create_error,
					new Object[]{"修改文号状态出错"});	
		}
	}
	/**
	 * 验证文号是否可以使用
	 * @author 胡丽丽
	 * @date 2010-01-20
	 * @param numberNo
	 * @param orgid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getIsNumberNo(String numberNo, String orgid)throws SystemException, ServiceException {
		try {
			String hql = "select count(*) from ToaSerialnumberNumber t where t.numberNo=? and (t.numberState is null or t.numberState='0')";
			if(orgid!=null&&!"".equals(orgid)){
				//hql=hql+" and t.orgId=?";
				return Integer.parseInt(numberDAO.findUnique(hql, numberNo)		//, orgid
						.toString());
			}else{
			return Integer.parseInt(numberDAO.findUnique(hql, numberNo)
					.toString());
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据多条件查询序列列表Manager"});		
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据多条件查询序列列表Manager"});		
		}
	}
	
	
}
