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
 *         table="T_OA_SYSMANAGE_FILTRATE_MODULE"
 *     
*/
@Entity
@Table(name="T_OA_SYSMANAGE_FILTRATE_MODULE")
public class ToaSysmanageFiltrateModule implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String filtrateModuleId;

    /** nullable persistent field */
    private String filtrateOpenstate;

    /** nullable persistent field */
    private String filtrateAuditing;

    /** nullable persistent field */
    private String filtrateMsgMethod;

    /** nullable persistent field */
    private String filtrateStopMsg;

    /** nullable persistent field */
    private String filtrateAuditingMsg;

    /** nullable persistent field */
    private String filtrateMsg;

    /** nullable persistent field */
    private String moduleId;

    /** full constructor */
    public ToaSysmanageFiltrateModule(String filtrateModuleId, String filtrateOpenstate, String filtrateAuditing, String filtrateMsgMethod, String filtrateStopMsg, String filtrateAuditingMsg, String filtrateMsg, String moduleId) {
        this.filtrateModuleId = filtrateModuleId;
        this.filtrateOpenstate = filtrateOpenstate;
        this.filtrateAuditing = filtrateAuditing;
        this.filtrateMsgMethod = filtrateMsgMethod;
        this.filtrateStopMsg = filtrateStopMsg;
        this.filtrateAuditingMsg = filtrateAuditingMsg;
        this.filtrateMsg = filtrateMsg;
        this.moduleId = moduleId;
    }

    /** default constructor */
    public ToaSysmanageFiltrateModule() {
    }

    /** minimal constructor */
    public ToaSysmanageFiltrateModule(String filtrateModuleId) {
        this.filtrateModuleId = filtrateModuleId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="FILTRATE_MODULE_ID"
     *         
     */
    @Id
	@Column(name="FILTRATE_MODULE_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getFiltrateModuleId() {
        return this.filtrateModuleId;
    }

    public void setFiltrateModuleId(String filtrateModuleId) {
        this.filtrateModuleId = filtrateModuleId;
    }

    /** 
     *            @hibernate.property
     *             column="FILTRATE_OPENSTATE"
     *             length="1"
     *         
     */
    @Column(name="FILTRATE_OPENSTATE",nullable=true)
    public String getFiltrateOpenstate() {
        return this.filtrateOpenstate;
    }

    public void setFiltrateOpenstate(String filtrateOpenstate) {
        this.filtrateOpenstate = filtrateOpenstate;
    }

    /** 
     *            @hibernate.property
     *             column="FILTRATE_AUDITING"
     *             length="100"
     *         
     */
    @Column(name="FILTRATE_AUDITING",nullable=true)
    public String getFiltrateAuditing() {
        return this.filtrateAuditing;
    }

    public void setFiltrateAuditing(String filtrateAuditing) {
        this.filtrateAuditing = filtrateAuditing;
    }

    /** 
     *            @hibernate.property
     *             column="FILTRATE_MSG_METHOD"
     *             length="1"
     *         
     */
    @Column(name="FILTRATE_MSG_METHOD",nullable=true)
    public String getFiltrateMsgMethod() {
        return this.filtrateMsgMethod;
    }

    public void setFiltrateMsgMethod(String filtrateMsgMethod) {
        this.filtrateMsgMethod = filtrateMsgMethod;
    }

    /** 
     *            @hibernate.property
     *             column="FILTRATE_STOP_MSG"
     *             length="400"
     *         
     */
    @Column(name="FILTRATE_STOP_MSG",nullable=true)
    public String getFiltrateStopMsg() {
        return this.filtrateStopMsg;
    }

    public void setFiltrateStopMsg(String filtrateStopMsg) {
        this.filtrateStopMsg = filtrateStopMsg;
    }

    /** 
     *            @hibernate.property
     *             column="FILTRATE_AUDITING_MSG"
     *             length="400"
     *         
     */
    @Column(name="FILTRATE_AUDITING_MSG",nullable=true)
    public String getFiltrateAuditingMsg() {
        return this.filtrateAuditingMsg;
    }

    public void setFiltrateAuditingMsg(String filtrateAuditingMsg) {
        this.filtrateAuditingMsg = filtrateAuditingMsg;
    }

    /** 
     *            @hibernate.property
     *             column="FILTRATE_MSG"
     *             length="400"
     *         
     */
    @Column(name="FILTRATE_MSG",nullable=true)
    public String getFiltrateMsg() {
        return this.filtrateMsg;
    }

    public void setFiltrateMsg(String filtrateMsg) {
        this.filtrateMsg = filtrateMsg;
    }

    /** 
     *            @hibernate.property
     *             column="MODULE_ID"
     *             length="32"
     *         
     */
    @Column(name="MODULE_ID",nullable=true)
    public String getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("filtrateModuleId", getFiltrateModuleId())
            .toString();
    }

}
