package com.strongit.oa.im;

import java.util.List;

import com.strongit.oa.common.workflow.model.Task;

public interface IMMessageService {

	/**
	 * 对外发送即时消息接口
	 * @author:邓志城
	 * @date:2009-5-18 下午03:15:59
	 * @param message
	 * @param receiveId
	 * @throws Exception
	 */
	public abstract String sendIMMessage(String message,List<String> receivers,Task task)throws Exception	;
	public abstract String sendIMMessageBySender(String message,List<String> receivers,Task task,String curUser)throws Exception	;
	/**
	 * 获取是否启用Rtx的标识
	 * @author:邓志城
	 * @date:2009-6-5 上午09:21:01
	 * @return {true:启用;false:不启用}
	 * @throws Exception
	 */
	public boolean isEnabled()throws Exception;
	/**
	 * 对外发送即时消息接口
	 * @author:邓志城
	 * @date:2009-5-18 下午03:15:59
	 * @param message 消息内容
	 * @param receivers 消息接受者
	 * @param task
	 * @param SenderID 发送者ID
	 * <P>默认是当前用户发送,如果当前用户不存在，则有系统管理员发送</P>
	 * 	 * @return 
	 * 	<P>0:发送成功；-1：软件未启用；-2：异常</P>
	 * @throws Exception
	 */
	public String sendIMMessage(String message,List<String> receivers,Task task,String SenderID)throws Exception;
}
