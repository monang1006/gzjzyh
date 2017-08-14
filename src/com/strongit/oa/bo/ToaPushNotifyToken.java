/**  
* @title: ToaPushNotifyToken.java
* @package com.strongit.oa.bo
* @description: TODO
* @author  hecj
* @date Mar 23, 2014 11:21:58 AM
*/


package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 保存用户id与设备id之间的关系
 * @classname: ToaPushNotifyToken	
 * @author hecj
 * @date Mar 23, 2014 11:21:58 AM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.bo
 * @update
 */
@Entity
@Table(name="T_OA_PUSHNOTIFY_TOKEN")
public class ToaPushNotifyToken {
	public ToaPushNotifyToken(){
		
	}
	public ToaPushNotifyToken(String tokenId,String userId,String clientType){
		this.userId=userId;
		this.tokenId=tokenId;
		this.clientType=clientType;
	}
	
	private String pId;
	/**
	 * 令牌id
	 */
	private String tokenId;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 客户端类型
	 */
	private String clientType;
	@Id
	@Column(name="PID")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
	@GeneratedValue(generator="hibernate-uuid")
	public String getPId() {
		return pId;
	}
	public void setPId(String id) {
		pId = id;
	}
	
	@Column(name="TOKEN_ID")
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	@Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name="CLIENT_TYPE")
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
}
