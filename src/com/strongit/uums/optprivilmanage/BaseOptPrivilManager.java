package com.strongit.uums.optprivilmanage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.PageContext;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.uums.security.UserInfo;
import com.strongit.oa.common.user.util.PrivilHelper;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 操作权限管理类.
 * 
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date 2009-11-9 上午08:51:42
 * @version 2.0.2.3
 * @classpath com.strongit.uums.optprivilmanage.BaseOptPrivilManager
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
@Transactional
public class BaseOptPrivilManager {

	@Autowired
	IUserService userService;// 注入资源管理服务对象

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 构造方法。
	 */
	public BaseOptPrivilManager() {
	}

	/**
	 * 根据权限系统编码得到权限信息
	 * 
	 * @author 喻斌
	 * @date Apr 6, 2009 1:00:52 PM
	 * @param privilSyscode
	 *            -权限系统编码
	 * @return ToaUumsBaseOperationPrivil 权限BO信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public ToaUumsBaseOperationPrivil getPrivilInfoByPrivilSyscode(
			String privilSyscode) throws DAOException, SystemException,
			ServiceException {
		if (!privilSyscode.startsWith("001-")) {
			privilSyscode = "001-" + privilSyscode;
		}
		TUumsBasePrivil privil = PrivilHelper.getPrivil(privilSyscode);
		return this.copy(privil);
	}

	/**
	 * 将资源权限转换成操作权限对象.
	 * 
	 * @author:邓志城
	 * @date:2010-6-18 上午11:26:28
	 * @param privil
	 *            资源权限对象
	 * @return 操作权限对象
	 */
	private ToaUumsBaseOperationPrivil copy(TUumsBasePrivil privil) {
		ToaUumsBaseOperationPrivil basePrivil = new ToaUumsBaseOperationPrivil();
		if (privil == null) {
			return basePrivil;
		}
		basePrivil.setPrivilAttribute(privil.getPrivilAttribute()); // 权限URL
		basePrivil.setPrivilDescription(privil.getPrivilDescription()); // 权限描述
		basePrivil.setPrivilId(privil.getPrivilId()); // 权限id
		basePrivil.setPrivilIsactive(privil.getPrivilIsactive()); // 权限是否启用
		basePrivil.setPrivilName(privil.getPrivilName()); // 权限名称
		basePrivil
				.setPrivilSequence(String.valueOf(privil.getPrivilSequence())); // 权限排序号
		basePrivil.setPrivilSyscode(privil.getPrivilSyscode()); // 权限编码
		basePrivil.setPrivilType(privil.getRest4()); // 权限类型
		basePrivil.setRest1(privil.getRest1());
		basePrivil.setRest2(privil.getRest2());
		basePrivil.setRest3(privil.getRest3());
		return basePrivil;
	}

	/**
	 * 得到当前用户权限列表
	 * 
	 * @author:邓志城
	 * @date:2010-6-19 下午07:46:59
	 * @param isActive
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<ToaUumsBaseOperationPrivil> getCurrentUserPrivilLst(
			boolean isActive) throws ServiceException, SystemException {
		List<String> userPrivil = this.getAllOptPrivilSysCode();
		List<ToaUumsBaseOperationPrivil> userPrivilLst = new ArrayList<ToaUumsBaseOperationPrivil>();
		for (Iterator<String> it = userPrivil.iterator(); it.hasNext();) {// 去掉禁用的权限
			ToaUumsBaseOperationPrivil privil = this.copy(PrivilHelper
					.getPrivil(it.next()));
			if ("1".equals(privil.getPrivilIsactive())) {
				userPrivilLst.add(privil);
			}
		}
		Collections.sort(userPrivilLst, new CompartorPrivil());
		return userPrivilLst;
	}

	/**
	 * 得到当前用户权限编码
	 * 
	 * @author:邓志城
	 * @date:2010-6-18 下午02:36:19
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<String> getAllOptPrivilSysCode() throws ServiceException,
			SystemException {
		UserInfo user = (UserInfo) this.getUserDetails();
		GrantedAuthority[] au = user.getAuthorities();
		List<String> codeList = new ArrayList<String>(0);
		for (GrantedAuthority authority : au) {
			codeList.add(authority.getAuthority());
		}
		return codeList;
	}

	/**
	 * 得到Spring安全框架中当前用户信息
	 * 
	 * @author:邓志城
	 * @date:2010-6-18 下午02:16:21
	 * @return
	 */
	UserDetails getUserDetails() {
		Authentication currentUser = SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetails user = (UserDetails) currentUser.getPrincipal();
		return user;
	}

