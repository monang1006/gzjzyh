package com.strongit.oa.common.workflow.model;

import java.text.MessageFormat;

import com.strongit.oa.util.OALogInfo;

/**
 * 适配日志类，扩展一些功能。
 * - 增加对日志内容格式化支持；
 * - 增加部分构造方法对一些属性赋值；
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-5-7 上午11:16:27
 * @version  2.0.2.3
 * @classpath com.strongit.oa.common.workflow.model.WorkflowLog
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class WorkflowLog extends OALogInfo {

	public WorkflowLog(){
		super("");
	}
	
	public WorkflowLog(String logInfo) {
		super(logInfo);
	}

	public WorkflowLog(String logInfo,String moduleName){
		this(logInfo);
		super.setLogModule(moduleName);
	}
	
	public WorkflowLog(String logInfo,Object...objects){
		super(null);
		try{
			logInfo = MessageFormat.format(logInfo, objects);
		}catch(Exception e){
			e.printStackTrace();
		}
		super.setLogInfo(logInfo);
	}

	public WorkflowLog(String logInfo,String module,Object...objects){
		this(logInfo,objects);
		super.setLogModule(module);
	}
	
}
