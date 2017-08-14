package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/** 
 *        @hibernate.class
 *         table="T_OA_SENDDOCREGIST"
 *     
*/
@Entity
@Table(name="T_OA_SENDDOCREGIST")
public class ToaSendDocRegist implements Serializable{

	@Id
	@GeneratedValue
	private String registId;
	
	private Date receiveTime;
	
	private String send;
	
	private String toRoom;
	
	private String docCode;
	
	private String docTitle;
	
	private String secret;
	
	private String toRoomName;
	
	private Date sendTime;
	
	/** full constructor */
	public ToaSendDocRegist (String registId, Date receiveTime, String send, String toRoom, String docCode, String docTitle, String secret, String toRoomName, Date sendTime){
		this.registId = registId;
		this.receiveTime = receiveTime;
		this.send = send;
		this.toRoom = toRoom;
		this.docCode = docCode;
		this.docTitle = docTitle;
		this.secret = secret;
		this.toRoomName = toRoomName;
		this.sendTime = sendTime;
	}
	
	/** default constructor */
	public ToaSendDocRegist(){
		
	}
	
	@Id
    @Column(name = "ID", nullable = false, length = 32)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getRegistId() {
		return registId;
	}

	public void setRegistId(String registId) {
		this.registId = registId;
	}

	@Column(name = "RECEIVE_TIME", nullable = true)
	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Column(name = "SEND", nullable = true)
	public String getSend() {
		return send;
	}

	public void setSend(String send) {
		this.send = send;
	}

	@Column(name = "TO_ROOM", nullable = true)
	public String getToRoom() {
		return toRoom;
	}

	public void setToRoom(String toRoom) {
		this.toRoom = toRoom;
	}

	@Column(name = "DOC_CODE", nullable = true)
	public String getDocCode() {
		return docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	@Column(name = "DOC_TITLE", nullable = true)
	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	@Column(name = "SECRET", nullable = true)
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	@Column(name = "TO_ROOMNAME", nullable = true)
	public String getToRoomName() {
		return toRoomName;
	}

	public void setToRoomName(String toRoomName) {
		this.toRoomName = toRoomName;
	}
	
	@Column(name = "SEND_TIME", nullable = true)
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
}
