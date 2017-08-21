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
@Table(name = "T_GZJZYH_APPLICATION")
public class TGzjzyhToHandle implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String thId;
	private String thToHandleMsg;
	private Date thToHandleTime;
	
	public TGzjzyhToHandle(){
		
	}

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "TH_ID")
	public String getThId() {
		return thId;
	}

	public void setThId(String thId) {
		this.thId = thId;
	}

	@Column(name = "TH_TOHANDLE_MSG")
	public String getThToHandleMsg() {
		return thToHandleMsg;
	}

	public void setThToHandleMsg(String thToHandleMsg) {
		this.thToHandleMsg = thToHandleMsg;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TH_TOHANDLE_TIME")
	public Date getThToHandleTime() {
		return thToHandleTime;
	}

	public void setThToHandleTime(Date thToHandleTime) {
		this.thToHandleTime = thToHandleTime;
	}

}
