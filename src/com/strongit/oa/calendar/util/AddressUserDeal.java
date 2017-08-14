package com.strongit.oa.calendar.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.tree.IDealTreeNode;

public class AddressUserDeal extends IDealTreeNode{

	

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
		if("userAddress".equals(this.getType())){
			url = "/calendar/calendar!assignUserList.action?treeUserId="+this.getTagNodeId();
		}else{
			url = "/calendar/calendar!assignUserList.action?treeUserId=";
		}
		return url;
	}

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
