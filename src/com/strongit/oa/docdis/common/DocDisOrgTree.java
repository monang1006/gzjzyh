package com.strongit.oa.docdis.common;

import com.strongit.oa.util.MessagesConst;
import com.strongit.tag.web.tree.IDealTreeNode;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.IUserService;
import com.strongmvc.service.ServiceLocator;

public class DocDisOrgTree extends IDealTreeNode{

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeImg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagNodeId() {
		// TODO Auto-generated method stub
		return ((TUumsBaseOrg)this.obj).getOrgSyscode();
	}

	@Override
	public String getTagNodeName() {
		// TODO Auto-generated method stub
		return ((TUumsBaseOrg)this.obj).getOrgName();
	}

	@Override
	public String getTagParentId() {
		// TODO Auto-generated method stub
		IUserService orgManager = (IUserService)ServiceLocator.getService("userService");
		String parentCode = orgManager.getParentCode(MessagesConst.CODE_RULE, ((TUumsBaseOrg)this.obj).getOrgSyscode());
		return parentCode;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

}
