package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_ADDRESS_GROUP"
 *     
*/
@Entity
@Table(name="T_OA_ADDRESS_GROUP",catalog="",schema="")
public class ToaAddressGroup implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2484031759724481843L;

	/** identifier field */
    private String addrGroupId;

    /** nullable persistent field */
    private String addrGroupName;

    /** nullable persistent field */
    private String addrGroupRemark;

    /** nullable persistent field */
    private String userId;

    /** persistent field */
    private Set<ToaAddress> toaAddresses;

    /** full constructor */
    public ToaAddressGroup(String addrGroupId, String addrGroupName, String addrGroupRemark, String userId, Set<ToaAddress> toaAddresses) {
        this.addrGroupId = addrGroupId;
        this.addrGroupName = addrGroupName;
        this.addrGroupRemark = addrGroupRemark;
        this.userId = userId;
        this.toaAddresses = toaAddresses;
    }

    /** default constructor */
    public ToaAddressGroup() {
    }

    /** minimal constructor */
    public ToaAddressGroup(String addrGroupId, Set<ToaAddress> toaAddresses) {
        this.addrGroupId = addrGroupId;
        this.toaAddresses = toaAddresses;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="ADDR_GROUP_ID"
     *         
     */
    @Id
    @Column(name="ADDR_GROUP_ID",nullable=false)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getAddrGroupId() {
        return this.addrGroupId;
    }

    public void setAddrGroupId(String addrGroupId) {
        this.addrGroupId = addrGroupId;
    }

    /** 
     *            @hibernate.property
     *             column="ADDR_GROUP_NAME"
     *             length="256"
     *         
     */
    @Column(name="ADDR_GROUP_NAME")
    public String getAddrGroupName() {
        return this.addrGroupName;
    }

    public void setAddrGroupName(String addrGroupName) {
        this.addrGroupName = addrGroupName;
    }

    /** 
     *            @hibernate.property
     *             column="ADDR_GROUP_REMARK"
     *             length="512"
     *         
     */
    @Column(name="ADDR_GROUP_REMARK")
    public String getAddrGroupRemark() {
        return this.addrGroupRemark;
    }

    public void setAddrGroupRemark(String addrGroupRemark) {
        this.addrGroupRemark = addrGroupRemark;
    }

    /** 
     *            @hibernate.property
     *             column="USER_ID"
     *             length="32"
     *         
     */
    @Column(name="USER_ID")
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="ADDR_GROUP_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaAddress"
     *         
     */
    @OneToMany(mappedBy="toaAddressGroup",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    public Set<ToaAddress> getToaAddresses() {
        return this.toaAddresses;
    }

    public void setToaAddresses(Set<ToaAddress> toaAddresses) {
        this.toaAddresses = toaAddresses;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("addrGroupId", getAddrGroupId())
            .toString();
    }

}
