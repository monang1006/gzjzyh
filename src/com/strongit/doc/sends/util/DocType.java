package com.strongit.doc.sends.util;

import java.util.List;

/**
 * 定义列表类别
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-8-10 下午04:43:43
 * @version  2.0.7
 * @classpath com.strongit.doc.sends.util.DocType
 * @comment
 * @email dengzc@strongit.com.cn
 */
public enum DocType {

	Draft("0"),		//草稿
	
	Sign("1"),		//签章
	
	Send("2"),		//分发	
	
	Sended("3"),	//已发送	
	
	ReturnBack("4"),//退回
	
	Recycle("5");	//回收
	
	public static final String DELETE = "1";		//删除状态
	
	public static final String NOTDELETE = "0";		//草稿状态
	
	private String value ;
	
	private List<DocType> docTypeList ;				//枚举对象中包含的枚举列表
	
	private Boolean rule  = Boolean.TRUE;			//定义匹配规则
	
	private Boolean dataInOrgRange = Boolean.TRUE; //将数据查询范围控制在机构范围内
	
	private Boolean dataInDeptRange = Boolean.FALSE;//将数据查询范围控制在部门范围内

	/**
	 * 因为枚举是静态类型，因此每次调用其属性时结果都相同。
	 * 需要调用之后再重置默认属性.
	 * @author:邓志城
	 * @date:2010-9-25 下午03:59:36
	 */
	public void reset() {
		this.setRule(Boolean.TRUE);
		this.dataInDeptRange(Boolean.FALSE);
		this.dataInOrgRange(Boolean.TRUE);
		if(docTypeList != null && !docTypeList.isEmpty()) {
			docTypeList.clear();
		}
	}
	
	DocType(String value) {
		this.value = value ;
	}
	
	public String getValue() {
		return value ;
	}

	public Boolean getRule() {
		return rule;
	}

	public DocType setRule(Boolean rule) {
		this.rule = rule;
		return this ;
	}

	public DocType dataInOrgRange(Boolean dataInOrgRange) {
		this.dataInOrgRange = dataInOrgRange ;
		return this ;
	}
	
	public DocType dataInDeptRange(Boolean dataInDeptRange) {
		this.dataInDeptRange = dataInDeptRange ;
		return this ;
	}

	public Boolean getDataInOrgRange() {
		return dataInOrgRange;
	}
	
	public Boolean getDataInDeptRange() {
		return dataInDeptRange;
	}
	
	public List<DocType> getDocTypeList() {
		return docTypeList;
	}

	public void setDocTypeList(List<DocType> docTypeList) {
		this.docTypeList = docTypeList;
	}
}
