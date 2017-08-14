package com.strongit.oa.common.tree;

import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.tree.IDealTreeNode;

/**
 * 简单的树实现,仅提供选择数据功能。
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2011-1-6 下午04:06:45
 * @version  3.0
 * @classpath com.strongit.oa.common.tree.TreeImpl
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class TreeImpl extends IDealTreeNode {
	
	@Override
	public String getClick() {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public String getNodeImg() {
		TempPo po = (TempPo)obj;
		return po.getCodeId();
	}

	@Override
	public String getTagNodeId() {
		TempPo po = (TempPo)obj;
		return po.getId();
	}

	@Override
	public String getTagNodeName() {
		TempPo po = (TempPo)obj;
		return po.getName();
	}

	@Override
	public String getTagParentId() {
		TempPo po = (TempPo)obj;
		return po.getParentId();
	}

	@Override
	public String getUrl() {
		// TODO 自动生成方法存根
		return null;
	}

}
