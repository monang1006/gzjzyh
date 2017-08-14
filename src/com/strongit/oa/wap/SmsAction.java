/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理短信action跳转类
 */
package com.strongit.oa.wap;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongmvc.webapp.action.BaseActionSupport;
import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaSms;
import com.strongit.oa.bo.ToaSmsRep;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.sms.SmsManager;
import com.strongit.oa.smsplatform.SmsPlatformManager;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.orm.hibernate.Page;

/**
 * 手机短信
 * 
 * @author luosy
 * @version 1.0
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "sms.action", type = ServletActionRedirectResult.class),
			@Result(name = "sendSms", value = "/WEB-INF/jsp/address/address-sendSms.jsp", type = ServletDispatcherResult.class)})
public class SmsAction extends BaseActionSupport<ToaSms> {

	private Page<ToaSms> page = new Page<ToaSms>(20, true);

	private IUserService user;
	
	private String smsId;
	
	private String recvUserIds;
	
	private String receiverNames;
	
	private String senderName;
	
	private String inputType;
	
	@Autowired private SmsPlatformManager platformManager;	//wap开发新增 
	
	private String moduleCode;								//wap开发新增
	
	private String message;									//wap开发新增
	
	private String orgId;									//wap开发新增
	
	private String userName;								//wap开发新增
	
	private int currentPage;								//wap开发新增
	
	private String smsSendDelayTime;						//wap开发新增
	
	private List addNum;
	
	private String ownerNum;

	private ToaSms model = new ToaSms();
	
	private ToaSmsRep repSms = new ToaSmsRep();

	private SmsManager manager;
	
	private String needRep;//是否需要回复
	
	
	private ToaBussinessModulePara modulePara;//对应模块配置
	
	private List means;	//短信回复信息{同意，不同意……}
	
	private HashMap<String, String> typemap = new HashMap<String, String>();

	private HashMap<String, String> statemap = new HashMap<String, String>();
	
	public ToaSms getModel() {
		return model;
	}
	
	public ToaSmsRep getRepSms() {
		return repSms;
	}

	public void setRepSms(ToaSmsRep repSms) {
		this.repSms = repSms;
	}

	@Autowired
	public void setManager(SmsManager aManager) {
		manager = aManager;
	}
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	
	public Page getPage() {
		return page;
	}
	
	public void setSmsId(java.lang.String aSmsId) {
		smsId = aSmsId;
	}
	
	public String getReceiverNames() {
		return receiverNames;
	}

	public void setReceiverNames(String receiverNames) {
		this.receiverNames = receiverNames;
	}

	public String getRecvUserIds() {
		return recvUserIds;
	}

	public void setRecvUserIds(String recvUserIds) {
		this.recvUserIds = recvUserIds;
	}
	
	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	
	public void setAddNum(List addNum) {
		this.addNum = addNum;
	}

	public void setOwnerNum(String ownerNum) {
		this.ownerNum = ownerNum;
	}
	
	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getNeedRep() {
		return needRep;
	}

	public void setNeedRep(String needRep) {
		this.needRep = needRep;
	}

	public List getMeans() {
		return means;
	}

	public void setMeans(List means) {
		this.means = means;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public ToaBussinessModulePara getModulePara() {
		return modulePara;
	}

	public void setModulePara(ToaBussinessModulePara modulePara) {
		this.modulePara = modulePara;
	}

	public HashMap getStatemap() {
		return statemap;
	}

	public HashMap getTypemap() {
		return typemap;
	}

	public SmsAction() {
		//发送返回状态
		typemap.put("0", "未发送");
		typemap.put("null", "未发送");
		typemap.put(null, "未发送");
		typemap.put("1", "发送成功");
		typemap.put("2", "发送失败");
		
		//发送状态
		statemap.put("0", "未发送");
		statemap.put("1", "已发送");
		
	}

	/**
	 * author:luosy
	 * description: 获取当前用户的短信发送记录
	 * modifyer:
	 * description:
	 * @return
	 */
	public String list() throws Exception {
		page = manager.getListByUser(page);
		inputType = "list";
		return "list";
	}

	/**
	 * author:luosy
	 * description: 保存发送短信
	 * modifyer:
	 * description:
	 * @return
	 */
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		model.setModelCode(moduleCode);
		model.setModelName(manager.getModuleParaByCode(moduleCode).getBussinessModuleName());
		
		if("1".equals(needRep)){//需要回复
			model = manager.setSmsRep(model,means,moduleCode);//添加设置回复答案
			manager.saveSmsRep(model);
		}
		
		manager.saveSms(recvUserIds,model,ownerNum, new OALogInfo("手机短信-短信模块-『发送』："+model.getSmsCon()));
		return null;
	}

	/**
	 * author:luosy
	 * description: 删除短信历史发送记录
	 * modifyer:
	 * description:
	 * @return
	 */
	public String delete() throws Exception {
		try {
			
			if ("".equals(smsId) | null == smsId) {
			} else {
				String[] ids = smsId.split(",");
				for (int i = 0; i < ids.length; i++) {
					ToaSms sms = manager.getSmsById(ids[i]);
					manager.delSms(sms, new OALogInfo("手机短信-发送记录-『删除』：("+sms.getSmsRecvnum()+")"+sms.getSmsCon()));
				}
			}
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	protected void prepareModel() throws Exception {
		if ("".equals(smsId) | null == smsId) {
			model = new ToaSms();
		} else {
			model = manager.getSmsById(smsId);
		}

	}

	/**
	 * author:luosy
	 * description: 跳转到发送页面 
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String input() throws Exception {
		//		通讯录下选用户发消息
		if(!"".equals(recvUserIds)&&null!=recvUserIds){
			receiverNames = "";
			String[] recvIds = recvUserIds.split(",");
			for(int i = 0;i<recvIds.length;i++){
				receiverNames += user.getUserNameByUserId(recvIds[i])+",";
			}
			if(!"".equals(receiverNames)&&null!=receiverNames){
				receiverNames = receiverNames.substring(0, receiverNames.length()-1);
			}
		}
		
		//取出默认短信回复答案
		means = manager.getModulStateMean(moduleCode);
		//短信模块配置
		modulePara = manager.getModuleParaByCode(moduleCode);
		
		return "send";
	}

	/**
	 * author:luosy
	 * description: 重新发送短信
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String reSend()throws Exception{
		ToaSms sms = manager.getSmsById(smsId);
		model.setSmsCon(sms.getSmsCon().replaceAll("\\t|\\n|\\r|\\f", " ").replaceAll("'", "‘"));
		model.setSmsRecvId(sms.getSmsRecvId());
		model.setSmsRecver(sms.getSmsRecver());
		model.setSmsRecvnum(sms.getSmsRecvnum());
		
//		if(!"".equals(sms.getIncreaseCode())&&null!=sms.getIncreaseCode()){
//			means = manager.getStateMean(moduleCode,sms.getIncreaseCode());
//		}else{
//			//取出默认短信回复答案
//			means = manager.getModulStateMean(moduleCode);
//		}
//		//短信模块配置
//		modulePara = manager.getModuleParaByCode(moduleCode);
		
//		取出默认短信回复答案
		means = manager.getModulStateMean(moduleCode);
//		短信模块配置
		modulePara = manager.getModuleParaByCode(moduleCode);
		return "send";
	}
	
	/**
	 * author:luosy
	 * description: 批量重新发送短信
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String reSends()throws Exception{
		String[] smsid = smsId.split(",");
		for(int i=0;i<smsid.length;i++){
			ToaSms sms = manager.getSmsById(smsid[i]);
			sms.setSmsSendTime(new Date());
			sms.setSmsServerRet(ToaSms.SERVICE_RET_UNSEND);
			sms.setSmsState(ToaSms.SMS_UNSEND);
			manager.saveSms(sms);
		}
		renderText("succ");
		return null;
	}
	/**
	 * author:luosy
	 * description: 查看短信
	 * modifyer:
	 * description:
	 * @return 
	 */
	public String view() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		if (smsId != null && !"".equals(smsId)) {
			model = manager.getSmsById(smsId);
			senderName = user.getUserNameByUserId(model.getSmsSendUser());
			if(null==senderName){
				senderName = model.getSmsSenderName();
			}
			return "view";
		} else {
			return RELOAD;
		}
	}
	
	/**
	 * author:luosy
	 * description: 查看所有短信发送记录
	 * modifyer:
	 * description:
	 * @return
	 */
	public String allList() throws Exception {
		page = manager.getAllList(page);
		inputType = "allList";
		return "list";
	}

	/**
	 * author:luosy
	 * description: 根据条件查找 短信记录
	 * modifyer:
	 * description:
	 * @return
	 */
	public String search() throws Exception {
		try {
			if(model.getSmsRecver()!=null&&!"".equals(model.getSmsRecver())){
				model.setSmsRecver(URLDecoder.decode(model.getSmsRecver(), "utf-8"));
			}
			if(model.getSmsRecvnum()!=null&&!"".equals(model.getSmsRecvnum())){
				model.setSmsRecvnum(URLDecoder.decode(model.getSmsRecvnum(), "utf-8"));
			}
			if(model.getSmsCon()!=null&&!"".equals(model.getSmsCon())){
				model.setSmsCon(URLDecoder.decode(model.getSmsCon(), "utf-8"));
			}
			if(model.getModelName()!=null&&!"".equals(model.getModelName())){
				model.setModelName(URLDecoder.decode(model.getModelName(), "utf-8"));
			}
			if(model.getSmsSenderName()!=null&&!"".equals(model.getSmsSenderName())){
				String ssn = URLDecoder.decode(model.getSmsSenderName(), "utf-8");
				if("undefined".equals(ssn)){
					model.setSmsSenderName("");
				}else{
					model.setSmsSenderName(ssn);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		page = manager.searchList(page, model,inputType);
		return "list";
	}

	/*************************wap开发*********************************/
	public String wapInput() throws Exception {
		String isopen = platformManager.isModuleParaOpen(moduleCode);
		if(moduleCode==null||"".equals(moduleCode)){		//上导航的短信功能
			isopen="1";
		}
		if("1".equals(isopen)){
			//通讯录下选用户发消息
			String temp="";
			if(!"".equals(recvUserIds)&&null!=recvUserIds){
				receiverNames = "";
				String[] recvIds = recvUserIds.split(",");
				for(int i = 0;i<recvIds.length;i++){
					temp+=","+recvIds[i].trim();
					receiverNames += user.getUserNameByUserId(recvIds[i].trim())+",";
				}
				if(!"".equals(receiverNames)&&null!=receiverNames){
					receiverNames = receiverNames.substring(0, receiverNames.length()-1);
				}
				recvUserIds=temp.substring(1);
			}
		}else if("0".equals(isopen)){
			message="该模块状态为关闭，请在短信平台设置中开启！";	
		}else if("no".equals(isopen)){
			message="该模块未配置短信发送，请在短信平台设置中添加！";
		}else{
			message="不能发送短信，请联系管理员！";
		}
		return "sendSms";
	}
	
	/*
	 * 
	 * Description:发送短信
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Dec 22, 2010 2:52:15 PM
	 */
	public String wapSave() throws Exception {
		if((recvUserIds==null||"".equals(recvUserIds))&&(ownerNum==null||"".equals(ownerNum))){
			message="请选择接收人！";
			return "sendSms";
		}
		if(!manager.checkIsTel(ownerNum)){
			message="自定义接收号码格式不对\n请确认后重新输入！";
			return "sendSms";
		}
		String content=model.getSmsCon();
		if(content==null||"".equals(content)){
			message="请输入短信内容！";
			return "sendSms";
		}else if(content.length()>200){
			message="短信内容超过字数限制!";
			return "sendSms";
		}
		if(smsSendDelayTime!=null&&!"".equals(smsSendDelayTime)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try{
				Date date=sdf.parse(smsSendDelayTime);
				model.setSmsSendDelay(date);
			}catch(Exception e){
				message="指定发送时间有误，正确格式为2000-01-01 01:00:00!";
				return "sendSms";
			}
		}
		if(moduleCode!=null&&!"".equals(moduleCode)){		//上导航的短信功能
			model.setModelCode(moduleCode);
			model.setModelName(manager.getModuleParaByCode(moduleCode).getBussinessModuleName());
		}
		manager.saveSms(recvUserIds,model,ownerNum, new OALogInfo("手机短信-短信模块-『发送』："+model.getSmsCon()));
		message="短信已提交服务器！";
		return "sendSms";
	}

	/**
	 * author:luosy
	 * description: 查看用户的回复短信发送记录
	 * modifyer:
	 * description:
	 * @return
	 */
	public String searchRepList() throws Exception {
		try {
			if(repSms.getSmsRepCon()!=null&&!"".equals(repSms.getSmsRepCon())){
				repSms.setSmsRepCon(URLDecoder.decode(repSms.getSmsRepCon(), "utf-8"));
			}
			if(repSms.getModelName()!=null&&!"".equals(repSms.getModelName())){
				repSms.setModelName(URLDecoder.decode(repSms.getModelName(), "utf-8"));
			}
			if(repSms.getSmsSenderName()!=null&&!"".equals(repSms.getSmsSenderName())){
				String ssn = URLDecoder.decode(repSms.getSmsSenderName(), "utf-8");
				if("undefined".equals(ssn)){
					repSms.setSmsSenderName("");
				}else{
					repSms.setSmsSenderName(ssn);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		page = manager.getRepList(page,repSms,inputType);
		return "replist";
	}
}
