/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-4-25
 * Autour: hull
 * Version: V1.0
 * Description： 个人收藏夹action
 */
package com.strongit.oa.knowledge.mykm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaInfopublishArticle;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaKnowledgeMykm;
import com.strongit.oa.bo.ToaKnowledgeMykmSort;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.im.IMManager;
import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongit.oa.knowledge.mykmsort.MykmSortManage;
import com.strongit.oa.message.IMessageService;
import com.strongit.oa.mymail.IMailService;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/knowledge/mykm/mykm.action", type = ServletActionRedirectResult.class) })
public class MykmAction extends BaseActionSupport {

	private Page<ToaKnowledgeMykm> page = new Page<ToaKnowledgeMykm>(FlexTableTag.MAX_ROWS, true);

	private ToaKnowledgeMykm model = new ToaKnowledgeMykm();// 知识收藏BO

	private MykmManage mykmManage;// 知识收藏manager

	private String mykmId;// 知识收藏ID

	private String mykmName;// 知识收藏名称

	private List<ToaKnowledgeMykm> mykmList;// 知识收藏列表

	private String mykmSortId;// 知识类型ID

	private MykmSortManage SortManage;// 知识类型manager

	private List<ToaKnowledgeMykmSort> sortList;// 知识类型列表

	private IUserService userservice;// 用户接口

	private String mykmUrl;// 知识路径

	private String articleId;// 文章ID

	private ArticlesManager articleManager;// 文章manager

	private IMailService mailService;// 发送邮件接口

	private IMessageService messageService;// 发送消息接口

	private List<String> list;// 分享人员列表

	private String recvUserIds;// 接收分享用户ID

	private String receiverNames;// 接收分享用户名称

	private String mytitle;// 分享信息描述

	private String mode;// 信息发送方式

	private String columnArticleId;// 上拦ID

	private IMManager immanager;// RTX

	private String content;// 文章内容
	
	private static final int CONTENTSLENGTH=300;//内容长度 

	private static final String MESSAGE = "MSG";// 内部消息

	private static final String MAIL = "MAIL";// 邮件方式

	private static final String RTX = "RTX";// RTX方式
	
	private String type;//区别是添加还是收藏知识
	
	private ToaKnowledgeMykmSort mykmsort;//信息类型

	
	public ToaKnowledgeMykmSort getMykmsort() {
		return mykmsort;
	}

	public void setMykmsort(ToaKnowledgeMykmSort mykmsort) {
		this.mykmsort = mykmsort;
	}

	/**
	 * 删除
	 * 
	 * @return java.lang.String
	 * @throws Exception
	 */
	@Override
	public String delete() throws Exception {
		List<String> mykmids = stringToArray(mykmId);
		List<ToaKnowledgeMykm> mykms = new ArrayList<ToaKnowledgeMykm>();// 知识列表
		for (String id : mykmids)//
		{
			mykms.add(mykmManage.getMykmByID(id));// 添加到列表里
		}
		mykmManage.deleteMykm(mykms);
		return renderHtml("<script>window.location='"
				+ getRequest().getContextPath()
				+ "/knowledge/mykm/mykm.action'; </script>");
	}

	/**
	 * 添加和修改初始化
	 * 
	 * @return java.lang.String
	 * @throws Exception
	 */
	@Override
	public String input() throws Exception {
		prepareModel();

		if (columnArticleId != null)// 是否为空
		{
			ToaInfopublishColumnArticl ca = articleManager
					.getColumnArticl(columnArticleId);
			articleId = ca.getToaInfopublishArticle().getArticlesId();

			if (!"".equals(articleId) && articleId != null) {// 文章是否为空
				ToaInfopublishArticle article = articleManager
						.getArticle(articleId);// 获取文章
				if (article.getArticlesIsredirect()!=null&&article.getArticlesIsredirect().equals("1"))// 判断是否是外部链接
				{
					model.setMykmUrl(article.getArticlesRedirecturl());
				} else {
					model.setMykmUrl(mykmUrl);
				}
				model.setMykmAuthor(article.getArticlesAuthor());
				model.setMykmName(article.getArticlesTitle());
			}
		}
		String mykmUser = getUserid();// 获取当前用户ID
		if (!mykmUser.equals("") && mykmUser != null) {// 判断用户是否登入
			sortList = SortManage.getMykmSortListByUser(mykmUser);
		}
		return INPUT;
	}

