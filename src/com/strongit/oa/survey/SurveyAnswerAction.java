package com.strongit.oa.survey;

import java.awt.Color;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSurveytableAnswer;
import com.strongit.oa.bo.ToaSurveytableQuestion;
import com.strongit.oa.survey.util.DrawBar;
import com.strongit.oa.survey.util.DrawPie;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

public class SurveyAnswerAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	private ToaSurveytableAnswer model = new ToaSurveytableAnswer();
	private Page<ToaSurveytableAnswer> page = new Page();

	private SurveyAnswerManager manager;

	private SurveyVoteManager managerVote;
	
	private SurveyManager    managerSurvey;

	private String answerValue;//答案值

	private String answerNumber;//答案ID

	private int answerQueNumber;//和问题ID对应
	
	private String surveyId;
	/*
	 * 问题类型
	 * <p>{radio:单选;checkbox:多选;select:下拉;textfield:文本; textarea:多行文本}</P>
	 */
	private String type;

	private String answerName;//答案名
	private int answerSortid;  //答案值ID

	private String strQnum;//投票结果,字符串格式
    /*
     * 图形类型
     * <p>{pie:饼图; bar:柱图}</p>
     */
	private String imageType;

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub

		this.manager.delAnswer(this.answerNumber);
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		StringBuffer text = new StringBuffer();
		this.manager.getAnswerPages(page, this.answerQueNumber);
		List<ToaSurveytableAnswer> list = page.getResult();
		if (null != list) {
			for (ToaSurveytableAnswer model : list) {
				if (!("".equals(model.getAnswerValue()) || null == model
						.getAnswerValue())) {
					text.append(model.getAnswerValue() + "<br>-------------------------------------------<br>");
				}
			}
		}
		return this.renderHtml(text.toString());
	}

	@Override
	protected void prepareModel() throws Exception {
	}
    
	/**
	 * 添加问题答案
	 */
	@Override
	public String save() throws Exception {
		model = new ToaSurveytableAnswer();
		model.setAnswerName(this.answerName);
		model.setAnswerValue(this.answerValue);
		model.setAnswerNumber(this.answerNumber);
		model.setAnswerQueNumber(this.answerQueNumber);
		model.setAnswerSortid(this.answerSortid);
		ToaSurveytableQuestion toaSurveytableQuestion = new ToaSurveytableQuestion();
		toaSurveytableQuestion.setQuestionId(this.managerVote
				.getQuestionId(this.answerQueNumber));
		model.setToaSurveytableQuestion(toaSurveytableQuestion);
		this.manager.addAnswer(this.model);
		return null;
	}
	
	public String saveAnswers() throws Exception {
		
		this.manager.getAnswerPages(page, this.answerQueNumber);
		List<ToaSurveytableAnswer> list = page.getResult();
		if (null != list) {
			manager.delAnswers(list);
		}
		
		String[] st=answerName.split(";");
		for(int i=0;i<st.length;i++){
		 
       
		model = new ToaSurveytableAnswer();
		model.setAnswerName(st[i]);
		model.setAnswerValue(this.answerValue);
		model.setAnswerNumber(this.answerNumber);
		model.setAnswerQueNumber(this.answerQueNumber);
		model.setAnswerSortid(this.answerSortid);
		ToaSurveytableQuestion toaSurveytableQuestion = new ToaSurveytableQuestion();
		toaSurveytableQuestion.setQuestionId(this.managerVote
				.getQuestionId(this.answerQueNumber));
		model.setToaSurveytableQuestion(toaSurveytableQuestion);
		this.manager.addAnswer(this.model);
	}
		return null;
	}

	/**
	 * 投票提交
	 * 
	 * @return
	 * @throws Exception
	 */
	public String submit() throws Exception {

		if (!("".equals(this.answerNumber))) {
			String[] ans_N = this.answerNumber.split(",");
			this.manager.updateAnswerCount(ans_N);
		}

		if (!("".equals(this.strQnum))) {
			String[] ans_QN = this.strQnum.split(",");
			ToaSurveytableQuestion toaSurveytableQuestion = new ToaSurveytableQuestion();
			//model = new ToaSurveytableAnswer();
			for (int i = 0; i < ans_QN.length; i++) {
				model = new ToaSurveytableAnswer();
				getRequest().getParameter(ans_QN[i]);
				int sortid = this.manager.getMaxNumber(Integer
						.parseInt(ans_QN[i]));
				model.setAnswerValue(getRequest().getParameter(ans_QN[i]));
				model.setAnswerNumber(ans_QN[i] + "_" + String.valueOf(sortid));
				model.setAnswerQueNumber(Integer.parseInt(ans_QN[i]));
				model.setAnswerSortid(sortid);
				toaSurveytableQuestion.setQuestionId(this.managerVote
						.getQuestionId(Integer.parseInt(ans_QN[i])));
				model.setToaSurveytableQuestion(toaSurveytableQuestion);
				this.manager.addAnswer(this.model);
			}
		}
		this.managerSurvey.surveyCount(this.surveyId);
		this.managerSurvey.surveyUnrepeat(this.surveyId);
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		addActionMessage("投票成功");
		//return "init";
	    return renderHtml("<script>window.location='" + getRequest().getContextPath() + "/survey/surveyVote!view.action?viewType=sub_sucess&surveyId="+this.surveyId+"'; </script>");
	}
	public String submitTable() throws Exception {

		if (!("".equals(this.answerNumber))) {
			String[] ans_N = this.answerNumber.split(",");
			this.manager.updateAnswerCount(ans_N);
		}

		this.managerSurvey.surveyCount(this.surveyId);
		this.managerSurvey.surveyUnrepeat(this.surveyId);
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		addActionMessage("投票成功");
		//return "init";
	    return renderHtml("<script>window.location='" + getRequest().getContextPath() + "/survey/surveyVote!view.action?viewType=sub_sucess&surveyId="+this.surveyId+"'; </script>");
	}
	
    /**
     * 读取最大答案id
     * @return
     * @throws Exception
     */
	public String getSortid() throws Exception {

		int number = this.manager.getMaxNumber(this.answerQueNumber);
		return this.renderText(String.valueOf(number));
	}

	/**
	 * 设置问卷时加载答案内容
	 * 
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
		StringBuffer text = new StringBuffer("");
		String path = getRequest().getContextPath();
		this.page = this.manager
				.getAnswerPages(this.page, this.answerQueNumber);
		List<ToaSurveytableAnswer> list = page.getResult();
		if (null != list) {
			for (ToaSurveytableAnswer model : list) {
				if ("radio".equals(this.type) || "checkbox".equals(this.type)) {
					text
							.append("<div id='"
									+ model.getAnswerNumber()
									+ "'><table><tr><td><input class="
									+ this.type
									+ " name="
									+ this.type
									+ "_"
									+ model.getAnswerQueNumber()
									+ " type="
									+ this.type
									+ ">&nbsp;<span id='title_"
									+ model.getAnswerNumber()
									+ "' onClick=\"editTitle('"
									+ model.getAnswerNumber()
									+ "','"
									+ model.getAnswerValue()
									+ "')\">"
									+ model.getAnswerName()
									+ "</span></td><td style='padding-left:20px'><img src='"
									+ path
									+ "/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editTitle('"
									+ model.getAnswerNumber()
									+ "','"
									+ model.getAnswerValue()
									+ "')\" title='编辑'> <img src='"
									+ path
									+ "/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'onClick=\"delQuestion('"
									+ model.getAnswerNumber()
									+ "')\"></td></tr></table></div>");
				}
				if ("select".equals(this.type)) {
					text
							.append("<div id='"
									+ model.getAnswerNumber()
									+ "'><table><tr><td>--<span id='title_"
									+ model.getAnswerNumber()
									+ "' onClick=\"editTitle('"
									+ model.getAnswerNumber()
									+ "','"
									+ model.getAnswerValue()
									+ "')\">"
									+ model.getAnswerName()
									+ "</span>--</td><td style='padding-left:20px'><img src='"
									+ path
									+ "/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editTitle('"
									+ model.getAnswerNumber()
									+ "','"
									+ model.getAnswerValue()
									+ "')\" title='编辑'> <img src='"
									+ path
									+ "/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'  onClick=\"delQuestion('"
									+ model.getAnswerNumber()
									+ "')\"></td></tr></table></div>");
				}

			}
		}
		if ("textfield".equals(this.type)) {
			text
					.append("<input type='text' name='textfield' id='textfield' />");
		}
		if ("textarea".equals(this.type)) {
			text
					.append("<textarea name='textarea' id='textarea' cols='45' rows='5'></textarea>");
		}
		return this.renderHtml(text.toString());
	}

	/**
	 * 点预览时加载答案内容
	 * 
	 * @return
	 * @throws Exception
	 */
	public String initView() throws Exception {
		StringBuffer text = new StringBuffer("");
		this.page = this.manager
				.getAnswerPages(this.page, this.answerQueNumber);
		List<ToaSurveytableAnswer> list = page.getResult();
		if (null != list) {
			if ("radio".equals(this.type) || "checkbox".equals(this.type)) {
				for (ToaSurveytableAnswer model : list) {
					text.append("<table><tr><td><input class=" + this.type
							+ " name='" + model.getAnswerQueNumber()
							+ "' type=" + this.type + " value='"
							+ model.getAnswerNumber()
							+ "' >&nbsp;<span id='title_"
							+ model.getAnswerNumber() + "' >"
							+ model.getAnswerName()
							+ "</span></td></tr></table>");
				}
			}
			if ("select".equals(this.type)) {
				String top = "";
				StringBuffer cen = new StringBuffer();
				String end = "</select>";
				for (ToaSurveytableAnswer model : list) {
					top = "<select name='" + model.getAnswerQueNumber() + "'>";
					cen.append("<option value='").append(
							model.getAnswerNumber()).append("' >").append(
							model.getAnswerName()).append("</option>");
				}
				text.append(top + cen + end);
			}

		}
		if ("textfield".equals(this.type)) {
			for (ToaSurveytableAnswer model : list) {
				text.append("<input type='text' name='").append(
						model.getAnswerQueNumber()).append(
						"' id='textfield' />");
				break;
			}
		}
		if ("textarea".equals(this.type)) {
			for (ToaSurveytableAnswer model : list) {
				text.append("<textarea name='").append(
						model.getAnswerQueNumber()).append(
						"' id='textarea' cols='45' rows='5'></textarea>");
				break;
			}
		}
		return this.renderHtml(text.toString());
	}

	public void update() throws Exception {
		this.manager.updateAnswerName(this.answerNumber, this.answerValue,
				this.answerName);
	}
	
	/**
	 *  检验用户是否以参与投票
	 * @return
	 */
	public String surveyUnrepeatCheck()
	{
		return this.renderText(this.managerSurvey.surveyUnrepeatCheck(this.surveyId));
	}

	public Object getModel() {
		return model;
	}

	public Page getPage() {
		return page;
	}

	// -----------------------

	/**
	 * 柱型数据集
	 */
	private DefaultCategoryDataset getBarDataSet() throws Exception {
		this.page = this.manager
				.getAnswerPages(this.page, this.answerQueNumber);
		List<ToaSurveytableAnswer> list = page.getResult();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (null != list) {
			for (ToaSurveytableAnswer model : list) {
				dataset.addValue(model.getAnswerCount(), "选项", model
						.getAnswerName());

			}
		}
		return dataset;
	}

	/**
	 * 饼型数据集
	 * 
	 * @return
	 * @throws Exception
	 */
	private DefaultPieDataset getPieDataSet() throws Exception {
		this.page = this.manager
				.getAnswerPages(this.page, this.answerQueNumber);
		List<ToaSurveytableAnswer> list = page.getResult();

		DefaultPieDataset dataset = new DefaultPieDataset();

		if (null != list) {
			for (ToaSurveytableAnswer model : list) {
				dataset.setValue(model.getAnswerName(), model.getAnswerCount());

			}
		}
		return dataset;
	}

	/**
	 * 柱形图
	 */
	/*public String answer() throws Exception {

		getResponse().setHeader("cache-control", "no-cache");
		getResponse().setHeader("cache-control", "no-store");
		getResponse().setDateHeader("expires", 0);
		getResponse().setHeader("pragma", "no-cache");

		DefaultCategoryDataset dataset = getBarDataSet();
		this.chart = ChartFactory.createBarChart3D("结果统计图", "调查结果", "投票数",
				dataset, PlotOrientation.VERTICAL, true, true, false);

		this.chart.setBackgroundPaint(new Color(15528953));

		this.chart.setTitle(new TextTitle("结果统计图", new Font("黑体", 2, 18)));

		LegendTitle legend = this.chart.getLegend(0);

		legend.setItemFont(new Font("宋体", 1, 14));
		CategoryPlot plot = (CategoryPlot) this.chart.getPlot();

		CategoryAxis categoryAxis = plot.getDomainAxis();

		categoryAxis.setLabelFont(new Font("宋体", 1, 18));

		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		categoryAxis.setTickLabelFont(new Font("宋体", 1, 18));

		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();

		numberAxis.setLabelFont(new Font("宋体", 1, 18));

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setDrawBarOutline(false);
		renderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		return "chart";
	}*/

	public String answerImage() throws Exception {
		// 清除缓存
		getResponse().setHeader("cache-control", "no-cache");
		getResponse().setHeader("cache-control", "no-store");
		getResponse().setDateHeader("expires", 0);
		getResponse().setHeader("pragma", "no-cache");

		if ("bar".equals(this.imageType)) {
			// 生成绘图类对象
			DrawBar bar = DrawBar.getInstance();
			// 添加数据
			bar.setDataset(getBarDataSet());
			// 3.设置图片属性
			// bar.init();
			bar.setTitle("问题调查柱状图");
			bar.setYTitle("投票数");
			bar.setXTitle("答案");
			bar.setBgColor("white");
			bar.setIsV(true);
			bar.setWidth(400);
			bar.setMargin(0.5);
			bar.setHeight(300);
			bar.setXFontSize(10);
			bar.setYFontSize(10);

			// 输出图片
			bar.writeWebChartAsJPEG(getResponse().getOutputStream());
		}
		if ("pie".equals(this.imageType)) {
			// 1.生成画饼图的类对象
			DrawPie pie = DrawPie.getInstance();
			// 添加数据
			pie.setDataset(getPieDataSet());

			// 3.设置图片属性
			// pie.init();
			pie.setBgcolor(Color.white);
			pie.setTitle("问题调查饼图");
			pie.setWidth(400);
			pie.setHeight(300);
			pie.setLabelFontSize(10);
			pie.setFactor(0.1);
			pie.writeWebChartAsJPEG(getResponse().getOutputStream());

		}

		return "chart";
	}

	// -----------------------

	public SurveyAnswerManager getManager() {
		return manager;
	}

	@Autowired
	public void setManager(SurveyAnswerManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setManagerVote(SurveyVoteManager managerVote) {
		this.managerVote = managerVote;
	}
	
	@Autowired
	public void setManagerSurvey(SurveyManager managerSurvey) {
		this.managerSurvey = managerSurvey;
	}

	public String getAnswerValue() {
		return answerValue;
	}

	public void setAnswerValue(String answerValue) {
		this.answerValue = answerValue;
	}

	public String getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(String answerNumber) {
		this.answerNumber = answerNumber;
	}

	public int getAnswerQueNumber() {
		return answerQueNumber;
	}

	public void setAnswerQueNumber(int answerQueNumber) {
		this.answerQueNumber = answerQueNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAnswerName() {
		return answerName;
	}

	public void setAnswerName(String answerName) {
		this.answerName = answerName;
	}

	public int getAnswerSortid() {
		return answerSortid;
	}

	public void setAnswerSortid(int answerSortid) {
		this.answerSortid = answerSortid;
	}


	public String getStrQnum() {
		return strQnum;
	}

	public void setStrQnum(String strQnum) {
		this.strQnum = strQnum;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}
}
