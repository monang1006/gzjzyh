package com.strongit.xxbs.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_INFO_QUERY_TREE database table.
 * 
 */
@Entity
@Table(name="T_INFO_QUERY_TREE")
public class TInfoQueryTree implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column
	private String name;

	@Column
	private String pid;

	@Column
	private String value;

    public TInfoQueryTree() {
    }

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}