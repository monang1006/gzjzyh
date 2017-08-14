package com.strongit.oa.prsnfldr.util;

import java.io.UnsupportedEncodingException;

import com.strongit.oa.bo.ToaPrsnfldrFolder;
import com.strongit.tag.web.tree.IDealTreeNode;


public class DealTreeNode extends IDealTreeNode{



	public String getTagNodeId() {
		return ((ToaPrsnfldrFolder)obj).getFolderId();
	}

	public String getTagNodeName() {
		return ((ToaPrsnfldrFolder)obj).getFolderName();
	}

	public String getTagParentId() {
		return ((ToaPrsnfldrFolder)obj).getFolderParentId();
	}

	public String getUrl() {
		String url = "prsnfldr/privateprsnfldr/prsnfldrFile.action";	   
		String params = "?folderId="+getTagNodeId();
			try {
				params += "&folderName="+java.net.URLEncoder.encode(java.net.URLEncoder.encode(getTagNodeName(), "utf-8"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		url += params;
		return url;
	}
	
	public String getNodeImg() {
		return null;
	}

	@Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}

}
