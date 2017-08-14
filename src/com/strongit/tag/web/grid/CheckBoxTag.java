package com.strongit.tag.web.grid;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class CheckBoxTag extends TableColTag {
	private String caption = null ; /**表头显示文字*/
	private String height = null; /**单元格高度*/
	private String width = null; /**单元格宽度*/
	private boolean isCheckAll = false; /**是否可以全选*/
	private boolean isCanDrag = true; /**是否可以拖动*/
	private boolean isCanSort = true; /**是否可以排序*/
	
	public CheckBoxTag(){
		caption = "选择" ;
		height = "22";
		width = "10";
		isCheckAll = false;
		isCanDrag = true;
	}
	
	public int doStartTag() throws JspException {
		StringBuffer out=new StringBuffer();
		
		FlexTableTag flextag = (FlexTableTag)pageContext.getAttribute("com.strongit.tag.web.grid.FLEXTABLETAG");
		String tablename = flextag.getName();
		String clickcolor = flextag.getClickColor();
		String headCss = flextag.getHeadCss();
		int footShow = flextag.getFootShow()==null||flextag.getFootShow().equals("null")?0:1;
		boolean isShowChecked = "showCheck".equals(flextag.getFootShow())?true:false;
		
		out.append("<th width=\""+width+"\" height=\""+height+"\" align=\"left\"  class=\"").append(headCss).append("\"");
		if(flextag.getIsCanDrag()&&isCanDrag)
			out.append(" onmousemove=\"moveCol(this,document.getElementById('"+tablename+"_div'))\"");
		if(isCanSort)
			out.append(" onclick=\"sort(this,"+footShow+")\"");
		if(isCheckAll)
			out.append(">").append("&nbsp;&nbsp;<input type=\"checkbox\" name=\"checkall\" onclick=\"checkAll(this,document.getElementById('"+tablename+"_td'),'"+clickcolor+"',"+isShowChecked+");\">").append("\r\n")
				.append("</th>");
		else
			out.append(">").append(caption).append("</th>");
		flextag.getHeadList().add(out);
		flextag.getCellList().add(new TableColHelp(getProperty(),getShowValue(),getValuepos(),getValueshowpos(),this));
		//pageContext.setAttribute("com.strongit.tag.web.grid.CHECKBOXTAG."+caption, ((Object) (this)));
		return EVAL_BODY_INCLUDE;
	}
	
	public String innerHTML(Object valueid,Object showvalue,Object onclick,Object ondbclick,Object map){
		StringBuffer out=new StringBuffer();
		FlexTableTag flextag=(FlexTableTag)pageContext.getAttribute("com.strongit.tag.web.grid.FLEXTABLETAG");
		String tablename=flextag.getName();
		String clickcolor=flextag.getClickColor();
		boolean isShowChecked = "showCheck".equals(flextag.getFootShow())?true:false;
		String tdcss=flextag.getDetailCss();
		out.append("<td id=\"chkButtonTd\" class=\""+tdcss+"\" style=\"text-indent: 0px;\" align=\"left\" onmousemove=\"moveCol(this,document.getElementById('"+tablename+"_div'))\">")
			.append("&nbsp;&nbsp;<input type=\"checkbox\" name=\"chkButton\"  id=\"chkButton"+valueid+"\" onclick=\"this.checked=!(this.checked);checkValue(this,document.getElementById('"+tablename+"_td'),'"+clickcolor+"',false);\" value=\""+valueid+"\" showValue=\""+showvalue+"\">")
			.append("</td>");
		return out.toString();
	}
	
	public int doEndTag() throws JspTagException{
		
		return EVAL_PAGE;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public boolean getIsCanDrag() {
		return isCanDrag;
	}

	public void setIsCanDrag(boolean isCanDrag) {
		this.isCanDrag = isCanDrag;
	}

	public boolean getIsCheckAll() {
		return isCheckAll;
	}

	public void setIsCheckAll(boolean isCheckAll) {
		this.isCheckAll = isCheckAll;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public boolean getIsCanSort() {
		return isCanSort;
	}

	public void setIsCanSort(boolean isCanSort) {
		this.isCanSort = isCanSort;
	}

}
