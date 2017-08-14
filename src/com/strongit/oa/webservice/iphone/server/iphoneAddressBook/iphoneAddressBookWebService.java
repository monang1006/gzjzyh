package com.strongit.oa.webservice.iphone.server.iphoneAddressBook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.address.AddressGroupManager;
import com.strongit.oa.address.AddressManager;
import com.strongit.oa.bo.ToaAddress;
import com.strongit.oa.bo.ToaAddressGroup;
import com.strongit.oa.bo.ToaAddressMail;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.ipadoa.util.WorkflowForIpadService;

import com.strongit.oa.util.Dom4jUtil;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;

public class iphoneAddressBookWebService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static String STATUS_SUC = "1";// 返回成功状态
	private static String STATUS_FAIL = "0";// 返回失败状态

	List<String[]> result = new ArrayList<String[]>();
    
	WorkflowForIpadService workflowForIpadService;
	private IUserService userService;
	private AddressGroupManager addressGroupManager;
	private AddressManager addressManager;

	public iphoneAddressBookWebService() {

		workflowForIpadService = (WorkflowForIpadService) ServiceLocator.getService("workflowForIpadService");
		userService = (IUserService) ServiceLocator.getService("userService");
		addressGroupManager = (AddressGroupManager) ServiceLocator.getService("addressGroupManager");
		addressManager = (AddressManager) ServiceLocator.getService("addressManager");
	}
	
	private Date parseDate(String dateStr) throws ParseException,SystemException {
		   try {
			  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      return dateFormat.parse(dateStr);
		   } catch (ParseException e) {
		     throw e;
		   } catch (Exception e) {
			   throw new SystemException(e);
		   }
		}
	
	/**向下获取组织机构列表
	 * @param userId
	 * @param parentSysCode
	 * @return
	 */
	public String getSubOrgInfo(String userId, String orgSysCode) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();

		try {
			List<TUumsBaseOrg> orgs = workflowForIpadService.getSubOrgInfo(userId, orgSysCode);
			if(!orgs.isEmpty()){
				for (TUumsBaseOrg org : orgs) {
					String[] str = new String[4];
					str[0] = org.getOrgId();
					str[1] = org.getOrgName();
					str[2] = org.getOrgSyscode();
					str[3] = orgSysCode;
					result.add(str);
				}
			}
			
			ret = dom.createXmlOrgInfo(STATUS_SUC, null, result);
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取部门机构发生异常:" +e.getMessage(), null, null);
		}

		return ret;
	}
	

	/**获取与当前节点(当前节点可能是组织机构也可能是人员)的父级同级别的组织机构列表
	 * @param 当前用户Id
	 * @param type 0代表部门，1代表人员
	 * @param orgSysCode(部门编码或者是人员Id)
	 * @return
	 */
	public String getParentOrgInfo(String userId,String type,String id){
		
		String parentSysCode = "";
		if("".equals(id)||id == null  ){
			
		}else{
			if("0".equals(type)){
				TUumsBaseOrg org = userService.getParentOrgByOrgSyscode(id);
				
				if(org == null){//最顶级节点
					
				}else{		
					TUumsBaseOrg parentOrg = userService.getParentOrgByOrgSyscode(org.getOrgSyscode());
					if(parentOrg == null){
						
					}else{
						parentSysCode = parentOrg.getOrgSyscode();
					}
				}	
			}else{
				TUumsBaseOrg org = userService.getUserDepartmentByUserId(id);
				parentSysCode = org.getSupOrgCode();
			}
		}

		
		return getSubOrgInfo(userId, parentSysCode);
	}
	
	/**获取与当前节点(当前节点可能是组织机构也可能是人员)的父级同级别的组织机构和人员列表
	 * @param 当前用户Id
	 * @param type 0代表部门，1代表人员
	 * @param id(部门编码或者是人员Id)
	 * @return
	 */
	public String getParentOrgAndUserInfo(String userId,String type,String id){
		
		String parentSysCode = "";	
		if("0".equals(type)){
			TUumsBaseOrg org = userService.getParentOrgByOrgSyscode(id);
			
			if(org == null){//最顶级节点
				
			}else{		
				TUumsBaseOrg parentOrg = userService.getParentOrgByOrgSyscode(org.getOrgSyscode());
				if(parentOrg == null){
					
				}else{
					parentSysCode = parentOrg.getOrgSyscode();
				}
			}	
		}else{
			TUumsBaseOrg org = userService.getUserDepartmentByUserId(id);
			parentSysCode = org.getSupOrgCode();
		}
		
		return getOrgInfo(userId, parentSysCode);
	}
	
	/**获取组织机构下机构和人员的列表
	 * @param userId
	 * @param parentSysCode
	 * @return
	 */
	public String getOrgInfo(String userId, String parentSysCode) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();

		try {
			List<TUumsBaseOrg> orgs = workflowForIpadService.getOrgInfo(userId, parentSysCode);
			if(!orgs.isEmpty()){
				for (TUumsBaseOrg org : orgs) {
					String[] str = new String[5];
					str[0] = "0";
					str[1] = org.getOrgId();
					str[2] = org.getOrgName();
					str[3] = org.getOrgSyscode();
					str[4] = parentSysCode;
					result.add(str);
				}
			}
			
			List<TUumsBaseUser> users = workflowForIpadService.getSystemAddressUsers(userId, parentSysCode);
			
			if(!users.isEmpty()){
				for (TUumsBaseUser user : users) {
					String[] str = new String[5];
					str[0] = "1";
					str[1] = user.getUserId();
					str[2] = user.getUserName();
					str[3] = "";
					str[4] = parentSysCode;

					result.add(str);
				}
			}
			ret = dom.createXmlOrgInfo(STATUS_SUC, null, result);
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取数据时发生异常:" +e.getMessage(), null, null);
		}

		return ret;
	}
	
	/**根据组织机构编码获取用户列表
	 * @param userId 当前用户
	 * @param orgSysCode
	 * @param pageSize
	 * @param pageNo
	 * @return
	 */
	public String getUsersByOrgSysCode( String userId, String orgSysCode, String pageSize,String pageNo){
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();

		try {
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			page.setPageNo(Integer.parseInt(pageNo));
			
			Page usersPage  = workflowForIpadService.getSystemAddressUsers(userId, orgSysCode, page);
			List<TUumsBaseUser> users = usersPage.getResult();
			if(users != null && !users.isEmpty()){
				for (TUumsBaseUser user : users) {	
					
					String[] uId = new String[2];
					uId[0] = "string";
					uId[1] = user.getUserId();
					
					String[] uName = new String[2];
					uName[0] = "string";
					uName[1] = user.getUserName();
					
					String[] uMobile = new String[2];
					uMobile[0] = "string";
					uMobile[1] = user.getRest2();
					
					String[] uTel = new String[2];
					uTel[0] = "string";
					uTel[1] = user.getUserTel();
					
					String[] uEmail = new String[2];
					uEmail[0] = "string";
					uEmail[1] = user.getUserEmail();
					
					String[] uIscomUsed = new String[2];
					uIscomUsed[0] = "string";
					uIscomUsed[1] = "0";
					ToaAddressGroup model  = addressGroupManager.getComUsedGroup();
					if(model != null){
						String iscomUsed = addressManager.checkIsComUsed(model.getAddrGroupId(), user.getUserId());
						if("false".equals(iscomUsed)){
							uIscomUsed[1] = "0";
						}else{
							uIscomUsed[1] = "1";
						}
					}

					result.add(uId);
					result.add(uName);
					result.add(uMobile);
					result.add(uTel);
					result.add(uEmail);
					result.add(uIscomUsed);
				}
			}			
			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 6,
					String.valueOf(page.getTotalCount()), String.valueOf(page.getTotalPages()));
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取用户列表发生异常:" +e.getMessage(), null, null);
		}

		return ret;
	}
	
	/**根据用户Id获取个人通讯录下常用联系人人员列表
	 * @param userId
	 * @return
	 */
	public String getComUsedAddress(String userId, String pageSize,String pageNo,String userName){
		
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			userService.setUserId(userId);
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			page.setPageNo(Integer.parseInt(pageNo));
			
		
			ToaAddressGroup model  = addressGroupManager.getComUsedGroup();
			if(model != null){
				//Page usersPage = addressGroupManager.getAddressByGroupId(model.getAddrGroupId(), page,userId);
				Page usersPage = addressManager.getAddressByGroupId(model.getAddrGroupId(), page,userId,userName);
				
				List<ToaAddress> users = usersPage.getResult();
				if(users!=null&&!users.isEmpty()){
					for (ToaAddress toaAddress : users) {
						
						TUumsBaseUser user = userService.getUserInfoByUserId(toaAddress.getUserId());
						
						String[] addressUserId = new String[2];
						addressUserId[0] = "string";
						addressUserId[1] = user.getUserId();
						
						String[] addressUserName = new String[2];						
						addressUserName[0] = "string";
						addressUserName[1] = userService.getUserInfoByUserId(user.getUserId()).getUserName();
												
						String [] addressMobile = new String[2];
						addressMobile[0] = "string";
						addressMobile[1] = user.getRest2();
						
						String [] addressTel = new String[2];
						addressTel[0] = "string";
						addressTel[1] = user.getUserTel();
						
						String [] addressEmail = new String[2];
						addressEmail[0] = "string";
						addressEmail[1] = user.getUserEmail();
						
						String[] addressId = new String[2];
						addressId[0] = "string";
						addressId[1] = toaAddress.getAddrId();
						
						
						result.add(addressUserId);
						result.add(addressUserName);
						result.add(addressMobile);
						result.add(addressTel);
						result.add(addressEmail);
						result.add(addressId);
					}
				}		
			}			
			
			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 6,
					String.valueOf(page.getTotalCount()), String.valueOf(page.getTotalPages()));
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取用户个人通讯录分组发生异常:" +e.getMessage(), null, null);
		}finally {
			userService.setUserId(null);// 因为userService是单例的,一定要在这里重置数据
		}

		return ret;
		
		
	}
	/**
	 * 根据用户Id获取个人通讯录下常用联系人人员列表  
	 * author  taoji
	 * @param userId
	 * @param userName  搜索字段
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * @date 2014-2-21 上午10:29:32
	 */
	public String queryComUsers(String userId,String userName, String pageSize,String pageNo){
		
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			userService.setUserId(userId);
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			page.setPageNo(Integer.parseInt(pageNo));
			
			
			ToaAddressGroup model  = addressGroupManager.getComUsedGroup(userId);
			if(model != null){
				Page usersPage = addressGroupManager.getAddressByGroupId(model.getAddrGroupId(),userName, page);
				List<ToaAddress> users = usersPage.getResult();
				if(users!=null&&!users.isEmpty()){
					for (ToaAddress toaAddress : users) {
						
						TUumsBaseUser user = userService.getUserInfoByUserId(toaAddress.getUserId());
						
						String[] addressUserId = new String[2];
						addressUserId[0] = "string";
						addressUserId[1] = user.getUserId();
						
						String[] addressUserName = new String[2];						
						addressUserName[0] = "string";
						addressUserName[1] = userService.getUserInfoByUserId(user.getUserId()).getUserName();
						
						String [] addressMobile = new String[2];
						addressMobile[0] = "string";
						addressMobile[1] = user.getRest2();
						
						String [] addressTel = new String[2];
						addressTel[0] = "string";
						addressTel[1] = user.getUserTel();
						
						String [] addressEmail = new String[2];
						addressEmail[0] = "string";
						addressEmail[1] = user.getUserEmail();
						
						String[] addressId = new String[2];
						addressId[0] = "string";
						addressId[1] = toaAddress.getAddrId();
						
						
						result.add(addressUserId);
						result.add(addressUserName);
						result.add(addressMobile);
						result.add(addressTel);
						result.add(addressEmail);
						result.add(addressId);
					}
				}		
			}			
			
			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 6,
					String.valueOf(page.getTotalCount()), String.valueOf(page.getTotalPages()));
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取用户个人通讯录分组发生异常:" +e.getMessage(), null, null);
		}finally {
			userService.setUserId(null);// 因为userService是单例的,一定要在这里重置数据
		}
		
		return ret;
		
		
	}
	
	/**根据当前用户Id向常用联系人列表中添加人员（如果常用联系人用户不存在，则创建一个新的常用联系人组）
	 * @param userId 当前用户Id
	 * @param userId 被添加的用户Id,可以添加多个用户，以","隔开
	 * @return
	 */
	public String addUserToComUsedAddress(String userId, String addUserId ){
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();

		try {
			userService.setUserId(userId);
			ToaAddressGroup model  = addressGroupManager.getComUsedGroup();
			if(model == null){//不存在常用联系人组，创建一个新的常用联系人组
				model = new ToaAddressGroup();
				model.setUserId(userId);
				model.setAddrGroupName("常用联系人");
				addressGroupManager.addGroup(model);
			}
			String[] addUserIds = addUserId.split(",");
			for (int i = 0; i < addUserIds.length; i++) {
				//判读该用户是否在是常用联系人
				String flag = addressManager.checkIsComUsed(model.getAddrGroupId(), addUserIds[i]);
				if("true".equals(flag)){
					continue;
				}
				TUumsBaseUser user = userService.getUserInfoByUserId(addUserIds[i]);
				ToaAddress address = new ToaAddress();
				address.setUserId(user.getUserId());
				address.setName(user.getUserName());
				address.setTel1(user.getUserTel());
				address.setMobile1(user.getRest2());
				address.setToaAddressGroup(model);
				addressManager.addAddress(address);//保存通讯录信息
				ToaAddressMail addressMail = new ToaAddressMail();
				addressMail.setToaAddress(address);
				addressMail.setIsDefault("1");//设置为默认邮件
				addressMail.setMail(user.getUserEmail());
				addressManager.addAddressMail(addressMail);//保存邮件信息
			}
			
			
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", "success");
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取用户列表发生异常:" +e.getMessage(), null, null);
		}finally {
			userService.setUserId(null);// 因为userService是单例的,一定要在这里重置数据
		}

		return ret;
	}
	
	/**根据当前用户Id删除常用联系人列表中人员
	 * @param userId 当前用户Id
	 * @param addressId 被删除的通讯记录（可以为多条，以","隔开）
	 * @return
	 */
	public String deleteUserFromComUsedAddress(String userId, String addressId){
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();

		try {
			addressManager.deleteAddress(addressId);			
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", "success");
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取用户列表发生异常:" +e.getMessage(), null, null);
		}

		return ret;
	}
	
	
	public String getUserInfo(String userId){
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();

		try {
			TUumsBaseUser user = userService.getUserInfoByUserId(userId);			
			String[] str = new String[5];
			str[0] = user.getUserId();
			str[1] = user.getUserName();
			str[2] = user.getRest2();
			str[3] = user.getUserTel();
			str[4] = user.getUserEmail();						
			ret = dom.createXmlUserInfo(STATUS_SUC, null, str);
			
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取用户信息发生异常:" +e.getMessage(), null, null);
		}

		return ret;
	}
	/**
	 * author  taoji
	 * @param userId
	 * @param userName
	 * @param orgSysCode
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * XML格式字符
	服务调用成功时返回数据格式如下：
	  <?xml version="1.0" encoding="UTF-8"?>
			<service-response>
				<status>1</status>
				<fail-reason />
				<data totalCount="总记录数" totalPages="总页数">
                    <row>
					<item type="string" value="用户id" />
					<item type="date" value="用户名称" />
					<item type="string" value="用户手机" />
					<item type="date" value="用户电话" />
					<item type="string" value="用户邮件" />
					<item type="date" value="是否为常用联系人" /><!-- 0：否 1：是-->
                    <row>
				</data>
			</service-response>
	服务调用失败时返回数据格式如下：
	<?xml version="1.0" encoding="UTF-8"?>
	<service-response>
		<status>0</status>
		<fail-reason>异常描述</fail-reason>
		<data />
	</service-response>
	 * @date 2014-2-20 下午03:05:22
	 */
	public String queryUsers( String userId,String userName, String orgSysCode, String pageSize,String pageNo){
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();

		try {
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			page.setPageNo(Integer.parseInt(pageNo));
			
			Page usersPage  = workflowForIpadService.getSystemAddressUsers(userId,userName,orgSysCode, page);
			List<TUumsBaseUser> users = usersPage.getResult();
			if(users != null && !users.isEmpty()){
				for (TUumsBaseUser user : users) {	
					
					String[] uId = new String[2];
					uId[0] = "string";
					uId[1] = user.getUserId();
					
					String[] uName = new String[2];
					uName[0] = "string";
					uName[1] = user.getUserName();
					
					String[] uMobile = new String[2];
					uMobile[0] = "string";
					uMobile[1] = user.getRest2();
					
					String[] uTel = new String[2];
					uTel[0] = "string";
					uTel[1] = user.getUserTel();
					
					String[] uEmail = new String[2];
					uEmail[0] = "string";
					uEmail[1] = user.getUserEmail();
					
					String[] uIscomUsed = new String[2];
					uIscomUsed[0] = "string";
					uIscomUsed[1] = "0";
					ToaAddressGroup model  = addressGroupManager.getComUsedGroup();
					if(model != null){
						String iscomUsed = addressManager.checkIsComUsed(model.getAddrGroupId(), user.getUserId());
						if("false".equals(iscomUsed)){
							uIscomUsed[1] = "0";
						}else{
							uIscomUsed[1] = "1";
						}
					}

					result.add(uId);
					result.add(uName);
					result.add(uMobile);
					result.add(uTel);
					result.add(uEmail);
					result.add(uIscomUsed);
				}
			}			
			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 6,
					String.valueOf(page.getTotalCount()), String.valueOf(page.getTotalPages()));
		} catch (Exception e) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取用户列表发生异常:" +e.getMessage(), null, null);
		}

		return ret;
	}
	/**
	* @Title: deleteComUsedAddress
	* @Description: 通过当前用户Id和传过来的被删除用户Id来删除当前用户下的该常用联系人
	* @detailed description：
	* @param：@param curUserId
	* @param：@param userId
	* @param：@return    
	* @return：String
	* @author：申仪玲  
	* @time：2012-12-20 下午04:02:21   
	* @throws
	*/
	public String deleteComUsedAddress(String curUserId, String userId){
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		//必须要设置当前用户Id
		userService.setUserId(curUserId);
		ToaAddress address = addressManager.queryAddressByUserId(userId);
		if(address != null){
			try {
				addressManager.deleteAddress(address.getAddrId());			
				ret = dom.createItemResponseData(STATUS_SUC, null, "string", "success");
			} catch (Exception e) {
				ret = dom.createItemResponseData(STATUS_FAIL, "删除常用联系人发生异常", null, null);
			}
		}else{
			ret = dom.createItemResponseData(STATUS_FAIL, "该用户不是常用联系人，无法删除", null, null);
		}
		
		return ret;
	}
}
