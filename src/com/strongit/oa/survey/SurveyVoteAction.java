package com.strongit.oa.survey;

import java.util.Iterator;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSurveytableAnswer;
import com.strongit.oa.bo.ToaSurveytableQuestion;
import com.strongit.oa.bo.ToaSurveytableSurvey;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "surveyVote.action", type = ServletActionRedirectResult.class) })
public class SurveyVoteAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ToaSurveytableQuestion model = new ToaSurveytableQuestion();
	
	private ToaSurveytableAnswer ansmodel = new ToaSurveytableAnswer();
	
	private Page<ToaSurveytableQuestion> page = new Page<ToaSurveytableQuestion>();
	private Page<ToaSurveytableAnswer> anspage = new Page();
	private SurveyVoteManager manager;
	private SurveyStyleManager styleManager;
	private SurveyManager surveyManager;
	private SurveyAnswerManager answManager;

	private String questionId;

	private String questionName;

	private String questionTrue;

	private String questionType;//问题类型

	private String surveyId;

	private int questionNumber;

	private String viewType;//查看方式

	private String topHtml = "";//样式

	private String bottomHtml = "";//样式

	private String isPublic;
	
	@Override
	public String delete() throws Exception {
		this.manager.delQuestion(this.manager
				.getQuestionId(this.questionNumber));
		return null;
	}

	/**
	 * 加载问题对像
	 */
	@Override
	public String input() throws Exception {
		getRequest().getSession().removeAttribute("surveyId");
		getRequest().getSession().setAttribute("surveyId", this.surveyId);
		
		ToaSurveytableSurvey surey=surveyManager.getSurvey(this.surveyId);
	if(surey.getTableTitle()!=null && !surey.getTableTitle().equals("")){
			String[] sortid = null;
		String s = this.surveyManager.getQusSort(this.surveyId);
			if (!("".equals(s) || null == s)) {
				sortid = s.split(",");
			sortid = this.manager.reverse(sortid);
			}
			this.page = this.manager.getQuestionPages(this.page, sortid,
					this.surveyId);
			
			List<ToaSurveytableQuestion> question=page.getResult();
			if(question!=null && question.size()>0){
			String headerstitle=surey.getTableTitle();
			String options="";
			ToaSurveytableQuestion q=question.get(0);
			
			String topTitles=q.getQuestionName();
			if(q.getQuestionType().equals("tableRadio")){
				options="radio";
			}
			else if(q.getQuestionType().equals("tableCheckbox")){
				options="checkbox";
			}
			anspage=answManager.getAnswerPages(anspage,q.getQuestionNumber());
			List<ToaSurveytableAnswer> list = anspage.getResult();
			StringBuffer quename=new StringBuffer("");
			for(Iterator<ToaSurveytableAnswer> it=list.iterator();it.hasNext();){
				ToaSurveytableAnswer que = it.next();
				quename.append(que.getAnswerName());
				quename.append(";");
			
			}		
			String queName=quename.toString();
			this.getRequest().setAttribute("titles", headerstitle);
			this.getRequest().setAttribute("queName", queName);
			this.getRequest().setAttribute("options", options);
			this.getRequest().setAttribute("topTitles", topTitles);
		}
			return "addTable";
		}
		else{
		String[] sortid = null;
		String s = this.surveyManager.getQusSort(this.surveyId);
		if (!("".equals(s) || null == s)) {
			sortid = s.split(",");
			sortid = this.manager.reverse(sortid);
		}
		this.page = this.manager.getQuestionPages(this.page, sortid,
				this.surveyId);
		return "add";
	 }
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
	}

	@Override
	public String save() throws Exception {
		String surveyId = (String) getRequest().getSession().getAttribute(
				"surveyId");
		int number = this.manager.getMaxNumber();
		model = new ToaSurveytableQuestion();
		model.setQuestionName(this.questionName);
		model.setQuestionNumber(number);
		model.setQuestionTrue(this.questionTrue);
		model.setQuestionType(this.questionType);
		model.setQuestionSurveyId(surveyId);
		ToaSurveytableSurvey oo = new ToaSurveytableSurvey();
		oo.setSurveyId(surveyId);
		model.setToaSurveytableSurvey(oo);
		this.manager.addQuestion(model);
		return this.renderText(String.valueOf(number));
	}
	
	public String saveTable()throws Exception {
		String str=(String) this.getRequest().getParameter("questionStr");
		
		String titles=(String) this.getRequest().getParameter("titles");
		String options=(String) this.getRequest().getParameter("options");
		String topTitles=(String)this.getRequest().getParameter("topTitles");
		String surveyId = (String) getRequest().getSession().getAttribute(
		"surveyId");
	//	String[] st=str.split(";");
		String[] sortid = null;
		this.page = this.manager.getQuestionPages(this.page, sortid,
				surveyId);
		List<ToaSurveytableQuestion> question=page.getResult();
		ToaSurveytableSurvey surey=surveyManager.getSurvey(surveyId);
		if(titles!=null && !titles.equals("")){
	    	surey.setTableTitle(titles);
		}
		surey.setSurveyCount(0);
		surveyManager.editSurvey(surey);
		//if(question!=null && question.size()>0){
		 //  manager.delQuestions(question);
		//}
		String tablesty="";
		if(options.equals("radio")){
			tablesty="tableRadio";
		}else if(options.equals("checkbox")){
			tablesty="tableCheckbox";
		}
		if(question!=null && question.size()>0){
			   model=question.get(0);
			   model.setQuestionName(topTitles);
		       model.setQuestionType(tablesty);
			  this.manager.updateQuestion(model);
		}
		else{
		    int number = this.manager.getMaxNumber();
		        model = new ToaSurveytableQuestion();
		       model.setQuestionName(topTitles);
		        model.setQuestionNumber(number);
		       model.setQuestionTrue("false");
		       model.setQuestionType(tablesty);
		       
		       model.setQuestionSurveyId(surveyId);
		      ToaSurveytableSurvey oo = new ToaSurveytableSurvey();
		           oo.setSurveyId(surveyId);
		        model.setToaSurveytableSurvey(oo);
		     this.manager.addQuestion(model); 
		}
		      this.saveAnswers(str,model.getQuestionNumber());
	
     
		return this.renderText("111");
	}
	
