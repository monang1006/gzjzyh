package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_CALENDAR_SHARE"
 *     
*/
@Entity
@Table(name="T_OA_CALENDAR_SHARE")
public class ToaCalendarShare implements Serializable {

    /** identifier field */
    private String shareId;

    /** nullable persistent field */
    private String userId;

    /** persistent field */
    private com.strongit.oa.bo.ToaCalendar toaCalendar;

    /** full constructor */
    public ToaCalendarShare(String shareId, String userId, com.strongit.oa.bo.ToaCalendar toaCalendar) {
        this.shareId = shareId;
        this.userId = userId;
        this.toaCalendar = toaCalendar;
    }

    /** default constructor */
    public ToaCalendarShare() {
    }

    /** minimal constructor */
    public ToaCalendarShare(String shareId, com.strongit.oa.bo.ToaCalendar toaCalendar) {
        this.shareId = shareId;
        this.toaCalendar = toaCalendar;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="SHARE_ID"
     *         
     */
    @Id
	@Column(name="SHARE_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getShareId() {
        return this.shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
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
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="CALENDAR_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CALENDAR_ID")
    public com.strongit.oa.bo.ToaCalendar getToaCalendar() {
        return this.toaCalendar;
    }

    public void setToaCalendar(com.strongit.oa.bo.ToaCalendar toaCalendar) {
        this.toaCalendar = toaCalendar;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("shareId", getShareId())
            .toString();
    }

}
