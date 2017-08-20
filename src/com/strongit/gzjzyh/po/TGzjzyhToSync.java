package com.strongit.gzjzyh.po;

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
@Table(name = "T_GZJZYH_TO_SYNC")
public class TGzjzyhToSync implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	private String tsId;
	private String tsToSyncMsg;
	private Date tsToSyncTime;
	
	public TGzjzyhToSync(){
		
	}

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "TS_ID")
	public String getTsId() {
		return tsId;
	}

	public void setTsId(String tsId) {
		this.tsId = tsId;
	}

	@Column(name = "TS_TOSYNC_MSG")
	public String getTsToSyncMsg() {
		return tsToSyncMsg;
	}

	public void setTsToSyncMsg(String tsToSyncMsg) {
		this.tsToSyncMsg = tsToSyncMsg;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_TOSYNC_TIME")
	public Date getTsToSyncTime() {
		return tsToSyncTime;
	}

	public void setTsToSyncTime(Date tsToSyncTime) {
		this.tsToSyncTime = tsToSyncTime;
	}

}
