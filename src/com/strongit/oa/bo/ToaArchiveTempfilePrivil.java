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
 *         table="T_OA_ARCHIVE_TEMPFILE_PRIVIL"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_TEMPFILE_PRIVIL")
public class ToaArchiveTempfilePrivil implements Serializable {

    /** identifier field 权限ID*/
	@Id 
	@GeneratedValue
    private String tempfileprivilId;

    /** nullable persistent field 用户ID*/
    private String tempfileprivilUser;

    /** nullable persistent field 组织ID ：暂不使用*/
    private String tempfileprivilOrg;

    /** nullable persistent field 用户组：暂不使用*/
    private String tempfileprivilUsergroup;

    /** nullable persistent field 权限类型：暂不使用*/
    private String tempfileprivilType;

    /** nullable persistent field 描述*/
    private String tempfileprivilDesc;

    /** persistent field */
    private com.strongit.oa.bo.ToaArchiveTempfile toaArchiveTempfile;

    /** full constructor */
    public ToaArchiveTempfilePrivil(String tempfileprivilId, String tempfileprivilUser, String tempfileprivilOrg, String tempfileprivilUsergroup, String tempfileprivilType, String tempfileprivilDesc, com.strongit.oa.bo.ToaArchiveTempfile toaArchiveTempfile) {
        this.tempfileprivilId = tempfileprivilId;
        this.tempfileprivilUser = tempfileprivilUser;
        this.tempfileprivilOrg = tempfileprivilOrg;
        this.tempfileprivilUsergroup = tempfileprivilUsergroup;
        this.tempfileprivilType = tempfileprivilType;
        this.tempfileprivilDesc = tempfileprivilDesc;
        this.toaArchiveTempfile = toaArchiveTempfile;
    }

    /** default constructor */
    public ToaArchiveTempfilePrivil() {
    }

    /** minimal constructor */
    public ToaArchiveTempfilePrivil(String tempfileprivilId, com.strongit.oa.bo.ToaArchiveTempfile toaArchiveTempfile) {
        this.tempfileprivilId = tempfileprivilId;
        this.toaArchiveTempfile = toaArchiveTempfile;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="TEMPFILEPRIVIL_ID"
     *         
     */
    @Id
	@Column(name="TEMPFILEPRIVIL_ID",nullable=false)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getTempfileprivilId() {
        return this.tempfileprivilId;
    }

    public void setTempfileprivilId(String tempfileprivilId) {
        this.tempfileprivilId = tempfileprivilId;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILEPRIVIL_USER"
     *             length="32"
     *         
     */
    @Column(name="TEMPFILEPRIVIL_USER",nullable=true)
    public String getTempfileprivilUser() {
        return this.tempfileprivilUser;
    }

    public void setTempfileprivilUser(String tempfileprivilUser) {
        this.tempfileprivilUser = tempfileprivilUser;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILEPRIVIL_ORG"
     *             length="32"
     *         
     */
    @Column(name="TEMPFILEPRIVIL_ORG",nullable=true)
    public String getTempfileprivilOrg() {
        return this.tempfileprivilOrg;
    }

    public void setTempfileprivilOrg(String tempfileprivilOrg) {
        this.tempfileprivilOrg = tempfileprivilOrg;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILEPRIVIL_USERGROUP"
     *             length="32"
     *         
     */
    @Column(name="TEMPFILEPRIVIL_USERGROUP",nullable=true)
    public String getTempfileprivilUsergroup() {
        return this.tempfileprivilUsergroup;
    }

    public void setTempfileprivilUsergroup(String tempfileprivilUsergroup) {
        this.tempfileprivilUsergroup = tempfileprivilUsergroup;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILEPRIVIL_TYPE"
     *             length="1"
     *         
     */
    @Column(name="TEMPFILEPRIVIL_TYPE",nullable=true)
    public String getTempfileprivilType() {
        return this.tempfileprivilType;
    }

    public void setTempfileprivilType(String tempfileprivilType) {
        this.tempfileprivilType = tempfileprivilType;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILEPRIVIL_DESC"
     *             length="400"
     *         
     */
    @Column(name="TEMPFILEPRIVIL_DESC",nullable=true)
    public String getTempfileprivilDesc() {
        return this.tempfileprivilDesc;
    }

    public void setTempfileprivilDesc(String tempfileprivilDesc) {
        this.tempfileprivilDesc = tempfileprivilDesc;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="TEMPFILE_ID"         
     *         
     */

    @ManyToOne()
	@JoinColumn(name="TEMPFILE_ID")
    public com.strongit.oa.bo.ToaArchiveTempfile getToaArchiveTempfile() {
        return this.toaArchiveTempfile;
    }

    public void setToaArchiveTempfile(com.strongit.oa.bo.ToaArchiveTempfile toaArchiveTempfile) {
        this.toaArchiveTempfile = toaArchiveTempfile;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("tempfileprivilId", getTempfileprivilId())
            .toString();
    }

}
