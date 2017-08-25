package com.strongit.gzjzyh.tohandle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.strongit.gzjzyh.GzjzyhApplicationConfig;
import com.strongit.gzjzyh.GzjzyhCommonService;
import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.bankaccount.IBankAccountManager;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.util.FileKit;
import com.strongit.gzjzyh.util.TimeKit;
import com.strongit.gzjzyh.vo.Packet;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional(readOnly = true)
public class ToHandleManager implements IToHandleManager {

	private GenericDAOHibernate<Object, java.lang.String> baseDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		baseDao = new GenericDAOHibernate<Object, java.lang.String>(
				sessionFactory, Object.class);
	}
	
	@Autowired
	IBankAccountManager bankAccountManager;
	@Autowired
	GzjzyhCommonService commonSerivce;
	@Autowired
	JdbcTemplate jdbcTemplate; // 提供Jdbc操作支持

	@Override
	@Transactional(readOnly = false)
	public void handleMsg(Packet requestPacket) throws SystemException {
		if (GzjzyhConstants.OPERATION_TYPE_ADD_BANKACCOUNT.equals(requestPacket
				.getOperationType())) {
			this.addBankAccount(requestPacket);
		} else if (GzjzyhConstants.OPERATION_TYPE_EDIT_BANKACCOUNT
				.equals(requestPacket.getOperationType())) {
			this.editBankAccount(requestPacket);
		} else if (GzjzyhConstants.OPERATION_TYPE_DELETE_BANKACCOUNT
				.equals(requestPacket.getOperationType())) {
			this.deleteBankAccount(requestPacket);
		} else if (GzjzyhConstants.OPERATION_TYPE_EDIT_BANKACCOUNT_PERSONAL
				.equals(requestPacket.getOperationType())) {
			this.editBankAccountPersonal(requestPacket);
		} else if (GzjzyhConstants.OPERATION_TYPE_ADD_APP.equals(requestPacket
				.getOperationType())) {
			this.addApplication(requestPacket);
		} else if (GzjzyhConstants.OPERATION_TYPE_REFUSED_APP
				.equals(requestPacket.getOperationType())) {
			this.refuseApplication(requestPacket);
		} else if (GzjzyhConstants.OPERATION_TYPE_RETURN_APP
				.equals(requestPacket.getOperationType())) {
			this.returnApplication(requestPacket);
		}
	}
	
	private void addBankAccount(Packet requestPacket) throws SystemException{
		JSONObject jsonObj = (JSONObject)requestPacket.getOperationObj();
		TUumsBaseUser user = new TUumsBaseUser();
		user.setUserId(jsonObj.getString("userId"));
		user.setUserName(jsonObj.getString("userName"));
		user.setUserSyscode(jsonObj.getString("userSyscode"));
		user.setUserLoginname(jsonObj.getString("userLoginname"));
		user.setUserPassword(jsonObj.getString("userPassword"));
		user.setUserAddr(jsonObj.getString("userAddr"));
		user.setUserSequence(jsonObj.getLong("userSequence"));
		user.setUserTel(jsonObj.getString("userTel"));
		user.setRest2(jsonObj.getString("rest2"));
		
		this.bankAccountManager.insertWithoutSync(user);
	}
	
	private void editBankAccount(Packet requestPacket) throws SystemException{
		JSONObject jsonObj = (JSONObject)requestPacket.getOperationObj();
		TUumsBaseUser user = new TUumsBaseUser();
		user.setUserId(jsonObj.getString("userId"));
		user.setUserName(jsonObj.getString("userName"));
		user.setUserSyscode(jsonObj.getString("userSyscode"));
		user.setUserLoginname(jsonObj.getString("userLoginname"));
		user.setUserPassword(jsonObj.getString("userPassword"));
		user.setUserAddr(jsonObj.getString("userAddr"));
		user.setUserSequence(jsonObj.getLong("userSequence"));
		user.setUserTel(jsonObj.getString("userTel"));
		user.setRest2(jsonObj.getString("rest2"));
		
		this.bankAccountManager.updateWithoutSync(user);
	}
	
	private void deleteBankAccount(Packet requestPacket) throws SystemException{
		JSONObject jsonObj = (JSONObject)requestPacket.getOperationObj();
		String userId = jsonObj.getString("userId");
		this.bankAccountManager.deleteWithoutSync(userId);
	}
	
	private void editBankAccountPersonal(Packet requestPacket) throws SystemException{
		JSONObject jsonObj = (JSONObject)requestPacket.getOperationObj();
		
		ToaPersonalInfo personInfo = (ToaPersonalInfo)this.baseDao.getSession().load(ToaPersonalInfo.class, jsonObj.getString("prsnId"));
		if(personInfo != null) {
			personInfo.setPrsnName(jsonObj.getString("prsnName"));
			personInfo.setPrsnNickname(jsonObj.getString("prsnNickname"));
			personInfo.setPrsnTel1(jsonObj.getString("prsnTel1"));
			personInfo.setHomeAddress(jsonObj.getString("homeAddress"));
			personInfo.setPrsnMobile1(jsonObj.getString("prsnMobile1"));
			
			this.baseDao.save(personInfo);
		}
		
		TUumsBaseUser user = (TUumsBaseUser)this.baseDao.getSession().load(TUumsBaseUser.class, personInfo.getUserId());
		if(user != null) {
			user.setUserTel(jsonObj.getString("prsnTel1"));
			user.setRest2(jsonObj.getString("prsnMobile1"));
			
			this.baseDao.save(user);
		}
	}
	
	private void addApplication(Packet requestPacket) throws SystemException{
		JSONObject jsonObj = (JSONObject)requestPacket.getOperationObj();
		
		StringBuilder columnSb = new StringBuilder("");
		StringBuilder valueSb = new StringBuilder("");
		StringBuilder sql = new StringBuilder("");
		List values = new ArrayList(0);
		
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_ID", jsonObj.getString("appId"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_USERID", jsonObj.getString("appUserid"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "CASE_ID", jsonObj.getString("caseId"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_BANKUSER", jsonObj.getString("appBankuser"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_TYPE", jsonObj.getString("appType"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_FILENO", jsonObj.getString("appFileno"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_DATE", jsonObj.getDate("appDate"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_LAWFILE", jsonObj.getString("appLawfile"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_LAWFILE_R", jsonObj.getString("appLawfileR"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_ATTACHMENT", jsonObj.getString("appAttachment"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_ORG_ACCOUNT", jsonObj.getString("appOrgAccount"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_PERSON_ACCOUNT", jsonObj.getString("appPersonAccount"));
		
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_ORG_DETAIL", jsonObj.getString("appOrgDetail"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_PERSON_DETAIL", jsonObj.getString("appPersonDetail"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_CHADE_DETAIL", jsonObj.getString("appChadeDetail"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_DATE_TYPE", jsonObj.getString("appDateType"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_START_DATE", jsonObj.getDate("appStartDate"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_END_DATE", jsonObj.getDate("appEndDate"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_STATUS", jsonObj.getString("appStatus"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_AUDIT_USERID", jsonObj.getString("appAuditUserId"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_AUDIT_USER", jsonObj.getString("appAuditUser"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_AUDIT_DATE", jsonObj.getDate("appAuditDate"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_NG_REASON", jsonObj.getString("appNgReason"));
		
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_MAIN_NAME", jsonObj.getString("appMainName"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_MAIN_ID", jsonObj.getString("appMainId"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_MAIN_NO", jsonObj.getString("appMainNo"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_MAIN_MOBILE", jsonObj.getString("appMainMobile"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_MAIN_NO1", jsonObj.getString("appMainNo1"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_MAIN_NO2", jsonObj.getString("appMainNo2"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_MAIN_ID1", jsonObj.getString("appMainId1"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_MAIN_ID2", jsonObj.getString("appMainId2"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_HELP_NAME", jsonObj.getString("appHelpName"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_HELP_ID", jsonObj.getString("appHelpId"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_HELP_NO", jsonObj.getString("appHelpNo"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_HELP_MOBILE", jsonObj.getString("appHelpMobile"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_HELP_NO1", jsonObj.getString("appHelpNo1"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_HELP_NO2", jsonObj.getString("appHelpNo2"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_HELP_ID1", jsonObj.getString("appHelpId1"));
		this.buildSqlColumnAndValue(columnSb, valueSb, values, "APP_HELP_ID2", jsonObj.getString("appHelpId2"));
		
		sql.append("insert into T_GZJZYH_APPLICATION (")
			.append(columnSb.toString().substring(1))
			.append(") values (")
			.append(valueSb.toString().substring(1))
			.append(")");
		this.jdbcTemplate.update(sql.toString(), values.toArray());
		
		//处理附件
		Map<String, String> attachmentMap = requestPacket.getAttachments();
		if(attachmentMap != null && !attachmentMap.isEmpty()) {
			try {
				for(Map.Entry<String, String> attachment : attachmentMap.entrySet()) {
					if(attachment.getValue() != null && !"".equals(attachment.getValue())) {
						FileKit.saveFileByRelativePathAndEncodedContent(jsonObj.getString(attachment.getKey()), attachment.getValue());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SystemException(e);
			}
		}
		
		//发送短信
		this.commonSerivce.sendSms(jsonObj.getString("appBankuser"), "您有一条查询申请待签收，请登录系统进行处理。");
	}
	
	private void refuseApplication(Packet requestPacket) throws SystemException{
		JSONObject jsonObj = (JSONObject)requestPacket.getOperationObj();
		
		TGzjzyhApplication application = (TGzjzyhApplication)this.baseDao.getSession().load(TGzjzyhApplication.class, jsonObj.getString("appId"));
		if(application != null) {
			application.setAppStatus(GzjzyhConstants.APP_STATUS_REFUSE);
			application.setAppReceiverId(jsonObj.getString("appReceiverId"));
			application.setAppReceiver(jsonObj.getString("appReceiver"));
			application.setAppReceivedate(jsonObj.getDate("appReceivedate"));
			application.setAppNgReason(jsonObj.getString("appNgReason"));
			
			this.baseDao.save(application);
			//发送短信
			this.commonSerivce.sendSms(application.getAppUserid(), "您的查询申请已被" + application.getAppReceiver() + "拒签，请登录系统查看。");
		}
	}
	
	private void returnApplication(Packet requestPacket) throws SystemException{
		JSONObject jsonObj = (JSONObject)requestPacket.getOperationObj();
		
		TGzjzyhApplication application = (TGzjzyhApplication)this.baseDao.getSession().load(TGzjzyhApplication.class, jsonObj.getString("appId"));
		if(application != null) {
			application.setAppStatus(GzjzyhConstants.APP_STATUS_RETURN);
			application.setAppResponseDate(jsonObj.getDate("appResponseDate"));
			application.setAppResponserId(jsonObj.getString("appResponserId"));
			application.setAppResponser(jsonObj.getString("appResponser"));
			application.setAppResponsefile(jsonObj.getString("appResponsefile"));
			
			this.baseDao.save(application);
			//处理附件
			Map<String, String> attachmentMap = requestPacket.getAttachments();
			if(attachmentMap != null && attachmentMap.get("appResponsefile") != null && !"".equals(attachmentMap.get("appResponsefile"))) {
				try {
					FileKit.saveFileByRelativePathAndEncodedContent(application.getAppAttachment(), attachmentMap.get("appResponsefile"));
				} catch (Exception e) {
					e.printStackTrace();
					throw new SystemException(e);
				}
			}
			
			//发送短信
			this.commonSerivce.sendSms(application.getAppUserid(), "您的查询申请已由" + application.getAppReceiver() + "处理并返回，请登录系统查看。");
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
