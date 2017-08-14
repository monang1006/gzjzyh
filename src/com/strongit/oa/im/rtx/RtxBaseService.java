package com.strongit.oa.im.rtx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.address.AddressOrgManager;
import com.strongit.oa.address.AddressOrgManager.CompatorOrg;
import com.strongit.oa.bo.ToaGroup;
import com.strongit.oa.bo.ToaView;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.model.Task;
import com.strongit.oa.im.service.AbstractBaseService;
import com.strongmvc.exception.SystemException;
import com.strongmvc.service.ServiceLocator;

/**
 * 即时通讯接口实现 - RTX.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-6-1 上午09:10:51
 * @version  2.0.2.3
 * @classpath com.strongit.oa.im.rtx.RtxBaseService
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
public class RtxBaseService extends AbstractBaseService {

	static RTX361Service rtxService = RTX361Service.getInstance();	//通过单例模式得到.
	
	IUserService userService;//注入统一用户接口
	
	AddressOrgManager orgManager;
	
	public RtxBaseService(){
		this.userService = (IUserService)ServiceLocator.getService("userService");
		this.orgManager = (AddressOrgManager)ServiceLocator.getService("addressOrgManager");
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
	public String addDept(String deptId, String parentDeptId,
			String deptName, String desc, Object... objects) {
		try {
			return rtxService.addDept(deptId, parentDeptId, deptName, desc);
		} catch (SystemException e) {
			logger.error("添加机构失败！"+e.getMessage());
		}
		return null;
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
	 * @param objects	附加参数
	 * @return 操作结果
	 */
	@Override
	public String addUser(String deptId, String loginName, String password,
			String realName, String mobile, Object... objects) {
		try {
			return rtxService.addUser(deptId, loginName, password, realName, mobile);
		} catch (SystemException e) {
			logger.error("添加用户失败！"+e.getMessage());
		}
		return null;
	}

	/**
	 * 删除部门
	 * @author:邓志城
	 * @date:2010-5-24 下午07:27:03
	 * @param deptId	部门id
	 * @param cascade	是否级联删除子部门以及人员 "1"表示级联删除
	 * @return			返回操作结果
	 */
	@Override
	public String delDept(String deptId, String cascade) {
		try {
			return rtxService.delDept(deptId, cascade);
		} catch (SystemException e) {
			logger.error("删除部门失败！"+e.getMessage());
		}
		return null;
	}

	/**
	 * 删除RTX中所有数据.
	 * @author:邓志城
	 * @date:2010-7-5 下午04:47:33
	 */
	public void delDepts() {
		String rootDept = this.getDeptSubDepts("0");//得到所有根机构
		if(rootDept != null && !"".equals(rootDept)){
			String[] rootDepts = rootDept.split(",");
			for(String rootDeptId : rootDepts){
				//删除一级部门之前先删除用户
				String userId = getDeptUsers(rootDeptId);//需要获取此部门下的所有人员
				if(userId!=null && !"".equals(userId)){
					String[] userIds = userId.split(",");
					for(int j=0;j<userIds.length;j++){
						this.delUser(userIds[j]);		
					}
					
				}
				this.delDept(rootDeptId, "1");
			}
		}
	}
	
	/**
	 * 删除用户
	 * @author:邓志城
	 * @date:2010-5-24 下午07:25:17
	 * @param userLoginName			用户登录名
	 * @return						返回操作结果
	 */
	@Override
	public String delUser(String userLoginName) {
		try {
			return rtxService.delUser(userLoginName);
		} catch (SystemException e) {
			logger.error("删除用户失败！"+e.getMessage());
		}
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
	 * 校验指定的用户是否存在
	 * @author:邓志城
	 * @date:2010-5-24 下午05:22:52
	 * @param userLoginName 用户登录名
	 * @return
	 * 	<P>返回是否存在的标示，true：表示存在；false：表示不存在</P>
	 */
	@Override
	public boolean isUserExist(String userLoginName) {
		String ret = rtxService.isUserExist(userLoginName);
		if(ret.equals("0")){
			return true;
		}
		return false;
	}

	/**
	 * 发送即时消息
	 * @author:邓志城
	 * @date:2010-5-24 下午05:02:00
	 * @param sender   发送人
	 * @param receiver 接收人
	 * @param message  消息内容
	 * @param objects  附加参数
	 * @return 操作结果
	 * <P>
	 * 	格式:操作码|操作结果 
	 * 	约定：操作成功操作码为“0”，操作失败操作码为“1”
	 * </P>
	 * 例如：
	 * 	发送成功时返回内容：0|发送成功
	 *  发送失败时返回内容：1|失败描述信息
	 */
	@Override
	public String sendMessage(String sender, String receiver,
			String message, Task task) {
		//modify by yangwg
		String password = task==null?"":task.getPassword();
		return rtxService.sendIM(sender, receiver, message, password);
	}

	/**
	 * 发送消息提醒
	 * @author:邓志城
	 * @date:2010-5-28 下午03:35:05
	 * @param title		提醒标题
	 * @param message	提醒内容
	 * @param receiver	接收人
	 * @param objects	附加参数
	 * @return			操作结果
	 */
	@Override
	public String sendNotify(String title, String message, String receiver,
			Object... objects) {
		String type = "0";
		String delayTime = "5000";
		if(objects.length > 0){
			type = objects[0].toString();
		}
		if(objects.length > 1){
			delayTime = objects[1].toString();
		}
		return rtxService.sendNotify(receiver, message, "", type, title, delayTime);
	}

	/**
	 * 获取用户的sessionKey，用于登陆RTX
	 * @param userLoginName
	 * @return
	 * @throws SystemException
	 */
	@Override
	public String getSessionKey(String userLoginName) {
		return rtxService.getSessionKey(userLoginName);
	}

	/**
	 * 得到用户信息
	 */
	public String getUserInfo(String userLoginName){
		return rtxService.getUserInfo(userLoginName);
	}
	
	/**
	 * 获取部门下所有用户
	 * @param deptId 部门ID
	 * @return "user1,user2,user3,...,userN"
	 */
	public String getDeptUsers(String deptId){
		String ret = "";
		try{
			ret = rtxService.getDeptUsers(deptId);
		}catch(Exception e){
			logger.error("getDeptUsers", e);
			ret = "1";
		}
		return ret;
	}
	
	/**
	 * 根据部门id得到部门信息
	 * @author:邓志城
	 * @date:2010-5-24 下午07:08:58
	 * @param deptId 部门id
	 * @return
	 * 	<P>返回部门信息
	 * 	格式：deptName:部门名称,deptId:部门ID
	 * </P>
	 */
	public String getDeptInfo(String deptId){
		try {
			return rtxService.getDeptInfo(deptId);
		} catch (SystemException e) {
			logger.error("getDeptInfo()"+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 获取指定部门下的部门id字符串集合.
	 * @author:邓志城
	 * @date:2010-5-24 下午07:02:23
	 * @param deptId 部门id
	 * @return
	 * 	<P>返回子部门id字符串集合，多个部门以逗号隔开</P>
	 */
	public String getDeptSubDepts(String deptId){
		return rtxService.getDeptSubDepts(deptId);
	}

	/**
	 * 同步用户信息
	 */
	public void synchronizedUserInfo() {
		logger.error("Rtx中密码存储的是明文,更新用户信息密码也是明文，无法直接从用户表中读取明文的密码，因此同步功能暂不实现.");
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
			/*List<Organization> lstOaOrg = orgManager.getAllDeparments();
			for(Organization org:lstOaOrg){
				orgManager.updateOrganization(org.getOrgId());
			}*/
			//清空数据.
			this.delDepts();
			Map<String, List<User>> userMap = userService.getUserMap();
			doOa2Im(orgManager.getAllDeparments(),userMap);
		}catch(Exception e){
			logger.error("oa2im()", e);
			throw new SystemException("用户同步失败!");
		}
		return null;
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
	private String doOa2Im(List<Organization> lstOaOrg,Map<String, List<User>> userMap) throws Exception {
			if(lstOaOrg!=null && lstOaOrg.size()>0){
				AddressOrgManager.CompatorOrg compator = orgManager.new CompatorOrg(); 
				Collections.sort(lstOaOrg,compator);
				Map<String, Organization> orgMap = new HashMap<String, Organization>();
				for(int j=0,k=1;j<lstOaOrg.size();k++,j++){
					Organization organization = lstOaOrg.get(j);
					organization.setRest2(String.valueOf(k));//用于作为RTX机构id
					orgMap.put(organization.getOrgId(), organization);
				}
				for(int i=0;i<lstOaOrg.size();i++){
					Organization org = lstOaOrg.get(i);
					String orgParentId = org.getOrgParentId();
					if(orgParentId == null || org.getOrgId().equals(orgParentId)){//顶级机构 
						orgParentId = "0";
					}else{
						Organization parentOrg = orgMap.get(orgParentId);
						orgParentId = parentOrg.getRest2();
					}
					this.addDept(org.getRest2(), orgParentId, org.getOrgName(), String.valueOf(org.getOrgSequence()));
					//同步部门下的人员
					List<User> userLst =  userMap.get(org.getOrgId());
					if(userLst!=null && userLst.size()>0){
						for(User user:userLst){
							addUser(org.getRest2(), user.getUserLoginname(), "1", user.getUserName(), user.getRest2());	
						}
					}
				}	
			}
		return "finish";
	}
	
	/**
	 * author:dengzc
	 * description:递归同步组织机构和用户
	 * 设计思路:一直同步根节点；遍历List时，如果遇到对象中rtx parentId为0的即为根节点。遇到这样的对象时,
	 * 取出其子节点列表。然后更新每个子节点的rtx parentId为0，同时删除当前对象。这样，list列表没循环一次就
	 * 会减少一个对象。更新子节点时，因为rest4字段保存了rtx parentId信息，所以下次同步到rtx时可以取出其关于
	 * rtx的标示。并且父节点一定是存在的。
	 * modifyer:
	 * description:
	 * @param lstOaOrg
	 * @return
	 * @throws Exception
	 */
/*	private  String rtx(List<Organization> lstOaOrg)throws Exception{
		//synchronized (lock) {
		Map<String, List<User>> userMap = userService.getUserMap();
			if(lstOaOrg!=null && lstOaOrg.size()>0){
				for(int i=0;i<lstOaOrg.size();i++){
					Organization org = lstOaOrg.get(i);
					if(org.getRest3().equals("0")){
						if(!isDeptExist(org.getRest2())){//不存在
							addDept(org.getRest2(), org.getRest4(), org.getOrgName(), org.getOrgName());
							
						}else{//如果存在,则先删除再添加
							delDept(org.getRest2(),"1");
							addDept(org.getRest2(), org.getRest4(), org.getOrgName(), org.getOrgName());
						}
						//同步机构下的用户
						List<User> userLst =  userMap.get(org.getOrgId());
						if(userLst!=null && userLst.size()>0){
							for(User user:userLst){
								if(!"0".equals(isUserExist(user.getUserLoginname()))){ //不存在此用户
									addUser(org.getRest2(), user.getUserLoginname(), "1", user.getUserName(), user.getUserTel());	
								}else{
									delUser(user.getUserLoginname());
									addUser(org.getRest2(), user.getUserLoginname(), "1", user.getUserName(), user.getUserTel());	
								}
							}
						}
						List<Organization> l = child(org,lstOaOrg);
						if(l.size()>0){
							for(int j=0;j<l.size();j++){
								Organization o = l.get(j);
								int k = lstOaOrg.indexOf(o);
								o.setRest3("0");
								lstOaOrg.set(k, o);
							}
						}
						lstOaOrg.remove(org);
					}
				}	
				rtx(lstOaOrg);
			}
		//}
		return "finish";
	}*/

	/**
	 * author:dengzc
	 * description:递归查找Rtx中所有的部门
	 * modifyer:
	 * description:
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public List<RtxOrgBean> findOrgList(String deptId,List<RtxOrgBean> rtxOrgLst)throws Exception{
		if(deptId == null || "".equals(deptId)){
			deptId = "0";
		}
		if(rtxOrgLst == null){
			rtxOrgLst = new ArrayList<RtxOrgBean>();//存储所有的Rtx组织机构
		}
		String id = getDeptSubDepts(deptId);//子部门
		if(!"".equals(id)){
			String[] ids = id.split(",");
			for(int i=0;i<ids.length;i++){
				String deptInfo = getDeptInfo(ids[i]);
				String deptNameInfo = deptInfo.split(",")[0];
				String deptName = deptNameInfo.split(":")[1];
				String deptDescInfo = deptInfo.split(",")[2];
				String deptDesc = deptDescInfo.split(":")[1];
				RtxOrgBean bean = new RtxOrgBean();
				bean.setOrgId(ids[i]);
				bean.setOrgName(deptName);
				bean.setOrgDesc(deptDesc);
				rtxOrgLst.add(bean);
				findOrgList(ids[i],rtxOrgLst);
			}
		}
		return rtxOrgLst;
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
		List<Object[]> lst = new ArrayList<Object[]>();
		String id = getDeptSubDepts(deptId);
		String[] ids = id.split(",");
		String[] hasChild = new String[ids.length];
		if(!"".equals(id)){
			for(int j=0;j<ids.length;j++){
				String deptInfo = getDeptInfo(ids[j]);
				String deptNameInfo = deptInfo.split(",")[0];
				String deptName = deptNameInfo.split(":")[1];
				lst.add(new Object[]{ids[j],deptName,deptId});
			}
			for(int i=0;i<lst.size();i++){
				Object[] org = lst.get(i);
				String userId = getDeptUsers(org[0].toString());
				if((userId!=null && !"".equals(userId))||isHasChild(org[0].toString())){
					hasChild[i] = "1";//组织机构下有人员,或部门
				}else{
					hasChild[i] = "0";
				}
			}
		}
		return new Object[]{lst,hasChild};
	}

	/**
	 * author:dengzc
	 * description:
	 * modifyer:
	 * description:
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	private boolean isHasChild(String orgId){
		//获取部门下的子部门
		String childId = getDeptSubDepts(orgId);
		if(childId!=null && !"".equals(childId)){
			return true;
		}
		return false;
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
		StringBuffer str=new StringBuffer("");
		String userId = getDeptUsers(deptId);//需要获取此部门下的所有人员
		String childId = getDeptSubDepts(deptId);;//需要获取此部门下的所有子部门
		//将人员挂接到此树下
		if(userId!=null && !"".equals(userId)){
			String[] userIds = userId.split(",");
			for(int j=userIds.length-1;j>=0;j--){
				String userInfo = getUserInfo(userIds[j]);
				String userNameInfo = userInfo.split(",")[1];
				String userName = userNameInfo.split(":")[1];
				str.append("<li id="+userIds[j]+"><span>"+userName+"</span><label style='display:none;'>person</label></li>\n");			
			}
			
		}
		if(childId!=null && !"".equals(childId)){
			String[] childIds = childId.split(",");
			for(int i=0;i<childIds.length;i++){
				String childDeptInfo = getDeptInfo(childIds[i]);
				String childDeptNameInfo = childDeptInfo.split(",")[0];
				String childDeptName = childDeptNameInfo.split(":")[1];
				//如果当前部门下无人员
				String userLst = getDeptUsers(childIds[i]);//需要获取此部门下的所有人员
				if("".equals(userLst)&& !isHasChild(childIds[i])){ //无子节点
					str.append("<li id="+childIds[i]+" leafChange='folder-org-leaf'><span>"+childDeptName+"</span><label style='display:none;'>org</label></li>\n");
				}else{//此部门下还存在人员或子部门
					str.append("<li id="+childIds[i]+" ><span>"+childDeptName+"</span>\n");
					str.append("<ul class=ajax>\n");
					str.append("<li id="+childIds[i]+i+">{url:"+ServletActionContext.getRequest().getContextPath()+"/im/iM!synajaxtree.action?deptId="+childIds[i]+"}</li>\n");
					str.append("</ul>\n");
					str.append("</li>\n");					
				}
			}
		}
		return str.toString();
	}

	@Override
	public int getUserStatus(String userLoginName) {
		// TODO Auto-generated method stub
		return 0;
	}
}
