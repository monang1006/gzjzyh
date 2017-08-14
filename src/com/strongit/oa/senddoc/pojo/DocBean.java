package com.strongit.oa.senddoc.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

public class DocBean implements Serializable {

	private static final long serialVersionUID = 5933760753726613514L;

	private String docId;
	private String docTitle;
	private byte[] content;
	private String draftTime;
	
	public DocBean() {
		
	}
	
	public String getDraftTime() {
		return draftTime;
	}

	public void setDraftTime(String draftTime) {
		this.draftTime = draftTime;
	}

	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getDocTitle() {
		return docTitle;
	}
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
	
	
}
