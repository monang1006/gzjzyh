/*
 * Copyright : Jiang Xi Strong Co.. Ltd.
 * All right reserved.
 * JDK 1.5.0_14;Struts：2.1.2;Spring 2.5.6;Hibernate： 3.3.1.GA
 * Create Date: 2008-12-19
 * Autour: zhangli
 * Version: V1.0
 * Description： 字典管理通用接口
 */
package com.strongit.oa.dict;

import java.util.List;

import com.strongit.oa.bo.ToaSysmanageDictitem;

public interface IDictService {

	/**
	 * 通过字典类值，得到该字典类值下的字典项对象
	 * 
	 * @param dictValue 字典类值
	 * @return java.util.List 字典项对象列表
	 * @roseuid 494B530103C8
	 */
	public List<ToaSysmanageDictitem> getItemsByDictValue(String dictValue);

	/**
	 * 通过字典类值和字典项名称，得到该字典类值下的字典项对象
	 * 
	 * @param dictValue 字典类值
	 * @param itemName 字典项名称
	 * @return java.lang.List 字典项对象列表
	 * @roseuid 494B53AE007D
	 */
	public List getItemsByDictValueAndItemName(String dictValue, String itemName);

	/**
	 * 通过字典类编号和字典项名称，得到该字典类值下的字典项对象
	 * 
	 * @param dictCode 字典类编号
	 * @param itemName 字典项名称
	 * @return java.util.List 字典项对象列表
	 * @roseuid 494B5431037A
	 */
	public List getItemsByDictIdAndItemName(String dictCode, String itemName);

	/**
	 * 通过字典项编号，得到该字典项名称
	 * 
	 * @param dictItemCode 字典项编号
	 * @return java.lang.String 字典项名称
	 * @roseuid 494B54BA02BF
	 */
	public String getDictItemName(String dictItemCode);

	/**
	 * 通过字典类编号，得到该字典类名称
	 * 
	 * @param dictItemCode 字典类编号
	 * @return java.lang.String 字典类名称
	 * @roseuid 494B54BA02BF
	 */
	public String getDictName(String dictItemCode);

	/**
	 * 通过字典类值，得到该字典类名称
	 * 
	 * @param dictItemValue 字典类值
	 * @return java.lang.String 字典类名称
	 * @roseuid 494B54BA02BF
	 */
	public String getDictNameByValue(String dictItemValue);
	
	/*
	 * 
	 * Description:获取某字典可选的字典项
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jul 1, 2010 6:14:14 PM
	 */
	public List<ToaSysmanageDictitem> getItemsByValue(String dictValue);
}
