/**
 * 
 */
package com.strongit.oa.webservice.server.columnArticles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.axis.utils.JavaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.util.Dom4jUtil;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.service.ServiceLocator;

/**
 * @description 供外部调用的知识中心信息栏目webservice接口
 * @className columnArticlesWebService
 * @company Strongit Ltd. (C) copyright
 * @author 申仪玲
 * @email shenyl@strongit.com.cn
 * @created 2011-11-7 下午03:21:24
 * @version 3.0
 */
public class columnArticlesWebService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private static String STATUS_SUC  = "1";//返回成功状态
	private static String STATUS_FAIL = "0";//返回失败状态
	
	private ArticlesManager articlesManager;
	
	/**
	* @构造函数
	* @description 构造方法获取articlesManager对象
	* @param logger
	* @param articlesManager
	*/
	public columnArticlesWebService() {
		articlesManager = (ArticlesManager)ServiceLocator.getService("articlesManager");
		logger.info("栏目文章服务类初始化完毕.。。。");
	}
	
	/**
	* @method parseDate
	* @author 申仪玲
	* @created 2011-11-7 下午03:46:10
	* @description 日期格式化
	* @return Date 返回类型
	*/
	private Date parseDate(String dateStr) throws ParseException,SystemException {
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
	 * 提供获取栏目文章的接口,支持分页、查询.
	 * @param pageNo					页码
	 * @param pageSize					每页显示待办事宜数量
	 * @return							XML格式字符的待办事宜数据
	 * 	服务调用成功时返回数据格式如下：
			<?xml version="1.0" encoding="UTF-8"?>
			<service-response>
				<status>1</status>
				<fail-reason />
				<data totalCount="总记录数" totalPages="总页数">
					<row>
						<item type="string" value="文章ID"/> 
						<item type="string" value="栏目名称"/> 	 
						<item type="string" value="文章标题"/>   
						<item type="string" value="文章作者"/>   
						<item type="string" value="点击次数"/>  	
						<item type="string" value="文章属性"/>		
						<item type="string" value="评论次数"/>	
						<item type="date" value="发布时间"/>		
					</row>	
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
	@SuppressWarnings("unchecked")
	public String getColumnArticles(String pageSize,String pageNo)throws DAOException,ServiceException,SystemException{
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

			List<String[]> result = new ArrayList<String[]>();
			
			Page pageArticles = articlesManager.getColumnArticleAll(page);
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
	 * 根据用户ID获取有权限栏目下文章的接口,支持分页、查询.
	 * @param pageNo					页码
	 * @param pageSize					每页显示待办事宜数量
	 * @return							XML格式字符的待办事宜数据
	 * 	服务调用成功时返回数据格式如下：
			<?xml version="1.0" encoding="UTF-8"?>
			<service-response>
				<status>1</status>
				<fail-reason />
				<data totalCount="总记录数" totalPages="总页数">
					<row>
						<item type="string" value="文章ID"/> 
						<item type="string" value="栏目名称"/> 	 
						<item type="string" value="文章标题"/>   
						<item type="string" value="文章作者"/>   
						<item type="string" value="点击次数"/>  	
						<item type="string" value="文章属性"/>		
						<item type="string" value="评论次数"/>	
						<item type="date" value="发布时间"/>		
					</row>	
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
	@SuppressWarnings("unchecked")
	public String getColumnArticles(String userId, String pageSize,String pageNo)throws DAOException,ServiceException,SystemException{
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

			List<String[]> result = new ArrayList<String[]>();
			
			Page pageArticles = articlesManager.getColumnArticleAll(userId,page);
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
		
		pagesize = columnArticl.getToaInfopublishArticle().getArticlesPaginationnum();
		
		String articleContent = columnArticl.getToaInfopublishArticle().getArticlesArticlecontent();// 获取文章内容
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
