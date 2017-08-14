package com.strongit.xxbs.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_INFO_BASE_WORD_TEMPLATE")
public class TInfoBaseWordTemplate implements Serializable 
{
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="WT_ID")
	private String wtId;
	
	@Column(name="WT_TITLE")
	private String wtTitle;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="WT_DATE")
	private Date wtDate;

	public String getWtId()
	{
		return wtId;
	}

	public void setWtId(String wtId)
	{
		this.wtId = wtId;
	}

	public String getWtTitle()
	{
		return wtTitle;
	}

	public void setWtTitle(String wtTitle)
	{
		this.wtTitle = wtTitle;
	}

	public Date getWtDate()
	{
		return wtDate;
	}

	public void setWtDate(Date wtDate)
	{
		this.wtDate = wtDate;
	}
	
	
}
