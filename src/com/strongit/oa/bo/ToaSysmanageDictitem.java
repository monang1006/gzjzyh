package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_DICTITEM"
 *     
*/
@Entity
@Table(name="T_OA_SYSMANAGE_DICTITEM",catalog="",schema="")
public class ToaSysmanageDictitem implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String dictItemCode;

    /** nullable persistent field */
    private String dictItemValue;

    /** nullable persistent field */
    private String dictItemShortdesc;

    /** nullable persistent field */
    private String dictItemParentvalue;

    /** nullable persistent field */
    private String dictItemIsExist;

    /** nullable persistent field */
    private String dictItemDescspell;

    /** nullable persistent field */
    private Integer dictItemFlag;

    /** nullable persistent field */
    private String dictItemName;

    /** nullable persistent field */
    private String dictItemIsSystem;

    /** nullable persistent field */
    private Integer dictItemIsSelect;

    /** persistent field */
    private com.strongit.oa.bo.ToaSysmanageDict toaSysmanageDict;

    /** full constructor */
    public ToaSysmanageDictitem(String dictItemCode, String dictItemValue, String dictItemShortdesc, String dictItemParentvalue, String dictItemIsExist, String dictItemDescspell, Integer dictItemFlag, String dictItemName, String dictItemIsSystem, Integer dictItemIsSelect, com.strongit.oa.bo.ToaSysmanageDict toaSysmanageDict) {
        this.dictItemCode = dictItemCode;
        this.dictItemValue = dictItemValue;
        this.dictItemShortdesc = dictItemShortdesc;
        this.dictItemParentvalue = dictItemParentvalue;
        this.dictItemIsExist = dictItemIsExist;
        this.dictItemDescspell = dictItemDescspell;
        this.dictItemFlag = dictItemFlag;
        this.dictItemName = dictItemName;
        this.dictItemIsSystem = dictItemIsSystem;
        this.dictItemIsSelect = dictItemIsSelect;
        this.toaSysmanageDict = toaSysmanageDict;
    }

    /** default constructor */
    public ToaSysmanageDictitem() {
    }

    /** minimal constructor */
    public ToaSysmanageDictitem(String dictItemCode, com.strongit.oa.bo.ToaSysmanageDict toaSysmanageDict) {
        this.dictItemCode = dictItemCode;
        this.toaSysmanageDict = toaSysmanageDict;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DICT_ITEM_CODE"
     *         
     */
    @Id
	@Column(name="DICT_ITEM_CODE",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDictItemCode() {
        return this.dictItemCode;
    }

    public void setDictItemCode(String dictItemCode) {
        this.dictItemCode = dictItemCode;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_ITEM_VALUE"
     *             length="100"
     *         
     */
    @Column(name="DICT_ITEM_VALUE",nullable=true)
    public String getDictItemValue() {
        return this.dictItemValue;
    }

    public void setDictItemValue(String dictItemValue) {
        this.dictItemValue = dictItemValue;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_ITEM_SHORTDESC"
     *             length="100"
     *         
     */
    @Column(name="DICT_ITEM_SHORTDESC",nullable=true)
    public String getDictItemShortdesc() {
        return this.dictItemShortdesc;
    }

    public void setDictItemShortdesc(String dictItemShortdesc) {
        this.dictItemShortdesc = dictItemShortdesc;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_ITEM_PARENTVALUE"
     *             length="20"
     *         
     */
    @Column(name="DICT_ITEM_PARENTVALUE",nullable=true)
    public String getDictItemParentvalue() {
        return this.dictItemParentvalue;
    }

    public void setDictItemParentvalue(String dictItemParentvalue) {
        this.dictItemParentvalue = dictItemParentvalue;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_ITEM_IS_EXIST"
     *             length="1"
     *         
     */
    @Column(name="DICT_ITEM_IS_EXIST",nullable=true)
    public String getDictItemIsExist() {
        return this.dictItemIsExist;
    }

    public void setDictItemIsExist(String dictItemIsExist) {
        this.dictItemIsExist = dictItemIsExist;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_ITEM_DESCSPELL"
     *             length="50"
     *         
     */
    @Column(name="DICT_ITEM_DESCSPELL",nullable=true)
    public String getDictItemDescspell() {
        return this.dictItemDescspell;
    }

    public void setDictItemDescspell(String dictItemDescspell) {
        this.dictItemDescspell = dictItemDescspell;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_ITEM_FLAG"
     *             length="1"
     *         
     */
    @Column(name="DICT_ITEM_FLAG",nullable=true)
    public Integer getDictItemFlag() {
        return this.dictItemFlag;
    }

    public void setDictItemFlag(Integer dictItemFlag) {
        this.dictItemFlag = dictItemFlag;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_ITEM_NAME"
     *             length="100"
     *         
     */
    @Column(name="DICT_ITEM_NAME",nullable=true)
    public String getDictItemName() {
        return this.dictItemName;
    }

    public void setDictItemName(String dictItemName) {
        this.dictItemName = dictItemName;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_ITEM_IS_SYSTEM"
     *             length="1"
     *         
     */
    @Column(name="DICT_ITEM_IS_SYSTEM",nullable=true)
    public String getDictItemIsSystem() {
        return this.dictItemIsSystem;
    }

    public void setDictItemIsSystem(String dictItemIsSystem) {
        this.dictItemIsSystem = dictItemIsSystem;
    }

    /** 
     *            @hibernate.property
     *             column="DICT_ITEM_IS_SELECT"
     *             length="1"
     *         
     */
    @Column(name="DICT_ITEM_IS_SELECT",nullable=true)
    public Integer getDictItemIsSelect() {
        return this.dictItemIsSelect;
    }

    public void setDictItemIsSelect(Integer dictItemIsSelect) {
        this.dictItemIsSelect = dictItemIsSelect;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="DICT_CODE"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="DICT_CODE", nullable=false)
    public com.strongit.oa.bo.ToaSysmanageDict getToaSysmanageDict() {
        return this.toaSysmanageDict;
    }

    public void setToaSysmanageDict(com.strongit.oa.bo.ToaSysmanageDict toaSysmanageDict) {
        this.toaSysmanageDict = toaSysmanageDict;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("dictItemCode", getDictItemCode())
            .toString();
    }

}
