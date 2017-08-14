package com.strongit.oa.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.strongit.oa.bo.ToaUnitsBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="T_OA_REPORT_BEAN")
public class ToaReportBean1 implements Serializable{
    
    private String department;//处室
    private String businessId;
    private String title;
    private List<ToaUnitsBean> unitsList;
    private String returnReason;
    private Date gwDeptStartTime;//公文处室办理开始时间
    private int totalGw;//公文总数
    /**
     * 百分比
     */
    private BigDecimal percentageReturn;
    //private String text5;
    @Id
    @GeneratedValue
    private String id;
    private String units;//来文单位
    private Date receiveTime;//收文时间
    private Date sendTime;//发出时间
    private String guFinishPrior;//公文处室办理时段
    private int fixTime;//规定时限
    private int returnPrior;//反馈时段
    private String sendCode;//发文编号
    private String returnFlg;//退文标志
    private String glFinishPrior;//公文领导签批时段
    private String fuFinishPrior;//发文处室办理时段
    private String flFinishPrior;//发文领导签批时段
    private int finishPrior;//办结时段
    private String finishflg;//办结状态
    private String  onTimeFlg;//按时办结状态
    private Date turnTime;//转办时间
    private Date feedBackTime;//转征询时间
    public ToaReportBean1(String department, String businessId, String title,
            String returnReason, int totalGw,
            BigDecimal percentageReturn, String id, String units,
            Date receiveTime, Date sendTime, String guFinishPrior, int fixTime,
            int returnPrior, String sendCode, String returnFlg,
            String glFinishPrior, String fuFinishPrior, String flFinishPrior,
            int finishPrior, String finishflg, String onTimeFlg, Date turnTime,Date gwDeptStartTime) {
        super();
        this.department = department;
        this.businessId = businessId;
        this.title = title;
        this.returnReason = returnReason;
        this.totalGw = totalGw;
        this.percentageReturn = percentageReturn;
        this.id = id;
        this.units = units;
        this.receiveTime = receiveTime;
        this.sendTime = sendTime;
        this.guFinishPrior = guFinishPrior;
        this.fixTime = fixTime;
        this.returnPrior = returnPrior;
        this.sendCode = sendCode;
        this.returnFlg = returnFlg;
        this.glFinishPrior = glFinishPrior;
        this.fuFinishPrior = fuFinishPrior;
        this.flFinishPrior = flFinishPrior;
        this.finishPrior = finishPrior;
        this.finishflg = finishflg;
        this.onTimeFlg = onTimeFlg;
        this.turnTime = turnTime;
        this.gwDeptStartTime = gwDeptStartTime;
    }

