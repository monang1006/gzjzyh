package com.strongit.gzjzyh.bankaccount;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.security.ApplicationConfig;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results({ @Result(name = BaseActionSupport.RELOAD, value = "bankAccount.action", type = ServletActionRedirectResult.class) })
public class BankAccountAction extends BaseActionSupport<TUumsBaseUser> {

	private Page<TUumsBaseUser> page = new Page<TUumsBaseUser>(
			FlexTableTag.MIDDLE_ROWS, true);

	private String userId;

	// 用户是否启用
	private Map userActiveTypeMap = new HashMap();

	private TUumsBaseUser model = new TUumsBaseUser();

	@Autowired
	private MyLogManager myLogManager;
	
	@Autowired
	private MyInfoManager myInfoManager;

	@Autowired
	IUserService userService;
	
	@Autowired
	IBankAccountManager bankAccountManager;

	/*
	 * 查询相关属性
	 */
	private String selectname;// 用户名称

	private String selectloginname;// 用户登陆名

	private String isActive;// 是否启用

	private String selectorg = "";// 用户所属组织机构编码

	// 全局设置信息
	@Autowired
	ApplicationConfig applicationConfig;

	// 密码是否要md5加密
	private String md5Enable;

	public String getMd5Enable() {
		return md5Enable;
	}

	public void setMd5Enable(String md5Enable) {
		this.md5Enable = md5Enable;
	}

	/**
	 * 构造方法
	 * 
	 */
	private BankAccountAction() {
		userActiveTypeMap.put("0", "未启用");
		userActiveTypeMap.put("1", "已启用");
	}

	/**
	 * 把字符串转成utf8编码，保证中文文件名不会乱码
	 * 
	 * @param s
	 * @return
	 */
	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = Character.toString(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String delete() throws Exception {
		try {
			if ((userId != null) && (!("".equals(userId)))) {
				this.bankAccountManager.delete(userId);
			}
				
			addActionMessage("删除成功");
			// 添加删除操作的日志信息
			String ip = getRequest().getRemoteAddr();
			String logInfo = "";
			logInfo = "删除了银行账号:" + userId;
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

	@Override
	public String input() throws Exception {
		// prepareModel();
		// 获取md5加密设置
		if (applicationConfig.isMd5Enable()) {
			this.md5Enable = "1";
		} else {
			this.md5Enable = "0";
		}

		String isSupman = userService.getCurrentUserInfo()
				.getUserIsSupManager();
		this.getRequest().setAttribute("isSupman", isSupman);
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		TUumsBaseOrg org = userService
				.getOrgInfoBySyscode(GzjzyhConstants.DEFAULT_BANKORG_SYSCODE);
		String extOrgId = org.getOrgId();
		page = userService.queryOrgUsers(page, extOrgId, selectname,
				selectloginname, "0", isActive);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		try {
			if (userId != null && !"".equals(userId) && !"null".equals(userId)) {
				model = userService.getUumsUserInfoByUserId(userId);
			} else {
				model = new TUumsBaseUser();
				model.setUserSequence(userService.getNextUserSequenceCode());
				TUumsBaseOrg org = userService
						.getOrgInfoBySyscode(GzjzyhConstants.DEFAULT_BANKORG_SYSCODE);
				model.setOrgId(org.getOrgId());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String save() throws Exception {
		// 添加用户还是编辑用户的标志
		String flag = "edit";
		if ("".equals(model.getUserId()) || model.getUserId() == null) {
			flag = "add";
			model.setUserId(null);
		}
		this.bankAccountManager.save(model);
		
		addActionMessage("保存成功");

		// 添加日志信息
		String ip = getRequest().getRemoteAddr();
		String logInfo = "";
		if (flag.equals("add")) {
			logInfo = "添加了银行账号" + model.getUserName();
		} else {
			logInfo = "编辑了银行账号" + model.getUserName();
		}

		this.myLogManager.addLog(logInfo, ip);

		return "winclose";
	}

	public TUumsBaseUser getModel() {
		return model;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MyLogManager getMyLogManager() {
		return myLogManager;
	}

	@Autowired
	public void setMyLogManager(MyLogManager myLogManager) {
		this.myLogManager = myLogManager;
	}

	public Page<TUumsBaseUser> getPage() {
		return page;
	}

	public String getSelectname() {
		return selectname;
	}

	public void setSelectname(String selectname) {
		this.selectname = selectname;
	}

	public String getSelectloginname() {
		return selectloginname;
	}

	public void setSelectloginname(String selectloginname) {
		this.selectloginname = selectloginname;
	}

	public String getSelectorg() {
		return selectorg;
	}

	public void setSelectorg(String selectorg) {
		this.selectorg = selectorg;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Map getUserActiveTypeMap() {
		return userActiveTypeMap;
	}

	public void setUserActiveTypeMap(Map userActiveTypeMap) {
		this.userActiveTypeMap = userActiveTypeMap;
	}
}