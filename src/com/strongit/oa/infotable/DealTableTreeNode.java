/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-18
 * Autour: zhangli
 * Version: V1.0
 * Description： 父信息树结构辅助类
 */
package com.strongit.oa.infotable;

import com.strongit.tag.web.tree.IDealTreeNode;

public class DealTableTreeNode extends IDealTreeNode {

	public String getTagNodeId() {
		// TODO 自动生成方法存根
		return ((TreeHelp) obj).getNodeid();
	}

	public String getTagNodeName() {
		// TODO 自动生成方法存根
		return ((TreeHelp) obj).getNodename();
	}

	public String getTagParentId() {
		// TODO 自动生成方法存根
		return ((TreeHelp) obj).getNodeparentid();
	}

	public String getUrl() {
		// TODO 自动生成方法存根
		return "infotable/infoTable.action?tableName="
				+ ((TreeHelp) obj).getTableName() + "&fpro="
				+ ((TreeHelp) obj).getFpro() + "&fid=" + getTagNodeId();
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
