package com.strongit.oa.webservice.server.sms;

import java.util.List;

import org.apache.axis.utils.JavaUtils;

import com.strongit.oa.bo.ToaSms;
import com.strongit.oa.sms.SmsManager;
import com.strongit.oa.sms.SmsService;
import com.strongit.oa.util.Dom4jUtil;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.service.ServiceLocator;

public class SmsWebService {

	private SmsService smsService;
	private SmsManager smsManager;
	
	public SmsWebService(){
		smsService = (SmsService)ServiceLocator.getService("smsService");
		smsManager  = (SmsManager)ServiceLocator.getService("smsManager");
	}
	
	/**
	 * author:luosy
	 * description:   短信发送  通过webservice调用的接口
	 * modifyer:
	 * description:
	 * @param sender 发送人
	 * @param recvNum 接收号码 
	 * @param smsCon 短信内容
	 * @param moduleId 模块id
	 * @return
	 */
	public String sendSmsbyWS(String sender,String recvNum, String smsCon, String moduleId){
		Dom4jUtil d4u = new Dom4jUtil();
		String retStr = "";
		
		
		try{
			
			if(null!=recvNum){
				if(!recvNum.matches("\\d+")){
					return d4u.createItemResponseData("0", "\n短信未保存：手机号码只能为数字！", "string", "");
				}
			}
			
			if("".equals(smsCon)|null==smsCon){
				return d4u.createItemResponseData("0", "\n短信未保存：短信内容不能为空！", "string", "");
			}
			
			String str = smsCon.replaceAll("\\w", "");
			int  length = str.length() + (smsCon.length()-str.length()+1)/2;
			if(length>250){
				return d4u.createItemResponseData("0", "\n短信未保存：短信内容过长，字数超过存储范围！", "string", "");
			}

			if(smsService.isModuleParaOpen(moduleId)){
				retStr = smsManager.sendSmsByWS(sender, recvNum, smsCon, moduleId);
				return d4u.createItemResponseData("1", null, "string", retStr);
			}else{
				return d4u.createItemResponseData("0", "模块功能没有开启", "string", "");
			}
		}catch(Exception e){
			e.printStackTrace();
			return d4u.createItemResponseData("0", "短信保存异常:"+retStr+"\n"+JavaUtils.stackToString(e), "string", "");
		}
	}
	
	/**
	 * author:luosy
	 * description: 获取短信列表 通过WEBSERVICES 调用
	 * modifyer:
	 * description:
	 * @param sender 发送人
	 * @param recvNum 接受人号码
	 * @param smsCon 短信内容
	 * @param moduleId 模块ID
	 * @return
	 */
	public String getSmsListByWS(String sender, String recvNum, String smsCon, String moduleId){
		Dom4jUtil d4u = new Dom4jUtil();
		try{
			List<ToaSms> smsList = smsManager.getSmsListByWS(sender.trim(), recvNum.trim(), smsCon.trim(), moduleId.trim());
			return d4u.createListResponseData(smsList, "1", null);
		}catch(Exception e){
			e.printStackTrace();
			return d4u.createItemResponseData("0", "短信列表查询异常:\n"+JavaUtils.stackToString(e), "string", "");
		}
	}
}
