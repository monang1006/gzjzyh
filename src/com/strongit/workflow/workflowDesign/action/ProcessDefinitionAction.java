package com.strongit.workflow.workflowDesign.action;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSystemformModel;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.util.DESPlus;
import com.strongit.workflow.util.TimeKit;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 流程模型文件管理Action
 * 
 * @author 喻斌
 * @company Strongit Ltd. (C) copyright
 * @date Dec 12, 2008 8:49:15 AM
 * @version 1.0.0.0
 * @classpath com.strongit.workflow.workflowDesign.action.ProcessDefinitionAction
 */
@ParentPackage("default")
@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "processFile.action", type = ServletActionRedirectResult.class),
		@Result(name = BaseActionSupport.INPUT, value = "/workflow/designer/designmain.jsp", type = ServletDispatcherResult.class),
		@Result(name = "forms", value = "/WEB-INF/jsp/workflowDesign/action/processDefinition-allForms.jsp", type = ServletDispatcherResult.class),
		@Result(name = "subProcess", value = "/WEB-INF/jsp/workflowDesign/action/processDefinition-subProcess.jsp", type = ServletDispatcherResult.class),
		@Result(name = "initImport", value = "/WEB-INF/jsp/workflowDesign/action/processDefinition-initImport.jsp", type = ServletDispatcherResult.class),
		@Result(name = "fileImport", value = "/WEB-INF/jsp/workflowDesign/defaultpage/fileImport_return.jsp", type = ServletDispatcherResult.class),
		@Result(name = "processDefinitionAdd", value = "/WEB-INF/jsp/workflowDesign/defaultpage/processDefinition_save_return.jsp", type = ServletDispatcherResult.class) })
