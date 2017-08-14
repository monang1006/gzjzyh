package com.strongit.workflow.workflowDesign.util;

import com.strongit.tag.web.tree.IDealTreeNode;
import com.strongit.oa.util.TempPo;

/**
 * 岗位树形展现处理类.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-7-15 下午04:19:04
 * @version  2.0.2.3
 * @classpath com.strongit.workflow.workflowDesign.util.PostTreeDeal
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class PostTreeDeal extends IDealTreeNode{

	@Override
	public String getNodeImg() {
		return null;
	}

	@Override
	public String getTagNodeId() {
		TempPo temp=(TempPo)this.obj;
		String name = temp.getName();
		String id = temp.getId();
		return id+","+name;
	}

	@Override
	public String getTagNodeName() {
		TempPo temp=(TempPo)this.obj;
		return temp.getName();
	}

	@Override
	public String getTagParentId() {
		TempPo temp=(TempPo)this.obj;
		return temp.getParentId();
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
