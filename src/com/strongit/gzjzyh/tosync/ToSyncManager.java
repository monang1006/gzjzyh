package com.strongit.gzjzyh.tosync;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.strongit.gzjzyh.GzjzyhApplicationConfig;
import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.po.TGzjzyhCase;
import com.strongit.gzjzyh.po.TGzjzyhToSync;
import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
import com.strongit.gzjzyh.util.FileKit;
import com.strongit.gzjzyh.util.HttpConnector;
import com.strongit.gzjzyh.vo.Packet;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class ToSyncManager implements IToSyncManager {

	private HttpConnector connector = null;
	private GenericDAOHibernate<Object, java.lang.String> baseDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		baseDao = new GenericDAOHibernate<Object, java.lang.String>(
				sessionFactory, Object.class);
	}

	@Override
	@Transactional(readOnly = false)
	public void createToSyncMsg(Object obj, String operation)
			throws SystemException {
		if (!GzjzyhApplicationConfig.isDistributedDeployed()) {
			return;
		}
		if (GzjzyhConstants.OPERATION_TYPE_ADD_BANKACCOUNT.equals(operation)) {
			this.createBankAccountSyncMsg((TUumsBaseUser) obj, operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_EDIT_BANKACCOUNT
				.equals(operation)) {
			this.createBankAccountSyncMsg((TUumsBaseUser) obj, operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_DELETE_BANKACCOUNT
				.equals(operation)) {
			this.createBankAccountSyncMsg((TUumsBaseUser) obj, operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_EDIT_BANKACCOUNT_PERSONAL
				.equals(operation)) {
			this.createBankAccountPersonalSyncMsg((ToaPersonalInfo) obj,
					operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_ADD_APP.equals(operation)) {
			this.createApplicationSyncMsg((TGzjzyhApplication) obj, operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_REFUSED_APP.equals(operation)) {
			this.createApplicationSyncMsg((TGzjzyhApplication) obj, operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_RETURN_APP.equals(operation)) {
			this.createApplicationSyncMsg((TGzjzyhApplication) obj, operation);
		}
	}

	private void createBankAccountSyncMsg(TUumsBaseUser user, String operation)
			throws SystemException {
		TGzjzyhToSync toSyncVo = new TGzjzyhToSync();

		JSONObject json = new JSONObject();
		if (GzjzyhConstants.OPERATION_TYPE_ADD_BANKACCOUNT.equals(operation)
				|| GzjzyhConstants.OPERATION_TYPE_EDIT_BANKACCOUNT
						.equals(operation)) {
			json.put("userId", user.getUserId());
			json.put("userName", user.getUserName());
			json.put("userSyscode", user.getUserSyscode());
			json.put("userLoginname", user.getUserLoginname());
			json.put("userPassword", user.getUserPassword());
			json.put("userAddr", user.getUserAddr());
			json.put("userSequence", user.getUserSequence());
			json.put("userTel", user.getUserTel());
			json.put("rest2", user.getRest2());
		} else if (GzjzyhConstants.OPERATION_TYPE_DELETE_BANKACCOUNT
				.equals(operation)) {
			json.put("userId", user.getUserId());
		}

		Packet packet = new Packet();
		packet.setOperationType(operation);
		packet.setOperationObj(json);

		toSyncVo.setTsToSyncMsg(JSONObject.toJSONString(packet));
		toSyncVo.setTsToSyncTime(new Date());

		this.baseDao.insert(toSyncVo);
	}

	private void createBankAccountPersonalSyncMsg(ToaPersonalInfo user,
			String operation) throws SystemException {
		TGzjzyhToSync toSyncVo = new TGzjzyhToSync();

		JSONObject json = new JSONObject();
		json.put("prsnId", user.getPrsnId());
		json.put("prsnName", user.getPrsnName());
		json.put("prsnNickname", user.getPrsnNickname());
		json.put("prsnTel1", user.getPrsnTel1());
		json.put("homeAddress", user.getHomeAddress());
		json.put("prsnMobile1", user.getPrsnMobile1());

		Packet packet = new Packet();
		packet.setOperationType(operation);
		packet.setOperationObj(json);

		toSyncVo.setTsToSyncMsg(JSONObject.toJSONString(packet));
		toSyncVo.setTsToSyncTime(new Date());

		this.baseDao.insert(toSyncVo);
	}

	private void createApplicationSyncMsg(TGzjzyhApplication application,
			String operation) throws SystemException {
		TGzjzyhToSync toSyncVo = new TGzjzyhToSync();
		JSONObject json = new JSONObject();
		Object operationObj = json;
		Map<String, String> attachments = null;

		if (GzjzyhConstants.OPERATION_TYPE_ADD_APP.equals(operation)) {
			try {
				TGzjzyhApplication applicationCopy = new TGzjzyhApplication();
				BeanUtils.copyProperties(applicationCopy, application);
				String hql = "from TGzjzyhUserExtension t where t.tuumsBaseUser.userId = ?";
				TGzjzyhUserExtension ue = (TGzjzyhUserExtension) this.baseDao
						.findUnique(hql,
								new Object[] { applicationCopy.getAppUserid() });
				applicationCopy.setAppMainId(ue.getUeMainId());
				applicationCopy.setAppMainMobile(ue.getUeMainMobile());
				applicationCopy.setAppMainName(ue.getUeMainName());
				applicationCopy.setAppMainNo(ue.getUeMainNo());
				applicationCopy.setAppMainId1(ue.getUeMainId1());
				applicationCopy.setAppMainId2(ue.getUeMainId2());
				applicationCopy.setAppMainNo1(ue.getUeMainNo1());
				applicationCopy.setAppMainNo2(ue.getUeMainNo2());
				
				applicationCopy.setAppHelpId(ue.getUeHelpId());
				applicationCopy.setAppHelpMobile(ue.getUeHelpMobile());
				applicationCopy.setAppHelpName(ue.getUeHelpName());
				applicationCopy.setAppHelpNo(ue.getUeHelpNo());
				applicationCopy.setAppHelpId1(ue.getUeHelpId1());
				applicationCopy.setAppHelpId2(ue.getUeHelpId2());
				applicationCopy.setAppHelpNo1(ue.getUeHelpNo1());
				applicationCopy.setAppHelpNo2(ue.getUeHelpNo2());
				
				TGzjzyhCase gzjzyhCase = (TGzjzyhCase)this.baseDao.getSession().load(TGzjzyhCase.class, applicationCopy.getCaseId());
				applicationCopy.setAppCaseCode(gzjzyhCase.getCaseCode());
				applicationCopy.setAppCaseConfirmTime(gzjzyhCase.getCaseConfirmTime());
				applicationCopy.setAppCaseAddress(gzjzyhCase.getCaseAddress());
				applicationCopy.setAppCaseBeginTime(gzjzyhCase.getCaseBeginTime());
				applicationCopy.setAppCaseName(gzjzyhCase.getCaseName());
				applicationCopy.setAppCaseOrg(gzjzyhCase.getCaseOrg());
				
				operationObj = applicationCopy;
				
				// 加载附件
				if (applicationCopy.getAppLawfile() != null
						&& !"".equals(applicationCopy.getAppLawfile())) {
					attachments = new HashMap<String, String>(0);
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(applicationCopy
									.getAppLawfile());
					attachments.put("appLawfile", encodedFileContent);
				}
				if (applicationCopy.getAppLawfileR() != null
						&& !"".equals(applicationCopy.getAppLawfileR())) {
					attachments = new HashMap<String, String>(0);
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(applicationCopy
									.getAppLawfileR());
					attachments.put("appLawfileR", encodedFileContent);
				}
				if (applicationCopy.getAppAttachment() != null
						&& !"".equals(applicationCopy.getAppAttachment())) {
					attachments = new HashMap<String, String>(0);
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(applicationCopy
									.getAppAttachment());
					attachments.put("appAttachment", encodedFileContent);
				}
				if (applicationCopy.getAppMainId1() != null
						&& !"".equals(applicationCopy.getAppMainId1())) {
					attachments = new HashMap<String, String>(0);
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(applicationCopy
									.getAppMainId1());
					attachments.put("appMainId1", encodedFileContent);
				}
				if (applicationCopy.getAppMainId2() != null
						&& !"".equals(applicationCopy.getAppMainId2())) {
					attachments = new HashMap<String, String>(0);
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(applicationCopy
									.getAppMainId2());
					attachments.put("appMainId2", encodedFileContent);
				}
				if (applicationCopy.getAppMainNo1() != null
						&& !"".equals(applicationCopy.getAppMainNo1())) {
					attachments = new HashMap<String, String>(0);
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(applicationCopy
									.getAppMainNo1());
					attachments.put("appMainNo1", encodedFileContent);
				}
				if (applicationCopy.getAppMainNo2() != null
						&& !"".equals(applicationCopy.getAppMainNo2())) {
					attachments = new HashMap<String, String>(0);
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(applicationCopy
									.getAppMainNo2());
					attachments.put("appMainNo2", encodedFileContent);
				}
				if (applicationCopy.getAppHelpId1() != null
						&& !"".equals(applicationCopy.getAppHelpId1())) {
					attachments = new HashMap<String, String>(0);
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(applicationCopy
									.getAppHelpId1());
					attachments.put("appHelpId1", encodedFileContent);
				}
				if (applicationCopy.getAppHelpId2() != null
						&& !"".equals(applicationCopy.getAppHelpId2())) {
					attachments = new HashMap<String, String>(0);
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(applicationCopy
									.getAppHelpId2());
					attachments.put("appHelpId2", encodedFileContent);
				}
				if (applicationCopy.getAppHelpNo1() != null
						&& !"".equals(applicationCopy.getAppHelpNo1())) {
					attachments = new HashMap<String, String>(0);
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(applicationCopy
									.getAppHelpNo1());
					attachments.put("appHelpNo1", encodedFileContent);
				}
				if (applicationCopy.getAppHelpNo2() != null
						&& !"".equals(applicationCopy.getAppHelpNo2())) {
					attachments = new HashMap<String, String>(0);
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(applicationCopy
									.getAppHelpNo2());
					attachments.put("appHelpNo2", encodedFileContent);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SystemException(e);
			}
		} else if (GzjzyhConstants.OPERATION_TYPE_REFUSED_APP.equals(operation)) {
			json.put("appId", application.getAppId());
			json.put("appReceiverId", application.getAppReceiverId());
			json.put("appReceiver", application.getAppReceiver());
			json.put("appReceiveDate", application.getAppReceiveDate());
			json.put("appNgReason", application.getAppNgReason());
		} else if (GzjzyhConstants.OPERATION_TYPE_RETURN_APP.equals(operation)) {
			json.put("appId", application.getAppId());
			json.put("appResponseDate", application.getAppResponseDate());
			json.put("appResponserId", application.getAppResponserId());
			json.put("appResponser", application.getAppResponser());
			json.put("appResponsefile", application.getAppResponsefile());
			// 加载附件
			if (application.getAppResponsefile() != null
					&& !"".equals(application.getAppResponsefile())) {
				attachments = new HashMap<String, String>(0);
				try {
					String encodedFileContent = FileKit
							.getBase64EncodedFileContentByRelativePath(application
									.getAppResponsefile());
					attachments.put("appResponsefile", encodedFileContent);
				} catch (Exception e) {
					e.printStackTrace();
					throw new SystemException(e);
				}
			}
		}

		Packet packet = new Packet();
		packet.setOperationType(operation);
		packet.setOperationObj(operationObj);
		packet.setAttachments(attachments);

		toSyncVo.setTsToSyncMsg(JSONObject.toJSONString(packet));
		toSyncVo.setTsToSyncTime(new Date());

		this.baseDao.insert(toSyncVo);
	}

	@Transactional(readOnly = false)
	public int syncMsg() throws SystemException {
		int counter = 0;
		Page page = new Page(1, false);
		String hql = "from TGzjzyhToSync t where t.tsToSyncFlag = ? order by t.tsToSyncTime";
		page = this.baseDao.find(page, hql, new Object[] {GzjzyhApplicationConfig.getHandleflag()});
		List<TGzjzyhToSync> result = page.getResult();
		if (result != null && !result.isEmpty()) {
			counter = 1;
			TGzjzyhToSync syncMsg = result.get(0);
			this.getHttpConnector().perform(syncMsg.getTsToSyncMsg());
			this.baseDao.delete(syncMsg);
		}
		return counter;
	}

	private HttpConnector getHttpConnector() throws SystemException {
		if (this.connector == null) {
			this.connector = new HttpConnector();
			this.connector.open(GzjzyhApplicationConfig.getSyncUrl());
		}
		return this.connector;
	}

}