public class ProcessDefinitionAction extends
		BaseActionSupport<TwfBaseProcessfile> {
	List<Object[]> forms;

	List<Object[]> types;

	List<Object[]> subProcesses;
	//挂接的子流程Id
	private String subProcessId;

	private String pfId;

	// 流程文件操作类型：1.新增、2.修改、3.重设计
	private String type;

	// 流程文件内容（流程添加修改及导出导出时使用）
	private String processfile;

	// 导入流程文件流
	private File processFile;

	// 流程名称(流程添加修改时使用)
	private String processName;

	// 流程类型（流程添加修改时使用）
	private String processType;

	// 设置流程表单时排除的表单ID
	private String formId;
	
	private String formModelId;	//表单模板ID

	private TwfBaseProcessfile model = new TwfBaseProcessfile();

	private IProcessDefinitionService manager;
	
	@Autowired ProcessDesignManager designManager;

	public String getPfId() {
		return pfId;
	}

	public void setModel(TwfBaseProcessfile model) {
		this.model = model;
	}

	/**
	 * @roseuid 493692F5002E
	 */
	public ProcessDefinitionAction() {

	}

	/**
	 * Sets the value of the infoSetCode property.
	 * 
	 * @param aInfoSetCode
	 *            the new value of the infoSetCode property
	 */
	public void setPfId(java.lang.String pfId) {
		this.pfId = pfId;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public TwfBaseProcessfile getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(IProcessDefinitionService aManager) {
		manager = aManager;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936902101B5
	 */
	@Override
	public String list() throws Exception {
		// this.types = manager.getAllProcessTypeList();
		this.types = manager.getAllProcessTypeList();
		this.forms = manager.getAllFormsList("");
		return SUCCESS;
	}

	/**
	 * @roseuid 49369077005D
	 */
	@Override
	public void prepareModel() {
		if (pfId != null && !"".equals(pfId)) {
			model = manager.getProcessfileByPfId(pfId);
		} else {
			model = new TwfBaseProcessfile();
		}
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936910B034B
	 */
	@Override
	public String save() throws Exception {
		String saveType = this.getRequest().getParameter("saveType");
		if ("save".equals(saveType)) {
			manager.saveProcessFile(processfile, processName, processType);
		} else if ("edit".equals(saveType)) {
			manager.editProcessFile(processfile, processName);
		}
		addActionMessage("信息保存成功");
		return "processDefinitionAdd";
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936911402EE
	 */
	@Override
	public String delete() throws Exception {
		try {
			String ids = getRequest().getParameter("ids");
			manager.delProcessType(ids);
			addActionMessage("信息删除成功");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			addActionMessage(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return RELOAD;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936911F033C
	 */
	@Override
	public String input() throws Exception {
		Properties properties = new Properties();
	    InputStream is = ProcessDefinitionAction.class.getClassLoader().getResourceAsStream("workflow.properties");
	    try {
	      properties.load(is);
	    }
	    catch (IOException e) {
	      throw e;
	    }finally{
	    	is.close();
	    }
	    String wfversion = properties.getProperty("workflowversion");
	    this.getRequest().setAttribute("wfversion", wfversion);
		
		if (model.getPfContent() != null && !"".equals(model.getPfContent())) {
			String content = model.getPfContent().replaceAll("\"", "\\\\\\\"");
			content = content.replaceAll("\\r\\n", "");
			content = content.replaceAll("\\n", "");
			this.setProcessfile(content);
		} else {
			this.setType("add");
		}
		this.setProcessName(model.getPfName());
		return INPUT;
	}

	/**
	 * 设置节点挂接表单，及子流程表单
	 * 
	 * @author 喻斌
	 * @date Dec 12, 2008 12:48:11 PM
	 * @return
	 * @throws Exception
	 */
	public String allForms() throws Exception {
		this.forms = manager.getAllFormsList(formId);
		return "forms";
	}

	/**
	 * 根据表单Id找到表单所有域信息（jquery使用）
	 * 
	 * @author 喻斌
	 * @date Dec 17, 2008 10:30:03 AM
	 * @return
	 * @throws Exception
	 */
	public String getFormDomains() throws Exception {
		String formId = this.getRequest().getParameter("formId");
		List domains = manager.getFormDomains(formId);
		Object[] obj;
		StringBuffer returnValue = new StringBuffer("");
		for (int i = 0; i < domains.size(); i++) {
			obj = (Object[]) domains.get(i);
			returnValue.append("|").append(obj[0]).append(",").append(obj[1]);
		}
		if (!"".equals(returnValue)) {
			return this.renderText(returnValue.substring(1));
		} else {
			return this.renderText("");
		}
	}

	/**
	 * 设置挂接子流程
	 * 
	 * @author 喻斌
	 * @date Dec 13, 2008 9:24:14 AM
	 * @return
	 * @throws Exception
	 */
	public String subProcess() throws Exception {
		this.subProcesses = manager.getToSetSubProcess();
		return "subProcess";
	}

	/**
	 * 流程文件加密导出
	 * 
	 * @author 喻斌
	 * @date Dec 13, 2008 10:02:06 AM
	 * @return
	 * @throws Exception
	 */
	public String fileExport() throws Exception {
		processfile = processfile.replaceAll("\\r\\n", "");
		HttpServletResponse response = getResponse();
		// response.reset();
		OutputStream outputStream = new BufferedOutputStream(response
				.getOutputStream());
		response.setContentType("application/x-msdownload");
		response.setHeader("content-disposition", "attachment;filename=\""
				+ new String((TimeKit.formatDate(new Date(), "short") + ".xml")
						.getBytes(), response.getCharacterEncoding()) + "\"");
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "GBK");
		processfile = DESPlus.byteArr2HexStr(processfile.getBytes("GBK"));
		writer.write(processfile);
		writer.close();
		outputStream.close();
		return null;
	}

	/**
	 * 流程文件解密导入
	 * 
	 * @author 喻斌
	 * @date Dec 13, 2008 10:03:55 AM
	 * @return
	 * @throws Exception
	 */
	public String fileImport() throws Exception {
		StringBuffer processfile1 = new StringBuffer();
		byte[] array =null;
		if(processFile!=null&&processFile.length()>0&&(formModelId==null||formModelId.length()<1)){
			
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(
					new FileInputStream(processFile), "GBK"));
			String str;
			
			// ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			while ((str = inputStream.readLine()) != null) {
				processfile1.append(str);
			}
			array = DESPlus.hexStr2ByteArr(processfile1.toString(), "GBK");
		}else {
			ToaSystemformModel formModel=designManager.getFormModel(formModelId);
			if(formModel!=null){
				array=formModel.getModelContent();
			}
		}

		String content = new String(array, "GBK").replaceAll("\"", "\\\\\\\"");
		content = content.replaceAll("\\r\\n", "");
		content = content.replaceAll("\\n", "");
		this.processfile = content;
		return "fileImport";
	}

	public String initImport() {
		return "initImport";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IProcessDefinitionService getManager() {
		return manager;
	}

	public String getProcessfile() {
		return processfile;
	}

	public void setProcessfile(String processfile) {
		this.processfile = processfile;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public List<Object[]> getForms() {
		return forms;
	}

	public void setForms(List<Object[]> forms) {
		this.forms = forms;
	}

	public List<Object[]> getTypes() {
		return types;
	}

	public void setTypes(List<Object[]> types) {
		this.types = types;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public List<Object[]> getSubProcesses() {
		return subProcesses;
	}

	public void setSubProcesses(List<Object[]> subProcesses) {
		this.subProcesses = subProcesses;
	}

	public File getProcessFile() {
		return processFile;
	}

	public void setProcessFile(File processFile) {
		this.processFile = processFile;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	/**
	 * @return the subProcessId
	 */
	public String getSubProcessId() {
		return subProcessId;
	}

	/**
	 * @param subProcessId the subProcessId to set
	 */
	public void setSubProcessId(String subProcessId) {
		this.subProcessId = subProcessId;
	}

	public String getFormModelId() {
		return formModelId;
	}

	public void setFormModelId(String formModelId) {
		this.formModelId = formModelId;
	}


}
