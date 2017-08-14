package com.strongit.tag.web.grid.stronger;

import java.util.HashMap;

public class TableColHelp{
	private String property = null;
	private String showValue = null;
	private String valuepos = null;
	private String valueshowpos = null;
	private String onclick = null;/**单元格点击事件*/
	private String ondblclick = null;/**单元格双击事件*/
	private HashMap map = null;
	private TableColTag coltag= null;
	public TableColHelp(){
	}
	
	public TableColHelp(String property,String showValue,String valuepos,String valueshowpos,TableColTag coltag){
		this.property = property;
		this.showValue = showValue;
		this.valuepos = valuepos;
		this.valueshowpos = valueshowpos;
		this.coltag = coltag;
	}
	
	public TableColHelp(String property,String showValue,String valuepos,String valueshowpos,String onclick,String ondblclick,TableColTag coltag){
		this.property = property;
		this.showValue = showValue;
		this.valuepos = valuepos;
		this.valueshowpos = valueshowpos;
		this.coltag = coltag;
		this.onclick = onclick;
		this.ondblclick = ondblclick;
	}
	
	public TableColHelp(String property,String showValue,String valuepos,String valueshowpos,String onclick,String ondblclick,HashMap map,TableColTag coltag){
		this.property = property;
		this.showValue = showValue;
		this.valuepos = valuepos;
		this.valueshowpos = valueshowpos;
		this.coltag = coltag;
		this.onclick = onclick;
		this.ondblclick = ondblclick;
		this.map = map;
	}
	
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

	public TableColTag getColtag() {
		return coltag;
	}

	public void setColtag(TableColTag coltag) {
		this.coltag = coltag;
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

	public HashMap getMap() {
		return map;
	}

	public void setMap(HashMap map) {
		this.map = map;
	}
	
	
}
