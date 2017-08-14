package com.strongit.oa.bo;

import java.io.Serializable;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_ADDRESS"
 *     
*/
@Entity
@Table(name="T_OA_ADDRESS",catalog="",schema="")
public class ToaAddress implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8843559728229143992L;

	/** identifier field */
    private String addrId;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String nickname;

    /** nullable persistent field */
    private String mobile1;

    /** nullable persistent field */
    private String mobile2;

    /** nullable persistent field */
    private String sex;

    /** nullable persistent field */
    private Date birthday;

    /** nullable persistent field */
    private String qq;

    /** nullable persistent field */
    private String msn;

    /** nullable persistent field */
    private String likeThing;

    /** nullable persistent field */
    private String homepage;

    /** nullable persistent field */
    private String country;

    /** nullable persistent field */
    private String address;

    /** nullable persistent field */
    private String province;

    /** nullable persistent field */
    private String fax;

    /** nullable persistent field */
    private String city;

    /** nullable persistent field */
    private String position;

    /** nullable persistent field */
    private String company;

    /** nullable persistent field */
    private String zipcode;

    /** nullable persistent field */
    private String addressRemark;

    /** nullable persistent field */
    private String department;

    /** nullable persistent field */
    private String tel1;

    /** nullable persistent field */
    private String tel2;

    /** nullable persistent field */
    private String coTel1;

    /** nullable persistent field */
    private String coTel2;

    /** persistent field */
    private String userId;
    
    private String groupName;		//wap开发新增

    /** persistent field */
    private com.strongit.oa.bo.ToaAddressGroup toaAddressGroup;

    /** persistent field */
    private Set<ToaAddressMail> toaAddressMails;
    
    private String defaultEmail = "" ;

