package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 模板与电子表单字段关联BO.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-7-7 下午04:47:39
 * @version  2.0.2.3
 * @classpath com.strongit.oa.bo.ToaBookMarkEForm
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Entity
@Table(name = "T_OA_TEMPLATE_EFORM", catalog = "", schema = "")
public class ToaTemplateEForm {

	private String id ;				//主键
	
	private String formId ;			//表单id
	
	private String templateId;		//模板id

	public ToaTemplateEForm(){
		
	}

	@Column(name = "FORMID")
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TEMPLATE_ID")
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
