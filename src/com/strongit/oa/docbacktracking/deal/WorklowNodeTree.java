package com.strongit.oa.docbacktracking.deal;

import com.strongit.oa.docbacktracking.vo.WorklowNodeVo;
import com.strongit.tag.web.tree.IDealTreeNode;

/**
 * 显示节点树
 * 
 * @author yanjian
 *
 * Oct 10, 2012 9:50:28 AM
 */
public class WorklowNodeTree  extends IDealTreeNode {

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		WorklowNodeVo vo=(WorklowNodeVo)this.obj;
		String disable = "1";
		if(!vo.isDisable()){
			disable = "0";
		}
		return "javascript:treeNodeClick(\""+vo.getNodeName()+"\",\""+vo.getTaskId()+"\",\""+vo.getProcessId()+"\",\""+disable+"\");";
	}

	@Override
	public String getNodeImg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagNodeId() {
		WorklowNodeVo vo=(WorklowNodeVo)this.obj;
		// TODO Auto-generated method stub
		return vo.getTaskId().toString();
	}

	@Override
	public String getTagNodeName() {
		// TODO Auto-generated method stub
		WorklowNodeVo vo=(WorklowNodeVo)this.obj;
		return  vo.getNodeName();
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
