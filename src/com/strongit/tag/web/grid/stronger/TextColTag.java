package com.strongit.tag.web.grid.stronger;

import java.util.UUID;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class TextColTag extends TableColTag {
	private String caption = null ; /**表头显示文字*/
	private String height = null; /**单元格宽度*/
	private String align = null; /**单元格对齐方式*/
	private boolean isCanDrag = true; /**是否可以拖动*/
	private boolean isCanSort = true; /**是否可以排序*/
	
	public TextColTag(){
		caption = "" ;
		height = "22";
		width = "100";
		align = "left";
		isCanDrag = true;
	}
	
	public int doStartTag() throws JspException {
		
		FlexTableTag flextag = (FlexTableTag)pageContext.getAttribute("com.strongit.tag.web.grid.FLEXTABLETAG");
		try {
			flextag.addTableColTag((TableColTag)this.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}
	
	public void doStartHtmml() {
		/**/
		StringBuffer out=new StringBuffer();
		FlexTableTag flextag = (FlexTableTag)pageContext.getAttribute("com.strongit.tag.web.grid.FLEXTABLETAG");
		String headCss = flextag.getHeadCss();
		out.append("<th width=\""+width+"\" height=\""+height+"\" class=\"").append(headCss).append("\" showsize=\"").append(getShowsize()).append("\"");
		String tablename = flextag.getName();
		int footShow = flextag.getFootShow()==null||flextag.getFootShow().equals("null")?0:1;
		if(flextag.getIsCanDrag()&&isCanDrag)
			out.append(" onmousemove=\"moveCol(this,document.getElementById('"+tablename+"_div'))\"");
		if(isCanSort)
			out.append(" onclick=\"sort(this,"+footShow+")\"");
		out.append(" title=\"").append(caption).append("\">").append(caption).append("</th>");
		flextag.getHeadList().add(out);
		flextag.getCellList().add(new TableColHelp(getProperty(),getShowValue(),getValuepos(),getValueshowpos(),getOnclick(),getOndblclick(),this));
		//pageContext.setAttribute("com.strongit.tag.web.grid.TEXTCOLTAG."+caption, ((Object) (this)));
	}
	
	public String innerHTML(Object valueid,Object showvalue,Object onclick,Object ondbclick,Object map){
		StringBuffer out=new StringBuffer();
		boolean isScript = false;
		boolean isImg = false;
		if(showvalue==null){
			showvalue="";
		}else{			
			isScript = showvalue.toString().trim().endsWith("</script>");
		}
		FlexTableTag flextag=(FlexTableTag)pageContext.getAttribute("com.strongit.tag.web.grid.FLEXTABLETAG");
		String tablename=flextag.getName();
		String tdcss=flextag.getDetailCss();
		String tdtitle = "title=\""+showvalue+"\" ";
		if(showvalue.toString().toLowerCase().indexOf("<") != -1){//存在img标签不显示title
			tdtitle = "";
			isImg = true;
		}
		String style = "";
		if(!"".equals(getFontColor())){
			style += "style=\"color:red; text-align:center;\"";
		}
		String current = UUID.randomUUID().toString(); 
		out.append("<td  id=\""+current+"\" "+tdtitle+" "+style+" align=\""+align+"\" class=\""+tdcss+"\" onmousemove=\"moveCol(this,document.getElementById('"+tablename+"_div'))\" value=\""+valueid+"\"");
		if(onclick!=null&&!"".equals(onclick)&&!"null".equals(onclick))
			out.append(" onclick=\""+onclick+"\" style=\"cursor: hand;color: #3366cc;\"");
		if(ondbclick!=null&&!"".equals(ondbclick)&&!"null".equals(ondbclick))
			out.append(" ondblclick=\""+ondbclick+"\"");
		out.append(">")	;
		out.append(showvalue);
		out.append("</td>");
		if(isScript){
			String v= showvalue.toString();
			v = v.substring("<SCRIPT> document.write(".length(), v.length());
			v = v.substring(0, v.length()-"</script>".length()-2);
			String[]  s=v.split(",");//判断函数中是否含有多个参数
			if(s!=null&&s.length>1){
				v = v.substring(0, v.indexOf("(")) + "1" + v.substring(v.indexOf("("), v.length());
			}
			out.append("<script>")
			
			.append("var td = $('#").append(current).append("');")
			.append("td.attr('title',").append(v).append(".indexOf(\"<img\") != -1?\"\":").append(v).append(");")
			.append("</script>");
		}
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

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public boolean getIsCanDrag() {
		return isCanDrag;
	}

	public void setIsCanDrag(boolean isCanDrag) {
		this.isCanDrag = isCanDrag;
	}

	public boolean getIsCanSort() {
		return isCanSort;
	}

	public void setIsCanSort(boolean isCanSort) {
		this.isCanSort = isCanSort;
	}

}
