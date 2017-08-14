package com.strongit.oa.common.remind;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.strongit.oa.address.AddressManager;
import com.strongit.oa.common.workflow.model.Task;
import com.strongit.oa.im.IMMessageService;
import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.message.IMessageService;
import com.strongit.oa.mymail.IMailService;
import com.strongit.oa.sms.IsmsService;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 消息提醒类
 * 此类提供了邮件、手机短信、内部消息、Rtx等提醒手段
 * @author 邓志城
 *@classpath    com.strongit.oa.common.remind.RemindManager
 */
@Service
@Transactional
public class RemindManager {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 邮件提醒服务
	 */
	private IMailService mailService;
	/**
	 * 手机短信提醒服务
	 */
	private IsmsService smsService;
	/**
	 * 内部消息提醒服务
	 */
	private IMessageService messageService;
	/**
	 * Rtx提醒服务
	 */
	private IMMessageService imService;
	
	/**
	 * 个人通讯录服务（用户获取是否是否配置默认邮箱）
	 */
	private AddressManager addressService;
	/**
	 * 标示是个人还是系统发送
	 * 如果为null则默认为人工发送,为“system”则为系统发送
	 */
	private String sendFlag;
	

	/**
	 * 工作流流转过程中发送用户自定义消息提醒
	 * @author:邓志城
	 * @date:2009-5-18 下午03:54:20
	 * @throws ServiceException
	 * @throws SystemException
	 * 
	 * 修改：注释掉
	 *  session.remove("remindType");
		session.remove("handlerMes");
		session.remove("moduleType");
		当会签时，任务开始会根据人员个数进入若干次，因此不能执行完
		这段方法后清理SESSION中数据.
	   时间：2010年9月20日10:09:36
	 */
	@SuppressWarnings("unchecked")
	public void sendRemind(List<String> receivers,Task task)throws ServiceException,SystemException{
		ActionContext cxt = ActionContext.getContext();
		if(cxt == null){
			return ;
		}
		Map<String, Object> session = cxt.getSession();
		//获取提醒方式
		String remindType = (String)session.get("remindType");
		//获取提醒内容
		String handlerMes = (String)session.get("handlerMes");
		//调用消息的模块
		String moduleType = (String)session.get("moduleType");
		//至少选择了一种提醒方式
		if(null!=remindType && !"".equals(remindType) && receivers!=null && !receivers.isEmpty()){
			String[] remindTypes = remindType.split(",");
			for(String type:remindTypes){
				if(Constants.EMAIL.equals(type)){//邮件提醒
					try {
						mailService.autoSendMail(receivers, handlerMes, handlerMes, "person");
						logger.info("邮件提醒发送成功。");
					} catch (Exception e) {
						logger.info("发送邮件提醒发生异常。",e);
					}
				}else if(Constants.MSG.equals(type)){//内部消息提醒
					messageService.sendMsg("person", receivers, handlerMes, handlerMes,moduleType);
					logger.info("发送内部消息提醒成功！"+new Date());
				}else if(Constants.RTX.equals(type)){//Rtx提醒
					try {
						imService.sendIMMessage(handlerMes, receivers,task);
						logger.info("发送即时消息提醒成功！"+new Date());
					} catch (Exception e) {
						logger.error("发送即时消息提醒出错！"+new Date(),e);
					}
				}else if(Constants.SMS.equals(type)){//手机短信提醒
					smsService.sendSms("person", receivers, handlerMes);
					logger.info("发送手机短信提醒成功！"+new Date());
				}else{
					//nothing
				}
			}
		}
		/*session.remove("remindType");
		session.remove("handlerMes");
		session.remove("moduleType");*/
	}

