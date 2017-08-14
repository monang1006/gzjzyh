package com.strongit.oa.senddoc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.form.action.BaseAction;
import com.strongit.form.action.FormDataAware;
import com.strongit.form.action.FormTemplateAware;
import com.strongit.form.services.FormService;
import com.strongit.form.vo.FormData;
import com.strongit.form.vo.FormData.FormDataForm;
import com.strongit.form.vo.FormData.FormDataRow;
import com.strongit.oa.approveinfo.ApproveinfoManager;
import com.strongit.oa.bo.TEFormTemplate;
import com.strongit.oa.bo.TOaFreedomWorkflow;
import com.strongit.oa.bo.ToaApproveinfo;
import com.strongit.oa.bo.ToaDoctemplate;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.service.VoFormDataBean;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.service.UserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.freedomworkflow.service.FreedomWorkflowMimicSubmit;
import com.strongit.oa.freedomworkflow.service.IFreedomWorkflowService;
import com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.util.JsonUtil;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.util.UUIDGenerator;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 电子表单V2.0 模板实现
 * 
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date Aug 24, 2011
 * @classpath com.strongit.oa.eformManager.EFormTemplateAction
 * @version 3.0.2
 * @email dengzc@strongit.com.cn
 * @tel 0791-8186916
 */
@SuppressWarnings("serial")
@ParentPackage("default")
public class EFormTemplateAction extends BaseAction implements
		FormTemplateAware, FormDataAware {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String bussinessId;
	
	private String docType;
	
	private String formId = null;

	private String workflowName = null; // 流程名称


	private String workflowState = "0"; // 流程状态 0：草稿；1：已提交流程

	private File formTemplate = null; // 表单模板

	private File formData = null; // 表单模板数据

	private String title = null; // 模板标题

	private String type = null; // 模板类型

	private String orgCode = null; // 模板所属机构编码

	private String tableName = null;

	private String pkFieldName = null;

	private String pkFieldValue = null;

	private String approveData;//审批意见数据

	private String instanceId; //流程实例ID
	
	private String taskId;     //任务ID
	
	private String userId;     //用户ID
	
	//秘书的默认意见为空
	public static final String DEFAULT_SUGGESTION="";
	
	@Autowired
	UserService userService;

	@Autowired
	IEFormService eformService;

	@Autowired
	private FormService formService;


	@Autowired
	SendDocManager manager;

	@Autowired
	private IWorkflowService workflowService;

	@Autowired
	private SystemsetManager systemsetManager;
	
	@Autowired
	protected AdapterBaseWorkflowManager adapterBaseWorkflowManager;
	
	@Autowired
	private MyInfoManager myInfoManager;
	
	private String suggestionStyle;    //意见输入模式
	
	private String[] auditOpinionName = null;
	
	private String flowInstanceId;
	
	private String tabSelectedName;

	@Autowired
	private IFreedomWorkflowTaskService ftSrv;
	
	@Autowired
	private IFreedomWorkflowService fwSrv;

	@Autowired
	private ApproveinfoManager approveinfoManager;//拟稿意见服务类
	
	@Autowired
	private FreedomWorkflowMimicSubmit mimicSubmit;


	public String getBussinessId() {
		return bussinessId;
	}

	public String getDocType() {
		return docType;
	}

	public String getFormId() {
		return formId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public String getPkFieldName() {
		return pkFieldName;
	}

	public String getPkFieldValue() {
		return pkFieldValue;
	}

	public String getSuggestionStyle() {
		return suggestionStyle;
	}

	public String getTableName() {
		return tableName;
	}

	public String getTaskId() {
		return taskId;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public String getUserId() {
		return userId;
	}

	/**
	 * 加载意见至表单上
	 * @return
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 * Map<控件名称,List<String[意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,第一个委派/指派人名称,意见记录id
	 *         ,处理人机构名称,最后一个委派/指派人名称]>>
	 */
	public InputStream loadAuditOpinion() throws SystemException, DAOException,
			ServiceException {
		ByteArrayInputStream bais = null;
		ToaSystemset systemset = systemsetManager.getSystemset(); // 获取系统全局配置信息
		suggestionStyle = systemset.getSuggestionStyle() ;        //得到意见输入模式
		try {
			/*****
			 * 用户的角色是秘书时，没有默认意见
			 * from 林业厅
			 */
			String isRole = "1";
//			List<String> roleList = userService.getRoleIdByUserId(userService.getCurrentUser().getUserId());
//			if (roleList != null) {
//				for (String role : roleList) {
//					if(role != null){
//						if ("4028a44942209afb014225afc74d061c".equals(role)) {
//							isRole = "1";
//							break;
//						}
//					}
//				}
//			}
//			Map<控件名称,List<String[意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,第一个委派/指派人名称,组织机构名称,被代办人处理时间,任务处理类型]>>
			Map<String, List<String[]>> map = null;
			
			boolean isDepartmentSort = false;//是否有按部门排序
			HashMap<String,String> auditOpinionName_deptMap = new HashMap<String, String>();//保存控件名_部门名信息
			List<ToaApproveinfo> approveInfoList = null;
			//增加在拟稿环节处理办理意见
			if(flowInstanceId.length() == 32) {//说明是在拟稿环节存储的意见
				map = new HashMap<String, List<String[]>>();
				User user = userService.getCurrentUser();
				String userId = user.getUserId();
				String orgName = userService.getOrgInfoByOrgId(user.getOrgId()).getOrgName();
				approveInfoList = approveinfoManager.findApproveInfo(flowInstanceId, userId);
				if(approveInfoList != null && !approveInfoList.isEmpty()) {
					for(ToaApproveinfo approveInfo : approveInfoList) {
						String controlName = approveInfo.getAiControlName();
						String atDate = "";
						if(approveInfo.getAtDate() != null){
							atDate = approveInfo.getAtDate().toString();
						}
						if(controlName != null && controlName.length() > 0) {
							if(!map.containsKey(controlName)) {
								List<String[]> appList = new ArrayList<String[]>();
								appList.add(new String[]{approveInfo.getAiContent(),
											approveInfo.getAiActorId(),approveInfo.getAiActor(),
											approveInfo.getAiDate().toString(),approveInfo.getAtoPersonId(),
											approveInfo.getAtoPersonName(),approveInfo.getAiId(),
											orgName,atDate,approveInfo.getAtAssignType()});
								map.put(controlName, appList);
							} else {
								map.get(controlName).add(new String[]{approveInfo.getAiContent(),
											approveInfo.getAiActorId(),approveInfo.getAiActor(),
											approveInfo.getAiDate().toString(),approveInfo.getAtoPersonId(),
											approveInfo.getAtoPersonName(),approveInfo.getAiId(),
											orgName,atDate,approveInfo.getAtAssignType()});
							}
						}
					}
				}
			} else if(flowInstanceId.startsWith("$0")) {//查看父流程表单数据时记载审批意见
				flowInstanceId = flowInstanceId.substring(2);
				map = JsonUtil.generatejsonToApproveBase64(flowInstanceId);//将json格式的意见数据还原成Map结构
			} else {
				String insId = flowInstanceId.split(",")[0];
				if("null".equals(insId)||"".equals(insId)){//处理存在"null"的情况 yanjian 2012-07-06 11:02 
					map = new HashMap<String, List<String[]>>();
				}else{
					String tasId = "";
					if(flowInstanceId.split(",").length >1){
						tasId = flowInstanceId.split(",")[1];					
					}
					map = workflowService.getWorkflowApproveinfo(insId,tasId);   //得到流程中审批域的意见
				}
//				String tasId = "";
//				if(flowInstanceId.split(",").length >1){
//					tasId = flowInstanceId.split(",")[1];					
//				}
//				if(tasId != null && !"".equals(tasId)){	//得到待办中未提交的审批域意见				
//					approveInfoList = approveinfoManager.findApproveInfoByBid(tasId);
//					if(approveInfoList != null && !approveInfoList.isEmpty()) {
//						for(ToaApproveinfo approveInfo : approveInfoList) {
//							String controlName = approveInfo.getAiControlName();
//							String atDate = "";
//							if(approveInfo.getAtDate() != null){
//								atDate = approveInfo.getAtDate().toString();
//							}
//							String userId = approveInfo.getAiActorId();
//							User user = userService.getUserInfoByUserId(userId);
//							String orgName = userService.getOrgInfoByOrgId(user.getOrgId()).getOrgName();
//							if(controlName != null && controlName.length() > 0) {
//								if(!map.containsKey(controlName)) {
//									List<String[]> appList = new ArrayList<String[]>();
//									appList.add(new String[]{approveInfo.getAiContent(),
//												approveInfo.getAiActorId(),approveInfo.getAiActor(),
//												approveInfo.getAiDate().toString(),approveInfo.getAtoPersonId(),
//												approveInfo.getAtoPersonName(),approveInfo.getAiId(),
//												orgName,atDate,approveInfo.getAtAssignType()});
//									map.put(controlName, appList);
//								} else {
//									map.get(controlName).add(new String[]{approveInfo.getAiContent(),
//												approveInfo.getAiActorId(),approveInfo.getAiActor(),
//												approveInfo.getAiDate().toString(),approveInfo.getAtoPersonId(),
//												approveInfo.getAtoPersonName(),approveInfo.getAiId(),
//												orgName,atDate,approveInfo.getAtAssignType()});
//								}
//							}
//						}
//					}
//				}
				//workflowService.getApproveInfosByPIId(processInstanceId)
			}
			//aiIds存放 待办中未提交的审批域意见的id
			//ps: 审批域意见表 T_OA_APPROVEINFO
			List<String> aiIds = new ArrayList<String>();
			if(approveInfoList != null && !approveInfoList.isEmpty()){
				for(ToaApproveinfo approveInfo : approveInfoList){
					String id = approveInfo.getAiId();
					if(id != null && !"".equals(id)){
						aiIds.add(id);						
					}
				}
			}
			if(map.containsKey("isDepartmentSort")){//意见域是否按部门排序
				isDepartmentSort = true;
				map.remove("isDepartmentSort");
			}
			List<String> userIds = new ArrayList<String>();
			Collection<List<String[]>> cols = map.values();
			if(cols != null && !cols.isEmpty()) {
				for(Iterator<List<String[]>> it=cols.iterator();it.hasNext();) {
					List<String[]> list = it.next();
					if(list != null) {
						for(String[] strs : list) {
							if(strs[4] == null || "null".equals(strs[4]) || strs[4].length() == 0) {
								userIds.add(strs[1]);//人员id 用户查找用户签名
							} else {
								userIds.add(strs[4]);
							}
						}
					}
				}
			}
			List<FormDataForm> lstFormDataForm = new ArrayList<FormDataForm>(1);
			if(auditOpinionName != null && auditOpinionName.length > 0) {
				Map<String, byte[]> signMap = myInfoManager.getUserSign(userIds);
				for(String name : auditOpinionName) {
					List<String[]> suggestions = map.get(name);
					if(suggestions != null && !suggestions.isEmpty()) {
						FormDataForm formDataForm = new FormDataForm(name);
						List<FormDataRow> rows = new ArrayList<FormDataRow>();
						Map<String, byte[]> lobMap = new HashMap<String, byte[]>();
						for(String[] strs : suggestions) {
							FormDataRow formDataRow = new FormDataRow();
							Map<String, String> row = new HashMap<String, String>(4);
							String userId = strs[1];//用户id
							String userName = strs[2];//用户名称
							String stdate = strs[3]; 
														
								if(strs[4] != null && !"null".equals(strs[4]) && strs[4].length() > 0) {
									userId = strs[4];
									userName = strs[5];
								if(strs[9] != null && !"null".equals(strs[9]) && "2".equals(strs[9]) ){//存在代办
									if(strs[8] != null && !"null".equals(strs[8]) ){										
										stdate = strs[8];								
									}
								}
							}
							byte[] sign = signMap.get(userId);//得到签名信息,如果不存在,则取用户名称
							UUIDGenerator uuid = new UUIDGenerator();
							String uid = uuid.generate().toString();
							String type = "1";
							if(sign != null) {
								type = "0";
								userName = uid;//如果存在签名,就显示签名,若不存在,则显示姓名
								lobMap.put(uid, sign);
							}
//							String opinion = "";
									
							/**
							 * 意见内容显示策略：当opinion为空串时，表单的领导批示列不显示意见
							 * 						 当opinion有值时（空格也算），则显示意见
							 * @author 钟伟
							 */
							String opinion = "";	//yanjian 2012-08-08 09：43
							boolean flag = false;
							if(strs[0] != null && !"null".equals(strs[0]) && strs[0].length() > 0){
								opinion = strs[0];
//								//前端传来的尖括号  可能是英文格式  也可能是中文格式
								if(opinion.length() > 3 && (opinion.indexOf("＜签发＞")!=-1 || opinion.indexOf("<签发>")!=-1)){
									String str = opinion.substring(4);
									if (!"".equals(str)) {
										if(str != "null"){
											opinion = str;
											opinion = opinion.replaceAll("＜签发＞", "");
											opinion = opinion.replaceAll("<签发>", "");
										}else{
											opinion = DEFAULT_SUGGESTION;
											if (isRole.equals("1")) {
												opinion = " ";
											}
										}
									} else {
										opinion = DEFAULT_SUGGESTION;
										if (isRole.equals("1")) {
											opinion = " ";
										}
									}
									for (int i = 0; i < auditOpinionName.length; i++) {
										if ("AuditOpinion_qf".equals(auditOpinionName[i])) {
											FormDataForm formDataForm1 = new FormDataForm(auditOpinionName[i]);
											List<FormDataRow> rows1 = new ArrayList<FormDataRow>();
											FormDataRow formDataRow1 = new FormDataRow();
											Map<String, String> row1 = new HashMap<String, String>(4);
											row1.put("opinion", opinion);
											row1.put("type", type);
											row1.put("sign", userName);
											row1.put("date", stdate);
											row1.put("id", strs[6]);
											row1.put("signerId", userId);
											StringBuilder info1 = new StringBuilder();
											if (!suggestionStyle.equals("1")) {// 意见输入模式是笔形图标输入
												if (strs[strs.length - 1] != null
														&& strs[strs.length - 1]
																.equals("temp"))
													info1.append(
															"鼠标左键双击处理人，可以查看或修改意见。")
															.append("[\\r\\n]");
												info1.append("处理人：")
														.append(strs[2])
														.append("[\\r\\n]");
												String strDate = strs[3];
												try {
													SimpleDateFormat sdf = new SimpleDateFormat(
															"yyyy-MM-dd HH:mm");
													Date date = sdf
															.parse(strDate);
													strDate = sdf.format(date);
												} catch (Exception e) {
													e.printStackTrace();
												}
												info1.append("录入时间：")
														.append(strDate)
														.append("[\\r\\n]");
												info1.append("所在部门：").append(
														strs[7]);
												row1.put("info",
														info1.toString());
												formDataRow1.Row = row1;
												rows1.add(formDataRow1);
											} else {// 正常模式
												if (isDepartmentSort) {
													String auditOpinionName_dept = strs[7]
															+ "_" + name;
													if (!auditOpinionName_deptMap
															.containsKey(auditOpinionName_dept)) {
														auditOpinionName_deptMap
																.put(auditOpinionName_dept,
																		null);
														row.put("opinion", "["
																+ strs[7]
																+ "]\r\n"
																+ opinion);
													}
												}
												String strDate = strs[3];
												try {
													SimpleDateFormat sdf = new SimpleDateFormat(
															"yyyy-MM-dd HH:mm");
													Date date = sdf
															.parse(strDate);
													strDate = sdf.format(date);
												} catch (Exception e) {
													e.printStackTrace();
												}
												info1.append("录入时间：")
														.append(strDate)
														.append("[\\r\\n]");
												info1.append("所在部门：").append(
														strs[7]);
												row1.put("info",
														info1.toString());
												formDataRow1.Row = row1;
												rows1.add(formDataRow1);

											}
											formDataForm1.Rows = rows1;
											formDataForm1.Lobs = lobMap;
											//置于list首位  【签发】中不优先被显示
											lstFormDataForm.add(0,formDataForm1);
											flag = true;
										}
									}
								}
								// 表示该条意见是签发意见,已放在签发表单意见域中，跳过
								if (flag == true) {
									continue;
								}
							}else{
								//意见记录id
								String aiId = strs[6];
								//任务id
								String taskId = approveinfoManager.getTaskIdByAiId(aiId);
								//节点设置
								TwfBaseNodesetting nodeSetting = workflowService.getNodesettingByTid(taskId);
								//是否允许显示空白意见，1是允许，0是不允许
								String chkShowBlankSuggestion = nodeSetting.getPlugin("plugins_chkShowBlankSuggestion");
								if(chkShowBlankSuggestion != null && "1".equals(chkShowBlankSuggestion)){
									opinion = " ";
								}
							}
							row.put("opinion", opinion);
							row.put("type", type);
							if(name.indexOf("showdep")>-1 && sign==null){
								row.put("sign", "["+strs[7]+"]  "+userName);
							}else{
								row.put("sign", userName);
							}
							row.put("date", stdate);
							row.put("id", strs[6]);	
							row.put("signerId", userId);
							StringBuilder info = new StringBuilder();
							if(!suggestionStyle.equals("1")){//意见输入模式是笔形图标输入
								//待办中未提交的审批域意见 才显示如下文字内容
								if(aiIds != null && !aiIds.isEmpty()){
									for(String aiId : aiIds){
										if(strs[6] != null && strs[6].equals(aiId)){											
											info.append("鼠标左键双击处理人，可以查看或修改意见。").append("[\\r\\n]");
										}
									}
								}
								info.append("处理人：").append(strs[2]).append("[\\r\\n]");
								String strDate = strs[3];
								try {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
									Date date = sdf.parse(strDate);
									strDate = sdf.format(date);
								} catch (Exception e) {
									e.printStackTrace();
								}
								info.append("录入时间：").append(strDate).append("[\\r\\n]");
							    info.append("所在部门：").append(strs[7]);									
								row.put("info", info.toString());
								formDataRow.Row = row;
								rows.add(formDataRow);
							}else{//正常模式
								if(isDepartmentSort){
									String auditOpinionName_dept = strs[7]+"_"+name;
									if(!auditOpinionName_deptMap.containsKey(auditOpinionName_dept)){
										auditOpinionName_deptMap.put(auditOpinionName_dept, null);
										row.put("opinion", "[" + strs[7] + "]\r\n"+opinion);
									}
								}
								String strDate = strs[3];
								try {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
									Date date = sdf.parse(strDate);
									strDate = sdf.format(date);
								} catch (Exception e) {
									e.printStackTrace();
								}
								info.append("录入时间：").append(strDate).append("[\\r\\n]");
								info.append("所在部门：").append(strs[7]);									
								row.put("info", info.toString());
								formDataRow.Row = row;
								rows.add(formDataRow);
								
							}
						}
						formDataForm.Rows = rows;
						formDataForm.Lobs = lobMap;
						lstFormDataForm.add(formDataForm);
					}
				}
			}
			// 采用ZIP算法压缩成多文件Zip文件
			Map<String, byte[]> mapLob = new HashMap<String, byte[]>(1);
			byte[] binaryFormData = FormData.GenerateLoadFormDataResultToXML(
					lstFormDataForm, mapLob);
			if (binaryFormData != null && binaryFormData.length > 0) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(baos);
				zos.putNextEntry(new ZipEntry("formData.xml"));
				zos.write(binaryFormData);
				binaryFormData = null;
				zos.flush();
				zos.closeEntry();
				if (mapLob != null && mapLob.size() > 0) {
					for (Iterator<Map.Entry<String, byte[]>> iter = mapLob
							.entrySet().iterator(); iter.hasNext();) {
						Map.Entry<String, byte[]> entry = iter.next();
						zos.putNextEntry(new ZipEntry(entry.getKey()));
						if (entry.getValue() != null
								&& entry.getValue().length > 0) {
							zos.write(entry.getValue());
						}
						zos.flush();
						zos.closeEntry();
					}
				}
				zos.close();
				zos = null;
				bais = new ByteArrayInputStream(baos.toByteArray());
				baos.close();
				baos = null;
				mapLob.clear();
				mapLob = null;
				// Runs the garbage collector.
				// System.gc();
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
		return bais;

	}

	/**
	 * 加载表单模板数据
	 * 
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public InputStream LoadFormData() throws DAOException, ServiceException,
			SystemException {
		InputStream responseData = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(formData);
			responseData = eformService.loadFormData(fis);
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
	
	/**
	 * 加载电子表单数据
	 * 
	 * @author zw
	 * @param
	 * @return
	 */
	public String loadFormData() throws Exception
	{
		FileInputStream fisFormData = new FileInputStream(this.formData);
		InputStream responseData = this.formService.LoadFormData(fisFormData);
		
		HttpServletResponse res = ServletActionContext.getResponse();
		res.setContentType(com.strongit.form.action.BaseAction.ContentType_StrongFormData);
		ServletOutputStream outputStream = res.getOutputStream();
		byte[] data = IOUtils.toByteArray(responseData);
		IOUtils.write(data, outputStream);
		responseData.close();
		outputStream.close();
		return null;
	}

	/**
	 * 装载表单模板
	 * 
	 * @param formId -
	 *            表单模板id
	 * @throws UnsupportedEncodingException
	 */
	public InputStream LoadFormTemplate() throws SystemException, DAOException,
			ServiceException {
		InputStream responseData = null;
		try {
			TEFormTemplate template = eformService.get(formId);
			if (template == null) {
				logger.error("id为" + formId + "表单模板不存在！");
				throw new SystemException("id为" + formId + "表单模板不存在！");
			} else {
				byte[] content = template.getContent();
				responseData = new ByteArrayInputStream(content);
			}
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

	/**
	 * 从URL打开正文WORD
	 * 
	 * @author:申仪玲
	 * @date:2012-08-15 上午09:31:23
	 * @throws Exception
	 */
	public void loadWordFromUrl() throws Exception{
		try {
			if (bussinessId == null || "".equals(bussinessId)) {
				logger.error("参数bussinessId为空");
				throw new SystemException("参数bussinessId为空");
			}
			if (formId == null || "".equals(formId)) {
				logger.error("参数formId为空");
				throw new SystemException("参数formId为空");
			}
			String args[] = bussinessId.split(";");		
			tableName = args[0];// 业务表名
			pkFieldName= args[1];// 业务表主键名
			pkFieldValue = args[2];;// 业务表主键值
			
			List<EFormField> fieldList = manager.getFormTemplateFieldList(formId);
			EFormField eformField = null;
			
			String tabName=null;
			if(this.tabSelectedName!=null){
				tabName = URLDecoder.decode(this.tabSelectedName,"UTF-8");
			}else{
				tabName = "";
			}
			for (EFormField field : fieldList) {
				if ("Office".equals(field.getType())) {// 找到OFFICE控件类型
					if(tabName!= null && !"".equals(tabName)){
						if(tabName.equals(field.getCaption())){							
							eformField = field;
							break;
						}
					}else{
						eformField = field;
						break;
					}

				}
			}
			if (eformField == null) {
				throw new SystemException("表单中不存在正文。");
			}
			InputStream is = (InputStream) manager.getFieldValue(eformField.getFieldname(), tableName, pkFieldName,
					pkFieldValue);
			
			if (is == null) {				
				//表单正文没内容，则初始化一个空的word
				if("公文征求意见单".equals(tabName)){
					openDocFromUrl("0");
				}else if("公文转办单".equals(tabName)){
					openDocFromUrl("1");
				}else{					
					openEmptyDocFromUrl();
				}
			}
			if (is != null) {
				manager.openInputStreamToHttpResponse(
						FileUtil.inputstream2ByteArray(is));
			}
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
		
	}
	
	/**
	 * author:luosy
	 * description: 打开公文征求意见单word模板或者公文转办单word模板
	 * modifyer:
	 * description:
	 */
	public void openDocFromUrl(String type) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.reset();
		response.setContentType("application/octet-stream");
		OutputStream output = null;
		logger.info("文档类型：doc");
		try {
			output = response.getOutputStream();

				String rootPath = "";
				File file = null;
				if ("0".equals(type)) {
					rootPath = ServletActionContext.getRequest().getSession().getServletContext()
					.getRealPath("/empty_yjzx.doc");
				}else if("1".equals(type)){
					rootPath = ServletActionContext.getRequest().getSession().getServletContext()
					.getRealPath("/empty_gwzb.doc");
				}else{
					rootPath = ServletActionContext.getRequest().getSession().getServletContext()
					.getRealPath("/empty.doc");
				}
				file = new File(rootPath);
				logger.info("打开文档:" + rootPath);
				byte[] buf = FileUtil
						.inputstream2ByteArray(new FileInputStream(file));
				output.write(buf);

		} catch (Exception e) {
			logger.error("打开文档出错了。", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 兼容2003 - 2007 打开空文档。
	 * 
	 * @author:邓志城
	 * @date:2010-4-13 上午11:49:00
	 */
	public void openEmptyDocFromUrl() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.reset();
		response.setContentType("application/octet-stream");
		OutputStream output = null;
		String type = ".doc";
		logger.info("文档类型：" + docType);
		try {
			String templateId = null;// ;templateEFormManager.findLatestTemplate(formId);
			output = response.getOutputStream();
			if (templateId == null) {
				if ("1".equals(docType)) {
					type = ".doc";
				} else if ("2".equals(docType)) {
					type = ".xls";
				} else if ("3".equals(docType)) {
					type = ".ppt";
				} else if ("4".equals(docType)) {
					type = ".vsd";
				} else if ("5".equals(docType)) {
					type = ".mpp";
				} else if ("6".equals(docType)) {
					type = ".wps";
				}
				String rootPath = ServletActionContext.getRequest().getSession().getServletContext()
						.getRealPath("/empty" + type);
				File file = new File(rootPath);
				if (!file.exists()) {
					rootPath = ServletActionContext.getRequest().getSession().getServletContext()
							.getRealPath("/empty.doc");
					file = new File(rootPath);
				}
				logger.info("打开文档:" + rootPath);
				byte[] buf = FileUtil
						.inputstream2ByteArray(new FileInputStream(file));
				output.write(buf);
			} else {
				ToaDoctemplate template = adapterBaseWorkflowManager
						.getDocTempItemManager().getDoctemplate(templateId);
				if (template != null) {
					byte[] buf = template.getDoctemplateContent();
					output.write(buf);
				} else {
					if ("1".equals(docType)) {
						type = ".doc";
					} else if ("2".equals(docType)) {
						type = ".xls";
					} else if ("3".equals(docType)) {
						type = ".ppt";
					} else if ("4".equals(docType)) {
						type = ".vsd";
					} else if ("5".equals(docType)) {
						type = ".mpp";
					} else if ("6".equals(docType)) {
						type = ".wps";
					}
					String rootPath = ServletActionContext.getRequest().getSession()
							.getServletContext().getRealPath("/empty" + type);
					output = response.getOutputStream();
					File file = new File(rootPath);
					if (!file.exists()) {
						rootPath = ServletActionContext.getRequest().getSession()
								.getServletContext().getRealPath("/empty.doc");
						file = new File(rootPath);
					}
					logger.info("打开文档:" + rootPath);
					byte[] buf = FileUtil
							.inputstream2ByteArray(new FileInputStream(file));
					output.write(buf);
				}
			}

		} catch (Exception e) {
			logger.error("打开文档出错了。", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 保存表单模板数据
	 * 
	 * @return
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public InputStream SaveFormData() throws SystemException, DAOException,
			ServiceException {
		InputStream responseData = null;
		FileInputStream fisFormData = null;
		try {
			fisFormData = new FileInputStream(formData);
			// responseData = eformService.doSaveFormData(fisFormData);
			//多了一个参数userId,在保存模板数据时，代办意见的处理人不能是当前用户，
			//所以由页面传参userId过来   申仪玲 20120203
			workflowName = URLDecoder.decode(URLDecoder.decode(workflowName, "UTF-8"), "UTF-8");
			VoFormDataBean bean = manager.saveFormData(fisFormData,
					workflowName, workflowState, approveData,instanceId,taskId,userId);
			/*
			 * responseData = manager.saveFormData(fisFormData, workflowName,
			 * workflowState, null);
			 */
			responseData = bean.getIsReturnFormData();
			bean.deleteFile();
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
	
	/**
	 * 自由流程中保存电子表单数据
	 * 
	 * @author zw
	 * @param
	 * @return
	 */
	public InputStream saveFormData() throws Exception
	{
		InputStream responseData = null;
		FileInputStream fisFormData = null;
		
		fisFormData = new FileInputStream(formData);
		List<File> lstDeleteFormAttachment = new ArrayList<File>();
		responseData = formService.SaveFormData(fisFormData, lstDeleteFormAttachment);
		String retData = IOUtils.toString(responseData);
		Document doc = Jsoup.parseBodyFragment(retData);
		
		Element form = doc.select("forms > form").first();
		String table = form.attr("name");
		Element field = doc.select("fields > field").first();
		String pk = field.attr("name");
		Element row = doc.select("rows > row").first();
		String id = row.attr(pk);
		String flag = row.text();

		HttpServletRequest req = ServletActionContext.getRequest();
		String formId = req.getParameter("formId");
		
		
		String jsonHandles = "";
		jsonHandles = req.getParameter("jsonHandles");
		if(StringUtils.isNotEmpty(jsonHandles))
			jsonHandles = URLDecoder.decode(jsonHandles, "UTF-8");
		
		String fwTitle = "";
		fwTitle = req.getParameter("fwTitle");
		if(StringUtils.isNotEmpty(fwTitle))
			fwTitle = URLDecoder.decode(fwTitle, "UTF-8");
		
		String firstTaskMemo = "";
		firstTaskMemo = req.getParameter("firstTaskMemo");
		if(StringUtils.isNotEmpty(firstTaskMemo))
			firstTaskMemo = URLDecoder.decode(firstTaskMemo, "UTF-8");
		
		String ftMemo = "";
		ftMemo = req.getParameter("ftMemo");
		if(StringUtils.isNotEmpty(ftMemo))
			ftMemo = URLDecoder.decode(ftMemo, "UTF-8");
		
		String remindMsg = "";
		remindMsg = req.getParameter("remindMsg");
		if(StringUtils.isNotEmpty(remindMsg))
			remindMsg = URLDecoder.decode(remindMsg, "UTF-8");
		
		String remindTypes = "";
		remindTypes = req.getParameter("remindTypes");
		if(StringUtils.isNotEmpty(remindTypes))
			remindTypes = URLDecoder.decode(remindTypes, "UTF-8");
		
		User user = userService.getCurrentUser();
		String nextHandlerId = req.getParameter("nextHandlerId");

		String ftId = req.getParameter("ftId");
		
		//新建自由流的保存
		if("Insert".equals(flag))
		{			
			TOaFreedomWorkflow fw = new TOaFreedomWorkflow();
			fw.setFwFormId(formId);
			fw.setFwTitle(fwTitle);
			fw.setFwFormBizId(id);
			fw.setFwFormBizPk(pk);
			fw.setFwFormBizTable(table);
			fw.setFwCreator(user.getUserId());
			fw.setFwCreatTime(new Date());
			
			ftSrv.saveFreedomWorkflowTasks(fw, firstTaskMemo, jsonHandles);
		}
		else
		{
			ftSrv.doneTask(user.getUserId(), ftId, ftMemo);
		}

		List<String> users = new ArrayList<String>();
		if(StringUtils.isNotEmpty(nextHandlerId))
		{
			users.add(nextHandlerId);
		}
		
		if(StringUtils.isNotEmpty(remindTypes) && users.size() > 0)
		{
			ftSrv.sendMsg(remindTypes, users, remindMsg);
		}

		
//		ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
//		byte[] data = IOUtils.toByteArray(responseData);
//		IOUtils.write(data, outputStream);
//		responseData.close();
//		outputStream.close();
		
//		if(!"Insert".equals(flag))
//		{
//			Map<String, String> stepTwo = new HashMap<String, String>();
//			mimicSubmit.two(stepTwo);
//		}

		return responseData;
	}
	

	/**
	 * @author 严建
	 * @createTime Aug 24, 2011
	 * @description 保存或跟新表单模板
	 */
	public InputStream saveFormTemplate() throws SystemException, DAOException,
			ServiceException {
		InputStream responseData = null;
		FileInputStream fis = null;
		try {
			User user = userService.getCurrentUser();
			TEFormTemplate model = null;
			if (formId != null && !"".equals(formId)) {// 存在表单模板,说明是更新模板
				model = eformService.get(formId);
				model.setMender(user.getUserName());
				model.setModifyTime(new Date());
			} else {
				model = new TEFormTemplate();
				model.setCreator(user.getUserName());
				model.setCreateTime(new Date());
			}
			model.setTitle(URLDecoder.decode(URLDecoder.decode(title,"UTF-8"),"UTF-8"));
			model.setOrgCode(orgCode);
			fis = new FileInputStream(formTemplate);
			byte[] content = FileUtil.inputstream2ByteArray(fis);
			model.setContent(content);
			model.setType(type);
			eformService.saveFormTemplate(model);
			responseData = new ByteArrayInputStream(model.getId().toString()
					.getBytes("utf-8"));
		} catch (SystemException ex) {
			throw ex;
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception ex) {
					logger.error("", ex);
				}
			}
		}
		return responseData;
	}
	
	public String savePdf() throws Exception
	{
		OutputStream output = null;
		HttpServletResponse response = getResponse();
		response.addHeader("Content-Disposition", "attachment;filename=saaaa.pdf");
		    output = response.getOutputStream();
		    output.write(IOUtils.toByteArray(new FileInputStream(formData)));
		    output.flush();

		return null;
	}
	
	

	private HttpServletResponse getResponse()
	{
		return null;
	}

	public void setApproveData(String approveData) {
		this.approveData = approveData;
	}
	
	public void setAuditOpinionName(String[] paramValue) {
		this.auditOpinionName = paramValue;
	}
	
	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public void setFlowInstanceId(String flowInstanceId) {
		this.flowInstanceId = flowInstanceId;
	}
	
	public void setFormData(File formData) {
		this.formData = formData;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public void setFormTemplate(File formTemplate) {
		this.formTemplate = formTemplate;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public void setPkFieldName(String pkFieldName) {
		this.pkFieldName = pkFieldName;
	}

	public void setPkFieldValue(String pkFieldValue) {
		this.pkFieldValue = pkFieldValue;
	}

	public void setSuggestionStyle(String suggestionStyle) {
		this.suggestionStyle = suggestionStyle;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public void setWorkflowState(String workflowState) {
		this.workflowState = workflowState;
	}
	
	public String getTabSelectedName() {
		return tabSelectedName;
	}

	public void setTabSelectedName(String tabSelectedName) {
		this.tabSelectedName = tabSelectedName;
	}
}
