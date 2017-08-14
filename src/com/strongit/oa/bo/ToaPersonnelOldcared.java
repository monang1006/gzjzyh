package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_PERSONNEL_OLDCARED"
 *     
*/
public class ToaPersonnelOldcared implements Serializable {

    /** identifier field */
    private String oldcaredId;

    /** nullable persistent field */
    private String oldcaredName;

    /** nullable persistent field */
    private String oldcaredSex;

    /** nullable persistent field */
    private Date oldcaredBorn;

    /** nullable persistent field */
    private Object oldcaredPhoto;

    /** nullable persistent field */
    private String oldcaredNativeplace;

    /** nullable persistent field */
    private String oldcaredBornplace;

    /** nullable persistent field */
    private String oldcaredNation;

    /** nullable persistent field */
    private String oldcaredHealthState;

    /** nullable persistent field */
    private String oldcaredMarrState;

    /** nullable persistent field */
    private String oldcaredRegistPlace;

    /** nullable persistent field */
    private String oldcaredRegistType;

    /** nullable persistent field */
    private String oldcaredCardId;

    /** nullable persistent field */
    private String oldcaredOperater;

    /** nullable persistent field */
    private Date oldcaredOperateDate;

    /** persistent field */
    private Set toaPersonnelCondoles;

    /** full constructor */
    public ToaPersonnelOldcared(String oldcaredId, String oldcaredName, String oldcaredSex, Date oldcaredBorn, Object oldcaredPhoto, String oldcaredNativeplace, String oldcaredBornplace, String oldcaredNation, String oldcaredHealthState, String oldcaredMarrState, String oldcaredRegistPlace, String oldcaredRegistType, String oldcaredCardId, String oldcaredOperater, Date oldcaredOperateDate, Set toaPersonnelCondoles) {
        this.oldcaredId = oldcaredId;
        this.oldcaredName = oldcaredName;
        this.oldcaredSex = oldcaredSex;
        this.oldcaredBorn = oldcaredBorn;
        this.oldcaredPhoto = oldcaredPhoto;
        this.oldcaredNativeplace = oldcaredNativeplace;
        this.oldcaredBornplace = oldcaredBornplace;
        this.oldcaredNation = oldcaredNation;
        this.oldcaredHealthState = oldcaredHealthState;
        this.oldcaredMarrState = oldcaredMarrState;
        this.oldcaredRegistPlace = oldcaredRegistPlace;
        this.oldcaredRegistType = oldcaredRegistType;
        this.oldcaredCardId = oldcaredCardId;
        this.oldcaredOperater = oldcaredOperater;
        this.oldcaredOperateDate = oldcaredOperateDate;
        this.toaPersonnelCondoles = toaPersonnelCondoles;
    }

    /** default constructor */
    public ToaPersonnelOldcared() {
    }

    /** minimal constructor */
    public ToaPersonnelOldcared(String oldcaredId, Set toaPersonnelCondoles) {
        this.oldcaredId = oldcaredId;
        this.toaPersonnelCondoles = toaPersonnelCondoles;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="OLDCARED_ID"
     *         
     */
    public String getOldcaredId() {
        return this.oldcaredId;
    }

    public void setOldcaredId(String oldcaredId) {
        this.oldcaredId = oldcaredId;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_NAME"
     *             length="20"
     *         
     */
    public String getOldcaredName() {
        return this.oldcaredName;
    }

    public void setOldcaredName(String oldcaredName) {
        this.oldcaredName = oldcaredName;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_SEX"
     *             length="20"
     *         
     */
    public String getOldcaredSex() {
        return this.oldcaredSex;
    }

    public void setOldcaredSex(String oldcaredSex) {
        this.oldcaredSex = oldcaredSex;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_BORN"
     *             length="7"
     *         
     */
    public Date getOldcaredBorn() {
        return this.oldcaredBorn;
    }

    public void setOldcaredBorn(Date oldcaredBorn) {
        this.oldcaredBorn = oldcaredBorn;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_PHOTO"
     *             length="4000"
     *         
     */
    public Object getOldcaredPhoto() {
        return this.oldcaredPhoto;
    }

    public void setOldcaredPhoto(Object oldcaredPhoto) {
        this.oldcaredPhoto = oldcaredPhoto;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_NATIVEPLACE"
     *             length="100"
     *         
     */
    public String getOldcaredNativeplace() {
        return this.oldcaredNativeplace;
    }

    public void setOldcaredNativeplace(String oldcaredNativeplace) {
        this.oldcaredNativeplace = oldcaredNativeplace;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_BORNPLACE"
     *             length="100"
     *         
     */
    public String getOldcaredBornplace() {
        return this.oldcaredBornplace;
    }

    public void setOldcaredBornplace(String oldcaredBornplace) {
        this.oldcaredBornplace = oldcaredBornplace;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_NATION"
     *             length="20"
     *         
     */
    public String getOldcaredNation() {
        return this.oldcaredNation;
    }

    public void setOldcaredNation(String oldcaredNation) {
        this.oldcaredNation = oldcaredNation;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_HEALTH_STATE"
     *             length="20"
     *         
     */
    public String getOldcaredHealthState() {
        return this.oldcaredHealthState;
    }

    public void setOldcaredHealthState(String oldcaredHealthState) {
        this.oldcaredHealthState = oldcaredHealthState;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_MARR_STATE"
     *             length="20"
     *         
     */
    public String getOldcaredMarrState() {
        return this.oldcaredMarrState;
    }

    public void setOldcaredMarrState(String oldcaredMarrState) {
        this.oldcaredMarrState = oldcaredMarrState;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_REGIST_PLACE"
     *             length="100"
     *         
     */
    public String getOldcaredRegistPlace() {
        return this.oldcaredRegistPlace;
    }

    public void setOldcaredRegistPlace(String oldcaredRegistPlace) {
        this.oldcaredRegistPlace = oldcaredRegistPlace;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_REGIST_TYPE"
     *             length="20"
     *         
     */
    public String getOldcaredRegistType() {
        return this.oldcaredRegistType;
    }

    public void setOldcaredRegistType(String oldcaredRegistType) {
        this.oldcaredRegistType = oldcaredRegistType;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_CARD_ID"
     *             length="18"
     *         
     */
    public String getOldcaredCardId() {
        return this.oldcaredCardId;
    }

    public void setOldcaredCardId(String oldcaredCardId) {
        this.oldcaredCardId = oldcaredCardId;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_OPERATER"
     *             length="20"
     *         
     */
    public String getOldcaredOperater() {
        return this.oldcaredOperater;
    }

    public void setOldcaredOperater(String oldcaredOperater) {
        this.oldcaredOperater = oldcaredOperater;
    }

    /** 
     *            @hibernate.property
     *             column="OLDCARED_OPERATE_DATE"
     *             length="7"
     *         
     */
    public Date getOldcaredOperateDate() {
        return this.oldcaredOperateDate;
    }

    public void setOldcaredOperateDate(Date oldcaredOperateDate) {
        this.oldcaredOperateDate = oldcaredOperateDate;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="OLDCARED_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaPersonnelCondole"
     *         
     */
    public Set getToaPersonnelCondoles() {
        return this.toaPersonnelCondoles;
    }

    public void setToaPersonnelCondoles(Set toaPersonnelCondoles) {
        this.toaPersonnelCondoles = toaPersonnelCondoles;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("oldcaredId", getOldcaredId())
            .toString();
    }

}
