package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 	
 * 		  排班表
 *        @hibernate.class
 *         table="T_OA_ATTEND_ARRANGE"
 *     
*/
@Entity
@Table(name = "T_OA_ATTEND_ARRANGE", catalog = "", schema = "")
public class ToaAttendArrange implements Serializable {

    /** identifier field */
    private String arrangeId;

    /** nullable persistent field 用户ID*/
    private String userId;

    /** nullable persistent field 班别ID*/
    private String groupId;

    /** nullable persistent field 起始班次*/
    private String startSchedules;

    /** full constructor */
    public ToaAttendArrange(String arrangeId, String userId, String groupId, String startSchedules) {
        this.arrangeId = arrangeId;
        this.userId = userId;
        this.groupId = groupId;
        this.startSchedules = startSchedules;
    }

    /** default constructor */
    public ToaAttendArrange() {
    }

    /** minimal constructor */
    public ToaAttendArrange(String arrangeId) {
        this.arrangeId = arrangeId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="ARRANGE_ID"
     *         
     */
    @Id
	@Column(name = "ARRANGE_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getArrangeId() {
        return this.arrangeId;
    }

    public void setArrangeId(String arrangeId) {
        this.arrangeId = arrangeId;
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
     *             column="GROUP_ID"
     *             length="32"
     *         
     */
    @Column(name = "GROUP_ID")
    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /** 
     *            @hibernate.property
     *             column="START_SCHEDULES"
     *             length="32"
     *         
     */
    @Column(name = "START_SCHEDULES")
    public String getStartSchedules() {
        return this.startSchedules;
    }

    public void setStartSchedules(String startSchedules) {
        this.startSchedules = startSchedules;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("arrangeId", getArrangeId())
            .toString();
    }

}
