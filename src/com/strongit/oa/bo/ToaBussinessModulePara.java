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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_BUSSINESS_MODULE_PARA"
 *     
*/
@Entity
@Table(name="T_OA_BUSSINESS_MODULE_PARA",catalog="",schema="")
public class ToaBussinessModulePara implements Serializable {

    /** identifier field */
    private String bussinessModuleId;

    /** nullable persistent field */
    private String bussinessModuleCode;

    /** nullable persistent field */
    private String bussinessModuleName;

    /** nullable persistent field */
    private String increaseLength;

    /** nullable persistent field */
    private String isEnable;

    /** nullable persistent field */
    private Integer wordNumber;
    
    private Long increaseCode;
    
    private String thankOrNot;
    
    private String thankContent;
    

    private String timingType;
    
    private Date timingDelay;

    /** persistent field */
    private Set toaModuleStateMeans;

    /** full constructor */
    public ToaBussinessModulePara(String bussinessModuleId, String bussinessModuleCode, String bussinessModuleName, String increaseLength, String isEnable, Integer wordNumber,String timingType,Date timingDelay, Set toaModuleStateMeans) {
        this.bussinessModuleId = bussinessModuleId;
        this.bussinessModuleCode = bussinessModuleCode;
        this.bussinessModuleName = bussinessModuleName;
        this.increaseLength = increaseLength;
        this.isEnable = isEnable;
        this.wordNumber = wordNumber;
        this.timingType = timingType;
        this.timingDelay = timingDelay;
        this.toaModuleStateMeans = toaModuleStateMeans;
    }

    /** default constructor */
    public ToaBussinessModulePara() {
    }

    /** minimal constructor */
    public ToaBussinessModulePara(String bussinessModuleId, Set toaModuleStateMeans) {
        this.bussinessModuleId = bussinessModuleId;
        this.toaModuleStateMeans = toaModuleStateMeans;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="BUSSINESS_MODULE_ID"
     *         
     */
	@Id
	@Column(name="BUSSINESS_MODULE_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getBussinessModuleId() {
        return this.bussinessModuleId;
    }

    public void setBussinessModuleId(String bussinessModuleId) {
        this.bussinessModuleId = bussinessModuleId;
    }

    /** 
     *            @hibernate.property
     *             column="BUSSINESS_MODULE_CODE"
     *             length="2"
     *         
     */
    @Column(name="BUSSINESS_MODULE_CODE",nullable=true)
    public String getBussinessModuleCode() {
        return this.bussinessModuleCode;
    }

    public void setBussinessModuleCode(String bussinessModuleCode) {
        this.bussinessModuleCode = bussinessModuleCode;
    }

    /** 
     *            @hibernate.property
     *             column="BUSSINESS_MODULE_NAME"
     *             length="50"
     *         
     */
    @Column(name="BUSSINESS_MODULE_NAME",nullable=true)
    public String getBussinessModuleName() {
        return this.bussinessModuleName;
    }

    public void setBussinessModuleName(String bussinessModuleName) {
        this.bussinessModuleName = bussinessModuleName;
    }

    /** 
     *            @hibernate.property
     *             column="INCREASE_LENGTH"
     *             length="1"
     *         
     */
    @Column(name="INCREASE_LENGTH",nullable=true)
    public String getIncreaseLength() {
        return this.increaseLength;
    }

    public void setIncreaseLength(String increaseLength) {
        this.increaseLength = increaseLength;
    }

    /** 
     *            @hibernate.property
     *             column="IS_ENABLE"
     *             length="1"
     *         
     */
    @Column(name="IS_ENABLE",nullable=true)
    public String getIsEnable() {
        return this.isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    /** 
     *            @hibernate.property
     *             column="WORD_NUMBER"
     *             length="3"
     *         
     */
    @Column(name="WORD_NUMBER",nullable=true)
    public Integer getWordNumber() {
        return this.wordNumber;
    }

    public void setWordNumber(Integer wordNumber) {
        this.wordNumber = wordNumber;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="BUSSINESS_MODULE_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaModuleStateMean"
     *         
     */
    @OneToMany(mappedBy="toaBussinessModulePara",targetEntity=com.strongit.oa.bo.ToaModuleStateMean.class,cascade=CascadeType.ALL)
    @OrderBy("moduleStateFlag")
    public Set getToaModuleStateMeans() {
        return this.toaModuleStateMeans;
    }

    public void setToaModuleStateMeans(Set toaModuleStateMeans) {
        this.toaModuleStateMeans = toaModuleStateMeans;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("bussinessModuleId", getBussinessModuleId())
            .toString();
    }

    @Column(name="INCREASE_CODE",nullable=true)
	public Long getIncreaseCode() {
		return increaseCode;
	}

	public void setIncreaseCode(Long increaseCode) {
		this.increaseCode = increaseCode;
	}
	
	@Column(name="THANK_OR_NOT",nullable=true)
	public String getThankOrNot() {
		return thankOrNot;
	}

	public void setThankOrNot(String thankOrNot) {
		this.thankOrNot = thankOrNot;
	}

	@Column(name="THANK_CONTENT",nullable=true)
	public String getThankContent() {
		return thankContent;
	}

	public void setThankContent(String thankContent) {
		this.thankContent = thankContent;
	}


	@Column(name="TIMINGTYPE",nullable=true)
	public String getTimingType() {
		return timingType;
	}

	public void setTimingType(String timingType) {
		this.timingType = timingType;
	}

	@Column(name="TIMINGDELAY",nullable=true)
	public Date getTimingDelay() {
		return timingDelay;
	}

	public void setTimingDelay(Date timingDelay) {
		this.timingDelay = timingDelay;
	}


}
