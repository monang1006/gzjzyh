package com.strongit.oa.im.rtx;

public class RTXConst {
	//协议编码
	static int PRO_ADDUSER = 0x0001;  //添加用户
	static int PRO_DELUSER = 0x0002;  //删除用户
	static int PRO_SETUSERSMPLINFO = 0x0003;  //修改用户简单信息
	static int PRO_GETUSERMOREINFO = 0x0004;  //获取用户详细信息
	static int PRO_SETUSERMOREINFO = 0x0005;  //修改用户详细信息
	static int PRO_GETUSERSMPLINFO = 0x0006;  //获取用户简单信息
	static int PRO_SETUSERPRIVILEGE = 0x0007;  //修改用户权限
	static int PRO_IFUSEREXIST = 0x0008;  //判断用户是否存在

	static int PRO_ADDDEPT = 0x0101;  //添加部门
	static int PRO_DELDEPT = 0x0102;  //删除部门
	static int PRO_SETDEPT = 0x0103;  //修改部门信息
	static int PRO_SETDEPTPRIVILEGE = 0x0106;  //修改部门权限
	static int PRO_GETDEPTINFO = 0x0107;  //获取部门信息

	static int PRO_SYS_GETSESSIONKEY = 0x2000;  //获取用户登录SessionKey
	static int PRO_SYS_GETUSERSTATUS = 0x2001;	//获取用户状态
	static int PRO_SYS_SENDIM = 0x2002;  //发送即时消息
	static int PRO_SYS_USERLOGINVERIFY = 0x2003;  //校验用户登录
	
	static int PRO_SYS_SENDNOTIFY = 0x2100;  //发送消息提醒
	
	static int PRO_GETSUBDEPTS = 0x0104;  //获取部门下子部门列表
	static int PRO_GETDEPTUSERS = 0x0105;  //获取部门下用户列表

	//对象名称
	static String OBJNAME_RTXSYS = "SYSTOOLS";
	static String OBJNAME_USERMANAGER = "USERMANAGER";
	static String OBJNAME_DEPTMANAGER = "DEPTMANAGER";
	
	//Rtx系统消息发送者,在配置文件im.properties中定义
	public static final String SYSTEMMESSAGESENDER = "rtx.system.message.sender";
	public static final String CLASSNAME = "im.implClass";
	public static final String ENABLED = "im.enabled";
	
}