	/**
	 * 获取提醒方式的HTML内容
	 * @author:邓志城
	 * @date:2009-6-5 下午05:00:59
	 * @param includeRemind 如果不为null，则表示自定义包含哪些提醒，否则显示所有提醒
	 * @param exceptRemind 暂时没用
	 * @param code 手机短信中对模块的控制，表示模块编码
	 * @param String isDisplayInfo 是否显示整个DIV {true:显示；false：不显示}
	 * @param String isDisplayContent 是否显示提醒内容输入框 {true：显示：false：不显示}
	 * @param String defaultRemindContent 默认的提醒内容
	 * @param String isShowButton 是否显示操作按钮
	 * @return
	 * @throws Exception
	 */
	public String getRemindHtml(String includeRemind,
								String exceptRemind,
								String code,
								String isDisplayInfo,
								String isDisplayContent,
								String MsgChecked,
								String EmailChecked,
								String RtxChecked,
								String SmsChecked,
								String defaultRemindContent,
								String isShowButton)throws Exception{
		StringBuffer html = new StringBuffer("");
		if(includeRemind!=null&&exceptRemind!=null){
			throw new SystemException("暂不支持同时使用includeRemind和exceptRemind！");
		}
		html.append("<div id=\"msg\" style=\"padding: 0px 10px 10px 10px;");
		if("false".equals(isDisplayInfo)){
			html.append("display:none;\" >");
		}else{
			html.append("\">");
		}
		String remindInfoAfterChecked= getRemindInfoAfterChecked(includeRemind, exceptRemind, code,MsgChecked,EmailChecked,RtxChecked,SmsChecked);
		//yanjian 2012-04-16 获取启用状态下的所有提醒方式 如果不存在相关提醒方式，对应的操作区域不显示
		boolean ishide = ((remindInfoAfterChecked == null || remindInfoAfterChecked.equals(""))?true:false);
		html.append("	<fieldset "+(ishide?"style=\"display:none;\"":"")+">") 
			.append("		<legend style=\"font-size: 14px;font-family:宋体\"> ")
			.append("			请选择") 
			.append("		</legend>") ;
		if(ishide){
			html.append("		<div style=\"padding: 10px 10px 10px 10px;display:none;\">提醒方式：") ;
//			html.append("		<div style=\"padding: 10px 10px 10px 10px;display:none;\">提醒方式：") ;
		}else{
			html.append("		<div style=\"padding: 10px 10px 10px 10px;font-size: 14px;font-family:宋体\">提醒方式：") ;
//			html.append("		<div style=\"padding: 10px 10px 10px 10px;\">提醒方式：") ;
		}
			html.append(remindInfoAfterChecked);
			html.append("</br><span id='xiha'></span>");
			html.append("		<table width=96%>");
			if("false".equals(isDisplayContent)){
				html.append("			<tr id=\"remind\"  style=\"display:none;\">") 
					//.append("				<td width='15%'>提醒内容：</td>") 
					.append("				<td aligh='left'>")
					.append("				<textarea id=\"handlerMes\" style=\"width: 99%%\" rows=\"3\" name=\"handlerMes\" >"+defaultRemindContent+"</textarea>")
					.append("				</td>")
					.append("			</tr>");
			}else{
				html.append("			<tr id=\"remind\">") 
					//.append("				<td width='15%'>提醒内容：</td>") 
					.append("				<td aligh='left'>")
				    .append("				<textarea id=\"handlerMes\" style=\"width: 99%\" rows=\"5\"  name=\"handlerMes\" >"+defaultRemindContent+"</textarea>")
					.append("				</td>")
					.append("			</tr>");
			}
			html.append("		</table>");
			if(!"false".equals(isShowButton)){
				html.append("		<input type='button' name='btnEdit' id='btnEdit' class=\"input_bg\" onclick='editRemindTxt();' value='显示编辑区域'>");
			}
			
				html.append("	</div>")
			.append("	<div id=\"timerRemind\" style=\"padding: 10px 10px 10px 10px;\">")
			.append("	</div>")
			.append("	</fieldset>")
			.append("	<script>function checkRemindCont(){")
			.append("			var len = $('#handlerMes').val().length;")
			.append("			if(len>500){")
			.append("				$('#handlerMes').val($('#handlerMes').val().substring(0,500));")
			.append("				alert(\"输入提醒内容过长，请控制在500字以内！\");")
			.append("				return false;")
			.append("			}else{return true;}")
			.append("}")
			.append("			function editRemindTxt(){")
			.append("				if($('#remind').is(':hidden')){")
			.append("                	$('#remind').show();")
			.append("                	$('#btnEdit').val('隐藏编辑区域');")
			.append("				}else{")
			.append("					$('#remind').hide();")
			.append("                	$('#btnEdit').val('显示编辑区域');}")
			.append("			}")
			.append("	</script>")
			.append("</div>");
			System.out.println(html.toString());
		return html.toString();
	}

