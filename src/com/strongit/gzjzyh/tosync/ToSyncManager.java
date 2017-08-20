package com.strongit.gzjzyh.tosync;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.po.TGzjzyhToSync;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional(readOnly = true)
public class ToSyncManager implements IToSyncManager {

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
		if (obj instanceof TUumsBaseUser) {
			this.createBankAccountSyncMsg((TUumsBaseUser)obj, operation);
		} else if (obj instanceof TGzjzyhApplication) {
			this.createApplicationSyncMsg((TGzjzyhApplication)obj, operation);
		}
	}

	private void createBankAccountSyncMsg(TUumsBaseUser user, String operation)
			throws SystemException {
		TGzjzyhToSync toSyncVo = new TGzjzyhToSync();
		JSONObject json = new JSONObject();
		json.put("userId", user.getUserId());
		json.put("userName", user.getUserName());
		json.put("userSyscode", user.getUserSyscode());
		json.put("userLoginname", user.getUserLoginname());
		json.put("userPassword", user.getUserPassword());
		json.put("userAddr", user.getUserAddr());
		json.put("userSequence", user.getUserSequence());
		json.put("userTel", user.getUserTel());
		json.put("rest2", user.getRest2());
		json.put("operation", operation);
		
		toSyncVo.setTsToSyncMsg(json.toJSONString());
		toSyncVo.setTsToSyncTime(new Date());
		
		this.baseDao.insert(toSyncVo);
	}

	private void createApplicationSyncMsg(TGzjzyhApplication application,
			String operation) throws SystemException {
		
	}

}
