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
 *         table="T_OA_DOCNUMBER_WORD"
 *     
*/
@Entity
@Table(name="T_OA_DOCNUMBER_WORD")
public class ToaDocnumberWord implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String docnumberWordid;

    /** nullable persistent field */
    private String docnumberWordname;

    /** nullable persistent field */
    private String docnumberWordremaek;

    /** full constructor */
    public ToaDocnumberWord(String docnumberWordid, String docnumberWordname, String docnumberWordremaek) {
        this.docnumberWordid = docnumberWordid;
        this.docnumberWordname = docnumberWordname;
        this.docnumberWordremaek = docnumberWordremaek;
    }

    /** default constructor */
    public ToaDocnumberWord() {
    }

    /** minimal constructor */
    public ToaDocnumberWord(String docnumberWordid) {
        this.docnumberWordid = docnumberWordid;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DOCNUMBER_WORDID"
     *         
     */
    @Id
	@Column(name="DOCNUMBER_WORDID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDocnumberWordid() {
        return this.docnumberWordid;
    }

    public void setDocnumberWordid(String docnumberWordid) {
        this.docnumberWordid = docnumberWordid;
    }

    /** 
     *            @hibernate.property
     *             column="DOCNUMBER_WORDNAME"
     *             length="30"
     *         
     */
    @Column(name="DOCNUMBER_WORDNAME",nullable=true)
    public String getDocnumberWordname() {
        return this.docnumberWordname;
    }

    public void setDocnumberWordname(String docnumberWordname) {
        this.docnumberWordname = docnumberWordname;
    }

    /** 
     *            @hibernate.property
     *             column="DOCNUMBER_WORDREMAEK"
     *             length="400"
     *         
     */
    @Column(name="DOCNUMBER_WORDREMAEK",nullable=true)
    public String getDocnumberWordremaek() {
        return this.docnumberWordremaek;
    }

    public void setDocnumberWordremaek(String docnumberWordremaek) {
        this.docnumberWordremaek = docnumberWordremaek;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("docnumberWordid", getDocnumberWordid())
            .toString();
    }

}
