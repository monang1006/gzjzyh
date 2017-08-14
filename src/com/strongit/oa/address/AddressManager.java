//Source file: F:\\workspace\\StrongOA2.0_DEV\\src\\com\\strongit\\oa\\address\\AddressManager.java

package com.strongit.oa.address;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.bo.ToaAddress;
import com.strongit.oa.bo.ToaAddressGroup;
import com.strongit.oa.bo.ToaAddressMail;
import com.strongit.oa.bo.ToaPersonalConfig;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.model.UserGroup;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.util.FiltrateContent;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      个人通讯录Manager
 */
@Service
@Transactional
public class AddressManager {
	private GenericDAOHibernate<ToaAddress, String> addressDao;
	
	GenericDAOHibernate<Object[], String> dao ;
	
	private AddressMailManager addressMailManager;
	private MyInfoManager infoManager;
	private IUserService userService;
	private AddressGroupManager groupManager;
	/**
	 * @roseuid 495041D2002E
	 */
	public AddressManager() {

	}

	/**
	 * @param sessionFactory
	 * @roseuid 49503EE9002E
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		addressDao = new GenericDAOHibernate<ToaAddress, String>(sessionFactory,ToaAddress.class);
		dao = new GenericDAOHibernate<Object[], String>(sessionFactory,Object[].class);
	}

	/**
	 * @return com.strongit.oa.bo.ToaAddress
	 * @roseuid 49503F270280
	 */
	@Transactional(readOnly=true)
	public ToaAddress getAddressById(String id) throws SystemException,ServiceException {
		try {
			return addressDao.get(id);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
	}

	/**
	 * author:dengzc
	 * description:
	 * 		批量删除邮件信息
	 * modifyer:
	 * description:
	 * @param mail
	 */
	public void deleteMail(Set<ToaAddressMail> mail) throws SystemException,ServiceException{
		try {
			addressMailManager.deleteMail(mail);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,new Object[] {"联系人邮件列表"});
		}
	}
	
