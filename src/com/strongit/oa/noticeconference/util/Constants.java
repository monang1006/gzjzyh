package com.strongit.oa.noticeconference.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {
	public static final String CONFERENCE_STATE_DRAFT="0"; //会议通知草稿
	public static final String CONFERENCE_STATE_ISSIED="1";//已发会议通知
	public static final String CONFERENCE_STATE_END="2";//已办结会议通知
	
	public static final String CONFERENCE_SEND_RECVSTATE_DEFAULT="0"; //会议通知下发 默认接收状态
	public static final String CONFERENCE_SEND_RECVSTATE_SIGN="1"; //会议通知下发 签收状态
	public static final String CONFERENCE_SEND_RECVSTATE_REPORTED="2"; //会议通知下发 已上报状态
	public static final String CONFERENCE_SEND_RECVSTATE_ACCEPTED="3"; //会议通知下发 受理状态
	public static final String CONFERENCE_SEND_RECVSTATE_REBACK="4"; //会议通知下发 返回重办状态
	 
	public static final String CONFERENCE_SEND_RECVSTATE_DEL="5"; ////会议通知下发 废除状态
	
	public static final String DEPT_REPORT_WAIT_STATE="0";//单位上报人员 待上报状态（待审核）
	public static final String DEPT_REPORT_ISSIED_STATE="1";//单位上报人员 已上报状态
	public static final String DEPT_REPORT_LEAVE_STATE="2";//单位上报人员 请假状态
 

	public static final Map CONFERENCE_SEND_STATE_MAP  = getConferenceSendReceStateMap(); 

	private static Map getConferenceSendReceStateMap() {
		Map<String, String> SEND_STATE_MAP = new LinkedHashMap();
		SEND_STATE_MAP.put(CONFERENCE_SEND_RECVSTATE_DEFAULT, "未签收");
		SEND_STATE_MAP.put(CONFERENCE_SEND_RECVSTATE_SIGN, "签收未报名");
		SEND_STATE_MAP.put(CONFERENCE_SEND_RECVSTATE_REPORTED, "已报名");
		SEND_STATE_MAP.put(CONFERENCE_SEND_RECVSTATE_ACCEPTED, "已处理");
		SEND_STATE_MAP.put(CONFERENCE_SEND_RECVSTATE_REBACK, "返回重办");
		SEND_STATE_MAP.put(CONFERENCE_SEND_RECVSTATE_DEL, "废除重办");
		return SEND_STATE_MAP;
	}

}
