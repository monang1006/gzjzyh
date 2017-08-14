package com.strongit.oa.freedomworkflow.define;

public enum FreedomWorkflowField
{
	FW_ID("fwId"),
	FW_TITLE("fwTitle"),
	FW_BUSINESS_ID("fwBusinessId"),
	FW_BUSINESS_NAME("fwBusinessName"),
	FW_CREAT_TIME("fwCreatTime"),
	FW_CREATOR("fwCreator"),
	FW_END_TIME("fwEndTime"),
	FW_FORM_ID("fwFormId"),
	FW_STATUS("fwStatus");
	
	private String value;
	
	private FreedomWorkflowField(String value)
	{
		this.value = value;
	}
	
	public String val()
	{
		return this.value;
	}
}
