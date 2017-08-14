package com.strongit.oa.workinspect.autonode;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.TOsWorktask;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.workinspect.worksend.IWorkSendService;
import com.strongmvc.service.ServiceLocator;

/**
 * 自动保存会议通知草稿
 * 
 * @author yanjian
 *
 * Oct 17, 2012 6:46:35 PM
 */
public class DrafHandlerAction implements ActionHandler {
	/**
	 * @field workflowAttachService	附件管理类
	 */
	 IWorkflowAttachService workflowAttachService ;
	 SendDocManager manager;
	 IWorkSendService workSendService;
	public DrafHandlerAction(){
		workflowAttachService = (IWorkflowAttachService)ServiceLocator.getService("workflowAttachManager");
		manager = (SendDocManager)ServiceLocator.getService("sendDocManager");
		workSendService = (IWorkSendService)ServiceLocator.getService("workSendService");
	}
	/**
	 * @field serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public void execute(ExecutionContext cxt) throws Exception {
		// TODO Auto-generated method stub
		String businessId = cxt.getContextInstance().getProcessInstance().getBusinessId();
		String[] businfo = businessId.split(";");
		String tableName = businfo[0];
		String pkFieldName = businfo[1];
		String pkFieldValue = businfo[2];
		List<WorkflowAttach> workflowAttachs = workflowAttachService.getWorkflowAttachsByDocId(pkFieldValue);	//获取附件列表
		StringBuilder sql = new StringBuilder().append("SELECT WORKFLOWTITLE FROM ").append(tableName).append("  WHERE ").append(pkFieldName).append("='").append(pkFieldValue).append("'");
		Map map = manager.queryForMap(sql.toString());
		String workflowTitle = map.get("WORKFLOWTITLE").toString();
		TOsWorktask model = new TOsWorktask();
		model.setRest1("0");//标识草稿
		model.setWorktaskTitle(workflowTitle);
		String[] attachsFileName = null;
		File[] attachs	= null;
		if(workflowAttachs != null && !workflowAttachs.isEmpty()){
			int size = workflowAttachs.size();
			attachsFileName = new String[size];
			attachs	= new File[size];
			for (int i = 0; i < size; i++) {
				WorkflowAttach workflowAttach = workflowAttachs.get(i);
				attachsFileName[i] = workflowAttach.getAttachName();
				attachs[i] = FileUtil.byteArray2File(workflowAttach.getAttachContent());
			}
		}
		workSendService.save(model, null, attachs, attachsFileName);
		cxt.getToken().signal();
	}

}
