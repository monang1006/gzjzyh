package com.strongit.oa.common.eform.model;

public class EFormField {
	/* 表单域ID */
	private String id;
	/* 表单域名称 */
	private String name;
	/* 表单域标题 */
	private String caption;
	/* 表单域类型 */
	private String type;
	/* 表单对应表名称 */
	private String tablename = "";
	/* 表单域对应字段名称 */
	private String fieldname = "";
	
	private int fieldDiaplaySize ;//指示指定列的最大标准宽度，以字符为单位
	private String fieldTypeName ;//获取指定列的数据库特定的类型名称。
	private String fieldClassName;//如果调用方法 ResultSet.getObject 从列中获取值，则返回构造其实例的 Java 类的完全限定名称。
	private int fieldType;//获取指定列的 SQL 类型。
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	public String getFieldClassName() {
		return fieldClassName;
	}
	public void setFieldClassName(String fieldClassName) {
		this.fieldClassName = fieldClassName;
	}
	public int getFieldDiaplaySize() {
		return fieldDiaplaySize;
	}
	public void setFieldDiaplaySize(int fieldDiaplaySize) {
		this.fieldDiaplaySize = fieldDiaplaySize;
	}
	public int getFieldType() {
		return fieldType;
	}
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldTypeName() {
		return fieldTypeName;
	}
	public void setFieldTypeName(String fieldTypeName) {
		this.fieldTypeName = fieldTypeName;
	}
	
	
}
