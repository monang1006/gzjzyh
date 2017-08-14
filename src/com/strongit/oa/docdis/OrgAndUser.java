package com.strongit.oa.docdis;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Dec 19, 2009 7:44:35 PM
 * @version  2.0.4
 * @comment
 */
public class OrgAndUser{

	private String id;
	
	private String name;
	
	private String parentId;
	
	private String isOrgOrUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIsOrgOrUser() {
		return isOrgOrUser;
	}

	public void setIsOrgOrUser(String isOrgOrUser) {
		this.isOrgOrUser = isOrgOrUser;
	}
	
	

}
