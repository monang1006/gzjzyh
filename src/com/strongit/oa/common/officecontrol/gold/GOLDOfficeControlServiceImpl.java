package com.strongit.oa.common.officecontrol.gold;


import com.strongit.oa.common.officecontrol.BaseOfficeControlService;
import com.strongit.oa.common.officecontrol.OfficeControlService;
/**
 * 金格OFFICE控件服务类
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-12-8 上午11:15:25
 * @version  3.0
 * @classpath com.strongit.oa.common.officecontrol.gold.GOLDOfficeControlServiceImpl
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class GOLDOfficeControlServiceImpl  extends BaseOfficeControlService implements OfficeControlService {

	String version = "#version=8,2,4,0";
	
	public String getJsPath() {
		return "/goldgrid/office";
	}

	/**
	 * 得到控件CAB包路径
	 * @author:邓志城
	 * @date:2010-12-9 下午02:01:53
	 * @return
	 */
	public String getCabPath() {
		return "/goldgrid/OfficeControl.cab";
	}

}
