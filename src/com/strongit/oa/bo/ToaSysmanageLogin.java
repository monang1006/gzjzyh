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
 *         table="T_OA_SYSMANAGE_LOGIN"
 *     
*/
@Entity
@Table(name="T_OA_SYSMANAGE_LOGIN")
public class ToaSysmanageLogin implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String loginId;

    /** nullable persistent field */
    private String loginBeginIp;

    /** nullable persistent field */
    private String loginEndIp;

    /** nullable persistent field */
    private String loginType;

    /** nullable persistent field */
    private String loginDesc;

    /** full constructor */
    public ToaSysmanageLogin(String loginId, String loginBeginIp, String loginEndIp, String loginType, String loginDesc) {
        this.loginId = loginId;
        this.loginBeginIp = loginBeginIp;
        this.loginEndIp = loginEndIp;
        this.loginType = loginType;
        this.loginDesc = loginDesc;
    }

    /** default constructor */
    public ToaSysmanageLogin() {
    }

    /** minimal constructor */
    public ToaSysmanageLogin(String loginId) {
        this.loginId = loginId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="LOGIN_ID"
     *         
     */
    @Id
	@Column(name="LOGIN_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getLoginId() {
        return this.loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /** 
     *            @hibernate.property
     *             column="LOGIN_BEGIN_IP"
     *             length="30"
     *         
     */
    @Column(name="LOGIN_BEGIN_IP",nullable=true)
    public String getLoginBeginIp() {
        return this.loginBeginIp;
    }

    public void setLoginBeginIp(String loginBeginIp) {
        this.loginBeginIp = loginBeginIp;
    }

    /** 
     *            @hibernate.property
     *             column="LOGIN_END_IP"
     *             length="30"
     *         
     */
    @Column(name="LOGIN_END_IP",nullable=true)
    public String getLoginEndIp() {
        return this.loginEndIp;
    }

    public void setLoginEndIp(String loginEndIp) {
        this.loginEndIp = loginEndIp;
    }

    /** 
     *            @hibernate.property
     *             column="LOGIN_TYPE"
     *             length="1"
     *         
     */
    @Column(name="LOGIN_TYPE",nullable=true)
    public String getLoginType() {
        return this.loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    /** 
     *            @hibernate.property
     *             column="LOGIN_DESC"
     *             length="400"
     *         
     */
    @Column(name="LOGIN_DESC",nullable=true)
    public String getLoginDesc() {
        return this.loginDesc;
    }

    public void setLoginDesc(String loginDesc) {
        this.loginDesc = loginDesc;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("loginId", getLoginId())
            .toString();
    }

}
