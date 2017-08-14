package com.strongit.oa.globalconfig.util;

public enum GlobalConfigModule {

	GlobalConfigWorkflow("0");
	
	String value;

	public static GlobalConfigModule getEnumEntry(String value) {
		if(value == null) {
			return null;
		}
		GlobalConfigModule[] values = values();
		for(GlobalConfigModule entry : values) {
			if(value.equals(entry.getValue())) {
				return entry;
			}
		}
		return null;
	}
	
	public String getValue() {
		return value;
	}
	
	GlobalConfigModule(String value) {
		this.value = value;
	}
}
