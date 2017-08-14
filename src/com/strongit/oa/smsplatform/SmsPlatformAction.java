package com.strongit.oa.smsplatform;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.smsplatform.util.PropertiesUtil;
import com.strongmvc.webapp.action.BaseActionSupport;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2012-12-18
 * Autour: luosy
 * Version: V1.0
 * Description： 
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/smsPlatform.action") })
public class SmsPlatformAction extends BaseActionSupport{
	
	private SmsPlatformManager platformManager;
	
	private ModelStateMeanManager stateManager;
	
	private String thankOrNot;					//是否需要感谢回复
	
	private String replyOrNot;					//是否需要回复
	
	private String answerNum;					//短信答案个数
	
	private String[] means;						//短信答案含义
	
	private String[] state;						//短信对应的状态位值
	
	private String sendid;						//前台传过来的模块ID
	
	private List<ToaBussinessModulePara> list;
	
	private ToaBussinessModulePara model = new ToaBussinessModulePara();
	
	private Log logger = LogFactory.getLog(SmsPlatformAction.class);
	
	private static final String needReply="1";	//需要进行回复
	
	private static final String noReply="0";	//不需要进行回复
	
	private static final String open="1";		//开启状态
	
	private static final String close="0";		//关闭状态
	
	private String mobile;
	
	private String mobiletext;
	
	private String unicom;
	
	private String unicomtext;
	
	private String telecom;
	
	private String telecomtext;
	
	@Autowired
	public void setPlatformManager(SmsPlatformManager platformManager) {
		this.platformManager = platformManager;
	}

	@Autowired
	public void setStateManager(ModelStateMeanManager stateManager) {
		this.stateManager = stateManager;
	}
	
	public void setThankOrNot(String thankOrNot) {
		this.thankOrNot = thankOrNot;
	}
	
	public void setReplyOrNot(String replyOrNot) {
		this.replyOrNot = replyOrNot;
	}
	
	public void setMeans(String[] means) {
		this.means = means;
	}

