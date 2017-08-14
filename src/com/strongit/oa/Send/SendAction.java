package com.strongit.oa.Send;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TabContentFileSend;
import com.strongit.oa.bo.TabContentSend;
import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 公文传输——发文部分
 * 
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 30, 2012 8:48:39 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.Send.SendAction
 */
@ParentPackage("default")
public class SendAction extends BaseActionSupport {
	private TabContentSend sendbean;

	private File[] uploads;

	private String[] uploadsFileName;

	private Page<ToaSenddoc> page = new Page<ToaSenddoc>(FlexTableTag.MAX_ROWS, true);

	private ToaSenddoc senddoc;

	@Autowired
	private SendManage sendmanage;

	@Autowired
	private IWorkflowService workflowService;

	private String pkFieldValue;

	private String workflowName;

	private String workflowNameParame;

	private String formId;

	/**
	 * @field serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		pkFieldValue = senddoc.getSenddocId();
		workflowName = workflowNameParame;
		formId = workflowService.getMainFormIdByWorkflowName(workflowName)
				.toString();
		// TODO Auto-generated method stub
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	public String query() throws Exception {
		page = sendmanage.query(page, senddoc);
		return "query";
	}

	public String upload() throws Exception {
		return "upload";
	}

	public void saveInfo() throws Exception {
		String message = "success";
		if (sendmanage.lock(sendbean.getSenddocId())) {// 锁定指定数据
			try {
				if (uploadsFileName != null && uploadsFileName.length != 0) {
					TabContentFileSend mainContent = new TabContentFileSend();
					mainContent.setFileName(uploadsFileName[0]);
					mainContent.setFileContent(FileUtil
							.inputstream2ByteArray(new FileInputStream(
									uploads[0])));
					sendbean.setMainContent(mainContent);
				}
				// 保存发文信息
				sendmanage.saveSendAllInfo(sendbean);
			} catch (Exception ex) {
				sendmanage.unLockIfException(sendbean.getSenddocId());
				// TODO: handle Exception
				logger.info(ex);
				message = "errordo";
			}
		} else {
			message = "error";
		}
		PrintWriter out = getResponse().getWriter();
		out.println("<script>parent.callback('" + message + "')</script>");
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public TabContentSend getSendbean() {
		return sendbean;
	}

	public void setSendbean(TabContentSend sendbean) {
		this.sendbean = sendbean;
	}

	public File[] getUploads() {
		return uploads;
	}

	public void setUploads(File[] uploads) {
		this.uploads = uploads;
	}

	public String[] getUploadsFileName() {
		return uploadsFileName;
	}

	public void setUploadsFileName(String[] uploadsFileName) {
		this.uploadsFileName = uploadsFileName;
	}

	public Page<ToaSenddoc> getPage() {
		return page;
	}

	public void setPage(Page<ToaSenddoc> page) {
		this.page = page;
	}

	public String getPkFieldValue() {
		return pkFieldValue;
	}

	public void setPkFieldValue(String pkFieldValue) {
		this.pkFieldValue = pkFieldValue;
	}

	public ToaSenddoc getSenddoc() {
		return senddoc;
	}

	public void setSenddoc(ToaSenddoc senddoc) {
		this.senddoc = senddoc;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getWorkflowNameParame() {
		return workflowNameParame;
	}

	public void setWorkflowNameParame(String workflowNameParame) {
		try {
			workflowNameParame= java.net.URLDecoder.decode(workflowNameParame, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.workflowNameParame = workflowNameParame;
	}

}
