package com.strongit.gzjzyh;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.sms.SmsManager;
import com.strongmvc.exception.SystemException;

@Service
@Transactional(readOnly = true)
public class GzjzyhCommonService {
	
	@Autowired
	SmsManager smsManager;
	
	@Transactional(readOnly = false)
	public void sendSms(String userId, String content) throws SystemException{
		List receiver = new ArrayList();
		receiver.add(userId);
		this.smsManager.sendSmsByImpl("system", receiver, content, "");
	}

}
