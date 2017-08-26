package com.strongit.gzjzyh.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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
import com.strongit.gzjzyh.service.IQueryApplyService;
import com.strongit.gzjzyh.vo.TGzjzyhApplyVo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
import com.strongmvc.exception.ServiceException;

@ParentPackage("default")
public class QueryApplyAction extends BaseActionSupport {

	public TGzjzyhApplyVo model = new TGzjzyhApplyVo();

	private IQueryApplyService queryApplyService = null;

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

	private MyLogManager myLogManager;

	private static final String DEFAULT_UPLOAD_IMAGE = "/images/upload/defaultUpload.jpg";

	private String lawDocTmp;//文书

	private String lawRecTmp;//回执

	private String otherTmp;//其它

	private String ueMainNo1Tmp;

	private String ueMainNo2Tmp;

	private String ueMainId1Tmp;

	private String ueMainId2Tmp;

	private String ueHelpNo1Tmp;

	private String ueHelpNo2Tmp;

	private String ueHelpId1Tmp;

	private String ueHelpId2Tmp;

	private String accountStr;//导入账户返回值 
	
	private String attrId;//导入的属性ID
	
	public QueryApplyAction() {
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
		TUumsBaseUser user = this.userService.getCurrentUser();
		model = this.queryApplyService.getExtensionByUserId(user.getUserId());
		//
		this.lawDocTmp = this.DEFAULT_UPLOAD_IMAGE;
		this.lawRecTmp = this.DEFAULT_UPLOAD_IMAGE;
		this.otherTmp = this.DEFAULT_UPLOAD_IMAGE;

		this.ueMainNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueMainId2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		this.ueHelpId2Tmp = this.DEFAULT_UPLOAD_IMAGE;

		return INPUT;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		userList = userService.getUserInfoByOrgId(
				GzjzyhConstants.DEFAULT_BANKORG_SYSCODE, "0", "1");
		page = this.queryApplyService.findQueryApplyPage(page, accoutType,
				appFileno, appBankuser, appStartDate, appEndDate, caseCode);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() throws Exception {

		try {
			//冻解申请
			if ("1".equals(model.getGzjzyhApplication().getAppType())) {
				model.getGzjzyhApplication()
						.setAppOrgAccount(frozenAppOrgAccount);
				model.getGzjzyhApplication()
						.setAppPersonAccount(frozenAppPersonAccount);
			}
			//续冻申请
			if ("2".equals(model.getGzjzyhApplication().getAppType())) {
				model.getGzjzyhApplication()
						.setAppOrgAccount(continueAppOrgAccount);
				model.getGzjzyhApplication()
						.setAppPersonAccount(continueAppPersonAccount);
			}
			//解冻申请
			if ("3".equals(model.getGzjzyhApplication().getAppType())) {
				model.getGzjzyhApplication()
						.setAppOrgAccount(thawAppOrgAccount);
				model.getGzjzyhApplication()
						.setAppPersonAccount(thawAppPersonAccount);
			}
			TUumsBaseUser user = this.userService.getCurrentUser();
			model.getGzjzyhApplication().setAppUserid(user.getUserId());
			model.getGzjzyhApplication()
					.setCaseId(model.getGzjzyhCase().getCaseId());

			if (model.getGzjzyhApplication().getAppId() != null
					&& !"".equals(model.getGzjzyhApplication().getAppId())) {
				this.queryApplyService.update(model);
			} else {
				model.getGzjzyhApplication().setAppId(null);
				model.getGzjzyhCase().setCaseId(null);
				model.getGzjzyhUserExtension().setUeId(null);
				this.queryApplyService.save(model);
			}
		} catch (Exception e) {
			String x = e.getCause().getMessage();
			System.out.println(x);
			// TODO: handle exception
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(x);
		}

		return "close";
	}

	public String doCommits() throws Exception {

		try {
			//冻解申请
			if ("1".equals(model.getGzjzyhApplication().getAppType())) {
				model.getGzjzyhApplication()
						.setAppOrgAccount(frozenAppOrgAccount);
				model.getGzjzyhApplication()
						.setAppPersonAccount(frozenAppPersonAccount);
			}
			//续冻申请
			if ("2".equals(model.getGzjzyhApplication().getAppType())) {
				model.getGzjzyhApplication()
						.setAppOrgAccount(continueAppOrgAccount);
				model.getGzjzyhApplication()
						.setAppPersonAccount(continueAppPersonAccount);
			}
			//解冻申请
			if ("3".equals(model.getGzjzyhApplication().getAppType())) {
				model.getGzjzyhApplication()
						.setAppOrgAccount(thawAppOrgAccount);
				model.getGzjzyhApplication()
						.setAppPersonAccount(thawAppPersonAccount);
			}
			TUumsBaseUser user = this.userService.getCurrentUser();
			model.getGzjzyhApplication().setAppUserid(user.getUserId());
			model.getGzjzyhApplication()
					.setCaseId(model.getGzjzyhCase().getCaseId());

			if (model.getGzjzyhApplication().getAppId() != null
					&& !"".equals(model.getGzjzyhApplication().getAppId())) {
				this.queryApplyService.updateOrCommit(model);
			} else {
				model.getGzjzyhApplication().setAppId(null);
				model.getGzjzyhCase().setCaseId(null);
				model.getGzjzyhUserExtension().setUeId(null);
				this.queryApplyService.saveOrCommit(model);
			}
		} catch (Exception e) {
			String x = e.getCause().getMessage();
			System.out.println(x);
			// TODO: handle exception
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(x);
		}

		return "close";
	}

	public String getApply() throws Exception {
		// TODO Auto-generated method stub
		model = this.queryApplyService.getApplyById(appIds);
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

		//
		if (model.getGzjzyhApplication().getAppLawfile() == null
				|| "".equals(model.getGzjzyhApplication().getAppLawfile())) {
			this.lawDocTmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.lawDocTmp = this.model.getGzjzyhApplication().getAppLawfile();
		}
		//
		if (model.getGzjzyhApplication().getAppLawfileR() == null
				|| "".equals(model.getGzjzyhApplication().getAppLawfileR())) {
			this.lawRecTmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.lawRecTmp = this.model.getGzjzyhApplication().getAppLawfileR();
		}
		//
		if (model.getGzjzyhApplication().getAppAttachment() == null
				|| "".equals(model.getGzjzyhApplication().getAppAttachment())) {
			this.otherTmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.otherTmp = this.model.getGzjzyhApplication()
					.getAppAttachment();
		}

		if (model.getGzjzyhUserExtension().getUeMainId1() == null
				|| "".equals(model.getGzjzyhUserExtension().getUeMainId1())) {
			this.ueMainId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.ueMainId1Tmp = this.model.getGzjzyhUserExtension()
					.getUeMainId1();
		}
		if (model.getGzjzyhUserExtension().getUeMainId2() == null
				|| "".equals(model.getGzjzyhUserExtension().getUeMainId2())) {
			this.ueMainId2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.ueMainId2Tmp = this.model.getGzjzyhUserExtension()
					.getUeMainId2();
		}
		if (model.getGzjzyhUserExtension().getUeMainNo1() == null
				|| "".equals(model.getGzjzyhUserExtension().getUeMainNo1())) {
			this.ueMainNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.ueMainNo1Tmp = model.getGzjzyhUserExtension().getUeMainNo1();
		}
		if (model.getGzjzyhUserExtension().getUeMainNo2() == null
				|| "".equals(model.getGzjzyhUserExtension().getUeMainNo2())) {
			this.ueMainNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.ueMainNo2Tmp = model.getGzjzyhUserExtension().getUeMainNo2();
		}

		if (model.getGzjzyhUserExtension().getUeHelpId1() == null
				|| "".equals(model.getGzjzyhUserExtension().getUeHelpId1())) {
			this.ueHelpId1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.ueHelpId1Tmp = model.getGzjzyhUserExtension().getUeHelpId1();
		}
		if (model.getGzjzyhUserExtension().getUeHelpId2() == null
				|| "".equals(model.getGzjzyhUserExtension().getUeHelpId2())) {
			this.ueHelpId2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.ueHelpId2Tmp = model.getGzjzyhUserExtension().getUeHelpId2();
		}
		if (model.getGzjzyhUserExtension().getUeHelpNo1() == null
				|| "".equals(model.getGzjzyhUserExtension().getUeHelpNo1())) {
			this.ueHelpNo1Tmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.ueHelpNo1Tmp = model.getGzjzyhUserExtension().getUeHelpNo1();
		}
		if (model.getGzjzyhUserExtension().getUeHelpNo2() == null
				|| "".equals(model.getGzjzyhUserExtension().getUeHelpNo2())) {
			this.ueHelpNo2Tmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.ueHelpNo2Tmp = model.getGzjzyhUserExtension().getUeHelpNo2();
		}

		return INPUT;
	}

	public String getApplyView() throws Exception {
		// TODO Auto-generated method stub
		model = this.queryApplyService.getViewById(appIds);
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

		//
		if (model.getGzjzyhApplication().getAppLawfile() == null
				|| "".equals(model.getGzjzyhApplication().getAppLawfile())) {
			this.lawDocTmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.lawDocTmp = this.model.getGzjzyhApplication().getAppLawfile();
		}
		//
		if (model.getGzjzyhApplication().getAppLawfileR() == null
				|| "".equals(model.getGzjzyhApplication().getAppLawfileR())) {
			this.lawRecTmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.lawRecTmp = this.model.getGzjzyhApplication().getAppLawfileR();
		}
		//
		if (model.getGzjzyhApplication().getAppAttachment() == null
				|| "".equals(model.getGzjzyhApplication().getAppAttachment())) {
			this.otherTmp = this.DEFAULT_UPLOAD_IMAGE;
		} else {
			this.otherTmp = this.model.getGzjzyhApplication()
					.getAppAttachment();
		}
		return "view";
	}

	public String del() throws Exception {
		// TODO Auto-generated method stub
		try {
			if ((appIds != null) && (!("".equals(appIds)))) {
				this.queryApplyService.delete(appIds);
			}
			addActionMessage("删除成功");
			// 添加删除操作的日志信息
			String ip = getRequest().getRemoteAddr();
			String logInfo = "";
			logInfo = "删除了查询申请:" + appIds;
			this.myLogManager.addLog(logInfo, ip);
			renderHtml("true");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			renderHtml("false");
		}
		return null;
	}

	public String doCommit() throws Exception {
		// TODO Auto-generated method stub
		try {
			if ((appIds != null) && (!("".equals(appIds)))) {
				this.queryApplyService.goCommits(appIds);
			}
			addActionMessage("提交成功");

			String ip = getRequest().getRemoteAddr();
			String logInfo = "";
			logInfo = "提交了查询申请:" + appIds;
			this.myLogManager.addLog(logInfo, ip);
			renderHtml("true");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			renderHtml("false");
		}
		return null;
	}

	public String back() throws Exception {
		// TODO Auto-generated method stub
		try {
			if ((appIds != null) && (!("".equals(appIds)))) {
				this.queryApplyService.goBack(appIds);
			}
			addActionMessage("撤消成功");

			String ip = getRequest().getRemoteAddr();
			String logInfo = "";
			logInfo = "撤消了查询申请:" + appIds;
			this.myLogManager.addLog(logInfo, ip);
			renderHtml("true");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			renderHtml("false");
		}
		return null;
	}

	/**查找案件分页
	 * @return
	 * @throws Exception
	 */
	public String casePage() throws Exception {
		// TODO Auto-generated method stub
		casePage = this.queryApplyService.findCasePage(casePage, caseCode,
				caseName);
		return "case";
	}

	public String imp() throws Exception {
		// TODO Auto-generated method stub
		return "imp";
	}

	/**
	 * @throws Exception
	 */
	public void download() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		String name = "账户模版.xls";

		//获取目标文件的绝对路径 
		String path = ServletActionContext.getServletContext()
				.getRealPath("/account_template.xls");

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

	/**
	 * @throws Exception
	 */
	public String upload() throws Exception {
		try {
			Set<String> set = new HashSet<String>();

			InputStream in = new FileInputStream(uploadFile);//不用request.getInputStream();
			//2.得到Excel工作簿对象  
			HSSFWorkbook wb = new HSSFWorkbook(in);
			//3.得到Excel工作表对象  
			HSSFSheet sheet = wb.getSheetAt(0);
			//总行数  
			int trLength = sheet.getLastRowNum();
			//4.得到Excel工作表的行  
			HSSFRow row = sheet.getRow(0);
			//总列数  
			int tdLength = row.getLastCellNum();
			//5.得到Excel工作表指定行的单元格  
			//			HSSFCell cell = row.getCell((short) 1);
			//6.得到单元格样式  
			//			HSSFCellStyle cellStyle = cell.getCellStyle();
			for (int i = 0; i < trLength; i++) {
				//得到Excel工作表的行  
				HSSFRow row1 = sheet.getRow(i);
				for (int j = 0; j < tdLength; j++) {

					//得到Excel工作表指定行的单元格  
					HSSFCell cell = row1.getCell((short) 0);

					/** 
					 * 为了处理：Excel异常Cannot get a text value from a numeric cell 
					 * 将所有列中的内容都设置成String类型格式 
					 */
//					if (cell != null) {
//						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//					}

					//获得每一列中的值  
					set.add(getStringCellValue(cell));
					//					System.out.print(cell.getStringCellValue() + "\t\t\t");
				}
			}
			StringBuffer appBf = new StringBuffer();
			for (String str : set) {
				appBf.append(str).append(",");
			}
			
			accountStr = appBf.toString();
			accountStr = accountStr.substring(0, accountStr.length() - 1);
			


		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		return "impinit";
	}
	/**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell Excel单元格
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

	public String getLawDocTmp() {
		return lawDocTmp;
	}

	public void setLawDocTmp(String lawDocTmp) {
		this.lawDocTmp = lawDocTmp;
	}

	public String getLawRecTmp() {
		return lawRecTmp;
	}

	public void setLawRecTmp(String lawRecTmp) {
		this.lawRecTmp = lawRecTmp;
	}

	public String getOtherTmp() {
		return otherTmp;
	}

	public void setOtherTmp(String otherTmp) {
		this.otherTmp = otherTmp;
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

}
