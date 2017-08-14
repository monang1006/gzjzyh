package com.strongit.oa.address.util;

import com.strongit.tag.web.tree.IDealTreeNode;
import com.strongit.oa.util.TempPo;
public class AddressSelectUserDeal extends IDealTreeNode{

	

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
	public String getClick() {
		return null;
	}
	
}
