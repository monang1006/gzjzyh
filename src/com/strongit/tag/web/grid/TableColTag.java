package com.strongit.tag.web.grid;

import javax.servlet.jsp.tagext.TagSupport;

public abstract class TableColTag extends TagSupport{
	private String property = null; /**单元格值对应字段字段*/
	private String showValue = null; /**单元格显示内容对应字段*/
	private String valuepos = null; /**单元格值对应数组位置*/
	private String valueshowpos = null; /**单元格值对应数组位置*/
	private String onclick = null;/**单元格点击事件*/
	private String ondblclick = null;/**单元格双击事件*/
	private int showsize = 18;/**单元格显示字符数*/
	private String fontColor = "";/**每个td显示的字体颜色*/
	public TableColTag(){
		
	}
	
	public abstract String innerHTML(Object valueid,Object showvalue,Object onclick,Object ondbclick,Object map);
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValuepos() {
		return valuepos;
	}

	public void setValuepos(String valuepos) {
		this.valuepos = valuepos;
	}

	public String getShowValue() {
		return showValue;
	}

	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}

	public String getValueshowpos() {
		return valueshowpos;
	}

	public void setValueshowpos(String valueshowpos) {
		this.valueshowpos = valueshowpos;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getOndblclick() {
		return ondblclick;
	}

	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	public int getShowsize() {
		return showsize;
	}

	public void setShowsize(int showsize) {
		this.showsize = showsize;
	}


	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

}
