package com.strongit.oa.meetingmanage.util;
/**
 * 会议管理常用的常量值
 * @author Administrator蒋国斌
 * StrongOA2.0 
 * 上午10:23:16
 */
public final class MeetingmanageConst {
	//议题状态
	public static final String UN_AUDIT ="0";//"未送审");
	public static final String AUDITING ="1";//审核中
	public static final String ISAUDIT ="2";//已审核
	public static final String USING="3";//占用中
	public static final String USING_OVER ="4";//已结束
	
	//会议通知发送状态
	public static final String IS_SEND ="1";//已发送
	public static final String UN_SEND ="0";//未发送
	
	//会议分类是否停用
	public static final String IS_DISABLE ="0";//已停用
	public static final String NO_DISABLE ="1";//未停用
	

}
