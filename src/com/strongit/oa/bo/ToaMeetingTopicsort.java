package com.strongit.oa.bo;

import java.io.Serializable;
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
import org.hibernate.annotations.OrderBy;


/** 
 *        @hibernate.class
 *         table="T_OA_MEETING_TOPICSORT"
 *     
*/
@Entity
@Table(name = "T_OA_MEETING_TOPICSORT", catalog = "", schema = "")
public class ToaMeetingTopicsort implements Serializable {

    /** identifier field */
    private String topicsortId;
    
    private String topicsortNo;

    /** nullable persistent field */
    private String topicsortName;

    /** nullable persistent field */
    private String topicsortDemo;

    /** nullable persistent field */
    private String processName;
    
    private String  isDisbled;//IS_DISABLED

    /** persistent field */
  
    /** full constructor */
    public ToaMeetingTopicsort(String topicsortId, String topicsortName, String topicsortDemo, String processName) {
        this.topicsortId = topicsortId;
        this.topicsortName = topicsortName;
        this.topicsortDemo = topicsortDemo;
        this.processName = processName;
        
    }

    /** default constructor */
    public ToaMeetingTopicsort() {
    }

    /** minimal constructor */
    public ToaMeetingTopicsort(String topicsortId) {
        this.topicsortId = topicsortId;
        
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="TOPICSORT_ID"
     *         
     */
    @Id
    @Column(name = "TOPICSORT_ID", nullable = false)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getTopicsortId() {
        return this.topicsortId;
    }

    public void setTopicsortId(String topicsortId) {
        this.topicsortId = topicsortId;
    }

    /** 
     *            @hibernate.property
     *             column="TOPICSORT_NAME"
     *             length="50"
     *         
     */
    @Column(name = "TOPICSORT_NAME")
    public String getTopicsortName() {
        return this.topicsortName;
    }

    public void setTopicsortName(String topicsortName) {
        this.topicsortName = topicsortName;
    }

    /** 
     *            @hibernate.property
     *             column="TOPICSORT_DEMO"
     *             length="500"
     *         
     */
    @Column(name = "TOPICSORT_DEMO")
    public String getTopicsortDemo() {
        return this.topicsortDemo;
    }

    public void setTopicsortDemo(String topicsortDemo) {
        this.topicsortDemo = topicsortDemo;
    }

    /** 
     *            @hibernate.property
     *             column="PROCESS_NAME"
     *             length="100"
     *         
     */
    @Column(name = "PROCESS_NAME")
    public String getProcessName() {
        return this.processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
    @Column(name = "TOPICSORT_NO")
	public String getTopicsortNo() {
		return topicsortNo;
	}

	public void setTopicsortNo(String topicsortNo) {
		this.topicsortNo = topicsortNo;
	}
	@Column(name = "IS_DISABLED")
	public String getIsDisbled() {
		return isDisbled;
	}

	public void setIsDisbled(String isDisbled) {
		this.isDisbled = isDisbled;
	}

}
