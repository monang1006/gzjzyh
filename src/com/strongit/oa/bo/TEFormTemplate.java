package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 电子表单模板管理实体类
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Aug 22, 2011
 * @classpath	com.strongit.oa.bo.TEFormTemplate
 * @version  	3.0.2
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
@Entity
@Table(name = "T_EF_TEMPLATE")
public class TEFormTemplate implements Serializable {

	/**
	 * 系列化id
	 */
	private static final long serialVersionUID = 8231796924125125463L;
	
	private Long id	;		//主键id
	
	private String title ;		//模板标题
	
	private byte[] content;		//模板内容
	
	private Date createTime;	//模板创建时间
	
	private String creator;		//模板创建人员
	
	private Date modifyTime;	//模板最后修改时间
	
	private String mender;		//模板最后修改人
	
	private String type;		//模板类型
	
	private String orgCode;		//模板所属机构编码
	

	public TEFormTemplate() {
		
	}
	
	public TEFormTemplate(Long id ,String title,Date createTime,Date modifyTime,String creator,String mender,String type,String orgCode) {
		this.id = id;
		this.title = title;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.creator = creator;
		this.type = type;
		this.mender = mender;
		this.orgCode = orgCode;
	}
	
	@Id
	@Column(name="T_ID",nullable=false,length = 32)
	@GeneratedValue(strategy = GenerationType.AUTO,generator="eformtemplate_seq")
	@SequenceGenerator(name = "eformtemplate_seq", sequenceName="SEQ_T_EF_TEMPLATE_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Lob
	@Column(name="T_CONTENT")
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Column(name="T_CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="T_CREATOR")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Column(name="T_MENDER")
	public String getMender() {
		return mender;
	}

	public void setMender(String mender) {
		this.mender = mender;
	}

	@Column(name="T_MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name="T_TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="T_TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name="T_ORGCODE")
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

}
