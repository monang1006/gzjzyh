package com.strongit.gzjzyh.policeregister;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
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
@Results({ @Result(name = BaseActionSupport.RELOAD, value = "policeRegister.action", type = ServletActionRedirectResult.class) })
public class PoliceRegisterAction extends BaseActionSupport<TGzjzyhUserExtension> {

	private Page<TGzjzyhUserExtension> page = new Page<TGzjzyhUserExtension>(
			FlexTableTag.MIDDLE_ROWS, true);

	private String ueId;

	// 用户是否启用
	private Map userActiveTypeMap = new HashMap();

	private TGzjzyhUserExtension model = new TGzjzyhUserExtension();

	@Autowired
	private MyLogManager myLogManager;
	
	@Autowired
	private MyInfoManager myInfoManager;

	@Autowired
	IUserService userService;
	
	@Autowired
	IPoliceRegisterManager registerManager;

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

		String isSupman = "0";
		this.getRequest().setAttribute("isSupman", isSupman);
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		try {
			if (ueId != null && !"".equals(ueId) && !"null".equals(ueId)) {
				model = this.registerManager.getUserExtensionById(ueId);
			} else {
				model = new TGzjzyhUserExtension();
				TUumsBaseUser user = new TUumsBaseUser();
				model.setTuumsBaseUser(user);
				user.setUserSequence(userService.getNextUserSequenceCode());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public String save() throws Exception {
		// 添加用户还是编辑用户的标志
		String flag = "edit";
		if ("".equals(model.getUeId()) || model.getUeId() == null) {
			flag = "add";
			model.setUeId(null);
		}
		this.registerManager.save(model);
		
		addActionMessage("保存成功");

		// 添加日志信息
		String ip = getRequest().getRemoteAddr();
		String logInfo = "";
		if (flag.equals("add")) {
			logInfo = "添加了账号" + model.getTuumsBaseUser().getUserName();
		} else {
			logInfo = "编辑了账号" + model.getTuumsBaseUser().getUserName();
		}

		this.myLogManager.addLog(logInfo, ip);

		return "winclose";
	}

	public TGzjzyhUserExtension getModel() {
		return model;
	}

	public void setUeId(String ueId) {
		this.ueId = ueId;
	}

	public MyLogManager getMyLogManager() {
		return myLogManager;
	}

	@Autowired
	public void setMyLogManager(MyLogManager myLogManager) {
		this.myLogManager = myLogManager;
	}

	public Page<TGzjzyhUserExtension> getPage() {
		return page;
	}

	public Map getUserActiveTypeMap() {
		return userActiveTypeMap;
	}

	public void setUserActiveTypeMap(Map userActiveTypeMap) {
		this.userActiveTypeMap = userActiveTypeMap;
	}
}