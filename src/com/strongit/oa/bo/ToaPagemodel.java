package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_PAGEMODEL"
 *     
*/

@Entity
@Table(name="T_OA_PAGEMODEL")
public class ToaPagemodel implements Serializable {

	private static final long serialVersionUID = -4721263813441602636L;

	/** identifier field */
    private String pagemodelId;							//主键

    /** nullable persistent field */
    private String pagemodelDes;						//页面模型描述

    /** nullable persistent field */
    private String pagemodelParentid;					//页面层级关系父节点ID

    /** nullable persistent field */
    private String pagemodelVal;						//页面变量值

    /** nullable persistent field */
    private String pagemodelAction;						//页面模型页面名称

    /** nullable persistent field */
    private String pagemodelName;						//页面模型名（既模板文件名）

    /** nullable persistent field */
    private String pagemodelSavepath;					//模板对应生成文件所在文件夹路径
    
    private String pagemodelActionName;					//生成页面前置Action名称
    
    private String pagemodelValname;					//父级页面所在中对应变量名称
    
    private String pagemodelPagetype;					//页面类型
    
    private String pagemodelFramename;					//父级页面名称
    
    /** persistent field */
    private com.strongit.oa.bo.ToaForamula toaForamula;

    /** full constructor */
    public ToaPagemodel(String pagemodelId, String pagemodelDes, String pagemodelParentid, String pagemodelVal, String pagemodelAction, String pagemodelName, String pagemodelSavepath, String pagemodelActionName,String pagemodelPagetype,String pagemodelFramename,com.strongit.oa.bo.ToaForamula toaForamula) {
        this.pagemodelId = pagemodelId;
        this.pagemodelDes = pagemodelDes;
        this.pagemodelParentid = pagemodelParentid;
        this.pagemodelVal = pagemodelVal;
        this.pagemodelAction = pagemodelAction;
        this.pagemodelName = pagemodelName;
        this.pagemodelSavepath = pagemodelSavepath;
        this.pagemodelActionName = pagemodelActionName;
        this.pagemodelPagetype = pagemodelPagetype;
        this.pagemodelFramename	= pagemodelFramename;
        this.toaForamula = toaForamula;
    }

    /** default constructor */
    public ToaPagemodel() {
    }

    /** minimal constructor */
    public ToaPagemodel(String pagemodelId, com.strongit.oa.bo.ToaForamula toaForamula) {
        this.pagemodelId = pagemodelId;
        this.toaForamula = toaForamula;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="PAGEMODEL_ID"
     *         
     */
	@Id
	@Column(name="PAGEMODEL_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getPagemodelId() {
        return this.pagemodelId;
    }

    public void setPagemodelId(String pagemodelId) {
        this.pagemodelId = pagemodelId;
    }

    /** 
     *            @hibernate.property
     *             column="PAGEMODEL_DES"
     *             length="100"
     *         
     */
    @Column(name="PAGEMODEL_DES",nullable=true)
    public String getPagemodelDes() {
        return this.pagemodelDes;
    }

    public void setPagemodelDes(String pagemodelDes) {
        this.pagemodelDes = pagemodelDes;
    }

    /** 
     *            @hibernate.property
     *             column="PAGEMODEL_PARENTID"
     *             length="10"
     *         
     */
    @Column(name="PAGEMODEL_PARENTID",nullable=true)
    public String getPagemodelParentid() {
        return this.pagemodelParentid;
    }

    public void setPagemodelParentid(String pagemodelParentid) {
        this.pagemodelParentid = pagemodelParentid;
    }

    /** 
     *            @hibernate.property
     *             column="PAGEMODEL_VAL"
     *             length="30"
     *         
     */
    @Column(name="PAGEMODEL_VAL",nullable=true)
    public String getPagemodelVal() {
        return this.pagemodelVal;
    }

    public void setPagemodelVal(String pagemodelVal) {
        this.pagemodelVal = pagemodelVal;
    }

    /** 
     *            @hibernate.property
     *             column="PAGEMODEL_ACTION"
     *             length="200"
     *         
     */
    @Column(name="PAGEMODEL_ACTION",nullable=true)
    public String getPagemodelAction() {
        return this.pagemodelAction;
    }

    public void setPagemodelAction(String pagemodelAction) {
        this.pagemodelAction = pagemodelAction;
    }

    /** 
     *            @hibernate.property
     *             column="PAGEMODEL_NAME"
     *             length="100"
     *         
     */
    @Column(name="PAGEMODEL_NAME",nullable=true)
    public String getPagemodelName() {
        return this.pagemodelName;
    }

    public void setPagemodelName(String pagemodelName) {
        this.pagemodelName = pagemodelName;
    }

    /** 
     *            @hibernate.property
     *             column="PAGEMODEL_SAVEPATH"
     *             length="100"
     *         
     */
    @Column(name="PAGEMODEL_SAVEPATH",nullable=true)
    public String getPagemodelSavepath() {
        return this.pagemodelSavepath;
    }

    public void setPagemodelSavepath(String pagemodelSavepath) {
        this.pagemodelSavepath = pagemodelSavepath;
    }
    
    @Column(name="PAGEMODEL_ACTIONNAME",nullable=true)
	public String getPagemodelActionName() {
		return pagemodelActionName;
	}

	public void setPagemodelActionName(String pagemodelActionName) {
		this.pagemodelActionName = pagemodelActionName;
	}

	@Column(name="PAGEMODEL_VALNAME",nullable=true)
	public String getPagemodelValname() {
		return pagemodelValname;
	}

	public void setPagemodelValname(String pagemodelValname) {
		this.pagemodelValname = pagemodelValname;
	}
	
	@Column(name="PAGEMODEL_PAGETYPE",nullable=true)
	public String getPagemodelPagetype() {
		return pagemodelPagetype;
	}

	public void setPagemodelPagetype(String pagemodelPagetype) {
		this.pagemodelPagetype = pagemodelPagetype;
	}
	
	@Column(name="PAGEMODEL_FRAMENAME",nullable=true)
	public String getPagemodelFramename() {
		return pagemodelFramename;
	}

	public void setPagemodelFramename(String pagemodelFramename) {
		this.pagemodelFramename = pagemodelFramename;
	}

	
    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="FORAMULA_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="FORAMULA_ID", nullable=false)
    public com.strongit.oa.bo.ToaForamula getToaForamula() {
        return this.toaForamula;
    }

    public void setToaForamula(com.strongit.oa.bo.ToaForamula toaForamula) {
        this.toaForamula = toaForamula;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("pagemodelId", getPagemodelId())
            .toString();
    }

}
