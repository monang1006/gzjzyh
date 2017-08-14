package com.strongit.oa.im.bigant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
import com.strongit.oa.address.AddressOrgManager;
import com.strongit.oa.bo.ToaBigAntUser;
import com.strongit.oa.bo.ToaGroup;
import com.strongit.oa.bo.ToaView;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.model.Task;
import com.strongit.oa.im.bigant.util.BigAntServiceHelper;
import com.strongit.oa.im.service.AbstractBaseService;
import com.strongit.oa.webservice.client.bigant.BigAntClient;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.SystemException;
import com.strongmvc.service.ServiceLocator;

/**
 * 即时通讯软件实现 - 大蚂蚁
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-6-12 下午02:05:44
 * @version  2.0.2.3
 * @classpath com.strongit.oa.im.bigant.BigAntBaseService
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
public class BigAntBaseService extends AbstractBaseService {

	IUserService userService;//注入统一用户接口
	
	AddressOrgManager orgManager;
	BigAntServiceHelper bigAnt;
	
	public BigAntBaseService(){
		this.userService = (IUserService)ServiceLocator.getService("userService");
		this.orgManager = (AddressOrgManager)ServiceLocator.getService("addressOrgManager");
		bigAnt = new BigAntServiceHelper();
	}

	/**
	 * 添加部门
	 * @author:邓志城
	 * @date:2010-5-24 下午07:23:41
	 * @param deptId		部门id
	 * @param parentDeptId  父部门id
	 * @param deptName		部门名称
	 * @param desc			部门描述信息
	 * @param objects		附加参数
	 * @return				操作结果
	 */
	@Override
	public String addDept(String deptId, String parentDeptId, String deptName,
			String desc, Object... objects) {
		ToaGroup model = new ToaGroup();
		model.setCol_Name(deptName);
		model.setCol_ItemIndex(Long.valueOf(objects[1].toString()));  
		model.setCol_Creator_Name(deptId);
		model = bigAnt.saveGroup(model);
		if(objects.length > 0){
			if(objects[0].toString().equals("0")){
				bigAnt.saveViewAndGroup(Integer.parseInt(parentDeptId), model.getCol_ID());				
			}else{
				bigAnt.saveGroupAndGroup(Integer.parseInt(parentDeptId), model.getCol_ID());
			}
		}
		return String.valueOf(model.getCol_ID());
	}

	/**
	 * 同步根机构.
	 * @author:邓志城
	 * @date:2010-6-17 上午09:52:34
	 * @param deptName	机构名称
	 * @return	机构对象
	 */
	private ToaView addRootDept(ToaView view){
		return bigAnt.saveView(view);
	}

	/**
	 * 添加用户
	 * @author:邓志城
	 * @date:2010-5-24 下午07:21:33
	 * @param deptId    部门id
	 * @param loginName 登录名
	 * @param password  密码
	 * @param realName  用户姓名
	 * @param mobile	手机号码
	 * @param objects	附加参数：1、是否为超级管理员；2、用户部门名称；3、机构为组或视图；4、用户排序号；5、视图或组ID；6、用户ID；7、用户职务；8、用户单位电话；9、用户Email。
	 * @return null
	 * @modifier:刘皙
	 * @modifyContent:添加参数
	 * @modifyDate:2012-8-13 14:42:27
	 */
	@Override
	public String addUser(String deptId, String loginName, String password,
			String realName, String mobile, Object... objects) {
		//if(!this.isUserExist(loginName.toLowerCase())){
			ToaBigAntUser model = new ToaBigAntUser();
			model.setCol_LoginName(loginName.toLowerCase());//登录名全部为小写	
			model.setCol_Name(realName);
			model.setCol_PWord(password);
			model.setCol_Mobile(mobile);//手机号码
			
			if(objects.length > 0){
				if(objects[0] != null){
					model.setCol_IsSuper(objects[0].toString());
				}
			}
			
			if(objects.length >= 2) {
				model.setCol_DeptInfo(objects[1] == null ? "" : objects[1].toString());//用户部门
			}
			
			if(objects.length >= 4) {
				if(objects[3] == null){
					model.setCol_ItemIndex(Long.valueOf("100"));//用户排序号
				}else{
					model.setCol_ItemIndex(Long.valueOf(objects[3].toString()));//用户排序号
				}
			}
			
			if(objects.length >= 6) {
				model.setCol_HomePage(objects[5].toString());//OA中对应的用户id
			}
			
			if(objects.length >= 7) {
				model.setCol_o_JobTitle(objects[6] == null ? "" : objects[6].toString());//用户职务
			}
			
			if(objects.length >= 8) {
				model.setCol_o_Phone(objects[7] == null ? "" : objects[7].toString());//用户单位电话
			}
			
			if(objects.length >= 9) {
				model.setCol_EMail(objects[8] == null ? "" : objects[8].toString());//用户Email
			}
			
			model = bigAnt.saveUser(model);
			if(objects[2].toString().equals("0")){
				bigAnt.saveViewAndUser(Integer.parseInt(deptId), model.getCol_ID(), Integer.parseInt(objects[4].toString()));			
			}else{
				bigAnt.saveGroupAndUser(Integer.parseInt(deptId), model.getCol_ID(), Integer.parseInt(objects[4].toString()));
			}
		//}
		return null;
	}

	@Override
	public String delDept(String deptId, String cascade) {
		return null;
	}

	/**
	 * 删除大蚂蚁中的用户
	 * @param userId 用户ID
	 */
	@Override
	public String delUser(String userId) {
		bigAnt.delete(userId);
		return null;
	}

	@Override
	public List getChildDeptList(String deptId) {
		return null;
	}

	@Override
	public Object[] getDepartmentInfo(String deptId) {
		return null;
	}

	@Override
	public List getDeptUserList(String deptId) {
		return null;
	}

	/**
	 * 根据用户登录名得到用户信息
	 * 返回JSON格式组装的用户对象.
	 */
	@Override
	public String getUserInfo(String userLoginName) {
		ToaBigAntUser model = new ToaBigAntUser();
		model.setCol_LoginName(userLoginName);
		model = bigAnt.getUserInfo(model);
		JSONObject obj = JSONObject.fromObject(model);
		return obj.toString();
	}

	/**
	 * 获取IM组织机构树信息
	 * <P>机构树,从根节点开始遍历,读取每个机构下是否存在子机构信息.</P>
	 * @author:邓志城
	 * @date:2010-6-2 上午09:37:52
	 * @param deptId
	 * @return
	 */
	@Override
	public Object[] imTreeInfo(String deptId) {
		List<ToaView> lst = bigAnt.getAllDeparments();
		List<Object[]> newList = new ArrayList<Object[]>();
		String[] hasChild = new String[lst.size()];
		for(int i = 0; i < lst.size(); i++){
			ToaView model = lst.get(i);
			newList.add(new Object[]{model.getCol_ID(),model.getCol_Name()});
			if(bigAnt.isHasChild(model.getCol_ID(), 4)){
				hasChild[i] = "1";
			}else{
				hasChild[i] = "0";
			}
		}
		return new Object[]{newList,hasChild};
	}

	/**
	 * 获取IM组织机构人员信息.
	 * @author:邓志城
	 * @date:2010-6-2 上午09:45:50
	 * @param deptId	部门id
	 * @return	人员信息.
	 */
	@Override
	public String imUserInfo(String deptId,Object...objects) {
		StringBuffer str = new StringBuffer();
		List<ToaBigAntUser> userList = bigAnt.getUsersByOrgID(Integer.parseInt(deptId), Integer.parseInt(objects[0].toString())); //需要获取此部门下的所有人员
		List<ToaGroup> groupList = bigAnt.getGroupById(Integer.parseInt(deptId), Integer.parseInt(objects[0].toString()));			//需要获取此部门下的所有子部门
		for(int j=0;j<userList.size();j++){
			ToaBigAntUser user = userList.get(j);
			str.append("<li id="+user.getCol_ID()+"><span>"+user.getCol_Name()+"</span><label style='display:none;'>person</label></li>\n");			
		}
		for(int i = 0; i <	groupList.size(); i++){
			ToaGroup group = groupList.get(i);
			List<ToaBigAntUser> lst = bigAnt.getUsersByOrgID(group.getCol_ID(), 2); //需要获取此部门下的所有人员
			if(!bigAnt.isHasChild(group.getCol_ID(), 2) && lst.size() == 0){
				str.append("<li id="+group.getCol_ID()+""+new Date().getTime()+" leafChange='folder-org-leaf'><span>"+group.getCol_Name()+"</span><label style='display:none;'>org</label></li>\n");
			}else{
				//此部门下还存在人员或子部门
				str.append("<li id="+group.getCol_ID()+""+new Date().getTime()+" ><span>"+group.getCol_Name()+"</span><label style='display:none;'>org</label>\n");
				str.append("<ul class=ajax>\n");
				str.append("<li id="+group.getCol_ID()+""+new Date().getTime()+">{url:"+ServletActionContext.getRequest().getContextPath()+"/im/iM!synajaxtree.action?deptId="+group.getCol_ID()+"&type="+2+"}</li>\n");
				str.append("</ul>\n");
				str.append("</li>\n");	
			}
		}
		logger.info(str.toString());
		return str.toString();
	}

	@Override
	public boolean isUserExist(String userLoginName) {
		ToaBigAntUser user = new ToaBigAntUser();
		user.setCol_LoginName(userLoginName);
		return bigAnt.isHasUser(user);
	}

	/**
	 * 从OA像大蚂蚁同步用户信息.
	 * @author:邓志城
	 * @date:2010-9-19 上午11:16:06
	 */
	public void synchronizedUserInfo() {
		List<TUumsBaseUser> userList = userService.getAllUserInfos();
		List<ToaBigAntUser> bigAntUserList = bigAnt.getAllUserInfo();
		Map<String, ToaBigAntUser> bigAntUserMap = new HashMap<String, ToaBigAntUser>();
		for(ToaBigAntUser bigAntUser : bigAntUserList){
			//bigAntUserMap.put(bigAntUser.getCol_LoginName(), bigAntUser);
			bigAntUserMap.put(bigAntUser.getCol_HomePage(), bigAntUser);
		}
		if(userList != null && !userList.isEmpty()) {
			for(TUumsBaseUser user : userList) {
				String userLoginName = user.getUserId();
				ToaBigAntUser bigAntUser = bigAntUserMap.get(userLoginName);
				if(bigAntUser != null){
					String userPassword = user.getUserPassword();
					String bigAntPassword = bigAntUser.getCol_PWord();
					String userName = user.getUserName();
					String bigAntUserName = bigAntUser.getCol_Name();
					String userMobile = user.getRest2();
					String bigAntMobile = bigAntUser.getCol_Mobile();
					ToaBigAntUser model = null;
					if(!userPassword.equals(bigAntPassword)) {//密码不一致
						model = new ToaBigAntUser();
						model.setCol_PWord(userPassword);
					}
					if(!userName.equals(bigAntUserName)) {
						if(model == null) {
							model = new ToaBigAntUser();
						}
						model.setCol_Name(userName);
					}
					if(userMobile != null) {
						if(bigAntMobile == null || !userMobile.equals(bigAntMobile)) {
							if(model == null) {
								model = new ToaBigAntUser();
							}
							model.setCol_Mobile(userMobile);
						}
					}
					if(model != null) {
						model.setCol_o_JobTitle(user.getUserDescription());//职务
						bigAnt.updateUserInfo(bigAntUser.getCol_ID(), model);
					}
				}
			}
		}
		if(!bigAntUserMap.isEmpty()) {
			bigAntUserMap.clear();
		}
		userList.clear();
		logger.info("清空数据...");
	}
	
	/**
	 * 同步oa数据到即时通讯软件中.
	 * @author:邓志城
	 * @date:2010-6-2 上午08:46:54
	 * @return	返回操作结果.
	 */
	@Override
	public String oa2im() {
		try{
			//清空数据
			boolean flag = bigAnt.deleteAll();
			if(flag){
				bigAnt.setNeedCloseConnection(false);
				List<TUumsBaseUser> userLst = userService.getAllUserInfos();
				TUumsBaseUser user = null;
				String orgId = null;
				Map<String,List<TUumsBaseUser>> userMap = new HashMap<String, List<TUumsBaseUser>>();
				Map<String, Object> userIdsMap = new HashMap<String, Object>();
				for(int i=0;i<userLst.size();i++){
					user = userLst.get(i);
					orgId = user.getOrgId();
					userIdsMap.put(user.getUserId(), null);
					if(!userMap.containsKey(orgId)){
						List<TUumsBaseUser> tempUserLst = new ArrayList<TUumsBaseUser>();
						tempUserLst.add(user);
						userMap.put(orgId, tempUserLst);
					}else{
						userMap.get(orgId).add(user);
					}
				}
				List<String> bigAntUserList = bigAnt.getAllUserIds();
				for(String id : bigAntUserList) {
					if(!userIdsMap.containsKey(id)) {//不包含大蚂蚁中的用户
						bigAnt.delete(id);
						logger.info("删除大蚂蚁中的用户，对应OA的id为：" + id);
					}
				}
				doOa2Im(userService.getAllOrgInfo(),userMap);
				//关闭连接
				bigAnt.close();
				bigAnt.setNeedCloseConnection(true);
			}
			return null;
			
		}catch(Exception e){
			logger.error("oa2im()", e);
			throw new SystemException("用户同步失败!");
		}
	}

	/**
	 * 同步数据.
	 * @author:邓志城
	 * @date:2010-6-17 下午12:51:51
	 * @param lstOaOrg
	 * @param userMap
	 * @return
	 * @throws Exception
	 */
	private String doOa2Im(List<TUumsBaseOrg> lstOaOrg,Map<String, List<TUumsBaseUser>> userMap) throws Exception {
			if(lstOaOrg!=null && lstOaOrg.size()>0){
				Collections.sort(lstOaOrg,new Comparator<TUumsBaseOrg>(){

					public int compare(TUumsBaseOrg o1, TUumsBaseOrg o2) {
						Integer io1 = o1.getOrgSyscode().length();
						Integer io2 = o2.getOrgSyscode().length();
						return io1.compareTo(io2);
					}
					
				});
				Map<String, ToaView> orgMap = new HashMap<String, ToaView>();
				for(TUumsBaseOrg organization : lstOaOrg){
					if(organization.getOrgParentId() == null) {//顶级机构
						ToaView view = this.addRootDept(new ToaView(organization.getOrgName(),organization.getOrgSequence().longValue(),organization.getOrgId()));
						List<TUumsBaseUser> userLst =  userMap.get(organization.getOrgId());
						if(userLst!=null && userLst.size()>0){
							for(TUumsBaseUser user:userLst){
								addUser(String.valueOf(view.getCol_ID()), user.getUserLoginname(), user.getUserPassword(), user.getUserName(), user.getRest2(), 
										user.getUserIsSupManager(), view.getCol_Name(), "0", user.getUserSequence(), String.valueOf(view.getCol_ID()), 
										user.getUserId(), user.getUserDescription(), user.getUserTel(), user.getUserEmail());									
							}
						}
						orgMap.put(organization.getOrgId(), view);	
					} else {
						String parentOrgId = organization.getOrgParentId();
						String deptId = null;
						if(orgMap.containsKey(parentOrgId)) {//父机构是View对象
							ToaView view = orgMap.get(parentOrgId);
							deptId = this.addDept(organization.getOrgId(), String.valueOf(view.getCol_ID()), organization.getOrgName(), organization.getOrgDescription(),"0",organization.getOrgSequence());
						} else {
							ToaGroup group = bigAnt.findGroupByOrgId(parentOrgId);
							deptId = this.addDept(organization.getOrgId(), String.valueOf(group.getCol_ID()), organization.getOrgName(), organization.getOrgDescription(),"1",organization.getOrgSequence());
						}
						//同步部门下的人员
						ToaView view = orgMap.get(parentOrgId);
						while(view==null){
							parentOrgId = userService.getOrgInfoByOrgId(parentOrgId).getOrgParentId();
							view = orgMap.get(parentOrgId);
						}
						List<TUumsBaseUser> userLst =  userMap.get(organization.getOrgId());
						if(userLst!=null && userLst.size()>0){
							for(TUumsBaseUser user:userLst){
								addUser(deptId, user.getUserLoginname(), user.getUserPassword(), user.getUserName(), user.getUserTel(), 
										user.getUserIsSupManager(), organization.getOrgName(), "1", user.getUserSequence(), String.valueOf(view.getCol_ID()), 
										user.getUserId(), user.getUserDescription(), user.getUserTel(), user.getUserEmail());	
							}
						}
					}
				}
				orgMap.clear();
				/*for(int i=0;i<lstOaOrg.size();i++){
					TUumsBaseOrg org = lstOaOrg.get(i);
					String orgParentId = org.getOrgParentId();
					if(orgParentId == null || org.getOrgId().equals(orgParentId)){//顶级机构 
						ToaView view = this.addRootDept(new ToaView(org.getOrgName(),org.getOrgSequence().longValue(),org.getOrgId()));
						//同步机构下的用户
						List<TUumsBaseUser> userLst =  userMap.get(org.getOrgId());
						if(userLst!=null && userLst.size()>0){
							for(TUumsBaseUser user:userLst){
								//addUser(String.valueOf(view.getCol_ID()), user.getUserLoginname(), user.getUserPassword(), user.getUserName(), user.getUserTel(),user.getUserIsSupManager(),view.getCol_Name(),"0",user.getUserSequence(),user.getRest2());	
								addUser(String.valueOf(view.getCol_ID()), user.getUserLoginname(), user.getUserPassword(),
										user.getUserName(), user.getUserTel(),user.getUserIsSupManager(),user.getUserId(),
										"0",user.getUserSequence(),user.getRest2());	
								
								addUser(String.valueOf(view.getCol_ID()), user.getUserLoginname(), user.getUserPassword(),
										user.getUserName(), user.getUserTel(),user.getUserIsSupManager(),view.getCol_Name(),
										"0",user.getUserSequence(),user.getRest2(),user.getUserId(),user.getUserDescription());	
							}
						}
					}else{//部门
						TUumsBaseOrg parent = orgMap.get(orgParentId);//得到当前机构的父级机构
						String deptId = null;  
						if(parent.getOrgParentId() == null || parent.getOrgParentId().equals(parent.getOrgId())){//父级机构的父级机构为空
							//ToaView view = bigAnt.isHasView(parent.getOrgName());
							ToaView view = bigAnt.findViewByOAOrgId(parent.getOrgId());
							if(view == null){
								view = this.addRootDept(new ToaView(parent.getOrgName(),parent.getOrgSequence(),parent.getOrgId()));
							}
							deptId = this.addDept(org.getOrgId(), String.valueOf(view.getCol_ID()), org.getOrgName(), org.getOrgDescription(),"0",org.getOrgSequence());//保存部门与机构的关系
						}else{//保存部门与部门的关系
							String parentId = parent.getOrgParentId();
							TUumsBaseOrg root = orgMap.get(parentId);
							//ToaView view = bigAnt.isHasView(root.getOrgName());
							ToaView view = bigAnt.findViewByOAOrgId(root.getOrgId());
							if(view == null){
								view = this.addRootDept(new ToaView(parent.getOrgName(),parent.getOrgSequence(),parent.getOrgId()));
							}
							ToaGroup group = bigAnt.getGroupByName(view.getCol_ID(), parent.getOrgName());
							String id = null;
							if(group == null){
								id = this.addDept(parent.getOrgId(), String.valueOf(view.getCol_ID()), parent.getOrgName(), null,"0",parent.getOrgSequence());//保存部门与机构的关系
							}else{
								id = String.valueOf(group.getCol_ID());
							}
							deptId = this.addDept(org.getOrgId(), id, org.getOrgName(), org.getOrgDescription(),"1",org.getOrgSequence());
						}
						//同步部门下的人员
						List<TUumsBaseUser> userLst =  userMap.get(org.getOrgId());
						if(userLst!=null && userLst.size()>0){
							for(TUumsBaseUser user:userLst){
								//addUser(deptId, user.getUserLoginname(), user.getUserPassword(), user.getUserName(), user.getUserTel(),user.getUserIsSupManager(),org.getOrgName(),"1",user.getUserSequence(),user.getRest2());	
								addUser(deptId, user.getUserLoginname(), user.getUserPassword(), user.getUserName(),
										user.getUserTel(),user.getUserIsSupManager(),org.getOrgName(),"1",
										user.getUserSequence(),user.getRest2(),user.getUserId(),user.getUserDescription());	
							}
						}
					}
				}*/	
			}
		return "finish";
	}

	/**
	 * 大蚂蚁发送即时消息
	 * 扩展参数objects中：第一个参数携带密码信息；
	 * 第二个参数携带标题信息；
	 */
	@Override
	public String sendMessage(String sender, String receiver, String message,
			Task task) {
		return BigAntClient.sendMessage("", message, sender, receiver, task);
	}

	@Override
	public String sendNotify(String title, String message, String receiver,
			Object... objects) {
		// TODO 自动生成方法存根
		return null;
	}

	/**
	 * 更新用户信息
	 * @param user	用户信息对象
	 */
	public void updateUserInfo(User user) {
		Integer id  = bigAnt.getBigAntUserIdFromOAUserId(user.getUserId());
		if(id != null) {
			ToaBigAntUser model = new ToaBigAntUser();
			model.setCol_PWord(user.getUserPassword());//密码
			model.setCol_Name(user.getUserName());//姓名
			model.setCol_Mobile(user.getRest2());//手机号码
			model.setCol_LoginName(user.getUserLoginname());//登录名
			model.setCol_o_Phone(user.getUserTel());//单位电话
			model.setCol_EMail(user.getUserEmail());//Email
			//model.setCol_DeptInfo(user.getOrgId());//机构名称
			model.setCol_o_JobTitle(user.getUserDescription());//职务
			model.setCol_ItemIndex(user.getUserSequence());//排序号
			bigAnt.updateUserInfo(id, model);
			String bigOrgInfo = bigAnt.getBigAntOrgIdFromOAOrgId(user.getOrgId());
			if(bigOrgInfo != null) {
				String[] bigOrgs = bigOrgInfo.split(",");
				String deptId = bigOrgs[0];
				String flag = bigOrgs[1];
				if(flag.toString().equals("0")){
					bigAnt.deleteViewAndUser(bigAnt.getBigAntUserIdFromOAUserId(user.getUserId()));
					bigAnt.saveViewAndUser(Integer.parseInt(deptId), bigAnt.getBigAntUserIdFromOAUserId(user.getUserId()), Integer.parseInt(deptId));			
				}else{
					bigAnt.deleteGroupAndUser(bigAnt.getBigAntUserIdFromOAUserId(user.getUserId()));
					bigAnt.saveGroupAndUser(Integer.parseInt(deptId), bigAnt.getBigAntUserIdFromOAUserId(user.getUserId()), Integer.parseInt(deptId));
				}
			}
		} else {//在大蚂蚁中不存在用户,触发添加操作
			String bigOrgInfo = bigAnt.getBigAntOrgIdFromOAOrgId(user.getOrgId());
			if(bigOrgInfo != null) {
				String[] bigOrgs = bigOrgInfo.split(",");
				String deptId = bigOrgs[0];
				String flag = bigOrgs[1];
				this.addUser(deptId, user.getUserLoginname(), user.getUserPassword(), user.getUserName(), user.getRest2(), 
							"0", user.getOrgName(), flag, user.getUserSequence(), "0", 
							user.getUserId(), user.getUserDescription(), user.getUserTel(), user.getUserEmail());
			}
		}
	}

	@Override
	public int getUserStatus(String userLoginName) {
		// TODO Auto-generated method stub
		return 0;
	}
}
