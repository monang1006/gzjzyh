/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: qindh
 * Version: V1.0
 * Description： 栏目树型2
 */
package com.strongit.oa.infopub.articles;

import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.tag.web.tree.IDealTreeNode;

public class RecDealTreeNode extends IDealTreeNode {

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
		return "infopub/articles/columnArticles.action?columnId="+getTagNodeId();
	}

}
