package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 标签与电子表单字段关联BO.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-7-7 下午04:47:39
 * @version  2.0.2.3
 * @classpath com.strongit.oa.bo.ToaBookMarkEForm
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Entity
@Table(name = "T_OA_BOOKMARK_EFORM", catalog = "", schema = "")
public class ToaBookMarkEForm {

	private String id ;				//主键
	
	private String formId ;			//表单id
	
	private String fieldName ;		//字段名称
	
	private String tableName ;		//表名称	
	
	private String bookMarkId ;		//标签id
	
	private String rest1 ;			//扩展字段一（已用于存储控件id）
	
	private String rest2 ;			//扩展字段二
	
	private String rest3 ;			//扩展字段三
	
	private String rest4 ;			//扩展字段四

	public ToaBookMarkEForm(){
		
	}

	@Column(name = "BOOKMARK_ID")
	public String getBookMarkId() {
		return bookMarkId;
	}

	public void setBookMarkId(String bookMarkId) {
		this.bookMarkId = bookMarkId;
	}

	@Column(name = "FIELD_NAME")
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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

	@Column(name = "REST1")
	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2")
	public String getRest2() {
		return rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "REST3")
	public String getRest3() {
		return rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}

	@Column(name = "REST4")
	public String getRest4() {
		return rest4;
	}

	public void setRest4(String rest4) {
		this.rest4 = rest4;
	}

	@Column(name = "TABLE_NAME")
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
