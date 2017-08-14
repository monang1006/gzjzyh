package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_PROPERTY"
 *     
*/
@Entity
@Table(name="T_OA_SYSMANAGE_PROPERTY")
public class ToaSysmanageProperty implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String infoItemCode;

    /** nullable persistent field */
    private String infoItemField;

    /** nullable persistent field */
    private String infoItemDatatype;

    /** nullable persistent field */
    private String infoItemLength;

    /** nullable persistent field */
    private String infoItemDecimal;

    /** nullable persistent field */
    private String infoItemDefaultvalue;

    /** nullable persistent field */
    private String infoItemDescription;

    /** nullable persistent field */
    private String infoItemCondition;

    /** nullable persistent field */
    private String infoItemState;

    /** nullable persistent field */
    private Long infoItemOrderby;

    /** nullable persistent field */
    private String infoItemSeconddisplay;

    /** nullable persistent field */
    private String infoItemFlag;

    /** nullable persistent field */
    private String infoItemProset;

    /** nullable persistent field */
    private String infoItemDictCode;

    /** nullable persistent field */
    private String infoItemIsSystem;

    /** nullable persistent field */
    private String properTypeId;
    
    private String properTypeName;
    
    private String infoItemValue;
    
    private String systemIdentify;
    
    private String refernceDesc;//字典引用值
    
    private String mapFiled;	//数据映射模块使用
    
    private String mapFildDesc; //数据映射模块使用
    
    private String mapFildType;	//数据映射模块使用
    
    private String mapFildSize;	//数据映射模块使用
    
    private String isQuery ; //是否作为查询字段 added by dengzc 2010年11月9日16:14:38
    
    private String isView ;//是否作为展现字段   added by dengzc 2010年11月9日16:14:46

    /** persistent field */
    private com.strongit.oa.bo.ToaSysmanageStructure toaSysmanageStructure;
    
    private Set toaPostProperty;

    /** full constructor */
    public ToaSysmanageProperty(String infoItemCode, String infoItemField, String infoItemDatatype, String infoItemLength, String infoItemDecimal, String infoItemDefaultvalue, String infoItemDescription, String infoItemCondition, String infoItemState, Long infoItemOrderby, String infoItemSeconddisplay, String infoItemFlag, String infoItemProset, String infoItemDictCode, String infoItemIsSystem, String properTypeId, com.strongit.oa.bo.ToaSysmanageStructure toaSysmanageStructure) {
        this.infoItemCode = infoItemCode;
        this.infoItemField = infoItemField;
        this.infoItemDatatype = infoItemDatatype;
        this.infoItemLength = infoItemLength;
        this.infoItemDecimal = infoItemDecimal;
        this.infoItemDefaultvalue = infoItemDefaultvalue;
        this.infoItemDescription = infoItemDescription;
        this.infoItemCondition = infoItemCondition;
        this.infoItemState = infoItemState;
        this.infoItemOrderby = infoItemOrderby;
        this.infoItemSeconddisplay = infoItemSeconddisplay;
        this.infoItemFlag = infoItemFlag;
        this.infoItemProset = infoItemProset;
        this.infoItemDictCode = infoItemDictCode;
        this.infoItemIsSystem = infoItemIsSystem;
        this.properTypeId = properTypeId;
        this.toaSysmanageStructure = toaSysmanageStructure;
    }

    /** default constructor */
    public ToaSysmanageProperty() {
    }

    /** minimal constructor */
    public ToaSysmanageProperty(String infoItemCode, com.strongit.oa.bo.ToaSysmanageStructure toaSysmanageStructure) {
        this.infoItemCode = infoItemCode;
        this.toaSysmanageStructure = toaSysmanageStructure;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="INFO_ITEM_CODE"
     *         
     */
    @Id
	@Column(name="INFO_ITEM_CODE",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getInfoItemCode() {
        return this.infoItemCode;
    }

    public void setInfoItemCode(String infoItemCode) {
        this.infoItemCode = infoItemCode;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_FIELD"
     *             length="50"
     *         
     */
    @Column(name="INFO_ITEM_FIELD",nullable=true)
    public String getInfoItemField() {
        return this.infoItemField;
    }

    public void setInfoItemField(String infoItemField) {
        this.infoItemField = infoItemField;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_DATATYPE"
     *             length="100"
     *         
     */
    @Column(name="INFO_ITEM_DATATYPE",nullable=true)
    public String getInfoItemDatatype() {
        return this.infoItemDatatype;
    }

    public void setInfoItemDatatype(String infoItemDatatype) {
        this.infoItemDatatype = infoItemDatatype;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_LENGTH"
     *             length="4"
     *         
     */
    @Column(name="INFO_ITEM_LENGTH",nullable=true)
    public String getInfoItemLength() {
        return this.infoItemLength;
    }

    public void setInfoItemLength(String infoItemLength) {
        this.infoItemLength = infoItemLength;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_DECIMAL"
     *             length="2"
     *         
     */
    @Column(name="INFO_ITEM_DECIMAL",nullable=true)
    public String getInfoItemDecimal() {
        return this.infoItemDecimal;
    }

    public void setInfoItemDecimal(String infoItemDecimal) {
        this.infoItemDecimal = infoItemDecimal;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_DEFAULTVALUE"
     *             length="400"
     *         
     */
    @Column(name="INFO_ITEM_DEFAULTVALUE",nullable=true)
    public String getInfoItemDefaultvalue() {
        return this.infoItemDefaultvalue;
    }

    public void setInfoItemDefaultvalue(String infoItemDefaultvalue) {
        this.infoItemDefaultvalue = infoItemDefaultvalue;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_DESCRIPTION"
     *             length="400"
     *         
     */
    @Column(name="INFO_ITEM_DESCRIPTION",nullable=true)
    public String getInfoItemDescription() {
        return this.infoItemDescription;
    }

    public void setInfoItemDescription(String infoItemDescription) {
        this.infoItemDescription = infoItemDescription;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_CONDITION"
     *             length="50"
     *         
     */
    @Column(name="INFO_ITEM_CONDITION",nullable=true)
    public String getInfoItemCondition() {
        return this.infoItemCondition;
    }

    public void setInfoItemCondition(String infoItemCondition) {
        this.infoItemCondition = infoItemCondition;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_STATE"
     *             length="1"
     *         
     */
    @Column(name="INFO_ITEM_STATE",nullable=true)
    public String getInfoItemState() {
        return this.infoItemState;
    }

    public void setInfoItemState(String infoItemState) {
        this.infoItemState = infoItemState;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_ORDERBY"
     *             length="10"
     *         
     */
    @Column(name="INFO_ITEM_ORDERBY",nullable=true)
    public Long getInfoItemOrderby() {
        return this.infoItemOrderby;
    }

    public void setInfoItemOrderby(Long infoItemOrderby) {
        this.infoItemOrderby = infoItemOrderby;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_SECONDDISPLAY"
     *             length="100"
     *         
     */
    @Column(name="INFO_ITEM_SECONDDISPLAY",nullable=true)
    public String getInfoItemSeconddisplay() {
        return this.infoItemSeconddisplay;
    }

    public void setInfoItemSeconddisplay(String infoItemSeconddisplay) {
        this.infoItemSeconddisplay = infoItemSeconddisplay;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_FLAG"
     *             length="1"
     *         
     */
    @Column(name="INFO_ITEM_FLAG",nullable=true)
    public String getInfoItemFlag() {
        return this.infoItemFlag;
    }

    public void setInfoItemFlag(String infoItemFlag) {
        this.infoItemFlag = infoItemFlag;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_PROSET"
     *             length="1"
     *         
     */
    @Column(name="INFO_ITEM_PROSET",nullable=true)
    public String getInfoItemProset() {
        return this.infoItemProset;
    }

    public void setInfoItemProset(String infoItemProset) {
        this.infoItemProset = infoItemProset;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_DICT_CODE"
     *             length="10"
     *         
     */
    @Column(name="INFO_ITEM_DICT_CODE",nullable=true)
    public String getInfoItemDictCode() {
        return this.infoItemDictCode;
    }

    public void setInfoItemDictCode(String infoItemDictCode) {
        this.infoItemDictCode = infoItemDictCode;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_IS_SYSTEM"
     *             length="1"
     *         
     */
    @Column(name="INFO_ITEM_IS_SYSTEM",nullable=true)
    public String getInfoItemIsSystem() {
        return this.infoItemIsSystem;
    }

    public void setInfoItemIsSystem(String infoItemIsSystem) {
        this.infoItemIsSystem = infoItemIsSystem;
    }

    /** 
     *            @hibernate.property
     *             column="PROPER_TYPE_ID"
     *             length="32"
     *         
     */
    @Column(name="PROPER_TYPE_ID",nullable=true)
    public String getProperTypeId() {
        return this.properTypeId;
    }

    public void setProperTypeId(String properTypeId) {
        this.properTypeId = properTypeId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="INFO_SET_CODE"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="INFO_SET_CODE", nullable=false)
    public com.strongit.oa.bo.ToaSysmanageStructure getToaSysmanageStructure() {
        return this.toaSysmanageStructure;
    }

    public void setToaSysmanageStructure(com.strongit.oa.bo.ToaSysmanageStructure toaSysmanageStructure) {
        this.toaSysmanageStructure = toaSysmanageStructure;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("infoItemCode", getInfoItemCode())
            .toString();
    }

    @Transient
	public String getProperTypeName() {
		return properTypeName;
	}

	public void setProperTypeName(String properTypeName) {
		this.properTypeName = properTypeName;
	}

	@Transient
	public String getInfoItemValue() {
		return infoItemValue;
	}

	public void setInfoItemValue(String infoItemValue) {
		this.infoItemValue = infoItemValue;
	}

	@Transient
	public String getRefernceDesc() {
		return refernceDesc;
	}

	public void setRefernceDesc(String refernceDesc) {
		this.refernceDesc = refernceDesc;
	}

	@Transient
	public String getMapFildDesc() {
		return mapFildDesc;
	}

	public void setMapFildDesc(String mapFildDesc) {
		this.mapFildDesc = mapFildDesc;
	}

	@Transient
	public String getMapFiled() {
		return mapFiled;
	}


	public void setMapFiled(String mapFiled) {
		this.mapFiled = mapFiled;
	}

	@Transient
	public String getMapFildSize() {
		return mapFildSize;
	}

	public void setMapFildSize(String mapFildSize) {
		this.mapFildSize = mapFildSize;
	}

	@Transient
	public String getMapFildType() {
		return mapFildType;
	}

	public void setMapFildType(String mapFildType) {
		this.mapFildType = mapFildType;
	}

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="INFO_ITEM_CODE"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaPostProperty"
     *         
     */
    @OneToMany(mappedBy="infoItem",targetEntity=com.strongit.oa.bo.ToaPostProperty.class,cascade=CascadeType.ALL)
	public Set getToaPostProperty() {
		return toaPostProperty;
	}

	public void setToaPostProperty(Set toaPostProperty) {
		this.toaPostProperty = toaPostProperty;
	}

	@Column(name="SYSTEM_IDENTIFY",nullable=true)
	public String getSystemIdentify() {
		return systemIdentify;
	}

	public void setSystemIdentify(String systemIdentify) {
		this.systemIdentify = systemIdentify;
	}

	@Column(name = "INFO_ITEM_ISQUERY")
	public String getIsQuery() {
		return isQuery;
	}

	public void setIsQuery(String isQuery) {
		this.isQuery = isQuery;
	}

	@Column(name = "INFO_ITEM_ISSHOW")
	public String getIsView() {
		return isView;
	}

	public void setIsView(String isView) {
		this.isView = isView;
	}

}
