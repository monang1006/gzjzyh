package com.strongit.oa.archive.tempfile;

import com.strongit.tag.web.tree.IDealTreeNode;

public class RecDealDepTreeType extends IDealTreeNode {

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeImg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagNodeId() {
		// TODO Auto-generated method stub
		return ((TempfileTree)obj).getTreeid();
	}

	@Override
	public String getTagNodeName() {
		// TODO Auto-generated method stub
		return ((TempfileTree)obj).getTreeName();
	}

	@Override
	public String getTagParentId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrl() {
			return "archive/tempfile/tempFile.action?depLogo=depLogo&treeValue="+getTagNodeId()+"&treeType="+((TempfileTree)obj).getTreetype();
	}

}
