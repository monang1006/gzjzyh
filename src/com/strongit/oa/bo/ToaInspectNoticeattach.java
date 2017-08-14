package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_INSPECT_NOTICEATTACH"
 *     
*/
@Entity
@Table(name="T_OA_INSPECT_NOTICEATTACH",schema="",catalog="")
public class ToaInspectNoticeattach implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4051858823979761184L;

	/** identifier field */
    private String noticeattachId;

    /** nullable persistent field */
    private String attachId;

    /** persistent field */
    private com.strongit.oa.bo.ToaInspectNotice toaInspectNotice;

    /** full constructor */
    public ToaInspectNoticeattach(String noticeattachId, com.strongit.oa.bo.ToaInspectNotice toaInspectNotice) {
        this.noticeattachId = noticeattachId;
        this.toaInspectNotice = toaInspectNotice;
    }

    /** default constructor */
    public ToaInspectNoticeattach() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="NOTICEATTACH_ID"
     *         
     */
    @Id
	@Column(name="NOTICEATTACH_ID",nullable=false,length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getNoticeattachId() {
        return this.noticeattachId;
    }

    public void setNoticeattachId(String noticeattachId) {
        this.noticeattachId = noticeattachId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="NOTICE_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="NOTICE_ID")
    public com.strongit.oa.bo.ToaInspectNotice getToaInspectNotice() {
        return this.toaInspectNotice;
    }

    public void setToaInspectNotice(com.strongit.oa.bo.ToaInspectNotice toaInspectNotice) {
        this.toaInspectNotice = toaInspectNotice;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("noticeattachId", getNoticeattachId())
            .toString();
    }
    
    @Column(name="ATTACH_ID")
	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

}
