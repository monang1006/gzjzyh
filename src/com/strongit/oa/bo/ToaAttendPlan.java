package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 执行计划参数配置表
 *        @hibernate.class
 *         table="T_OA_ATTEND_PLAN"
 *     
*/
@Entity
@Table(name = "T_OA_ATTEND_PLAN", catalog = "", schema = "")
public class ToaAttendPlan implements Serializable {

    /** identifier field */
    private String planId;

    /** nullable persistent field */
    private String planFrequency;//执行频度。0：不执行；1：每月

    /** nullable persistent field */
    private String isAuto;//是否自动汇总考勤。0，‘’：不自动汇总；1：自动汇总

    /** nullable persistent field */
    private Integer intervalTime;//自动计算考勤后第几天自动汇总考勤

    /** nullable persistent field */
    private Date planStime;//计划开始时间

    /** nullable persistent field */
    private Date planEtime;//计划结束时间

    /** nullable persistent field */
    private String planTime;//计划执行时间点

    /** nullable persistent field */
    private Date planLtime;//最后一次自动计算时间

    /** full constructor */
    public ToaAttendPlan(String planId, String planFrequency, String isAuto, Integer intervalTime, Date planStime, Date planEtime, String planTime, Date planLtime) {
        this.planId = planId;
        this.planFrequency = planFrequency;
        this.isAuto = isAuto;
        this.intervalTime = intervalTime;
        this.planStime = planStime;
        this.planEtime = planEtime;
        this.planTime = planTime;
        this.planLtime = planLtime;
    }

    /** default constructor */
    public ToaAttendPlan() {
    }

    /** minimal constructor */
    public ToaAttendPlan(String planId) {
        this.planId = planId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="PLAN_ID"
     *         
     */
    @Id
	@Column(name = "PLAN_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getPlanId() {
        return this.planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    /** 
     *            @hibernate.property
     *             column="PLAN_FREQUENCY"
     *             length="1"
     *         
     */
    @Column(name = "PLAN_FREQUENCY")
    public String getPlanFrequency() {
        return this.planFrequency;
    }

    public void setPlanFrequency(String planFrequency) {
        this.planFrequency = planFrequency;
    }

    /** 
     *            @hibernate.property
     *             column="IS_AUTO"
     *             length="1"
     *         
     */
    @Column(name = "IS_AUTO")
    public String getIsAuto() {
        return this.isAuto;
    }

    public void setIsAuto(String isAuto) {
        this.isAuto = isAuto;
    }

    /** 
     *            @hibernate.property
     *             column="INTERVAL_TIME"
     *             length="2"
     *         
     */
    @Column(name = "INTERVAL_TIME")
    public Integer getIntervalTime() {
        return this.intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    /** 
     *            @hibernate.property
     *             column="PLAN_STIME"
     *             length="7"
     *         
     */
    @Column(name = "PLAN_STIME")
    public Date getPlanStime() {
        return this.planStime;
    }

    public void setPlanStime(Date planStime) {
        this.planStime = planStime;
    }

    /** 
     *            @hibernate.property
     *             column="PLAN_ETIME"
     *             length="7"
     *         
     */
    @Column(name = "PLAN_ETIME")
    public Date getPlanEtime() {
        return this.planEtime;
    }

    public void setPlanEtime(Date planEtime) {
        this.planEtime = planEtime;
    }

    /** 
     *            @hibernate.property
     *             column="PLAN_TIME"
     *             length="10"
     *         
     */
    @Column(name = "PLAN_TIME")
    public String getPlanTime() {
        return this.planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    /** 
     *            @hibernate.property
     *             column="PLAN_LTIME"
     *             length="7"
     *         
     */
    @Column(name = "PLAN_LTIME")
    public Date getPlanLtime() {
        return this.planLtime;
    }

    public void setPlanLtime(Date planLtime) {
        this.planLtime = planLtime;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("planId", getPlanId())
            .toString();
    }

}
