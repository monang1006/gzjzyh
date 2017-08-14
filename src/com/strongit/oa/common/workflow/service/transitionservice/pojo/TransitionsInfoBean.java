package com.strongit.oa.common.workflow.service.transitionservice.pojo;

import java.util.List;
import java.util.Set;

/**
 * 迁移线相关信息
 * 
 * @author yanjian
 * 
 * Dec 1, 2012 11:09:41 PM
 */
public class TransitionsInfoBean {

	private Set<Long> transitionIds; // 迁移线id集合

	private List TransitionsInfo; // 提交下一步迁移线信息


	public Set<Long> getTransitionIds() {
		return transitionIds;
	}

	public void setTransitionIds(Set<Long> transitionIds) {
		this.transitionIds = transitionIds;
	}

	public List getTransitionsInfo() {
		return TransitionsInfo;
	}

	public void setTransitionsInfo(List transitionsInfo) {
		TransitionsInfo = transitionsInfo;
	}
}
