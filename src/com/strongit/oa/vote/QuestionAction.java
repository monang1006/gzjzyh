package com.strongit.oa.vote;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOaVote;
import com.strongit.oa.bo.TOaVoteAnswer;
import com.strongit.oa.bo.TOaVoteQuestion;
import com.strongit.oa.vote.util.VoteConst;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author piyu
 * Jun 11, 2010
 *
 */
@ParentPackage("default")
@Results( 
		{ @Result(name = BaseActionSupport.RELOAD, value = "question.action", type = ServletActionRedirectResult.class) })
public class QuestionAction extends BaseActionSupport {

	private TOaVoteQuestion question=new TOaVoteQuestion();
	private QuestionManager qtmanager;
	private VoteManager votemanager;
	private TOaVote vote ;
	private String vid ;//问卷ID
	private String vote_type ;//问卷类型:网页参与，短信参与
	private List<TOaVoteQuestion> list_qt ;
	private String ques_sort ;//问题ID的序列，重新计算显示顺序使用
	/**
	 * 更新问题
	 */
	public String editQuestion(){
		qtmanager.updateQuestion(question);
		return null;
	}
	/**
	 * 新建问题
	 */
	public String saveQuestion(){
		qtmanager.addQuestion(question);
		//返回问题ID
		return  this.renderText(question.getQid());
	}
	/**
	 * 删除问题
	 */
	public String delQuestion(){
		qtmanager.delQuestion(question.getQid());
		return null;
	}
	
	public TOaVoteQuestion getQuestion() {
		return question;
	}

	public void setQuestion(TOaVoteQuestion question) {
		this.question = question;
	}

