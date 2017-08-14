package com.strongit.doc.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;



/** 
 *        @hibernate.class
 *         table="T_DOC_SENDS"
 *     
*/
@Entity
@Table(name = "T_DOC_SENDS", catalog = "", schema = "")
public class TdocSend implements Serializable {

    /** 分发ID   */
    private String senddocId;
    
    /** docId　　*/
    private String docId;

    /** 收文单位标识*/
    private String deptCode;

    /** 收文单位名称 */
    private String deptName;

    /** 收文日期 */
    private Date docRecvTime;

    /** 收文人 */
    private String docRecvUser;

    /** 打印份数 */
    private String docHavePrintSum;

    /** 已打印份数 */
    private String docHavePrintNum;

    /** 回执 */
    private String docRecvRemark;

    /** 备注 */
    private String docRemark;
    
    /**退文理由**/
    private String liyous;

    /** 状态 */
    private String recvState;

    /** 归档日期 */
    private Date docFilingTime;

    /** 归档人 */
    private String filingUser;

    /** 是否删除 */
    private String isdelete;

    /** 操作IP */
    private String operateIp;
    
    /** 公文传输转来文草稿  标识 */
    private String docTurnDraft;
    /**退文单拒收其他原因***/
    private String qita;

	public final static String HAS_FILIONG = "1";// 已归档
	public final static String HAS_NO_FILIONG = "0";// 未归档

    /** persistent field */
    private com.strongit.doc.bo.TtransDoc ttransDoc;
    
    /** 删除状态*/
    public static final String DOCSEND_NOTDEL = "0";	//未删除
    public static final String DOCSEND_DEL = "1";	//已删除
    
    /** 分发状态*/
    public static final String DOCSEND_WAITRECV = "0";	//待收
    public static final String DOCSEND_RECVED = "1";		//已收
    public static final String DOCSEND_NOTRECV = "2";	//已拒收
    public static final String DOCSEND_RECYCLER = "3";		//回收
    
    /**区分主送抄送标识***/
    private String zsorcs;				// 1 主送  0  抄送
    /**退回来的收文编号***/
    private String swbh;				//退回来的收文编号
    
    /** full constructor */
    public TdocSend(String senddocId, String docId, String deptCode, String deptName, Date docRecvTime, String docRecvUser, String docHavePrintSum, String docHavePrintNum, String docRecvRemark, String docRemark, String recvState, Date docFilingTime, String filingUser, String isdelete,String zsorcs, String operateIp,String docTurnDraft,String swbh, com.strongit.doc.bo.TtransDoc ttransDoc) {
        this.senddocId = senddocId;
        this.docId = docId;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.docRecvTime = docRecvTime;
        this.docRecvUser = docRecvUser;
        this.docHavePrintSum = docHavePrintSum;
        this.docHavePrintNum = docHavePrintNum;
        this.docRecvRemark = docRecvRemark;
        this.docRemark = docRemark;
        this.recvState = recvState;
        this.docFilingTime = docFilingTime;
        this.filingUser = filingUser;
        this.isdelete = isdelete;
        this.operateIp = operateIp;
        this.docTurnDraft=docTurnDraft;
        this.ttransDoc = ttransDoc;
        this.zsorcs = zsorcs;
        this.swbh = swbh;
    }

    /** default constructor */
    public TdocSend() {
    }

    /** minimal constructor */
    public TdocSend(String senddocId, com.strongit.doc.bo.TtransDoc ttransDoc) {
        this.senddocId = senddocId;
        this.ttransDoc = ttransDoc;
    }

    /** 
     *            @hibernate.id
     *             generator-class="uuid"
     *             type="java.lang.String"
     *             column="SENDDOC_ID"
     *         
     */
    @Id
    @Column(name="SENDDOC_ID",nullable=false)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getSenddocId() {
        return this.senddocId;
    }

    public void setSenddocId(String senddocId) {
        this.senddocId = senddocId;
    }

