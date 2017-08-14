/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description： 
*/
package com.strongit.oa.archive.tempfile;

import com.strongit.oa.bo.ToaArchiveSort;
import com.strongit.tag.web.tree.IDealTreeNode;

/**
 * @author pengxq
 * @version 1.0
 */
public class DealSortTreeNode extends IDealTreeNode{



	public String getTagNodeId() {
		// TODO 自动生成方法存根
		return ((ToaArchiveSort)obj).getSortId();
	}

	public String getTagNodeName() {
		// TODO 自动生成方法存根
		return ((ToaArchiveSort)obj).getSortName();
	}

	public String getTagParentId() {
		// TODO 自动生成方法存根
		return ((ToaArchiveSort)obj).getSortParentNo();
	}

	public String getUrl() {
		// TODO 自动生成方法存根
		return null;
	}
	public String getNodeImg() {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}

}
