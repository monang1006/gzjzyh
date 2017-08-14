package com.strongit.oa.adapter;

import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongmvc.orm.hibernate.Page;

/**
 * 业务系统对工作流需求的适配接口.
 * 办公厅OA、人保厅OA在工作流应用中有着不同的应用场景和需求.
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Feb 27, 2012
 * @classpath	com.strongit.oa.adapter.WorkflowAdapter
 * @version  	5.0
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
public interface WorkflowAdapter {

	public Page loadProcessedData(Page page, String userId, TaskBean model, String tableName,String formColumns);
}
