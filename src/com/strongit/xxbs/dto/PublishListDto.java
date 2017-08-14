package com.strongit.xxbs.dto;

import java.util.Date;

public class PublishListDto
{
	private String pubId;
	private String orgId;
	private Date pubDate;
	private String pubIsInstruction;
	private String pubIsComment;
	private String pubIsShare;
	private String pubTitle;
	private String pubUseStatus;
	private String pubMergeOrg;
	private String preId;
	private String nextId;
	private String pubMergeName;
	private String orgName;
	public String getPubId()
	{
		return pubId;
	}
	public void setPubId(String pubId)
	{
		this.pubId = pubId;
	}
	public String getOrgId()
	{
		return orgId;
	}
	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}
	public Date getPubDate()
	{
		return pubDate;
	}
	public void setPubDate(Date pubDate)
	{
		this.pubDate = pubDate;
	}
	public String getPubIsInstruction() {
		return pubIsInstruction;
	}
	public void setPubIsInstruction(String pubIsInstruction) {
		this.pubIsInstruction = pubIsInstruction;
	}
	public String getPubIsComment()
	{
		return pubIsComment;
	}
	public void setPubIsComment(String pubIsComment)
	{
		this.pubIsComment = pubIsComment;
	}
	public String getPubIsShare()
	{
		return pubIsShare;
	}
	public void setPubIsShare(String pubIsShare)
	{
		this.pubIsShare = pubIsShare;
	}
	public String getPubTitle()
	{
		return pubTitle;
	}
	public void setPubTitle(String pubTitle)
	{
		this.pubTitle = pubTitle;
	}
	public String getPubUseStatus()
	{
		return pubUseStatus;
	}
	public void setPubUseStatus(String pubUseStatus)
	{
		this.pubUseStatus = pubUseStatus;
	}
	public String getPubMergeOrg() {
		return pubMergeOrg;
	}
	public void setPubMergeOrg(String pubMergeOrg) {
		this.pubMergeOrg = pubMergeOrg;
	}
	public String getPreId() {
		return preId;
	}
	public void setPreId(String preId) {
		this.preId = preId;
	}
	public String getNextId() {
		return nextId;
	}
	public void setNextId(String nextId) {
		this.nextId = nextId;
	}
	public String getPubMergeName() {
		return pubMergeName;
	}
	public void setPubMergeName(String pubMergeName) {
		this.pubMergeName = pubMergeName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
}