	/**
	 * 知识分享
	 * 
	 * @return java.lang.String
	 * @throws Exception
	 */
	public String shore() throws Exception {
		prepareModel();
		if (model.getMykmId() == null || "".equals(model.getMykmId())) {// 判断是否是个人收藏夹里的知识
			if (columnArticleId != null)// 判断上拦ID是否为空
			{
				ToaInfopublishColumnArticl ca = articleManager
						.getColumnArticl(columnArticleId);
				articleId = ca.getToaInfopublishArticle().getArticlesId();// 获取文章ID
				if (!"".equals(articleId) && articleId != null) {// 判断ID是否为空
					ToaInfopublishArticle article = articleManager
							.getArticle(articleId);// 根据ID获取文章
					if (article.getArticlesRedirecturl() == null
							|| "http://".equals(article
									.getArticlesRedirecturl()))// 判断是否是外部链接
					{
						model.setMykmUrl(mykmUrl);
					} else {
						model.setMykmUrl(article.getArticlesRedirecturl());
					}
				} else {
					model.setMykmUrl(mykmUrl);
				}

				model.setMykmUser(getUserid());
			}
		}
		return "shore";
	}

	/**
	 * 发送分享
	 * 
	 * @return java.lang.String
	 * @throws Exception
	 */
	public String saveShore() throws Exception {
		content = "";// 清空内容
		if(mykmUrl==null){
			mykmUrl=model.getMykmUrl();
		}
			
		columnArticleId = mykmUrl.substring(mykmUrl.indexOf("=") + 1);// 获取上拦ID
		ToaInfopublishColumnArticl ca = articleManager
		.getColumnArticl(columnArticleId);
		// 判断上拦稿件是否为空
		if (ca != null) {
			content = ca.getToaInfopublishArticle().getArticlesArticlecontent();
		}
		if (content!=null&&content.length() > CONTENTSLENGTH) {//判断内容是否多于300字
			content = content.substring(0, CONTENTSLENGTH);
		}
		list = stringToArray(recvUserIds);// 获取分享人员的ID
		List<String> modeList=stringToArray(mode);
		String url = getRequest().getContextPath();
		
		//发送到内部信息（默认发送方式）
		if(mode==null||"".equals(mode)){
			if(model.getMykmUrl().substring(0,4).equals("http"))//是否是外部连接
			{
				messageService.sendMsg("person", list, mytitle, "<a href='" 
						+ model.getMykmUrl() + "'>连接</a><br>" + content + "......",GlobalBaseData.MSG_KNOWLEDGE);
			}else{
				messageService.sendMsg("person", list, mytitle, "<a href='" + url
						+ model.getMykmUrl() + "'>连接</a><br>" + content + "......",GlobalBaseData.MSG_KNOWLEDGE);
			}
		}
		for(String sendmode:modeList){
		
			if (sendmode == MAIL || MAIL.equals(sendmode))// 邮件方式
			{
				mailService.autoSendMail(list, mytitle, content + "......",
				"person");
			}else if (sendmode == MESSAGE || MESSAGE.equals(sendmode))// 内部消息方式
			{
				if(model.getMykmUrl().substring(0,4).equals("http"))//是否是外部连接
				{
					messageService.sendMsg("person", list, mytitle, "<a href='" 
							+ model.getMykmUrl() + "'>连接</a><br>" + content + "......",GlobalBaseData.MSG_KNOWLEDGE);
				}else{
					messageService.sendMsg("person", list, mytitle, "<a href='" + url
							+ model.getMykmUrl() + "'>连接</a><br>" + content + "......",GlobalBaseData.MSG_KNOWLEDGE);
				}
				
			}
		
			if (sendmode == RTX || RTX.equals(sendmode))// RTX方式
			{
				String userids = "";

				// 过滤掉当前用户
				for (int i = 0; i < list.size(); i++) {
					if (!list.get(i).equals(getUserid())) {
						userids = userids + list.get(i) + ",";
					}
				}
				if (userids.equals("")) {//用户ID是否为空
				
					  StringBuffer returnhtml = new StringBuffer(
						"<script> var scriptroot = '")
						.append(getRequest().getContextPath())
						.append("'</script>")
						.append("<SCRIPT src='")
						.append(getRequest().getContextPath())
						.append(
								"/common/js/commontab/workservice.js'>")
						.append("</SCRIPT>")
						.append("<SCRIPT src='")
						.append(getRequest().getContextPath())
						.append(
								"/common/js/commontab/service.js'>")
						.append("</SCRIPT>")
						.append("<script>")
						.append("alert('发送失败！');window.close();</script>");
				//	return renderHtml("<script>alert('发送失败！');window.close(); </script>");
					return renderHtml(returnhtml.toString());
				}
				try {
					immanager.sendIM(content + "......", userids,null);
				} catch (Exception e) {
				
					StringBuffer returnhtml = new StringBuffer(
							"<script> var scriptroot = '")
							.append(getRequest().getContextPath())
							.append("'</script>")
							.append("<SCRIPT src='")
							.append(getRequest().getContextPath())
							.append(
									"/common/js/commontab/workservice.js'>")
							.append("</SCRIPT>")
							.append("<SCRIPT src='")
							.append(getRequest().getContextPath())
							.append(
									"/common/js/commontab/service.js'>")
							.append("</SCRIPT>").append("<script>").append("alert('RTX发送失败！请不要发送给自己！');window.close();</script>");
				
					return renderHtml(returnhtml.toString());

				}
			
			}
		}
		return renderHtml("<script>window.close(); </script>");
	}

