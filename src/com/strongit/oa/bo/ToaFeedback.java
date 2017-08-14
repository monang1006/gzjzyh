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
@Table(name="T_OA_FEEDBACK")
public class ToaFeedback implements Serializable{
    @Id
    @GeneratedValue
    private String id;
    private String unitName;//部门名称
    private String bussinessId;//业务id
    private String title;//来文标题
    private Date feedBackStartTime;//反馈意见开始时间
    private Date feedBackBackTime;//反馈意见返回时间
    private int feedBackFixPrior;//反馈意见规定定时限
    private String onTimeFlg;//按时标志符
    private int actualPrior;//实际平均用时
    private int laterPrior;//实际超时平均用时
    
   public ToaFeedback(String id, String unitName, String bussinessId,
            String title, Date feedBackStartTime, Date feedBackBackTime,
            int feedBackFixPrior, String onTimeFlg,
            int actualPrior, int laterPrior) {
        super();
        this.id = id;
        this.unitName = unitName;
        this.bussinessId = bussinessId;
        this.title = title;
        this.feedBackStartTime = feedBackStartTime;
        this.feedBackBackTime = feedBackBackTime;
        this.feedBackFixPrior = feedBackFixPrior;
        this.onTimeFlg = onTimeFlg;
        this.actualPrior = actualPrior;
        this.laterPrior = laterPrior;
    }
public ToaFeedback() {
        
       }
    @Column(name = "TITLE", nullable = true)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    @Column(name = "UNITS_NAME", nullable = true)
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    @Column(name = "BUSSINESS_ID", nullable = true)
    public String getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(String bussinessId) {
        this.bussinessId = bussinessId;
    }
    @Column(name = "FEEDBACK_START_TIME", nullable = true)
    public Date getFeedBackStartTime() {
        return feedBackStartTime;
    }

    public void setFeedBackStartTime(Date feedBackStartTime) {
        this.feedBackStartTime = feedBackStartTime;
    }
    @Column(name = "FEEDBACK_FIX_PRIOR", nullable = true)
    public int getFeedBackFixPrior() {
        return feedBackFixPrior;
    }

    public void setFeedBackFixPrior(int feedBackFixPrior) {
        this.feedBackFixPrior = feedBackFixPrior;
    }
    @Column(name = "FEEDBACK_BACK_TIME", nullable = true)
    public Date getFeedBackBackTime() {
        return feedBackBackTime;
    }

    public void setFeedBackBackTime(Date feedBackBackTime) {
        this.feedBackBackTime = feedBackBackTime;
    }
    @Column(name = "ON_TIME_FLG", nullable = true)
    public String getOnTimeFlg() {
        return onTimeFlg;
    }
    public void setOnTimeFlg(String onTimeFlg) {
        this.onTimeFlg = onTimeFlg;
    }
    
    @Column(name = "ACTUAL_PRIOR", nullable = true)
    public int getActualPrior() {
        return actualPrior;
    }
    public void setActualPrior(int actualPrior) {
        this.actualPrior = actualPrior;
    }
    @Column(name = "LATER_PRIOR", nullable = true)
    public int getLaterPrior() {
        return laterPrior;
    }
    public void setLaterPrior(int laterPrior) {
        this.laterPrior = laterPrior;
    }
    

}