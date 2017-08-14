package com.strongit.oa.common.workflow.comparator;

import java.util.Comparator;

/**
 * @author yanjian
 * 
 * @param <T>
 */
public class BaseComparator<T> implements Comparator<T> {
	
	/**
	 * @param sortType 排序方式
	 */
	private static int sortType = 1;

	/**
	 * @param index 比较数据所在数组的索引位置
	 */
	private static int index = 0;

	public synchronized int getSortType() {
		return sortType;
	}

	/**
	 * 设置排序方式
	 * @author 严建
	 * @param sortType
	 * @createTime Jan 9, 2012 10:22:48 AM
	 */
	public synchronized void setSortType(int sortType) {
		BaseComparator.sortType = sortType;
	}

	public synchronized void setSortType(int sortType, int index) {
		BaseComparator.sortType = sortType;
		BaseComparator.index = index;
	}

	public int compare(T t1, T t2) {
		return 0;
	}

	public static int getIndex() {
		return index;
	}

	/**
	 * 设置比较数据所在数组的索引位置
	 * @author 严建
	 * @param index
	 * @createTime Jan 9, 2012 10:22:16 AM
	 */
	public static void setIndex(int index) {
		BaseComparator.index = index;
	}

}
