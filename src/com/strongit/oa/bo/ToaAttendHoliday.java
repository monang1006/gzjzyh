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
 *         法定假日
 *        @hibernate.class
 *         table="T_OA_ATTEND_HOLIDAY"
 *     
*/
@Entity
@Table(name = "T_OA_ATTEND_HOLIDAY", catalog = "", schema = "")
public class ToaAttendHoliday implements Serializable {

    /** identifier field  节假日设置ID*/
	@Id 
	@GeneratedValue
    private String holidayId;

    /** nullable persistent field 假日名称 */
    private String holidayName;

    /** nullable persistent field  开始时间*/
    private String holidayStime;

    /** nullable persistent field  结束时间*/
    private String holidayEtime;

    /** nullable persistent field 假日有效期*/
    private Date henableTime;

    /** nullable persistent field 假日失效期*/
    private Date hdisableTime;

    /** nullable persistent field 是否启用*/
    private String isEnable;

    /** nullable persistent field 假日描述*/
    private String holidayDesc;

    /** nullable persistent field 是否全天*/
    private String isWholeDay;

    /** full constructor */
    public ToaAttendHoliday(String holidayId, String holidayName, String holidayStime, String holidayEtime, Date henableTime, Date hdisableTime, String isEnable, String holidayDesc, String isWholeDay) {
        this.holidayId = holidayId;
        this.holidayName = holidayName;
        this.holidayStime = holidayStime;
        this.holidayEtime = holidayEtime;
        this.henableTime = henableTime;
        this.hdisableTime = hdisableTime;
        this.isEnable = isEnable;
        this.holidayDesc = holidayDesc;
        this.isWholeDay = isWholeDay;
    }

    /** default constructor */
    public ToaAttendHoliday() {
    }

    /** minimal constructor */
    public ToaAttendHoliday(String holidayId) {
        this.holidayId = holidayId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="HOLIDAY_ID"
     *         
     */

    @Id
	@Column(name = "HOLIDAY_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getHolidayId() {
        return this.holidayId;
    }

    public void setHolidayId(String holidayId) {
        this.holidayId = holidayId;
    }

    /** 
     *            @hibernate.property
     *             column="HOLIDAY_NAME"
     *             length="30"
     *         
     */
    @Column(name = "HOLIDAY_NAME")
    public String getHolidayName() {
        return this.holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    /** 
     *            @hibernate.property
     *             column="HOLIDAY_STIME"
     *             length="7"
     *         
     */
    @Column(name = "HOLIDAY_STIME")
    public String getHolidayStime() {
        return this.holidayStime;
    }

    public void setHolidayStime(String holidayStime) {
        this.holidayStime = holidayStime;
    }

    /** 
     *            @hibernate.property
     *             column="HOLIDAY_ETIME"
     *             length="7"
     *         
     */
    @Column(name = "HOLIDAY_ETIME")
    public String getHolidayEtime() {
        return this.holidayEtime;
    }

    public void setHolidayEtime(String holidayEtime) {
        this.holidayEtime = holidayEtime;
    }

    /** 
     *            @hibernate.property
     *             column="H_ENABLE_TIME"
     *             length="7"
     *         
     */
    @Column(name = "H_ENABLE_TIME")
    public Date getHenableTime() {
        return this.henableTime;
    }

    public void setHenableTime(Date henableTime) {
        this.henableTime = henableTime;
    }

    /** 
     *            @hibernate.property
     *             column="H_DISABLE_TIME"
     *             length="7"
     *         
     */
    @Column(name = "H_DISABLE_TIME")
    public Date getHdisableTime() {
        return this.hdisableTime;
    }

    public void setHdisableTime(Date hdisableTime) {
        this.hdisableTime = hdisableTime;
    }

    /** 
     *            @hibernate.property
     *             column="IS_ENABLE"
     *             length="1"
     *         
     */
    @Column(name = "IS_ENABLE")
    public String getIsEnable() {
        return this.isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    /** 
     *            @hibernate.property
     *             column="HOLIDAY_DESC"
     *             length="1000"
     *         
     */
    @Column(name = "HOLIDAY_DESC")
    public String getHolidayDesc() {
        return this.holidayDesc;
    }

    public void setHolidayDesc(String holidayDesc) {
        this.holidayDesc = holidayDesc;
    }

    /** 
     *            @hibernate.property
     *             column="IS_WHOLE_DAY"
     *             length="1"
     *         
     */
    @Column(name = "IS_WHOLE_DAY")
    public String getIsWholeDay() {
        return this.isWholeDay;
    }

    public void setIsWholeDay(String isWholeDay) {
        this.isWholeDay = isWholeDay;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("holidayId", getHolidayId())
            .toString();
    }

}