    public ToaReportBean1() {
        
    }
    @Column(name = "DEPARTMENT", nullable = true)
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    @Column(name = "BUSINESS_ID", nullable = true)
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    @Column(name = "TITLE", nullable = true)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Column(name = "RETURN_REASON", nullable = true)
    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }
    @Transient
    public int getTotalGw() {
        return totalGw;
    }

    public void setTotalGw(int totalGw) {
        this.totalGw = totalGw;
    }
    @Transient
    public BigDecimal getPercentageReturn() {
        return percentageReturn;
    }

    public void setPercentageReturn(BigDecimal percentageReturn) {
        this.percentageReturn = percentageReturn;
    }
    @Id
    @Column(name="ID", nullable=false, length=32)
    @GeneratedValue(generator="hibernate-uuid")
    @GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "UNITS", nullable = true)
    public String getUnits() {
        return units;
    }
    
    public void setUnits(String units) {
        this.units = units;
    }
    @Column(name = "RECEIVE_TIME", nullable = true)
    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }
    @Column(name = "SEND_TIME", nullable = true)
    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
    @Column(name = "GU_FINISH_PRIOR", nullable = true)
    public String getGuFinishPrior() {
        return guFinishPrior;
    }

    public void setGuFinishPrior(String guFinishPrior) {
        this.guFinishPrior = guFinishPrior;
    }
    @Column(name = "FIX_TIME", nullable = true)
    public int getFixTime() {
        return fixTime;
    }

    public void setFixTime(int fixTime) {
        this.fixTime = fixTime;
    }
    @Column(name = "RETURN_PRIOR", nullable = true)
    public int getReturnPrior() {
        return returnPrior;
    }

    public void setReturnPrior(int returnPrior) {
        this.returnPrior = returnPrior;
    }
    @Column(name = "SEND_CODE", nullable = true)
    public String getSendCode() {
        return sendCode;
    }

    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    @Column(name = "RETURN_FLG", nullable = true)
    public String getReturnFlg() {
        return returnFlg;
    }

    public void setReturnFlg(String returnFlg) {
        this.returnFlg = returnFlg;
    }
    @Column(name = "GL_FINISH_PRIOR", nullable = true)
    public String getGlFinishPrior() {
        return glFinishPrior;
    }

    public void setGlFinishPrior(String glFinishPrior) {
        this.glFinishPrior = glFinishPrior;
    }
    @Column(name = "FU_FINISH_PRIOR", nullable = true)
    public String getFuFinishPrior() {
        return fuFinishPrior;
    }

    public void setFuFinishPrior(String fuFinishPrior) {
        this.fuFinishPrior = fuFinishPrior;
    }
    @Column(name = "FL_FINISH_PRIOR", nullable = true)
    public String getFlFinishPrior() {
        return flFinishPrior;
    }

    public void setFlFinishPrior(String flFinishPrior) {
        this.flFinishPrior = flFinishPrior;
    }
    @Column(name = "FINISH_PRIOR", nullable = true)
    public int getFinishPrior() {
        return finishPrior;
    }

    public void setFinishPrior(int finishPrior) {
        this.finishPrior = finishPrior;
    }
    @Column(name = "FINISH_FLG", nullable = true)
    public String getFinishflg() {
        return finishflg;
    }

    public void setFinishflg(String finishflg) {
        this.finishflg = finishflg;
    }
    @Column(name = "ON_TIME_FLG", nullable = true)
    public String getOnTimeFlg() {
        return onTimeFlg;
    }

    public void setOnTimeFlg(String onTimeFlg) {
        this.onTimeFlg = onTimeFlg;
    }
    @Column(name = "TURN_TIME", nullable = true)
    public Date getTurnTime() {
        return turnTime;
    }

    public void setTurnTime(Date turnTime) {
        this.turnTime = turnTime;
    }
    @Transient
    public List<ToaUnitsBean> getUnitsList() {
        return unitsList;
    }

    public void setUnitsList(List<ToaUnitsBean> unitsList) {
        this.unitsList = unitsList;
    }
    @Column(name = "FEEDBACK_TIME", nullable = true)
    public Date getFeedBackTime() {
        return feedBackTime;
    }

    public void setFeedBackTime(Date feedBackTime) {
        this.feedBackTime = feedBackTime;
    }
    @Column(name = "GW_DEPT_START_TIME", nullable = true)
    public Date getGwDeptStartTime() {
        return gwDeptStartTime;
    }

    public void setGwDeptStartTime(Date gwDeptStartTime) {
        this.gwDeptStartTime = gwDeptStartTime;
    }
    
    /*public ToaReportBean(String[] arr){
        this.department = arr[0];
        this.title = arr[1];
        this.returnReason = arr[2];
        this.totalGw = arr[3];
        this.percentageReturn = arr[4];
        this.text6 = arr[5];
        this.text7 = arr[6];
        this.text8 = arr[7];
        this.text9 = arr[8];
        this.text10 = arr[9];
        this.text11 = arr[10];
        this.text12 = arr[11];
        this.text13 = arr[12];
        this.text14 = arr[13];
        this.text15 = arr[14];
        this.text16 = arr[15];
        this.text17 = arr[16];
        this.text18 = arr[17];
        this.text19 = arr[18];
        this.text20 = arr[19];
        this.text21 = arr[20];
        this.text22 = arr[21];
        this.text23 = arr[22];
        this.text24 = arr[23];
        this.text25 = arr[24];
        this.text26 = arr[25];
        this.text27 = arr[26];
        this.text28 = arr[27];
        this.text29 = arr[28];
        this.text30 = arr[29];
        this.text31 = arr[30];
        this.text32 = arr[31];
        this.text33 = arr[32];
        this.text34 = arr[33];
        this.text35 = arr[34];
        this.text36 = arr[35];
        this.text37 = arr[36];
        this.text38 = arr[37];
        this.text39 = arr[38];
        this.text40 = arr[39];
        this.text41 = arr[40];
        this.text42 = arr[41];
        this.text43 = arr[42];
        this.text44 = arr[43];
        this.text45 = arr[44];
    }*/

    
    

}