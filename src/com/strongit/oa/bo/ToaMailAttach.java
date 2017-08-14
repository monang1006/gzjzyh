package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MAIL_ATTACH"
 *     
*/
@Entity
@Table(name="T_OA_MAIL_ATTACH",catalog="",schema="")
public class ToaMailAttach implements Serializable {

    /** identifier field */
    private String mailattachId;

    /** nullable persistent field */
    private String attachId6;

    /** persistent field */
    private com.strongit.oa.bo.ToaMail toaMail;

    /** full constructor */
    public ToaMailAttach(String mailattachId, String attachId6, com.strongit.oa.bo.ToaMail toaMail) {
        this.mailattachId = mailattachId;
        this.attachId6 = attachId6;
        this.toaMail = toaMail;
    }

    /** default constructor */
    public ToaMailAttach() {
    }

    /** minimal constructor */
    public ToaMailAttach(String mailattachId, com.strongit.oa.bo.ToaMail toaMail) {
        this.mailattachId = mailattachId;
        this.toaMail = toaMail;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MAILATTACH_ID"
     *         
     */
	@Id
	@Column(name="MAILATTACH_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getMailattachId() {
        return this.mailattachId;
    }

    public void setMailattachId(String mailattachId) {
        this.mailattachId = mailattachId;
    }

    /** 
     *            @hibernate.property
     *             column="ATTACH_ID6"
     *             length="32"
     *         
     */
    @Column(name="ATTACH_ID6",nullable=true)
    public String getAttachId6() {
        return this.attachId6;
    }

    public void setAttachId6(String attachId6) {
        this.attachId6 = attachId6;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="MAIL_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="MAIL_ID", nullable=false)
    public com.strongit.oa.bo.ToaMail getToaMail() {
        return this.toaMail;
    }

    public void setToaMail(com.strongit.oa.bo.ToaMail toaMail) {
        this.toaMail = toaMail;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("mailattachId", getMailattachId())
            .toString();
    }

}
