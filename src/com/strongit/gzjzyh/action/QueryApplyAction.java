package com.strongit.gzjzyh.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.appConstants;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.po.TGzjzyhCase;
import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
import com.strongit.gzjzyh.policeregister.IPoliceRegisterManager;
import com.strongit.gzjzyh.service.IQueryApplyService;
import com.strongit.gzjzyh.util.FileKit;
import com.strongit.gzjzyh.vo.TGzjzyhApplyVo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.rolemanage.IRoleManager;
import com.strongit.uums.util.Const;
import com.strongit.workflow.util.TimeKit;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
public class QueryApplyAction extends BaseActionSupport {

	public TGzjzyhApplyVo model = new TGzjzyhApplyVo();

	private IQueryApplyService queryApplyService = null;

	@Autowired
	IRoleManager roleManager;
	@Autowired
	IPoliceRegisterManager policeRegisterManager;
	@Autowired
	DesktopSectionManager sectionManager;

	private Page<TGzjzyhApplication> page = new Page<TGzjzyhApplication>(10,
			true);

	private Page<TGzjzyhCase> casePage = new Page<TGzjzyhCase>(10, true);

	private String appId;
	
	private String caseId;

	private String searchRequiredType;

	private String searchAppFileNo;

	private String searchAppBankuser;

	private Date searchAppDateStart;

	private Date searchAppDateEnd;

	private String searchAppStatus;

	private String searchCaseName;

	private String searchCaseId;

	private String searchCaseCode;

	private List<TUumsBaseUser> userList = new ArrayList<TUumsBaseUser>();
	
	private String bankUserName;
	
	private String appTypeName;
	
	private String appDateDesc;
	
	private String appResponsefile;

	IUserService userService;

	private File uploadFile;// excel

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

	private String accountStr;// 导入账户返回值

	private String attrId;// 导入的属性ID

	private String errorMsg = "";
	
	private String isDesktop;
	private String blockId;
	
