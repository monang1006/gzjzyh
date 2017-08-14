/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 6/28/2013 1:21 PM
 * Autour: xush
 * Version: V1.0
 * Description： 点击tree节点链接到某个页面
 */
package com.strongit.oa.archive.sort;

import com.strongit.oa.bo.ToaArchiveSort;
import com.strongit.tag.web.tree.IDealTreeNode;

/**
 * @author xush
 * date 6/28/2013 1:16 PM
 * @version 1.0
 */
public class DealFileTreeNode1 extends IDealTreeNode{


//获取节点id
	public String getTagNodeId() {
		// TODO 自动生成方法存根
		return ((ToaArchiveSort)obj).getSortId();
	}
//获取节点名称
	public String getTagNodeName() {
		// TODO 自动生成方法存根
		return ((ToaArchiveSort)obj).getSortName();
	}
  //获取父节点id
	public String getTagParentId() {
		// TODO 自动生成方法存根
		return ((ToaArchiveSort)obj).getSortParentNo();
	}
//链接的url
	public String getUrl() {
		// TODO 自动生成方法存根
		return "archive/archivefolder/archiveFolder.action?archiveSortId="+getTagNodeId()+"&forward=selected&moduletype=pige1";
	}
	public String getNodeImg() {
		// TODO 自动生成方法存根
		return null;
	}
	//点击方法
   @Override
	public String getClick() {
		// TODO Auto-generated method stub
		return null;
	}

}
