package com.strongit.oa.bo;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/** 
 *        @hibernate.class
 *         table="T_OA_CAR"
 *     
*/
@Entity
@Table(name="T_OA_CAR",catalog="",schema="")
public class ToaCar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** identifier field */
	private String carId;   //车辆ID
	
	/** nullable persistent field */
	private String carno;   //车牌号
	
	/** nullable persistent field */
	private String cartype;  //车型
	
	/** nullable persistent field */
	private String takenumber;     //可乘人数
	
	/** nullable persistent field */
	private String status;  //状态（0可用、1在用、2维修中）
	
	/** nullable persistent field */
	private Date buydate;  //购置日期
	
	/** nullable persistent field */
	private String cardescription;  //车辆说明
	
	/** nullable persistent field */
	private byte[] img; //图片
	
	private String carbrand; //车辆品牌
	
	private String driver; //司机
	
	private String isdel; //是否删除 1表示删除了（假删除）
	
	/** 车辆申请单 */
    private Set toaCarApplicants;   //车辆申请单
	
    /** 车辆可用 */
	public final static String CAR_USEABLE = "0";
	/** 车辆不可用 */
	public final static String CAR_USELESS = "1";

    /** nullable persistent field */
    private String orgCode;
    
    /** nullable persistent field */
    private String orgId;
    
	/** full constructor */
	public ToaCar(String carId, String carno, String cartype, String takenumber, String status, Date buydate, String cardescription, byte[] img,String carbrand,String driver,String isdel,Set toaCarApplicants,String orgCode,String orgId){
		this.carId=carId;
		this.carno=carno;
		this.cartype=cartype;
		this.takenumber=takenumber;
		this.status=status;
		this.buydate=buydate;
		this.cardescription=cardescription;
		this.img=img;
		this.carbrand=carbrand;
		this.driver=driver;
		this.isdel=isdel;
		this.toaCarApplicants=toaCarApplicants;
		
		this.orgCode=orgCode;
		this.orgId=orgId;
	}
	
	/** default constructor */
	public ToaCar(){
	}
	
	/** minimal constructor */
	public ToaCar(String carId,Set toaCarApplicants){
		this.carId=carId;
		this.toaCarApplicants=toaCarApplicants;
		
	}
	
	  /** 
     *            @hibernate.property
     *             column="BUYDATE"
     *             length="7"
     *         
     */
    @Column(name="BUYDATE", nullable=true)
	public Date getBuydate() {
		return buydate;
	}

	public void setBuydate(Date buydate) {
		this.buydate = buydate;
	}

	  /** 
     *            @hibernate.property
     *             column="CARDESCRIPTION"
     *             length="1000"
     *         
     */
    @Column(name="CARDESCRIPTION", nullable=true)
	public String getCardescription() {
		return cardescription;
	}

	public void setCardescription(String cardescription) {
		this.cardescription = cardescription;
	}

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="CARID"
     *         
     */
    @Id
    @Column(name="CARID",nullable=false, length=32)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	  /** 
     *            @hibernate.property
     *             column="CARNUMBER"
     *             length="32"
     *         
     */
    @Column(name="CARNUMBER", length=32, nullable=true)
	public String getCarno() {
		return carno;
	}

	public void setCarno(String carno) {
		this.carno = carno;
	}

	  /** 
     *            @hibernate.property
     *             column="CARTYPE"
     *             length="1"
     *         
     */
    @Column(name="CARTYPE",  nullable=true)
	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}




	  /** 
     *            @hibernate.property
     *             column="STATUS"
     *             length="1"
     *         
     */
    @Column(name="STATUS", length=1, nullable=true)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	  /** 
     *            @hibernate.property
     *             column="TAKENUMBER"
     *             length="3"
     *         
     */
    @Column(name="TAKENUMBER", length=3, nullable=true)
	public String getTakenumber() {
		return takenumber;
	}

	public void setTakenumber(String takenumber) {
		this.takenumber = takenumber;
	}
	
	
	 /** 
     *            @hibernate.property
     *             column="IMG"
     *             length="4000"
     *         
     */
    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "IMG", columnDefinition = "BLOB", nullable=true)
	 public void setImg(byte[] img) {
			this.img = img;
		}
	public byte[] getImg() {
		return img;
	}
	
	
	 /** 
     *            @hibernate.property
     *             column="CARBRAND"
     *             length="32"
     *         
     */
    @Column(name="CARBRAND", length=32, nullable=true)
	public String getCarbrand() {
		return carbrand;
	}

	public void setCarbrand(String carbrand) {
		this.carbrand = carbrand;
	}

	  /** 
     *            @hibernate.property
     *             column="DRIVER"
     *             length="32"
     *         
     */
    @Column(name="DRIVER", length=32, nullable=true)
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	
	/** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="CARID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaCarApplicant"
     *         
     */
    @OneToMany(mappedBy="toaCar",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE},targetEntity=com.strongit.oa.bo.ToaCarApplicant.class)
	
	public Set getToaCarApplicants() {
		return toaCarApplicants;
	}

	public void setToaCarApplicants(Set toaCarApplicants) {
		this.toaCarApplicants = toaCarApplicants;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("carId", getCarId())
            .toString();
    }

	
	  /** 
     *            @hibernate.property
     *             column="ISDEL"
     *             length="1"
     *         
     */
    @Column(name="ISDEL",  nullable=true)
	public String getIsdel() {
		return isdel;
	}

	public void setIsdel(String isdel) {
		this.isdel = isdel;
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
