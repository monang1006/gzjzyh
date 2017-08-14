/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理新闻公告附件action跳转类
 */
package com.strongit.oa.notifyattach;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaAfficheAttach;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaPrsnfldrFile;
import com.strongit.oa.notify.NotifyManager;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 新闻公告附件action
 * 
 * @author luosy
 * @version 1.0
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "notify.action", type = ServletActionRedirectResult.class) })
public class NotifyAttachAction extends BaseActionSupport<ToaAfficheAttach>{

	private String attachId;

	private NotifyManager notifyManager;
	
	private ToaAfficheAttach model = new ToaAfficheAttach();

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public ToaAfficheAttach getModel() {
		return null;
	}

	public void setModel(ToaAfficheAttach model) {
		this.model = model;
	}
	
	@Autowired
	public void setNotifyManager(NotifyManager notifyManager) {
		this.notifyManager = notifyManager;
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
	
	/**
	 * author:luosy
	 * description:  下载 公共附件下的文件
	 * modifyer:
	 * description:
	 * @return
	 */
	public String down() throws Exception {
		HttpServletResponse response = super.getResponse();
		ToaAttachment file = notifyManager.getToaAttachmentById(attachId);
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
		return null;
	}





}
