package com.strongit.oa.common.workflow;

import com.strongit.oa.message.MessageType;

/**
 * (系统流程类型|审批意见类型)常量
 * @author 邓志城
 *
 */
public final class WorkFlowTypeConst {

	/**
	 * 栏目类型
	 */
	@MessageType(name="栏目审批")
	public static final int COLUMN = 1;
	
	/**
	 * 发文处理
	 */
	@MessageType(name="待办公文")
	public static final int SENDDOC = 2;
	
	@MessageType(name="考勤申请")
	public static final int APPLYDOC = 14;
	
	@MessageType(name="销假申请")
	public static final int APPLYCANDOC = 15;
	
	/**
	 * 收文处理
	 */
	@MessageType(name="待办来文")
	public static final int RECEDOC = 3;
	
	/**
	 * 督察督办
	 */
	@MessageType(name="督察督办")
	public static final int INSPECT = 4;
	
	/**
	 * 会议管理
	 */
	@MessageType(name="会议审批")
	public static final int MEETING = 5;
	
	/**
	 * 提案建议
	 */
	@MessageType(name="提案建议")
	public static final int TIANJIANYI = 6;
	
	/**
	 * 信访管理
	 */
	@MessageType(name="信访管理")
	public static final int XINFANGGUANLI = 7;
	
	/**
	 * 值班管理
	 */
	@MessageType(name="值班管理")
	public static final int ZHIBANGUANLI = 8;
	
	/**
	 * 信息处理
	 */
	@MessageType(name="信息管理")
	public static final int XINXICHULI = 9;
	
	/**
	 * 新闻管理
	 */
	@MessageType(name="新闻管理")
	public static final int XINWENGUANLI = 10;
	
	/**
	 * 签报管理
	 */
	@MessageType(name="签报管理")
	public static final int QIANBAOGUANLI = 11;
	
	/**
	 * 档案管理
	 */
	@MessageType(name="档案管理")
	public static final int DANGDANGUANLI = 12;
	
	/**
	 * 发文状态(0:草稿;1:非草稿)
	 */
	public static final String SENDDOCDRAFT = "0";
	
	/**
	 * 发文状态(0:草稿;1:非草稿)
	 */
	public static final String SENDDOCDOCUMENT = "1";
	
	/**
	 * 督察督办状态(0:草稿;1:非草稿)
	 */
	public static final String INSPECTDRAFT = "0";
	
	/**
	 * 督察督办状态(0:草稿;1:非草稿)
	 */
	public static final String INSPECTDOCUMENT = "1";
	
	/**
	 * 收文状态(0:草稿;1:非草稿)
	 */
	public static final String RECVDOCDRAFT = "0";
	
	/**
	 * 收文状态(0:草稿;1:非草稿)
	 */
	public static final String RECVDOCDOCUMENT = "1";
	
	/***************审批意见类型常量******************/
	/**
	 * 发文审批意见所属字典类型
	 */
	public static final String SENDDOCIDEA = "FW_IDEA";
	/**
	 * 收文审批意见所属字典类型
	 */
	public static final String RECVDOCIDEA = "SW_IDEA";
	/**
	 * 工作处理审批意见所属字典类型
	 */
	public static final String WORKIDEA = "GZCL_IDEA";
	/**
	 * 信息发布审批处理意见字典类型
	 */
	public static final String ARTICLESAPPROIDEA = "XXFB_IDEA";
	

	//会议管理审批意见
	public static final String MEETINGIDEA="MEET_IDEA";
	

	//车辆申请审批意见
	public static final String CARIDEA = "CAR_IDEA";
	

	/***************督察督办我的意见类型常量******************/
	/**
	 * 督察督办我的意见
	 */
	public static final String INSPECTIDEA = "IN_IDEA";
	
	
	//IPP--OA通讯定义自动节点名称，这里采用一个代理类来代理所有的业务操作，通过自动节点名称来区分
	public static final String BSYSHTG = "办事员审核通过";
	public static final String BSYSHBTG = "办事员审核不通过";
	public static final String KSLDSHTG = "科室领导审批通过";
	public static final String KSLDSHBTG = "科室领导审批不通过";
	
}
