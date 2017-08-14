/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-23
 * Autour: zhangli
 * Version: V1.0
 * Description： 字典树辅助类（用于案卷管理）
 */
package com.strongit.oa.archive.archivefolder;

import com.strongit.oa.common.user.model.User;
import com.strongit.tag.web.tree.IDealTreeNode;

public class DealUserTreeNode extends IDealTreeNode {

	public String getTagNodeId() {
		// TODO 自动生成方法存根
		return ((User) obj).getUserId();
	}

	public String getTagNodeName() {
		// TODO 自动生成方法存根
		return ((User) obj).getUserName();
	}

	public String getTagParentId() {
		// TODO 自动生成方法存根
		return null;
	}

	public String getNodeImg() {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}

}
