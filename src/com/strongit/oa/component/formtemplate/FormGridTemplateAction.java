package com.strongit.oa.component.formtemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.strongit.form.action.BaseAction;
import com.strongit.form.action.FormGridAware;
import com.strongit.form.utils.FormUtils;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.component.formtemplate.model.FormGridBean;
import com.strongit.oa.component.formtemplate.util.FormGridDataHelper;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * 电子表单列表组件实现.
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Jan 16, 2012
 * @classpath	com.strongit.oa.component.formtemplate.FormGridTemplateAction
 * @version  	5.0
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
@ParentPackage("default")
public class FormGridTemplateAction extends BaseAction implements FormGridAware,ModelDriven<TaskBean> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private int pageNo;
	
	private int pageSize;
	
	private String tableName;
	
	private String formColumns;
	
	private String type;//查询类型 0：待办事宜，1：已办事宜，2：主办事宜
	
	private String todoType;//待办公文签收类型 notsign：签收节点上的公文；sign：非签收节点上的公文
							//已办事宜 排序方式
	
	private String action; //设置分页动作(first / prev / goto / next / last)。
	
	private TaskBean model = new TaskBean();
	
	@Autowired
	private FormGridTemplateService gridService;
	
	@Autowired
	private IUserService userService;
	
	public InputStream loadFormGridData()  throws SystemException, DAOException,
									ServiceException {
		InputStream responseData = null;
		try {
			if(type == null || type.length() == 0) {
				throw new SystemException("参数type不可为空！");
			}
			User user = userService.getCurrentUser();
			if(pageNo == 0) {
				pageNo = 1;
			}
			Page<Map<String, String>> page = gridService.loadData(pageNo, pageSize, user.getUserId(), model,
						tableName,formColumns,LoadDataType.getLoadDataType(type),todoType);
			FormGridBean formGridBean = FormGridDataHelper.generateFormGridData(page);
			List<Map<String, String>> rsList = formGridBean.getRsList();
			Map<String, String> rsAttrib = formGridBean.getRsAttrib();
			byte[] strData = FormUtils.GenerateFormGridDataToXML(rsList, 
											rsAttrib.get(FormGridDataHelper.FORM_GRID_TABLENAME), 
											rsAttrib.get(FormGridDataHelper.FORM_GRID_PKFIELDNAME), 
											Long.valueOf(rsAttrib.get(FormGridDataHelper.FORM_GRID_COUNT)),pageNo,true);
			logger.info(new String(strData,0,strData.length,"utf-8"));
			responseData = new ByteArrayInputStream(strData);
		} catch (SystemException ex) {
			throw ex;
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
		return responseData;
	}

	public TaskBean getModel() {
		return model;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setType(String type) {
		this.type = type;
	}

	 /**
     * 设置分页动作(first / prev / goto / next / last)。
     * @param action String
     */
    public void setFormPageAction(String action) {
    	this.action = action;
    }
	
	public void setFormColumns(String arg0) {
		formColumns = arg0;
	}

	public void setFormPage(int arg0) {
		this.pageNo = arg0;
	}

	public void setFormPageSize(int arg0) {
		this.pageSize = arg0;
	}

	public void setTodoType(String todoType) {
		this.todoType = todoType;
	}
}
