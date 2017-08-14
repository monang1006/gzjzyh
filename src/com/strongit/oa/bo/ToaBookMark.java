package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 标签管理BO类.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-7-7 下午04:40:49
 * @version  2.0.2.3
 * @classpath com.strongit.oa.bo.ToaBookMark
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Entity
@Table(name = "T_OA_BOOKMARK", catalog = "", schema = "")
public class ToaBookMark {

	private String id ;				//主键
	
	private String name ;			//标签名称
	
	private String desc ;			//标签描述
	
	private String remark ;			//标签备注
	
	private String rest1 ;			//扩展字段一
	
	private String rest2 ;			//扩展字段二
	
	private String rest3 ;			//扩展字段三
	
	private String rest4 ;			//扩展字段四
	
	private String rest5 ;			//扩展临时字段：存储字段下拉列表信息

	@Column(name = "BOOKMARK_DESC")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

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

	@Column(name = "BOOKMARK_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "BOOKMARK_REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "REST1")
	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2")
	public String getRest2() {
		return rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "REST3")
	public String getRest3() {
		return rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}

	@Column(name = "REST4")
	public String getRest4() {
		return rest4;
	}

	public void setRest4(String rest4) {
		this.rest4 = rest4;
	}

	@Transient
	public String getRest5() {
		return rest5;
	}

	public void setRest5(String rest5) {
		this.rest5 = rest5;
	}
}
