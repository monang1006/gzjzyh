package com.strongit.oa.common.workflow.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.workflow.ITransitionPluginService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

@Service
@OALogger
public class TransitionPluginService implements ITransitionPluginService {
	@Autowired
	IWorkflowService workflowService;// 工作流服务类
	@Autowired
	SendDocManager manager;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 根据迁移线Id得到迁移线插件属性信息列表
	 * @param transitionId    迁移线Id
	 * @return				  List 迁移线插件属性信息列表
	 * @throws com.strongit.workflow.exception.WorkflowException
	 */
	public java.util.List<com.strongit.workflow.bo.TwfBaseTransitionPlugin> getTransitionPluginsByTsId(
			java.lang.String transitionId)
			throws com.strongit.workflow.exception.WorkflowException{
		return workflowService.getTransitionPluginsByTsId(transitionId);
	}
	
	
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
			throws ServiceException, DAOException, SystemException {
		Map<Long, Map<String, String>> result = null;
		try {
			if (transitionids == null || transitionids.isEmpty() || pluginNames == null
					|| pluginNames.isEmpty()) {
				throw new NullPointerException(
				"transitionids or pluginNames is not null or empty!");
			}
			StringBuilder sql = new StringBuilder().append(
			"select t.processDefinition.id,t.name,t.from.name,t.to.name,t.id from org.jbpm.graph.def.Transition t ");
			int transitionidsize = transitionids.size();
			String[] transitionidsizeStrings = SQLInSplit500Util(transitionidsize);
			StringBuilder transitionidsSql = new StringBuilder(" ( ");
			for (int i = 0; i < transitionidsizeStrings.length; i++) {
				if (i != 0) {
					transitionidsSql.append(" or ");
				}
				transitionidsSql.append(" t.id in (").append(transitionidsizeStrings[i])
				.append(")");
			}
			transitionidsSql.append(" ) ");
			sql.append(" where ").append(transitionidsSql);
			Long[] transitionidsArray = new Long[transitionidsize];
			transitionids.toArray(transitionidsArray);
			System.out.println();
			List tranList = manager.getServiceDAO().find(sql.toString(), transitionidsArray);
			int tranListSize =(tranList == null?0:tranList.size());
			if(tranListSize>0){
				//keyMaptransitionid缓存业务标识和主键映射关系
				Map<String,Long> keyMaptransitionid = new LinkedHashMap<String, Long>(tranListSize);
				Set<Long> processDefinitionIds = new HashSet<Long>(tranListSize);
				Set<String> names = new HashSet<String>(tranListSize);
				Set<String> fromNames = new HashSet<String>(tranListSize);
				Set<String> toNames = new HashSet<String>(tranListSize);
				for(int i=0;i<tranListSize;i++){
					Object[] objs = (Object[])tranList.get(i);
					processDefinitionIds.add(Long.valueOf(objs[0]+""));
					names.add(StringUtil.castString(objs[1]));
					fromNames.add(StringUtil.castString(objs[2]));
					toNames.add(StringUtil.castString(objs[3]));
					String key = objs[0]+"@"+ objs[1] +"@"+ objs[2] + "@" + objs[3];
					Long transitionid = Long.valueOf(objs[4]+"");
					keyMaptransitionid.put(key, transitionid);
				}
				//processDefinitionIds 处理流程定义id
				int processDefinitionIdsSize = processDefinitionIds.size();
				String[] processDefinitionIdsStrings = SQLInSplit500Util(processDefinitionIdsSize);
				StringBuilder processDefinitionIdsSql = new StringBuilder(" ( ");
				for (int i = 0; i < processDefinitionIdsSize; i++) {
					if (i != 0) {
						processDefinitionIdsSql.append(" or ");
					}
					processDefinitionIdsSql.append(" t.pdId in (").append(processDefinitionIdsStrings[i])
					.append(")");
				}
				processDefinitionIdsSql.append(" ) ");
				
				//namesSize 处理
				int namesSize = names.size();
				String[]  namesStrings = SQLInSplit500Util(namesSize);
				StringBuilder  namesSql = new StringBuilder(" ( ");
				for (int i = 0; i < namesStrings.length; i++) {
					if (i != 0) {
						namesSql.append(" or ");
					}
					namesSql.append(" t.tsName in (").append(namesStrings[i])
					.append(")");
				}
				namesSql.append(" ) ");
				
				//fromNames 处理
				int fromNamesSize = fromNames.size();
				String[] fromNamesStrings = SQLInSplit500Util(fromNamesSize);
				StringBuilder fromNamesSql = new StringBuilder(" ( ");
				for (int i = 0; i < fromNamesStrings.length; i++) {
					if (i != 0) {
						fromNamesSql.append(" or ");
					}
					fromNamesSql.append(" t.fnName in (").append(fromNamesStrings[i])
					.append(")");
				}
				fromNamesSql.append(" ) ");
				
				//toNames 处理
				int toNamesSize = toNames.size();
				String[] toNamesStrings = SQLInSplit500Util(toNamesSize);
				StringBuilder toNamesSql = new StringBuilder(" ( ");
				for (int i = 0; i < toNamesStrings.length; i++) {
					if (i != 0) {
						toNamesSql.append(" or ");
					}
					toNamesSql.append(" t.tnName in (").append(toNamesStrings[i])
					.append(")");
				}
				toNamesSql.append(" ) ");
				
				//处理插件名称参数
				int pluginNamesSize = pluginNames.size();
				String[] pluginNamesSizes = SQLInSplit500Util(pluginNamesSize);
				StringBuilder pluginNamesSql = new StringBuilder(" ( ");
				for (int i = 0; i < pluginNamesSizes.length; i++) {
					if (i != 0) {
						pluginNamesSql.append(" or ");
					}
					pluginNamesSql.append(" t.tspPluginame in (").append(
							pluginNamesSizes[i]).append(")");
				}
				pluginNamesSql.append(" ) ");
				
				List params = new ArrayList(processDefinitionIdsSize + namesSize + fromNamesSize + toNamesSize + pluginNamesSize);
				params.addAll(processDefinitionIds);
				params.addAll(names);
				params.addAll(fromNames);
				params.addAll(toNames);
				params.addAll(pluginNames);
				StringBuilder HQL = new StringBuilder(" select t.pdId,t.tsName,t.fnName,t.tnName,t.tspPluginame,t.value")
					.append(" from TwfBaseTransitionPlugin t").append(" where 1=1 ")
					.append(" and ").append(processDefinitionIdsSql)
					.append(" and ").append(namesSql)
					.append(" and ").append(fromNamesSql)
					.append(" and ").append(toNamesSql)
					.append(" and ").append(pluginNamesSql)
					.append(" order by t.tspId");
				Object[] paramsArray = new Object[params.size()];
				params.toArray(paramsArray);
				List transitionPluginList = manager.getServiceDAO().find(HQL.toString(), paramsArray);
				int transitionPluginListSize = (transitionPluginList == null?0:transitionPluginList.size());
				if(transitionPluginListSize > 0 ){
					result = new LinkedHashMap<Long, Map<String,String>>(transitionPluginListSize);
					for (int i = 0; i < transitionPluginListSize; i++) {
						Object[] objs = (Object[])transitionPluginList.get(i);
						String key = objs[0]+"@"+ objs[1] +"@"+ objs[2] + "@" + objs[3];
						String pluginame = StringUtil.castString(objs[4]);
						String plugiValue = StringUtil.castString(objs[5]);
						if(keyMaptransitionid.containsKey(key)){
							Long transitionid = keyMaptransitionid.get(key);
							Map<String,String> tempMap = null;
							if(!result.containsKey(transitionid)){
								tempMap = new LinkedHashMap<String, String>();
							}else{
								 tempMap = result.get(transitionid);
							}
							tempMap.put(pluginame, plugiValue);
							result.put(transitionid, tempMap);
						}
					}
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
			e.printStackTrace();
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
