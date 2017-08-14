package com.strongit.oa.common.workflow.comparator.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yanjian
 * 
 * @createTime Aug 13, 2011 排序帮助类
 */
public class SortConst {
	/* 升序还是降序 */
	public static int SORT_ASC = 1;

	public static int SORT_DESC = 0;

	/* 按什么条件排序 */
	public static String SORT_TYPE_DATE_ASC = "date_asc"; // 按时间升序

	public static String SORT_TYPE_DATE_DESC = "date_desc"; // 按时间降序
	
	public static String SORT_TYPE_TASKSTART_ASC = "taskstart_asc"; // 按任务开顺序升序
	
	public static String SORT_TYPE_TASKSTART_DESC = "taskstart_desc"; // 按任务开始顺序降序

	public static String SORT_TYPE_PERSON_ASC = "personnumber_asc"; // 按人员升序

	public static String SORT_TYPE_PERSON_DESC = "personnumber_desc"; // 按人员降序

	
	public static String SORT_TYPE_DEPARTMENT_ASC_AND_PERSON_ASC = "departmentnumber_asc_and_personnumber_asc"; // 按部门升序,人员升序

	public static String SORT_TYPE_DEPARTMENT_ASC_AND_PERSON_DESC = "departmentnumber_asc_and_personnumber_desc"; // 按部门升序,人员降序

	public static String SORT_TYPE_DEPARTMENT_ASC_AND_DATE_ASC = "departmentnumber_asc_and_date_asc"; // 按部门升序,时间升序

	public static String SORT_TYPE_DEPARTMENT_ASC_AND_DATE_DESC = "departmentnumber_asc_and_date_desc"; // 按部门升序,时间降序
	
	public static String SORT_TYPE_DEPARTMENT_ASC_AND_TASKSTART_ASC = "departmentnumber_asc_and_taskstart_asc"; // 按部门升序,任务开始顺序升序
	
	public static String SORT_TYPE_DEPARTMENT_ASC_AND_TASKSTART_DESC = "departmentnumber_asc_and_taskstart_desc"; // 按部门升序,任务开始顺序降序

	public static String SORT_TYPE_PROCESSSTARTDATE_ASC = "processstartdate_asc"; //按来文时间升序排序

	public static String SORT_TYPE_PROCESSSTARTDATE_DESC = "processstartdate_desc"; //按来文时间降序排序

	public static String SORT_TYPE_TASKENDDATE_ASC = "taskenddate_asc"; //按办文时间升序排序

	public static String SORT_TYPE_TASKENDDATE_DESC = "taskenddate_desc"; //按办文时间降序排序

	/**
	 * @author 严建
	 * @createTime Aug 13, 2011
	 * @description 将2层list转换成1层得list
	 */
	public static List<String[]> transList1StringArray(
			List<List<String[]>> list2StringArray) {
		List<String[]> list1StringArray = new LinkedList<String[]>();
		for (List<String[]> list : list2StringArray) {
			list1StringArray.addAll(list);
		}
		return list1StringArray;
	}
	
}
