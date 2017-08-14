package com.strongit.doc.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



/** 
 *        @hibernate.class
 *         table="T_DOC_SENDS_RET"
 *     
*/
@Entity
@Table(name = "T_DOC_SENDS_RET", catalog = "", schema = "")
public class TdocSendRet implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 退文ID   */
    private String senddocretId;

    /** 流程的businessId*/
    private String businessId;

    /** 退文原因 */
    private String docrecvRemark;

    /** 退文理由 */
    private String liyou;

    /**退文单拒收其他原因***/
    private String qita;

    /** full constructor */
    public TdocSendRet(String senddocretId, String businessId, String docrecvRemark, String liyou, String qita) {
        this.senddocretId = senddocretId;
        this.businessId = businessId;
        this.docrecvRemark = docrecvRemark;
        this.liyou = liyou;
        this.qita = qita;
    }

    /** default constructor */
    public TdocSendRet() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="uuid"
     *             type="java.lang.String"
     *             column="SENDS_RET_ID"
     *         
     */
    @Id
    @Column(name="SENDS_RET_ID",nullable=false)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getSenddocretId() {
        return this.senddocretId;
    }

    public void setSenddocretId(String senddocretId) {
        this.senddocretId = senddocretId;
    }

    /** 
     *            @hibernate.property
     *             column="BUSINESS_ID"
     *             length="1000"
     *         
     */
    @Column(name="BUSINESS_ID")
    public String getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_RECV_REMARK"
     *             length="4000"
     *         
     */
    @Column(name="DOC_RECV_REMARK")
    public String getDocrecvRemark() {
        return this.docrecvRemark;
    }

    public void setDocrecvRemark(String docrecvRemark) {
        this.docrecvRemark = docrecvRemark;
    }


    /** 
     *            @hibernate.property
     *             column="LIYOU"
     *             length="100"
     *         
     */
    @Column(name="LIYOU")
    public String getLiyou() {
        return this.liyou;
    }

    public void setLiyou(String liyou) {
        this.liyou = liyou;
    }
	
	 /** 
     *            @hibernate.property
     *             column="QITA"
     *             length="300"
     *         
     */
    @Column(name="QITA")
	public String getQita() {
		return qita;
	}

	public void setQita(String qita) {
		this.qita = qita;
	}

}
