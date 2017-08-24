package com.strongit.gzjzyh.tosync;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.strongit.gzjzyh.GzjzyhApplicationConfig;
import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.po.TGzjzyhToSync;
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
		if(!GzjzyhApplicationConfig.isDistributedDeployed()) {
			return;
		}
		if (GzjzyhConstants.OPERATION_TYPE_ADD_BANKACCOUNT.equals(operation)) {
			this.createBankAccountSyncMsg((TUumsBaseUser)obj, operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_EDIT_BANKACCOUNT.equals(operation)) {
			this.createBankAccountSyncMsg((TUumsBaseUser)obj, operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_DELETE_BANKACCOUNT.equals(operation)) {
			this.createBankAccountSyncMsg((TUumsBaseUser)obj, operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_EDIT_BANKACCOUNT_PERSONAL.equals(operation)) {
			this.createBankAccountPersonalSyncMsg((ToaPersonalInfo)obj, operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_ADD_APP.equals(operation)) {
			this.createApplicationSyncMsg((TGzjzyhApplication)obj, operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_REFUSED_APP.equals(operation)) {
			this.createApplicationSyncMsg((TGzjzyhApplication)obj, operation);
		} else if (GzjzyhConstants.OPERATION_TYPE_RETURN_APP.equals(operation)) {
			this.createApplicationSyncMsg((TGzjzyhApplication)obj, operation);
		}
	}

	private void createBankAccountSyncMsg(TUumsBaseUser user, String operation)
			throws SystemException {
		TGzjzyhToSync toSyncVo = new TGzjzyhToSync();
		
		JSONObject json = new JSONObject();
		if(GzjzyhConstants.OPERATION_TYPE_ADD_BANKACCOUNT.equals(operation)
				|| GzjzyhConstants.OPERATION_TYPE_EDIT_BANKACCOUNT.equals(operation)) {
			json.put("userId", user.getUserId());
			json.put("userName", user.getUserName());
			json.put("userSyscode", user.getUserSyscode());
			json.put("userLoginname", user.getUserLoginname());
			json.put("userPassword", user.getUserPassword());
			json.put("userAddr", user.getUserAddr());
			json.put("userSequence", user.getUserSequence());
			json.put("userTel", user.getUserTel());
			json.put("rest2", user.getRest2());
		}else if(GzjzyhConstants.OPERATION_TYPE_DELETE_BANKACCOUNT.equals(operation)){
			json.put("userId", user.getUserId());
		}
		
		Packet packet = new Packet();
		packet.setOperationType(operation);
		packet.setOperationObj(json);
		
		toSyncVo.setTsToSyncMsg(JSONObject.toJSONString(packet));
		toSyncVo.setTsToSyncTime(new Date());
		
		this.baseDao.insert(toSyncVo);
	}
	
	private void createBankAccountPersonalSyncMsg(ToaPersonalInfo user, String operation)
			throws SystemException {
		TGzjzyhToSync toSyncVo = new TGzjzyhToSync();
		
		JSONObject json = new JSONObject();
		json.put("prsnId", user.getPrsnId());
		json.put("prsnName", user.getPrsnName());
		json.put("prsnNickname", user.getPrsnNickname());
		json.put("prsnTel1", user.getPrsnTel1());
		json.put("homeAddress", user.getHomeAddress());
		json.put("prsnMobile1", user.getPrsnMobile1());
		json.put("homeAddress", user.getHomeAddress());
		
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
		Map<String, String> attachments = null;
		
		if (GzjzyhConstants.OPERATION_TYPE_ADD_APP.equals(operation)) {
			
		} else if (GzjzyhConstants.OPERATION_TYPE_REFUSED_APP.equals(operation)) {
			json.put("appId", application.getAppId());
			json.put("appReceiverId", application.getAppReceiverId());
			json.put("appReceiver", application.getAppReceiver());
			json.put("appReceivedate", application.getAppReceivedate().getTimes());
			json.put("appNgReason", application.getAppNgReason());
		} else if (GzjzyhConstants.OPERATION_TYPE_RETURN_APP.equals(operation)) {
			json.put("appId", application.getAppId());
			json.put("appResponserId", application.getAppResponserId());
			json.put("appResponser", application.getAppResponser());
			json.put("appResponsefile", application.getAppResponsefile());
			//加载附件
			attachments = new HashMap<String, String>(0);
			
		}
		
		Packet packet = new Packet();
		packet.setOperationType(operation);
		packet.setOperationObj(json);
		packet.setAttachments(attachments);
		
		toSyncVo.setTsToSyncMsg(JSONObject.toJSONString(packet));
		toSyncVo.setTsToSyncTime(new Date());
		
		this.baseDao.insert(toSyncVo);
	}
	
	@Transactional(readOnly = false)
	public int syncMsg() throws SystemException{
		int counter = 0;
		Page page = new Page(1, false);
		String hql = "from TGzjzyhToSync t order by t.tsToSyncTime";
		page = this.baseDao.find(page, hql, new Object[] {});
		List<TGzjzyhToSync> result = page.getResult();
		if(result != null && !result.isEmpty()) {
			counter = 1;
			TGzjzyhToSync syncMsg = result.get(0);
			this.getHttpConnector().perform(syncMsg.getTsToSyncMsg());
			this.baseDao.delete(syncMsg);
		}
		return counter;
	}
	
	private HttpConnector getHttpConnector() throws SystemException {
		if(this.connector == null) {
			this.connector = new HttpConnector();
			this.connector.open(GzjzyhApplicationConfig.getSyncUrl());
		}
		return this.connector;
	}
	
}
