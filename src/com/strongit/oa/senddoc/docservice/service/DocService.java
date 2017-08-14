/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.senddoc.docservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.senddoc.docservice.IDocService;
import com.strongit.oa.senddoc.manager.SendDocBaseManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.annotation.OALogger;

/**
 * 处理业务表中的字段信息
 * 
 * @author dengwenqiang
 * @version 1.0
 */
@Service
@Transactional
@OALogger
public class DocService implements IDocService {

	@Autowired
	SendDocBaseManager baseManager;

	@Autowired
	SendDocManager sendDocManager;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 获取公文限时时间
	 * 
	 * @author 严建
	 * @param taskIdList
	 * @return
	 * @createTime Feb 6, 2012 2:15:33 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getTaskIdMapDocTimeOutDate(List<Long> taskIdList) {
		Map<String, String> returnMap = new HashMap<String, String>();
		if (taskIdList != null && !taskIdList.isEmpty()) {
			Map<String, List<String>> tableNameAndpkFieldNameMapPkFieldValueArray = new HashMap<String, List<String>>();
			Map<String, String> idMapTaskid = new HashMap<String, String>();// 公文id【MAp】任务id
			Map<String, String> map = new HashMap<String, String>();// 公文id【Map】任务超时字段
			String tableName = null, pkFieldName = null, pkFieldValue = null;
			for (int i = 0; i < taskIdList.size(); i++) {
				String taskId = taskIdList.get(i).toString();
				String businessId = sendDocManager
						.getFormIdAndBussinessIdByTaskId(taskId)[0];
				String[] args = businessId.split(";");
				tableName = args[0];
				pkFieldName = args[1];
				pkFieldValue = args[2];
				idMapTaskid.put(pkFieldValue, taskId);
				if (tableName.toLowerCase().equals("t_oarecvdoc")) {
					String tableNameAndpkFieldName = tableName + ";"
							+ pkFieldName;
					if (!tableNameAndpkFieldNameMapPkFieldValueArray
							.containsKey(tableNameAndpkFieldName)) {
						List<String> pkFieldValueList = new LinkedList<String>();
						pkFieldValueList.add(pkFieldValue);
						tableNameAndpkFieldNameMapPkFieldValueArray.put(
								tableNameAndpkFieldName, pkFieldValueList);
					} else {
						List<String> pkFieldValueList = tableNameAndpkFieldNameMapPkFieldValueArray
								.get(tableNameAndpkFieldName);
						pkFieldValueList.add(pkFieldValue);
						tableNameAndpkFieldNameMapPkFieldValueArray.put(
								tableNameAndpkFieldName, pkFieldValueList);
					}
				} else {
					map.put(pkFieldValue, null);
				}

			}
			List<String> tableNameAndpkFieldNameList = new ArrayList(
					tableNameAndpkFieldNameMapPkFieldValueArray.keySet());
			if (tableNameAndpkFieldNameList != null
					&& !tableNameAndpkFieldNameList.isEmpty()) {
				StringBuilder hqls = new StringBuilder();
				for (int j = 0; j < tableNameAndpkFieldNameList.size(); j++) {
					String tableNameAndpkFieldName = tableNameAndpkFieldNameList
							.get(j);
					List<String> pkFieldValueList = tableNameAndpkFieldNameMapPkFieldValueArray
							.get(tableNameAndpkFieldName);
					String[] args = tableNameAndpkFieldName.split(";");
					tableName = args[0];
					pkFieldName = args[1];
					StringBuilder params = new StringBuilder();
					for (int k = 0; k < pkFieldValueList.size(); k++) {
						params.append("'" + pkFieldValueList.get(k) + "',");
					}

					StringBuilder hql = new StringBuilder();
					hql.append("select t" + j + ".").append(pkFieldName)
							.append(" as id,t" + j + ".END_TIME ").append(
									"from ").append(tableName).append(
									" t" + j + " where  t" + j + ".").append(
									pkFieldName).append(" in(").append(
									params.deleteCharAt(params.length() - 1))
							.append(")");
					if (j < tableNameAndpkFieldNameList.size() - 1) {
						hql.append(" UNION  ");
					}
					hqls.append(hql);
				}
				logger.info(hqls.toString());

				List sss = sendDocManager.queryForList(hqls.toString());
				List sssss = new ArrayList(sss);
				for (int i = 0; i < sssss.size(); i++) {
					Map tempMap = (ListOrderedMap) sssss.get(i);
					String endtime = null;
					if (tempMap.get("END_TIME") != null) {
						endtime = (String) tempMap.get("END_TIME");
					}
					String id = tempMap.get("id").toString();
					map.put(id, endtime);
				}
			}

			List<String> list = new ArrayList<String>(idMapTaskid.keySet());
			for (int i = 0; i < list.size(); i++) {
				String id = list.get(i);
				String taskId = idMapTaskid.get(id);
				String endtime = map.get(id);
				returnMap.put(taskId, endtime);
			}
		}
		return returnMap;
	}

