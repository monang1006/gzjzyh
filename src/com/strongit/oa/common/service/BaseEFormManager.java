package com.strongit.oa.common.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.util.MessagesConst;
import com.strongit.workflow.exception.WorkflowException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 抽象电子表单相关操作
 * 这里继承工作流管理类是因为电子表单操作有些方法依赖工作流提供的接口。
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2009-12-11 下午04:32:17
 * @version  2.0.2.3
 * @classpath com.strongit.oa.common.service.BaseEFormManager
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
@Transactional
public class BaseEFormManager extends BaseWorkflowManager{

	@Autowired protected IEFormService eform;		//电子表单服务
	
	/**
	 * 得到表单模板列表
	 * @author:邓志城
	 * @date:2009-12-11 下午04:25:22
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public List<EForm> getFormList()throws SystemException,ServiceException{
		try {
			return eform.getFormTemplateList(null);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"表单模板列表"});
		}
	}
	/**
	 * 得到JSp表单模板列表
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public List<EForm> getJspFormList()throws SystemException,ServiceException{
		try {
			return eform.getJSpFormTemplateList(null);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"表单模板列表"});
		}
	}

	/**
	 * 根据数据id得到指定的字段值
	 * @param tableName				表名称
	 * @param pkFieldName			主键名称
	 * @param pkFieldValue			主键值
	 * @param toQueryFieldName		需要查询的字段
	 * @return
	 */
	public Object getFormTemplateFieldValue(String tableName, String pkFieldName,String pkFieldValue,String toQueryFieldName) {
		StringBuilder sql = new StringBuilder("select ").append(toQueryFieldName).append(" from ");
		sql.append(tableName).append(" where ").append(pkFieldName).append(" = '").append(pkFieldValue).append("'");
		Map map = jdbcTemplate.queryForMap(sql.toString());
		return map.get(toQueryFieldName);
	}

	
	
