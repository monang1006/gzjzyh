package com.strongit.doc.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_DOC_SENDS_PRINT_PASSWORD")
public class TdocSendPrintPassword {
	private String id;
	private String userId;
	private String password;
	private String rest1;
	private String rest2;
	private String rest3;
	@Id
	@Column(name="PRINT_ID",nullable=false)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name="PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="REST1")
	public String getRest1() {
		return rest1;
	}
	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}
	@Column(name="REST2")
	public String getRest2() {
		return rest2;
	}
	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}
	@Column(name="REST3")
	public String getRest3() {
		return rest3;
	}
	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}
}
