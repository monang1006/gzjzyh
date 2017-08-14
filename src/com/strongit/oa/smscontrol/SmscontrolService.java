package com.strongit.oa.smscontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmscontrolService implements IsmscontrolService{

	private SmscontrolManager manager;

	@Autowired
	public void setManager(SmscontrolManager manager) {
		this.manager = manager;
	}
	
	
	public boolean hasSendRight(String userId){
		return manager.hasSendRight(userId);
	}
}
