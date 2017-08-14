package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_PERSON_DEPLOY"
 *     
*/
@Entity
@Table(name = "T_OA_PERSON_DEPLOY", catalog = "", schema = "")
public class ToaPersonDeploy implements Serializable {

    /** identifier field */
    private String pdepId;

    /** nullable persistent field */
    private String pdepName;

    /** nullable persistent field */
    private String pdepEditcode;

    /** nullable persistent field */
    private String pdepEditname;

    /** nullable persistent field */
    private String pdepNewcode;

    /** nullable persistent field */
    private String pdepNewname;

    /** nullable persistent field */
    private String pdepNewtable;

    /** nullable persistent field */
    private String pdepNewtabname;

    /** nullable persistent field */
    private String pdepNewtabrow;

    /** nullable persistent field */
    private String pdepNewtabrowname;

    /** nullable persistent field */
    private String pdepPersontype;

    /** nullable persistent field */
    private String pdepIsveteran;

    /** nullable persistent field */
    private String pdepIsactiv;

    /** full constructor */
    public ToaPersonDeploy(String pdepId, String pdepName, String pdepEditcode, String pdepEditname, String pdepNewcode, String pdepNewname, String pdepNewtable, String pdepNewtabname, String pdepNewtabrow, String pdepNewtabrowname, String pdepPersontype, String pdepIsveteran, String pdepIsactiv) {
        this.pdepId = pdepId;
        this.pdepName = pdepName;
        this.pdepEditcode = pdepEditcode;
        this.pdepEditname = pdepEditname;
        this.pdepNewcode = pdepNewcode;
        this.pdepNewname = pdepNewname;
        this.pdepNewtable = pdepNewtable;
        this.pdepNewtabname = pdepNewtabname;
        this.pdepNewtabrow = pdepNewtabrow;
        this.pdepNewtabrowname = pdepNewtabrowname;
        this.pdepPersontype = pdepPersontype;
        this.pdepIsveteran = pdepIsveteran;
        this.pdepIsactiv = pdepIsactiv;
    }

    /** default constructor */
    public ToaPersonDeploy() {
    }

    /** minimal constructor */
    public ToaPersonDeploy(String pdepId) {
        this.pdepId = pdepId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="PDEP_ID"
     *         
     */
	@Id
	@Column(name = "PDEP_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getPdepId() {
        return this.pdepId;
    }

    public void setPdepId(String pdepId) {
        this.pdepId = pdepId;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_NAME"
     *             length="32"
     *         
     */
    @Column(name = "PDEP_NAME")
    public String getPdepName() {
        return this.pdepName;
    }

    public void setPdepName(String pdepName) {
        this.pdepName = pdepName;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_EDITCODE"
     *             length="32"
     *         
     */
    @Column(name = "PDEP_EDITCODE")
    public String getPdepEditcode() {
        return this.pdepEditcode;
    }

    public void setPdepEditcode(String pdepEditcode) {
        this.pdepEditcode = pdepEditcode;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_EDITNAME"
     *             length="32"
     *         
     */
    @Column(name = "PDEP_EDITNAME")
    public String getPdepEditname() {
        return this.pdepEditname;
    }

    public void setPdepEditname(String pdepEditname) {
        this.pdepEditname = pdepEditname;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_NEWCODE"
     *             length="32"
     *         
     */
    @Column(name = "PDEP_NEWCODE")
    public String getPdepNewcode() {
        return this.pdepNewcode;
    }

    public void setPdepNewcode(String pdepNewcode) {
        this.pdepNewcode = pdepNewcode;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_NEWNAME"
     *             length="32"
     *         
     */
    @Column(name = "PDEP_NEWNAME")
    public String getPdepNewname() {
        return this.pdepNewname;
    }

    public void setPdepNewname(String pdepNewname) {
        this.pdepNewname = pdepNewname;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_NEWTABLE"
     *             length="32"
     *         
     */
    @Column(name = "PDEP_NEWTABLE")
    public String getPdepNewtable() {
        return this.pdepNewtable;
    }

    public void setPdepNewtable(String pdepNewtable) {
        this.pdepNewtable = pdepNewtable;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_NEWTABNAME"
     *             length="32"
     *         
     */
    @Column(name = "PDEP_NEWTABNAME")
    public String getPdepNewtabname() {
        return this.pdepNewtabname;
    }

    public void setPdepNewtabname(String pdepNewtabname) {
        this.pdepNewtabname = pdepNewtabname;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_NEWTABROW"
     *             length="32"
     *         
     */
    @Column(name = "PDEP_NEWTABROW")
    public String getPdepNewtabrow() {
        return this.pdepNewtabrow;
    }

    public void setPdepNewtabrow(String pdepNewtabrow) {
        this.pdepNewtabrow = pdepNewtabrow;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_NEWTABROWNAME"
     *             length="32"
     *         
     */
    @Column(name = "PDEP_NEWTABROWNAME")
    public String getPdepNewtabrowname() {
        return this.pdepNewtabrowname;
    }

    public void setPdepNewtabrowname(String pdepNewtabrowname) {
        this.pdepNewtabrowname = pdepNewtabrowname;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_PERSONTYPE"
     *             length="32"
     *         
     */
    @Column(name = "PDEP_PERSONTYPE")
    public String getPdepPersontype() {
        return this.pdepPersontype;
    }

    public void setPdepPersontype(String pdepPersontype) {
        this.pdepPersontype = pdepPersontype;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_ISVETERAN"
     *             length="12"
     *         
     */
    @Column(name = "PDEP_ISVETERAN")
    public String getPdepIsveteran() {
        return this.pdepIsveteran;
    }

    public void setPdepIsveteran(String pdepIsveteran) {
        this.pdepIsveteran = pdepIsveteran;
    }

    /** 
     *            @hibernate.property
     *             column="PDEP_ISACTIV"
     *             length="12"
     *         
     */
    @Column(name = "PDEP_ISACTIV")
    public String getPdepIsactiv() {
        return this.pdepIsactiv;
    }

    public void setPdepIsactiv(String pdepIsactiv) {
        this.pdepIsactiv = pdepIsactiv;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("pdepId", getPdepId())
            .toString();
    }

}
