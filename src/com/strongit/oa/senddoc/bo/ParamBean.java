package com.strongit.oa.senddoc.bo;

/**
 * ParamBean 专门用于设置参数
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Feb 16, 2012 8:08:02 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.bo.ParamBean
 */
public class ParamBean {
	private int length;

	private String rootPath;

	private int num;

	private String type;

	private String showCreator;

	private String showDate;

	private String filterSign;

	private String sortType;

	private String url;// 链接跳转路径

	private StringBuilder sortHtml;

	private StringBuilder yjzxHtml;

	private String workflowType;

	private String sectionFontSize;

	private String processStatus;

	// 产品里查看已办是：/senddoc/sendDoc!viewProcessed.action
	// 办公厅环境是：

	private String filterYJZX;

	public String getFilterYJZX() {
		return filterYJZX;
	}

	public void setFilterYJZX(String filterYJZX) {
		this.filterYJZX = filterYJZX;
	}

	public String getShowCreator() {
		return showCreator;
	}

	public void setShowCreator(String showCreator) {
		this.showCreator = showCreator;
	}

	public String getShowDate() {
		return showDate;
	}

	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilterSign() {
		return filterSign;
	}

	public void setFilterSign(String filterSign) {
		this.filterSign = filterSign;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public StringBuilder getYjzxHtml() {
		return yjzxHtml;
	}

	public void setYjzxHtml(StringBuilder yjzxHtml) {
		this.yjzxHtml = yjzxHtml;
	}

	public StringBuilder getSortHtml() {
		return sortHtml;
	}

	public void setSortHtml(StringBuilder sortHtml) {
		this.sortHtml = sortHtml;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public void setSectionFontSize(String sectionFontSize) {
		this.sectionFontSize = sectionFontSize;
	}

	public String getSectionFontSize() {
		return sectionFontSize;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
}
