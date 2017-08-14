package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_PERSONNEL_PERSON"
 *     
*/
public class ToaPersonnelPerson implements Serializable {

    /** identifier field */
    private String personId;

    /** nullable persistent field */
    private String personName;

    /** nullable persistent field */
    private Object personPhoto;

    /** nullable persistent field */
    private String personSax;

    /** nullable persistent field */
    private String personLabourno;

    /** nullable persistent field */
    private Date personBorn;

    /** nullable persistent field */
    private String personNativeplace;

    /** nullable persistent field */
    private String personBornplace;

    /** nullable persistent field */
    private String personNation;

    /** nullable persistent field */
    private String personHealthState;

    /** nullable persistent field */
    private String personMarriageState;

    /** nullable persistent field */
    private String personStatus;

    /** nullable persistent field */
    private Date personEntryCompanyTime;

    /** nullable persistent field */
    private Date personStatusTime;

    /** nullable persistent field */
    private String personWorkState;

    /** nullable persistent field */
    private String personEmploymentKind;

    /** nullable persistent field */
    private String personRegisteredPlace;

    /** nullable persistent field */
    private String personRegisteredCharacter;

    /** nullable persistent field */
    private String personCardId;

    /** nullable persistent field */
    private String personPersonKind;

    /** nullable persistent field */
    private String personPset;

    /** nullable persistent field */
    private Date personWorkTime;

    /** nullable persistent field */
    private String personManCharacter;

    /** nullable persistent field */
    private String personOperater;

    /** nullable persistent field */
    private Date personOperateDate;

    /** persistent field */
    private com.strongit.oa.bo.ToaPersonnelCompany toaPersonnelCompany;

    /** full constructor */
    public ToaPersonnelPerson(String personId, String personName, Object personPhoto, String personSax, String personLabourno, Date personBorn, String personNativeplace, String personBornplace, String personNation, String personHealthState, String personMarriageState, String personStatus, Date personEntryCompanyTime, Date personStatusTime, String personWorkState, String personEmploymentKind, String personRegisteredPlace, String personRegisteredCharacter, String personCardId, String personPersonKind, String personPset, Date personWorkTime, String personManCharacter, String personOperater, Date personOperateDate, com.strongit.oa.bo.ToaPersonnelCompany toaPersonnelCompany) {
        this.personId = personId;
        this.personName = personName;
        this.personPhoto = personPhoto;
        this.personSax = personSax;
        this.personLabourno = personLabourno;
        this.personBorn = personBorn;
        this.personNativeplace = personNativeplace;
        this.personBornplace = personBornplace;
        this.personNation = personNation;
        this.personHealthState = personHealthState;
        this.personMarriageState = personMarriageState;
        this.personStatus = personStatus;
        this.personEntryCompanyTime = personEntryCompanyTime;
        this.personStatusTime = personStatusTime;
        this.personWorkState = personWorkState;
        this.personEmploymentKind = personEmploymentKind;
        this.personRegisteredPlace = personRegisteredPlace;
        this.personRegisteredCharacter = personRegisteredCharacter;
        this.personCardId = personCardId;
        this.personPersonKind = personPersonKind;
        this.personPset = personPset;
        this.personWorkTime = personWorkTime;
        this.personManCharacter = personManCharacter;
        this.personOperater = personOperater;
        this.personOperateDate = personOperateDate;
        this.toaPersonnelCompany = toaPersonnelCompany;
    }

    /** default constructor */
    public ToaPersonnelPerson() {
    }

