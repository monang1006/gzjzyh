package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_JBPM_WORKFLOWBUSINESS"<br/>
 *                  工作流和业务关联的中间表,用于解决流程关联多张业务数据情况下的数据查询缓慢的性能问题
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_JBPM_WORKFLOWBUSINESS")
public class TjbpmWorkflowBusiness implements Serializable {

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 记录实例id为instanceId时，该实例对应业务数据表中字段为PERSON_CONFIG_FLAG的值
	 */
	private String businessId;

	/**
	 * 记录业务id
	 */
	private Long instanceId;
	
	
	public TjbpmWorkflowBusiness() {
	}

	public TjbpmWorkflowBusiness(String businessId, Long instanceId) {
		this.businessId = businessId;
		this.instanceId = instanceId;
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

	@Column(name = "INSTANCE_ID", nullable = true)
	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	@Column(name = "BUSINESS_ID", nullable = true)
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

}
