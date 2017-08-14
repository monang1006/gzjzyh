package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

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
 *         table="T_OA_VETERAN_REGARDS"
 *     
*/
@Entity
@Table(name = "T_OA_VETERAN_REGARDS", catalog = "", schema = "")
public class ToaVeteranRegard implements Serializable {

    /** identifier field */
    private String vereId;//慰问信息ID

    /** nullable persistent field */
    private String verePersons;//慰问领导

    /** nullable persistent field */
    private String vereInfo;//慰问信息
    
    private Date vereTime;//慰问时间
    private String vereTopic;//慰问主题

    /** nullable persistent field */
    private String rest1;

    /** nullable persistent field */
    private String rest2;

    /** persistent field */
    private com.strongit.oa.bo.ToaBaseVeteran toaBaseVeteran;//被慰问老干部

    /** full constructor */
    public ToaVeteranRegard(String vereId, String verePersons, String vereInfo, String rest1, String rest2, com.strongit.oa.bo.ToaBaseVeteran toaBaseVeteran) {
        this.vereId = vereId;
        this.verePersons = verePersons;
        this.vereInfo = vereInfo;
        this.rest1 = rest1;
        this.rest2 = rest2;
        this.toaBaseVeteran = toaBaseVeteran;
    }

    /** default constructor */
    public ToaVeteranRegard() {
    }

    /** minimal constructor */
    public ToaVeteranRegard(String vereId, com.strongit.oa.bo.ToaBaseVeteran toaBaseVeteran) {
        this.vereId = vereId;
        this.toaBaseVeteran = toaBaseVeteran;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="VERE_ID"
     *         
     */
	@Id
	@Column(name = "VERE_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getVereId() {
        return this.vereId;
    }

    public void setVereId(String vereId) {
        this.vereId = vereId;
    }

    /** 
     *            @hibernate.property
     *             column="VERE_PERSONS"
     *             length="130"
     *         
     */
    @Column(name = "VERE_PERSONS")
    public String getVerePersons() {
        return this.verePersons;
    }

    public void setVerePersons(String verePersons) {
        this.verePersons = verePersons;
    }

    /** 
     *            @hibernate.property
     *             column="VERE_INFO"
     *             length="230"
     *         
     */
    @Column(name = "VERE_INFO")
    public String getVereInfo() {
        return this.vereInfo;
    }

    public void setVereInfo(String vereInfo) {
        this.vereInfo = vereInfo;
    }

    /** 
     *            @hibernate.property
     *             column="REST1"
     *             length="130"
     *         
     */
    @Column(name = "REST1")
    public String getRest1() {
        return this.rest1;
    }

    public void setRest1(String rest1) {
        this.rest1 = rest1;
    }

    /** 
     *            @hibernate.property
     *             column="REST2"
     *             length="30"
     *         
     */
    @Column(name = "REST2")
    public String getRest2() {
        return this.rest2;
    }

    public void setRest2(String rest2) {
        this.rest2 = rest2;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="PERSON_ID"         
     *         
     */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
    public com.strongit.oa.bo.ToaBaseVeteran getToaBaseVeteran() {
        return this.toaBaseVeteran;
    }

    public void setToaBaseVeteran(com.strongit.oa.bo.ToaBaseVeteran toaBaseVeteran) {
        this.toaBaseVeteran = toaBaseVeteran;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("vereId", getVereId())
            .toString();
    }
    @Column(name = "VERE_TIME")
	public Date getVereTime() {
		return vereTime;
	}

	public void setVereTime(Date vereTime) {
		this.vereTime = vereTime;
	}
	  @Column(name = "VERE_TOPIC")
	public String getVereTopic() {
		return vereTopic;
	}

	public void setVereTopic(String vereTopic) {
		this.vereTopic = vereTopic;
	}

}
