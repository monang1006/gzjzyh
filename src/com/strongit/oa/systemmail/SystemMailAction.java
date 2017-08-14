/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-02-11
 * Autour: pengxq
 * Version: V1.0
 * Description：系统默认邮箱配置action
*/
package com.strongit.oa.systemmail;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.strongit.oa.bo.ToaSysDefaultmail;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * ��������ACTION
 * 
 * @author pengxq
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "systemMail!input.action", type = ServletActionRedirectResult.class) })
public class SystemMailAction extends BaseActionSupport<ToaSysDefaultmail> 
{
	private ToaSysDefaultmail model = new ToaSysDefaultmail();	//个人信息bo
	private SystemMailManager defaultMailManager;				//系统默认邮箱配置manager
	private String defaultMailId;		//个人信息主键
	private String recievers;			//收件人人员字符串
    
   	/**
   	 * @roseuid 4948589E038A
   	 */
   	@Override
   	protected void prepareModel() 
   	{
   		
   	}
   	
   	public ToaSysDefaultmail getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		  getRequest().setAttribute("backlocation", 
	 				getRequest().getContextPath()+"/systemmail/systemMail!input.action");
		model=defaultMailManager.getDefaultMail();
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save() throws Exception {
		  getRequest().setAttribute("backlocation", 
	 				getRequest().getContextPath()+"/systemmail/systemMail!input.action");
		// TODO Auto-generated method stub
		if(model.getDefaultMailId()==null||model.getDefaultMailId().equals("")||model.getDefaultMailId().equals("null")){
			model.setDefaultMailId(null);
		}
		String msg=defaultMailManager.saveDefaultMail(model);
		
		addActionMessage(msg);
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
		.append("<script>");
  		if(msg!=null&&!"".equals(msg)){
			returnhtml.append("alert('")
			.append(getActionMessages().toString())
			.append("');");
  		}
		returnhtml.append(" window.location='")
		.append(getRequest().getContextPath())
		.append("/systemmail/systemMail!input.action';</script>");	
  		return renderHtml(returnhtml.toString());
	}
	
	public String configTest() throws Exception{
		if(model!=null){
			String msg=defaultMailManager.sendMail(model.getDefaultMail(), model);
			return renderText(msg);
		}else{
			return renderText("false");
		}
	}

	/**
  	  * @author：pengxq
  	  * @time：2009-2-12下午02:10:09
  	  * @desc: 系统默认邮箱发送邮件测试
  	  * @param
  	  * @return
  	 */
	public String sendMailTest() throws Exception{
		  getRequest().setAttribute("backlocation", 
	 				getRequest().getContextPath()+"/systemmail/systemMail!input.action");
		model=defaultMailManager.getDefaultMail();			//邮箱配置对象
		String receivers=model.getDefaultMail();			//收件人员
		String msg=defaultMailManager.sendMail(receivers, model);	//系统邮箱发送邮件
  		addActionMessage(msg);
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
		.append("alert('")
		.append(getActionMessages().toString())
		.append("');window.location='")
		.append(getRequest().getContextPath())
		.append("/systemmail/systemMail!input.action';</script>");	
  		return renderHtml(returnhtml.toString());
	}
	
	/**
	 * author:luosy
	 * description: 关闭系统邮件相关功能
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String closeSysMail()throws Exception{
		model = defaultMailManager.getDefaultMail();
		if(model != null){
			model.setDefaultMailUseable(ToaSysDefaultmail.USEABLE_FALSE);			
		}
		String msg = defaultMailManager.saveDefaultMail(model);
		if("".equals(msg)){
			renderText("success");
		}else{
			renderText(msg);
		}
		return null;
		
	}
	
	
	
	public String getDefaultMailId() {
		return defaultMailId;
	}

	public void setDefaultMailId(String defaultMailId) {
		this.defaultMailId = defaultMailId;
	}
	
	@Autowired
	public void setDefaultMailManager(SystemMailManager defaultMailManager) {
		this.defaultMailManager = defaultMailManager;
	}

	public String getRecievers() {
		return recievers;
	}

	public void setRecievers(String recievers) {
		this.recievers = recievers;
	}

}
