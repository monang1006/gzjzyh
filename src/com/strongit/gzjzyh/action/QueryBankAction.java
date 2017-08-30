package com.strongit.gzjzyh.action;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.gzjzyh.GzjzyhApplicationConfig;
import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.appConstants;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.po.TGzjzyhCase;
import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
import com.strongit.gzjzyh.policeregister.IPoliceRegisterManager;
import com.strongit.gzjzyh.service.IQueryApplyService;
import com.strongit.gzjzyh.service.IQueryAuditService;
import com.strongit.gzjzyh.service.IQueryBankService;
import com.strongit.gzjzyh.util.FileKit;
import com.strongit.gzjzyh.vo.TGzjzyhApplyVo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.rolemanage.IRoleManager;
import com.strongit.uums.util.Const;
import com.strongit.workflow.util.TimeKit;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
public class QueryBankAction extends BaseActionSupport {

	public TGzjzyhApplyVo model = new TGzjzyhApplyVo();

	private IQueryApplyService queryApplyService = null;

	@Autowired
	IRoleManager roleManager;
	@Autowired
	IPoliceRegisterManager policeRegisterManager;
	@Autowired
	IQueryAuditService auditService;
	@Autowired
	IQueryBankService bankService;

	private Page<TGzjzyhApplication> page = new Page<TGzjzyhApplication>(10,
			true);

	private String appId;

	private String searchRequiredType;

	private String searchAppFileNo;

	private String searchAppBankuser;

	private Date searchAppDateStart;

	private Date searchAppDateEnd;
	
	private String searchAppOrg;

	private List<TUumsBaseUser> userList = new ArrayList<TUumsBaseUser>();
	
	private String bankUserName;
	
	private String appTypeName;
	
	private String appDateDesc;
	
	private String appResponsefile;
	
	private String appResponsefile1;

	IUserService userService;

	private String uploadFileFileName;

	private String searchAppOrgAccount;

	private String searchAppPersonAccount;

	private String searchAppOrgDetail;

	private String searchAppPersonDetail;

	private String searchAppChadeDetail;

	private String searchAppDateType;

	private Date searchAppStartDate;

	private Date searchAppEndDate;

	private String frozenAppOrgAccount;

	private String frozenAppPersonAccount;

	private Date frozenAppStartDate;

	private Date frozenAppEndDate;

	private String continueAppOrgAccount;

	private String continueAppPersonAccount;

	private Date continueAppStartDate;

	private Date continueAppEndDate;

	private String thawAppOrgAccount;

	private String thawAppPersonAccount;

	private Date thawAppStartDate;

	private Map<String, String> statusMap = new HashMap<String, String>();

	private Map<String, String> typeMap = new HashMap<String, String>();
	
	private Map<String, String> userMap = new HashMap<String, String>();

	private MyLogManager myLogManager;

	private static final String DEFAULT_UPLOAD_IMAGE = "/images/upload/defaultUpload.jpg";

	private String appLawfileTmp;// 文书

	private String appLawfileRTmp;// 回执

	private String appAttachmentTmp;// 其它

	private String ueMainNo1Tmp;

	private String ueMainNo2Tmp;

	private String ueMainId1Tmp;

	private String ueMainId2Tmp;

	private String ueHelpNo1Tmp;

	private String ueHelpNo2Tmp;

	private String ueHelpId1Tmp;

	private String ueHelpId2Tmp;

	private String errorMsg = "";
	
	public QueryBankAction() {
		statusMap.put(appConstants.STATUS_SUBMIT_NO, "待提交");
		statusMap.put(appConstants.STATUS_SUBMIT_YES, "待审核");
		statusMap.put(appConstants.STATUS_AUDIT_YES, "已审核");
		statusMap.put(appConstants.STATUS_AUDIT_BACK, "已驳回");

		statusMap.put(appConstants.STATUS_SIGN_YES, "已签收");
		statusMap.put(appConstants.STATUS_PROCESS_YES, "已处理");
		statusMap.put(appConstants.APP_STATUS_REFUSE, "已拒签");

		typeMap.put("0", "查询申请");
		typeMap.put("1", "冻结申请");
		typeMap.put("2", "续冻申请");
		typeMap.put("3", "解冻申请");
	}

