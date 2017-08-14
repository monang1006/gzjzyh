package com.strongit.oa.mymail;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaMailBox;
import com.strongit.oa.bo.ToaMailFolder;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * 文件夹处理Action
 * @author yuhz
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/mailFolder.action") })
public class MailFolderAction extends BaseActionSupport{
	
	private ToaMailFolder model=new ToaMailFolder();
	
	private MailBoxManager mailBoxManager;
	
	private MailFolderManager mailFolderManager;
	
	private String parentid;							//父节点ID
	
	private String id;									//当前操作节点ID
	
	private String type;								//当前方法操作类型
	
	@Autowired
	public void setMailBoxManager(MailBoxManager mailBoxManager) {
		this.mailBoxManager = mailBoxManager;
	}

	@Autowired
	public void setMailFolderManager(MailFolderManager mailFolderManager) {
		this.mailFolderManager = mailFolderManager;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	@Override
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:10:34 PM
	 * @desc: 删除操作
	 * @return
	 * @throws Exception String
	 */
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		if(id==null||"".equals(id)){
			return renderText("noid");
		}else{
			ToaMailFolder delObj=mailFolderManager.getObjById(id);
			if("1".equals(delObj.getMailfolderType())||"2".equals(delObj.getMailfolderType())||"3".equals(delObj.getMailfolderType())||"4".equals(delObj.getMailfolderType())){
				return renderText("cant");
			}else{
				try{
					mailFolderManager.delObj(delObj);
					return renderText("true");
				}catch(Exception e){
					logger.error(this, e);
					return renderText("exception");
				}
			}
		}
	}

	@Override
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:10:34 PM
	 * @desc: 对文件夹录入操作
	 * @return
	 * @throws Exception String
	 */
	public String input() throws Exception {
		// TODO Auto-generated method stub
		if("show".equals(type)){
			return INPUT;
		}else if("add".equals(type)){
			model.setMailfolderType("5");
			ToaMailBox parentObj=mailBoxManager.getObjById(parentid);
			if(mailFolderManager.getFolder(parentid, model.getMailfolderName())){
				return renderText("rename");
			}
			model.setToaMailBox(parentObj);
			try{
				mailFolderManager.save(model);
				return renderText("true");
			}catch(Exception e){
				logger.error(this,e);
				return renderText("false");
			}
		}
		return null;
	}
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20093:10:34 PM
	 * @desc: 对邮件文件夹进行编辑操作
	 * @return
	 * @throws Exception String
	 */
	public String edit() throws Exception {
		if("show".equals(type)){				  				//文件夹显示操作
			try{
				model=mailFolderManager.getObjById(id);
			}catch(Exception e){
				e.printStackTrace();
			}
			return "edit";
		}else if("charge".equals(type)){						//文件夹更改操作
			if(id!=null&!"".equals(id)){
				ToaMailFolder folder=mailFolderManager.getObjById(id);
				if("1".equals(folder.getMailfolderType())||"2".equals(folder.getMailfolderType())||"3".equals(folder.getMailfolderType())||"4".equals(folder.getMailfolderType())){
					return renderText("true");
				}else{
					return renderText("false");
				}
			}else{
				return renderText("noid");
			}
		}else if("save".equals(type)){							//文件夹保存操作
			try{
				ToaMailBox parentObj=mailBoxManager.getObjById(parentid);
				if(mailFolderManager.getFolder(parentid, model.getMailfolderName())){
					return renderText("rename");
				}
				model.setToaMailBox(parentObj);
				mailFolderManager.save(model);
				return renderText("true");
			}catch(Exception e){
				logger.error(this, e);
				return renderText("error");
			}
		}else{
			return null;
		}
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

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public ToaMailFolder getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public String getParentid() {
		return parentid;
	}


}
