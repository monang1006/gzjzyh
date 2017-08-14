package com.strongit.oa.util;

import com.strongit.oa.message.MessageType;

public class GlobalBaseData {
	/** 比较 **************************************************************************/

    /** 空结果 */
    public static final int nullresult = -1;

    /** 等于 */
    public static final int equal = 0;

    /** 大于 */
    public static final int morethan = 1;

    /** 小于 */
    public static final int lessthan = 2;
    
    /** 时间比较类型**************************************************************************/

    /**只比较年*/
    public static final int compare_year = 1;
    
    /**只比较年和月*/
    public static final int compare_yearmonth = 2;
    
    /**比较年月日*/
    public static final int compare_yearmonthday = 3;
    
    /** 模块对应编号**************************************************************************/
    
    public static final String message_model = "02"; //短消息模块
    
    /** 可上传附件的最大（单位：B）*****************************************/
    
    public static final int ATTACH_SIZE = 2097152; //可上传附件的最大（单位：B）
    
    public static final int IMANGE_SIZE = 1048576; //可上传图片的最大（单位：B）1M
    

    /**手机模块 对应手机短信的模块编码*/
    public static final String SMSCODE_SMS = "1";
    /**消息模块 对应手机短信的模块编码*/
    public static final String SMSCODE_MESSAGE = "0";
    /**日程模块 对应手机短信的模块编码*/
    public static final String SMSCODE_CALENDAR = "b";
    /**会议管理模块 对应手机短信的模块编码*/
    public static final String SMSCODE_MEETING = "2";
    /**通讯录模块 对应手机短信的模块编码*/
    public static final String SMSCODE_ADDRESS = "3";
    /**工作流模块 对应手机短信的模块编码*/
    public static final String SMSCODE_WORKFLOW = "4";
    /**知识库模块 对应手机短信的模块编码*/
    public static final String SMSCODE_KNOWLEDGE = "5";
    /**webservice--IPP 对应手机短信的模块编码*/
    public static final String SMSCODE_WS_IPP = "6";
    /**webservice--财政 对应手机短信的模块编码*/
    public static final String SMSCODE_WS_FINANCE = "7";
    /** 大蚂蚁发送短信对应手机短信的模块编码 */
    public static final String SMSCODE_BIGANT = "8";
    /** 投票调查发送短信对应手机短信的模块编码 */
    public static final String SMSCODE_SURVEY = "9";
    /** webservice--财政定时发送 对应手机短信的模块编码 */
    public static final String SMSCODE_WS_FINANCE_INTIME = "a";

    /** 大蚂蚁发送短信对应手机短信的模块编码 */
    public static final String SMSCODE_DCDB = "c";
    
    /**消息模块分类 ———— 工作处理*/
    @MessageType(name="待办工作")
    public static final String MSG_GZCL = "GZCL";
    /**消息模块分类 ————日程管理*/
    @MessageType(name="日程提醒")
    public static final String MSG_CALENDAR = "CALENDAR";
    /**消息模块分类 ———— 知识库*/
    @MessageType(name="知识库")
    public static final String MSG_KNOWLEDGE = "KNOWLEDGE";
    /**消息模块分类 ———— 会议管理*/
    @MessageType(name="会议通知")
    public static final String MSG_MEETING = "MEETING";
    
    //操作权限常量定义
    public static final String PRIVIL_TYPE_MODULE = "0";
    public static final String PRIVIL_TYPE_FUNCTION = "1";
    public static final String IS_YES = "1";
    public static final String IS_NO = "0";
    public static final String RULE_CODE_PRIVIL = "privil";
    
    public static String USER_TYPE_SUPER = "0";
	public static String USER_TYPE_SYSTEM = "1";
	public static String USER_TYPE_MANAGER = "2";
	public static String USER_TYPE_USER = "3";
	public static String USER_TYPE_ANONY = "4";
	public static String RULE_CODE_ORG = "org";
	public static String RULE_CODE_GROUP = "group";
	public static String RULE_CODE_AREA = "area";
	public static String RULE_CODE_USER = "user";
	public static final int RULE_TYPE_PARALLEL = 0;
	public static final int RULE_TYPE_NEXT = 1;
	public static final String PRIVIL_TYPE_MANAGE = "0";
	public static final String PRIVIL_TYPE_BUSINESS = "1";
	public static final int INFO_SEQUENCE_LENGTH = 10;
	public static final String MOVE_DOWN_IN_TREE = "down";
	public static final String MOVE_UP_IN_TREE = "up";
	public static final String SERVICE_AUTHENTICATION_EXCEPTION = "服务访问权限异常";
	public static final String SERVICE_DAO_EXCEPTION = "数据级异常";
	public static final String SERVICE_SYSTEM_EXCEPTION = "系统级异常";
	public static final String SERVICE_SERVICE_EXCEPTION = "服务级异常";
	public static final String SERVICE_ELSE_EXCEPTION = "其他未知异常";
	public static final String CURRENT_SERVICE_VALIDITY = "session";
	public static final int RELATION_MAXNUM = 100;
	

