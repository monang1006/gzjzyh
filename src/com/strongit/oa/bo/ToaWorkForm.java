package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 流程与表单的关联对象
 * @author zouhr
 *
 */
@Entity
@Table(name = "T_OA_WF_EF")
public class ToaWorkForm implements Serializable {

	@Id
	@GeneratedValue
	private String id;

	private long pfId;//流程ID

	private String pfName;//流程名

	private String qformName;//查询表单名
	
	private String vformName;//展现表单名

	private long qformId;//查询表单ID

	private long vformId;//展现表单ID



	public ToaWorkForm(String id, long pfId, String pfName, String qformName,
			String vformName, long qformId, long vformId) {
		super();
		this.id = id;
		this.pfId = pfId;
		this.pfName = pfName;
		this.qformName = qformName;
		this.vformName = vformName;
		this.qformId = qformId;
		this.vformId = vformId;
	}

	public ToaWorkForm() {
		super();
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

	@Column(name = "PF_ID", nullable = true)
	public long getPfId() {
		return pfId;
	}

	public void setPfId(long pfId) {
		this.pfId = pfId;
	}

	@Column(name = "PF_NAME", nullable = true)
	public String getPfName() {
		return pfName;
	}

	public void setPfName(String pfName) {
		this.pfName = pfName;
	}

	@Column(name = "QFORM_NAME", nullable = true)
	public String getQformName() {
		return qformName;
	}

	public void setQformName(String qformName) {
		this.qformName = qformName;
	}
	
	@Column(name = "VFORM_NAME", nullable = true)
	public String getVformName() {
		return vformName;
	}

	public void setVformName(String vformName) {
		this.vformName = vformName;
	}

	@Column(name = "Q_FORMID", nullable = true)
	public long getQformId() {
		return qformId;
	}

	public void setQformId(long qformId) {
		this.qformId = qformId;
	}

	@Column(name = "V_FORMID", nullable = true)
	public long getVformId() {
		return vformId;
	}

	public void setVformId(long vformId) {
		this.vformId = vformId;
	}

}
