package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_PERSONAL_INFO"
 *     
*/
@Entity
@Table(name="T_OA_PERSONAL_INFO")
public class ToaPersonalInfo implements Serializable {

    /** identifier field */
    private String prsnId;

    /** nullable persistent field */
    private String prsnName;

    /** nullable persistent field */
    private String prsnNickname;

    /** nullable persistent field */
    private String prsnSex;

    /** nullable persistent field */
    private Date prsnBirthday;

    /** nullable persistent field */
    private String prsnTel1;

    /** nullable persistent field */
    private String prsnTel2;

    /** nullable persistent field */
    private String prsnPhs;

    /** nullable persistent field */
    private String prsnMobile1;

    /** nullable persistent field */
    private String prsnMobile2;

    /** nullable persistent field */
    private String prsnMail1;

    /** nullable persistent field */
    private String prsnMail2;

    /** nullable persistent field */
    private String prsnFax;

    /** nullable persistent field */
    private String prsnQq;

    /** nullable persistent field */
    private String prsnMsn;

    /** nullable persistent field */
    private String homeAddress;

    /** nullable persistent field */
    private String homeZipcode;

    /** persistent field */
    private String userId;
    //private com.strongit.oa.bo.ToaUser toaUser;

    /** persistent field */
    private Set toaPersonalConfigs;
    
    private String company;
    private String position;
    private String state;
    private String dept;
    private String province;
    private String tel;
    private String city;
    private String fax;
    private String mailnum;
    
    private byte[] sign;//签名
    
    private String tempFilePath;
    
    /** full constructor */
    public ToaPersonalInfo(String prsnId, String prsnName, String prsnNickname,
			String prsnSex, Date prsnBirthday, String prsnTel1,
			String prsnTel2, String prsnPhs, String prsnMobile1,
			String prsnMobile2, String prsnMail1, String prsnMail2,
			String prsnFax, String prsnQq, String prsnMsn, String homeAddress,
			String homeZipcode, String userId, Set toaPersonalConfigs,
			String company, String position, String state, String dept,
			String province, String tel, String city, String fax,
			String mailnum) {
		super();
		this.prsnId = prsnId;
		this.prsnName = prsnName;
		this.prsnNickname = prsnNickname;
		this.prsnSex = prsnSex;
		this.prsnBirthday = prsnBirthday;
		this.prsnTel1 = prsnTel1;
		this.prsnTel2 = prsnTel2;
		this.prsnPhs = prsnPhs;
		this.prsnMobile1 = prsnMobile1;
		this.prsnMobile2 = prsnMobile2;
		this.prsnMail1 = prsnMail1;
		this.prsnMail2 = prsnMail2;
		this.prsnFax = prsnFax;
		this.prsnQq = prsnQq;
		this.prsnMsn = prsnMsn;
		this.homeAddress = homeAddress;
		this.homeZipcode = homeZipcode;
		this.userId = userId;
		this.toaPersonalConfigs = toaPersonalConfigs;
		this.company = company;
		this.position = position;
		this.state = state;
		this.dept = dept;
		this.province = province;
		this.tel = tel;
		this.city = city;
		this.fax = fax;
		this.mailnum = mailnum;
	}

	/** default constructor */
    public ToaPersonalInfo() {
    }

