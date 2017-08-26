package com.strongit.gzjzyh.action;

import java.io.File;
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

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.gzjzyh.appConstants;
import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.po.TGzjzyhCase;
import com.strongit.gzjzyh.service.IQueryBankService;
import com.strongit.gzjzyh.vo.TGzjzyhApplyVo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
public class QueryBankAction extends BaseActionSupport {

	public TGzjzyhApplyVo model = new TGzjzyhApplyVo();

	private IQueryBankService queryBankService = null;

	private Page<TGzjzyhApplication> page = new Page<TGzjzyhApplication>(10,
			true);

	private Page<TGzjzyhCase> casePage = new Page<TGzjzyhCase>(10, true);

	private String accoutType;

	private String appFileno;

	private String appBankuser;

	private Date appStartDate;

	private Date appEndDate;

	private List<TUumsBaseUser> userList = new ArrayList<TUumsBaseUser>();

	private String caseCode;

	private String caseName;

	private String appIds;

	IUserService userService;

	private File uploadFile;//excel

	private String uploadFileFileName;

	private String frozenAppOrgAccount;

	private String frozenAppPersonAccount;

	private String continueAppOrgAccount;

	private String continueAppPersonAccount;

	private String thawAppOrgAccount;

	private String thawAppPersonAccount;

	private Map<String, String> statusMap = new HashMap<String, String>();

	private Map<String, String> typeMap = new HashMap<String, String>();

	private File bank;

	private String bankFileName;

	public QueryBankAction() {
		
		statusMap.put(appConstants.STATUS_SUBMIT_NO, "待提交");
		statusMap.put(appConstants.STATUS_SUBMIT_YES, "已提交");
		statusMap.put(appConstants.STATUS_AUDIT_YES, "已审核");
		statusMap.put(appConstants.STATUS_AUDIT_BACK, "已驳回");

		statusMap.put(appConstants.STATUS_SIGN_YES, "已签收");
		statusMap.put(appConstants.STATUS_PROCESS_YES, "已处理");
		statusMap.put(appConstants.APP_STATUS_REFUSE, "已拒签");

		typeMap.put("0", "查询申请");
		typeMap.put("1", "冻解申请");
		typeMap.put("2", "续冻申请");
		typeMap.put("3", "解冻申请");
	}

	@Autowired
	public void setQueryBankService(IQueryBankService queryBankService) {
		this.queryBankService = queryBankService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return this.model;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		userList = userService.getUserInfoByOrgId("001999", "0", "1");
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		userList = userService.getUserInfoByOrgId("001999", "0", "1");
		page = this.queryBankService.findQueryBankNotSignPage(page, accoutType,
				appFileno, appBankuser, appStartDate, appEndDate);
		return SUCCESS;
	}

	//已签
	public String bankSigList() throws Exception {
		// TODO Auto-generated method stub
		userList = userService.getUserInfoByOrgId("001999", "0", "1");
		page = this.queryBankService.findQueryBankSignPage(page, accoutType,
				appFileno, appBankuser, appStartDate, appEndDate);
		return "signlist";
	}

