package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_CALENDAR_ACTIVITY"
 *     
*/
@Entity
@Table(name="T_OA_CALENDAR_ACTIVITY")
public class ToaCalendarActivity implements Serializable {

    /** identifier field */
    private String activityId;

    /** nullable persistent field */
    private String activityName;

    /** nullable persistent field */
    private String activityRemark;

    /** nullable persistent field */
    private String userId;

    /** nullable persistent field */
    private String activityIsDel;
    
    /** 已删除 */
    public static final String ACTIVITY_DEL = "1";	
    /** 未删除*/
    public static final String ACTIVITY_NOTDEL = "0";
    
    
    /** persistent field */
    private Set toaCalendars;

    /**
     * 默认分类
     */
    public final static String DEFINE_ACTIVITY = "默认分类";
    
    /** full constructor */
    public ToaCalendarActivity(String activityId, String activityName, String activityRemark, String activityIsDel, String userId, Set toaCalendars) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityRemark = activityRemark;
        this.activityIsDel = activityIsDel;
        this.userId = userId;
        this.toaCalendars = toaCalendars;
    }

    /** default constructor */
    public ToaCalendarActivity() {
    }

    /** minimal constructor */
    public ToaCalendarActivity(String activityId, Set toaCalendars) {
        this.activityId = activityId;
        this.toaCalendars = toaCalendars;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="ACTIVITY_ID"
     *         
     */
    @Id
	@Column(name="ACTIVITY_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getActivityId() {
        return this.activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    /** 
     *            @hibernate.property
     *             column="ACTIVITY_NAME"
     *             length="128"
     *         
     */
    @Column(name="ACTIVITY_NAME",nullable=true)
    public String getActivityName() {
        return this.activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    /** 
     *            @hibernate.property
     *             column="ACTIVITY_REMARK"
     *             length="4000"
     *         
     */
    @Column(name="ACTIVITY_REMARK",nullable=true)
    public String getActivityRemark() {
        return this.activityRemark;
    }

    public void setActivityRemark(String activityRemark) {
        this.activityRemark = activityRemark;
    }

    /** 
     *            @hibernate.property
     *             column="ACTIVITY_ISDEL"
     *             length="1"
     *         
     */
    @Column(name="ACTIVITY_ISDEL",nullable=true)
	public String getActivityIsDel() {
		return activityIsDel;
	}

	public void setActivityIsDel(String activityIsDel) {
		this.activityIsDel = activityIsDel;
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
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="ACTIVITY_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaCalendar"
     *         
     */
    @OneToMany(mappedBy="toaCalendarActivity",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE},targetEntity=ToaCalendar.class)
    public Set getToaCalendars() {
        return this.toaCalendars;
    }

    public void setToaCalendars(Set toaCalendars) {
        this.toaCalendars = toaCalendars;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("activityId", getActivityId())
            .toString();
    }


}
