package com.strongit.oa.senddoc.customquery;

/**
 * 
 * 处理工作流自定义查询数据格式
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Jul 9, 2012 2:11:33 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.customquery.CustomQueryUtil
 */
public class CustomQueryUtil {
	/**
	 * 获取包含流程类型id作为参数构建的sql语句
	 * 
	 * @description
	 * @author 严建
	 * @param workflowType
	 *            流程类型值（存在多个用","隔开）
	 * @param customQuery
	 * @createTime Jul 9, 2012 2:13:46 PM
	 */
	public static String genWorkflowTypeStringForSql(String workflowType,
			StringBuilder customQuery) {
		if (customQuery == null) {
			throw new NullPointerException(" customQuery is not null ");
		}
		if (customQuery.length() > 0) {
			customQuery.append(" and ");
		}
		customQuery.append(" exists ").append(" ( ").append(
				"	select " + "	processinstance.id_ " + "	from "
						+ "		jbpm_processinstance processinstance " + "	where "
						+ "		processinstance.type_id_ in (" + workflowType
						+ ") " + "		and processinstance.id_ = pi.ID_ ").append(
				" ) ");
		return customQuery.toString();
	}

	/**
	 * 获取不包含流程类型id作为参数构建的sql语句
	 * 
	 * @description
	 * @author 严建
	 * @param excludeWorkflowType
	 *            流程类型值（存在多个用","隔开）
	 * @param customQuery
	 * @createTime Jul 9, 2012 2:13:46 PM
	 */
	public static String genExcludeWorkflowTypeStringForSql(
			String excludeWorkflowType, StringBuilder customQuery) {
		if (customQuery == null) {
			throw new NullPointerException(" customQuery is not null ");
		}
		if (customQuery.length() > 0) {
			customQuery.append(" and ");
		}
		customQuery.append(" exists ").append(" ( ").append(
				"	select " + "	processinstance.id_ " + "	from "
						+ "		jbpm_processinstance processinstance " + "	where "
						+ "		processinstance.type_id_  not in ("
						+ excludeWorkflowType + ") "
						+ "		and processinstance.id_ = pi.ID_ ").append(" ) ");
		return customQuery.toString();
	}

}
