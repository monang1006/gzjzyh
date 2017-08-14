package com.strongit.xxbs.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.runqian.report.engine.operator.In;
import com.strongit.oa.util.OALogInfo;
import com.strongit.platform.webcomponent.tree.JsonUtil;
import com.strongit.platform.webcomponent.tree.TreeNode;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.security.UserInfo;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongit.xxbs.common.CharsetUtil;
import com.strongit.xxbs.common.PathUtil;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.CommentDto;
import com.strongit.xxbs.dto.JournalListDto;
import com.strongit.xxbs.dto.MailReceiveDto;
import com.strongit.xxbs.dto.SubmitDto;
import com.strongit.xxbs.entity.TInfoBaseComment;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.entity.TInfoQueryTree;
import com.strongit.xxbs.service.IInvitationService;
import com.strongit.xxbs.service.IMailReceiveService;
import com.strongit.xxbs.service.IOrgService;
import com.strongit.xxbs.service.IPublishService;
import com.strongit.xxbs.service.ISubmitService;
import com.strongit.xxbs.service.JdbcSubmitService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * Project : 政务信息采编与报送系统
 * Copyright : Strong Digital Technology Co. Ltd.
 * All right reserved
 * @author 钟伟
 * @version 1.0, 2012-4-11
 * Description: 信息报送Action类
 */
/**
 * 
 * Project : 政务信息采编与报送系统
 * Copyright : Strong Digital Technology Co. Ltd.
 * All right reserved
 * @author 钟伟
 * @version 1.0, 2012-4-11
 * Description:
 */
public class SubmitAction extends BaseActionSupport<TInfoBasePublish>
{
	private static final long serialVersionUID = 1L;
	
	private TInfoBasePublish model = new TInfoBasePublish();
	private ISubmitService submitService;
	private IPublishService publishService;
	private IOrgService orgService;
	@Autowired private IUserService userService;
	private IInvitationService invitationService;
	private JdbcSubmitService jdbcSubmitService;
	@Autowired
	private IMailReceiveService mailReceiveService;
	
	private int curpage;
	private int unitpage;
	private String sidx;
	private String sord;
	private Page<TInfoBasePublish> page;
	private Page<SubmitDto> pagePl;
	private Map<String, String> mapColumns = 
			new HashMap<String, String>();
	
	private Integer submittedNum;
	private Integer usedNum;

	private String op;
	private String toId;
	private String orgName;
	private String userName;
	private String aptTitle;
	private String columnName;
	private String submitStatus = Publish.ALL;
	private String userTelephone;
	private String textContent;
	private String userMail;
	
	private File upload;
	private String uploadFileName;
	private String uploadContentType;
	
	private String IssuePeople; //签发领导
	
	private String editor; //责任编辑

	private String isShared = "";//是否成刊
	
	private File file1;  //附件1
	private File file2;  //附件2
	
	private String file1FileName;
	private String file2FileName;
	private List<TUumsBaseOrg> orgList;
	private String dp[];
	
	@Autowired
	public void setJdbcSubmitService(JdbcSubmitService jdbcSubmitService) {
		this.jdbcSubmitService = jdbcSubmitService;
	}

	@Autowired
	public void setPublishService(IPublishService publishService)
	{
		this.publishService = publishService;
	}

	@Autowired
	public void setSubmitService(ISubmitService submitService)
	{
		this.submitService = submitService;
	}
	
	@Autowired
	public void setOrgService(IOrgService orgService)
	{
		this.orgService = orgService;
	}

	@Autowired
	public void setInvitationService(IInvitationService invitationService)
	{
		this.invitationService = invitationService;
	}

	public TInfoBasePublish getModel()
	{
		return model;
	}

	@Override
	public String delete() throws Exception
	{
		return null;
	}
	
	public String deleteNotSubmitted() throws Exception
	{
		String flag = publishService.deleteByBs(toId);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}
	
