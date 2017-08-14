/**
 * CopyRight © 2008 Jiang Xi Strong Co. Ltd.
 * All rights reserved. 
 * @FileName: Pageable.java
 * @Description: 分页要实现的主要方法接口
 * @author: Jia Dongsheng
 * @JDKVersion: 1.4.2
 * @date: 2008-7-30
 * 
 */
package com.engine.jdbcPage;

public interface Pageable extends java.sql.ResultSet {
	/*** 返回总页数*/
	int getPageCount();
	/*** 返回总记录行数*/
	int getRowsCount();
	/*** 返回当前页的记录条数*/
	int getPageRowsCount();
    /*** 返回分页大小*/
	int getPageSize();
    /*** 转到指定页*/
	void gotoPage(int page);
    /*** 设置分页大小*/
	void setPageSize(int pageSize);
    /*** 转到当前页的第一条记录*/
	void pageFirst() throws java.sql.SQLException;
	/*** 转到当前页的最后一条记录*/
	void pageLast() throws java.sql.SQLException;
	/*** 返回当前页号*/
	int getCurPage();
}
