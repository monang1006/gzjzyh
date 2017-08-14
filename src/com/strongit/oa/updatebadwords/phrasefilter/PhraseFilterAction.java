/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：词语过滤管理action
*/

package com.strongit.oa.updatebadwords.phrasefilter;

import java.util.HashMap;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSysmanageFiltrateModule;
import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * ����ģ��Action
 * @author pengxq
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "phraseFilter.action", type = ServletActionRedirectResult.class) })
public class PhraseFilterAction extends BaseActionSupport<ToaSysmanageFiltrateModule> 
{
   private Page<ToaSysmanageFiltrateModule> page = new Page<ToaSysmanageFiltrateModule>(FlexTableTag.MAX_ROWS, true);
   private String id;			//主键id
   private ToaSysmanageFiltrateModule modle = new ToaSysmanageFiltrateModule();//词语过滤bo
   private IPhraseFilterManage manage;		//词语过滤接口
   //private BasePrivilManager privilManger;	//权限接口
   private BaseOptPrivilManager optprivilmanager;
   private HashMap<String, String> map = new HashMap<String, String>();	//存放权限id和权限名称的map
   private HashMap<String, String> map1 = new HashMap<String, String>();//存放过滤的模块开启、关闭状态的map
   private String moduleName;		//权限名称
   private String status;			//开关状态
   private final String open="0";	//开关开启
   private final String close="1";	//开关关闭
   /**
    * @roseuid 493E2D490119
    */
   public PhraseFilterAction() 
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
    * Sets the value of the id property.
    * 
    * @param aId the new value of the id property
    */
   public void setId(java.lang.String aId) 
   {
      id = aId;
   }
   
   /**
    * Access method for the modle property.
    * 
    * @return   the current value of the modle property
    */
   public ToaSysmanageFiltrateModule getModle() 
   {
      return modle;
   }
   
   /**
    * Sets the value of the manage property.
    * 
    * @param aManage the new value of the manage property
    */
   @Autowired
   public void setManage(IPhraseFilterManage aManage) 
   {
      manage = aManage;
   }
   
   /**
    * @return java.lang.String
    * @roseuid 493E25CB004E
    */
   public String input() 
   {
	   return null;
   }
   
   /**
     * @author：pengxq
     * @time：2009-1-4下午09:00:42
     * @desc: 查看
     * @param
     * @return
    */
   public String view() throws Exception
   {
	   getRequest().setAttribute("backlocation", "javascript:cancel()");
	   modle=manage.get(id);	
	   String status=modle.getFiltrateOpenstate();	//获取过滤开关状态
	   //String modleId=modle.getModuleId();		//获取模块代号
	   //ToaUumsBaseOperationPrivil privil=optprivilmanager.getPrivilInfoByPrivilSyscode(modleId);
	   //moduleName=privil.getPrivilName();
	   if(open.equals(status))
		   status="开启";
	   else
		   status="关闭";
	   return "view";
   }
   
   /**
    * @return java.lang.String
    * @roseuid 493E2D490167
    */
   @Override
   public String list() throws Exception
   {
	   getRequest().setAttribute("backlocation", 
				getRequest().getContextPath()+"/updatebadwords/phrasefilter/phraseFilter.action");
	   page=manage.getAll(page);
	   /*List list=page.getResult();
	   ToaSysmanageFiltrateModule obj=new ToaSysmanageFiltrateModule();
	   String modleId="";
	   for(int i=0;i<list.size();i++){
		   obj=(ToaSysmanageFiltrateModule) list.get(i);
		   if(obj.getFiltrateModuleId()!=null&&!"".equals(obj.getFiltrateModuleId())&&!"null".equals(obj.getFiltrateModuleId())){
			   modleId=obj.getModuleId();
			   ToaUumsBaseOperationPrivil privil=optprivilmanager.getPrivilInfoByPrivilSyscode(modleId);
			   if(privil!=null){
				   map.put(obj.getModuleId(), privil.getPrivilName());
			   }
		   }
	   } */
	   map1.put("0", "开启");
	   map1.put("1", "关闭");
	   return SUCCESS;
   }
   
   /**
    * @return java.lang.String
    * @roseuid 493E2D490196
    */
   @Override
   public String save() throws Exception 
   {
	   getRequest().setAttribute("backlocation", 
				getRequest().getContextPath()+"/updatebadwords/phrasefilter/phraseFilter.action");
	   modle=manage.get(id);
	   String status=modle.getFiltrateOpenstate();
	   if(status.equals(open))
		   modle.setFiltrateOpenstate(close);
	   else
		   modle.setFiltrateOpenstate(open);
	   String msg=manage.save(modle);
	   addActionMessage(msg);
	   StringBuffer returnhtml = new StringBuffer(
		"<script> var scriptroot = '")
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
		returnhtml.append(" location='")
		.append(getRequest().getContextPath())
		.append("/updatebadwords/phrasefilter/phraseFilter.action")
		.append("';</script>");
		return renderHtml(returnhtml.toString());
   }
   
   /**
    * @return java.lang.String
    * @roseuid 493E2D4901B5
    */
   @Override
   public String delete() 
   {
    return null;
   }
   
   /**
    * @roseuid 493E2D4901E4
    */
   @Override
   protected void prepareModel() 
   {
   }

	public ToaSysmanageFiltrateModule getModel() {
		// TODO Auto-generated method stub
		return modle;
	}
	
	public HashMap getMap() {
		return map;
	}
	
	public HashMap getMap1() {
		return map1;
	}
	
	//@Autowired
	//public void setPrivilManger(BasePrivilManager privilManger) {
	//	this.privilManger = privilManger;
	//}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	@Autowired
	public void setOptprivilmanager(BaseOptPrivilManager optprivilmanager) {
		this.optprivilmanager = optprivilmanager;
	}
}
