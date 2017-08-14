package com.strongit.oa.address.util;

import com.strongit.oa.bo.ToaAddressGroup;
import com.strongit.tag.web.tree.IDealTreeNode;

/**
 * 展示个人通讯录组的树形结构
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2009-7-20 上午09:35:37
 * @version  2.0.2.3
 * @classpath com.strongit.oa.address.util.AddressGroupTree
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class AddressGroupTree extends IDealTreeNode{

	@Override
	public String getNodeImg() {
		return null;
	}

	@Override
	public String getTagNodeId() {
		ToaAddressGroup temp=(ToaAddressGroup)this.obj;
		return temp.getAddrGroupId();
	}

	@Override
	public String getTagNodeName() {
		ToaAddressGroup temp=(ToaAddressGroup)this.obj;
		return temp.getAddrGroupName();
	}

	@Override
	public String getTagParentId() {
		return "0";
	}

	@Override
	public String getUrl() {
		return null;
	}

	@Override
	public String getClick() {
		return null;
	}
	
}
