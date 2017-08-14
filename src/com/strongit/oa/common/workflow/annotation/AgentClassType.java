package com.strongit.oa.common.workflow.annotation;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Feb 25, 2010 10:37:27 AM
 * @version  2.0.4
 * @comment
 */
public enum AgentClassType {

	AUTONODECLASS("0"),//自动节点
	
	NODECLASS("1");//普通节点，用于节点进入、节点离开
	
	private String value;
	
	AgentClassType(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
	
}
