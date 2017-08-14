package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 	  班组表
 *        @hibernate.class
 *         table="T_OA_ATTEND_SCHED_GROUP"
 *     
*/
@Entity
@Table(name = "T_OA_ATTEND_SCHED_GROUP", catalog = "", schema = "")
public class ToaAttendSchedGroup implements Serializable {

    /** identifier field */
    private String groupId;

    /** nullable persistent field 班组名称*/
    private String groupName;

    /** nullable persistent field 是否需倒班。0：不需要；1：需要*/
    private String logo;

    /** nullable persistent field 班别有效时间*/
    private Date groupStime;

    /** nullable persistent field 班别失效时间*/
    private Date groupEtime;

    /** nullable persistent field 操作日期*/
    private Date operateDate;

    /** nullable persistent field 班别描述*/
    private String groupDesc;

    /** persistent field 班次*/
    private Set toaAttendSchedules;

    /** full constructor */
    public ToaAttendSchedGroup(String groupId, String groupName, String logo, Date groupStime, Date groupEtime, Date operateDate, String groupDesc, Set toaAttendSchedules) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.logo = logo;
        this.groupStime = groupStime;
        this.groupEtime = groupEtime;
        this.operateDate = operateDate;
        this.groupDesc = groupDesc;
        this.toaAttendSchedules = toaAttendSchedules;
    }

    /** default constructor */
    public ToaAttendSchedGroup() {
    }

    /** minimal constructor */
    public ToaAttendSchedGroup(String groupId, Set toaAttendSchedules) {
        this.groupId = groupId;
        this.toaAttendSchedules = toaAttendSchedules;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="GROUP_ID"
     *         
     */
    @Id
	@Column(name = "GROUP_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /** 
     *            @hibernate.property
     *             column="GROUP_NAME"
     *             length="50"
     *         
     */
    @Column(name = "GROUP_NAME")
    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /** 
     *            @hibernate.property
     *             column="LOGO"
     *             length="1"
     *         
     */
    @Column(name = "LOGO")
    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    /** 
     *            @hibernate.property
     *             column="GROUP_STIME"
     *             length="7"
     *         
     */
    @Column(name = "GROUP_STIME")
    public Date getGroupStime() {
        return this.groupStime;
    }

    public void setGroupStime(Date groupStime) {
        this.groupStime = groupStime;
    }

    /** 
     *            @hibernate.property
     *             column="GROUP_ETIME"
     *             length="7"
     *         
     */
    @Column(name = "GROUP_ETIME")
    public Date getGroupEtime() {
        return this.groupEtime;
    }

    public void setGroupEtime(Date groupEtime) {
        this.groupEtime = groupEtime;
    }

    /** 
     *            @hibernate.property
     *             column="OPERATE_DATE"
     *             length="7"
     *         
     */
    @Column(name = "OPERATE_DATE")
    public Date getOperateDate() {
        return this.operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    /** 
     *            @hibernate.property
     *             column="GROUP_DESC"
     *             length="400"
     *         
     */
    @Column(name = "GROUP_DESC")
    public String getGroupDesc() {
        return this.groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="GROUP_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaAttendSchedule"
     *         
     */
    @OneToMany(mappedBy="toaAttendSchedGroup",targetEntity=com.strongit.oa.bo.ToaAttendSchedule.class,cascade=CascadeType.ALL)
    public Set getToaAttendSchedules() {
        return this.toaAttendSchedules;
    }

    public void setToaAttendSchedules(Set toaAttendSchedules) {
        this.toaAttendSchedules = toaAttendSchedules;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("groupId", getGroupId())
            .toString();
    }

}
