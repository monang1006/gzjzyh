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
/**
 * 操作权限-岗位关联BO
 * @author 邓志城
 *
 */
@Entity
@Table(name="T_OA_POST_OPERATIONPRIVIL")
public class ToaPostOperationPrivil implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8757976013799863875L;
	
	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 岗位id（和岗位弱关联,这里的岗位为全局岗位）
	 */
	private String postId;
	/**
	 * 权限对象
	 */
	private ToaUumsBaseOperationPrivil privil;
	
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
	
	@Column(name="POST_ID")
	public String getPostId() {
		return postId;
	}
	
	public void setPostId(String postId) {
		this.postId = postId;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRIVIL_ID")
	public ToaUumsBaseOperationPrivil getPrivil() {
		return privil;
	}
	
	public void setPrivil(ToaUumsBaseOperationPrivil privil) {
		this.privil = privil;
	}

}
