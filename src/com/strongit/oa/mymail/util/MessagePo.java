package com.strongit.oa.mymail.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessagePo {
	
	private String pSubject;
	private String pContent;
	private Date pSentDate;
	private String pSenter;
	private String pMessageId;
	private String pReceiver;
	private String pSize;
	private String hasAtt;
	private List<FileObj> attList=new ArrayList<FileObj>();
	public String getHasAtt() {
		return hasAtt;
	}
	public void setHasAtt(String hasAtt) {
		this.hasAtt = hasAtt;
	}
	public String getPContent() {
		return pContent;
	}
	public void setPContent(String content) {
		pContent = content;
	}
	public String getPMessageId() {
		return pMessageId;
	}
	public void setPMessageId(String messageId) {
		pMessageId = messageId;
	}
	public String getPReceiver() {
		return pReceiver;
	}
	public void setPReceiver(String receiver) {
		pReceiver = receiver;
	}
	public Date getPSentDate() {
		return pSentDate;
	}
	public void setPSentDate(Date sentDate) {
		pSentDate = sentDate;
	}
	public String getPSenter() {
		return pSenter;
	}
	public void setPSenter(String senter) {
		pSenter = senter;
	}
	public String getPSize() {
		return pSize;
	}
	public void setPSize(String size) {
		pSize = size;
	}
	public String getPSubject() {
		if(pSubject==null||"".equals(this.pSubject)||"null".equals(pSubject)){
			return "无标题";
		}
		return pSubject;
	}
	public void setPSubject(String subject) {
		pSubject = subject;
	}
	public List<FileObj> getAttList() {
		return attList;
	}
	public void setAttList(List<FileObj> attList) {
		this.attList = attList;
	}

}
