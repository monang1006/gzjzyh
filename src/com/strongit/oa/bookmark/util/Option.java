package com.strongit.oa.bookmark.util;

/**
 * 对于没有绑定表字段的控件名字不能修改。
 * 
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-7-7 下午11:04:51
 * @version  2.0.2.3
 * @classpath com.strongit.oa.bookmark.util.Option
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class Option {

	private String id 	;			//控件名称
	
	private String value ;			//控件绑定的字段名
	
	private String name ;			//控件绑定的表名称
	
	private String text ;			//控件标题

	private boolean checked = false;	//是否选中	
	
	public Option(String id,String value , String name,String text){
		this.id = id ;
		this.value = value ;
		this.name = name ;
		this.text = text ;
	}

	public Option(){
		this("","","","");
	}

	public Option(Option option){
		this(option.getId(),option.getValue(),option.getName(),option.getText());
	}
	
	public Option(String text){
		this("","","",text);
	}

	public Option(String id,String value){
		this(id,value,"","");
	}
	
	public Option(String id,String value,String name){
		this(id,value,name,"");
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("<option");
		if(checked){
			builder.append(" selected=\"").append("selected").append("\""); 			
		}
		builder.append(" id=\"").append(id).append("\" "); 
		builder.append(" name=\"").append(name).append("\" ");
		builder.append(" value=\"").append(value).append("\" ");
		builder.append(">");
		builder.append(text);
		builder.append("</option>");
		return builder.toString();
	}

	//@Override
	/*public boolean equals(Object obj) {
		Option option = (Option)obj;
		return option.getId().equals(this.getId()) ;//&& 
			//	option.getName().equals(this.getName()) && 
			//	option.getText().equals(this.getText()) && 
			//	option.getValue().equals(this.getValue());
	}*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
