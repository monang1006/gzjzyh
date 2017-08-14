package com.strongit.oa.bo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * TEfJsptemplate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_EF_JSPTEMPLATE")
public class TEJsptemplate implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private Date createtime;
	private String url;
	private Date modifyTime;
	private String creator;//创建者
	private String type;//流程类型，保存时设置为0。用于区别于电子表单

	// Constructors

	/** default constructor */
	public TEJsptemplate() {
	}


	/** full constructor */
	public TEJsptemplate(Long id, String name, Date createtime, String url,
			Date modifyTime) {
		this.id = id;
		this.name = name;
		this.createtime = createtime;
		this.url = url;
		this.modifyTime = modifyTime;
	}

	@Id
	@Column(name="ID",nullable=false,length = 32)
	@GeneratedValue(strategy = GenerationType.AUTO,generator="jspeformtemplate_seq")
	@SequenceGenerator(name = "jspeformtemplate_seq", sequenceName="SEQ_T_EF_JSPTEMPLATE_ID")
	public Long getId() {
		return this.id;
	}
	@Column(name = "CREATOR")
	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length = 7)
	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Column(name = "URL", length = 40)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME", length = 7)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}