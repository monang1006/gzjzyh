package com.strongit.oa.common.statichtml;

import com.strongmvc.webapp.action.BaseActionSupport;

public class StaticHtmlAction extends BaseActionSupport{
	private String blockid;
	
	public void setBlockid(String blockid) {
		this.blockid = blockid;
	}

	/**
	 * author:zhangli
	 * description:图片新闻
	 * modifyer:
	 * description:
	 * @return
	 */
	public String showImgNews(){
		StringBuffer innerHtml=new StringBuffer("<div>")
	      .append("    <div style=\"float:left;width:50%\">")
		  .append("		<img src=\""+getRequest().getContextPath()+"/oa/image/desktop//pictest.jpg\"  width=100%\"/>")
		  .append("    </div>")
		  .append("		<div class=\"linkdiv\" valign=top style=\"float:right;width:48%\">")
		  .append("         <span title=\"关于贯彻党中央贯彻\">")
		  .append("				 2008年11月27日，财政部预算司《中期预算框架研讨会》在江西南昌召开，会议主要研讨我国实施中期预算框架的可行性、必要性以及操作措施，并就编制中期预算框架的内容、程序等问题进行了研究。财政部预算司李承副司长参加会议并讲话。受省长助理...... ")
		  .append("			</span>")
		  .append("		</div>")
		  .append("</div>")
		  .append("   ")
		  .append("");
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * author:zhangli
	 * description:新闻中心
	 * modifyer:
	 * description:
	 * @return
	 */
	public String showNewsCenter(){
		StringBuffer innerHtml=new StringBuffer("	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"元旦放假通知公告\">")
		.append("	<a href=\"#\"> 元旦放假通知公告</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-08 15:28:06)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"OA项目组进入编码阶段\">")
		.append("	<a href=\"#\"> OA项目组进入编码阶段</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-06 09:24:56)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    <div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\">更多</a></div>");
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * author:zhangli
	 * description:公共信息
	 * modifyer:
	 * description:
	 * @return
	 */
	public String showCommonInfo(){
		StringBuffer innerHtml=new StringBuffer("	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"固定电话话费\">")
		.append("	<a href=\"#\"> 固定电话话费</a></span>")
		.append("	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"邮政编码查询\">")
		.append("	<a href=\"#\"> 邮政编码查询</a></span>")
		.append("	")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"常用电话查询\">")
		.append("	<a href=\"#\"> 常用电话查询</a></span>")
		.append("	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"驾照办理\">")
		.append("	<a href=\"#\"> 驾照办理</a></span>")
		.append("	")
		.append("	</div>")
		.append("    	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"交通违法记录查询\">")
		.append("	<a href=\"#\"> 交通违法记录查询</a></span>")
		.append("	&nbsp;&nbsp;&nbsp;&nbsp;")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"交通违法记分查询\">")
		.append("	<a href=\"#\"> 交通违法记分查询</a></span>")
		.append("	")
		.append("	</div>")
		.append("    	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"移动话费查询\">")
		.append("	<a href=\"#\"> 移动话费查询</a></span>")
		.append("	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"联通话费查询\">")
		.append("	<a href=\"#\"> 联通话费查询</a></span>")
		.append("	")
		.append("	</div>")
		.append("    	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"住房公积金查询\">")
		.append("	<a href=\"#\"> 住房公积金查询</a></span>")
		.append("	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"住房贷款查询\">")
		.append("	<a href=\"#\"> 住房贷款查询</a></span>")
		.append("	")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    <div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\">更多</a></div>");
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * author:zhangli
	 * description:指标审核
	 * modifyer:
	 * description:
	 * @return
	 */
	public String showIndexAudit(){
		StringBuffer innerHtml=new StringBuffer("	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位5000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位5000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-05 15:28:06)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位4000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位4000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-01 09:24:56)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    <div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\">更多</a></div>");
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * author:zhangli
	 * description:拨款审核
	 * modifyer:
	 * description:
	 * @return
	 */
	public String showMoneyAudit(){
		StringBuffer innerHtml=new StringBuffer("	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位5000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位5000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-05 15:28:06)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位4000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位4000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-01 09:24:56)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    <div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\">更多</a></div>");
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * author:zhangli
	 * description:银行账户审核
	 * modifyer:
	 * description:
	 * @return
	 */
	public String showBankAudit(){
		StringBuffer innerHtml=new StringBuffer("	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位5000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位5000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-05 15:28:06)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位4000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位4000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-01 09:24:56)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    <div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\">更多</a></div>");
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * author:zhangli
	 * description:网上调查
	 * modifyer:
	 * description:
	 * @return
	 */
	public String showSurveyInLine(){
		StringBuffer innerHtml=new StringBuffer("	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位5000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位5000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-05 15:28:06)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位4000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位4000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-01 09:24:56)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    <div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\">更多</a></div>");
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * author:zhangli
	 * description:处室报告
	 * modifyer:
	 * description:
	 * @return
	 */
	public String showUnitReport(){
		StringBuffer innerHtml=new StringBuffer("	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位5000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位5000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-05 15:28:06)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位4000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位4000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-01 09:24:56)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    <div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\">更多</a></div>");
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * author:zhangli
	 * description:财政运行
	 * modifyer:
	 * description:
	 * @return
	 */
	public String showFinaceRun(){
		StringBuffer innerHtml=new StringBuffer("	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位5000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位5000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-05 15:28:06)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    	<div class=\"linkdiv\" title=\"\">")
		.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">")
		.append("	<span title=\"xxx单位4000万资金的审核\">")
		.append("	<a href=\"#\"> xxx单位4000万资金的审核</a></span>")
		.append("	")
		.append("	<span class =\"linkgray10\">(2008-12-01 09:24:56)</span><span class =\"linkgray\">周吉祥</span>")
		.append("	</div>")
		.append("   ")
		.append("")
		.append("    <div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\">更多</a></div>");
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * author:zhangli
	 * description:预算执行
	 * modifyer:
	 * description:
	 * @return
	 */
	public String showFinaceBudgetRun(){
		StringBuffer innerHtml=new StringBuffer("<div class=\"linkdiv\" title=\"\">")
		  .append("		<img src=\"").append(getRequest().getContextPath()).append("/oa/image/desktop//total.jpg\" width=\"100%\"/>")
		  .append("</div>")
		  .append("   ")
		  .append("")
		  .append("    <div align=\"center\" style=\"padding:2px;font-size:12px;\">省财政厅召开年会......</div>");
		return renderHtml(innerHtml.toString());
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
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
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
}