    /** minimal constructor */
    public ToaPersonalInfo(String prsnId,String userId, Set toaPersonalConfigs) {
        this.prsnId = prsnId;
        this.userId = userId;
        this.toaPersonalConfigs = toaPersonalConfigs;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="PRSN_ID"
     *         
     */
    @Id
	@Column(name="PRSN_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getPrsnId() {
        return this.prsnId;
    }

    public void setPrsnId(String prsnId) {
        this.prsnId = prsnId;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_NAME"
     *             length="25"
     *         
     */
    @Column(name="PRSN_NAME",nullable=true)
    public String getPrsnName() {
        return this.prsnName;
    }

    public void setPrsnName(String prsnName) {
        this.prsnName = prsnName;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_NICKNAME"
     *             length="25"
     *         
     */
    @Column(name="PRSN_NICKNAME",nullable=true)
    public String getPrsnNickname() {
        return this.prsnNickname;
    }

    public void setPrsnNickname(String prsnNickname) {
        this.prsnNickname = prsnNickname;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_SEX"
     *             length="1"
     *         
     */
    @Column(name="PRSN_SEX",nullable=true)
    public String getPrsnSex() {
        return this.prsnSex;
    }

    public void setPrsnSex(String prsnSex) {
        this.prsnSex = prsnSex;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_BIRTHDAY"
     *             length="7"
     *         
     */
    @Column(name="PRSN_BIRTHDAY",nullable=true)
    public Date getPrsnBirthday() {
        return this.prsnBirthday;
    }

    public void setPrsnBirthday(Date prsnBirthday) {
        this.prsnBirthday = prsnBirthday;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_TEL1"
     *             length="20"
     *         
     */
    @Column(name="PRSN_TEL1",nullable=true)
    public String getPrsnTel1() {
        return this.prsnTel1;
    }

    public void setPrsnTel1(String prsnTel1) {
        this.prsnTel1 = prsnTel1;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_TEL2"
     *             length="20"
     *         
     */
    @Column(name="PRSN_TEL2",nullable=true)
    public String getPrsnTel2() {
        return this.prsnTel2;
    }

    public void setPrsnTel2(String prsnTel2) {
        this.prsnTel2 = prsnTel2;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_PHS"
     *             length="20"
     *         
     */
    @Column(name="PRSN_PHS",nullable=true)
    public String getPrsnPhs() {
        return this.prsnPhs;
    }

    public void setPrsnPhs(String prsnPhs) {
        this.prsnPhs = prsnPhs;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_MOBILE1"
     *             length="20"
     *         
     */
    @Column(name="PRSN_MOBILE1",nullable=true)
    public String getPrsnMobile1() {
        return this.prsnMobile1;
    }

    public void setPrsnMobile1(String prsnMobile1) {
        this.prsnMobile1 = prsnMobile1;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_MOBILE2"
     *             length="20"
     *         
     */
    @Column(name="PRSN_MOBILE2",nullable=true)
    public String getPrsnMobile2() {
        return this.prsnMobile2;
    }

    public void setPrsnMobile2(String prsnMobile2) {
        this.prsnMobile2 = prsnMobile2;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_MAIL1"
     *             length="128"
     *         
     */
    @Column(name="PRSN_MAIL1",nullable=true)
    public String getPrsnMail1() {
        return this.prsnMail1;
    }

    public void setPrsnMail1(String prsnMail1) {
        this.prsnMail1 = prsnMail1;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_MAIL2"
     *             length="128"
     *         
     */
    @Column(name="PRSN_MAIL2",nullable=true)
    public String getPrsnMail2() {
        return this.prsnMail2;
    }

    public void setPrsnMail2(String prsnMail2) {
        this.prsnMail2 = prsnMail2;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_FAX"
     *             length="20"
     *         
     */
    @Column(name="PRSN_FAX",nullable=true)
    public String getPrsnFax() {
        return this.prsnFax;
    }

    public void setPrsnFax(String prsnFax) {
        this.prsnFax = prsnFax;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_QQ"
     *             length="20"
     *         
     */
    @Column(name="PRSN_QQ",nullable=true)
    public String getPrsnQq() {
        return this.prsnQq;
    }

    public void setPrsnQq(String prsnQq) {
        this.prsnQq = prsnQq;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_MSN"
     *             length="128"
     *         
     */
    @Column(name="PRSN_MSN",nullable=true)
    public String getPrsnMsn() {
        return this.prsnMsn;
    }

    public void setPrsnMsn(String prsnMsn) {
        this.prsnMsn = prsnMsn;
    }

    /** 
     *            @hibernate.property
     *             column="HOME_ADDRESS"
     *             length="128"
     *         
     */
    @Column(name="HOME_ADDRESS",nullable=true)
    public String getHomeAddress() {
        return this.homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    /** 
     *            @hibernate.property
     *             column="HOME_ZIPCODE"
     *             length="25"
     *         
     */
    @Column(name="HOME_ZIPCODE",nullable=true)
    public String getHomeZipcode() {
        return this.homeZipcode;
    }

    
    public void setHomeZipcode(String homeZipcode) {
        this.homeZipcode = homeZipcode;
    }
    
    /** 
     *            @hibernate.property
     *             column="USER_ID"
     *             length="32"
     *         
     */
    @Column(name="USER_ID",nullable=true)
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	
	
	
//    /** 
//     *            @hibernate.many-to-one
//     *             not-null="true"
//     *            @hibernate.column name="USER_ID"         
//     *         
//     */
//    public com.strongit.oa.bo.ToaUser getToaUser() {
//        return this.toaUser;
//    }
//
//    public void setToaUser(com.strongit.oa.bo.ToaUser toaUser) {
//        this.toaUser = toaUser;
//    }
	@Column(name="COMPANY",nullable=true)
    public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	@Column(name="POSITION",nullable=true)
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	@Column(name="STATE",nullable=true)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	@Column(name="DEPT",nullable=true)
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
	@Column(name="PROVINCE",nullable=true)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	@Column(name="TEL",nullable=true)
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	@Column(name="CITY",nullable=true)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	@Column(name="FAX",nullable=true)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	@Column(name="MAILNUM",nullable=true)
	public String getMailnum() {
		return mailnum;
	}

	public void setMailnum(String mailnum) {
		this.mailnum = mailnum;
	}

	/** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="PRSN_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaPersonalConfig"
     *         
     */
	@OneToMany(mappedBy="toaPersonalInfo",targetEntity=com.strongit.oa.bo.ToaPersonalConfig.class,cascade=CascadeType.ALL)
    public Set getToaPersonalConfigs() {
        return this.toaPersonalConfigs;
    }

    public void setToaPersonalConfigs(Set toaPersonalConfigs) {
        this.toaPersonalConfigs = toaPersonalConfigs;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("prsnId", getPrsnId())
            .toString();
    }

    @Column(name = "SIGN")
    @Lob
	public byte[] getSign() {
		return sign;
	}

	public void setSign(byte[] sign) {
		this.sign = sign;
	}

	@Transient
	public String getTempFilePath() {
		return tempFilePath;
	}

	public void setTempFilePath(String tempFilePath) {
		this.tempFilePath = tempFilePath;
	}

}
