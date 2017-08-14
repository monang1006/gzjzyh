package com.strongit.oa.common.eform;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.strongit.oa.bo.TEFormTemplate;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.service.VoFormDataBean;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 电子表单接口
 * 
 * @author dengwenqiang
 * @version 1.0
 */
public interface IEFormService {
	
	//目前支持：单行文本框、日期、下拉列表、多行文本框的查询
	//new String[]{"TEFEdit","TEFDateTimePicker","TEFComboBox","TEFMemo"};
	public static String[] components = new String[]{"Strong.Form.Controls.Edit",
							"Strong.Form.Controls.DateTimePicker","Strong.Form.Controls.ComboxBox"};
	
	public static final String MAINTABLENAME = "MAIN_TABLE_NAME";

	public List<String> GetTablesByXML();

	/**
	 * 注入表单配置文件
	 * @param formProperties
	 */
	public void setFormProperties(Properties formProperties);
	
	/**
	 * 根据表单模板id得到表单模板对象
	 * @param id
	 * @return
	 */
	public TEFormTemplate get(String id);

	/**
	 * 保存附件
	 * @param attachment					附件内容
	 * @param attachmentName				附件名称
	 * @param subPath						附件相对路径
	 * @throws ServiceException		
	 * @throws DAOException
	 * @throws SystemException
	 */
	public void SaveAttachment(InputStream attachment, String attachmentName, String subPath)
    			throws ServiceException, DAOException, SystemException;
	
	/**
	 * 删除附件
	 * @param attachmentName			存储在硬盘上的附件名称
	 * @param subPath					相对路径
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 */
	public void RemoveAttachment(String attachmentName, String subPath)
    			throws ServiceException, DAOException, SystemException ;
	
	public InputStream LoadAttachment(String attachmentName, String subPath)
    		throws ServiceException, DAOException, SystemException ;
	
	/**
	 * 加载模板数据
	 * @param data
	 * @return
	 */
	public InputStream loadFormData(InputStream data);
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
	    SystemException;
	/**
	 * 根据表单模板id得到控件属性
	 * @author:邓志城
	 * @date:2010-11-10 下午05:32:02
	 * @param formId
	 * @return
	 * 	Map<控件名称,EFormComponent>
	 */
	public Map<String, EFormComponent> getFieldInfo(String formId) throws Exception;
	
	/**
	 * 得到表单下所有控件及其属性
	 * @author:邓志城
	 * @date:2011-6-15 上午09:26:03
	 * @param formId			表单模板id
	 * @return					控件列表
	 */
	public List<EFormComponent> getFormtemplateComponents(String formId);
	
	/**
	 * 获取电子表单模板列表
	 * @param String formType
	 * <p>表单类型,如果传入null则取所有表单</p>
	 * @param List 表单模板对象EFormField集合EForm对象列表
	 */
	public List<EForm> getFormTemplateList(String formType) throws ServiceException, DAOException, SystemException;
	/**
	 * 获取jsp表单模板列表
	 * @param String formType
	 * <p>表单类型,如果传入null则取所有表单</p>
	 * @param List 表单模板对象EFormField集合EForm对象列表
	 */
	public List<EForm> getJSpFormTemplateList(String formType) throws ServiceException, DAOException, SystemException;

	/**
	 * 根据表单ID得到表单所挂接主表
	 * @author:邓志城
	 * @date:2010-11-9 上午09:34:38
	 * @param formId
	 * @return
	 */
	public String getTable(String formId) ;
	
	/**
	 * 获取电子表单模板域信息列表
	 * @param templateId 电子模板ID
	 * @return List 表单域对象EFormField集合
	 */
	public List<EFormField> getFormTemplateFieldList(String templateId) throws ServiceException, DAOException, SystemException;
	
	/**
	 * 获取电子表单模板中指定域的值
	 * @param templateId 电子模板ID
	 * @param fieldId 域ID
	 * @param dataId 数据ID
	 * @return String 域值
	 */
	public String getFormTemplateFieldValue(String templateId, String fieldId,
			String dataId) throws ServiceException, DAOException, SystemException;
	
	/**
	 * 保存电子表单数据
	 * @param data XML格式的电子表单数据值对象
	 * @return 
	 * 	Object[]{}
	 * 	 {0}String "表名,主键字段名,主键值" {1}返回的流信息
	 */
	public VoFormDataBean saveFormData(InputStream data) throws ServiceException, DAOException, SystemException;
	
	public List<EFormField> getFormTemplateComponents(String templateId) throws ServiceException, DAOException, SystemException ;
	
	/**
	 * 保存表单数据
	 * @param data					表单模板数据流
	 * @return	
	 * 			new Object[]{表单模板数据流,要删除的附件列表};
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 */
	//public Object[] doSaveFormData(InputStream data) throws ServiceException, DAOException, SystemException;
	
	/**
	 * 保存表单模板数据
	 * @param model
	 */
	public void saveFormTemplate(TEFormTemplate model);
	
	/**
	 * 保存表单模板数据
	 * @param data
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 */
	public VoFormDataBean saveFormData(File data) throws ServiceException, DAOException, SystemException;
}
