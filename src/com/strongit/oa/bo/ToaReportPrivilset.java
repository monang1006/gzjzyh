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
 *         table="T_OA_REPORT_PRIVILSET"
 *     
*/
@Entity
@Table(name="T_OA_REPORT_PRIVILSET")
public class ToaReportPrivilset implements Serializable {

    /** 权限设置ID*/
    private String privilsetId;

    /** 权限设置类别名称 */
    private String privilsetTypename;

    /** 权限设置类别ID */
    private String privilsetTypeid;

    /** 权限设置类别标识 */
    private String privilsetTypeflag;	//0：所有人；1：部门；2：角色

    /** 安全级别 */
    private String privilsetLevel;

    /** 报表定义  */
    private com.strongit.oa.bo.ToaReportDefinition toaReportDefinition;

    /** full constructor */
    public ToaReportPrivilset(String privilsetId, String privilsetTypename, String privilsetTypeid, String privilsetTypeflag, String privilsetLevel, com.strongit.oa.bo.ToaReportDefinition toaReportDefinition) {
        this.privilsetId = privilsetId;
        this.privilsetTypename = privilsetTypename;
        this.privilsetTypeid = privilsetTypeid;
        this.privilsetTypeflag = privilsetTypeflag;
        this.privilsetLevel = privilsetLevel;
        this.toaReportDefinition = toaReportDefinition;
    }

    /** default constructor */
    public ToaReportPrivilset() {
    }

    /** minimal constructor */
    public ToaReportPrivilset(String privilsetId, com.strongit.oa.bo.ToaReportDefinition toaReportDefinition) {
        this.privilsetId = privilsetId;
        this.toaReportDefinition = toaReportDefinition;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="PRIVILSET_ID"
     *         
     */
    @Id
	@Column(name="PRIVILSET_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getPrivilsetId() {
        return this.privilsetId;
    }

    public void setPrivilsetId(String privilsetId) {
        this.privilsetId = privilsetId;
    }

    /** 
     *            @hibernate.property
     *             column="PRIVILSET_TYPENAME"
     *             length="255"
     *         
     */
    @Column(name="PRIVILSET_TYPENAME",nullable=true)
    public String getPrivilsetTypename() {
        return this.privilsetTypename;
    }

    public void setPrivilsetTypename(String privilsetTypename) {
        this.privilsetTypename = privilsetTypename;
    }

    /** 
     *            @hibernate.property
     *             column="PRIVILSET_TYPEID"
     *             length="32"
     *         
     */
    @Column(name="PRIVILSET_TYPEID",nullable=true)
    public String getPrivilsetTypeid() {
        return this.privilsetTypeid;
    }

    public void setPrivilsetTypeid(String privilsetTypeid) {
        this.privilsetTypeid = privilsetTypeid;
    }

    /** 
     *            @hibernate.property
     *             column="PRIVILSET_TYPEFLAG"
     *             length="2"
     *         
     */
    @Column(name="PRIVILSET_TYPEFLAG",nullable=true)
    public String getPrivilsetTypeflag() {
        return this.privilsetTypeflag;
    }

    public void setPrivilsetTypeflag(String privilsetTypeflag) {
        this.privilsetTypeflag = privilsetTypeflag;
    }

    /** 
     *            @hibernate.property
     *             column="PRIVILSET_LEVEL"
     *             length="10"
     *         
     */
    @Column(name="PRIVILSET_LEVEL",nullable=true)
    public String getPrivilsetLevel() {
        return this.privilsetLevel;
    }

    public void setPrivilsetLevel(String privilsetLevel) {
        this.privilsetLevel = privilsetLevel;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="DEFINITION_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="DEFINITION_ID", nullable=true)
    public com.strongit.oa.bo.ToaReportDefinition getToaReportDefinition() {
        return this.toaReportDefinition;
    }

    public void setToaReportDefinition(com.strongit.oa.bo.ToaReportDefinition toaReportDefinition) {
        this.toaReportDefinition = toaReportDefinition;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("privilsetId", getPrivilsetId())
            .toString();
    }

}