    /** minimal constructor */
    public ToaPersonnelPerson(String personId, com.strongit.oa.bo.ToaPersonnelCompany toaPersonnelCompany) {
        this.personId = personId;
        this.toaPersonnelCompany = toaPersonnelCompany;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="PERSON_ID"
     *         
     */
    public String getPersonId() {
        return this.personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_NAME"
     *             length="20"
     *         
     */
    public String getPersonName() {
        return this.personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_PHOTO"
     *             length="4000"
     *         
     */
    public Object getPersonPhoto() {
        return this.personPhoto;
    }

    public void setPersonPhoto(Object personPhoto) {
        this.personPhoto = personPhoto;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_SAX"
     *             length="20"
     *         
     */
    public String getPersonSax() {
        return this.personSax;
    }

    public void setPersonSax(String personSax) {
        this.personSax = personSax;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_LABOURNO"
     *             length="10"
     *         
     */
    public String getPersonLabourno() {
        return this.personLabourno;
    }

    public void setPersonLabourno(String personLabourno) {
        this.personLabourno = personLabourno;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_BORN"
     *             length="7"
     *         
     */
    public Date getPersonBorn() {
        return this.personBorn;
    }

    public void setPersonBorn(Date personBorn) {
        this.personBorn = personBorn;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_NATIVEPLACE"
     *             length="100"
     *         
     */
    public String getPersonNativeplace() {
        return this.personNativeplace;
    }

    public void setPersonNativeplace(String personNativeplace) {
        this.personNativeplace = personNativeplace;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_BORNPLACE"
     *             length="100"
     *         
     */
    public String getPersonBornplace() {
        return this.personBornplace;
    }

    public void setPersonBornplace(String personBornplace) {
        this.personBornplace = personBornplace;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_NATION"
     *             length="20"
     *         
     */
    public String getPersonNation() {
        return this.personNation;
    }

    public void setPersonNation(String personNation) {
        this.personNation = personNation;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_HEALTH_STATE"
     *             length="20"
     *         
     */
    public String getPersonHealthState() {
        return this.personHealthState;
    }

    public void setPersonHealthState(String personHealthState) {
        this.personHealthState = personHealthState;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_MARRIAGE_STATE"
     *             length="20"
     *         
     */
    public String getPersonMarriageState() {
        return this.personMarriageState;
    }

    public void setPersonMarriageState(String personMarriageState) {
        this.personMarriageState = personMarriageState;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_STATUS"
     *             length="20"
     *         
     */
    public String getPersonStatus() {
        return this.personStatus;
    }

    public void setPersonStatus(String personStatus) {
        this.personStatus = personStatus;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_ENTRY_COMPANY_TIME"
     *             length="7"
     *         
     */
    public Date getPersonEntryCompanyTime() {
        return this.personEntryCompanyTime;
    }

    public void setPersonEntryCompanyTime(Date personEntryCompanyTime) {
        this.personEntryCompanyTime = personEntryCompanyTime;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_STATUS_TIME"
     *             length="7"
     *         
     */
    public Date getPersonStatusTime() {
        return this.personStatusTime;
    }

    public void setPersonStatusTime(Date personStatusTime) {
        this.personStatusTime = personStatusTime;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_WORK_STATE"
     *             length="20"
     *         
     */
    public String getPersonWorkState() {
        return this.personWorkState;
    }

    public void setPersonWorkState(String personWorkState) {
        this.personWorkState = personWorkState;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_EMPLOYMENT_KIND"
     *             length="20"
     *         
     */
    public String getPersonEmploymentKind() {
        return this.personEmploymentKind;
    }

    public void setPersonEmploymentKind(String personEmploymentKind) {
        this.personEmploymentKind = personEmploymentKind;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_REGISTERED_PLACE"
     *             length="100"
     *         
     */
    public String getPersonRegisteredPlace() {
        return this.personRegisteredPlace;
    }

    public void setPersonRegisteredPlace(String personRegisteredPlace) {
        this.personRegisteredPlace = personRegisteredPlace;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_REGISTERED_CHARACTER"
     *             length="20"
     *         
     */
    public String getPersonRegisteredCharacter() {
        return this.personRegisteredCharacter;
    }

    public void setPersonRegisteredCharacter(String personRegisteredCharacter) {
        this.personRegisteredCharacter = personRegisteredCharacter;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_CARD_ID"
     *             length="18"
     *         
     */
    public String getPersonCardId() {
        return this.personCardId;
    }

    public void setPersonCardId(String personCardId) {
        this.personCardId = personCardId;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_PERSON_KIND"
     *             length="20"
     *         
     */
    public String getPersonPersonKind() {
        return this.personPersonKind;
    }

    public void setPersonPersonKind(String personPersonKind) {
        this.personPersonKind = personPersonKind;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_PSET"
     *             length="50"
     *         
     */
    public String getPersonPset() {
        return this.personPset;
    }

    public void setPersonPset(String personPset) {
        this.personPset = personPset;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_WORK_TIME"
     *             length="7"
     *         
     */
    public Date getPersonWorkTime() {
        return this.personWorkTime;
    }

    public void setPersonWorkTime(Date personWorkTime) {
        this.personWorkTime = personWorkTime;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_MAN_CHARACTER"
     *             length="20"
     *         
     */
    public String getPersonManCharacter() {
        return this.personManCharacter;
    }

    public void setPersonManCharacter(String personManCharacter) {
        this.personManCharacter = personManCharacter;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_OPERATER"
     *             length="20"
     *         
     */
    public String getPersonOperater() {
        return this.personOperater;
    }

    public void setPersonOperater(String personOperater) {
        this.personOperater = personOperater;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_OPERATE_DATE"
     *             length="7"
     *         
     */
    public Date getPersonOperateDate() {
        return this.personOperateDate;
    }

    public void setPersonOperateDate(Date personOperateDate) {
        this.personOperateDate = personOperateDate;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="COMPANY_ID"         
     *         
     */
    public com.strongit.oa.bo.ToaPersonnelCompany getToaPersonnelCompany() {
        return this.toaPersonnelCompany;
    }

    public void setToaPersonnelCompany(com.strongit.oa.bo.ToaPersonnelCompany toaPersonnelCompany) {
        this.toaPersonnelCompany = toaPersonnelCompany;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("personId", getPersonId())
            .toString();
    }

}
