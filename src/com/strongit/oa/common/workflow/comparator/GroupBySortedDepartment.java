package com.strongit.oa.common.workflow.comparator;

import java.util.Collections;
import java.util.List;

import com.strongit.oa.common.workflow.comparator.groupby.GroupByDepartment;

/**
 * @author yanjian
 *
 * @createTime Aug 13, 2011  部门分组排序
 */
public class GroupBySortedDepartment {
	private GroupByDepartment groupByDepartment;

	private DepartmentComparator<String[]> departmentComparator;

	public GroupBySortedDepartment() {
	}

	public GroupBySortedDepartment(
			DepartmentComparator<String[]> departmentComparator,
			GroupByDepartment groupByDepartment) {
		setDepartmentComparator(departmentComparator);
		setGroupByDepartment(groupByDepartment);
	}

	// 返回分组排序之后的结果
	/**
	 * @author 严建
	 * @createTime Aug 13, 2011
	 * @description
	 */
	public List<List<String[]>> getGroupbySortedResult() {
		List<List<String[]>> groupByList = getGroupByDepartment().groupBy();
		for (List<String[]> valueList : groupByList) {
			Collections.sort(valueList, getDepartmentComparator());
		}
		return groupByList;
	}

	private GroupByDepartment getGroupByDepartment() {
		return groupByDepartment;
	}

	private void setGroupByDepartment(GroupByDepartment groupByDepartment) {
		this.groupByDepartment = groupByDepartment;
	}

	private DepartmentComparator<String[]> getDepartmentComparator() {
		return departmentComparator;
	}

	private void setDepartmentComparator(
			DepartmentComparator<String[]> departmentComparator) {
		this.departmentComparator = departmentComparator;
	}

}
