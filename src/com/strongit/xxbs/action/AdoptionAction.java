package com.strongit.xxbs.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.dto.AdoptionDto;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.service.IGradeService;
import com.strongmvc.webapp.action.BaseActionSupport;

public class AdoptionAction extends BaseActionSupport<AdoptionDto>
{
	private static final long serialVersionUID = 1L;
	
	private IGradeService gradeService;
	
	private AdoptionDto model = new AdoptionDto();
	
	private String toId;
	private TInfoBasePublish publish;
	
	private String scoreUse;
	private String scoreInstruction;

	@Autowired
	public void setGradeService(IGradeService gradeService)
	{
		this.gradeService = gradeService;
	}

	@Override
	public String delete() throws Exception
	{
		return null;
	}

	public AdoptionDto getModel()
	{
		return model;
	}

	@Override
	public String input() throws Exception
	{
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
		return null;
	}
	
	public String viewInstruction() throws Exception
	{
		//TInfoBasePublish publish = submitService.getSubmit(toId);
		//pubTitle = publish.getPubTitle();
		//model = adoptionService.getAdoption(toId);
		return "viewInstruction";
	}
	
	public String scoreSetting() throws Exception
	{
		scoreUse = gradeService.get("score.use");
		scoreInstruction = gradeService.get("score.instruction");
		return "scoreSetting";
	}
	
	public String saveScoreSetting() throws Exception
	{
		gradeService.set("score.use", scoreUse);
		gradeService.set("score.instruction", scoreInstruction);
		gradeService.save();
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS);
		return null;
	}
	
	
	
	
	
	/*
	 * 以下是setter/getter
	 */


	public String getToId()
	{
		return toId;
	}

	public void setToId(String toId)
	{
		this.toId = toId;
	}

	public TInfoBasePublish getPublish()
	{
		return publish;
	}

	public void setPublish(TInfoBasePublish publish)
	{
		this.publish = publish;
	}

	public String getScoreUse()
	{
		return scoreUse;
	}

	public void setScoreUse(String scoreUse)
	{
		this.scoreUse = scoreUse;
	}

	public String getScoreInstruction()
	{
		return scoreInstruction;
	}

	public void setScoreInstruction(String scoreInstruction)
	{
		this.scoreInstruction = scoreInstruction;
	}
	
}
