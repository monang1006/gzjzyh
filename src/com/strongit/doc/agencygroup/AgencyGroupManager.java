package com.strongit.doc.agencygroup;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.doc.bo.ToaGroupAgency;
import com.strongit.doc.bo.ToaGroupDet;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.FiltrateContent;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.IGenericDAO;
import com.strongmvc.orm.hibernate.Page;

/**
 * 机构分组服务类.
 * 
 * @author
 * @company Strongit Ltd. (C) copyright
 * @date
 * @version 2.0.2.3
 * @classpath
 * @comment
 * @email
 */
@Service
@Transactional
@OALogger
public class AgencyGroupManager {

	IGenericDAO<ToaGroupAgency, java.lang.String> agencyGroupDao = null; // 定义DAO操作类.
	@Autowired
	IUserService userService; // 统一用户服务类.

	private GroupDetManager agroupDetManager;

	/**
	 * 注入SESSION工厂
	 * 
	 * @author:
	 * @date:2010-7-7 下午04:54:30
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		agencyGroupDao = new GenericDAOHibernate<ToaGroupAgency, java.lang.String>(
				sessionFactory, ToaGroupAgency.class);
	}

	/**
	 * @param id
	 * @return com.strongit.oa.bo.ToaAddressGroup
	 * @roseuid 495041820148
	 */
	@Transactional(readOnly = true)
	public ToaGroupAgency getGroupById(String id) throws SystemException,
			ServiceException {
		try {
			return agencyGroupDao.get(id);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "发文组" });
		}
	}

	/**
	 * author:dengzc description:获取组下的列表 modifyer: description:
	 * 
	 * @param groupId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public List<ToaGroupDet> getDetByGroupId(String groupId)
			throws SystemException, ServiceException {
		try {
			return agroupDetManager.getDetByGroupId(groupId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "机构" });
		}
	}

	/**
	 * 获取所有联系组,返回字符串【包含组下面的人员数目】
	 * 
	 * @author:
	 * @date:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public String getAgencyGroupList() throws SystemException, ServiceException {
		try {
			String userId = userService.getCurrentUser().getUserId();
			StringBuffer html = new StringBuffer("");

			String groupHql = "select t.groupAgencyId,t.groupAgencyName from ToaGroupAgency t where userId=? order by t.groupAgencyName";
			String countHql = "select count(*) from ToaGroupDet t where t.toaGroupAgency.groupAgencyId=?";
			List groupLst = agencyGroupDao.find(groupHql, userId);
			if (groupLst != null && groupLst.size() > 0) {
				for (int i = 0; i < groupLst.size(); i++) {
					Object[] objs = (Object[]) groupLst.get(i);// Object[]{addrGroupId,addreGroupName}
					Object count = agencyGroupDao.findUnique(countHql, objs[0]);
					html.append("<li id=\"" + objs[0] + "\">").append(
							" 	<span>" + objs[1] + "(<font color='blue'>"
									+ count + "</font>)" + "</span>").append(
							"</li>");
				}
			}
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "发文组列表" });
		}
	}

	/**
	 * 
	 * @author:
	 * @date:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public Page getGroupList(Page page, String orgCode, String orgName)
			throws SystemException, ServiceException {
		try {
			StringBuilder hql = new StringBuilder(
					"select tm.orgId,tm.orgSyscode,tm.orgName,tm.orgTel,tm.orgAddr from TUumsBaseOrg tm ");
			hql.append(" where tm.isOrg=1"); // 表关联T_UUMS_BASE_ORG

			if (null != orgCode && !"".equals(orgCode)) {
				hql.append("and tm.orgSyscode like '%"
						+ FiltrateContent.getNewContent(orgCode) + "%' ");
			}
			if (null != orgName && !"".equals(orgName)) {
				hql.append("and tm.orgName like '%"
						+ FiltrateContent.getNewContent(orgName) + "%' ");
			}

			page = agencyGroupDao.find(page, hql.append(" order by tm.orgSequence ").toString());

			return page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "发文组列表" });
		}
	}

	@Transactional(readOnly = true)
	public Page getOrgs(Page page, String orgCode, String orgName)
			throws SystemException, ServiceException {
		try {
			StringBuilder hql = new StringBuilder(
					"select tm.orgId, tm.orgSyscode,tm.orgName,tm.orgTel,tm.orgAddr from TUumsBaseOrg tm where instr( tm.orgSyscode,'001999') > 0 and tm.orgSyscode not  in (select distinct subStr(orgSyscode, 0, length(orgSyscode) - 3) from TUumsBaseOrg  where  length(orgSyscode) != 3 ) ");
			if (null != orgCode && !"".equals(orgCode)) {
				hql.append("and tm.orgSyscode like '%"
						+ FiltrateContent.getNewContent(orgCode) + "%' ");
			}
			if (null != orgName && !"".equals(orgName)) {
				hql.append("and tm.orgName like '%"
						+ FiltrateContent.getNewContent(orgName) + "%' ");
			}
			
			page = agencyGroupDao.find(page, hql.append(" order by tm.orgSequence ").toString());
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "发文组列表" });
		}
	}
	
	/**
	 * author:luosy description: 查找重复的用户名 modifyer: description:
	 * 
	 * @param groupId
	 *            组id
	 * @param users
	 *            用户信息
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String checkOrgName(String groupId, String orgs)
			throws SystemException, ServiceException {
		List<ToaGroupDet> userlist = this.getDetByGroupId(groupId);// 获取组下的列表
		JSONArray userArray = JSONArray.fromObject(orgs);
		int count = userArray.size();
		String orgId = null;
		String orgName = null;
		String repname = "";
		if (null != userlist) {// 组下存在人员
			for (int i = 0; i < count; i++) {
				JSONObject user = userArray.getJSONObject(i);
				orgId = user.getString("orgId");
				orgName = user.getString("orgName");
				for (ToaGroupDet det : userlist) {
					if (det.getOrgId().equals(orgId)) {
						if (repname.indexOf(orgName) < 0) {
							repname += orgName + ",";
						}
					}
				}
			}
		}
		if (repname.length() > 0) {
			repname = repname.substring(0, repname.length() - 1);
		}
		return repname;
	}

	/**
	 * 将选择的机构导入到组中
	 * 
	 * @author:
	 * @date:
	 * @param groupId
	 *            发文组ID，不导入重复机构
	 * @param orgs
	 *            选定的机构信息【标准的JSON格式：[{\"name\":\"dengzc\",\"age\":123},{\"name\":\"andy\",\"age\":1111}]】
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void importPublic2Groups(String groupId, String orgs)
			throws SystemException, ServiceException {
		List<ToaGroupDet> userlist = this.getDetByGroupId(groupId);// 获取组下的列表 
		Boolean bl = true;

		JSONArray userArray = JSONArray.fromObject(orgs); 
		String orgId = null;
		ToaGroupAgency group = getGroupById(groupId);// 获取组对象
		for (int i = 0; i < userArray.size(); i++) {
			bl = true;
			JSONObject user = userArray.getJSONObject(i);
			orgId = user.getString("orgId"); 
			for (ToaGroupDet det : userlist) {
				if (det.getOrgId().equals(orgId)) {
					bl = false;
					break;//continue
				}
			}
			if (bl) {
				ToaGroupDet dets = new ToaGroupDet();
				dets.setOrgId(orgId);
				dets.setToaGroupAgency(group);
				agroupDetManager.save(dets);
			}
		} 
	}

	/**
	 * 获取指定当前用户的组织机构信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public Organization getUserDepartmentByUserId() throws SystemException {
		User user = userService.getCurrentUser();
		Assert.notNull(user, "当前用户不能为空！");
		String userId = user.getUserId();
		Organization org = userService.getUserDepartmentByUserId(userId);
		Assert.notNull(org, "当前用户所属机构不存在或已被删除！");
		return org;
	}

	/**
	 * @author：qint
	 * @time：
	 * @desc：新增
	 * @param
	 * @param
	 * @return void
	 */
	public void addGroupAgency() throws SystemException, ServiceException {
		try {
			ToaGroupAgency group = new ToaGroupAgency();
			/** 新建一个对象 */

			/** 值赋 */
			group.setUserId(this.userService.getCurrentUser().getUserId());
			group.setGroupAgencyName("");
			group.setGroupAgencyRemark("");
			this.save(group);
			/** 保存 */

		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "新增发文组" });
		}
	}

	/**
	 * @param group
	 * @roseuid 495041520242
	 */
	public void deleteGroup(String group,OALogInfo...infos) throws SystemException,
			ServiceException {
		try {
			// ToaGroupAgency groupA = agencyGroupDao.get(group);
			agencyGroupDao.delete(group);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "发文组" });
		}
	}
	/**
	 * 保存
	 * 
	 * @author:
	 * @date:
	 * @param model
	 */
	public void save(ToaGroupAgency model,OALogInfo...infos) {
		save(model);
	}
	/**
	 * 保存
	 * 
	 * @author:
	 * @date:
	 * @param model
	 */
	void save(ToaGroupAgency model) {
		if (model.getUserId() == null || "".equals(model.getUserId())) {
			model.setUserId(userService.getCurrentUser().getUserId());
		}

		agencyGroupDao.save(model);
	}

	public GroupDetManager getAgroupDetManager() {
		return agroupDetManager;
	}

	@Autowired
	public void setAgroupDetManager(GroupDetManager agroupDetManager) {
		this.agroupDetManager = agroupDetManager;
	}

	public IUserService getUserService() {
		return userService;
	}

}
