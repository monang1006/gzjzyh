package com.strongit.oa.bgt.documentview.bo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * 文件监管（文件办理情况）功能辅助实体信息
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Apr 5, 2012 2:00:13 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.bgt.documentview.bo.DocumentViewUtil
 */
public class DocumentViewUtil {
	private Map<String, String> userIdMapOrgId = new HashMap<String, String>();

	private Map<String, Integer> orgIdMapCounter = new HashMap<String, Integer>();
	
	private Map<String, Integer> orgIdMapCounter0 = new HashMap<String, Integer>();//超时
	
	private Map<String, Integer> orgIdMapCounter1 = new HashMap<String, Integer>();//不超时

	private Map<String, Long> orgIdMapOrgSqe = new HashMap<String, Long>();

	private Map<String, Long> orgNameMapOrgSqe = new HashMap<String, Long>();

	private Map<String, String> orgIdMapOrgName = new HashMap<String, String>();

	private Map<String, String> orgNameMapOrgId = new HashMap<String, String>();

	private Map<String, Integer> orgNameMapCounter = new HashMap<String, Integer>();

	/**
	 * 初始化数据
	 * 
	 * @description
	 * @author 严建
	 * @param userAndOrgInfoList
	 * @createTime Apr 5, 2012 2:00:54 PM
	 */
	public DocumentViewUtil(List<UserAndOrgInfo> userAndOrgInfoList) {
		for (UserAndOrgInfo userAndOrgInfo : userAndOrgInfoList) {
			userIdMapOrgId.put(userAndOrgInfo.getUserId(), userAndOrgInfo
					.getOrgId());
			orgIdMapCounter.put(userAndOrgInfo.getOrgId(), new Integer(0));
			orgIdMapCounter0.put(userAndOrgInfo.getOrgId(), new Integer(0));
			orgIdMapCounter1.put(userAndOrgInfo.getOrgId(), new Integer(0));
			orgIdMapOrgSqe.put(userAndOrgInfo.getOrgId(), userAndOrgInfo
					.getOrgSequence());
			orgIdMapOrgName.put(userAndOrgInfo.getOrgId(), userAndOrgInfo
					.getOrgName());
			orgNameMapCounter.put(userAndOrgInfo.getOrgName(), new Integer(0));
			orgNameMapOrgSqe.put(userAndOrgInfo.getOrgName(), userAndOrgInfo
					.getOrgSequence());
			orgNameMapOrgId.put(userAndOrgInfo.getOrgName(), userAndOrgInfo
					.getOrgId());
		}
	}

	/**
	 * 获取所属机构的公文命中情况
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 5, 2012 2:01:11 PM
	 */
	public Map<String, Integer> getOrgIdMapCounter() {
		return orgIdMapCounter;
	}

	/**
	 * 
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 5, 2012 2:01:47 PM
	 */
	public Map<String, String> getUserIdMapOrgId() {
		return userIdMapOrgId;
	}

	/**
	 * 添加命中信息
	 * 
	 * @description
	 * @author 严建
	 * @param userId
	 * @createTime Apr 5, 2012 2:03:02 PM
	 */
	public void addUserId(String userId) {
		if (userIdMapOrgId.containsKey(userId)) {
			String tempOrgId = userIdMapOrgId.get(userId);
			if (orgIdMapCounter.containsKey(tempOrgId)) {
				orgIdMapCounter.put(tempOrgId,
						orgIdMapCounter.get(tempOrgId) + 1);
			}
		}
	}

	/**
	 * 超时或非超时命中
	 * 
	 * @description
	 * @author 严建
	 * @param userId
	 * @param processTimeout
	 * @createTime Apr 17, 2012 5:45:53 PM
	 */
	public void addUserId(String userId, String processTimeout) {
        	Map<String, Integer> orgidmapcounter = null;
        	if ("1".equals(processTimeout)) {
        	    orgidmapcounter = orgIdMapCounter1;
        	} else {
        	    orgidmapcounter = orgIdMapCounter0;
        	}
        	if (userIdMapOrgId.containsKey(userId)) {
        	    String tempOrgId = userIdMapOrgId.get(userId);
        	    if (orgidmapcounter.containsKey(tempOrgId)) {
        		orgidmapcounter.put(tempOrgId,
        			orgidmapcounter.get(tempOrgId) + 1);
        	    }
        	}
        }
	/**
         * 添加命中信息
         * 
         * @description
         * @author 严建
         * @param deptName
         * @createTime Apr 9, 2012 4:56:07 PM
         */
	public void addDeptName(String deptName) {
		if (orgNameMapCounter.containsKey(deptName)) {
			orgNameMapCounter.put(deptName,
					orgNameMapCounter.get(deptName) + 1);
		}
	}