	@Autowired
	public void setQueryApplyService(IQueryApplyService queryApplyService) {
		this.queryApplyService = queryApplyService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setMyLogManager(MyLogManager myLogManager) {
		this.myLogManager = myLogManager;
	}

	@Override
	public Object getModel() {
		return this.model;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	private List<TUumsBaseUser> getBankAccountInfos() {
		TUumsBaseOrg bankOrg = this.userService
				.getOrgInfoBySyscode(GzjzyhConstants.DEFAULT_BANKORG_SYSCODE);
		userList = this.userService.getUserInfoByOrgId(bankOrg.getOrgId(),
				Const.IS_YES, Const.IS_NO);
		return userList;
	}
	
	@Override
	public String input() throws Exception {
		this.parseModel();
		
		this.appLawfileTmp = this.DEFAULT_UPLOAD_IMAGE;
		this.appLawfileRTmp = this.DEFAULT_UPLOAD_IMAGE;
		this.appAttachmentTmp = this.DEFAULT_UPLOAD_IMAGE;
		
		if (model.getGzjzyhApplication().getAppLawfile() != null
				&& !"".equals(model.getGzjzyhApplication().getAppLawfile())) {
			this.appLawfileTmp = model.getGzjzyhApplication()
					.getAppLawfile();
		}
		if (model.getGzjzyhApplication().getAppLawfileR() != null
				&& !"".equals(model.getGzjzyhApplication().getAppLawfileR())) {
			this.appLawfileRTmp = model.getGzjzyhApplication()
					.getAppLawfileR();
		}
		if (model.getGzjzyhApplication().getAppAttachment() != null
				&& !"".equals(model.getGzjzyhApplication().getAppAttachment())) {
			this.appAttachmentTmp = model.getGzjzyhApplication()
					.getAppAttachment();
		}

		this.ueMainNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainId2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpId2Tmp = this.DEFAULT_UPLOAD_IMAGE;

		if (model.getGzjzyhUserExtension().getUeMainNo1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainNo1())) {
			this.ueMainNo1Tmp = model.getGzjzyhUserExtension()
					.getUeMainNo1();
		}
		if (model.getGzjzyhUserExtension().getUeMainNo2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainNo2())) {
			this.ueMainNo2Tmp = model.getGzjzyhUserExtension()
					.getUeMainNo2();
		}
		if (model.getGzjzyhUserExtension().getUeMainId1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainId1())) {
			this.ueMainId1Tmp = model.getGzjzyhUserExtension()
					.getUeMainId1();
		}
		if (model.getGzjzyhUserExtension().getUeMainId2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainId2())) {
			this.ueMainId2Tmp = model.getGzjzyhUserExtension()
					.getUeMainId2();
		}
		if (model.getGzjzyhUserExtension().getUeHelpNo1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpNo1())) {
			this.ueHelpNo1Tmp = model.getGzjzyhUserExtension()
					.getUeHelpNo1();
		}
		if (model.getGzjzyhUserExtension().getUeHelpNo2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpNo2())) {
			this.ueHelpNo2Tmp = model.getGzjzyhUserExtension()
					.getUeHelpNo2();
		}
		if (model.getGzjzyhUserExtension().getUeHelpId1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpId1())) {
			this.ueHelpId1Tmp = model.getGzjzyhUserExtension()
					.getUeHelpId1();
		}
		if (model.getGzjzyhUserExtension().getUeHelpId2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpId2())) {
			this.ueHelpId2Tmp = model.getGzjzyhUserExtension()
					.getUeHelpId2();
		}

		return INPUT;
	}

	@Override
	public String list() throws Exception {
		this.getBankAccountInfos();
		if(this.userList != null && !this.userList.isEmpty()){
			for(TUumsBaseUser user : this.userList){
				this.userMap.put(user.getUserId(), user.getUserName());
			}
		}
		page = this.bankService.findQueryBankNotSignPage(page,
				this.searchRequiredType, this.searchAppFileNo,
				this.searchAppOrg, this.searchAppDateStart,
				this.searchAppDateEnd);
		return SUCCESS;
	}
	
	public void prepareGoProcess() throws Exception{
		prepareModel();
	}
	
	public String goProcess() throws Exception{
		this.input();
		return "process";
	}
	
	public void prepareProcess() throws Exception{
		prepareModel();
	}
	
	public String process() throws Exception{
		try {
			this.bankService.process(model);
			// 添加日志信息
			String ip = getRequest().getRemoteAddr();
			String logInfo = "";
			logInfo = "处理了查询申请" + model.getGzjzyhApplication().getAppId();
			this.myLogManager.addLog(logInfo, ip);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "处理失败：" + e.getMessage();
		}
		return "close";
	}
	
	public String signList() throws Exception {
		this.getBankAccountInfos();
		if(this.userList != null && !this.userList.isEmpty()){
			for(TUumsBaseUser user : this.userList){
				this.userMap.put(user.getUserId(), user.getUserName());
			}
		}
		page = this.bankService.findQueryBankSignPage(page,
				this.searchRequiredType, this.searchAppFileNo,
				this.searchAppOrg, this.searchAppDateStart,
				this.searchAppDateEnd);
		return "signlist";
	}
	
	public String processList() throws Exception {
		this.getBankAccountInfos();
		if(this.userList != null && !this.userList.isEmpty()){
			for(TUumsBaseUser user : this.userList){
				this.userMap.put(user.getUserId(), user.getUserName());
			}
		}
		page = this.bankService.findQueryBankProcessedPage(page,
				this.searchRequiredType, this.searchAppFileNo,
				this.searchAppOrg, this.searchAppDateStart,
				this.searchAppDateEnd);
		return "processlist";
	}

	@Override
	protected void prepareModel() throws Exception {
		if (this.appId != null && !"".equals(this.appId)) {
			this.model = new TGzjzyhApplyVo();
			TGzjzyhApplication application = this.queryApplyService
					.getApplicationById(this.appId);
			this.model.setGzjzyhApplication(application);
			if(GzjzyhApplicationConfig.isDistributedDeployed()) {
				TGzjzyhUserExtension ue = new TGzjzyhUserExtension();
				ue.setUeMainId(application.getAppMainId());
				ue.setUeMainNo(application.getAppMainNo());
				ue.setUeMainName(application.getAppMainName());
				ue.setUeMainMobile(application.getAppMainMobile());
				ue.setUeMainId1(application.getAppMainId1());
				ue.setUeMainId2(application.getAppMainId2());
				ue.setUeMainNo1(application.getAppMainNo1());
				ue.setUeMainNo2(application.getAppMainNo2());
				
				ue.setUeHelpId(application.getAppHelpId());
				ue.setUeHelpNo(application.getAppHelpNo());
				ue.setUeHelpName(application.getAppHelpName());
				ue.setUeHelpMobile(application.getAppHelpMobile());
				ue.setUeHelpId1(application.getAppHelpId1());
				ue.setUeHelpId2(application.getAppHelpId2());
				ue.setUeHelpNo1(application.getAppHelpNo1());
				ue.setUeHelpNo2(application.getAppHelpNo2());
				
				this.model.setGzjzyhUserExtension(ue);
				
				TGzjzyhCase caseInfo = new TGzjzyhCase();
				caseInfo.setCaseCode(application.getAppCaseCode());
				caseInfo.setCaseName(application.getAppCaseName());
				caseInfo.setCaseConfirmTime(application.getAppCaseConfirmTime());
				
				this.model.setGzjzyhCase(caseInfo);
			}else {
				TGzjzyhUserExtension ue = this.policeRegisterManager
						.getUserExtensionByUserId(application.getAppUserid());
				this.model.setGzjzyhUserExtension(ue);
				TGzjzyhCase caseInfo = this.queryApplyService
						.getCaseById(application.getCaseId());
				this.model.setGzjzyhCase(caseInfo);
			}
			
			this.getBankAccountInfos();
			if(this.userList != null && !this.userList.isEmpty()) {
				for(TUumsBaseUser user : this.userList) {
					if(user.getUserId().equals(this.model.getGzjzyhApplication().getAppBankuser())) {
						this.bankUserName = user.getUserName();
					}
				}
			}
			this.appTypeName = this.typeMap.get(this.model.getGzjzyhApplication().getAppType());
			String appDateType = this.model.getGzjzyhApplication().getAppDateType();
			if("0".equals(appDateType)) {
				this.appDateDesc = "开启之日启至今";
			}else if("1".equals(appDateType)) {
				this.appDateDesc = "近一年";
			}else if("2".equals(appDateType)) {
				this.appDateDesc = TimeKit.formatDate(this.model.getGzjzyhApplication().getAppStartDate(), "yyyy-MM-dd")
						+ " 至 " + TimeKit.formatDate(this.model.getGzjzyhApplication().getAppEndDate(), "yyyy-MM-dd");
			}
		}
	}

	@Override
	public String save() throws Exception {
		try {
			this.bankService.doSign(model);
			// 添加日志信息
			String ip = getRequest().getRemoteAddr();
			String logInfo = "";
			logInfo = "签收了查询申请" + model.getGzjzyhApplication().getAppId();
			this.myLogManager.addLog(logInfo, ip);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "签收失败：" + e.getMessage();
		}
		return "close";
	}
	
	private void parseModel(){
		String appType = this.model.getGzjzyhApplication().getAppType();
		TGzjzyhApplication application = this.model.getGzjzyhApplication();
		if("0".equals(appType)){
			this.searchAppOrgAccount = wrapAccount(application.getAppOrgAccount());
			this.searchAppPersonAccount = wrapAccount(application.getAppPersonAccount());
			this.searchAppOrgDetail = wrapAccount(application.getAppOrgDetail());
			this.searchAppPersonDetail = wrapAccount(application.getAppPersonDetail());
			this.searchAppChadeDetail = wrapAccount(application.getAppChadeDetail());
			this.searchAppDateType = application.getAppDateType();
			this.searchAppStartDate = application.getAppStartDate();
			this.searchAppEndDate = application.getAppEndDate();
		}else if("1".equals(appType)){
			this.frozenAppOrgAccount = wrapAccount(application.getAppOrgAccount());
			this.frozenAppPersonAccount = wrapAccount(application.getAppPersonAccount());
			this.frozenAppStartDate = application.getAppStartDate();
			this.frozenAppEndDate = application.getAppEndDate();
		}else if("2".equals(appType)){
			this.continueAppOrgAccount = wrapAccount(application.getAppOrgAccount());
			this.continueAppPersonAccount = wrapAccount(application.getAppPersonAccount());
			this.continueAppStartDate = application.getAppStartDate();
			this.continueAppEndDate = application.getAppEndDate();
		}else if("3".equals(appType)){
			this.thawAppOrgAccount = wrapAccount(application.getAppOrgAccount());
			this.thawAppPersonAccount = wrapAccount(application.getAppPersonAccount());
			this.thawAppStartDate = application.getAppStartDate();
		}
	}
	
	private String wrapAccount(String account) {
		if(account == null || "".equals(account)) {
			return account;
		}
		return account.replaceAll(",", ",\\\r\\\n");
	}
	
	public void prepareGoPrint() throws Exception{
		prepareModel();
	}
	
	public String goPrint() throws Exception {
		this.parseModel();

		this.ueMainNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainId2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpId2Tmp = this.DEFAULT_UPLOAD_IMAGE;

		if (model.getGzjzyhUserExtension().getUeMainNo1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainNo1())) {
			this.ueMainNo1Tmp = model.getGzjzyhUserExtension()
					.getUeMainNo1();
		}
		if (model.getGzjzyhUserExtension().getUeMainNo2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainNo2())) {
			this.ueMainNo2Tmp = model.getGzjzyhUserExtension()
					.getUeMainNo2();
		}
		if (model.getGzjzyhUserExtension().getUeMainId1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainId1())) {
			this.ueMainId1Tmp = model.getGzjzyhUserExtension()
					.getUeMainId1();
		}
		if (model.getGzjzyhUserExtension().getUeMainId2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainId2())) {
			this.ueMainId2Tmp = model.getGzjzyhUserExtension()
					.getUeMainId2();
		}
		if (model.getGzjzyhUserExtension().getUeHelpNo1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpNo1())) {
			this.ueHelpNo1Tmp = model.getGzjzyhUserExtension()
					.getUeHelpNo1();
		}
		if (model.getGzjzyhUserExtension().getUeHelpNo2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpNo2())) {
			this.ueHelpNo2Tmp = model.getGzjzyhUserExtension()
					.getUeHelpNo2();
		}
		if (model.getGzjzyhUserExtension().getUeHelpId1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpId1())) {
			this.ueHelpId1Tmp = model.getGzjzyhUserExtension()
					.getUeHelpId1();
		}
		if (model.getGzjzyhUserExtension().getUeHelpId2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpId2())) {
			this.ueHelpId2Tmp = model.getGzjzyhUserExtension()
					.getUeHelpId2();
		}

		return "print";
	}
	
	public void prepareGetApplyView() throws Exception{
		prepareModel();
	}

	public String getApplyView() throws Exception {
		this.parseModel();

		this.appLawfileTmp = this.DEFAULT_UPLOAD_IMAGE;
		this.appLawfileRTmp = this.DEFAULT_UPLOAD_IMAGE;
		this.appAttachmentTmp = this.DEFAULT_UPLOAD_IMAGE;
		
		if (model.getGzjzyhApplication().getAppLawfile() != null
				&& !"".equals(model.getGzjzyhApplication().getAppLawfile())) {
			this.appLawfileTmp = model.getGzjzyhApplication()
					.getAppLawfile();
		}
		if (model.getGzjzyhApplication().getAppLawfileR() != null
				&& !"".equals(model.getGzjzyhApplication().getAppLawfileR())) {
			this.appLawfileRTmp = model.getGzjzyhApplication()
					.getAppLawfileR();
		}
		if (model.getGzjzyhApplication().getAppAttachment() != null
				&& !"".equals(model.getGzjzyhApplication().getAppAttachment())) {
			this.appAttachmentTmp = model.getGzjzyhApplication()
					.getAppAttachment();
		}

		this.ueMainNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainId2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpId2Tmp = this.DEFAULT_UPLOAD_IMAGE;

		if (model.getGzjzyhUserExtension().getUeMainNo1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainNo1())) {
			this.ueMainNo1Tmp = model.getGzjzyhUserExtension()
					.getUeMainNo1();
		}
		if (model.getGzjzyhUserExtension().getUeMainNo2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainNo2())) {
			this.ueMainNo2Tmp = model.getGzjzyhUserExtension()
					.getUeMainNo2();
		}
		if (model.getGzjzyhUserExtension().getUeMainId1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainId1())) {
			this.ueMainId1Tmp = model.getGzjzyhUserExtension()
					.getUeMainId1();
		}
		if (model.getGzjzyhUserExtension().getUeMainId2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeMainId2())) {
			this.ueMainId2Tmp = model.getGzjzyhUserExtension()
					.getUeMainId2();
		}
		if (model.getGzjzyhUserExtension().getUeHelpNo1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpNo1())) {
			this.ueHelpNo1Tmp = model.getGzjzyhUserExtension()
					.getUeHelpNo1();
		}
		if (model.getGzjzyhUserExtension().getUeHelpNo2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpNo2())) {
			this.ueHelpNo2Tmp = model.getGzjzyhUserExtension()
					.getUeHelpNo2();
		}
		if (model.getGzjzyhUserExtension().getUeHelpId1() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpId1())) {
			this.ueHelpId1Tmp = model.getGzjzyhUserExtension()
					.getUeHelpId1();
		}
		if (model.getGzjzyhUserExtension().getUeHelpId2() != null
				&& !"".equals(model.getGzjzyhUserExtension().getUeHelpId2())) {
			this.ueHelpId2Tmp = model.getGzjzyhUserExtension()
					.getUeHelpId2();
		}

		return "view";
	}

	public void downloadAttachment() throws Exception {
		String fileName = null;
		int index = this.appResponsefile1.lastIndexOf("/");
		if(index != -1) {
			fileName = this.appResponsefile1.substring(index + 1, this.appResponsefile1.length());
		}
		// 获取目标文件的绝对路径
		String filePath = FileKit.getProjectPath() + this.appResponsefile1;
		this.download(fileName, filePath);
	}
	
	private void download(String fileName, String filePath) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		response.setContentType("text/plain");
		response.setHeader("Location", URLEncoder.encode(fileName, "UTF-8"));
		response.setHeader("Content-Disposition", "attachment; filename="
				+ URLEncoder.encode(fileName, "UTF-8"));
		// 读取文件
		InputStream in = new FileInputStream(filePath);
		OutputStream out = response.getOutputStream();

		// 写文件
		int i;
		while ((i = in.read()) != -1) {
			out.write(i);
		}
		in.close();
		out.close();
	}


	public Page<TGzjzyhApplication> getPage() {
		return page;
	}

	public List<TUumsBaseUser> getUserList() {
		return userList;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	public String getFrozenAppOrgAccount() {
		return frozenAppOrgAccount;
	}

	public void setFrozenAppOrgAccount(String frozenAppOrgAccount) {
		this.frozenAppOrgAccount = frozenAppOrgAccount;
	}

	public String getFrozenAppPersonAccount() {
		return frozenAppPersonAccount;
	}

	public void setFrozenAppPersonAccount(String frozenAppPersonAccount) {
		this.frozenAppPersonAccount = frozenAppPersonAccount;
	}

	public String getContinueAppOrgAccount() {
		return continueAppOrgAccount;
	}

	public void setContinueAppOrgAccount(String continueAppOrgAccount) {
		this.continueAppOrgAccount = continueAppOrgAccount;
	}

	public String getContinueAppPersonAccount() {
		return continueAppPersonAccount;
	}

	public void setContinueAppPersonAccount(String continueAppPersonAccount) {
		this.continueAppPersonAccount = continueAppPersonAccount;
	}

	public String getThawAppOrgAccount() {
		return thawAppOrgAccount;
	}

	public void setThawAppOrgAccount(String thawAppOrgAccount) {
		this.thawAppOrgAccount = thawAppOrgAccount;
	}

	public String getThawAppPersonAccount() {
		return thawAppPersonAccount;
	}

	public void setThawAppPersonAccount(String thawAppPersonAccount) {
		this.thawAppPersonAccount = thawAppPersonAccount;
	}

	public Map<String, String> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<String, String> statusMap) {
		this.statusMap = statusMap;
	}

	public Map<String, String> getTypeMap() {
		return typeMap;
	}

	public void setTypeMap(Map<String, String> typeMap) {
		this.typeMap = typeMap;
	}

	public String getUeMainNo1Tmp() {
		return ueMainNo1Tmp;
	}

	public void setUeMainNo1Tmp(String ueMainNo1Tmp) {
		this.ueMainNo1Tmp = ueMainNo1Tmp;
	}

	public String getUeMainNo2Tmp() {
		return ueMainNo2Tmp;
	}

	public void setUeMainNo2Tmp(String ueMainNo2Tmp) {
		this.ueMainNo2Tmp = ueMainNo2Tmp;
	}

	public String getUeMainId1Tmp() {
		return ueMainId1Tmp;
	}

	public void setUeMainId1Tmp(String ueMainId1Tmp) {
		this.ueMainId1Tmp = ueMainId1Tmp;
	}

	public String getUeMainId2Tmp() {
		return ueMainId2Tmp;
	}

	public void setUeMainId2Tmp(String ueMainId2Tmp) {
		this.ueMainId2Tmp = ueMainId2Tmp;
	}

	public String getUeHelpNo1Tmp() {
		return ueHelpNo1Tmp;
	}

	public void setUeHelpNo1Tmp(String ueHelpNo1Tmp) {
		this.ueHelpNo1Tmp = ueHelpNo1Tmp;
	}

	public String getUeHelpNo2Tmp() {
		return ueHelpNo2Tmp;
	}

	public void setUeHelpNo2Tmp(String ueHelpNo2Tmp) {
		this.ueHelpNo2Tmp = ueHelpNo2Tmp;
	}

	public String getUeHelpId1Tmp() {
		return ueHelpId1Tmp;
	}

	public void setUeHelpId1Tmp(String ueHelpId1Tmp) {
		this.ueHelpId1Tmp = ueHelpId1Tmp;
	}

	public String getUeHelpId2Tmp() {
		return ueHelpId2Tmp;
	}

	public void setUeHelpId2Tmp(String ueHelpId2Tmp) {
		this.ueHelpId2Tmp = ueHelpId2Tmp;
	}

	public IUserService getUserService() {
		return userService;
	}

	public String getAppLawfileTmp() {
		return appLawfileTmp;
	}

	public void setAppLawfileTmp(String appLawfileTmp) {
		this.appLawfileTmp = appLawfileTmp;
	}

	public String getAppLawfileRTmp() {
		return appLawfileRTmp;
	}

	public void setAppLawfileRTmp(String appLawfileRTmp) {
		this.appLawfileRTmp = appLawfileRTmp;
	}

	public String getAppAttachmentTmp() {
		return appAttachmentTmp;
	}

	public void setAppAttachmentTmp(String appAttachmentTmp) {
		this.appAttachmentTmp = appAttachmentTmp;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSearchRequiredType() {
		return searchRequiredType;
	}

	public void setSearchRequiredType(String searchRequiredType) {
		this.searchRequiredType = searchRequiredType;
	}

	public String getSearchAppFileNo() {
		return searchAppFileNo;
	}

	public void setSearchAppFileNo(String searchAppFileNo) {
		this.searchAppFileNo = searchAppFileNo;
	}

	public String getSearchAppBankuser() {
		return searchAppBankuser;
	}

	public void setSearchAppBankuser(String searchAppBankuser) {
		this.searchAppBankuser = searchAppBankuser;
	}

	public Date getSearchAppDateStart() {
		return searchAppDateStart;
	}

	public void setSearchAppDateStart(Date searchAppDateStart) {
		this.searchAppDateStart = searchAppDateStart;
	}

	public Date getSearchAppDateEnd() {
		return searchAppDateEnd;
	}

	public void setSearchAppDateEnd(Date searchAppDateEnd) {
		this.searchAppDateEnd = searchAppDateEnd;
	}

	public String getSearchAppOrgAccount() {
		return searchAppOrgAccount;
	}

	public void setSearchAppOrgAccount(String searchAppOrgAccount) {
		this.searchAppOrgAccount = searchAppOrgAccount;
	}

	public String getSearchAppPersonAccount() {
		return searchAppPersonAccount;
	}

	public void setSearchAppPersonAccount(String searchAppPersonAccount) {
		this.searchAppPersonAccount = searchAppPersonAccount;
	}

	public String getSearchAppOrgDetail() {
		return searchAppOrgDetail;
	}

	public void setSearchAppOrgDetail(String searchAppOrgDetail) {
		this.searchAppOrgDetail = searchAppOrgDetail;
	}

	public String getSearchAppPersonDetail() {
		return searchAppPersonDetail;
	}

	public void setSearchAppPersonDetail(String searchAppPersonDetail) {
		this.searchAppPersonDetail = searchAppPersonDetail;
	}

	public String getSearchAppChadeDetail() {
		return searchAppChadeDetail;
	}

	public void setSearchAppChadeDetail(String searchAppChadeDetail) {
		this.searchAppChadeDetail = searchAppChadeDetail;
	}

	public String getSearchAppDateType() {
		return searchAppDateType;
	}

	public void setSearchAppDateType(String searchAppDateType) {
		this.searchAppDateType = searchAppDateType;
	}

	public Date getSearchAppStartDate() {
		return searchAppStartDate;
	}

	public void setSearchAppStartDate(Date searchAppStartDate) {
		this.searchAppStartDate = searchAppStartDate;
	}

	public Date getSearchAppEndDate() {
		return searchAppEndDate;
	}

	public void setSearchAppEndDate(Date searchAppEndDate) {
		this.searchAppEndDate = searchAppEndDate;
	}

	public Date getFrozenAppStartDate() {
		return frozenAppStartDate;
	}

	public void setFrozenAppStartDate(Date frozenAppStartDate) {
		this.frozenAppStartDate = frozenAppStartDate;
	}

	public Date getFrozenAppEndDate() {
		return frozenAppEndDate;
	}

	public void setFrozenAppEndDate(Date frozenAppEndDate) {
		this.frozenAppEndDate = frozenAppEndDate;
	}

	public Date getContinueAppStartDate() {
		return continueAppStartDate;
	}

	public void setContinueAppStartDate(Date continueAppStartDate) {
		this.continueAppStartDate = continueAppStartDate;
	}

	public Date getContinueAppEndDate() {
		return continueAppEndDate;
	}

	public void setContinueAppEndDate(Date continueAppEndDate) {
		this.continueAppEndDate = continueAppEndDate;
	}

	public Date getThawAppStartDate() {
		return thawAppStartDate;
	}

	public void setThawAppStartDate(Date thawAppStartDate) {
		this.thawAppStartDate = thawAppStartDate;
	}

	public String getBankUserName() {
		return bankUserName;
	}

	public void setBankUserName(String bankUserName) {
		this.bankUserName = bankUserName;
	}

	public String getAppTypeName() {
		return appTypeName;
	}

	public void setAppTypeName(String appTypeName) {
		this.appTypeName = appTypeName;
	}

	public String getAppDateDesc() {
		return appDateDesc;
	}

	public void setAppDateDesc(String appDateDesc) {
		this.appDateDesc = appDateDesc;
	}

	public String getAppResponsefile() {
		return appResponsefile;
	}

	public void setAppResponsefile(String appResponsefile) {
		this.appResponsefile = appResponsefile;
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}

	public String getSearchAppOrg() {
		return searchAppOrg;
	}

	public void setSearchAppOrg(String searchAppOrg) {
		this.searchAppOrg = searchAppOrg;
	}

	public String getAppResponsefile1() {
		return appResponsefile1;
	}

	public void setAppResponsefile1(String appResponsefile1) {
		this.appResponsefile1 = appResponsefile1;
	}

}
