package com.strongit.xxbs.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.util.ListUtils;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.security.UserInfo;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.dto.ColumnDto;
import com.strongit.xxbs.entity.TInfoBaseColumn;
import com.strongit.xxbs.entity.TInfoBaseJournal;
import com.strongit.xxbs.entity.TInfoBasePiece;
import com.strongit.xxbs.service.IColumnService;
import com.strongit.xxbs.service.IJournalService;
import com.strongit.xxbs.service.IOrgService;
import com.strongit.xxbs.service.IPieceService;
import com.strongit.xxbs.service.JdbcColumnService;
import com.strongit.xxbs.service.impl.JournalService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class PieceAction extends BaseActionSupport<TInfoBasePiece>
{
	private static final long serialVersionUID = 1L;
	
	private TInfoBasePiece model = new TInfoBasePiece();
	
	private IPieceService pieceService;
	private IOrgService orgService;
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBasePiece> page;
	
	private String op;
	private String toId;
	private String orgId;
	private String pieceDate;
	private String flag;
	private String nature;
	
	
	@Autowired
	public void setOrgService(IOrgService orgService) {
		this.orgService = orgService;
	}

	@Autowired
	public void setPieceService(IPieceService pieceService) {
		this.pieceService = pieceService;
	}

	public TInfoBasePiece getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		String flag = pieceService.deletePiece(toId);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}

	@Override
	public String input() throws Exception
	{
		
		if("add".equals(op))
		{
			String orgId = getRequest().getParameter("orgId");
			TUumsBaseOrg org = orgService.getOrgById(orgId);
			 nature = org.getOrgNature();
			if("4".equals(nature)){
				 nature= "true";
			}
			model.setOrgId(orgId);
			model.setPieceOpen(0);
			model.setIsInstruction("0");
		}
		if("edit".equals(op))
		{
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
			model = pieceService.getPiece(toId);
			TUumsBaseOrg org = orgService.getOrgById(model.getOrgId());
			 nature = org.getOrgNature();
			if("4".equals(nature)){
				 nature= "true";
			}
			Date time = model.getPieceTime();
			pieceDate = sdfTime.format(time);
		}
		return INPUT;
	}
	
	public String input1() throws Exception
	{
		
		if("add".equals(op))
		{
			String orgId = getRequest().getParameter("orgId");
			TUumsBaseOrg org = orgService.getOrgById(orgId);
			 nature = org.getOrgNature();
			if("4".equals(nature)){
				 nature= "true";
			}
			model.setOrgId(orgId);
			model.setPieceOpen(0);
		}
		if("edit".equals(op))
		{
			SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
			model = pieceService.getPiece(toId);
			TUumsBaseOrg org = orgService.getOrgById(model.getOrgId());
			 nature = org.getOrgNature();
			if("4".equals(nature)){
				 nature= "true";
			}
			Date time = model.getPieceTime();
			pieceDate = sdfTime.format(time);
		}
		return "input1";
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
		String pieceTitle = getRequest().getParameter("pieceTitle");
		String pieceTime = getRequest().getParameter("pieceTime");
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
		Date time = sdfTime.parse(pieceTime);
		String pieceCode = getRequest().getParameter("pieceCode");
		String isInstruction = getRequest().getParameter("isInstruction");
		int pcode = Integer.parseInt(pieceCode);
		String orgId = getRequest().getParameter("orgId");
		String pieceFlag = getRequest().getParameter("pieceFlags");
		TUumsBaseOrg org = orgService.getOrgById(orgId);
		if("2".equals(org.getOrgNature())){
			model.setParentOrgId(org.getOrgParentId());
		}
		model.setPieceTitle(pieceTitle);
		model.setPieceTime(time);
		model.setPieceCode(pcode);
		model.setOrgId(orgId);
		model.setPieceFlag(pieceFlag);
		if(isInstruction!=null){
		model.setIsInstruction(isInstruction);
		}
		pieceService.savePiece(model);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		return null;
	}

	public String showList() throws Exception
	{
		page = new Page<TInfoBasePiece>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		String flag = getRequest().getParameter("flag");
		String orgId = getRequest().getParameter("orgId");
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String pieceTitle = getRequest().getParameter("pieceTitle");
			pieceTitle = pieceTitle.replace("%", "\'\'%");
			if((flag!=null)&&("guoban".equals(flag))){
				page = pieceService.getPiecesByTitle(page, pieceTitle,orgId,"1");
			}
			else if((flag!=null)&&("shengji".equals(flag))){
				page = pieceService.getPiecesByTitle(page, pieceTitle,orgId,"3");
			}
			else{
				page = pieceService.getPiecesByTitle(page, pieceTitle,orgId,"2");
			}
			
		}
		else
		{
			if((flag!=null)&&("guoban".equals(flag))){
				page = pieceService.getPieces(page,orgId,"1");
			}
			else if((flag!=null)&&("shengji".equals(flag))){
				page = pieceService.getPieces(page,orgId,"3");
			}
			else{
				page = pieceService.getPieces(page,orgId,"2");
			}
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TInfoBasePiece> result = page.getResult();
		JSONArray rows = new JSONArray();
		JSONObject obj = null;
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> orgNames = orgService.getOrgNames();
		if(result != null)
		{
		for(TInfoBasePiece one : result)
		{
			obj = new JSONObject();
			obj.put("pieceId", one.getPieceId());
			obj.put("pieceTitle", one.getPieceTitle());
			obj.put("pieceTime", sdfTime.format(one.getPieceTime()));
			obj.put("pieceCode", one.getPieceCode());
			obj.put("pieceOpen", one.getPieceOpen());
			obj.put("isInstruction", one.getIsInstruction());
			obj.put("orgName", orgNames.get(one.getOrgId()));
			rows.add(obj);
		}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	public String showListByjiafen() throws Exception
	{
		page = new Page<TInfoBasePiece>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		
		page.setOrder(sord);
		page.setOrderBy(sidx);
		String flag = getRequest().getParameter("flag");
		String orgId = getRequest().getParameter("orgId");
		if(getRequest().getParameter("isSearch") != null 
				&& getRequest().getParameter("isSearch").equals("true"))
		{
			String pieceTitle = getRequest().getParameter("pieceTitle");
			pieceTitle = pieceTitle.replace("%", "\'\'%");
			page = pieceService.getPiecesByTitle(page, pieceTitle,orgId,"4");
			
		}
		else
		{
				page = pieceService.getPieces(page,orgId,"4");
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TInfoBasePiece> result = page.getResult();
		JSONArray rows = new JSONArray();
		JSONObject obj = null;
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> orgNames = orgService.getOrgNames();
		if(result != null)
		{
		for(TInfoBasePiece one : result)
		{
			obj = new JSONObject();
			obj.put("pieceId", one.getPieceId());
			obj.put("pieceTitle", one.getPieceTitle());
			obj.put("pieceTime", sdfTime.format(one.getPieceTime()));
			obj.put("pieceCode", one.getPieceCode());
			obj.put("pieceOpen", one.getPieceOpen());
			obj.put("orgName", orgNames.get(one.getOrgId()));
			rows.add(obj);
		}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	
	public String content() throws Exception
	{
		if("jiafen".equals(flag)){
			return "content1";
		}
		return "content";
	}

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public int getUnitpage() {
		return unitpage;
	}

	public void setUnitpage(int unitpage) {
		this.unitpage = unitpage;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public Page<TInfoBasePiece> getPage() {
		return page;
	}

	public void setPage(Page<TInfoBasePiece> page) {
		this.page = page;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public IPieceService getPieceService() {
		return pieceService;
	}

	public void setModel(TInfoBasePiece model) {
		this.model = model;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPieceDate() {
		return pieceDate;
	}

	public void setPieceDate(String pieceDate) {
		this.pieceDate = pieceDate;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	
	
	
	/*
	 * 以下是setter/getter
	 */
	

	
	
}
