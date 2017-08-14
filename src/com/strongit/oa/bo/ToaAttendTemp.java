package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 考勤登记临时bo
 *     
*/
public class ToaAttendTemp implements Serializable {

	private String id;
	
	private String scheduleId;
	
	private String userId;
	
	private String sequencecode;
	
	private Date attendDate;
	
	private String attendType;
	
	private String registerStime;
	
	private String registerEtime;
	
	private String regulationTime;
	
	private Date registerTime;
	
    public ToaAttendTemp(String id, String scheduleId, String userId, String sequencecode, Date attendDate, String attendType, String registerStime, String registerEtime, String regulationTime, Date registerTime) {
		super();
		this.id = id;
		this.scheduleId = scheduleId;
		this.userId = userId;
		this.sequencecode = sequencecode;
		this.attendDate = attendDate;
		this.attendType = attendType;
		this.registerStime = registerStime;
		this.registerEtime = registerEtime;
		this.regulationTime = regulationTime;
		this.registerTime = registerTime;
	}

	public ToaAttendTemp() {
    	
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }


	public Date getAttendDate() {
		return attendDate;
	}


	public void setAttendDate(Date attendDate) {
		this.attendDate = attendDate;
	}


	public String getAttendType() {
		return attendType;
	}


	public void setAttendType(String attendType) {
		this.attendType = attendType;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getRegisterEtime() {
		return registerEtime;
	}


	public void setRegisterEtime(String registerEtime) {
		this.registerEtime = registerEtime;
	}


	public String getRegisterStime() {
		return registerStime;
	}


	public void setRegisterStime(String registerStime) {
		this.registerStime = registerStime;
	}


	public String getRegulationTime() {
		return regulationTime;
	}


	public void setRegulationTime(String regulationTime) {
		this.regulationTime = regulationTime;
	}


	public String getScheduleId() {
		return scheduleId;
	}


	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}


	public String getSequencecode() {
		return sequencecode;
	}


	public void setSequencecode(String sequencecode) {
		this.sequencecode = sequencecode;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public Date getRegisterTime() {
		return registerTime;
	}


	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
}
