package com.strongit.gzjzyh.tohandle;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.strongit.gzjzyh.GzjzyhApplicationConfig;
import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
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
		
		
		
	}
	
	private void editBankAccount(Packet requestPacket) throws SystemException{
		
	}
	
	private void deleteBankAccount(Packet requestPacket) throws SystemException{
		
	}
	
	private void editBankAccountPersonal(Packet requestPacket) throws SystemException{
		
	}
	
	private void addApplication(Packet requestPacket) throws SystemException{
		
	}
	
	private void refuseApplication(Packet requestPacket) throws SystemException{
		
	}
	
	private void returnApplication(Packet requestPacket) throws SystemException{
		
	}

}
