/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息附件 action跳转类
 */
package com.strongit.oa.message;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaAttachment;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/messageAtt.action") })
public class MessageAttAction extends BaseActionSupport{

	private String attachId;
//	公共附件接口
	private IAttachmentService attachmentService;
	
	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}
	@Autowired
	public void setAttachmentService(IAttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
	public String delete() throws Exception {
		return null;
	}
	public String input() throws Exception {
		return null;
	}
	public String list() throws Exception {
		return null;
	}
	protected void prepareModel() throws Exception {
		
	}
	public String save() throws Exception {
		return null;
	}
	public Object getModel() {
		return null;
	}
	
	/**
	 * author:luosy
	 * description: 下载附件
	 * modifyer:
	 * description:
	 * @throws Exception
	 */
	public void download() throws Exception{
		HttpServletResponse response = super.getResponse();
		ToaAttachment file =attachmentService.getAttachmentById(attachId);
		response.reset();
		response.setContentType("application/x-download");         //windows
		OutputStream output = null;
		try{
			response.addHeader("Content-Disposition", "attachment;filename=" +
			         new String(file.getAttachName().getBytes("gb2312"),"iso8859-1"));
		    output = response.getOutputStream();
		    output.write(file.getAttachCon());
		    output.flush();
		} catch(Exception e) {
			if(logger.isErrorEnabled()){
				logger.error(e.getMessage(),e);
			}
		} finally {		 	    
		    if(output != null){
		      try {
				output.close();
			} catch (IOException e) {
				if(logger.isErrorEnabled()){
					logger.error(e.getMessage(),e);
				}
			}
		      output = null;
		    }
		}
	}

}
