package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_PERSONNEL_CONDOLE"
 *     
*/
public class ToaPersonnelCondole implements Serializable {

    /** identifier field */
    private String condoleId;

    /** nullable persistent field */
    private Date condoleTime;

    /** nullable persistent field */
    private String condoleContent;

    /** nullable persistent field */
    private String condoleLead;

    /** nullable persistent field */
    private String condoleDemo;

    /** persistent field */
    private com.strongit.oa.bo.ToaPersonnelOldcared toaPersonnelOldcared;

    /** full constructor */
    public ToaPersonnelCondole(String condoleId, Date condoleTime, String condoleContent, String condoleLead, String condoleDemo, com.strongit.oa.bo.ToaPersonnelOldcared toaPersonnelOldcared) {
        this.condoleId = condoleId;
        this.condoleTime = condoleTime;
        this.condoleContent = condoleContent;
        this.condoleLead = condoleLead;
        this.condoleDemo = condoleDemo;
        this.toaPersonnelOldcared = toaPersonnelOldcared;
    }

    /** default constructor */
    public ToaPersonnelCondole() {
    }

    /** minimal constructor */
    public ToaPersonnelCondole(String condoleId, com.strongit.oa.bo.ToaPersonnelOldcared toaPersonnelOldcared) {
        this.condoleId = condoleId;
        this.toaPersonnelOldcared = toaPersonnelOldcared;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="CONDOLE_ID"
     *         
     */
    public String getCondoleId() {
        return this.condoleId;
    }

    public void setCondoleId(String condoleId) {
        this.condoleId = condoleId;
    }

    /** 
     *            @hibernate.property
     *             column="CONDOLE_TIME"
     *             length="7"
     *         
     */
    public Date getCondoleTime() {
        return this.condoleTime;
    }

    public void setCondoleTime(Date condoleTime) {
        this.condoleTime = condoleTime;
    }

    /** 
     *            @hibernate.property
     *             column="CONDOLE_CONTENT"
     *             length="500"
     *         
     */
    public String getCondoleContent() {
        return this.condoleContent;
    }

    public void setCondoleContent(String condoleContent) {
        this.condoleContent = condoleContent;
    }

    /** 
     *            @hibernate.property
     *             column="CONDOLE_LEAD"
     *             length="500"
     *         
     */
    public String getCondoleLead() {
        return this.condoleLead;
    }

    public void setCondoleLead(String condoleLead) {
        this.condoleLead = condoleLead;
    }

    /** 
     *            @hibernate.property
     *             column="CONDOLE_DEMO"
     *             length="4000"
     *         
     */
    public String getCondoleDemo() {
        return this.condoleDemo;
    }

    public void setCondoleDemo(String condoleDemo) {
        this.condoleDemo = condoleDemo;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="OLDCARED_ID"         
     *         
     */
    public com.strongit.oa.bo.ToaPersonnelOldcared getToaPersonnelOldcared() {
        return this.toaPersonnelOldcared;
    }

    public void setToaPersonnelOldcared(com.strongit.oa.bo.ToaPersonnelOldcared toaPersonnelOldcared) {
        this.toaPersonnelOldcared = toaPersonnelOldcared;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("condoleId", getCondoleId())
            .toString();
    }

}
