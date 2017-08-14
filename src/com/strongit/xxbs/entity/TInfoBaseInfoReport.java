package com.strongit.xxbs.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.Date;


/**
 * The persistent class for the T_INFO_BASE_REPORT database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_REPORT")
public class TInfoBaseInfoReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="RP_ID")
	private String rpId;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="RP_DATE")
	private Date rpDate;

    @Lob()
    //@Type(type="org.springframework.orm.hibernate3.support.BlobByteArrayType")
	@Column(name="RP_WORD")
	private byte[] rpWord;

	@Column(name="RP_TITLE")
	private String rpTitle;

    public TInfoBaseInfoReport() {
    }

	public String getRpId() {
		return this.rpId;
	}

	public void setRpId(String rpId) {
		this.rpId = rpId;
	}

	public Date getRpDate() {
		return this.rpDate;
	}

	public void setRpDate(Date rpDate) {
		this.rpDate = rpDate;
	}

	public String getRpTitle() {
		return this.rpTitle;
	}

	public void setRpTitle(String rpTitle) {
		this.rpTitle = rpTitle;
	}

	public byte[] getRpWord() {
		return this.rpWord;
	}

	public void setRpWord(byte[] rpWord) {
		this.rpWord = rpWord;
	}

}