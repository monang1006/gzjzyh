package com.strongit.workflow.workflowDesign.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSystemformModel;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class ProcessDesignManager extends BaseManager {

	private GenericDAOHibernate<ToaSystemformModel, java.lang.String>formModelDao;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		formModelDao=new GenericDAOHibernate<ToaSystemformModel, java.lang.String>(sessionFactory,ToaSystemformModel.class);
	}
	
	/** 用户管理Service接口*/
	private IUserService userservice;
	
	@Autowired IProcessDefinitionService manager;
	
	/**
	 * 获取当前用户所在机构的所有表单模板
	 * @author zhengzb
	 * @desc 
	 * 2010-11-6 上午09:36:39 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaSystemformModel> getAllModelList() throws SystemException,ServiceException{   
		try {
			User user=userservice.getCurrentUser();											//获取当前用户
			Organization org=userservice.getUserDepartmentByUserId(user.getUserId());		//获取当前用户所属机构
			String hql="from ToaSystemformModel t where t.modelDepartment=? order by t.modelDate desc ";
			List<ToaSystemformModel>list=formModelDao.find(hql.toString(),org.getOrgId());
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取所有表单模板" });
		}
	}
	
	
	/**
	 * 根据流程类型id,获取当前流程下的所有表单模板
	 * @author zhengzb
	 * @desc 
	 * 2010-11-8 下午03:11:18 
	 * @param modelProcesstype
	 * @return
	 */
	public List<ToaSystemformModel> getModelListByPtId(String modelProcesstype) throws SystemException,ServiceException{   
		try {
			User user=userservice.getCurrentUser();											//获取当前用户
			Organization org=userservice.getUserDepartmentByUserId(user.getUserId());		//获取当前用户所属机构
			String hql="from ToaSystemformModel t where t.modelDepartment=? and t.modelProcesstype=? order by t.modelDate desc ";
			List<ToaSystemformModel>list=formModelDao.find(hql.toString(),org.getOrgId(),modelProcesstype);
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "获取所有表单模板" });
		}
	}
	
	/**
	 * 流程模板列表，分页
	 * @author zhengzb
	 * @desc 
	 * 2010-12-3 下午03:51:28 
	 * @param page
	 * @param modelProcesstype
	 * @param modelName
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page getWorkflowModel(Page page,ToaSystemformModel formModel,Date startDate,Date endDate) throws SystemException,ServiceException{
		try {

			
			List<Object>list=new ArrayList<Object>();
			User user=userservice.getCurrentUser();											//获取当前用户
			Organization org=userservice.getUserDepartmentByUserId(user.getUserId());		//获取当前用户所属机构
			StringBuffer hql = new StringBuffer("from ToaSystemformModel t where t.modelDepartment=?");
			list.add(org.getOrgId());
			if(formModel!=null&&formModel.getModelProcesstype()!=null&&!formModel.getModelProcesstype().equals("")&&!formModel.getModelProcesstype().equals("0")){
				hql.append(" and t.modelProcesstype=?");
				list.add(formModel.getModelProcesstype());
			}
			if(formModel!=null&&formModel.getModelName()!=null&&!formModel.getModelName().equals("")){
				formModel.setModelName(formModel.getModelName().trim());
				hql.append(" and t.modelName like ?");
				
				list.add("%"+formModel.getModelName()+"%");
			}
			if(startDate!=null){
				hql.append(" and t.modelDate>= ?");
				list.add(startDate);
			}
			if(endDate!=null){
				hql.append(" and t.modelDate<= ?");
				list.add(endDate);
			}
			
			Object[] obj = new Object[list.size()];
			for(int i=0;i<list.size();i++){
				obj[i]=list.get(i);
			}
			hql.append(" order by t.modelDate desc");
			List<ToaSystemformModel> modelList=new ArrayList<ToaSystemformModel>();
			page=formModelDao.find(page,hql.toString(), obj);
			
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"流程模板列表"});
		}
	}
	
	/**
	 * 根据流程模板ID，删除流程模板
	 * @author zhengzb
	 * @desc 
	 * 2010-12-6 下午04:32:01 
	 * @param modelId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteFormModel(String modelId) throws SystemException,ServiceException{
		try {
			formModelDao.delete(modelId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "删除流程模板" });
		}
	}
	
	/**
	 * 保存表单
	 * @author zhengzb
	 * @desc 
	 * 2010-11-6 上午09:36:28 
	 * @param model
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveFormModel(ToaSystemformModel model) throws SystemException,ServiceException{
		try {
			formModelDao.save(model);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "保存表单模板" });
		}
	}
	/**
	 * 根据模板表单ID，获取表单对象
	 * @author zhengzb
	 * @desc 
	 * 2010-11-6 上午09:35:58 
	 * @param modelId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaSystemformModel getFormModel(String modelId) throws SystemException,ServiceException{
		try {
			ToaSystemformModel model=formModelDao.get(modelId);
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] {"根据表单Id查询表单模板" });
		}
	}
	/**
	 * 判断表单名是否存在
	 * @author zhengzb
	 * @desc 
	 * 2010-11-6 上午09:35:37 
	 * @param modelName
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int getCountByModelName(String modelName ,String processType) throws SystemException,ServiceException{
		try {
			User user=userservice.getCurrentUser();											//获取当前用户
			Organization org=userservice.getUserDepartmentByUserId(user.getUserId());		//获取当前用户所属机构
			String hql="select count(*) from ToaSystemformModel t where t.modelDepartment=?  and t.modelName=? and t.modelProcesstype=?";
			int num= Integer.parseInt(formModelDao.findUnique(hql, org.getOrgId(),modelName,processType).toString());
//			int num=Integer.parseInt(formModelDao.findUnique(hql).toString());
			return num;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[]{"判断表单名是否存在"});	
		}
	}

	public IUserService getUserservice() {
		return userservice;
	}
	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}
}
