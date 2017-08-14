package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_CALENDAR"
 *     
*/
@Entity
@Table(name="T_OA_CALENDAR")
public class ToaCalendar implements Serializable {

    /** identifier field */
    private String calendarId;

    /** 标题 */
    private String calTitle;

    /** 日程内容 */
    private String calCon;

    /** 地点 */
    private String calPlace;

    /** 开始时间 */
    private Date calStartTime;

    /** 结束时间 */
    private Date calEndTime;

    /** 用户ID */
    private String userId;

    /** 创建者*/
    private String calUserName;
    
    /** 添加他人日程的操作者ID*/
    private String assignUserId;

    /** 是否公开为领导日程 */
    private String isLeader;
    public final static String IS_LEADER = "1";//是领导日程
    public final static String IS_NOT_LEADER = "0";//不是领导日程

    /** 重复频率 */
    private String repeatType;
    
    public final static String REPEAT_NONE= "0";//按日
    public final static String REPEAT_BY_DAY = "2";//按日
    public final static String REPEAT_BY_WEEK = "3";//按周
    public final static String REPEAT_BY_MONTH = "4";//按月
    public final static String REPEAT_BY_YEAR = "5";//按年
    
    /** 重复段开始 */
    private Date calRepeatStime;

    /** 重复段结束 */
    private Date calRepeatEtime;
    
    /** 是否提醒 */
    private String calHasRemind;
    public final static String HAS_REMIND = "1";//有提醒
    public final static String HAS_NO_REMIND = "0";//没有提醒
    
    /** 用户表 */
    private com.strongit.oa.bo.ToaUser toaUser;

    /**  活动分类 */
    private com.strongit.oa.bo.ToaCalendarActivity toaCalendarActivity;

    /** 日程共享 */
    private Set toaCalendarShares;

    /** 日程附件 */
    private Set toaCalendarAttaches;

    /** 日程提醒 */
    private Set toaCalendarReminds;

    /** full constructor */
    public ToaCalendar(String calendarId, String calTitle, String calPlace, Date calStartTime, Date calEndTime, String userId, String calUserName, String isLeader, String repeatType, Date calRepeatStime, Date calRepeatEtime, String calCon, String calHasRemind, com.strongit.oa.bo.ToaUser toaUser, com.strongit.oa.bo.ToaCalendarActivity toaCalendarActivity, Set toaCalendarShares, Set toaCalendarAttaches, Set toaCalendarReminds,String assignUserId) {
        this.calendarId = calendarId;
        this.calTitle = calTitle;
        this.calPlace = calPlace;
        this.calStartTime = calStartTime;
        this.calEndTime = calEndTime;
        this.userId = userId;
        this.calUserName = calUserName;
        this.isLeader = isLeader;
        this.repeatType = repeatType;
        this.calRepeatStime = calRepeatStime;
        this.calRepeatEtime = calRepeatEtime;
        this.calCon = calCon;
        this.calHasRemind = calHasRemind;
        this.toaUser = toaUser;
        this.toaCalendarActivity = toaCalendarActivity;
        this.toaCalendarShares = toaCalendarShares;
        this.toaCalendarAttaches = toaCalendarAttaches;
        this.toaCalendarReminds = toaCalendarReminds;
        this.assignUserId=assignUserId;
    }

    /** default constructor */
    public ToaCalendar() {
    }

