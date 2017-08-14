package com.strongit.oa.bo;


import java.math.BigDecimal;
import java.util.Date;

public class ToaFeedbackBean {
   
    private String unitName;//部门名称
    private String bussinessId;//业务id
    private String title;//来文标题
    private int total;//意见总数
    private Date feedBackStartTime;//反馈意见开始时间
    private Date feedBackBackTime;//反馈意见返回时间
    private int feedBackFixPrior;//反馈意见规定定时限
    private int onTimeTotal;//按时反馈意见数
    private int laterTotal;//未按时反馈意见数
    private BigDecimal fixRavPrior;//规定平均用时
    private BigDecimal actualRavPrior;//实际平均用时
    private BigDecimal laterRavPrior;//实际超时平均用时
    private BigDecimal onTimePercentage;//按时反馈百分比
    private BigDecimal laterPercentage;//未按时反馈百分比
    
   public ToaFeedbackBean(String unitName, String bussinessId, String title,
            int total, Date feedBackStartTime,Date feedBackBackTime, int feedBackFixPrior,
            int onTimeTotal, int laterTotal, BigDecimal fixRavPrior,
            BigDecimal actualRavPrior, BigDecimal laterRavPrior, BigDecimal onTimePercentage,
            BigDecimal laterPercentage) {
        super();
        this.unitName = unitName;
        this.bussinessId = bussinessId;
        this.title = title;
        this.total = total;
        this.feedBackStartTime = feedBackStartTime;
        this.feedBackBackTime = feedBackBackTime;
        this.feedBackFixPrior = feedBackFixPrior;
        this.onTimeTotal = onTimeTotal;
        this.laterTotal = laterTotal;
        this.fixRavPrior = fixRavPrior;
        this.actualRavPrior = actualRavPrior;
        this.laterRavPrior = laterRavPrior;
        this.onTimePercentage = onTimePercentage;
        this.laterPercentage = laterPercentage;
    }

   public ToaFeedbackBean() {
        
       }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    
    public String getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(String bussinessId) {
        this.bussinessId = bussinessId;
    }
    
    public Date getFeedBackStartTime() {
        return feedBackStartTime;
    }

    public void setFeedBackStartTime(Date feedBackStartTime) {
        this.feedBackStartTime = feedBackStartTime;
    }
    public Date getFeedBackBackTime() {
        return feedBackStartTime;
    }

    public void setFeedBackBackTime(Date feedBackBackTime) {
        this.feedBackBackTime = feedBackBackTime;
    }
    public int getFeedBackFixPrior() {
        return feedBackFixPrior;
    }

    public void setFeedBackFixPrior(int feedBackFixPrior) {
        this.feedBackFixPrior = feedBackFixPrior;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getOnTimeTotal() {
        return onTimeTotal;
    }
    public void setOnTimeTotal(int onTimeTotal) {
        this.onTimeTotal = onTimeTotal;
    }
    public int getLaterTotal() {
        return laterTotal;
    }
    public void setLaterTotal(int laterTotal) {
        this.laterTotal = laterTotal;
    }
    public BigDecimal getFixRavPrior() {
        return fixRavPrior;
    }
    public void setFixRavPrior(BigDecimal fixRavPrior) {
        this.fixRavPrior = fixRavPrior;
    }
    public BigDecimal getActualRavPrior() {
        return actualRavPrior;
    }
    public void setActualRavPrior(BigDecimal actualRavPrior) {
        this.actualRavPrior = actualRavPrior;
    }
    public BigDecimal getLaterRavPrior() {
        return laterRavPrior;
    }
    public void setLaterRavPrior(BigDecimal laterRavPrior) {
        this.laterRavPrior = laterRavPrior;
    }
    public BigDecimal getOnTimePercentage() {
        return onTimePercentage;
    }
    public void setOnTimePercentage(BigDecimal onTimePercentage) {
        this.onTimePercentage = onTimePercentage;
    }
    public BigDecimal getLaterPercentage() {
        return laterPercentage;
    }
    public void setLaterPercentage(BigDecimal laterPercentage) {
        this.laterPercentage = laterPercentage;
    }
    

}