	/**
	 * 获取当前用户拥有的一级菜单模块权限
	 * 
	 * @param String
	 *            userId 用户ID
	 * @date 2009年4月28日15:29:18
	 * @author 邓志城
	 */
	@SuppressWarnings("unchecked")
	public List<ToaUumsBaseOperationPrivil> getCurrentUserFirstMenuPrivil()
			throws ServiceException, SystemException, IOException {
		String privilRuleCode = PropertiesUtil
				.getCodeRule(GlobalBaseData.RULE_CODE_PRIVIL);// 编码规则
		int size = Integer.parseInt(privilRuleCode.substring(0, 1));// 编码规则第一个数字

		List<String> userPrivil = this.getAllOptPrivilSysCode();
		List<ToaUumsBaseOperationPrivil> userPrivilLst = new ArrayList<ToaUumsBaseOperationPrivil>();
		for (Iterator<String> it = userPrivil.iterator(); it.hasNext();) {// 去掉禁用的权限
			ToaUumsBaseOperationPrivil privil = this.copy(PrivilHelper
					.getPrivil(it.next()));
			if ("1".equals(privil.getPrivilIsactive())
					&& GlobalBaseData.PRIVIL_TYPE_MODULE.equals(privil
							.getPrivilType())
					&& privil.getPrivilSyscode().length() == size) {
				userPrivilLst.add(privil);
			}
		}
		// this.logger.info("----------------->size:"+size);
		Collections.sort(userPrivilLst, new CompartorPrivil());
		return userPrivilLst;
	}

	/**
	 * 获取当前用户二级菜单权限
	 * 
	 * @param String
	 *            userId 用户ID
	 * @param String
	 *            parentSysCode 二级菜单所属一级菜单系统编码
	 * @date 2009年4月28日15:29:18
	 * @author 邓志城
	 */
	@SuppressWarnings("unchecked")
	public List<ToaUumsBaseOperationPrivil> getCurrentUserSecondMenuPrivil(
			String parentSysCode) throws ServiceException, SystemException,
			IOException {
		List<ToaUumsBaseOperationPrivil> userPrivilLst = new ArrayList<ToaUumsBaseOperationPrivil>();
		List<String> userPrivil = this.getAllOptPrivilSysCode();
		for (Iterator<String> it = userPrivil.iterator(); it.hasNext();) {
			ToaUumsBaseOperationPrivil privil = this.copy(PrivilHelper
					.getPrivil(it.next()));
			if ("1".equals(privil.getPrivilIsactive())
					&& GlobalBaseData.PRIVIL_TYPE_MODULE.equals(privil
							.getPrivilType())
					&& privil.getPrivilSyscode().startsWith(parentSysCode)
					&& !privil.getPrivilSyscode().equals(parentSysCode)) {
				userPrivilLst.add(privil);
			}
		}
		Collections.sort(userPrivilLst, new CompartorPrivil());
		return userPrivilLst;

	}

