package com.strongit.oa.senddoc.manager.service;

import java.util.Map;

import com.strongit.oa.common.workflow.model.TaskBusinessBean;

public interface ISendDocIcoService {
	/**
	 * 加载待办列表列显示图片
	 * 
	 * @author yanjian
	 * @param map
	 * @throws Exception
	 *             Sep 11, 2012 8:46:23 PM
	 */
	public void todoGridViewIco(Map map) throws Exception ;
	/**
	 * 加载废除列表图标
	 * 
	 * @author yanjian
	 * @param map
	 * @throws Exception
	 *             Sep 11, 2012 8:53:14 PM
	 */
	public void repealGridViewIco(Map map) throws Exception;
	/**
	 * 加载已办列表图标
	 * 
	 * @author yanjian
	 * @param map
	 * @throws Exception
	 *             Sep 11, 2012 8:57:45 PM
	 */
	public void processedGridViewIco(Map map) throws Exception;
	/**
	 * 加载桌面已办事宜
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param taskbusinessbean
	 * @param rootPath
	 * @createTime Feb 16, 2012 6:41:04 PM
	 */
	public void loadProcessedIco(StringBuffer innerHtml,TaskBusinessBean taskbusinessbean, String rootPath)throws Exception ;
	/**
	 * 加载桌面待办事宜
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param taskbusinessbean
	 * @param rootPath
	 * @createTime Feb 16, 2012 6:41:04 PM
	 */
	public void loadToDoIco(StringBuffer innerHtml,TaskBusinessBean taskbusinessbean, String rootPath)throws Exception;
}
