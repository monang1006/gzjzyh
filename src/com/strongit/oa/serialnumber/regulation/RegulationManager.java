/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-7-10
 * Autour: hull
 * Version: V1.0
 * Description： 文号规则manager
 */
package com.strongit.oa.serialnumber.regulation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSerialnumberRegulation;
import com.strongit.oa.bo.ToaSerialnumberSort;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class RegulationManager {

	private GenericDAOHibernate<ToaSerialnumberRegulation, String> regulationDAO;
	
	
private IUserService userservice;
	
	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}

	/**
	 * setSessionFactory
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		regulationDAO = new GenericDAOHibernate<ToaSerialnumberRegulation, String>(
				sessionFactory, ToaSerialnumberRegulation.class);
	}

	
	
	
	/**
	 * 保存文号生成规则
	 * 
	 * @param regulation
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveRegulation(ToaSerialnumberRegulation regulation)
			throws SystemException, ServiceException {
		try {
			regulationDAO.save(regulation);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"保存文号生成规则Manager"});
		}
	}

	/**
	 * 删除文号生成规则
	 * 
	 * @param regulationId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteRegulation(String regulationId) throws SystemException,
			ServiceException {
		try {
			String[] id = regulationId.split(",");
			for (int i = 0; i < id.length; i++) {
				regulationDAO.delete(id[i]);
			}
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"删除文号生成规则Manager"});
		}
	}

	/**
	 * 根据ID查询文号生成规则
	 * @param regulationId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaSerialnumberRegulation getRegulationByID(String regulationId)
			throws SystemException, ServiceException {
		try {
			return regulationDAO.get(regulationId);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据ID查询文号生成规则Manager"});
		}
	}

	/**
	 * 根据单位查询文号生成规则
	 * @param page
	 * @param units
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaSerialnumberRegulation> getRegulationByUnits(
			Page<ToaSerialnumberRegulation> page, String units)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaSerialnumberRegulation t where t.regulationUnits=?";
			return regulationDAO.find(page, hql, units);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据单位查询文号生成规则Manager"});
		}
	}
	
	/**
	 * 根据单位查询文号生成规则列表
	 * @param units
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaSerialnumberRegulation> getRegulationByUnits(String units)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaSerialnumberRegulation t where t.regulationUnits=?";
			List<ToaSerialnumberRegulation> list=regulationDAO.find(hql, units);
			return list;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据单位查询文号生成规则Manager"});
		}
	}

	/**
	 * 查询所有文号生成规则列表
	 * @param page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaSerialnumberRegulation> getAllRegulation(
			Page<ToaSerialnumberRegulation> page) throws SystemException,
			ServiceException {
		try {
			return regulationDAO.findAll(page);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"文号生成规则查询Manager"});
		}
	}
	
	/**
	 * 根据多条件搜索
	 * @param page
	 * @param model
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaSerialnumberRegulation> getRegulationByProperty(Page<ToaSerialnumberRegulation> page,ToaSerialnumberRegulation model) throws SystemException,
			ServiceException {
		try {
			String hql="from ToaSerialnumberRegulation t where 1=1";
			List<Object> list =new ArrayList<Object>();
			if(model.getRegulationName()!=null&&!"".equals(model.getRegulationName())){//判断规则名称是否为空
			    hql=hql+" and t.regulationName like ?";	
			    list.add("%"+model.getRegulationName()+"%");
			}
		    if(model.getRegulationParagraph()!=null&&!"".equals(model.getRegulationParagraph())){//判断规则段落是否为空
		    	hql=hql+" and t.regulationParagraph=?";
		    	list.add(model.getRegulationParagraph());
		    }
		    if(model.getRegulationUser()!=null&&!"".equals(model.getRegulationUser())){//判断创建人是否为空
		    	hql=hql+" and t.regulationUser=?";
		    	list.add(model.getRegulationUser());
		    }
		    if(model.getRegulationTime()!=null){//判断时间是否为空
		    	hql=hql+" and t.regulationTime>=?";
		    	list.add(model.getRegulationTime());
		    }
		    if(model.getRegulationSort()!=null&&!"".equals(model.getRegulationSort())){//判断文号规则类型是否为空
				if(model.getRegulationSort().equals("0")){
					hql=hql+" and ( t.regulationSort=? or t.regulationSort=null)";
					list.add(model.getRegulationSort());
				}else {
					
					hql=hql+" and t.regulationSort=?";
					list.add(model.getRegulationSort());
				}
			}
			
			
			
			
			if(list.size()>0){
				Object[] obj=new Object[list.size()];
				for(int i=0;i<list.size();i++){
					obj[i]=list.get(i);
				}
				return regulationDAO.find(page, hql, obj);
			}else{
				return regulationDAO.find(page, hql);
			}
		}  catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据多条件查询序规则Manager"});		
		}
		
	}
	
	/**
	 * 
	 * @author zhengzb
	 * @desc 根据文号名，判断当前所添加的文号名，是否存在
	 * 2010-5-11 上午11:44:27 
	 * @param regulationName 文号名
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getIsRegulationName(String regulationName) throws SystemException,ServiceException {
		try {
			String hql="select count(*) from ToaSerialnumberRegulation t  ";
				hql=hql+" where t.regulationName like ?";					
			return Integer.parseInt(regulationDAO.findUnique(hql,regulationName).toString());
		} catch (Exception e) {
			// TODO: handle exceptio
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据文号规则名称查询文号规则是否存在"});	
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
	 * 查询所有文号生成规则列表
	 * @param page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaSerialnumberRegulation> getAllRegulation() throws SystemException,
			ServiceException {
		try {
			return regulationDAO.findAll();
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"文号生成规则查询Manager"});
		}
	}
	
	/**
	 * 根据文号规则类型查询规则列表 
	 * @author zhengzb
	 * @desc 
	 * 2010-10-14 下午03:03:59 
	 * @param regulationSort
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaSerialnumberRegulation> getRegulationListByRegulationsort(String regulationSort) throws SystemException,
	ServiceException {
		try {
			String hql="from ToaSerialnumberRegulation t where 1=1 ";
			Object[] obj=new Object[1];
			hql=hql+" and (t.regulationSort =? or t.regulationSort ='0' or t.regulationSort =null )";	
			obj[0]=regulationSort;
			hql=hql+" order by t.regulationSort  asc ,t.regulationName asc";
			List<ToaSerialnumberRegulation> list=regulationDAO.find(hql.toString(), obj);
			return list;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"文号生成规则查询Manager"});
		}
	}
	
	/**
	 * 
	 * @author zhengzb
	 * @desc 根据文号类型ID，查询文号规则中是否存在当前文号类型
	 * 2010-5-9 下午06:07:53 
	 * @param sortId  文号类型ID
	 * @return 返回 INT
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getIsSortId(String sortId)throws SystemException, ServiceException {
		try {
			String hql = "select count(*) from ToaSerialnumberRegulation t where t.sortId like ?";
			return Integer.parseInt(regulationDAO.findUnique(hql,sortId).toString());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据文号类型ID查询文号规则"});	
		}
	}
	
}
