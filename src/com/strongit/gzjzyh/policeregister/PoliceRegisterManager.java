package com.strongit.gzjzyh.policeregister;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.gzjzyh.GzjzyhApplicationConfig;
import com.strongit.gzjzyh.GzjzyhConstants;
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
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

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
		
		/*boolean isAdd = (model.getUserId() == null || "".equals(model.getUserId()));
		
		TUumsBaseOrg org = userService
				.getOrgInfoBySyscode(GzjzyhConstants.DEFAULT_BANKORG_SYSCODE);
		model.setUserSyscode(model.getUserLoginname());
		model.setOrgId(org.getOrgId());
		model.setOrgIds("," + org.getOrgId() + ",");
		model.setSupOrgCode("," + org.getOrgSyscode() + ",");

		userService.saveUser(model);

		// 处理同步到个人信息中；added by dengzc 2010年5月18日10:56:21
		ToaPersonalInfo myInfo = myInfoManager.getInfoByUserid(model
				.getUserId());
		if (myInfo == null) {// 个人信息部存在
			myInfo = new ToaPersonalInfo();
			myInfo.setUserId(model.getUserId());
		}
		myInfo.setPrsnName(model.getUserName());// 姓名
		myInfo.setPrsnMobile1(model.getRest2());// 手机号码
		myInfo.setPrsnMail1(model.getUserEmail());// email
		myInfo.setPrsnTel1(model.getUserTel());// 电话
		myInfoManager.saveObj(myInfo);
		
		if(isAdd){
			TUumsBaseRole bankRole = this.roleManager.getRoleInfoByRoleCode(GzjzyhConstants.BANK_ROLE);
			this.roleManager.saveRoleUsers(bankRole.getRoleId(), model.getUserId());
			if(GzjzyhApplicationConfig.isDistributedDeployed()){
				this.toSyncManager.createToSyncMsg(model, GzjzyhConstants.OPERATION_TYPE_ADD);
			}
		}else{
			if(GzjzyhApplicationConfig.isDistributedDeployed()){
				this.toSyncManager.createToSyncMsg(model, GzjzyhConstants.OPERATION_TYPE_EDIT);
			}
		}*/
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

	
}
