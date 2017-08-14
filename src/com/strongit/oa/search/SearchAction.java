/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-26
 * Autour: qindh
 * Version: V1.0
 * Description： 全文检索
 */
package com.strongit.oa.search;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongit.plugin.search.SearchService;
import com.strongit.plugin.search.compass.CompassPage;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results({})
public class SearchAction extends BaseActionSupport{
	
	private String searchContent;
	private SearchService searchService;
	private int pageNo = 1;//当前页数
	private CompassPage compassPage=new CompassPage();
	private ArticlesManager articlesManager;
	private SearchManager searchManager;
	private List<ToaInfopublishColumnArticl> list;
	/**
	 * 全文检索,搜索
	 * qindh
	 * @return
	 * @throws Exception
	 */
	public String searchContent() throws Exception{
		searchContent = java.net.URLDecoder.decode(searchContent, "utf-8");
//		searchContent = new String(searchContent.getBytes("ISO8859_1"), "utf-8");
		searchService = new SearchService();
		compassPage.setPageSize(10);//传入分页大小
		//compassPage.setQuery(searchContent+" +field9=INFOPUB9");//传入搜索内容
		compassPage.setQuery(searchContent);//传入搜索内容
		compassPage.setPageNumber(pageNo);//传入当前页
		compassPage=searchService.getResult(compassPage);//返回结果Page对象
		return "search";
	}
	
	public String searchContent1() throws Exception{
		searchContent = java.net.URLDecoder.decode(searchContent, "utf-8");
//		searchContent = new String(searchContent.getBytes("ISO8859_1"), "utf-8");
		searchService = new SearchService();
		compassPage.setPageSize(10);//传入分页大小
		compassPage.setQuery(searchContent+" +field9=ARCHIVEFILE9");//传入搜索内容
		//compassPage.setQuery(searchContent);//传入搜索内容
		compassPage.setPageNumber(pageNo);//传入当前页
		compassPage=searchService.getResult(compassPage);//返回结果Page对象
		return "search";
	}
	/**
	 * 更新索引(目前只更新信息发布)
	 * qindh
	 * @return
	 * @throws Exception
	 */
	public String UpdateAllSearchIndex() throws Exception{
		list = articlesManager.getColumnArticleAllList();
		ToaInfopublishColumnArticl bo = null;
		for(Iterator iter=list.iterator();iter.hasNext();){
			bo = (ToaInfopublishColumnArticl) iter.next();
			searchManager.updetIndex(bo);
		}
		return renderHtml("<script> alert('全文检索所有索引全部更新完毕');<script>");
	}
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	public Object getModel() {
		return null;
	}

	public String getSearchContent() {
		return searchContent;
	}

	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}

	public CompassPage getCompassPage() {
		return compassPage;
	}

	public void setCompassPage(CompassPage compassPage) {
		this.compassPage = compassPage;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	@Autowired
	public void setArticlesManager(ArticlesManager articlesManager) {
		this.articlesManager = articlesManager;
	}

	public List<ToaInfopublishColumnArticl> getList() {
		return list;
	}

	public void setList(List<ToaInfopublishColumnArticl> list) {
		this.list = list;
	}
	@Autowired
	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}



}