	/**
	 *  列出所有的二级菜单
	 * @author strong_yangwg
	 * @param parentSysCode
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public List<ToaUumsBaseOperationPrivil> getCurrentUserAllSecondMenuPrivil(
			String parentSysCode) throws ServiceException, SystemException,
			IOException {
		List<ToaUumsBaseOperationPrivil> userPrivilLst = new ArrayList<ToaUumsBaseOperationPrivil>();
		List<String> userPrivil = this.getAllOptPrivilSysCode();
		for (Iterator<String> it = userPrivil.iterator(); it.hasNext();) {
			ToaUumsBaseOperationPrivil privil = this.copy(PrivilHelper
					.getPrivil(it.next()));
			if ("1".equals(privil.getPrivilIsactive())
					&& GlobalBaseData.PRIVIL_TYPE_MODULE.equals(privil
							.getPrivilType())
					&& privil.getPrivilSyscode().startsWith(parentSysCode)
					&& !privil.getPrivilSyscode().equals(parentSysCode) && privil.getPrivilSyscode().length() == 8) {
				if (privil.getPrivilAttribute().indexOf(".jsp") != -1
						|| privil.getPrivilAttribute().indexOf(".action") != -1) {
					// 只列出第二级
					if (privil.getPrivilSyscode().length() != 8) {
						continue;
					}
				}
				userPrivilLst.add(privil);
			}
		}
		
		//办公厅公文处理下的定制要求--显示没有权限的二级菜单
		if("0002".equals(parentSysCode)){
			userPrivilLst = AddBGTSecondMenuPrivil(userPrivilLst);
		}
		Collections.sort(userPrivilLst, new CompartorPrivil());
		return userPrivilLst;

	}
	
	/**
	 * 		办公厅公文处理下添加菜单，显示没有权限的二级菜单也单独添加,但不可用
	 * @param list
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws IOException
	 */
	public List<ToaUumsBaseOperationPrivil> AddBGTSecondMenuPrivil(
			List<ToaUumsBaseOperationPrivil> list2) throws ServiceException,
			SystemException, IOException {

		//00020009登记分发；00020020公文办理；00020019/00020021发文管理；00020003档案管理；
		//办公厅默认要求展现以上模块二级菜单，没有权限也显示不可用；
		//modify by luosy 2012-11-18
		HashMap <String, String> meunMap = new  HashMap<String, String>();
		ToaUumsBaseOperationPrivil m = new ToaUumsBaseOperationPrivil();
		if(list2 != null&&list2.size()>0){
			for (int i = 0; i < list2.size(); i++) {
				m = list2.get(i);
				if("00020009".equals(m.getPrivilSyscode())){
					meunMap.put("00020009", "00020009");
				}
				if("00020020".equals(m.getPrivilSyscode())){
					meunMap.put("00020020", "00020020");
				}
				if("00020019".equals(m.getPrivilSyscode())){
					meunMap.put("00020019", "00020019");
				}
				if("00020021".equals(m.getPrivilSyscode())){
					meunMap.put("00020021", "00020021");
				}
				if("00020003".equals(m.getPrivilSyscode())){
					meunMap.put("00020003", "00020003");
				}
			}
			
			if(meunMap.get("00020009")==null||"".equals(meunMap.get("00020009"))){
				ToaUumsBaseOperationPrivil privLess =  getPrivilInfoByPrivilSyscode("00020009");
				privLess.setRest4("false");
				list2.add(0, privLess);
			}
			if(meunMap.get("00020020")==null||"".equals(meunMap.get("00020020"))){
				ToaUumsBaseOperationPrivil privLess =  getPrivilInfoByPrivilSyscode("00020020");
				privLess.setRest4("false");
				list2.add(1, privLess);
			}
			if(meunMap.get("00020019")==null||"".equals(meunMap.get("00020019"))){
				ToaUumsBaseOperationPrivil privLess =  getPrivilInfoByPrivilSyscode("00020019");
				if(null!=privLess.getPrivilId()&&!"".equals(privLess.getPrivilId())){
					privLess.setRest4("false");
					list2.add(2, privLess);
				}
			}
			if(meunMap.get("00020021")==null||"".equals(meunMap.get("00020021"))){
				ToaUumsBaseOperationPrivil privLess =  getPrivilInfoByPrivilSyscode("00020021");
				if(null!=privLess.getPrivilId()&&!"".equals(privLess.getPrivilId())){
					privLess.setRest4("false");
					list2.add(2, privLess);
				}
			}
			if(meunMap.get("00020003")==null||"".equals(meunMap.get("00020003"))){
				ToaUumsBaseOperationPrivil privLess =  getPrivilInfoByPrivilSyscode("00020003");
				privLess.setRest4("false");
				list2.add(3, privLess);
			}
		}else{
			ToaUumsBaseOperationPrivil privLess =  getPrivilInfoByPrivilSyscode("00020009");
			privLess.setRest4("false");
			list2.add(privLess);
			privLess =  getPrivilInfoByPrivilSyscode("00020020");
			privLess.setRest4("false");
			list2.add(privLess);
			privLess =  getPrivilInfoByPrivilSyscode("00020021");
			privLess.setRest4("false");
			list2.add(privLess);
			privLess =  getPrivilInfoByPrivilSyscode("00020003");
			privLess.setRest4("false");
			list2.add(privLess);
		}
		//end			modify by luosy

		Collections.sort(list2, new CompartorPrivil());
		return list2;
	}
	
	/**
	* @description 获取当前用户三级菜单
	* @method getCurrentUserThirdMenuPrivil
	* @author 申仪玲(shenyl)
	* @created 2012-6-11 下午05:22:40
	* @param parentSysCode
	* @return
	* @throws ServiceException
	* @throws SystemException
	* @throws IOException
	* @return List<ToaUumsBaseOperationPrivil>
	* @throws Exception
	*/
	public List<ToaUumsBaseOperationPrivil> getCurrentUserThirdMenuPrivil(
			String parentSysCode) throws ServiceException, SystemException,
			IOException {
		List<ToaUumsBaseOperationPrivil> userPrivilLst = new ArrayList<ToaUumsBaseOperationPrivil>();
		List<String> userPrivil = this.getAllOptPrivilSysCode();
		for (Iterator<String> it = userPrivil.iterator(); it.hasNext();) {
			ToaUumsBaseOperationPrivil privil = this.copy(PrivilHelper
					.getPrivil(it.next()));
			if("0001".equals(privil.getPrivilSyscode())){
				System.out.println(privil.getPrivilSyscode());
			}
			if ("1".equals(privil.getPrivilIsactive())
					&& GlobalBaseData.PRIVIL_TYPE_MODULE.equals(privil
							.getPrivilType())
					&& privil.getPrivilSyscode().startsWith(parentSysCode)
					&& !privil.getPrivilSyscode().equals(parentSysCode) && privil.getPrivilSyscode().length() == 12) {
				if (privil.getPrivilAttribute().indexOf(".jsp") != -1
						|| privil.getPrivilAttribute().indexOf(".action") != -1) {
					// 只列出第三级
					if (privil.getPrivilSyscode().length() != 12) {
						continue;
					}
				}
				userPrivilLst.add(privil);
			}
		}
		Collections.sort(userPrivilLst, new CompartorPrivil());
		return userPrivilLst;

	}

