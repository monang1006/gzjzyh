package com.strongit.oa.address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.address.util.ComparatorOrgUser;
import com.strongit.oa.address.util.UserBean;
import com.strongit.oa.bo.ToaAddress;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.model.UserGroup;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.TempPo;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.organisemanage.IOrgManager;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      组织机构Manager
 */
@Service
@Transactional
public class AddressOrgManager {

	private IUserService userService;
	private AddressManager addressManager;
	//个人信息
	private MyInfoManager  myInfoManager;
	
	private GenericDAOHibernate<TUumsBaseOrg,String> orgDAO;
	private GenericDAOHibernate<TUumsBaseUser,String> userDAO;
	@Autowired
	private IOrgManager orgMangger;
	@Autowired IDictService dictService;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		orgDAO = new GenericDAOHibernate<TUumsBaseOrg,String>(sessionFactory, TUumsBaseOrg.class);
		userDAO = new GenericDAOHibernate<TUumsBaseUser,String>(sessionFactory, TUumsBaseUser.class);
	}

	/**
	 * 获取当前用户
	 */
	@Transactional(readOnly=true)
	public User getCurrentUser()throws SystemException{
		return userService.getCurrentUser();
	}
	
	/**
	 * 获取指定当前用户的组织机构信息
	 * @param userId 用户ID
	 * @return 
	 */
	@Transactional(readOnly=true)
	public Organization getUserDepartmentByUserId() throws SystemException{
		User user = this.getCurrentUser();
		Assert.notNull(user, "当前用户不能为空！");
		String userId = user.getUserId();
		Organization org = userService.getUserDepartmentByUserId(userId);
		Assert.notNull(org, "当前用户所属机构不存在或已被删除！");
		return org;
	}
	
	/**
	 * 获取所有部门以及部门下的部门
	 * @return List
	 */
	@Transactional(readOnly=true) 
	public List<Organization> getAllDeparments() throws SystemException,ServiceException{
		try {
			List<Organization> list = userService.getAllDeparments();
			return list;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"部门列表"});
		}
	}

	/**
	 * 根据部门ID获取用户列表
	 * @param orgId
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<User> getUsersByOrgID(String orgId) throws SystemException,ServiceException{
		try {
			List<User> lstUser = userService.getUsersByOrgID(orgId);
			Collections.sort(lstUser, new ComparatorOrgUser());
			return lstUser;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"部门下所有用户列表"});
		}
	}

	/**
	 * 得到某机构下的用户分页列表
	 * @author:邓志城
	 * @date:2009-9-9 下午12:30:51
	 * @modify:刘皙
	 * @date：2012-6-12上午10:00:35
	 * @param page
	 * @param orgId
	 * @param userName
	 * @return
	 */
	public Page<TUumsBaseUser> getUsersByOrgId(Page<TUumsBaseUser> page,String orgId,String userName,String parentId){
//		try{
//			page = userManager.queryOrgUsersByHa(page, orgId, userName, null, "0", "1", userManager.getCurrentUserInfo().getOrgId());
//			page = userManager.queryOrgUsers(page, orgId,userName,null,"0","1");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		try{
			List<Object> list=new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			if((orgId!=null&&!("").equals(orgId))){
				   if("".equals(parentId)||parentId==null){
						hql.append("select t from TUumsBaseUser t,TUumsBaseOrg o where 1  = '1'");
					}else{
						hql.append("select t  from TUumsBaseUser t,TUumsBaseOrg o where t.orgId  = '" + orgId +"'");
						
					}
					
			}else{
				hql.append("select t from TUumsBaseUser t,TUumsBaseOrg o where 1  = '1'");
				
			}
			if(userName!=null&&!(userName).equals("")){
				//hql.append("select t from TUumsBaseUser t,TUumsBaseOrg o where t.userName like ? ");
				//list.add("%"+userName+"%");
				hql.append(" and ((t.userName like ? ");
				list.add("%"+userName+"%");
				hql.append(" or t.userLoginname like ?))");
				list.add("%"+userName+"%");
				}
			hql.append(" and t.orgId=o.orgId and t.userIsdel<>1 and t.userIsactive<>0 order by o.orgSequence,t.userSequence");
			Object[] objs = new Object[list.size()];
			for (int i = 0; i < list.size(); i++) {
				objs[i] = list.get(i);
			}
			page=userDAO.find(page, hql.toString(), objs);
		}catch(Exception e){
			e.printStackTrace();
		}
		return page;
	}
	/**移动webservice调用方法
	 * 得到某机构下的用户分页列表
	 * @author:邓志城
	 * @date:2009-9-9 下午12:30:51
	 * @modify:刘皙
	 * @date：2012-6-12上午10:00:35
	 * @param page
	 * @param orgId
	 * @param userName
	 * @return
	 */
	public Page<TUumsBaseUser> getUsersByOrgId(Page<TUumsBaseUser> page, String orgId, String userName, String userTel, String userEmail) throws Exception{
		try{
			//page = userManager.queryOrgUsers(page, orgId,userName,null,"0","1");
			String orgName = null;
			if(!("").equals(orgId)&&orgId!=null){
				System.out.println("orgId:"+orgId);
				System.out.println("orgMangger:"+orgMangger);
				TUumsBaseOrg orginfolist = orgMangger.getOrgInfoByOrgId(orgId);
				orgName = orginfolist.getOrgName();
			}
			//page = userManager.queryUsersByHa(page, userName, userLoginName, orgName, "0", "1", userManager.getUserInfoByLoginname("admin").getOrgId());
			

			List<Object> list=new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			if(orgId!=null&&!"".equals(orgId)){
				hql.append("select t from TUumsBaseUser t, TUumsBaseOrg o where t.orgId = o.orgId and t.userIsdel = '0' ");
//				hql.append(" and t.userIsSupManager is null and t.userIsactive ='1' and t.orgIds  like '%" + orgId +"%' ");
				hql.append(" and t.userIsactive ='1' and t.orgIds  like '%" + orgId +"%' ");
				if((userName!=null&&!(userName).equals(""))||(userTel!=null&&!(userTel).equals(""))||(userEmail!=null&&!(userEmail).equals(""))){
					hql.append(" and (t.userName like ? )");
					list.add("%"+userName+"%");
					//hql.append(" or t.userLoginname like ?)");
					//list.add("%"+userName+"%");
					if(userTel!=null&&!(userTel).equals("")){
						hql.append(" and (t.userTel like ?");
						list.add("%"+userTel+"%");
						hql.append(" or t.rest2 like ?)");
						list.add("%"+userTel+"%");
					}
					if(userEmail!=null&&!(userEmail).equals("")){
						hql.append(" and t.userEmail like ? ");
						list.add("%"+userEmail+"%");
					}
				}
			}else{
				if((userName!=null&&!(userName).equals(""))||(userTel!=null&&!(userTel).equals(""))||(userEmail!=null&&!(userEmail).equals(""))){
					hql.append("select t from TUumsBaseUser t, TUumsBaseOrg o");
					hql.append(" where t.orgId = o.orgId and t.userIsSupManager is null and t.userIsdel = '0' and t.userIsactive ='1' and (t.userName like ? )");
					list.add("%"+userName+"%");
					//hql.append(" or t.userLoginname like ?)");
					//list.add("%"+userName+"%");
					if(userTel!=null&&!(userTel).equals("")){
						hql.append(" and (t.userTel like ?");
						list.add("%"+userTel+"%");
						hql.append(" or t.rest2 like ?)");
						list.add("%"+userTel+"%");
					}
					if(userEmail!=null&&!(userEmail).equals("")){
						hql.append(" and t.userEmail like ? ");
						list.add("%"+userEmail+"%");
					}
				}else{
					hql.append("select t from TUumsBaseUser t, TUumsBaseOrg o ");
					hql.append(" where t.orgId = o.orgId and t.userIsdel = '0' and t.userIsactive ='1' and t.userIsSupManager is null ");
				}
			}
//			hql.append(" and t.userLoginname <> 'wuyx' order by o.orgSyscode, t.userSyscode");
			hql.append(" order by o.orgSequence,t.userSequence");
			Object[] objs = new Object[list.size()];
			for (int i = 0; i < list.size(); i++) {
				objs[i] = list.get(i);
			}
			page=userDAO.find(page, hql.toString(),objs);
		}catch(Exception e){
			e.printStackTrace();
		}
		return page;
	}
	/**
	 * author:dengzc
	 * description:查询通讯录
	 * modifyer:
	 * description:
	 * @param userName 用户姓名
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws AjaxException
	 */
	public List<User> getAllUsers(String userName,String searchType)throws SystemException,ServiceException{
		try{
			List<User> lstAllUser = new ArrayList<User>();
			if("public".equals(searchType)){//查询方式，public表示在公共通讯录中搜索。
				List<Organization> lstOrg = getAllDeparments();
				//获取系统通讯录中人员
				for(int i=0;i<lstOrg.size();i++){
					Organization org = lstOrg.get(i);
					List<User> lstUser = getUsersByOrgID(org.getOrgId());
					for(int j=0;j<lstUser.size();j++){
						User user = lstUser.get(j);
						String thisUserName = user.getUserName();
						if(thisUserName.toLowerCase().indexOf(userName)!=-1){
							lstAllUser.add(user);
						}
					}
				}				
			}else if("personal".equals(searchType)){//表示在个人通讯录中搜索
				List<ToaAddress> lstAddress = addressManager.getAllAdress();
				String addrName = null;
				//获取个人通讯录中的人员信息
				for(int k=0;k<lstAddress.size();k++){
					ToaAddress address = lstAddress.get(k);
					addrName = address.getName();
					if(null!=addrName){
						if(address.getName().toLowerCase().indexOf(userName)!=-1){
							User addressUser = new User();
							addressUser.setUserEmail(address.getAllEmail());//邮件
							addressUser.setUserName(address.getName());//姓名
							addressUser.setUserTel(address.getAllPhone());		
							addressUser.setUserId(address.getAddrId());
							lstAllUser.add(addressUser);
						}
					}
				}
				
			}else{
				throw new ServiceException(MessagesConst.find_error,new Object[]{"通讯录类型"});
			}
			return lstAllUser;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[]{"用户"});
		}
	}

	/**
	 * author:dengzc
	 * description:系统通讯录中查看用户信息
	 * modifyer:
	 * description:
	 * @param UserBean bean 用户属性bean 
	 * @param String userId 用户ID
	 * @return bean
	 * @throws SystemException
	 */
	public UserBean getUserInfo(String userId) throws SystemException{
		User user = userService.getUserInfoByUserId(userId);
		ToaPersonalInfo info = myInfoManager.getInfoByUserid(userId);
		if(info == null){
			info = new ToaPersonalInfo();
		}
		UserBean bean = new UserBean();
		bean.setUserLoginName(user.getUserLoginname());
		bean.setUserName(user.getUserName());
		String sex = info.getPrsnSex();//得到性别，数据库中存储的是0和1；其中0表示男，1表示女
		if("1".equals(sex)){
			bean.setSex("女");
		}else{
			bean.setSex("男");
		}
		Organization org = userService.getDepartmentByLoginName(bean.getUserLoginName());
		if(org == null){
			throw new SystemException("用户所属部门不存在！");
		}
		bean.setDeptName(getAllFatherOrgName(org)); 
		String email = null;
//		if(info.getPrsnMail1() == null){
//			if(info.getPrsnMail2() != null){
//				email = info.getPrsnMail2();
//			}
//		}else{
//			if(info.getPrsnMail2() == null){
//				email = info.getPrsnMail1();
//			}else{
//				if(info.getPrsnMail1().equals(info.getPrsnMail2())) {
//					email = info.getPrsnMail1();
//				} else {
//					email = info.getPrsnMail1()+","+info.getPrsnMail2();					
//				}
//			}
//		}
		if(user.getUserEmail() != null){			//qibh 0708修改，为保证系统通讯录显示的联系方式跟查看时一直，都去TUUMSBASEUSER里面的信息
				email = user.getUserEmail();
		}
		String mobile = null;
//		if(info.getPrsnMobile1() == null){
//			if(info.getPrsnMobile2() != null){
//				mobile = info.getPrsnMobile2();
//			}
//		}else{
//			if(info.getPrsnMobile2() == null){
//				mobile = info.getPrsnMobile1();
//			}else{
//				if(info.getPrsnMobile1().equals(info.getPrsnMobile2())) {
//					mobile = info.getPrsnMobile1();
//				} else {
//					mobile = info.getPrsnMobile1()+","+info.getPrsnMobile2();
//				}
//			}
//		}
		if(user.getRest2() != null){
				mobile = user.getRest2();
		}
		String tel = null;
		if(user.getUserTel() != null){
				tel = user.getUserTel();
		}
		bean.setEmail(email);
		bean.setMobile(mobile);
		bean.setTel(tel);
		bean.setAddress(info.getHomeAddress());
		return bean;
	}
	
	/**
	 * author:dengzc
	 * description:获取组织机构对象
	 * modifyer:
	 * description:
	 * @param id
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Organization getOrgById(String id)throws SystemException,ServiceException{
		try{
			return userService.getDepartmentByOrgId(id);
		}catch(Exception e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"部门"});
		}
	}

	/**
	 * 根据用户ID获取用户信息
	 * @param userId 用户ID
	 * @return com.strongit.oa.common.user.User 内部用户对象
	 */
	public User getUserInfoByUserId(String userId) throws SystemException{
		return userService.getUserInfoByUserId(userId);
	}

	/**
	 * author:dengzc
	 * description:获取当前组织机构的所有父组织机构
	 * modifyer:
	 * description:
	 * @param org
	 * @return
	 * @throws SystemException
	 */
	private List<Organization> getAllFatherOrg(List<Organization> lstOrg, Organization org) throws SystemException{
		if(lstOrg == null){
			lstOrg = new ArrayList<Organization>();
			lstOrg.add(org);
		}
		Organization fatherOrg = userService.getParentDepartmentByOrgId(org.getOrgId());
		if(fatherOrg.getOrgId().equals(org.getOrgId())){//说明是根节点
			return lstOrg;
		}else{
			lstOrg.add(fatherOrg);
			getAllFatherOrg(lstOrg, fatherOrg);
		}
		return lstOrg;
	}

	/**
	 * author:dengzc
	 * description:
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 */
	private String getAllFatherOrgName(Organization org)throws SystemException{
		List<Organization> lstOrg = getAllFatherOrg(null, org);
		StringBuffer orgSb = new StringBuffer("");
		Collections.sort(lstOrg,new CompatorOrg());
		for(Organization organization:lstOrg){
			orgSb.append(organization.getOrgName()).append("->");
		}
		if(orgSb.length()>0){
			return orgSb.substring(0, orgSb.lastIndexOf("->"));
		}
		return orgSb.toString();
	}

	/**
	* 比较算法	
	 * @author Administrator
	 *
	 */
	public class CompatorOrg implements Comparator<Object>{

		public int compare(Object o1, Object o2) {
			Organization org1 = (Organization)o1;
			Organization org2 = (Organization)o2;
			if(org1.getOrgSyscode()!=null && org2.getOrgSyscode()!=null){
				return Long.valueOf(org1.getOrgSyscode().length()).compareTo(Long.valueOf(org2.getOrgSyscode().length())); 				
			}
			return 0;
		}
		
	}
	
	/**
	 * author:dengzc
	 * description:获取父级节点
	 * modifyer:
	 * description:
	 * @param code
	 * @param ruleCode
	 * @param defaultParent
	 * @return
	 */
	@Transactional(readOnly=true)
	public String findFatherCode(String code,String ruleCode,String defaultParent) throws SystemException,ServiceException{
		int length1 = 0;
		int length2 = 0;
		int codeLength = code.length();
		String fatherCode = "0";
		if(defaultParent != null && !"".equals(defaultParent)){
			fatherCode = defaultParent;
		}
		for (int i = 0; i < ruleCode.length(); i++) {
			length1 = length1 + Integer.parseInt(ruleCode.substring(i, i+1));
			if (i > 0) {
				length2 = length2 + Integer.parseInt(ruleCode.substring(i-1, i));
			}
			if (codeLength == length1) {
				if (length2 != 0) {
					fatherCode = code.substring(0, length2);
					break;
				}
			}
		}
		return fatherCode;
	}

	/**
	 * author:dengzc
	 * description:组织机构下部门
	 * modifyer:
	 * description:
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public List<Organization> childOrg(String orgId,List<Organization> lst,String config_orgCode)throws Exception{
		Organization org = getOrgById(orgId);//需要获取此部门下的所有子部门
		List<Organization> childOrgLst = new ArrayList<Organization>();
		String orgSysCode = org.getOrgSyscode();
		for(int i=0;i<lst.size();i++){
			Organization thisOrg = lst.get(i);
			String fatherCode = findFatherCode(thisOrg.getOrgSyscode(), config_orgCode, null);
			if(orgSysCode.equals(fatherCode)){//如果当前部门是他的子部门
				childOrgLst.add(thisOrg);
			}
		}
		return childOrgLst;
	}
	
	/**
	 * author:dengzc
	 * description:
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws IOException
	 */
	public List<TempPo> getOrgUserList() throws SystemException,ServiceException,IOException{
		try{
			List<Organization> lst = getAllDeparments();
			List<TempPo> newList = new ArrayList<TempPo>();
			String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
			for(Iterator<Organization> it=lst.iterator();it.hasNext();){
				TempPo po = new TempPo();
				Organization org = it.next();
				String orgSysCode = org.getOrgSyscode();
				po.setId(orgSysCode);
				po.setCodeId(org.getOrgId());
				po.setParentId(findFatherCode(orgSysCode, config_orgCode, null));
				po.setName(org.getOrgName());
				po.setType("org");
				newList.add(po);
				
				
				//根据机构获取用户列表，在树展现时人员挂接在组织机构下
				List<User> userList = getUsersByOrgID(org.getOrgId());
				for(int i=0;i<userList.size();i++){
					User user = userList.get(i);
					TempPo userPo = new TempPo();
					userPo.setId(user.getUserId());
					userPo.setCodeId(user.getUserId());
					userPo.setParentId(org.getOrgSyscode());
					userPo.setName(user.getUserName());
					userPo.setType("user");
					newList.add(userPo);
				}
			}
			return newList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.operation_error,new Object[]{"获取系统用户和组织机构"});
		}
	}

	/**
	 * 更新组织机构,用于在组织机构中插入一个RtxId
	 * @param loginName
	 * @return
	 */
	public TUumsBaseOrg updateOrganization(String orgId)throws SystemException{
		//String[] rtx = new String[2];
		TUumsBaseOrg org = orgDAO.get(orgId);
		TUumsBaseOrg parentOrg = getParentOrgByOrgId(orgId);//获取当前机构的上级机构
		if("".equals(parentOrg.getRest2()) || null == parentOrg.getRest2()){//判断此机构是否有RtxID
			//获取组织机构下的最小RtxId
			String minOrgRtxId = minOrgRtxId(null);
			parentOrg.setRest2(minOrgRtxId);//对应RTX中部门呢ID
			orgDAO.update(parentOrg);	
		}
		if("".equals(org.getRest2()) || null == org.getRest2()){//判断此机构是否有RtxID
			//获取组织机构下的最小RtxId
			String minOrgRtxId = minOrgRtxId(null);
			org.setRest2(minOrgRtxId);//对应RTX中部门呢ID
			org.setRest3(parentOrg.getRest2());//对应RTX中父部门ID
			org.setRest4(org.getRest3());
			orgDAO.update(org);			
		}else{
			if("".equals(org.getRest3()) || null == org.getRest3()){
				org.setRest3(parentOrg.getRest2());//对应RTX中父部门ID
				org.setRest4(org.getRest3());
				orgDAO.update(org);
			}
		}
		if(org.getOrgId().equals(parentOrg.getOrgId())){
			org.setRest3("0");
			org.setRest4("0");
			orgDAO.update(org);
		}
	    return org;
	}

	/**
	 * 根据组织机构Id获取组织机构信息
	 * @author  喻斌
	 * @date    Jan 21, 2009  10:25:29 AM
	 * @param orgId -组织机构Id
	 * @return TUumsBaseOrg 组织机构信息
	 */
	public TUumsBaseOrg getOrgInfoByOrgId(String orgId) throws SystemException{
		return userService.getOrgInfoByOrgId(orgId);
	}

	/**
	 * 根据组织机构id获取上级组织机构信息
	 * 
	 * @author 喻斌
	 * @date Jan 8, 2009 11:04:14 AM
	 * @param orgId
	 *            组织机构id
	 * @return TUumsBaseOrg 上级组织机构信息
	 */
	public TUumsBaseOrg getParentOrgByOrgId(String orgId) throws SystemException{
		return userService.getParentOrgByOrgId(orgId);
	}
	
	/**
	 * 获取在组织机构中最小的RtxId。每次更新组织机构时,在最小的组织机构上加1
	 * @param loginName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String minOrgRtxId(String orgId)throws SystemException{
		StringBuffer hql = new StringBuffer("select max(t.rest2)  from TUumsBaseOrg t ");
		List<String> lst = null;
		String rtxId = "100";//从100开始累加
		if(orgId!=null && !"".equals(orgId)){
			hql.append("where t.orgId=?");
			lst = orgDAO.find(hql.toString(), new Object[]{orgId});
		}else{
			lst = orgDAO.find(hql.toString(), new Object[0]);
		}
		if(lst!=null && lst.size()>0){//找到记录
			rtxId = lst.get(0);
			if(!"".equals(rtxId) && null!=rtxId){//记录不为空
				rtxId = String.valueOf(Integer.parseInt(rtxId)+1);
			}else{//记录为空
				rtxId = "100";
			}
		}else{
			rtxId = "100";
		}
		if(Integer.parseInt(rtxId) >= 999){
			throw new SystemException("RTX中的部门Id不能大于或等于999!");
		}
		return rtxId;
	}

	/**
	 * author:dengzc
	 * description:获取用户
	 * modifyer:
	 * description:
	 * @param userId
	 * @return
	 * @throws SystemException
	 */
	public TUumsBaseUser updateUser(String userId) throws SystemException{

		TUumsBaseUser user = (TUumsBaseUser)orgDAO.getSession().get(TUumsBaseUser.class, userId);
		if(user.getRest2() == null || "".equals(user.getRest2())){
			user.setRest2(minUserRtxId(userId));
			orgDAO.getSession().update(user);
		}
		return user;
	}
	
	/**
	 * 获取在用户中最小的RtxId。每次更新组织机构时,在最小的用户上加1
	 * @param loginName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String minUserRtxId(String userId)throws SystemException{
		StringBuffer hql = new StringBuffer("select min(t.rest2)  from TUumsBaseUser t ");
		List<String> lst = null;
		String rtxId = "0";//RTXID，从0开始计算
		lst = orgDAO.find(hql.toString(), new Object[]{userId});
		if(lst!=null && lst.size()>0){//找到记录
			rtxId = lst.get(0);
			if(!"".equals(rtxId)){//记录不为空
				rtxId = String.valueOf(Integer.parseInt(rtxId)+1);
			}else{//记录为空
				rtxId = "1";
			}
		}
		if(Integer.parseInt(rtxId) >= 9999){
			throw new SystemException("RTX中的用户Id不能大于或等于9999!");
		}
		return rtxId;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setAddressManager(AddressManager addressManager) {
		this.addressManager = addressManager;
	}
	
	/**
	 * author:dengzc
	 * description:按部门选择人员
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public List<TempPo> getUserTreeByDept()throws Exception{
		List<Organization> lst = getAllDeparments();//获取所有机构
		List<TempPo> newList = new ArrayList<TempPo>();//构造树节点
		Map<String,List<User>> userMap = userService.getUserMap();//得到用户集合	
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		for(Iterator<Organization> it=lst.iterator();it.hasNext();){
			TempPo po = new TempPo();
			po.setType("org");
			Organization org = it.next();
			String orgSysCode = org.getOrgSyscode();
			String fatherCode = findFatherCode(orgSysCode, config_orgCode, null);
			po.setId(orgSysCode);
			po.setParentId(fatherCode);
			po.setName(org.getOrgName());
			newList.add(po);
			
			
			//根据机构获取用户列表，在树展现时人员挂接在组织机构下
			List<User> userList = userMap.get(org.getOrgId());//得到机构下的人员列表
			if(userList != null && !userList.isEmpty()){
				for(int i=0;i<userList.size();i++){
					User user = userList.get(i);
					TempPo userPo = new TempPo();
					userPo.setId("u"+user.getUserId()+"@"+user.getUserName()+"@");
					userPo.setParentId(po.getId());
					userPo.setName(user.getUserName());
					userPo.setType("user");
					newList.add(userPo);
				}				
			}
		}
		return newList;
	}

	/***
	 * author:dengzc
	 * description:根据岗位展示人员树
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public List<TempPo> getUserTreeByPost() throws Exception{
		List<Organization> lst = getAllDeparments();//得到所有机构
		List<TempPo> newList = new ArrayList<TempPo>();//构造树节点
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		Map<String,List<String[]>> postMap = userService.getPostMap();
		for(Iterator<Organization> it=lst.iterator();it.hasNext();){
			TempPo po = new TempPo();
			po.setType("org");
			Organization org = it.next();
			String orgSysCode = org.getOrgSyscode();
			String fatherCode = findFatherCode(orgSysCode, config_orgCode, null);
		//	if("0".equals(fatherCode)){
				po.setId(orgSysCode);
		//	}else{
		//		po.setId(po.getType()+orgSysCode);
		//	}
			po.setParentId(fatherCode);
			po.setName(org.getOrgName());
			newList.add(po);

			List<String[]> postList = postMap.get(org.getOrgId());//岗位挂接在组织机构下 
			if(postList != null && !postList.isEmpty()){
				for(String[] postInfo:postList){
					TempPo poPost = new TempPo();
					poPost.setId(po.getId()+"<-post->"+postInfo[0]);
					poPost.setParentId(po.getId());
					poPost.setName(postInfo[1]);
					poPost.setType("post");
					newList.add(poPost);
					List<User> lstPostUser = userService.getUsersByPositionId(postInfo[0]);
					for(User postUser:lstPostUser){//人员挂接在岗位下
						TempPo userPo = new TempPo();
						userPo.setId(poPost.getId()+"<-user->"+postUser.getUserId());
						userPo.setParentId(poPost.getId());
						userPo.setName(postUser.getUserName());
						userPo.setType("user");
						newList.add(userPo);
					}
				}				
			}
		}
		return newList;
	}

	/**
	 * 根据用户组选择人员
	 * @author:邓志城
	 * @date:2009-5-25 下午02:12:01
	 * @return
	 * @throws SystemException
	 * @see {选择人员}
	 */
	public List<TempPo> getUserTreeByUserGroup()throws SystemException{
		//获取所有用户组
		List<UserGroup> userGroupLst = userService.getAllGroupInfo();
		List<TempPo> tree = new ArrayList<TempPo>();
		for(UserGroup userGroup:userGroupLst){
			TempPo groupPo = new TempPo();
			groupPo.setId(userGroup.getGroupId());
			groupPo.setName(userGroup.getGroupName());
			groupPo.setParentId("0");
			tree.add(groupPo);
			List<User> userLst = userService.getUsersInfoByGroupId(userGroup.getGroupId());
			for(User user:userLst){
				TempPo userPo = new TempPo();
				userPo.setId(groupPo.getId()+"<-user->"+user.getUserId());
				userPo.setName(user.getUserName());
				userPo.setParentId(groupPo.getId());//用户挂接在用户组下
				userPo.setType("user");
				tree.add(userPo);
			}
		}
		return tree;
	}
	
	/**
     * @desc: 根据userid获取用户个人信息
     * @return 获取个人信息
    */
	@Transactional(readOnly=true)
    public ToaPersonalInfo getInfoByUserid(String userid)throws SystemException{
	    return myInfoManager.getInfoByUserid(userid);
    }

	/**
     * 获取个人信息表信息,存储在HASHMAP中
     * @return
     * @throws ServiceException
    */
    @SuppressWarnings("unchecked")
    public Map getPersonalInfo() throws ServiceException {
    	return myInfoManager.getPersonalInfo();
    }
	
	/**
	 * 通过数据字典得到自定义的组织机构（大集中项目移植）
	 * @author:邓志城
	 * @date:2009-12-24 下午08:25:33
	 * @return
	 * @throws SystemException
	 */
	public List<TempPo> getOrgListFromDict(String type)throws SystemException {
		List<TempPo> newList = new ArrayList<TempPo>();//构造树节点
		if(type == null || "".equals(type) || "undefined".equals(type)){
			return newList;
		}
		List<ToaSysmanageDictitem> list = dictService.getItemsByDictValue(type);
		String rootName = dictService.getDictNameByValue(type);
		if(rootName == null || "".equals(rootName)){
			return newList;
		}
		TempPo root = new TempPo();//构造根节点
		root.setId(GlobalBaseData.GXSYGJ);
		root.setParentId("root");
		root.setName(rootName);
		root.setType("false");//这种类型节点不可编辑
		newList.add(root);
		
		if(list != null && !list.isEmpty()){
			for(ToaSysmanageDictitem item : list){
				TempPo po = new TempPo();
				po.setId(item.getDictItemCode());
				String parent = item.getDictItemParentvalue();
				if(parent == null){
					parent = "0";//root.getId();//挂在根节点下
					//po.setType("false");
				}
				po.setParentId(parent);
				po.setName(item.getDictItemName());
				if(item.getDictItemIsSelect() == 0){//可选
					po.setType("true");
				}else {
					po.setType("false");
				}
				newList.add(po);
			}
		}
		return newList;
	}
	
	/**
	 * 选择人员全选功能。
	 * @author:郑志斌
	 * @date:2010-1-15 上午10:38:13
	 * @param type 全选类型，“1”：常用联系人；其他：系统通讯录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getAllUserInfoForWorkflow(String type,String orgId){
		JSONArray userArray = new JSONArray();//组装用户姓名和用户id字符串
		if(type != null && "1".equals(type)){//查询公文通讯录
			List<ToaAddress> list=null;
			if(orgId.length()<3){
				list=addressManager.getUserAddressByGroup();//得到个人通信录下所有组的用户列表
			}
			else{
				
				list = addressManager.getAddressByGroupId(orgId);	//得到当前用户公文通讯录用户列表  orgId为“groupId”
			}
			if(list != null && !list.isEmpty()){
				for(ToaAddress address : list){
					JSONObject objUser = new JSONObject();
					objUser.put("userId", address.getAddrId());//ToaAddress中addrId字段存的是主键
					objUser.put("userName", address.getName());
					userArray.add(objUser);
				}
			}
		}else{
			List<TUumsBaseUser> l =null;   //userService.getOrgUserListForWorkflow(orgId, null);
			if(!l.isEmpty()){
				for(TUumsBaseUser user : l){
					JSONObject objUser = new JSONObject();
					objUser.put("userId", user.getUserId());
					objUser.put("userName", user.getUserName());
					userArray.add(objUser);
				}
			}
		}
		return userArray.toString();
	}
 
	/**
	 * 构造当前用户所在部门人员树形
	 * @author:邓志城
	 * @date:2010-3-30 下午07:02:16
	 * @return
	 */
	public List<TempPo> CurrentUserDeptTree() throws Exception {
		//Map<String,List<User>> userMap = userService.getUserMap();//得到用户集合	
		List<Organization> orgList = userService.getAllDeparments();
		User currentUser = userService.getCurrentUser();
		Organization organization = userService.getDepartmentByOrgId(currentUser.getOrgId());//得到当前用户所在机构
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		//List<Organization> parentOrgList = new LinkedList<Organization>();//记录当前机构所有父级机构
		List<TempPo> newList = new ArrayList<TempPo>();//构造树节点
		String orgSyscode = organization.getOrgSyscode();
		if(orgList != null && !orgList.isEmpty()) {
			for(Organization org : orgList) {
				if(orgSyscode.startsWith(org.getOrgSyscode())) {
					TempPo orgPo = new TempPo();
					orgPo.setId(org.getOrgSyscode());
					orgPo.setName(org.getOrgName());
					String fatherCode = userService.findFatherCode(org.getOrgSyscode(), config_orgCode, null);
					orgPo.setParentId(fatherCode);
					newList.add(orgPo);
				}
			}
		}
		List<TUumsBaseUser> userList = userService.getUserListByOrgId(currentUser.getOrgId());//userMap.get(currentUser.getOrgId());
		if(userList != null && !userList.isEmpty()){
			for(TUumsBaseUser user : userList){
				TempPo userPo = new TempPo();
				userPo.setId(user.getUserId());
				userPo.setParentId(orgSyscode);
				userPo.setName(user.getUserName());
				userPo.setType("user");
				userPo.setCodeId(user.getUserId());
				newList.add(userPo);
			}
		}
		return newList;
	}

	public List<TempPo> CurrentUserOrgTree() throws Exception {
		Map<String,List<User>> userMap = userService.getUserMap();//得到用户集合	
		List<Organization> orgList = userService.getAllDeparments();
		User currentUser = userService.getCurrentUser();
		Organization organization = userService.getDepartmentByOrgId(currentUser.getOrgId());//得到当前用户所在机构
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		List<Organization> parentOrgList = new LinkedList<Organization>();//记录当前机构所有父级机构
		List<TempPo> newList = new ArrayList<TempPo>();//构造树节点
		String orgSyscode = organization.getOrgSyscode();
		if(orgList != null && !orgList.isEmpty()) {
			for(Organization org : orgList) {
				if(orgSyscode.startsWith(org.getOrgSyscode())) {
					TempPo orgPo = new TempPo();
					orgPo.setId(org.getOrgSyscode());
					orgPo.setName(org.getOrgName());
					String fatherCode = userService.findFatherCode(org.getOrgSyscode(), config_orgCode, null);
					orgPo.setParentId(fatherCode);
					newList.add(orgPo);
					parentOrgList.add(org);
				}
			}
		}
		for(Organization org : parentOrgList) {
			List<User> userList = userMap.get(org.getOrgId());
			if(userList != null && !userList.isEmpty()){
				for(TUumsBaseUser user : userList){
					TempPo userPo = new TempPo();
					userPo.setId(user.getUserId());
					userPo.setParentId(org.getOrgSyscode());
					userPo.setName(user.getUserName());
					userPo.setType("user");
					userPo.setCodeId(user.getUserId());
					newList.add(userPo);
				}
			}			
		}
		return newList;
	}
	
	@Autowired
	public void setMyInfoManager(MyInfoManager myInfoManager) {
		this.myInfoManager = myInfoManager;
	}

}
