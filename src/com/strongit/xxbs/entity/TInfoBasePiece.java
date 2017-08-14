package com.strongit.xxbs.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the T_INFO_BASE_PIECE database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_PIECE")
public class TInfoBasePiece implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="PIECE_ID")
	private String pieceId;

	@Column(name="ORG_ID")
	private String orgId;
	
	@Column(name="PARENT_ORG_ID")
	private String parentOrgId;

	@Column(name="PIECE_TITLE")
	private String pieceTitle;
	
	@Column(name="PIECE_TIME")
	private Date pieceTime;
	
	@Column(name="PIECE_CODE")
	private Integer pieceCode;
	
	//"1"代表批示。"2"代表呈批转 。 
	@Column(name="PIECE_OPEN")
	private Integer pieceOpen;
	
	
	//"1"代表呈国办。"2"代表呈阅件 。 "3"代表省级 。  "4"代表加分管理
	@Column(name="PIECE_FLAG")
	private String pieceFlag;
	
	//"0"已批示。"1"未批示。
	@Column(name="PIECE_IS_INSTRUCTION")
	private String isInstruction;

	public String getPieceId() {
		return pieceId;
	}

	public void setPieceId(String pieceId) {
		this.pieceId = pieceId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPieceTitle() {
		return pieceTitle;
	}

	public void setPieceTitle(String pieceTitle) {
		this.pieceTitle = pieceTitle;
	}

	public Date getPieceTime() {
		return pieceTime;
	}

	public void setPieceTime(Date pieceTime) {
		this.pieceTime = pieceTime;
	}

	public Integer getPieceCode() {
		return pieceCode;
	}

	public void setPieceCode(Integer pieceCode) {
		this.pieceCode = pieceCode;
	}

	public String getPieceFlag() {
		return pieceFlag;
	}

	public void setPieceFlag(String pieceFlag) {
		this.pieceFlag = pieceFlag;
	}

	public Integer getPieceOpen() {
		return pieceOpen;
	}

	public void setPieceOpen(Integer pieceOpen) {
		this.pieceOpen = pieceOpen;
	}

	public String getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public String getIsInstruction() {
		return isInstruction;
	}

	public void setIsInstruction(String isInstruction) {
		this.isInstruction = isInstruction;
	}

	
	
}