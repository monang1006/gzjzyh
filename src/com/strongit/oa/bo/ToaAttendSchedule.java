package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 	   班次表
 *        @hibernate.class
 *         table="T_OA_ATTEND_SCHEDULES"
 *     
*/
@Entity
@Table(name = "T_OA_ATTEND_SCHEDULES", catalog = "", schema = "")
public class ToaAttendSchedule implements Serializable {

    /** identifier field */
    private String schedulesId;

    /** nullable persistent field 班次名称*/
    private String schedulesName;

    /** nullable persistent field 上班时间*/
    private String workStime;

    /** nullable persistent field 上班登记有效时间*/
    private String registerStimeOn;

    /** nullable persistent field 上班登记失效时间*/
    private String registerEtimeOn;

    /** nullable persistent field 下班时间*/
    private String workEtime;

    /** nullable persistent field 下班登记有效时间*/
    private String registerStimeOff;

    /** nullable persistent field 下班登记失效时间*/
    private String registerEtimeOff;

    /** nullable persistent field 迟到时间*/
    private Integer laterTime;

    /** nullable persistent field 早退时间*/
    private Integer earlyTime;

    /** nullable persistent field 旷工时间*/
    private Integer skipTime;

    /** nullable persistent field 班次类型。已弃用*/
    private String isrest;
    
    /** nullable persistent field 操作日期*/
    private Date operateDate;
    
    /** nullable persistent field 班次是否跨天*/
    private String jumpDay;

    /** nullable persistent field 班次排序号*/
    private Integer schedulesOrder;
    
    /** nullable persistent field  班次类型。0：正常班；1：休息班*/
    private String schedulesType;

    /** persistent field 班组*/
    private com.strongit.oa.bo.ToaAttendSchedGroup toaAttendSchedGroup;

    /** persistent field 轮班规则*/
    private com.strongit.oa.bo.ToaAttendRegulation toaAttendRegulation;

    /** persistent field 轮班规则*/
    private Set toaAttendRegulations;

    /** full constructor */
    public ToaAttendSchedule(String schedulesId, String schedulesName, String workStime, String registerStimeOn, String registerEtimeOn, String workEtime, String registerStimeOff, String registerEtimeOff, Integer laterTime, Integer earlyTime, Integer skipTime, String isrest, Integer schedulesOrder, com.strongit.oa.bo.ToaAttendSchedGroup toaAttendSchedGroup, com.strongit.oa.bo.ToaAttendRegulation toaAttendRegulation, Set toaAttendRegulations) {
        this.schedulesId = schedulesId;
        this.schedulesName = schedulesName;
        this.workStime = workStime;
        this.registerStimeOn = registerStimeOn;
        this.registerEtimeOn = registerEtimeOn;
        this.workEtime = workEtime;
        this.registerStimeOff = registerStimeOff;
        this.registerEtimeOff = registerEtimeOff;
        this.laterTime = laterTime;
        this.earlyTime = earlyTime;
        this.skipTime = skipTime;
        this.isrest = isrest;
        this.schedulesOrder = schedulesOrder;
        this.toaAttendSchedGroup = toaAttendSchedGroup;
        this.toaAttendRegulation = toaAttendRegulation;
        this.toaAttendRegulations = toaAttendRegulations;
    }

    /** default constructor */
    public ToaAttendSchedule() {
    }

