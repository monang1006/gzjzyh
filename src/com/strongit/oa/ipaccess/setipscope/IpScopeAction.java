/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：访问ip控制action
*/
package com.strongit.oa.ipaccess.setipscope;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.strongit.oa.bo.ToaSysmanageLogin;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * ����ip����Action
 * @author pengxq
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "ipScope.action", type = ServletActionRedirectResult.class) })
public class IpScopeAction extends BaseActionSupport<ToaSysmanageLogin> 
{
   private Page<ToaSysmanageLogin> page = new Page<ToaSysmanageLogin>(FlexTableTag.MAX_ROWS, true);
   private String IpScopeId;
   private IpScopeManager ipScopeManager;
   private ToaSysmanageLogin toaSysmanageLogin = new ToaSysmanageLogin();
   private String loginBeginIp;
   private String loginEndIp;
   private String loginDesc;
   private String disLogo;
   
   /**
    * @roseuid 4938CAB303C8
    */
   public IpScopeAction() 
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
   public void setPage(Page aPage) 
   {
      page = aPage;
   }
   
   /**
    * Sets the value of the IpScopeId property.
    * 
    * @param aIpScopeId the new value of the IpScopeId property
    */
   public void setIpScopeId(java.lang.String aIpScopeId) 
   {
      IpScopeId = aIpScopeId;
   }
   
   /**
    * Sets the value of the ipScopeManager property.
    * 
    * @param aIpScopeManager the new value of the ipScopeManager property
    */
   @Autowired
   public void setIpScopeManager(IpScopeManager aIpScopeManager) 
   {
      ipScopeManager = aIpScopeManager;
   }
   
   /**
    * Access method for the toaSysmanageLogin property.
    * 
    * @return   the current value of the toaSysmanageLogin property
    */
   public ToaSysmanageLogin getToaSysmanageLogin() 
   {
      return toaSysmanageLogin;
   }
   
   /**
    * @return java.lang.String
    * @roseuid 4938C9B8034B
    */
   public String input() 
   {
	   prepareModel();
	   return INPUT;
   }
   
   
   public String view() throws Exception{
	   prepareModel();
	   return "view";
   }
   /**
    * @return java.lang.String
    * @roseuid 493C77FA01D4
    */
   @Override
   public String list() throws Exception
   {
	   getRequest().setAttribute("backlocation", 
				getRequest().getContextPath()+"/ipaccess/setipscope/ipScope.action");
	   if(disLogo!=null&&"search".equals(disLogo)){
		   page= ipScopeManager.searchIpScope(page,loginBeginIp,loginEndIp,loginDesc);
	   }else{   
		   page=ipScopeManager.getAllIpScope(page);
	   }
	   return SUCCESS;
   }
   
   /**
    * @return java.lang.String
    * @roseuid 493C77FA0203
    */
   @Override
   public String save() throws Exception
   {
	   getRequest().setAttribute("backlocation", "javascript:cancel()");
	   if("".equals(toaSysmanageLogin.getLoginId()))
		   toaSysmanageLogin.setLoginId(null);
	   String msg=ipScopeManager.saveIpScope(toaSysmanageLogin);
	   addActionMessage(msg);
	   StringBuffer returnhtml = new StringBuffer("<script> var scriptroot = '")
	   .append(getRequest().getContextPath())
	   .append("'</script>")
	   .append("<SCRIPT src='")
	   .append(getRequest().getContextPath())
	   .append("/common/js/commontab/workservice.js'>")
	   .append("</SCRIPT>")
	   .append("<SCRIPT src='")
	   .append(getRequest().getContextPath())
	   .append("/common/js/commontab/service.js'>")
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
    * @roseuid 493C77FA0222
    */
   @Override
   public String delete() throws Exception
   {
	   getRequest().setAttribute("backlocation", 
				getRequest().getContextPath()+"/ipaccess/setipscope/ipScope.action");
	   String[] IpScopeIds=IpScopeId.split(",");
	   String msg="删除Ip权限控制失败！" ;
	   for(String keyid:IpScopeIds){
		   ipScopeManager.deleteIpScope(keyid);
	   }
	   msg="" ;
	   addActionMessage(msg);
	   StringBuffer returnhtml = new StringBuffer("<script> var scriptroot = '").append(getRequest().getContextPath()).append("'</script>")
	   .append("<SCRIPT src='")
	   .append(getRequest().getContextPath())
	   .append("/common/js/commontab/workservice.js'>")
	   .append("</SCRIPT>")
	   .append("<SCRIPT src='")
	   .append(getRequest().getContextPath())
	   .append("/common/js/commontab/service.js'>")
	   .append("</SCRIPT>")
	   .append("<script>");
	   if(msg!=null&&!"".equals(msg)){
		   returnhtml.append("alert('")
		   .append(getActionMessages().toString())
		   .append("');");
	   }  
	   returnhtml.append(" location='")
	   .append(getRequest().getContextPath())
	   .append("/ipaccess/setipscope/ipScope.action")
	   .append("';</script>");
	   return renderHtml(returnhtml.toString());
   }
   
   /**
    * @roseuid 493C77FA0242
    */
   @Override
   protected void prepareModel() 
   {
	   if (IpScopeId != null&&!IpScopeId.equals("")&&!IpScopeId.equals("null")) {
		   toaSysmanageLogin = ipScopeManager.getIpScope(IpScopeId);
		} else {
			toaSysmanageLogin = new ToaSysmanageLogin();
		}
   }

	public ToaSysmanageLogin getModel() {
		// TODO Auto-generated method stub
		return toaSysmanageLogin;
	}
	
	public void setLoginBeginIp(String loginBeginIp) {
		this.loginBeginIp = loginBeginIp;
	}
	
	public void setLoginDesc(String loginDesc) {
		this.loginDesc = loginDesc;
	}
	
	public void setLoginEndIp(String loginEndIp) {
		this.loginEndIp = loginEndIp;
	}
	
	public String getLoginBeginIp() {
		return loginBeginIp;
	}
	
	public String getLoginDesc() {
		return loginDesc;
	}
	
	public String getLoginEndIp() {
		return loginEndIp;
	}
	
	public String getDisLogo() {
		return disLogo;
	}
	
	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}
}
