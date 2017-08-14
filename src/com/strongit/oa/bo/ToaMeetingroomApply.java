package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MEETINGROOM_APPLY"
 *     
*/
@Entity
@Table(name="T_OA_MEETINGROOM_APPLY")
public class ToaMeetingroomApply implements Serializable {

    /** 申请单编号 */
    private String maId;

    /** 申请人 */
    private String maCreater;
    
    /** 申请人 */
    private String maCreaterName;

    /** 发起部门 */
    private String maDepartment;

    /** 会议主持人 */
    private String maEmcee;

    /** 申请备注 */
    private String maRemark;

    /** 与会人员 */
    private String maJoinperson;

    /** 议题描述 */
    private String maMeetingdec;

    /** 申请时间 */
    private Date maSubmittime;

    /** 申请预定开始时间 */
    private Date maAppstarttime;

    /** 申请预定结束时间 */
    private Date maAppendtime;

    /** 实际结束时间*/
    private Date maRealendtime;

    /** 实际开始时间 */
    private Date maRealstarttime;

    /** 办理时间 */
    private Date maHandletime;

    /** 申请状态 */
    private String maState;
    
    /** 申请状态--新的申请单 */
    public final static String APPLY_NEW = "0";		//新申请
    /** 申请状态--审批通过的申请单 */
    public final static String APPLY_ALLOW = "1";	//审批通过
    /** 申请状态--审批不通过的申请单 */
    public final static String APPLY_DISALLOW = "2";//审批不通过
    /** 申请状态--结束使用的申请单 */
    public final static String APPLY_END = "3";		//结束使用
    
    private String departmentId;//DEPARTMENT_ID
	
	private String topOrgcode;// TOP_ORGCODE

    /** 会议室 */
    private com.strongit.oa.bo.ToaMeetingroom toaMeetingroom;

    /** full constructor */
    public ToaMeetingroomApply(String maId, String maCreater, String maCreaterName, String maDepartment, String maEmcee, String maRemark, String maJoinperson, String maMeetingdec, Date maSubmittime, Date maAppstarttime, Date maAppendtime, Date maRealendtime, Date maRealstarttime, Date maHandletime, String maState, com.strongit.oa.bo.ToaMeetingroom toaMeetingroom) {
        this.maId = maId;
        this.maCreater = maCreater;
        this.maCreaterName = maCreaterName;
        this.maDepartment = maDepartment;
        this.maEmcee = maEmcee;
        this.maRemark = maRemark;
        this.maJoinperson = maJoinperson;
        this.maMeetingdec = maMeetingdec;
        this.maSubmittime = maSubmittime;
        this.maAppstarttime = maAppstarttime;
        this.maAppendtime = maAppendtime;
        this.maRealendtime = maRealendtime;
        this.maRealstarttime = maRealstarttime;
        this.maHandletime = maHandletime;
        this.maState = maState;
        this.toaMeetingroom = toaMeetingroom;
    }

    /** default constructor */
    public ToaMeetingroomApply() {
    }

