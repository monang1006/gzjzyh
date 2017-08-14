package com.strongit.xxbs.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongit.uums.security.UserInfo;
import com.strongit.xxbs.common.DeployType;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.BulletinDto;
import com.strongit.xxbs.dto.InvitationDto;
import com.strongit.xxbs.dto.IssueDto;
import com.strongit.xxbs.dto.PublishDateDto;
import com.strongit.xxbs.dto.PublishDto;
import com.strongit.xxbs.dto.ScoreRankDto;
import com.strongit.xxbs.dto.UsedReportDto;
import com.strongit.xxbs.dto.roleDto;
import com.strongit.xxbs.entity.TInfoBaseAppoint;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.entity.TInfoBaseInfoReport;
import com.strongit.xxbs.entity.TInfoBaseIssue;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.service.IBulletinMarkedService;
import com.strongit.xxbs.service.IBulletinService;
import com.strongit.xxbs.service.IInfoReportService;
import com.strongit.xxbs.service.IInvitationService;
import com.strongit.xxbs.service.IIssueService;
import com.strongit.xxbs.service.IOrgService;
import com.strongit.xxbs.service.IPublishService;
import com.strongit.xxbs.service.ISubmitService;
import com.strongit.xxbs.service.JdbcBulletinService;
import com.strongit.xxbs.service.JdbcInfoReportService;
import com.strongit.xxbs.service.JdbcInvitationService;
import com.strongit.xxbs.service.JdbcIssueService;
import com.strongit.xxbs.service.JdbcPublishService;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings("rawtypes")
public class IndexAction extends BaseActionSupport
{
	private static final long serialVersionUID = 1L;
	
	private IBulletinService bulletinService;
	private ISubmitService submitService;
	private IOrgService orgService;
	private IIssueService issueService;
	private IInvitationService invitationService;
	private IBulletinMarkedService bulletinMarkedService;
	private IPublishService publishService;
	private IInfoReportService infoReportService;
	private JdbcPublishService jdbcPublishService;
	private JdbcBulletinService jdbcBulletinService;
	private JdbcInvitationService jdbcInvitationService;
	private JdbcIssueService jdbcIssueService;
	private JdbcInfoReportService jdbcInfoReportService;
	
	private List<BulletinDto> bulletins;
	private List<TInfoBaseIssue> issues;
	private List<PublishDto> submitted;
	private List<PublishDto> used;
	private List<PublishDto> shared;
	private List<String> useList;
	private List<PublishDateDto> pubDate;
	private List<TInfoBaseAppoint> invitations;
	private List<InvitationDto> invitation;
	private List<IssueDto> issue = new ArrayList<IssueDto>();
	
	private List<ScoreRankDto> scoreRanks;
	private DeployType deployType;
	private List<UsedReportDto> usedReports;
	private String orgType;

	private Boolean isSubmitter = false;
	private List list4;
	private List list5;
	
	
	
	@Autowired
	public void setJdbcInfoReportService(JdbcInfoReportService jdbcInfoReportService) {
		this.jdbcInfoReportService = jdbcInfoReportService;
	}

	@Autowired
	public void setJdbcIssueService(JdbcIssueService jdbcIssueService) {
		this.jdbcIssueService = jdbcIssueService;
	}

	@Autowired
	public void setJdbcInvitationService(JdbcInvitationService jdbcInvitationService) {
		this.jdbcInvitationService = jdbcInvitationService;
	}

	@Autowired
	public void setJdbcBulletinService(JdbcBulletinService jdbcBulletinService) {
		this.jdbcBulletinService = jdbcBulletinService;
	}

