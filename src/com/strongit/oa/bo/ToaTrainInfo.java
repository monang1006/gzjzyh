package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_TRAIN_INFO"
 *     
*/
@Entity
@Table(name = "T_OA_TRAIN_INFO", catalog = "", schema = "")
public class ToaTrainInfo implements Serializable {

    /** identifier field */
    private String trainId;//培训信息ID

    /** nullable persistent field */
    private String trainType;//培训类别

    /** nullable persistent field */
    private String trainTopic;//培训主题

    /** nullable persistent field */
    private String trainMoney;//培训费用

    /** nullable persistent field */
    private String trainCommpany;//主办单位

    /** nullable persistent field */
    private String trainTotaltime;//总学时

    /** nullable persistent field */
    private Date trainStartdate;//培训起始时间

    /** nullable persistent field */
    private Date trainEnddate;//培训截止时间

    /** nullable persistent field */
    private String trainDemo;//备注
    
    private byte[] attachCon;

    /** nullable persistent field */
    private String rest1;

    /** nullable persistent field */
    private String rest2;

    private com.strongit.oa.bo.ToaTrainColumn toaTrainColumn;
    /** persistent field */


    /** full constructor */
    public ToaTrainInfo(String trainId, String trainType, String trainTopic, String trainMoney, String trainCommpany, String trainTotaltime, Date trainStartdate, Date trainEnddate, String trainDemo, String rest1, String rest2) {
        this.trainId = trainId;
        this.trainType = trainType;
        this.trainTopic = trainTopic;
        this.trainMoney = trainMoney;
        this.trainCommpany = trainCommpany;
        this.trainTotaltime = trainTotaltime;
        this.trainStartdate = trainStartdate;
        this.trainEnddate = trainEnddate;
        this.trainDemo = trainDemo;
        this.rest1 = rest1;
        this.rest2 = rest2;
      
    }

    /** default constructor */
    public ToaTrainInfo() {
    }

    /** minimal constructor */
    public ToaTrainInfo(String trainId) {
        this.trainId = trainId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="TRAIN_ID"
     *         
     */
	@Id
	@Column(name = "TRAIN_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getTrainId() {
        return this.trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    /** 
     *            @hibernate.property
     *             column="TRAIN_TYPE"
     *             length="32"
     *         
     */
    @Column(name = "TRAIN_TYPE")
    public String getTrainType() {
        return this.trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    /** 
     *            @hibernate.property
     *             column="TRAIN_TOPIC"
     *             length="232"
     *         
     */
    @Column(name = "TRAIN_TOPIC")
    public String getTrainTopic() {
        return this.trainTopic;
    }

    public void setTrainTopic(String trainTopic) {
        this.trainTopic = trainTopic;
    }

    /** 
     *            @hibernate.property
     *             column="TRAIN_MONEY"
     *             length="32"
     *         
     */
    @Column(name = "TRAIN_MONEY")
    public String getTrainMoney() {
        return this.trainMoney;
    }

    public void setTrainMoney(String trainMoney) {
        this.trainMoney = trainMoney;
    }

    /** 
     *            @hibernate.property
     *             column="TRAIN_COMMPANY"
     *             length="32"
     *         
     */
    @Column(name = "TRAIN_COMMPANY")
    public String getTrainCommpany() {
        return this.trainCommpany;
    }

    public void setTrainCommpany(String trainCommpany) {
        this.trainCommpany = trainCommpany;
    }

    /** 
     *            @hibernate.property
     *             column="TRAIN_TOTALTIME"
     *             length="12"
     *         
     */
    @Column(name = "TRAIN_TOTALTIME")
    public String getTrainTotaltime() {
        return this.trainTotaltime;
    }

    public void setTrainTotaltime(String trainTotaltime) {
        this.trainTotaltime = trainTotaltime;
    }

    /** 
     *            @hibernate.property
     *             column="TRAIN_STARTDATE"
     *             length="32"
     *         
     */
    @Column(name = "TRAIN_STARTDATE")
    public Date getTrainStartdate() {
        return this.trainStartdate;
    }

    public void setTrainStartdate(Date trainStartdate) {
        this.trainStartdate = trainStartdate;
    }

    /** 
     *            @hibernate.property
     *             column="TRAIN_ENDDATE"
     *             length="32"
     *         
     */
    @Column(name = "TRAIN_ENDDATE")
    public Date getTrainEnddate() {
        return this.trainEnddate;
    }

    public void setTrainEnddate(Date trainEnddate) {
        this.trainEnddate = trainEnddate;
    }

    /** 
     *            @hibernate.property
     *             column="TRAIN_DEMO"
     *             length="232"
     *         
     */
    @Column(name = "TRAIN_DEMO")
    public String getTrainDemo() {
        return this.trainDemo;
    }

    public void setTrainDemo(String trainDemo) {
        this.trainDemo = trainDemo;
    }

    /** 
     *            @hibernate.property
     *             column="REST1"
     *             length="32"
     *         
     */
    @Column(name = "REST1")
    public String getRest1() {
        return this.rest1;
    }

    public void setRest1(String rest1) {
        this.rest1 = rest1;
    }

    /** 
     *            @hibernate.property
     *             column="REST2"
     *             length="32"
     *         
     */
    @Column(name = "REST2")
    public String getRest2() {
        return this.rest2;
    }

    public void setRest2(String rest2) {
        this.rest2 = rest2;
    }

  
    

    public String toString() {
        return new ToStringBuilder(this)
            .append("trainId", getTrainId())
            .toString();
    }
    @Column(name = "TRAIN_ATTACH_CON")
	public byte[] getAttachCon() {
		return attachCon;
	}

	public void setAttachCon(byte[] attachCon) {
		this.attachCon = attachCon;
	}

	 @ManyToOne()
	@JoinColumn(name = "CLUMN_ID", nullable = true)
	public com.strongit.oa.bo.ToaTrainColumn getToaTrainColumn() {
		return toaTrainColumn;
	}

	public void setToaTrainColumn(com.strongit.oa.bo.ToaTrainColumn toaTrainColumn) {
		this.toaTrainColumn = toaTrainColumn;
	}

}
