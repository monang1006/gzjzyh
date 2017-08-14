package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_CALENDAR_REMIND"
 *     
*/
@Entity
@Table(name="T_OA_CALENDAR_REMIND")
public class ToaCalendarRemind implements Serializable {

    /** identifier field */
    private String remindId;

    /** 提醒方式 */
    private String remindMethod;
    public final static String REMIND_BY_MAIL = "mail";//用邮件提醒
    public final static String REMIND_BY_MSG = "msg";//用站内消息 
    public final static String REMIND_BY_RTX = "rtx";//用即时通讯 
    public final static String REMIND_BY_SMS = "sms";//用手机短信

    /** nullable persistent field */
    private Date remindTime;

    private String remindStatus;//是否已发提醒，0或者空是已发送
    
    /** nullable persistent field */
    private String remindCon;
    
    private String remindShare; //是否提醒共享人 0：只提醒自己；1：提醒自己和共享人；2：只提醒处理人；3:只提醒共享人

    private String remindTaskId;	//流程实例ID

    /** persistent field */
    private com.strongit.oa.bo.ToaCalendar toaCalendar;

    /** full constructor */
    public ToaCalendarRemind(String remindId, String remindMethod, Date remindTime, String remindCon, String remindShare, com.strongit.oa.bo.ToaCalendar toaCalendar,String remindStatus,String remindTaskId) {
        this.remindId = remindId;
        this.remindMethod = remindMethod;
        this.remindTime = remindTime;
        this.remindCon = remindCon;
        this.remindShare = remindShare;
        this.toaCalendar = toaCalendar;
        this.remindStatus = remindStatus;
        this.remindTaskId = remindTaskId;
    }
    /** 
     *            @hibernate.property
     *             column="REMIND_STATUS"
     *             length="1"
     *         
     */
    @Column(name="REMIND_STATUS",nullable=true)
    public String getRemindStatus() {
		return remindStatus;
	}

	public void setRemindStatus(String remindStatus) {
		this.remindStatus = remindStatus;
	}

	/** default constructor */
    public ToaCalendarRemind() {
    }

    /** minimal constructor */
    public ToaCalendarRemind(String remindId, com.strongit.oa.bo.ToaCalendar toaCalendar) {
        this.remindId = remindId;
        this.toaCalendar = toaCalendar;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="REMIND_ID"
     *         
     */
    @Id
	@Column(name="REMIND_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getRemindId() {
        return this.remindId;
    }

    public void setRemindId(String remindId) {
        this.remindId = remindId;
    }

    /** 
     *            @hibernate.property
     *             column="REMIND_METHOD"
     *             length="1"
     *         
     */
    @Column(name="REMIND_METHOD",nullable=true)
    public String getRemindMethod() {
        return this.remindMethod;
    }

    public void setRemindMethod(String remindMethod) {
        this.remindMethod = remindMethod;
    }

    /** 
     *            @hibernate.property
     *             column="REMIND_TIME"
     *             length="7"
     *         
     */
    @Column(name="REMIND_TIME",nullable=true)
    public Date getRemindTime() {
        return this.remindTime;
    }

    public void setRemindTime(Date remindTime) {
        this.remindTime = remindTime;
    }

    /** 
     *            @hibernate.property
     *             column="REMIND_CON"
     *             length="4000"
     *         
     */
    @Column(name="REMIND_CON",nullable=true)
    public String getRemindCon() {
        return this.remindCon;
    }

    public void setRemindCon(String remindCon) {
        this.remindCon = remindCon;
    }
    
    /** 
     *            @hibernate.property
     *             column="REMIND_SHARE"
     *             length="1"
     *         
     */
    @Column(name="REMIND_SHARE",nullable=true)
	public String getRemindShare() {
		return remindShare;
	}

	public void setRemindShare(String remindShare) {
		this.remindShare = remindShare;
	}

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="CALENDAR_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="CALENDAR_ID")
    public com.strongit.oa.bo.ToaCalendar getToaCalendar() {
        return this.toaCalendar;
    }

    public void setToaCalendar(com.strongit.oa.bo.ToaCalendar toaCalendar) {
        this.toaCalendar = toaCalendar;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("remindId", getRemindId())
            .toString();
    }
    

    /** 
     *            @hibernate.property
     *             column="REMIND_TASKID"
     *             length="1"
     *         
     */
    @Column(name="REMIND_TASKID",nullable=true)
	public String getRemindTaskId() {
		return remindTaskId;
	}
	public void setRemindTaskId(String remindTaskId) {
		this.remindTaskId = remindTaskId;
	}

}
