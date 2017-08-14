package com.strongit.oa.common.workflow.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bookmark.BookMarkManager;
import com.strongit.oa.calendar.CalendarManager;
import com.strongit.oa.calendar.CalendarRemindManager;
import com.strongit.oa.calendar.CalendarShareManager;
import com.strongit.oa.common.business.jbpmbusiness.JBPMBusinessManager;
import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.remind.RemindManager;
import com.strongit.oa.common.service.util.IActorAdpter;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.INodeService;
import com.strongit.oa.common.workflow.INodesettingPluginService;
import com.strongit.oa.common.workflow.ITransitionPluginService;
import com.strongit.oa.common.workflow.ITransitionService;
import com.strongit.oa.common.workflow.IWorkflowConstService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.plugin.DefinitionPluginService;
import com.strongit.oa.dict.dictItem.DictItemManager;
import com.strongit.oa.doctemplate.doctempItem.DocTempItemManager;
import com.strongit.oa.doctemplate.eform.TemplateEFormManager;
import com.strongit.oa.senddoc.manager.SendDocAnnalManager;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.suggestion.IApprovalSuggestionService;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.traceDoc.TraceDocManager;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.oa.work.manager.WorkExtendManager;
import com.strongit.uums.organisemanage.IOrgManager;

/**
 * Action
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 14, 2012 9:16:25 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.manager.AbstractBaseWorkflowManager
 */
@Service
@Transactional
@OALogger
public class AdapterBaseWorkflowManager {
	@Autowired
	private JBPMBusinessManager jbpmbusinessmanager;

	private @Autowired
	TemplateEFormManager templateEFormManager;// 表单模板服务类

	private @Autowired
	DocTempItemManager docTempItemManager; // 模板项服务类

	@Autowired
	private SystemsetManager systemsetManager;

	private @Autowired
	DictItemManager dictItemManager; // 字典项服务类

	private @Autowired
	SendDocUploadManager sendDocUploadManager;

	private @Autowired
	TraceDocManager traceDocManager; // 注入跟踪重要文件接口

	private @Autowired
	SendDocAnnalManager sendDocAnnalManager; // 注入办理记录接口

	@Autowired
	private CalendarManager calManager;// 日程管理模块

	@Autowired
	private CalendarRemindManager calRemindManager;// 日程管理模块

	@Autowired
	private IWorkflowAttachService workflowAttachManager; // 公文附件处理类

	@Autowired
	private WorkExtendManager workExtendManager; // 公文附件处理类

	private @Autowired
	IUserService userService; // 注入当前用户接口
	private @Autowired
	IOrgManager orgManager;

	private @Autowired
	IApprovalSuggestionService approvalSuggestionService; // 审批意见接口

	private @Autowired
	IActorAdpter actorAdpter;

	@Autowired
	private CalendarShareManager calShareManager;

	private @Autowired
	RemindManager remindService;// 提醒服务类

	private @Autowired
	BookMarkManager bookMarkService;// 标签服务类

	private @Autowired
	IWorkflowService workflowService;// 工作流服务类

	private @Autowired
	IWorkflowConstService workflowConstService;// 工作流服务类

	private @Autowired
	IEFormService eformService; // 电子表单服务类

	@Autowired
	private DefinitionPluginService definitionPluginService;// 流程定义插件服务类.

	@Autowired
	private INodesettingPluginService nodesettingPluginService;// 定义节点插件服务类.
	@Autowired
	private ITransitionPluginService transitionPluginService;// 定义迁移线插件服务类.

	@Autowired
	private ITransitionService transitionService;// 定义迁移线服务类.

	@Autowired
	private INodeService nodeService;// 定义迁移线服务类.

	@Autowired
	private ICustomUserService customUserService;// 定义迁移线服务类.

	public CalendarManager getCalManager() {
		return calManager;
	}

	public CalendarRemindManager getCalRemindManager() {
		return calRemindManager;
	}

	public DictItemManager getDictItemManager() {
		return dictItemManager;
	}

	public SendDocAnnalManager getSendDocAnnalManager() {
		return sendDocAnnalManager;
	}

	public TraceDocManager getTraceDocManager() {
		return traceDocManager;
	}

	public SystemsetManager getSystemsetManager() {
		return systemsetManager;
	}

	public JBPMBusinessManager getJbpmbusinessmanager() {
		return jbpmbusinessmanager;
	}

	public DocTempItemManager getDocTempItemManager() {
		return docTempItemManager;
	}

	public TemplateEFormManager getTemplateEFormManager() {
		return templateEFormManager;
	}

	public IWorkflowAttachService getWorkflowAttachManager() {
		return workflowAttachManager;
	}

	public IUserService getUserService() {
		return userService;
	}

	public IApprovalSuggestionService getApprovalSuggestionService() {
		return approvalSuggestionService;
	}

	public IActorAdpter getActorAdpter() {
		return actorAdpter;
	}

	public CalendarShareManager getCalShareManager() {
		return calShareManager;
	}

	public RemindManager getRemindService() {
		return remindService;
	}

	public BookMarkManager getBookMarkService() {
		return bookMarkService;
	}

	public IWorkflowService getWorkflowService() {
		return workflowService;
	}

	public IEFormService getEformService() {
		return eformService;
	}

	public DefinitionPluginService getDefinitionPluginService() {
		return definitionPluginService;
	}

	public INodesettingPluginService getNodesettingPluginService() {
		return nodesettingPluginService;
	}

	public ITransitionService getTransitionService() {
		return transitionService;
	}

	public WorkExtendManager getWorkExtendManager() {
		return workExtendManager;
	}

	public void setWorkExtendManager(WorkExtendManager workExtendManager) {
		this.workExtendManager = workExtendManager;
	}

	public IWorkflowConstService getWorkflowConstService() {
		return workflowConstService;
	}

	public SendDocUploadManager getSendDocUploadManager() {
		return sendDocUploadManager;
	}

	public ICustomUserService getCustomUserService() {
		return customUserService;
	}

	public INodeService getNodeService() {
		return nodeService;
	}

	public ITransitionPluginService getTransitionPluginService() {
		return transitionPluginService;
	}

	public IOrgManager getOrgManager() {
		return orgManager;
	}
	
}
