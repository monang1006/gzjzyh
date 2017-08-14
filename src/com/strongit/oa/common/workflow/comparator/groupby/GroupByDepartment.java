package com.strongit.oa.common.workflow.comparator.groupby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.strongit.oa.common.workflow.comparator.util.SortConst;

/**
 * @author yanjian
 * 
 * @createTime Aug 13, 2011 根据部门排序号分组
 */
public class GroupByDepartment {
	private List<String[]> groupByList;

	private int sortType;

	private static Map<String, String> orgIdOrgSequenceMap = new HashMap<String, String>();

	public GroupByDepartment(List<String[]> groupByList, int sortType) {
		setGroupByList(groupByList);
		setSortType(sortType);
	}

	public List<List<String[]>> groupBy() {
		List<String[]> groupByList = getGroupByList();
		Map<String, List<String[]>> groupByMap = new HashMap<String, List<String[]>>();
		for (int i = 0; i < groupByList.size(); i++) {
			String[] sArrTemp = groupByList.get(i);
			String orgId = sArrTemp[3].toString();
			String orgSequence = sArrTemp[4].toString();
			if (!groupByMap.containsKey(orgId)) {
				List<String[]> list = new ArrayList<String[]>();
				groupByMap.put(orgId, list);
			}
			groupByMap.get(orgId).add(sArrTemp);
			if (!orgIdOrgSequenceMap.containsKey(orgId)) {
				orgIdOrgSequenceMap.put(orgId, orgSequence);
			}
		}
		Set<String> setTemp = groupByMap.keySet();
		List<String> orgIdList = new ArrayList<String>();
		orgIdList.addAll(setTemp);
		Collections.sort(orgIdList, new Comparator<String>() {
			public int compare(String orgId1, String orgId2) {
				Long orgSequence1 = Long.MAX_VALUE;
				Long orgSequence2 = Long.MAX_VALUE;
				if (orgId1 != null) {
					orgSequence1 = Long
							.valueOf(orgIdOrgSequenceMap.get(orgId1));
				}
				if (orgId2 != null) {
					orgSequence2 = Long
							.valueOf(orgIdOrgSequenceMap.get(orgId2));
				}
				if (getSortType() == SortConst.SORT_ASC) {
					return orgSequence1.compareTo(orgSequence2);
				} else {
					return orgSequence2.compareTo(orgSequence1);
				}
			}

		});
		if (orgIdList != null & !orgIdList.isEmpty()) {
			List<List<String[]>> groupByAfterList = new LinkedList<List<String[]>>();
			for (String orgId : orgIdList) {
				groupByAfterList.add(groupByMap.get(orgId));
			}
			return groupByAfterList;
		}
		return null;
	}

	private List<String[]> getGroupByList() {
		return groupByList;
	}

	private void setGroupByList(List<String[]> groupByList) {
		this.groupByList = groupByList;
	}

	private int getSortType() {
		return sortType;
	}

	private void setSortType(int sortType) {
		this.sortType = sortType;
	}

}
