package com.strongit.oa.senddoc.bo;

import java.util.List;
import java.util.Map;

import com.strongit.oa.common.workflow.service.nodeservice.pojo.BackBean;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;

public class PreNodeMapHtml extends NodeMapHtml {
	public PreNodeMapHtml(Map<String, List<BackBean>> map, String preNodes) {
		super(map, preNodes);
		setBackType(CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_BACKSPACE);
		// TODO Auto-generated constructor stub
	}

}
