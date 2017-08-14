package com.strongit.tag.web.tree;

/**
 * <p>Title: IDealTreeNode.java</p>
 * <p>Description: 当前的接口是为了使数据库中的字段名称不影响到程序，将用户的bo实现当前的接口在取出相应内容的时候就利用接口的内容进行取出</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2009-11-30 16:48:10
 * @version  1.0
 */
public abstract class IDealTreeNode {
	
	public Object obj=new Object();
	
	public void setObject(Object obj){
		this.obj=obj;
	}
		
	public abstract String getTagParentId();							//得到树结构当前节点的父节点ID

	public abstract String getTagNodeId();								//得到树结构当前节点的节点ID
	
	public abstract String getTagNodeName();							//得到树结构当前节点的节点名称
	
	public abstract String getUrl();									//得到相应的URL
	
	public abstract String getNodeImg();								//得到相应的节点图片
	
	public abstract String getClick();									//得到相应的点击事件
	
	public boolean checkAble(){											//当前节点的checkbox是否为可选
		return true;
	}
	
}
