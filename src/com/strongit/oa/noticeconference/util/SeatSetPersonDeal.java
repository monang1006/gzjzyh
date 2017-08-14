package com.strongit.oa.noticeconference.util;

import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.tree.IDealTreeNode;

public class SeatSetPersonDeal extends IDealTreeNode{

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
	

	@Override
	public String getUrl() {
		String url = null;
		url = "/noticeconference/clientConference!showlist.action?orgCode=" + this.getCode();
		/*if("orgAddress".equals(this.getType())){//系统通讯录子节点
			url = "/address/address!showlist.action?type="+getType()+"&groupId="+this.getCode();
		}else if("bookAddress".equals(this.getType())){//领导名册联系人
			url = "/seatset/addressPerson!showlist.action?orgCode=" + this.getCode();
		}*/
		return url;
	}

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}
}