	/**
	 * 获取排序之后的机构id列表
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 5, 2012 9:21:04 PM
	 */
	public List<String> getOrgIdListSorted() {
		List<String> list = null;
		if (orgIdMapOrgSqe != null && !orgIdMapOrgSqe.isEmpty()) {
			list = new LinkedList<String>(orgIdMapOrgSqe.keySet());
			Collections.sort(list, new Comparator<String>() {

				public int compare(String arg0, String arg1) {
					// TODO Auto-generated method stub
					Long userSequence1 = orgIdMapOrgSqe.get(arg0);
					Long userSequence2 = orgIdMapOrgSqe.get(arg1);
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
					return key1.compareTo(key2);
				}

			});
		}
		if (list != null) {
			list = new ArrayList<String>(list);
		}
		return list;
	}

	/**
	 * 获取排序之后的机构name列表
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 5, 2012 9:21:04 PM
	 */
	public List<String> getOrgNameListSorted() {
		List<String> list = null;
		if (orgNameMapOrgSqe != null && !orgNameMapOrgSqe.isEmpty()) {
			list = new LinkedList<String>(orgNameMapOrgSqe.keySet());
			Collections.sort(list, new Comparator<String>() {

				public int compare(String arg0, String arg1) {
					// TODO Auto-generated method stub
					Long userSequence1 = orgNameMapOrgSqe.get(arg0);
					Long userSequence2 = orgNameMapOrgSqe.get(arg1);
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
					return key1.compareTo(key2);
				}

			});
		}
		if (list != null) {
			list = new ArrayList<String>(list);
		}
		return list;
	}

	/**
	 * 获取指定机构的命中信息
	 * 
	 * @description
	 * @author 严建
	 * @param orgId
	 * @return
	 * @createTime Apr 5, 2012 2:03:22 PM
	 */
	public Integer getCounterByOrgId(String orgId) {
		if (orgIdMapCounter.containsKey(orgId)) {
			return orgIdMapCounter.get(orgId);
		} else {
			return new Integer(0);
		}
	}
	/**
	 * 根据机构id和processTimeout，获取指定机构的命中信息
	 * 
	 * @description
	 * @author 严建
	 * @param orgId
	 * @param processTimeout
	 * @return
	 * @createTime Apr 17, 2012 5:47:43 PM
	 */
	public Integer getCounterByOrgIdAndProcessTimeout(String orgId,String processTimeout) {
	    Map<String, Integer> orgidmapcounter = null;
	    if ("1".equals(processTimeout)) {
		orgidmapcounter = orgIdMapCounter1;
	    } else {
		orgidmapcounter = orgIdMapCounter0;
	    }
	    if (orgidmapcounter.containsKey(orgId)) {
		return orgidmapcounter.get(orgId);
	    } else {
		return new Integer(0);
	    }
	}

	/**
	 * 获取指定机构的命中信息
	 * 
	 * @description
	 * @author 严建
	 * @param orgId
	 * @return
	 * @createTime Apr 5, 2012 2:03:22 PM
	 */
	public Integer getCounterByOrgName(String deptName) {
		if (orgNameMapCounter.containsKey(deptName)) {
			return orgNameMapCounter.get(deptName);
		} else {
			return new Integer(0);
		}
	}

	public Map<String, Long> getOrgIdMapOrgSqe() {
		return orgIdMapOrgSqe;
	}

	protected void setOrgIdMapOrgSqe(Map<String, Long> orgIdMapOrgSqe) {
		this.orgIdMapOrgSqe = orgIdMapOrgSqe;
	}

	public Map<String, String> getOrgIdMapOrgName() {
		return orgIdMapOrgName;
	}

	protected void setOrgIdMapOrgName(Map<String, String> orgIdMapOrgName) {
		this.orgIdMapOrgName = orgIdMapOrgName;
	}

	public Map<String, Integer> getOrgNamemapCounter() {
		return orgNameMapCounter;
	}

	protected void setOrgNamemapCounter(Map<String, Integer> orgNameMapCounter) {
		this.orgNameMapCounter = orgNameMapCounter;
	}

	public Map<String, Long> getOrgNameMapOrgSqe() {
		return orgNameMapOrgSqe;
	}

	protected void setOrgNameMapOrgSqe(Map<String, Long> orgNameMapOrgSqe) {
		this.orgNameMapOrgSqe = orgNameMapOrgSqe;
	}

	public Map<String, String> getOrgNameMapOrgId() {
		return orgNameMapOrgId;
	}

	protected void setOrgNameMapOrgId(Map<String, String> orgNameMapOrgId) {
		this.orgNameMapOrgId = orgNameMapOrgId;
	}

	public Map<String, Integer> getOrgIdMapCounter0() {
	    return orgIdMapCounter0;
	}

	public Map<String, Integer> getOrgIdMapCounter1() {
	    return orgIdMapCounter1;
	}
}