    public static final String GXSYGJ = "4028822725bfea420125bfefc6530001";//在数据字典中设置的自定义机构
    
    public static final String WORKFLOWPREFIX = "plugins_flowName";//发起新流程设置的流程名称前缀
    public static final String WORKFLOWIDPREFIX = "plugins_flowId";//发起流程设置的流程id前缀
    public static final String FORMPREFIX = "plugins_formName";//发起新流程挂接表单前缀
    public static final String FORMIDPRIFIX = "plugins_formId";
    
    
    //自定义提醒标签资源文件键值定义 ---  ApplicationMessage.properties
    public static final String REMIND_EMAIL = "remind.email";//电子邮件
    public static final String REMIND_MESSAGE = "remind.message";//短消息
    public static final String REMIND_SMS = "remind.sms";//手机短信
    public static final String REMIND_IM = "remind.im";//即时通讯
    
    
    //资源文件中信息Key值定义
    public static final String WORKFLOW_RESSIGN_REMIND = "workflow.ressign.remind";//任务指派提醒
    public static final String WORKFLOW_RESSIGN_LOG    = "workflow.ressign.log";//任务指派日志
    public static final String WORKFLOW_HANDLERWORKFLOW = "workflow.handlerworkflow"; //提交数据至工作流
    public static final String WORKFLOW_HANDLERWORKFLOWNEXT = "workflow.handlerworkflow.next"; //提交数据至工作流下一环节
    public static final String WORKFLOW_SYSTEM_REMIND = "workflow.system.remind";//工作流流转过程中,系统发送提醒,提醒内容从资源文件中读取
    public static final String WORKFLOW_ASSIGN_REMIND = "workflow.assign.remind";//流程委派提醒
    
    public static final String ADDRESS_PERSONAL_ROOT_NAME = "address.personal.root.name";//个人通讯录根节点名
    public static final String ADDRESS_PUBLIC_ROOT_NAME = "address.public.root.name";//系统通讯录跟节点名
    public static final String ADDRESS_ALL_ROOT_NAME = "address.public.root.name";//全部人员节点名
    public static final String ADDRESS_USERGROUP_ROOT_NAME="address.usergroup.root.name";//用户组节点名
    
    public static final String WORKFLOW_RESSIGN_FINDRECORD = "workflow.ressign.findrecord";//找到委派记录{流程委派时检验是否有其他人委派给当前用户}
    
    public static final String BOOKMARK_NOTSET = "bookmark.notset"	;						   //书签映射未设置	
    public static final String BOOKMARK_SAVE = "bookmark.save";								   //保存映射
    
    public static final String SENDDOC_DELETE_DOC = "senddoc.delete.doc";						//删除公文草稿
    
    //页面设置时按钮默认值
    public static final String WORKFLOW_BUTTONNAME_BUTTONNAME="workflow.buttonname.buttonname";//提交下一步处理人
    public static final String WORKFLOW_BUTTONNAME_TOVIEWSTATE="workflow.buttonname.toViewState";//处理状态
    public static final String WORKFLOW_BUTTONNAME_TOSAVE="workflow.buttonname.toSave";//保存
    public static final String WORKFLOW_BUTTONNAME_TOSAVEORTONEXT="workflow.buttonname.toSaveOrToNext";//保存或提交
    public static final String WORKFLOW_BUTTONNAME_TOCHANGEMAINACTOR="workflow.buttonname.toChangeMainActor";//主板变更
    public static final String WORKFLOW_BUTTONNAME_TOBACK="workflow.buttonname.toBack";//退回
    public static final String WORKFLOW_BUTTONNAME_TORETURNBACK="workflow.buttonname.toReturnBack";//驳回
    public static final String WORKFLOW_BUTTONNAME_TOPRINT="workflow.buttonname.toPrint";//打印
    public static final String WORKFLOW_BUTTONNAME_TOPREV="workflow.buttonname.toPrev";//退回上一处理人
    
    //签收节点后缀名称
    public static final String WORKFLOW_SIGNNODE_NAMESUFFIX="workflow.signnode.namesuffix";//退回上一处理人
    
    public static final String SENDDOC_SUBMIT_DOC = "senddoc.submit.doc";						//提交公文草稿
    public static final String SENDDOC_BACK_DOC = "senddoc.back.doc";
}
