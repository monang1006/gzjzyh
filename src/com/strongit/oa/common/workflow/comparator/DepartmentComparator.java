package com.strongit.oa.common.workflow.comparator;

import java.util.Map;

/**
 * @author yanjian
 * 
 * @createTime Aug 12, 2011 部门信息比较器
 */
public class DepartmentComparator<T> extends BaseComparator<String[]> {
	private Map<String, String> departmentMap;

	public DepartmentComparator(int sortType, Map<String, String> departmentMap) {
		setSortType(sortType);
		this.departmentMap = departmentMap;
	}

	public DepartmentComparator() {
	}
	public DepartmentComparator(int sortType,int index, Map<String, String> departmentMap) {
		setIndex(index);
		setSortType(sortType);
		setDepartmentMap(departmentMap);
	}
	public int compare(String[] arg0, String[] arg1) {
//		String departmentId1 = getDepartmentMap().get(arg0[1]);
//		String departmentId2 = getDepartmentMap().get(arg1[1]);
		String str1=arg0[getIndex()];
		if(str1==null){
			str1="";
		}
		String str2=arg1[getIndex()];
		if(str2==null){
			str2="";
		}
		String departmentId1 = getDepartmentMap().get(str1);
		String departmentId2 = getDepartmentMap().get(str2);
		String key1;
		if (departmentId1 != null) {
			key1 = departmentId1;
		} else {
			key1 = "";
		}
		String key2;
		if (departmentId2 != null) {
			key2 = departmentId2;
		} else {
			key2 = "";
		}
		if (getSortType() == 1) {
			return key1.compareTo(key2);
		} else {
			return key2.compareTo(key1);
		}
	}

	private Map<String, String> getDepartmentMap() {
		return departmentMap;
	}

	private void setDepartmentMap(Map<String, String> departmentMap) {
		this.departmentMap = departmentMap;
	}

}
