package com.strongit.oa.work.util;

/**
 * WEBSERVICE调用时用到的临时JavaBean,用于生成XML
 * @author Administrator
 *
 */
public class TranstionBean {

	private String transId;
	
	private String transName;
	
	private String concurrentMode;

	public String getConcurrentMode() {
		return concurrentMode;
	}

	public void setConcurrentMode(String concurrentMode) {
		this.concurrentMode = concurrentMode;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}
}
