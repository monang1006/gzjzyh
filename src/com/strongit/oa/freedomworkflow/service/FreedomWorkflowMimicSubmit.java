package com.strongit.oa.freedomworkflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Scope("prototype")
public class FreedomWorkflowMimicSubmit
{
	@Autowired
	private IFreedomWorkflowTaskService ftSrv;

	private Map<String, String> stepOne;
	
	private Map<String, String> stepTwo;
	
	public void one(Map<String, String> stepOne)
	{
		this.stepOne = stepOne;
	}
	
	public void two(Map<String, String> stepTwo)
	{
		this.stepTwo = stepTwo;
		submit();
	}
	
	private void submit()
	{
		ftSrv.doneTask(stepOne.get("handler"), stepOne.get("ftId"), stepOne.get("ftMemo"));
		List<String> users = new ArrayList<String>();
		users.add(stepOne.get("nextHandler"));
		
		String types = stepOne.get("remindTypes");
		if(StringUtils.isNotEmpty(types) && users.size() > 0)
		{
			ftSrv.sendMsg(types, users, stepOne.get("remindMsg"));
		}
	}
	
}