    /** minimal constructor */
    public ToaCalendar(String calendarId, com.strongit.oa.bo.ToaUser toaUser, com.strongit.oa.bo.ToaCalendarActivity toaCalendarActivity, Set toaCalendarShares, Set toaCalendarAttaches, Set toaCalendarReminds,String assignUserId) {
        this.calendarId = calendarId;
        this.toaUser = toaUser;
        this.toaCalendarActivity = toaCalendarActivity;
        this.toaCalendarShares = toaCalendarShares;
        this.toaCalendarAttaches = toaCalendarAttaches;
        this.toaCalendarReminds = toaCalendarReminds;
        this.assignUserId=assignUserId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="CALENDAR_ID"
     *         
     */
    @Id
	@Column(name="CALENDAR_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getCalendarId() {
        return this.calendarId;
    } 
    
    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    /**
     * @hibernate.property
     * column="ASSIGN_USER_ID"
     * @return
     */
    @Column(name="ASSIGN_USER_ID",nullable=true)
    public String getAssignUserId(){
    	return this.assignUserId;
    }
    
    public void setAssignUserId(String assignUserId){
    	this.assignUserId=assignUserId;
    }
    
    /** 
     *            @hibernate.property
     *             column="CAL_TITLE"
     *             length="128"
     *         
     */
    @Column(name="CAL_TITLE",nullable=true)
    public String getCalTitle() {
        return this.calTitle;
    }

    public void setCalTitle(String calTitle) {
        this.calTitle = calTitle;
    }

    /** 
     *            @hibernate.property
     *             column="CAL_CON"
     *             length="4000"
     *         
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="CAL_CON", columnDefinition="CLOB", nullable=true)    
    public String getCalCon() {
        return this.calCon;
    }

    public void setCalCon(String calCon) {
        this.calCon = calCon;
    }

    /** 
     *            @hibernate.property
     *             column="CAL_PLACE"
     *             length="128"
     *         
     */
    @Column(name="CAL_PLACE",nullable=true)
    public String getCalPlace() {
        return this.calPlace;
    }

    public void setCalPlace(String calPlace) {
        this.calPlace = calPlace;
    }

    /** 
     *            @hibernate.property
     *             column="CAL_START_TIME"
     *             length="7"
     *         
     */
    @Column(name="CAL_START_TIME",nullable=true)
    public Date getCalStartTime() {
        return this.calStartTime;
    }

    public void setCalStartTime(Date calStartTime) {
        this.calStartTime = calStartTime;
    }

    /** 
     *            @hibernate.property
     *             column="CAL_END_TIME"
     *             length="7"
     *         
     */
    @Column(name="CAL_END_TIME",nullable=true)
    public Date getCalEndTime() {
        return this.calEndTime;
    }

    public void setCalEndTime(Date calEndTime) {
        this.calEndTime = calEndTime;
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
     *             column="CAL_USER_NAME"
     *             length="10"
     *         
     */
    @Column(name="CAL_USER_NAME",nullable=true)
    public String getCalUserName() {
        return this.calUserName;
    }

    public void setCalUserName(String calUserName) {
        this.calUserName = calUserName;
    }

    /** 
     *            @hibernate.property
     *             column="IS_LEADER"
     *             length="1"
     *         
     */
    @Column(name="IS_LEADER",nullable=true)
    public String getIsLeader() {
        return this.isLeader;
    }

    public void setIsLeader(String isLeader) {
        this.isLeader = isLeader;
    }

    /** 
     *            @hibernate.property
     *             column="CAL_HASREMIND"
     *             length="1"
     *         
     */
    @Column(name="CAL_HASREMIND",nullable=true)
	public String getCalHasRemind() {
		return calHasRemind;
	}

	public void setCalHasRemind(String calHasRemind) {
		this.calHasRemind = calHasRemind;
	}
	
    /** 
     *            @hibernate.property
     *             column="REPEAT_TYPE"
     *             length="1"
     *         
     */
    @Column(name="REPEAT_TYPE",nullable=true)
    public String getRepeatType() {
        return this.repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    /** 
     *            @hibernate.property
     *             column="CAL_REPEAT_STIME"
     *             length="7"
     *         
     */
    @Column(name="CAL_REPEAT_STIME",nullable=true)
    public Date getCalRepeatStime() {
        return this.calRepeatStime;
    }

    public void setCalRepeatStime(Date calRepeatStime) {
        this.calRepeatStime = calRepeatStime;
    }

    /** 
     *            @hibernate.property
     *             column="CAL_REPEAT_ETIME"
     *             length="7"
     *         
     */
    @Column(name="CAL_REPEAT_ETIME",nullable=true)
    public Date getCalRepeatEtime() {
        return this.calRepeatEtime;
    }

    public void setCalRepeatEtime(Date calRepeatEtime) {
        this.calRepeatEtime = calRepeatEtime;
    }

     /**            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="USE_USER_ID"         
     *         
     */
    @Transient
    public com.strongit.oa.bo.ToaUser getToaUser() {
        return this.toaUser;
    }

    public void setToaUser(com.strongit.oa.bo.ToaUser toaUser) {
        this.toaUser = toaUser;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="ACTIVITY_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ACTIVITY_ID")
    public com.strongit.oa.bo.ToaCalendarActivity getToaCalendarActivity() {
        return this.toaCalendarActivity;
    }

    public void setToaCalendarActivity(com.strongit.oa.bo.ToaCalendarActivity toaCalendarActivity) {
        this.toaCalendarActivity = toaCalendarActivity;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="CALENDAR_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaCalendarShare"
     *         
     */
    @OneToMany(mappedBy="toaCalendar",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE},targetEntity=ToaCalendarShare.class)
    public Set getToaCalendarShares() {
        return this.toaCalendarShares;
    }

    public void setToaCalendarShares(Set toaCalendarShares) {
        this.toaCalendarShares = toaCalendarShares;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="CALENDAR_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaCalendarAttach"
     *         
     */
    @OneToMany(mappedBy="toaCalendar",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE},targetEntity=ToaCalendarAttach.class)
    public Set getToaCalendarAttaches() {
        return this.toaCalendarAttaches;
    }

    public void setToaCalendarAttaches(Set toaCalendarAttaches) {
        this.toaCalendarAttaches = toaCalendarAttaches;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="CALENDAR_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaCalendarRemind"
     *         
     */
    @OneToMany(mappedBy="toaCalendar",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE},targetEntity=ToaCalendarRemind.class)
    public Set getToaCalendarReminds() {
        return this.toaCalendarReminds;
    }

    public void setToaCalendarReminds(Set toaCalendarReminds) {
        this.toaCalendarReminds = toaCalendarReminds;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("calendarId", getCalendarId())
            .toString();
    }



}
