package com.strongit.oa.util;

import com.strongit.oa.bo.ToaSysmanageProperty;

public class FieldValue  {
	ToaSysmanageProperty pro;//记录属性
	String value;//记录属性对应的值
	
	public FieldValue(){
		
	}
	public FieldValue(ToaSysmanageProperty pro,String value){
		this.pro=pro;
		this.value=value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public ToaSysmanageProperty getPro() {
		return pro;
	}
	public void setPro(ToaSysmanageProperty pro) {
		this.pro = pro;
	}

	
	
}
