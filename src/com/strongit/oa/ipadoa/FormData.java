package com.strongit.oa.ipadoa;

import java.util.ArrayList;
import java.util.List;

import com.strongit.oa.common.workflow.model.Transition;
import com.strongit.oa.ipadoa.model.Suggestions;
import com.strongit.oa.util.Dom4jUtil;
import com.strongit.oa.ipadoa.model.Suggestion;
import org.apache.axis.utils.JavaUtils;

public class FormData{
	private Status status;
	private Form form ;
	private Attachments attachments;
	private Suggestions suggestions;//意见集合
	private Boolean hasContent;
	private OperateButton operateButton;
	private OperateButton transition;
	private OperateButton tasktion;

	public Form getForm() {
		return form;
	}
	public void setForm(Form form) {
		this.form = form;
	}
	public Attachments getAttachments() {
		return attachments;
	}
	public void setAttachments(Attachments attachments) {
		this.attachments = attachments;
	}
	public Suggestions getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(Suggestions suggestions) {
		this.suggestions = suggestions;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Boolean getHasContent() {
		return hasContent;
	}
	public void setHasContent(Boolean hasContent) {
		this.hasContent = hasContent;
	}
	public OperateButton getOperateButton() {
		return operateButton;
	}
	public void setOperateButton(OperateButton operateButton) {
		this.operateButton = operateButton;
	}
	public OperateButton getTransition() {
		return transition;
	}
	public void setTransition(OperateButton transition) {
		this.transition = transition;
	}
	public OperateButton getTasktion() {
		return tasktion;
	}
	public void setTasktion(OperateButton tasktion) {
		this.tasktion = tasktion;
	}
	
	
}
