/**  
* @title: ToaIOSPushNotify.java
* @package com.strongit.oa.bo
* @description: TODO
* @author  hecj
* @date Jan 11, 2014 1:22:43 PM
*/


package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 *用于给手机加固
 * @classname: ToaIMEI
 * @author hecj
 * @date Jan 11, 2014 1:22:43 PM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.bo
 * @update
 */
@Entity
@Table(name="T_OA_IMEI")
public class ToaIMEI {
	private String imeiId;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * imei码
	 */
	private String  iemiCode;
	/**
	 * 开启状态
	 */
	private String isOpen;
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
	public String getImeiId() {
		return imeiId;
	}
	public void setImeiId(String imeiId) {
		this.imeiId = imeiId;
	}
	@Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name="IMEI_CODE")
	public String getIemiCode() {
		return iemiCode;
	}
	public void setIemiCode(String iemiCode) {
		this.iemiCode = iemiCode;
	}
	
	@Column(name="ISOPEN")
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	@Column(name="USER_NAME")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