	public String deleteSubmitted() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String userId = userInfo.getUserId();
		String mid[] = toId.split(",");
		String flag = publishService.deletePU(toId, userId);
		flag = publishService.deleteByCb(toId,new OALogInfo("信息采编-删除信息-『删除条数』："+mid.length));
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}
	
	public String isExistTitle() throws Exception
	{
		String title = getRequest().getParameter("title");
		String t = publishService.isExistTitle(title).toString();
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(t);
		return null;
	}

	@Override
	public String input() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = "";
		String userId = "";
		if(op.equals("add"))
		{
			if(!userInfo.getIsAdmin()&&!userInfo.getIsSubmit()){
				orgList = orgService.getOrgNames1();
			}
			model.setPubIsShare(Publish.NO_SHARED);
			model.setPubUseStatus(Publish.NO_USED);
			model.setPubIsText(Publish.TEXT_CONTENT);
			model.setPubIsInstruction(Publish.NO_INSTRUCTION);
			model.setPubIsComment(Publish.NO_COMMENTED);
			model.setPubIsMailInfo(Publish.NO_MAIL_INFO);
			
			userId = userInfo.getUserId();
			orgId = userInfo.getOrgId();
			userTelephone = userInfo.getUserTel();
			model.setPubPublisherId(userId);			
			model.setOrgId(orgId);
		}
		else if(op.equals("edit"))
		{
			userTelephone = userInfo.getUserTel();
			model = publishService.getPublish(toId);
			userId = userInfo.getUserId();
			orgId = model.getOrgId();
			String title = model.getPubRawTitle();
			title = title.replaceAll( "\"", "\\“" );
			model.setPubTitle(title);
		}
		userMail = userInfo.getUserEmail();

		TUumsBaseUser user = userService.getUserInfoByUserId(userId);
		String userTelephone = user.getUserTel();
		getRequest().setAttribute("userTelephone", userTelephone);
		userName = user.getUserName();
		TUumsBaseOrg org = userService.getOrgInfoByOrgId(orgId);
		orgName = org.getOrgName();
		getRequest().setAttribute("orgName", orgName);
		TUumsBaseOrg baseOrg = userService.getOrgInfoByOrgId(orgId);
		String rest1 = baseOrg.getRest3();
		if(rest1!=null&&!rest1.equals("")){
		String rs1[] = rest1.split(",");
		if((!"".equals(model.getPubEditor()))&&((model.getPubEditor()!=null))){
			IssuePeople = model.getPubEditor();
		}
		else{
			IssuePeople = rs1[0].trim();
		}
		if((!"".equals(model.getPubSigner()))&&((model.getPubSigner()!=null))){
			editor = model.getPubSigner();
		}
		else{
			editor = rs1[1].trim();
		}
		}
		return INPUT;
	}

	@Override
	public String list() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();			
		//submittedNum = publishService.numSubmitted(orgId);
		//usedNum = publishService.numUsed(orgId);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception
	{
	}
	
	public String saveMail() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String uId = userInfo.getUserId();
		TUumsBaseUser user = userService.getUserInfoByUserId(uId);
		user.setUserEmail(userMail);
		userService.saveUserInfo(user);
		userInfo.setUserEmail(userMail);
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(Info.SUCCESS);
		return null;
	}

	//上报
	@Override
	public String save() throws Exception
	{
		model.setPubSubmitStatus(Publish.SUBMITTED);

		commonSave();
		return null;
	}
	
	private void sendMail()
	{
		/*MailReceiveDto mr = mailReceiveService.get();
		SimpleEmail email = new SimpleEmail();
		email.setSmtpPort(mr.getSmtpPort());
		email.setHostName(mr.getSmtpUrl());
		//email.setAuthentication("strongtest123@163.com", "strongtest789");
		try
		{
			email.addTo(mr.getBsmail());
			UserInfo userInfo = (UserInfo) getUserDetails();
			email.setFrom(userInfo.getUserEmail());
			email.setSubject(Publish.BSMAIL_TAG+model.getPubTitle());
			email.setCharset("GBK");
			email.setMsg(model.getPubRawContent());
			email.send();
		}
		catch (EmailException e)
		{
			e.printStackTrace();
		}*/
	}
	
	//待报
	public String saveNotSubmitted() throws Exception
	{
		model.setPubSubmitStatus(Publish.NO_SUBMITTED);
		commonSave();
		return null;
	}
	
	private void commonSave() throws Exception
	{
		if(upload!=null){
		if(!Publish.TEXT_CONTENT.equals(model.getPubIsText()))
		{
			//HttpSession session = getRequest().getSession();
			//String uFile = (String) session.getAttribute("uploadedFile");
			//File file = new File(uFile);
			FileInputStream fIn = null;
			
			try
			{				
				fIn = new FileInputStream(upload);
				byte[] bys = IOUtils.toByteArray(fIn);
				String charset = CharsetUtil.getCharset(bys);				
				String word = new String(bys, charset);
				
				model.setPubRawContent(word);
				model.setPubEditContent(word);
				model.setPubWord(bys);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				//fIn.close();
				//deleteTempFile();
			}
			if(uploadFileName!=null){
			//String fileExt = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
			//fileExt = fileExt.toLowerCase();
			model.setPubWordName(uploadFileName);
			}
		}
		}
		else
		{
			model.setPubEditContent(model.getPubRawContent());
		}
		model.setPubRawTitle(model.getPubTitle());
		Map<String, String> orgNames = orgService.getOrgNames();
		String mname = orgNames.get(model.getOrgId());
		model.setOrgName(mname);
		//针对金仓
		if("".equals(model.getTInfoBaseAppoint().getAptId()))
		{
			model.setTInfoBaseAppoint(null);
		}
		
		model.setPubDate(new Date());
		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();		
		TUumsBaseOrg baseOrg = userService.getOrgInfoByOrgId(orgId);
		String rest1 = baseOrg.getRest3();
		if("".equals(rest1)||rest1==null){
			IssuePeople =  model.getPubSigner();
			editor =   model.getPubEditor();
			rest1 = IssuePeople+","+editor;
			baseOrg.setRest3(rest1);
			orgService.updateOrg(baseOrg);
		}
		
		//当是报送时，发送邮件
		if(Publish.SUBMITTED.equals(model.getPubSubmitStatus()))
		{
			sendMail();
		}
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		
		String id = sim.format(date);
		FileInputStream fIn = null;
		FileOutputStream fOut = null;
		FileInputStream fIn2 = null;
		FileOutputStream fOut2 = null;
		String flag = "";
		try
		{
			if(file1 != null)
			{
				if(file1.length()>0){
				fIn = new FileInputStream(file1);
				byte[] bys = IOUtils.toByteArray(fIn);
				String fie1name = "";
				if(file1FileName!=null){
				 fie1name = file1FileName.substring(file1FileName.indexOf("."), file1FileName.length());
				}
				fOut = new FileOutputStream(PathUtil.upload() + id+"-1" + fie1name);
				System.out.print(file1.length());
				if(file1.length()<20000000){
				model.setPubFile1(PathUtil.upload() + id+"-1" + fie1name);
				model.setPubFile1Name(file1FileName);
				System.out.println(PathUtil.upload() + id + "-header.doc");
				
				IOUtils.write(bys, fOut);
				}
				else{
					flag= "nosuccess";
				}
				}
			}
			
			if(file2 != null)
			{
				if(file2.length()>0){
				fIn2 = new FileInputStream(file2);
				byte[] bys2 = IOUtils.toByteArray(fIn2);
				String fie2name = "";
				if(file2FileName!=null){
				fie2name = file2FileName.substring(file2FileName.indexOf("."), file2FileName.length());
				}
				fOut2 = new FileOutputStream(PathUtil.upload() + id+"-2" + fie2name);
				if(file1.length()<20000000){
				model.setPubFile2(PathUtil.upload() + id+"-2" + fie2name);
				
				model.setPubFile2Name(file2FileName);
				IOUtils.write(bys2, fOut2);
				}
				else{
					flag= "nosuccess";
				}
				}
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(file1 != null)
			{
				if(file1.length()>0){
				fIn.close();
				fOut.close();
				}
			}
			if(file2 != null)
			{
				if(file2.length()>0){
				fIn2.close();
				fOut2.close();
				}
			}
		}
		getResponse().setContentType("text/html");
		if("nosuccess".equals(flag)){
			getResponse().getWriter().write(Info.NO_UPLOAD);
		}
		else{
		getResponse().getWriter().write(Info.SUCCESS_AND_CLOSE);
		publishService.savePublish(model,new OALogInfo("信息上报-上报信息-『上报』："
				+ model.getPubTitle()));
		}
	}
	
	public String officeStream() throws Exception
	{
		if(op != null || op != "")
		{
			TInfoBasePublish submit = publishService.getPublish(toId);
			byte[] office = submit.getPubWord();
			if(office != null)
			{
				getResponse().setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(submit.getPubWordName(), "UTF-8"));

				getResponse().setContentType("application/x-download");
				//getResponse().addHeader("Content-Disposition", "attachment;filename="
				//		+ new String(newConAttch.getAttachFileName().getBytes("gb2312"),
				//		"iso8859-1"));

				byte[] bOffice = office;
				ServletOutputStream outStream = null;
				try
				{
					outStream = getResponse().getOutputStream();
					outStream.write(bOffice);
					outStream.flush();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					//outStream.close();
				}
			}
		}
		return null;
	}
	
	public String officeStream1() throws Exception
	{
			if("1".equals(op)){
			file1 = new File(PathUtil.files()+"/GB2312.TTF");
			}
			else if("2".equals(op)){
				file1 = new File(PathUtil.files()+"/simkai.ttf");	
			}
			file1FileName = file1.getName();
			FileInputStream filein = new FileInputStream(file1.getPath());
			InputStream fileout = new BufferedInputStream(filein);
			byte[] office = IOUtils.toByteArray(fileout);
			if(office != null)
			{
				getResponse().setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(file1FileName, "UTF-8"));

				getResponse().setContentType("application/x-download");
				//getResponse().addHeader("Content-Disposition", "attachment;filename="
				//		+ new String(newConAttch.getAttachFileName().getBytes("gb2312"),
				//		"iso8859-1"));

				byte[] bOffice = office;
				ServletOutputStream outStream = null;
				try
				{
					outStream = getResponse().getOutputStream();
					outStream.write(bOffice);
					outStream.flush();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					outStream.close();
				}
			}
		return null;
	}
	
	public String officeStream2() throws Exception
	{
			String file = getRequest().getParameter("file");
			String fileName = getRequest().getParameter("fileName");
			fileName = java.net.URLDecoder.decode(fileName,"UTF-8"); 
			file1 = new File(file);
			FileInputStream filein = new FileInputStream(file1.getPath());
			InputStream fileout = new BufferedInputStream(filein);
			byte[] office = IOUtils.toByteArray(fileout);
			if(office != null)
			{
				getResponse().setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));

				getResponse().setContentType("application/x-download");
				//getResponse().addHeader("Content-Disposition", "attachment;filename="
				//		+ new String(newConAttch.getAttachFileName().getBytes("gb2312"),
				//		"iso8859-1"));

				byte[] bOffice = office;
				ServletOutputStream outStream = null;
				try
				{
					outStream = getResponse().getOutputStream();
					outStream.write(bOffice);
					outStream.flush();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					//outStream.close();
				}
			}
		return null;
	}
	
