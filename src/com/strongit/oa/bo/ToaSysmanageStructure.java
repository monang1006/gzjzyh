package com.strongit.oa.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_STRUCTURE"
 *     
*/
@Entity
@Table(name="T_OA_SYSMANAGE_STRUCTURE")
public class ToaSysmanageStructure implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String infoSetCode ; 

    /** nullable persistent field */
    private String infoSetValue;

    /** nullable persistent field */
    private String infoSetName;

    /** nullable persistent field */
    private String infoSetType;

    /** nullable persistent field */
    private String infoSetShort;

    /** nullable persistent field */
    private String infoSetIsexistchild;

    /** nullable persistent field */
    private String infoSetCondition;

    /** nullable persistent field */
    private String infoSetState;

    /** nullable persistent field */
    //修改类型
    private Long infoSetOrderno;

    /** nullable persistent field */
    private String infoSetPkey;

    /** nullable persistent field */
    private String infoSetParentid;

    /** nullable persistent field */
    private String infoSetIsSystem;

    /** persistent field */
    private Set toaSysmanagePropertypes;

    /** persistent field */
    private Set toaSysmanageProperties;
    
    private Set toaPostStructure;

    /** full constructor */
    public ToaSysmanageStructure(String infoSetCode, String infoSetValue, String infoSetName, String infoSetType, String infoSetShort, String infoSetIsexistchild, String infoSetCondition, String infoSetState, Long infoSetOrderno, String infoSetPkey, String infoSetParentid, String infoSetIsSystem, Set toaSysmanagePropertypes, Set toaSysmanageProperties) {
        this.infoSetCode = infoSetCode;
        this.infoSetValue = infoSetValue;
        this.infoSetName = infoSetName;
        this.infoSetType = infoSetType;
        this.infoSetShort = infoSetShort;
        this.infoSetIsexistchild = infoSetIsexistchild;
        this.infoSetCondition = infoSetCondition;
        this.infoSetState = infoSetState;
        this.infoSetOrderno = infoSetOrderno;
        this.infoSetPkey = infoSetPkey;
        this.infoSetParentid = infoSetParentid;
        this.infoSetIsSystem = infoSetIsSystem;
        this.toaSysmanagePropertypes = toaSysmanagePropertypes;
        this.toaSysmanageProperties = toaSysmanageProperties;
    }

    /** default constructor */
    public ToaSysmanageStructure() {
    }

    /** minimal constructor */
    public ToaSysmanageStructure(String infoSetCode, Set toaSysmanagePropertypes, Set toaSysmanageProperties) {
        this.infoSetCode = infoSetCode;
        this.toaSysmanagePropertypes = toaSysmanagePropertypes;
        this.toaSysmanageProperties = toaSysmanageProperties;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="INFO_SET_CODE"
     *         
     */
    @Id
	@Column(name="INFO_SET_CODE",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getInfoSetCode() {
        return this.infoSetCode;
    }

    public void setInfoSetCode(String infoSetCode) {
        this.infoSetCode = infoSetCode;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_VALUE"
     *             length="20"
     *         
     */
    @Column(name="INFO_SET_VALUE",nullable=true)
    public String getInfoSetValue() {
        return this.infoSetValue;
    }

    public void setInfoSetValue(String infoSetValue) {
        this.infoSetValue = infoSetValue;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_NAME"
     *             length="100"
     *         
     */
    @Column(name="INFO_SET_NAME",nullable=true)
    public String getInfoSetName() {
        return this.infoSetName;
    }

    public void setInfoSetName(String infoSetName) {
        this.infoSetName = infoSetName;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_TYPE"
     *             length="1"
     *         
     */
    @Column(name="INFO_SET_TYPE",nullable=true)
    public String getInfoSetType() {
        return this.infoSetType;
    }

    public void setInfoSetType(String infoSetType) {
        this.infoSetType = infoSetType;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_SHORT"
     *             length="100"
     *         
     */
    @Column(name="INFO_SET_SHORT",nullable=true)
    public String getInfoSetShort() {
        return this.infoSetShort;
    }

    public void setInfoSetShort(String infoSetShort) {
        this.infoSetShort = infoSetShort;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_ISEXISTCHILD"
     *             length="1"
     *         
     */
    @Column(name="INFO_SET_ISEXISTCHILD",nullable=true)
    public String getInfoSetIsexistchild() {
        return this.infoSetIsexistchild;
    }

    public void setInfoSetIsexistchild(String infoSetIsexistchild) {
        this.infoSetIsexistchild = infoSetIsexistchild;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_CONDITION"
     *             length="2000"
     *         
     */
    @Column(name="INFO_SET_CONDITION",nullable=true)
    public String getInfoSetCondition() {
        return this.infoSetCondition;
    }

    public void setInfoSetCondition(String infoSetCondition) {
        this.infoSetCondition = infoSetCondition;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_STATE"
     *             length="1"
     *         
     */
    @Column(name="INFO_SET_STATE",nullable=true)
    public String getInfoSetState() {
        return this.infoSetState;
    }

    public void setInfoSetState(String infoSetState) {
        this.infoSetState = infoSetState;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_ORDERNO"
     *             length="22"
     *         
     */
    @Column(name="INFO_SET_ORDERNO",nullable=true)
    public Long getInfoSetOrderno() {
        return this.infoSetOrderno;
    }

    public void setInfoSetOrderno(Long infoSetOrderno) {
        this.infoSetOrderno = infoSetOrderno;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_PKEY"
     *             length="200"
     *         
     */
    @Column(name="INFO_SET_PKEY",nullable=true)
    public String getInfoSetPkey() {
        return this.infoSetPkey;
    }

    public void setInfoSetPkey(String infoSetPkey) {
        this.infoSetPkey = infoSetPkey;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_PARENTID"
     *             length="4"
     *         
     */
    @Column(name="INFO_SET_PARENTID",nullable=true)
    public String getInfoSetParentid() {
        return this.infoSetParentid;
    }

    public void setInfoSetParentid(String infoSetParentid) {
        this.infoSetParentid = infoSetParentid;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_IS_SYSTEM"
     *             length="1"
     *         
     */
    @Column(name="INFO_SET_IS_SYSTEM",nullable=true)
    public String getInfoSetIsSystem() {
        return this.infoSetIsSystem;
    }

    public void setInfoSetIsSystem(String infoSetIsSystem) {
        this.infoSetIsSystem = infoSetIsSystem;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="INFO_SET_CODE"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaSysmanagePropertype"
     *         
     */
    @OneToMany(mappedBy="toaSysmanageStructure",targetEntity=com.strongit.oa.bo.ToaSysmanagePropertype.class,cascade=CascadeType.ALL)
    public Set getToaSysmanagePropertypes() {
        return this.toaSysmanagePropertypes;
    }

    public void setToaSysmanagePropertypes(Set toaSysmanagePropertypes) {
        this.toaSysmanagePropertypes = toaSysmanagePropertypes;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="INFO_SET_CODE"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaSysmanageProperty"
     *         
     */
    @OneToMany(mappedBy="toaSysmanageStructure",targetEntity=com.strongit.oa.bo.ToaSysmanageProperty.class,cascade=CascadeType.ALL)
    public Set getToaSysmanageProperties() {
        return this.toaSysmanageProperties;
    }

    public void setToaSysmanageProperties(Set toaSysmanageProperties) {
        this.toaSysmanageProperties = toaSysmanageProperties;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("infoSetCode", getInfoSetCode())
            .toString();
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="INFO_SET_CODE"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaPostStructure"
     *         
     */
    @OneToMany(mappedBy="infoSet",targetEntity=com.strongit.oa.bo.ToaPostStructure.class,cascade=CascadeType.ALL)
	public Set getToaPostStructure() {
		return toaPostStructure;
	}

	public void setToaPostStructure(Set toaPostStructure) {
		this.toaPostStructure = toaPostStructure;
	}

}
