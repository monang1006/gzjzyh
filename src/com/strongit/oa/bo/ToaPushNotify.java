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

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 用于保存推送给ios的消息统计记录
 * 1、在系统管理的推送设置那里增加了推送设置
 * 2、并且开启了推送
 * 3
 * @classname: ToaIOSPushNotify	
 * @author hecj
 * @date Jan 11, 2014 1:22:43 PM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.bo
 * @update
 */
@Entity
@Table(name="T_OA_PUSHNOTIFY")
public class ToaPushNotify {
	public ToaPushNotify(){
		
	}
	private String pID;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 模块编号
	 */
	private String  moduleNo;
	/**
	 * 推送消息的总数量
	 */
	private Integer messageCount;
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
	public String getPID() {
		return pID;
	}
	public void setPID(String pid) {
		pID = pid;
	}
	@Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name="MODULE_NO")
	public String getModuleNo() {
		return moduleNo;
	}
	public void setModuleNo(String moduleNo) {
		this.moduleNo = moduleNo;
	}
	
	@Column(name="MESSAGE_COUNT")
	public Integer getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(Integer messageCount) {
		this.messageCount = messageCount;
	}
	
	
}
