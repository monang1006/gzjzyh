package com.strongit.oa.report;

import com.strongit.tag.web.tree.IDealTreeNode;
import com.strongit.oa.util.TempPo;
/**
 * 树处理类
 * @Create Date: 2010-12-24
 * @author luosy
 * @version 1.0
 */
public class ChooseTreeDeal extends IDealTreeNode{

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
