package com.strongit.oa.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**公共联系人  
 * ToaPubliccontact entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OA_PUBLICCONTACT", schema = "")
public class ToaPubliccontact implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pcId;
	private String pcName; //姓名
	private String pcTell;	//电话
	private String pcPhone;// 手机
	private String pcEmail;// email
	private String pcPost;//职务
	private String pcOther;//其他
	private Date pcCreateDate;//创建时间
	private String rest1;
	private String rest2;
	private String rest3;
	private ToaPcCategory TOaPcCategory; //分类id

	// Constructors

	/** default constructor */
	public ToaPubliccontact() {
	}

	/** full constructor */
	public ToaPubliccontact(ToaPcCategory TOaPcCategory, String pcName, String pcTell, String pcPhone, String pcEmail, String pcPost, String pcOther, String rest1, String rest2, String rest3) {
        this.TOaPcCategory = TOaPcCategory;
        this.pcName = pcName;
        this.pcTell = pcTell;
        this.pcPhone = pcPhone;
        this.pcEmail = pcEmail;
        this.pcPost = pcPost;
        this.pcOther = pcOther;
        this.rest1 = rest1;
        this.rest2 = rest2;
        this.rest3 = rest3;
    }

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "PC_ID", unique = true, nullable = false, length = 32)
	public String getPcId() {
		return this.pcId;
	}

	public void setPcId(String pcId) {
		this.pcId = pcId;
	}

	@Column(name = "PC_NAME", length = 100)
	public String getPcName() {
		return this.pcName;
	}

	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	@Column(name = "PC_TELL", length = 32)
	public String getPcTell() {
		return this.pcTell;
	}

	public void setPcTell(String pcTell) {
		this.pcTell = pcTell;
	}

	@Column(name = "PC_PHONE", length = 32)
	public String getPcPhone() {
		return this.pcPhone;
	}

	public void setPcPhone(String pcPhone) {
		this.pcPhone = pcPhone;
	}

	@Column(name = "PC_EMAIL", length = 32)
	public String getPcEmail() {
		return this.pcEmail;
	}

	public void setPcEmail(String pcEmail) {
		this.pcEmail = pcEmail;
	}

	@Column(name = "PC_POST", length = 32)
	public String getPcPost() {
		return this.pcPost;
	}

	public void setPcPost(String pcPost) {
		this.pcPost = pcPost;
	}

	@Column(name = "PC_OTHER", length = 200)
	public String getPcOther() {
		return this.pcOther;
	}

	public void setPcOther(String pcOther) {
		this.pcOther = pcOther;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PCC_ID")
	public ToaPcCategory getTOaPcCategory() {
        return this.TOaPcCategory;
    }
    
    public void setTOaPcCategory(ToaPcCategory TOaPcCategory) {
        this.TOaPcCategory = TOaPcCategory;
    }
    
	@Column(name = "REST1", length = 200)
	public String getRest1() {
		return this.rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2", length = 200)
	public String getRest2() {
		return this.rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "REST3", length = 200)
	public String getRest3() {
		return this.rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}
	
	@Column(name = "PC_CREATEDATE")
	public Date getPcCreateDate() {
		return pcCreateDate;
	}

	public void setPcCreateDate(Date pcCreateDate) {
		this.pcCreateDate = pcCreateDate;
	}
	

}