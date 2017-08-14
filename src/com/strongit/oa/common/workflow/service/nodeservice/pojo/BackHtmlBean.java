package com.strongit.oa.common.workflow.service.nodeservice.pojo;

import java.util.List;
import java.util.Map;

public class BackHtmlBean {
	public List<String> list;

	public Map<String, BackBeanList> map;

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public Map<String, BackBeanList> getMap() {
		return map;
	}

	public void setMap(Map<String, BackBeanList> map) {
		this.map = map;
	}

	public BackHtmlBean(List<String> list, Map<String, BackBeanList> map) {
		this.list = list;
		this.map = map;
	}

	public BackHtmlBean() {
	}

}
