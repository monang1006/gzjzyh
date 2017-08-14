package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_OA_INFO_OPERATIONPRIVIL")
public class ToaInfoOperationPrivil implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4027510951104396943L;
	
	/**
	 * 主键ID
	 */
	private String id;
	
	private String userId;
	/**
	 * 操作权限BO
	 */
	private ToaUumsBaseOperationPrivil toaUumsBaseOptPrivil;
	
	@Id
	@Column(name="ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRIVIL_ID")
	public ToaUumsBaseOperationPrivil getToaUumsBaseOptPrivil() {
		return toaUumsBaseOptPrivil;
	}
	public void setToaUumsBaseOptPrivil(
			ToaUumsBaseOperationPrivil toaUumsBaseOptPrivil) {
		this.toaUumsBaseOptPrivil = toaUumsBaseOptPrivil;
	}
	
	@Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
