package com.strongit.workflow.workflowreport;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-6 下午01:50:41
 * Autour: lanlc
 * Version: V1.0
 * Description：流程报表统计manager
 */
@Service
@Transactional
public class WorkFlowReportManager {
	//工作流服务
	private IWorkflowService workflow;
		
	public WorkFlowReportManager(){}
	/**
	 * author:lanlc
	 * description:取得流程类型集
	 * modifyer:
	 * @return
	 */
	public List getAllProcessTypeList()throws SystemException,ServiceException{
		try{
			return workflow.getAllProcessTypeList();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"取得流程类型集"});
		}
	}		
	
	/**
	 * author:lanlc
	 * description:根据流程类型按流程名称分类得到流程统计信息
	 * modifyer:
	 * @param processTypeId 流程类型ID
	 * @return
	 */
	public List<WorkFlowTypeDataBean> getProcessAnalyzeByProcessForList(String processTypeId)throws SystemException,ServiceException{
		try{
			List<Object[]> processAnalyzeByProcessForLis=null;
			processAnalyzeByProcessForLis = workflow.getProcessAnalyzeByProcessForList(processTypeId);			
			List<WorkFlowTypeDataBean> processTypeList = new ArrayList();
			if(processAnalyzeByProcessForLis.size()>0){
				for(Object[] obj : processAnalyzeByProcessForLis){
					String processId = String.valueOf(obj[0]);
					String processName = String.valueOf(obj[1]);
					Integer processTodo =Integer.valueOf(String.valueOf(obj[2]));
					Integer processDone =Integer.valueOf(String.valueOf(obj[3]));
					WorkFlowTypeDataBean p = new WorkFlowTypeDataBean(processId,processName,processTodo,processDone);
					processTypeList.add(p);
				}
			}		
			return processTypeList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"流程类型按流程名称分类得到流程统计信息"});
		}
	}
		
	@Autowired
	public void setWorkflow(IWorkflowService workflow) {
		this.workflow = workflow;
	}

}
