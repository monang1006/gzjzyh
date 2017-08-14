package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_AttendanceTime"
 *     
*/
@Entity
@Table(name="T_OA_AttendanceTime")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ToaAttendanceTime implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	@Id 
	@GeneratedValue
    private String id;

    private Date time;

    private String worktime;

    private String leavetime;

 

	/** full constructor */
    public ToaAttendanceTime(Date time,String worktime,String leavetime){
    	this.time=time;
    	this.worktime=worktime;
    	this.leavetime=leavetime;
    }

    /** default constructor */
    public ToaAttendanceTime() {
    }


    @Id
	@Column(name="ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="TIME",nullable=true)
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name="WORKTIME",nullable=true)
	public String getWorktime() {
		return worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}

	@Column(name="LEAVETIME",nullable=true)
	public String getLeavetime() {
		return leavetime;
	}

	public void setLeavetime(String leavetime) {
		this.leavetime = leavetime;
	}

    

}
