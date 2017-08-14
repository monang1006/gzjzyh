package com.strongit.oa.im.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.bo.ToaImConfig;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.model.Task;
import com.strongit.oa.im.cache.Cache;

/**
 * 即时通讯软件接口.
 * - 对所有即时通讯软件提供统一的接口.
 * - 操作返回结果约定：
 * 	操作成功返回“0”；操作失败返回“1”
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-5-24 下午04:53:13
 * @version  2.0.2.3
 * @classpath com.strongit.oa.im.service.IMService
 * @comment
 * @email dengzc@strongit.com.cn
 */
public abstract class AbstractBaseService {

	protected Logger logger = LoggerFactory.getLogger(AbstractBaseService.class);

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
	public abstract String sendMessage(String sender,String receiver,String message,Task task);
	
	/**
	 * 查询即时通讯软件是否启用
	 * @author:邓志城
	 * @date:2010-5-24 下午05:15:53
	 * @return 
	 * 	<P>返回是否启用的标示，true：表示启用状态；false：表示禁用状态</P>
	 */
	public boolean isEnabled(){
		ToaImConfig config = Cache.get();
		if(config == null){
			return false;
		}
		String state = config.getImconfigState();
		if("1".equals(state)){
			return true;
		}
		return false;
	}

	/**
	 * 查询即时通讯软件轮询器是否启用
	 * @author:邓志城
	 * @date:2010-5-24 下午05:15:53
	 * @return 
	 * 	<P>返回是否启用的标示，true：表示启用状态；false：表示禁用状态</P>
	 */
	public boolean isLoopEnabled(){
		ToaImConfig config = Cache.get();
		if(config == null){
			return false;
		}
		String state = config.getRest2();
		if("1".equals(state)){
			return true;
		}
		return false;
	}
	
	/**
	 * 校验指定的用户是否存在
	 * @author:邓志城
	 * @date:2010-5-24 下午05:22:52
	 * @param userLoginName 用户登录名
	 * @return
	 * 	<P>返回是否存在的标示，true：表示存在；false：表示不存在</P>
	 */
	public abstract boolean isUserExist(String userLoginName);
	
	
	public abstract int getUserStatus(String userLoginName);

	

	/**
	 * 根据用户登录名得到用户信息
	 * - 此方法提供给子类重载。
	 * @author:邓志城
	 * @date:2010-5-24 下午06:54:52
	 * @param userLoginName 用户登录名
	 * @return 返回用户信息.
	 */
	public abstract String getUserInfo(String userLoginName) ;

	/**
	 * 获取指定部门下的部门id列表.
	 * @author:邓志城
	 * @date:2010-5-24 下午07:00:05
	 * @param deptId 部门id
	 * @return
	 * 	<P>返回子部门id列表</P>
	 */
	public abstract List getChildDeptList(String deptId);
	
	/**
	 * 根据部门id得到部门信息
	 * @author:邓志城
	 * @date:2010-5-24 下午07:06:10
	 * @param deptId 部门id
	 * @return
	 * 	<P>返回部门信息</P>
	 */
	public abstract Object[] getDepartmentInfo(String deptId);

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
	public abstract String addUser(String deptId,String loginName,String password,String realName,String mobile,Object...objects);
	
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
	public abstract String addDept(String deptId,String parentDeptId,String deptName,String desc,Object...objects);
	
	/**
	 * 删除用户
	 * @author:邓志城
	 * @date:2010-5-24 下午07:25:17
	 * @param userLoginName			用户登录名
	 * @return						返回操作结果
	 */
	public abstract String delUser(String userLoginName);
	
	/**
	 * 删除部门
	 * @author:邓志城
	 * @date:2010-5-24 下午07:27:03
	 * @param deptId	部门id
	 * @param cascade	是否级联删除子部门以及人员 "1"表示级联删除
	 * @return			返回操作结果
	 */
	public abstract String delDept(String deptId,String cascade);
	
	/**
	 * 获取部门下的用户.
	 * @author:邓志城
	 * @date:2010-5-28 下午03:29:28
	 * @param deptId 部门id
	 * @return 用户列表
	 */
	public abstract List getDeptUserList(String deptId);

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
	public abstract String sendNotify(String title,String message,String receiver,Object...objects);

	/**
	 * 得到用户SESSIONKEY。
	 * @author:邓志城
	 * @date:2010-6-1 上午11:23:08
	 * @param userLoginName
	 * @return
	 */
	public String getSessionKey(String userLoginName) {
		return null;
	}

	/**
	 * 同步oa数据到即时通讯软件中.
	 * @author:邓志城
	 * @date:2010-6-2 上午08:46:54
	 * @return	返回操作结果.
	 */
	public abstract String oa2im();

	/**
	 * 获取IM组织机构树信息
	 * <P>机构树,从根节点开始遍历,读取每个机构下是否存在子机构信息.</P>
	 * @author:邓志城
	 * @date:2010-6-2 上午09:37:52
	 * @param deptId
	 * @return
	 */
	public abstract Object[] imTreeInfo(String deptId);

	/**
	 * 获取IM组织机构人员信息.
	 * @author:邓志城
	 * @date:2010-6-2 上午09:45:50
	 * @param deptId	部门id
	 * @return	人员信息.
	 */
	public abstract String imUserInfo(String deptId,Object...objects);
	
	/**
	 * 即时通讯定时轮询,同步用户数据
	 * @author:邓志城
	 * @date:2010-10-13 下午02:55:29
	 */
	public void synchronizedUserInfo(){}
	
	/**
	 * 更新用户信息
	 * @param user	用户信息对象
	 */
	public void updateUserInfo(User user){};
}
