package com.strongit.oa.util;

import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;

public class MessagesConst {
	/** 查找异常*/
	public static final String find_error = "find.error";
	
	/** 保存异常*/
	public static final String save_error = "save.error";
	
	/** 删除异常*/
	public static final String del_error = "delete.error";
	
	/** 审核异常*/
	public static final String aduit_error = "aduit.error";
	
	/** 反审核异常*/
	public static final String unaduit_error = "unaduit.error";
	
	/** 判断异常*/
	public static final String judge_error = "judge.error";
	
	/** 归档异常*/
	public static final String pige_error = "pigeonhole.error";
	
	/** 归档异常*/
	public static final String apply_error = "apply.error";
	
	/** 移动异常*/
	public static final String move_error = "move.error";
	
	/** 案卷组卷异常*/
	public static final String folder_group_error = "folder.group.error";
	
	/** 发送异常*/
	public static final String send_error = "send.error";
	
	/** 一般的操作异常*/
	public static final String operation_error = "operation.error";
	
	/** 创建异常*/
	public static final String create_error = "create.error";
	
	/** 销毁异常*/
	public static final String destory_error = "destory.error";
	
	/** 执行异常*/
	public static final String execute_error = "execute.error";
	
	/** 自定义错误信息*/
	public static final String ANY_ERROR = "any.error";
	
	/**存储俞斌接口变量信息*/
	public static String CODE_RULE; 
	
	static{
		try {
			CODE_RULE = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
