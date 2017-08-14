/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：菜单快捷组action
*/
package com.strongit.oa.shortcutmenu;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.userdetails.UserDetails;
import com.strongit.oa.bo.ToaSystemmanageUserFastmen;
import com.strongit.oa.common.user.model.User;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * �˵������Action
 * @author pengxq
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "fastMenu.action", type = ServletActionRedirectResult.class) })
public class FastMenuAction extends BaseActionSupport<ToaSystemmanageUserFastmen> 
{
   private String userid;	//用户id
   private String id;		//主键id
   private ToaSystemmanageUserFastmen modle = new ToaSystemmanageUserFastmen();	//默认快捷菜单bo
   private IFastMenuManager manager;	//快捷菜单接口
   private List userFMList=new ArrayList();	//用户的快捷菜单列表
   private List FMList=new ArrayList();		//备选菜单类表
   private String modleIds;					//权限id字符串
   private String param;

   
   public FastMenuAction() 
   {
    
   }
   

   public java.lang.String getUserid() 
   {
      return userid;
   }
   

   public void setUserid(java.lang.String aUserid) 
   {
      userid = aUserid;
   }
   

   public ToaSystemmanageUserFastmen getModle() 
   {
      return modle;
   }
   

   @Autowired
   public void setManager(IFastMenuManager aManager) 
   {
      manager = aManager;
   }
   
 
   @Override
   public String list() 
   {
	   getRequest().setAttribute("backlocation", 
				getRequest().getContextPath()+"/shortcutmenu/fastMenu.action");
	   UserDetails userDetail = this.getUserDetails();
	   User user=manager.getCurrentUser();
	   if(param.equals("person")){
		   modle=manager.getRecords(user.getUserId());	//获取该用户快捷菜单对象
		   userFMList=manager.getFastMenu(userDetail);	//当前用户快捷菜单列表
		   FMList=manager.getMenuList(userDetail,modle);		//备选快捷菜单列表	  
	   }else{
		   modle=manager.getSystemSetting();		//获取该用户快捷菜单对象
		   userFMList=manager.getMenuList(modle);	//当前用户快捷菜单列表
		   FMList=manager.getMenuList(userDetail,modle);	//备选快捷菜单列表
	   }	   
	   StringBuilder sb = new StringBuilder();
	   sb.append("<select name='select2' ondblclick='func_insert();' multiple='multiple' class='select2'>");
	   
	   for(Object m : FMList)
	   {
		   com.strongit.oa.bo.ToaUumsBaseOperationPrivil tub = (com.strongit.oa.bo.ToaUumsBaseOperationPrivil) m;
		   if(tub.getPrivilSyscode().length() == 8)
			   sb.append("<option value='MENU_SORT' isParent = '" + tub.getPrivilSyscode() + "'>------------" + tub.getPrivilName() + "-------------</option>");
		   else
			   sb.append("<option value='" + tub.getPrivilSyscode() + "'>" + tub.getPrivilName() + "</option>");
	   }
	   sb.append("</select>");
	   HttpServletRequest request=getRequest();
	   request.setAttribute("FMList", FMList);
	   request.setAttribute("userFMList",userFMList);
	   request.setAttribute("select2", sb.toString());
	   
	   
	   return SUCCESS;
   }
   
  
   @Override
   public String save() 
   {
	   getRequest().setAttribute("backlocation", "javascript:cancel()");
	   if("".equals(id))						//新建用户快捷菜单
		   	modle.setFastmenuId(null);
	   else										//修改用户快捷菜单
		   modle.setFastmenuId(id);
	   modle.setModerIds(modleIds);	
	   if(param!=null&&param.equals("system")){	//如果是系统的快捷菜单
		   modle.setSystemLogo("1");  
	   }else{									//个人的快捷菜单
		   User user=manager.getCurrentUser();  
		   String userid=user.getUserId();
		   modle.setUserid(userid);
		   modle.setSystemLogo("0");
	   }
	   String msg=manager.saveFastMenu(modle);	//保存快捷菜单
	   this.renderText(msg);
	   return null;
	   /*addActionMessage(msg);
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
	   .append("/theme/theme!RefreshToolbar.action")
	   .append("';</script>");
	   return renderHtml(returnhtml.toString());*/
   }
   

   @Override
   public String delete() 
   {
    return null;
   }
   
  
   @Override
   protected void prepareModel() 
   {
	 /*  modle=manager.getRecords(userid);
	   if(modle==null)
			modle=new ToaSystemmanageUserFastmen();*/
   }

   @Override
	public String input() throws Exception {
	   prepareModel();
	   return INPUT;
   }

   public ToaSystemmanageUserFastmen getModel() {
	return modle;
   }

   public void setId(String id) {
	   this.id = id;
   }

	public List getFMList() {
		return FMList;
	}
	
	public List getUserFMList() {
		return userFMList;
	}
	
	public void setModleIds(String modleIds) {
		this.modleIds = modleIds;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}
