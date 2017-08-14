package com.strongit.xxbs.dto;

public class ReleaseDto
{
	private String pubId;
	
	private String pubTitle;
	
	private String pubEditContent;
	
	private String colId;
	
	private String colName;
	
	private String orgId;
	
	private String orgName;
	
	private Boolean isFirstColumn;

	
	public String getPubId()
	{
		return pubId;
	}

	public void setPubId(String pubId)
	{
		this.pubId = pubId;
	}

	public String getPubTitle()
	{
		return pubTitle;
	}

	public void setPubTitle(String pubTitle)
	{
		this.pubTitle = pubTitle;
	}

	public String getPubEditContent()
	{
		return pubEditContent;
	}

	public void setPubEditContent(String pubEditContent)
	{
		this.pubEditContent = pubEditContent;
	}

	public String getColId()
	{
		return colId;
	}

	public void setColId(String colId)
	{
		this.colId = colId;
	}

	public String getColName()
	{
		return colName;
	}

	public void setColName(String colName)
	{
		this.colName = colName;
	}

	public String getOrgId()
	{
		return orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	public Boolean getIsFirstColumn()
	{
		return isFirstColumn;
	}

	public void setIsFirstColumn(Boolean isFirstColumn)
	{
		this.isFirstColumn = isFirstColumn;
	}
	
	
}
