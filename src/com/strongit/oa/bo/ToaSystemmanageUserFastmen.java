package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSTEMMANAGE_USER_FASTMEN"
 *     
*/
@Entity
@Table(name="T_OA_SYSTEMMANAGE_USER_FASTMEN")
public class ToaSystemmanageUserFastmen implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String fastmenuId;

    /** nullable persistent field */
    private String moderIds;

    /** nullable persistent field */
    private Date createTime;

    /** persistent field */
    private String userid;
    
    private String systemLogo;

    /** full constructor */
    public ToaSystemmanageUserFastmen(String fastmenuId, String moderIds, Date createTime,String userid) {
        this.fastmenuId = fastmenuId;
        this.moderIds = moderIds;
        this.createTime = createTime;
        this.userid = userid;
    }

    /** default constructor */
    public ToaSystemmanageUserFastmen() {
    }

    /** minimal constructor */
    public ToaSystemmanageUserFastmen(String fastmenuId,String userid) {
        this.fastmenuId = fastmenuId;
        this.userid = userid;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="FASTMENU_ID"
     *         
     */
    @Id
	@Column(name="FASTMENU_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getFastmenuId() {
        return this.fastmenuId;
    }

    public void setFastmenuId(String fastmenuId) {
        this.fastmenuId = fastmenuId;
    }

    /** 
     *            @hibernate.property
     *             column="MODER_IDS"
     *             length="400"
     *         
     */
    @Column(name="MODER_IDS",nullable=true)
    public String getModerIds() {
        return this.moderIds;
    }

    public void setModerIds(String moderIds) {
        this.moderIds = moderIds;
    }

    /** 
     *            @hibernate.property
     *             column="CREATE_TIME"
     *             length="7"
     *         
     */
    @Column(name="CREATE_TIME",nullable=true)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    /** 
     *            @hibernate.property
     *             column="USER_ID"
     *             length="32"
     *         
     */
    @Column(name="USER_ID",nullable=true)
    public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
    public String toString() {
        return new ToStringBuilder(this)
            .append("fastmenuId", getFastmenuId())
            .toString();
    }

    
    /** 
     *            @hibernate.property
     *             column="SYSTEM_SETTING"
     *             length="1"
     *         
     */
    @Column(name="SYSTEM_SETTING",nullable=false)
	public String getSystemLogo() {
		return systemLogo;
	}

	public void setSystemLogo(String systemLogo) {
		this.systemLogo = systemLogo;
	}

	

}
