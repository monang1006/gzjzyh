package com.strongit.oa.bo;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/** 
 *        @hibernate.class
 *         table="T_OA_EXCHANGE"
 *     
*/
@Entity
@Table(name="T_OA_EXCHANGE")
public class ToaExchange {

	/** 主键ID */
    private String exchangeId;

    /** 标题 */
    private String docTitle;

    /** 作者 */
    private String docAuthor;

    /** 文号 */
    private String docNum;

    /** 发送机关 */
    private String sendEnterprise;

    /** 正文 */
    private byte[]  docContent;

    /** 发文时间 */
    private Date sendDate;
    
    /** full constructor */
	public ToaExchange(String exchangeId, String docTitle, String docAuthor, String docNum, String sendEnterprise, byte[] docContent, Date sendDate) {
		super();
		this.exchangeId = exchangeId;
		this.docTitle = docTitle;
		this.docAuthor = docAuthor;
		this.docNum = docNum;
		this.sendEnterprise = sendEnterprise;
		this.docContent = docContent;
		this.sendDate = sendDate;
	}
	
	public ToaExchange(){
	}

    /** 
     *            @hibernate.property
     *             column="DOC_AUTHOR"
     *             length="25"
     *         
     */
    @Column(name="DOC_AUTHOR",nullable=true)
	public String getDocAuthor() {
		return docAuthor;
	}

	public void setDocAuthor(String docAuthor) {
		this.docAuthor = docAuthor;
	}
	
	/** 
     *            @hibernate.property
     *             column="DOC_NUM"
     *             length="25"
     *         
     */
    @Column(name="DOC_NUM",nullable=true)
	public String getDocNum() {
		return docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}

	/** 
     *            @hibernate.property
     *             column="DOC_TITILE"
     *             length="255"
     *         
     */
    @Column(name="DOC_TITILE",nullable=true)
	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}
	
	/** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="EXCHANGE_ID"
     */
    @Id
	@Column(name="EXCHANGE_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

	/** 
     *            @hibernate.property
     *             column="SEND_DATE"
     *             length="7"
     *         
     */
    @Column(name="SEND_DATE",nullable=true)
	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/** 
     *            @hibernate.property
     *             column="SEND_ENTERPRISE"
     *             length="25"
     *         
     */
    @Column(name="SEND_ENTERPRISE",nullable=true)
	public String getSendEnterprise() {
		return sendEnterprise;
	}

	public void setSendEnterprise(String sendEnterprise) {
		this.sendEnterprise = sendEnterprise;
	}

	/** 
	 *            @hibernate.property
	 *             column="DOC_CONTENT"
	 *             length="4000"
	 *         
	 */
    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_CONTENT", columnDefinition = "BLOB")
	public byte[] getDocContent() {
		return docContent;
	}

	public void setDocContent(byte[] docContent) {
		this.docContent = docContent;
	}

}
