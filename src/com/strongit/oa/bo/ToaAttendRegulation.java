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


/** 	  轮班规则表
 *        @hibernate.class
 *         table="T_OA_ATTEND_REGULATION"
 *     
*/
@Entity
@Table(name = "T_OA_ATTEND_REGULATION", catalog = "", schema = "")
public class ToaAttendRegulation implements Serializable {

    /** identifier field 规则ID*/
    private String regId;

    /** nullable persistent field 规则描述*/
    private String regDesc;

    /** nullable persistent field 规则重复范围方式。0：按时间段；1：按年*/
    private String rangeWay;

    /** nullable persistent field 规则开始时间*/
    private Date regStime;

    /** nullable persistent field 规则结束时间*/
    private Date regEtime;

    /** nullable persistent field 规则开始月份*/
    private Integer regSmonth;

    /** nullable persistent field 规则结束月份*/
    private Integer regEmonth;

    /** nullable persistent field 规则重复方式。0：按日；1：按周；2：按月*/
    private String repeatWay;

    /** nullable persistent field 轮班开始时间*/
    private Date repeatStime;

    /** nullable persistent field 轮班周期*/
    private Integer repeatCycle;

    /** nullable persistent field 连续天数*/
    private Integer contiguoousDays;

    /** nullable persistent field 按周时，存放星期*/
    private String weekDays;

    /** nullable persistent field 按月时，存放日*/
    private String monthDays;

    /** nullable persistent field 是否包含一个月的最后一天。0,''：不包含；1：包含*/
    private String isContainLastDay;

    /** persistent field 班次*/
    private com.strongit.oa.bo.ToaAttendSchedule toaAttendSchedule;

    /** persistent field 班次*/
    private Set toaAttendSchedules;

    /** full constructor */
    public ToaAttendRegulation(String regId, String regDesc, String rangeWay, Date regStime, Date regEtime, Integer regSmonth, Integer regEmonth, String repeatWay, Date repeatStime, Integer repeatCycle, Integer contiguoousDays, String weekDays, String monthDays, String isContainLastDay, com.strongit.oa.bo.ToaAttendSchedule toaAttendSchedule, Set toaAttendSchedules) {
        this.regId = regId;
        this.regDesc = regDesc;
        this.rangeWay = rangeWay;
        this.regStime = regStime;
        this.regEtime = regEtime;
        this.regSmonth = regSmonth;
        this.regEmonth = regEmonth;
        this.repeatWay = repeatWay;
        this.repeatStime = repeatStime;
        this.repeatCycle = repeatCycle;
        this.contiguoousDays = contiguoousDays;
        this.weekDays = weekDays;
        this.monthDays = monthDays;
        this.isContainLastDay = isContainLastDay;
        this.toaAttendSchedule = toaAttendSchedule;
        this.toaAttendSchedules = toaAttendSchedules;
    }

    /** default constructor */
    public ToaAttendRegulation() {
    }

