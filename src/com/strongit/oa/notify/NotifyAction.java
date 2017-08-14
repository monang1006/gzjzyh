/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理新闻公告action跳转类
 */
package com.strongit.oa.notify;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaAffiche;
import com.strongit.oa.bo.ToaAfficheAttach;
import com.strongit.oa.bo.ToaAfficheReceiver;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 处理新闻公告action跳转类
 * 
 * @author luosy
 * @version 1.0
 * @company      Strongit Ltd. (C) copyright
 * @date         May 11, 2012 3:38:26 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.notify.NotifyAction
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "notify.action", type = ServletActionRedirectResult.class) })
public class NotifyAction extends BaseActionSupport<ToaAffiche> {

	private Page<ToaAffiche> page = new Page<ToaAffiche>(FlexTableTag.MAX_ROWS, true);
	
	private String afficheId;

	private String inputType;
	
	private NotifyManager manager;

	private ToaAffiche model = new ToaAffiche();
	
	private ToaAfficheReceiver receiver = new ToaAfficheReceiver();

	private HashMap<String, String> typemap = new HashMap<String, String>();
	
	private DesktopSectionManager sectionManager;
	
	
	//收件人
	private String affReceiver;
	private String affReceiverId;	
	private String affReceiverName; //收件人姓名

	
	//附件
	private File[] file;
	private String[] fileFileName;

	private String attachId;
	private String attachFiles ;

	private Long defAttSize ;	//默认附件大小限制

	private String delAttIds;
	//栏目ID
	private String lanmuId;
	
	public NotifyAction() {
		typemap.put("1", "已发布");
		typemap.put("0", "未发布");
		typemap.put("2", "已过期");
	}
	
	public HashMap getTypemap() {
		return typemap;
	}

	public Page getPage() {
		return page;
	}

	public void setAfficheId(java.lang.String aAfficheId) {
		afficheId = aAfficheId;
	}

	public void setInputType(java.lang.String aInputType) {
		inputType = aInputType;
	}

