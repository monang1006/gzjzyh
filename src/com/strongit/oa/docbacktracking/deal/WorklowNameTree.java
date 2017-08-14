package com.strongit.oa.docbacktracking.deal;

import com.strongit.oa.docbacktracking.vo.WorkflowInfoVo;
import com.strongit.tag.web.tree.IDealTreeNode;

/**
 * 显示公文类型树
 * 
 * @author yanjian
 *
 * Oct 10, 2012 10:48:30 AM
 */
public class WorklowNameTree  extends IDealTreeNode {

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		WorkflowInfoVo vo=(WorkflowInfoVo)this.obj;
		return "javascript:treeNodeClick(\""+vo.getPfname()+"\",\""+vo.getMainFormId()+"\",\""+vo.getTypeId()+"\");";
//		return "javascript:treeNodeClick(\"workflowName,formId,workflowType\");";
	}

	@Override
	public String getNodeImg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagNodeId() {
		// TODO Auto-generated method stub
		return getTagNodeName();
	}

	@Override
	public String getTagNodeName() {
		// TODO Auto-generated method stub
		WorkflowInfoVo vo=(WorkflowInfoVo)this.obj;
		return  vo.getPfname();
	}

	@Override
	public String getTagParentId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

}
