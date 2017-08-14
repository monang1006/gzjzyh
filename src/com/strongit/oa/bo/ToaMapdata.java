package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MAPDATA"
 *         彭小青
 *     
*/
@Entity
@Table(name="T_OA_MAPDATA",catalog="",schema="")
public class ToaMapdata implements Serializable {

    /** identifier field */
    private String mapdataId;

    /** identifier field */
    private String parentTable;

    /** identifier field */
    private String subTable;

    /** identifier field */
    private String parentFiled;

    /** identifier field */
    private String subFiled;

    /** identifier field */
    private String subFileDesc;
    
    private String filedType;
    
    private String filedType2;
    
    private String filedSize;
    
    private String fileSize2;

    /** full constructor */
    public ToaMapdata(String mapdataId, String parentTable, String subTable, String parentFiled, String subFiled, String subFileDesc) {
        this.mapdataId = mapdataId;
        this.parentTable = parentTable;
        this.subTable = subTable;
        this.parentFiled = parentFiled;
        this.subFiled = subFiled;
        this.subFileDesc = subFileDesc;
    }

    /** default constructor */
    public ToaMapdata() {
    }

    /** 
     *                @hibernate.property
     *                 column="MAPDATA_ID"
     *                 length="32"
     *             
     */
	@Id
	@Column(name="MAPDATA_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getMapdataId() {
        return this.mapdataId;
    }

    public void setMapdataId(String mapdataId) {
        this.mapdataId = mapdataId;
    }

    /** 
     *                @hibernate.property
     *                 column="PARENT_TABLE"
     *                 length="100"
     *             
     */
    @Column(name="PARENT_TABLE",nullable=true)
    public String getParentTable() {
        return this.parentTable;
    }

    public void setParentTable(String parentTable) {
        this.parentTable = parentTable;
    }

    /** 
     *                @hibernate.property
     *                 column="SUB_TABLE"
     *                 length="100"
     *             
     */
    @Column(name="SUB_TABLE",nullable=true)
    public String getSubTable() {
        return this.subTable;
    }

    public void setSubTable(String subTable) {
        this.subTable = subTable;
    }

    /** 
     *                @hibernate.property
     *                 column="PARENT_FILED"
     *                 length="100"
     *             
     */
    @Column(name="PARENT_FILED",nullable=true)
    public String getParentFiled() {
        return this.parentFiled;
    }

    public void setParentFiled(String parentFiled) {
        this.parentFiled = parentFiled;
    }

    /** 
     *                @hibernate.property
     *                 column="SUB_FILED"
     *                 length="100"
     *             
     */
    @Column(name="SUB_FILED",nullable=true)
    public String getSubFiled() {
        return this.subFiled;
    }

    public void setSubFiled(String subFiled) {
        this.subFiled = subFiled;
    }

    /** 
     *                @hibernate.property
     *                 column="SUB_FILE_DESC"
     *                 length="200"
     *             
     */
    @Column(name="SUB_FILE_DESC",nullable=true)
    public String getSubFileDesc() {
        return this.subFileDesc;
    }

    public void setSubFileDesc(String subFileDesc) {
        this.subFileDesc = subFileDesc;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("mapdataId", getMapdataId())
            .append("parentTable", getParentTable())
            .append("subTable", getSubTable())
            .append("parentFiled", getParentFiled())
            .append("subFiled", getSubFiled())
            .append("subFileDesc", getSubFileDesc())
            .toString();
    }

    @Column(name="FILED_TYPE",nullable=true)
	public String getFiledType() {
		return filedType;
	}

	public void setFiledType(String filedType) {
		this.filedType = filedType;
	}

	 @Column(name="FILED_SIZE",nullable=true)
	public String getFiledSize() {
		return filedSize;
	}

	public void setFiledSize(String filedSize) {
		this.filedSize = filedSize;
	}

	 @Column(name="FILED_TYPE2",nullable=true)
	public String getFiledType2() {
		return filedType2;
	}

	public void setFiledType2(String filedType2) {
		this.filedType2 = filedType2;
	}

	 @Column(name="FILED_SIZE2",nullable=true)
	public String getFileSize2() {
		return fileSize2;
	}

	public void setFileSize2(String fileSize2) {
		this.fileSize2 = fileSize2;
	}

}
