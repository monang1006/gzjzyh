package com.strongit.oa.archive.sort;

import com.strongit.oa.bo.ToaArchiveSort;
import com.strongit.tag.web.tree.IDealTreeNode;

/**
 * @author pengxq
 * @version 1.0
 */
public class DealTreeNode extends IDealTreeNode{



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
		return "archive/sort/archiveSort!input.action?sortId="+getTagNodeId();
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
