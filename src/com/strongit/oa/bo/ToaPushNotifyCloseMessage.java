/**  
* @title: ToaPushNotifyModuleOpen.java
* @package com.strongit.oa.bo
* @description: TODO
* @author  hecj
* @date Mar 23, 2014 6:28:42 PM
*/


package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @classname: ToaPushNotifyModuleOpen	
 * @author hecj
 * @date Mar 23, 2014 6:28:42 PM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.bo
 * @update
 */
@Entity
@Table(name="T_OA_PUSHNOTIFY_CLOSEMESSAGE")
public class ToaPushNotifyCloseMessage {
	public ToaPushNotifyCloseMessage(){
		
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
	 * 开关状态 1:打开 0:关闭
	 */
	private String stateFlag;

	@Id
	@Column(name="PID")
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
	@Column(name="STATE_FLAG")
	public String getStateFlag() {
		return stateFlag;
	}
	public void setStateFlag(String stateFlag) {
		this.stateFlag = stateFlag;
	}
}
