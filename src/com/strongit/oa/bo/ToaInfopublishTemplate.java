package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_OA_INFOPUBLISH_TEMPLATE"
 * 
 */
@Entity
@Table(name = "T_OA_INFOPUBLISH_TEMPLATE")
public class ToaInfopublishTemplate implements Serializable {

	/** identifier field */
	@Id
	@GeneratedValue
	private String templateId;
	 /** persistent field */
	private String templateName;
	 /** nullable persistent field */
	private String templateContent;
	 /** persistent field */
	private String templateDesc;

	/** full constructor */
	public ToaInfopublishTemplate(String templateId, String templateName,
			String templateContent, String templateDesc) {
		this.templateId = templateId;
		this.templateName = templateName;
		this.templateContent = templateName;
		this.templateDesc = templateDesc;
	}

	/** default constructor */
	public ToaInfopublishTemplate() {
	}

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String"
	 *               column="TEMPLATE_ID"
	 * 
	 */
	@Id
	@Column(name = "TEMPLATE_ID", nullable = false, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * @hibernate.property column="TEMPLATE_NAME" length="100" not-null="true"
	 * 
	 */
	@Column(name = "TEMPLATE_NAME", nullable = false)
	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Column(name = "TEMPLATE_CONTENT", nullable = true)
	@Lob
	public String getTemplateContent() {
		return this.templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	/**
	 * @hibernate.property column="TEMPLATE_DECS" length="100"
	 * 
	 */
	@Column(name = "TEMPLATE_DECS", nullable = false)
	public String getTemplateDesc() {
		return this.templateDesc;
	}

	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}

}
