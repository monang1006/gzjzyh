package com.strongit.oa.common.officecontrol;


/**
 * OFFICE#OCX控件管理接口.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-12-8 上午10:53:59
 * @version  3.0
 * @classpath com.strongit.oa.common.officecontrol.OfficeControlService
 * @comment
 * @email dengzc@strongit.com.cn
 */
public interface OfficeControlService {
	
	/**
	 * 得到控件CAB包路径
	 * @author:邓志城
	 * @date:2010-12-9 下午02:01:53
	 * @return
	 */
	public String getCabPath();
	
	/**
	 * 得到控件引用JS的路径
	 * @author:邓志城
	 * @date:2010-12-8 上午10:55:46
	 * @return	JS路径
	 */
	public String getJsPath();
}
