package com.strongit.oa.common.workflow;

import java.util.Map;
import java.util.Set;
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
public interface INodesettingPluginService {
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
	public String getNodesettingPluginValue(String nodeId, String PluginName);

	/**
	 * 根据节点设置信息和插件名称获取插件的值
	 * 
	 * @author 严建
	 * @param twfbasenodesetting
	 *            节点设置信息
	 * @param PluginName	插件名称
	 * @return
	 * @createTime Mar 7, 2012 11:08:40 PM
	 */
	public String getNodesettingPluginValue(
			TwfBaseNodesetting twfbasenodesetting, String PluginName);

	/**
	 * 根据节点id获取节点设置信息
	 * 
	 * @author 严建
	 * @param nodeId
	 *            节点id
	 * @return
	 * @createTime Mar 7, 2012 8:44:41 PM
	 */
	public TwfBaseNodesetting getNodesetting(String nodeId);

	/**
	 * 根据节点id得到节点设置信息.
	 * 
	 * @author:邓志城
	 * @date:2010-8-2 上午10:44:12
	 * @param nodeId
	 *            节点id
	 * @return 节点设置信息 new Object[]{ 最大处理人数,是否允许选择其他人,是否返回经办人 }
	 */
	public Object[] getNodesettingByNodeId(String nodeId);
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
	public Map<Long, Map<String, String>> getNodesettingPluginValueForMap(
			Set<Long> nsids, Set<String> pluginNames)
			throws ServiceException, DAOException, SystemException ;
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
			throws ServiceException, DAOException, SystemException ;
}