	public QuestionManager getQtmanager() {
		return qtmanager;
	}
	@Autowired
	public void setQtmanager(QuestionManager qtmanager) {
		this.qtmanager = qtmanager;
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
	/**
	 * 拖动后，重新计算问题的显示顺序
	 */
    public String updateSort(){
    	if(ques_sort==null){
    		return null;
    	}else{
    		qtmanager.updateSort(ques_sort);
    	}
    	return null;
    }
	@Override
	/**
	 * 编辑问卷内容时，获取问卷下的问题
	 */
	public String list() {
		list_qt=qtmanager.loadQuestion(vid,"asc");
		if(VoteConst.vote_type_page.equals(vote_type)){
			//页面投票
			return "page";
		}else if(VoteConst.vote_type_sms.equals(vote_type)){
			//手机短信投票
			return "sms";
		}
		return null;
	}
	/**
	 *  查看问卷时加载问题的答案
	 */
	public String preview_ques(){
		List<TOaVoteAnswer> list_answer=qtmanager.loadAnswer(question.getQid());
		StringBuffer html_answer=new StringBuffer();
		String root=getRequest().getContextPath();
		question=qtmanager.getQuestion(question.getQid());//
		
		if("text".equals(question.getType())){
			//文本框型的问题，名字使用text_开头
			html_answer.append(String.format("<input type='text' maxlength=100 style='width:200px' name='text_%s' />",question.getQid()));
		}else if("textarea".equals(question.getType())){
			//文本域型的问题，名字使用textarea_开头
			html_answer.append(String.format("<textarea name='textarea_%s' cols='50' rows='5'></textarea>",question.getQid()));
		}else if("select".equals(question.getType())){
			html_answer.append(String.format("<select name='%s'>",question.getQid()));
			for(TOaVoteAnswer answer:list_answer){
				html_answer.append(String.format("<option value='%s'>%s</option>",answer.getAid(),answer.getContent()));
			}
			html_answer.append("</select>");
		}else if("radio".equals(question.getType())||"checkbox".equals(question.getType())){
			TOaVoteAnswer answer=null;
			html_answer.append("<table cellspacing='6px' class='rc'><tr class='rc_row'>");
			for(int i=0;i<list_answer.size();i++){
				answer=list_answer.get(i);
				html_answer.append("<td><div id='content_div'>");
				html_answer.append(String.format("<input type='%s' name='%s' value='%s'>",question.getType(),question.getQid(),answer.getAid()));
				//详情链接处理
				if(answer.getUrl()!=null&&answer.getUrl().length()>1){
					html_answer.append(String.format("<a href='%s' target='_blank'>%s</a>",answer.getUrl(),answer.getContent()));
				}else{
					html_answer.append(String.format("%s</div>",answer.getContent()));
				}
				html_answer.append("</td>");
				if(list_answer.size()==(i+1)){
					//不存在下一个答案项
					html_answer.append("</tr></table>");
				}else{
					if((i+1)%question.getMaxRow()==0){
						//换行
						html_answer.append("</tr><tr class='rc_row'>");
					}
				}
			}
		}else if("pradio".equals(question.getType())||"pcheckbox".equals(question.getType())){
			html_answer.append("<table class='rc' ><tr class='rc_row'>");
			TOaVoteAnswer answer=null;
			int width=200,height=200;//图片的尺寸
			String []pic_size=null;
			String tmp_type=question.getType().substring(1);//截取类型
			if(question.getPicSize()!=null&&question.getPicSize().length()>0){
					// 设置图片尺寸
					pic_size=question.getPicSize().split("\\*");
					try{
						width=Integer.parseInt(pic_size[0]);
						height=Integer.parseInt(pic_size[1]);
					}catch(Exception e){
						this.logger.error(String.format("图片尺寸错误:[%s]",question.getPicSize()));
					}
				}
			
			for(int i=0;i<list_answer.size();i++){
				answer=list_answer.get(i);
				if(question.getPicSize()!=null&&question.getPicSize().length()>0){
					//配置图片
					html_answer.append("<td style='vertical-align:top'>");
					html_answer.append(String.format("<div style='width:%dpx;height:%dpx'>",width+10,height+10));
					if(answer.getPicPath()!=null&&answer.getPicPath().length()>0){
						html_answer.append(String.format("<img border='1px' width='%dpx' height='%dpx' src='%s%s/%s'>",width,height,root,VoteConst.prefix_picPath,answer.getPicPath()));		
					}
				
				html_answer.append("</div>");
				html_answer.append(String.format("<div style='width:%dpx'>",width+10,height+10));
				html_answer.append(String.format("<input type='%s' name='%s' value='%s'>",tmp_type,question.getQid(),answer.getAid()));
				//详情链接处理
				if(answer.getUrl()!=null&&answer.getUrl().length()>1){
					html_answer.append(String.format("<a href='%s' target='_blank'>%s</a>",answer.getUrl(),answer.getContent()));
				}else{
					html_answer.append(answer.getContent());
				}
				html_answer.append("</div></td>");
				if(list_answer.size()==(i+1)){
					//不存在下一个答案项
					html_answer.append("</tr></table>");
				}else{
					if((i+1)%question.getMaxRow()==0){
						//换行
						html_answer.append("</tr><tr class='rc_row'>");
					}
				}
			}else{
				//没有配置图片
				html_answer.append("<td><div id='content_div'>");
				html_answer.append(String.format("<input type='%s' name='%s' value='%s'>",tmp_type,question.getQid(),answer.getAid()));
				//详情链接处理
				if(answer.getUrl()!=null&&answer.getUrl().length()>1){
					html_answer.append(String.format("<a href='%s' target='_blank'>%s</a>",answer.getUrl(),answer.getContent()));
				}else{
					html_answer.append(answer.getContent());
				}
				html_answer.append("</div></td>");
				if(list_answer.size()==(i+1)){
					//不存在下一个答案项
					html_answer.append("</tr></table>");
				}else{
					if((i+1)%question.getMaxRow()==0){
						//换行
						html_answer.append("</tr><tr class='rc_row'>");
					}
				}
			}
		  }
		}else if("table".equals(question.getType())){
			String[] tableheader=question.getTableHeader().split(VoteConst.table_separator);
			html_answer.append("<table class='mt'><tr class='mt_row'>");
			{//表头处理
			html_answer.append("<td>&nbsp;</td>");
		    for(int i=0;i<tableheader.length;i++){
		    	html_answer.append(String.format("<td>%s</td>",tableheader[i]));
		    }
		    html_answer.append("</tr>");
			}
			
			{//表格内容处理
				String[] table_content_row=null;
				for(TOaVoteAnswer answer:list_answer){
					if("Y".equals(question.getTableisonly())){
						//单选
						html_answer.append(String.format("<tr class='mt_row'><td><input name='%s' type='radio' value='%s'></td>",question.getQid(),answer.getAid()));
					}else{
						//多选
						html_answer.append(String.format("<tr class='mt_row'><td><input name='%s' type='checkbox' value='%s'></td>",question.getQid(),answer.getAid()));
					}
					table_content_row=answer.getContent().split(VoteConst.table_separator);
					
					for(int i=0;i<table_content_row.length;i++){
						html_answer.append(String.format("<td>%s</td>", table_content_row[i]));
					}
					html_answer.append("</tr>");
				}
			}
			html_answer.append("</table>");
		}
		
		return this.renderHtml(html_answer.toString());
	}
	
	/**
	 * 编辑问题时,加载问题的答案 
	 */
	public String load_answer(){
		List<TOaVoteAnswer> list_answer=qtmanager.loadAnswer(question.getQid());
		StringBuffer html_answer=new StringBuffer();
		String root=getRequest().getContextPath();
		
		if("text".equals(question.getType())){
			html_answer.append("<input type='text' maxlength=50 style='width:160px' readonly='true' name='textfield' id='textfield' />");
		}else if("textarea".equals(question.getType())){
			html_answer.append("<textarea name='textarea' readonly='true' id='textarea' cols='45' rows='5'></textarea>");
		}else if("radio".equals(question.getType())||"checkbox".equals(question.getType())){
			for(TOaVoteAnswer answer:list_answer){
				html_answer.append(String.format("<div id='answer_%s'>",answer.getAid()));
			    html_answer.append(String.format("<table><tr><td><input name='rd' class='%s' type='%s'>&nbsp;<span id='content_%s' ondblClick=editContent('%s',this.value)>%s</span></td>",question.getType(),question.getType(),answer.getAid(),answer.getAid(),answer.getContent()));
			    html_answer.append(String.format("<td style='padding-left:20px'><img src='%s/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editContent('%s',this.value)\" title='编辑'> <img src='%s/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'onClick=\"delAnswer('%s')\">",root,answer.getAid(),root,answer.getAid()));
			    if(VoteConst.vote_type_page.equals(vote_type)){
			    	//页面投票才存在详情链接
			    	html_answer.append(String.format("<a href='#' onclick=seturl('%s','%s')>[设置详情链接]</a>", answer.getAid(),answer.getUrl()==null?"":answer.getUrl()));
			    }
			    html_answer.append("</td></tr></table></div>");
			}	
		}else if("pradio".equals(question.getType())||"pcheckbox".equals(question.getType())){
			String tmp_type=question.getType().substring(1);//截取类型
			for(TOaVoteAnswer answer:list_answer){
				html_answer.append(String.format("<div id='answer_%s'>",answer.getAid()));
			    html_answer.append(String.format("<table><tr><td><input name='rd' class='%s' type='%s'>&nbsp;<span id='content_%s' ondblClick=editContent('%s',this.value)>%s</span></td>",tmp_type,tmp_type,answer.getAid(),answer.getAid(),answer.getContent()));
			    html_answer.append(String.format("<td style='padding-left:20px'><img src='%s/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editContent('%s',this.value)\" title='编辑'> <img src='%s/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'onClick=\"delAnswer('%s')\">",root,answer.getAid(),root,answer.getAid()));
			    html_answer.append(String.format("<a href='#' onclick=seturl('%s','%s')>[设置详情链接]</a>", answer.getAid(),answer.getUrl()==null?"":answer.getUrl()));
			    if(VoteConst.vote_type_page.equals(vote_type)){
			    	//网页投票
			    	html_answer.append(String.format("<a href='#' onclick=uploadpic('%s','%s','%s')>[配置图片]</a>", answer.getAid(),answer.getPicPath()==null?"":answer.getPicPath(),question.getQid()));
			    }
			    html_answer.append("</td></tr></table></div>");
			}
		}else if("select".equals(question.getType())){
			for(TOaVoteAnswer answer:list_answer){
				html_answer.append(String.format("<div id='answer_%s'>",answer.getAid()));
				html_answer.append(String.format("<table><tr><td>--<span id='content_%s' onClick=\"editContent('%s',this.value)\">%s</span>--</td>",answer.getAid(),answer.getAid(),answer.getContent()));
				html_answer.append(String.format("<td style='padding-left:20px'><img src='%s/oa/image/survey/edit.gif' class='imglinkgray' onClick=\"editContent('%s',this.value)\" title='编辑'> <img src='%s/oa/image/survey/delete_s.gif' class='imglinkgray' title='删除'  onClick=\"delAnswer('%s')\">",root,answer.getAid(),root,answer.getAid()));
				html_answer.append("</td></tr></table></div>");
			}
		}else if("table".equals(question.getType())){
			question=qtmanager.getQuestion(question.getQid());
			//table型问题
			String[] tableheader=question.getTableHeader().split(VoteConst.table_separator);
			{
			//表头处理
			html_answer.append(String.format("<table id='table_%s'><tr><td>",question.getQid()));
			html_answer.append(String.format("<img src='%s/oa/image/survey/save.gif' name='tableSaveImg' class='imglinkgray' onclick=savetable('%s') title='保存表格数据' style='display:'></td>",root,question.getQid()));
            for(int i=0;i<tableheader.length;i++){
            	html_answer.append(String.format("<td><input type=text value='%s'></td>",tableheader[i]));
            }
            html_answer.append("</tr>");
			}
			{
			//答案加载
			for(TOaVoteAnswer answer:list_answer){
				html_answer.append("<tr><td><input ondblclick=del_row(this) onmouseout='hideTip()' onmouseover='showTip(\"双击删除行\",this)'");
				if("Y".equals(question.getTableisonly())){
					html_answer.append(String.format(" type='radio' name='rd' value='%s'></td>",answer.getAid()));
				}else{
					html_answer.append(String.format(" type='checkbox' name='rd' value='%s'></td>",answer.getAid()));
				}
					
				String []array_content=answer.getContent().split(VoteConst.table_separator);
				for(int i=0;i<array_content.length;i++){
					html_answer.append(String.format("<td><input maxlength=30 type=text  value='%s'></td>",array_content[i]));
				}
				html_answer.append("</tr>");
				}
			}
		}
		return this.renderHtml(html_answer.toString());
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
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	
	public String getVote_type() {
		return vote_type;
	}
	public void setVote_type(String vote_type) {
		this.vote_type = vote_type;
	}
	public List<TOaVoteQuestion> getList_qt() {
		return list_qt;
	}
	public void setList_qt(List<TOaVoteQuestion> list_qt) {
		this.list_qt = list_qt;
	}
	public String getQues_sort() {
		return ques_sort;
	}
	public void setQues_sort(String ques_sort) {
		this.ques_sort = ques_sort;
	}
	public VoteManager getVotemanager() {
		return votemanager;
	}
	@Autowired
	public void setVotemanager(VoteManager votemanager) {
		this.votemanager = votemanager;
	}
	public TOaVote getVote() {
		return vote;
	}
	public void setVote(TOaVote vote) {
		this.vote = vote;
	}

}