	@Autowired
	public void setBulletinService(IBulletinService bulletinService)
	{
		this.bulletinService = bulletinService;
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
	public void setIssueService(IIssueService issueService)
	{
		this.issueService = issueService;
	}

	@Autowired
	public void setInvitationService(IInvitationService invitationService)
	{
		this.invitationService = invitationService;
	}

	@Autowired
	public void setBulletinMarkedService(
			IBulletinMarkedService bulletinMarkedService)
	{
		this.bulletinMarkedService = bulletinMarkedService;
	}

	@Autowired
	public void setPublishService(IPublishService publishService)
	{
		this.publishService = publishService;
	}

	@Autowired
	public void setDeployType(DeployType deployType)
	{
		this.deployType = deployType;
	}

	@Autowired
	public void setInfoReportService(IInfoReportService infoReportService)
	{
		this.infoReportService = infoReportService;
	}
	
	@Autowired
	public void setJdbcPublishService(JdbcPublishService jdbcPublishService)
	{
		this.jdbcPublishService = jdbcPublishService;
	}

	public Object getModel()
	{
		return null;
	}

	@Override
	public String delete() throws Exception
	{
		return null;
	}

	@Override
	public String input() throws Exception
	{
		return null;
	}

	@Override
	public String list() throws Exception
	{		
		UserInfo userInfo = (UserInfo) getUserDetails();
        if(userInfo.getIsAdmin()||userInfo.getIsSubmit())
		  return "submitter";
        else
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
	
	public String bulletin() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String userId = userInfo.getUserId();
		
		bulletins = new ArrayList<BulletinDto>();
		List<TInfoBaseBulletin> lists = bulletinService.lastBulletins();
		for(TInfoBaseBulletin one : lists)
		{
			BulletinDto bd = new BulletinDto();
			bd.setBlDate(one.getBlDate());
			bd.setBlId(one.getBlId());
			bd.setBlTitle(one.getBlTitle());
			bd.setIsRead(bulletinMarkedService.isRead(one.getBlId(), userId));
			bulletins.add(bd);
		}
		return "bulletin";
	}
	
	public String JdcbBulletin() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String userId = userInfo.getUserId();
		
		bulletins = new ArrayList<BulletinDto>();
		List<BulletinDto> lists = jdbcBulletinService.lastBulletins();
		for(BulletinDto one : lists)
		{
			BulletinDto bd = new BulletinDto();
			bd.setBlDate(one.getBlDate());
			bd.setBlId(one.getBlId());
			bd.setBlTitle(one.getBlTitle());
			bd.setIsRead(jdbcBulletinService.isRead(one.getBlId(), userId));
			bulletins.add(bd);
		}
		return "bulletin";
	}
	

	public String submitted() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = Publish.ALL;	
		if(userInfo.getIsAdmin()||userInfo.getIsSubmit()){
			orgId = userInfo.getOrgId();
		}
		/*if(Publish.TERMINAL_CAI.equals(deployType.getType()))
		{
			orgId = Publish.ALL;
		}
		else
		{
			orgId = userInfo.getOrgId();	
		}*/

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//submitted = jdbcPublishService.lastSubmitted(orgId);
		//for(PublishDto p : submitted)
		//{
		//	p.setPubDate(p.getPubDate().substring(0, 10));
		//	p.setOrgName(p.getOrgName());
		//}

