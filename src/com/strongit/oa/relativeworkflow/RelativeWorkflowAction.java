package com.strongit.oa.relativeworkflow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOaRelativeWorkflow;
import com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService;
import com.strongit.uums.security.UserInfo;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class RelativeWorkflowAction extends BaseActionSupport<TOaRelativeWorkflow>
{
	
	private TOaRelativeWorkflow model = new TOaRelativeWorkflow();
	
	@Autowired
	private IRelativeWorkflowService rwSrv;
	
	private String toId;
	
	private String toIds;
	
	private Long toPiId;
	
	private String jsonSelPiIds;

	private int curpage;
	
	private int unitpage;
		
	private Page<TOaRelativeWorkflow> page = new Page<TOaRelativeWorkflow>(unitpage, true);

	private Page<ProcessInstanceDto> piPage = new Page<ProcessInstanceDto>(unitpage, true);

	
	public TOaRelativeWorkflow getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		if(StringUtils.isNotEmpty(toIds))
		{
			String[] ids = toIds.split(",");
			for(String id : ids)
			{
				rwSrv.deleteRelativeWorkflowByPiId(Long.valueOf(id));
			}
		}
		renderHtml("success");
		return null;
	}

	@Override
	public String input() throws Exception
	{
		Assert.notNull(toPiId, "流程实例ID不能为空");
		
		
		if(toPiId.compareTo(0L) > 0)
		{
			UserInfo user = (UserInfo) getUserDetails();
			Long taskId = rwSrv.getTaskId(user.getUserId(), toPiId);
			
			List<TOaRelativeWorkflow> rws = rwSrv.getRelativeWorkflows(toPiId);
			JSONObject obj = new JSONObject();
			for(TOaRelativeWorkflow one : rws)
			{
				if(!one.getPiId().equals(toPiId))
				{
					obj.put("id_"+one.getPiId(), one.getPiId());
				}
				if(!one.getPiRefId().equals(toPiId))
				{
					obj.put("id_"+one.getPiRefId(), one.getPiRefId());
				}
			}
			jsonSelPiIds = obj.toString();
		}
		
		return INPUT;
	}

	@Override
	public String list() throws Exception
	{
		return SUCCESS;
	}
	
	public String showList() throws Exception
	{
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		int totalPages = (piPage.getTotalPages() == -1) ? 1 : piPage.getTotalPages();
		jsonObj.put("totalpages", totalPages);
		jsonObj.put("totalrecords", piPage.getTotalCount());

		List<ProcessInstanceDto> result = rwSrv.getRelativeWorkflowsBySql(toPiId);
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			for(ProcessInstanceDto one : result)
			{
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				JSONObject obj = new JSONObject();
				obj.put("processInstanceId", one.getProcessInstanceId());
				obj.put("businessName", one.getBusinessName());
				obj.put("startUserName", one.getStartUserName());
				obj.put("processName", one.getProcessName());
				obj.put("startDate", df.format(one.getProcessStartDate()));
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}

	@Override
	protected void prepareModel() throws Exception
	{
	}

	@Override
	public String save() throws Exception
	{
		String[] piRefIds = new String[]{};
		piRefIds = toIds.split(",");

		rwSrv.saveRelativeWorkflow(toPiId, piRefIds);
		renderHtml("success");
		return null;
	}
	
	public String processInstances() throws Exception
	{
		UserInfo user = (UserInfo) getUserDetails();
		
		piPage = rwSrv.getProcessInstances(curpage, unitpage, user.getUserId(), toPiId);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		int totalPages = (piPage.getTotalPages() == -1) ? 1 : piPage.getTotalPages();
		jsonObj.put("totalpages", totalPages);
		jsonObj.put("totalrecords", piPage.getTotalCount());

		List<ProcessInstanceDto> result = piPage.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for(ProcessInstanceDto one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("processInstanceId", one.getProcessInstanceId());
				obj.put("processName", one.getProcessName());
				obj.put("businessName", one.getBusinessName());
				obj.put("startUserName", one.getStartUserName());
				obj.put("startDate", df.format(one.getProcessStartDate()));
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	public String viewProcessed() throws Exception
	{
		UserInfo user = (UserInfo) getUserDetails();
		
		if(rwSrv.ProcessInstanceIsSuspend(toPiId))
		{
			renderHtml("<script>alert('该流程已废除或已取消，不能查看。');window.close();</script>");
			return null;
		}
		
		Long taskId = rwSrv.getTaskId(user.getUserId(), toPiId);
		
		StringBuilder url = new StringBuilder();
		url.append(getRequest().getContextPath());
		//关联表单查看  没有按钮操作  tj
		String tempShow = getRequest().getParameter("tempShow");
		if("1".equals(tempShow)){
			url.append("/senddoc/sendDoc!viewProcessed.action?tempShow=1&");
		}else{
			url.append("/senddoc/sendDoc!viewProcessed.action?");
		}
		url.append("taskId=");
		url.append(taskId);
		url.append("&instanceId=");
		url.append(toPiId);
		getResponse().sendRedirect(url.toString());
		return null;
	}


	
	
	/*
	 * setter/getter
	 */
	
	
	public Page<TOaRelativeWorkflow> getPage()
	{
		return page;
	}

	public String getToId()
	{
		return toId;
	}

	public void setToId(String toId)
	{
		this.toId = toId;
	}

	public String getToIds()
	{
		return toIds;
	}

	public void setToIds(String toIds)
	{
		this.toIds = toIds;
	}

	public void setPage(Page<TOaRelativeWorkflow> page)
	{
		this.page = page;
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

	public Page<ProcessInstanceDto> getPiPage()
	{
		return piPage;
	}

	public void setPiPage(Page<ProcessInstanceDto> piPage)
	{
		this.piPage = piPage;
	}

	public Long getToPiId()
	{
		return toPiId;
	}

	public void setToPiId(Long toPiId)
	{
		this.toPiId = toPiId;
	}

	public String getJsonSelPiIds()
	{
		return jsonSelPiIds;
	}

	public void setJsonSelPiIds(String jsonSelPiIds)
	{
		this.jsonSelPiIds = jsonSelPiIds;
	}

}