	public String getInputType() {
		return inputType;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}
	

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}
	
	public String getAttachFiles() {
		return attachFiles;
	}

	public String getDelAttIds() {
		return delAttIds;
	}

	public void setDelAttIds(String delAttIds) {
		this.delAttIds = delAttIds;
	}

	@Autowired
	public void setManager(NotifyManager aManager) {
		manager = aManager;
	}
	
	@Autowired
	public void setSectionManager(DesktopSectionManager sectionManager) {
		this.sectionManager = sectionManager;
	}

	public ToaAffiche getModel() {
		return model;
	}
	
	public ToaAfficheReceiver getReceiver() {
		return receiver;
	}

	public void setReceiver(ToaAfficheReceiver receiver) {
		this.receiver = receiver;
	}

	@Override
	public String input() throws Exception {
		return null;
	}
	
	public void setLanmuId(String lanmuId) {
		this.lanmuId = lanmuId;
	}
	
	public String getLanmuId() {
		return lanmuId;
	}


	public Long getDefAttSize() {
		return defAttSize;
	}

	public void setDefAttSize(Long defAttSize) {
		this.defAttSize = defAttSize;
	}

	
	public String getAffReceiver() {
		return affReceiver;
	}

	public void setAffReceiver(String affReceiver) {
		this.affReceiver = affReceiver;
	}

	public String getAffReceiverId() {
		return affReceiverId;
	}

	public void setAffReceiverId(String affReceiverId) {
		this.affReceiverId = affReceiverId;
	}

	public String getAffReceiverName() {
		return affReceiverName;
	}

	public void setAffReceiverName(String affReceiverName) {
		this.affReceiverName = affReceiverName;
	}

	/**
	 * author:luosy
	 * description:获取公告列表
	 * modifyer:
	 * description:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
//		page = manager.getListByState(page ,inputType);
//		page = manager.getListByState(page, "");
		Calendar rightNow = Calendar.getInstance();
		page = manager.getList(page, model);
		List<ToaAffiche> result = new ArrayList();
		result = page.getResult();
		if(result != null && !result.isEmpty()) {
			for(int i = 0;i<result.size();i++){
			ToaAffiche toaAffiche = result.get(i);
			if(toaAffiche.getAfficheUsefulLife()!=null){
				if(toaAffiche.getAfficheUsefulLife().getTime()<rightNow.getTime().getTime()){
					toaAffiche.setAfficheState(ToaAffiche.NOTIFY_OVERDUE);
				}
			  }
			}
			//page=ListUtils.splitList2Page(page, result);
		}
		
		return "list";
	}
	
	/**
	 * author:qibh
	 * description:获取公告列表
	 * modifyer:
	 * description:
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String loginList() throws Exception {
		Calendar rightNow = Calendar.getInstance();
		Page<ToaAffiche> page1 = new Page<ToaAffiche>(100, true);
		page1 = manager.getList(page1, model);
		List<ToaAffiche> result = new ArrayList();
		result = page1.getResult();
		if(result != null && !result.isEmpty()) {
			for(int i = 0;i<result.size();i++){
			ToaAffiche toaAffiche = result.get(i);
			if(toaAffiche.getAfficheUsefulLife()!=null){
				if(toaAffiche.getAfficheUsefulLife().getTime()<rightNow.getTime().getTime()){
					toaAffiche.setAfficheState(ToaAffiche.NOTIFY_OVERDUE);
				}
			  }
			}
			page1=ListUtils.splitList2Page(page1, result);
		}
		model.setAfficheState("1");
		page = manager.search(page,model);
		List<ToaAffiche> result1 = new ArrayList<ToaAffiche>();
		result1 = page.getResult();
		List newList =new ArrayList<ToaAffiche>();
		if(result1 != null && !result1.isEmpty()) {
			for(int i = 0;i<result1.size();i++){
				ToaAffiche ta = result1.get(i);
				if("1".equals(ta.getViewAfterLogin())){
					newList.add(ta);
				}
				
			}
		}
		//page=ListUtils.splitList2Page(page, newList);
		String arr="";
		if(newList != null && newList.size()>0) {
			for(int j = 0;j<newList.size();j++){
				ToaAffiche aff= (ToaAffiche)newList.get(j);
				arr = arr+aff.getAfficheId()+",";
			}
			if(!"".equals(arr)&&arr.lastIndexOf(",")!=-1){
				arr=arr.substring(0, arr.length()-1);
			}
		}
		renderHtml(arr);
		return null;
	}


	/**
	 * author:luosy
	 * description:保存公告
	 * modifyer:
	 * description:
	 * @return
	 */
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		receiver = this.getReceiver();
		model.setAfficheState(inputType);
		if("alluser".equals(affReceiverId)){
			affReceiverId = "alluser";
			affReceiverName = "所有人";
		}
		
		if("".equals(affReceiverId)){
			affReceiverId = "alluser";
			affReceiverName = "所有人";
		}
		defAttSize = manager.getDefAttSize();
		long temp = 0;
		if(file!=null){
			for(int i=0;i<file.length;i++){
				temp += file[i].length();
				if(file[i].length() <= 0 ){
					addActionMessage("error不能上传内容为空的附件。");
					return edit();
				}
			}
			if(temp>defAttSize){
				addActionMessage("error不能上传附件，附件大小超出最大范围（"+defAttSize/1024/1024+"M）。");
				return edit();
			}
		}
		if(null!=delAttIds&&!"".equals(delAttIds)){
//			delAttIds = delAttIds.substring(0,delAttIds.length()-1);
			String[] ids = delAttIds.split(",");
			for(int i=0;i<ids.length;i++){
				manager.delAttach(ids[i]);
			}
		}
		model.getToaAfficheReceivers();
		if(model.getAfficheUsefulLife() != null){//时限时间默认为改天的最后一秒
			Calendar cal = Calendar.getInstance();
			cal.setTime(model.getAfficheUsefulLife());
			cal.set(Calendar.HOUR, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			model.setAfficheUsefulLife(cal.getTime());
		}
		manager.saveNotify(model,file,fileFileName, new OALogInfo("新闻公告-『保存』："+model.getAfficheTitle()));
		String recvUser = manager.formatUserId(affReceiverId);
		
		afficheId = model.getAfficheId();
		manager.deleteReceivers(model.getToaAfficheReceivers());
	    if(null!=recvUser&&!"".equals(recvUser)){
	    	String[] res = recvUser.split(",");
	    	for(int j=0;j<res.length;j++){
	    		ToaAfficheReceiver rece = new ToaAfficheReceiver();
	    		rece.setToaAffiche(model);
	    		rece.setAfficheReceiver(res[j]);
	    		rece.setAfficheReceiverId(manager.getUserIdsInFormat(res[j]));
	    		manager.saveAfficheRece(model, rece);
	    	}
	    }
		
		
		addActionMessage("success");
		return "add";
		
	}

	
	/**
	 * author:luosy
	 * description: 发布公告
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String publicNotify() throws Exception{
		try {
			model = manager.getNotifyById(afficheId);
			if(model!=null){
				if(model.getAfficheState().equals(ToaAffiche.NOTIFY_SENDED)){
					return renderText("sended");
				}
			}else{
				return renderText("notfind");
			}
			manager.publicNotify(afficheId, new OALogInfo("新闻公告-『发布』："+model.getAfficheTitle()));
			addActionMessage("发布成功");
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return renderText("reload");
	}
	
	/**
	 * author:luosy
	 * description:删除一个或多个公告
	 * modifyer:
	 * description:
	 * @return
	 */
	public String delete() throws Exception {
		try {
			if ("".equals(afficheId) | null == afficheId) {
			} else {
				String[] ids = afficheId.split(",");
				for (int i = 0; i < ids.length; i++) {
					ToaAffiche aff = manager.getNotifyById(ids[i]);
					manager.deleteNotify(ids[i], new OALogInfo("新闻公告-『删除』："+aff.getAfficheTitle()));
				}
			}
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	protected void prepareModel() throws Exception {
		if ("".equals(afficheId) | null == afficheId) {
			model = new ToaAffiche();
		} else {
			model = manager.getNotifyById(afficheId);
		}
	}

	/**
	 * author:luosy
	 * description:查看
	 * modifyer:
	 * description:
	 * @return
	 */
	public String view() throws Exception {
		model = manager.getNotifyById(afficheId);
		String afficheDesc = model.getAfficheDesc();
		afficheDesc = afficheDesc.replaceAll("&amp;", "&");
		afficheDesc = afficheDesc.replaceAll("&quot;", "\"");
		afficheDesc = afficheDesc.replaceAll("&lt;", "<");
		afficheDesc = afficheDesc.replaceAll("&gt;", ">");
		getRequest().setAttribute("desc", afficheDesc);
		if(null==model){
			return "view";
		}
		//获取附件
		Set att = model.getToaAfficheAttachs();
		Iterator it=att.iterator();
		attachFiles="";
		while (it.hasNext()) {
			ToaAfficheAttach objs = (ToaAfficheAttach) it.next();
			ToaAttachment attachment = manager.getToaAttachmentById(objs.getAttachId());
			attachFiles+="<div>&nbsp;&nbsp;&nbsp;&nbsp;<a target=\"downloadframe\" href=\"../notifyattach/notifyAttach!down.action?attachId="+attachment.getAttachId()+"\">"+attachment.getAttachName()+"</a></div>";
		}
		
		return "view";
	}
	/**
	 * author:qibh
	 * description:查看
	 * modifyer:
	 * description:
	 * @return
	 */
	public String viewLogin() throws Exception {
		model = manager.getNotifyById(afficheId);
		
		if(null==model){
			return "viewlogin";
		}
		//获取附件
		Set att = model.getToaAfficheAttachs();
		Iterator it=att.iterator();
		attachFiles="";
		while (it.hasNext()) {
			ToaAfficheAttach objs = (ToaAfficheAttach) it.next();
			ToaAttachment attachment = manager.getToaAttachmentById(objs.getAttachId());
			attachFiles+="<div>&nbsp;&nbsp;&nbsp;&nbsp;<a target=\"downloadframe\" href=\"../notifyattach/notifyAttach!down.action?attachId="+attachment.getAttachId()+"\">"+attachment.getAttachName()+"</a></div>";
		}
		
		return "viewlogin";
	}
	/**
	 * author:luosy
	 * description:跳转到添加的页面
	 * modifyer:
	 * description:
	 * @return
	 */
	public String add() throws Exception {
		defAttSize = manager.getDefAttSize();
		model.setAfficheTime(new Date());
		model.setAfficheUsefulLife(new Date());
		return "add";
	}

	/**
	 * author:luosy
	 * description:编辑功能 
	 * modifyer:
	 * description:
	 * @return
	 */
	public String edit() throws Exception {
		if ("".equals(afficheId) | null == afficheId) {
		} else {
			model = manager.getNotifyById(afficheId);
			List<ToaAfficheReceiver> reList = manager.getAfficheReceiver(afficheId);
			if(reList!=null){
				Iterator it=reList.iterator();
				affReceiverName = "";
				StringBuffer affReceiverNames = new StringBuffer();
				StringBuffer affReceiverIds = new StringBuffer();
				while(it.hasNext()){
					ToaAfficheReceiver re = (ToaAfficheReceiver) it.next();
					affReceiver = re.getAfficheReceiver();
					affReceiverId = re.getAfficheReceiverId();
					if("alluser".equals(affReceiverId)){
						affReceiverNames.append("所有人");
						affReceiverNames.append(",");
					}else{
						affReceiverName = affReceiver.replaceAll("<id:\\w{32}>", "");
						affReceiverNames.append(affReceiverName);
						affReceiverNames.append(",");
						affReceiverIds.append(affReceiverId);
						affReceiverIds.append(",");
						
					}
					affReceiverName = affReceiverNames.toString();
					affReceiverId = affReceiverIds.toString();
					if (!"".equals(affReceiverName) && affReceiverName != null) {// 截取去掉最后一个，
						affReceiverName = affReceiverName.substring(0,
								affReceiverName.length() - 1);
					}
					if (!"".equals(affReceiverId) && affReceiverId != null) {// 截取去掉最后一个，
						affReceiverId = affReceiverId.substring(0,
								affReceiverId.length() - 1);
					}
					
					}
				}
		}
		
		/*affReceiverNames=receiver.getAfficheReceiver();//获取收件人
		if(!"".equals(affReceiverNames)&&null!=affReceiverNames){
			affReceiverIds = manager.getUserIdsInFormat(affReceiverNames);
			affReceiverNames = affReceiverNames.replaceAll("<id:\\w{32}>", "");
		}*/
		defAttSize = manager.getDefAttSize();
//		获取附件
		Set att = model.getToaAfficheAttachs();
		if(att!=null){
			Iterator it=att.iterator();
			attachFiles="";
			while (it.hasNext()) {
				ToaAfficheAttach objs = (ToaAfficheAttach) it.next();
				ToaAttachment attachment = manager.getToaAttachmentById(objs.getAttachId());
				attachFiles+="<div id=\"att"+attachment.getAttachId()+"\"><a class=\"button\" href=\"javascript:delAttach('"+attachment.getAttachId()+"')\">删除<a><a href=\"../notify/notifyAttach!down.action?attachId="
					+attachment.getAttachId()+"\">"+attachment.getAttachName()+"</a>&nbsp;</div>";
			}
		}
		return "add";
	}

	/**
	 * author:luosy
	 * description: 搜索功能
	 * modifyer:
	 * description:
	 * @return
	 */
	public String search() throws Exception {
		return "search";
	}

	/**
	 * author:luosy
	 * description: 删除附件
	 * modifyer:
	 * description:
	 * @return
	 */
	public String delAttach() throws Exception {
		manager.delAttach(attachId);
		ToaAffiche aff = manager.getNotifyById(afficheId);
		Set s = aff.getToaAfficheAttachs();
		model.setToaAfficheAttachs(s);
				
		return edit();
	}
	/**
	 * @return java.lang.String
	 */
	public String getsearchlist() throws Exception {

		Calendar rightNow = Calendar.getInstance();
		try {
			if(model.getAfficheTitle()!=null&&!"".equals(model.getAfficheTitle())){
				model.setAfficheTitle(URLDecoder.decode(model.getAfficheTitle(), "utf-8"));
			}
			if(model.getAfficheGov()!=null&&!"".equals(model.getAfficheGov())){
				model.setAfficheGov(URLDecoder.decode(model.getAfficheGov(), "utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		page = manager.search(page, model);
		List<ToaAffiche> result = new ArrayList();
		result = page.getResult();
		if(result != null && !result.isEmpty()) {
			for(int i = 0;i<result.size();i++){
			ToaAffiche toaAffiche = result.get(i);
			if(toaAffiche.getAfficheUsefulLife()!=null){
				if(toaAffiche.getAfficheUsefulLife().getTime()<rightNow.getTime().getTime()){
					toaAffiche.setAfficheState(ToaAffiche.NOTIFY_OVERDUE);
				}
			  }
			}
			//page=ListUtils.splitList2Page(page, result);
		}
		return "list";
	}

	/**
	 * 获取当前用户发布的公告列表
	 * @return java.lang.String
	 */
	@SuppressWarnings("unchecked")
	public String mylist() throws Exception {
		try {
			if(model.getAfficheTitle()!=null&&!"".equals(model.getAfficheTitle())){
				model.setAfficheTitle(URLDecoder.decode(model.getAfficheTitle(), "utf-8"));
			}
			if(model.getAfficheGov()!=null&&!"".equals(model.getAfficheGov())){
				model.setAfficheGov(URLDecoder.decode(model.getAfficheGov(), "utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		Calendar rightNow = Calendar.getInstance();
		page = manager.getMyList(page, model);
		List<ToaAffiche> result = new ArrayList();
		result = page.getResult();
		if(result != null && !result.isEmpty()) {
			for(int i = 0;i<result.size();i++){
			ToaAffiche toaAffiche = result.get(i);
			if(toaAffiche.getAfficheUsefulLife()!=null){
				if(toaAffiche.getAfficheUsefulLife().getTime()<rightNow.getTime().getTime()){
					toaAffiche.setAfficheState(ToaAffiche.NOTIFY_OVERDUE);
				}
			  }
			}
			//page=ListUtils.splitList2Page(page, result);
		}
		return "mylist";
	}
	
	/**
	 * author:luosy
	 * description:获取栏目信息
	 * modifyer:
	 * description:
	 * @return
	 */
	public String getLanmuList()throws Exception, AjaxException {
		JSONArray jArray = new JSONArray();
		try{
			List lst = manager.getLanmuList();
			jArray = JSONArray.fromObject(lst);
		}catch (Exception e) {
			renderText("获取栏目信息出错");
			return null;
		}
		renderText(jArray.toString());
		return null;
	}
	
	/**
	 * author:luosy
	 * description: 将公告发布到Ipp
	 * modifyer:
	 * description:
	 * @return
	 */
	public String SendToIpp()throws Exception {
		try{
			String ret = manager.sendTOIpp(afficheId,lanmuId, new OALogInfo("新闻公告-『发布到IPP』："+afficheId+"lanmuId"));
			renderText(ret);
		}catch (Exception e) {
			return renderText("不能发布信息");
		}
		return null;
	}
	
	
	/**
	 * author:luosy
	 * description: 桌面显示
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		
		String blockid = getRequest().getParameter("blockid");//获取blockid
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum"); 
		String showCreator =getRequest().getParameter("showCreator"); 
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockid != null){
			Map<String,String> map = sectionManager.getParam(blockid);//通过blockid获取映射对象
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			showCreator = map.get("showCreator");//是否显示作者
			showDate = map.get("showDate");//是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		if(sectionFontSize == null || "".equals("sectionFontSize") || "null".equals("sectionFontSize") ){
			sectionFontSize = "12";
		}
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		//		链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/notify/notify.action")
			.append("', '公告栏'")
			.append(");");
		
//		获取公告列表list
		List<ToaAffiche> list = null;
		page.setPageSize(num);
		//page = manager.getListForTable(user.getCurrentUser().getUserId(),page);
		Calendar rightNow = Calendar.getInstance();
		page = manager.getList(page, model);
		List<ToaAffiche> result = new ArrayList();
		result = page.getResult();
		if(result != null && !result.isEmpty()) {
			for(int i = 0;i<result.size();i++){
			ToaAffiche toaAffiche = result.get(i);
				if(toaAffiche.getAfficheUsefulLife()!=null){
					if(toaAffiche.getAfficheUsefulLife().getTime()<rightNow.getTime().getTime()){
						toaAffiche.setAfficheState(ToaAffiche.NOTIFY_OVERDUE);
						result.remove(i);//过期的新闻公告在桌面不显示
						i--;
					}
				}
			}
			page=ListUtils.splitList2Page(page, result);
		}
		model.setAfficheState("1");
		page = manager.search(page,model);
		list = page.getResult();
		
		if(list!=null){
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				ToaAffiche notify = list.get(i);
				//标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
				.append("/notify/notify!view.action?afficheId="+notify.getAfficheId())
				.append("', '查看公告'")
				.append(");");
				
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				if(notify.getToaAfficheAttachs().size()>0)//如果为有附件，则显示已读图片，否则显示未读图片
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				else
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				String title = notify.getAfficheTitle();
				if(title==null){
					title="";
				}
				
				StringBuilder tip = new StringBuilder();
				tip.append(notify.getAfficheTitle())
					.append("\n").append("发布人：" + notify.getAfficheAuthor() )
					.append("\n").append("发布部门：" + notify.getAfficheGov())
					.append("\n").append("发布时间：" + st.format(notify.getAfficheTime()))
					.append("\n").append("有效期限：" + st.format(notify.getAfficheUsefulLife()));
				
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<a href=\"#\" onclick=\"").append(titleLink.toString()).append("\">")
				.append("<span style=\"font-size: "+sectionFontSize+"px;color:"+notify.getAfficheTitleColour()+"\" title=\"").append(tip).append("\">").append(title).append("</span></a>");
				innerHtml.append("</td>");
				if("1".equals(showCreator)){//如果设置为显示作者，则显示作者信息 
					innerHtml.append("<td width=\"80px\">");
					if(notify.getAfficheAuthor()==null){
						//innerHtml.append("	<span class =\"linkgray\">").append("").append("</span>");
						innerHtml.append("");
					}else{
						innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" class =\"linkgray\">").append(notify.getAfficheAuthor()).append("</span>");
						//innerHtml.append(notify.getAfficheAuthor());
					}
					innerHtml.append("</td>");
				}
				if("1".equals(showDate)){//如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" title =\""+new SimpleDateFormat("yyyy-MM-dd hh:mm").format(notify.getAfficheTime())+"\" class =\"linkgray10\">").append(st.format(notify.getAfficheTime())).append("</span>");
					innerHtml.append("</td>");
				}
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		if(list==null){
			for (int i = 0; i < num; i++) { // 获取在条数范围内的列表
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml
				.append("	&nbsp;");
				innerHtml.append("</td>");
				innerHtml.append("</tr>");
				innerHtml.append("	</table>");
				}
		}
		if(list!=null&&list.size()<num){
			for (int i = 0; i < num-list.size() ; i++) { // 获取在条数范围内的列表
			innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
			innerHtml.append("<tr>");
			innerHtml.append("<td>");
			innerHtml
			.append("	&nbsp;");
			innerHtml.append("</td>");
			innerHtml.append("</tr>");
			innerHtml.append("	</table>");
			}
		}
		innerHtml.append("<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ")
				 .append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}
	
	/**
	* @description 
	* @method showDesktop
	* @author 申仪玲(shenyl)
	* @created 2012-5-31 下午03:51:00
	* @return
	* @throws Exception
	* @return String
	* @throws Exception
	*/
	public String showTableDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		
		String blockid = getRequest().getParameter("blockid");//获取blockid
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum"); 
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockid != null){
			Map<String,String> map = sectionManager.getParam(blockid);//通过blockid获取映射对象
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			showCreator = map.get("showCreator");//是否显示作者
			showDate = map.get("showDate");//是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		if(sectionFontSize == null || "".equals("sectionFontSize") || "null".equals("sectionFontSize") ){
			sectionFontSize = "14";
		}
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		//		链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/notify/notify.action")
			.append("', '公告栏'")
			.append(");");
		
//		获取公告列表list
		List<ToaAffiche> list = null;
		page.setPageSize(num);
		//page = manager.getListForTable(user.getCurrentUser().getUserId(),page);
		Calendar rightNow = Calendar.getInstance();
		page = manager.getList(page, model);
		List<ToaAffiche> result = new ArrayList();
		result = page.getResult();
		if(result != null && !result.isEmpty()) {
			for(int i = 0;i<result.size();i++){
			ToaAffiche toaAffiche = result.get(i);
				if(toaAffiche.getAfficheUsefulLife()!=null){
					if(toaAffiche.getAfficheUsefulLife().getTime()<rightNow.getTime().getTime()){
						toaAffiche.setAfficheState(ToaAffiche.NOTIFY_OVERDUE);
						result.remove(i);//过期的新闻公告在桌面不显示
						i--;
					}
				}
				if(toaAffiche.getAfficheTime()!=null){
					if(toaAffiche.getAfficheTime().getTime()>rightNow.getTime().getTime()){
						toaAffiche.setAfficheState(ToaAffiche.NOTIFY_SENDED);
						result.remove(i);//生效日期未到的新闻公告在桌面不显示
						i--;
					}
				}
			}
			page=ListUtils.splitList2Page(page, result);
		}
		//model.setAfficheState("1");
		//page = manager.search(page,model);
		list = page.getResult();
		
		if(list!=null){
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				ToaAffiche notify = list.get(i);
				//标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript:refreshWorkByTitle('").append(getRequest().getContextPath())
				.append("/notify/notify!view.action?afficheId="+notify.getAfficheId())
				.append("', '查看公告'")
				.append(");");
				
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				if(notify.getToaAfficheAttachs().size()>0)//如果为有附件，则显示已读图片，否则显示未读图片
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				else
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				String title = notify.getAfficheTitle();
				if(title==null){
					title="";
				}
				
				StringBuilder tip = new StringBuilder();
				tip.append(notify.getAfficheTitle())
					.append("\n").append("发布人：" + notify.getAfficheAuthor() )
					.append("\n").append("发布部门：" + notify.getAfficheGov())
					.append("\n").append("生效日期：" + st.format(notify.getAfficheTime()))
					.append("\n").append("失效日期：" + st.format(notify.getAfficheUsefulLife()));
				
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<a href=\"#\" onclick=\"").append(titleLink.toString()).append("\">")
				.append("<span style=\"font-size: "+sectionFontSize+"px;color:"+notify.getAfficheTitleColour()+"\" title=\"").append(tip).append("\">").append(title).append("</span></a>");
				innerHtml.append("</td>");
				if("1".equals(showCreator)){//如果设置为显示作者，则显示作者信息 
					innerHtml.append("<td width=\"115px\">");
					if(notify.getAfficheAuthor()==null){
						//innerHtml.append("	<span class =\"linkgray\">").append("").append("</span>");
						innerHtml.append("");
					}else{
						innerHtml.append("<span class =\"linkgray\">").append(notify.getAfficheAuthor()).append("</span>&nbsp;&nbsp;&nbsp;");
						//innerHtml.append(notify.getAfficheAuthor());
					}
					innerHtml.append("</td>");
				}
				if("1".equals(showDate)){//如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("<span class =\"linkgray10\">").append(st.format(notify.getAfficheTime())).append("</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					innerHtml.append("</td>");
				}
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}
	
	//获取当前日期是前一天和前两天
	 public String orderDate(){
		   Date nowdate = new Date();
		   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
           sdf.format(nowdate);
           Calendar c=sdf.getCalendar();
           //当前日期的前一天
           c.add(Calendar.DATE, -1);
           //格式化当前日期的前一天
           String lastDay=sdf.format(c.getTime());
           //当前日期的前两天
           Calendar ca=sdf.getCalendar();
           ca.add(Calendar.DATE, -1);
           //格式化当前日期的前两天
           String lastTowDay=sdf.format(ca.getTime());
           return renderHtml(lastDay+";"+lastTowDay);
	 }
	

}
