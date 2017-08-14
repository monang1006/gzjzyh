package com.strongit.oa.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_PERSONNEL_COMPANY"
 *     
*/
public class ToaPersonnelCompany implements Serializable {

    /** identifier field */
    private String companyId;

    /** persistent field */
    private String companyCode;

    /** nullable persistent field */
    private String companyName;

    /** nullable persistent field */
    private String companyQueryCode;

    /** nullable persistent field */
    private String companyTel;

    /** nullable persistent field */
    private String companyTax;

    /** nullable persistent field */
    private String companySubjectionRelation;

    /** nullable persistent field */
    private String companyLevel;

    /** nullable persistent field */
    private String companyCharacter;

    /** nullable persistent field */
    private String companyEconomyKind;

    /** nullable persistent field */
    private String companyKind;

    /** nullable persistent field */
    private String companyAppertainRelation;

    /** nullable persistent field */
    private String companyChargeDept;

    /** nullable persistent field */
    private String companyCountry;

    /** nullable persistent field */
    private Date companyBuildTime;

    /** nullable persistent field */
    private String companyPlace;

    /** nullable persistent field */
    private String companyCalling;

    /** nullable persistent field */
    private String companySort;

    /** nullable persistent field */
    private BigDecimal companyWeave;

    /** nullable persistent field */
    private String companyDuty;

    /** nullable persistent field */
    private String companyParentCode;

    /** nullable persistent field */
    private Date companyDate;

    /** nullable persistent field */
    private String companyOperater;

    /** persistent field */
    private Set toaPersonnelPersons;

    /** full constructor */
    public ToaPersonnelCompany(String companyId, String companyCode, String companyName, String companyQueryCode, String companyTel, String companyTax, String companySubjectionRelation, String companyLevel, String companyCharacter, String companyEconomyKind, String companyKind, String companyAppertainRelation, String companyChargeDept, String companyCountry, Date companyBuildTime, String companyPlace, String companyCalling, String companySort, BigDecimal companyWeave, String companyDuty, String companyParentCode, Date companyDate, String companyOperater, Set toaPersonnelPersons) {
        this.companyId = companyId;
        this.companyCode = companyCode;
        this.companyName = companyName;
        this.companyQueryCode = companyQueryCode;
        this.companyTel = companyTel;
        this.companyTax = companyTax;
        this.companySubjectionRelation = companySubjectionRelation;
        this.companyLevel = companyLevel;
        this.companyCharacter = companyCharacter;
        this.companyEconomyKind = companyEconomyKind;
        this.companyKind = companyKind;
        this.companyAppertainRelation = companyAppertainRelation;
        this.companyChargeDept = companyChargeDept;
        this.companyCountry = companyCountry;
        this.companyBuildTime = companyBuildTime;
        this.companyPlace = companyPlace;
        this.companyCalling = companyCalling;
        this.companySort = companySort;
        this.companyWeave = companyWeave;
        this.companyDuty = companyDuty;
        this.companyParentCode = companyParentCode;
        this.companyDate = companyDate;
        this.companyOperater = companyOperater;
        this.toaPersonnelPersons = toaPersonnelPersons;
    }

    /** default constructor */
    public ToaPersonnelCompany() {
    }