//	public String wordFile() throws Exception
//	{
//		File file = new File("d:/temp/tem.docx");
//		FileInputStream fIn = new FileInputStream(file);
//		
//		ServletOutputStream outStream = null;
//		try
//		{
//			outStream = getResponse().getOutputStream();
//			//outStream.write(bOffice);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		finally
//		{
//			outStream.close();
//		}
//		return null;
//	}
	
	public String saveTempFile() throws Exception
	{
		HttpSession session = getRequest().getSession();
		String fileExt = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
		fileExt = fileExt.toLowerCase();
		if("doc".equals(fileExt) || "docx".equals(fileExt))
		{
			FileInputStream fIn = null;
			FileOutputStream fOut = null;
			try
			{
				fIn = new FileInputStream(upload);
		        String s = getClass().getResource("/").toString();
		        s = s.substring(6);	        
				String tempPath = s+"../jsp/xxbs/tempfile/";
				String oFile = tempPath + UUID.randomUUID().toString() + "." + fileExt;
				session.setAttribute("uploadedFile", oFile);
				fOut = new FileOutputStream(oFile);
		        byte buffer[] = new byte[8192];
		        int count = 0;
		        while((count = fIn.read(buffer)) > 0)
		        {
		            fOut.write(buffer, 0, count);
		        }
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				fIn.close();
				fOut.close();
			}
		}
		getResponse().setContentType("text/html");
		getResponse().getWriter().write("success");
		return null;
	}
	
	public String deleteTempFile() throws Exception
	{
		HttpSession session = getRequest().getSession();
		String uFile = (String) session.getAttribute("uploadedFile");
		if(!"".equals(uFile))
		{
			File file = new File(uFile);
			file.delete();
			session.removeAttribute("uploadedFile");
		}
		return null;
	}
	
	public String listSubmit() throws Exception
	{
		page = new Page<TInfoBasePublish>(unitpage, true);
		page.setPageNo(curpage);
		page.setPageSize(unitpage);
		page.setOrder(sord);
		page.setOrderBy(sidx);
		
		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();
		
		if(getRequest().getParameter("isSearch") != null 
				&& "true".equals(getRequest().getParameter("isSearch")))
		{
			Map<String, String> search = new HashMap<String, String>();			
			String pubTitle = getRequest().getParameter("pubTitle");
			pubTitle = pubTitle.replace("%", "\'\'%");
			String startDate = getRequest().getParameter("startDate");
			String endDate = getRequest().getParameter("endDate");
			search.put("pubTitle", pubTitle);
			search.put("startDate", startDate);
			search.put("endDate", endDate);
			search.put("submitStatus", submitStatus);
			page = publishService.findPublishs(page, orgId, search, true);
		}
		else
		{
			page = publishService.getPublishs2(page, orgId, submitStatus, Publish.ALL);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(page.getTotalPages()==-1){
			page.setTotalCount(0);
		}
		jsonObj.put("totalpages", page.getTotalPages());
		jsonObj.put("totalrecords", page.getTotalCount());
		
		List<TInfoBasePublish> result = page.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for(TInfoBasePublish one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("pubId", one.getPubId());
				obj.put("pubTitle", one.getPubTitle());
				obj.put("pubInfoType", one.getPubInfoType());
				String isAppoint = (one.getTInfoBaseAppoint() == null)
							? Publish.NO_APPOINT : Publish.APPOINTED;
				obj.put("isAppoint", isAppoint);
				obj.put("pubUseStatus", one.getPubUseStatus());
				obj.put("submitStatus", one.getPubSubmitStatus());
				obj.put("pubSubmitStatus", one.getPubSubmitStatus());
				obj.put("pubIsShare", one.getPubIsShare());
				obj.put("pubIsComment", one.getPubIsComment());
				obj.put("pubIsInstruction", one.getPubIsInstruction());
				obj.put("pubDate", sdf.format(one.getPubDate()));
				obj.put("pubIsComment", one.getPubIsComment());
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
	
	public String JdbcListSubmit() throws Exception
	{
		pagePl = new Page<SubmitDto>(unitpage, true);
		pagePl.setPageNo(curpage);
		pagePl.setPageSize(unitpage);
		pagePl.setOrder(sord);
		pagePl.setOrderBy(sidx);
		
		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();
		Map<String, String> search = new HashMap<String, String>();		
		if(getRequest().getParameter("isSearch") != null 
				&& "true".equals(getRequest().getParameter("isSearch")))
		{
			String pubTitle = getRequest().getParameter("pubTitle");
			pubTitle = pubTitle.replace("%", "\'\'%");
			String startDate = getRequest().getParameter("startDate");
			String endDate = getRequest().getParameter("endDate");
			search.put("pubTitle", pubTitle);
			search.put("startDate", startDate);
			search.put("endDate", endDate);
			search.put("submitStatus", submitStatus);
			pagePl = jdbcSubmitService.getPublishs(pagePl, orgId, submitStatus,search,isShared);
		}
		else
		{
			pagePl = jdbcSubmitService.getPublishs(pagePl, orgId, submitStatus,search ,isShared);
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("curpage", curpage);
		if(pagePl.getTotalPages()==-1){
			pagePl.setTotalCount(0);
		}
		jsonObj.put("totalpages", pagePl.getTotalPages());
		jsonObj.put("totalrecords", pagePl.getTotalCount());
		
		List<SubmitDto> result = pagePl.getResult();
		JSONArray rows = new JSONArray();
		if(result != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for(SubmitDto one : result)
			{
				JSONObject obj = new JSONObject();
				obj.put("pubId", one.getPubId());
				obj.put("pubTitle", one.getPubTitle());
				obj.put("pubInfoType", one.getPubIsInstruction());
				String isAppoint = (one.getAptId() == null)
							? Publish.NO_APPOINT : Publish.APPOINTED;
				obj.put("isAppoint", isAppoint);
				obj.put("pubUseStatus", one.getPubUseStatus());
				obj.put("submitStatus", one.getPubSubmitStatus());
				obj.put("pubSubmitStatus", one.getPubSubmitStatus());
				obj.put("pubIsShare", one.getPubIsShare());
				obj.put("pubIsComment", one.getPubIsComment());
				obj.put("pubDate", sdf.format(one.getPubDate()));
				obj.put("pubIsComment", one.getPubIsComment());
				rows.add(obj);
			}
		}
		jsonObj.put("rows", rows);
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonObj.toString());
		return null;
	}
	
//	public String notSubmitted() throws Exception
//	{
//		return "notSubmitted";
//	}
//	
//	public String submitted() throws Exception
//	{
//		return null;
//	}
	
	public String view() throws Exception
	{
		model = publishService.getPublish(toId);
		String title = model.getPubTitle();
		title = title.replaceAll( "\"", "\\“" );
		model.setPubTitle(title);
		TUumsBaseUser user = userService.getUserInfoByUserId(model.getPubPublisherId());
		userName = user.getUserName();
		getRequest().setAttribute("userName", userName);
		TUumsBaseOrg org = userService.getOrgInfoByOrgId(model.getOrgId());
		orgName = org.getOrgName();
		getRequest().setAttribute("orgName", orgName);
		return "view";
	}

	public String viewComment() throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<CommentDto> comm = new ArrayList<CommentDto>();
		List<TInfoBaseComment> comment =  publishService.findComment(toId);
		for(int i=0;i<comment.size();i++){
			String userId = comment.get(i).getComUserId();
			UserInfo userInfo = (UserInfo) getUserDetails();
			String userId1 = userInfo.getUserId();
			String true1 ="";
			TUumsBaseUser user = userService.getUserInfoByUserId(userId);
			String username = user.getUserName();
			CommentDto com = new CommentDto();
			com.setCommentName(username);
			String date = sdf.format(comment.get(i).getComDate());
			com.setCommentDate(date);
			if(userId.equals(userId1)){
				true1 = "1";
			}
			else{
				true1 ="0";
			}
			com.setCommentIstrue(true1);
			com.setCommentInfo(comment.get(i).getComInfo());
			com.setCommentId(comment.get(i).getComId());
			comm.add(com);
		}
		ActionContext.getContext().put("comm", comm);
		TInfoBasePublish publish = publishService.getPublish(toId);
		String userId = publish.getPubAdoptUserId();
		if(userId!=null){
		String userName = userService.getUserInfoByUserId(userId).getUserName();
		String info = publish.getPubComment();
		String date = sdf.format(publish.getPubSubmitDate());
		dp = new String[3];
		dp[0]=userName;
		dp[1]= date;
		dp[2]= info;
		getRequest().setAttribute("dp",dp );
		}
		return "viewComment";
	}
	
	
	public String tree() throws Exception
	{
		return "tree";
	}
	
	public String showTree() throws Exception
	{
		List<TreeNode> nodeLst = new ArrayList<TreeNode>(0);
		List<TUumsBaseOrg> allList = userService.getSelfAndChildOrgsByOrgSyscodeByHa("001", "0", "60075BA77A04D77EE040007F01001D7E");
		int topOrgcodelength = 3;
		TreeNode node;
		String parentCode;
		String codeRule = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);
		for(TUumsBaseOrg org : allList)
		{
			if(!("6").equals(org.getOrgNature())){
			node = new TreeNode();
			node.setId(org.getOrgSyscode());
			parentCode = this.getParentCode(org.getOrgSyscode(), codeRule);
			node.setPid(org.getOrgSyscode().length() == topOrgcodelength?"-1":parentCode);
			node.setComplete(true);
			node.setIsexpand(org.getOrgSyscode().length() == topOrgcodelength?true:false);
			
			if(!("5").equals(org.getOrgNature())){
				if(org.getOrgName().equals("公文传输单位")){
					node.setShowcheck(false);
				}else{
			    node.setShowcheck(true);
				}
			}
			else{
				node.setShowcheck(false);
			}
			node.setText(org.getOrgName());
			node.setValue(org.getOrgId());
			nodeLst.add(node);
			}
		}

		String jsonTreeNode = JsonUtil.fromTreeNodes(nodeLst);
		
		getResponse().setContentType("application/json");
		getResponse().getWriter().write(jsonTreeNode);
		return null;
	}
	/**
	 * 得到父级代码。
	 * 
	 * @param codeRule -
	 *            编码规则
	 * @param code -
	 *            代码
	 * @return String - 父级代码,如果为最顶级则返回""
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 */
	private String getParentCode(String code, String codeRule)
			throws Exception {
		String parentCode = null;
		if (code != null && codeRule != null) {
			// 校验代码编码规则
			// 分析编码规则
			List<Integer> lstCodeRule = new ArrayList<Integer>();
			for (int i = 0; i < codeRule.length(); i++) {
				String str = codeRule.substring(i, i + 1);
				lstCodeRule.add(new Integer(str));
			}
			int codeLength = code.length();
			int count = 0;
			for (Iterator<Integer> iter = lstCodeRule.iterator(); iter
					.hasNext();) {
				Integer len = iter.next();
				count += len.intValue();
				if (count == codeLength) {
					count -= len.intValue();
					parentCode = code.substring(0, count);
				}
			}
		}
		return parentCode;
	}
	
	
	/*
	 * 以下是getter/setter
	 */

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

	public Page<TInfoBasePublish> getPage()
	{
		return page;
	}

	public void setPage(Page<TInfoBasePublish> page)
	{
		this.page = page;
	}

	public String getOp()
	{
		return op;
	}

	public void setOp(String op)
	{
		this.op = op;
	}

	public String getToId()
	{
		return toId;
	}

	public void setToId(String toId)
	{
		this.toId = toId;
	}

	public String getOrgName()
	{
		return orgName;
	}

	public void setOrgName(String orgName)
	{
		this.orgName = orgName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public File getUpload()
	{
		return upload;
	}

	public void setUpload(File upload)
	{
		this.upload = upload;
	}

	public String getUploadFileName()
	{
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName)
	{
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType()
	{
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType)
	{
		this.uploadContentType = uploadContentType;
	}

	public String getAptTitle()
	{
		return aptTitle;
	}

	public void setAptTitle(String aptTitle)
	{
		this.aptTitle = aptTitle;
	}

	public Map<String, String> getMapColumns()
	{
		return mapColumns;
	}

	public void setMapColumns(Map<String, String> mapColumns)
	{
		this.mapColumns = mapColumns;
	}

	public String getColumnName()
	{
		return columnName;
	}

	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

	public String getSidx()
	{
		return sidx;
	}

	public void setSidx(String sidx)
	{
		this.sidx = sidx;
	}

	public String getSord()
	{
		return sord;
	}

	public void setSord(String sord)
	{
		this.sord = sord;
	}

	public String getSubmitStatus()
	{
		return submitStatus;
	}

	public void setSubmitStatus(String submitStatus)
	{
		this.submitStatus = submitStatus;
	}

	public Integer getSubmittedNum()
	{
		return submittedNum;
	}

	public void setSubmittedNum(Integer submittedNum)
	{
		this.submittedNum = submittedNum;
	}

	public Integer getUsedNum()
	{
		return usedNum;
	}

	public void setUsedNum(Integer usedNum)
	{
		this.usedNum = usedNum;
	}

	public String getUserTelephone()
	{
		return userTelephone;
	}

	public void setUserTelephone(String userTelephone)
	{
		this.userTelephone = userTelephone;
	}

	public String getTextContent()
	{
		return textContent;
	}

	public void setTextContent(String textContent)
	{
		this.textContent = textContent;
	}

	public String getIssuePeople() {
		return IssuePeople;
	}

	public void setIssuePeople(String issuePeople) {
		IssuePeople = issuePeople;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Page<SubmitDto> getPagePl() {
		return pagePl;
	}

	public void setPagePl(Page<SubmitDto> pagePl) {
		this.pagePl = pagePl;
	}

	public String getIsShared() {
		return isShared;
	}

	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}

	public String getUserMail()
	{
		return userMail;
	}

	public void setUserMail(String userMail)
	{
		this.userMail = userMail;
	}

	public File getFile1() {
		return file1;
	}

	public void setFile1(File file1) {
		this.file1 = file1;
	}

	public File getFile2() {
		return file2;
	}

	public void setFile2(File file2) {
		this.file2 = file2;
	}

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<TUumsBaseOrg> orgList) {
		this.orgList = orgList;
	}

	public String getFile1FileName() {
		return file1FileName;
	}

	public void setFile1FileName(String file1FileName) {
		this.file1FileName = file1FileName;
	}

	public String getFile2FileName() {
		return file2FileName;
	}

	public void setFile2FileName(String file2FileName) {
		this.file2FileName = file2FileName;
	}

	public String[] getDp() {
		return dp;
	}

	public void setDp(String[] dp) {
		this.dp = dp;
	}


}
