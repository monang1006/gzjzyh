package com.strongit.oa.common.workflow.comparator.groupby;

import java.util.Collections;
import java.util.List;

import com.strongit.oa.common.workflow.comparator.BaseComparator;
import com.strongit.oa.common.workflow.comparator.util.SortConst;

/**
 * @author yanjian
 * 
 * @createTime Aug 13, 2011 分组之后，使用GroupByAfterSorted类的方法进行排序
 */
public class GroupByAfterSorted {
	private List<List<String[]>> groupByList;

	private BaseComparator<String[]> comparator;
	
	public GroupByAfterSorted() {
	}

	public GroupByAfterSorted(List<List<String[]>> groupByList,
			BaseComparator<String[]> comparator) {
		setGroupByList(groupByList);
		setComparator(comparator);
	}

	public List<String[]> getSortedResult() {
		System.out.println(getGroupByList());
		List<List<String[]>> results = getGroupByList();
		System.out.println(results);
		for (List<String[]> result : results) {
			Collections.sort(result, getComparator());
		}
		return SortConst.transList1StringArray(results);
	}

	private List<List<String[]>> getGroupByList() {
		return groupByList;
	}

	private void setGroupByList(List<List<String[]>> groupByList) {
		this.groupByList = groupByList;
	}

	private BaseComparator<String[]> getComparator() {
		return comparator;
	}

	private void setComparator(BaseComparator<String[]> comparator) {
		this.comparator = comparator;
	}

}
