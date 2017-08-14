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
 *         table="T_OA_TRAIN_RECORDS"
 *     
*/
@Entity
@Table(name = "T_OA_TRAIN_RECORDS", catalog = "", schema = "")
public class ToaTrainRecord implements Serializable {

    /** identifier field */
    private String recordId;//记录ID

    /** nullable persistent field */
    private String recordInfo;//备注

    /** persistent field */
    private com.strongit.oa.bo.ToaBasePerson toaBasePerson;//培训人员
    
  
    /** persistent field */
    private com.strongit.oa.bo.ToaTrainInfo toaTrainInfo;//培训信息
 
    private String personName;//人员姓名

    
	@Column(name = "PERSON_NAME")
    public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	/** full constructor */
    public ToaTrainRecord(String recordId, String recordInfo, com.strongit.oa.bo.ToaBasePerson toaBasePerson, com.strongit.oa.bo.ToaTrainInfo toaTrainInfo) {
        this.recordId = recordId;
        this.recordInfo = recordInfo;
        this.toaBasePerson = toaBasePerson;
        this.toaTrainInfo = toaTrainInfo;
    }

    /** default constructor */
    public ToaTrainRecord() {
    }

    /** minimal constructor */
    public ToaTrainRecord(String recordId, com.strongit.oa.bo.ToaBasePerson toaBasePerson, com.strongit.oa.bo.ToaTrainInfo toaTrainInfo) {
        this.recordId = recordId;
        this.toaBasePerson = toaBasePerson;
        this.toaTrainInfo = toaTrainInfo;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="RECORD_ID"
     *         
     */
    @Id
	@Column(name = "RECORD_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /** 
     *            @hibernate.property
     *             column="RECORD_INFO"
     *             length="132"
     *         
     */
	@Column(name = "RECORD_INFO")
    public String getRecordInfo() {
        return this.recordInfo;
    }

    public void setRecordInfo(String recordInfo) {
        this.recordInfo = recordInfo;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="PERSON_ID"         
     *         
     */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
    public com.strongit.oa.bo.ToaBasePerson getToaBasePerson() {
        return this.toaBasePerson;
    }

    public void setToaBasePerson(com.strongit.oa.bo.ToaBasePerson toaBasePerson) {
        this.toaBasePerson = toaBasePerson;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="TRAIN_ID"         
     *         
     */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRAIN_ID")
    public com.strongit.oa.bo.ToaTrainInfo getToaTrainInfo() {
        return this.toaTrainInfo;
    }

    public void setToaTrainInfo(com.strongit.oa.bo.ToaTrainInfo toaTrainInfo) {
        this.toaTrainInfo = toaTrainInfo;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("recordId", getRecordId())
            .toString();
    }

}
