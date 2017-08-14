package com.strongit.oa.archive.tempfile;

import com.strongit.tag.web.tree.IDealTreeNode;

public class RecDealTreeDepTempfile extends IDealTreeNode {

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
	/**
	 * 树的ID
	 */
	@Override
	public String getTagNodeId() {
		return ((TempfileTree)obj).getTreeid();
	}
	/**
	 * 树的名称
	 */
	@Override
	public String getTagNodeName() {
		
		return ((TempfileTree)obj).getTreeid()+((TempfileTree)obj).getTreeName();
	
	}
	/**
	 * 树的父节点
	 */
	@Override
	public String getTagParentId() {
		return ((TempfileTree)obj).getParentId();
	}
	/**
	 * 树的URL
	 */
	@Override
	public String getUrl() {
		if(getTagParentId()!=null&&!"".equals(getTagParentId())){
		return "archive/tempfile/tempFile.action?treeValue="+getTagParentId()+","+getTagNodeId()+"&treeType=4&depLogo=depLogo";
		}else{
			return "archive/tempfile/tempFile.action?treeValue="+getTagNodeId()+"&treeType=4&depLogo=depLogo";
		}
	}

}
