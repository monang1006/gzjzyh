package com.strongit.oa.bo;

import java.io.Serializable;
public class ToaUnitsBean implements Serializable{
    private String title;
    private String returnReason;
    public ToaUnitsBean(){}
    public ToaUnitsBean(String title, String returnReason) {
        super();
        this.title = title;
        this.returnReason = returnReason;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
   
    public String getReturnReason() {
        return returnReason;
    }
    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }
    
	
}