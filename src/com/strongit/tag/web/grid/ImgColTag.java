package com.strongit.tag.web.grid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class ImgColTag extends TableColTag {
	private String caption = null ; /**表头显示文字*/
	private String height = null; /**单元格高度*/
	private String width = null;  /**单元格宽度*/
	private boolean isCanDrag = true; /**是否可以拖动*/
	private boolean isCanSort = true; /**是否可以排序*/
	private String imgWidth = "";
	private String imgHigh = "";
	
	public ImgColTag(){
		caption = "文本" ;
		height = "22";
		width = "100";
		isCanDrag = true;
	}
	
	public int doStartTag() throws JspException {
		StringBuffer out=new StringBuffer();
		FlexTableTag flextag = (FlexTableTag)pageContext.getAttribute("com.strongit.tag.web.grid.FLEXTABLETAG");
		String headCss = flextag.getHeadCss();
		out.append("<th width=\""+width+"\" height=\""+height+"\" class=\"").append(headCss).append("\"");
		String tablename = flextag.getName();
		int footShow = flextag.getFootShow()==null||flextag.getFootShow().equals("null")?0:1;
		if(flextag.getIsCanDrag()&&isCanDrag)
			out.append(" onmousemove=\"moveCol(this,document.getElementById('"+tablename+"_div'))\"");
		if(isCanSort)
			out.append(" onclick=\"sort(this,"+footShow+")\"");
		out.append(">").append(caption).append("</th>");
		flextag.getHeadList().add(out);
		flextag.getCellList().add(new TableColHelp(getProperty(),getShowValue(),getValuepos(),getValueshowpos(),getOnclick(),getOndblclick(),this));
		//pageContext.setAttribute("com.strongit.tag.web.grid.TEXTCOLTAG."+caption, ((Object) (this)));
		return EVAL_BODY_INCLUDE;
	}
	
	public String innerHTML(Object valueid,Object showvalue,Object onclick,Object ondbclick,Object map){
		StringBuffer out=new StringBuffer();
		FlexTableTag flextag=(FlexTableTag)pageContext.getAttribute("com.strongit.tag.web.grid.FLEXTABLETAG");
		String tablename=flextag.getName();
		String tdcss=flextag.getDetailCss();
		out.append("<td class=\""+tdcss+"\" onmousemove=\"moveCol(this,document.getElementById('"+tablename+"_div'))\" value=\""+valueid+"\"");
		if(onclick!=null&&!"".equals(onclick)&&!"null".equals(onclick))
			out.append(" onclick=\""+onclick+"\" style=\"cursor: hand;color: blue;\"");
		if(ondbclick!=null&&!"".equals(ondbclick)&&!"null".equals(ondbclick))
			out.append(" ondblclick=\""+ondbclick+"\"");
		out.append("><img src=\"");
		if(showvalue instanceof Blob)
			out.append(getBlob((Blob)showvalue));
		else
			out.append(showvalue);
		out.append("\"></td>");
		
		System.out.println("设置图片长宽为:imgWidth:"+imgWidth+",imgHigh:"+imgHigh);
		return out.toString();
	}
	
	private OutputStream getBlob(Blob imageblob){
//		通过BLOB对象获得一个InputStream
		InputStream input = null;
		try {
			input = imageblob.getBinaryStream();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ServletOutputStream用来传输数据,
		HttpServletResponse response = null;
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			int firstChar = input.read();
			if(firstChar==-1)
				return null;
			int length = input.available();
			
			//将其转换成二进制数据
			byte[] image = new byte[length];
			image[0] = (byte)firstChar;

			int len = 0;
			while ((len = input.read(image,1,length-1)) != -1) {
				out.write(image, 0, len);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
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

	public String getImgHigh() {
		return imgHigh;
	}

	public void setImgHigh(String imgHigh) {
		this.imgHigh = imgHigh;
	}

	public String getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(String imgWidth) {
		this.imgWidth = imgWidth;
	}

}
