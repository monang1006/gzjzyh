package com.strongit.oa.archive.filesearch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.strongit.tag.web.tree.IDealTreeNode;

public class RecDealTreeTypeTree extends IDealTreeNode{

	@Override
	public String getClick() {
		StringBuffer str=new StringBuffer("javascript:goFileList(\""+this.getTagNodeId()+"\")");
		return str.toString();
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
		return ((SearchTree)obj).getTreeid();
	}
	/**
	 * 树的名称
	 */
	@Override
	public String getTagNodeName() {
		
		return ((SearchTree)obj).getTreeName();
	
	}
	/**
	 * 树的父节点
	 */
	@Override
	public String getTagParentId() {
		return ((SearchTree)obj).getParentId();
	}
	/**
	 * 树的URL
	 */
	@Override
	public String getUrl() {
		return null;
	}

}