	/**
	 * @description 根据任务实例id获取公文超期的期限(字符串格式：yyyy-mm-dd)
	 * @author 严建
	 * @createTime Dec 15, 2011 4:38:25 PM
	 * @return String
	 */
	public String getDocTimeOutDate(String taskId) {
		String businessId = sendDocManager
				.getFormIdAndBussinessIdByTaskId(taskId)[0];
		String tablename = businessId.split(";")[0];
		String pkKey = businessId.split(";")[1];
		String pkValue = businessId.split(";")[2];
		String sql = "select * from " + tablename + " t where t." + pkKey
				+ " = '" + pkValue + "'";
		List list = sendDocManager.queryForList(sql);
		ListOrderedMap o = (ListOrderedMap) list.get(0);
		String endtime = null;
		if (o.get("END_TIME") != null) {
			endtime = (String) o.get("END_TIME");
		}
		return endtime;
	}

	/**
	 * 
	 * 通过业务id的Map格式数据获取对应业务数据流程标题和紧急程度信息
	 * 
	 * @author 严建
	 * @param tableNameAndpkFieldNameMapPkFieldValueArray
	 * @return 数据类型Map;
	 *         数据格式--Key(String):【业务数据的主键值】;Value(Map):【流程标题和紧急程度信息的Map形式[WORKFLOWTITLE:值1,PERSON_CONFIG_FLAG:值2]】
	 * @createTime Feb 1, 2012 4:07:17 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map> getPKMapDocWorkflowtitleAndPersonConfigFlag(
			Map<String, List<String>> tableNameAndpkFieldNameMapPkFieldValueArray) {
		return getPKMapDocWorkflowtitleAndPersonConfigFlag(
				tableNameAndpkFieldNameMapPkFieldValueArray, null);
	}

	/**
	 * 
	 * 通过业务id的Map格式数据获取对应业务数据流程标题和紧急程度信息
	 * 
	 * @author 严建
	 * @param tableNameAndpkFieldNameMapPkFieldValueArray
	 * @param listType
	 * @return 数据类型Map;
	 *         数据格式--Key(String):【业务数据的主键值】;Value(Map):【流程标题和紧急程度信息的Map形式[WORKFLOWTITLE:值1,PERSON_CONFIG_FLAG:值2]】
	 * @createTime Feb 1, 2012 4:07:17 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map> getPKMapDocWorkflowtitleAndPersonConfigFlag(
			Map<String, List<String>> tableNameAndpkFieldNameMapPkFieldValueArray,
			String listType) {
		if (tableNameAndpkFieldNameMapPkFieldValueArray == null
				|| tableNameAndpkFieldNameMapPkFieldValueArray.isEmpty()) {
			return null;
		}
		String tableName = null, pkFieldName = null;
		StringBuilder hqls = new StringBuilder();
		List<String> tableNameAndpkFieldNameList = new ArrayList<String>(
				tableNameAndpkFieldNameMapPkFieldValueArray.keySet());
		for (int j = 0; j < tableNameAndpkFieldNameList.size(); j++) {
			String tableNameAndpkFieldName = tableNameAndpkFieldNameList.get(j);
			List<String> pkFieldValueList = tableNameAndpkFieldNameMapPkFieldValueArray
					.get(tableNameAndpkFieldName);
			String[] args = tableNameAndpkFieldName.split(";");
			tableName = args[0];
			pkFieldName = args[1];
			StringBuilder params = new StringBuilder();
			for (int k = 0; k < pkFieldValueList.size(); k++) {
				params.append("'" + pkFieldValueList.get(k) + "',");
			}
			StringBuilder hql = new StringBuilder();

			hql.append("select t" + j + ".").append(pkFieldName).append(
					" as id,t" + j + ".WORKFLOWTITLE,t" + j
							+ ".PERSON_CONFIG_FLAG from ").append(tableName)
					.append(" t" + j + " where  t" + j + ".").append(
							pkFieldName).append(" in(").append(
							params.deleteCharAt(params.length() - 1)).append(
							")");
			if (listType != null && "jj_div".equals(listType)) {// 急件
				hql.append("  and  t" + j + ".PERSON_CONFIG_FLAG >0  ");
			}
			if (j < tableNameAndpkFieldNameList.size() - 1) {
				hql.append(" UNION  ");
			}
			hqls.append(hql);
		}
		logger.info(hqls.toString());
		List sss = sendDocManager.queryForList(hqls.toString());
		List sssss = new ArrayList(sss);
		Map<String, Map> map = new HashMap<String, Map>();
		for (int i = 0; i < sssss.size(); i++) {
			Map tempMap = (ListOrderedMap) sssss.get(i);
			String id = tempMap.get("id").toString();
			map.put(id, tempMap);
		}
		return map;
	}
}
