/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-07-20
 * Autour: pengxq
 * Version: V1.0
 * Description：系统链接管理action
*/

package com.strongit.oa.systemlink;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSystemmanageSystemLink;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * �������Action
 * @author pengxq
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "sysLink.action", type = ServletActionRedirectResult.class) })
public class SysLinkAction extends BaseActionSupport<ToaSystemmanageSystemLink> 
{
   private Page<ToaSystemmanageSystemLink> page = new Page<ToaSystemmanageSystemLink>(FlexTableTag.MAX_ROWS, true);
   
   private ToaSystemmanageSystemLink model = new ToaSystemmanageSystemLink();	//系统链接bo
   private SysLinkManage manager;
   private String linkId;
   private String linkUrl;
   private String systemName;
   private String systemDesc;
   
   /**
    * @roseuid 493DE238000F
    */
   public SysLinkAction() 
   {
    
   }
   
   /**
    * Access method for the page property.
    * 
    * @return   the current value of the page property
    */
   public Page getPage() 
   {
      return page;
   }
   
   /**
    * Sets the value of the page property.
    * 
    * @param aPage the new value of the page property
    */
   public void setPage(Page<ToaSystemmanageSystemLink> aPage) 
   {
      page = aPage;
   }

   /**
    * @return java.lang.String
    * @roseuid 493DE22D00BB
    */
   public String input()throws Exception 
   {
	   prepareModel(); 
	   return INPUT;
   }
   
   /**
    * @return java.lang.String
    * @roseuid 493DE90901D4
    */
   @Override
   public String list() throws Exception
   {
	   getRequest().setAttribute("backlocation", 
				getRequest().getContextPath()+"/systemlink/sysLink.action");		
	   page=manager.getSysLink(page, systemName ,linkUrl,systemDesc);
	   page.getResult();
	   getRequest().setAttribute("list", page.getResult());
	   return SUCCESS;
   }
   
   /**
    * @return java.lang.String
    * @roseuid 493DE90901F4
    */
   @Override
   public String save() throws Exception
   {
	   getRequest().setAttribute("backlocation", "javascript:cancel()");	
	   if(model.getLinkId()!=null&&"".equals(model.getLinkId())){
		   model.setLinkId(null);
	   }
	   String msg=manager.saveSystemLink(model);
	   StringBuffer returnhtml=new StringBuffer("");
	   addActionMessage(msg);
	   returnhtml.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE7\" />")
	   	.append(
		"<script> var scriptroot = '")
		.append(getRequest().getContextPath())
		.append("'</script>")
		.append("<SCRIPT  type=\"text/javascript\" language=\"javascript\" src='")
		.append(getRequest().getContextPath())
		.append(
				"/common/js/commontab/workservice.js'>")
		.append("</SCRIPT>")
		.append("<SCRIPT  type=\"text/javascript\" language=\"javascript\" src='")
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
		returnhtml.append("window.dialogArguments.submitForm();window.close();")
		.append("</script>");
	    return renderHtml(returnhtml.toString());
   }

   /**
    * @return java.lang.String
    * @roseuid 493DE9090213
    */
   @Override
   public String delete() throws Exception
   {	   
	   getRequest().setAttribute("backlocation", 
				getRequest().getContextPath()+"/systemlink/sysLink.action");
	   String msg="";
	   String msgs="";
	   String[] ids=linkId.split(",");
	   for(String keyid:ids){
		   msg=manager.delete(keyid); //删除对象  
		   if(!"".equals(msg)){
			   ToaSystemmanageSystemLink obj=manager.getSystemLink(keyid);
			   msgs+=","+obj.getSystemName();
		   }
	   }
	   if(!"".equals(msgs))
	   		msgs=msgs.substring(1);
	   return renderText(msgs);
   }
   
   /**
    * @roseuid 493DE9090242
    */
   @Override
   protected void prepareModel() 
   {
	   if(linkId!=null&&!"".equals(linkId)&&!"null".equals(linkId)){
	   		model=manager.getSystemLink(linkId);		//根据id获取对象
	   	}else{
	   		model=new ToaSystemmanageSystemLink();		
	   	}  
   }

   public String view() throws Exception { 
	   prepareModel();
	   return "view";
   }

	public ToaSystemmanageSystemLink getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	public void setModel(ToaSystemmanageSystemLink model) {
		this.model = model;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getSystemDesc() {
		return systemDesc;
	}

	public void setSystemDesc(String systemDesc) {
		this.systemDesc = systemDesc;
	}

	@Autowired
	public void setManager(SysLinkManage manager) {
		this.manager = manager;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}	
}
