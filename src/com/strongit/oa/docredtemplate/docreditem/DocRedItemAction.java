/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-27
 * Autour: lanlc
 * Version: V1.0
 * Description：公文模板套红项action
 */
package com.strongit.oa.docredtemplate.docreditem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaDocred;
import com.strongit.oa.bo.ToaDocredGroup;
import com.strongit.oa.docredtemplate.docredtype.DocRedTypeManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "docRedItem.action", type = ServletActionRedirectResult.class) })
public class DocRedItemAction extends BaseActionSupport {
	private Page<ToaDocred> page = new Page<ToaDocred>(FlexTableTag.MAX_ROWS,true);
	private String redstempId; //传递页面套红ID
	private String redtempGroupId; //套红类别ID
	private String redtempGroupName; //套红类别名称
	private File wordDoc; //文档
	private ToaDocred model = new ToaDocred();
	private DocRedItemManager manager;
	private DocRedTypeManager typemanager;
	
	private File[] upload;
	private String[] fileName;
	
	/**
	 * author:lanlc
	 * description:删除公文套红
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String delete() throws Exception {
		try{
			manager.delDocRed(redstempId);
			addActionMessage("删除公文套红成功");
		}catch(ServiceException e){
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		return "reloads";
	}
	
	/**
	 * author:lanlc
	 * description:添加或编辑公文套红
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String input() throws Exception {
		redtempGroupName = typemanager.redtempGroupName(redtempGroupId);
		prepareModel();
		return INPUT;
		
	}
	
	/**
	 * author:lanlc
	 * description:符合条件的套红类别下的套红项列表
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String list() throws Exception {
		page = manager.getItemBydocredType(redtempGroupId, page, model);
		redtempGroupName = typemanager.redtempGroupName(redtempGroupId);
		return SUCCESS;
		
	}
	
	/**
	 * author:lanlc
	 * description:公文套红管理初始化提示界面
	 * modifyer:
	 * @return
	 */
	public String init()throws Exception{
		return "init";
	}
	
	/**
	 * author:lanlc
	 * description:编辑时打开文档，文档流直接写入HttpServletResponse请求
	 * modifyer:
	 * @return
	 * @throws Exception
	 */
	public String opendoc() throws Exception{
		prepareModel();
		HttpServletResponse response = this.getResponse();
		if(redstempId!=null){
			manager.setContentToHttpResponse(response, redstempId);
		}
		return null;
	}
	
	
	@Override
	protected void prepareModel() throws Exception {
		if(redstempId!=null){
			model = manager.getDocRed(redstempId);
			redtempGroupName = model.getToaDocredGroup().getRedtempGroupName();
			redtempGroupId = model.getToaDocredGroup().getRedtempGroupId();
		}else{
			model = new ToaDocred();
		}
	}
	
	/**
	 * author:lanlc
	 * description:保存公文套红
	 * modifyer:
	 * description:
	 * @return
	 */
	@Override
	public String save() throws Exception {
		if("".equals(model.getRedstempId())){
			model.setRedstempId(null);
		}
		try{
			FileInputStream fs = new FileInputStream(wordDoc);
			byte[] fileBuffer = new byte[(int)wordDoc.length()];
			fs.read(fileBuffer);
			model.setRedstempContent(fileBuffer);
		}catch (IOException ex) {
			ex.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		String newdate = sdf.format(new Date());
		model.setRedstempCreateTime(sdf.parse(newdate));
		manager.saveDocRed(model);
		StringBuffer message = new StringBuffer("");
		message.append("{")
		.append("id: '").append(model.getRedstempId()).append("',")
		.append("title: '").append(model.getRedstempTitle()).append("',")
		.append("success: '").append("yes'")
		.append("}");
		this.renderText(message.toString());
		return null;
	}
    	
	/**
	 * 转到上传页面
	 * @author:邓志城
	 * @date:2009-9-17 下午04:47:54
	 * @return
	 * @throws Exception
	 */
	public String toUploadPage()throws Exception {
		return "upload";
	}
	
	/**
	 * 上传套红
	 * @author:邓志城
	 * @date:2009-9-17 下午04:47:07
	 * @return
	 * @throws Exception
	 */
	public String upload() throws Exception {
		try{
			if(upload != null){
				FileInputStream fis = null;
				ToaDocredGroup group = model.getToaDocredGroup();
				for(int i=0;i<upload.length;i++){
					fis = new FileInputStream(upload[i]);
					byte[] buf = FileUtil.inputstream2ByteArray(fis);
					ToaDocred docRed = new ToaDocred();
					docRed.setRedstempContent(buf);
					docRed.setRedstempCreateTime(new Date());
					String name = StringUtils.substringBeforeLast(fileName[i],".");
					docRed.setRedstempTitle(name);
					docRed.setToaDocredGroup(group);
					manager.saveDocRed(docRed);
				}
				addActionMessage("ok");
			}
		}catch(Exception e){
			LogPrintStackUtil.logExceptionInfo(e, "上传套红失败！");
			addActionMessage("error");
		}
		return "upload";
	}

	public String getRedstempId() {
		return redstempId;
	}

	public void setRedstempId(String redstempId) {
		this.redstempId = redstempId;
	}

	public String getRedtempGroupId() {
		return redtempGroupId;
	}

	public void setRedtempGroupId(String redtempGroupId) {
		this.redtempGroupId = redtempGroupId;
	}

	public String getRedtempGroupName() {
		return redtempGroupName;
	}

	public void setRedtempGroupName(String redtempGroupName) {
		this.redtempGroupName = redtempGroupName;
	}

	public File getWordDoc() {
		return wordDoc;
	}

	public void setWordDoc(File wordDoc) {
		this.wordDoc = wordDoc;
	}

	public Page<ToaDocred> getPage() {
		return page;
	}
	@Autowired
	public void setManager(DocRedItemManager manager) {
		this.manager = manager;
	}

	public void setModel(ToaDocred model) {
		this.model = model;
	}
	@Autowired
	public void setTypemanager(DocRedTypeManager typemanager) {
		this.typemanager = typemanager;
	}

	public ToaDocred getModel() {
		return model;
	}

	public String[] getFileName() {
		return fileName;
	}

	public void setUploadFileName(String[] fileName) {
		this.fileName = fileName;
	}

	public File[] getUpload() {
		return upload;
	}

	public void setUpload(File[] upload) {
		this.upload = upload;
	}
	
}
