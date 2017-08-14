package com.strongit.oa.personnel.util;

import java.io.Serializable;
import java.util.Date;

public class Regard implements Serializable {
	private String regardId;
	private String leader;
	private Date rDate;
	private String mark;
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}

	public Date getRDate() {
		return rDate;
	}
	public void setRDate(Date date) {
		rDate = date;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public Regard() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getRegardId() {
		return regardId;
	}
	public void setRegardId(String regardId) {
		this.regardId = regardId;
	}
	public Regard(String regardId, String leader, Date date, String mark) {
		super();
		this.regardId = regardId;
		this.leader = leader;
		rDate = date;
		this.mark = mark;
	}
	

	
}
