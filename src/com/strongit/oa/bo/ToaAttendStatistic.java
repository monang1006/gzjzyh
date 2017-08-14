package com.strongit.oa.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        考勤汇总BO
 *        @hibernate.class
 *         table="T_OA_ATTEND_STATISTICS"
 *     
*/
public class ToaAttendStatistic implements Serializable {

    /** identifier field 汇总ID*/
    private String statisticsId;

    /** nullable persistent field  部门ID*/
    private String orgId;

    /** nullable persistent field 部门名称*/
    private String orgName;

    /** nullable persistent field 用户ID*/
    private String userId;

    /** nullable persistent field 用户名称*/
    private String userName;

    /** nullable persistent field 汇总开始时间*/
    private Date statisticsStime;

    /** nullable persistent field 汇总结束时间*/
    private Date statisticsEtime;

    /** nullable persistent field  汇总日期*/
    private Date statisticsTime;

    /** nullable persistent field 应出勤时数*/
    private BigDecimal shouldAttendDays;

    /** nullable persistent field 实际出勤*/
    private BigDecimal actualAttendDays;

    /** nullable persistent field  迟到次数*/
    private Integer laterTimes;

    /** nullable persistent field 迟到时间*/
    private Integer laterSumTime;

    /** nullable persistent field 早退次数*/
    private Integer earlyTimes;

    /** nullable persistent field 早退时间*/
    private Integer earlySumTime;

    /** nullable persistent field  加班时数*/
    private BigDecimal overworkTime;

    /** nullable persistent field 缺勤时数*/
    private BigDecimal absenceTime;

    /** nullable persistent field 矿工*/
    private BigDecimal skipSumTime;

    /** nullable persistent field 出差*/
    private BigDecimal errandTime;

    /** nullable persistent field 外出*/
    private BigDecimal outTime;

    /** nullable persistent field 调休*/
    private BigDecimal restTime;

    /** nullable persistent field 事假*/
    private BigDecimal affairTime;

    /** nullable persistent field 病假*/
    private BigDecimal illTime;

    /** nullable persistent field 工伤假*/
    private BigDecimal injureTime;

    /** nullable persistent field 丧假*/
    private BigDecimal funeralLeave;

    /** nullable persistent field 年假*/
    private BigDecimal annualLeave;

    /** nullable persistent field 婚假*/
    private BigDecimal weddingLeave;

    /** nullable persistent field 产假*/
    private BigDecimal matemityLeave;

    /** nullable persistent field  审核状态*/
    private String autiState;

    /** full constructor */
    public ToaAttendStatistic(String statisticsId, String orgId, String orgName, String userId, String userName, Date statisticsStime, Date statisticsEtime, Date statisticsTime, BigDecimal shouldAttendDays, BigDecimal actualAttendDays, Integer laterTimes, Integer laterSumTime, Integer earlyTimes, Integer earlySumTime, BigDecimal overworkTime, BigDecimal absenceTime, BigDecimal skipSumTime, BigDecimal errandTime, BigDecimal outTime, BigDecimal restTime, BigDecimal affairTime, BigDecimal illTime, BigDecimal injureTime, BigDecimal funeralLeave, BigDecimal annualLeave, BigDecimal weddingLeave, BigDecimal matemityLeave, String autiState) {
        this.statisticsId = statisticsId;
        this.orgId = orgId;
        this.orgName = orgName;
        this.userId = userId;
        this.userName = userName;
        this.statisticsStime = statisticsStime;
        this.statisticsEtime = statisticsEtime;
        this.statisticsTime = statisticsTime;
        this.shouldAttendDays = shouldAttendDays;
        this.actualAttendDays = actualAttendDays;
        this.laterTimes = laterTimes;
        this.laterSumTime = laterSumTime;
        this.earlyTimes = earlyTimes;
        this.earlySumTime = earlySumTime;
        this.overworkTime = overworkTime;
        this.absenceTime = absenceTime;
        this.skipSumTime = skipSumTime;
        this.errandTime = errandTime;
        this.outTime = outTime;
        this.restTime = restTime;
        this.affairTime = affairTime;
        this.illTime = illTime;
        this.injureTime = injureTime;
        this.funeralLeave = funeralLeave;
        this.annualLeave = annualLeave;
        this.weddingLeave = weddingLeave;
        this.matemityLeave = matemityLeave;
        this.autiState = autiState;
    }

    /** default constructor */
    public ToaAttendStatistic() {
    }

