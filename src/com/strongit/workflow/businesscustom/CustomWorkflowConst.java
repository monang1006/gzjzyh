package com.strongit.workflow.businesscustom;

public class CustomWorkflowConst {
	/**
	 * 子流程在父流程中对应的节点id
	 */
	public static final String WORKFLOW_SUPERPROCESS_NODEID = "com.strongit.superprocess.nodeid";

	/**
	 * 协办的标志：人保厅二级单位需求，科室单位需要控制到第四级
	 */
	public static final String WORKFLOW_SUPERPROCESS_ISALLOWCONTINUETODEPT = "com.strongit.isAllowContinueToDept";

	/**
	 * 协办的标志
	 */
	public static final String WORKFLOW_SUPERPROCESS_TRANSITIONDEPT = "com.strongit.transitionDept";

	/**
	 * 存储父流程表单数据
	 */
	public static final String WORKFLOW_SUPERPROCESS_PERSONDEMO = "@{personDemo}";

	/**
	 * 保存上一步退回信息的变量标识前缀（配合Token id一起使用）
	 */
	public static final String WORKFLOW_PROCESS_BACKINFO_PREFIX = "workflow_process_backinfo_";

	/**
	 * 退文方式：退回
	 */
	public static final String WORKFLOW_TASK_BACKTYPE_BACKSPACE = "0";

	/**
	 * 退文方式：退回上一步
	 */
	public static final String WORKFLOW_TASK_BACKTYPE_BACKSPACEPREV = "1";

	/**
	 * 退文方式：驳回
	 */
	public static final String WORKFLOW_TASK_BACKTYPE_OVERRULE = "2";

}
