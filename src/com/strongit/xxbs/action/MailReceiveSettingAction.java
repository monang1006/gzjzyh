package com.strongit.xxbs.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.dto.MailReceiveDto;
import com.strongit.xxbs.service.IMailReceiveService;
import com.strongmvc.webapp.action.BaseActionSupport;

public class MailReceiveSettingAction extends BaseActionSupport<MailReceiveDto>
{
	private static final long serialVersionUID = 1L;
	
	private IMailReceiveService mailReceiveService;
	
	private MailReceiveDto model = new MailReceiveDto();

	@Autowired
	public void setMailReceiveService(IMailReceiveService mailReceiveService)
	{
		this.mailReceiveService = mailReceiveService;
	}

	public MailReceiveDto getModel()
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
		model = mailReceiveService.get();
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
		mailReceiveService.save(model);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS);
		return null;
	}

}
