package com.strongit.oa.senddoc.customquery;

/**
 * 
 * 适配工作流自定义查询数据
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Jul 9, 2012 2:35:07 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.customquery.CustomQueryAdpter
 */
public class CustomQueryAdpter {
	/**
	 * 适配之前版本使用"-"好处理不包含的问题以及处理一些不规范参数的问题
	 * 
	 * @description
	 * @author 严建
	 * @param workflowTypes
	 *            [workflowType,excludeWorkflowType]
	 * @return newworkflowTypes[workflowType,excludeWorkflowType]
	 * @createTime Jul 9, 2012 2:40:02 PM
	 */
	public static String[] adpterWorkflowType(String[] workflowTypes) {
		String workflowType = workflowTypes[0];
		String excludeWorkflowType = workflowTypes[1];
		String[] newworkflowTypes = new String[2];
		if (workflowType != null && !"".equals(workflowType)
				&& !"null".equals(workflowType)) {
			if (workflowType.startsWith("-")) {
				if (excludeWorkflowType != null
						&& !"".equals(excludeWorkflowType)
						&& !"null".equals(excludeWorkflowType)) {
					if (!excludeWorkflowType.endsWith(",")) {
						excludeWorkflowType += ",";
					}
				} else {
					excludeWorkflowType = "";
				}
				excludeWorkflowType += workflowType.substring(1, workflowType
						.length() - 1);
				workflowType = null;
			}
		} else {
			workflowType = null;
		}
		if (!(excludeWorkflowType != null && !"".equals(excludeWorkflowType) && !"null"
				.equals(excludeWorkflowType))) {
			excludeWorkflowType = null;
		}
		newworkflowTypes[0] = workflowType;
		newworkflowTypes[1] = excludeWorkflowType;
		return newworkflowTypes;
	}
}
