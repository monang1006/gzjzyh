package com.strongit.oa.common.workflow.comparator;

import java.util.Map;

/**
 * @author yanjian
 * 
 * @createTime Aug 12, 2011 人员比较器
 */
public class PersonComparator<T> extends BaseComparator<String[]> {
	private Map<String, Long> userMap;

	public PersonComparator(int sortType, Map<String, Long> userMap) {
		setSortType(sortType);
		this.userMap = userMap;
	}

	public PersonComparator() {
	}
	public PersonComparator(int sortType,int index, Map<String, Long> userMap) {
		setIndex(index);
		setSortType(sortType);
		setUserMap(userMap);
	}
	public int compare(String[] o1, String[] o2) {
//		Long userSequence1 = getUserMap().get(o1[1]);
//		Long userSequence2 = getUserMap().get(o2[1]);
		String str1=o1[getIndex()];
		if(str1==null){
			str1="";
		}
		String str2=o2[getIndex()];
		if(str2==null){
			str2="";
		}
		Long userSequence1 = getUserMap().get(str1);
		Long userSequence2 = getUserMap().get(str2);
		Long key1;
		if (userSequence1 != null) {
			key1 = userSequence1;
		} else {
			key1 = Long.MAX_VALUE;
		}
		Long key2;
		if (userSequence2 != null) {
			key2 = userSequence2;
		} else {
			key2 = Long.MAX_VALUE;
		}
		if (getSortType() == 1) {
			return key1.compareTo(key2);
		} else {
			return key2.compareTo(key1);
		}
	}

	private Map<String, Long> getUserMap() {
		return userMap;
	}

	private void setUserMap(Map<String, Long> userMap) {
		this.userMap = userMap;
	}

}
