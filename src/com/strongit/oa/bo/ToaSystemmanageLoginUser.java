package com.strongit.oa.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSTEMMANAGE_LOGIN_USER"
 *     
*/
public class ToaSystemmanageLoginUser implements Serializable {

    /** identifier field */
    private String loginUserId;

    /** nullable persistent field */
    private String loginUser;

    /** full constructor */
    public ToaSystemmanageLoginUser(String loginUserId, String loginUser) {
        this.loginUserId = loginUserId;
        this.loginUser = loginUser;
    }

    /** default constructor */
    public ToaSystemmanageLoginUser() {
    }

    /** minimal constructor */
    public ToaSystemmanageLoginUser(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="LOGIN_USER_ID"
     *         
     */
    public String getLoginUserId() {
        return this.loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    /** 
     *            @hibernate.property
     *             column="LOGIN_USER"
     *             length="4000"
     *         
     */
    public String getLoginUser() {
        return this.loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("loginUserId", getLoginUserId())
            .toString();
    }

}