    /** minimal constructor */
    public ToaAttendSchedule(String schedulesId, com.strongit.oa.bo.ToaAttendSchedGroup toaAttendSchedGroup, com.strongit.oa.bo.ToaAttendRegulation toaAttendRegulation, Set toaAttendRegulations) {
        this.schedulesId = schedulesId;
        this.toaAttendSchedGroup = toaAttendSchedGroup;
        this.toaAttendRegulation = toaAttendRegulation;
        this.toaAttendRegulations = toaAttendRegulations;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="SCHEDULES_ID"
     *         
     */
    @Id
	@Column(name = "SCHEDULES_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getSchedulesId() {
        return this.schedulesId;
    }

    public void setSchedulesId(String schedulesId) {
        this.schedulesId = schedulesId;
    }

    /** 
     *            @hibernate.property
     *             column="SCHEDULES_NAME"
     *             length="50"
     *         
     */
    @Column(name = "SCHEDULES_NAME")
    public String getSchedulesName() {
        return this.schedulesName;
    }

    public void setSchedulesName(String schedulesName) {
        this.schedulesName = schedulesName;
    }

    /** 
     *            @hibernate.property
     *             column="WORK_STIME"
     *             length="10"
     *         
     */
    @Column(name = "WORK_STIME")
    public String getWorkStime() {
        return this.workStime;
    }

    public void setWorkStime(String workStime) {
        this.workStime = workStime;
    }

    /** 
     *            @hibernate.property
     *             column="REGISTER_STIME_ON"
     *             length="10"
     *         
     */
    @Column(name = "REGISTER_STIME_ON")
    public String getRegisterStimeOn() {
        return this.registerStimeOn;
    }

    public void setRegisterStimeOn(String registerStimeOn) {
        this.registerStimeOn = registerStimeOn;
    }

    /** 
     *            @hibernate.property
     *             column="REGISTER_ETIME_ON"
     *             length="10"
     *         
     */
    @Column(name = "REGISTER_ETIME_ON")
    public String getRegisterEtimeOn() {
        return this.registerEtimeOn;
    }

    public void setRegisterEtimeOn(String registerEtimeOn) {
        this.registerEtimeOn = registerEtimeOn;
    }

    /** 
     *            @hibernate.property
     *             column="WORK_ETIME"
     *             length="10"
     *         
     */
    @Column(name = "WORK_ETIME")
    public String getWorkEtime() {
        return this.workEtime;
    }

    public void setWorkEtime(String workEtime) {
        this.workEtime = workEtime;
    }

    /** 
     *            @hibernate.property
     *             column="REGISTER_STIME_OFF"
     *             length="10"
     *         
     */
    @Column(name = "REGISTER_STIME_OFF")
    public String getRegisterStimeOff() {
        return this.registerStimeOff;
    }

    public void setRegisterStimeOff(String registerStimeOff) {
        this.registerStimeOff = registerStimeOff;
    }

    /** 
     *            @hibernate.property
     *             column="REGISTER_ETIME_OFF"
     *             length="10"
     *         
     */
    @Column(name = "REGISTER_ETIME_OFF")
    public String getRegisterEtimeOff() {
        return this.registerEtimeOff;
    }

    public void setRegisterEtimeOff(String registerEtimeOff) {
        this.registerEtimeOff = registerEtimeOff;
    }

    /** 
     *            @hibernate.property
     *             column="LATER_TIME"
     *             length="4"
     *         
     */
    @Column(name = "LATER_TIME")
    public Integer getLaterTime() {
        return this.laterTime;
    }

    public void setLaterTime(Integer laterTime) {
        this.laterTime = laterTime;
    }

    /** 
     *            @hibernate.property
     *             column="EARLY_TIME"
     *             length="4"
     *         
     */
    @Column(name = "EARLY_TIME")
    public Integer getEarlyTime() {
        return this.earlyTime;
    }

    public void setEarlyTime(Integer earlyTime) {
        this.earlyTime = earlyTime;
    }

    /** 
     *            @hibernate.property
     *             column="SKIP_TIME"
     *             length="4"
     *         
     */
    @Column(name = "SKIP_TIME")
    public Integer getSkipTime() {
        return this.skipTime;
    }

    public void setSkipTime(Integer skipTime) {
        this.skipTime = skipTime;
    }

    /** 
     *            @hibernate.property
     *             column="ISREST"
     *             length="1"
     *         
     */
    @Column(name = "ISREST")
    public String getIsrest() {
        return this.isrest;
    }

    public void setIsrest(String isrest) {
        this.isrest = isrest;
    }

    /** 
     *            @hibernate.property
     *             column="SCHEDULES_ORDER"
     *             length="2"
     *         
     */
    @Column(name = "SCHEDULES_ORDER")
    public Integer getSchedulesOrder() {
        return this.schedulesOrder;
    }

    public void setSchedulesOrder(Integer schedulesOrder) {
        this.schedulesOrder = schedulesOrder;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="GROUP_ID"         
     *         
     */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUP_ID")
    public com.strongit.oa.bo.ToaAttendSchedGroup getToaAttendSchedGroup() {
        return this.toaAttendSchedGroup;
    }

    public void setToaAttendSchedGroup(com.strongit.oa.bo.ToaAttendSchedGroup toaAttendSchedGroup) {
        this.toaAttendSchedGroup = toaAttendSchedGroup;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="REG_ID"         
     *         
     */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REG_ID")
    public com.strongit.oa.bo.ToaAttendRegulation getToaAttendRegulation() {
        return this.toaAttendRegulation;
    }

    public void setToaAttendRegulation(com.strongit.oa.bo.ToaAttendRegulation toaAttendRegulation) {
        this.toaAttendRegulation = toaAttendRegulation;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="SCHEDULES_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaAttendRegulation"
     *         
     */
    @OneToMany(mappedBy="toaAttendSchedule",targetEntity=com.strongit.oa.bo.ToaAttendRegulation.class,cascade=CascadeType.ALL)
    public Set getToaAttendRegulations() {
        return this.toaAttendRegulations;
    }

    public void setToaAttendRegulations(Set toaAttendRegulations) {
        this.toaAttendRegulations = toaAttendRegulations;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("schedulesId", getSchedulesId())
            .toString();
    }

    @Column(name = "OPERATE_DATE")
	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

    @Column(name = "JUMP_DAY")
	public String getJumpDay() {
		return jumpDay;
	}

	public void setJumpDay(String jumpDay) {
		this.jumpDay = jumpDay;
	}

	@Column(name = "SCHEDULES_TYPE")
	public String getSchedulesType() {
		return schedulesType;
	}

	public void setSchedulesType(String schedulesType) {
		this.schedulesType = schedulesType;
	}

}
