package com.strongit.oa.common.workflow.plugin.util;

/**
 * Const 定义节点插件属性常量
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 20, 2012 11:04:37 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.plugin.util.NodesettingPluginConst
 */
public class NodesettingPluginConst {
	/**
	 * 不可驳回到此节点 "0":可驳回|"1":不可驳回 默认值为"0"
	 */
	public static String PLUGINS_NOTOVERRULE = "plugins_notoverrule";
	/**
	 * @field PLUGINS_NOTBACKSPACE 不可退回到此节点 "0":可退回|"1":不可退回 默认值为"0"
	 */
	public static String PLUGINS_NOTBACKSPACE = "plugins_notbackspace";

	/**
	 * 显示意见征询 "0":不显示|"1":显示 默认值为"0"
	 */
	public static String PLUGINS_YJZX = "plugins_yjzx";

	/**
	 * 审批意见必填 "0":不验证必填|"1":验证必填 默认值为"0"
	 */
	public static String PLUGINS_SUGGESTIONREQUIRED = "plugins_suggestionrequired";

	/**
	 * 提示上一步退回意见 "0":不提示|"1":提示 默认值为"0"
	 */
	public static String PLUGINS_NOTIFYBACKINFO = "plugins_notifybackinfo";

	/**
	 * 快捷办理模式 "0":未选中|"1":选中 默认值为"0"
	 */
	public static String PLUGINS_ISSUGGESTIONBUTTON = "plugins_isSuggestionButton";

	/**
	 * 弹出办理模式 "0":未选中|"1":选中 默认值为"0"
	 */
	public static String PLUGINS_ISPOP = "plugins_isPop";

	/**
	 * 菜单办理模式 "0":未选中|"1":选中 默认值为"0"
	 */
	public static String PLUGINS_ISMENU = "plugins_isMenu";

	/**
	 * 展开办理模式 "0":未选中|"1":选中 默认值为"0"
	 */
	public static String PLUGINS_ISOPEN = "plugins_isOpen";

	/**
	 * 判断启动此子流程时是选择了多个处室还是多个科室 "0":未选中|"1":选中 默认值为"0"
	 */
	public static String PLUGINS_ISNOTALLOWCONTINUETODEPT = "plugins_isNotAllowContinueToDept";

	/**
	 * 子流程节点选择部门id;对应flow_subprocess.jsp中plugins_NodeOrgId
	 */
	public static String PLUGINS_NODEORGID = "plugins_NodeOrgId";

	/**
	 * 子流程节点选择部门名称;对应flow_subprocess.jsp中plugins_nodeDePartName
	 */
	public static String PLUGINS_NODEDEPARTNAME = "plugins_nodeDePartName";

	/**
	 * 经办人控件名称；对应flow_next.jsp中返回经办人HTML控件名称
	 */
	public static final String PLUGINS_DORETURN_TASKACTOR = "plugins_doReturnTaskActos";

	/**
	 * 子流程返回父流程时不选择人员
	 */
	public static final String PLUGINS_DORETURN = "plugins_doReturn";

	/**
	 * @field PLUGINS_CHKSHOWBACKSUGGESTION 是否允许显示退回意见，1是允许，0是不允许
	 */
	public static final String PLUGINS_CHKSHOWBACKSUGGESTION = "plugins_chkShowBackSuggestion";

	/**
	 * @field plugins_chkShowBlankSuggestion 是否允许显示空白意见，1是允许，0是不允许
	 */
	public static final String plugins_chkShowBlankSuggestion = "plugins_chkShowBlankSuggestion";

	/**
	 * @field plugins_businessFlag 节点上设置的控件信息（含绑定的意见控件,及其他控件属性设置）
	 */
	public static final String PLUGINS_BUSINESSFLAG = "plugins_businessFlag";

	/**
	 * @field PLUGINS_MUSTFETCHTASKTIMEOUT 超期自动退回标识
	 * 选中：值为1,标识超期自动退回到该节点；未选中：值为0。
	 */
	public static final String PLUGINS_MUSTFETCHTASKTIMEOUT = "plugins_mustFetchTaskTimeOut";
}
