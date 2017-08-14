package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_DOCAFTERFLOW"
 *     
*/
@Entity
@Table(name="T_OA_DOCAFTERFLOW")
public class ToaDocafterflow implements Serializable {

	private static final long serialVersionUID = 3075245651215254990L;

	/** identifier field */
	@Id
	@GeneratedValue
    private String docId;

    /** nullable persistent field */
    private String deptId;

    /** nullable persistent field */
    private String deptCode;

    /** nullable persistent field */
    private String getOrnot;

    /** nullable persistent field */
    private Date getDate;

    /** nullable persistent field */
    private Date getDocDate;
    
    private String deptname;
    
    private String getType;
    
    private String userId;
    
    private String userName;
    
    private String userLoginName;

    /** persistent field */
    private com.strongit.oa.bo.ToaDocDis toaDocDis;

    /** full constructor */
    public ToaDocafterflow(String docId, String deptId, String deptCode, String getOrnot, Date getDate, Date getDocDate, com.strongit.oa.bo.ToaDocDis toaDocDis) {
        this.docId = docId;
        this.deptId = deptId;
        this.deptCode = deptCode;
        this.getOrnot = getOrnot;
        this.getDate = getDate;
        this.getDocDate = getDocDate;
        this.toaDocDis = toaDocDis;
    }

    /** default constructor */
    public ToaDocafterflow() {
    }

    /** minimal constructor */
    public ToaDocafterflow(String docId, com.strongit.oa.bo.ToaDocDis toaDocDis) {
        this.docId = docId;
        this.toaDocDis = toaDocDis;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DOC_ID"
     *         
     */
    @Id
	@Column(name="DOC_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getDocId() {
        return this.docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    /** 
     *            @hibernate.property
     *             column="DEPT_ID"
     *             length="32"
     *         
     */
    @Column(name="DEPT_ID",length=32)
    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /** 
     *            @hibernate.property
     *             column="DEPT_CODE"
     *             length="42"
     *         
     */
    @Column(name="DEPT_CODE",length=42)
    public String getDeptCode() {
        return this.deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    /** 
     *            @hibernate.property
     *             column="GET_ORNOT"
     *             length="1"
     *         
     */
    @Column(name="GET_ORNOT",length=1)
    public String getGetOrnot() {
        return this.getOrnot;
    }

    public void setGetOrnot(String getOrnot) {
        this.getOrnot = getOrnot;
    }

    /** 
     *            @hibernate.property
     *             column="GET_DATE"
     *             length="7"
     *         
     */
    @Column(name="GET_DATE",length=7)
    public Date getGetDate() {
        return this.getDate;
    }

    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }
    
    @Column(name="DEPTNAME",length=100)
	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

    /** 
     *            @hibernate.property
     *             column="GET_DOC_DATE"
     *             length="7"
     *         
     */
    @Column(name="GET_DOC_DATE",length=7)
    public Date getGetDocDate() {
        return this.getDocDate;
    }

    public void setGetDocDate(Date getDocDate) {
        this.getDocDate = getDocDate;
    }
    
    

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="SENDDOC_ID"         
     *         
     */
	@ManyToOne
	@JoinColumn(name="SENDDOC_ID")
	@Cascade(value={CascadeType.PERSIST, CascadeType.MERGE})
    public com.strongit.oa.bo.ToaDocDis getToaDocDis() {
        return this.toaDocDis;
    }

    public void setToaDocDis(com.strongit.oa.bo.ToaDocDis toaDocDis) {
        this.toaDocDis = toaDocDis;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("docId", getDocId())
            .toString();
    }

    @Column(name="GET_TYPE",length=1)
	public String getGetType() {
		return getType;
	}

	public void setGetType(String getType) {
		this.getType = getType;
	}

	@Column(name="USER_ID",length=32)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name="USER_LOGINNAME",length=100)
	public String getUserLoginName() {
		return userLoginName;
	}
	
	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	@Column(name="USER_NAME",length=100)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


}