	/**
	 * 把字符串拆分成集合
	 * 
	 * @param temp
	 * @return
	 */
	private List<String> stringToArray(String temp) {
		String[] shore = temp.split(",");
		List<String> list1 = new ArrayList<String>();
		for (int i = 0; i < shore.length; i++) {
			list1.add(shore[i]);
		}
		return list1;
	}

	/**
	 * 获取知识收藏列表
	 * 
	 * @return java.lang.String
	 * @throws Exception
	 */
	@Override
	public String list() throws Exception {
		String mykmUser = getUserid();
		if (!mykmUser.equals("") && mykmUser != null) {// 判断用户是否登入
			sortList = SortManage.getMykmSortListByUser(mykmUser);// 根据用户获取收藏类型列表
			ToaKnowledgeMykmSort sort=new ToaKnowledgeMykmSort();
			sort.setMykmSortName("全部类型");
			sortList.add(0,sort);

			page = mykmManage.getMykmByProUser(page, mykmName, mykmUser,mykmSortId);
			if (mykmSortId != null && !mykmSortId.equals("")) {// 判断知识类型是否为空
				mykmsort=SortManage.getMykmSortByID(mykmSortId);
			}


		}
		return SUCCESS;
	}

	/**
	 * 返回一个集合
	 * 
	 * @param list
	 * @return
	 */
	protected List<ToaKnowledgeMykm> getList(List<ToaKnowledgeMykm> list) {
		List<ToaKnowledgeMykm> mylist = new ArrayList<ToaKnowledgeMykm>();

		return mylist;
	}

	/**
	 * 显示收藏信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String show() throws Exception {
		prepareModel();
//		if (model.getMykmUrl() != null) {
//			if (model.getMykmUrl().substring(0, 4).equals("http")||model.getMykmUrl().substring(0, 3).equals("www")) {// 判断是否是外部链接
//
//				return renderHtml("<script>window.location='"
//						+ model.getMykmUrl() + " '; </script>");
//			}
//		}
//		return renderHtml("<script>window.location='"
//				+ getRequest().getContextPath() + model.getMykmUrl()
//				+ " '; </script>");
		
		if (model.getMykmUrl() != null) {
			if (model.getMykmUrl().substring(0, 4).equals("http")||model.getMykmUrl().substring(0, 3).equals("www")) {// 判断是否是外部链接
				mykmUrl = model.getMykmUrl();
				return "show";
			}
		}
		mykmUrl = getRequest().getContextPath() + model.getMykmUrl();
		
		return "show";
	}

	/**
	 * 初始化model
	 */
	@Override
	protected void prepareModel() throws Exception {
		if (mykmId == null || mykmId.equals("")) {/*判断ID是否为空*/
			model = new ToaKnowledgeMykm();
			model.setMykmUser(getUserid());
		} else {
			model = mykmManage.getMykmByID(mykmId);
		}

	}

