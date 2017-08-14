package com.strongit.workflow.workflowDesign.action;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.autoencoder.IRuleService;
import com.strongit.oa.bo.ToaDefinitionPlugin;
import com.strongit.oa.bo.ToaRule;
import com.strongit.oa.bo.ToaSystemformModel;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.plugin.DefinitionPluginService;
import com.strongit.oa.eformJspManager.EformJspManagerManager;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.bo.TwfBaseProcessfile;
import com.strongit.workflow.bo.TwfInfoProcessType;
import com.strongit.workflow.util.DESPlus;
import com.strongit.workflow.util.TimeKit;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 流程模型文件管理Action
 * 
 * @author 喻斌
 * @company Strongit Ltd. (C) copyright
 * @date Dec 12, 2008 8:49:15 AM
 * @version 1.0.0.0
 * @classpath com.strongit.workflow.workflowDesign.action.ProcessDesignAction
 */
@ParentPackage("default")
@Results( {
		@Result(name = BaseActionSupport.SUCCESS, value = "/workflow/designer/actionjsp/processDesign.jsp", type = ServletDispatcherResult.class),
		@Result(name = BaseActionSupport.RELOAD, value = "processFile.action", type = ServletActionRedirectResult.class),
		@Result(name = BaseActionSupport.INPUT, value = "/workflow/designer/designmain.jsp", type = ServletDispatcherResult.class),
		@Result(name ="input", value = "/workflow/designer/designmain.jsp", type = ServletDispatcherResult.class),
		@Result(name = "forms", value = "/workflow/designer/actionjsp/processDesign-allForms.jsp", type = ServletDispatcherResult.class),
		@Result(name = "allforms", value = "/workflow/designer/ext/processDesign-getallForms.jsp", type = ServletDispatcherResult.class),
		@Result(name = "subProcess", value = "/workflow/designer/actionjsp/processDesign-subProcess.jsp", type = ServletDispatcherResult.class),
		@Result(name = "initImport", value = "/workflow/designer/actionjsp/processDesign-initImport.jsp", type = ServletDispatcherResult.class),
		@Result(name = "initTplImport", value = "/workflow/designer/actionjsp/processDesign-initTplImport.jsp", type = ServletDispatcherResult.class),
		@Result(name = "initExport", value = "/workflow/designer/actionjsp/processDesign-initExport.jsp", type = ServletDispatcherResult.class),
		@Result(name = "fileImport", value = "/workflow/designer/actionjsp/fileImport_return.jsp", type = ServletDispatcherResult.class),
		@Result(name = "processDefinitionAdd", value = "/workflow/designer/actionjsp/processDesign_save_return.jsp", type = ServletDispatcherResult.class),
		@Result(name = "workflowmodel", value = "/workflow/designer/ext/processDesign_workflowmodel.jsp", type = ServletDispatcherResult.class) })
		
