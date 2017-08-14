/*jode*/
/* QueryParameter - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package com.strongmvc.orm.hibernate;
import org.apache.commons.lang.StringUtils;

/**
 * QueryParameter 覆盖jar包中page父类
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Mar 19, 2012 6:07:59 PM
 * @version      1.0.0.0
 * @classpath    com.strongmvc.orm.hibernate.QueryParameter
 */
public class QueryParameter
{
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    protected int pageNo = 1;
    protected int pageSize = -1;
    protected String orderBy = null;
    protected String order = "asc";
    protected boolean autoCount = false;
    protected int  pageNoBak= 1;
    protected int pageSizeBak = -1;
    
    public int getPageNoBak() {
		return pageNoBak;
	}

	public void setPageNoBak(int pageNoBak) {
		this.pageNoBak = pageNoBak;
	}

	public int getPageSizeBak() {
		return pageSizeBak;
	}

	public void setPageSizeBak(int pageSizeBak) {
		this.pageSizeBak = pageSizeBak;
	}

	public int getPageSize() {
	return pageSize;
    }
    
    public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
    }
    
    public boolean isPageSizeSetted() {
	if (pageSize > -1)
	    return true;
	return false;
    }
    
    public int getPageNo() {
	return pageNo;
    }
    
    public void setPageNo(int pageNo) {
	this.pageNo = pageNo;
    }
    
    public int getFirst() {
	if (pageNo < 1 || pageSize < 1)
	    return 0;
	return (pageNo - 1) * pageSize;
    }
    
    public boolean isFirstSetted() {
	if (pageNo > 0 && pageSize > 0)
	    return true;
	return false;
    }
    
    public String getOrderBy() {
	return orderBy;
    }
    
    public void setOrderBy(String orderBy) {
	this.orderBy = orderBy;
    }
    
    public boolean isOrderBySetted() {
	return StringUtils.isNotBlank(orderBy);
    }
    
    public String getOrder() {
	return order;
    }
    
    public void setOrder(String order) {
	if ("asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order))
	    this.order = order.toLowerCase();
	else
	    throw new IllegalArgumentException
		      ("order should be 'desc' or 'asc'");
    }
    
    public boolean isAutoCount() {
	return autoCount;
    }
    
    public void setAutoCount(boolean autoCount) {
	this.autoCount = autoCount;
    }
}