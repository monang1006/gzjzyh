package com.strongit.oa.infopub.statistic;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-5-15 上午09:25:38
 * Autour: hull
 * Version: V1.0
 * Description：信息发布统计类
 */
public class ArticleStatistic {
	
   private String columnId;//ID
   private String columnName;//名称
   private int articleCount;//文章数量
   private int pubCount;//发布数量
   
public String getColumnId() {
	return columnId;
}
public void setColumnId(String columnId) {
	this.columnId = columnId;
}
public String getColumnName() {
	return columnName;
}
public void setColumnName(String columnName) {
	this.columnName = columnName;
}
public int getArticleCount() {
	return articleCount;
}
public void setArticleCount(int articleCount) {
	this.articleCount = articleCount;
}
public int getPubCount() {
	return pubCount;
}
public void setPubCount(int pubCount) {
	this.pubCount = pubCount;
}

}
