/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-18
 * Autour: zhangli
 * Version: V1.0
 * Description： 信息集树结构辅助类（用于信息项管理模块）
 */
package com.strongit.oa.infotable.infoset;

import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.tag.web.tree.IDealTreeNode;

public class DealItemTreeNode extends IDealTreeNode {

	public String getTagNodeId() {
		// TODO 自动生成方法存根
		return ((ToaSysmanageStructure) obj).getInfoSetCode();
	}

	public String getTagNodeName() {
		// TODO 自动生成方法存根
		return ((ToaSysmanageStructure) obj).getInfoSetName();
	}

	public String getTagParentId() {
		// TODO 自动生成方法存根
		return ((ToaSysmanageStructure) obj).getInfoSetParentid();
	}

	public String getUrl() {
		// TODO 自动生成方法存根
		return "infotable/infoitem/infoItem.action?infoSetCode="
				+ getTagNodeId();
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
