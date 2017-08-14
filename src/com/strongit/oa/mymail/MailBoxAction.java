package com.strongit.oa.mymail;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaMailBox;
import com.strongit.oa.bo.ToaMailFolder;
import com.strongit.oa.common.user.IUserService;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 邮箱管理Action
 * @author yuhz
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/mailBox.action") })
public class MailBoxAction extends BaseActionSupport{
	
	private ToaMailBox model=new ToaMailBox();
	
	private List<ToaMailBox> mailBoxList;
	
	private MailBoxManager mailBoxManager;
	
	private MailFolderManager mailFolderManager;
	
	private MailManager mailManager;
	
	private String type;
	
	private String id;
	
	private String name;
	
	private String serverType;
	
	private List<String[]> folderNumList;
	
	private IUserService userService;
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}


	public List<String[]> getFolderNumList() {
		return folderNumList;
	}


	public void setType(String type) {
		this.type = type;
	}
	

	public void setId(String id) {
		this.id = id;
	}
	
	@Autowired
	public void setMailFolderManager(MailFolderManager mailFolderManager) {
		this.mailFolderManager = mailFolderManager;
	}

	@Autowired
	public void setMailBoxManager(MailBoxManager mailBoxManager) {
		this.mailBoxManager = mailBoxManager;
	}
	
	@Autowired
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	@Override
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:23:46 PM
	 * @desc: 邮箱删除模块
	 * @param str
	 * @param sign
	 * @return String
	 */
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		if(id!=null&&!"".equals(id)){
			ToaMailBox delObj=mailBoxManager.getObjById(id);
			if(delObj==null){
				return renderText("notexits");
			}else{
				try{
					mailBoxManager.delObj(delObj);
					return renderText("true");
				}catch(Exception e){
					logger.error(this, e);
					return renderText("false");
				}
			}
		}else{
			return renderText("noid");
		}
	}

	@Override
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:23:46 PM
	 * @desc: 邮箱录入模块
	 * @param str
	 * @param sign
	 * @return String
	 */
	public String input() throws Exception {
		// TODO Auto-generated method stub
		if("add".equals(type)){
			if("true".equals(model.getPop3Ssl())){
				model.setPop3Ssl("1");					//收取服务器需要进行SSL验证
			}else{
				model.setPop3Ssl("0");					//收取服务器不需要进行SSL验证
			}
			if("true".equals(model.getSmtpSsl())){
				model.setSmtpSsl("1");					//发送服务器需要进行SSL验证
			}else{
				model.setSmtpSsl("0");					//发送服务器不需要进行SSL验证
			}
			model.setUserId(userService.getCurrentUser().getUserId());
			try{
				Serializable reId=mailBoxManager.saveBySession(model);			//利用此方法返回保存后的邮箱对应的uuid
				ToaMailBox tempObj=mailBoxManager.getObjById(reId.toString());
				for(int i=1;i<=4;i++){
					ToaMailFolder folder=new ToaMailFolder();
					if(i==1){
						folder.setMailfolderName("收件箱");
					}else if(i==2){
						folder.setMailfolderName("发件箱");
					}else if(i==3){
						folder.setMailfolderName("草稿箱") ;
					}else{
						folder.setMailfolderName("垃圾箱");
					}
					folder.setMailfolderType(String.valueOf(i));
					folder.setToaMailBox(tempObj);
					mailFolderManager.save(folder);
				}
				return renderText("true");
			}catch(Exception e){
				logger.error(this, e);
				return renderText("false");
			}
		}else if("showinput".equals(type)){
			return INPUT;
		}
		return null;
	}
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:23:46 PM
	 * @desc: 根据字符对字符串进行从头到该字符的截取
	 * @param str
	 * @param sign
	 * @return String
	 */
	public String operStr(String str,String sign){
		try{
			return str.substring(0, str.indexOf(sign));
		}catch(Exception e){
			logger.error(this, e);
			return null;
		}
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:20:24 PM
	 * @desc: 对邮箱的保存和修改
	 * @return
	 * @throws Exception String
	 */
	public String edit() throws Exception{
		if("showedit".equals(type)){								//跳转到邮箱的修改页面			
			model=mailBoxManager.getObjById(id);
			//System.out.println(model.getPop3Server());
			return "edit";
		}else if("save".equals(type)){								//利用Ajax进行存储
			if(model.getMailboxId()!=null&&!"".equals(model.getMailboxId())){
				try{
					model.setUserId(userService.getCurrentUser().getUserId());
					if("true".equals(model.getPop3Ssl())){
						model.setPop3Ssl("1");						//收取服务需要进行SSL验证
					}else{
						model.setPop3Ssl("0");						//收取服务不需要进行SSL验证
					}
					if("true".equals(model.getSmtpSsl())){
						model.setSmtpSsl("1");						//发送服务需要进行SSL验证
					}else{
						model.setSmtpSsl("0");						//发送服务不需要进行SSL验证
					}
					mailBoxManager.saveObj(model);
					return renderText("true");
				}catch(Exception e){
					logger.error(this, e);
					return renderText("false");
				}
			}else{
				return renderText("noid");
			}
		}
		return null;
	}
	
	public String charge() throws Exception{
		if(mailBoxManager.chargeName(name)){
			return this.renderText("true");
		}else{
			return this.renderText("false");
		}
	}
	
	public String chargeNo() throws Exception{
		if(mailBoxManager.chargeNoChange(id, name)){
			return renderText("true");
		}else{
			return renderText("false");
		}
	}
	
	@Override
	/**
	 * @author：yuhz
	 * @time：Feb 17, 20092:20:24 PM
	 * @desc: 查询出邮箱列表
	 * @return
	 * @throws Exception String
	 */
	public String list() throws Exception {
		// TODO Auto-generated method stub
		mailBoxList=mailBoxManager.getAllList();
		for(int i=0;i<mailBoxList.size();i++){
			ToaMailBox mailBox=mailBoxList.get(i);
			Set<ToaMailFolder> set=mailBox.getToaMailFolders();
			Iterator iterator=set.iterator();
			while(iterator.hasNext()){
				ToaMailFolder folder=(ToaMailFolder)iterator.next();
				folder.setMailNum(String.valueOf(mailManager.getFolderNum(folder)));			//对邮箱中未读邮件进行设置
			}
		}
		return SUCCESS;
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

	public ToaMailBox getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public List<ToaMailBox> getMailBoxList() {
		return mailBoxList;
	}

	public String getServerType() {
		return serverType;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

}
