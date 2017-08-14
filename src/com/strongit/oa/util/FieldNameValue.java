package com.strongit.oa.util;

/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_PROPERTY"
 *     
*/
public class FieldNameValue  {
	String proname;//记录属性
	String type;//记录属性类型
	String value;//记录属性对应的值
	
	public FieldNameValue(){
		
	}
	
	public FieldNameValue(String proname,String type,String value){
		this.proname=proname;
		this.type=type;
		this.value=value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
