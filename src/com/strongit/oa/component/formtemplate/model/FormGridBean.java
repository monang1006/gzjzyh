package com.strongit.oa.component.formtemplate.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FormGridBean {

	private List<Map<String, String>> rsList = null;

	private Map<String, String> rsAttrib = null;

	public Map<String, String> getRsAttrib() {
		return rsAttrib;
	}

	public void setRsAttrib(Map<String, String> rsAttrib) {
		this.rsAttrib = rsAttrib;
	}

	public List<Map<String, String>> getRsList() {
		if(rsList != null && !rsList.isEmpty()) {
			SimpleDateFormat sdf = null;
			for(Map<String,String> map : rsList) {
				Set<String> set = map.keySet();
				for(String str : set) {
					Object obj = map.get(str);
					if(obj instanceof Date) {
						if(sdf == null) {
							sdf = new SimpleDateFormat("yyyy-MM-dd");
						}
						String strValue = sdf.format((Date)obj);
						map.put(str, strValue);
					}
				}
			}
		}
		return rsList;
	}

	public void setRsList(List<Map<String, String>> rsList) {
		this.rsList = rsList;
	}
}
