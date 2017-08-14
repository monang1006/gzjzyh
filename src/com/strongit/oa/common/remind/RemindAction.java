package com.strongit.oa.common.remind;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息提醒类
 * @author 邓志城
 *
 */
@ParentPackage("default")
public class RemindAction {

	/**
	 * 提醒服务类
	 */
	private RemindManager manager;

	@Autowired
	public void setManager(RemindManager manager) {
		this.manager = manager;
	}
	
}