    /** minimal constructor */
    public ToaAttendStatistic(String statisticsId) {
        this.statisticsId = statisticsId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="STATISTICS_ID"
     *         
     */
    @Id
	@Column(name="STATISTICS_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getStatisticsId() {
        return this.statisticsId;
    }

    public void setStatisticsId(String statisticsId) {
        this.statisticsId = statisticsId;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_ID"
     *             length="32"
     *         
     */
    @Column(name="ORG_ID",nullable=true)
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
    @Column(name="ORG_NAME",nullable=true)
    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /** 
     *            @hibernate.property
     *             column="USER_ID"
     *             length="32"
     *         
     */
    @Column(name="USER_ID",nullable=true)
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
    @Column(name="USER_NAME",nullable=true)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** 
     *            @hibernate.property
     *             column="STATISTICS_STIME"
     *             length="7"
     *         
     */
    @Column(name="STATISTICS_STIME",nullable=true)
    public Date getStatisticsStime() {
        return this.statisticsStime;
    }

    public void setStatisticsStime(Date statisticsStime) {
        this.statisticsStime = statisticsStime;
    }

    /** 
     *            @hibernate.property
     *             column="STATISTICS_ETIME"
     *             length="7"
     *         
     */
    @Column(name="STATISTICS_ETIME",nullable=true)
    public Date getStatisticsEtime() {
        return this.statisticsEtime;
    }

    public void setStatisticsEtime(Date statisticsEtime) {
        this.statisticsEtime = statisticsEtime;
    }

    /** 
     *            @hibernate.property
     *             column="STATISTICS_TIME"
     *             length="7"
     *         
     */
    @Column(name="STATISTICS_TIME",nullable=true)
    public Date getStatisticsTime() {
        return this.statisticsTime;
    }

    public void setStatisticsTime(Date statisticsTime) {
        this.statisticsTime = statisticsTime;
    }

    /** 
     *            @hibernate.property
     *             column="SHOULD_ATTEND_DAYS"
     *             length="8"
     *         
     */
    @Column(name="SHOULD_ATTEND_DAYS",nullable=true)
    public BigDecimal getShouldAttendDays() {
        return this.shouldAttendDays;
    }

    public void setShouldAttendDays(BigDecimal shouldAttendDays) {
        this.shouldAttendDays = shouldAttendDays;
    }

    /** 
     *            @hibernate.property
     *             column="ACTUAL_ATTEND_DAYS"
     *             length="8"
     *         
     */
    @Column(name="ACTUAL_ATTEND_DAYS",nullable=true)
    public BigDecimal getActualAttendDays() {
        return this.actualAttendDays;
    }

    public void setActualAttendDays(BigDecimal actualAttendDays) {
        this.actualAttendDays = actualAttendDays;
    }

    /** 
     *            @hibernate.property
     *             column="LATER_TIMES"
     *             length="4"
     *         
     */
    @Column(name="LATER_TIMES",nullable=true)
    public Integer getLaterTimes() {
        return this.laterTimes;
    }

    public void setLaterTimes(Integer laterTimes) {
        this.laterTimes = laterTimes;
    }

    /** 
     *            @hibernate.property
     *             column="LATER_SUM_TIME"
     *             length="8"
     *         
     */
    @Column(name="LATER_SUM_TIME",nullable=true)
    public Integer getLaterSumTime() {
        return this.laterSumTime;
    }

    public void setLaterSumTime(Integer laterSumTime) {
        this.laterSumTime = laterSumTime;
    }

    /** 
     *            @hibernate.property
     *             column="EARLY_TIMES"
     *             length="4"
     *         
     */
    @Column(name="EARLY_TIMES",nullable=true)
    public Integer getEarlyTimes() {
        return this.earlyTimes;
    }

    public void setEarlyTimes(Integer earlyTimes) {
        this.earlyTimes = earlyTimes;
    }

    /** 
     *            @hibernate.property
     *             column="EARLY_SUM_TIME"
     *             length="8"
     *         
     */
    @Column(name="EARLY_SUM_TIME",nullable=true)
    public Integer getEarlySumTime() {
        return this.earlySumTime;
    }

    public void setEarlySumTime(Integer earlySumTime) {
        this.earlySumTime = earlySumTime;
    }

    /** 
     *            @hibernate.property
     *             column="OVERWORK_TIME"
     *             length="8"
     *         
     */
    @Column(name="OVERWORK_TIME",nullable=true)
    public BigDecimal getOverworkTime() {
        return this.overworkTime;
    }

    public void setOverworkTime(BigDecimal overworkTime) {
        this.overworkTime = overworkTime;
    }

    /** 
     *            @hibernate.property
     *             column="ABSENCE_TIME"
     *             length="8"
     *         
     */
    @Column(name="ABSENCE_TIME",nullable=true)
    public BigDecimal getAbsenceTime() {
        return this.absenceTime;
    }

    public void setAbsenceTime(BigDecimal absenceTime) {
        this.absenceTime = absenceTime;
    }

    /** 
     *            @hibernate.property
     *             column="SKIP_SUM_TIME"
     *             length="8"
     *         
     */
    @Column(name="SKIP_SUM_TIME",nullable=true)
    public BigDecimal getSkipSumTime() {
        return this.skipSumTime;
    }

    public void setSkipSumTime(BigDecimal skipSumTime) {
        this.skipSumTime = skipSumTime;
    }

    /** 
     *            @hibernate.property
     *             column="ERRAND_TIME"
     *             length="8"
     *         
     */
    @Column(name="ERRAND_TIME",nullable=true)
    public BigDecimal getErrandTime() {
        return this.errandTime;
    }

    public void setErrandTime(BigDecimal errandTime) {
        this.errandTime = errandTime;
    }

    /** 
     *            @hibernate.property
     *             column="OUT_TIME"
     *             length="8"
     *         
     */
    @Column(name="OUT_TIME",nullable=true)
    public BigDecimal getOutTime() {
        return this.outTime;
    }

    public void setOutTime(BigDecimal outTime) {
        this.outTime = outTime;
    }

    /** 
     *            @hibernate.property
     *             column="REST_TIME"
     *             length="8"
     *         
     */
    @Column(name="REST_TIME",nullable=true)
    public BigDecimal getRestTime() {
        return this.restTime;
    }

    public void setRestTime(BigDecimal restTime) {
        this.restTime = restTime;
    }

    /** 
     *            @hibernate.property
     *             column="AFFAIR_TIME"
     *             length="8"
     *         
     */
    @Column(name="AFFAIR_TIME",nullable=true)
    public BigDecimal getAffairTime() {
        return this.affairTime;
    }

    public void setAffairTime(BigDecimal affairTime) {
        this.affairTime = affairTime;
    }

    /** 
     *            @hibernate.property
     *             column="ILL_TIME"
     *             length="8"
     *         
     */
    @Column(name="ILL_TIME",nullable=true)
    public BigDecimal getIllTime() {
        return this.illTime;
    }

    public void setIllTime(BigDecimal illTime) {
        this.illTime = illTime;
    }

    /** 
     *            @hibernate.property
     *             column="INJURE_TIME"
     *             length="8"
     *         
     */
    @Column(name="INJURE_TIME",nullable=true)
    public BigDecimal getInjureTime() {
        return this.injureTime;
    }

    public void setInjureTime(BigDecimal injureTime) {
        this.injureTime = injureTime;
    }

    /** 
     *            @hibernate.property
     *             column="FUNERAL_LEAVE"
     *             length="8"
     *         
     */
    @Column(name="FUNERAL_LEAVE",nullable=true)
    public BigDecimal getFuneralLeave() {
        return this.funeralLeave;
    }

    public void setFuneralLeave(BigDecimal funeralLeave) {
        this.funeralLeave = funeralLeave;
    }

    /** 
     *            @hibernate.property
     *             column="ANNUAL_LEAVE"
     *             length="8"
     *         
     */
    @Column(name="ANNUAL_LEAVE",nullable=true)
    public BigDecimal getAnnualLeave() {
        return this.annualLeave;
    }

    public void setAnnualLeave(BigDecimal annualLeave) {
        this.annualLeave = annualLeave;
    }

    /** 
     *            @hibernate.property
     *             column="WEDDING_LEAVE"
     *             length="8"
     *         
     */
    @Column(name="WEDDING_LEAVE",nullable=true)
    public BigDecimal getWeddingLeave() {
        return this.weddingLeave;
    }

    public void setWeddingLeave(BigDecimal weddingLeave) {
        this.weddingLeave = weddingLeave;
    }

    /** 
     *            @hibernate.property
     *             column="MATEMITY_LEAVE"
     *             length="8"
     *         
     */
    @Column(name="MATEMITY_LEAVE",nullable=true)
    public BigDecimal getMatemityLeave() {
        return this.matemityLeave;
    }

    public void setMatemityLeave(BigDecimal matemityLeave) {
        this.matemityLeave = matemityLeave;
    }

    /** 
     *            @hibernate.property
     *             column="AUTI_STATE"
     *             length="1"
     *         
     */
    @Column(name="AUTI_STATE",nullable=true)
    public String getAutiState() {
        return this.autiState;
    }

    public void setAutiState(String autiState) {
        this.autiState = autiState;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("statisticsId", getStatisticsId())
            .toString();
    }

}
