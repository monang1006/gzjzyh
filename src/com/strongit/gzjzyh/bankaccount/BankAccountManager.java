package com.strongit.gzjzyh.bankaccount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.gzjzyh.GzjzyhApplicationConfig;
import com.strongit.gzjzyh.GzjzyhConstants;
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
public class BankAccountManager implements IBankAccountManager {
	
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
	JdbcTemplate jdbcTemplate; // 提供Jdbc操作支持
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		baseDao = new GenericDAOHibernate<Object, java.lang.String>(sessionFactory, Object.class);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(TUumsBaseUser model) throws SystemException {
		
		boolean isAdd = (model.getUserId() == null || "".equals(model.getUserId()));
		
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
		myInfo.setHomeAddress(model.getUserAddr());//住址
		myInfoManager.saveObj(myInfo);
		
		if(isAdd){
			TUumsBaseRole bankRole = this.roleManager.getRoleInfoByRoleCode(GzjzyhConstants.BANK_ROLE);
			this.userService.saveUserRoles(model.getUserId(), bankRole.getRoleId());
			this.toSyncManager.createToSyncMsg(model, GzjzyhConstants.OPERATION_TYPE_ADD_BANKACCOUNT);
		}else{
			this.toSyncManager.createToSyncMsg(model, GzjzyhConstants.OPERATION_TYPE_EDIT_BANKACCOUNT);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void insertWithoutSync(TUumsBaseUser model) throws SystemException {
		//用户信息
		TUumsBaseOrg org = userService
				.getOrgInfoBySyscode(GzjzyhConstants.DEFAULT_BANKORG_SYSCODE);
		
		StringBuilder columnSb = new StringBuilder("");
		StringBuilder valueSb = new StringBuilder("");
		StringBuilder sql = new StringBuilder("");
		List values = new ArrayList(0);
		
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "USER_ID", model.getUserId());
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "ORG_ID", org.getOrgId());
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "USER_NAME", model.getUserName());
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "USER_SYSCODE", model.getUserSyscode());
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "USER_LOGINNAME", model.getUserLoginname());
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "USER_PASSWORD", model.getUserPassword());
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "USER_ADDR", model.getUserAddr());
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "USER_SEQUENCE", model.getUserSequence());
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "USER_TEL", model.getUserTel());
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "REST2", model.getRest2());
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "ORG_IDS", "," + org.getOrgId() + ",");
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "SUP_ORG_CODE", "," + org.getOrgSyscode() + ",");
		
		sql.append("insert into t_uums_base_user (")
			.append(columnSb.toString().substring(1))
			.append(") values (")
			.append(valueSb.toString().substring(1))
			.append(")");
		this.jdbcTemplate.update(sql.toString(), values.toArray());
		
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
		myInfo.setHomeAddress(model.getUserAddr());//住址
		myInfoManager.saveObj(myInfo);
		
		TUumsBaseRole bankRole = this.roleManager.getRoleInfoByRoleCode(GzjzyhConstants.BANK_ROLE);
		this.userService.saveUserRoles(model.getUserId(), bankRole.getRoleId());
	}
	
	@Override
	@Transactional(readOnly = false)
	public void updateWithoutSync(TUumsBaseUser model) throws SystemException {
		String hql = "select t.userPassword from TUumsBaseUser t where t.userId = ?";
		String oldPassword = (String)this.baseDao.findUnique(hql, new Object[] {model.getUserId()});
		TUumsBaseOrg org = userService
				.getOrgInfoBySyscode(GzjzyhConstants.DEFAULT_BANKORG_SYSCODE);
		model.setUserSyscode(model.getUserLoginname());
		model.setOrgId(org.getOrgId());
		model.setOrgIds("," + org.getOrgId() + ",");
		model.setSupOrgCode("," + org.getOrgSyscode() + ",");
		model.setUserPassword(oldPassword);

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
		myInfo.setHomeAddress(model.getUserAddr());//住址
		myInfoManager.saveObj(myInfo);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(String userId) throws SystemException {
		TUumsBaseUser model;
		if ((userId != null) && (!("".equals(userId)))) {
			String[] userIds = userId.split(",");
			for (int i = 0; i < userIds.length; i++) {
				model = userService.getUumsUserInfoByUserId(userIds[i]);
				String[] orgidsss = model.getOrgIds().split(",");
				int tempInt = 0;
				String tempString = "";
				TUumsBaseOrg org = userService
						.getOrgInfoBySyscode(GzjzyhConstants.DEFAULT_BANKORG_SYSCODE);
				/*if (org != null) {
					for (int k = 0; k < orgidsss.length; k++) {
						if (orgidsss[k] != null && !"".equals(orgidsss[k])) {
							if (org.getOrgId().equals(orgidsss[k])) {
								model.setOrgIds(model.getOrgIds().replace(
										orgidsss[k], ""));
								model.setSupOrgCode(model.getSupOrgCode()
										.replaceFirst(org.getSupOrgCode(),
												""));
								tempString = tempString + "," + orgidsss[k];

							}
							tempInt++;
						}
					}
				}*/
				if (tempInt <= 1) {
					//model.setOrgIds(model.getOrgIds() + tempString);
					long time = new Date().getTime();
					model.setUserLoginname(model.getUserLoginname()
							+ "_del" + time);
					model.setUserSyscode(model.getUserSyscode() + "_del"
							+ time);
					model.setUserIsdel(Const.IS_YES);
					userService.saveUserForDel(model);
				} else {
					// userService.saveUser(model);
					if (userId.indexOf(",") == -1) {
						userId = "";
					} else {
						userId = userId.replaceAll(model.getUserId(), " ");
					}
					// 默认机构展现
					String[] orgidssss = model.getOrgIds().split(",");
					if (model.getOrgId().indexOf(model.getOrgIds()) == -1) {
						for (int k = 0; k < orgidssss.length; k++) {
							if (orgidsss[k] != null
									&& !"".equals(orgidssss[k])) {
								model.setOrgId(orgidssss[k]);
								break;
							}
						}
					}
					userService.saveUserForDel(model);
				}
				this.toSyncManager.createToSyncMsg(model, GzjzyhConstants.OPERATION_TYPE_DELETE_BANKACCOUNT);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteWithoutSync(String userId) throws SystemException {
		TUumsBaseUser model;
		if ((userId != null) && (!("".equals(userId)))) {
			String[] userIds = userId.split(",");
			for (int i = 0; i < userIds.length; i++) {
				model = userService.getUumsUserInfoByUserId(userIds[i]);
				String[] orgidsss = model.getOrgIds().split(",");
				int tempInt = 0;
				String tempString = "";
				TUumsBaseOrg org = userService
						.getOrgInfoBySyscode(GzjzyhConstants.DEFAULT_BANKORG_SYSCODE);
				if (org != null) {
					for (int k = 0; k < orgidsss.length; k++) {
						if (orgidsss[k] != null && !"".equals(orgidsss[k])) {
							if (org.getOrgId().equals(orgidsss[k])) {
								model.setOrgIds(model.getOrgIds().replace(
										orgidsss[k], ""));
								model.setSupOrgCode(model.getSupOrgCode()
										.replaceFirst(org.getSupOrgCode(),
												""));
								tempString = tempString + "," + orgidsss[k];

							}
							tempInt++;
						}
					}
				}
				if (tempInt <= 1) {
					model.setOrgIds(model.getOrgIds() + tempString);
					long time = new Date().getTime();
					model.setUserLoginname(model.getUserLoginname()
							+ "_del" + time);
					model.setUserSyscode(model.getUserSyscode() + "_del"
							+ time);
					model.setUserIsdel(Const.IS_YES);
					userService.saveUserForDel(model);
				} else {
					// userService.saveUser(model);
					if (userId.indexOf(",") == -1) {
						userId = "";
					} else {
						userId = userId.replaceAll(model.getUserId(), " ");
					}
					// 默认机构展现
					String[] orgidssss = model.getOrgIds().split(",");
					if (model.getOrgId().indexOf(model.getOrgIds()) == -1) {
						for (int k = 0; k < orgidssss.length; k++) {
							if (orgidsss[k] != null
									&& !"".equals(orgidssss[k])) {
								model.setOrgId(orgidssss[k]);
								break;
							}
						}
					}
					userService.saveUserForDel(model);
				}
			}
		}
	}
	
	private void buildSqlColumnAndValue(StringBuilder columns, StringBuilder values, List params, String columnName, Object value) {
		if(value == null && value instanceof String) {
			value = "";
		}
		if(value != null) {
			columns.append(",").append(columnName);
			values.append(",").append("?");
			params.add(value);
		}
	}
	
}
