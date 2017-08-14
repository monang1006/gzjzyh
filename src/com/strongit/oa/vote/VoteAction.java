package com.strongit.oa.vote;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOaVote;
import com.strongit.oa.bo.TOaVoteAnswer;
import com.strongit.oa.bo.TOaVoteLog;
import com.strongit.oa.bo.TOaVoteQuestion;
import com.strongit.oa.bo.TOaVoteSMS;
import com.strongit.oa.bo.TOaVoteTarget;
import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaSms;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.message.MessageManager;
import com.strongit.oa.sms.IsmsService;
import com.strongit.oa.smsplatform.SmsPlatformManager;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.vote.util.VoteConst;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author piyu
 * Jun 10, 2010
 * 
 */
@ParentPackage("default")
@Results( {
	@Result(name = BaseActionSupport.RELOAD, value = "vote.action", type = ServletActionRedirectResult.class),
	@Result(name = "error", value = "www.baidu.com",type=ServletRedirectResult.class)
})
public class VoteAction extends BaseActionSupport {

	public static final String moduleCode=GlobalBaseData.SMSCODE_SURVEY;//在线调查的编号
	
	private TOaVote vote=new TOaVote();
	@Autowired private IUserService user;
	private List styleList; //样式列表
	private Page<TOaVote> page = new Page(FlexTableTag.MAX_ROWS, true);
	private VoteManager votemanager ;
	private QuestionManager qtmanager;
	private VoteStyleManager sytleManager;
	private TargetManager targetmanager ;
	private VoteLogManager votelogmanager;
	private AnswerManager asmanager;
	@Autowired private MessageManager msgmanager;//短信服务
	private String vids ;//问卷ID，使用逗号分隔
	private String startDate;//问卷开始时间yyyy-mm-dd
	private String endDate ;//问卷结束时间yyyy-mm-dd
	private List<TOaVoteQuestion> list_qt ;
	private String msg ;
	private String vote_header; //问卷模板的头
	private String vote_foot;  //问卷模板的尾
	private String admin="N";  //
	private boolean canSubmitVote=false;//是否能够参与投票
	private boolean limitViewResult=true;//是否能够查看投票结果
	private String sms_submit ;//用户短信提交的答案,五位数字组成
	@Autowired private SMSCodeManager smscodemg;//短信网页问题的编号管理
	
	@Autowired private SmsPlatformManager smsPlatformManager;
	@Autowired private IsmsService smsService ;
	
	private String blockid; //个人桌面传入
	@Autowired private DesktopSectionManager dsmanager;
	
	private String smsVote_memo;//短信和页面问卷的短信参与提示
	
	/**
	 * 测试使用
	 */
	public String smscode(){
		TOaVoteSMS smscode=new TOaVoteSMS();
		smscode.setCode("asd");
		smscode.setQid("11");
		smscodemg.addSmsCode(smscode);
		return null;
	}
	/**
	 * 查看问卷的结果
	 */
	public String viewResult(){
		list_qt=qtmanager.loadQuestion(vote.getVid(),"desc");//	获取问卷下的问题
	
		return "viewresult";
	}
	
	/**
	 * 页面提交调查问卷
	 */
	public String submitVote(){
		try{
			vote=votemanager.getVote(vote.getVid());
			TOaVoteLog log=new TOaVoteLog();
			if(user.getCurrentUser()!=null){
				log.setUserid(user.getCurrentUser().getUserId());
				log.setUsername(user.getCurrentUser().getUserName());
			}
			log.setIP(getIP());
			log.setVote_date(new Date());
			log.setVote(vote);
			asmanager.submitAnswer(getRequest(),log,"Y".equals(vote.getIsRealname()));		
			return this.renderHtml("<script language='javascript'>parent.success();</script>") ;
		}catch(Exception e){
			logger.error("参与页面问卷：", e);
			return this.renderHtml("<script language='javascript'>parent.failure();</script>") ;
		}
	}
	
	/**
	 * 设置canSubmitVote
	 */
	private void setCanSubmitVote(){
		String userid=user.getCurrentUser().getUserId();

		if(targetmanager.isTarget(vote.getVid(), userid, null)){
			if("N".equals(vote.getIsRepeated())){
				//不限制重复参与
				canSubmitVote=true;
				return ;
			}
			if(votelogmanager.existLog(vote.getVid(), userid, null)){
				canSubmitVote=false;
			}else{
				canSubmitVote=true;
			}
		}else{
			canSubmitVote=false;
		}
	}
	/**
	 * 设置limitViewResult
	 */	
	private void setCanViewResult(){
		if("N".equals(vote.getIsPrivate())){
			//不限制查看结果
			limitViewResult=false;
		}else{
			limitViewResult=true;
		}
	}
	
