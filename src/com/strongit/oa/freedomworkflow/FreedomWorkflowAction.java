package com.strongit.oa.freedomworkflow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOaFreedomWorkflow;
import com.strongit.oa.bo.TOaFreedomWorkflowTask;
import com.strongit.oa.bo.TOaRelativeWorkflow;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.freedomworkflow.define.FreedomWorkflowStatus;
import com.strongit.oa.freedomworkflow.define.FwMonitorDto;
import com.strongit.oa.freedomworkflow.service.IFreedomWorkflowService;
import com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService;
import com.strongit.oa.relativeworkflow.ProcessInstanceDto;
import com.strongit.uums.security.UserInfo;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @author 钟伟
 * @version 1.0
 * @date 2013-12-25
 */

public class FreedomWorkflowAction extends BaseActionSupport<TOaFreedomWorkflow>
{
	private TOaFreedomWorkflow model = new TOaFreedomWorkflow();
	
	@Autowired
	private IFreedomWorkflowService fwSrv;
	
	@Autowired
	private IFreedomWorkflowTaskService ftSrv;

	private String formId;

	private int curpage;
	
	private int unitpage;
		
	private Page<TOaFreedomWorkflowTask> page = new Page<TOaFreedomWorkflowTask>(unitpage, true);

	private String ftId;
	
	private String ftTitle;
	
	private String ftHandler;
	
	private String nextFtTitle;
	
	private String nextFtHandler;
	
	private String nextFtHandlerId;
	
	private List<FwMonitorDto> monitorTasks;
	
