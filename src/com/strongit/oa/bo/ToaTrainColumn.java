package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_INFOPUBLISH_COLUMN"
 *     
*/
@Entity
@Table(name = "T_OA_TRAIN_COLUMN")
public class ToaTrainColumn implements Serializable {

    /** identifier field */
	@Id
	@GeneratedValue
    private String clumnId;

    /** nullable persistent field */
    private String clumnParent;

    /** nullable persistent field */
    private String clumnDir;

    /** persistent field */
    private String clumnName;

    /** nullable persistent field */
    private Date clumnCreatetime;

    /** nullable persistent field */
    private String clumnMemo;

  
    /** full constructor */
    public ToaTrainColumn(String clumnId, String clumnParent, String clumnDir, String clumnName, Date clumnCreatetime, String clumnMemo) {
        this.clumnId = clumnId;
        this.clumnParent = clumnParent;
        this.clumnDir = clumnDir;
        this.clumnName = clumnName;
        this.clumnCreatetime = clumnCreatetime;
        this.clumnMemo = clumnMemo;
       
    }

    /** default constructor */
    public ToaTrainColumn() 
    {
    }

  

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="CLUMN_ID"
     *         
     */
    @Id
    @Column(name = "CLUMN_ID", nullable = false, length = 32)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getClumnId() {
        return this.clumnId;
    }

    public void setClumnId(String clumnId) {
        this.clumnId = clumnId;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_PARENT"
     *             length="32"
     *         
     */
    @Column(name = "CLUMN_PARENT", nullable = true)
    public String getClumnParent() {
        return this.clumnParent;
    }

    public void setClumnParent(String clumnParent) {
        this.clumnParent = clumnParent;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_DIR"
     *             length="100"
     *         
     */
    @Column(name = "CLUMN_DIR", nullable = true)
    public String getClumnDir() {
        return this.clumnDir;
    }

    public void setClumnDir(String clumnDir) {
        this.clumnDir = clumnDir;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_NAME"
     *             length="100"
     *             not-null="true"
     *         
     */
    @Column(name = "CLUMN_NAME", nullable = false)
    public String getClumnName() {
        return this.clumnName;
    }

    public void setClumnName(String clumnName) {
        this.clumnName = clumnName;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_CREATETIME"
     *             length="7"
     *         
     */
    @Column(name = "CLUMN_CREATETIME", nullable = true)
    public Date getClumnCreatetime() {
        return this.clumnCreatetime;
    }

    public void setClumnCreatetime(Date clumnCreatetime) {
        this.clumnCreatetime = clumnCreatetime;
    }

    /** 
     *            @hibernate.property
     *             column="CLUMN_MEMO"
     *             length="150"
     *         
     */
    @Column(name = "CLUMN_MEMO", nullable = true)
    public String getClumnMemo() {
        return this.clumnMemo;
    }

    public void setClumnMemo(String clumnMemo) {
        this.clumnMemo = clumnMemo;
    }
    public String toString() {
        return new ToStringBuilder(this)
            .append("clumnId", getClumnId())
            .toString();
    }

}
