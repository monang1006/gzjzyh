package com.strongit.oa.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 	  考勤明细表
 *        @hibernate.class
 *         table="T_OA_ATTEND_RECORD"
 *     
*/
@Entity
@Table(name = "T_OA_ATTEND_RECORD", catalog = "", schema = "")
public class ToaAttendRecord implements Serializable {

    /** identifier field */
    private String attendId;

    /** nullable persistent field用户ID */
    private String userId;

    /** nullable persistent field 用户名 */
    private String userName;

    /** nullable persistent field 部门ID */
    private String orgId;

    /** nullable persistent field 部门名称*/
    private String orgName;

    /** nullable persistent field 班次ID */
    private String schedulesId;

    /** nullable persistent field 出勤日期*/
    private Date attendTime;

    /** nullable persistent field 规定上班时间*/
    private String mregulationTime;

    /** nullable persistent field 上班登记时间 */
    private Date mregisterTime;

    /** nullable persistent field 规定下班时间 */
    private String aregulationTime;

    /** nullable persistent field下班登记时间*/
    private Date aregisterTime;

    /** nullable persistent field 迟到时间（分）*/
    private Integer attendLaterTime;

    /** nullable persistent field 早退时间（分）*/
    private Integer attendEarlyTime;

    /** nullable persistent field 应出勤（天）*/
    private BigDecimal shouldAttendDay;

    /** nullable persistent field 缺勤时数*/
    private BigDecimal absenceHours;

    /** nullable persistent field 缺勤天数*/
    private BigDecimal absenceDays;
    
    /** nullable persistent field 类型ID*/
    private String attendanceTypeId;

    /** nullable persistent field 类型（事假、病假、婚假、旷工、加班、调休、出差等）*/
    private String attendanceType;

    /** nullable persistent field 是否为缺勤统计项 0:是  1：否*/
    private String isCalcuAbsence;

    /** nullable persistent field 是否已计算。0、‘’：没计算；1：已计算*/
    private String isCalcu;

    /** nullable persistent field 考勤描述*/
    private String attendDesc;

    /** nullable persistent field 应出勤（时）*/
    private BigDecimal shouldAttendHours;

    /** nullable persistent field 班次名称*/
    private String schedulesName;

    /** full constructor */
    public ToaAttendRecord(String attendId, String userId, String userName, String orgId, String orgName, String schedulesId, Date attendTime, String mregulationTime, Date mregisterTime, String aregulationTime, Date aregisterTime, Integer attendLaterTime, Integer attendEarlyTime, BigDecimal shouldAttendDay, BigDecimal absenceHours, BigDecimal absenceDays, String attendanceType, String isCalcuAbsence, String isCalcu, String attendDesc, BigDecimal shouldAttendHours, String schedulesName) {
        this.attendId = attendId;
        this.userId = userId;
        this.userName = userName;
        this.orgId = orgId;
        this.orgName = orgName;
        this.schedulesId = schedulesId;
        this.attendTime = attendTime;
        this.mregulationTime = mregulationTime;
        this.mregisterTime = mregisterTime;
        this.aregulationTime = aregulationTime;
        this.aregisterTime = aregisterTime;
        this.attendLaterTime = attendLaterTime;
        this.attendEarlyTime = attendEarlyTime;
        this.shouldAttendDay = shouldAttendDay;
        this.absenceHours = absenceHours;
        this.absenceDays = absenceDays;
        this.attendanceType = attendanceType;
        this.isCalcuAbsence = isCalcuAbsence;
        this.isCalcu = isCalcu;
        this.attendDesc = attendDesc;
        this.shouldAttendHours = shouldAttendHours;
        this.schedulesName = schedulesName;
    }

    /** default constructor */
    public ToaAttendRecord() {
    }