	/**
	 * 保存信息
	 * 
	 * @return java.lang.String
	 * @throws Exception
	 */
	@Override
	public String save() throws Exception {

		if ("".equals(model.getMykmId())) {/*判断model对象ID是否为空字符串*/
			model.setMykmId(null);
		}else if(model.getMykmId()!=null&&mykmId==null){
			mykmId=model.getMykmId();
		}
		if (model.getMykmUrl() == null) {
			model.setMykmUrl(mykmUrl);
		}
		mykmManage.saveMykm(model);
		if (mykmId != null && !mykmId.equals("")) {
			return renderHtml("<script> window.dialogArguments.parent.document.location.reload();window.close();</script>");
		//	return renderHtml("<script> window.parent.location.reload();window.close();</script>");
		
		}
		if (type!=null&&!"".equals(type)) {
			return renderHtml("<script> window.dialogArguments.parent.document.location.reload();window.close();</script>");
		//	return renderHtml("<script> window.parent.location.reload();window.close();</script>");
		
		}
		return renderHtml("<script>window.close();</script>");

	}

	/****	 * 获取当前用户名
	 * 
	 * @return java.lang.String
	 */
	private String getUserid() {
		String userid = userservice.getCurrentUser().getUserId();
		return userid;
	}

	@Autowired
	public void setImmanager(IMManager immanager) {
		this.immanager = immanager;
	}

	public String getColumnArticleId() {
		return columnArticleId;
	}

	public void setColumnArticleId(String columnArticleId) {
		this.columnArticleId = columnArticleId;
	}

	public ToaKnowledgeMykm getModel() {
		return model;
	}

	public String getMykmId() {
		return mykmId;
	}

	public void setMykmId(String mykmId) {
		this.mykmId = mykmId;
	}

	public List<ToaKnowledgeMykm> getMykmList() {
		return mykmList;
	}

	public void setMykmList(List<ToaKnowledgeMykm> mykmList) {
		this.mykmList = mykmList;
	}

	public String getMykmSortId() {
		return mykmSortId;
	}

	public void setMykmSortId(String mykmSortId) {
		this.mykmSortId = mykmSortId;
	}

	@Autowired
	public void setMykmManage(MykmManage mykmManage) {
		this.mykmManage = mykmManage;
	}

	public String getMykmName() {
		return mykmName;
	}

	public void setMykmName(String mykmName) {
		this.mykmName = mykmName;
	}

	public String getMykmUrl() {
		return mykmUrl;
	}

	public void setMykmUrl(String mykmUrl) {
		this.mykmUrl = mykmUrl;
	}

	public Page<ToaKnowledgeMykm> getPage() {
		return page;
	}

	public void setPage(Page<ToaKnowledgeMykm> page) {
		this.page = page;
	}

	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}

	public List<ToaKnowledgeMykmSort> getSortList() {
		return sortList;
	}

	public void setSortList(List<ToaKnowledgeMykmSort> sortList) {
		this.sortList = sortList;
	}

	@Autowired
	public void setSortManage(MykmSortManage sortManage) {
		SortManage = sortManage;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	@Autowired
	public void setArticleManager(ArticlesManager articleManager) {
		this.articleManager = articleManager;
	}

	@Autowired
	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	public String getMytitle() {
		return mytitle;
	}

	public void setMytitle(String mytitle) {
		this.mytitle = mytitle;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@Autowired
	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}

	public String getRecvUserIds() {
		return recvUserIds;
	}

	public void setRecvUserIds(String recvUserIds) {
		this.recvUserIds = recvUserIds;
	}

	public String getReceiverNames() {
		return receiverNames;
	}

	public void setReceiverNames(String receiverNames) {
		this.receiverNames = receiverNames;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
