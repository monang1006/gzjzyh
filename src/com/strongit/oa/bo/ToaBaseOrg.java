package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_BASE_ORG"
 *     
*/
@Entity
@Table(name = "T_OA_BASE_ORG", catalog = "", schema = "")
public class ToaBaseOrg implements Serializable {

    /** identifier field  机构ID*/
    private String orgid;

    /** nullable persistent field  */
   
    /** nullable persistent field */

    private Date personOperateDate;

    /** nullable persistent field */
    private String personOperater;

    /** nullable persistent field */
    private byte[] personDemo;

    /** nullable persistent field */
    private String personFilename;

    /** nullable persistent field 机构代码*/

    private String orgCode;

    /** nullable persistent field 传真号码*/
    private String orgFax;

    /** nullable persistent field 机构名称*/
    private String orgName;

    /** nullable persistent field 地址*/
    private String orgAddr;

    /** nullable persistent field 机构性质*/
    private String orgNature;

    /** nullable persistent field 创建日期 */
    private String orgCreatedate;

    /** nullable persistent field 描述*/
    private String orgDescription;

    /** nullable persistent field 电话*/
    private String orgTel;

    /** nullable persistent field 是否删除*/
    private String orgIsdel;

    /** nullable persistent field 机构编码*/
    private String orgSyscode;

    /** nullable persistent field 负责人*/
    private String orgManager;

    /** nullable persistent field 邮编*/
    private String orgZip;

    /** nullable persistent field 预留*/
    private String rest;//保存统一用户机构代码
    
    private Set<ToaStructure> toaStructures;
    
    /** 机构性质名称*/
    private String orgNatureName;
    
    private String leadNumbe;//领导职务人数

    private String noLeadNumber;//非领导职务人数
    
    private String userOrgid;
    
    private String userOrgcode;
    
   

    /** full constructor */
    public ToaBaseOrg(String orgid, String personConfigFlag, Date personOperateDate, String personOperater, byte[] personDemo, String personFilename, String orgCode, String orgFax, String orgName, String orgAddr, String orgNature, String orgCreatedate, String orgDescription, String orgTel, String orgGrade, String orgSyscode, String orgManager, String orgZip, String rest) {
        this.orgid = orgid;
     
        this.orgCode = orgCode;
        this.orgFax = orgFax;
        this.orgName = orgName;
        this.orgAddr = orgAddr;
        this.orgNature = orgNature;
        this.orgCreatedate = orgCreatedate;
        this.orgDescription = orgDescription;
        this.orgTel = orgTel;
        this.orgSyscode = orgSyscode;
        this.orgManager = orgManager;
        this.orgZip = orgZip;
        this.rest = rest;
       
    }

    /** default constructor */
    public ToaBaseOrg() {
    }

    /** minimal constructor */
    public ToaBaseOrg(String orgid) {
        this.orgid = orgid;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="ORGID"
     *         
     */
	@Id
	@Column(name = "ORGID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getOrgid() {
        return this.orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

  
    @Column(name = "ORG_CODE")
    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_FAX"
     *             length="64"
     *         
     */
    @Column(name = "ORG_FAX")
    public String getOrgFax() {
        return this.orgFax;
    }

    public void setOrgFax(String orgFax) {
        this.orgFax = orgFax;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_NAME"
     *             length="200"
     *         
     */
    @Column(name = "ORG_NAME")
    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_ADDR"
     *             length="264"
     *         
     */
    @Column(name = "ORG_ADDR")
    public String getOrgAddr() {
        return this.orgAddr;
    }

    public void setOrgAddr(String orgAddr) {
        this.orgAddr = orgAddr;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_NATURE"
     *             length="264"
     *         
     */
    @Column(name = "ORG_NATURE",nullable=true)
    public String getOrgNature() {
        return this.orgNature;
    }

    public void setOrgNature(String orgNature) {
        this.orgNature = orgNature;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_CREATEDATE"
     *             length="10"
     *         
     */
    @Column(name = "ORG_CREATEDATE")
    public String getOrgCreatedate() {
        return this.orgCreatedate;
    }

    public void setOrgCreatedate(String orgCreatedate) {
        this.orgCreatedate = orgCreatedate;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_DESCRIPTION"
     *             length="464"
     *         
     */
    @Column(name = "ORG_DESCRIPTION")
    public String getOrgDescription() {
        return this.orgDescription;
    }

    public void setOrgDescription(String orgDescription) {
        this.orgDescription = orgDescription;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_TEL"
     *             length="64"
     *         
     */
    @Column(name = "ORG_TEL")
    public String getOrgTel() {
        return this.orgTel;
    }

    public void setOrgTel(String orgTel) {
        this.orgTel = orgTel;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_GRADE"
     *             length="264"
     *         
     */



    /** 
     *            @hibernate.property
     *             column="ORG_SYSCODE"
     *             length="64"
     *         
     */
    @Column(name = "ORG_SYSCODE")
    public String getOrgSyscode() {
        return this.orgSyscode;
    }

    public void setOrgSyscode(String orgSyscode) {
        this.orgSyscode = orgSyscode;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_MANAGER"
     *             length="64"
     *         
     */
    @Column(name = "ORG_MANAGER")
    public String getOrgManager() {
        return this.orgManager;
    }

    public void setOrgManager(String orgManager) {
        this.orgManager = orgManager;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_ZIP"
     *             length="36"
     *         
     */
    @Column(name = "ORG_ZIP")
    public String getOrgZip() {
        return this.orgZip;
    }

    public void setOrgZip(String orgZip) {
        this.orgZip = orgZip;
    }

    /** 
     *            @hibernate.property
     *             column="REST"
     *             length="64"
     *         
     */
    @Column(name = "REST")
    public String getRest() {
        return this.rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("orgid", getOrgid())
            .toString();
    }
    @Column(name = "ORG_ISDEL")
	public String getOrgIsdel() {
		return orgIsdel;
	}

	public void setOrgIsdel(String orgIsdel) {
		this.orgIsdel = orgIsdel;
	}

	@OneToMany(mappedBy="toaBaseOrg",targetEntity=com.strongit.oa.bo.ToaStructure.class,cascade=CascadeType.ALL)
	public Set<ToaStructure> getToaStructures() {
		return toaStructures;
	}

	public void setToaStructures(Set<ToaStructure> toaStructures) {
		this.toaStructures = toaStructures;
	}

	@Transient
	public String getOrgNatureName() {
		return orgNatureName;
	}

	public void setOrgNatureName(String orgNatureName) {
		this.orgNatureName = orgNatureName;
	}
	  @Column(name = "LEAD_NUMBER") 
	public String getLeadNumbe() {
		return leadNumbe;
	}

	public void setLeadNumbe(String leadNumbe) {
		this.leadNumbe = leadNumbe;
	}
	 @Column(name = "NOLEAD_NUMBER") 
	public String getNoLeadNumber() {
		return noLeadNumber;
	}

	public void setNoLeadNumber(String noLeadNumber) {
		this.noLeadNumber = noLeadNumber;
	}

	@Column(name = "USER_ORGCODE") 
	public String getUserOrgcode() {
		return userOrgcode;
	}

	public void setUserOrgcode(String userOrgcode) {
		this.userOrgcode = userOrgcode;
	}

	@Column(name = "USER_ORGID") 
	public String getUserOrgid() {
		return userOrgid;
	}

	public void setUserOrgid(String userOrgid) {
		this.userOrgid = userOrgid;
	}

}
