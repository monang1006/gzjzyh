package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name="T_OA_SEND_PRINT_PER")
public class ToaPrintSet implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue	
	private String sppId;
	
	private String userId;//用户Id
	
	private String senddocId;//公文Id
	
	private int    printCount;//个人已打印份数
	
	
	

	public ToaPrintSet() {
	}

	public ToaPrintSet(String sppId, String userId, String senddocId,
			int printCount) {
		this.sppId = sppId;
		this.userId = userId;
		this.senddocId = senddocId;
		this.printCount = printCount;
	}

	@Id
    @Column(name="SPP_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getSppId() {
		return sppId;
	}

	public void setSppId(String sppId) {
		this.sppId = sppId;
	}

	@Column(name="USER_ID", nullable=false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name="SENDDOC_ID", nullable=false)
	public String getSenddocId() {
		return senddocId;
	}

	public void setSenddocId(String senddocId) {
		this.senddocId = senddocId;
	}

	@Column(name="PRINT_COUNT", nullable=false)
	public int getPrintCount() {
		return printCount;
	}

	public void setPrintCount(int printCount) {
		this.printCount = printCount;
	}
	
	

}
