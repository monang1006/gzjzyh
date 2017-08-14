package com.strongit.oa.address.util;

import com.strongit.tag.web.tree.IDealTreeNode;
import com.strongit.oa.util.TempPo;
/**
 * 机构树处理类
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2009-12-22 下午04:34:32
 * @version  2.0.2.3
 * @classpath com.strongit.oa.address.util.OrgTreeDeal
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class OrgTreeDeal extends IDealTreeNode{

	

	@Override
	public String getNodeImg() {
		TempPo temp=(TempPo)this.obj;
		if("user".equals(temp.getType())){
			return "/renyuan.gif";
		}else if("post".equals(temp.getType())){
			return "/tree.gif";
		}
		return "/folder_closed.gif";
	}

	@Override
	public String getTagNodeId() {
		TempPo temp=(TempPo)this.obj;
		return temp.getId();
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
	public boolean checkAble() {
		TempPo temp=(TempPo)this.obj;
		//System.out.println(Boolean.valueOf(temp.getType()));
		return Boolean.valueOf(temp.getType());
	}

	@Override
	public String getClick() {
		return null;
	}
	
}
