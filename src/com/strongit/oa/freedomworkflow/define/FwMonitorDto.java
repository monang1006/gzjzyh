package com.strongit.oa.freedomworkflow.define;

public class FwMonitorDto
{
	/*
	 * 任务id
	 */
	private String ftId;
	
	/*
	 * 任务标题 
	 */
	private String ftTitle;
	
	/*
	 * 处理人
	 */
	private String ftHandler;
	
	/*
	 * 任务开始时间
	 */
	private String ftStartTime;
	
	/*
	 * 任务结束时间
	 */
	private String ftEndTime;
	
	/*
	 * 任务状态
	 */
	private String ftStatus;
	
	/*
	 * 备注
	 */
	private String ftMemo;

	public String getFtId()
	{
		return ftId;
	}

	public void setFtId(String ftId)
	{
		this.ftId = ftId;
	}

	public String getFtTitle()
	{
		return ftTitle;
	}

	public void setFtTitle(String ftTitle)
	{
		this.ftTitle = ftTitle;
	}

	public String getFtHandler()
	{
		return ftHandler;
	}

	public void setFtHandler(String ftHandler)
	{
		this.ftHandler = ftHandler;
	}

	public String getFtStartTime()
	{
		return ftStartTime;
	}

	public void setFtStartTime(String ftStartTime)
	{
		this.ftStartTime = ftStartTime;
	}

	public String getFtEndTime()
	{
		return ftEndTime;
	}

	public void setFtEndTime(String ftEndTime)
	{
		this.ftEndTime = ftEndTime;
	}

	public String getFtStatus()
	{
		return ftStatus;
	}

	public void setFtStatus(String ftStatus)
	{
		this.ftStatus = ftStatus;
	}

	public String getFtMemo()
	{
		return ftMemo;
	}

	public void setFtMemo(String ftMemo)
	{
		this.ftMemo = ftMemo;
	}
	
	
}
