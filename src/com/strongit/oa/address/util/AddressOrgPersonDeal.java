package com.strongit.oa.address.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.tree.IDealTreeNode;

public class AddressOrgPersonDeal extends IDealTreeNode{

	

	@Override
	public String getNodeImg() {
		return null;
	}

	@Override
	public String getTagNodeId() {
		TempPo org=(TempPo)this.obj;
		return org.getId();
	}

	private String getCode() {
		TempPo po = (TempPo)this.obj;
		return po.getCodeId();
	}
	
	@Override
	public String getTagNodeName() {
		TempPo org=(TempPo)this.obj;
		return org.getName();
	}

	@Override
	public String getTagParentId() {
		TempPo org=(TempPo)this.obj;
		return org.getParentId();
	}

	public String getType(){
		TempPo org = (TempPo)obj;
		return org.getType();
	}
	public String getTypewei(){
		TempPo org = (TempPo)obj;
		return org.getTypewei();
	}
	@Override
	public String getUrl() {
		String url = null;
		if(GlobalBaseData.ADDRESS_PERSONAL_ROOT_NAME.equals(this.getTagNodeId())){//常用联系人跟节点
			url = "/address/address!showlist.action?type="+getType()+"&typewei="+getTypewei();
		}else if(GlobalBaseData.ADDRESS_PUBLIC_ROOT_NAME.equals(this.getTagNodeId())){//系统通讯录跟节点
			url = "/address/address!showlist.action?type="+getType()+"&typewei="+getTypewei();
		}else if(GlobalBaseData.ADDRESS_USERGROUP_ROOT_NAME.equals(this.getTagNodeId())){//用户组
			url = "/address/address!showlist.action?type="+getType()+"&typewei="+getTypewei();//根节点，只有根节点的
		}else{
			if("docAddress".equals(this.getType())){//个人通讯录子节点
				url = "/address/address!showlist.action?type="+getType()+"&groupId="+this.getTagNodeId()+"&typewei="+getTypewei();
			}else if("orgAddress".equals(this.getType())){//系统通讯录子节点
				url = "/address/address!showlist.action?type="+getType()+"&groupId="+this.getCode()+"&typewei="+getTypewei();
			}else if("userGroup".equals(this.getType())){//用户组联系人
				url = "/address/address!showlist.action?type="+getType()+"&groupId="+this.getCode()+"&typewei="+getTypewei();//跟节点下的子节点
			}
		}
		return url;
	}

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
