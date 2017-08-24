package com.strongit.gzjzyh.vo;

import java.io.Serializable;
import java.util.Map;

public class Packet implements Serializable {

	private static final long serialVersionUID = 1L;
	private String operationType;
	private Object operationObj;
	private Map<String, String> attachments;

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public Object getOperationObj() {
		return operationObj;
	}

	public void setOperationObj(Object operationObj) {
		this.operationObj = operationObj;
	}

	public Map<String, String> getAttachments() {
		return attachments;
	}

	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}

}
