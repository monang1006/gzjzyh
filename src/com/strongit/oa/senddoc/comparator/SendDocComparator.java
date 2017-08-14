package com.strongit.oa.senddoc.comparator;

import java.util.Date;

import com.strongit.oa.common.workflow.comparator.BaseComparator;

public class SendDocComparator<T> extends BaseComparator<Object[]> {
	public SendDocComparator() {
	}

	public SendDocComparator(int sortType) {
		setSortType(sortType);
	}

	public SendDocComparator(int sortType, int index) {
		setSortType(sortType);
		setIndex(index);
	}

	public int compare(Object[] o1, Object[] o2) {
		Date date1 = (Date) o1[getIndex()];
		if (date1 == null) {
			date1 = new Date();
		}
		Date date2 = (Date) o2[getIndex()];
		if (date2 == null) {
			date2 = new Date();
		}
		Long time1 = date1.getTime();
		Long time2 = date2.getTime();
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
