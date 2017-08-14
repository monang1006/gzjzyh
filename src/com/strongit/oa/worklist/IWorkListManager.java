package com.strongit.oa.worklist;

import java.util.Map;

import com.strongmvc.orm.hibernate.Page;

public interface IWorkListManager {
	public String getUserIDByUserName(String username);
	
	public Map<String,String> getProccessTypeNameAndCount(Page page);
}
