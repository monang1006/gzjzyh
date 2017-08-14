package com.strongit.oa.common.eform.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import com.strongit.form.services.FormDictionaryService;
import com.strongit.form.services.FormService;
import com.strongit.form.services.FormTableService;
import com.strongit.form.vo.FormAttachment;
import com.strongit.form.vo.FormProperty;
import com.strongit.form.vo.FormTemplate;
import com.strongit.form.vo.FormData.FormDataForm;
import com.strongit.form.vo.FormData.FormDataRow;
import com.strongit.form.vo.FormTemplate.FormTemplateColumn;
import com.strongit.form.vo.FormTemplate.FormTemplateComponent;
import com.strongit.form.vo.FormTemplate.FormTemplateForm;
import com.strongit.form.vo.FormTemplate.FormTemplatePage;
import com.strongit.form.vo.FormTemplate.FormTemplateTable;
import com.strongit.oa.bo.TEFormTemplate;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.common.business.jbpmbusiness.JBPMBusinessManager;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.service.VoFormDataBean;
import com.strongit.oa.eformManager.EformManagerManager;
import com.strongit.oa.infotable.infoitem.InfoItemManager;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.StringUtil;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 电子表单接口实现类，适配电子表单构件提供的接口
 * 
 * @author dengwenqiang
 * @version 1.0
 */
