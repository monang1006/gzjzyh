package com.strongit.oa.address.util;

import com.strongit.tag.web.tree.IDealTreeNode;
import com.strongit.oa.util.TempPo;
public class AddressDeal extends IDealTreeNode{

	

	@Override
	public String getNodeImg() {
		return null;
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

	public String getPK(){
		TempPo org=(TempPo)this.obj;
		return org.getCodeId();
	}

	public String getType(){
		TempPo org=(TempPo)this.obj;
		return org.getType();
	}
	
	@Override
	public String getTagParentId() {
		TempPo temp=(TempPo)this.obj;
		return temp.getParentId();
	}

	@Override
	public String getUrl() {
		/*if(!"personalgroupId".equals(getTagNodeId()) && !"publicgroupId".equals(getTagNodeId())){//非根节点情况
			if("public".equals(getType())){//公共通讯录节点
				return "javascript:select(\""+getPK()+"\""+",\"public\")";
			}
			return "javascript:select(\""+getTagNodeId()+"\")";
		}*/
		
		return null;
	}

	@Override
	public String getClick() {
		if(!"personalgroupId".equals(getTagNodeId()) && !"publicgroupId".equals(getTagNodeId())){//非根节点情况
			if("public".equals(getType())){//公共通讯录节点
				return "javascript:select(\""+getPK()+"\""+",\"public\")";
			}
			return "javascript:select(\""+getTagNodeId()+"\")";
		}
		
		return null;
	}
	
}
