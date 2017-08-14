/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: qindh
 * Version: V1.0
 * Description： 信息发布action
 */
package com.strongit.oa.infopub.articles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.bo.ToaInfopublishArticle;
import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaInfopublishComment;
import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.parameter.OtherParameter;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.infopub.column.ColumnManager;
import com.strongit.oa.knowledge.mykm.MykmManage;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @company Strongit Ltd. (C) copyright
 * @date May 11, 2012 4:09:39 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.infopub.articles.ArticlesAction
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/infopub/articles/articles!reclist.action", type = ServletActionRedirectResult.class) })
public class ArticlesAction extends AbstractBaseWorkflowAction<ToaInfopublishArticle> {

	@Autowired
	ArticlesManager manager;

	private int pagenow;// 当前页
	private int pagecontent;// 当前内容页
	private String isactive;// 是否是管理员
	private String commentid;// 评论ID
	private Page<ToaInfopublishArticle> page = new Page<ToaInfopublishArticle>(
			FlexTableTag.MAX_ROWS, true);
	private Page<ToaInfopublishColumnArticl> articlpage = new Page<ToaInfopublishColumnArticl>(
			FlexTableTag.MAX_ROWS, true);
	private String articlesId;// 稿件ID
	private String columnId;// 栏目ID
	private String treeType;// 树显示类型
	private String columnArticleId;// 上栏稿件ID
	private String showtype;// 个人桌面新闻展现方式
	private String commentText;// 评论内容
	private String commentName;// 评论名称
	private String disLogo;// 标示搜索属性
	private int commentNum;// 评论数
	private Boolean likeBoolean;// 外部连接
	private Boolean daoduBoolean;// 导读
	private Boolean redianBoolean;// 热点
	private Boolean gudingBoolean;// 固顶
	private Boolean yunxuprBoolean;// 允许评论
	private Boolean cuntuBoolean;// 存图
	private Boolean xiansBoolean;// 显示
	private File[] file;// 上传文件
	private String fileName;// 文件名称
	private String[] hasChild; // 子类别
	private List<ToaInfopublishColumn> columnList;// 栏目列表
	private List<ToaInfopublishComment> commentsList;// 评论list
	private ArticlesManager articlesManager;// 稿件Manager
	private ColumnManager columnManager;// 文件Manager
	private DesktopSectionManager dsmanager;// 桌面Manager
	IUserService userService;// 用户IUserService
	private Map ArcticlesTypeMap = new HashMap(); // 文件类型MAP
	private ToaInfopublishArticle model = new ToaInfopublishArticle();// 稿件BO
	private ToaInfopublishColumnArticl ca = null;// 已上栏稿件BO
	private static final String YES = "1";// 是
	private static final String NO = "0";// 否
	private static final String SHOWTREE = "2";// 查看
	private static final String ARCTICLENOTYPE = "1";// 稿件未上栏ArcticlesNoCType
	private static final String ARCTICLEISPUB = "3";// 审核中状态
	private static final String ARCTICLEISTRUEPUB = "9";// 发布状态
	private static final String SHOWTYEP = "1";// 显示方式,图文混排
	private static final String SESHOWTYEP = "2";// 显示方式,图片
	private static final Integer SHOWCOMMENTSIZE = 6;// 评论每页显示行数
	private String articlesTitle;// 搜索稿件名称
	private String articlesAuthor;// 搜索稿件作者
	private String articlesLatestuser;// 搜索最后稿件编辑人
	private Date articlesLatestchangtime;// 搜索最后修改时间
	private String latestchangtime;// 最后修改时间的STRING表现方式
	private String clumnName;// 搜索稿件作者
	private String fileFileName; // 导读图片名称
	private String delAttachIds;
	/**
	 * 工作流
	 */
	private String articlesName; // 文章标题
	private List workflowTransitionslist; // 流程流向集
	private String showContent;// 显示内容

	private String columnIds;// 添加稿件时栏目ID

	private String isPromulgate;// 判断是否发布来源

	private MykmManage mykmManager;// 个人收藏Manager

	private String columnNames;// 栏目名称

	private String promulgate;// 标示是信息发布了的修改