	/**
	 * 获取启用状态下的所有提醒方式,对于短信提醒还需要验证
	 * 调用模块是否有发送短信权限。
	 * @author:邓志城
	 * @date:2009-6-8 下午02:55:00
	 * @param includeRemind 可选的提醒方式
	 * @param exceptRemind 过滤的提醒方式
	 * @param code 发送短信的模块编码
	 * @return 短信提醒方式
	 * @throws Exception
	 */
	public String getRemindInfoAfterChecked(String includeRemind,
											String exceptRemind,
											String code,
											String MsgChecked,
											String EmailChecked,
											String RtxChecked,
											String SmsChecked)throws Exception{
//		短信功能是否启用
		boolean isSMSEnabled = smsService.isSMSOpen(code);
		//邮件功能是否启用
		boolean isEmailEnabled = mailService.isMailUseable();
		//Rtx功能是否启用
		boolean isRtxEnabled = imService.isEnabled();
		StringBuffer html = new StringBuffer("");
		if(includeRemind!=null){
			String[] reminds = includeRemind.split(",");
			if(isRemindExist(reminds)){//提醒类型存在
				for(String remind:reminds){
					if(remind.equals(Constants.EMAIL)){
						if(isEmailEnabled){
							if(isConfigDefaultEmail()){//用户配置了个人邮箱
								html.append("	<br/><input type=\"checkbox\" "+EmailChecked+" name=\"mail\" value=\""+Constants.EMAIL+"\">"+"<span class='wz'>"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_EMAIL, ActionContext.getContext().getLocale()));
							}else{
								html.append("	<br/><input type=\"checkbox\" "+EmailChecked+" name=\"mail\" title=\"系统检测到您没有配置默认邮箱，请在个人信息里配置。\" disabled value=\""+Constants.EMAIL+"\">"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_EMAIL, ActionContext.getContext().getLocale()));
							}
						}
					}else if(remind.equals(Constants.MSG)){
							html.append("	<br/><input type=\"checkbox\" "+MsgChecked+" name=\"message\" value=\""+Constants.MSG+"\">"+"<span class='wz'>"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_MESSAGE, ActionContext.getContext().getLocale())) ;
						//取消内部消息提醒方式
						html.append("	<br/>");
					}else if(remind.equals(Constants.SMS)){
						if(isSMSEnabled){
							html.append("	<br/><input type=\"checkbox\" "+SmsChecked+" name=\"sms\" value=\""+Constants.SMS+"\">"+"<span class='wz'>"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_SMS, ActionContext.getContext().getLocale())+"</span>");		
						}
					}else if(remind.equals(Constants.RTX)){
						if(isRtxEnabled){
							html.append("	<br/><input type=\"checkbox\" "+RtxChecked+" name=\"rtx\" value=\""+Constants.RTX+"\">"+"<span class='wz'>"+Cache.get().getRest1()+"</span>") ;
						}
					}
				}
			}
		}else{
			if(exceptRemind==null){
				if(isRtxEnabled){
					html.append("	<br/><input type=\"checkbox\" "+RtxChecked+" name=\"rtx\" value=\""+Constants.RTX+"\">"+"<span class='wz'>"+Cache.get().getRest1()+"</span>") ;
				}	
				
				html.append("		<br/><input type=\"checkbox\" "+MsgChecked+" name=\"message\" value=\""+Constants.MSG+"\">"+"<span class='wz'>"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_MESSAGE, ActionContext.getContext().getLocale())+"</span>") ;
				//取消内部消息提醒方式
				html.append("	");
				if(isSMSEnabled){
					html.append("	<br/><input type=\"checkbox\" "+SmsChecked+" name=\"sms\" value=\""+Constants.SMS+"\">"+"<span class='wz'>"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_SMS, ActionContext.getContext().getLocale())+"</span>");				
				}
				if(isEmailEnabled){
					if(isConfigDefaultEmail()){//用户配置了个人邮箱
						html.append("	<br/><input type=\"checkbox\" "+EmailChecked+" name=\"mail\" value=\""+Constants.EMAIL+"\">"+"<span class='wz'>"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_EMAIL, ActionContext.getContext().getLocale())+"</span>");
					}else{
						html.append("	<br/><input type=\"checkbox\" "+EmailChecked+" name=\"mail\" title=\"系统检测到您没有配置默认邮箱，请在个人信息里配置。\" disabled value=\""+Constants.EMAIL+"\">"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_EMAIL, ActionContext.getContext().getLocale()));
					} 
				}
							
			}
		}
		if(exceptRemind!=null){
			String[] reminds = exceptRemind.split(",");
			if(isRemindExist(reminds)){
				String[] sysReminds = Constants.REMINDARRAY;
				for(String sysRemind:sysReminds){
					if(!isExistInArray(reminds, sysRemind)){
						if(sysRemind.equals(Constants.EMAIL)){
							if(isEmailEnabled){
								if(isConfigDefaultEmail()){//用户配置了个人邮箱
									html.append("	<br/><input type=\"checkbox\" "+EmailChecked+" name=\"mail\" value=\""+Constants.EMAIL+"\">"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_EMAIL, ActionContext.getContext().getLocale()));
								}else{
									html.append("	<br/><input type=\"checkbox\" "+EmailChecked+" name=\"mail\" title=\"系统检测到您没有配置默认邮箱，请在个人信息里配置。\" disabled value=\""+Constants.EMAIL+"\">"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_EMAIL, ActionContext.getContext().getLocale()));
								}
							}
						}else if(sysRemind.equals(Constants.MSG)){
							html.append("	<br/><input type=\"checkbox\" "+MsgChecked+" name=\"message\" value=\""+Constants.MSG+"\">"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_MESSAGE, ActionContext.getContext().getLocale())) ;
						}else if(sysRemind.equals(Constants.SMS)){
							if(isSMSEnabled){
								html.append("	<br/><input type=\"checkbox\" "+SmsChecked+" name=\"sms\" value=\""+Constants.SMS+"\">"+LocalizedTextUtil.findDefaultText(GlobalBaseData.REMIND_SMS, ActionContext.getContext().getLocale()));		
							}
						}else if(sysRemind.equals(Constants.RTX)){
							if(isRtxEnabled){
								html.append("	<br/><input type=\"checkbox\" "+RtxChecked+" name=\"rtx\" value=\""+Constants.RTX+"\">"+Cache.get().getRest1()) ;
							}
						}
					}
				}
			}
		}
		return html.toString();
	}
	
	/**
	 * 判断提醒类型是否存在
	 * @author:邓志城
	 * @date:2009-6-5 下午04:50:21
	 * @param reminds
	 * @return
	 */
	private boolean isRemindExist(String[] reminds){
		String[] sysReminds = Constants.REMINDARRAY;
		List<String> lst = Arrays.asList(sysReminds);
		for(String remind:reminds){
			if(!lst.contains(remind)){ 
				throw new SystemException("提醒方式【"+remind+"】未定义！");
			}
		}
		return true;
	}

	/**
	 * 判断一个元素是否在数组中存在
	 * @author:邓志城
	 * @date:2009-6-6 上午10:35:12
	 * @param array
	 * @param element
	 * @return
	 */
	private boolean isExistInArray(String[] array,String element){
		for(String src:array){
			if(element.equals(src)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 如果是个人发送邮件,判断用户是否配置了默认邮箱
	 * @author:邓志城
	 * @date:2009-6-12 上午10:03:09
	 * @return
	 */
	private boolean isConfigDefaultEmail(){
		String defaultEmail = "";
		try{
			defaultEmail = addressService.getUserDefaultEmail("");
		}catch(Exception e){
			LogPrintStackUtil.logExceptionInfo(e, "获取用户默认邮箱异常！");
		}
		if(sendFlag == null){
			return defaultEmail==null?false:true;			
		}else if("system".equals(sendFlag)){
			return true;			
		}else{
			throw new SystemException("发送标示错误！");
		}
	}
	
	@Autowired
	public void setImService(IMMessageService imService) {
		this.imService = imService;
	}
	
	@Autowired
	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}
	
	@Autowired
	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}
	
	@Autowired
	public void setSmsService(IsmsService smsService) {
		this.smsService = smsService;
	}

	@Autowired
	public void setAddressService(AddressManager addressService) {
		this.addressService = addressService;
	}

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}
}
