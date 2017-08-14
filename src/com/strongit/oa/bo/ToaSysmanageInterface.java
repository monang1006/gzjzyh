package com.strongit.oa.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_INTERFACE"
 *     
*/
public class ToaSysmanageInterface implements Serializable {

    /** identifier field */
    private String interfaceId;

    /** nullable persistent field */
    private String interfaceUsername;

    /** nullable persistent field */
    private String interfacePassword;

    /** nullable persistent field */
    private String interfaceOpenstate;

    /** nullable persistent field */
    private String interfaceDesc;

    /** full constructor */
    public ToaSysmanageInterface(String interfaceId, String interfaceUsername, String interfacePassword, String interfaceOpenstate, String interfaceDesc) {
        this.interfaceId = interfaceId;
        this.interfaceUsername = interfaceUsername;
        this.interfacePassword = interfacePassword;
        this.interfaceOpenstate = interfaceOpenstate;
        this.interfaceDesc = interfaceDesc;
    }

    /** default constructor */
    public ToaSysmanageInterface() {
    }

    /** minimal constructor */
    public ToaSysmanageInterface(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="INTERFACE_ID"
     *         
     */
    public String getInterfaceId() {
        return this.interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    /** 
     *            @hibernate.property
     *             column="INTERFACE_USERNAME"
     *             length="30"
     *         
     */
    public String getInterfaceUsername() {
        return this.interfaceUsername;
    }

    public void setInterfaceUsername(String interfaceUsername) {
        this.interfaceUsername = interfaceUsername;
    }

    /** 
     *            @hibernate.property
     *             column="INTERFACE_PASSWORD"
     *             length="30"
     *         
     */
    public String getInterfacePassword() {
        return this.interfacePassword;
    }

    public void setInterfacePassword(String interfacePassword) {
        this.interfacePassword = interfacePassword;
    }

    /** 
     *            @hibernate.property
     *             column="INTERFACE_OPENSTATE"
     *             length="1"
     *         
     */
    public String getInterfaceOpenstate() {
        return this.interfaceOpenstate;
    }

    public void setInterfaceOpenstate(String interfaceOpenstate) {
        this.interfaceOpenstate = interfaceOpenstate;
    }

    /** 
     *            @hibernate.property
     *             column="INTERFACE_DESC"
     *             length="400"
     *         
     */
    public String getInterfaceDesc() {
        return this.interfaceDesc;
    }

    public void setInterfaceDesc(String interfaceDesc) {
        this.interfaceDesc = interfaceDesc;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("interfaceId", getInterfaceId())
            .toString();
    }

}
