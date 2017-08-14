package com.strongit.oa.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="USER_TEST"
 *     
*/
public class UserTest implements Serializable {

    /** identifier field */
    private String id;

    /** identifier field */
    private String userid;

    /** identifier field */
    private String username;

    /** identifier field */
    private String addr;

    /** full constructor */
    public UserTest(String id, String userid, String username, String addr) {
        this.id = id;
        this.userid = userid;
        this.username = username;
        this.addr = addr;
    }

    /** default constructor */
    public UserTest() {
    }

    /** 
     *                @hibernate.property
     *                 column="ID"
     *                 length="32"
     *             
     */
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /** 
     *                @hibernate.property
     *                 column="USERID"
     *                 length="32"
     *             
     */
    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    /** 
     *                @hibernate.property
     *                 column="USERNAME"
     *                 length="20"
     *             
     */
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /** 
     *                @hibernate.property
     *                 column="ADDR"
     *                 length="100"
     *             
     */
    public String getAddr() {
        return this.addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .append("userid", getUserid())
            .append("username", getUsername())
            .append("addr", getAddr())
            .toString();
    }

}
