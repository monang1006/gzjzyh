package com.strongit.oa.common.workflow;

import java.util.Map;
import java.util.Set;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface ITransitionPluginService {
	/**
	 * 根据迁移线Id得到迁移线插件属性信息列表
	 * 
	 * @param transitionId
	 *            迁移线Id
	 * @return List 迁移线插件属性信息列表
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public java.util.List<com.strongit.workflow.bo.TwfBaseTransitionPlugin> getTransitionPluginsByTsId(
			java.lang.String transitionId)
			throws com.strongit.workflow.exception.WorkflowException;
	/**
	 * 批量获取迁移线插件信息(通过迁移线id和插件属性名称)
	 * 
	 * @author yanjian
	 * @param transitionids
	 * @param pluginNames
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 14, 2012 9:57:45 AM
	 */
	@SuppressWarnings("unchecked")
	public Map<Long, Map<String, String>> getTransitionPluginValueForMapByTransitionIdsAndpluginNames(
			Set<Long> transitionids, Set<String> pluginNames)
			throws ServiceException, DAOException, SystemException;

}
