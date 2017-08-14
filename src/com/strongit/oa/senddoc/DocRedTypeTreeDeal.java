package com.strongit.oa.senddoc;

import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.tree.IDealTreeNode;

public class DocRedTypeTreeDeal extends IDealTreeNode {

/*	*//**
	 * 双击事件
	 *//*
	@Override
	public String getDbClick() {
		// TODO 自动生成方法存根
		return "javascript:doSelect()";
	}
*/
	@Override
	public String getClick() {
		TempPo po = (TempPo)this.obj;
		String type = po.getType();
		return "javascript:select(\""+this.getTagNodeId()+"\""+",\""+type+"\")";
	}

	@Override
	public String getNodeImg() {
		TempPo po = (TempPo)this.obj;
		String type = po.getType();
		if("item".equals(type)){
			return "/tree.gif";
		}
		return "/folder_closed.gif";
	}

	@Override
	public String getTagNodeId() {
		TempPo po = (TempPo)this.obj;
		return po.getId();
	}

	@Override
	public String getTagNodeName() {
		TempPo po = (TempPo)this.obj;
		return po.getName();
	}

	@Override
	public String getTagParentId() {
		TempPo po = (TempPo)this.obj;
		return po.getParentId();
	}

	@Override
	public String getUrl() {
		// TODO 自动生成方法存根
		return null;
	}

}
