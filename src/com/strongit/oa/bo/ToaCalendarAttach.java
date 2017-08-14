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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_CALENDAR_ATTACH"
 *     
*/
@Entity
@Table(name="T_OA_CALENDAR_ATTACH")
public class ToaCalendarAttach implements Serializable {

    /** identifier field */
    private String calAttachId;

    /** nullable persistent field */
    private String attachId;

    /** persistent field */
    private com.strongit.oa.bo.ToaCalendar toaCalendar;

    /** full constructor */
    public ToaCalendarAttach(String calAttachId, String attachId, com.strongit.oa.bo.ToaCalendar toaCalendar) {
        this.calAttachId = calAttachId;
        this.attachId = attachId;
        this.toaCalendar = toaCalendar;
    }

    /** default constructor */
    public ToaCalendarAttach() {
    }

    /** minimal constructor */
    public ToaCalendarAttach(String calAttachId, com.strongit.oa.bo.ToaCalendar toaCalendar) {
        this.calAttachId = calAttachId;
        this.toaCalendar = toaCalendar;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="CAL_ATTACH_ID"
     *         
     */
    @Id
	@Column(name="CAL_ATTACH_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getCalAttachId() {
        return this.calAttachId;
    }

    public void setCalAttachId(String calAttachId) {
        this.calAttachId = calAttachId;
    }

    /** 
     *            @hibernate.property
     *             column="ATTACH_ID"
     *             length="32"
     *         
     */
    @Column(name="ATTACH_ID",nullable=true)
    public String getAttachId() {
        return this.attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
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
            .append("calAttachId", getCalAttachId())
            .toString();
    }

}
