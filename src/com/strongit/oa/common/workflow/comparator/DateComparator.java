package com.strongit.oa.common.workflow.comparator;

/**
 * @author yanjian
 * 
 * @createTime Aug 12, 2011 日期比较器
 */
public class DateComparator<T> extends BaseComparator<String[]> {
	public DateComparator() {
	}

	public DateComparator(int sortType) {
		setSortType(sortType);
	}

	public DateComparator(int sortType, int index) {
		setSortType(sortType);
		setIndex(index);
	}

	public int compare(String[] o1, String[] o2) {
//		Long time1 = Long.parseLong(o1[2]);
//		Long time2 = Long.parseLong(o2[2]);
		String str1=o1[getIndex()];
		if(str1==null){
			str1="0";
		}
		String str2=o2[getIndex()];
		if(str2==null){
			str2="0";
		}
		Long time1 = Long.parseLong(str1);
		Long time2 = Long.parseLong(str2);
		Long key1;
		if (time1 != null) {
			key1 = time1;
		} else {
			key1 = Long.MAX_VALUE;
		}
		Long key2;
		if (time2 != null) {
			key2 = time2;
		} else {
			key2 = Long.MAX_VALUE;
		}
		if (getSortType() == 1) {
			return key1.compareTo(key2);
		} else {
			return key2.compareTo(key1);
		}
	}
}
