package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_FILTRATE"
 *     
*/
@Entity
@Table(name="T_OA_SYSMANAGE_FILTRATE")
public class ToaSysmanageFiltrate implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String filtrateId;

    /** nullable persistent field */
    private String filtrateWord;

    /** nullable persistent field */
    private String filtrateRaplace;

    /** nullable persistent field */
    private Date filtrateTime;

    /** full constructor */
    public ToaSysmanageFiltrate(String filtrateId, String filtrateWord, String filtrateRaplace, Date filtrateTime) {
        this.filtrateId = filtrateId;
        this.filtrateWord = filtrateWord;
        this.filtrateRaplace = filtrateRaplace;
        this.filtrateTime = filtrateTime;
    }

    /** default constructor */
    public ToaSysmanageFiltrate() {
    }

    /** minimal constructor */
    public ToaSysmanageFiltrate(String filtrateId) {
        this.filtrateId = filtrateId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="FILTRATE_ID"
     *         
     */
    @Id
	@Column(name="FILTRATE_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getFiltrateId() {
        return this.filtrateId;
    }

    public void setFiltrateId(String filtrateId) {
        this.filtrateId = filtrateId;
    }

    /** 
     *            @hibernate.property
     *             column="FILTRATE_WORD"
     *             length="100"
     *         
     */
    @Column(name="FILTRATE_WORD",nullable=true)
    public String getFiltrateWord() {
        return this.filtrateWord;
    }

    public void setFiltrateWord(String filtrateWord) {
        this.filtrateWord = filtrateWord;
    }

    /** 
     *            @hibernate.property
     *             column="FILTRATE_RAPLACE"
     *             length="100"
     *         
     */
    @Column(name="FILTRATE_RAPLACE",nullable=true)
    public String getFiltrateRaplace() {
        return this.filtrateRaplace;
    }

    public void setFiltrateRaplace(String filtrateRaplace) {
        this.filtrateRaplace = filtrateRaplace;
    }

    /** 
     *            @hibernate.property
     *             column="FILTRATE_TIME"
     *             length="7"
     *         
     */
    @Column(name="FILTRATE_TIME",nullable=true)
    public Date getFiltrateTime() {
        return this.filtrateTime;
    }

    public void setFiltrateTime(Date filtrateTime) {
        this.filtrateTime = filtrateTime;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("filtrateId", getFiltrateId())
            .toString();
    }

}
