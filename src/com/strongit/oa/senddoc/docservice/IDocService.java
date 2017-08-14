package com.strongit.oa.senddoc.docservice;

import java.util.List;
import java.util.Map;
/**
* 处理业务表中的字段信息
* @author dengwenqiang
* @version 1.0
*/
public interface IDocService {
	/**
	 * 获取公文限时时间
	 * 
	 * @author 严建
	 * @param taskIdList
	 * @return
	 * @createTime Feb 6, 2012 2:15:33 PM
	 */
	public Map<String,String> getTaskIdMapDocTimeOutDate(List<Long> taskIdList);
	/**
	 * @description 根据任务实例id获取公文超期的期限(字符串格式：yyyy-mm-dd)
	 * @author 严建
	 * @createTime Dec 15, 2011 4:38:25 PM
	 * @return String 
	 */
	public String getDocTimeOutDate(String taskId);

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
			Map<String, List<String>> tableNameAndpkFieldNameMapPkFieldValueArray);
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
			String listType) ;
	
}
