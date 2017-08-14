package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * OA业务表与机构表关联的中间表.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2009-11-11 下午01:43:06
 * @version  2.0.2.3
 * @classpath com.strongit.oa.bo.ToaOrgRelate
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Entity
@Table(name = "T_OA_ORGRELATE")
public class ToaOrgRelate implements Serializable {

	/**
	 * 系列化id
	 */
	private static final long serialVersionUID = 8231796924125125463L;
	
	private String id;//主键id
	
	private String businessId;//业务表主键

	private String orgId;//需要保存的当前用户所在机构（部门）id
	
	private String orgCode;//需要保存当前用户所在机构编码

	
	@Column(name = "BUSINESS_ID" ,nullable = false)
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Id
	@Column(name="ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(name = "ORG_CODE")
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@Column(name = "ORG_ID")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}
