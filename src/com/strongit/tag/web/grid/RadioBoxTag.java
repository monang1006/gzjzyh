package com.strongit.tag.web.grid;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class RadioBoxTag extends TableColTag {
	private String caption = null ; /**表头显示文字*/
	private String height = null; /**单元格高度*/
	private String width = null; /**单元格宽度*/
	private boolean isCanDrag = true; /**是否可以拖动*/
	private boolean isCanSort = true; /**是否可以排序*/
	
	public RadioBoxTag(){
		caption = "选择" ;
		height = "22";
		width = "10";
		isCanDrag = true;
	}
	
	public int doStartTag() throws JspException {
		StringBuffer out=new StringBuffer();
		
		FlexTableTag flextag = (FlexTableTag)pageContext.getAttribute("com.strongit.tag.web.grid.FLEXTABLETAG");
		String tablename = flextag.getName();
		String headCss = flextag.getHeadCss();
		int footShow = flextag.getFootShow()==null||flextag.getFootShow().equals("null")?0:1;
		
		out.append("<th width=\""+width+"\" height=\""+height+"\" class=\"").append(headCss).append("\"");
		if(flextag.getIsCanDrag()&&isCanDrag)
			out.append(" onmousemove=\"moveCol(this,document.getElementById('"+tablename+"_div'))\"");
		if(isCanSort)
			out.append(" onclick=\"sort(this,"+footShow+")\"");
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
		String tdcss=flextag.getDetailCss();
		boolean isShowChecked = "showCheck".equals(flextag.getFootShow())?true:false;
		out.append("<td id=\"chkRadioTd\" class=\""+tdcss+"\" style=\"text-indent: 0px;\" align=\"center\" onmousemove=\"moveCol(this,document.getElementById('"+tablename+"_div'))\">")
			.append("<input type=\"radio\" name=\"chkRadio\" id=\"radio"+valueid+"\" onclick=\"this.checked=!(this.checked);checkSingleValue(this,document.getElementById('"+tablename+"_td'),'"+clickcolor+"',"+isShowChecked+");\" value=\""+valueid+"\" showValue=\""+showvalue+"\">")
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
