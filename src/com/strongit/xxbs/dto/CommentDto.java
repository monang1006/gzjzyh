package com.strongit.xxbs.dto;

import java.util.Date;


public class CommentDto
{
	private String commentId;
	private String commentName;
	private String commentDate;
	private String commentInfo;
	private String commentIstrue;
	public String getCommentName() {
		return commentName;
	}
	public void setCommentName(String commentName) {
		this.commentName = commentName;
	}
	
	
	public String getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}
	public String getCommentInfo() {
		return commentInfo;
	}
	public void setCommentInfo(String commentInfo) {
		this.commentInfo = commentInfo;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getCommentIstrue() {
		return commentIstrue;
	}
	public void setCommentIstrue(String commentIstrue) {
		this.commentIstrue = commentIstrue;
	}
	
	
}