	//已处理
	public String processList() throws Exception {
		// TODO Auto-generated method stub
		userList = userService.getUserInfoByOrgId("001999", "0", "1");
		page = this.queryBankService.findQueryBankProcessedPage(page,
				accoutType, appFileno, appBankuser, appStartDate, appEndDate);
		return "processlist";
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	public String getApply() throws Exception {
		// TODO Auto-generated method stub
		model = this.queryBankService.getApplyById(appIds);
		//冻解申请
		if ("1".equals(model.getGzjzyhApplication().getAppType())) {
			frozenAppOrgAccount = model.getGzjzyhApplication()
					.getAppOrgAccount();
			frozenAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		//续冻申请
		if ("2".equals(model.getGzjzyhApplication().getAppType())) {
			continueAppOrgAccount = model.getGzjzyhApplication()
					.getAppOrgAccount();
			continueAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		//解冻申请
		if ("3".equals(model.getGzjzyhApplication().getAppType())) {
			thawAppOrgAccount = model.getGzjzyhApplication().getAppOrgAccount();
			thawAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		return INPUT;
	}

	public String getAppProcess() throws Exception {
		// TODO Auto-generated method stub
		model = this.queryBankService.getApplyById(appIds);
		//冻解申请
		if ("1".equals(model.getGzjzyhApplication().getAppType())) {
			frozenAppOrgAccount = model.getGzjzyhApplication()
					.getAppOrgAccount();
			frozenAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		//续冻申请
		if ("2".equals(model.getGzjzyhApplication().getAppType())) {
			continueAppOrgAccount = model.getGzjzyhApplication()
					.getAppOrgAccount();
			continueAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		//解冻申请
		if ("3".equals(model.getGzjzyhApplication().getAppType())) {
			thawAppOrgAccount = model.getGzjzyhApplication().getAppOrgAccount();
			thawAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		return "process";
	}

	public String getAppView() throws Exception {
		// TODO Auto-generated method stub
		model = this.queryBankService.getApplyById(appIds);
		//冻解申请
		if ("1".equals(model.getGzjzyhApplication().getAppType())) {
			frozenAppOrgAccount = model.getGzjzyhApplication()
					.getAppOrgAccount();
			frozenAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		//续冻申请
		if ("2".equals(model.getGzjzyhApplication().getAppType())) {
			continueAppOrgAccount = model.getGzjzyhApplication()
					.getAppOrgAccount();
			continueAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		//解冻申请
		if ("3".equals(model.getGzjzyhApplication().getAppType())) {
			thawAppOrgAccount = model.getGzjzyhApplication().getAppOrgAccount();
			thawAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		return "view";
	}

	//签收
	@Override
	public String save() throws Exception {
		TUumsBaseUser user = this.userService.getCurrentUser();
		model.getGzjzyhApplication().setAppUserid(user.getUserId());
		model.getGzjzyhApplication().setAppAuditUser(user.getUserName());
		this.queryBankService.doSign(model);

		return "close";
	}

	public String back() throws Exception {
		// TODO Auto-generated method stub
		TUumsBaseUser user = this.userService.getCurrentUser();
		model.getGzjzyhApplication().setAppUserid(user.getUserId());
		model.getGzjzyhApplication().setAppAuditUser(user.getUserName());
		model.getGzjzyhApplication().setAppId(appIds);
		this.queryBankService.back(model);
		addActionMessage("退回成功");
		return "temp";
	}

	public String view() throws Exception {
		// TODO Auto-generated method stub
		model = this.queryBankService.getApplyById(appIds);
		//冻解申请
		if ("1".equals(model.getGzjzyhApplication().getAppType())) {
			frozenAppOrgAccount = model.getGzjzyhApplication()
					.getAppOrgAccount();
			frozenAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		//续冻申请
		if ("2".equals(model.getGzjzyhApplication().getAppType())) {
			continueAppOrgAccount = model.getGzjzyhApplication()
					.getAppOrgAccount();
			continueAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		//解冻申请
		if ("3".equals(model.getGzjzyhApplication().getAppType())) {
			thawAppOrgAccount = model.getGzjzyhApplication().getAppOrgAccount();
			thawAppPersonAccount = model.getGzjzyhApplication()
					.getAppPersonAccount();
		}
		return "view";
	}

	//处理
	public String doProcess() throws Exception {

		try {
			String realpath = ServletActionContext.getServletContext()
					.getRealPath("/bankquery"); //路径 
			System.out.println("realpath: " + realpath);
			if (bank != null && bank.isFile()) {
				File saveFile = new File(new File(realpath), bankFileName);
				if (!saveFile.getParentFile().exists()) {
					saveFile.getParentFile().mkdirs();
				}
				FileUtils.copyFile(bank, saveFile);

				TUumsBaseUser user = this.userService.getCurrentUser();
				model.getGzjzyhApplication().setAppReceiverId(user.getUserId());
				model.getGzjzyhApplication().setAppReceiver(user.getUserName());
				model.getGzjzyhApplication()
						.setAppResponsefile(saveFile.getAbsolutePath());
				this.queryBankService.process(model);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}

		return "close";
	}

	/**
	 * @throws Exception
	 */
	public void download() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		//获取目标文件的绝对路径 
		String path = model.getGzjzyhApplication().getAppResponsefile();
		File f = new File(path);
		String name = f.getName();

		response.setContentType("text/plain");
		response.setHeader("Location", URLEncoder.encode(name, "UTF-8"));
		response.setHeader("Content-Disposition",
				"attachment; filename=" + URLEncoder.encode(name, "UTF-8"));
		//读取文件  
		InputStream in = new FileInputStream(path);
		OutputStream out = response.getOutputStream();

		//写文件  
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

	public String getAccoutType() {
		return accoutType;
	}

	public void setAccoutType(String accoutType) {
		this.accoutType = accoutType;
	}

	public String getAppFileno() {
		return appFileno;
	}

	public void setAppFileno(String appFileno) {
		this.appFileno = appFileno;
	}

	public String getAppBankuser() {
		return appBankuser;
	}

	public void setAppBankuser(String appBankuser) {
		this.appBankuser = appBankuser;
	}

	public Date getAppStartDate() {
		return appStartDate;
	}

	public void setAppStartDate(Date appStartDate) {
		this.appStartDate = appStartDate;
	}

	public Date getAppEndDate() {
		return appEndDate;
	}

	public void setAppEndDate(Date appEndDate) {
		this.appEndDate = appEndDate;
	}

	public List<TUumsBaseUser> getUserList() {
		return userList;
	}

	public String getCaseCode() {
		return caseCode;
	}

	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
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

	public String getAppIds() {
		return appIds;
	}

	public void setAppIds(String appIds) {
		this.appIds = appIds;
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

	public File getBank() {
		return bank;
	}

	public void setBank(File bank) {
		this.bank = bank;
	}

	public String getBankFileName() {
		return bankFileName;
	}

	public void setBankFileName(String bankFileName) {
		this.bankFileName = bankFileName;
	}

}
