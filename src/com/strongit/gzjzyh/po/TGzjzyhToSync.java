package com.strongit.gzjzyh.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.strongit.gzjzyh.GzjzyhApplicationConfig;

@Entity
@Table(name = "T_GZJZYH_TO_SYNC")
public class TGzjzyhToSync implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	private String tsId;
	private String tsToSyncMsgPath;
	private Date tsToSyncTime;
	private String tsToSyncFlag = GzjzyhApplicationConfig.getSyncflag();
	
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_TOSYNC_TIME")
	public Date getTsToSyncTime() {
		return tsToSyncTime;
	}

	public void setTsToSyncTime(Date tsToSyncTime) {
		this.tsToSyncTime = tsToSyncTime;
	}

	@Column(name = "TS_TOSYNC_FLAG")
	public String getTsToSyncFlag() {
		return tsToSyncFlag;
	}

	public void setTsToSyncFlag(String tsToSyncFlag) {
		this.tsToSyncFlag = tsToSyncFlag;
	}

	@Column(name = "TS_TOSYNC_MSGPATH")
	public String getTsToSyncMsgPath() {
		return tsToSyncMsgPath;
	}

	public void setTsToSyncMsgPath(String tsToSyncMsgPath) {
		this.tsToSyncMsgPath = tsToSyncMsgPath;
	}

}
