package com.strongit.oa.common.workflow.parameter;

public class Parameter {
	private boolean isHandleProcess = true;// 默认可执行handleProcess()操作

	private boolean back = false; // 是否为退回
	/**
	 * 退回意见
	 */
	private String suggestion;
	/**
	 * @field daiBan 是否代办
	 */
	private boolean daiBan = false; // 代办意见

	public boolean isHandleProcess() {
		return isHandleProcess;
	}

	public void setHandleProcess(boolean isHandleProcess) {
		this.isHandleProcess = isHandleProcess;
	}

	public boolean isDaiBan() {
		return daiBan;
	}

	public void setDaiBan(boolean daiBan) {
		this.daiBan = daiBan;
	}

	public boolean isBack() {
		return back;
	}

	public void setBack(boolean back) {
		this.back = back;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
}
