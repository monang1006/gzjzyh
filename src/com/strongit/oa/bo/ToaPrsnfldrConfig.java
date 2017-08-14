package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_PRSNFLDR_CONFIG"
 *     
*/
@Entity
@Table(name="T_OA_PRSNFLDR_CONFIG",catalog="",schema="")
public class ToaPrsnfldrConfig implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8555081905688605094L;

	/** identifier field */
    private String configId;

    /** nullable persistent field */
    private String configSize;

    /** nullable persistent field */
    private String configRemark;

    /** persistent field */
    private String userId;

    /** full constructor */
    public ToaPrsnfldrConfig(String configId, String configSize, String configRemark) {
        this.configId = configId;
        this.configSize = configSize;
        this.configRemark = configRemark;
    }

    /** default constructor */
    public ToaPrsnfldrConfig() {
    }

    /** minimal constructor */
    public ToaPrsnfldrConfig(String configId) {
        this.configId = configId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="CONFIG_ID"
     *         
     */
    @Id
    @Column(name="CONFIG_ID")
    @GeneratedValue(generator="hibernate-uuid")
    @GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getConfigId() {
        return this.configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    /** 
     *            @hibernate.property
     *             column="CONFIG_SIZE"
     *             length="32"
     *         
     */
    @Column(name="CONFIG_SIZE")
    public String getConfigSize() {
        return this.configSize;
    }

    public void setConfigSize(String configSize) {
        this.configSize = configSize;
    }

    /** 
     *            @hibernate.property
     *             column="CONFIG_REMARK"
     *             length="4000"
     *         
     */
    @Column(name="CONFIG_REMARK")
    public String getConfigRemark() {
        return this.configRemark;
    }

    public void setConfigRemark(String configRemark) {
        this.configRemark = configRemark;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="USER_ID"         
     *         
     */
    @Column(name="USER_ID")
    public String getUserId() {
    	return userId;
    }
    
    public void setUserId(String userId) {
    	this.userId = userId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("configId", getConfigId())
            .toString();
    }


}
