package com.strongit.oa.common.officecontrol.ntko;


import com.strongit.oa.common.officecontrol.BaseOfficeControlService;
import com.strongit.oa.common.officecontrol.OfficeControlService;

/**
 * 千航OFFICE控件服务类
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-12-8 上午11:07:20
 * @version  3.0
 * @classpath com.strongit.oa.common.officecontrol.ntko.NTKOOfficeControlServiceImpl
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class NTKOOfficeControlServiceImpl extends BaseOfficeControlService implements OfficeControlService {

	String version = "#Version=5,0,1,0";		//设置千航OFFICE控件默认版本号
	
	/**
	 * 系统中默认就是千航控件
	 * 路径为/common/OfficeControl/officecontrol.js
	 * CAB路径：/common/OfficeControl/OfficeControl.cab
	 */
	public String getJsPath() {
		return "/office";
	}

	/**
	 * 得到控件CAB包路径
	 * @author:邓志城
	 * @date:2010-12-9 下午02:01:53
	 * @return
	 */
	public String getCabPath() {
		return "/OfficeControl.cab";
	}

}
