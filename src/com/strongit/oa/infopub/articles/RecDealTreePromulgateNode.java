package com.strongit.oa.infopub.articles;

import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.tag.web.tree.IDealTreeNode;

public class RecDealTreePromulgateNode extends IDealTreeNode {

	@Override
	public String getClick() {
		return null;
	}

	@Override
	public String getNodeImg() {
		return null;
	}
	/**
	 * 树的ID
	 */
	@Override
	public String getTagNodeId() {
		return ((ToaInfopublishColumn)obj).getClumnId();
	}
	/**
	 * 树的名称
	 */
	@Override
	public String getTagNodeName() {
		return ((ToaInfopublishColumn)obj).getClumnName();
	}
	/**
	 * 树的父节点
	 */
	@Override
	public String getTagParentId() {
		return ((ToaInfopublishColumn)obj).getClumnParent();
	}
	/**
	 * 树的URL
	 */
	@Override
	public String getUrl() {
		return "infopub/articles/columnArticles.action?columnId="+getTagNodeId()+"&promulgate=promulgate";
	}

}
