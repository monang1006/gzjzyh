//Source file: F:\\workspace\\StrongOA2.0_DEV\\src\\com\\strongit\\oa\\address\\AddressGroupManager.java

package com.strongit.oa.address;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAddress;
import com.strongit.oa.bo.ToaAddressGroup;
import com.strongit.oa.bo.ToaAddressMail;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      个人通讯录分组Manager
 */
@Service
@Transactional
public class AddressGroupManager {
	private GenericDAOHibernate<ToaAddressGroup, String> groupDao;
	
	private AddressManager addressManager ;
	@Autowired 
	private MyInfoManager infoManager;						//个人信息manager
	/**
	 * @roseuid 495041D20109
	 */
	public AddressGroupManager() {

	}

	/**
	 * @param sessionFactory
	 * @roseuid 495040F7029F
	 */
	@Autowired
	public void setSessioinFactory(SessionFactory sessionFactory) {
		groupDao = new GenericDAOHibernate<ToaAddressGroup, String> (sessionFactory,ToaAddressGroup.class);
	}

	/**
	 * @param group
	 * @roseuid 4950412E03C8
	 */
	public void addGroup(ToaAddressGroup group)  throws SystemException,ServiceException{
		try {
			groupDao.save(group);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"联系组 "+group.getAddrGroupName()});
		}
	}

	/**
	 * @param group
	 * @roseuid 495041520242
	 */
	public void deleteGroup(String group)  throws SystemException,ServiceException{
		try{
			groupDao.delete(group);			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,new Object[] {"联系组"});
		}
	}

	/**
	 * @param group
	 * @roseuid 495041690148
	 */
	public void editGroup(ToaAddressGroup group)  throws SystemException,ServiceException{
		try {
			groupDao.update(group);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"联系组 "+group.getAddrGroupName()});
		}
	}

	/**
	 * @param id
	 * @return com.strongit.oa.bo.ToaAddressGroup
	 * @roseuid 495041820148
	 */
	@Transactional(readOnly=true)
	public ToaAddressGroup getGroupById(String id) throws SystemException,ServiceException {
		try {
			return groupDao.get(id);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系组"});
		}
	}

	/**
	 * author:GD
	 * description:是否存在同样名称的通讯录
	 * modifyer:
	 * description:
	 * @param goupName
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Boolean isExistGroup(String goupName)throws SystemException,ServiceException{
		try{ 
			String userId = userService.getCurrentUser().getUserId();
			Object[] obj = {userId,goupName};
			List list = groupDao.find("from ToaAddressGroup t where t.userId like ? and t.addrGroupName like ?",obj);
			if(null!=list){
				if(list.size()>0){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取通讯录"});
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
	@Transactional(readOnly=true)
	public List<ToaAddress> getAddressByGroupId(String groupId)throws SystemException,ServiceException{
		try{
			return addressManager.getAddressByGroupId(groupId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
	}
	
	/**20140321 tj修改
	 * author:shenyl
	 * description:获取组下的人员分页列表
	 * modifyer:
	 * description:
	 * @param groupId
	 * @param page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public Page<ToaAddress> getAddressByGroupId(String groupId, Page page ,String userId)throws SystemException,ServiceException{
		try{
			return addressManager.getAddressByGroupId(groupId,page,userId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
	}
	/**webservice 调用
	 * 获取组下的人员列表
	 * author  taoji
	 * @param groupId
	 * @param name  查询字段
	 * @param page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @date 2014-2-21 上午10:40:11
	 */
	@Transactional(readOnly=true)
	public Page<ToaAddress> getAddressByGroupId(String groupId,String name, Page page )throws SystemException,ServiceException{
		try{
			return addressManager.getAddressByGroupId(groupId,name,page);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人"});
		}
	}
	
	/**
	 * @return List
	 * @roseuid 4950419D02EE
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<ToaAddressGroup> getGroupList() throws SystemException,ServiceException { 
		String userId = getCurrentUser().getUserId();
		String hql = "from ToaAddressGroup as t where t.userId=? order by t.addrGroupName";
		Object[] values = {userId};
		try {
			return groupDao.find(hql, values);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系组列表"});
		}
	}
	
	/**
	 * 获取常用联系人组
	 * @author:邓志城
	 * @date:2009-7-21 上午11:29:34
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaAddressGroup getComUsedGroup() throws SystemException,ServiceException { 
		String userId = getCurrentUser().getUserId();
		String hql = "from ToaAddressGroup as t where t.userId=? and  t.addrGroupName=?";
		Object[] values = {userId,"常用联系人"};
		try {
			List<ToaAddressGroup> groupList =  groupDao.find(hql, values);
			if(groupList != null && !groupList.isEmpty()){
				return groupList.get(0);
			}else{
				return null;
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系组列表"});
		}
	}
	/**webservice 调用
	 * 获取常用联系人组
	 * @date:2009-7-21 上午11:29:34
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaAddressGroup getComUsedGroup(String userId) throws SystemException,ServiceException { 
		String hql = "from ToaAddressGroup as t where t.userId=? and  t.addrGroupName=?";
		Object[] values = {userId,"常用联系人"};
		try {
			List<ToaAddressGroup> groupList =  groupDao.find(hql, values);
			if(groupList != null && !groupList.isEmpty()){
				return groupList.get(0);
			}else{
				return null;
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系组列表"});
		}
	}

	/**
	 * 获取所有联系组,返回字符串【包含组下面的人员数目】
	 * @author:邓志城
	 * @date:2009-7-21 上午11:29:34
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public String getStrGroupLst()throws SystemException,ServiceException{
		try{
			StringBuffer html = new StringBuffer("");
			
			String userId = getCurrentUser().getUserId();
			String groupHql = "select t.addrGroupId,t.addrGroupName from ToaAddressGroup as t where t.userId=? order by t.addrGroupName";
			//String userCountHql = "select count(*) from ToaAddress t where t.toaAddressGroup.addrGroupId=?";
			StringBuilder userCountHql = new StringBuilder("select count(t.userId) from ToaAddress t,TUumsBaseUser tb where t.toaAddressGroup.addrGroupId=? ");
			userCountHql.append(" and t.userId=tb.userId and tb.userIsdel='0' and tb.userIsactive='1'");
			List groupLst = groupDao.find(groupHql,userId);
			if(groupLst!=null && groupLst.size()>0){
				for(int i=0;i<groupLst.size();i++){
					Object[] objs = (Object[])groupLst.get(i);//Object[]{addrGroupId,addreGroupName}
					Object userCount = groupDao.findUnique(userCountHql.toString(), objs[0]);
					html.append("<li id=\""+objs[0]+"\">")
						.append(" 	<span  >"+objs[1]+"</span><label>&nbsp;(<font>" + userCount + "</font>)"+ "</label>")
						.append("</li>");
				}
			}
			return html.toString();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系组列表"});
		}
	}

	/**
	 * 获取某个组下面的人员数目
	 * @author:邓志城
	 * @date:2009-7-21 下午01:12:46
	 * @param groupId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public int getUserCountByGroupId(String groupId)throws SystemException,ServiceException{
		String userCountSql = "select count(*) from ToaAddress t where t.toaAddressGroup.addrGroupId=?";
		try{
			Object objCount = groupDao.findUnique(userCountSql, groupId);
			/*if(objCount instanceof Long){
				return objCount == null?0:((Long)objCount).intValue();
			}else if(objCount instanceof Integer){
				return objCount == null?0:((Integer)objCount).intValue();
			}*/
			return objCount == null?0:Integer.parseInt(String.valueOf(objCount));  
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系组列表"});
		}
	}
	
	@Autowired
	public void setAddressManager(AddressManager addressManager) {
		this.addressManager = addressManager;
	}
	
	/**
	 * author:dengzc
	 * description:获取所有联系人;用于添加组时同时添加联系人到组中.
	 * @see com.strongit.oa.address.AddressGroupAction.initAdd()
	 * modifyer:
	 * description:
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ToaAddress> getAllAddress() throws SystemException,ServiceException{
		try {
			return addressManager.getAllAdress();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系人列表"});
		}
	}
	
	/**
	 * 将从系统通讯录中的人员导入个人通讯录中
	 * @author:周晓山
	 * @date:2009-7-20 下午02:05:09
	 * @param groupId 用户组ID
	 * @param users 选定的人员信息【标准的格式：user1,user2】
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void androidPersonalAddress(String groupId,List list)throws SystemException,ServiceException{

	//	String[] arr=users.split(",");
		ToaAddressGroup  group = getGroupById(groupId);//获取组对象
		for(int i=0;i<list.size();i++){
			ToaAddress m=new ToaAddress();
			m=(ToaAddress)list.get(i);
		
			ToaAddress address = new ToaAddress();
			address.setUserId(m.getUserId());
			address.setName(m.getName());
			address.setTel1(m.getTel1());
			address.setMobile1(m.getMobile1());
			address.setToaAddressGroup(group);
			addressManager.addAddress(address);//保存通讯录信息
			ToaAddressMail addressMail = new ToaAddressMail();
			addressMail.setToaAddress(address);
			addressMail.setIsDefault("1");//设置为默认邮件
			addressMail.setMail(m.getDefaultEmail());
			addressManager.addAddressMail(addressMail);//保存邮件信息
		}
	}
	
	/**
	 * @author:hecj
	 * @description:根据组别ID用于安卓获取组别内的成员
	 * @param addressManager
	 */
	public Page<ToaAddress> getUserListByGroupId(Page<ToaAddress> page,String groupid){
		
		return  addressManager.getAddressPageByGroupId(page,groupid);
	}
	
	/**
	 * @author:hecj
	 * @description:用于安卓系统获取用户自定义组
	 * @param addressManager
	 */
	@Transactional(readOnly=true)
	public List getAndroidGroupList()throws SystemException,ServiceException{
		try{
			String userId = getCurrentUser().getUserId();
			String groupHql = "select t.addrGroupId,t.addrGroupName from ToaAddressGroup as t where t.userId=? order by t.addrGroupName";
			return  groupDao.find(groupHql,userId);
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系组列表"});
		}
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

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * 根据编码规则获取指定编码对应的父节点编码
	 * @author:邓志城
	 * @date:2009-12-9 上午10:28:20
	 * @param code  需要获取父编码的编码
	 * @param ruleCode 编码规则
	 * @param defaultParent 默认的父节点
	 * @return 获取某编码的父编码
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public String findFatherCode(String code,String ruleCode,String defaultParent) throws SystemException,ServiceException{
		int length1 = 0;//累加器，相当于阶乘
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
			if (codeLength == length1) {//根据编码规则找到对应编码所处的位置。
				if (length2 != 0) {
					fatherCode = code.substring(0, length2);//截取编码
					break;
				}
			}
		}
		return fatherCode;
	}
	
	@Transactional(readOnly=true)
	public User getCurrentUser() throws SystemException,ServiceException{
		return userService.getCurrentUser();
	}
	
	private IUserService userService;
	
	
	/**
	 * author:luosy
	 * description:格式化邮件格式
	 * modifyer:
	 * description:
	 * @param mailAddress  name<****@***.com>,name<****@***.com>.....
	 * @return	<option value=****@***.com>name</option> <option value=****@***.com>name</option> ...
	 */
	@Transactional(readOnly=true)
	public String formatMailAddress(String mailAddress)throws SystemException,ServiceException{
		if(!"".equals(mailAddress)&&null!=mailAddress){
			
			//中文转码
			try {
				if(mailAddress!=null&&!"".equals(mailAddress)){
					mailAddress = URLDecoder.decode(mailAddress, "utf-8");
				}
			} catch (UnsupportedEncodingException e) {
				throw new SystemException("用户名中文转码异常");
			}
			
			String[] mailAdds = mailAddress.split(",");
			StringBuilder sb = new StringBuilder();
			for(String mailadd : mailAdds){
				String name = mailadd.replaceAll("<.*>", "");
				String add =mailadd.replaceAll(name+"<", "").replaceAll(">", "");
				sb.append("<option value='"+add+"'>"+name+"</option>");
			}
			return sb.toString();
		}else{
			return "";
		}
		
	}

	/**
	 * author:luosy
	 * description: 查找重复的用户名
	 * modifyer:
	 * description:
	 * @param groupId 组id
	 * @param users 用户信息
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String checkUserName(String groupId,String users)throws SystemException,ServiceException{
		List<ToaAddress> userlist = this.getAddressByGroupId(groupId);//获取组下的人员列表
		JSONArray userArray = JSONArray.fromObject(users);
		int count = userArray.size();
		String userName = null;//姓名
		String repname = "";
		if(null!=userlist){//组下存在人员
			for(int i=0;i<count;i++){
				JSONObject user = userArray.getJSONObject(i);
				userName = user.getString("userName");
				for(ToaAddress addr:userlist){
					if(addr.getName().equals(userName)){
						if(repname.indexOf(userName)<0){
							repname += userName+",";
						}
					}
				}
			}
		}
		if(repname.length()>0){
			repname = repname.substring(0, repname.length()-1);
		}
		return repname;
	}
	
	/**
	 * 将从系统通讯录中的人员导入个人通讯录中
	 * @author:邓志城
	 * @date:2009-7-20 下午02:05:09
	 * @param groupId 用户组ID
	 * @param users 选定的人员信息【标准的JSON格式：[{\"name\":\"dengzc\",\"age\":123},{\"name\":\"andy\",\"age\":1111}]】
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws ParseException 
	 */
	public void importPublic2PersonalAddress(String groupId,String users)throws SystemException,ServiceException, ParseException{
		JSONArray userArray = JSONArray.fromObject(users);
		int count = userArray.size();
		String userName = null;//姓名
		String userPhone = null;//手机
		String userTel = null;//电话
		String userEmail = null;//EMAIL
		String userId = null;//系统用户ID
		Date birthDay=null;//人员生日
		ToaAddressGroup  group = getGroupById(groupId);//获取组对象
		List<ToaAddress> userlist = this.getAddressByGroupId(groupId);//获取组下的人员列表
		String existNames="";//组已存在的用户名
		if(null!=userlist && userlist.size()>0){//组下存在人员
			for(ToaAddress addr:userlist){
				existNames=existNames+addr.getName()+";";
			}
			for(int i=0;i<count;i++){
				JSONObject user = userArray.getJSONObject(i);
				userName = user.getString("userName");
				if(existNames.indexOf(userName)==-1){//不存在,则添加新的人员
					userPhone = user.getString("userPhone");
					userTel = user.getString("userTel");
					userEmail = user.getString("userEmail");
					userId = user.getString("userId");
					ToaAddress address = new ToaAddress();
					address.setUserId(userId);
					address.setName(userName);
					address.setTel1(userTel);
					address.setMobile1(userPhone);
					address.setToaAddressGroup(group);
					addressManager.addAddress(address);//保存通讯录信息
					ToaAddressMail addressMail = new ToaAddressMail();
					addressMail.setToaAddress(address);
					addressMail.setIsDefault("1");//设置为默认邮件
					addressMail.setMail(userEmail);
					addressManager.addAddressMail(addressMail);//保存邮件信息
				}
			}
		}else{
				for(int i=0;i<count;i++){
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					JSONObject user = userArray.getJSONObject(i);
					userName = user.getString("userName");
					userPhone = user.getString("userPhone");
					userTel = user.getString("userTel");
					userEmail = user.getString("userEmail");
					userId = user.getString("userId");
					if(infoManager.getInfoByUserid(userId)!=null){
					  birthDay=infoManager.getInfoByUserid(userId).getPrsnBirthday();
					}
					ToaAddress address = new ToaAddress();
					address.setUserId(userId);
					address.setName(userName);
					address.setTel1(userTel);
					address.setMobile1(userPhone);
					address.setToaAddressGroup(group);
					address.setBirthday(birthDay);
					addressManager.addAddress(address);//保存通讯录信息
					ToaAddressMail addressMail = new ToaAddressMail();
					addressMail.setToaAddress(address);
					addressMail.setIsDefault("1");//设置为默认邮件
					addressMail.setMail(userEmail);
					addressManager.addAddressMail(addressMail);//保存邮件信息
				}
		}
	}
	
	
	/**
	 * author:luosy
	 * description:根据分组名称找到分组
	 * modifyer:
	 * description:
	 * @param name
	 * @param userId
	 * @return
	 */
	public ToaAddressGroup getGroupByGroupName(String name,String userId){
		try {
			String hql = "from ToaAddressGroup as t where t.addrGroupName=? and t.userId=? ";
			Object[] values = {name,userId};
			List list = groupDao.find(hql, values);
			if(null!=list){
				if(list.size()>0){
					return (ToaAddressGroup)list.get(0);
				}else{
					return null;
				}
			}else{
				return null;
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"联系组列表"});
		}
	}
}
