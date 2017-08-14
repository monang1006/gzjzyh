package com.strongit.oa.common.eform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.strongit.oa.bo.ToaGlobalConfig;
import com.strongit.oa.common.eform.constants.Constants;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.globalconfig.GlobalConfigService;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.ReflectionUtils;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 电子表单管理类.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2009-11-11 下午01:59:22
 * @version  2.0.2.3
 * @classpath com.strongit.oa.common.eform.EFormAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "eForm.action", type = ServletActionRedirectResult.class),
			@Result(name = BaseActionSupport.SUCCESS, value = "/WEB-INF/jsp/eform/eform.jsp", type = ServletDispatcherResult.class),
			@Result(name = "formDesigner", value = "/WEB-INF/jsp/eform/FormDesigner.jsp", type = ServletDispatcherResult.class),
			@Result(name = "fieldset", value = "/WEB-INF/jsp/eform/eform-fieldset.jsp", type = ServletDispatcherResult.class)
		})
public class EFormAction extends BaseActionSupport {
	
	private Page<EForm> page = new Page<EForm>(FlexTableTag.MAX_ROWS, true);//电子表单分页对象
	
	private EForm eform = new EForm();//电子表单对象
	
	@Autowired IEFormService eformService;//电子表单接口
	
	private String id;//表单id,多个表单id以逗号隔开；id1,id2,id3...
	
	@Autowired
	GlobalConfigService globalConfigService;//全局设置
	
	private String formId;//电子表单模板id
	private String flagId;//电子表单域控件名称
	private String permitReassigned;//是否允许指派
	List<EFormField> fieldList = new ArrayList<EFormField>();//表单模板对应的域字段集合

	/**
	 * 电子表单V2.0设计器
	 * @return
	 */
	public String formDesigner() {
		return "formDesigner";
	}
	
	@Override
	public String delete() throws Exception {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO 自动生成方法存根
		return null;
	}

	/**
	 * 加载电子表单列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}


	/**
	 * 得到电子表单模板里的域控件集合
	 * @author:邓志城
	 * @date:2009-12-19 下午03:28:29
	 * @return
	 * @throws Exception 模板不存在时抛出异常
	 */
	public String fieldSet() throws Exception {
		Assert.hasLength(formId, "formId不存在！");
		if(StringUtils.hasLength(flagId)){
			flagId = flagId.replaceAll("#", ",");
			logger.info(flagId);
		}
		fieldList = eformService.getFormTemplateComponents(formId);
		/**
		 * 是否启动主办权限
		 * */
		ToaGlobalConfig toaGlobalConfig = globalConfigService
		.getToaGlobalConfig("plugins_maindoing");
		String biaozhi = "1";// 插件默认值为1，表示默认是启用模式
		if (toaGlobalConfig != null) {
			if (toaGlobalConfig.getGlobalValue() != null) {
				biaozhi = toaGlobalConfig.getGlobalValue();
			}
		}
		this.getRequest().setAttribute("biaozhi", biaozhi);
		return  "fieldset";
	}
	
	@Override
	protected void prepareModel() throws Exception {
		// TODO 自动生成方法存根

	}

	@Override
	public String save() throws Exception {
		// TODO 自动生成方法存根
		return null;
	}

	public EForm getModel() {
		return eform;
	}

	public Page<EForm> getPage() {
		return page;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEform(EForm eform) {
		this.eform = eform;
	}

	public List<EFormField> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<EFormField> fieldList) {
		this.fieldList = fieldList;
	}

	public String getFlagId() {
		return flagId;
	}

	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public void setPage(Page<EForm> page) {
		this.page = page;
	}

	public String getPermitReassigned() {
		return permitReassigned;
	}

	public void setPermitReassigned(String permitReassigned) {
		this.permitReassigned = permitReassigned;
	}
  
	
}
