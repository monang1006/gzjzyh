package com.strongit.oa.address.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.strongit.oa.util.TempPo;
import com.strongit.tag.web.tree.IDealTreeNode;

public class AddressOrgDeal extends IDealTreeNode{

	

	@Override
	public String getNodeImg() {
		return null;
	}

	@Override
	public String getTagNodeId() {
		TempPo org=(TempPo)this.obj;
		return org.getId();
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

	public String getPK(){
		TempPo org=(TempPo)this.obj;
		return org.getCodeId();
	}

	public String getType(){
		TempPo org = (TempPo)obj;
		return org.getType();
	}
	
	@Override
	public String getUrl() {
		//if(!"0".equals(getTagNodeId())){//非根节点情况
			try {
				String orgName = URLEncoder.encode(getTagNodeName(), "utf-8");
				orgName = URLEncoder.encode(orgName, "utf-8");
				if("import".equals(getType())){
					return "/address/addressOrg!importOrgUserList.action?orgName="+orgName+"&orgId="+getPK();
				}
				return "/address/addressOrg!orguserlist.action?orgName="+orgName+"&orgId="+getPK();
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
	//	}
		
		return null;
	}

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