    /** 
     *            @hibernate.property
     *            update="false"
     *             insert="false"
     *             column="DOC_ID"
     *             length="200"
     *         
     */
    @Column(name="DOC_ID",insertable=false,updatable=false,nullable=false)
    public String getDocId() {
        return this.docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
    
    /** 
     *            @hibernate.property
     *             column="DEPT_CODE"
     *             length="50"
     *         
     */
    @Column(name="DEPT_CODE")
    public String getDeptCode() {
        return this.deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    /** 
     *            @hibernate.property
     *             column="DEPT_NAME"
     *             length="1000"
     *         
     */
    @Column(name="DEPT_NAME")
    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_RECV_TIME"
     *             length="7"
     *         
     */
    @Column(name="DOC_RECV_TIME")
    public Date getDocRecvTime() {
        return this.docRecvTime;
    }

    public void setDocRecvTime(Date docRecvTime) {
        this.docRecvTime = docRecvTime;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_RECV_USER"
     *             length="200"
     *         
     */
    @Column(name="DOC_RECV_USER")
    public String getDocRecvUser() {
        return this.docRecvUser;
    }

    public void setDocRecvUser(String docRecvUser) {
        this.docRecvUser = docRecvUser;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_HAVE_PRINT_SUM"
     *             length="4"
     *         
     */
    @Column(name="DOC_HAVE_PRINT_SUM")
    public String getDocHavePrintSum() {
        return this.docHavePrintSum;
    }

    public void setDocHavePrintSum(String docHavePrintSum) {
        this.docHavePrintSum = docHavePrintSum;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_HAVE_PRINT_NUM"
     *             length="4"
     *         
     */
    @Column(name="DOC_HAVE_PRINT_NUM")
    public String getDocHavePrintNum() {
        return this.docHavePrintNum;
    }

    public void setDocHavePrintNum(String docHavePrintNum) {
        this.docHavePrintNum = docHavePrintNum;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_RECV_REMARK"
     *             length="4000"
     *         
     */
    @Column(name="DOC_RECV_REMARK")
    public String getDocRecvRemark() {
        return this.docRecvRemark;
    }

    public void setDocRecvRemark(String docRecvRemark) {
        this.docRecvRemark = docRecvRemark;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_REMARK"
     *             length="4000"
     *         
     */
    @Column(name="DOC_REMARK")
    public String getDocRemark() {
        return this.docRemark;
    }

    public void setDocRemark(String docRemark) {
        this.docRemark = docRemark;
    }

    /** 
     *            @hibernate.property
     *             column="RECV_STATE"
     *             length="1"
     *         
     */
    @Column(name="RECV_STATE")
    public String getRecvState() {
        return this.recvState;
    }

    public void setRecvState(String recvState) {
        this.recvState = recvState;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_FILING_TIME"
     *             length="7"
     *         
     */
    @Column(name="DOC_FILING_TIME")
    public Date getDocFilingTime() {
        return this.docFilingTime;
    }

    public void setDocFilingTime(Date docFilingTime) {
        this.docFilingTime = docFilingTime;
    }

    /** 
     *            @hibernate.property
     *             column="FILING_USER"
     *             length="200"
     *         
     */
    @Column(name="FILING_USER")
    public String getFilingUser() {
        return this.filingUser;
    }

    public void setFilingUser(String filingUser) {
        this.filingUser = filingUser;
    }

    /** 
     *            @hibernate.property
     *             column="ISDELETE"
     *             length="1"
     *         
     */
    @Column(name="ISDELETE")
    public String getIsdelete() {
        return this.isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    /** 
     *            @hibernate.property
     *             column="OPERATE_IP"
     *             length="15"
     *         
     */
    @Column(name="OPERATE_IP")
    public String getOperateIp() {
        return this.operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }
    /** 
     *            @hibernate.property
     *             column="LIYOUS"
     *             length="100"
     *         
     */
    @Column(name="LIYOUS")
    public String getLiyous() {
		return liyous;
	}
   
	public void setLiyous(String liyous) {
		this.liyous = liyous;
	}
	 /** 
     *            @hibernate.property
     *             column="QITA"
     *             length="100"
     *         
     */
    @Column(name="QITA")
	public String getQita() {
		return qita;
	}

	public void setQita(String qita) {
		this.qita = qita;
	}

	/** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="DOC_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DOC_ID")
    public com.strongit.doc.bo.TtransDoc getTtransDoc() {
        return this.ttransDoc;
    }

    public void setTtransDoc(com.strongit.doc.bo.TtransDoc ttransDoc) {
        this.ttransDoc = ttransDoc;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("senddocId", getSenddocId())
            .toString();
    }
    @Column(name="DOCTURNDRAFT")
	public String getDocTurnDraft() {
		return docTurnDraft;
	}

	public void setDocTurnDraft(String docTurnDraft) {
		this.docTurnDraft = docTurnDraft;
	}
	@Column(name="ZSORCS")
	public String getZsorcs() {
		return zsorcs;
	}

	public void setZsorcs(String zsorcs) {
		this.zsorcs = zsorcs;
	}
	@Column(name="SWBH")
	public String getSwbh() {
		return swbh;
	}

	public void setSwbh(String swbh) {
		this.swbh = swbh;
	}

}
