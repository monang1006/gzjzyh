package com.strongit.oa.survey;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaSurveytableStyle;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "surveyStyle.action", type = ServletActionRedirectResult.class) })
public class SurveyStyleAction extends BaseActionSupport {

	
	private static final long serialVersionUID = 1L;
	private Page<ToaSurveytableStyle> page = new Page(FlexTableTag.MAX_ROWS, true);
	private ToaSurveytableStyle model = new ToaSurveytableStyle();
	private SurveyStyleManager manager;

	private String styleId;

	@Override
	public String delete() throws Exception {
		this.manager.delStyle(this.styleId);
		return renderHtml("<script>  window.location='"
				+ getRequest().getContextPath()
				+ "/survey/surveyStyle.action'; </script>");
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		prepareModel();
		return "add";
	}

	public String edit() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		addActionMessage("修改成功");
		this.manager.editStyle(this.model);
		return "init";
	}

	@Override
	public String list() throws Exception {
		this.page = this.manager.getStylePages(this.page);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (null != this.styleId) {
			this.model = this.manager.getStyle(this.styleId);
		} else {
			this.model = new ToaSurveytableStyle();
		}

	}

	@Override
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		addActionMessage("保存成功");
		this.manager.addStyle(this.model);
		return "init";
	}
    /**
     * 设置为默认样式
     * @throws Exception
     */
	public void reseat()throws Exception{
		prepareModel();
		String temp ="<TABLE cellSpacing=0 cellPadding=1 width='100%' border=1>"+
		"<TBODY>"+
		"<TR>"+
		"<TD borderColor=#ffffff>&nbsp;<FONT color=#228b22><STRONG>凝聚分散智慧 协同提速发展<BR></STRONG></FONT><FONT color=#000000 size=2>&nbsp;发布日期：&nbsp;${开始时间}--${结束时间}</FONT><FONT color=#000000 size=2>&nbsp;</FONT></TD></TD></TR>"+
		"<TR>"+
		"<TD borderColor=#ffffff><SPAN class=Title><SPAN style='FONT-WEIGHT: bold'>"+
		"<P align=center><FONT color=#ff0000 size=5>${调查表名称}</FONT></P></SPAN></SPAN></TD></TR>"+
		"<TR>"+
		"<TD borderColor=#ffffff align=left>&nbsp;&nbsp; ${调查表内容}</TD></TR></TBODY></TABLE>";
		this.model.setStyleContent(temp);	
		this.manager.editStyle(this.model);
	}
	//--------------------------------------------------------------
	@Autowired
	public void setManager(SurveyStyleManager manager) {
		this.manager = manager;
	}

	public Page getPage() {
		return page;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	//-----------------------------------------------------

	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

}