    /** minimal constructor */
    public ToaAttendRecord(String attendId) {
        this.attendId = attendId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="ATTEND_ID"
     *         
     */
    @Id
	@Column(name = "ATTEND_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getAttendId() {
        return this.attendId;
    }

    public void setAttendId(String attendId) {
        this.attendId = attendId;
    }

    /** 
     *            @hibernate.property
     *             column="USER_ID"
     *             length="32"
     *         
     */
    @Column(name = "USER_ID")
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /** 
     *            @hibernate.property
     *             column="USER_NAME"
     *             length="100"
     *         
     */
    @Column(name = "USER_NAME")
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_ID"
     *             length="32"
     *         
     */
    @Column(name = "ORG_ID")
    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_NAME"
     *             length="100"
     *         
     */
    @Column(name = "ORG_NAME")
    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /** 
     *            @hibernate.property
     *             column="SCHEDULES_ID"
     *             length="32"
     *         
     */
    @Column(name = "SCHEDULES_ID")
    public String getSchedulesId() {
        return this.schedulesId;
    }

    public void setSchedulesId(String schedulesId) {
        this.schedulesId = schedulesId;
    }

    /** 
     *            @hibernate.property
     *             column="ATTEND_TIME"
     *             length="7"
     *         
     */
    @Column(name = "ATTEND_TIME")
    public Date getAttendTime() {
        return this.attendTime;
    }

    public void setAttendTime(Date attendTime) {
        this.attendTime = attendTime;
    }

    /** 
     *            @hibernate.property
     *             column="M_REGULATION_TIME"
     *             length="10"
     *         
     */
    @Column(name = "M_REGULATION_TIME")
    public String getMregulationTime() {
        return this.mregulationTime;
    }

    public void setMregulationTime(String mregulationTime) {
        this.mregulationTime = mregulationTime;
    }

    /** 
     *            @hibernate.property
     *             column="M_REGISTER_TIME"
     *             length="7"
     *         
     */
    @Column(name = "M_REGISTER_TIME")
    public Date getMregisterTime() {
        return this.mregisterTime;
    }

    public void setMregisterTime(Date mregisterTime) {
        this.mregisterTime = mregisterTime;
    }

    /** 
     *            @hibernate.property
     *             column="A_REGULATION_TIME"
     *             length="10"
     *         
     */
    @Column(name = "A_REGULATION_TIME")
    public String getAregulationTime() {
        return this.aregulationTime;
    }

    public void setAregulationTime(String aregulationTime) {
        this.aregulationTime = aregulationTime;
    }

    /** 
     *            @hibernate.property
     *             column="A_REGISTER_TIME"
     *             length="7"
     *         
     */
    @Column(name = "A_REGISTER_TIME")
    public Date getAregisterTime() {
        return this.aregisterTime;
    }

    public void setAregisterTime(Date aregisterTime) {
        this.aregisterTime = aregisterTime;
    }

    /** 
     *            @hibernate.property
     *             column="ATTEND_LATER_TIME"
     *             length="4"
     *         
     */
    @Column(name = "ATTEND_LATER_TIME")
    public Integer getAttendLaterTime() {
        return this.attendLaterTime;
    }

    public void setAttendLaterTime(Integer attendLaterTime) {
        this.attendLaterTime = attendLaterTime;
    }

    /** 
     *            @hibernate.property
     *             column="ATTEND_EARLY_TIME"
     *             length="4"
     *         
     */
    @Column(name = "ATTEND_EARLY_TIME")
    public Integer getAttendEarlyTime() {
        return this.attendEarlyTime;
    }

    public void setAttendEarlyTime(Integer attendEarlyTime) {
        this.attendEarlyTime = attendEarlyTime;
    }

    /** 
     *            @hibernate.property
     *             column="SHOULD_ATTEND_DAY"
     *             length="4"
     *         
     */
    @Column(name = "SHOULD_ATTEND_DAY")
    public BigDecimal getShouldAttendDay() {
        return this.shouldAttendDay;
    }

    public void setShouldAttendDay(BigDecimal shouldAttendDay) {
        this.shouldAttendDay = shouldAttendDay;
    }

    /** 
     *            @hibernate.property
     *             column="ABSENCE_HOURS"
     *             length="4"
     *         
     */
    @Column(name = "ABSENCE_HOURS")
    public BigDecimal getAbsenceHours() {
        return this.absenceHours;
    }

    public void setAbsenceHours(BigDecimal absenceHours) {
        this.absenceHours = absenceHours;
    }

    /** 
     *            @hibernate.property
     *             column="ABSENCE_DAYS"
     *             length="4"
     *         
     */
    @Column(name = "ABSENCE_DAYS")
    public BigDecimal getAbsenceDays() {
        return this.absenceDays;
    }

    public void setAbsenceDays(BigDecimal absenceDays) {
        this.absenceDays = absenceDays;
    }

    /** 
     *            @hibernate.property
     *             column="ATTENDANCE_TYPE"
     *             length="32"
     *         
     */
    @Column(name = "ATTENDANCE_TYPE")
    public String getAttendanceType() {
        return this.attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    /** 
     *            @hibernate.property
     *             column="IS_CALCU_ABSENCE"
     *             length="1"
     *         
     */
    @Column(name = "IS_CALCU_ABSENCE")
    public String getIsCalcuAbsence() {
        return this.isCalcuAbsence;
    }

    public void setIsCalcuAbsence(String isCalcuAbsence) {
        this.isCalcuAbsence = isCalcuAbsence;
    }

    /** 
     *            @hibernate.property
     *             column="IS_CALCU"
     *             length="1"
     *         
     */
    @Column(name = "IS_CALCU")
    public String getIsCalcu() {
        return this.isCalcu;
    }

    public void setIsCalcu(String isCalcu) {
        this.isCalcu = isCalcu;
    }

    /** 
     *            @hibernate.property
     *             column="ATTEND_DESC"
     *             length="400"
     *         
     */
    @Column(name = "ATTEND_DESC")
    public String getAttendDesc() {
        return this.attendDesc;
    }

    public void setAttendDesc(String attendDesc) {
        this.attendDesc = attendDesc;
    }

    /** 
     *            @hibernate.property
     *             column="SHOULD_ATTEND_HOURS"
     *             length="4"
     *         
     */
    @Column(name = "SHOULD_ATTEND_HOURS")
    public BigDecimal getShouldAttendHours() {
        return this.shouldAttendHours;
    }

    public void setShouldAttendHours(BigDecimal shouldAttendHours) {
        this.shouldAttendHours = shouldAttendHours;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("attendId", getAttendId())
            .toString();
    }

    @Column(name = "ATTENDANCE_TYPE_ID")
	public String getAttendanceTypeId() {
		return attendanceTypeId;
	}

	public void setAttendanceTypeId(String attendanceTypeId) {
		this.attendanceTypeId = attendanceTypeId;
	}

}