public class ProcessDesignAction extends
		BaseActionSupport<TwfBaseProcessfile> {
	List<Object[]> forms;
	List<Object[]> jspForm;

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
	

	
	private String modelUrl;	//表单模板URL
	
	private String modelName;  //表单模板名称
	
	private String radioValue;	//是本地，还是URL
	
	private String modelId;   //流程模板ID
	
	Map<String, String> typeMap=new HashMap<String, String>();			//流程模板类型
	
	private Date startDate;											//开始时间
	
	private Date endDate;												//结束时间


	private List<ToaSystemformModel> modelList=new ArrayList<ToaSystemformModel>();    //流程模板List
	
	private Page modelPage=new Page(FlexTableTag.MAX_ROWS,true);		//流程模板分页
	
	private List<TwfInfoProcessType>ptList=new ArrayList<TwfInfoProcessType>();		//流程类型List
	
	private TwfBaseProcessfile model = new TwfBaseProcessfile();
	
	private ToaSystemformModel formModel=new ToaSystemformModel();

	private IProcessDefinitionService manager;
	
	private ProcessDesignManager designManager;
	private EformJspManagerManager jspManager;
	
	
	/** 用户管理Service接口*/
	private IUserService userservice;

	@Autowired DefinitionPluginService definitionPluginService;	//流程定义插件服务类.
	
	@Autowired private IRuleService ruleService;//编号生成器服务类.
	
	private List<ToaRule> list;
	
	private List<ToaDefinitionPlugin> plugins;
	
	public String getPfId() {
		return pfId;
	}

	public void setModel(TwfBaseProcessfile model) {
		this.model = model;
	}

	/**
	 * @roseuid 493692F5002E
	 */
	public ProcessDesignAction() {

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
	 * 保存插件信息
	 * @author:邓志城
	 * @date:2011-2-9 上午11:54:30
	 */
	public void savePlugin() {
		String ret = "0";
		try{
			String pluginParam = getRequest().getParameter("pluginParam");
			if(pluginParam == null || "".equals(pluginParam)) {
				//throw new SystemException("参数pluginParam传递错误！");
				return;
			}
			JSONArray jsonArray = JSONArray.fromObject(pluginParam);
			List<ToaDefinitionPlugin> plugins = new LinkedList<ToaDefinitionPlugin>();
			for(int i=0;i<jsonArray.size();i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);//转换成JSON对象 
				String workflowId = jsonObj.getString("workflowId");//流程定义id
				String workflowName = jsonObj.getString("workflowName");//流程名称
				String pluginName = jsonObj.getString("pluginName");//插件名称
				String pluginValue = jsonObj.getString("pluginValue");//插件值
				ToaDefinitionPlugin plugin = new ToaDefinitionPlugin(pluginName,pluginValue,workflowId,workflowName);
				plugins.add(plugin);
			}
			definitionPluginService.save(plugins,new OALogInfo("保存流程定义插件项，插件信息：" + plugins));
		}catch(Exception e) {
			logger.error("保存流程定义插件出错。", e);
			ret = "-1";
		}
		this.renderText(ret);
	}
	
	/**
	 * @return java.lang.String
	 * @roseuid 4936902101B5
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String list() throws Exception {
		// this.types = manager.getAllProcessTypeList();
		this.types = manager.getAllProcessTypeList();
		this.forms = manager.getAllFormsList("");
		plugins = definitionPluginService.find(pfId);
		list = ruleService.getAllList();
		if(list!= null && !list.isEmpty()){
			for(int i=0;i<list.size();i++) {
				ToaRule rule = (ToaRule)list.get(i);
				String ruleComent = rule.getRule();
				if(ruleComent.indexOf("Array")> 0 ) {//发文规则是否存在变量的，是则过滤
					list.remove(rule);
					i--;
				}
			}
		}
		getRequest().setAttribute("size", list.size());
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
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAllForms() throws Exception {
		this.forms = manager.getAllFormsList("");
		this.jspForm = jspManager.getAllJsp();
		return "allforms";
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
		String subProcessId = this.getRequest().getParameter("subProcessId");
		if(subProcessId != null && !"".equals(subProcessId) && !"null".equalsIgnoreCase(subProcessId)){
			this.getRequest().setAttribute("subProcessId", subProcessId);
		}
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
		String filename = processName + "-" + TimeKit.formatDate(new Date(), "yyyy-MM-dd_HHmmss") + ".xml";
		
		response.setHeader("content-disposition", "attachment;filename=\""
				+ URLEncoder.encode(filename,"utf-8") + "\"");

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
	public String fileImport(){
		StringBuffer processfile1 = new StringBuffer();
		String content = null;
		try {
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(
					new FileInputStream(processFile), "GBK"));
			String str;

			// ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			while ((str = inputStream.readLine()) != null) {
				processfile1.append(str);
			}

			byte[] array = DESPlus.hexStr2ByteArr(processfile1.toString(), "GBK");
			content = new String(array, "GBK").replaceAll("\"", "\\\\\\\"");
			content = content.replaceAll("\\r\\n", "");
			content = content.replaceAll("\\n", "");
		} catch (Exception e) {
			this.processfile = "false";
			return "fileImport";
		}
		
		if(content != null && "<docroot>".equals(content.substring(0, 9))){
			this.processfile = content;
		}else{
			this.processfile = "false";
		}
		
		return "fileImport";
	}	
	
	/**
	 * 根据选择流程类型id,获取当前流程下的所有表单模板
	 * @author zhengzb
	 * @desc 
	 * 2010-11-8 下午03:20:45 
	 * @return
	 * @throws Exception··
	 */
	public String select() throws Exception{
		StringBuffer strb=new StringBuffer();
		modelList=designManager.getModelListByPtId(processType);
		
		if(modelList!=null){
			for(ToaSystemformModel formModel:modelList){

				strb.append("<option value='"+formModel.getModelId()+"'>"+formModel.getModelName()+"</option>");
			}
		}else {
			strb.append("<option value=''>当前流程没在表单模板</option>");
		}
		
		return renderHtml(strb.toString());
	}
	
	/**
	 * 初始化表单模板名称
	 * @author zhengzb 修改方法
	 * @desc 
	 * 2010-11-6 上午09:42:28 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String initExport()throws UnsupportedEncodingException{
		TwfBaseProcessfile fileModel= manager.getProcessfileByPfId(pfId);
		if(fileModel.getTwfInfoProcessType()!=null){			
			processType=fileModel.getTwfInfoProcessType().getPtId().toString();
		}
		this.types = manager.getAllProcessTypeList();
		modelName=URLDecoder.decode(modelName, "utf-8");
		return "initExport";
	}
	
	/**
	 * 判断表单模板名称是否存在
	 * @author zhengzb
	 * @desc 
	 * 2010-11-6 上午09:41:49 
	 * @return
	 * @throws Exception
	 */
	public String isHasModelName() throws Exception{
		int count=designManager.getCountByModelName(modelName,processType);
		if(count>0){
			return renderHtml( "1");
		}else{
			return renderHtml( "0");
		}
	}
	
	 /**
	 * 将一个字节数组对象转换成一个文件对象
	 * @author:郑志斌
	 * @date:2009-7-17 下午05:45:38
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public  File byteArray2File(byte[] input)throws Exception{
		File file = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try{
			//C:\DOCUME~1\ADMINI~1\LOCALS~1\Temp\test52933.temp
			file = File.createTempFile("test", ".tmp");//创建临时文件
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(input);
//			file.deleteOnExit();
		}catch(Exception e){
			throw new SystemException("字节数组转成文件异常：" + e);
		}finally{
			if(bos!=null){
				bos.close();
			}
			if(fos!=null){
				fos.close();
			}
		}
		return file;
	}
	
	/**
	 * 流程模板列表分页
	 * @author zhengzb
	 * @desc 
	 * 2010-12-3 下午03:58:42 
	 * @return
	 * @throws Exception
	 */
	public String workflowModelList() throws Exception{

			
			modelPage=designManager.getWorkflowModel(modelPage, formModel,startDate,endDate);	//获取流程模板列表
			this.types = manager.getAllProcessTypeList();						//获取所有流程类型
			for(Object[] obj:types){
				typeMap.put(obj[0].toString(), obj[1].toString());				//组装一个流程类型Map
			}
			
			if(formModel==null||formModel.getModelProcesstype()==null||formModel.getModelProcesstype().equals("")){	
				processType="0";
			}else {
				processType=formModel.getModelProcesstype();
			}
			


		return "workflowmodel";
	}
	



	/**
	 * 在流程模板管理 中，删除流程模板
	 * @author zhengzb
	 * @desc 
	 * 2010-12-6 下午04:31:28 
	 * @return
	 * @throws Exception
	 */
	public String deleteWorkflowModel() throws Exception {
		String []arr=modelId.split(",");
		for(int i=0;i<arr.length;i++){
			designManager.deleteFormModel(arr[i]);
		}
		return workflowModelList();
	}
	
	
	
	/**
	 * 导出子模板
	 * @param
	 * @return
	 */
	public String exportTpl() throws Exception
	{
		processfile = processfile.replaceAll("\\r\\n", "");
		HttpServletResponse response = getResponse();
		OutputStream outputStream = new BufferedOutputStream(response
				.getOutputStream());
		response.setContentType("application/x-msdownload");
		String filename = processName + "-子模板-" + TimeKit.formatDate(new Date(), "yyyy-MM-dd_HHmmss") + ".xml";
		response.setHeader("content-disposition", "attachment;filename=\""
				+ URLEncoder.encode(filename,"utf-8") + "\"");
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
		writer.write(processfile);
		writer.close();
		outputStream.close();
		return null;
	}
	
	/**
	 * 导入子模板
	 * 
	 * @param
	 * @return
	 */
	public String importTpl(){
		StringBuffer processfile1 = new StringBuffer();
		String content = null;
		try {
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(
					new FileInputStream(processFile), "UTF-8"));
			String str;

			// ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			while ((str = inputStream.readLine()) != null) {
				processfile1.append(str);
			}

			content = processfile1.toString().replaceAll("\"", "\\\\\\\"");;
			content = content.replaceAll("\\r\\n", "");
			content = content.replaceAll("\\n", "");
		} catch (Exception e) {
			this.processfile = "false";
			return "fileImport";
		}
		
		if(content != null && "<doc-fragment>".equals(content.substring(0, 14))){
			this.processfile = content;
			
		}else{
			this.processfile = "false";
		}
		return "fileImport";
	}

	
	public String initTplImport()
	{
		return "initTplImport";
	}

	public String initImport() {
		return "initImport";
	}
	
	public String checkSubProcess(){
		String processFileName = processName.split(",")[1];
		boolean result = manager.checkProcessFileName(processFileName);
		if(result){
			this.renderText("1");
		}else{
			this.renderText("0");
		}
		return null;
	}
	
	
	/**
	 * action连接，主要用来在设计流程时保持设计器与服务器的连接不超时
	 * 
	 * @author 喻斌
	 * @date 2014-01-09 10:26:21
	 * @return
	 */
	public String connect(){
		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		this.renderText("connected");
		return null;
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

	public List<ToaSystemformModel> getModelList() {
		return modelList;
	}

	public void setModelList(List<ToaSystemformModel> modelList) {
		this.modelList = modelList;
	}
	
	public EformJspManagerManager getJspManager() {
		return jspManager;
	}
	@Autowired
	public void setJspManager(EformJspManagerManager jspManager) {
		this.jspManager = jspManager;
	}
	
	public ProcessDesignManager getDesignManager() {
		return designManager;
	}
	
	@Autowired
	public void setDesignManager(ProcessDesignManager designManager) {
		this.designManager = designManager;
	}



	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelUrl() {
		return modelUrl;
	}

	public void setModelUrl(String modelUrl) {
		this.modelUrl = modelUrl;
	}

	public String getRadioValue() {
		return radioValue;
	}

	public void setRadioValue(String radioValue) {
		this.radioValue = radioValue;
	}

	public IUserService getUserservice() {
		return userservice;
	}
	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}

	public List<TwfInfoProcessType> getPtList() {
		return ptList;
	}

	public void setPtList(List<TwfInfoProcessType> ptList) {
		this.ptList = ptList;
	}

	

	public ToaSystemformModel getFormModel() {
		return formModel;
	}

	public void setFormModel(ToaSystemformModel formModel) {
		this.formModel = formModel;
	}

	public Page getModelPage() {
		return modelPage;
	}

	public void setModelPage(Page modelPage) {
		this.modelPage = modelPage;
	}

	public Map<String, String> getTypeMap() {
		return typeMap;
	}

	public Date getEndDate() {
		return endDate;
	}

	@SuppressWarnings("deprecation")
	public void setEndDate(Date endDate) {
		endDate.setHours(23);
		endDate.setMinutes(59);
		this.endDate = endDate;
//		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public List<ToaDefinitionPlugin> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<ToaDefinitionPlugin> plugins) {
		this.plugins = plugins;
	}

	public List<ToaRule> getList() {
		return list;
	}

	public void setList(List<ToaRule> list) {
		this.list = list;
	}

	public List<Object[]> getJspForm() {
		return jspForm;
	}

	public void setJspForm(List<Object[]> jspForm) {
		this.jspForm = jspForm;
	}
	
}
