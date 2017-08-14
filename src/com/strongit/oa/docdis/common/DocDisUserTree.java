package com.strongit.oa.docdis.common;

import com.strongit.oa.docdis.OrgAndUser;
import com.strongit.tag.web.tree.IDealTreeNode;

public class DocDisUserTree extends IDealTreeNode{

	@Override
	public String getClick() {
		return null;
	}

	@Override
	public String getNodeImg() {
		return null;
	}

	@Override
	public String getTagNodeId() {
		return ((OrgAndUser)this.obj).getId();
	}

	@Override
	public String getTagNodeName() {
		return ((OrgAndUser)this.obj).getName();
	}

	@Override
	public String getTagParentId() {
		// TODO Auto-generated method stub
		//IOrgManager orgManager = (IOrgManager)ServiceLocator.getService("orgManager");
		//String parentCode = orgManager.getParentCode(MessagesConst.CODE_RULE, ((TUumsBaseOrg)this.obj).getOrgSyscode());
		return ((OrgAndUser)this.obj).getParentId();
	}
	
	

	@Override
	public boolean checkAble() {
		if(((OrgAndUser)this.obj).getIsOrgOrUser().equals("0")){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public String getUrl() {
		return null;
	}

}
