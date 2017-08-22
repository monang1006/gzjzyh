package com.strongit.gzjzyh.policeregister;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
import com.strongit.gzjzyh.tosync.IToSyncManager;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.rolemanage.IRoleManager;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class PoliceRegisterManager implements IPoliceRegisterManager {
	
	private GenericDAOHibernate<Object, java.lang.String> baseDao;
	
	@Autowired
	IUserService userService;// 统一用户服务
	@Autowired
	MyInfoManager myInfoManager;
	@Autowired
	IRoleManager roleManager;
	@Autowired
	IToSyncManager toSyncManager;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new GenericDAOHibernate<Object, java.lang.String>(sessionFactory, Object.class);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(TGzjzyhUserExtension model) throws SystemException {
		boolean isAdd = (model.getUeId() == null || "".equals(model.getUeId()));
		TUumsBaseUser user = model.getTuumsBaseUser();
		if(isAdd) {
			user.setOrgIds("," + user.getOrgId() + ",");
			TUumsBaseOrg org = this.userService.getOrgInfoByOrgId(user.getOrgId());
			user.setSupOrgCode("," + org.getOrgSyscode() + ",");
			user.setUserIsactive(Const.IS_YES);
		}
		this.userService.saveUser(user);
		
		// 处理同步到个人信息中；added by dengzc 2010年5月18日10:56:21
		ToaPersonalInfo myInfo = myInfoManager.getInfoByUserid(user
				.getUserId());
		if (myInfo == null) {// 个人信息不存在
			myInfo = new ToaPersonalInfo();
			myInfo.setUserId(user.getUserId());
		}
		myInfo.setPrsnName(user.getUserName());// 姓名
		myInfo.setPrsnMobile1(user.getRest2());// 手机号码
		myInfo.setPrsnMail1(user.getUserEmail());// email
		myInfo.setPrsnTel1(user.getUserTel());// 电话
		myInfoManager.saveObj(myInfo);
		
		if(isAdd) {
			TUumsBaseRole bankRole = this.roleManager.getRoleInfoByRoleCode(GzjzyhConstants.UNACTIVE_ROLE);
			this.roleManager.saveRoleUsers(bankRole.getRoleId(), user.getUserId());
		}
		
		model.setUeDate(new Date());
		model.setUeStatus(GzjzyhConstants.STATUS_WAIT_AUDIT);
		this.baseDao.save(model);
	}

	@Override
	public TGzjzyhUserExtension getUserExtensionById(String ueId)
			throws SystemException {
		TGzjzyhUserExtension userExtension = null;
		if(ueId != null && !"".equals(ueId)){
			List<TGzjzyhUserExtension> lst = this.baseDao.find("from TGzjzyhUserExtension t where t.ueId=?", new Object[]{ueId});
			if(lst != null && !lst.isEmpty()){
				userExtension = lst.get(0);
			}
		}
		return userExtension;
	}

	@Override
	public TGzjzyhUserExtension getUserExtensionByUserId(String userId) throws SystemException {
		TGzjzyhUserExtension userExtension = null;
		if(userId != null && !"".equals(userId)){
			List<TGzjzyhUserExtension> lst = this.baseDao.find("from TGzjzyhUserExtension t where t.tuumsBaseUser.userId=?", new Object[]{userId});
			if(lst != null && !lst.isEmpty()){
				userExtension = lst.get(0);
			}
		}
		return userExtension;
	}
	
	@Override
	public Page<TGzjzyhUserExtension> queryApplyPage(
			Page page, String searchLoginName, String searchName,
			Date appStartDate, Date appEndDate) throws SystemException {
		StringBuffer hql = new StringBuffer(
				"from TGzjzyhUserExtension t where 1=1");
		List values = new ArrayList();
		if(searchLoginName != null && !"".equals(searchLoginName)) {
			hql.append(" and t.tuumsBaseUser.userLoginname like ?");
			values.add("%" + searchLoginName + "%");
		}
		if(searchName != null && !"".equals(searchName)) {
			hql.append(" and t.tuumsBaseUser.userName like ?");
			values.add("%" + searchName + "%");
		}
		if (appStartDate != null) {
			hql.append(" and t.ueDate >= ? ");
			values.add(appStartDate);
		}
		if (appEndDate != null) {
			hql.append(" and t.ueDate <= ? ");
			values.add(appEndDate);
		}

		hql.append(" order by t.ueStatus, t.ueDate desc");
		page = this.baseDao.find(page, hql.toString(), values.toArray());
		return page;
	}

	
}
