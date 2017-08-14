/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理短信配置action跳转类
 */
package com.strongit.oa.sms;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSmsCommConf;
import com.strongit.oa.sms.util.PhoneConfig;
import com.strongit.oa.sms.util.UtilXML;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 短信配置action
 * @author luosy
 * @version 1.0
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "smsConf.action", type = ServletActionRedirectResult.class) })
public class SmsConfAction extends BaseActionSupport<ToaSmsCommConf> {
	private String sendid;	
	private String smscomId;

	private ToaSmsCommConf model = new ToaSmsCommConf();

	private SmsConfManager manager;
	
	private String smsSystemRate;

	private List<PhoneConfig> listphone;
	private String phonetype;
	private String meth;
	private PhoneConfig phoneConfig=new PhoneConfig();
	
	public List<PhoneConfig> getListphone() {
		return listphone;
	}

	public void setListphone(List<PhoneConfig> listphone) {
		this.listphone = listphone;
	}

	public PhoneConfig getPhoneConfig() {
		return phoneConfig;
	}

	public void setPhoneConfig(PhoneConfig phoneConfig) {
		this.phoneConfig = phoneConfig;
	}

	public String getPhonetype() {
		return phonetype;
	}

	public void setPhonetype(String phonetype) {
		this.phonetype = phonetype;
	}

	public SmsConfAction() {

	}

	public void setSmscomId(java.lang.String aSmscomId) {
		smscomId = aSmscomId;
	}

	public ToaSmsCommConf getModel() {
		return model;
	}

	@Autowired
	public void setManager(SmsConfManager aManager) {
		manager = aManager;
	}

	public String getSmsSystemRate() {
		return smsSystemRate;
	}

	public void setSmsSystemRate(String smsSystemRate) {
		this.smsSystemRate = smsSystemRate;
	}
	
	/**
	 * author:luosy
	 * description: 测试短信猫端口
	 * modifyer:
	 * description:
	 * @return
	 */
	public String testComm() throws Exception {
		String response = manager.testComm(model);
		LogPrintStackUtil.printInfo(logger, "执行端口测试，返回值 :"+response);
		String str = "";
		if (response.indexOf(",") >= 0) {
			str = "success,"+response;
		}else{
			str="fail";
		}
		renderText(str);
		return null;
	}

	/**
	 * author:luosy
	 * description: 跳转到列表
	 * modifyer:
	 * description:
	 * @return
	 */
	public String list() throws Exception {
		return SUCCESS;
	}

	/**
	 * author:luosy
	 * description: 保存短信猫端口配置
	 * modifyer:
	 * description:
	 * @return
	 */
	public String save() throws Exception {
		try{
			manager.saveSmsSystemRate(smsSystemRate);
			manager.saveConf(model);
			renderText("ok");
		}catch(Exception e){
			renderText("error");
		}
		return null;
	}

	public String delete() {
		return null;
	}

	protected void prepareModel() {
		model = manager.getSmsConf();
		if (model == null) {
			model = new ToaSmsCommConf();
		}
	}

	/**
	 * author:luosy
	 * description: 跳转到短信猫端口配置页面
	 * modifyer:
	 * description:
	 * @return
	 */
	public String input() throws Exception {
		//	smsSystemRate = manager.getSmsSystemRate();
		
			UtilXML obj=new UtilXML();
			listphone=obj.findAllConfig();
			return "shan";
		}
		
//		启动或停止通道
		public String isOpen()throws Exception{
			UtilXML obj=new UtilXML();
			obj.isOpen(phonetype,meth);
			return this.renderText("true");
		}
		
		public String editConfig() throws DocumentException{
			UtilXML obj=new UtilXML();
			listphone=obj.findAllConfig();
			phoneConfig=listphone.get(0);
			if(sendid.equals("gsmmodem")){
			model = manager.getSmsConf();
			smsSystemRate = manager.getSmsSystemRate();
			}
			return sendid;
		}
		
		public String saveConfig() throws Exception{
		
			UtilXML obj=new UtilXML();
			phoneConfig.setGsmmodem_smscomBps(model.getSmscomBps());
			phoneConfig.setGsmmodem_smscomSimnum(model.getSmscomSimnum());
			if(model.getSmscomName()!=null)
			phoneConfig.setGsmmodem_smscomName(model.getSmscomName());
			phoneConfig.setGsmmodem_smscomPort(model.getSmscomPort());
			phoneConfig.setGsmmodem_smsSystemRate(smsSystemRate);
			if(obj.updateSMSConfig(phoneConfig, sendid)){
				if(sendid.equals("gsmmodem")){
					manager.saveSmsSystemRate(smsSystemRate);
					if(model.getSmscomId().equals("")){
						ToaSmsCommConf m=new ToaSmsCommConf();
						m.setSmscomBps(model.getSmscomBps());
						m.setSmscomName(model.getSmscomName());
						m.setSmscomSimnum(model.getSmscomSimnum());
						m.setSmscomPort(model.getSmscomPort());
						manager.saveConf(m);
					}else{
						manager.saveConf(model);
					}
				}
				this.renderHtml("<script>window.returnValue=\"true\";window.close();</script>");
			}else{
				this.renderHtml("<script>alert(\"配置失败\");window.close();</script>");				
			}
			return null;
		}

		public String getSendid() {
			return sendid;
		}

		public void setSendid(String sendid) {
			this.sendid = sendid;
		}

		public String getMeth() {
			return meth;
		}

		public void setMeth(String meth) {
			this.meth = meth;
		}



}
