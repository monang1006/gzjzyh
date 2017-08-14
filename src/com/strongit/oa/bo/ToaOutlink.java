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
 * <p>Title: ToaOutlink.java</p>
 * <p>Description: 对应表T_OA_OUTLINK BO类</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2010-03-22 22:31:39
 * @version  1.0
 */

@Entity
@Table(name="T_OA_OUTLINK")
public class ToaOutlink implements Serializable{
	
	private static final long serialVersionUID = 1163741935045070734L;
    /** identifier field */
    private String outlinkId;

    /** nullable persistent field */
    private String outlinkDes;

    /** nullable persistent field */
    private String outlinkAddress;

    /** nullable persistent field */
    private String outlinkClass;

    /** nullable persistent field */
    private String outlinkType;

    /** nullable persistent field */
    private Date outlinkDate;

    /** full constructor */
    public ToaOutlink(String outlinkId, String outlinkDes, String outlinkAddress, String outlinkClass, String outlinkType, Date outlinkDate) {
        this.outlinkId = outlinkId;
        this.outlinkDes = outlinkDes;
        this.outlinkAddress = outlinkAddress;
        this.outlinkClass = outlinkClass;
        this.outlinkType = outlinkType;
        this.outlinkDate = outlinkDate;
    }

    /** default constructor */
    public ToaOutlink() {
    }

    /** minimal constructor */
    public ToaOutlink(String outlinkId) {
        this.outlinkId = outlinkId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="OUTLINK_ID"
     *         
     */
    @Id
	@Column(name="OUTLINK_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getOutlinkId() {
        return this.outlinkId;
    }

    public void setOutlinkId(String outlinkId) {
        this.outlinkId = outlinkId;
    }

    /** 
     *            @hibernate.property
     *             column="OUTLINK_DES"
     *             length="100"
     *         
     */
    @Column(name="OUTLINK_DES",nullable=true,length=100)
    public String getOutlinkDes() {
        return this.outlinkDes;
    }

    public void setOutlinkDes(String outlinkDes) {
        this.outlinkDes = outlinkDes;
    }

    /** 
     *            @hibernate.property
     *             column="OUTLINK_ADDRESS"
     *             length="1000"
     *         
     */
    @Column(name="OUTLINK_ADDRESS",nullable=true,length=1000)
    public String getOutlinkAddress() {
        return this.outlinkAddress;
    }

    public void setOutlinkAddress(String outlinkAddress) {
        this.outlinkAddress = outlinkAddress;
    }

    /** 
     *            @hibernate.property
     *             column="OUTLINK_CLASS"
     *             length="500"
     *         
     */
    @Column(name="OUTLINK_CLASS",nullable=true,length=500)
    public String getOutlinkClass() {
        return this.outlinkClass;
    }

    public void setOutlinkClass(String outlinkClass) {
        this.outlinkClass = outlinkClass;
    }

    /** 
     *            @hibernate.property
     *             column="OUTLINK_TYPE"
     *             length="1"
     *         
     */
    @Column(name="OUTLINK_TYPE",nullable=true,length=1)
    public String getOutlinkType() {
        return this.outlinkType;
    }

    public void setOutlinkType(String outlinkType) {
        this.outlinkType = outlinkType;
    }

    /** 
     *            @hibernate.property
     *             column="OUTLINK_DATE"
     *             length="7"
     *         
     */
    @Column(name="OUTLINK_DATE",nullable=true,length=7)
    public Date getOutlinkDate() {
        return this.outlinkDate;
    }

    public void setOutlinkDate(Date outlinkDate) {
        this.outlinkDate = outlinkDate;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("outlinkId", getOutlinkId())
            .toString();
    }


}
