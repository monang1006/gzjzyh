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


/** 销假说明单
 *        @hibernate.class
 *         table="T_OA_ATTEND_CANCLE"
 *     
*/
@Entity
@Table(name = "T_OA_ATTEND_CANCLE", catalog = "", schema = "")
public class ToaAttendCancle implements Serializable {

    /** identifier field */
    private String cancleId;

    /** nullable persistent field */
    private String cancleReason;//销假原因

    /** nullable persistent field */
    private Date cancleTime;//销假时间

    /** persistent field */
    private com.strongit.oa.bo.ToaAttenApply toaAttenApply;//申请单
    
    private Date cancleStime;//销假开始时间
    

    /** nullable persistent field */
    private Date cancleEtime;//销假结束时间
    
    private String cancleState;//销假状态。0：未提交；1：审核中；2：已审核
    
   private String cancleFormId;//销假表单ID 

    /** full constructor */
    public ToaAttendCancle(String cancleId, String cancleReason, Date cancleTime, com.strongit.oa.bo.ToaAttenApply toaAttenApply) {
        this.cancleId = cancleId;
        this.cancleReason = cancleReason;
        this.cancleTime = cancleTime;
        this.toaAttenApply = toaAttenApply;
    }

    /** default constructor */
    public ToaAttendCancle() {
    }

    /** minimal constructor */
    public ToaAttendCancle(String cancleId, com.strongit.oa.bo.ToaAttenApply toaAttenApply) {
        this.cancleId = cancleId;
        this.toaAttenApply = toaAttenApply;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="CANCLE_ID"
     *         
     */
	@Id
	@Column(name = "CANCLE_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getCancleId() {
        return this.cancleId;
    }

    public void setCancleId(String cancleId) {
        this.cancleId = cancleId;
    }

    /** 
     *            @hibernate.property
     *             column="CANCLE_REASON"
     *             length="400"
     *         
     */
    @Column(name = "CANCLE_REASON")
    public String getCancleReason() {
        return this.cancleReason;
    }

    public void setCancleReason(String cancleReason) {
        this.cancleReason = cancleReason;
    }

    /** 
     *            @hibernate.property
     *             column="CANCLE_TIME"
     *             length="7"
     *         
     */
    @Column(name = "CANCLE_TIME")
    public Date getCancleTime() {
        return this.cancleTime;
    }

    public void setCancleTime(Date cancleTime) {
        this.cancleTime = cancleTime;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="APPLY_ID"         
     *         
     */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPLY_ID")
    public com.strongit.oa.bo.ToaAttenApply getToaAttenApply() {
        return this.toaAttenApply;
    }

    public void setToaAttenApply(com.strongit.oa.bo.ToaAttenApply toaAttenApply) {
        this.toaAttenApply = toaAttenApply;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("cancleId", getCancleId())
            .toString();
    }
    @Column(name = "CANCLE_FORMID")
	public String getCancleFormId() {
		return cancleFormId;
	}

	public void setCancleFormId(String cancleFormId) {
		this.cancleFormId = cancleFormId;
	}
	@Column(name = "CANCLE_STATE")
    public String getCancleState() {
        return this.cancleState;
    }

    public void setCancleState(String cancleState) {
        this.cancleState = cancleState;
    }

    /** 
     *            @hibernate.property
     *             column="CANCLE_STIME"
     *             length="7"
     *         
     */
	@Column(name = "CANCLE_STIME")
    public Date getCancleStime() {
        return this.cancleStime;
    }

    public void setCancleStime(Date cancleStime) {
        this.cancleStime = cancleStime;
    }

    /** 
     *            @hibernate.property
     *             column="CANCLE_ETIME"
     *             length="7"
     *         
     */
	@Column(name = "CANCLE_ETIME")
    public Date getCancleEtime() {
        return this.cancleEtime;
    }

    public void setCancleEtime(Date cancleEtime) {
        this.cancleEtime = cancleEtime;
    }
}
