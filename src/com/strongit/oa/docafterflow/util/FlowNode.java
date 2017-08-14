package com.strongit.oa.docafterflow.util;

import com.strongit.tag.web.tree.IDealTreeNode;

public class FlowNode extends IDealTreeNode {

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return "javascript:getFlow(\""+this.getTagNodeName()+"\")";
	}

	@Override
	public String getNodeImg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagNodeId() {
		// TODO Auto-generated method stub
		return ((Node)obj).getId();
	}

	@Override
	public String getTagNodeName() {
		// TODO Auto-generated method stub
		return ((Node)obj).getName();
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
