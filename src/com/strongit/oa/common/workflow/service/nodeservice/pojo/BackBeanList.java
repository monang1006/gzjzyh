package com.strongit.oa.common.workflow.service.nodeservice.pojo;

import java.util.LinkedList;
import java.util.List;

public class BackBeanList {
	private List<BackBean> list;

	private List<String> userIdtaskNodeIdList = new LinkedList<String>();

	public void add(BackBean bo) {
		if (bo == null) {
			throw new NullPointerException("BackBean 不能为空 ");
		}
		if (bo.getAiActorId() == null || "".equals(bo.getAiActorId())) {
			throw new NullPointerException("BackBean.aiActorId 不能为空 ");
		}
		if (bo.getTaskNodeId() == null || "".equals(bo.getTaskNodeId())) {
			throw new NullPointerException("BackBean.taskNodeId 不能为空 ");
		}
		if (list == null) {
			list = new LinkedList<BackBean>();
		}
		if (!userIdtaskNodeIdList.contains(bo.getTaskNodeId() + "$"
				+ bo.getAiActorId())) {// 去除同一节点同一个人处理多次的数据
			userIdtaskNodeIdList.add(bo.getTaskNodeId() + "$"
					+ bo.getAiActorId());
			list.add(bo);
		}
	}

	public List<BackBean> getList() {
		return list;
	}

}
