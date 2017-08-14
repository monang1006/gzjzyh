package com.strongit.oa.report;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaReportDefinition;
import com.strongit.oa.bo.ToaReportSort;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;


@Service
@Transactional
@OALogger
public class WorkflowReportSortManager {
	
	private GenericDAOHibernate<ToaReportSort, java.lang.String>reportSortDao;
	
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		reportSortDao=new GenericDAOHibernate<ToaReportSort, String>(session,ToaReportSort.class);
	}
	
	private IUserService userService;
	
	/*
	 * 流程报表类型分页列表
	 */
	public Page<ToaReportSort> getPage(Page<ToaReportSort> page, ToaReportSort model) throws SystemException,ServiceException{
		try {
			User user=userService.getCurrentUser();
			TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(user.getUserId());
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构   
			
			List<String> list=new ArrayList<String>();
			StringBuffer hql = new StringBuffer();
			hql.append(" from ToaReportSort t where 1=1");
			if(model!=null&&model.getSortName()!=null&&!"".equals(model.getSortName())){				//类型名
				hql.append(" and t.sortName like ?");
				model.setSortName(model.getSortName().trim());
				list.add("%"+model.getSortName()+"%");
			}
			
			if(model!=null&&model.getSortSequence()!=null&&!"".equals(model.getSortSequence())){
				hql.append(" and t.sortSequence like ?");
				list.add("%"+model.getSortSequence()+"%");
			}
			if(model!=null&&model.getSortDesc()!=null&&!"".equals(model.getSortDesc())){				//类型描述
				hql.append(" and t.sortDesc like ?");
				model.setSortDesc(model.getSortDesc().trim());
				list.add("%"+model.getSortDesc()+"%");
			}
			if (isView) {
				if(org!=null){					
					hql.append(" and t.sortOrgCode like ?");
					list.add(org.getSupOrgCode()+"%");
				}
			}else {
				if(org!=null){
					hql.append(" and t.sortOrgId =?");
					list.add(org.getOrgId());
				}
			}
			
			hql.append(" order by t.sortSequence asc ");
			if(list!=null&&list.size()>0){
				Object[] obj=new Object[list.size()];
				for(int i=0;i<list.size();i++){
					obj[i]=list.get(i);
				}
				page = reportSortDao.find(page, hql.toString(),obj);
				
			}else {
				page = reportSortDao.find(page, hql.toString());
			}
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 流程报表类型分页列表"}); 
		}
	}
	
	/*
	 * 根据ID获取流程报表类型
	 */
	public ToaReportSort getReportSortById(String sortId)throws SystemException,ServiceException{
		try {
			ToaReportSort reportSort=reportSortDao.get(sortId);
			return reportSort;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据ID获取流程报表类型"}); 
		}
	}
	
	/*
	 * 根据ID删除流程报表类型
	 */
	public void delReoprtSortById(String sortId)throws SystemException,ServiceException{
		try {
			reportSortDao.delete(sortId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"根据ID删除流程报表类型"}); 
		}
	}
	
	/*
	 * 查询所有流程报表类型List
	 */
	public List<ToaReportSort> getAllList()throws SystemException,ServiceException{
		try {
			List<ToaReportSort> list=new ArrayList<ToaReportSort>();
//			StringBuffer hql = new StringBuffer();
//			hql.append(" select * from ToaReportSort t where 1=1");
//			hql.append(" order by t.sortSequence desc ");
			list=reportSortDao.findAll();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查询所有流程报表类型List"}); 
		}
	}
	
	/*
	 * 保存报表类型
	 */
	public String save(ToaReportSort model)throws SystemException,ServiceException{
		try {
			reportSortDao.save(model);
			return model.getSortId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"保存报表类型"}); 
		}
	}
	
	

	/**
	 * 判断类型名是否存在
	 * @author zhengzb
	 * @desc 
	 * 2010-12-17 上午10:29:17 
	 * @param sortName   报表类型名
	 * @return
	 * 返回 true: 已存在
	 *      false: 不存在
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean isHasSortName(String sortName,String sortId) throws SystemException,ServiceException{
		try {
			List<ToaReportSort> list=new ArrayList<ToaReportSort>();
			StringBuffer hql = new StringBuffer();
			hql.append(" from ToaReportSort t where t.sortName=?");
			list=reportSortDao.find(hql.toString(),sortName);
			if(list!=null&&list.size()>0){
				ToaReportSort sortmodel=list.get(0);
				if(sortId!=null&&!sortId.equals("")&&sortmodel.getSortId().equals(sortId)){
					return false;
				}else {
					
					return true;
				}
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"判断类型名是否存在"}); 
		}
	}

	public IUserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	

}
