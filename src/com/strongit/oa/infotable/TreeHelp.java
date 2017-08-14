/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-18
 * Autour: zhangli
 * Version: V1.0
 * Description： 父信息树结构辅助BO
 */
package com.strongit.oa.infotable;

public class TreeHelp {
	private String nodeid;

	private String nodename;

	private String nodeparentid;

	private String tableName;

	private String fpro;

	public String getFpro() {
		return fpro;
	}

	public void setFpro(String fpro) {
		this.fpro = fpro;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getNodename() {
		return nodename;
	}

	public void setNodename(String nodename) {
		this.nodename = nodename;
	}

	public String getNodeparentid() {
		return nodeparentid;
	}

	public void setNodeparentid(String nodeparentid) {
		this.nodeparentid = nodeparentid;
	}
}
