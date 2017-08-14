package com.strongit.oa.component.formtemplate;

/**
 * 定义加载数据的类型
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Jan 16, 2012
 * @classpath	com.strongit.oa.component.formtemplate.LoadDataType
 * @version  	5.0
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
public enum LoadDataType {

	LoadDataTodo("0"),
	LoadDataProcessed("1"),
	LoadDataHosted("2"),
	LoadDataDraft("3");

	public static LoadDataType getLoadDataType(String value) {
		if("0".equals(value)) {
			return LoadDataTodo;
		} else if("1".equals(value)) {
			return LoadDataProcessed;
		} else if("2".equals(value)) {
			return LoadDataHosted;
		} else if("3".equals(value)) {
			return LoadDataDraft;
		}
		return LoadDataTodo;
	}
	
	private String value;

	public String getValue() {
		return value;
	}
	
	LoadDataType(String value){
		this.value = value;
	}
}
