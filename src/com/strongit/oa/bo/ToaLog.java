package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_LOG"
 *     
*/
@Entity
@Table(name="T_OA_LOG")
public class ToaLog implements Serializable {

	private static final long serialVersionUID = 6239483367191555595L;

	/** identifier field */
	@Id 
	@GeneratedValue
    private String logId;				//日志ID

    /** nullable persistent field */
    private String opeUser;				//操作人员名称

    /** nullable persistent field */
    private Date opeTime;				//操作时间

    /** nullable persistent field */
    private String opeIp;				//操作者IP地址

    /** nullable persistent field */
    private String logInfo;				//日志信息

    /** nullable persistent field */
    private String logState;			//日志状态  1：未删除  0：已删除

    /** nullable persistent field */
    private String logModule;			//日志所在模块

    private Date beginTime;
    /** full constructor */
    public ToaLog(String logId, String opeUser, Date opeTime, String opeIp, String logInfo, String logState, String logModule) {
        this.logId = logId;
        this.opeUser = opeUser;
        this.opeTime = opeTime;
        this.opeIp = opeIp;
        this.logInfo = logInfo;
        this.logState = logState;
        this.logModule = logModule;
    }

    /** default constructor */
    public ToaLog() {
    }

    /** minimal constructor */
    public ToaLog(String logId) {
        this.logId = logId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="LOG_ID"
     *         
     */
    @Id
	@Column(name="LOG_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getLogId() {
        return this.logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    /** 
     *            @hibernate.property
     *             column="OPE_USER"
     *             length="100"
     *         
     */
    @Column(name="OPE_USER",length=100)
    public String getOpeUser() {
        return this.opeUser;
    }

    public void setOpeUser(String opeUser) {
        this.opeUser = opeUser;
    }

    /** 
     *            @hibernate.property
     *             column="OPE_TIME"
     *             length="7"
     *         
     */
    @Column(name="OPE_TIME",length=7)
    public Date getOpeTime() {
        return this.opeTime;
    }

    public void setOpeTime(Date opeTime) {
        this.opeTime = opeTime;
    }

    /** 
     *            @hibernate.property
     *             column="OPE_IP"
     *             length="50"
     *         
     */
    @Column(name="OPE_IP",length=50)
    public String getOpeIp() {
        return this.opeIp;
    }

    public void setOpeIp(String opeIp) {
        this.opeIp = opeIp;
    }

    /** 
     *            @hibernate.property
     *             column="LOG_INFO"
     *             length="3000"
     *         
     */
    @Column(name="LOG_INFO",length=3000)
    public String getLogInfo() {
        return this.logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }

    /** 
     *            @hibernate.property
     *             column="LOG_STATE"
     *             length="1"
     *         
     */
    @Column(name="LOG_STATE",length=1)
    public String getLogState() {
        return this.logState;
    }

    public void setLogState(String logState) {
        this.logState = logState;
    }

    /** 
     *            @hibernate.property
     *             column="LOG_MODULE"
     *             length="32"
     *         
     */
    @Column(name="LOG_MODULE",length=32)
    public String getLogModule() {
        return this.logModule;
    }

    public void setLogModule(String logModule) {
        this.logModule = logModule;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("logId", getLogId())
            .toString();
    }

    @Transient
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

}