	/**
	 * 查看问卷
	 */
	public String viewVote(){
		vote=votemanager.getVote(vote.getVid());
			
		String template = null;
		try {
			if(vote.getStyleId()==null){
				msg="问卷模板没有设置！";
				return this.renderHtml(String.format("<script language='javascript'>window.alert('%s');window.close();</script>",msg));
			}
			template = this.sytleManager.getStyle(vote.getStyleId()).getStyleContent();
		} catch (Exception e) {
			//super.logger.error(e);
			msg="加载模板出错！";
			logger.error(String.format("%s[vid=%d]",msg,vote.getVid()),e);
			return this.renderHtml(String.format("<script language='javascript'>window.alert('%s');window.close();</script>",msg));
		}

		if (template!=null&&template.length()>0) {
			template = template.replaceAll("\\$\\{开始时间\\}", vote.getStartDate()
					.toString().substring(0, 10));
			template = template.replaceAll("\\$\\{结束时间\\}", vote.getEndDate()
					.toString().substring(0, 10));
			template = template.replaceAll("\\$\\{调查表名称\\}", vote.getTitle());

			String value[] = template.split("\\$\\{调查表内容\\}");
			if (value.length > 1) {
				this.vote_header = value[0];
				this.vote_foot = value[1];
			}
		}
		
		list_qt=qtmanager.loadQuestion(vote.getVid(),"asc");//	获取问卷下的问题
		if(VoteConst.vote_type_sms.equals(vote.getType())){
			//短信和页面参与问卷，设置页面的短信参与提示信息
			if(list_qt.size()>0){
				proccess_smsVote_memo(list_qt.get(0));
			}
		}
		setCanViewResult();
		setCanSubmitVote();
		if("N".equals(admin))
		 {
			return "view";
		 }else{
			return "viewadmin" ;
		 }
	}
	/**
	 * 设置smsVote_memo
	 */
	private void proccess_smsVote_memo(TOaVoteQuestion question){
		String sms_gate=smsService.getSmsModelNum();//获取短信网关接口
		List<TOaVoteAnswer> list_answer=qtmanager.loadAnswer(question.getQid());
		StringBuffer sms_answer=new StringBuffer("答案编号：");
		String question_code=null;
		String demo_sms=null;
		TOaVoteSMS smscode = smscodemg.getByQid(question.getQid());
		if (smscode != null) {
			// 读取问题编号
			question_code = smscode.getCode();
		} else {
			logger.error(String.format("%s对应的问题编号不存在",question.getQid()));
			this.smsVote_memo=null;
			return ;
		}
		if(list_answer.size()>0){
			demo_sms=String.format("%s%s%s",moduleCode,question_code,list_answer.get(0).getShowno());
		}
		for(TOaVoteAnswer answer:list_answer){
			//保存消息对应的短信回复答案
			//使用答案的显示顺序号
			sms_answer.append(moduleCode).append(question_code).append(answer.getShowno()).append(answer.getContent()).append("，");
		}
		sms_answer=sms_answer.delete(sms_answer.length()-1,sms_answer.length());//设置短信问卷的参与提示
		sms_answer.append("<br>");
		sms_answer.append(String.format("短信参与请发送数字XXXXX至%s。例如发送%s至%s。",sms_gate,demo_sms,sms_gate));
		smsVote_memo=sms_answer.toString();
	}
	/**
	 * 预览问卷
	 */
	public String previewVote(){
		vote=votemanager.getVote(vote.getVid());
		list_qt=qtmanager.loadQuestion(vote.getVid(),"desc");//获取问卷下的问题
		
		return "preview";
	}
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}
	
	public String init_add() throws Exception {
		this.styleList = this.sytleManager.getStylePages();
		return "add";
	}
	public String init_edit() throws Exception {
		this.styleList = this.sytleManager.getStylePages();
		vote=votemanager.getVote(vote.getVid());
		
		DateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		startDate=datef.format(vote.getStartDate());
		endDate=datef.format(vote.getEndDate());
		return "edit";
	}
	/**
	 * 查询所有激活状态的问卷
	 */
	public String list_enabled(){
		Calendar calendar = Calendar.getInstance();
		StringBuffer hql=new StringBuffer("from TOaVote vote where vote.state=? ");
		List<Object> params=new ArrayList<Object>();
		params.add(VoteConst.vote_yjh);//激活状态
		if(vote.getTitle()!=null&&vote.getTitle().length()>0){
			params.add(String.format("%%%s%%",vote.getTitle()));
			hql.append(" and vote.title like ? ");		
		}
		if(vote.getStartDate()!=null){
			params.add(vote.getStartDate());
			hql.append(" and vote.startDate >=? ");
		}
		if(vote.getEndDate()!=null){
			calendar.setTime(vote.getEndDate());
			//calendar.add(Calendar.DAY_OF_MONTH, 1);
			params.add(calendar.getTime());
			hql.append(" and vote.endDate <=? ");
		}
		hql.append(" order by vote.endDate desc ");
		page=votemanager.getVotePage(page,hql.toString(),params.toArray(new Object[params.size()]));
		
		return "listenabled";
	}
	
	@Override
	/**
	 * 查询调查问卷
	 */	
	public String list() throws Exception {
		Calendar calendar = Calendar.getInstance();
		StringBuffer hql=new StringBuffer("from TOaVote vote where 1=1 ");
		List<Object> params=new ArrayList<Object>();
		if(vote.getTitle()!=null&&vote.getTitle().length()>0){
			params.add(String.format("%%%s%%",vote.getTitle()));
			hql.append(" and vote.title like ? ");		
		}
		if(vote.getStartDate()!=null){
			params.add(vote.getStartDate());
			hql.append(" and vote.startDate >=? ");
		}
		if(vote.getEndDate()!=null){
			calendar.setTime(vote.getEndDate());
			//calendar.add(Calendar.DAY_OF_MONTH, 1);
			params.add(calendar.getTime());
			hql.append(" and vote.endDate <=? ");
		}
		hql.append(" order by vote.endDate desc ");
		page=votemanager.getVotePage(page,hql.toString(),params.toArray(new Object[params.size()]));
		return "list";
	}
	/**
	 * 保存问卷对象 
	 */
	public String saveVote()throws Exception {
		if(vote.getVid()==null||vote.getVid().length()<1){
			votemanager.addVote(vote);
		}else{
			vote.setState(VoteConst.vote_wjh);
			votemanager.updateVote(vote);
		}
		// 跳转到list()
		return  "init";
	}
	/**
	 * 支持批量删除问卷
	 */
	public String delVotes()throws Exception {
		if(vids!=null&&vids.length()>0){
			votemanager.delVote(vids);			
		}
		return list();
	}
	
	/**
	 * 冻结问卷
	 */
	public String disableVote()throws Exception{
		if(vote.getVid()!=null){
			votemanager.disableVote(vote.getVid());
		}
		return list();
	}
	/**
	 * 激活问卷
	 */
	public String enableVote()throws Exception{
		if(vote.getVid()!=null){
			vote=votemanager.getVote(vote.getVid());
			if(vote.getStyleId()==null){
				//模板不存在
				msg="问卷模板没有设置！";
		    	vote=new TOaVote();
		    	return list();
			}
			if(VoteConst.vote_type_sms.equals(vote.getType())){
				//手机和网页投票
				String result=sendSMS();
			    if(result!=null){
			    	//返回结果不为空，出错
			    	msg="激活失败！";
			    	vote=new TOaVote();
			    	return list();
			    }
			}
			//votemanager.enableVote(vote.getVid());
			//同一个session的处理
			vote.setState(VoteConst.vote_yjh);
			votemanager.updateVote(vote);
			vote=new TOaVote();
		}
		return list();
	}
	/**
	 * 使用短信将问题发送到用户手机,返回null标识成功
	 */
	public String sendSMS(){
		try {
			List<TOaVoteTarget> list_target = targetmanager.getAllTargetByVid(vote.getVid());
			if (list_target.size() < 1) {
				// 没有调查对象
				return null;
			}
			List<TOaVoteQuestion> list_qt = qtmanager.loadQuestion(vote.getVid(), "desc");
			if (list_qt.size() < 1) {
				// 没有调查问题
				return null;
			}

			TOaVoteQuestion qt_sms = list_qt.get(0);
			String sms_question = String.format("[单选题]%s。", qt_sms.getTitle());

			List<TOaVoteAnswer> list_answer = qtmanager.loadAnswer(qt_sms.getQid());
			
			// 获取自增长
			String question_code = null;
			TOaVoteSMS smscode = smscodemg.getByQid(qt_sms.getQid());
			if (smscode != null) {
				// 如果已经存在编号
				question_code = smscode.getCode();
			} else {
				question_code = votemanager.getCode(moduleCode);//同步获取问题唯一编号
				if ("toolong".equals(question_code)) {
					logger.error("自增码太长");
					return "toolong";
				}
				smscode = new TOaVoteSMS();
				smscode.setCode(question_code);
				smscode.setQid(qt_sms.getQid());
				smscodemg.addSmsCode(smscode);
				//自增长编码+1,必须同步
				//smsPlatformManager.updateMsgCode(moduleCode);
				votemanager.updateCode(moduleCode);//同步自增
			}

			StringBuffer userid = new StringBuffer();
			StringBuffer mobile = new StringBuffer();
			for (TOaVoteTarget target : list_target) {
				if (target.getMobile() != null) {
					mobile.append(target.getMobile());
					mobile.append(",");
				} else if (target.getUserid() != null) {
					userid.append(target.getUserid());
					userid.append(",");
				} else {
					targetmanager.delTargets(target.getTid());
				}
			}
			if(userid.length()>0){
				userid=userid.delete(userid.length()-1, userid.length());
			}
			if(mobile.length()>0){
				mobile=mobile.delete(mobile.length()-1, mobile.length());
			}
			this.send(question_code, userid.toString(), mobile.toString(), sms_question, list_answer,new Date());
		} catch (Exception e) {
			logger.error("短信发送问卷问题：", e);
			return "error";
		}
		return null;//成功返回null
	}
	/**
	 * 调用短信发送接口
	 */
	private void send(String question_code,String userid,String mobile,String sms_question,List<TOaVoteAnswer>list_answer,Date sendDate)throws Exception{
		
		StringBuffer sms_content=new StringBuffer(sms_question);
		StringBuffer sms_answer = new StringBuffer("\n回复:");
		for(int i=0;i<list_answer.size();i++){
			//保存消息对应的短信回复答案
			TOaVoteAnswer answer = list_answer.get(i);
			if(answer!=null){
				//使用答案的显示顺序号
				sms_answer.append(moduleCode).append(question_code).append(answer.getShowno()).append(answer.getContent()).append("，");
			}
		}
		
		if(sms_answer.length()>1){
			//清楚尾部的逗号
			sms_content.append(sms_answer.substring(0, sms_answer.length()-1));
		}
		
		ToaSms sms = new ToaSms();
		sms.setSmsCon(sms_content.toString());
		sms.setSmsIsdel(ToaSms.SMS_NOTDEL);
		sms.setSmsSendTime(sendDate);
		sms.setSmsSendUser(user.getCurrentUser().getUserId());
		sms.setModelCode(moduleCode);
		sms.setIncreaseCode(question_code);
		
		ToaBussinessModulePara modelCof= smsPlatformManager.getObjByCode(moduleCode);
		if(null!=modelCof){
			sms.setModelName(modelCof.getBussinessModuleName());
		}
		smsService.sendSmsByMessage(mobile, userid, sms);
	}
	/**
	 * 获取客户的IP
	 */
	public String getIP(){
		String ip = getRequest().getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = getRequest().getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = getRequest().getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = getRequest().getRemoteAddr();
        }
        return ip;
	}
	
	/**
	 * 与个人桌面的接口 在桌面上显示调查
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		List<TOaVote> list = null;
	
//		 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");//是否显示字体大小
		if (blockId != null) {
			Map<String, String> map = dsmanager.getParam(blockid);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum)
				&& !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "12";
		}
		//page.setPageSize(num);
		//page = this.manager.getSurveySuccPages(this.page);
		//list = page.getResult();
		list_enabled();
		list=page.getResult();//获得激活的调查问卷
		if(null!=list){
			for (int i = 0; i < num && i < list.size(); i++) { // 获取在条数范围内的列表
				TOaVote vote = list.get(i);
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml
				.append("	<IMG SRC=\"")
				.append(getRequest().getContextPath())
				.append(
						"/oa/image/desktop/littlegif/news_bullet.gif\">");
				String title = vote.getTitle();
				title = title == null ? "无标题" : title;
				if (title.length() > length) // 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0, length) + "...";
				innerHtml
				.append("	<span title=\"")
				.append(vote.getTitle())
				.append("\">")
				.append(" <a style=\"font-size:"+sectionFontSize+"px\" href='#' onclick=\"javascript:window.showModalDialog('")
				.append(getRequest().getContextPath())
				.append(
						"/vote/vote!viewVote.action?admin=N&vote.vid=")
						.append(vote.getVid()).append(
						"',window,'help:no;status:no;scroll:auto;dialogWidth:950px; dialogHeight:680px')\">").append(title).append(
						"</a></span>");
				innerHtml.append("</td>");
				innerHtml.append("<td width=\"110px\">");
				if ("1".equals(showCreator)) // 如果设置为显示作者，则显示作者信息
				{// innerHtml.append(" <span class
					// =\"linkgray\">").append(mail.getMailSender()).append("</span>");
					//uumsInterface.getUserInfoByUserId("").getUserName();
				}
				
				if ("1".equals(showDate)) { // 如果设置为显示日期，则显示日期信息
					String dateStr = "";
					if (vote.getStartDate() == null) {
						
					} else {
						dateStr = st.format(vote.getStartDate());
					}
					innerHtml.append("	<span class =\"linkgray10\">")
					.append(dateStr).append("</span>");
				}
				innerHtml.append("</td>");
				innerHtml.append("</tr>");
				innerHtml.append("	</table>");
			}
			//固定行数
		}
		if(list==null){
			for (int i = 0; i < num; i++) { // 获取在条数范围内的列表
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml
				.append("	&nbsp;");
				innerHtml.append("</td>");
				innerHtml.append("</tr>");
				innerHtml.append("	</table>");
				}
		}
		if(list!=null&&list.size()<num){
			for (int i = 0; i < num-list.size() ; i++) { // 获取在条数范围内的列表
			innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
			innerHtml.append("<tr>");
			innerHtml.append("<td>");
			innerHtml
			.append("	&nbsp;");
			innerHtml.append("</td>");
			innerHtml.append("</tr>");
			innerHtml.append("	</table>");
			}
		}
		return renderHtml(innerHtml.toString()); //用renderHtml将设置好的html代码返回桌面显示
	}
	
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public TOaVote getVote() {
		return vote;
	}

	public void setVote(TOaVote vote) {
		this.vote = vote;
	}

	public VoteManager getVotemanager() {
		return votemanager;
	}
	@Autowired
	public void setVotemanager(VoteManager votemanager) {
		this.votemanager = votemanager;
	}
	@Autowired
	public void setSytleManager(VoteStyleManager sytleManager) {
		this.sytleManager = sytleManager;
	}

	public List getStyleList() {
		return styleList;
	}

	public void setStyleList(List styleList) {
		this.styleList = styleList;
	}

	public VoteStyleManager getSytleManager() {
		return sytleManager;
	}

	public Page<TOaVote> getPage() {
		return page;
	}

	public void setPage(Page<TOaVote> page) {
		this.page = page;
	}

	public String getVids() {
		return vids;
	}

	public void setVids(String vids) {
		this.vids = vids;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public QuestionManager getQtmanager() {
		return qtmanager;
	}
	@Autowired
	public void setQtmanager(QuestionManager qtmanager) {
		this.qtmanager = qtmanager;
	}
	
	public TargetManager getTargetmanager() {
		return targetmanager;
	}
	@Autowired
	public void setTargetmanager(TargetManager targetmanager) {
		this.targetmanager = targetmanager;
	}
	public VoteLogManager getVotelogmanager() {
		return votelogmanager;
	}
	@Autowired
	public void setVotelogmanager(VoteLogManager votelogmanager) {
		this.votelogmanager = votelogmanager;
	}
	
	public AnswerManager getAsmanager() {
		return asmanager;
	}
	@Autowired
	public void setAsmanager(AnswerManager asmanager) {
		this.asmanager = asmanager;
	}
	public List<TOaVoteQuestion> getList_qt() {
		return list_qt;
	}
	public void setList_qt(List<TOaVoteQuestion> list_qt) {
		this.list_qt = list_qt;
	}
	public String getVote_header() {
		return vote_header;
	}
	public void setVote_header(String vote_header) {
		this.vote_header = vote_header;
	}
	public String getVote_foot() {
		return vote_foot;
	}
	public void setVote_foot(String vote_foot) {
		this.vote_foot = vote_foot;
	}
	public boolean isCanSubmitVote() {
		return canSubmitVote;
	}
	public void setCanSubmitVote(boolean canSubmitVote) {
		this.canSubmitVote = canSubmitVote;
	}
	
	public boolean isLimitViewResult() {
		return limitViewResult;
	}
	public void setLimitViewResult(boolean limitViewResult) {
		this.limitViewResult = limitViewResult;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getSms_submit() {
		return sms_submit;
	}
	public void setSms_submit(String sms_submit) {
		this.sms_submit = sms_submit;
	}
	public String getBlockid() {
		return blockid;
	}
	public void setBlockid(String blockid) {
		this.blockid = blockid;
	}
	public String getSmsVote_memo() {
		return smsVote_memo;
	}
	public void setSmsVote_memo(String smsVote_memo) {
		this.smsVote_memo = smsVote_memo;
	}
	
}