	/**
	 * 权限按权限编码排序
	 * 
	 * @author 邓志城
	 * @company Strongit Ltd. (C) copyright
	 * @date 2009-9-8 下午05:50:55
	 * @version 2.0.2.3
	 * @classpath com.strongit.uums.optprivilmanage.CompartorPrivil
	 * @comment
	 * @email dengzc@strongit.com.cn
	 */
	private class CompartorPrivil implements
			Comparator<ToaUumsBaseOperationPrivil> {

		public int compare(ToaUumsBaseOperationPrivil o1,
				ToaUumsBaseOperationPrivil o2) {

			if (o1 != null && o2 != null) {
				try {
					Long key1 = Long.valueOf(o1.getPrivilSequence());
					Long key2 = Long.valueOf(o2.getPrivilSequence());
					return key1.compareTo(key2);
				} catch (Exception e) {
					LogPrintStackUtil.logException(e);
					return 0;
				}
			}
			return 0;
		}

	}

	/**
	 * 获取当前用户权限以及带链接的权限列表
	 * 
	 * @author 邓志城
	 * @date 2009年5月8日17:35:46
	 * @see 快捷菜单设置（彭小青）
	 */
	@SuppressWarnings("unchecked")
	public List<ToaUumsBaseOperationPrivil> getCurrentUserPrivilLstWithLink()
			throws ServiceException, SystemException, IOException {
		String privilRuleCode = PropertiesUtil
				.getCodeRule(GlobalBaseData.RULE_CODE_PRIVIL);// 编码规则
		List<ToaUumsBaseOperationPrivil> userPrivilLst = new ArrayList<ToaUumsBaseOperationPrivil>();
//		int size = Integer.parseInt(privilRuleCode.substring(0, 1));// 编码规则第一个数字  取一级菜单的位数
		int size = Integer.parseInt(privilRuleCode.substring(0, 1))+Integer.parseInt(privilRuleCode.substring(1, 2));// 编码规则第一个数字  取二级菜单的位数
		List<ToaUumsBaseOperationPrivil> lst = getCurrentUserPrivilLst(true);
		for (ToaUumsBaseOperationPrivil privil : lst) {
			String attr = privil.getPrivilAttribute();
			if (privil.getPrivilSyscode().length() == size 
					|| (attr != null && attr.startsWith("/") && (attr
							.indexOf(".action") != -1 || attr.indexOf(".jsp") != -1))) {
				
				if(!privil.getPrivilSyscode().startsWith("0005")){//去除系统管理的权限
					userPrivilLst.add(privil);
				}
			}
		}

		Collections.sort(userPrivilLst,
				new Comparator<ToaUumsBaseOperationPrivil>() {

					public int compare(ToaUumsBaseOperationPrivil o1,
							ToaUumsBaseOperationPrivil o2) {
						return o1.getPrivilSyscode().compareTo(
								o2.getPrivilSyscode());
					}

				});
		return userPrivilLst;
	}

	/**
	 * 根据编码查找用户是否有权限
	 * 
	 * @param String
	 *            sysCode 模块编码
	 * @date 2009年4月28日16:13:26
	 * @author 邓志城
	 * 
	 * 增加页面存储权限,提升性能
	 */
	@SuppressWarnings("unchecked")
	public boolean checkPrivilBySysCode(String sysCode)
			throws ServiceException, SystemException {
		PageContext page = ServletActionContext.getPageContext();
		List<String> userPrivil = null;
		if (page != null) {
			userPrivil = (List<String>) page.getAttribute("privilCodeList");
			if (userPrivil == null) {
				userPrivil = this.getAllOptPrivilSysCode();
				page.setAttribute("privilCodeList", userPrivil);
			}
		} else {
			userPrivil = this.getAllOptPrivilSysCode();
		}
		// List<String> userPrivil = this.getAllOptPrivilSysCode();
		for (String code : userPrivil) {
			if (code.equals(sysCode)) {
				return true;
			}
		}
		return false;
	}

}
