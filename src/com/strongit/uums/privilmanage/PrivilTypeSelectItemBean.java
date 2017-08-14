/**
 * 编辑资源权限时需要配置的资源权限所属类型select元素类
 * @author 喻斌
 * @company Strongit Ltd. (c) copyright
 * @date Jun 23, 2009 2:24:54 PM
 * @version 1.0
 * @classpath com.strongit.uums.privilmanage.PrivilTypeSelectItemBean
 */
package com.strongit.uums.privilmanage;

public class PrivilTypeSelectItemBean {
	//select item Id
	public String itemId;
	//select item 名称
	public String itemName;
	
	public PrivilTypeSelectItemBean(String itemId, String itemName){
		this.itemId = itemId;
		this.itemName = itemName;
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}
