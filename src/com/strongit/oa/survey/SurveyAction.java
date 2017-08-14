package com.strongit.oa.survey;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOaVote;
import com.strongit.oa.bo.ToaSurveytableSurvey;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.vote.VoteManager;
import com.strongit.oa.vote.util.VoteConst;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "survey.action", type = ServletActionRedirectResult.class) })
public class SurveyAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	@Autowired private VoteManager votemanager ;
	private Page<ToaSurveytableSurvey> page = new Page(FlexTableTag.MAX_ROWS, true);
	private ToaSurveytableSurvey model = new ToaSurveytableSurvey();
	private SurveyManager manager;
	private SurveyStyleManager sytleManager;
	private String surveyId;
	private String surveyName;
	/*
	 * 状态标示
	 * <p>{0:未激活状态;1:激活;2:过期}</p>
	 */
	private String state;
	/**
	 * 重复提交标示
	 * <p>{0:可重复;1:不可重复}</p>
	 */
	private String surveyUnRepeat;
	private Date surveyStartTime;//开始时间
	private Date surveyEndTime; //结束时间
	private String startTime; //格式化后的开始时间
	private String endTime; //格式化后的结束时间
	private String surveyQusSort; //记录问题排序  
	private List styleList; //样式列表
	private String surveyStyleId;
	private String search;

	private String blockid; //个人桌面传入
	private DesktopSectionManager dsmanager;

	
	@Autowired
	public void setManager(SurveyManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setSytleManager(SurveyStyleManager sytleManager) {
		this.sytleManager = sytleManager;
	}

	public Page getPage() {
		return page;
	}

	@Autowired
	public void setDsmanager(DesktopSectionManager dsmanager) {
		this.dsmanager = dsmanager;
	}

	/**
	 * 删除操作
	 */
	@Override
	public String delete() throws Exception {
		this.manager.delSurvey(this.surveyId);
		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/survey/survey.action'; </script>");

	}

	@Override
	public String input() throws Exception {
		this.styleList = this.sytleManager.getStylePages();
		return "add";
	}

	public String initEdit() throws Exception {
		prepareModel();
		this.styleList = this.sytleManager.getStylePages();
		this.startTime = this.model.getSurveyStartTime().toString().substring(
				0, 10);
		this.endTime = this.model.getSurveyEndTime().toString()
				.substring(0, 10);
		return "edit";
	}

	/**
	 * 修改
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		addActionMessage("修改成功");
		this.manager.editSurvey(this.model);
		return "init";
	}

	/**
	 * 设置状态位
	 * @return
	 * @throws Exception
	 */
	public String setState() throws Exception {
		prepareModel();
		this.model.setState(this.state);
		this.manager.editSurvey(this.model);

		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/survey/survey.action'; </script>");

	}

	/**
	 * 设置重复提交
	 * @return
	 * @throws Exception
	 */
	public String setUnRepeat() throws Exception {
		prepareModel();
		this.model.setSurveyUnRepeat(this.surveyUnRepeat);
		this.manager.editSurvey(this.model);

		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/survey/survey.action'; </script>");

	}
	
	/**
	 * 读取调查时间
	 * @return
	 * @throws Exception
	 */
	public String redTime() throws Exception {
		prepareModel();
		this.startTime = this.model.getSurveyStartTime().toString().substring(
				0, 10);
		this.endTime = this.model.getSurveyEndTime().toString()
				.substring(0, 10);
		return "time";
	}

	/**
	 * 修改时间
	 * @return
	 * @throws Exception
	 */
	public String setTime() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(model.getSurveyStartTime());
		
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(model.getSurveyEndTime());
		calEnd.set(Calendar.HOUR_OF_DAY, 23);
		calEnd.set(Calendar.MINUTE, 59);
		calEnd.set(Calendar.SECOND, 59);
		
		this.model = this.manager.getSurvey(model.getSurveyId());
		this.model.setSurveyStartTime(calStart.getTime());
		this.model.setSurveyEndTime(calEnd.getTime());
		this.model.setState("0");
		this.manager.editSurvey(this.model);
		addActionMessage("重置成功");

		return "init";
	}

	/**
	 * 问卷调查集合
	 */
	@Override
	public String list() throws Exception {

		if ("true".equals(this.search)) {
			this.page = this.manager.getSelectSurveyPages(page, surveyName,
					surveyStartTime, surveyEndTime);
		} else
			this.page = this.manager.getSurveyPages(this.page);
		return SUCCESS;
	}

	/**
	 * 通过审核的问卷集合
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listSucc() throws Exception {
		this.page = this.manager.getSurveySuccPages(this.page);
		return "succ";
	}

	@Override
	protected void prepareModel() throws Exception {
		if (null != this.surveyId) {
			this.model = this.manager.getSurvey(this.surveyId);
		} else {
			this.model = new ToaSurveytableSurvey();
		}

	}

	@Override
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		addActionMessage("保存成功");
		this.manager.addSurvey(this.model);
		return "init";
	}

	/**
	 * 记录问题的排序
	 * @throws Exception
	 */
	public void updateQusSort() throws Exception {
		this.surveyId = (String) getRequest().getSession().getAttribute(
				"surveyId");
		String temp = this.surveyQusSort;
		temp = temp.replaceAll("drag_", "");
		prepareModel();
		this.model.setSurveyQusSort(temp);
		this.manager.editSurvey(this.model);
	}

	/**
	 * 返回激活中的调查问卷
	 */
	public List<TOaVote> getVote(){
		Page<TOaVote> vote_page = new Page(FlexTableTag.MAX_ROWS, true);
		String hql=new String("from TOaVote vote where vote.state=? order by vote.endDate desc");
		Object []params=new Object[]{VoteConst.vote_yjh};//激活状态
		vote_page=votemanager.getVotePage(vote_page,hql,params);
		return vote_page.getResult() ;
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

		Map<String, String> map = dsmanager.getParam(blockid); // 通过blockid获取映射对象
		String showNum = map.get("showNum"); // 显示条数
		String subLength = map.get("subLength"); // 主题长度
		String showCreator = map.get("showCreator"); // 是否显示作者
		String showDate = map.get("showDate"); // 是否显示日期
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum)
				&& !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		
		//page.setPageSize(num);
		//page = this.manager.getSurveySuccPages(this.page);
		//list = page.getResult();
		list=getVote();//获得激活的调查问卷
		if(null!=list){
			for (int i = 0; i < num && i < list.size(); i++) { // 获取在条数范围内的列表
				TOaVote vote = list.get(i);
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<div class=\"linkdiv\" title=\"\">");
				innerHtml
				.append("	<IMG SRC=\"")
				.append(getRequest().getContextPath())
				.append(
						"/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				String title = vote.getTitle();
				title = title == null ? "无标题" : title;
				if (title.length() > length) // 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0, length) + "...";
				innerHtml
				.append("	<span title=\"")
				.append(vote.getTitle())
				.append("\">")
				.append(" <a href='#' onclick=\"javascript:window.showModalDialog('")
				.append(getRequest().getContextPath())
				.append(
						"/vote/vote!viewVote.action?admin=N&vote.vid=")
						.append(vote.getVid()).append(
						"',window,'help:no;status:no;scroll:auto;dialogWidth:950px; dialogHeight:680px')\">").append(title).append(
						"</a></span>");
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
				innerHtml.append("	</div>");
			}
		}
		return renderHtml(innerHtml.toString()); //用renderHtml将设置好的html代码返回桌面显示
	}

	public Object getModel() {
		return model;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getSurveyStartTime() {
		return surveyStartTime;
	}

	public void setSurveyStartTime(Date surveyStartTime) {
		this.surveyStartTime = surveyStartTime;
	}

	public Date getSurveyEndTime() {
		return surveyEndTime;
	}

	public void setSurveyEndTime(Date surveyEndTime) {
		this.surveyEndTime = surveyEndTime;
	}

	public String getSurveyQusSort() {
		return surveyQusSort;
	}

	public void setSurveyQusSort(String surveyQusSort) {
		this.surveyQusSort = surveyQusSort;
	}

	public List getStyleList() {
		return styleList;
	}

	public void setStyleList(List styleList) {
		this.styleList = styleList;
	}

	public String getSurveyStyleId() {
		return surveyStyleId;
	}

	public void setSurveyStyleId(String surveyStyleId) {
		this.surveyStyleId = surveyStyleId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public String getBlockid() {
		return blockid;
	}

	public void setBlockid(String blockid) {
		this.blockid = blockid;
	}

	public String getSurveyUnRepeat() {
		return surveyUnRepeat;
	}

	public void setSurveyUnRepeat(String surveyUnRepeat) {
		this.surveyUnRepeat = surveyUnRepeat;
	}
	
}
