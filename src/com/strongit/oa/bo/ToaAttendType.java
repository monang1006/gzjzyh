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


/** 类型设置表
 *        @hibernate.class
 *         table="T_OA_ATTEND_TYPE"
 *     
*/
@Entity
@Table(name = "T_OA_ATTEND_TYPE")
public class ToaAttendType implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String typeId;

    /** nullable persistent field */
    private String typeName;//类型名称

    /** nullable persistent field */
    private String canRewriter;//是否能补填申请单。0：能补填；1：不能补填

    /** nullable persistent field */
    private String isAbsence;//是否是缺勤统计项。0：是；1：不是

    /** nullable persistent field */
    private String isEnable;//是否启用。0：启用；1：禁用

    /** nullable persistent field */
    private String isSystem;//是否是系统数据。0：是；1：不是

    /** nullable persistent field */
    private String isApplyType;//是否是申请类型。0：是；1：不是

    /** nullable persistent field */
    private String typeDesc;//类型描述
    
    /** 添加时间*/
    private Date typeCreateDate;

    /** full constructor */
    public ToaAttendType(String typeId, String typeName, String canRewriter, String isAbsence, String isEnable, String isSystem, String isApplyType, String typeDesc) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.canRewriter = canRewriter;
        this.isAbsence = isAbsence;
        this.isEnable = isEnable;
        this.isSystem = isSystem;
        this.isApplyType = isApplyType;
        this.typeDesc = typeDesc;
    }

    /** default constructor */
    public ToaAttendType() {
    }

    /** minimal constructor */
    public ToaAttendType(String typeId) {
        this.typeId = typeId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="TYPE_ID"
     *         
     */
    @Id
	@Column(name = "TYPE_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getTypeId() {
        return this.typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /** 
     *            @hibernate.property
     *             column="TYPE_NAME"
     *             length="30"
     *         
     */
    @Column(name = "TYPE_NAME",nullable=true)
    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /** 
     *            @hibernate.property
     *             column="CAN_REWRITER"
     *             length="1"
     *         
     */
    @Column(name = "CAN_REWRITER",nullable=true)
    public String getCanRewriter() {
        return this.canRewriter;
    }

    public void setCanRewriter(String canRewriter) {
        this.canRewriter = canRewriter;
    }

    /** 
     *            @hibernate.property
     *             column="IS_ABSENCE"
     *             length="1"
     *         
     */
    @Column(name = "IS_ABSENCE",nullable=true)
    public String getIsAbsence() {
        return this.isAbsence;
    }

    public void setIsAbsence(String isAbsence) {
        this.isAbsence = isAbsence;
    }

    /** 
     *            @hibernate.property
     *             column="IS_ENABLE"
     *             length="1"
     *         
     */
    @Column(name = "IS_ENABLE",nullable=true)
    public String getIsEnable() {
        return this.isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    /** 
     *            @hibernate.property
     *             column="IS_SYSTEM"
     *             length="1"
     *         
     */
    @Column(name = "IS_SYSTEM",nullable=true)
    public String getIsSystem() {
        return this.isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    /** 
     *            @hibernate.property
     *             column="IS_APPLY_TYPE"
     *             length="1"
     *         
     */
    @Column(name = "IS_APPLY_TYPE",nullable=true)
    public String getIsApplyType() {
        return this.isApplyType;
    }

    public void setIsApplyType(String isApplyType) {
        this.isApplyType = isApplyType;
    }

    /** 
     *            @hibernate.property
     *             column="TYPE_DESC"
     *             length="400"
     *         
     */
    @Column(name = "TYPE_DESC",nullable=true)
    public String getTypeDesc() {
        return this.typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("typeId", getTypeId())
            .toString();
    }

    @Column(name="TYPE_CREATEDATE",nullable=true)
	public Date getTypeCreateDate() {
		return typeCreateDate;
	}

	public void setTypeCreateDate(Date typeCreateDate) {
		this.typeCreateDate = typeCreateDate;
	}

}
