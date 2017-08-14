package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSTEMMANAGE_SYSTEM_LINK"
 *     
*/
@Entity
@Table(name="T_OA_SYSTEMMANAGE_SYSTEM_LINK")
public class ToaSystemmanageSystemLink implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String linkId;

    /** nullable persistent field */
    private String linkUrl;

    /** nullable persistent field */
    private String systemDesc;
    
    private String systemName;

    /** full constructor */
    public ToaSystemmanageSystemLink(String linkId, String linkUrl, String systemDesc) {
        this.linkId = linkId;
        this.linkUrl = linkUrl;
        this.systemDesc = systemDesc;
    }

    /** default constructor */
    public ToaSystemmanageSystemLink() {
    }

    /** minimal constructor */
    public ToaSystemmanageSystemLink(String linkId) {
        this.linkId = linkId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="LINK_ID"
     *         
     */
    @Id
	@Column(name="LINK_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getLinkId() {
        return this.linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    /** 
     *            @hibernate.property
     *             column="LINK_URL"
     *             length="200"
     *         
     */
    @Column(name="LINK_URL",nullable=true)
    public String getLinkUrl() {
        return this.linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    /** 
     *            @hibernate.property
     *             column="SYSTEM_DESC"
     *             length="4000"
     *         
     */
    @Column(name="SYSTEM_DESC",nullable=true)
    public String getSystemDesc() {
        return this.systemDesc;
    }

    public void setSystemDesc(String systemDesc) {
        this.systemDesc = systemDesc;
    }
    
    @Column(name="SYSTEM_NAME",nullable=true)
    public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .append("linkId", getLinkId())
            .toString();
    }

	

}
