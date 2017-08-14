package com.strongit.oa.helpedit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.di.exception.DAOException;
import com.strongit.di.exception.ServiceException;
import com.strongit.di.exception.SystemException;
import com.strongit.oa.bo.ToaHelp;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.util.Const;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class HelpeditManager {
	private GenericDAOHibernate<ToaHelp, java.lang.String> helpDao;

	@Autowired
	private IUserService userService;

	public HelpeditManager() {
	}

	public String getHelpIdByHelpTreeId(String helpTreeId) throws DAOException,
			SystemException, ServiceException {
		final String hql = "from ToaHelp as help where help.helptreeId =?";
		@SuppressWarnings("unchecked")
		List<ToaHelp> list = helpDao.find(hql, helpTreeId);
		String hId = "";
		if (list.size() > 0) {
			ToaHelp help = list.get(0);
			hId = help.getHelpId();
		}
		return hId;
	}

	public ToaHelp getToaHelp(String helpId) {
		return helpDao.get(helpId);

	}

	public String getHelpDescByHelpId(String helpId) {
		if (helpId == null || helpId == "") {
			return "";
		}
		return helpDao.findById(helpId, true).getHelpDesc();
	}

	public void save(ToaHelp t) {
		helpDao.save(t);
	}

	public void update(ToaHelp t) {
		helpDao.update(t);
	}

	/**
	 * 根据 页面链接地址 得到helpId
	 * 
	 * @param url
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 * 通过url与Privil数据库中的值进行比较，查找到与当前页面对应的HelpTreeId。
	 */
	public String findbyPrivilsUrl(String url) throws DAOException,
			SystemException, ServiceException {
//		System.out.println(url);
		String userId = userService.getCurrentUser().getUserId();
		List<TUumsBasePrivil> userPrivilsSet = userService.getUserPrivilsByUserId(
				userId, Const.IS_YES);
		String privId = "";
		for (TUumsBasePrivil priv : userPrivilsSet) {
			String privAttr = priv.getPrivilAttribute();
//			System.out.println(privAttr);
			privAttr = privAttr.replaceAll("&", "and");
			if (url.equals(privAttr)) {
				privId = priv.getPrivilSyscode();
				break;
			}
		}
//		return getHelpIdByHelpTreeId(privId);
		return privId;
	}

	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		helpDao = new GenericDAOHibernate<ToaHelp, java.lang.String>(session,
				ToaHelp.class);
	}

//	public boolean checkUserIsManager(UserInfo currentUser) {
//		return userManager.checkUserIsManager(currentUser.getUserId());
//	}

	public User getCurrentUserInfo() {
		return userService.getCurrentUser();
	}

	@SuppressWarnings("rawtypes")
	public List getPrivilInfosByUserLoginName() {
		return userService.getPrivilInfosByUserLoginName(getCurrentUserInfo()
				.getUserLoginname(), "2", "2", "2");
	}

	@SuppressWarnings("rawtypes")
	public List getCurrentUserSystems(String constIsyes) {
		return userService.getCurrentUserSystems(Const.IS_YES);
	}
	
	public TUumsBasePrivil getPrivilInfoByPrivilId(String TID){
		TUumsBasePrivil tbp = userService.getPrivilInfoByPrivilSyscode(TID);
		return tbp;
	}
}
