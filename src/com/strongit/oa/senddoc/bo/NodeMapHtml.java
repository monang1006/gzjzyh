package com.strongit.oa.senddoc.bo;

import java.util.List;
import java.util.Map;

import com.strongit.oa.common.workflow.service.nodeservice.pojo.BackBean;

public class NodeMapHtml {
	private Map<String, List<BackBean>> map;

	private String preNodes;

	private String backType;

	public String getBackType() {
		return backType;
	}

	protected void setBackType(String backType) {
		this.backType = backType;
	}

	public Map<String, List<BackBean>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<BackBean>> map) {
		this.map = map;
	}

	public String getPreNodes() {
		return preNodes;
	}

	public void setPreNodes(String preNodes) {
		this.preNodes = preNodes;
	}

	public NodeMapHtml(Map<String, List<BackBean>> map, String preNodes) {
		this.map = map;
		this.preNodes = preNodes;
	}

	public NodeMapHtml(Map<String, List<BackBean>> map, String preNodes,
			String backType) {
		this.map = map;
		this.preNodes = preNodes;
		this.backType = backType;
	}

	public NodeMapHtml() {
	}

}
