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



public class TgwReportBean implements Serializable{
    
    private String department;//处室
    private List<TgwReportBean1> gwList;
    private List<TgwReportBean2> numberList;
    public TgwReportBean(String department, List<TgwReportBean1> unitsList,List<TgwReportBean2> numberList) {
        super();
        this.department = department;
        this.gwList = gwList;
        this.numberList=numberList;
    }
    public TgwReportBean(){
            
        }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public List<TgwReportBean1> getGwList() {
        return gwList;
    }
    public void setGwList(List<TgwReportBean1> gwList) {
        this.gwList = gwList;
    }
    public List<TgwReportBean2> getNumberList() {
        return numberList;
    }
    public void setNumberList(List<TgwReportBean2> numberList) {
        this.numberList = numberList;
    }
    
}