//    private String fileId;//多余的,删除会报错
    /** full constructor */
    public ToaAddress(String addrId, String name, String nickname, String mobile1, String mobile2, String sex, Date birthday, String qq, String msn, String likeThing, String homepage, String country, String address, String province, String fax, String city, String position, String company, String zipcode, String addressRemark, String department, String tel1, String tel2, String coTel1, String coTel2, com.strongit.oa.bo.ToaAddressGroup toaAddressGroup, Set<ToaAddressMail> toaAddressMails) {
        this.addrId = addrId;
        this.name = name;
        this.nickname = nickname;
        this.mobile1 = mobile1;
        this.mobile2 = mobile2;
        this.sex = sex;
        this.birthday = birthday;
        this.qq = qq;
        this.msn = msn;
        this.likeThing = likeThing;
        this.homepage = homepage;
        this.country = country;
        this.address = address;
        this.province = province;
        this.fax = fax;
        this.city = city;
        this.position = position;
        this.company = company;
        this.zipcode = zipcode;
        this.addressRemark = addressRemark;
        this.department = department;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.coTel1 = coTel1;
        this.coTel2 = coTel2;
        this.toaAddressGroup = toaAddressGroup;
        this.toaAddressMails = toaAddressMails;
    }

    /** default constructor */
    public ToaAddress() {
    }

    public ToaAddress(String addrId, String name,String tel1,String mobile,String userEmail,String position) {
    	this.addrId = addrId ;
    	this.name = name ;
    	this.tel1 = tel1;
    	this.mobile1 = mobile;
    	this.defaultEmail = userEmail;
    	this.position = position;
    }
    
    /** minimal constructor */
    public ToaAddress(String addrId, com.strongit.oa.bo.ToaAddressGroup toaAddressGroup, Set<ToaAddressMail> toaAddressMails) {
        this.addrId = addrId;
        this.toaAddressGroup = toaAddressGroup;
        this.toaAddressMails = toaAddressMails;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="ADDR_ID"
     *         
     */
    @Id
    @Column(name="ADDR_ID",nullable=false)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getAddrId() {
        return this.addrId;
    }

    public void setAddrId(String addrId) {
        this.addrId = addrId;
    }

    /** 
     *            @hibernate.property
     *             column="NAME"
     *             length="12"
     *         
     */
    @Column(name="NAME")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 
     *            @hibernate.property
     *             column="NICKNAME"
     *             length="12"
     *         
     */
    @Column(name="NICKNAME")
    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /** 
     *            @hibernate.property
     *             column="MOBILE1"
     *             length="22"
     *         
     */
    @Column(name="MOBILE1")
    public String getMobile1() {
        return this.mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    /** 
     *            @hibernate.property
     *             column="MOBILE2"
     *             length="22"
     *         
     */
    @Column(name="MOBILE2")
    public String getMobile2() {
        return this.mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    /** 
     *            @hibernate.property
     *             column="SEX"
     *             length="22"
     *         
     */
    @Column(name="SEX")
    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    /** 
     *            @hibernate.property
     *             column="BIRTHDAY"
     *             length="7"
     *         
     */
    @Column(name="BIRTHDAY")
    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /** 
     *            @hibernate.property
     *             column="QQ"
     *             length="22"
     *         
     */
    @Column(name="QQ")
    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    /** 
     *            @hibernate.property
     *             column="MSN"
     *             length="256"
     *         
     */
    @Column(name="MSN")
    public String getMsn() {
        return this.msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    /** 
     *            @hibernate.property
     *             column="LIKE_THING"
     *             length="1024"
     *         
     */
    @Column(name="LIKE_THING")
    public String getLikeThing() {
        return this.likeThing;
    }

    public void setLikeThing(String likeThing) {
        this.likeThing = likeThing;
    }

    /** 
     *            @hibernate.property
     *             column="HOMEPAGE"
     *             length="256"
     *         
     */
    @Column(name="HOMEPAGE")
    public String getHomepage() {
        return this.homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /** 
     *            @hibernate.property
     *             column="COUNTRY"
     *             length="25"
     *         
     */
    @Column(name="COUNTRY")
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /** 
     *            @hibernate.property
     *             column="ADDRESS"
     *             length="256"
     *         
     */
    @Column(name="ADDRESS")
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /** 
     *            @hibernate.property
     *             column="PROVINCE"
     *             length="25"
     *         
     */
    @Column(name="PROVINCE")
    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    /** 
     *            @hibernate.property
     *             column="FAX"
     *             length="128"
     *         
     */
    @Column(name="FAX")
    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    /** 
     *            @hibernate.property
     *             column="CITY"
     *             length="25"
     *         
     */
    @Column(name="CITY")
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /** 
     *            @hibernate.property
     *             column="POSITION"
     *             length="128"
     *         
     */
    @Column(name="POSITION")
    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY"
     *             length="256"
     *         
     */
    @Column(name="COMPANY")
    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /** 
     *            @hibernate.property
     *             column="ZIPCODE"
     *             length="25"
     *         
     */
    @Column(name="ZIPCODE")
    public String getZipcode() {
        return this.zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /** 
     *            @hibernate.property
     *             column="ADDRESS_REMARK"
     *         
     */
    @Column(name="ADDRESS_REMARK")
    public String getAddressRemark() {
        return this.addressRemark;
    }

    public void setAddressRemark(String addressRemark) {
        this.addressRemark = addressRemark;
    }

    /** 
     *            @hibernate.property
     *             column="DEPARTMENT"
     *             length="256"
     *         
     */
    @Column(name="DEPARTMENT")
    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    /** 
     *            @hibernate.property
     *             column="TEL1"
     *             length="128"
     *         
     */
    @Column(name="TEL1")
    public String getTel1() {
    	if(null == tel1){
    		return "";
    	}
//    	if("".equals(this.tel1)){
//    		return this.tel2;
//    	}
        return this.tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    /** 
     *            @hibernate.property
     *             column="TEL2"
     *             length="128"
     *         
     */
    @Column(name="TEL2")
    public String getTel2() {
        return this.tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    /** 
     *            @hibernate.property
     *             column="CO_TEL1"
     *             length="128"
     *         
     */
    @Column(name="CO_TEL1")
    public String getCoTel1() {
        return this.coTel1;
    }

    public void setCoTel1(String coTel1) {
        this.coTel1 = coTel1;
    }

    /** 
     *            @hibernate.property
     *             column="CO_TEL2"
     *             length="128"
     *         
     */
    @Column(name="CO_TEL2")
    public String getCoTel2() {
        return this.coTel2;
    }

    public void setCoTel2(String coTel2) {
        this.coTel2 = coTel2;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="ADDR_GROUP_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ADDR_GROUP_ID")
    public com.strongit.oa.bo.ToaAddressGroup getToaAddressGroup() {
        return this.toaAddressGroup;
    }

    public void setToaAddressGroup(com.strongit.oa.bo.ToaAddressGroup toaAddressGroup) {
        this.toaAddressGroup = toaAddressGroup;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="ADDR_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaAddressMail"
     *         
     */
    @OneToMany(mappedBy="toaAddress",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE})
    @OrderBy("mail")
    public Set<ToaAddressMail> getToaAddressMails() {
        return this.toaAddressMails;
    }

    public void setToaAddressMails(Set<ToaAddressMail> toaAddressMails) {
        this.toaAddressMails = toaAddressMails;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("addrId", getAddrId())
            .toString();
    }

    @Transient
	public String getDefaultEmail() {
    	return this.defaultEmail;
	}

	public void setDefaultEmail(String defaultEmail) {
		this.defaultEmail = defaultEmail;
	}

	@Transient
	public String getAllEmail(){
		Iterator<ToaAddressMail> it = toaAddressMails.iterator();
		StringBuffer sb = new StringBuffer();
    	while(it.hasNext()){
    		ToaAddressMail mail = it.next();
    		sb.append(mail.getMail()).append(",");
    	}
    	if(sb.length()>0){
    		return sb.substring(0, sb.length()-1).toString();
    	}
    	return "";
	}

	@Transient
	public String getAllPhone(){
		StringBuffer sb = new StringBuffer("");
		if(getTel1()!=null && !"".equals(getTel1())){ 
			sb.append(getTel1()).append(","); 
		}
		if(getTel2()!=null && !"".equals(getTel2())){
			sb.append(getTel2()).append(",");
		}
		if(getMobile1()!=null && !"".equals(getMobile1())){
			sb.append(getMobile1()).append(",");
		}
		if(getMobile2()!=null && !"".equals(getMobile2())){
			sb.append(getMobile2());
		}
		if(sb.toString().endsWith(",")){
			return sb.substring(0,sb.length()-1);
		}
		return sb.toString();
	}
	
	@Transient
	public String getStrMobile1() {
		if(null == mobile1){
			return "";
		}
		return String.valueOf(mobile1); 
	}

	@Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Transient
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
