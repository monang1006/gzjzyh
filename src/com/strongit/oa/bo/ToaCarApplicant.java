package com.strongit.oa.bo;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 *         table="T_OA_CARAPPLICANT"
 *     
*/
@Entity
@Table(name="T_OA_CARAPPLICANT",catalog="",schema="")
public class ToaCarApplicant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** identifier field */
	private String applicantId;    //申请ID
	
	/** nullable persistent field */
	private String applierId;   //申请人ID
	
	/** nullable persistent field */
	private String applier;   //申请人姓名
	
	/** nullable persistent field */
	private String passengernumber;     //乘客人数
	
	/** nullable persistent field */
	private Date applytime;  //申请时间
	
	/** nullable persistent field */
	private Date starttime;  //起始时间
	
	/** nullable persistent field */
	private Date endtime;  //结束时间
	
	/** nullable persistent field */
	private String destination; //目的地
	
	/** nullable persistent field */
	private String applyreason; //事由
	
	/** nullable persistent field */
	private String approvalsuggestion; //审批记录
	
	/** nullable persistent field */
	private String applystatus;	//申请状态
	
	private String caruser;  //车辆使用者
	
	private String needdriver; //是否需要安排司机
	

	private String userDepart; //使用部门
	
	private String driver; //驾驶员

	private String localPosition; //发车地点

	private String distance; //里程

	private String gasCost; //汽油费

	private String bridgeCost; //路桥费

	private String pullCost; //停车费

	private String cleanCost; //洗车费

	private String otherCost; //其他费用

	private String costNotes; //费用说明

	private String isConfirm; //是否已登记

	
    private com.strongit.oa.bo.ToaCar toaCar; //车辆
    
    public static final String CARAPPLY_NOTCONFIRM = "0";//未登记
    public static final String CARAPPLY_ISCONFIRM = "1";//已登记

    /** nullable persistent field */
    private String orgCode;
    
    /** nullable persistent field */
    private String orgId;

	/** full constructor */
	public ToaCarApplicant( String applicantId,	 String applierId, String applier,  String passengernumber,  Date applytime, Date starttime, Date endtime, String destination, String applyreason, String approvalsuggestion, String applystatus, String caruser, String needdriver, String userDepart, String driver, String localPosition, String distance, String gasCost, String bridgeCost, String pullCost, String cleanCost, String otherCost, String costNotes, String isConfirm, com.strongit.oa.bo.ToaCar toaCar,String orgCode,String orgId){
		this.applicantId=applicantId;
		this.applierId=applierId;
		this.applier=applier;
		this.applyreason=applyreason;
		this.applystatus=applystatus;
		this.applytime=applytime;
		this.approvalsuggestion=approvalsuggestion;
		this.destination=destination;
		this.endtime=endtime;
		this.passengernumber=passengernumber;
		this.starttime=starttime;
		this.caruser=caruser;
		this.needdriver=needdriver;
		this.userDepart=userDepart; //使用部门
		this.driver = driver;
		this.localPosition=localPosition; //发车地点
		this.distance=distance; //里程
		this.gasCost=gasCost; //汽油费
		this.bridgeCost=bridgeCost; //路桥费
		this.pullCost=pullCost; //停车费
		this.cleanCost=cleanCost; //洗车费
		this.otherCost=otherCost; //其他费用
		this.costNotes=costNotes; //费用说明
		this.isConfirm = isConfirm;//是否已经登记
		this.toaCar=toaCar;

		this.orgCode=orgCode;
		this.orgId=orgId;
	}
	
	/** default constructor */
	public ToaCarApplicant(){
	}
	
	/** minimal constructor */
	public ToaCarApplicant(String applicantId,String applierId, com.strongit.oa.bo.ToaCar toaCar){
		this.applicantId=applicantId;
		this.applierId=applierId;
		this.toaCar=toaCar;
	}
    
    
	  /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="APPLICANTID"
     *         
     */
    @Id
    @Column(name="APPLICANTID",nullable=false, length=32)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
	
	/** 
     *            @hibernate.property
     *             column="APPLIER"
     *             length="100"
     *         
     */
    @Column(name="APPLIER", nullable=true, length=100)
	  public String getApplier() {
		return applier;
	}

	public void setApplier(String applier) {
		this.applier = applier;
	}

	
	/** 
     *            @hibernate.property
     *             column="APPLIERID"
     *             length="32"
     *         
     */
    @Column(name="APPLIERID", nullable=false, length=32)
	public String getApplierId() {
		return applierId;
	}

	public void setApplierId(String applierId) {
		this.applierId = applierId;
	}

	/** 
     *            @hibernate.property
     *             column="APPLYREASON"
     *             length="1000"
     *         
     */
    @Column(name="APPLYREASON", nullable=true, length=1000)
	public String getApplyreason() {
		return applyreason;
	}

	public void setApplyreason(String applyreason) {
		this.applyreason = applyreason;
	}

	  /** 
     *            @hibernate.property
     *             column="APPLYSTATUS"
     *             length="1"
     *         
     */
    @Column(name="APPLYSTATUS", nullable=true, length=1)
	public String getApplystatus() {
		return applystatus;
	}

	public void setApplystatus(String applystatus) {
		this.applystatus = applystatus;
	}

	  /** 
     *            @hibernate.property
     *             column="APPLYTIME"
     *             length="7"
     *         
     */
    @Column(name="APPLYTIME", nullable=true, length=7)
	public Date getApplytime() {
		return applytime;
	}

	public void setApplytime(Date applytime) {
		this.applytime = applytime;
	}

	  /** 
     *            @hibernate.property
     *             column="APPROVALSUGGESTION"
     *             length="1000"
     *         
     */
    @Column(name="APPROVALSUGGESTION", nullable=true, length=1000)
	public String getApprovalsuggestion() {
		return approvalsuggestion;
	}

	public void setApprovalsuggestion(String approvalsuggestion) {
		this.approvalsuggestion = approvalsuggestion;
	}

	  /** 
     *            @hibernate.property
     *             column="DESTINATION"
     *             length="200"
     *         
     */
    @Column(name="DESTINATION", nullable=true, length=200)
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	  /** 
     *            @hibernate.property
     *             column="ENDTIME"
     *              length="7"
     *         
     */
    @Column(name="ENDTIME", nullable=true, length=7)
	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	  /** 
     *            @hibernate.property
     *             column="PASSENGERNUMBER"
     *             length="3"
     *         
     */
    @Column(name="PASSENGERNUMBER", nullable=true, length=3)
	public String getPassengernumber() {
		return passengernumber;
	}

	public void setPassengernumber(String passengernumber) {
		this.passengernumber = passengernumber;
	}

	  /** 
     *            @hibernate.property
     *             column="STARTTIME"
     *             length="7"
     *         
     */
    @Column(name="STARTTIME", nullable=true, length=7)
	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	 /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="CARID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CARID")
	public com.strongit.oa.bo.ToaCar getToaCar() {
		return toaCar;
	}

	public void setToaCar(com.strongit.oa.bo.ToaCar toaCar) {
		this.toaCar = toaCar;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("applicantId", getApplicantId())
            .toString();
    }

	
	  /** 
     *            @hibernate.property
     *             column="CARUSER"
     *             length="32"
     *         
     */
    @Column(name="CARUSER", nullable=true, length=32)
	public String getCaruser() {
		return caruser;
	}

	public void setCaruser(String caruser) {
		this.caruser = caruser;
	}

	
	  /** 
     *            @hibernate.property
     *             column="NEEDDRIVER"
     *             length="1"
     *         
     */
    @Column(name="NEEDDRIVER", nullable=true, length=1)
	public String getNeeddriver() {
		return needdriver;
	}

	public void setNeeddriver(String needdriver) {
		this.needdriver = needdriver;
	}

	/** 
     *            @hibernate.property
     *             column="BRIDGECOST"
     *             length="50"
     *         
     */
    @Column(name="BRIDGECOST", nullable=true, length=50)
	public String getBridgeCost() {
		return bridgeCost;
	}

	public void setBridgeCost(String bridgeCost) {
		this.bridgeCost = bridgeCost;
	}

	/** 
     *            @hibernate.property
     *             column="CLEANCOST"
     *             length="50"
     *         
     */
    @Column(name="CLEANCOST", nullable=true, length=50)
	public String getCleanCost() {
		return cleanCost;
	}

	public void setCleanCost(String cleanCost) {
		this.cleanCost = cleanCost;
	}

	/** 
     *            @hibernate.property
     *             column="COSTNOTES"
     *             length="2000"
     *         
     */
    @Column(name="COSTNOTES", nullable=true, length=2000)
	public String getCostNotes() {
		return costNotes;
	}

	public void setCostNotes(String costNotes) {
		this.costNotes = costNotes;
	}

	/** 
     *            @hibernate.property
     *             column="DISTANCE"
     *             length="100"
     *         
     */
    @Column(name="DISTANCE", nullable=true, length=100)
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	/** 
     *            @hibernate.property
     *             column="GASCOST"
     *             length="50"
     *         
     */
    @Column(name="GASCOST", nullable=true, length=50)
	public String getGasCost() {
		return gasCost;
	}

	public void setGasCost(String gasCost) {
		this.gasCost = gasCost;
	}

	/** 
     *            @hibernate.property
     *             column="LOCALPOSITION"
     *             length="100"
     *         
     */
    @Column(name="LOCALPOSITION", nullable=true, length=100)
	public String getLocalPosition() {
		return localPosition;
	}

	public void setLocalPosition(String localPosition) {
		this.localPosition = localPosition;
	}

	/** 
     *            @hibernate.property
     *             column="OTHERCOST"
     *             length="50"
     *         
     */
    @Column(name="OTHERCOST", nullable=true, length=50)
	public String getOtherCost() {
		return otherCost;
	}

	public void setOtherCost(String otherCost) {
		this.otherCost = otherCost;
	}

	/** 
     *            @hibernate.property
     *             column="PULLCOST"
     *             length="50"
     *         
     */
    @Column(name="PULLCOST", nullable=true, length=50)
	public String getPullCost() {
		return pullCost;
	}

	public void setPullCost(String pullCost) {
		this.pullCost = pullCost;
	}

	/** 
     *            @hibernate.property
     *             column="USEDEPART"
     *             length="50"
     *         
     */
    @Column(name="USEDEPART", nullable=true, length=100)
	public String getUserDepart() {
		return userDepart;
	}

	public void setUserDepart(String userDepart) {
		this.userDepart = userDepart;
	}

	/** 
     *            @hibernate.property
     *             column="DRIVER"
     *             length="100"
     *         
     */
    @Column(name="DRIVER", nullable=true, length=100)
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	/** 
     *            @hibernate.property
     *             column="ISCONFIRM"
     *             length="100"
     *         
     */
    @Column(name="ISCONFIRM", nullable=true, length=1)
	public String getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(String isConfirm) {
		this.isConfirm = isConfirm;
	}
	

    /** 
     *            @hibernate.property
     *             column="ORG_CODE"
     *             length="100"
     *         
     */
    @Column(name = "ORG_CODE",nullable=true)
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
    
    /** 
     *            @hibernate.property
     *             column="ORG_ID"
     *             length="32"
     *         
     */
    @Column(name = "ORG_ID",nullable=true)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
