package com.strongit.tag.web.remind;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.strongit.oa.common.remind.RemindManager;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.tag.web.util.ResponseUtils;
import com.strongmvc.service.ServiceLocator;

/**
 * 消息提醒标签类，用户生成可选的提醒方式
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class RemindTag extends TagSupport{

	/**
	 * 包含的提醒方式
	 */
	private String includeRemind ;
	/**
	 * 排除的提醒方式
	 */
	private String exceptRemind;

	/**
	 * 提醒服务类
	 */
	private RemindManager manager;

	/**
	 * 模块编码（手机短信对模块调用做了控制）
	 */
	private String code;
	
	/**
	 * 是否显示提醒
	 */
	private String isDisplayInfo;
	
	/**
	 * 是否显示提醒内容输入域
	 */
	private String isDisplayContent;
	
	/**
	 * 是否紧急获取提醒方式,不包括其他内容
	 */
	private String isOnlyRemindInfo;
	
	/**
	 * 内部消息提醒是否默认选中
	 */
	private String msgChecked;
	
	/**
	 * 电子邮件是否默认选中
	 */
	private String emailChecked;
	
	/**
	 * 手机短信是否默认选中
	 */
	private String smsChecked;
	
	/**
	 * Rtx是否默认选中
	 */
	private String rtxChecked;
	
	/**
	 * 默认的提醒内容
	 */
	private String defaultRemindContent;
	
	/**
	 * 提醒内容是否要加上时间戳
	 */
	private String isNeedTimeStamp;
	
	private String isShowButton ; //是否显示按钮

	/**
	 * 构造方法初始化服务类
	 */
	public RemindTag(){
		this.msgChecked   = "";
		this.emailChecked = "";
		this.rtxChecked   = "";
		this.smsChecked   = "";
		this.defaultRemindContent = "";
		this.manager      = (RemindManager)ServiceLocator.getService("remindManager");
		this.isShowButton = "";
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			String remindHtml = "";
			if("true".equals(isOnlyRemindInfo)){
				remindHtml = manager.getRemindInfoAfterChecked(includeRemind, 
															   exceptRemind,
															   code,
															   msgChecked,
															   emailChecked,
															   rtxChecked,
															   smsChecked);
			}else{
				//如果提醒内容需要加上时间戳
				if("true".equals(isNeedTimeStamp)){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
					Calendar c = Calendar.getInstance();
					defaultRemindContent = defaultRemindContent+"("+sdf.format(c.getTime())+")";
				}
				remindHtml = manager.getRemindHtml(includeRemind,
												   exceptRemind,
												   code,
												   isDisplayInfo,
												   isDisplayContent,
												   msgChecked,
												   emailChecked,
												   rtxChecked,
												   smsChecked,
												   defaultRemindContent,
												   isShowButton);
			}
			LogPrintStackUtil.logInfo("remind:"+remindHtml);
			ResponseUtils.write(pageContext, remindHtml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getIncludeRemind() {
		return includeRemind;
	}

	public void setIncludeRemind(String includeRemind) {
		this.includeRemind = includeRemind;
	}

	public String getExceptRemind() {
		return exceptRemind;
	}

	public void setExceptRemind(String exceptRemind) {
		this.exceptRemind = exceptRemind;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIsDisplayInfo() {
		return isDisplayInfo;
	}

	public void setIsDisplayInfo(String isDisplayInfo) {
		this.isDisplayInfo = isDisplayInfo;
	}

	public String getIsDisplayContent() {
		return isDisplayContent;
	}

	public void setIsDisplayContent(String isDisplayContent) {
		this.isDisplayContent = isDisplayContent;
	}

	public String getIsOnlyRemindInfo() {
		return isOnlyRemindInfo;
	}

	public void setIsOnlyRemindInfo(String isOnlyRemindInfo) {
		this.isOnlyRemindInfo = isOnlyRemindInfo;
	}

	public String getEmailChecked() {
		return emailChecked;
	}

	public void setEmailChecked(String emailChecked) {
		this.emailChecked = emailChecked;
	}

	public String getMsgChecked() {
		return msgChecked;
	}

	public void setMsgChecked(String msgChecked) {
		this.msgChecked = msgChecked;
	}

	public String getRtxChecked() {
		return rtxChecked;
	}

	public void setRtxChecked(String rtxChecked) {
		this.rtxChecked = rtxChecked;
	}

	public String getSmsChecked() {
		return smsChecked;
	}

	public void setSmsChecked(String smsChecked) {
		this.smsChecked = smsChecked;
	}

	public String getDefaultRemindContent() {
		return defaultRemindContent;
	}

	public void setDefaultRemindContent(String defaultRemindContent) {
		this.defaultRemindContent = defaultRemindContent;
	}

	public String getIsNeedTimeStamp() {
		return isNeedTimeStamp;
	}

	public void setIsNeedTimeStamp(String isNeedTimeStamp) {
		this.isNeedTimeStamp = isNeedTimeStamp;
	}

	public String getIsShowButton() {
		return isShowButton;
	}

	public void setIsShowButton(String isShowButton) {
		this.isShowButton = isShowButton;
	}

}
