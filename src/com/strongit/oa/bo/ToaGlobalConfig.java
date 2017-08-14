package com.strongit.oa.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 全局配置实体.
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Feb 15, 2012
 * @classpath	com.strongit.oa.bo.ToaGlobalConfig
 * @version  	5.0
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
@Entity
@Table(name = "T_OA_GLOBAL_CONFIG")
public class ToaGlobalConfig {

	private String globalId;						//业务主键
	
	private String globalName;						//配置项名称,前缀为plugins_
	
	private String globalValue;						//配置项值
	
	private String globalModule;					//配置项所属模块
	
	private String globalDesc;						//配置项描述
	
	private Date globalDate;						//配置项创建日期

	@Column(name = "GLOBAL_DATE")
	public Date getGlobalDate() {
		return globalDate;
	}

	public void setGlobalDate(Date globalDate) {
		this.globalDate = globalDate;
	}

	@Column(name = "GLOBAL_DESC")
	public String getGlobalDesc() {
		return globalDesc;
	}

	public void setGlobalDesc(String globalDesc) {
		this.globalDesc = globalDesc;
	}

	@Id
	@Column(name = "GLOBAL_ID",nullable = false,length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid",strategy = "uuid")
	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}

	@Column(name = "GLOBAL_MODULE")
	public String getGlobalModule() {
		return globalModule;
	}

	public void setGlobalModule(String globalModule) {
		this.globalModule = globalModule;
	}

	@Column(name = "GLOBAL_NAME")
	public String getGlobalName() {
		return globalName;
	}

	public void setGlobalName(String globalName) {
		this.globalName = globalName;
	}

	@Lob
	@Column(name = "GLOBAL_VALUE")
	public String getGlobalValue() {
		return globalValue;
	}

	public void setGlobalValue(String globalValue) {
		this.globalValue = globalValue;
	}
}