	/**
	 * 根据流程类型获取所有挂接了流程的表单
	 * @author:邓志城
	 * @date:2009-5-8 上午10:02:59
	 * @return
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public List<EForm> getAllEFormByWorkFlowType()throws SystemException{
		List<Object[]> typeList = getAllProcessTypeList();
		List<EForm> formLst = new ArrayList<EForm>();
		for(int i=0;i<typeList.size();i++){
			Object[] obj = typeList.get(i);
			if(obj[0]!=null){
				List<EForm> eformList = getRelativeFormByProcessType(obj[0].toString());
				for(EForm eform : eformList){
					if(!this.isEformExist(formLst, eform)){ 
						formLst.add(eform);
					}
				}
						
			}
		}
		return formLst;
	}
	
	/**
	 * 表单是否已存在
	 * @author:邓志城
	 * @date:2010-1-9 下午02:54:18
	 * @param list
	 * @param eform
	 * @return
	 */
	private boolean isEformExist(List<EForm> list,EForm eform){
		for(EForm e : list){
			if(e.getId().equals(eform.getId())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据流程类型Id获取当前用户所拥有的所有启动表单
	 * @param processTypeId -流程类型Id
	 * @return List<Object[]> 启动表单信息集<br>
	 * 		<p>数据结构：</p>
	 * 		<p>Object[]{表单Id}</p>
	 * @throws WorkflowException
	 * 
	 * 
	 * 修改人：彭小青
	 * 修改时间：2010-01-21
	 * 修改原因：收发文表单是可配置的，因为传过来的listObj是已经排序的，所以要将listObj得循环放在外层
	 */
	public List<EForm> getRelativeFormByProcessType(String processTypeId) throws SystemException,ServiceException{
		try {
			List listObj = workflow.getRelativeFormByProcessType(processTypeId);
			List<EForm> formList = new ArrayList<EForm>();
			List<EForm> formTemplateList = getFormList();
			for(int i=0;i<listObj.size();i++){
				String formId = String.valueOf(listObj.get(i));
				for(Iterator<EForm> it=formTemplateList.iterator();it.hasNext();){//加入这段代码是为了获取表单名称
					EForm ef = it.next();
					String formTemplateId = ef.getId().toString();
					if(formTemplateId.equals(formId)){
						EForm tempEForm = new EForm();
						tempEForm.setId(Long.parseLong(formId));
						tempEForm.setTitle(ef.getTitle());
						tempEForm.setFlowType(processTypeId);
						formList.add(tempEForm);
					}
				}
			}
			return formList;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"获取表单"});
		}
	}
	
	/**
	 * 获取所有查询表单
	 * @author:邓志城
	 * @date:2009-5-23 上午09:51:24
	 * @return 查询表单列表
	 * @throws SystemException
	 */
	public List<EForm> getAllQueryForm()throws SystemException{
		return eform.getFormTemplateList(com.strongit.oa.common.eform.constants.Constants.QF);
	}
	
	/**
	 * 获取所有展现表单
	 * @author:邓志城
	 * @date:2009-6-4 下午03:20:50
	 * @return
	 * @throws SystemException
	 */
	public List<EForm> getAllViewForm()throws SystemException{
		return eform.getFormTemplateList(com.strongit.oa.common.eform.constants.Constants.VF);
	}
	
	/**
	 * 获取电子表单模板域信息列表
	 * @param templateId 电子模板ID
	 * @return List 表单域对象EFormField集合
	 */
	public List<EFormField> getFormTemplateFieldList(String formId)throws SystemException{
		return eform.getFormTemplateFieldList(formId);
	}

	/**
	 * 得到将主键放至在第一个位置后的列表.
	 * @author:邓志城
	 * @date:2010-3-1 下午07:02:58
	 * @param formId 表单模板id
	 * @return 字段列表
	 */
	public List<EFormField> getFormTemplateFieldOrderByPrimaryKey(String formId){
		List<EFormField> eformFieldList = getFormTemplateFieldList(formId);
		String tableName = null ;
		String pkName = null;
		EFormField pkEformField = null;
		for(EFormField eformField : eformFieldList){
			if(eformField.getFieldname()==null ){//yanjian 2011-12-17 20:42 
				continue;
			}
			if(tableName == null){
				tableName = eformField.getTablename();
				pkName = super.getPrimaryKeyName(tableName);
			}
			if(eformField.getFieldname().equalsIgnoreCase(pkName)){
				pkEformField = eformField ;
			}
		}
		eformFieldList.remove(pkEformField);
		eformFieldList.add(0, pkEformField);
		return eformFieldList;
	}

	/**
	 * 根据电子表单得到域信息,包括数据库中的字段信息：类型完整名称、字段长度、字段SQL类型、字段类型名称
	 * @author:邓志城
	 * @date:2010-3-2 下午02:37:25
	 * @param formId 表单模板id
	 * @return 字段信息列表
	 */
	public List<EFormField> getFormTemplateFieldListWithColumnInfo(String formId) {
		try{
			List<EFormField> eformFieldList = getFormTemplateFieldList(formId);
			List<EFormField> fieldList = new ArrayList<EFormField>();
			String tableName = null ;
			for(EFormField eformField : eformFieldList){
				if(tableName == null){
					tableName = eformField.getTablename();
					if(tableName == null){ continue;}
					Map<String,EFormField> columnInfo = super.getColumnInfo(tableName);
					for(int i=0;i<eformFieldList.size();i++){
						EFormField field = eformFieldList.get(i);
						EFormField column = columnInfo.get(field.getFieldname());
						if(column != null){
							field.setFieldClassName(column.getFieldClassName());
							field.setFieldDiaplaySize(column.getFieldDiaplaySize());
							field.setFieldType(column.getFieldType());
							field.setFieldTypeName(column.getFieldTypeName());
						}
						fieldList.add(field);
					}
					break;
				}
			}
			return fieldList ; 
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 得到电子表单组件名，返回JSON格式。
	 * @author:邓志城
	 * @date:2009-12-31 下午02:03:17
	 * @param formId 表单模板id
	 * @param type 如果为空返回所有组件名，JSON格式
	 * @return JSON格式组件名 
	 */
	public String getFormComponent(String formId,String type) {
		//List<EFormField> list = eform.getFormTemplateComponents(formId);
		List<EFormComponent> list = eform.getFormtemplateComponents(formId);
		JSONArray components = new JSONArray();
		for(EFormComponent field : list){
			if(StringUtils.hasLength(field.getType())){
				if(!StringUtils.hasLength(type)){
					JSONObject component = new JSONObject();
					component.put("fieldName", field.getName());
					component.put("fieldType", field.getType());
					component.put("visible", field.isVisible());
					components.add(component);
				}else{
					if(field.getType().equals(type)){
						JSONObject component = new JSONObject();
						component.put("fieldName", field.getName());
						component.put("fieldType", field.getType());
						component.put("visible", field.isVisible());
						components.add(component);
					}					
				}
			}
		}
		return components.toString();
	}
}