public void saveAnswers(String answerName,int queNumber){
		
	    anspage=answManager.getAnswerPages(anspage,queNumber);
		List<ToaSurveytableAnswer> list = anspage.getResult();
		if (null != list) {
			answManager.delAnswers(list);
		}
		
		String[] st=answerName.split(";");
		int sortId=1;
		for(int i=0;i<st.length;i++){
		
			ansmodel = new ToaSurveytableAnswer();
			ansmodel.setAnswerName(st[i]);
			String answerValue=String.valueOf(queNumber)+"||"+sortId+"||"+st[i]+"||N||Y";
			String answerNumber=String.valueOf(queNumber)+"_"+sortId;
			ansmodel.setAnswerValue(answerValue);
			ansmodel.setAnswerNumber(answerNumber);
			ansmodel.setAnswerQueNumber(queNumber);
			ansmodel.setAnswerSortid(sortId);
		ToaSurveytableQuestion toaSurveytableQuestion = new ToaSurveytableQuestion();
		toaSurveytableQuestion.setQuestionId(this.manager
				.getQuestionId(queNumber));
		ansmodel.setToaSurveytableQuestion(toaSurveytableQuestion);
		answManager.addAnswer(ansmodel);
		sortId++;
	}
		//return null;
	}


	/**
	 * 更新问题名
	 * @throws Exception
	 */
	public void update() throws Exception {
		this.manager.updateQuestionName(this.questionNumber, this.questionName);
	}
    /**
     * 修改该问题是否必填
     * @throws Exception
     */
	public void updateQuTrue() throws Exception {
		this.manager.updateQuestionTrue(this.questionNumber, this.questionTrue);
	}
	
	/**
	 * 读取调查问卷的问题并加载页面样式
	 * @return
	 * @throws Exception
	 */
	public String see() throws Exception {
		String forward = "";
		String[] sortid = null;
		forward = "see";
				
		String s = this.surveyManager.getQusSort(this.surveyId);
		if (!("".equals(s) || null == s)) {
			sortid = s.split(",");
			sortid = this.manager.reverse(sortid);
		}
		ToaSurveytableSurvey vo = surveyManager.getSurvey(this.surveyId);

		String html = "";
		try {
			html = this.styleManager.getStyle(vo.getSurveyStyleId())
					.getStyleContent();
		} catch (RuntimeException e) {
			html = null;
		}

		if (!("".equals(html) || null == html)) {
			String tem1 = null;
			tem1 = html.replaceAll("\\$\\{开始时间\\}", vo.getSurveyStartTime()
					.toString().substring(0, 10));
			tem1 = tem1.replaceAll("\\$\\{结束时间\\}", vo.getSurveyEndTime()
					.toString().substring(0, 10));
			tem1 = tem1.replaceAll("\\$\\{调查表名称\\}", vo.getSurveyName());

			String value[] = tem1.split("\\$\\{调查表内容\\}");
			if (value.length > 1) {
				this.topHtml = value[0];
				this.bottomHtml = value[1];
			}
		}
	
		this.page = this.manager.getQuestionPages(this.page, sortid,
				this.surveyId);
		
		return forward;
	}

	/**
	 * 读取调查问卷的问题并加载页面样式
	 * @return
	 * @throws Exception
	 */
	public String view() throws Exception {
		String forward = "";
		String[] sortid = null;
		forward = "view".equals(this.viewType) ? "view" : ("see"
				.equals(this.viewType) ? "see" : "sub");
		String s = this.surveyManager.getQusSort(this.surveyId);
		if (!("".equals(s) || null == s)) {
			sortid = s.split(",");
			sortid = this.manager.reverse(sortid);
		}
		ToaSurveytableSurvey vo = surveyManager.getSurvey(this.surveyId);
		
		isPublic = vo.getIsPublic();//1:限制查看结果
		if("1".equals(isPublic)&&"see".equals(this.viewType)){
			return renderHtml("<script>alert(\"不能查看调查结果\"); window.close();</script>");
		}

		String html = "";
		try {
			html = this.styleManager.getStyle(vo.getSurveyStyleId())
					.getStyleContent();
		} catch (RuntimeException e) {
			html = null;
		}

		if (!("".equals(html) || null == html)) {
			String tem1 = null;
			tem1 = html.replaceAll("\\$\\{开始时间\\}", vo.getSurveyStartTime()
					.toString().substring(0, 10));
			tem1 = tem1.replaceAll("\\$\\{结束时间\\}", vo.getSurveyEndTime()
					.toString().substring(0, 10));
			tem1 = tem1.replaceAll("\\$\\{调查表名称\\}", vo.getSurveyName());

			String value[] = tem1.split("\\$\\{调查表内容\\}");
			if (value.length > 1) {
				this.topHtml = value[0];
				this.bottomHtml = value[1];
			}
		}
		
	
		this.page = this.manager.getQuestionPages(this.page, sortid,
				this.surveyId);
		
		if(vo.getTableTitle()!=null && !vo.getTableTitle().equals("")){
			
			List<ToaSurveytableQuestion> question=page.getResult();
			if(question!=null && question.size()>0){
			String headerstitle=vo.getTableTitle();
			String options="";
			ToaSurveytableQuestion q=question.get(0);
			if(q.getQuestionType().equals("tableRadio")){
				options="radio";
			}
			else if(q.getQuestionType().equals("tableCheckbox")){
				options="checkbox";
			}
			anspage=answManager.getAnswerPages(anspage,q.getQuestionNumber());
			List<ToaSurveytableAnswer> list = anspage.getResult();
			StringBuffer quename=new StringBuffer("");
			StringBuffer answerNumber=new StringBuffer("");
			for(Iterator<ToaSurveytableAnswer> it=list.iterator();it.hasNext();){
				ToaSurveytableAnswer que = it.next();
				quename.append(que.getAnswerName());
				quename.append(";");
				answerNumber.append(que.getAnswerNumber());
				answerNumber.append(",");		
			}
			
			String queName=quename.toString();
			String answNumber=answerNumber.toString();
			this.getRequest().setAttribute("titles", headerstitle);
			this.getRequest().setAttribute("queName", queName);
			this.getRequest().setAttribute("options", options);
			this.getRequest().setAttribute("answNumber", answNumber);
			
			if(viewType.equals("sub") || viewType.equals("sub_sucess")){
				forward="tableSub";
			}else if(viewType.equals("see")){
			
				forward="see";
			}else
			{
				 forward="tableView";
			}
			}
		}
		return forward;
	}
	
	
	public String viewMan(){
		String forward = "";
		String[] sortid = null;
		forward = "view".equals(this.viewType) ? "view" : ("see"
				.equals(this.viewType) ? "see" : "sub");
		String s = this.surveyManager.getQusSort(this.surveyId);
		if (!("".equals(s) || null == s)) {
			sortid = s.split(",");
			sortid = this.manager.reverse(sortid);
		}
		ToaSurveytableSurvey vo = surveyManager.getSurvey(this.surveyId);
		isPublic = vo.getIsPublic();
		
		String html = "";
		try {
			html = this.styleManager.getStyle(vo.getSurveyStyleId())
					.getStyleContent();
		} catch (RuntimeException e) {
			html = null;
		}

		if (!("".equals(html) || null == html)) {
			String tem1 = null;
			tem1 = html.replaceAll("\\$\\{开始时间\\}", vo.getSurveyStartTime()
					.toString().substring(0, 10));
			tem1 = tem1.replaceAll("\\$\\{结束时间\\}", vo.getSurveyEndTime()
					.toString().substring(0, 10));
			tem1 = tem1.replaceAll("\\$\\{调查表名称\\}", vo.getSurveyName());

			String value[] = tem1.split("\\$\\{调查表内容\\}");
			if (value.length > 1) {
				this.topHtml = value[0];
				this.bottomHtml = value[1];
			}
		}
		
	
		this.page = this.manager.getQuestionPages(this.page, sortid,
				this.surveyId);
		
		if(vo.getTableTitle()!=null && !vo.getTableTitle().equals("")){
			
			List<ToaSurveytableQuestion> question=page.getResult();
			if(question!=null && question.size()>0){
			String headerstitle=vo.getTableTitle();
			String options="";
			ToaSurveytableQuestion q=question.get(0);
			if(q.getQuestionType().equals("tableRadio")){
				options="radio";
			}
			else if(q.getQuestionType().equals("tableCheckbox")){
				options="checkbox";
			}
			anspage=answManager.getAnswerPages(anspage,q.getQuestionNumber());
			List<ToaSurveytableAnswer> list = anspage.getResult();
			StringBuffer quename=new StringBuffer("");
			StringBuffer answerNumber=new StringBuffer("");
			for(Iterator<ToaSurveytableAnswer> it=list.iterator();it.hasNext();){
				ToaSurveytableAnswer que = it.next();
				quename.append(que.getAnswerName());
				quename.append(";");
				answerNumber.append(que.getAnswerNumber());
				answerNumber.append(",");		
			}
			
			String queName=quename.toString();
			String answNumber=answerNumber.toString();
			this.getRequest().setAttribute("titles", headerstitle);
			this.getRequest().setAttribute("queName", queName);
			this.getRequest().setAttribute("options", options);
			this.getRequest().setAttribute("answNumber", answNumber);
			
			if(viewType.equals("sub") || viewType.equals("sub_sucess")){
				forward="tableSub";
			}else if(viewType.equals("see")){
			
				forward="see";
			}else
			{
				 forward="tableView";
			}
			}
		}
		return forward;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public Page getPage() {
		return page;
	}

	//---------------------------------

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public String getQuestionTrue() {
		return questionTrue;
	}

	public void setQuestionTrue(String questionTrue) {
		this.questionTrue = questionTrue;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	@Autowired
	public void setManager(SurveyVoteManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setStyleManager(SurveyStyleManager styleManager) {
		this.styleManager = styleManager;
	}

	@Autowired
	public void setSurveyManager(SurveyManager surveyManager) {
		this.surveyManager = surveyManager;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getTopHtml() {
		return topHtml;
	}

	public void setTopHtml(String topHtml) {
		this.topHtml = topHtml;
	}

	public String getBottomHtml() {
		return bottomHtml;
	}

	public void setBottomHtml(String bottomHtml) {
		this.bottomHtml = bottomHtml;
	}

	public SurveyAnswerManager getAnswManager() {
		return answManager;
	}
	@Autowired
	public void setAnswManager(SurveyAnswerManager answManager) {
		this.answManager = answManager;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

}
