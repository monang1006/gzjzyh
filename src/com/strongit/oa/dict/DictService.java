/*
 * Copyright : Jiang Xi Strong Co.. Ltd.
 * All right reserved.
 * JDK 1.5.0_14;Struts：2.1.2;Spring 2.5.6;Hibernate： 3.3.1.GA
 * Create Date: 2008-12-19
 * Autour: zhangli
 * Version: V1.0
 * Description： 字典管理通用接口实现类
 */
package com.strongit.oa.dict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.dict.dictItem.DictItemManager;
import com.strongit.oa.dict.dictType.DictTypeManager;
import com.strongit.oa.util.LogPrintStackUtil;

@Service
@Transactional
public class DictService implements IDictService {
	/** 字典项Manager*/
	private DictItemManager itemanager;

	/** 字典类Manager*/
	private DictTypeManager typemanager;

	/**
	 * @roseuid 494B54F40177
	 */
	public DictService() {

	}

	/**
	 * 注册字典项Manager
	 * @param aItemanager 字典项Manager
	 */
	@Autowired
	public void setItemanager(DictItemManager aItemanager) {
		itemanager = aItemanager;
	}

	/**
	 * 注册字典类Manager
	 * @param aTypemanager 字典类Manager
	 */
	@Autowired
	public void setTypemanager(DictTypeManager aTypemanager) {
		typemanager = aTypemanager;
	}

	/**
	 * 通过字典类值，得到该字典类值下的字典项对象
	 * 
	 * @param dictValue 字典类值
	 * @return java.util.List 字典项对象列表
	 * @roseuid 494B54F401A5
	 */
	public List<ToaSysmanageDictitem> getItemsByDictValue(String dictValue) {
		ToaSysmanageDict dict = typemanager.getDictByValue(dictValue);
		/**
		 * 邓志城 修改：2009年9月10日9:52:30
		 * 防止没有审批意见导致出错
		 */
		if(dict == null){
			LogPrintStackUtil.error("数据字典中不存在“"+dictValue+"”！");
			return new ArrayList<ToaSysmanageDictitem>();
		}
		return itemanager.getAllDictItems(dict.getDictCode());
	}

	/**
	 * 通过字典类值和字典项名称，得到该字典类值下的字典项对象
	 * 
	 * @param dictValue 字典类值
	 * @param itemName 字典项名称
	 * @return java.lang.List 字典项对象列表
	 * @roseuid 494B54F401F4
	 */
	public List getItemsByDictValueAndItemName(String dictValue, String itemName) {
		ToaSysmanageDict dict = typemanager.getDictByValue(dictValue);
		if(dict==null){
			return null;
		}
		return getItemsByDictIdAndItemName(dict.getDictCode(), itemName);
	}

	/**
	 * 通过字典类编号和字典项名称，得到该字典类值下的字典项对象
	 * 
	 * @param dictCode 字典类编号
	 * @param itemName 字典项名称
	 * @return java.util.List 字典项对象列表
	 * @roseuid 494B54F40261
	 */
	public List getItemsByDictIdAndItemName(String dictCode, String itemName) {
		return itemanager.getDictItem(itemName, dictCode);
	}

	/**
	 * 通过字典项编号，得到该字典项名称
	 * 
	 * @param dictItemCode 字典项编号
	 * @return java.lang.String 字典项名称
	 * @roseuid 494B54F402DE
	 */
	public String getDictItemName(String dictItemCode) {
		return itemanager.getDictItemName(dictItemCode);
	}

	/**
	 * 通过字典类编号，得到该字典类名称
	 * 
	 * @param dictItemCode 字典类编号
	 * @return java.lang.String 字典类名称
	 * @roseuid 494B54BA02BF
	 */
	public String getDictName(String dictItemCode) {
		// TODO Auto-generated method stub
		return typemanager.getDictName(dictItemCode);
	}

	/**
	 * 通过字典类值，得到该字典类名称
	 * 
	 * @param dictItemValue 字典类值
	 * @return java.lang.String 字典类名称
	 * @roseuid 494B54BA02BF
	 */
	public String getDictNameByValue(String dictItemValue) {
		// TODO Auto-generated method stub
		return typemanager.getDictNameByValue(dictItemValue);
	}
	
	
	/*
	 * 
	 * Description:获取某字典可选的字典项
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jul 1, 2010 6:14:14 PM
	 */
	public List<ToaSysmanageDictitem> getItemsByValue(String dictValue) {
		ToaSysmanageDict dict = typemanager.getDictByValue(dictValue);
		if(dict == null){
			return new ArrayList<ToaSysmanageDictitem>();
		}
		return itemanager.getDictItemsByCode(dict.getDictCode());
	}
}
