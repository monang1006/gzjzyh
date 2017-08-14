package com.strongit.oa.freedomworkflow.define;

public enum FreedomWorkflowStatus
{
	//流程进行中
	FW_PROCESSING("0"),
	//流程已经结束
	FW_END("1"),
	//任务未开始
	FT_NOT_START("0"),
	//任务待处理
	FT_PENDING("1"),
	//任务已完成
	FT_DONE("2");
	
	private String value;
	
	private FreedomWorkflowStatus(String value)
	{
		this.value = value;
	}
	
	public String val()
	{
		return this.value;
	}

}
