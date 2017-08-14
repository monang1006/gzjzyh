package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 扩展任务实例信息
 * 
 * @author yanjian
 * 
 * Nov 23, 2012 8:56:21 PM
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_JBPM_TASKEXTEND")
public class TJbpmTaskExtend implements Serializable {
	/**
	 * @field ACTION_ZHUBANBIANGENG 主办变更标识常量
	 */
	public final static String ACTION_ZHUBANBIANGENG = "主办变更";

	/**
	 * @field ACTION_ZHIPAI 指派标识常量
	 */
	public final static String ACTION_ZHIPAI = "指派";

	/**
	 * @field ACTION_ZHIPAIDFANHUI 指派返回
	 */
	public final static String ACTION_ZHIPAIDFANHUI = "指派返回";

	/**
	 * 匹配Action类型
	 * 
	 * @author yanjian
	 * @param type
	 * @return
	 * Nov 23, 2012 9:44:34 PM
	 */
	public static boolean matchActionType(String type){
		
		return (
				  type.equals(ACTION_ZHUBANBIANGENG)
				||type.equals(ACTION_ZHIPAI)
				||type.equals(ACTION_ZHIPAIDFANHUI)
				);
		
	}
	
	/**
	 * 获取匹配失败信息
	 * 
	 * @author yanjian
	 * @return
	 * Nov 23, 2012 9:48:03 PM
	 */
	public static String getMatchActionTypeErrorMessage(){
		return new StringBuilder("Action 必须为：")
				.append("\r\n1、TJbpmTaskExtend.ACTION_ZHUBANBIANGENG ")
				.append("\r\n2、TJbpmTaskExtend.ACTION_ZHIPAI ")
				.append("\r\n3、TJbpmTaskExtend.ACTION_ZHIPAIDFANHUI ")
				.toString();
	}
	
	/**
	 * @field id 主键
	 */
	private String id;

	/**
	 * @field taskInstanceId 任务实例id
	 */
	private Long taskInstanceId;

	/**
	 * @field action 流转动作
	 */
	private String action;

	/**
	 * @field fromUserId 动作发起者id
	 */
	private String fromUserId;

	/**
	 * @field fromUserName 动作发起者名称
	 */
	private String fromUserName;

	/**
	 * @field toUserId 动作接受者id
	 */
	private String toUserId;

	/**
	 * @field toUserName 动作接受者名称
	 */
	private String toUserName;

	/**
	 * @field createDate 记录产生时间
	 */
	private Date createDate;

	/**
	 * @field remark 备注（建议使用JSON格式存放数据）
	 */
	private String remark;

	@Column(name = "ACTION_", nullable = true)
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Column(name = "CREATEDATE_", nullable = true)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "FROMUSERID_", nullable = true, length = 32)
	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	@Column(name = "FROMUSERNAME_", nullable = true)
	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	@Id
	@Column(name = "ID_", nullable = false, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "REMARK_", nullable = true)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "TASKINSTANCEID_", nullable = true)
	public Long getTaskInstanceId() {
		return taskInstanceId;
	}

	public void setTaskInstanceId(Long taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}

	@Column(name = "TOUSERID_", nullable = true, length = 32)
	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	@Column(name = "TOUSERNAME_", nullable = true)
	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

}
