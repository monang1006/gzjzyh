package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

/** 
 *        @hibernate.class
 *         table="T_OA_SENDDOC_ATTACH"
 *     
 */
@Entity
@Table(name = "T_OA_SENDDOC_ATTACH")
public class ToaSenddocAttach implements Serializable {

	private static final long serialVersionUID = 69319457084179202L;

	/** identifier field */
	@Id
	@GeneratedValue
	private String senddocAttachId;

	/** nullable persistent field */
	private String senddocAttachTitle;

	/** nullable persistent field */
	private String senddocAttachRemark;

	/** nullable persistent field */
	private String attachId3;

	/** persistent field */
	private com.strongit.oa.bo.ToaSenddoc toaSenddoc;

	/** full constructor */
	public ToaSenddocAttach(String senddocAttachId, String senddocAttachTitle,
			String senddocAttachRemark, String attachId3,
			com.strongit.oa.bo.ToaSenddoc toaSenddoc) {
		this.senddocAttachId = senddocAttachId;
		this.senddocAttachTitle = senddocAttachTitle;
		this.senddocAttachRemark = senddocAttachRemark;
		this.attachId3 = attachId3;
		this.toaSenddoc = toaSenddoc;
	}

	/** default constructor */
	public ToaSenddocAttach() {
	}

	/** minimal constructor */
	public ToaSenddocAttach(String senddocAttachId,
			com.strongit.oa.bo.ToaSenddoc toaSenddoc) {
		this.senddocAttachId = senddocAttachId;
		this.toaSenddoc = toaSenddoc;
	}

	/** 
	 *            @hibernate.id
	 *             generator-class="assigned"
	 *             type="java.lang.String"
	 *             column="SENDDOC_ATTACH_ID"
	 *         
	 */
	@Id
	@Column(name="SENDDOC_ATTACH_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getSenddocAttachId() {
		return this.senddocAttachId;
	}

	public void setSenddocAttachId(String senddocAttachId) {
		this.senddocAttachId = senddocAttachId;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_ATTACH_TITLE"
	 *             length="100"
	 *         
	 */
	@Column(name="SENDDOC_ATTACH_TITLE", length=100)
	public String getSenddocAttachTitle() {
		return this.senddocAttachTitle;
	}

	public void setSenddocAttachTitle(String senddocAttachTitle) {
		this.senddocAttachTitle = senddocAttachTitle;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_ATTACH_REMARK"
	 *             length="4000"
	 *         
	 */
	@Column(name="SENDDOC_ATTACH_REMARK", length=4000)
	public String getSenddocAttachRemark() {
		return this.senddocAttachRemark;
	}

	public void setSenddocAttachRemark(String senddocAttachRemark) {
		this.senddocAttachRemark = senddocAttachRemark;
	}

	/** 
	 *            @hibernate.property
	 *             column="ATTACH_ID3"
	 *             length="32"
	 *         
	 */
	@Column(name="ATTACH_ID3", length=32)
	public String getAttachId3() {
		return this.attachId3;
	}

	public void setAttachId3(String attachId3) {
		this.attachId3 = attachId3;
	}

	/** 
	 *            @hibernate.many-to-one
	 *             not-null="true"
	 *            @hibernate.column name="SENDDOC_ID"         
	 *         
	 */
	@ManyToOne
	@JoinColumn(name="SENDDOC_ID")
	@Cascade(value={CascadeType.PERSIST, CascadeType.MERGE})
	public com.strongit.oa.bo.ToaSenddoc getToaSenddoc() {
		return this.toaSenddoc;
	}

	public void setToaSenddoc(com.strongit.oa.bo.ToaSenddoc toaSenddoc) {
		this.toaSenddoc = toaSenddoc;
	}

	@Transient
	public String toString() {
		return new ToStringBuilder(this).append("senddocAttachId",
				getSenddocAttachId()).toString();
	}

}