    /** minimal constructor */
    public ToaAttendRegulation(String regId, com.strongit.oa.bo.ToaAttendSchedule toaAttendSchedule, Set toaAttendSchedules) {
        this.regId = regId;
        this.toaAttendSchedule = toaAttendSchedule;
        this.toaAttendSchedules = toaAttendSchedules;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="REG_ID"
     *         
     */
    @Id
	@Column(name = "REG_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getRegId() {
        return this.regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    /** 
     *            @hibernate.property
     *             column="REG_DESC"
     *             length="100"
     *         
     */
    @Column(name = "REG_DESC")
    public String getRegDesc() {
        return this.regDesc;
    }

    public void setRegDesc(String regDesc) {
        this.regDesc = regDesc;
    }

    /** 
     *            @hibernate.property
     *             column="RANGE_WAY"
     *             length="1"
     *         
     */
    @Column(name = "RANGE_WAY")
    public String getRangeWay() {
        return this.rangeWay;
    }

    public void setRangeWay(String rangeWay) {
        this.rangeWay = rangeWay;
    }

    /** 
     *            @hibernate.property
     *             column="REG_STIME"
     *             length="7"
     *         
     */
    @Column(name = "REG_STIME")
    public Date getRegStime() {
        return this.regStime;
    }

    public void setRegStime(Date regStime) {
        this.regStime = regStime;
    }

    /** 
     *            @hibernate.property
     *             column="REG_ETIME"
     *             length="7"
     *         
     */
    @Column(name = "REG_ETIME")
    public Date getRegEtime() {
        return this.regEtime;
    }

    public void setRegEtime(Date regEtime) {
        this.regEtime = regEtime;
    }

    /** 
     *            @hibernate.property
     *             column="REG_SMONTH"
     *             length="2"
     *         
     */
    @Column(name = "REG_SMONTH")
    public Integer getRegSmonth() {
        return this.regSmonth;
    }

    public void setRegSmonth(Integer regSmonth) {
        this.regSmonth = regSmonth;
    }

    /** 
     *            @hibernate.property
     *             column="REG_EMONTH"
     *             length="2"
     *         
     */
    @Column(name = "REG_EMONTH")
    public Integer getRegEmonth() {
        return this.regEmonth;
    }

    public void setRegEmonth(Integer regEmonth) {
        this.regEmonth = regEmonth;
    }

    /** 
     *            @hibernate.property
     *             column="REPEAT_WAY"
     *             length="1"
     *         
     */
    @Column(name = "REPEAT_WAY")
    public String getRepeatWay() {
        return this.repeatWay;
    }

    public void setRepeatWay(String repeatWay) {
        this.repeatWay = repeatWay;
    }

    /** 
     *            @hibernate.property
     *             column="REPEAT_STIME"
     *             length="7"
     *         
     */
    @Column(name = "REPEAT_STIME")
    public Date getRepeatStime() {
        return this.repeatStime;
    }

    public void setRepeatStime(Date repeatStime) {
        this.repeatStime = repeatStime;
    }

    /** 
     *            @hibernate.property
     *             column="REPEAT_CYCLE"
     *             length="4"
     *         
     */
    @Column(name = "REPEAT_CYCLE")
    public Integer getRepeatCycle() {
        return this.repeatCycle;
    }

    public void setRepeatCycle(Integer repeatCycle) {
        this.repeatCycle = repeatCycle;
    }

    /** 
     *            @hibernate.property
     *             column="CONTIGUOOUS_DAYS"
     *             length="2"
     *         
     */
    @Column(name = "CONTIGUOOUS_DAYS")
    public Integer getContiguoousDays() {
        return this.contiguoousDays;
    }

    public void setContiguoousDays(Integer contiguoousDays) {
        this.contiguoousDays = contiguoousDays;
    }

    /** 
     *            @hibernate.property
     *             column="WEEK_DAYS"
     *             length="20"
     *         
     */
    @Column(name = "WEEK_DAYS")
    public String getWeekDays() {
        return this.weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    /** 
     *            @hibernate.property
     *             column="MONTH_DAYS"
     *             length="100"
     *         
     */
    @Column(name = "MONTH_DAYS")
    public String getMonthDays() {
        return this.monthDays;
    }

    public void setMonthDays(String monthDays) {
        this.monthDays = monthDays;
    }

    /** 
     *            @hibernate.property
     *             column="IS_CONTAIN_LAST_DAY"
     *             length="1"
     *         
     */
    @Column(name = "IS_CONTAIN_LAST_DAY")
    public String getIsContainLastDay() {
        return this.isContainLastDay;
    }

    public void setIsContainLastDay(String isContainLastDay) {
        this.isContainLastDay = isContainLastDay;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="SCHEDULES_ID"         
     *         
     */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCHEDULES_ID")
    public com.strongit.oa.bo.ToaAttendSchedule getToaAttendSchedule() {
        return this.toaAttendSchedule;
    }

    public void setToaAttendSchedule(com.strongit.oa.bo.ToaAttendSchedule toaAttendSchedule) {
        this.toaAttendSchedule = toaAttendSchedule;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="REG_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaAttendSchedule"
     *         
     */
    @OneToMany(mappedBy="toaAttendRegulation",targetEntity=com.strongit.oa.bo.ToaAttendSchedule.class)
    public Set getToaAttendSchedules() {
        return this.toaAttendSchedules;
    }

    public void setToaAttendSchedules(Set toaAttendSchedules) {
        this.toaAttendSchedules = toaAttendSchedules;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("regId", getRegId())
            .toString();
    }

}
