/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理短信action跳转类
 */
package com.strongit.oa.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongmvc.webapp.action.BaseActionSupport;
import com.strongit.oa.bo.TOmConference;
import com.strongit.oa.bo.TOmConferenceSend;
import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaSms;
import com.strongit.oa.bo.ToaSmsRep;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.noticeconference.INoticeConferenceManager;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.model.Role;
import com.strongit.oa.common.user.util.Const;
import com.strongmvc.orm.hibernate.Page;

/**
 * 手机短信
 * 
 * @author luosy
 * @version 1.0
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "sms.action", type = ServletActionRedirectResult.class) })
public class SmsAction extends BaseActionSupport<ToaSms> {

	private Page<ToaSms> page = new Page<ToaSms>(FlexTableTag.MAX_ROWS, true);

	@Autowired private IUserService userService;
	
	private String smsId;
	
	private String recvUserIds;
	
	private String receiverNames;
	
	private String senderName;
	
	private String inputType;
	
	private List addNum;
	
	private String ownerNum;

	private ToaSms model = new ToaSms();
	
	private ToaSmsRep repSms = new ToaSmsRep();

	private SmsManager manager;
	
	private String needRep;//是否需要回复
	
	private String moduleCode;//模块ID
	
	private ToaBussinessModulePara modulePara;//对应模块配置
	
	private List means;	//短信回复信息{同意，不同意……}
	
	private HashMap<String, String> typemap = new HashMap<String, String>();

	private HashMap<String, String> statemap = new HashMap<String, String>();
	
	private TOmConference conference;
	
	private INoticeConferenceManager noticeConferenceManager;
	
	private String flag;
	
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
	public void setNoticeConferenceManager(
			INoticeConferenceManager noticeConferenceManager) {
		this.noticeConferenceManager = noticeConferenceManager;
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
	
	

	public TOmConference getConference() {
		return conference;
	}

	public void setConference(TOmConference conference) {
		this.conference = conference;
	}
	
	

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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
		return renderHtml("<script type=\"text/javascript\">returnValue =\"reload\";window.close();</script>");
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
				receiverNames += userService.getUserNameByUserId(recvIds[i])+",";
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
	 * author:yangzc
	 * description: 会议通知短信通知 
	 * modifyer:
	 * description:
	 * @return
	 */
	public String noticeinput() throws Exception {
		//获得会议通知ID
		String conferenceId = getRequest().getParameter("conferenceId");
		
		//查询会议通知对象
		conference = noticeConferenceManager.getConNoticeByConId(conferenceId);
		Set<TOmConferenceSend> send =  conference.getTOmConferenceSends();
		String codes = "";
		String names = "";
		if(send!=null){
			for(TOmConferenceSend se :send){
				
				String code = "";
				String name = "";
				List<TUumsBaseUser> users = userService.getUserListByOrgId(se.getDeptCode());
				for(TUumsBaseUser user :users){
					List<Role> role = userService.getUserRoleInfosByUserId(user.getUserId(), "1");
					if(role!=null){
						for(int i=0;i<role.size();i++){
							if("sms".equals(role.get(i).getRoleSyscode())){
								code = code + user.getUserId() +",";
								name = name + user.getUserName() + ",";
							}
							
						}
					}
				}
				if(!"".equals(code)){
					code = code.substring(0, code.length()-1);
				}
				if(!"".equals(name)){
					name = name.substring(0, name.length()-1);
				}
				
				codes = codes+code+",";
				names = names + name +",";
			}
			codes = codes.substring(0, codes.length()-1);
			names = names.substring(0, names.length()-1);
		}
		getRequest().setAttribute("codes", codes);
		getRequest().setAttribute("names", names);
		
		flag = "isCon";
		//		通讯录下选用户发消息
		if(!"".equals(recvUserIds)&&null!=recvUserIds){
			receiverNames = "";
			String[] recvIds = recvUserIds.split(",");
			for(int i = 0;i<recvIds.length;i++){
				receiverNames += userService.getUserNameByUserId(recvIds[i])+",";
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
			String recvId=sms.getSmsRecvId();
			if(recvId !=null){
			TUumsBaseUser tUser=userService.getUserInfoByUserId(recvId);
			if (Const.IS_YES.equals(tUser.getUserIsdel())) {
				renderText("del");
				return null;
			}
			sms.setSmsSendTime(new Date());
			sms.setSmsServerRet(ToaSms.SERVICE_RET_UNSEND);
			sms.setSmsState(ToaSms.SMS_UNSEND);
			manager.saveSms(sms);
		}
		}
		renderText("succ");
		return null;
	}
		
	
	public String reSends1()throws Exception{
			ToaSms sms = manager.getSmsById(smsId);
			String recvId=sms.getSmsRecvId();
			if(recvId!=null){
			TUumsBaseUser tUser=userService.getUserInfoByUserId(recvId);
			if (Const.IS_YES.equals(tUser.getUserIsdel())) {
				renderText("del");
				return null;
			}
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
			senderName = userService.getUserNameByUserId(model.getSmsSendUser());
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
