package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MSG_STATE_MEAN"
 *     
*/
@Entity
@Table(name="T_OA_MSG_STATE_MEAN")
public class ToaMsgStateMean implements Serializable {

    /** identifier field */
    private String msgStateId;

    /** nullable persistent field */
    private String msgState;

    /** nullable persistent field */
    private String msgStateMean;

    /** persistent field */
    private com.strongit.oa.bo.ToaMessage toaMessage;
    
    private String modelCode;//模块ID
    
    private String increaseCode;//业务ID

    /** full constructor */
    public ToaMsgStateMean(String msgStateId, String msgState, String msgStateMean, com.strongit.oa.bo.ToaMessage toaMessage) {
        this.msgStateId = msgStateId;
        this.msgState = msgState;
        this.msgStateMean = msgStateMean;
        this.toaMessage = toaMessage;
    }

    /** default constructor */
    public ToaMsgStateMean() {
    }

    /** minimal constructor */
    public ToaMsgStateMean(String msgStateId, com.strongit.oa.bo.ToaMessage toaMessage) {
        this.msgStateId = msgStateId;
        this.toaMessage = toaMessage;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MSG_STATE_ID"
     *         
     */
    @Id
	@Column(name="MSG_STATE_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getMsgStateId() {
        return this.msgStateId;
    }

    public void setMsgStateId(String msgStateId) {
        this.msgStateId = msgStateId;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_STATE"
     *             length="1"
     *         
     */
    @Column(name="MSG_STATE",nullable=true)
    public String getMsgState() {
        return this.msgState;
    }

    public void setMsgState(String msgState) {
        this.msgState = msgState;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_STATE_MEAN"
     *             length="50"
     *         
     */
    @Column(name="MSG_STATE_MEAN",nullable=true)
    public String getMsgStateMean() {
        return this.msgStateMean;
    }

    public void setMsgStateMean(String msgStateMean) {
        this.msgStateMean = msgStateMean;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="MSG_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="MSG_ID")
    public com.strongit.oa.bo.ToaMessage getToaMessage() {
        return this.toaMessage;
    }

    public void setToaMessage(com.strongit.oa.bo.ToaMessage toaMessage) {
        this.toaMessage = toaMessage;
    }
    
    /** 
     *            @hibernate.property
     *             column="increaseCode"
     *             length="32"
     *         
     */
    @Column(name="increaseCode",nullable=true)
	public String getIncreaseCode() {
		return increaseCode;
	}

	public void setIncreaseCode(String increaseCode) {
		this.increaseCode = increaseCode;
	}
	
    /** 
     *            @hibernate.property
     *             column="MODEL_CODE"
     *             length="3"
     *         
     */
    @Column(name="MODEL_CODE",nullable=true)
	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .append("msgStateId", getMsgStateId())
            .toString();
    }
}
