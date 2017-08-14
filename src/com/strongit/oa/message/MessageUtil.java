package com.strongit.oa.message;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.util.GlobalBaseData;

/**
 * 消息处理工具类
 * <P>用户获取所有消息类型</P>
 * @author Administrator
 *
 */
public class MessageUtil {

	/**
	 * 获取所有消息类型
	 * @author:邓志城
	 * @date:2009-7-13 上午11:09:09
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getAllMsgType()throws Exception{
		//存储所有消息类型【这些类型包括工作流中的类型、工作处理、日程、知识库、会议管理】
		Map<String, String> mapType = new HashMap<String, String>();//Map类的值结构<消息类型值,消息类型注解值>
		MessageType type = null;
		//获取工作流系统流程类型【这些类型定义在com.strongit.oa.common.workflow.WorkFlowTypeConst】
		Object obj = WorkFlowTypeConst.class.newInstance();
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field field:fields){
			type = field.getAnnotation(MessageType.class);
			if(type!=null){
				mapType.put(String.valueOf(field.getInt(obj)), type.name());
			}
		}
		//获取其他类型【这些类型定义在com.strongit.oa.util.GlobalBaseData】
		Object globalObj = GlobalBaseData.class.newInstance();
		Field[] globalFields = globalObj.getClass().getDeclaredFields();
		for(Field globalField:globalFields){
			type = globalField.getAnnotation(MessageType.class);
			if(type!=null){
				mapType.put(globalField.get(globalObj).toString(), type.name());
			}
		}
		return mapType;
	}
}
