package com.strongit.xxbs.dto;

import java.math.BigDecimal;


public class AdoptionDto
{
	private String pubId;
	
	private BigDecimal pubUseScore;

	private BigDecimal pubInstructionScore;

	private String pubInstructor;

	private String pubInstructionContent;

	private String colId;
	
	private String issId;
	

	public String getPubId()
	{
		return pubId;
	}

	public void setPubId(String pubId)
	{
		this.pubId = pubId;
	}

	public BigDecimal getPubUseScore()
	{
		return pubUseScore;
	}

	public void setPubUseScore(BigDecimal pubUseScore)
	{
		this.pubUseScore = pubUseScore;
	}

	public BigDecimal getPubInstructionScore()
	{
		return pubInstructionScore;
	}

	public void setPubInstructionScore(BigDecimal pubInstructionScore)
	{
		this.pubInstructionScore = pubInstructionScore;
	}

	public String getPubInstructor()
	{
		return pubInstructor;
	}

	public void setPubInstructor(String pubInstructor)
	{
		this.pubInstructor = pubInstructor;
	}

	public String getPubInstructionContent()
	{
		return pubInstructionContent;
	}

	public void setPubInstructionContent(String pubInstructionContent)
	{
		this.pubInstructionContent = pubInstructionContent;
	}

	public String getColId()
	{
		return colId;
	}

	public void setColId(String colId)
	{
		this.colId = colId;
	}

	public String getIssId()
	{
		return issId;
	}

	public void setIssId(String issId)
	{
		this.issId = issId;
	}
}
