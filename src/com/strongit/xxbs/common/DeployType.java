package com.strongit.xxbs.common;

import com.strongit.xxbs.common.contant.Publish;

public class DeployType
{
	private String type = Publish.TERMINAL_CAI;
	private String dbType = Publish.DATEBASE_ORACLE;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDbType()
	{
		return dbType;
	}

	public void setDbType(String dbType)
	{
		this.dbType = dbType;
	}
	
	
}
