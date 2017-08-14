package com.strongit.doc.sends.util;

import com.strongit.doc.receives.archive.ArchiveDocTree;
import com.strongit.tag.web.tree.IDealTreeNode;

public class RecDealTreeArchiveDocTree extends IDealTreeNode {

	@Override
	public String getClick() {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public String getNodeImg() {
		// TODO 自动生成方法存根
		return null;
	}

	/*
	 * 树的ID(non-Javadoc)
	 * @see com.strongit.tag.web.tree.IDealTreeNode#getTagNodeId()
	 */
	@Override
	public String getTagNodeId() {
		
		return ((ArchiveDocTree)obj).getTreeid();
	}

	/*
	 * 树的名(non-Javadoc)
	 * @see com.strongit.tag.web.tree.IDealTreeNode#getTagNodeName()
	 */
	@Override
	public String getTagNodeName() {
		
		return ((ArchiveDocTree)obj).getTreeid()+((ArchiveDocTree)obj).getTreeName();
	}

	/*
	 * 树的父节点(non-Javadoc)
	 * @see com.strongit.tag.web.tree.IDealTreeNode#getTagParentId()
	 */
	@Override
	public String getTagParentId() {
		
		return ((ArchiveDocTree)obj).getParentId();
	}

	@Override
	public String getUrl() {
		if(getTagParentId()!=null&&!"".equals(getTagParentId())){
			return "sends/docSend!sendList.action?treeValue="+getTagParentId()+","+getTagNodeId()+"&treeType=4";
			}else{
				return "sends/docSend!sendList.action?treeValue="+getTagNodeId()+"&treeType=4";
			}
	}

}
