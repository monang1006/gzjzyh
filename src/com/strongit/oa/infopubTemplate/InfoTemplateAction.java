/**
 * 
 */
package com.strongit.oa.infopubTemplate;

import javax.annotation.Resource;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;

import com.strongit.oa.bo.ToaInfopublishTemplate;
import com.strongit.tag.web.grid.stronger.FlexTableTag;


import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @description 类描述
 * @className infopubTemplate
 * @company Strongit Ltd. (C) copyright
 * @author 申仪玲
 * @email shenyl@strongit.com.cn
 * @created 2011-11-23 下午07:48:13
 * @version 3.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "infoTemplate.action", type = ServletActionRedirectResult.class) })
public class InfoTemplateAction extends BaseActionSupport{
	
	private InfoTemplateManager manager;
	
	private ToaInfopublishTemplate model = new ToaInfopublishTemplate();   //信息模板对象
	
	private String tId;
	
	private String tName;
	
	private String tDesc;
	

	/** page对象* */
	private Page<ToaInfopublishTemplate> page = new Page<ToaInfopublishTemplate>(
			FlexTableTag.MAX_ROWS, true);
	
	

	// 注入templateManager
	@Resource
	public void setManager(InfoTemplateManager manager) {
		this.manager = manager;
	}
	/* 
	* @see com.strongmvc.webapp.action.BaseActionSupport#delete()
	* @method delete
	* @author 申仪玲
	* @created 2011-11-23 下午07:51:47
	* @description 描述
	* @return
	*/
	@Override
	public String delete() throws Exception {
		try {
		String tId=	getRequest().getParameter("tId");
			manager.deleteTemplate(tId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			renderText("deletefalse");
		}
		return list();
	}



	/* 
	* @see com.strongmvc.webapp.action.BaseActionSupport#input()
	* @method input
	* @author 申仪玲
	* @created 2011-11-23 下午07:51:47
	* @description 描述
	* @return
	*/
	@Override
	public String input() throws Exception {
		this.prepareModel();
		return INPUT;
	}

	/* 
	* @see com.strongmvc.webapp.action.BaseActionSupport#list()
	* @method list
	* @author 申仪玲
	* @created 2011-11-23 下午07:51:47
	* @description 描述
	* @return
	*/
	@Override
	public String list() throws Exception {
		page = manager.getTemPages(page, model);
		return SUCCESS;
	}

	/* 
	* @see com.strongmvc.webapp.action.BaseActionSupport#prepareModel()
	* @method prepareModel
	* @author 申仪玲
	* @created 2011-11-23 下午07:51:47
	* @description 描述
	* @return
	*/
	@Override
	protected void prepareModel() throws Exception {
		String tId=getRequest().getParameter("tId");
		if(tId !=null && !"".equals(tId)){
			model = manager.getOneTemplate(tId);
		}else {
			model = new ToaInfopublishTemplate();
		}
		
	}

	/* 
	* @see com.strongmvc.webapp.action.BaseActionSupport#save()
	* @method save
	* @author 申仪玲
	* @created 2011-11-23 下午07:51:47
	* @description 描述
	* @return
	*/
	@Override
	public String save() throws Exception {
		if ("".equals(model.getTemplateId())) {
			model.setTemplateId(null);
		}

		manager.saveTemplate(model);
		return renderHtml("<script>window.dialogArguments.location='"
				+ getRequest().getContextPath()
				+ "/infopubTemplate/infoTemplate.action'; window.close();</script>");
	}
	
	
	/**
	 * @method checkExit
	 * @author 申仪玲
	 * @created 2011-11-24 下午08:58:59
	 * @description 模板同名验证
	 * @return String 返回类型
	 */
	public String checkExit() {
		if (null != tId && !"".equals(tId)) {// 编辑
			model = manager.getOneTemplate(tId);
			if (tName.equals(model.getTemplateName())
					&& tDesc.equals(model.getTemplateDesc())) {
				return renderHtml("true");
			} else if (!tName.equals(model.getTemplateName())
					&& tDesc.equals(model.getTemplateDesc())) {
				model = manager.getTemByName(tName);
				if (model == null) {
					return renderHtml("true");
				} else {
					return renderHtml("namefalse");
				}
			} else if (tName.equals(model.getTemplateName())
					&& !tDesc.equals(model.getTemplateDesc())) {
				model = manager.getTemByDesc(tDesc);
				if (model == null) {
					return renderHtml("true");
				} else {
					return renderHtml("descfalse");
				}
			} else {
				ToaInfopublishTemplate model1 = manager
						.getTemByName(tName);
				ToaInfopublishTemplate model2 = manager
						.getTemByDesc(tDesc);
				if (model1 == null && model2 == null) {
					return renderHtml("true");
				} else if (model1 != null && model2 == null) {
					return renderHtml("namefalse");

				} else if (model1 == null && model2 != null) {
					return renderHtml("descfalse");
				} else {

					return renderHtml("bosefalse");
				}
			}

		} else {// 添加
			ToaInfopublishTemplate model1 = manager.getTemByName(tName);
			ToaInfopublishTemplate model2 = manager.getTemByDesc(tDesc);
			if (model1 == null && model2 == null) {
				return renderHtml("true");
			} else if (model1 != null && model2 == null) {
				return renderHtml("namefalse");

			} else if (model1 == null && model2 != null) {
				return renderHtml("descfalse");
			} else {
				return renderHtml("bosefalse");
			}
		}
	}
	
	public String gettId() {
		return tId;
	}
	public void settId(String tId) {
		this.tId = tId;
	}
	
	public String gettName() {
		return tName;
	}
	public void settName(String tName) {
		this.tName = tName;
	}
	public String gettDesc() {
		return tDesc;
	}
	public void settDesc(String tDesc) {
		this.tDesc = tDesc;
	}
	
	public void setModel(ToaInfopublishTemplate model) {
		this.model = model;
	}
	public ToaInfopublishTemplate getModel() {
		
		return this.model;
	}
	
	public Page<ToaInfopublishTemplate> getPage() {
		return page;
	}
	public void setPage(Page<ToaInfopublishTemplate> page) {
		this.page = page;
	}

}
