package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_JBPM_BUSINESS"<br/>
 *                  工作流和业务关联的中间表,用于解决流程关联多张业务数据情况下的数据查询缓慢的性能问题
 * 
 */
/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Apr 9, 2012 3:05:38 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.bo.Tjbpmbusiness
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_JBPM_BUSINESS")
public class Tjbpmbusiness implements Serializable {
	/**
	 * @field BUSINESS_TYPE_YJZX	意见征询
	 */
	public final static String BUSINESS_TYPE_YJZX = "0";	//意见征询
	/**
	 * @field BUSINESS_TYPE_YJZX_SUSPEND	废除时，意见征询挂起
	 */
	public final static String BUSINESS_TYPE_YJZX_SUSPEND = "2";	//废除时，意见征询挂起
	/**
	 * @field BUSINESS_TYPE_COMMON	普通类型
	 */
	public final static String BUSINESS_TYPE_COMMON = "1";	//普通类型
	/**
	 * @field BUSINESS_TYPE_DOCBACKTRACK_COMMON	公文补录，普通类型
	 */
	public final static String BUSINESS_TYPE_DOCBACKTRACK_COMMON = "3";	//公文补录，普通类型
	/**
	 * @field BUSINESS_TYPE_DOCBACKTRACK_YJZX	公文补录，意见征询
	 */
	public final static String BUSINESS_TYPE_DOCBACKTRACK_YJZX  = "4";	//公文补录，意见征询
	/**
	 * @field BUSINESS_TYPE_DOCBACKTRACK_YJZX_SUSPEND	公文补录，意见征询挂起
	 */
	public final static String BUSINESS_TYPE_DOCBACKTRACK_YJZX_SUSPEND  = "5";	//公文补录，意见征询挂起
	/**
	 * 可以显示的业务类型
	 * 
	 * @author yanjian
	 * @return
	 * Nov 23, 2012 7:25:18 PM
	 */
	public static String getShowableBusinessType() {
		return new StringBuilder()
							.append("'").append(BUSINESS_TYPE_YJZX).append("'")
				.append(",").append("'").append(BUSINESS_TYPE_COMMON).append("'")
				.toString();
	}
	
	
	/**
	 * 主键
	 */
	private String id;

	/**
	 * 记录实例id为instanceId时，该实例对应业务数据表中字段为PERSON_CONFIG_FLAG的值
	 */
	private String personConfigFlag;

	/**
	 * 记录实例id为instanceId时，该实例对应业务数据表中字段为END_TIME的值,若不存在该字段默认为null
	 */
	private String endTime;

	/**
	 * 记录业务id
	 */
	private String businessId;

	private String businessType = BUSINESS_TYPE_COMMON;

	private String recvNum;

	private String docNumber;

	private String issueDepartSigned;
	
	@Column(name = "DOC_NUMBER")
	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	@Column(name = "ISSUE_DEPART_SIGNED")
	public String getIssueDepartSigned() {
		return issueDepartSigned;
	}

	public void setIssueDepartSigned(String issueDepartSigned) {
		this.issueDepartSigned = issueDepartSigned;
	}

	@Column(name = "RECV_NUM")
	public String getRecvNum() {
		return recvNum;
	}

	public void setRecvNum(String recvNum) {
		this.recvNum = recvNum;
	}

	@Column(name = "BUSINESS_ID", nullable = true)
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Tjbpmbusiness() {

	}

	public Tjbpmbusiness(String businessId) {
		this.businessId = businessId;
	}

	@Id
	@Column(name = "ID", nullable = false, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "PERSON_CONFIG_FLAG", nullable = true, length = 1)
	public String getPersonConfigFlag() {
		return personConfigFlag;
	}

	public void setPersonConfigFlag(String personConfigFlag) {
		this.personConfigFlag = personConfigFlag;
	}

	@Column(name = "END_TIME", nullable = true)
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "BUSINESS_TYPE")
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

}
