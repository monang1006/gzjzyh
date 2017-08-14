package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_DICT"
 *     
*/
@Entity
@Table(name="T_OA_SYSMANAGE_DICT",catalog="",schema="")
public class ToaSysmanageDict implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String dictCode;

    /** nullable persistent field */
    private String dictValue;

    /** nullable persistent field */
    private String dictName;

    /** nullable persistent field */
    private String dictType;

    /** nullable persistent field */
    private String dictFlag;

    /** nullable persistent field */
    private Integer dictOrderby;

    /** nullable persistent field */
    private String dictIsSystem;

    /** persistent field */
    private Set toaSysmanageDictitems;

    /** full constructor */
    public ToaSysmanageDict(String dictCode, String dictValue, String dictName, String dictType, String dictFlag, Integer dictOrderby, String dictIsSystem, Set toaSysmanageDictitems) {
        this.dictCode = dictCode;
        this.dictValue = dictValue;
        this.dictName = dictName;
        this.dictType = dictType;
        this.dictFlag = dictFlag;
        this.dictOrderby = dictOrderby;
        this.dictIsSystem = dictIsSystem;
        this.toaSysmanageDictitems = toaSysmanageDictitems;
    }

    /** default constructor */
    public ToaSysmanageDict() {
    }

    /** minimal constructor */
    public ToaSysmanageDict(String dictCode, Set toaSysmanageDictitems) {
        this.dictCode = dictCode;
        this.toaSysmanageDictitems = toaSysmanageDictitems;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DICT_CODE"
     *         
     */
    @Id
	@Column(name="DICT_CODE",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDictCode() {
        return this.dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_VALUE"
     *             length="20"
     *         
     */
    @Column(name="DICT_VALUE",nullable=true)
    public String getDictValue() {
        return this.dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_NAME"
     *             length="500"
     *         
     */
    @Column(name="DICT_NAME",nullable=true)
    public String getDictName() {
        return this.dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_TYPE"
     *             length="1"
     *         
     */
    @Column(name="DICT_TYPE",nullable=true)
    public String getDictType() {
        return this.dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_FLAG"
     *             length="1"
     *         
     */
    @Column(name="DICT_FLAG",nullable=true)
    public String getDictFlag() {
        return this.dictFlag;
    }

    public void setDictFlag(String dictFlag) {
        this.dictFlag = dictFlag;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_ORDERBY"
     *             length="5"
     *         
     */
    @Column(name="DICT_ORDERBY",nullable=true)
    public Integer getDictOrderby() {
        return this.dictOrderby;
    }

    public void setDictOrderby(Integer dictOrderby) {
        this.dictOrderby = dictOrderby;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_IS_SYSTEM"
     *             length="1"
     *         
     */
    @Column(name="DICT_IS_SYSTEM",nullable=true)
    public String getDictIsSystem() {
        return this.dictIsSystem;
    }

    public void setDictIsSystem(String dictIsSystem) {
        this.dictIsSystem = dictIsSystem;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="DICT_CODE"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaSysmanageDictitem"
     *         
     */
    @OneToMany(mappedBy="toaSysmanageDict",targetEntity=com.strongit.oa.bo.ToaSysmanageDictitem.class,cascade=CascadeType.ALL)
    public Set getToaSysmanageDictitems() {
        return this.toaSysmanageDictitems;
    }

    public void setToaSysmanageDictitems(Set toaSysmanageDictitems) {
        this.toaSysmanageDictitems = toaSysmanageDictitems;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("dictCode", getDictCode())
            .toString();
    }

}
