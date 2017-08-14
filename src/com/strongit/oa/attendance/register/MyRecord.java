package com.strongit.oa.attendance.register;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 考勤明细临时BO
 * @author 胡丽丽
 * @date:2009-12-02
 *
 */
public class MyRecord {
	 /** identifier field */
    private String attendId;

    /** nullable persistent field用户ID */
    private String userId;

    /** nullable persistent field 用户名 */
    private String userName;

    /** nullable persistent field 部门ID */
    private String orgId;

    /** nullable persistent field 部门名称*/
    private String orgName;

    /** nullable persistent field 出勤日期*/
    private String attendTime;

    private List<Object> workList;
//    /** nullable persistent field 规定上班时间*/
//    private String mregulationTime;
//
//    /** nullable persistent field 上班登记时间 */
//    private Date mregisterTime;
//
//    /** nullable persistent field 规定下班时间 */
//    private String aregulationTime;
//
//    /** nullable persistent field下班登记时间*/
//    private Date aregisterTime;

    /** nullable persistent field 迟到时间（分）*/
    private int attendLaterTime;

    /** nullable persistent field 早退时间（分）*/
    private int attendEarlyTime;

    /** nullable persistent field 缺勤时数*/
    private BigDecimal absenceHours;
    
    /** nullable persistent field 类型ID*/
    private String attendanceTypeId;

    /** nullable persistent field 类型（事假、病假、婚假、旷工、加班、调休、出差等）*/
    private String attendanceType;

    /** nullable persistent field 是否为缺勤统计项*/
    private String isCalcuAbsence;

    /** nullable persistent field 是否已计算。0、‘’：没计算；1：已计算*/
    private String isCalcu;

    /** nullable persistent field 考勤描述*/
    private String attendDesc;

    /** nullable persistent field 应出勤（时）*/
    private BigDecimal shouldAttendHours;

    private String test1;
    private String test2;
    private String test3;
    private String test4;
    private String test5;
    private String test6;
    private String test7;
    /** 加班时间*/
    private BigDecimal jiaBanHours;
	public BigDecimal getJiaBanHours() {
		return jiaBanHours;
	}

	public void setJiaBanHours(BigDecimal jiaBanHours) {
		this.jiaBanHours = jiaBanHours;
	}

	public List<Object> getWorkList() {
		return workList;
	}

	public void setWorkList(List<Object> workList) {
		this.workList = workList;
	}

	public String getAttendId() {
		return attendId;
	}

	public void setAttendId(String attendId) {
		this.attendId = attendId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAttendTime() {
		return attendTime;
	}

	public void setAttendTime(String attendTime) {
		this.attendTime = attendTime;
	}

	public int getAttendLaterTime() {
		return attendLaterTime;
	}

	public void setAttendLaterTime(int attendLaterTime) {
		this.attendLaterTime = attendLaterTime;
	}

	public int  getAttendEarlyTime() {
		return attendEarlyTime;
	}

	public void setAttendEarlyTime(int attendEarlyTime) {
		this.attendEarlyTime = attendEarlyTime;
	}

	public BigDecimal getAbsenceHours() {
		return absenceHours;
	}

	public void setAbsenceHours(BigDecimal absenceHours) {
		this.absenceHours = absenceHours;
	}

	public String getAttendanceTypeId() {
		return attendanceTypeId;
	}

	public void setAttendanceTypeId(String attendanceTypeId) {
		this.attendanceTypeId = attendanceTypeId;
	}

	public String getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(String attendanceType) {
		this.attendanceType = attendanceType;
	}

	public String getIsCalcuAbsence() {
		return isCalcuAbsence;
	}

	public void setIsCalcuAbsence(String isCalcuAbsence) {
		this.isCalcuAbsence = isCalcuAbsence;
	}

	public String getIsCalcu() {
		return isCalcu;
	}

	public void setIsCalcu(String isCalcu) {
		this.isCalcu = isCalcu;
	}

	public String getAttendDesc() {
		return attendDesc;
	}

	public void setAttendDesc(String attendDesc) {
		this.attendDesc = attendDesc;
	}

	public BigDecimal getShouldAttendHours() {
		return shouldAttendHours;
	}

	public void setShouldAttendHours(BigDecimal shouldAttendHours) {
		this.shouldAttendHours = shouldAttendHours;
	}

	public String getTest1() {
		return test1;
	}

	public void setTest1(String test1) {
		this.test1 = test1;
	}

	public String getTest2() {
		return test2;
	}

	public void setTest2(String test2) {
		this.test2 = test2;
	}

	public String getTest3() {
		return test3;
	}

	public void setTest3(String test3) {
		this.test3 = test3;
	}

	public String getTest4() {
		return test4;
	}

	public void setTest4(String test4) {
		this.test4 = test4;
	}

	public String getTest5() {
		return test5;
	}

	public void setTest5(String test5) {
		this.test5 = test5;
	}

	public String getTest6() {
		return test6;
	}

	public void setTest6(String test6) {
		this.test6 = test6;
	}

	public String getTest7() {
		return test7;
	}

	public void setTest7(String test7) {
		this.test7 = test7;
	}

}
