package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MEETINGROOM"
 *     
*/
@Entity
@Table(name="T_OA_MEETINGROOM")
public class ToaMeetingroom implements Serializable {

    /** 会议室编号 */
    private String mrId;

    /** 会议室名称 */
    private String mrName;

    /** 可容纳人数 */
    private String mrPeople;

    /** 地点 */
    private String mrLocation;

    /** 图片 */
    private byte[] mrImg;

    /** 会议室类型 */
    private String mrType;

    /** 说明 */
    private String mrRemark;

    /** 状态 */
    private String mrState;
    
    /**会议室可以使用*/
    public final static String ROOM_OPEN = "0";
    /**会议室停止使用*/
    public final static String ROOM_CLOSE = "1";
    /**会议室删除*/
    public final static String ROOM_DEL = "2";
    
    private String departmentId;//DEPARTMENT_ID
	
	private String topOrgcode;// TOP_ORGCODE

    /** 会议室申请单 */
    private Set toaMeetingroomApplies;

    /** full constructor */
    public ToaMeetingroom(String mrId, String mrName, String mrPeople, String mrLocation, byte[] mrImg, String mrType, String mrRemark, String mrState, Set toaMeetingroomApplies) {
        this.mrId = mrId;
        this.mrName = mrName;
        this.mrPeople = mrPeople;
        this.mrLocation = mrLocation;
        this.mrImg = mrImg;
        this.mrType = mrType;
        this.mrRemark = mrRemark;
        this.mrState = mrState;
        this.toaMeetingroomApplies = toaMeetingroomApplies;
    }

    /** default constructor */
    public ToaMeetingroom() {
    }

    /** minimal constructor */
    public ToaMeetingroom(String mrId, Set toaMeetingroomApplies) {
        this.mrId = mrId;
        this.toaMeetingroomApplies = toaMeetingroomApplies;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MR_ID"
     *         
     */
    @Id
	@Column(name="MR_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getMrId() {
        return this.mrId;
    }

    public void setMrId(String mrId) {
        this.mrId = mrId;
    }

    /** 
     *            @hibernate.property
     *             column="MR_NAME"
     *             length="255"
     *         
     */
    @Column(name="MR_NAME",nullable=true)
    public String getMrName() {
        return this.mrName;
    }

    public void setMrName(String mrName) {
        this.mrName = mrName;
    }

    /** 
     *            @hibernate.property
     *             column="MR_PEOPLE"
     *             length="255"
     *         
     */
    @Column(name="MR_PEOPLE",nullable=true)
    public String getMrPeople() {
        return this.mrPeople;
    }

    public void setMrPeople(String mrPeople) {
        this.mrPeople = mrPeople;
    }

    /** 
     *            @hibernate.property
     *             column="MR_LOCATION"
     *             length="255"
     *         
     */
    @Column(name="MR_LOCATION",nullable=true)
    public String getMrLocation() {
        return this.mrLocation;
    }

    public void setMrLocation(String mrLocation) {
        this.mrLocation = mrLocation;
    }

    /** 
     *            @hibernate.property
     *             column="MR_IMG"
     *             length="4000"
     *         
     */
    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "MR_IMG", columnDefinition = "BLOB", nullable=true)
    public byte[] getMrImg() {
        return this.mrImg;
    }

    public void setMrImg(byte[] mrImg) {
        this.mrImg = mrImg;
    }

    /** 
     *            @hibernate.property
     *             column="MR_TYPE"
     *             length="255"
     *         
     */
    @Column(name="MR_TYPE",nullable=true)
    public String getMrType() {
        return this.mrType;
    }

    public void setMrType(String mrType) {
        this.mrType = mrType;
    }

    /** 
     *            @hibernate.property
     *             column="MR_REMARK"
     *             length="4000"
     *         
     */
    @Column(name="MR_REMARK",nullable=true)
    public String getMrRemark() {
        return this.mrRemark;
    }

    public void setMrRemark(String mrRemark) {
        this.mrRemark = mrRemark;
    }

    /** 
     *            @hibernate.property
     *             column="MR_STATE"
     *             length="1"
     *         
     */
    @Column(name="MR_STATE",nullable=true)
    public String getMrState() {
        return this.mrState;
    }

    public void setMrState(String mrState) {
        this.mrState = mrState;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="MR_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaMeetingroomApply"
     *         
     */
    @OneToMany(mappedBy="toaMeetingroom",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE},targetEntity=ToaMeetingroomApply.class)
    public Set getToaMeetingroomApplies() {
        return this.toaMeetingroomApplies;
    }

    public void setToaMeetingroomApplies(Set toaMeetingroomApplies) {
        this.toaMeetingroomApplies = toaMeetingroomApplies;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("mrId", getMrId())
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