	public String getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}

	@Autowired
	public void setMykmManager(MykmManage mykmManager) {
		this.mykmManager = mykmManager;
	}

	public String getIsPromulgate() {
		return isPromulgate;
	}

	public void setIsPromulgate(String isPromulgate) {
		this.isPromulgate = isPromulgate;
	}

	public String getColumnIds() {
		return columnIds;
	}

	public void setColumnIds(String columnIds) {
		this.columnIds = columnIds;
	}

	/**
	 * 构造方法
	 * 
	 */
	ArticlesAction() {
		ArcticlesTypeMap.put("0", "已删除");
		ArcticlesTypeMap.put("1", "未上栏");
		ArcticlesTypeMap.put("2", "已上栏");
		ArcticlesTypeMap.put("3", "审核中");
		ArcticlesTypeMap.put("5", "已审核");
		ArcticlesTypeMap.put("9", "已发布");
	}

	/**
	 * 逻辑删除 articlesId 稿件id
	 */
	@Override
	public String delete() throws Exception {
		articlesManager.deleteOrgs(articlesId);
		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/infopub/articles/articles.action'; </script>");
	}

	/**
	 * 增加及新建初始化
	 */
	@Override
	public String input() throws Exception {
		prepareModel();
		columnIds = "";
		columnNames = "";// 栏目名称
		if (model.getArticlesId() == null || "".equals(model.getArticlesId())) {// 如果ArticlesId为空，获取新页面
			if (columnId != null) {// 如果columnId栏目不为空，获取columnNames名称
				columnManager.getColumn(columnId);
				columnIds = columnId;
				columnNames = columnManager.getClumnName(columnId);
			}
		} else {// ArticlesId不为空
			List<ToaInfopublishColumnArticl> list = articlesManager
					.getColumnArticlByArticleId(model.getArticlesId());
			for (ToaInfopublishColumnArticl ta : list) {
				columnIds = columnIds
						+ ta.getToaInfopublishColumn().getClumnId() + ",";
				columnNames = columnNames
						+ ta.getToaInfopublishColumn().getClumnName() + ",";
			}
		}
		return INPUT;
	}

	/**
	 * 获取未上栏文章列表 Page
	 */
	@Override
	public String list() throws Exception {
		if (!"".equals(disLogo) && disLogo != null) {// 判断是否是搜索文章
			page = articlesManager.getSelectAricesPages(page, articlesTitle,
					articlesAuthor, articlesLatestuser,
					articlesLatestchangtime, disLogo);
		} else {
			page = articlesManager.getAricesPages(page);
		}
		return SUCCESS;
	}

	/**
	 * 新增上栏 columnArticleId上栏ID
	 */
	@Override
	protected void prepareModel() throws Exception {
		if (columnArticleId != null && !"".equals(columnArticleId)
				&& !"null".equals(columnArticleId)) {
			articlesId = articlesManager.getColumnArticl(columnArticleId)
					.getToaInfopublishArticle().getArticlesId();
		}

		if (articlesId != null && !"".equals(articlesId)
				&& !"null".equals(articlesId)) {
			model = articlesManager.getArticle(articlesId);

			if (YES.equals(model.getArticlesIsredirect())) {// ?外部链接
				likeBoolean = true;
			}
			/**
			 * if (YES.equals(model.getArticlesGuidetype())) {// 导读 daoduBoolean
			 * = true; } if (YES.equals(model.getArticlesIshot().trim())) {// 热点
			 * redianBoolean = true; }
			 **/
			if (YES.equals(model.getArticlesIsstandtop())) {// 固顶
				gudingBoolean = true;
			}
			if (YES.equals(model.getArticlesIscancomment())) {// 是否评论
				yunxuprBoolean = true;
			}
			if (YES.equals(model.getArticlesIsshowcomment())) {// ?显示评论
				xiansBoolean = true;
			}
		} else {
			model = new ToaInfopublishArticle();
			model.setArticlesRedirecturl("http://");
			model.setArticlesPaginationnum(10000);
			model.setArticlesLatestuser(userService.getCurrentUser().getUserName());
			model.setArticlesCreatedate(new Date());
			daoduBoolean = true;
		}
	}

	/**
	 * 保存
	 */
	@Override
	public String save() throws Exception {
		//栏目没选  置空
		if(columnIds==null||"".equals(columnIds)){
			columnId = "";
		}
		
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		if ("".equals(model.getArticlesId())) {
			model.setArticlesId(null);
		}
		model.setArticlesLatestchangtime(new Date());
		if (likeBoolean) {// 外部链接
			model.setArticlesIsredirect(YES);
		} else {
			model.setArticlesIsredirect(NO);
		}
		/**
		 * if (daoduBoolean) {// ?导读 model.setArticlesGuidetype(YES); } else {
		 * model.setArticlesGuidetype(NO); } if (redianBoolean) {// 热点
		 * model.setArticlesIshot(YES); } else { model.setArticlesIshot(NO); }
		 **/
		if (gudingBoolean) {// 固顶
			model.setArticlesIsstandtop(YES);
		} else {
			model.setArticlesIsstandtop(NO);
		}
		if (yunxuprBoolean) {// 是否可评论
			model.setArticlesIscancomment(YES);
		} else {
			model.setArticlesIscancomment(NO);
		}
		model.setArticlesAticlestate(ARCTICLENOTYPE);// 稿件状态为未上栏
		// model.setArticlesHits(0L); //qibh修改2012-05-31 加了这个每次默认点击次数都是0
		model.setArticlesLatestchangtime(new Date());
		model.setArticlesLatestuser(userService.getCurrentUser().getUserName());
		if (file != null && file.length > 0) {// 导读图片是否为空
			FileInputStream fis = new FileInputStream(file[0]);
			byte[] buf = new byte[(int) file[0].length()];
			SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyyMMddHHmmssms");
			String newFileName = sdf.format(new Date());
			String ext = fileName.substring(fileName.length() - 3, fileName
					.length());
			String src = "/common/ewebeditor/uploadfile/" + newFileName + "."
					+ ext;
			FileOutputStream fos = new FileOutputStream(getRequest()
					.getSession().getServletContext().getRealPath("/")
					+ src);
			fis.read(buf);
			fos.write(buf);
			fos.flush();
			model.setArticlesPic(src);// 获取文件
			fos.close();
		} else if (delAttachIds != null && !"".equals(delAttachIds)) {
			model.setArticlesPic(null);// 获取文件
		}
		// String con = FiltrateContent.getNewContent(model.getArticlesTitle());
		// model.setArticlesTitle(con);// 更新标题
		articlesManager.saveArticle(model, columnId, columnArticleId,
				new OALogInfo(this.getRequest().getRemoteAddr(), "稿件上栏"));
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		// addActionMessage("保存成功");
		if (columnIds != null && !"".equals(columnIds)) {// 判断是否选择多条栏目
			// filterColumn();
			articlesManager.setArticlesClumn(columnIds, model.getArticlesId());
		}else{
			// columnIds为空 这清空中间表
			if (model.getArticlesId() != null && !"".equals(model.getArticlesId())){
				List<ToaInfopublishColumnArticl> t = articlesManager.getColumnArticlByArticleId(model.getArticlesId());
//				System.out.println("sss");
				articlesManager.delete(t);
			}
		}
		if (columnId != null && !"".equals(columnId)
				&& (isPromulgate == null || "".equals(isPromulgate))) {// 判断是否在栏目下添加文章
			return "cainit";
		}
		if (isPromulgate != null && !"".equals(isPromulgate)) {// 是否是信息发布了的编写
			return "promu";
		}
		return "init";
	}

	public ToaInfopublishArticle getModel() {
		return model;
	}

	/**
	 * 过滤栏目编号
	 * 
	 * @return
	 */
	private void filterColumn() {
		String columns[] = columnIds.split(",");
		String columnid = "";
		for (int i = 0; i < columns.length; i++) {// 过滤掉当前栏目
			if (columns[i] != columnId && !columnId.equals(columns[i])) {
				columnid = columnid + columns[i] + ",";
			}
		}
		if (columnid != "")// 判断栏目是否为空
		{
			columnIds = columnid.substring(0, columnid.length() - 1);
		} else {
			columnIds = columnid;
		}
	}

	/**
	 * 获取树列表
	 * 
	 * @return
	 */
	public String tree() throws Exception {
		columnList = columnManager.getMyColumn();
		Collections.sort(columnList, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				ToaInfopublishColumn p1 = (ToaInfopublishColumn)o1;
				ToaInfopublishColumn p2 = (ToaInfopublishColumn)o2;
				return  p1.getClumnId().compareTo(p2.getClumnId());
			}
		});
		if (NO.equals(treeType)) {// 显示信息发布结构树
			columnList = columnManager.getMyUpdateColumn();
			Collections.sort(columnList, new Comparator<Object>() {
				public int compare(Object o1, Object o2) {
					ToaInfopublishColumn p1 = (ToaInfopublishColumn)o1;
					ToaInfopublishColumn p2 = (ToaInfopublishColumn)o2;
					return  p1.getClumnId().compareTo(p2.getClumnId());
				}
			});
			return "tree";
		} else if (YES.equals(treeType)) {// 显示选择机构树
			columnList = columnManager.getMyUpdateColumn();
			Collections.sort(columnList, new Comparator<Object>() {
				public int compare(Object o1, Object o2) {
					ToaInfopublishColumn p1 = (ToaInfopublishColumn)o1;
					ToaInfopublishColumn p2 = (ToaInfopublishColumn)o2;
					return  p1.getClumnId().compareTo(p2.getClumnId());
				}
			});
			return "secletcolumn";
		} else if (SHOWTREE.equals(treeType)) {// 查看信息
			return "showtree";
		}
		return "tree";
	}

	/**
	 * 获取指定栏目树列表
	 * 
	 * @return
	 */
	public String columntree() throws Exception {
		columnList = columnManager.getChoseColumn(columnId);
		return "showtree";
	}

	/**
	 * 获取有权限查看的树列表
	 * 
	 * @return
	 */
	public String treepro() throws Exception {
		columnList = columnManager.getMyUpdateColumn();
		Collections.sort(columnList, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				ToaInfopublishColumn p1 = (ToaInfopublishColumn)o1;
				ToaInfopublishColumn p2 = (ToaInfopublishColumn)o2;
				return  p1.getClumnId().compareTo(p2.getClumnId());
			}
		});
		return "treepro";
	}

	/**
	 * author:lanlc description:异步加载树结构 modifyer:
	 * 
	 * @return
	 */
	public String syntree() {
		StringBuffer str = new StringBuffer();
		List<ToaInfopublishColumn> folds = columnManager.getSubColumn(columnId);
		for (int i = 0; i < folds.size(); i++) {
			ToaInfopublishColumn fold = folds.get(i);
			List<ToaInfopublishColumn> subColumnList = columnManager.getSubColumn(fold.getClumnId());
			if (!subColumnList.isEmpty()) {// 如果subColumnList不为空的话显示树
				str.append("<li id=" + fold.getClumnId() + ">");
				str.append("<span><input type='checkbox' id='"
						+ fold.getClumnId() + "' value='" + fold.getClumnId()
						+ "'></span><span>" + fold.getClumnName() + "</span>");
				str.append("<ul class=ajax>");
				str.append("<li id=" + fold.getClumnId() + i + ">{url:"
						+ getRequest().getContextPath()
						+ "/infopub/articles/articles!syntree.action?columnId="
						+ fold.getClumnId() + "}</li>");
				str.append("</ul>");
				str.append("</li>");
			} else {// 如果subColumnList为空
				str.append("<li id=" + fold.getClumnId() + ">");
				str.append("<span><input type='checkbox'  id='"
						+ fold.getClumnId() + "' value='" + fold.getClumnId()
						+ "'></span><span>" + fold.getClumnName());
				str.append("</span>");
				str.append("</li>");
			}
		}
		renderHtml(str.toString());
		return null;
	}

	/**
	 * 显示查看稿件
	 * 
	 * @return
	 */
	public String show() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		if (columnArticleId != null && !"".equals(columnArticleId)// 判断上拦稿件ID是否为空
				&& !"null".equals(columnArticleId)) {
			articlesId = articlesManager.getColumnArticl(columnArticleId)
					.getToaInfopublishArticle().getArticlesId();
		}
		model = articlesManager.getArticle(articlesId);
		return "show";
	}

	/**
	 * 删除页面所获取的树
	 * 
	 * @return
	 */
	public String rectree() throws Exception {
		columnList = columnManager.getColumns();
		return "rectree";
	}

	/**
	 * 稿件上栏
	 * 
	 * @return
	 */
	public String setArticlesClumn() throws Exception {// 稿件上栏
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		List<String> list = getArticlesId(articlesId);
		for (String aid : list) {
			articlesManager.setArticlesClumn(columnId, aid, new OALogInfo(
					"稿件上栏"));
		}
		return "init";
	}

	/**
	 * 拆分字符串
	 * 
	 * @param column
	 * @return
	 */
	public List<String> getArticlesId(String column) {
		String[] shore = column.split(",");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < shore.length; i++) {
			list.add(shore[i]);
		}
		return list;
	}

	/**
	 * 栏目中文章删除(假)
	 * 
	 * @return
	 */
	public String colAtrdelete() throws Exception {// 栏目中文章删除
		String[] ids = columnArticleId.split(",");
		for (int i = 0; i < ids.length; i++) {
			articlesManager.colAtrdelete(ids[i], new OALogInfo(this
					.getRequest().getRemoteAddr(), "逻辑删除栏目里稿件"));
		}
		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/infopub/articles/columnArticles.action?columnId="
				+ columnId + " '; </script>");
	}

	/**
	 * 获取已删除的列表
	 * 
	 * @return
	 */
	public String reclist() throws Exception {
		String userId = userService.getCurrentUser().getUserId();
		if (!"".equals(disLogo) && disLogo != null) {
			page = articlesManager.getSelectRecPages(page, articlesTitle,
					articlesAuthor, clumnName, articlesLatestuser,
					articlesLatestchangtime, disLogo);
		} else {
			page = articlesManager.getRecPages(page);
		}
		// articlesManager.getRecPages(page);
		return "reclist";
	}

	/**
	 * 还原已删除的信息
	 * 
	 * @return
	 */
	public String revert() throws Exception {
		articlesManager.revert(columnArticleId, new OALogInfo("还原已删除稿件信息"));
		return renderHtml("<script>  location='"
				+ getRequest().getContextPath()
				+ "/infopub/articles/articles!reclist.action '; </script>");
	}

	/**
	 * 彻底删除信息
	 * 
	 * @return
	 */
	public String truedelete() throws Exception {
		articlesManager.truedelete(articlesId, new OALogInfo(this.getRequest()
				.getRemoteHost(), "彻底删除稿件信息"));
		String[] artList = articlesId.split(",");
		for (int i = 0; i < artList.length; i++) {
			String url = "/infopub/articles/articles!showColumnArticl.action?columnArticleId="
					+ artList[i];
			mykmManager.deleteByUrl(url);
		}
		return renderHtml("<script>location='" + getRequest().getContextPath()
				+ "/infopub/articles/articles!reclist.action '; </script>");
	}

	/**
	 * 清空信息
	 * 
	 * @return
	 */
	public String clear() throws Exception {
		try {
			articlesManager.clear(new OALogInfo(this.getRequest()
					.getRemoteHost(), "清空回收站"));
			Page<ToaInfopublishColumnArticl> capage = new Page<ToaInfopublishColumnArticl>(
					FlexTableTag.MAX_ROWS, true);
			capage = articlesManager.getRecPages(capage);
			List<ToaInfopublishColumnArticl> tlist = capage.getResult();
			for (ToaInfopublishColumnArticl tca : tlist) {
				String url = "/infopub/articles/articles!showColumnArticl.action?columnArticleId="
						+ tca.getColumnArticleId();
				mykmManager.deleteByUrl(url);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return renderHtml("<script>  location='"
				+ getRequest().getContextPath()
				+ "/infopub/articles/articles!reclist.action '; </script>");
	}

	/**
	 * 获取评论数量
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showComments() throws Exception {
		ca = articlesManager.getColumnArticl(columnArticleId);
		commentNum = ca.getToaInfopublishArticle().getToaInfopublishComments()
				.size();
		Object obj = new Object();
		obj = commentNum;
		return renderHtml(obj.toString());
	}

	/**
	 * 用户查看新闻时显示
	 * 
	 * @return
	 */
	public String showColumnArticl() throws Exception {
		ca = articlesManager.getColumnArticl(columnArticleId);
		if (ca == null) {
			return "gonext";
		}
		int i = 0;
		// if("1".equals(anObject)ca.getToaInfopublishArticle().getArticlesIsredirect())
		if (!"".equals(ca.getToaInfopublishArticle().getArticlesHits())
				&& ca.getToaInfopublishArticle().getArticlesHits() != null) {
			i = ca.getToaInfopublishArticle().getArticlesHits().intValue();
		}
		i++;
		ca.getToaInfopublishArticle().setArticlesHits(new Long(i));
		articlesManager.saveArticle(ca.getToaInfopublishArticle());
		commentNum = ca.getToaInfopublishArticle().getToaInfopublishComments().size();
		articlesId = ca.getToaInfopublishArticle().getArticlesId();
		commentsList = articlesManager.getComments(articlesId);// 获取所有评论
		List<ToaInfopublishComment> comments = new ArrayList<ToaInfopublishComment>();
		for (ToaInfopublishComment comment : commentsList) {
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			comment.setShowTime(dateFm.format(comment.getCommentAddtime()));
			comments.add(comment);
		}
		if (ca.getColumnArticleLatestchangtime() != null) {// 判断最后修改时间是否为空
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			latestchangtime = format.format(ca
					.getColumnArticleLatestchangtime());
		}
		commentsList.clear();
		commentsList = comments;
		isactive = userService.getCurrentUser().getUserIsSupManager();
		if(ca.getToaInfopublishArticle().getArticlesPaginationnum()!=null){
		   int number = ca.getToaInfopublishArticle().getArticlesPaginationnum();// 分页显示字数
		}
		String content = ca.getToaInfopublishArticle()
				.getArticlesArticlecontent();// 获取显示信息内容
		if ((ca.getToaInfopublishArticle().getArticlesPagination() != null)
				&& (ca.getToaInfopublishArticle().getArticlesPagination()
						.equals("1"))
				&& (ca.getToaInfopublishArticle().getArticlesPaginationnum() > 0)) {
			showContent = getPageCon(content, ca.getToaInfopublishArticle()
					.getArticlesPaginationnum());
		} else {
			showContent = content;
		}

		int pagesize = 6;// 评论每页显示行数
		if (pagenow == 0) {// 首次加载时设现实的内容
			pagenow = 1;
		}
		// 获取评论的总页是
		int psize = commentsList.size() % pagesize == 0 ? commentsList.size()
				/ pagesize : commentsList.size() / pagesize + 1;

		getRequest().setAttribute("pagesize", pagesize);
		getRequest().setAttribute("pagenow", pagenow);
		getRequest().setAttribute("psize", psize);
		if ("1".equals(ca.getToaInfopublishArticle().getArticlesIsredirect())) {
			getRequest().setAttribute("outlink",
					ca.getToaInfopublishArticle().getArticlesRedirecturl());
			return "outlink";// renderHtml("<script>window.location='"+ca.getToaInfopublishArticle().getArticlesRedirecturl()+"';</script>");
		} else {
			return "showinfo";
		}
	}

	/**
	 * 内容分页
	 * 
	 * @param content
	 * @return
	 */
	public String getPageCon(String content, Integer pagesize) {
		int pagesize1 = pagesize;
		int psize1 = 1;
		if (pagecontent == Integer.parseInt(NO)) {
			pagecontent = Integer.parseInt(YES);
		}
		if (content != null && !"".equals(content))// 判断内容是否为空
		{
			int length = content.length();
			psize1 = length % pagesize1 == 0 ? length / pagesize1 : length
					/ pagesize1 + 1;
		}
		int begin = (pagecontent - 1) * pagesize1;
		int end = begin + pagesize1;
		getRequest().setAttribute("pagecontent", pagecontent);
		getRequest().setAttribute("pagesize1", pagesize1);
		getRequest().setAttribute("psize1", psize1);
		if (content != null && content.length() > pagesize1) {
			if (content.length() >= end) {
				content = content.substring(begin, end);
			} else {
				content = content.substring(begin);
			}
		}
		return content;
	}

	/**
	 * @return columnList
	 */
	public List<ToaInfopublishColumn> getColumnList() {
		return columnList;
	}

	/**
	 * 显示个人桌面列表,showtype为显示形式
	 * 
	 * @return innerHtml
	 */
	public String showDesk() throws Exception {
		articlesManager.notStandtop();
		articlesManager.isStendTop();
		String blockid = getRequest().getParameter("blockid");// 获取blockid
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = getRequest().getParameter("showCreator");
		String showDate = getRequest().getParameter("showDate");
		String showType = getRequest().getParameter("showType");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockid != null) {
			Map<String, String> map = dsmanager.getParam(blockid);// 通过blockid获取映射对象
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			showType = map.get("showType");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 是否限制字体
		}
		String showco = "";
		StringBuffer innerHtml = new StringBuffer();
		articlpage.setPageSize(Integer.parseInt(showNum));
		String ids = columnManager.getchildColumnIds("", columnId);
		if (ids != null && !"".equals(ids)) {
			ids = ids + "," + "'" + columnId + "'";
			articlpage = articlesManager.getAllColumnArticleList(articlpage, ids);
		}else{			
			articlpage = articlesManager.getColumnArticleList(articlpage,columnId);
		}
		List<ToaInfopublishColumnArticl> list = articlpage.getResult();

		if (list != null && list.size() > 0)// 判断是否有值
		{
			// showco = list.get(0).getToaInfopublishColumn().getClumnName();
			showco = columnManager.getClumnName(columnId);
		}
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "12";
		}
		if (list != null && list.size() > 0) {
			if (SHOWTYEP.equals(showType)) {// 这里的showtype为显示方式,可以在权限模块连接加入,1为flash图片显示,其它为简单元查看模式
				String src = "";
				String links = "";
				String texts = "";
				for (int i = 0; i < list.size(); i++) {
					ToaInfopublishColumnArticl columnBo = list.get(i);
					String annexContent = columnBo.getToaInfopublishArticle()
							.getArticlesPic();
					int j = 0;
					if (!"".equals(annexContent) && annexContent != null) {
						src += "|" + getRequest().getContextPath()
								+ annexContent;
						links += "|"
								+ getRequest().getContextPath()
								+ "/infopub/articles/articles!showColumnArticl.action?columnArticleId="
								+ columnBo.getColumnArticleId();
						String testTitle = "";
						if (columnBo.getToaInfopublishArticle()
								.getArticlesTitle().length() > 16)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
							testTitle = columnBo.getToaInfopublishArticle()
									.getArticlesTitle().substring(0, 14)
									+ "...";
						else {
							testTitle = columnBo.getToaInfopublishArticle()
									.getArticlesTitle();
						}
						texts += "|" + testTitle;
						j++;
					}
					if (j == 4) {
						break;
					}
				}
				if (!"".equals(src)) {
					src = src.substring(1);
				}
				if (!"".equals(links)) {
					links = links.substring(1);
				}
				if (!"".equals(texts)) {
					texts = texts.substring(1);
				}
				innerHtml
						.append(
								"<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ><tr><td  width=\"200\">")
						.append(
								"<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\" width=\"200\" height=\"175\"> ")
						.append(
								"<param name=\"allowScriptAccess\" value=\"sameDomain\">")
						.append("<param name=\"movie\" value=\"")
						.append(getRequest().getContextPath())
						.append(
								"/common/flash/focus.swf\"><param name=\"quality\" value=\"high\"> ")
						.append("<param name=\"bgcolor\" value=\"#F0F0F0\"> ")
						.append(
								"<param name=\"menu\" value=\"false\"><param name=wmode value=\"opaque\"> ")
						.append("<param name=\"FlashVars\" value=\"pics=")
						.append(src)
						.append("&links=")
						.append(links)
						.append("&texts=")
						.append(texts)
						.append(
								"&borderwidth=200&borderheight=155&textheight=15\"> ")
						.append(
								"<embed src=\"pixviewer.swf\" wmode=\"opaque\" FlashVars=\"pics=")
						.append(src)
						.append("&links=")
						.append(links)
						.append("&texts=")
						.append(texts)
						.append(
								"&borderwidth=200&borderheight=160&textheight=15\" menu=\"false\" bgcolor=\"#F0F0F0\" quality=\"high\" width=\"200\" height=\"160\" allowScriptAccess=\"sameDomain\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /> ")
						.append("</object></td><td valign=\"top\">");
				for (int i = 0; i < num && i < list.size(); i++) {// 获取在条数范围内的列表
					SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
					ToaInfopublishColumnArticl columnBo = list.get(i);
					innerHtml
							.append(
									"	<div class=\"linkdiv\" valign=top style=\"\">")
							.append("	<IMG SRC=\"")
							.append(getRequest().getContextPath())
							.append(
									"/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
					if (YES.equals(columnBo.getColumnArticleIsstandtop())) {// 是否为固顶
						innerHtml.append("<font color=\"#0000FF\">[顶]</font>");
					}
					innerHtml.append("	<span title=\"").append(
							columnBo.getToaInfopublishArticle()
									.getArticlesTitle()).append("\">");
					String title = columnBo.getToaInfopublishArticle()
							.getArticlesTitle();
					if (title.length() > length)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
						title = title.substring(0, length) + "...";
					innerHtml
							.append(
									"	<a style=\"font-size:"
											+ sectionFontSize
											+ "px;\" href=\"javascript:getSysConsole().refreshWorkByTitle('")
							.append(getRequest().getContextPath())
							.append(
									"/infopub/articles/articles!showColumnArticl.action?columnArticleId=")
							.append(columnBo.getColumnArticleId())
							.append("','").append(
									columnBo.getToaInfopublishArticle()
											.getArticlesTitle()).append(
									"')\"> ");
					String colour = columnBo.getToaInfopublishArticle()
							.getArticlesTitlecolor();
					String font = columnBo.getToaInfopublishArticle()
							.getArticlesTitlefont();
					if (!"0".equals(colour) && !"".equals(colour)) {// 设置了颜色
						if ("1".equals(font)) {// 粗体
							innerHtml.append("<B><font color=\"")
									.append(colour).append("\">").append(title)
									.append("</font></B></a></span>");
						} else if ("2".equals(font)) {// 斜体
							innerHtml.append("<I><font color=\"")
									.append(colour).append("\">").append(title)
									.append("</font></I></a></span>");
						} else if ("3".equals(font)) {// 粗斜体
							innerHtml.append("<B><I><font color=\"").append(
									colour).append("\">").append(title).append(
									"</font></I></B></a></span>");
						} else {
							innerHtml.append("<font color=\"").append(colour)
									.append("\">").append(title).append(
											"</font></a></span>");
						}
					} else {
						if ("1".equals(font)) {// 粗体
							innerHtml.append("<B>").append(title).append(
									"</B></a></span>");
						} else if ("2".equals(font)) {// 斜体
							innerHtml.append("<I>").append(title).append(
									"</I></a></span>");
						} else if ("3".equals(font)) {// 粗斜体
							innerHtml.append("<B><I>").append(title).append(
									"</I></B></a></span>");
						} else {
							innerHtml.append(title).append("</a></span>");
						}
					}
					if (YES.equals(columnBo.getToaInfopublishArticle()
							.getArticlesIshot())) {// 是否为热点
						innerHtml
								.append("<IMG SRC=\"")
								.append(getRequest().getContextPath())
								.append(
										"/oa/image/desktop/hot.gif\" WIDTH=\"23\" HEIGHT=\"7\" BORDER=\"0\" ALT=\"\">");
					}
					if (YES.equals(showCreator))// 如果设置为显示作者，则显示作者信息
						if ("".equals(columnBo.getToaInfopublishArticle()
								.getArticlesAuthor())
								|| columnBo.getToaInfopublishArticle()
										.getArticlesAuthor() == null) {
							innerHtml
									.append("	<span class =\"linkgray\">无</span>");
						} else {
							innerHtml.append("	<span class =\"linkgray\">")
									.append(
											columnBo.getToaInfopublishArticle()
													.getArticlesAuthor())
									.append("</span>");
						}
					if (YES.equals(showDate))// 如果设置为显示日期，则显示日期信息
						innerHtml
								.append("	<span class =\"linkgray10\">")
								.append(
										st.format(columnBo.getColumnArticleLatestchangtime()))
								.append("</span>");
					innerHtml.append("	</div>");
				}
				innerHtml.append("</td></tr></table>");
			} else if (SESHOWTYEP.equals(showType)) {// 2为只有图片的形式
				String src = "";// flash图片源
				String links = "";// flash图片链接
				String texts = "";// flash图片标题
				for (int i = 0; i < list.size(); i++) {
					ToaInfopublishColumnArticl columnBo = list.get(i);
					String annexContent = columnBo.getToaInfopublishArticle()
							.getArticlesPic();
					int j = 0;
					if (!"".equals(annexContent) && annexContent != null) {
						src += "|" + getRequest().getContextPath()
								+ annexContent;
						links += "|"
								+ getRequest().getContextPath()
								+ "/infopub/articles/articles!showColumnArticl.action?columnArticleId="
								+ columnBo.getColumnArticleId();
						String testTitle = "";
						if (columnBo.getToaInfopublishArticle()
								.getArticlesTitle().length() > 16)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
							testTitle = columnBo.getToaInfopublishArticle()
									.getArticlesTitle().substring(0, 14)
									+ "...";
						else {
							testTitle = columnBo.getToaInfopublishArticle()
									.getArticlesTitle();
						}
						texts += "|" + testTitle;
						j++;
					}
					if (j == 4) {
						break;
					}
				}
				if (!"".equals(src)) {
					src = src.substring(1);
				}
				if (!"".equals(links)) {
					links = links.substring(1);
				}
				if (!"".equals(texts)) {
					texts = texts.substring(1);
				}
				// 插入flash对象
				innerHtml
						.append(
								"<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ><tr><td  width=\"96%\">")
						.append(
								"<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\" width=\"375\" height=\"175\"> ")
						.append(
								"<param name=\"allowScriptAccess\" value=\"sameDomain\">")
						.append("<param name=\"movie\" value=\"")
						.append(getRequest().getContextPath())
						.append(
								"/common/flash/focus.swf\"><param name=\"quality\" value=\"high\"> ")
						.append("<param name=\"bgcolor\" value=\"#F0F0F0\"> ")
						.append(
								"<param name=\"menu\" value=\"false\"><param name=wmode value=\"opaque\"> ")
						.append("<param name=\"FlashVars\" value=\"pics=")
						.append(src)
						.append("&links=")
						.append(links)
						.append("&texts=")
						.append(texts)
						.append(
								"&borderwidth=375&borderheight=160&textheight=15\"> ")
						.append(
								"<embed src=\"pixviewer.swf\" wmode=\"opaque\" FlashVars=\"pics=")
						.append(src)
						.append("&links=")
						.append(links)
						.append("&texts=")
						.append(texts)
						.append(
								"&borderwidth=375&borderheight=160&textheight=15\" menu=\"false\" bgcolor=\"#F0F0F0\" quality=\"high\" width=\"375\" height=\"160\" allowScriptAccess=\"sameDomain\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /> ")
						.append("</object></td><td valign=\"top\">");
				innerHtml.append("</td></tr></table>");
			} else {
				for (int i = 0; i < num && i < list.size(); i++) {// 获取在条数范围内的列表
					SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
					ToaInfopublishColumnArticl columnBo = list.get(i);
					innerHtml
							.append("<table class=\"linkdiv\" width=\"100%\" title=\"\">");
					innerHtml.append("<tr>");
					// 图标
					innerHtml.append("<td>");
					innerHtml
							.append("	<IMG SRC=\"")
							.append(getRequest().getContextPath())
							.append(
									"/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
					if (YES.equals(columnBo.getColumnArticleIsstandtop())) {// 是否为固顶
						innerHtml.append("<font color=\"#0000FF\">[顶]</font>");
					}
					innerHtml.append("	<span title=\"").append(
							columnBo.getToaInfopublishArticle()
									.getArticlesTitle()).append("\">");
					String title = columnBo.getToaInfopublishArticle()
							.getArticlesTitle();
					if (title.length() > length)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
						title = title.substring(0, length) + "...";
					innerHtml
							.append(
									"	<a style=\"font-size:"
											+ sectionFontSize
											+ "px;\" href=\"javascript:getSysConsole().refreshWorkByTitle('")
							.append(getRequest().getContextPath())
							.append(
									"/infopub/articles/articles!showColumnArticl.action?columnArticleId=")
							.append(columnBo.getColumnArticleId())
							.append("','").append(
									columnBo.getToaInfopublishArticle()
											.getArticlesTitle()).append(
									"')\"> ");
					String colour = columnBo.getToaInfopublishArticle()
							.getArticlesTitlecolor();
					String font = columnBo.getToaInfopublishArticle()
							.getArticlesTitlefont();
					if (!"0".equals(colour) && !"".equals(colour)) {// 设置了颜色
						if ("1".equals(font)) {// 粗体
							innerHtml.append("<B><font color=\"")
									.append(colour).append("\">").append(title)
									.append("</font></B></a></span>");
						} else if ("2".equals(font)) {// 斜体
							innerHtml.append("<I><font color=\"")
									.append(colour).append("\">").append(title)
									.append("</font></I></a></span>");
						} else if ("3".equals(font)) {// 粗斜体
							innerHtml.append("<B><I><font color=\"").append(
									colour).append("\">").append(title).append(
									"</font></I></B></a></span>");
						} else {
							innerHtml.append("<font color=\"").append(colour)
									.append("\">").append(title).append(
											"</font></a></span>");
						}
					} else {
						if ("1".equals(font)) {// 粗体
							innerHtml.append("<B>").append(title).append(
									"</B></a></span>");
						} else if ("2".equals(font)) {// 斜体
							innerHtml.append("<I>").append(title).append(
									"</I></a></span>");
						} else if ("3".equals(font)) {// 粗斜体
							innerHtml.append("<B><I>").append(title).append(
									"</I></B></a></span>");
						} else {
							innerHtml.append(title).append("</a></span>");
						}
					}
					if (YES.equals(columnBo.getToaInfopublishArticle()
							.getArticlesIshot())) {// 是否为热点
						innerHtml
								.append("<IMG SRC=\"")
								.append(getRequest().getContextPath())
								.append("/oa/image/desktop/hot.gif\" WIDTH=\"23\" HEIGHT=\"7\" BORDER=\"0\" ALT=\"\">");
					}
					innerHtml.append("</td>");
					if (YES.equals(showCreator)) {// 如果设置为显示作者，则显示作者信息
						innerHtml.append("<td width=\"80px\">");
						if ("".equals(columnBo.getToaInfopublishArticle()
								.getArticlesAuthor())
								|| columnBo.getToaInfopublishArticle()
										.getArticlesAuthor() == null) {
							innerHtml
									.append("	<span style=\"font-size:"
											+ sectionFontSize
											+ "px\" title =\"无\" class =\"linkgray\">无</span>");
						} else {
							String showName = columnBo
									.getToaInfopublishArticle()
									.getArticlesAuthor();
							if (showName.length() > 3) {
								showName = showName.substring(0, 3) + "...";
							}
							innerHtml.append(
									"	<span style=\"font-size:"
											+ sectionFontSize
											+ "px\" title =\""
											+ columnBo
													.getToaInfopublishArticle()
													.getArticlesAuthor()
											+ "\" class =\"linkgray\">")
									.append(showName).append("</span>");
						}
						innerHtml.append("</td>");
					}
					if (YES.equals(showDate)) {// 如果设置为显示日期，则显示日期信息
						innerHtml.append("<td width=\"100px\">");
						innerHtml
								.append(
										"<span style=\"font-size:"
												+ sectionFontSize
												+ "px\" title =\""
												+ new SimpleDateFormat(
														"yyyy-MM-dd hh:mm")
														.format(columnBo
																.getColumnArticleLatestchangtime())
												+ "\" class =\"linkgray10\">")
								.append(
										st.format(columnBo
														.getColumnArticleLatestchangtime()))
								.append("</span>");
						innerHtml.append("	</td>");
					}
					innerHtml.append("</tr>");
					innerHtml.append("</table>");
				}
			}
		}
		if(list==null){
			for (int i = 0; i < num ; i++) { // 获取在条数范围内的列表
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
		if (showco != "") {
			innerHtml
					.append(
							"    <div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"javascript:getSysConsole().refreshWorkByTitle('")
					.append(getRequest().getContextPath())
					.append(
							"/fileNameRedirectAction.action?toPage=infopub/articles/articles-showcolumncontent.jsp?columnId=")
					.append(columnId).append(
							"','" + showco + "')\"><IMG SRC=\"").append(
							getRequest().getContextPath()).append(
							"/oa/image/more.gif\" BORDER=\"0\" /></a></div> ");
		}
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	public String showTableDesk() throws Exception {
		articlesManager.notStandtop();
		articlesManager.isStendTop();
		String blockid = getRequest().getParameter("blockid");// 获取blockid
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = getRequest().getParameter("showCreator");
		String showDate = getRequest().getParameter("showDate");
		String showType = getRequest().getParameter("showType");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockid != null) {
			Map<String, String> map = dsmanager.getParam(blockid);// 通过blockid获取映射对象
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			showType = map.get("showType");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 是否限制字体
		}
		StringBuffer innerHtml = new StringBuffer();
		articlpage.setPageSize(Integer.parseInt(showNum));
		String ids = columnManager.getchildColumnIds("", columnId);
		if (ids != null && !"".equals(ids)) {
			ids = ids + "," + "'" + columnId + "'";
			articlpage = articlesManager.getAllColumnArticleList(articlpage, ids);
		}else{
			articlpage = articlesManager.getColumnArticleList(articlpage,columnId);
		}
		List<ToaInfopublishColumnArticl> list = articlpage.getResult();
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "12";
		}
		if (list != null && list.size() > 0) {
			innerHtml
			.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
			for (int i = 0; i < num && i < list.size(); i++) {// 获取在条数范围内的列表
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				ToaInfopublishColumnArticl columnBo = list.get(i);
				// 标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript:refreshWorkByTitle('").append(
						getRequest().getContextPath()).append(
						"/infopub/articles/articles!showColumnArticl.action?columnArticleId="
								+ columnBo.getColumnArticleId()).append(
						"', '工作简报'").append(");");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml
				.append("	<IMG SRC=\"")
				.append(getRequest().getContextPath())
				.append(
						"/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				String title = columnBo.getToaInfopublishArticle()
						.getArticlesTitle();
				if (title == null) {
					title = "";
				}
				StringBuilder tip = new StringBuilder();
				tip.append(title);
				if (title.length() > length)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0, length) + "...";
				innerHtml.append("	<a href=\"#\" onclick=\"").append(
						titleLink.toString()).append("\">").append(
						"<span style=\"font-size: " + sectionFontSize
								+ "px;\" title=\"").append(tip).append("\">")
						.append(title).append("</span></a>");
				innerHtml.append("</td>");
				if (YES.equals(showCreator)) {// 如果设置为显示作者，则显示作者信息
					innerHtml.append("<td width=\"80px\">");
					if ("".equals(columnBo.getToaInfopublishArticle()
							.getArticlesAuthor())
							|| columnBo.getToaInfopublishArticle()
									.getArticlesAuthor() == null) {
						innerHtml
								.append("	<span style=\"font-size:"
										+ sectionFontSize
										+ "px\" title =\"无\" class =\"linkgray\">无</span>");
					} else {
						String showName = columnBo.getToaInfopublishArticle()
								.getArticlesAuthor();
						if (showName.length() > 3) {
							showName = showName.substring(0, 3) + "...";
						}
						innerHtml.append(
								"	<span style=\"font-size:"
										+ sectionFontSize
										+ "px\" title =\""
										+ columnBo.getToaInfopublishArticle()
												.getArticlesAuthor()
										+ "\" class =\"linkgray\">").append(
								showName).append("</span>");
					}
					innerHtml.append("</td>");
				}
				if (YES.equals(showDate)) {// 如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"100px\">");
					innerHtml
							.append(
									"<span style=\"font-size:"
											+ sectionFontSize
											+ "px\" title =\""
											+ new SimpleDateFormat(
													"yyyy-MM-dd hh:mm")
													.format(columnBo
															.getColumnArticleLatestchangtime())
											+ "\" class =\"linkgray10\">")
							.append(
									st.format(columnBo
											.getColumnArticleLatestchangtime()))
							.append("</span>");
					innerHtml.append("	</td>");
				}
				innerHtml.append("</tr>");
			}
			innerHtml.append("</table>");
		}
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * 获取评论LIST
	 * 
	 */
	public String getComments() throws Exception {
		model = articlesManager.getArticle(articlesId);// 根据文章ID获取文章
		commentsList = articlesManager.getComments(articlesId); // 获取文章的评论列表
		isactive = userService.getCurrentUser().getUserIsactive();// 判断是否是管理员
		int pagesize = SHOWCOMMENTSIZE; // 每页显示评论数量
		if (pagenow == Integer.parseInt(NO)) {// 首次加载页面是现实第一页
			pagenow = Integer.parseInt(YES);
		}
		return "comment";
	}

	/**
	 * 删除评论
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deleteComment() throws Exception {
		commentName = "";
		articlesManager.deleteComment(commentid);// 删除一条评论
		isactive = userService.getCurrentUser().getUserIsactive();// 判断是否是管理员
		// 获取一条评论
		commentsList = articlesManager.getComments(articlesId, new OALogInfo(
				"删除评论"));
		return renderHtml("<script> window.location='"
				+ getRequest().getContextPath()
				+ "/infopub/articles/articles!showColumnArticl.action?articlesId="
				+ articlesId + "&columnArticleId=" + columnArticleId
				+ " '; </script>");
	}

	/**
	 * 获取评论的分页
	 * 
	 * @param commentsList
	 * @return
	 */
	public String commentPages() throws Exception {
		return null;
	}

	/**
	 * 增加评论
	 * 
	 * @return
	 * @throws Exception
	 */
	public String comments() throws Exception {
		getRequest().setCharacterEncoding("UTF-8");
		commentName = "";
		isactive = userService.getCurrentUser().getUserIsactive();// 判断是否是管理员
		articlesManager.comments(articlesId, commentText, commentName,
				new OALogInfo(this.getRequest().getRemoteAddr(), "添加评论"));// 添加一条评论

		// 获取添加的评论ID
		commentsList = articlesManager.getComments(articlesId, new OALogInfo(
				"添加评论"));

		return renderHtml("<script> window.location='"
				+ getRequest().getContextPath()
				+ "/infopub/articles/articles!showColumnArticl.action?articlesId="
				+ articlesId + "&columnArticleId=" + columnArticleId
				+ " '; </script>");
	}

	/**
	 * 获取用户名称
	 * 
	 * @return
	 */
	public String getUser() {
		return userService.getCurrentUser().getUserName();
	}

	/**
	 * 获取当前时间并转换格式
	 * 
	 * @return
	 */
	public String gettime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time = format.format(date);
		return time;
	}

	/**
	 * 稿件发布
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pubArticl() throws Exception {
		String[] id = columnArticleId.split(",");
		for (int i = 0; i < id.length; i++) {
			ca = articlesManager.getColumnArticl(id[i]);
			model = ca.getToaInfopublishArticle();
			if (model.getArticlesAutopublishtime() == null) {
				model.setArticlesAutopublishtime(new Date());
			}
			if (model.getArticlesHits() == null) {
				model.setArticlesHits(0L);
			}
			ca.setColumnArticleState(ARCTICLEISTRUEPUB);// 已发布
			articlesManager.updataColumnArtile(ca, new OALogInfo(this
					.getRequest().getRemoteAddr(), "稿件发布"));
			articlesManager.saveArticle(model, new OALogInfo(this.getRequest()
					.getRemoteHost(), "修改稿件状态"));
		}
		if (isPromulgate != null && NO.equals(isPromulgate)) {
			return renderHtml("<script> window.location='"
					+ getRequest().getContextPath()
					+ "/infopub/articles/columnArticles.action?columnId="
					+ columnId + "'; </script>");
		} else {
			return renderHtml("<script> window.location='"
					+ getRequest().getContextPath()
					+ "/infopub/articles/columnArticles.action?columnId="
					+ columnId + "&promulgate=promulgate '; </script>");
		}
	}

	/**
	 * 取消发布
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delPubArticl() throws Exception {
		String[] id = columnArticleId.split(",");
		for (int i = 0; i < id.length; i++) {
			ca = articlesManager.getColumnArticl(id[i]);
			if (ca != null) {// 稿件是否已经修改
				model = ca.getToaInfopublishArticle();
				// model.setArticlesAutopublishtime(new Date());
				ca.setColumnArticleState(SHOWTREE);// 取消发布
				articlesManager.updataColumnArtile(ca);
				articlesManager.saveArticle(model);
			}
		}
		if (isPromulgate != null && NO.equals(isPromulgate)) {
			return renderHtml("<script> window.location='"
					+ getRequest().getContextPath()
					+ "/infopub/articles/columnArticles.action?columnId="
					+ columnId + "'; </script>");
		} else {
			return renderHtml("<script> window.location='"
					+ getRequest().getContextPath()
					+ "/infopub/articles/columnArticles.action?columnId="
					+ columnId + "&promulgate=promulgate '; </script>");
		}
	}

	/**
	 * author:lanlc description: 判断栏目下的文章是否挂了流程 modifyer:
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getProcessName() throws Exception {
		String[] id = columnArticleId.split(",");
		for (int i = 0; i < id.length; i++) {
			if(columnId != null && !"".equals(columnId)){
				String processName = columnManager.getProcessName(columnId);
				ca = articlesManager.getColumnArticl(id[i]);
				String articlesState = ca.getColumnArticleState(); // 文章状态
				if ((processName != null && !"".equals(processName) && !"9"
						.equals(articlesState))
						|| "0".equals(articlesState)) { // 文章栏目已挂流程并且文章状态未发布
					StringBuffer str = new StringBuffer(100);
					str.append(columnId).append(",").append(columnArticleId)
					.append(",").append(articlesState).append(",").append(ca.getToaInfopublishArticle().getArticlesTitle());
					return renderText(str.toString());
				} 
			}
		}
		return renderText("flagfalse");
	}

	/**
	 * 提交流程
	 * 
	 * @author:邓志城
	 * @date:2009-12-18 上午10:46:21
	 * @return
	 */
	public String wizard() throws Exception {
		workflowName = columnManager.getProcessName(columnId);
		ca = articlesManager.getColumnArticl(columnArticleId);
		bussinessId = ca.getColumnArticleId();
		businessName = ca.getToaInfopublishArticle().getArticlesTitle();
		// 获取审批意见列表
		ideaLst = getManager().getCurrentUserDictItem(getDictType());
		// 得到发文字典类主键
		ToaSysmanageDict dict = getManager().getDict(getDictType());
		if (dict != null) {
			dictId = dict.getDictCode();
		}
		return "wizard";
	}

	/**
	 * 提交流程走向下一步 提交成功返回“OK”，提交失败返回“NO”
	 * 
	 * @author:邓志城
	 * @date:2009-12-12 上午11:10:11
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String handleNextStep() {
		try {
			// 将提醒(方式|内容)存储在session中
			ActionContext cxt = ActionContext.getContext();
			cxt.getSession().put("remindType", remindType);
			cxt.getSession().put("handlerMes", handlerMes);
			cxt.getSession().put("moduleType", getModuleType());// 调用消息模块的类型
			String isNewForm = "";

			String[] info = getManager()
					.getFormIdAndBussinessIdByTaskId(taskId);
			bussinessId = info[0];
			if ("0".equals(bussinessId)) {
				isNewForm = "1";
			} else {
				isNewForm = "0";
			}
			if (suggestion != null) {
				suggestion = suggestion.replaceAll("\\r\\n", "");// 处理审批意见有回车换行的情况
			}
			String curUserId = userService.getCurrentUser().getUserId();
			OtherParameter otherparameter = initOtherParameter();
			if ("".equals(strTaskActors)) {
				getManager().handleWorkflowNextStep(taskId, transitionName, "",
						isNewForm, formId, bussinessId, suggestion, curUserId,
						null, formData, otherparameter);
			} else {
				getManager().handleWorkflowNextStep(taskId, transitionName, "",
						isNewForm, formId, bussinessId, suggestion, curUserId,
						strTaskActors.split(","), formData, otherparameter);
			}
			// 在这里保存审批意见
			getManager().saveIdea(dictId, suggestion);
			return renderHtml("<script>window.returnValue ='OK'; window.close();</script>");// 提交至流程后关闭窗口
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("提交工作流异常，异常信息：" + e.getMessage());
			return renderHtml("<script>window.returnValue ='NO'; window.close();</script>");// 提交至流程后关闭窗口
		}
	}

	/**
	 * 退回
	 */
	public String back() throws Exception {
		String ret = "0";
		try {
			if (StringUtils.hasLength(taskId)) {
				String returnNodeId = getRequest().getParameter("returnNodeId");
				String curUserId = userService.getCurrentUser().getUserId();
				getManager().backSpace(taskId, returnNodeId, formId,
						suggestion, curUserId, formData);
			} else {
				ret = "-1";
			}
		} catch (SystemException e) {
			logger.error(e.getMessage());
			ret = "-3";
		} catch (Exception ex) {
			logger.error("退回任务时出现异常,异常信息：" + ex.getMessage());
			ret = "-2";
		}
		return this.renderText(ret);
	}

	/**
	 * articlesManager set 方法
	 * 
	 * @param articlesManager
	 */
	@Autowired
	public void setArticlesManager(ArticlesManager articlesManager) {
		this.articlesManager = articlesManager;
	}

	/**
	 * columnManager set 方法
	 * 
	 * @param columnManager
	 */
	@Autowired
	public void setColumnManager(ColumnManager columnManager) {
		this.columnManager = columnManager;
	}

	/**
	 * page get 方法
	 * 
	 * @return page
	 */
	public Page<ToaInfopublishArticle> getPage() {
		return page;
	}

	public String getArticlesId() {
		return articlesId;
	}

	public void setArticlesId(String articlesId) {
		this.articlesId = articlesId;
	}

	public String getTreeType() {
		return treeType;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public Boolean getLikeBoolean() {
		return likeBoolean;
	}

	public void setLikeBoolean(Boolean likeBoolean) {
		this.likeBoolean = likeBoolean;
	}

	public Boolean getCuntuBoolean() {
		return cuntuBoolean;
	}

	public void setCuntuBoolean(Boolean cuntuBoolean) {
		this.cuntuBoolean = cuntuBoolean;
	}

	public Boolean getDaoduBoolean() {
		return daoduBoolean;
	}

	public void setDaoduBoolean(Boolean daoduBoolean) {
		this.daoduBoolean = daoduBoolean;
	}

	public Boolean getGudingBoolean() {
		return gudingBoolean;
	}

	public void setGudingBoolean(Boolean gudingBoolean) {
		this.gudingBoolean = gudingBoolean;
	}

	public Boolean getRedianBoolean() {
		return redianBoolean;
	}

	public void setRedianBoolean(Boolean redianBoolean) {
		this.redianBoolean = redianBoolean;
	}

	public Boolean getXiansBoolean() {
		return xiansBoolean;
	}

	public void setXiansBoolean(Boolean xiansBoolean) {
		this.xiansBoolean = xiansBoolean;
	}

	public Boolean getYunxuprBoolean() {
		return yunxuprBoolean;
	}

	public void setYunxuprBoolean(Boolean yunxuprBoolean) {
		this.yunxuprBoolean = yunxuprBoolean;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public void setArcticlesTypeMap(Map arcticlesTypeMap) {
		ArcticlesTypeMap = arcticlesTypeMap;
	}

	public Map getArcticlesTypeMap() {
		return ArcticlesTypeMap;
	}

	public String getShowtype() {
		return showtype;
	}

	public void setShowtype(String showtype) {
		this.showtype = showtype;
	}

	@Autowired
	public void setDsmanager(DesktopSectionManager dsmanager) {
		this.dsmanager = dsmanager;
	}

	public ToaInfopublishColumnArticl getCa() {
		return ca;
	}

	public void setCa(ToaInfopublishColumnArticl ca) {
		this.ca = ca;
	}

	public void setFileFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public List<ToaInfopublishComment> getCommentsList() {
		return commentsList;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getCommentName() {
		return commentName;
	}

	public void setCommentName(String commentName) {
		this.commentName = commentName;
	}

	public String getDisLogo() {
		return disLogo;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}

	public void setModel(ToaInfopublishArticle model) {
		this.model = model;
	}

	public String[] getHasChild() {
		return hasChild;
	}

	public void setHasChild(String[] hasChild) {
		this.hasChild = hasChild;
	}

	public String getArticlesName() {
		return articlesName;
	}

	public void setArticlesName(String articlesName) {
		this.articlesName = articlesName;
	}

	public List getWorkflowTransitionslist() {
		return workflowTransitionslist;
	}

	public void setWorkflowTransitionslist(List workflowTransitionslist) {
		this.workflowTransitionslist = workflowTransitionslist;
	}

	public String getArticlesAuthor() {
		return articlesAuthor;
	}

	public void setArticlesAuthor(String articlesAuthor) {
		this.articlesAuthor = articlesAuthor;
	}

	public Date getArticlesLatestchangtime() {
		return articlesLatestchangtime;
	}

	public void setArticlesLatestchangtime(Date articlesLatestchangtime) {
		this.articlesLatestchangtime = articlesLatestchangtime;
	}

	public String getArticlesLatestuser() {
		return articlesLatestuser;
	}

	public void setArticlesLatestuser(String articlesLatestuser) {
		this.articlesLatestuser = articlesLatestuser;
	}

	public String getArticlesTitle() {
		return articlesTitle;
	}

	public void setArticlesTitle(String articlesTitle) {
		this.articlesTitle = articlesTitle;
	}

	public String getClumnName() {
		return clumnName;
	}

	public void setClumnName(String clumnName) {
		this.clumnName = clumnName;
	}

	public String getColumnArticleId() {
		return columnArticleId;
	}

	public void setColumnArticleId(String columnArticleId) {
		this.columnArticleId = columnArticleId;
	}

	public int getPagenow() {
		return pagenow;
	}

	public void setPagenow(int pagenow) {
		this.pagenow = pagenow;
	}

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getCommentid() {
		return commentid;
	}

	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}

	public int getPagecontent() {
		return pagecontent;
	}

	public void setPagecontent(int pagecontent) {
		this.pagecontent = pagecontent;
	}

	public String getShowContent() {
		return showContent;
	}

	public void setShowContent(String showContent) {
		this.showContent = showContent;
	}

	public String getPromulgate() {
		return promulgate;
	}

	public void setPromulgate(String promulgate) {
		this.promulgate = promulgate;
	}

	public String getLatestchangtime() {
		return latestchangtime;
	}

	public void setLatestchangtime(String latestchangtime) {
		this.latestchangtime = latestchangtime;
	}

	public Page<ToaInfopublishColumnArticl> getArticlpage() {
		return articlpage;
	}

	public void setArticlpage(Page<ToaInfopublishColumnArticl> articlpage) {
		this.articlpage = articlpage;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public String getDelAttachIds() {
		return delAttachIds;
	}

	public void setDelAttachIds(String delAttachIds) {
		this.delAttachIds = delAttachIds;
	}

	@Override
	protected BaseManager getManager() {
		return this.manager;
	}

	@Override
	protected String getModuleType() {
		return String.valueOf(WorkFlowTypeConst.COLUMN);
	}

	@Override
	protected String getWorkflowType() {
		return String.valueOf(WorkFlowTypeConst.COLUMN);
	}

	@Override
	protected String getDictType() {
		// TODO 自动生成方法存根
		return String.valueOf(WorkFlowTypeConst.COLUMN);
	}
}