    /** minimal constructor */
    public ToaMeetingroomApply(String maId, com.strongit.oa.bo.ToaMeetingroom toaMeetingroom) {
        this.maId = maId;
        this.toaMeetingroom = toaMeetingroom;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MA_ID"
     *         
     */
    @Id
	@Column(name="MA_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getMaId() {
        return this.maId;
    }

    public void setMaId(String maId) {
        this.maId = maId;
    }

    /** 
     *            @hibernate.property
     *             column="MA_CREATER"
     *             length="255"
     *         
     */
    @Column(name="MA_CREATER",nullable=true)
    public String getMaCreater() {
        return this.maCreater;
    }

    public void setMaCreater(String maCreater) {
        this.maCreater = maCreater;
    }

    /** 
     *            @hibernate.property
     *             column="MA_CREATERNAME"
     *             length="255"
     *         
     */
    @Column(name="MA_CREATERNAME",nullable=true)
	public String getMaCreaterName() {
		return maCreaterName;
	}

	public void setMaCreaterName(String maCreaterName) {
		this.maCreaterName = maCreaterName;
	}
	
    /** 
     *            @hibernate.property
     *             column="MA_DEPARTMENT"
     *             length="255"
     *         
     */
    @Column(name="MA_DEPARTMENT",nullable=true)
    public String getMaDepartment() {
        return this.maDepartment;
    }

    public void setMaDepartment(String maDepartment) {
        this.maDepartment = maDepartment;
    }

    /** 
     *            @hibernate.property
     *             column="MA_EMCEE"
     *             length="255"
     *         
     */
    @Column(name="MA_EMCEE",nullable=true)
    public String getMaEmcee() {
        return this.maEmcee;
    }

    public void setMaEmcee(String maEmcee) {
        this.maEmcee = maEmcee;
    }

    /** 
     *            @hibernate.property
     *             column="MA_REMARK"
     *             length="4000"
     *         
     */
    @Column(name="MA_REMARK",nullable=true)
    public String getMaRemark() {
        return this.maRemark;
    }

    public void setMaRemark(String maRemark) {
        this.maRemark = maRemark;
    }

    /** 
     *            @hibernate.property
     *             column="MA_JOINPERSON"
     *             length="4000"
     *         
     */
    @Column(name="MA_JOINPERSON",nullable=true)
    public String getMaJoinperson() {
        return this.maJoinperson;
    }

    public void setMaJoinperson(String maJoinperson) {
        this.maJoinperson = maJoinperson;
    }

    /** 
     *            @hibernate.property
     *             column="MA_MEETINGDEC"
     *             length="4000"
     *         
     */
    @Column(name="MA_MEETINGDEC",nullable=true)
    public String getMaMeetingdec() {
        return this.maMeetingdec;
    }

    public void setMaMeetingdec(String maMeetingdec) {
        this.maMeetingdec = maMeetingdec;
    }

    /** 
     *            @hibernate.property
     *             column="MA_SUBMITTIME"
     *             length="7"
     *         
     */
    @Column(name="MA_SUBMITTIME",nullable=true)
    public Date getMaSubmittime() {
        return this.maSubmittime;
    }

    public void setMaSubmittime(Date maSubmittime) {
        this.maSubmittime = maSubmittime;
    }

    /** 
     *            @hibernate.property
     *             column="MA_APPSTARTTIME"
     *             length="7"
     *         
     */
    @Column(name="MA_APPSTARTTIME",nullable=true)
    public Date getMaAppstarttime() {
        return this.maAppstarttime;
    }

    public void setMaAppstarttime(Date maAppstarttime) {
        this.maAppstarttime = maAppstarttime;
    }

    /** 
     *            @hibernate.property
     *             column="MA_APPENDTIME"
     *             length="7"
     *         
     */
    @Column(name="MA_APPENDTIME",nullable=true)
    public Date getMaAppendtime() {
        return this.maAppendtime;
    }

    public void setMaAppendtime(Date maAppendtime) {
        this.maAppendtime = maAppendtime;
    }

    /** 
     *            @hibernate.property
     *             column="MA_REALENDTIME"
     *             length="7"
     *         
     */
    @Column(name="MA_REALENDTIME",nullable=true)
    public Date getMaRealendtime() {
        return this.maRealendtime;
    }

    public void setMaRealendtime(Date maRealendtime) {
        this.maRealendtime = maRealendtime;
    }

    /** 
     *            @hibernate.property
     *             column="MA_REALSTARTTIME"
     *             length="7"
     *         
     */
    @Column(name="MA_REALSTARTTIME",nullable=true)
    public Date getMaRealstarttime() {
        return this.maRealstarttime;
    }

    public void setMaRealstarttime(Date maRealstarttime) {
        this.maRealstarttime = maRealstarttime;
    }

    /** 
     *            @hibernate.property
     *             column="MA_HANDLETIME"
     *             length="7"
     *         
     */
    @Column(name="MA_HANDLETIME",nullable=true)
    public Date getMaHandletime() {
        return this.maHandletime;
    }

    public void setMaHandletime(Date maHandletime) {
        this.maHandletime = maHandletime;
    }

    /** 
     *            @hibernate.property
     *             column="MA_STATE"
     *             length="1"
     *         
     */
    @Column(name="MA_STATE",nullable=true)
    public String getMaState() {
        return this.maState;
    }

    public void setMaState(String maState) {
        this.maState = maState;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="MR_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="MR_ID")
    public com.strongit.oa.bo.ToaMeetingroom getToaMeetingroom() {
        return this.toaMeetingroom;
    }

    public void setToaMeetingroom(com.strongit.oa.bo.ToaMeetingroom toaMeetingroom) {
        this.toaMeetingroom = toaMeetingroom;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("maId", getMaId())
            .toString();
    }

    @Column(name = "DEPARTMENT_ID")
	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	@Column(name = "TOP_ORGCODE")
	public String getTopOrgcode() {
		return topOrgcode;
	}

	public void setTopOrgcode(String topOrgcode) {
		this.topOrgcode = topOrgcode;
	}

}
