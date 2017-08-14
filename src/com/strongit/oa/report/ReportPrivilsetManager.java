package com.strongit.oa.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaReportDefinition;
import com.strongit.oa.bo.ToaReportPrivilset;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.Role;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
@OALogger
public class ReportPrivilsetManager {
	
	private GenericDAOHibernate<ToaReportPrivilset, java.lang.String>privilsetDao;
	
	@Autowired ReportDefineManager reportDefineManager ;
	@Autowired IUserService userService ;
	
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		privilsetDao=new GenericDAOHibernate<ToaReportPrivilset, String>(session,ToaReportPrivilset.class);
	}
	
	/**
	 * @author:luosy
	 * @description:保存报表权限
	 * @date : 2010-12-20
	 * @modifyer:
	 * @description:
	 * @param model
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String save(ToaReportPrivilset model)throws SystemException,ServiceException{
		try {
			privilsetDao.save(model);
			return model.getPrivilsetId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 保存定义报表"}); 
		}
	}
	
	/**
	 * @author:luosy
	 * @description:	删除权限数据
	 * @date : 2010-12-20
	 * @modifyer:
	 * @description:
	 * @param definitionId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void delete(String privilsetId)throws SystemException,ServiceException{
		try {
			privilsetDao.delete(privilsetId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"删除定义报表"}); 
		}
	}
	
	
	/**
	 * @author:luosy
	 * @description: 根据获取定义报表的ID 获取其权限列表
	 * @date : 2010-12-20
	 * @modifyer:
	 * @description:
	 * @param definitionId 定义报表ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaReportPrivilset> getPrivisetListByDefinitionId(String definitionId)throws SystemException,ServiceException{
		try {
			 return reportDefineManager.getPrivisetListByDefinitionId(definitionId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据ID获取定义报表中的权限列表"});
		}
	}
	
	/**
	 * @author:luosy
	 * @description: 根据获取定义报表的ID 获取其权限列表
	 * @date : 2010-12-20
	 * @modifyer:
	 * @description:
	 * @param definitionId 定义报表ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaReportDefinition getDefinitionByDefinitionId(String definitionId)throws SystemException,ServiceException{
		try {
			 return reportDefineManager.getDefinitionById(definitionId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据ID获取定义报表中的权限列表"});
		}
	}
	
	/**
	 * @author:luosy
	 * @description:获取所有未删除的机构树
	 * @date : 2010-12-24
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws SystemException
	 */
	public List<TempPo> getOrgTreeList()throws SystemException {
		try {
			List<TempPo> newList = new ArrayList<TempPo>();//构造树节点
			List<Organization> list = userService.getOrgs("0");
			
			String rootName = "所有部门";
			TempPo root = new TempPo();//构造根节点
			root.setId("0");
			root.setParentId("root");
			root.setName(rootName);
			root.setType("false");//这种类型节点不可编辑
			newList.add(root);
			
			if(list != null && !list.isEmpty()){
				for(Organization item : list){
					TempPo po = new TempPo();
					po.setId(item.getOrgId());
					po.setParentId(item.getOrgParentId());
					po.setName(item.getOrgName());
					po.setType("true");
					newList.add(po);
				}
			}
			return newList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"删除定义报表"}); 
		}
	}
	
	/**
	 * @author:luosy
	 * @description: 获取所有已启用的角色
	 * @date : 2010-12-24
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws SystemException
	 */
	public List<TempPo> getRoleTreeList()throws SystemException {
		try {
			List<TempPo> newList = new ArrayList<TempPo>();//构造树节点
			List<Role> list = userService.getAllUsedRoleInfos("1");
			String rootName = "所有角色";
			TempPo root = new TempPo();//构造根节点
			root.setId(GlobalBaseData.GXSYGJ);
			root.setParentId("root");
			root.setName(rootName);
			root.setType("false");//这种类型节点不可编辑
			newList.add(root);
			
			if(list != null && !list.isEmpty()){
				for(Role item : list){
					TempPo po = new TempPo();
					po.setId(item.getRoleId());
					po.setParentId("0");
					po.setName(item.getRoleName());
					po.setType("true");
					newList.add(po);
				}
			}
			return newList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"删除定义报表"}); 
		}
	}
	
	/**
	 * @author:luosy
	 * @description: 根据userid获取其有权限的报表
	 * @date : 2010-12-24
	 * @modifyer:
	 * @description:
	 * @param userId
	 * @return  List<Object[]>--0:定义报表名，1:定义报表类型ID，2:定义报表类型名，3:定义报表ID
	 * @throws SystemException
	 */
	public List<Object[]> getReportPrivilsetByUserId(String userId)throws SystemException {
		try {
			List<Object[]> list = new ArrayList<Object[]>();
			TUumsBaseUser user = userService.getUserInfoByUserId(userId);
			String level = user.getRest3();//权限级别
			if(level==null||"".equals(level.trim())){
				return list;
			}
			String orgId = user.getOrgId();
			List<Role> roleList =  userService.getUserRoleInfosByUserId(userId,"1");
			StringBuilder rolesql = new StringBuilder();
			rolesql.append(" ");
			for(Role role : roleList){
				rolesql.append("and t.privilsetTypeid='").append(role.getRoleId()).append("' ");
			}
			
			TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userId);
			StringBuilder orghql = new StringBuilder();
			if(userService.isViewChildOrganizationEnabeld()){			//是否允许看到下级机构
				if(org!=null){
					orghql.append("  t.toaReportDefinition.definitionOrgCode like '").append(org.getSupOrgCode()).append("%' ");
				}
			}else {
				if(org!=null){
					orghql.append("  t.toaReportDefinition.definitionOrgId = '").append(org.getOrgId()).append("' ");
				}
			}
			
			
			StringBuilder sql = new StringBuilder();
			sql.append(" select distinct t.toaReportDefinition.definitionName , t.toaReportDefinition.toaReportSort.sortId , t.toaReportDefinition.toaReportSort.sortName , t.toaReportDefinition.definitionId from ToaReportPrivilset t where ")
			.append("(").append(orghql.toString()).append(") and( ")
			.append("(t.privilsetTypeflag=1 and ").append("t.privilsetTypeid='").append(orgId).append("' and").append(" t.privilsetLevel<=").append(level).append(" ) ")//部门
			.append(" or ")
			.append("(t.privilsetTypeflag=2 ").append(rolesql).append(" and t.privilsetLevel<=").append(level).append(" ) ")//角色
			.append(" or ")
			.append("(t.privilsetTypeflag=0 and ").append(" t.privilsetLevel<=").append(level).append(" ) ")//角色
			.append(" or ")
			.append("(t.toaReportDefinition.definitionCreateUserId='").append(userId).append("' ) ")//角色
			.append(" ) ");
			
			list = privilsetDao.find(sql.toString());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"删除定义报表"}); 
		}
	}
	
	/**
	 * @author:luosy
	 * @description: 删除报表权限
	 * @date : 2010-12-24
	 * @modifyer:
	 * @description:
	 * @param list
	 * @throws SystemException
	 */
	public void deletePrivilsetByList(List<ToaReportPrivilset> list)throws SystemException {
		try {
			privilsetDao.delete(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"删除定义报表"}); 
		}
	}
	
}
