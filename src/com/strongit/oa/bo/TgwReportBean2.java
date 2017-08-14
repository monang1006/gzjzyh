/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 6/28/2013 1:21 PM
 * Autour: xush
 * Version: V1.0
 * Description： 公文统计报表的编号子报表
 */
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

public class TgwReportBean2 implements Serializable{
   //获取编号的数目
    private int number;
   //不带参数的构造方法
    public TgwReportBean2(){
        
    }
    //带参数的构造方法
    public TgwReportBean2(int number) {
        super();
        this.number = number;
    }
   //成员变量的get和set方法
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    
}