	/**
	 * 这里是异步调用，不需要抛出异常
	 * @param address
	 * @roseuid 49503F4801F4
	 */
	public void deleteAddress(String id) throws SystemException,ServiceException{
		String[] ids = id.split(",");
		try {
			addressDao.delete(ids);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,new Object[] {"联系人"});
		}
	}

	/**
	 * author:dengzc
	 * description:获取组下的人员列表
	 * modifyer:
	 * description:
	 * @param groupId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Page<ToaAddress> getAddressPageByGroupId(Page<ToaAddress> page,String groupId)throws SystemException,ServiceException{
		try{
			String hql = "from ToaAddress as t where t.toaAddressGroup.addrGroupId=?";
			Object[] values = {groupId};
			return addressDao.find(page, hql, values);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
		
	}
	
	/**
	 * author:dengzc
	 * description:获取组下的人员列表
	 * modifyer:
	 * description:
	 * @param groupId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaAddress> getAddressByGroupId(String groupId)throws SystemException,ServiceException{
		try{
			String hql = "from ToaAddress as t where t.toaAddressGroup.addrGroupId=?";
			Object[] values = {groupId};
			return addressDao.find(hql, values);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
		
	}
	/**20140321 tj修改
	 * author:shenyl
	 * description:获取组下的人员列表
	 * modifyer:
	 * description:
	 * @param groupId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page getAddressByGroupId(String groupId, Page page ,String userId)throws SystemException,ServiceException{
		try{
			String hql = "select t from ToaAddress t,TUumsBaseUser tb where t.toaAddressGroup.addrGroupId=?";
			hql+=" and t.toaAddressGroup.userId =?";
			hql+=" and t.userId = tb.userId ";
			hql+=" and tb.userIsdel='0' and tb.userIsactive='1' ";
			hql+=" order by t.name ";
			Object[] values = {groupId,userId};
			return addressDao.find(page, hql, values);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
		
	}
	/**20140321 tj修改
	 * 
	 * 扩展，多增加一个用户名称查询
	 * author:shenyl
	 * description:获取组下的人员列表
	 * modifyer:
	 * description:
	 * @param groupId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page getAddressByGroupId(String groupId, Page page ,String userId,String userName)throws SystemException,ServiceException{
		try{
			String hql = "select t from ToaAddress t,TUumsBaseUser tb where t.toaAddressGroup.addrGroupId=?";
			List param=new ArrayList();
			param.add(groupId);
			if(userId!=null&&!"".equals(userId)){
				hql+=" and t.toaAddressGroup.userId =?";
				param.add(userId);
			}
			if(userName!=null&&!"".equals(userName)){
				hql+=" and tb.userName like '%"+userName+"%' ";
				//param.add(userName);
			}
			hql+=" and t.userId = tb.userId ";
			hql+=" and tb.userIsdel='0' and tb.userIsactive='1' ";
			hql+=" order by t.name ";
			
			return addressDao.find(page, hql, param.toArray());
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
		
	}
	/**webservice 调用
	 * 获取组下的人员列表  
	 * author  taoji
	 * @param groupId
	 * @param userName  查询字段 
	 * @param page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @date 2014-2-21 上午10:39:32
	 */
	public Page getAddressByGroupId(String groupId,String userName, Page page )throws SystemException,ServiceException{
		try{
			List<Object> list=new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append("select address from ToaAddress address,TUumsBaseUser user where address.toaAddressGroup.addrGroupId= '"+groupId+"' and address.userId=user.userId");
			if(userName!=null&&!(userName).equals("")){
				hql.append(" and ((user.userName like ? ");
				list.add("%"+userName+"%");
				hql.append(" or user.userLoginname like ?))");
				list.add("%"+userName+"%");
			}
			else{
				hql.append(" and user.userName like ? ");
				list.add("%"+userName+"%");
				userName="$";
			}
			hql.append(" order by address.name ");
			Object[] objs = new Object[list.size()];
			for (int i = 0; i < list.size(); i++) {
				objs[i] = list.get(i);
			}
			return addressDao.find(page, hql.toString(), objs);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
		
	}
	
	/**
	 * author:shenyl
	 * description:判断某个用户是否是当前用户常用联系人
	 * @param groupId
	 * @param userId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String checkIsComUsed(String groupId,String userId)throws SystemException,ServiceException{
		try{
			String flag = "false";
			
			String hql = "from ToaAddress as t where t.toaAddressGroup.addrGroupId=? and t.userId =?";
			Object[] values = {groupId,userId};
			List<ToaAddress>  list = addressDao.find(hql, values);
			if(list != null && list.size() > 0){
				flag = "true";
				 
			}
			return flag;
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
		
	}
	
	/**使用 hibernate 3 的 merge 方法. session.merge(newDetail)即可，它会在 session 缓存中找到持久化对象，把新对象的属性赋过去，再保存原session中的持久化对象。
　　	   如果在session或数据库中没有的对象，用merge方法的话，它也能够帮你把记录 insert 到表中，相当于 save 方法。
	 * @param adress
	 * @roseuid 49503F7A01A5
	 */
	public void editAddress(ToaAddress address)  throws SystemException,ServiceException{
		try {
			addressDao.getSession().merge(address);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"联系人"});
		} 
	}

	/**
	 * 郑志斌 2010-06-03 修改
	 * @desc "email"字段已经被我删除，修改为“parentId”，因为"email"字段在方法中没有用到，
	 * 
	 * @param parentId 父节点
	 * @param groupId
	 * @return List
	 * @roseuid 49503F9D0186
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Page<ToaAddress> getAddressList(Page<ToaAddress> page,String groupId,ToaAddress model) throws SystemException,ServiceException {
		try {
			/*StringBuilder hql = new StringBuilder("select new ToaAddress(t.addrId,t.name,t.tel1,t.mobile1,tm.mail) from ToaAddress t, ");
			hql.append(" ToaAddressMail tm where t.addrId=tm.toaAddress.addrId "); // 表关联
			hql.append(" and t.toaAddressGroup.userId=?");						   //当前用户的组	
			hql.append(" and tm.isDefault='1'");								   //默认邮箱
			List<String> params = new ArrayList<String>(1);
			params.add(userService.getCurrentUser().getUserId());
			if(groupId != null && !"".equals(groupId)){
				hql.append(" and t.toaAddressGroup.addrGroupId=?");
				params.add(groupId);
			}*/
			
			StringBuilder hql = new StringBuilder("select new ToaAddress(t.addrId,tb.userName,tb.userTel,tb.rest2,tb.userEmail,tb.userDescription) from ToaAddress t,TUumsBaseUser tb where t.toaAddressGroup.userId=? ");
			hql.append(" and t.userId=tb.userId and tb.userIsdel='0' and tb.userIsactive='1'");
			List<String> params = new ArrayList<String>(1);
			params.add(userService.getCurrentUser().getUserId());
			if(groupId != null && !"".equals(groupId)){
				hql.append(" and t.toaAddressGroup.addrGroupId=?");
				params.add(groupId);
			}
			
			if(null!=model.getName() && !"".equals(model.getName())){   
				hql.append("and t.name like '%"+FiltrateContent.getNewContent(model.getName())+"%' ");
			}
			if(null!=model.getTel1() && !"".equals(model.getTel1())){
				hql.append("and tb.userTel like '%"+FiltrateContent.getNewContent(model.getTel1())+"%' ");
			}
			if(null!=model.getMobile1() && !"".equals(model.getMobile1())){ 
				hql.append("and tb.rest2 like '%"+FiltrateContent.getNewContent(model.getMobile1())+"%' ");
			}
			hql.append(" order by t.name ");
			page = addressDao.find(page, hql.toString(), params.toArray());
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
		return page;
	}

	/**
	 * 得到联系人列表.仅支持按姓名查询
	 * @author:邓志城
	 * @date:2010-6-29 下午05:58:27
	 * @param page
	 * @param groupId
	 * @param userName
	 * @return
	 * 
	 * 过滤已删除人员
	 * @modify yanjian 2011-09-08 17:00
	 *         名称显示格式如：张三[部门1]
	 */
	public Page<Object[]> getAddressUserList(Page<Object[]> page,String groupId,String userName,String userId,String typewei) {
		StringBuilder hql = new StringBuilder("select DISTINCT t.userId,tb.userName from ToaAddress t,TUumsBaseUser tb where t.toaAddressGroup.userId=? ");
		hql.append(" and t.userId=tb.userId and tb.userIsdel='0' and tb.userIsactive='1'");
		List<String> params = new ArrayList<String>(1);
		params.add(userService.getCurrentUser().getUserId());
		if(groupId != null && !"".equals(groupId)){
			hql.append(" and t.toaAddressGroup.addrGroupId=?");
			params.add(groupId);
		}
		if(userName != null && !"".equals(userName)){
			hql.append(" and t.name like '%"+FiltrateContent.getNewContent(userName)+"%'");
		}
		if("weipai".equals(typewei)){
			hql.append(" and t.userId <> ?");
			params.add(userId);
		}
		
		List<Object[]> result = dao.find(hql.toString(), params.toArray());
		String orgNameTemp=null;
		String userNmameTemp=null;
		for(Object[] objs:result){
			orgNameTemp = "["+userService.getUserDepartmentByUserId(String.valueOf(objs[0])).getOrgName()+"]";
			userNmameTemp=String.valueOf(objs[1])+orgNameTemp;
			objs[1]= userNmameTemp;
		}
		page.setResult(result);
		return page;
	}

	/**
	 * 查询系统通讯,支持父级通讯查看子级通讯录人员.
	 * 注意：这里是直接操作统一用户BO.
	 * @author:邓志城
	 * @date:2010-6-29 下午06:45:20
	 * @param page			分页对象
	 * @param sysCode		机构编码
	 * @param userName		用户姓名
	 * @return
	 * 	Object[]{人员id，人员姓名，手机号码，邮件}
	 *  @modify yanjian 2011-09-08 17:00
	 *         名称显示格式如：张三[部门1]
	 */
	public Page<Object[]> getSystemAddressList(Page<Object[]> page,String sysCode,String userName,String userId,String typewei) {
		StringBuilder hql = new StringBuilder("select t.userId,t.userName,t.rest2,t.userEmail from TUumsBaseUser t,TUumsBaseOrg tb where t.orgId=tb.orgId and t.userIsactive='1'");
		hql.append(" and tb.orgIsdel='0'");//未删除的机构
		hql.append(" and t.userIsdel='0'");//未删除的用户
		if(sysCode != null && !"".equals(sysCode)){
			if(userService.isViewChildOrganizationEnabeld()) {
				hql.append(" and tb.orgSyscode like '"+sysCode+"%'");				
			} else {
				/*hql.append(" ((and tb.orgSyscode like '"+sysCode+"%'");
				hql.append(" and tb.isOrg='0')");//部门人员
				hql.append(" or (tb.orgSyscode like '"+sysCode+"' ))");*/
				TUumsBaseOrg org = userService.getOrgInfoBySyscode(sysCode);
				if("1".equals(org.getIsOrg())){//机构
					hql.append(" and t.supOrgCode like '"+sysCode+"' ");
				} else {
					hql.append(" and t.orgId = '"+org.getOrgId()+"' ");
				}
			}
		}
		if(userName != null && !"".equals(userName)){
			hql.append(" and t.userName like '%"+FiltrateContent.getNewContent(userName)+"%'");
		}
		if("weipai".equals(typewei)){
			hql.append(" and t.userId <>  '"+userId+"'");
			
		}
		hql.append(" order by tb.orgSequence, t.userSequence ");
		List<Object[]> result = dao.find( hql.toString());
		String orgNameTemp=null;
		String userNmameTemp=null;
		for(Object[] objs:result){
			orgNameTemp = "["+userService.getUserDepartmentByUserId(String.valueOf(objs[0])).getOrgName()+"]";
			userNmameTemp=String.valueOf(objs[1])+orgNameTemp;
			objs[1]= userNmameTemp;
		}
		page.setResult(result);
		return page;
	}

	/**
	 * 得到用户组的联系人列表
	 * @author:hecj
	 * @date:2011-07-22 13:17:10
	 * @modify yanjian 2011-09-08 17:00
	 *         名称显示格式如：张三[部门1]
	 */
	@SuppressWarnings("unchecked")
	public Page<Object[]> getGroupAddressList(Page<Object[]> page,String sysCode,String userName,String userId,String typewei) {
		StringBuilder hql;
		page.setAutoCount(false);
		hql = new StringBuilder("select distinct t.userId,t.userName,t.rest2,t.userEmail,t.userSequence from TUumsBaseUser t,TUumsBaseGroupUser tb where t.userId=tb.userId and t.userIsactive='1'");
		hql.append(" and t.userIsdel='0'");//未删除的用户
		StringBuilder hqlCount = new StringBuilder("select count(distinct t.userId) from TUumsBaseUser t,TUumsBaseGroupUser tb where t.userId=tb.userId and t.userIsactive='1'");
		hqlCount.append(" and t.userIsdel='0'");
		if(sysCode != null && !"".equals(sysCode)){
			hql.append(" and tb.baseGroup.groupId='" + sysCode + "'");
			hqlCount.append(" and tb.baseGroup.groupId='" + sysCode + "'");
		} else {
			List<UserGroup> grpList= userService.getAllGroupInfo();
			StringBuilder ids = new StringBuilder();
			if(grpList != null && !grpList.isEmpty()) {
				for(UserGroup g : grpList) {
					ids.append("'").append(g.getGroupId()).append("',"); 
				}
				if(ids.length() > 0) {
					ids.deleteCharAt(ids.length() - 1);
					hql.append(" and tb.baseGroup.groupId in(" + ids + ")");
					hqlCount.append(" and tb.baseGroup.groupId in(" + ids + ")");
				}
			}
		}
		if(userName != null && !"".equals(userName)){
			hql.append(" and t.userName like '%"+FiltrateContent.getNewContent(userName)+"%'");
			hqlCount.append(" and t.userName like '%"+FiltrateContent.getNewContent(userName)+"%'");
		}
		if("weipai".equals(typewei)){
			hql.append(" and t.userId <> '"+userId+"'");
			hqlCount.append(" and t.userId <>'"+userId+"'");
			
		}
		hql.append(" order by t.userSequence ");
		List list = dao.find(hqlCount.toString());
		if(!list.isEmpty()) {
			page.setTotalCount(Integer.parseInt(list.get(0).toString()));
		}
		List<Object[]> result = dao.find( hql.toString());
		String orgNameTemp=null;
		String userNmameTemp=null;
		for(Object[] objs:result){
			orgNameTemp = "["+userService.getUserDepartmentByUserId(String.valueOf(objs[0])).getOrgName()+"]";
			userNmameTemp=String.valueOf(objs[1])+orgNameTemp;
			objs[1]= userNmameTemp;
		}
		page.setResult(result);
		return page;
	}
	
	/**
	 * 得到常用联系人列表.
	 * @author:邓志城
	 * @date:2010-6-29 下午07:43:42
	 * @param groupId
	 * @param userName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getAddressList(String groupId,String userName){
		StringBuilder hql = new StringBuilder("select t.userId,tb.userName,t.addrId from ToaAddress t,TUumsBaseUser tb where t.toaAddressGroup.userId=? ");
		hql.append(" and t.userId=tb.userId and tb.userIsdel='0' and tb.userIsactive='1'");
		List<String> params = new ArrayList<String>(1);
		params.add(userService.getCurrentUser().getUserId());
		if(groupId != null && !"".equals(groupId)){
			hql.append(" and t.toaAddressGroup.addrGroupId=?");
			params.add(groupId);
		}
		if(userName != null && !"".equals(userName)){
			hql.append(" and t.name like '%"+FiltrateContent.getNewContent(userName)+"%'");
		}
		
		List<Object[]> result= dao.find(hql.toString(), params.toArray());
		String orgNameTemp=null;
		String userNmameTemp=null;
		for(Object[] objs:result){
			orgNameTemp = "["+userService.getUserDepartmentByUserId(String.valueOf(objs[0])).getOrgName()+"]";
			userNmameTemp=String.valueOf(objs[1])+orgNameTemp;
			objs[1]= userNmameTemp;
		}
		
		return result;
	}

	/**
	 * 得到系统通讯录用户列表.按机构编码过滤
	 * @author:邓志城
	 * @date:2010-6-29 下午07:43:18
	 * @param sysCode
	 * @param userName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getSystemAddressList(String sysCode,String userName) {
		StringBuilder hql = new StringBuilder("select t.userId,t.userName,t.rest2,t.userEmail from TUumsBaseUser t,TUumsBaseOrg tb where t.orgId=tb.orgId and t.userIsactive='1'");
		hql.append(" and tb.orgIsdel='0'");//未删除的机构
		hql.append(" and t.userIsdel='0'");//未删除的用户
		if(sysCode != null && !"".equals(sysCode)){
			//hql.append(" and tb.orgSyscode like '"+sysCode+"%'");
			if(userService.isViewChildOrganizationEnabeld()) {
				hql.append(" and tb.orgSyscode like '"+sysCode+"%'");				
			} else {
				//hql.append(" and t.supOrgCode like '"+sysCode+"' ");
				TUumsBaseOrg org = userService.getOrgInfoBySyscode(sysCode);
				if("1".equals(org.getIsOrg())){//机构
					hql.append(" and t.supOrgCode like '"+sysCode+"' ");
				} else {
					hql.append(" and t.orgId = '"+org.getOrgId()+"' ");
				}
			}
		}
		if(userName != null && !"".equals(userName)){
			hql.append(" and t.userName like '%"+FiltrateContent.getNewContent(userName)+"%'");
		}
		hql.append(" order by tb.orgSequence, t.userSequence ");
		
		List<Object[]> result= dao.find(hql.toString());
		String orgNameTemp=null;
		String userNmameTemp=null;
		for(Object[] objs:result){
			orgNameTemp = "["+userService.getUserDepartmentByUserId(String.valueOf(objs[0])).getOrgName()+"]";
			userNmameTemp=String.valueOf(objs[1])+orgNameTemp;
			objs[1]= userNmameTemp;
		}
		
		return result;
	}
	
	/**
	 * author:dengzc
	 * description:保存通讯录信息,并返回生成的主键ID
	 * modifyer:
	 * description:
	 * @param address
	 * @return
	 */
	public void addAddress(ToaAddress address) throws SystemException,ServiceException {
		try {
			//address.setUserId(userService.getCurrentUser().getUserId());
			addressDao.save(address);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"联系人"});
		}
	}
	
	/**
	 * author:dengzc
	 * description:添加联系人邮件信息
	 * modifyer:
	 * description:
	 * @param mail
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void addAddressMail(ToaAddressMail mail) throws SystemException,ServiceException{
		try {
			addressMailManager.addMail(mail);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"联系人邮件"});
		}
	}
	
	/**
	 * 
	 * author:dengzc
	 * description:获取所有联系人;用于搜索个人通讯录.
	 * @see com.strongit.oa.address.AddressGroupAction.initAdd()
	 * modifyer:
	 * description:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaAddress> getAllAdress() throws SystemException,ServiceException{
		try {
			String userId = userService.getCurrentUser().getUserId();
			String queryStr = "from ToaAddress t where t.toaAddressGroup.userId=?";
			Object[] values = {userId};
			return addressDao.find(queryStr, values);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
	}

	/**
	 * author:dengzc
	 * description:获取某组下的用户列表
	 * modifyer:
	 * description:
	 * @param groupId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaAddress> getUserAddress(String groupId) throws SystemException,ServiceException{
		try{
			StringBuilder hql = new StringBuilder("from ToaAddress t where t.toaAddressGroup.userId=? ");
			List<String> params = new ArrayList<String>(1);
			params.add(userService.getCurrentUser().getUserId());
			if(groupId != null && !"".equals(groupId)){
				hql.append(" and t.toaAddressGroup.addrGroupId=?");
				params.add(groupId);
			}
			return addressDao.find(hql.toString(), params.toArray());
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"联系人"});
		}
	}
	
	/**
	 * author:郑志斌
	 * description:获取个人通信录中所有组下的用户列表
	 * modifyer:
	 * description:
	 * @param 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaAddress> getUserAddressByGroup() throws SystemException,ServiceException{
		try{
			StringBuffer sb = new StringBuffer("from ToaAddress as address ");
			List<ToaAddressGroup> group =groupManager.getGroupList();
			if (group != null && group.size() > 0) {
				String groupIds="";
				for(int i=0;i< group.size();i++){						
					ToaAddressGroup addrGroup = group.get(i);
					groupIds+=",'"+addrGroup.getAddrGroupId()+"'";
				}
				groupIds=groupIds.substring(1);
				sb.append(" where  address.toaAddressGroup.addrGroupId in ("+groupIds+")");
			}
			
			return addressDao.find(sb.toString());
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"联系人"});
		}
	}
	
	/**
	 * author:dengzc
	 * description:获取用户默认邮箱
	 * modifyer:
	 * description:
	 * @param userId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public String getUserDefaultEmail(String userId) throws SystemException,ServiceException{
		try{
			String defaultEmail = "";
			userId = userService.getCurrentUser().getUserId();//获取当前用户ID
			ToaPersonalConfig config = infoManager.getConfigByUserid(userId);
			if(config!=null){
				defaultEmail = config.getDefaultMail();
			}
			return defaultEmail;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"用户默认邮箱"});
		}
	}

	/**
	 * author:dengzc
	 * description:获取用户邮箱
	 * modifyer:
	 * description:
	 * @param userId
	 * @param type
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public String getUserEmail(String userId,String type) throws SystemException,ServiceException{
		try{
			if("personal".equals(type)){//在个人通讯录中发送邮件,这里的userId为addressId
				ToaAddress address = getAddressById(userId);
				String email = address.getDefaultEmail();
				String userName = address.getName();
				if("".equals(email) || null == email){
					return "";
				}else{
					return userName + "<"+email+">";
				}
			}else{
				User user = userService.getUserInfoByUserId(userId);
				String email = user.getUserEmail();
				String userName = user.getUserName();
				if("".equals(email) || null == email){
					return "";
				}else{
					return userName + "<"+email+">";
				}
			}
		}catch(Exception e){
			throw new ServiceException(MessagesConst.find_error,new Object[]{"获取用户邮箱"});
		}
	}
	
	@Autowired
	public void setAddressMailManager(AddressMailManager addressMailManager) {
		this.addressMailManager = addressMailManager;
	}

	@Autowired
	public void setGroupManager(AddressGroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setInfoManager(MyInfoManager infoManager) {
		this.infoManager = infoManager;
	}
	
	/**
	 * author:luosy
	 * description: 保存联系人对象
	 * modifyer:
	 * description:
	 * @param address 联系人对象
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void save(ToaAddress address) throws SystemException,ServiceException {
		try {
			addressDao.save(address);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"联系人"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 判断是否已存在同名联系人
	 * modifyer:
	 * description:
	 * @param name 联系人姓名
	 * @param userId 用户id
	 * @param groupName 联系人所在组
	 * @return
	 */
	public Boolean isNotSameNameExist(String name,String userId,String groupName){
		try {
			String queryStr = "from ToaAddress t where t.toaAddressGroup.userId=? and t.name=? and t.toaAddressGroup.addrGroupName=? ";
			Object[] values = {userId,name,groupName};
			List list = addressDao.find(queryStr, values);
			if(null!=list){
				if(list.size()>0){
					return false;
				}else{
					return true;
				}
			}else{
				return true;
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
	}
	/**
	* @Title: queryAddressByUserId
	* @Description: 获取当前用户下某个常用联系人的通讯录信息
	* @detailed description：先获取当前用户的常用联系人组，然后根据当前用户的常用联系组Id和常用联系人Id获取通讯记录Id
	* 						 如果当前用户无常用联系人组，则返回空，如果当前用户的常用联系人组无该用户，则返回空
	* @param：@return
	* @param：@throws SystemException
	* @param：@throws ServiceException    
	* @return：ToaAddress
	* @author：申仪玲  
	* @time：2012-12-20 下午04:15:13   
	* @throws
	*/
	public ToaAddress queryAddressByUserId (String userId) throws SystemException,ServiceException{
		ToaAddressGroup addressGroup = groupManager.getComUsedGroup();
		if(addressGroup == null){
			return null;
		}else{		
			String hql = "from ToaAddress as t where t.toaAddressGroup.addrGroupId=? and t.userId =?";
			Object[] values = {addressGroup.getAddrGroupId(),userId};
			@SuppressWarnings("unchecked")
			List<ToaAddress>  list = addressDao.find(hql, values);
			if(list != null && list.size() > 0){
				ToaAddress address = list.get(0);
				return address;
				 
			}else{
				return null;
			}
		}		
	}

}