@Service
public class EFormService implements IEFormService, FormTableService,
		FormDictionaryService {
	// 电子表单服务接口
	FormService eformService;

	@Autowired
	EformManagerManager manager;

	@Autowired
	SendDocManager baseManager;
	
	@Autowired
	JBPMBusinessManager jbpmbusinessManager;
	
	/** 信息集Manager*/
	@Autowired
	InfoSetManager infoSetManager;

	private Properties formProperties;

	public void setFormProperties(Properties formProperties) {
		this.formProperties = formProperties;
	}

	/**
	 * 构造函数
	 */
	public EFormService() {

	}

	@Autowired
	public void setEformService(FormService eformService) {
		this.eformService = eformService;
	}

	/**
	 * 保存附件
	 * 
	 * @param attachment
	 *            附件内容
	 * @param attachmentName
	 *            附件名称
	 * @param subPath
	 *            附件相对路径
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 */
	public void SaveAttachment(InputStream attachment, String attachmentName,
			String subPath) throws ServiceException, DAOException,
			SystemException {
		try {
			eformService.SaveAttachment(attachment, attachmentName, subPath);
		} catch (ServiceException ex) {
			throw ex;
		} catch (DAOException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * 删除附件
	 * 
	 * @param attachmentName
	 *            存储在硬盘上的附件名称
	 * @param subPath
	 *            相对路径
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 */
	public void RemoveAttachment(String attachmentName, String subPath)
			throws ServiceException, DAOException, SystemException {
		try {
			// eformService.RemoveAttachment(attachmentName, subPath);
			File attachmentFile = FormProperty.getFormAttachmentFile(
					attachmentName, subPath);
			if (attachmentFile.exists()) {
				if (!(attachmentFile.delete())) {
					throw new SystemException("删除 "
							+ attachmentFile.getAbsolutePath() + " 文件失败。");
				}
				FormAttachment formAttachment = FormAttachment
						.get(attachmentName);
				if ((formAttachment != null)
						&& (formAttachment.AttachmentFile != null)
						&& (formAttachment.AttachmentFile.getAbsolutePath()
								.equals(attachmentFile.getAbsolutePath()))) {
					FormAttachment.remove(attachmentName);
				}
			}
		} catch (ServiceException ex) {
			throw ex;
		} catch (DAOException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * 得到附件
	 */
	public InputStream LoadAttachment(String attachmentName, String subPath)
			throws ServiceException, DAOException, SystemException {
		InputStream is = null;
		try {
			//return eformService.LoadAttachment(attachmentName, subPath);
			File attachmentFile = FormProperty.getFormAttachmentFile(attachmentName, subPath);
			if(attachmentFile.exists()) {
				is = new FileInputStream(attachmentFile);
			}
		} catch (ServiceException ex) {
			throw ex;
		} catch (DAOException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
		return is;
	}

	/**
	 * 根据表单模板id得到表单模板对象
	 * 
	 * @param id
	 * @return
	 */
	public TEFormTemplate get(String id) {
		return manager.get(id);
	}

	/**
	 * 保存表单模板数据
	 * 
	 * @param model
	 */
	public void saveFormTemplate(TEFormTemplate model) {
		manager.save(model);
	}

	/**
	 * 加载模板数据
	 * 
	 * @param data
	 * @return
	 */
	public InputStream loadFormData(InputStream data) {
		return eformService.LoadFormData(data);
	}

	/**
	 * 得到所有表
	 */
	public List<String> GetTablesByXML() {
		/*String tableXML = eformService.GetTablesByXML();
		Dom4jUtil du = new Dom4jUtil();
		Document document = du.loadXML(tableXML);
		List rows = document.selectNodes("rs/row");
		List<String> tableNameList = new LinkedList<String>();
		if (rows != null && !rows.isEmpty()) {
			for (int i = 0; i < rows.size(); i++) {
				Element element = (Element) rows.get(i);
				String tableName = element.attributeValue("code");
				tableNameList.add(tableName);
			}
		}
		return tableNameList;*/
		return null;
	}

	/**
	 * 获取电子表单模板列表
	 * 
	 * @param String
	 *            formType
	 *            <p>
	 *            表单类型,如果传入null则取所有表单
	 *            </p>
	 * @param List
	 *            表单模板对象EFormField集合EForm对象列表
	 */
	public List<EForm> getFormTemplateList(String formType)
			throws ServiceException, DAOException, SystemException {
		return manager.getFormTemplateList(formType);
	}
	public List<EForm> getJSpFormTemplateList(String formType)
			throws ServiceException, DAOException, SystemException {
		return manager.getJspFormTemplateList(formType);
	}

	/**
	 * 根据表单模板id得到表单中所有组件
	 */
	public List<EFormField> getFormTemplateComponents(String templateId)
			throws ServiceException, DAOException, SystemException {
		List<EFormField> list = new ArrayList<EFormField>();
		Map<String, EFormComponent> fieldMap;
		try {
			fieldMap = this.getFieldInfo(templateId, true, false);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (DAOException e) {
			e.printStackTrace();
			throw e;
		} catch (SystemException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
		Collection<EFormComponent> components = fieldMap.values();
		if (components != null) {
			for (Iterator<EFormComponent> iter = components.iterator(); iter
					.hasNext();) {
				EFormField formfield = new EFormField();
				EFormComponent ec = iter.next();
				formfield.setCaption(ec.getCaption());
				formfield.setName(ec.getName());
				formfield.setFieldname(ec.getFieldName());
				formfield.setTablename(ec.getTableName());
				formfield.setType(ec.getType());
				list.add(formfield);
			}
		}

		return list;
	}

	/**
	 * 获取电子表单模板域信息列表
	 * 
	 * @param templateId
	 *            电子模板ID
	 * @return List 表单域对象EFormField集合
	 */
	public List<EFormField> getFormTemplateFieldList(String templateId)
			throws ServiceException, DAOException, SystemException {
		List<EFormField> list = new ArrayList<EFormField>();
		Map<String, EFormComponent> fieldMap;
		try {
			fieldMap = this.getFieldInfo(templateId, false, false);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (DAOException e) {
			e.printStackTrace();
			throw e;
		} catch (SystemException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
		Collection<EFormComponent> components = fieldMap.values();
		if (components != null) {
			for (Iterator<EFormComponent> iter = components.iterator(); iter
					.hasNext();) {
				EFormField formfield = new EFormField();
				EFormComponent ec = iter.next();
				formfield.setCaption(ec.getCaption());
				formfield.setName(ec.getName());
				formfield.setFieldname(ec.getFieldName());
				formfield.setTablename(ec.getTableName());
				formfield.setType(ec.getType());
				list.add(formfield);
			}
		}

		return list;
	}

	/**
	 * 获取电子表单模板中指定域的值
	 * 
	 * @param templateId
	 *            电子模板ID
	 * @param fieldId
	 *            域ID
	 * @param dataId
	 *            数据ID
	 * @return String 域值
	 * @modify yanjian 2012-08-12 09:51 处理表单存在特殊数据类型无法强制转换为String的情况
	 */
	public String getFormTemplateFieldValue(String formId, String fieldId,
			String dataId) throws ServiceException, DAOException,
			SystemException {
		/*
		 * String value = eformService.GetFormTemplateFieldValue(new Long(
		 * templateId), fieldId, dataId); return value;
		 */
		try {
			Map<String, EFormComponent> fieldMap = this.getFieldInfo(formId);
			EFormComponent tableComponent = fieldMap.get(MAINTABLENAME);
			String tableName = tableComponent.getTableName();
			Collection<EFormComponent> components = fieldMap.values();
			for (Iterator<EFormComponent> it = components.iterator(); it
					.hasNext();) {
				EFormComponent component = it.next();
				if (component.getName() != null
						&& component.getName().equals(fieldId)) {
					String pkFieldName = baseManager
							.getPrimaryKeyName(tableName);
					return StringUtil.castString(baseManager.getFormTemplateFieldValue(
							tableName, pkFieldName, dataId, component
							.getFieldName()));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 保存电子表单数据
	 * 
	 * @param data
	 *            XML格式的电子表单数据值对象
	 * @return Object[]{} {0}String "表名,主键字段名,主键值" {1}返回的流信息
	 */
	public VoFormDataBean saveFormData(InputStream data)
			throws ServiceException, DAOException, SystemException {
		String ret = "";
		List<FormDataForm> list = new ArrayList<FormDataForm>();
		List<File> lstDeleteFormAttachment = new ArrayList<File>();
		InputStream isReturnFormData = eformService.SaveFormData(data, list,
				lstDeleteFormAttachment);
		if (!list.isEmpty()) {
			FormDataForm fdf = list.get(0);
			List<FormDataRow> rows = fdf.Rows;
			String tableName = fdf.Name;
			if (rows != null && !rows.isEmpty()) {
				FormDataRow row = rows.get(0);
				Map<String, String> m = row.Row;
				String key = m.keySet().iterator().next().toString();
				String value = m.get(key);
				ret = tableName + ";" + key + ";" + value;
			}
		}
		//modify yanjian 2012-0205 15:32	保存表单数据同时将业务信息保存到另外一张业务表中
		VoFormDataBean bean = new VoFormDataBean(ret, isReturnFormData,
				lstDeleteFormAttachment);
		Tjbpmbusiness model = jbpmbusinessManager.findByBusinessId(bean.getBusinessId());
		if(model == null){
			model = new Tjbpmbusiness(bean.getBusinessId());
		}
		jbpmbusinessManager.saveModel(model);
		return bean;
//		return new VoFormDataBean(ret, isReturnFormData,
//				lstDeleteFormAttachment);
	}

	/**
	 * 保存表单模板数据
	 * 
	 * @param data
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 */
	public VoFormDataBean saveFormData(File data) throws ServiceException,
			DAOException, SystemException {
		try {
			return this.saveFormData(new FileInputStream(data));
		} catch (FileNotFoundException e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 保存表单数据
	 * 
	 * @param data
	 *            表单模板数据流
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 */
	/*
	 * public Object[] doSaveFormData(InputStream data) throws ServiceException,
	 * DAOException, SystemException { List<File> lstDeleteFormAttachment = new
	 * ArrayList<File>(); InputStream is =
	 * eformService.SaveFormData(data,lstDeleteFormAttachment); return new
	 * Object[]{is,lstDeleteFormAttachment}; }
	 */

	/**
	 * 得到表单模板对应的主表名称
	 */
	public String getTable(String formId) {
		// 得到表单对应的XML数据
		byte[] content = manager.get(formId).getContent();
		FormTemplate formTemplate = null;
		try {
			formTemplate = FormTemplate.Parser(FileUtil
					.ByteArray2InputStream(content));
			if(formTemplate.Dataset==null){
				return null;
			}
			String tableName = formTemplate.Dataset.Name;
			return tableName;
		} catch (SystemException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到表单下所有控件及其属性
	 * 
	 * @author:邓志城
	 * @date:2011-6-15 上午09:26:03
	 * @param formId
	 *            表单模板id
	 * @return 控件列表
	 */
	public List<EFormComponent> getFormtemplateComponents(String formId) {
		List<EFormComponent> fieldMap = new ArrayList<EFormComponent>();
		try {
			fieldMap.addAll(getFieldInfo(formId).values());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldMap;
	}
	/**
	 * 获取所有表单的id
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Apr 28, 2012 6:47:13 PM
	 */
	public List getAllTEFormTemplateId() throws DAOException, ServiceException,
	    SystemException {
	    return manager.getAllTEFormTemplateId();
	}
	
	/**
	 * 根据表单模板id得到控件属性
	 * 
	 * @author:邓志城
	 * @date:2010-11-10 下午05:32:02
	 * @param formId
	 * @param containsNotBindDataBase
	 *            是否需要查询出未绑定数据表的控件
	 * @param isReturnTable
	 *            是否需要返回主表名称
	 * @return Map<控件所绑定字段名称,EFormComponent>
	 */
	private Map<String, EFormComponent> getFieldInfo(String formId,
			Boolean containsNotBindDataBase, Boolean isReturnTable)
			throws Exception {
		byte[] formData = null;
		Map<String, EFormComponent> fieldMap = new HashMap<String, EFormComponent>();
		formData = manager.get(formId).getContent();
		FormTemplate formTemplate = FormTemplate.Parser(FileUtil
				.ByteArray2InputStream(formData));// 解析表单模板
		if (formTemplate != null) {
			if (isReturnTable) {
				EFormComponent mainTable = new EFormComponent();
				mainTable.setTableName(formTemplate.Dataset.Name);// 得到主表名称
				fieldMap.put(MAINTABLENAME, mainTable);
			}
			List<FormTemplateForm> formTemplateForms = formTemplate.Forms;
			if (formTemplateForms != null && !formTemplateForms.isEmpty()) {
				for (FormTemplateForm formTemplateForm : formTemplateForms) {
										
					List<FormTemplatePage> formTemplatePages = formTemplateForm.Pages;
					if (formTemplatePages != null
							&& !formTemplatePages.isEmpty()) {
						for (FormTemplatePage formTemplatePage : formTemplatePages) {
							List<FormTemplateComponent> formTemplateComponents = formTemplatePage.Components;
							if (formTemplateComponents != null
									&& !formTemplateComponents.isEmpty()) {
								for (FormTemplateComponent formTemplateComponent : formTemplateComponents) {
									EFormComponent eformField = new EFormComponent();
									eformField = eformField
											.toBean(formTemplateComponent);
									if (!containsNotBindDataBase) {
										if (eformField.getTableName() != null
												&& !"".equals(eformField
														.getTableName())
												&& eformField.getFieldName() != null 
												&& !"".equals(eformField
														.getFieldName())) {// 只保存绑定了字段的控件
											fieldMap
													.put(eformField
															.getFieldName(),
															eformField);
										}else if(eformField.getTableName() != null
												&& !"".equals(eformField
														.getTableName())
												&& eformField.getFileNameField() != null){//只绑定了字段的附件

											//保存绑定了字段的附件
											eformField = new EFormComponent();
											
											Map<String, String> properties = formTemplateComponent.Properties;
											eformField.setCaption(properties.get("Caption"));
											eformField.setType(formTemplateComponent.Type);
											eformField.setName(formTemplateComponent.Name);
											eformField.setTableName(properties
													.get("TableName"));
											eformField.setFieldName(properties
													.get("FileContentField"));
											eformField.setValue(properties
													.get("FileNameField"));
											fieldMap.put(properties.get("FileNameField"),
													eformField);
											
										}
									} 
									else {
										// fieldMap.put(eformField.getFieldName(),
										// eformField);
										fieldMap.put(eformField.getName(),
												eformField);
									}
								}
							}
						}
					} else {// Office控件
						if (formTemplateForm.Type.equals("Office")) {
							EFormComponent eformField = new EFormComponent();
							eformField.setCaption(formTemplateForm.Name);
							eformField.setType(formTemplateForm.Type);
							Map<String, String> properties = formTemplateForm.Properties;
							eformField.setName(properties
									.get("OfficeControlName"));
							eformField.setTableName(properties
									.get("DocumentTable"));
							eformField.setFieldName(properties
									.get("DocumentContentField"));
							eformField.setValue(properties
									.get("DocumentNameField"));
							fieldMap.put(properties.get("OfficeControlName"),
									eformField);
						}
						
						if(formTemplateForm.Type.equals("Iframe")){
							EFormComponent eformField = new EFormComponent();
							eformField.setCaption(formTemplateForm.Name);
							Map<String, String> properties = formTemplateForm.Properties;
							eformField.setName(formTemplateForm.Name);
							eformField.setType(formTemplateForm.Type);
							eformField.setTableName(properties.get("Table"));
							eformField.setFieldName(properties.get("TableField"));
							eformField.setValue(properties.get("PageContent"));
							
							fieldMap.put(formTemplateForm.Name,eformField );
																					
						}
					}
					if(formTemplateForm.Type.equals("Blank")){						
						EFormComponent eformField = new EFormComponent();
						eformField.setCaption(formTemplateForm.Name);
						//eformField.setName(formTemplateForm.Name);
						eformField.setType(formTemplateForm.Type);
						
						fieldMap.put(formTemplateForm.Name,eformField );
						
						
					}
				}
			}
		}
		return fieldMap;

	}

	/**
	 * 根据表单模板id得到控件属性
	 * 
	 * @author:邓志城
	 * @date:2010-11-10 下午05:32:02
	 * @param formId
	 * @return Map<控件所绑定字段名称,EFormComponent>
	 */
	public Map<String, EFormComponent> getFieldInfo(String formId)
			throws Exception {
		return this.getFieldInfo(formId, false, true);
	}
	
	/**
	 * 从信息集中获取表名称
	 */
	public List<String> GetTables() throws SystemException, DAOException,
			ServiceException {
		formProperties = new Properties();
		try {
			this.formProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("form.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> result = new ArrayList<String>();
		List<ToaSysmanageStructure> ssList = infoSetManager.getStructureSets();
		if(!ssList.isEmpty()){
			for(ToaSysmanageStructure ss : ssList){
				result.add(ss.getInfoSetValue());
			}
		}
//		List<String> lst = baseManager.getTables();
//		if (!lst.isEmpty()) {
//			// 根据form.properties中的参数设置过滤出允许访问的表
//			String allowTable = (String) formProperties.getProperty(
//					"filter.table", null);
//			if (allowTable == null) {
//				return lst;
//			}
//			AntPathMatcher matcher = new AntPathMatcher();
//			for (String table : lst) {
//				String[] allowTables = allowTable.split(",");
//				boolean flag = false;
//				for (String at : allowTables) {
//					 String tempTable = table.toLowerCase();
//					 at = at.toLowerCase();
//					if (matcher.match(at, tempTable)) {
//						flag = true;
//						break;
//					}
//				}
//				if (flag) {
//					result.add(table);
//				}
//			}
//		}
		return result;
	}

	/**
	 * 加工同步表结构(修改FormTemplateTable对象中的表列信息，可以实现对同步的表列增加业务系统必须的列)。
	 * 
	 * @param formTemplateTable
	 *            FormTemplateTable
	 * @param synchronizeMode -
	 *            同步表结构方式，mode:contain - 包含 / same -
	 *            相同(由FormDesigner调用为same(相同)方式)。
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 */
	public void ProcessSynchronizeTableStruct(
			FormTemplateTable formTemplateTable, String synchronizeMode)
			throws SystemException, DAOException, ServiceException {
		List<FormTemplateColumn> columns = formTemplateTable.Columns;
		List<String> columnName = new ArrayList<String>(columns.size());
		if (columns != null && !columns.isEmpty()) {
			for (FormTemplateColumn column : columns) {
				columnName.add(column.Name);
			}
		}
		// 增加默认初始化字段
		if (!columnName.contains(BaseWorkflowManager.WORKFLOW_TITLE)) {
			FormTemplateColumn workflowTitle = new FormTemplateColumn();
			workflowTitle.Display = "标题";
			workflowTitle.Name = BaseWorkflowManager.WORKFLOW_TITLE;
			workflowTitle.Type = "String";
			workflowTitle.Size = "500";
			workflowTitle.IsNullable = true;
			workflowTitle.IsPrimaryKey = false;
			columns.add(workflowTitle);
		}
		// ---------------------------------------
		if (!columnName.contains(BaseWorkflowManager.WORKFLOW_STATE)) {
			FormTemplateColumn WORKFLOWSTATE = new FormTemplateColumn();
			WORKFLOWSTATE.Display = "状态";
			WORKFLOWSTATE.Name = BaseWorkflowManager.WORKFLOW_STATE;
			WORKFLOWSTATE.Type = "String";
			WORKFLOWSTATE.Size = "1";
			WORKFLOWSTATE.IsNullable = true;
			WORKFLOWSTATE.IsPrimaryKey = false;
			columns.add(WORKFLOWSTATE);
		}
		// ------------------------------------------
		if (!columnName.contains(BaseWorkflowManager.WORKFLOW_AUTHOR)) {
			FormTemplateColumn WORKFLOWAUTHOR = new FormTemplateColumn();
			WORKFLOWAUTHOR.Display = "拟稿人";
			WORKFLOWAUTHOR.Name = BaseWorkflowManager.WORKFLOW_AUTHOR;
			WORKFLOWAUTHOR.Type = "String";
			WORKFLOWAUTHOR.Size = "32";
			WORKFLOWAUTHOR.IsNullable = true;
			WORKFLOWAUTHOR.IsPrimaryKey = false;
			columns.add(WORKFLOWAUTHOR);
		}
		// -------------------------------------------
		if (!columnName.contains(BaseWorkflowManager.WORKFLOW_CODE)) {
			FormTemplateColumn WORKFLOWCODE = new FormTemplateColumn();
			WORKFLOWCODE.Display = "编号";
			WORKFLOWCODE.Name = BaseWorkflowManager.WORKFLOW_CODE;
			WORKFLOWCODE.Type = "String";
			WORKFLOWCODE.Size = "100";
			WORKFLOWCODE.IsNullable = true;
			WORKFLOWCODE.IsPrimaryKey = false;
			columns.add(WORKFLOWCODE);
		}
		// ---------------------------------------------
		if (!columnName.contains(BaseWorkflowManager.WORKFLOW_NAME)) {
			FormTemplateColumn WORKFLOWNAME = new FormTemplateColumn();
			WORKFLOWNAME.Display = "流程名称";
			WORKFLOWNAME.Name = BaseWorkflowManager.WORKFLOW_NAME;
			WORKFLOWNAME.Type = "String";
			WORKFLOWNAME.Size = "255";
			WORKFLOWNAME.IsNullable = true;
			WORKFLOWNAME.IsPrimaryKey = false;
			columns.add(WORKFLOWNAME);
		}
//		 ---------------------------------------------
		if (!columnName.contains(BaseWorkflowManager.PERSON_CONFIG_FLAG)) {
			FormTemplateColumn PERSON_CONFIG_FLAG = new FormTemplateColumn();
			PERSON_CONFIG_FLAG.Display = "紧急程度";
			PERSON_CONFIG_FLAG.Name = BaseWorkflowManager.PERSON_CONFIG_FLAG;
			PERSON_CONFIG_FLAG.Type = "String";
			PERSON_CONFIG_FLAG.Size = "2";
			PERSON_CONFIG_FLAG.IsNullable = true;
			PERSON_CONFIG_FLAG.IsPrimaryKey = false;
			columns.add(PERSON_CONFIG_FLAG);
		}
		if (!columnName.contains(BaseWorkflowManager.PERSON_DEMO)) {
			FormTemplateColumn PERSON_DEMO = new FormTemplateColumn();
			PERSON_DEMO.Display = "内容";
			PERSON_DEMO.Name = BaseWorkflowManager.PERSON_DEMO;
			PERSON_DEMO.Type = "Lob";
			PERSON_DEMO.IsNullable = true;
			PERSON_DEMO.IsPrimaryKey = false;
			columns.add(PERSON_DEMO);
		}
		columnName.clear();
		columnName = null;
		formTemplateTable.Columns = columns;
	}

	public List<Map<String, String>> GetDictionary(String arg0, String arg1,
			String arg2, String arg3) throws ServiceException, DAOException,
			SystemException {
		// TODO Auto-generated method stub
		return null;
	}

}