	@Autowired
	private IUserService userService;

	
	public TOaFreedomWorkflow getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		return null;
	}

	/**
	 * 处理任务
	 */
	@Override
	public String input() throws Exception
	{
		String fwId = getRequest().getParameter("fwId");
		if(StringUtils.isNotEmpty(fwId))
		{
			model = fwSrv.getFreedomWorkflow(fwId);
		}
		
		if(StringUtils.isNotEmpty(ftId))
		{
			TOaFreedomWorkflowTask task = ftSrv.getFreedomWorkflowTask(ftId);
			TOaFreedomWorkflowTask next = ftSrv.getNextTask(ftId);
			ftTitle = task.getFtTitle();
			ftHandler = task.getFtHandler();
			if(next != null)
			{
				nextFtTitle = next.getFtTitle();
				String userName = "";
				if(StringUtils.isNotEmpty(next.getFtHandler()))
				{
					User user = userService.getUserInfoByUserId(next.getFtHandler());
					userName = user.getUserName();
				}
				nextFtHandler = userName;
				nextFtHandlerId = next.getFtHandler();
			}
		}
		return INPUT;
	}

	@Override
	public String list() throws Exception
	{
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception
	{
	}

	@Override
	public String save() throws Exception
	{
		model.setFwStatus(FreedomWorkflowStatus.FW_PROCESSING.val());
		fwSrv.saveFreedomWorkflow(model);
		return null;
	}
	
	/**
	 * 待办列表数据
	 * 
	 * @param
	 * @return
	 */
	public String showList() throws Exception
	{
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		UserInfo userInfo = (UserInfo) getUserDetails();
		String ftTitle = getRequest().getParameter("ftTitle");
		page = ftSrv.getMyPendingTasks(page, userInfo.getUserId(), ftTitle);
				
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());

		List<TOaFreedomWorkflowTask> result = page.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			Map<String, User> localUserCache = new HashMap<String, User>();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(TOaFreedomWorkflowTask one : result)
			{
				JSONObject obj = new JSONObject();
				TOaFreedomWorkflow fw = one.getTOaFreedomWorkflow();
				if(fw != null)
				{
					obj.put("fwId", fw.getFwId());
					
					String userName = "";
					String userId = fw.getFwCreator();
					if(localUserCache.containsKey(fw.getFwCreator()))
					{
						userName = localUserCache.get(fw.getFwCreator()).getUserName();
					}
					else
					{
						User user = userService.getUserInfoByUserId(userId);
						localUserCache.put(user.getUserId(), user);
						userName = user.getUserName();
					}
					obj.put("fwCreator", userName);
					
					obj.put("formId", fw.getFwFormId());
					obj.put("fwTitle", fw.getFwTitle());
				}
				obj.put("ftId", one.getFtId());
				obj.put("ftTitle", one.getFtTitle());
				if(one.getFtStartTime() != null)
				{
					obj.put("ftStartTime", df.format(one.getFtStartTime()));
				}
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	public String doneList() throws Exception
	{
		return "doneList";
	}
	
	/**
	 * 已办列表数据
	 * 
	 * @param
	 * @return
	 */
	public String doneShowList() throws Exception
	{
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		UserInfo userInfo = (UserInfo) getUserDetails();
		String ftTitle = getRequest().getParameter("ftTitle");
		page = ftSrv.getMyDoneTasks(page, userInfo.getUserId(), ftTitle);
				
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());

		List<TOaFreedomWorkflowTask> result = page.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			Map<String, User> localUserCache = new HashMap<String, User>();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(TOaFreedomWorkflowTask one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("ftId", one.getFtId());
				obj.put("ftTitle", one.getFtTitle());
				if(one.getFtEndTime() != null)
				{
					obj.put("ftEndTime", df.format(one.getFtEndTime()));
				}
				TOaFreedomWorkflow fw = one.getTOaFreedomWorkflow();
				if(fw != null)
				{
					obj.put("fwId", fw.getFwId());
					
					String userName = "";
					String userId = fw.getFwCreator();
					if(StringUtils.isNotEmpty(userId))
					{
						if(localUserCache.containsKey(userId))
						{
							userName = localUserCache.get(userId).getUserName();
						}
						else
						{
							User user = userService.getUserInfoByUserId(userId);
							localUserCache.put(user.getUserId(), user);
							userName = user.getUserName();
						}
					}
					obj.put("fwCreator", userName);
					
					TOaFreedomWorkflowTask next = ftSrv.getNextTask(one.getFtId());
					String nextHandler = "";
					String nextHandlerName = "";
					if(next != null)
					{
						nextHandler = next.getFtHandler();
						if(localUserCache.containsKey(nextHandler))
						{
							nextHandlerName = localUserCache.get(nextHandler).getUserName();
						}
						else
						{
							User user2 = userService.getUserInfoByUserId(nextHandler);
							localUserCache.put(user2.getUserId(), user2);
							nextHandlerName = user2.getUserName();
						}
					}
					obj.put("nextHandler", nextHandlerName);
					
					obj.put("formId", fw.getFwFormId());
					obj.put("fwTitle", fw.getFwTitle());
				}
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	/**
	 * 查看流程表单信息
	 * 
	 * @param
	 * @return
	 */
	public String view() throws Exception
	{
		String fwId = getRequest().getParameter("fwId");
		if(StringUtils.isNotEmpty(fwId))
		{
			model = fwSrv.getFreedomWorkflow(fwId);
		}
		return "view";
	}
	
	/**
	 * 流程监控
	 * 
	 * @param
	 * @return
	 */
	public String monitor() throws Exception
	{
		String fwId = getRequest().getParameter("fwId");
		List<TOaFreedomWorkflowTask> tasks = new ArrayList<TOaFreedomWorkflowTask>();
		monitorTasks = new ArrayList<FwMonitorDto> ();
		Map<String, User> localUserCache = new HashMap<String, User>();
		if(StringUtils.isNotEmpty(fwId))
		{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			tasks = ftSrv.getFreedomWorkflowTasks(fwId);
			for(TOaFreedomWorkflowTask one : tasks)
			{
				FwMonitorDto fm = new FwMonitorDto();
				if(one.getFtEndTime() != null)
				{
					fm.setFtEndTime(df.format(one.getFtEndTime()));
				}
				if(one.getFtStartTime() != null)
				{
					fm.setFtStartTime(df.format(one.getFtStartTime()));
				}

				if(StringUtils.isNotEmpty(one.getFtHandler()))
				{
					String userName = "";
					if(localUserCache.containsKey(one.getFtHandler()))
					{
						userName = localUserCache.get(one.getFtHandler()).getUserName();
					}
					else
					{
						User user = userService.getUserInfoByUserId(one.getFtHandler());
						userName = user.getUserName();
					}
					fm.setFtHandler(userName);					
				}

				fm.setFtId(one.getFtId());
				fm.setFtTitle(one.getFtTitle());
				fm.setFtMemo(one.getFtMemo());
				
				String ftStatus = one.getFtStatus();
				String status = "";
				if(FreedomWorkflowStatus.FT_DONE.val().equals(ftStatus))
					status = "已处理";
				else if(FreedomWorkflowStatus.FT_NOT_START.val().equals(ftStatus))
					status ="未开始";
				else if(FreedomWorkflowStatus.FT_PENDING.val().equals(ftStatus))
					status = "处理中";
				fm.setFtStatus(status);
				monitorTasks.add(fm);
			}
		}
		return "monitor";
	}

	

	public String getFormId()
	{
		return formId;
	}

	public void setFormId(String formId)
	{
		this.formId = formId;
	}

	public int getCurpage()
	{
		return curpage;
	}

	public void setCurpage(int curpage)
	{
		this.curpage = curpage;
	}

	public int getUnitpage()
	{
		return unitpage;
	}

	public void setUnitpage(int unitpage)
	{
		this.unitpage = unitpage;
	}

	public Page<TOaFreedomWorkflowTask> getPage()
	{
		return page;
	}

	public void setPage(Page<TOaFreedomWorkflowTask> page)
	{
		this.page = page;
	}

	public String getFtId()
	{
		return ftId;
	}

	public void setFtId(String ftId)
	{
		this.ftId = ftId;
	}

	public String getFtTitle()
	{
		return ftTitle;
	}

	public void setFtTitle(String ftTitle)
	{
		this.ftTitle = ftTitle;
	}

	public String getFtHandler()
	{
		return ftHandler;
	}

	public void setFtHandler(String ftHandler)
	{
		this.ftHandler = ftHandler;
	}

	public String getNextFtTitle()
	{
		return nextFtTitle;
	}

	public void setNextFtTitle(String nextFtTitle)
	{
		this.nextFtTitle = nextFtTitle;
	}

	public String getNextFtHandler()
	{
		return nextFtHandler;
	}

	public void setNextFtHandler(String nextFtHandler)
	{
		this.nextFtHandler = nextFtHandler;
	}

	public List<FwMonitorDto> getMonitorTasks()
	{
		return monitorTasks;
	}

	public void setMonitorTasks(List<FwMonitorDto> monitorTasks)
	{
		this.monitorTasks = monitorTasks;
	}

	public String getNextFtHandlerId() {
		return nextFtHandlerId;
	}

	public void setNextFtHandlerId(String nextFtHandlerId) {
		this.nextFtHandlerId = nextFtHandlerId;
	}


}
