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
 *        @hibernate.class
 *         table="T_OA_DESKTOP_SECTIONSEL"
 *     
*/
@Entity
@Table(name="T_OA_DESKTOP_SECTIONSEL",catalog="",schema="")
public class ToaDesktopSectionsel implements Serializable {

    /** identifier field */
    private String sectionselId;

    /** nullable persistent field */
    private String privilId;
    
    private int sectionselOrderby;

	/** full constructor */
    public ToaDesktopSectionsel(String sectionselId, String privilId) {
        this.sectionselId = sectionselId;
        this.privilId = privilId;
    }

    /** default constructor */
    public ToaDesktopSectionsel() {
    }

    /** minimal constructor */
    public ToaDesktopSectionsel(String sectionselId) {
        this.sectionselId = sectionselId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="SECTIONSEL_ID"
     *         
     */
	@Id
	@Column(name="SECTIONSEL_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getSectionselId() {
        return this.sectionselId;
    }

    public void setSectionselId(String sectionselId) {
        this.sectionselId = sectionselId;
    }

    /** 
     *            @hibernate.property
     *             column="PRIVIL_ID"
     *             length="32"
     *         
     */
    @Column(name="PRIVIL_ID",nullable=true)
    public String getPrivilId() {
        return this.privilId;
    }

    public void setPrivilId(String privilId) {
        this.privilId = privilId;
    }
    
    @Column(name="SECTIONSEL_ORDERBY",nullable=true)
    public int getSectionselOrderby() {
		return sectionselOrderby;
	}

	public void setSectionselOrderby(int sectionselOrderby) {
		this.sectionselOrderby = sectionselOrderby;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .append("sectionselId", getSectionselId())
            .toString();
    }

}
