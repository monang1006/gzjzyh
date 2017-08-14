package com.strongit.oa.common.service.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.service.adapter.bo.EntrustWorkflowParameter;
import com.strongit.oa.common.service.adapter.entrust.EntrustWorkflowManager;
import com.strongit.oa.util.annotation.OALogger;

/**
 * 适配oa在办、待办、办结、主办、草稿等功能
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Apr 17, 2012 3:45:05 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.service.adapter.AdapterWorkflowManager
 */
@Service
@Transactional
@OALogger
public class AdapterWorkflowManager {
    @Autowired
    private EntrustWorkflowManager entrustWorkflowManager;

    /**
         * 得到委派|指派的流程
         * 
         * @author:邓志城
         * @date:2010-11-21 下午06:11:00
         * @param workflowType
         *                流程类别
         * @param assignType
         *                委托类型(“0”：委托；“1”：指派；“2”：全部)
         * @return List<Object[流程名称,流程类型id,流程类型名称,流程主表单,流程数量]>
         */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Object[]> getEntrustWorkflow(EntrustWorkflowParameter parameter) {
	return entrustWorkflowManager.getEntrustWorkflow(parameter);
    }
}
