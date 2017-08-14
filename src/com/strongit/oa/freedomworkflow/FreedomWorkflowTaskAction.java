package com.strongit.oa.freedomworkflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOaFreedomWorkflow;
import com.strongit.oa.bo.TOaFreedomWorkflowTask;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.freedomworkflow.service.FreedomWorkflowMimicSubmit;
import com.strongit.oa.freedomworkflow.service.IFreedomWorkflowService;
import com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService;
import com.strongit.oa.util.TempPo;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.security.UserInfo;
import com.strongit.uums.util.Const;
import com.strongmvc.webapp.action.BaseActionSupport;

public class FreedomWorkflowTaskAction extends BaseActionSupport<TOaFreedomWorkflowTask>
{

	private TOaFreedomWorkflowTask model = new TOaFreedomWorkflowTask();
	
	@Autowired
	private IFreedomWorkflowTaskService ftSrv;
	
	@Autowired
	private IFreedomWorkflowService fwSrv;
	
	private String jsonHandles;
	
	private String toFwId;
	
	private String formId;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private FreedomWorkflowMimicSubmit mimicSubmit;

	
	
	public TOaFreedomWorkflowTask getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		return null;
	}

	@Override
	public String input() throws Exception
	{
		List<TOaFreedomWorkflowTask> tasks = ftSrv.getFreedomWorkflowTasks(toFwId);
		JSONArray json = new JSONArray();
		for(TOaFreedomWorkflowTask one : tasks)
		{
			JSONObject obj = new JSONObject();
			obj.put("ftTitle", one.getFtTitle());
			obj.put("ftHandler", one.getFtHandler());
			String userId = one.getFtHandler();
			String userName = "";
			if(StringUtils.isNotEmpty(userId))
			{
				User user = userService.getUserInfoByUserId(one.getFtHandler());
				userName = user.getUserName();
			}
			obj.put("handlerName", userName);
			obj.put("ftStatus", one.getFtStatus());
			json.add(obj);
		}
		jsonHandles = json.toString();
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
		if(StringUtils.isNotEmpty(toFwId))
		{
			ftSrv.editFreedomWorkflowTasks(toFwId, jsonHandles);
		}
		return null;
	}
	
	public String doNext() throws Exception
	{
		UserInfo user = (UserInfo) getUserDetails();
		Map<String, String> stepOne = new HashMap<String, String>();
		stepOne.put("handler", user.getUserId());
		stepOne.put("ftId", model.getFtId());
		stepOne.put("ftMemo", model.getFtMemo());
		stepOne.put("remindTypes", getRequest().getParameter("remindTypes"));
		stepOne.put("remindMsg", getRequest().getParameter("remindMsg"));
		stepOne.put("nextHandler", getRequest().getParameter("nextHandler"));
		mimicSubmit.one(stepOne);
		//ftSrv.doneTask(user.getUserId(), model.getFtId(), model.getFtMemo());
		renderHtml("success");
		return null;
	}
	
	public String selectUser() throws Exception
	{
    	List<TempPo> nodes = new LinkedList<TempPo>();
        try {
            List<Organization> orgList = userService.getAllDeparments();// 得到机构列表
            Map<String, List<User>> userMap = userService.getUserMap();
            List<Organization> newList = new ArrayList<Organization>(orgList);
            Collections.sort(newList, new Comparator<Organization>() {

                public int compare(Organization o1, Organization o2) {
                    String code1 = o1.getOrgSyscode();
                    String code2 = o2.getOrgSyscode();
                    if (code1 != null && code2 != null) {
                        return code1.length() - code2.length();
                    }
                    return 0;
                }

            });
            String root = null;
            if (!newList.isEmpty()) {
                root = newList.get(0).getOrgSyscode();
            }
            for (int i = 0; i < orgList.size(); i++) {
                Organization organization = orgList.get(i);
                TempPo po = new TempPo();
                po.setId("o" + organization.getOrgId());
                po.setName(organization.getOrgName());
                String parentId = organization.getOrgParentId();
                po.setType("o");
                /*
                 * if(i == 0){//机构列表根据编码排序，第一个一定是根节点 parentId = "0"; } else {
                 * parentId = "o" + parentId; }
                 */
                if (organization.getOrgSyscode().equals(root)) {
                    parentId = "0";
                } else {
                    parentId = parentId == null ? "0" : "o" + parentId;
                }
                po.setParentId(parentId);
                nodes.add(po);
                // 添加人员
                List<User> userList = userMap.get(organization.getOrgId());
                if (userList != null) {
                    for (User u : userList) {
                        TempPo upo = new TempPo();
                        upo.setId("u" + u.getUserId());
                        upo.setName(u.getUserName());
                        upo.setParentId(po.getId());
                        upo.setType("u");
                        nodes.add(upo);
                    }
                }
            }
            userMap.clear();
            orgList.clear();
        } catch (Exception e) {
            logger.error("选择机构负责人发生异常", e);
        }
        getRequest().setAttribute("data", nodes);
        return "selectUser";
	}
	
	
	/*
	 * getter/setter
	 */

	public String getJsonHandles()
	{
		return jsonHandles;
	}

	public void setJsonHandles(String jsonHandles)
	{
		this.jsonHandles = jsonHandles;
	}

	public String getToFwId()
	{
		return toFwId;
	}

	public void setToFwId(String toFwId)
	{
		this.toFwId = toFwId;
	}

	public String getFormId()
	{
		return formId;
	}

	public void setFormId(String formId)
	{
		this.formId = formId;
	}

}
