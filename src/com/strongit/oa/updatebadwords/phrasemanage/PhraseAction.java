/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：词语管理action
*/

package com.strongit.oa.updatebadwords.phrasemanage;

import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSysmanageFiltrate;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * �������Action
 * @author pengxq
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "phrase.action", type = ServletActionRedirectResult.class) })
public class PhraseAction extends BaseActionSupport<ToaSysmanageFiltrate> 
{
   private Page<ToaSysmanageFiltrate> page = new Page<ToaSysmanageFiltrate>(FlexTableTag.MAX_ROWS, true);
   private String id;	//主键id
   private ToaSysmanageFiltrate modle = new ToaSysmanageFiltrate();	//词语管理bo
   private IPhraseManage manage;	//过滤词语接口
   private String recordDate;		//闲置变量，暂无使用		
   private String filtrateWord;		//不良词汇
   private String filtrateRaplace;	//替代词语
   private Date filtrateTime; 		//词语添加时间
   private String disLogo;			//区分查询全部记录还是按条件查询
   
   /**
    * @roseuid 493DE238000F
    */
   public PhraseAction() 
   {
    
   }
   
   @Autowired
   public void setManage(IPhraseManage manage) {
	   
   		this.manage = manage;
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
   public void setPage(Page<ToaSysmanageFiltrate> aPage) 
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
   public ToaSysmanageFiltrate getModle() 
   {
      return modle;
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
				getRequest().getContextPath()+"/updatebadwords/phrasemanage/phrase.action");
	   if(disLogo!=null&&disLogo.equals("search")){	//按条件查询
		   manage.searchPhrase(page, filtrateWord,filtrateRaplace,filtrateTime);
	   }else{
		   manage.getAllPhrase(page);
	   }
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
	   if("".equals(modle.getFiltrateId()))
		   	modle.setFiltrateId(null);	
	   String msg=""; 
	   StringBuffer returnhtml=new StringBuffer("");
	   msg=manage.save(modle);		//保存对象
	   addActionMessage(msg);
	   returnhtml.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE7\" />")
	   	.append(
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
		returnhtml.append("window.dialogArguments.submitForm();window.close();")
		.append("</script>");
	    return renderHtml(returnhtml.toString());
   }
   
   public String isExist(){
	   List list=manage.getObject(filtrateWord,id);
	   String msg="";
	   if(list!=null&&list.size()>0){
		   msg="已经存在["+modle.getFiltrateWord()+"]不良词语，请重新添加！";		//保存对象
	   }
	   renderText(msg);
	   return null;
   }
   /**
    * @return java.lang.String
    * @roseuid 493DE9090213
    */
   @Override
   public String delete() throws Exception
   {
	   getRequest().setAttribute("backlocation", 
				getRequest().getContextPath()+"/updatebadwords/phrasemanage/phrase.action");
	   String msg="删除失败！";
	   String[] ids=id.split(",");
	   for(String keyid:ids){
		   manage.delete(keyid); //删除对象
		   msg="";
	   }
	   addActionMessage(msg);	
		StringBuffer returnhtml = new StringBuffer(
		"<script> var scriptroot = '")
		.append(getRequest().getContextPath())
		.append("';</script>")
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
		.append("/updatebadwords/phrasemanage/phrase.action")
		.append("';</script>");
		return renderHtml(returnhtml.toString());
   }
   
   /**
    * @roseuid 493DE9090242
    */
   @Override
   protected void prepareModel() 
   {
	   	if(id!=null&&!"".equals(id)&&!"null".equals(id)){
	   		modle=manage.getPhrase(id);		//根据id获取对象
	   	}else{
			modle=new ToaSysmanageFiltrate();		
	   	}  
   }

   public String view() throws Exception { 
	   prepareModel();
	   return "view";
   }
   
	public ToaSysmanageFiltrate getModel() {
		// TODO Auto-generated method stub
		return modle;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setFiltrateRaplace(String filtrateRaplace) {
		this.filtrateRaplace = filtrateRaplace;
	}

	public void setFiltrateTime(Date filtrateTime) {
		this.filtrateTime = filtrateTime;
	}

	public void setFiltrateWord(String filtrateWord) {
		this.filtrateWord = filtrateWord;
	}

	public String getFiltrateRaplace() {
		return filtrateRaplace;
	}

	public Date getFiltrateTime() {
		return filtrateTime;
	}

	public String getFiltrateWord() {
		return filtrateWord;
	}

	public String getId() {
		return id;
	}

	public String getDisLogo() {
		return disLogo;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}
}
