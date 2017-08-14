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
 *         table="T_OA_INFOPUBLISH_COLUMN_PRIVIL"
 *     
*/
@Entity
@Table(name = "T_OA_INFOPUBLISH_COLUMN_PRIVIL")
public class ToaInfopublishColumnPrivil implements Serializable {

    /** identifier field */
	@Id
	@GeneratedValue
    private String columnPrivilId;

    /** nullable persistent field */
    private String columnPrivilUserid;

    /** nullable persistent field */
    private String columnPrivilGroupid;

    /** nullable persistent field */
    private String columnPrivilType;

    /** persistent field */
    private com.strongit.oa.bo.ToaInfopublishColumn toaInfopublishColumn;

    /** full constructor */
    public ToaInfopublishColumnPrivil(String columnPrivilId, String columnPrivilUserid, String columnPrivilGroupid, String columnPrivilType, com.strongit.oa.bo.ToaInfopublishColumn toaInfopublishColumn) {
        this.columnPrivilId = columnPrivilId;
        this.columnPrivilUserid = columnPrivilUserid;
        this.columnPrivilGroupid = columnPrivilGroupid;
        this.columnPrivilType = columnPrivilType;
        this.toaInfopublishColumn = toaInfopublishColumn;
    }

    /** default constructor */
    public ToaInfopublishColumnPrivil() {
    }

    /** minimal constructor */
    public ToaInfopublishColumnPrivil(String columnPrivilId, com.strongit.oa.bo.ToaInfopublishColumn toaInfopublishColumn) {
        this.columnPrivilId = columnPrivilId;
        this.toaInfopublishColumn = toaInfopublishColumn;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="COLUMN_PRIVIL_ID"
     *         
     */
    @Id
    @Column(name = "COLUMN_PRIVIL_ID", nullable = false, length = 32)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getColumnPrivilId() {
        return this.columnPrivilId;
    }

    public void setColumnPrivilId(String columnPrivilId) {
        this.columnPrivilId = columnPrivilId;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_PRIVIL_USERID"
     *             length="32"
     *         
     */
    @Column(name = "COLUMN_PRIVIL_USERID", nullable = true)
    public String getColumnPrivilUserid() {
        return this.columnPrivilUserid;
    }

    public void setColumnPrivilUserid(String columnPrivilUserid) {
        this.columnPrivilUserid = columnPrivilUserid;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_PRIVIL_GROUPID"
     *             length="32"
     *         
     */
    @Column(name = "COLUMN_PRIVIL_GROUPID", nullable = true)
    public String getColumnPrivilGroupid() {
        return this.columnPrivilGroupid;
    }

    public void setColumnPrivilGroupid(String columnPrivilGroupid) {
        this.columnPrivilGroupid = columnPrivilGroupid;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_PRIVIL_TYPE"
     *             length="1"
     *         
     */
    @Column(name = "COLUMN_PRIVIL_TYPE", nullable = true)
    public String getColumnPrivilType() {
        return this.columnPrivilType;
    }

    public void setColumnPrivilType(String columnPrivilType) {
        this.columnPrivilType = columnPrivilType;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="CLUMN_ID"         
     *         
     */
    @ManyToOne()
	@JoinColumn(name = "CLUMN_ID", nullable = true)
    public com.strongit.oa.bo.ToaInfopublishColumn getToaInfopublishColumn() {
        return this.toaInfopublishColumn;
    }

    public void setToaInfopublishColumn(com.strongit.oa.bo.ToaInfopublishColumn toaInfopublishColumn) {
        this.toaInfopublishColumn = toaInfopublishColumn;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("columnPrivilId", getColumnPrivilId())
            .toString();
    }

}
