package com.strongit.oa.webservice.iphone.server.iphoneArticles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.axis.utils.JavaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongit.oa.infopub.column.ColumnManager;
import com.strongit.oa.util.Dom4jUtil;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;

/**
 * 供外部调用的webservice接口
 * 
 * @author Administrator
 * 
 */
public class iphoneArticlesWebService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static String STATUS_SUC  = "1";//返回成功状态
	private static String STATUS_FAIL = "0";//返回失败状态
	
	private ArticlesManager articlesManager;   //栏目文章服务类
	private ColumnManager columnManager;       //栏目服务类

	/**
	 * 构造方法获取manager对象
	 */
	public iphoneArticlesWebService() {
		articlesManager = (ArticlesManager)ServiceLocator.getService("articlesManager");
		columnManager = (ColumnManager)ServiceLocator.getService("columnManager");
		logger.info("栏目文章服务类初始化完毕.。。。");
	}

	private Date parseDate(String dateStr) throws ParseException, SystemException {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	
	/**
	* @method getMyColumn
	* @author 申仪玲
	* @created 2012-08-28 下午16:46:10
	* @description 获取用户有权限的栏目列表
	* @return Date 返回类型
	*/
	public String getMyColumn(String userId,String pageSize,String pageNo) {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		
		try {
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page page = new Page(Integer.parseInt(pageSize),true);
			page.setPageNo(Integer.parseInt(pageNo));
			page = columnManager.queryMyColumn(page, userId);
			List<ToaInfopublishColumn> columnList = new ArrayList<ToaInfopublishColumn>(); 	
			//Page pageColumn = columnList.;
			columnList = page.getResult();
			List<String[]> result = new ArrayList<String[]>();
			if(columnList != null && !columnList.isEmpty()) {
				for(ToaInfopublishColumn column : columnList) {
					String[] columnId = new String[2];
					columnId[0] = "string";
					columnId[1] = column.getClumnId();
					
					String[] columnName = new String[2];
					columnName[0] = "string";
					columnName[1] = column.getClumnName();
					
					String[] columnChildSize = new String[2];
					columnChildSize[0] = "string";
					//获取子栏目数量
					List<ToaInfopublishColumn> columnListsss = columnManager.getSubColumnByUserId(column.getClumnId(),userId);
					if(columnListsss!=null&&columnListsss.size()>0){
						columnChildSize[1] = String.valueOf(columnListsss.size());
					}else{
						columnChildSize[1] = "0";
					}
					
					result.add(columnId);
					result.add(columnName);
					result.add(columnChildSize);
					
				}
			}
			
			
			ret = dom.createItemsResponseData(STATUS_SUC, null, result, 3,
					String.valueOf(page.getTotalCount()), String.valueOf(page.getTotalPages()));
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "获取栏目列表时发生异常:" + ex.getMessage(), null, null);
		}
		return ret;
		
	}
	
	/**
	* @method getColumnArticles
	* @author 申仪玲
	* @created 2012-08-30 下午18:36:10
	* @description 获取栏目文件列表
	* @return Date 返回类型
	*/
	public String getColumnArticles(String columnId,String pageSize,String pageNo)throws DAOException,ServiceException,SystemException{
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page<ToaInfopublishColumnArticl> page = new Page<ToaInfopublishColumnArticl>(Integer.parseInt(pageSize),true);
			page.setPageNo(Integer.parseInt(pageNo));

			List<String[]> result = new ArrayList<String[]>();
			
			Page<ToaInfopublishColumnArticl> pageArticles = articlesManager.getColumnArticles(columnId,page);
			List<ToaInfopublishColumnArticl> list = pageArticles.getResult();
			if(list != null && !list.isEmpty()) {
				for(ToaInfopublishColumnArticl articles : list) {
					
					String[] columnArticleId = new String[2];
					columnArticleId[0] = "String";
					columnArticleId[1] = articles.getColumnArticleId();
					
					String[] columnName = new String[2];
					columnName[0] = "string";
					columnName[1] = articles.getToaInfopublishColumn().getClumnName();
					
					
					String[] articlesTitle = new String[2];
					articlesTitle[0] = "string";
					articlesTitle[1] = articles.getToaInfopublishArticle().getArticlesTitle();
					
					String[] articlesAuthor = new String[2];
					articlesAuthor[0] = "string";
					articlesAuthor[1] = articles.getToaInfopublishArticle().getArticlesAuthor();
					
					String[] Hits = new String[2];
					Hits[0] = "string";
					Hits[1] = articles.getToaInfopublishArticle().getArticlesHits() == null ? "0" 
										: articles.getToaInfopublishArticle().getArticlesHits().toString();
					
					String[] disLogo = new String[2];
					disLogo[0] = "string";
					String guidetype = articles.getColumnArticleGuidetype();
					String ishot = articles.getToaInfopublishArticle().getArticlesIshot();
					String isstandtop = articles.getColumnArticleIsstandtop();
					String cancomment = articles.getToaInfopublishArticle().getArticlesIscancomment();
					String dis = "";
					if("1".equals(guidetype)){
						dis += "导";
					}
					if("1".equals(ishot)){
						dis += "热";
					}
					if("1".equals(isstandtop)){
						dis += "顶";
					}
					if("1".equals(cancomment)){
						dis += "评";
					}
					disLogo[1] = dis;
					
					String[] countComment = new String[2];
					countComment[0] = "string";
					int commentNum = articles.getToaInfopublishArticle().getToaInfopublishComments().size();
					countComment[0] = "string";
					countComment[1] =String.valueOf(commentNum);
					
					String[] publishtime = new String[2];
					publishtime[0] = "date";
					publishtime[1] = articles.getToaInfopublishArticle().getArticlesAutopublishtime().toString() == null ? ""
									: articles.getToaInfopublishArticle().getArticlesAutopublishtime().toString();
					
					result.add(columnArticleId);					
					result.add(columnName);
					result.add(articlesTitle);
					result.add(articlesAuthor);
					result.add(Hits);
					result.add(disLogo);
					result.add(countComment);
					result.add(publishtime);
				}
			}
			
			ret = dom.createItemsResponseData(STATUS_SUC, null, result,8,
					String.valueOf(pageArticles.getTotalCount()),String.valueOf(pageArticles.getTotalPages()));
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取栏目文章发生数据级异常:"+JavaUtils.stackToString(ex), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取栏目文章发生服务级异常:"+JavaUtils.stackToString(ex), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取栏目文章发生系统级异常:"+JavaUtils.stackToString(ex), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取栏目文章发生未知异常:"+JavaUtils.stackToString(ex), null, null);
		}
		logger.info(ret);
		return ret;
		
	}
	/**
	* @method getColumnArticles
	* @author 申仪玲
	* @created 2012-08-30 下午18:36:10
	* @description 获取栏目文件列表
	* @return Date 返回类型
	*/
	public String getColumnArticles(String columnId,String columnArticlesTitle ,String pageSize,String pageNo)throws DAOException,ServiceException,SystemException{
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		try {
			
			if(pageSize == null || "".equals(pageSize)) {
				throw new SystemException("参数pageSize不可为空！");
			}
			if(pageNo == null || "".equals(pageNo)) {
				throw new SystemException("参数pageNo不可为空！");
			}
			Page<ToaInfopublishColumnArticl> page = new Page<ToaInfopublishColumnArticl>(Integer.parseInt(pageSize),true);
			page.setPageNo(Integer.parseInt(pageNo));

			List<String[]> result = new ArrayList<String[]>();
			
			Page<ToaInfopublishColumnArticl> pageArticles = articlesManager.getColumnArticles(columnId,columnArticlesTitle,page);
			List<ToaInfopublishColumnArticl> list = pageArticles.getResult();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(list != null && !list.isEmpty()) {
				for(ToaInfopublishColumnArticl articles : list) {
					
					String[] columnArticleId = new String[2];
					columnArticleId[0] = "String";
					columnArticleId[1] = articles.getColumnArticleId();
					
					String[] columnName = new String[2];
					columnName[0] = "string";
					columnName[1] = articles.getToaInfopublishColumn().getClumnName();
					
					
					String[] articlesTitle = new String[2];
					articlesTitle[0] = "string";
					articlesTitle[1] = articles.getToaInfopublishArticle().getArticlesTitle();
					
					String[] articlesAuthor = new String[2];
					articlesAuthor[0] = "string";
					articlesAuthor[1] = articles.getToaInfopublishArticle().getArticlesAuthor()==null?"":articles.getToaInfopublishArticle().getArticlesAuthor();
					
					String[] Hits = new String[2];
					Hits[0] = "string";
					Hits[1] = articles.getToaInfopublishArticle().getArticlesHits() == null ? "0" 
										: articles.getToaInfopublishArticle().getArticlesHits().toString();
					
					String[] disLogo = new String[2];
					disLogo[0] = "string";
					String guidetype = articles.getColumnArticleGuidetype();
					String ishot = articles.getToaInfopublishArticle().getArticlesIshot();
					String isstandtop = articles.getColumnArticleIsstandtop();
					String cancomment = articles.getToaInfopublishArticle().getArticlesIscancomment();
					String dis = "";
					if("1".equals(guidetype)){
						dis += "导";
					}
					if("1".equals(ishot)){
						dis += "热";
					}
					if("1".equals(isstandtop)){
						dis += "顶";
					}
					if("1".equals(cancomment)){
						dis += "评";
					}
					disLogo[1] = dis;
					
					String[] countComment = new String[2];
					countComment[0] = "string";
					int commentNum = articles.getToaInfopublishArticle().getToaInfopublishComments().size();
					countComment[0] = "string";
					countComment[1] =String.valueOf(commentNum);
					
					String[] publishtime = new String[2];
					publishtime[0] = "date";
					publishtime[1] = articles.getToaInfopublishArticle().getArticlesAutopublishtime() == null ? ""
									: sf.format(articles.getToaInfopublishArticle().getArticlesAutopublishtime());
					
					result.add(columnArticleId);					
					result.add(columnName);
					result.add(articlesTitle);
					result.add(articlesAuthor);
					result.add(Hits);
					result.add(disLogo);
					result.add(countComment);
					result.add(publishtime);
				}
			}
			
			ret = dom.createItemsResponseData(STATUS_SUC, null, result,8,
					String.valueOf(pageArticles.getTotalCount()),String.valueOf(pageArticles.getTotalPages()));
		} catch (DAOException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取栏目文章发生数据级异常:"+JavaUtils.stackToString(ex), null, null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取栏目文章发生服务级异常:"+JavaUtils.stackToString(ex), null, null);
		} catch (SystemException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取栏目文章发生系统级异常:"+JavaUtils.stackToString(ex), null, null);
		} catch (Exception ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "读取栏目文章发生未知异常:"+JavaUtils.stackToString(ex), null, null);
		}
		logger.info(ret);
		return ret;
		
	}
	/**
	 * 得到栏目文章内容
	 * @param columnArticleId					文章id
	 * @param pageNow                           当前页数
	 * @return
	 * 	服务调用成功时返回数据格式如下：
			<?xml version="1.0" encoding="UTF-8"?>
			<service-response>
				<status>1</status>
				<fail-reason />
				<data totalPages = "总页数">
					<item type="string" value="文章内容" />
				</data>
			</service-response>
		服务调用失败时返回数据格式如下：
			<?xml version="1.0" encoding="UTF-8"?>
			<service-response>
				<status>0</status>
				<fail-reason>异常描述</fail-reason>
				<data />
			</service-response>
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public String loadArticleContent(String columnArticleId, int pageNow) throws DAOException,ServiceException,SystemException {
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		int totalPage = 1;
		int  pagesize;
		try {
		ToaInfopublishColumnArticl columnArticl = articlesManager.getColumnArticl(columnArticleId);
		if(columnArticl==null){
			ret = dom.createItemResponseData(STATUS_FAIL, "获取文章内容失败", null, null);
			return ret;
		}
		String articleContent = columnArticl.getToaInfopublishArticle().getArticlesArticlecontent();// 获取文章内容		
		/**
		pagesize = columnArticl.getToaInfopublishArticle().getArticlesPaginationnum();	
		int length = 0;
		if (articleContent != null && !"".equals(articleContent) && pagesize > 0){// 判断内容是否为空
			length = articleContent.length();
			totalPage = length % pagesize == 0 ? length / pagesize : length
					/ pagesize + 1;
		}	
		if(pageNow < 1 || pageNow > totalPage ){//限制传入pageNow的大小
			pageNow = 1;
		}
		
		int begin = (pageNow-1) * pagesize;
		int end = begin + pagesize;
		
		if (articleContent != null && length > pagesize) {
			if (length >= end) {
				articleContent = articleContent.substring(begin, end);
			} else {
				articleContent = articleContent.substring(begin);
			}
		}
		*/
		ret = dom.createItemsResponseData(STATUS_SUC, null, "string", articleContent,
				String.valueOf(totalPage));
		
	} catch (DAOException ex) {
		ret = dom.createItemResponseData(STATUS_FAIL, "获取文章内容发生数据级异常:"+JavaUtils.stackToString(ex), null, null);
	}  catch (ServiceException ex) {
		ret = dom.createItemResponseData(STATUS_FAIL, "获取文章内容发生服务级异常:"+JavaUtils.stackToString(ex), null, null);
	} catch (SystemException ex) {
		ret = dom.createItemResponseData(STATUS_FAIL, "获取文章内容发生系统级异常:"+JavaUtils.stackToString(ex), null, null);
	} catch (Exception ex) {
		ret = dom.createItemResponseData(STATUS_FAIL, "获取文章内容发生未知异常:"+JavaUtils.stackToString(ex), null, null);
	}
	logger.info(ret);
	return ret;
		
	}


}
