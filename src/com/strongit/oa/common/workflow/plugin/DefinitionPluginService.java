package com.strongit.oa.common.workflow.plugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDefinitionPlugin;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 流程定义插件服务类.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2011-2-8 下午04:50:03
 * @version  3.0
 * @classpath com.strongit.oa.common.workflow.plugin.DefinitionPluginService
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Transactional
@Service
@OALogger
public class DefinitionPluginService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private GenericDAOHibernate<ToaDefinitionPlugin, String> definitionPluginDao;			//DAO辅助类
	
	/**
	 * 由Spring容器注入
	 * @author:邓志城
	 * @date:2011-2-8 下午04:50:33
	 * @param sessionFactory
	 */
	@Autowired
	 public void setSessionFactory(SessionFactory sessionFactory) {
		definitionPluginDao = new GenericDAOHibernate<ToaDefinitionPlugin, String>(sessionFactory,ToaDefinitionPlugin.class);
	}
	
	/**
	 * 保存流程定义插件信息.
	 * @author:邓志城
	 * @date:2011-2-8 下午04:56:00
	 * @param model				插件信息
	 * @throws ServiceException
	 */
	public void save(List<ToaDefinitionPlugin> model,OALogInfo...infos) throws ServiceException {
		try{
			if(model != null && !model.isEmpty()) {
				ToaDefinitionPlugin plugin = model.get(0);
				String workflowId = plugin.getDefinitionPluginWorkflowId();//得到流程定义id
				this.delete(workflowId);
				definitionPluginDao.save(model);
			}
		}catch(ServiceException e) {
			logger.error("保存流程定义插件发生DAO异常", e);
			throw e;
		}catch(Exception ex) {
			logger.error("保存流程定义插件发生异常", ex);
			throw new SystemException(ex);
		}
	}
	
	/**
	 * 删除流程定义下的所有插件项.
	 * @author:邓志城
	 * @date:2011-2-9 上午11:47:53
	 * @param workflowId	流程定义Id
	 */
	@SuppressWarnings("unchecked")
	public void delete(String workflowId,OALogInfo...infos) {
		String hql = "from ToaDefinitionPlugin t where t.definitionPluginWorkflowId=?";
		List<ToaDefinitionPlugin> list = definitionPluginDao.find(hql, workflowId);
		if(list != null && !list.isEmpty()) {
			for(ToaDefinitionPlugin plugin : list) {
				definitionPluginDao.delete(plugin);
			}
		}
	}

	/**
	 * 得到流程定义下的插件项.
	 * @author:邓志城
	 * @date:2011-2-9 下午12:25:06
	 * @param workflowId		流程定义id
	 * @return
	 * 		返回流程定义下的插件项列表.
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ToaDefinitionPlugin> find(String workflowId) {
		String hql = "from ToaDefinitionPlugin t where t.definitionPluginWorkflowId=?";
		List<ToaDefinitionPlugin> list = definitionPluginDao.find(hql, workflowId);
		return list;
	}

	/**
	 * 以HashMap的方式返回流程定义下的插件项.
	 * @author:邓志城
	 * @date:2011-6-3 上午11:29:37
	 * @param workflowId			流程定义id
	 * @return
	 */
	public Map<String, String> getPlugins(String workflowId) {
		List<ToaDefinitionPlugin> list = this.find(workflowId);
		Map<String, String> map = new HashMap<String, String>();
		if(list != null && !list.isEmpty()) {
			for(ToaDefinitionPlugin plugin : list) {
				map.put(plugin.getDefinitionPluginName(), plugin.getDefinitionPluginValue());
			}
		}
		return map;
	}
	
	/**
	 * 得到流程定义下指定的插件项.
	 * @author:邓志城
	 * @date:2011-6-3 上午11:31:10
	 * @param workflowId		流程定义id
	 * @param pluginName		插件名称
	 * @return
	 */
	public String getPlugin(String workflowId,String pluginName) {
		return getPlugins(workflowId).get(pluginName);
	}
	
	/**
	 * 根据流程名称得到编码规则。
	 * @author:邓志城
	 * @date:2011-2-18 下午08:54:40
	 * @param workflowName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getRuleCodeIdByWorkflowName(String workflowName) {
		StringBuilder hql = new StringBuilder("from ToaDefinitionPlugin t where t.definitionPluginWorkflowName=? and t.definitionPluginName=?");
		hql.append(" order by t.definitionPluginWorkflowId asc");
		List<String> params = new LinkedList<String>();
		params.add(workflowName);
		params.add("plugins_rulecode");
		List<ToaDefinitionPlugin> list = definitionPluginDao.find(hql.toString(),params.toArray());
		if(list != null && !list.isEmpty()) {
			if(list.size() == 1) {
				return list.get(0).getDefinitionPluginValue();
			} else if(list.size() > 1) {
				String ret = list.get(list.size() - 1).getDefinitionPluginValue();
				for(int i=0;i<list.size()-1;i++) {
					definitionPluginDao.delete(list.get(i));
				}
				return ret;
			}
		}
		return null;
	}
	
}
