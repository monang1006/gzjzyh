package com.strongit.oa.common.workflow.plugin.util.po;

/**
 * 
 * OA 封装的迁移线扩展属性对象
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Jun 21, 2012 8:02:37 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.plugin.util.po.OATransitionPlugin
 */
public class OATransitionPlugin {
	/**
	 * @field tranlineType 迁移线类型
	 *        "defaultType":默认|"checkboxType":复选|"radioType":单选
	 */
	private String tranlineType;

	/**
	 * @field selectPersonType 人员选择方式 "treeType":树型|"buttonType":按钮型
	 */
	private String selectPersonType;

	/**
	 * @field tranlineValidate 办理必填验证
	 */
	private String tranlineValidate;

	/**
	 * @field dept 协办处室
	 */
	private String dept;

	/**
	 * @field transitionPri 排序号
	 */
	private String transitionPri;

	/**
	 * @field transitiongroup 迁移线分组
	 */
	private String transitiongroup;

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getSelectPersonType() {
		return selectPersonType;
	}

	public void setSelectPersonType(String selectPersonType) {
		this.selectPersonType = selectPersonType;
	}

	public String getTranlineType() {
		return tranlineType;
	}

	public void setTranlineType(String tranlineType) {
		this.tranlineType = tranlineType;
	}

	public String getTranlineValidate() {
		return tranlineValidate;
	}

	public void setTranlineValidate(String tranlineValidate) {
		this.tranlineValidate = tranlineValidate;
	}

	public String getTransitiongroup() {
		return transitiongroup;
	}

	public void setTransitiongroup(String transitiongroup) {
		this.transitiongroup = transitiongroup;
	}

	public String getTransitionPri() {
		return transitionPri;
	}

	public void setTransitionPri(String transitionPri) {
		this.transitionPri = transitionPri;
	}

}
