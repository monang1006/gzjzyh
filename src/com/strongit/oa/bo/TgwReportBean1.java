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

public class TgwReportBean1 implements Serializable{
    private String title;
    private String remarks;
    private String units;//来文单位
    private String receiveTime;//收文时间
    private String sendTime;//发出时间
    private String guFinishPrior;//公文处室办理时段
    private String fixTime;//规定时限
    private String returnPrior;//反馈时段
    private String sendCode;//发文编号
    private String glFinishPrior;//公文领导签批时段
    private String fuFinishPrior;//发文处室办理时段
    private String flFinishPrior;//发文领导签批时段
    private String finishPrior;//办结时段
    private String turnTime;//转办时间
    private String feedBackTime;//转征询时间
    public TgwReportBean1(){
        
    }
    public TgwReportBean1(String title,String remarks, String units, String receiveTime,
            String sendTime, String guFinishPrior, String fixTime, String returnPrior,
            String sendCode, String glFinishPrior, String fuFinishPrior,
            String flFinishPrior, String finishPrior, String turnTime,
            String feedBackTime) {
        super();
        this.title = title;
        this.remarks = remarks;
        this.units = units;
        this.receiveTime = receiveTime;
        this.sendTime = sendTime;
        this.guFinishPrior = guFinishPrior;
        this.fixTime = fixTime;
        this.returnPrior = returnPrior;
        this.sendCode = sendCode;
        this.glFinishPrior = glFinishPrior;
        this.fuFinishPrior = fuFinishPrior;
        this.flFinishPrior = flFinishPrior;
        this.finishPrior = finishPrior;
        this.turnTime = turnTime;
        this.feedBackTime = feedBackTime;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUnits() {
        return units;
    }
    public void setUnits(String units) {
        this.units = units;
    }
    public String getReceiveTime() {
        return receiveTime;
    }
    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }
    public String getSendTime() {
        return sendTime;
    }
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
    public String getGuFinishPrior() {
        return guFinishPrior;
    }
    public void setGuFinishPrior(String guFinishPrior) {
        this.guFinishPrior = guFinishPrior;
    }
    public String getFixTime() {
        return fixTime;
    }
    public void setFixTime(String fixTime) {
        this.fixTime = fixTime;
    }
    public String getReturnPrior() {
        return returnPrior;
    }
    public void setReturnPrior(String returnPrior) {
        this.returnPrior = returnPrior;
    }
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    public String getGlFinishPrior() {
        return glFinishPrior;
    }
    public void setGlFinishPrior(String glFinishPrior) {
        this.glFinishPrior = glFinishPrior;
    }
    public String getFuFinishPrior() {
        return fuFinishPrior;
    }
    public void setFuFinishPrior(String fuFinishPrior) {
        this.fuFinishPrior = fuFinishPrior;
    }
    public String getFlFinishPrior() {
        return flFinishPrior;
    }
    public void setFlFinishPrior(String flFinishPrior) {
        this.flFinishPrior = flFinishPrior;
    }
    public String getFinishPrior() {
        return finishPrior;
    }
    public void setFinishPrior(String finishPrior) {
        this.finishPrior = finishPrior;
    }
    public String getTurnTime() {
        return turnTime;
    }
    public void setTurnTime(String turnTime) {
        this.turnTime = turnTime;
    }
    public String getFeedBackTime() {
        return feedBackTime;
    }
    public void setFeedBackTime(String feedBackTime) {
        this.feedBackTime = feedBackTime;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
}