	public QueryApplyAction() {
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
	
	public void checkCanEdit() throws Exception{
		boolean canEdit = true;
		if(this.appId != null && !"".equals(this.appId)) {
			TGzjzyhApplication application = this.queryApplyService.getApplicationById(this.appId);
			if(!appConstants.APP_STATUS_REFUSE.equals(application.getAppStatus())
					&& !appConstants.STATUS_AUDIT_BACK.equals(application.getAppStatus())
					&& !appConstants.STATUS_SUBMIT_NO.equals(application.getAppStatus())) {
				canEdit = false;
			}
		}
		this.renderText(String.valueOf(canEdit));
	}

	@Override
	public String input() throws Exception {
		this.parseModel();
		
		this.getBankAccountInfos();

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
		page = this.queryApplyService.findQueryApplyPage(page,
				this.searchRequiredType, this.searchAppFileNo,
				this.searchAppBankuser, this.searchAppDateStart,
				this.searchAppDateEnd, this.searchCaseId, this.searchAppStatus);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (this.appId != null && !"".equals(this.appId)) {
			TUumsBaseUser user = this.userService.getCurrentUser();
			this.model = new TGzjzyhApplyVo();
			TGzjzyhUserExtension ue = this.policeRegisterManager
					.getUserExtensionByUserId(user.getUserId());
			this.model.setGzjzyhUserExtension(ue);
			TGzjzyhApplication application = this.queryApplyService
					.getApplicationById(this.appId);
			if(application.getAppDateType() == null || "".equals(application.getAppDateType())){
				application.setAppDateType("0");
			}
			this.model.setGzjzyhApplication(application);
			if(this.caseId != null && !"".equals(this.caseId)) {
				TGzjzyhCase caseInfo = this.queryApplyService
						.getCaseById(this.caseId);
				this.model.setGzjzyhCase(caseInfo);
			}else if(application.getCaseId() != null && !"".equals(application.getCaseId())){
				TGzjzyhCase caseInfo = this.queryApplyService
						.getCaseById(application.getCaseId());
				this.model.setGzjzyhCase(caseInfo);
			}else {
				this.model.setGzjzyhCase(new TGzjzyhCase());
			}
		} else {
			TUumsBaseUser user = this.userService.getCurrentUser();
			this.model = new TGzjzyhApplyVo();
			TGzjzyhUserExtension ue = this.policeRegisterManager
					.getUserExtensionByUserId(user.getUserId());
			this.model.setGzjzyhUserExtension(ue);
			this.model.setGzjzyhApplication(new TGzjzyhApplication());
			Date now = new Date();
			this.model.getGzjzyhApplication().setAppDate(now);
			this.model.getGzjzyhApplication().setAppUserid(user.getUserId());
			TUumsBaseOrg org = this.userService.getOrgInfoByOrgId(user
					.getOrgId());
			this.model.getGzjzyhApplication().setAppOrgId(org.getOrgId());
			this.model.getGzjzyhApplication().setAppOrg(org.getOrgName());
			this.model.getGzjzyhApplication().setAppStatus(appConstants.STATUS_SUBMIT_NO);
			this.model.getGzjzyhApplication().setAppType("0");
			this.model.getGzjzyhApplication().setAppDateType("0");
			if(this.caseId != null && !"".equals(this.caseId)) {
				TGzjzyhCase caseInfo = this.queryApplyService
						.getCaseById(this.caseId);
				this.model.setGzjzyhCase(caseInfo);
			}else {
				this.model.setGzjzyhCase(new TGzjzyhCase());
			}
		}
	}

	@Override
	public String save() throws Exception {
		this.buildModel();
		try {
			String opFlag = "add";
			if (model.getGzjzyhApplication().getAppId() != null
					&& !"".equals(model.getGzjzyhApplication().getAppId())) {
				opFlag = "edit";
			}else{
				model.getGzjzyhApplication().setAppId(null);
			}
			if (model.getGzjzyhCase().getCaseId() != null
					&& !"".equals(model.getGzjzyhCase().getCaseId())) {
			}else{
				model.getGzjzyhCase().setCaseId(null);
			}
			
			this.queryApplyService.save(model);
			
			// 添加日志信息
			String ip = getRequest().getRemoteAddr();
			String logInfo = "";
			if (opFlag.equals("add")) {
				logInfo = "添加了查询申请" + model.getGzjzyhApplication().getAppId();
			} else {
				logInfo = "编辑了查询申请" + model.getGzjzyhApplication().getAppId();
			}
			this.myLogManager.addLog(logInfo, ip);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "保存失败：" + e.getMessage();
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
	
	private void buildModel(){
		String appType = this.model.getGzjzyhApplication().getAppType();
		TGzjzyhApplication application = this.model.getGzjzyhApplication();
		if("0".equals(appType)){
			application.setAppOrgAccount(unWrapAccount(this.searchAppOrgAccount));
			application.setAppPersonAccount(unWrapAccount(this.searchAppPersonAccount));
			application.setAppOrgDetail(unWrapAccount(this.searchAppOrgDetail));
			application.setAppPersonDetail(unWrapAccount(this.searchAppPersonDetail));
			application.setAppChadeDetail(unWrapAccount(this.searchAppChadeDetail));
			application.setAppDateType(this.searchAppDateType);
			application.setAppStartDate(this.searchAppStartDate);
			application.setAppEndDate(this.searchAppEndDate);
		}else if("1".equals(appType)){
			application.setAppOrgAccount(unWrapAccount(this.frozenAppOrgAccount));
			application.setAppPersonAccount(unWrapAccount(this.frozenAppPersonAccount));
			application.setAppStartDate(this.frozenAppStartDate);
			application.setAppEndDate(this.frozenAppEndDate);
		}else if("2".equals(appType)){
			application.setAppOrgAccount(unWrapAccount(this.continueAppOrgAccount));
			application.setAppPersonAccount(unWrapAccount(this.continueAppPersonAccount));
			application.setAppStartDate(this.continueAppStartDate);
			application.setAppEndDate(this.continueAppEndDate);
		}else if("3".equals(appType)){
			application.setAppOrgAccount(unWrapAccount(this.thawAppOrgAccount));
			application.setAppPersonAccount(unWrapAccount(this.thawAppPersonAccount));
			application.setAppStartDate(this.thawAppStartDate);
		}
	}
	
	private String wrapAccount(String account) {
		if(account == null || "".equals(account)) {
			return account;
		}
		return account.replaceAll(",", ",\\\r\\\n");
	}
	
	private String unWrapAccount(String account) {
		if(account == null || "".equals(account)) {
			return account;
		}
		account = account.replaceAll("\\\r", "");
		account = account.replaceAll("\\\n", "");
		return account;
	}
	
	public void prepareDoCommits() throws Exception{
		prepareModel();
	}

	public String doCommits() throws Exception {
		this.buildModel();
		try {
			if (model.getGzjzyhCase().getCaseId() == null
					|| "".equals(model.getGzjzyhCase().getCaseId())) {
				model.getGzjzyhCase().setCaseId(null);
			}
			if (model.getGzjzyhApplication().getAppId() == null
					|| "".equals(model.getGzjzyhApplication().getAppId())) {
				model.getGzjzyhApplication().setAppId(null);
			}
			
			this.queryApplyService.saveOrCommit(model);
			
			// 添加日志信息
			String ip = getRequest().getRemoteAddr();
			String logInfo = "提交了查询申请" + model.getGzjzyhApplication().getAppId();
			this.myLogManager.addLog(logInfo, ip);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = "提交失败：" + e.getMessage();
		}
		return "close";
	}
	
	public void prepareGetApplyView() throws Exception{
		prepareModel();
	}

	public String getApplyView() throws Exception {
		this.parseModel();
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

	public String del() throws Exception {
		try {
			if ((this.appId != null) && (!("".equals(this.appId)))) {
				this.queryApplyService.delete(this.appId);
				// 添加删除操作的日志信息
				String ip = getRequest().getRemoteAddr();
				String logInfo = "";
				logInfo = "删除了查询申请:" + this.appId;
				this.myLogManager.addLog(logInfo, ip);
			}
			renderHtml("true");
		} catch (Exception e) {
			e.printStackTrace();
			renderHtml("false");
		}
		return null;
	}

	public String doCommit() throws Exception {
		try {
			if ((this.appId != null) && (!("".equals(this.appId)))) {
				this.queryApplyService.goCommits(this.appId);
				String ip = getRequest().getRemoteAddr();
				String logInfo = "";
				logInfo = "提交了查询申请:" + this.appId;
				this.myLogManager.addLog(logInfo, ip);
			}
			renderHtml("true");
		} catch (Exception e) {
			e.printStackTrace();
			renderHtml("false");
		}
		return null;
	}

	public String back() throws Exception {
		try {
			if ((this.appId != null) && (!("".equals(this.appId)))) {
				this.queryApplyService.goBack(this.appId);
				String ip = getRequest().getRemoteAddr();
				String logInfo = "";
				logInfo = "撤消了查询申请:" + this.appId;
				this.myLogManager.addLog(logInfo, ip);
			}
			renderHtml("true");
		} catch (Exception e) {
			e.printStackTrace();
			renderHtml("false");
		}
		return null;
	}

	/**
	 * 查找案件分页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String casePage() throws Exception {
		casePage = this.queryApplyService.findCasePage(casePage,
				this.searchCaseCode, this.searchCaseName);
		return "case";
	}

	public String imp() throws Exception {
		// TODO Auto-generated method stub
		return "imp";
	}

	/**
	 * @throws Exception
	 */
	public void downloadTemplat() throws Exception {
		String fileName = "账号导入模板.xls";
		// 获取目标文件的绝对路径
		String filePath = FileKit.getProjectPath() + "/template/account_template.xls";
		this.download(fileName, filePath);
	}
	
	public void downloadAttachment() throws Exception {
		String fileName = null;
		int index = this.appResponsefile.lastIndexOf("/");
		if(index != -1) {
			fileName = this.appResponsefile.substring(index + 1, this.appResponsefile.length());
		}
		// 获取目标文件的绝对路径
		String filePath = FileKit.getProjectPath() + this.appResponsefile;
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

	/**
	 * @throws Exception
	 */
	public String upload() throws Exception {
		try {
			List<String> lst = new ArrayList<String>();

			InputStream in = new FileInputStream(uploadFile);// 不用request.getInputStream();
			// 2.得到Excel工作簿对象
			HSSFWorkbook wb = new HSSFWorkbook(in);
			// 3.得到Excel工作表对象
			HSSFSheet sheet = wb.getSheetAt(0);
			// 总行数
			int trLength = sheet.getLastRowNum();
			// 4.得到Excel工作表的行
			HSSFRow row = sheet.getRow(0);
			// 总列数
			int tdLength = row.getLastCellNum();
			// 5.得到Excel工作表指定行的单元格
			// HSSFCell cell = row.getCell((short) 1);
			// 6.得到单元格样式
			// HSSFCellStyle cellStyle = cell.getCellStyle();
			for (int i = 1; i <= trLength; i++) {
				// 得到Excel工作表的行
				HSSFRow row1 = sheet.getRow(i);
				for (int j = 0; j < 1; j++) {
					// 得到Excel工作表指定行的单元格
					HSSFCell cell = row1.getCell((short) 0);
					// 获得每一列中的值
					lst.add(getStringCellValue(cell));
				}
			}
			StringBuffer appBf = new StringBuffer();
			if(lst != null && !lst.isEmpty()) {
				for (String str : lst) {
					appBf.append(str).append(",");
				}
				accountStr = appBf.toString();
				accountStr = accountStr.substring(0, accountStr.length() - 1);
				//accountStr = this.wrapAccount(accountStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.errorMsg = "导入文件错误，请下载导入模板并按模板规范填写账号。";
		}
		return "impinit";
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getStringCellValue(HSSFCell cell) {
		String strCell = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		if (cell == null) {
			return "";
		}
		return strCell;
	}
	
	public String showDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		
		String blockid = getRequest().getParameter("blockid");//获取blockid
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum"); 
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockid != null){
			Map<String,String> map = sectionManager.getParam(blockid);//通过blockid获取映射对象
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			showCreator = map.get("showCreator");//是否显示作者
			showDate = map.get("showDate");//是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		if(sectionFontSize == null || "".equals("sectionFontSize") || "null".equals("sectionFontSize") ){
			sectionFontSize = "14";
		}
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		//链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/action/queryApply.action")
			.append("', '查询申请'")
			.append(");");
		
		List<TGzjzyhApplication> list = null;
		Page<TGzjzyhApplication> desktopPage = new Page<TGzjzyhApplication>(num, false);
		desktopPage = this.queryApplyService.findDesktopQueryApplyPage(desktopPage);
		list = desktopPage.getResult();
		
		if(list!=null){
			this.getBankAccountInfos();
			if(this.userList != null && !this.userList.isEmpty()){
				for(TUumsBaseUser user : this.userList){
					this.userMap.put(user.getUserId(), user.getUserName());
				}
			}
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				TGzjzyhApplication notify = list.get(i);
				//标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript:window.showModalDialog('")
					.append(getRequest().getContextPath())
					.append("/action/queryApply!input.action?appId=").append(notify.getAppId())
					.append("&blockId=").append(blockid).append("&isDesktop=1").append("'")
					.append(",window,'help:no;status:no;scroll:no;dialogWidth:1500px; dialogHeight:800px'")
					.append(")");
				
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				
				String title = this.typeMap.get(notify.getAppType()) + "（" + this.userMap.get(notify.getAppBankuser()) + "）";
				if(title==null){
					title="";
				}
				
				StringBuilder tip = new StringBuilder();
				tip.append(title);
				
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<a href=\"#\" onclick=\"").append(titleLink.toString()).append("\">")
							.append("<span style=\"font-size: "+sectionFontSize+"px;\" title=\"").append(tip).append("\">").append(title).append("</span></a>");
				innerHtml.append("</td>");
				if("1".equals(showCreator)){//如果设置为显示作者，则显示作者信息
					innerHtml.append("<td width=\"115px\">");
					if(notify.getAppOrg() == null){
						innerHtml.append("");
					}else{
						innerHtml.append("<span class =\"linkgray\">").append(notify.getAppOrg() ).append("</span>&nbsp;&nbsp;&nbsp;");
					}
					innerHtml.append("</td>");
				}
				if("1".equals(showDate)){//如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("<span class =\"linkgray10\">").append(st.format(notify.getAppDate())).append("</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					innerHtml.append("</td>");
				}
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		if(list==null){
			for (int i = 0; i < num; i++) { // 获取在条数范围内的列表
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml.append("	&nbsp;");
				innerHtml.append("</td>");
				innerHtml.append("</tr>");
				innerHtml.append("	</table>");
				}
		}
		if(list!=null&&list.size()<num){
			for (int i = 0; i < num-list.size() ; i++) { // 获取在条数范围内的列表
			innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
			innerHtml.append("<tr>");
			innerHtml.append("<td>");
			innerHtml.append("	&nbsp;");
			innerHtml.append("</td>");
			innerHtml.append("</tr>");
			innerHtml.append("	</table>");
			}
		}
		innerHtml.append("<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ")
				 .append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}

	public Page<TGzjzyhApplication> getPage() {
		return page;
	}

	public List<TUumsBaseUser> getUserList() {
		return userList;
	}

	public Page<TGzjzyhCase> getCasePage() {
		return casePage;
	}

	public void setCasePage(Page<TGzjzyhCase> casePage) {
		this.casePage = casePage;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
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

	public String getAccountStr() {
		return accountStr;
	}

	public void setAccountStr(String accountStr) {
		this.accountStr = accountStr;
	}

	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
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

	public String getSearchAppStatus() {
		return searchAppStatus;
	}

	public void setSearchAppStatus(String searchAppStatus) {
		this.searchAppStatus = searchAppStatus;
	}

	public String getSearchCaseName() {
		return searchCaseName;
	}

	public void setSearchCaseName(String searchCaseName) {
		this.searchCaseName = searchCaseName;
	}

	public String getSearchCaseId() {
		return searchCaseId;
	}

	public void setSearchCaseId(String searchCaseId) {
		this.searchCaseId = searchCaseId;
	}

	public String getSearchCaseCode() {
		return searchCaseCode;
	}

	public void setSearchCaseCode(String searchCaseCode) {
		this.searchCaseCode = searchCaseCode;
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

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getIsDesktop() {
		return isDesktop;
	}

	public void setIsDesktop(String isDesktop) {
		this.isDesktop = isDesktop;
	}

}