	public void setState(String[] state) {
		this.state = state;
	}
	
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		if(platformManager.deleteObj(sendid)){
			return this.renderText("true");
		}else{
			return this.renderText("false");
		}
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		list=platformManager.getAllObj();
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
	}
	
	public String edit() throws Exception{
		if(sendid!=null){
			platformManager.changeState(sendid,"0");
			model=platformManager.getObjById(sendid);
			Set set=model.getToaModuleStateMeans();
			if(set!=null&&set.size()!=0){
				answerNum=String.valueOf(set.size());
				replyOrNot=needReply;
			}else{
				replyOrNot=noReply;
				answerNum="0";
			}
		}
		return "edit";
	}
	
	public String update() throws Exception{
		
		if(platformManager.saveObj(model)){											//保存对应修改
			if(stateManager.deleteObjByPar(model.getBussinessModuleId())){			//删除答案
				if(replyOrNot.equals(needReply)){
					if(stateManager.saveObj(model, means, state)){
						return this.renderHtml("<script>window.returnValue=\"true\";window.close();</script>");
					}else{
						return this.renderHtml("<script>alert(\"答案保存保存失败\");window.close();</script>");
					}
				}else{
					return this.renderHtml("<script>window.returnValue=\"true\";window.close();</script>");
				}
			}else{
				return this.renderHtml("<script>alert(\"保存失败\");window.close();");
			}
		}else{
			return this.renderHtml("<script>alert(\"保存失败\");window.close();</script>");
		}
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		int num=0;
		String code=platformManager.getMaxCode();						//当前最大CODE值
		if(code==null){													//如果当前取出的值为null认为数据库中无内容
			code="0";
		}else{
			num=Integer.parseInt(code);
			code=String.valueOf(num+1);
		}
		model.setBussinessModuleCode(code);
		Long val=new Long("0");
		model.setIncreaseCode(val);
		if(num<9){
			if(platformManager.saveObj(model)){							//保存模块对象成功
				logger.info("模块保存成功...");
				if(replyOrNot.equals(needReply)){						//如果需要回复
					if(stateManager.saveObj(model, means, state)){		//保存答案对象成功
						return this.renderHtml("<script>window.returnValue=\"true\";window.close();</script>");
					}else{
						return this.renderHtml("<script>alert(\"答案保存保存失败\");</script>");
					}
				}else{
					return this.renderHtml("<script>window.returnValue=\"true\";window.close();</script>");
				}
			}else{
				logger.info("模块保存失败...");
				return this.renderHtml("<script>alert（\"模块及答案保存失败\");</script>");
			}
		}else{
			return this.renderHtml("<script>alert(\"模块编码已经用完，请您重新添加\");</script>");
		}
	}
	
	public String operatorsConfig()throws Exception{
		String[] ar1=getArray(PropertiesUtil.getProperty(PropertiesUtil.mobile));
		if(ar1!=null){
			this.mobile=ar1[0];
			this.mobiletext=ar1[1];
		}
		String[] ar2=getArray(PropertiesUtil.getProperty(PropertiesUtil.unicom));
		if(ar2!=null){
			this.unicom=ar2[0];
			this.unicomtext=ar2[1];
		}
		String[] ar3=getArray(PropertiesUtil.getProperty(PropertiesUtil.telecom));
		if(ar3!=null){
			this.telecom=ar3[0];
			this.telecomtext=ar3[1];
		}
		return "optors";
	}
	
	public String savePro()throws Exception{
		Properties pro= PropertiesUtil.getProperties();
		Properties pro1=new Properties();
		pro1.setProperty(PropertiesUtil.mobile, this.mobile+","+this.mobiletext);
		pro1.setProperty(PropertiesUtil.telecom, this.telecom+","+this.telecomtext);
		pro1.setProperty(PropertiesUtil.unicom, this.unicom+","+this.unicomtext);
		try{
			PropertiesUtil.saveProp(pro1, "properties config");
			pro.setProperty(PropertiesUtil.mobile, this.mobile+","+this.mobiletext);
			pro.setProperty(PropertiesUtil.telecom, this.telecom+","+this.telecomtext);
			pro.setProperty(PropertiesUtil.unicom, this.unicom+","+this.unicomtext);
		}catch(Exception e){
			e.printStackTrace();
		}
  		StringBuffer returnhtml = new StringBuffer(
		"<script> var scriptroot = '")
		.append(getRequest().getContextPath())
		.append("'</script>")
		.append("<SCRIPT src='")
		.append(getRequest().getContextPath())
		.append(
				"/common/js/commontab/workservice.js'>")
		.append("</SCRIPT>")
		.append("<SCRIPT src='")
		.append(getRequest().getContextPath())
		.append(
				"/common/js/commontab/service.js'>")
		.append("</SCRIPT>")
		.append("<script>")
		.append("window.location='")
		.append(getRequest().getContextPath())
		.append("/smsplatform/smsPlatform!operatorsConfig.action';</script>");
  		return renderHtml(returnhtml.toString());
	}
	
	public String open() throws Exception{
		if(platformManager.changeState(sendid, open)){
			return this.renderText("true");
		}else{
			return this.renderText("false");
		}
	}
	
	public String close() throws Exception{
		if(platformManager.changeState(sendid, close)){
			return this.renderText("true");
		}else{
			return this.renderText("false");
		}
	}
	
	public Properties createProperty() throws IOException{
		Properties pro=PropertiesLoaderUtils.loadProperties(new ClassPathResource("proConfig.properties"));
		pro.setProperty(PropertiesUtil.mobile, this.mobile+","+this.mobiletext);
		pro.setProperty(PropertiesUtil.telecom, this.telecom+","+this.telecomtext);
		pro.setProperty(PropertiesUtil.unicom, this.unicom+","+this.unicomtext);
		return pro;
	}
	
	public String[] getArray(String info){
		if(info!=null){
			if(info.indexOf(",")!=0){
				return info.split(",");
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	
	/**
	 * author:luosy
	 * description:该模块是否开启
	 * modifyer:
	 * description:
	 * @return
	 */
	public String isModuleParaOpen(){
		String isopen = platformManager.isModuleParaOpen(sendid);
		if(null==isopen){
			renderText("no");
		}else{
			renderText(isopen);
		}
		return null;
	}
	
	public Object getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public List<ToaBussinessModulePara> getList() {
		return list;
	}

	public void setList(List<ToaBussinessModulePara> list) {
		this.list = list;
	}

	public String getSendid() {
		return sendid;
	}

	public void setSendid(String sendid) {
		this.sendid = sendid;
	}

	public String getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(String answerNum) {
		this.answerNum = answerNum;
	}

	public String getReplyOrNot() {
		return replyOrNot;
	}
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobiletext() {
		return mobiletext;
	}

	public void setMobiletext(String mobiletext) {
		this.mobiletext = mobiletext;
	}

	public String getTelecom() {
		return telecom;
	}

	public void setTelecom(String telecom) {
		this.telecom = telecom;
	}

	public String getTelecomtext() {
		return telecomtext;
	}

	public void setTelecomtext(String telecomtext) {
		this.telecomtext = telecomtext;
	}

	public String getUnicom() {
		return unicom;
	}

	public void setUnicom(String unicom) {
		this.unicom = unicom;
	}

	public String getUnicomtext() {
		return unicomtext;
	}

	public void setUnicomtext(String unicomtext) {
		this.unicomtext = unicomtext;
	}

}
