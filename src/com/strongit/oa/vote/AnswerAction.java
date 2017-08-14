package com.strongit.oa.vote;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOaVoteAnswer;
import com.strongit.oa.survey.util.DrawBar;
import com.strongit.oa.survey.util.DrawPie;
import com.strongit.oa.vote.util.VoteConst;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author piyu
 * Jun 12, 2010
 *
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "answer.action", type = ServletActionRedirectResult.class) })
public class AnswerAction extends BaseActionSupport {
	
	private TOaVoteAnswer answer=new TOaVoteAnswer();
	private AnswerManager asmanager;
	private File pic ;//上传的图片
	private String qid ;//问题ID
	private Page<TOaVoteAnswer> page = new Page(FlexTableTag.MAX_ROWS, true);
	private String question_type ;
	private String pic_size ;//问题的图片尺寸
	/*
     * 图形类型
     * <p>{pie:饼图; bar:柱图}</p>
     */
	private String imageType;
	
	/**
	 * 初始详情链接的数据
	 */
	public String init_set_url(){
		answer=asmanager.getAnswer(answer.getAid());
		
		return "seturl";
	}
	/**
	 * 初始图片设置
	 */
	public String init_set_pic(){
		answer=asmanager.getAnswer(answer.getAid());
		
		return "setpic";
	}
	/**
	 * 柱型数据集
	 */
	private DefaultCategoryDataset getBarDataSet() throws Exception {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<TOaVoteAnswer> list_answer = asmanager.loadAnswer(qid);
		if (list_answer!=null) {
			if(!"table".equals(question_type)){
				//非table型问题
				for (TOaVoteAnswer answer : list_answer) {
					dataset.addValue(answer.getCount(), answer.getContent(), String.format("选项%d",answer.getShowno()));
				}
			}else{
				//table型问题，统计时，使用前两列
				String tmp_content=null;
				for (TOaVoteAnswer answer : list_answer) {
					//table型问题统计时，使用前两列
					tmp_content=answer.getContent();
					if(tmp_content.indexOf("-")>-1){
						//存在一个-
						if(tmp_content.indexOf("-",tmp_content.indexOf("-")+1)>-1){
							//存在两个-
							tmp_content=tmp_content.substring(0,tmp_content.indexOf("-",tmp_content.indexOf("-")+1));
						}
						dataset.addValue(answer.getCount(), tmp_content, String.format("选项%d",answer.getShowno()));
					}else{
						//只有一列
						dataset.addValue(answer.getCount(), tmp_content, String.format("选项%d",answer.getShowno()));
					}
				}
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
		DefaultPieDataset dataset = new DefaultPieDataset();
		List<TOaVoteAnswer> list_answer = asmanager.loadAnswer(qid);
		
		if (list_answer!=null) {
			if(!"table".equals(question_type)){
				//非table型问题
				for (TOaVoteAnswer answer : list_answer) {
					dataset.setValue(answer.getContent(), answer.getCount());
				}
			}else{
				//table型问题，统计时，使用前两列
				String tmp_content=null;
				for (TOaVoteAnswer answer : list_answer) {
					//table型问题统计时，使用前两列
					tmp_content=answer.getContent();
					if(tmp_content.indexOf("-")>-1){
						//存在一个-
						if(tmp_content.indexOf("-",tmp_content.indexOf("-")+1)>-1){
							//存在两个-
							tmp_content=tmp_content.substring(0,tmp_content.indexOf("-",tmp_content.indexOf("-")+1));
						}
						dataset.setValue(tmp_content, answer.getCount());
					}else{
						//只有一列
						dataset.setValue(tmp_content, answer.getCount());
					}
				}
			}
		}
		return dataset;
	}
	/**
	 * 分页获取主观题的答案 
	 */
	public String answerTextPage(){
		String hql="from TOaVoteAnswer answer where answer.question.qid=? order by answer.showno asc";
		Object[]params=new Object[]{qid};
		page=asmanager.getAnswerPage(page, hql, params);
		return "list";
	}
	/**
	 * 获取主观题的答案 
	 */
	public String answerText() throws Exception{
		List<TOaVoteAnswer> list_answer = asmanager.loadAnswer(qid);
		if(list_answer==null){
			return this.renderHtml("");
		}
		StringBuffer html_content=new StringBuffer("");
		for(TOaVoteAnswer answer:list_answer)
		{		
			html_content.append(String.format("<u>%s</u>",answer.getContent()));
			html_content.append("<br>");
		}
		return this.renderHtml(html_content.toString());
	}
	/**
	 * 获取客观题的选项分布图
	 */
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
    /**
     *  文件拷贝到指定位置
     */
	private static void copy(File src, File dst) {
         try {
            InputStream in = null ;
            OutputStream out = null ;
             try {                
                in = new BufferedInputStream( new FileInputStream(src), VoteConst.BUFFER_SIZE);
                out = new BufferedOutputStream( new FileOutputStream(dst), VoteConst.BUFFER_SIZE);
                 byte [] buffer = new byte [VoteConst.BUFFER_SIZE];
                 while (in.read(buffer) > 0 ) {
                    out.write(buffer);
                } 
            } finally {
                 if ( null != in) {
                    in.close();
                } 
                 if ( null != out) {
                    out.close();
                } 
            } 
        } catch (Exception e) {
            e.printStackTrace();
        } 
    } 
    
	/**
	 * 上传图片
	 */
	public String uploadPic(){
		answer=asmanager.getAnswer(answer.getAid());
		String picPath=null;
		if(answer.getPicPath()!=null&&answer.getPicPath().length()>1){
			//原来已经配置图片
			File tmpFile=null;//要删除的文件
			picPath=String.format("%s/%s",getRequest().getRealPath(VoteConst.prefix_picPath),answer.getPicPath());
			tmpFile=new File(picPath);
			if(tmpFile.exists()){
				tmpFile.delete();
			}
		}
		
		TOaVoteAnswer tmpAnswer=new TOaVoteAnswer();
		tmpAnswer.setAid(answer.getAid());
		if(pic==null||pic.length()<1){
			tmpAnswer.setPicPath(null);
		}else{
			tmpAnswer.setPicPath(String.format("%d",System.currentTimeMillis()));
			copy(pic,new File(String.format("%s/%s",getRequest().getRealPath(VoteConst.prefix_picPath),tmpAnswer.getPicPath())));
		}
		
		asmanager.updatePicPath(tmpAnswer);
		
		if(tmpAnswer.getPicPath()==null){
			return this.renderHtml("<script language='javascript'>parent.showEle('');</script>") ;
		}else{
			return this.renderHtml(String.format("<script language='javascript'>parent.showEle('%s');</script>",tmpAnswer.getPicPath())) ;
		}
	}
	/**
	 * 设置详情链接
	 */
	public String updateUrl(){
		asmanager.updateUrl(answer);
		return this.renderHtml("<script language='javascript'>parent.showEle();</script>");
	}
	/**
	 * 通过ID获取答案的图片路径
	 */
	public String getPicPath(){
		answer=asmanager.getAnswer(answer.getAid());
		if(answer.getPicPath()!=null){
			return this.renderText(answer.getPicPath())	;
		}else{
			return this.renderText("")	;
		}
	}
	/**
	 * 通过ID获取答案的详情链接URL
	 */
	public String getUrl(){
		answer=asmanager.getAnswer(answer.getAid());
		if(answer.getUrl()!=null){
			return this.renderText(answer.getUrl())	;
		}else{
			return this.renderText("");
		}
	}
	/**
	 * 新建答案
	 */
	public String saveAnswer(){
		asmanager.addAnswer(answer);
		return this.renderText(answer.getAid()) ;
	}
	/**
	 * 更新答案
	 */
	public String  editAnswer(){
		asmanager.updateAnswer(answer);
		return null ;
	}
	/**
	 * 删除答案
	 */
	public String delAnswer(){
		asmanager.delAnswer(answer);
		return null ;
	}
	public TOaVoteAnswer getAnswer() {
		return answer;
	}
	public void setAnswer(TOaVoteAnswer answer) {
		this.answer = answer;
	}
	public AnswerManager getAsmanager() {
		return asmanager;
	}
	@Autowired
	public void setAsmanager(AnswerManager asmanager) {
		this.asmanager = asmanager;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
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

	public File getPic() {
		return pic;
	}

	public void setPic(File pic) {
		this.pic = pic;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public Page<TOaVoteAnswer> getPage() {
		return page;
	}

	public void setPage(Page<TOaVoteAnswer> page) {
		this.page = page;
	}

	public String getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(String question_type) {
		this.question_type = question_type;
	}
	public String getPic_size() {
		return pic_size;
	}
	public void setPic_size(String pic_size) {
		this.pic_size = pic_size;
	}

}
