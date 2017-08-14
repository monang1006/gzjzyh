package com.strongit.oa.common.workflow.plugin;


import java.sql.Clob;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.service.DataBaseUtil;
import com.strongit.oa.common.workflow.INodesettingPluginService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.plugin.util.NodesettingPluginConst;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * Service定义节点设置信息可扩展插件接口
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 7, 2012 8:46:08 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.nodesettingplugin.NodesettingPluginService
 */
@Service
@OALogger
public class NodesettingPluginService implements INodesettingPluginService {
	@Autowired
	IWorkflowService workflowService;// 工作流服务类

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SendDocManager manager;
	/**
	 * 根据节点id和插件名称获取插件的值
	 * 
	 * @author 严建
	 * @param nodeId
	 *            节点id
	 * @param PluginName
	 *            插件名称
	 * @return
	 * @createTime Mar 7, 2012 8:43:55 PM
	 */
	public String getNodesettingPluginValue(String nodeId, String PluginName) {
		return getNodesettingPluginValue(getNodesetting(nodeId), PluginName);
	}

	/**
	 * 根据节点设置信息和插件名称获取插件的值
	 * 
	 * @author 严建
	 * @param twfbasenodesetting
	 *            节点设置信息
	 * @param PluginName
	 *            插件名称
	 * @return
	 * @createTime Mar 7, 2012 11:08:40 PM
	 */
	public String getNodesettingPluginValue(
			TwfBaseNodesetting twfbasenodesetting, String PluginName) {
		return twfbasenodesetting.getPlugin(PluginName);
	}

	/**
	 * 根据节点id获取节点设置信息
	 * 
	 * @author 严建
	 * @param nodeId
	 *            节点id
	 * @return
	 * @createTime Mar 7, 2012 8:44:41 PM
	 */
	public TwfBaseNodesetting getNodesetting(String nodeId) {
		return workflowService.getNodesettingByNodeId(nodeId);
	}

	/**
	 * 根据节点id得到节点设置信息.
	 * 
	 * @author:邓志城
	 * @date:2010-8-2 上午10:44:12
	 * @param nodeId
	 *            节点id
	 * @return 节点设置信息 new Object[]{ 最大处理人数,是否允许选择其他人,是否返回经办人 }
	 */
	public Object[] getNodesettingByNodeId(String nodeId) {
		TwfBaseNodesetting setting = workflowService
				.getNodesettingByNodeId(nodeId);
		return new Object[] { setting.getNsTaskMaxactors(),
				setting.getNsTaskCanSelOther(),
				setting.getPlugin(NodesettingPluginConst.PLUGINS_DORETURN_TASKACTOR) };
	}