		List<TInfoBasePublish> lists = submitService.lastSubmitted(orgId);
		submitted = new ArrayList<PublishDto>();
		for(TInfoBasePublish one : lists)
		{
			PublishDto pub = new PublishDto();
			pub.setOrgName(one.getOrgName());
			pub.setPubDate(sdf.format(one.getPubDate()));
			pub.setPubId(one.getPubId());
			if(one.getPubTitle().length()>32){
			pub.setPubTitle(one.getPubTitle().substring(0, 32));
			}
			else{
				pub.setPubTitle(one.getPubTitle());
			}
			submitted.add(pub);
		}
		return "submitted";
	}
	
	public String used() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = Publish.ALL;	
		
		if(userInfo.getIsAdmin()||userInfo.getIsSubmit()){
			orgId = userInfo.getOrgId();
		}
				

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		/*used = jdbcPublishService.lastUsed(orgId);
		for(PublishDto p : used)
		{
			if(p.getPubSubmitDate()!=null){
			p.setPubSubmitDate(p.getPubSubmitDate().substring(0, 10));
			}
			Map<String, String> orgNames = orgService.getOrgNames();
			if(p.getPubMergeOrg()!=null){
				String orgName = "";
				String mid[] = p.getPubMergeOrg().split(",");
				for(int i=0;i<mid.length;i++){
					String name = orgNames.get(mid[i]);
					orgName = orgName + name+",";
				}
				orgName = orgName.substring(0, orgName.length()-1);
				p.setOrgName(orgName);
			}
			else{
			p.setOrgName(p.getOrgName());
			}
			TInfoBaseIssue issue = issueService.getIssue(p.getIssId());
			if(issue!=null){
			p.setIssNumber(issue.getIssNumber());
			p.setJourName(issue.getTInfoBaseJournal().getJourName());
			}
		}*/
		List<TInfoBasePublish> lists2 = submitService.lastUsed(orgId);
		used = new ArrayList<PublishDto>();
		for(TInfoBasePublish one2 : lists2)
		{
			PublishDto pub = new PublishDto();
			pub.setOrgName(one2.getOrgName());
			pub.setPubDate(sdf.format(one2.getPubDate()));
			pub.setPubId(one2.getPubId());
			if(one2.getPubTitle().length()>32){
				pub.setPubTitle(one2.getPubTitle().substring(0, 32));
				}
				else{
					pub.setPubTitle(one2.getPubTitle());
				}
			used.add(pub);
		}
		return "used";
	}
	
	public String shared() throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		/*shared = jdbcPublishService.lastShared();
		for(PublishDto p : shared)
		{
			p.setPubDate(p.getPubDate().substring(0, 10));
			Map<String, String> orgNames = orgService.getOrgNames();
			if(p.getPubMergeOrg()!=null){
				String orgName = "";
				String mid[] = p.getPubMergeOrg().split(",");
				for(int i=0;i<mid.length;i++){
					String name = orgNames.get(mid[i]);
					orgName = orgName + name+",";
				}
				orgName = orgName.substring(0, orgName.length()-1);
				p.setOrgName(orgName);
			}
			else{
			p.setOrgName(p.getOrgName());
			}
			p.setOrgName(p.getOrgName());
		}*/

		List<TInfoBasePublish> lists3 = submitService.lastShared();
		shared = new ArrayList<PublishDto>();
		for(TInfoBasePublish one3 : lists3)
		{
			PublishDto pub = new PublishDto();
			pub.setOrgName(one3.getOrgName());
			pub.setPubDate(sdf.format(one3.getPubDate()));
			pub.setPubId(one3.getPubId());
			if(one3.getPubTitle().length()>32){
				pub.setPubTitle(one3.getPubTitle().substring(0, 32));
				}
				else{
					pub.setPubTitle(one3.getPubTitle());
				}
			
			shared.add(pub);
		}
		return "shared";
	}
	
	public String rank() throws Exception
	{
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		String a[] = orgService.getOrgIdsByisOrg("4");
		list4 = submitService.getStatistics8(a, year1);
		return "rank";
	}
	
	public String rank1() throws Exception
	{
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String year1 =  formatter.format(date);
		if(orgType!=null){
		String a[] = orgService.getOrgIdsByisOrg(orgType);
		list4 = submitService.getStatistics9(a, year1);
		}
		else{
			String a[] = orgService.getOrgIdsByisOrg("0");
			list4 = submitService.getStatistics9(a, year1);
		}
		
		return "rank1";
	}
	
	public String invitation() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();
		invitation = new ArrayList<InvitationDto>();
		List<TInfoBaseAppoint> appoint = invitationService.lastAppoints(orgId);
		if(appoint!=null){
		for(int i=0;i<appoint.size();i++){
			InvitationDto app = new InvitationDto();
			app.setAptId(appoint.get(i).getAptId());
			app.setAptDate(appoint.get(i).getAptDate());
			app.setAptDuedate(appoint.get(i).getAptDuedate());
			app.setAptTitle(appoint.get(i).getAptTitle());
			invitation.add(app);
		}
		}
		return "invitation";
	}
	
	public String issue() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();
		//if(!userInfo.getIsAdmin()||!userInfo.getIsSubmit()){
		List<TInfoBaseIssue> issues = issueService.lastIssuesByYQ();
		if(issues!=null){
		for(int j=0;j<issues.size();j++){
			IssueDto iss = new IssueDto();
			iss.setIssId(issues.get(j).getIssId());
			iss.setIssNumber(issues.get(j).getIssNumber());
			iss.setIssTime(issues.get(j).getIssTime());
			iss.setJourName(issues.get(j).getTInfoBaseJournal().getJourName());
			SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月dd日");
			Date dd = issues.get(j).getIssTime();
			if(dd!=null){
			String ss = sim.format(dd);
			iss.setIssTime1(ss);
				}
			issue.add(iss);
			}
		}
		//}
		/*else{
			 issue = jdbcIssueService.lastIssuesBysb("");
			 List<IssueDto> ise = new ArrayList<IssueDto>();
				if(issue.size()>0){
				for(int i=0 ;i<issue.size();i++){
					List<IssueDto> pub= jdbcIssueService.findIssus(issue.get(i).getIssId(),orgId);
					if(pub.size()>0){
					for(int j=0;j<pub.size();j++){
					SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月dd日");
					Date dd = pub.get(j).getIssTime();
					String ss = sim.format(dd);
					String jourName = pub.get(j).getJourName();
					String issId = pub.get(j).getIssId();
					String issNumber = pub.get(j).getIssNumber();
					IssueDto iue = new IssueDto();
					iue.setIssId(issId);
					iue.setJourName(jourName);
					iue.setIssTime1(ss);
					iue.setIssNumber(issNumber);
					ise.add(iue);
					}
					}
				}
				}
				
				for(int i=0;i <ise.size();i++){  
					  for(int j=i+1;j <ise.size();j++){  
					  if((ise.get(i).getIssNumber().equals(ise.get(j).getIssNumber()))&&(ise.get(i).getJourName().equals(ise.get(j).getJourName()))){  
						  ise.remove(j);  
						  j--;
					  }  
					  }  
					}
				List<IssueDto> l = new ArrayList<IssueDto>();
				if(ise.size()>10){
				for(int i=0;i <10;i++){
					l.add(ise.get(i));
				}
				
				ActionContext.getContext().put("ise", l);
				}
				else{
					ActionContext.getContext().put("ise", ise);
				}
				isSubmitter = true;
				return "issueSubmit";
		 }*/
		return "issue";
	}
	
	
	public String issueByZW() throws Exception
	{
		UserInfo userInfo = (UserInfo) getUserDetails();
		String orgId = userInfo.getOrgId();
		//if(!userInfo.getIsAdmin()||!userInfo.getIsSubmit()){
		List<TInfoBaseIssue> issues = issueService.lastIssuesByZW();
		if(issues!=null){
		for(int j=0;j<issues.size();j++){
			IssueDto iss = new IssueDto();
			iss.setIssId(issues.get(j).getIssId());
			iss.setIssNumber(issues.get(j).getIssNumber());
			iss.setIssTime(issues.get(j).getIssTime());
			iss.setJourName(issues.get(j).getTInfoBaseJournal().getJourName());
			SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月dd日");
			Date dd = issues.get(j).getIssTime();
			if(dd!=null){
			String ss = sim.format(dd);
			iss.setIssTime1(ss);
				}
			issue.add(iss);
			}
		}
		return "issueByZW";
	}
	
	public String report() throws Exception
	{
		pubDate =new ArrayList<PublishDateDto>();
		List<TInfoBasePublish> list= publishService.findPublishDate();
		String str[] = new String[list.size()];
		for(int i = 0;i<list.size();i++){
			Date dt = (Date)list.get(i).getPubSubmitDate();
			if(dt!=null){
			String date = dt.toString().substring(0, 10);
			str[i] = date ;
			}
		}
		List l = new java.util.ArrayList();  
        for(int i=0; i< str.length; i++){  
            if(!l.contains(str[i]))  
                l.add(str[i]);  
        }         
        for(Iterator ll = l.iterator(); ll.hasNext();){    
            String date = (String) ll.next();
            PublishDateDto pDate = new PublishDateDto();
            if((date!=null)&&(!"".equals(date))){
            pDate.setPubSubmitDate(date);
            pubDate.add(pDate);
        }
        }   
		return "pubDate";
	}
	
	public String usedReport() throws Exception
	{
		List<TInfoBaseInfoReport> rets = infoReportService.lastReports();
		//List<UsedReportDto> rets = jdbcInfoReportService.lastReports();
		usedReports = new ArrayList<UsedReportDto>();
		for(int i=0;i<rets.size();i++)
		{
			UsedReportDto re = new UsedReportDto();
			re.setRpId(rets.get(i).getRpId());
			re.setRpTitle(rets.get(i).getRpTitle());
			usedReports.add(re);
		}
		return "usedReport";
	}
	
	/*
	 * 获取首页服务器时间
	 * 
	 */
	public String getDate() throws Exception
	{
		String flag= "";
		Date date = new Date();
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		flag = sim.format(date);
		int day = date.getDay();
		flag = flag +","+day;
		getResponse().setContentType("text/html");
		getResponse().getWriter().write(flag);
		return null;
	}
	

	
	/*
	 * 以下setter/getter
	 */
	
	public List<BulletinDto> getBulletins()
	{
		return bulletins;
	}

	public void setBulletins(List<BulletinDto> bulletins)
	{
		this.bulletins = bulletins;
	}

	public List<PublishDto> getSubmitted()
	{
		return submitted;
	}

	public void setSubmitted(List<PublishDto> submitted)
	{
		this.submitted = submitted;
	}

	public List<PublishDto> getUsed()
	{
		return used;
	}

	public void setUsed(List<PublishDto> used)
	{
		this.used = used;
	}

	public List<PublishDto> getShared()
	{
		return shared;
	}

	public void setShared(List<PublishDto> shared)
	{
		this.shared = shared;
	}

	public List<String> getUseList()
	{
		return useList;
	}

	public void setUseList(List<String> useList)
	{
		this.useList = useList;
	}

	public List<TInfoBaseIssue> getIssues()
	{
		return issues;
	}

	public void setIssues(List<TInfoBaseIssue> issues)
	{
		this.issues = issues;
	}

	public List<TInfoBaseAppoint> getInvitations()
	{
		return invitations;
	}

	public void setInvitations(List<TInfoBaseAppoint> invitations)
	{
		this.invitations = invitations;
	}

	public List<ScoreRankDto> getScoreRanks()
	{
		return scoreRanks;
	}

	public void setScoreRanks(List<ScoreRankDto> scoreRanks)
	{
		this.scoreRanks = scoreRanks;
	}

	public Boolean getIsSubmitter()
	{
		return isSubmitter;
	}

	public void setIsSubmitter(Boolean isSubmitter)
	{
		this.isSubmitter = isSubmitter;
	}

	public List<PublishDateDto> getPubDate() {
		return pubDate;
	}

	public void setPubDate(List<PublishDateDto> pubDate) {
		this.pubDate = pubDate;
	}

	public List<UsedReportDto> getUsedReports()
	{
		return usedReports;
	}

	public void setUsedReports(List<UsedReportDto> usedReports)
	{
		this.usedReports = usedReports;
	}

	public List<InvitationDto> getInvitation() {
		return invitation;
	}

	public void setInvitation(List<InvitationDto> invitation) {
		this.invitation = invitation;
	}

	public List<IssueDto> getIssue() {
		return issue;
	}

	public void setIssue(List<IssueDto> issue) {
		this.issue = issue;
	}

	public List getList4() {
		return list4;
	}

	public void setList4(List list4) {
		this.list4 = list4;
	}

	public List getList5() {
		return list5;
	}

	public void setList5(List list5) {
		this.list5 = list5;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	
}
