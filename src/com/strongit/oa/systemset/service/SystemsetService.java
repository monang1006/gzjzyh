package com.strongit.oa.systemset.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.systemset.ISystemsetService;
import com.strongit.oa.systemset.SystemsetManager;

@Service
public class SystemsetService implements ISystemsetService {

	
	private SystemsetManager manager;
	
	/**
	 * 公文打印控制
	 */
	public String gwControl() {
		return this.manager.getSystemset().getGwcontrol();
	}
	@Autowired
	public void setManager(SystemsetManager manager) {
		this.manager = manager;
	}
}