    /** minimal constructor */
    public ToaPersonnelCompany(String companyId, String companyCode, Set toaPersonnelPersons) {
        this.companyId = companyId;
        this.companyCode = companyCode;
        this.toaPersonnelPersons = toaPersonnelPersons;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="COMPANY_ID"
     *         
     */
    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_CODE"
     *             length="20"
     *             not-null="true"
     *         
     */
    public String getCompanyCode() {
        return this.companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_NAME"
     *             length="50"
     *         
     */
    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_QUERY_CODE"
     *             length="10"
     *         
     */
    public String getCompanyQueryCode() {
        return this.companyQueryCode;
    }

    public void setCompanyQueryCode(String companyQueryCode) {
        this.companyQueryCode = companyQueryCode;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_TEL"
     *             length="16"
     *         
     */
    public String getCompanyTel() {
        return this.companyTel;
    }

    public void setCompanyTel(String companyTel) {
        this.companyTel = companyTel;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_TAX"
     *             length="16"
     *         
     */
    public String getCompanyTax() {
        return this.companyTax;
    }

    public void setCompanyTax(String companyTax) {
        this.companyTax = companyTax;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_SUBJECTION_RELATION"
     *             length="10"
     *         
     */
    public String getCompanySubjectionRelation() {
        return this.companySubjectionRelation;
    }

    public void setCompanySubjectionRelation(String companySubjectionRelation) {
        this.companySubjectionRelation = companySubjectionRelation;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_LEVEL"
     *             length="10"
     *         
     */
    public String getCompanyLevel() {
        return this.companyLevel;
    }

    public void setCompanyLevel(String companyLevel) {
        this.companyLevel = companyLevel;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_CHARACTER"
     *             length="10"
     *         
     */
    public String getCompanyCharacter() {
        return this.companyCharacter;
    }

    public void setCompanyCharacter(String companyCharacter) {
        this.companyCharacter = companyCharacter;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_ECONOMY_KIND"
     *             length="10"
     *         
     */
    public String getCompanyEconomyKind() {
        return this.companyEconomyKind;
    }

    public void setCompanyEconomyKind(String companyEconomyKind) {
        this.companyEconomyKind = companyEconomyKind;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_KIND"
     *             length="30"
     *         
     */
    public String getCompanyKind() {
        return this.companyKind;
    }

    public void setCompanyKind(String companyKind) {
        this.companyKind = companyKind;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_APPERTAIN_RELATION"
     *             length="10"
     *         
     */
    public String getCompanyAppertainRelation() {
        return this.companyAppertainRelation;
    }

    public void setCompanyAppertainRelation(String companyAppertainRelation) {
        this.companyAppertainRelation = companyAppertainRelation;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_CHARGE_DEPT"
     *             length="10"
     *         
     */
    public String getCompanyChargeDept() {
        return this.companyChargeDept;
    }

    public void setCompanyChargeDept(String companyChargeDept) {
        this.companyChargeDept = companyChargeDept;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_COUNTRY"
     *             length="10"
     *         
     */
    public String getCompanyCountry() {
        return this.companyCountry;
    }

    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_BUILD_TIME"
     *             length="7"
     *         
     */
    public Date getCompanyBuildTime() {
        return this.companyBuildTime;
    }

    public void setCompanyBuildTime(Date companyBuildTime) {
        this.companyBuildTime = companyBuildTime;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_PLACE"
     *             length="20"
     *         
     */
    public String getCompanyPlace() {
        return this.companyPlace;
    }

    public void setCompanyPlace(String companyPlace) {
        this.companyPlace = companyPlace;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_CALLING"
     *             length="10"
     *         
     */
    public String getCompanyCalling() {
        return this.companyCalling;
    }

    public void setCompanyCalling(String companyCalling) {
        this.companyCalling = companyCalling;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_SORT"
     *             length="20"
     *         
     */
    public String getCompanySort() {
        return this.companySort;
    }

    public void setCompanySort(String companySort) {
        this.companySort = companySort;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_WEAVE"
     *             length="22"
     *         
     */
    public BigDecimal getCompanyWeave() {
        return this.companyWeave;
    }

    public void setCompanyWeave(BigDecimal companyWeave) {
        this.companyWeave = companyWeave;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_DUTY"
     *             length="20"
     *         
     */
    public String getCompanyDuty() {
        return this.companyDuty;
    }

    public void setCompanyDuty(String companyDuty) {
        this.companyDuty = companyDuty;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_PARENT_CODE"
     *             length="32"
     *         
     */
    public String getCompanyParentCode() {
        return this.companyParentCode;
    }

    public void setCompanyParentCode(String companyParentCode) {
        this.companyParentCode = companyParentCode;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_DATE"
     *             length="7"
     *         
     */
    public Date getCompanyDate() {
        return this.companyDate;
    }

    public void setCompanyDate(Date companyDate) {
        this.companyDate = companyDate;
    }

    /** 
     *            @hibernate.property
     *             column="COMPANY_OPERATER"
     *             length="20"
     *         
     */
    public String getCompanyOperater() {
        return this.companyOperater;
    }

    public void setCompanyOperater(String companyOperater) {
        this.companyOperater = companyOperater;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="COMPANY_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaPersonnelPerson"
     *         
     */
    public Set getToaPersonnelPersons() {
        return this.toaPersonnelPersons;
    }

    public void setToaPersonnelPersons(Set toaPersonnelPersons) {
        this.toaPersonnelPersons = toaPersonnelPersons;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("companyId", getCompanyId())
            .toString();
    }

}
