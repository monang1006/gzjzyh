package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/** 系统参数配置表
 *        @hibernate.class
 *         table="T_OA_SYSTEM_PARAM_CONFIG"
 *     
*/
@Entity
@Table(name = "T_OA_SYSTEM_PARAM_CONFIG", catalog = "", schema = "")
public class ToaSystemParamConfig {

	private String id ;							//主键id
	
	private String userId ;						//用户id
	
	private String module ;						//模块,采用枚举实现
	
	private String size ;						//设定大小 单位为KB
	
	private String rest1 ;						//扩展字段1
	
	private String rest2 ;						//扩展字段2
	
	private String rest3 ;						//扩展字段3
	
	private String rest4 ;						//扩展字段4

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "Module")
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Column(name = "Rest1")
	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "Rest2")
	public String getRest2() {
		return rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "Rest3")
	public String getRest3() {
		return rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}

	@Column(name = "Rest4")
	public String getRest4() {
		return rest4;
	}

	public void setRest4(String rest4) {
		this.rest4 = rest4;
	}

	@Column(name = "ConfigSize")
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Column(name = "UserId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