	/**
	 * 批量获取节点插件信息
	 * 
	 * @author yanjian
	 * @param nsids
	 * @param pluginNames
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 14, 2012 9:57:45 AM
	 */
	@SuppressWarnings("unchecked")
	public Map<Long, Map<String, String>> getNodesettingPluginValueForMap(
			Set<Long> nsids, Set<String> pluginNames)
			throws ServiceException, DAOException, SystemException {
		Map<Long, Map<String, String>> result = null;
		try {
			if (nsids == null || nsids.isEmpty() || pluginNames == null
					|| pluginNames.isEmpty()) {
				throw new NullPointerException(
						"nsids or pluginNames is not null or empty!");
			}
			StringBuilder sql = new StringBuilder(
					"select t.ns_id,t.nsp_pluginname,t.nsp_pluginclobvalue from t_wf_base_nodesettingplugin t ");
			int nsidsSize = nsids.size();
			int pluginNamesSize = pluginNames.size();
			// 查询条件ns_id SQL组装
			String[] nsidsSizeStrings = SQLInSplit500Util(nsidsSize);
			StringBuilder nsidsSql = new StringBuilder(" ( ");
			
			for (int i = 0; i < nsidsSizeStrings.length; i++) {
				if (i != 0) {
					nsidsSql.append(" or ");
				}
				nsidsSql.append(" t.ns_id in (").append(nsidsSizeStrings[i])
						.append(")");
			}
			nsidsSql.append(" ) ");
			// 查询条件nsp_pluginname SQL组装
			String[] pluginNamesSizes = SQLInSplit500Util(pluginNamesSize);
			StringBuilder pluginNamesSql = new StringBuilder(" ( ");
			for (int i = 0; i < pluginNamesSizes.length; i++) {
				if (i != 0) {
					nsidsSql.append(" or ");
				}
				pluginNamesSql.append(" t.nsp_pluginname in (").append(
						pluginNamesSizes[i]).append(")");
			}
			pluginNamesSql.append(" ) ");
			sql.append(" where ").append(nsidsSql).append(" and ").append(
					pluginNamesSql)
					.append(" order by t.ns_id,t.nsp_pluginname");
			List params = new ArrayList(nsidsSize + pluginNamesSize);
			params.addAll(nsids);			//添加nsids
			params.addAll(pluginNames);		//添加pluginNames
			Query  query = manager.getServiceDAO().createSqlQuery(sql.toString(), params);
			List list = query.list();
			if (list != null && !list.isEmpty()) {
				result = new LinkedHashMap<Long, Map<String,String>>(nsidsSize);
			}
			for(Object obj:list){
				Object[] res = (Object[])obj;
				Long ns_id = Long.valueOf(StringUtil.castString(res[0]));
				Map<String,String> pluginMap = null;
				if(!result.containsKey(ns_id)){
					pluginMap = new LinkedHashMap<String, String>(pluginNamesSize);
				}else{
					pluginMap = result.get(ns_id);
				}
				String nsp_pluginname = StringUtil.castString(res[1]);
					Clob clob =null;
					String nsp_pluginclobvalue = null;
					if(res[2] instanceof String){
						//clob=oracleStr2Clob(String.valueOf(res[2]),clob);
						nsp_pluginclobvalue = String.valueOf(res[2]);
					}else{
						clob =(Clob)res[2];
						if(clob != null){
							nsp_pluginclobvalue = clob.getSubString((long)1, (int)clob.length());
						}
					}
				pluginMap.put(nsp_pluginname,nsp_pluginclobvalue);
				result.put(ns_id, pluginMap);
			}
			return result;
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	/**
	 * 批量获取节点插件信息(通过节点id和插件属性名称)
	 * 
	 * @author yanjian
	 * @param nsids
	 * @param pluginNames
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 14, 2012 9:57:45 AM
	 */
	@SuppressWarnings("unchecked")
	public Map<Long, Map<String, String>> getNodesettingPluginValueForMapByNodeIdsAndpluginNames(
			Set<Long> nsNodeids, Set<String> pluginNames)
			throws ServiceException, DAOException, SystemException {
		Map<Long, Map<String, String>> result = null;
		try {
			if (nsNodeids == null || nsNodeids.isEmpty() || pluginNames == null
					|| pluginNames.isEmpty()) {
				throw new NullPointerException(
				"nsNodeids or pluginNames is not null or empty!");
			}
			StringBuilder sql = new StringBuilder(
			"select t.nsp_nodeid,t.nsp_pluginname,t.nsp_pluginclobvalue from t_wf_base_nodesettingplugin t ");
			int nsidsSize = nsNodeids.size();
			int pluginNamesSize = pluginNames.size();
			// 查询条件ns_id SQL组装
			String[] nsidsSizeStrings = SQLInSplit500Util(nsidsSize);
			StringBuilder nsidsSql = new StringBuilder(" ( ");
			
			for (int i = 0; i < nsidsSizeStrings.length; i++) {
				if (i != 0) {
					nsidsSql.append(" or ");
				}
				nsidsSql.append(" t.nsp_nodeid in (").append(nsidsSizeStrings[i])
				.append(")");
			}
			nsidsSql.append(" ) ");
			// 查询条件nsp_pluginname SQL组装
			String[] pluginNamesSizes = SQLInSplit500Util(pluginNamesSize);
			StringBuilder pluginNamesSql = new StringBuilder(" ( ");
			for (int i = 0; i < pluginNamesSizes.length; i++) {
				if (i != 0) {
					nsidsSql.append(" or ");
				}
				pluginNamesSql.append(" t.nsp_pluginname in (").append(
						pluginNamesSizes[i]).append(")");
			}
			pluginNamesSql.append(" ) ");
			sql.append(" where ").append(nsidsSql).append(" and ").append(
					pluginNamesSql)
					.append(" order by t.nsp_nodeid,t.nsp_pluginname");
			List params = new ArrayList(nsidsSize + pluginNamesSize);
			params.addAll(nsNodeids);			//添加nsids
			params.addAll(pluginNames);		//添加pluginNames
			Query  query = manager.getServiceDAO().createSqlQuery(sql.toString(), params);
			List list = query.list();
			if (list != null && !list.isEmpty()) {
				result = new LinkedHashMap<Long, Map<String,String>>(nsidsSize);
			}
			for(Object obj:list){
				Object[] res = (Object[])obj;
				Long ns_id = Long.valueOf(StringUtil.castString(res[0]));
				Map<String,String> pluginMap = null;
				if(!result.containsKey(ns_id)){
					pluginMap = new LinkedHashMap<String, String>(pluginNamesSize);
				}else{
					pluginMap = result.get(ns_id);
				}
				String nsp_pluginname = StringUtil.castString(res[1]);
				Clob clob =null;
				String nsp_pluginclobvalue = null;
				if(res[2] instanceof String){
					//clob=oracleStr2Clob(String.valueOf(res[2]),clob);
					nsp_pluginclobvalue = String.valueOf(res[2]);
				}else{
					clob =(Clob)res[2];
					if(clob != null){
						nsp_pluginclobvalue = clob.getSubString((long)1, (int)clob.length());
					}
				}
				pluginMap.put(nsp_pluginname,nsp_pluginclobvalue);
				result.put(ns_id, pluginMap);
			}
			return result;
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	private String[] SQLInSplit500Util(int size) throws ServiceException,DAOException, SystemException{
		try {
			if(size <0){
				throw new ArithmeticException("size is not Less than zero!");
			}
			int length = size/500 + (size%500 == 0?0:1);
			String[] result = new String[length];
			StringBuilder in500String = null;
			StringBuilder remainderString = null;
			if(size > 500){
				in500String = new StringBuilder();
				for(int i=0;i<500;i++){
					in500String.append(",").append("?");
				}
				in500String.deleteCharAt(0);
			}
			int remainder = size%500;
			if(remainder >0){
				remainderString = new StringBuilder();
				for(int i=0;i<remainder;i++){
					remainderString.append(",").append("?");
				}
				remainderString.deleteCharAt(0);
			}
			if(length == 1){
				result[0] = remainderString.toString();
			}else{
				result[length-1] = remainderString.toString();
				String in500 = in500String.toString();
				for(int i=0;i<length-1;i++){
					result[i]= in500;
				}
			}
			return result;
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
}
