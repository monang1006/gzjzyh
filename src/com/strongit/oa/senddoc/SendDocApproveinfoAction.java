/**
 * 
 */
package com.strongit.oa.senddoc;

import java.util.Date;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.approveinfo.ApproveinfoManager;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @description 处理临时意见表的的Action
 * @className SendDocApproveinfoAction
 * @company Strongit Ltd. (C) copyright
 * @author 申仪玲
 * @email shenyl@strongit.com.cn
 * @created 2012-1-18 上午11:46:58
 * @version 3.0
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "sendDocApproveinfo.action", type = ServletActionRedirectResult.class) })
public class SendDocApproveinfoAction extends BaseActionSupport {
	
	@Autowired
	private ApproveinfoManager manager;
	
	@Autowired
	private  IWorkflowService workflowService;//工作流服务类
	
	private String businessName;
	
	private String businessId;

	private String recordId;
	
	private String userId;   //当前处理人ID
	
	private String ruserId;   //委托人ID


	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	* @method saveApproveinfo
	* @author 申仪玲
	* @created 2012-1-18 下午01:50:34
	* @description 保存草稿意见到临时意见表中
	* @return void 返回类型
	*/
	public void saveApproveinfo() throws Exception{
		String ret = "0";
		try {
			manager.save(businessName, businessId,userId,ruserId);
			//处理退回流程的意见，该意见存在流程中，要将其清空
			if(recordId != null && !"".equals(recordId) && recordId.length() != 32){
				TwfInfoApproveinfo info = workflowService.getApproveInfoById(recordId);
				if(info != null	) {
					info.setAiContent("");
					info.setAiDate(new Date());
					workflowService.saveApproveInfo(info);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			ret = "-1";
		}
		this.renderText(ret);
		
	}
	
	public String getBusinessName() {
		return businessName;
	}


	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}


	public String getBusinessId() {
		return businessId;
	}


	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	public String getRecordId() {
		return recordId;
	}


	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRuserId() {
		return ruserId;
	}


	public void setRuserId(String ruserId) {
		this.ruserId = ruserId;
	}

}
