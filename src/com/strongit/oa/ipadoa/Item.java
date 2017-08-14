package com.strongit.oa.ipadoa;


public class Item {

	private String type;
	private String key;
	private String Value;


	public Item(String type, String value) {
		super();
		this.type = type;
		Value = value;
	}


	public Item(String type, String key, String value) {
		super();
		this.type = type;
		this.key = key;
		Value = value;
	}


	public Item(){
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return Value;
	}


	public void setValue(String value) {
		Value = value;
	}
	
	 
	 
}
