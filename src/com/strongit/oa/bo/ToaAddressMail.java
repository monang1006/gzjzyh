package com.strongit.oa.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_ADDRESS_MAIL"
 *     
*/
@Entity
@Table(name="T_OA_ADDRESS_MAIL",catalog="",schema="")
public class ToaAddressMail implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8307612572119951896L;

	/** identifier field */
    private String addrMailId;

    /** nullable persistent field */
    private String mail;

    /** nullable persistent field */
    private String isDefault;

    /** persistent field */
    private com.strongit.oa.bo.ToaAddress toaAddress;

    /** full constructor */
    public ToaAddressMail(String addrMailId, String mail, String isDefault, com.strongit.oa.bo.ToaAddress toaAddress) {
        this.addrMailId = addrMailId;
        this.mail = mail;
        this.isDefault = isDefault;
        this.toaAddress = toaAddress;
    }

    /** default constructor */
    public ToaAddressMail() {
    }

    /** minimal constructor */
    public ToaAddressMail(String addrMailId, com.strongit.oa.bo.ToaAddress toaAddress) {
        this.addrMailId = addrMailId;
        this.toaAddress = toaAddress;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="ADDR_MAIL_ID"
     *         
     */
    @Id
    @Column(name="ADDR_MAIL_ID")
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getAddrMailId() {
        return this.addrMailId;
    }

    public void setAddrMailId(String addrMailId) {
        this.addrMailId = addrMailId;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL"
     *             length="256"
     *         
     */
    @Column(name="MAIL")
    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    /** 
     *            @hibernate.property
     *             column="IS_DEFAULT"
     *             length="22"
     *         
     */
    @Column(name="IS_DEFAULT")
    public String getIsDefault() {
        return this.isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="ADDR_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ADDR_ID")
    public com.strongit.oa.bo.ToaAddress getToaAddress() {
        return this.toaAddress;
    }

    public void setToaAddress(com.strongit.oa.bo.ToaAddress toaAddress) {
        this.toaAddress = toaAddress;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("addrMailId", getAddrMailId())
            .toString();
    }

}
