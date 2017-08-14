package com.strongit.oa.common.eform.model;

import java.util.Map;

import com.strongit.form.vo.FormTemplate.FormTemplateComponent;

import net.sf.json.JSONObject;

/**
 * 封装电子表单控件,合并信息项字段属性
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-11-11 上午08:47:15
 * @version  2.0.7
 * @classpath com.strongit.oa.common.eform.model.EFormComponent
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class EFormComponent {

	private String top;						//控件顶部坐标
	
	private String left;					//控件左边坐标
	
	private String height;					//控件高度
	
	private String width;					//控件宽度
	
	private String caption;					//控件标题
	
	private boolean visible;				//控件是否可见
	
	private boolean readonly;				//控件是否只读
	
	private boolean required;				//控件是否必填
	
	private String tableName;				//控件绑定的表名
	
	private String fieldName;				//控件绑定的字段名
	
	private String fileNameField;			//附件绑定的字段名
	
	private boolean isPK;					//控件绑定的字段是否是主键字段
	
	private String tabOrder;                //控件的TabOrder属性
	
	private String sequenceName;			//控件序列名称
	
	private String items;					//下拉列表自定义数据,格式为分号隔开，如果不是自定义数据则为：tlDropCodeNameInput
	
	private String selTableName;			//下拉列表对应的表名（一般是数据字典表名）
	
	private String selCode;					//下拉列表编码字段
	
	private String selName;					//下拉列表名称字段
	
	private String selInputCode;			//下拉列表输入码字段
	
	private String selFilter;				//下拉列表过滤的SQL语句
	
	private String type;					//控件类型
	
	private String name;					//控件名称
	
	private String lable;					//控件绑定字段对应的信息项别名
	
	private long number;					//控件绑定字段对应的信息项排序号
	
	private String value;					//控件绑定字段对应的值
	
	private String valueType;				//控件绑定字段对应信息项类型
	
	private String dateFormat;				//日期控件格式化形式 默认为：yyyy-MM-dd
	
	private int columnType;					//控件所绑定字段对应的SQL类型
	
	private String columnTypeName;			//控件所绑定字段对应的SQL类型名称
	
	private String columnClassName;			//控件所绑定字段对应的Java类型名称

	public EFormComponent toBean(FormTemplateComponent formTemplateComponent) {
		if(formTemplateComponent != null) {
			this.setType(formTemplateComponent.Type);							//控件类型
			this.setName(formTemplateComponent.Name);							//控件名称
			Map<String,String> properties = formTemplateComponent.Properties;	//控件属性
			/*if(properties.get("Content") != null) {
				this.setCaption(properties.get("Content"));
			} else {
				this.setCaption(properties.get("Caption"));						//控件标题				
			}*/
			this.setCaption(properties.get("Caption"));							//控件标题	
			this.setVisible(Boolean.parseBoolean(properties.get("Visible"))); 	//控件是否可见
			this.setReadonly(Boolean.parseBoolean(properties.get("ReadOnly"))); //控件是否只读
			this.setRequired(properties.get("Required"));						//控件是否必填
			this.setTableName(properties.get("TableName"));						//控件绑定的表名称
			this.setFieldName(properties.get("FieldName"));						//控件对应字段名称
			this.setFileNameField(properties.get("FileNameField"));             //附件对应字段名称 
			this.setPK(properties.get("IsPK"));									//控件是否是主键
			this.setSelTableName(properties.get("ListTableName"));				//下拉控件绑定的表名称
			this.setSelCode(properties.get("ListCodeName"));					//下拉控件绑定的字段编码
			this.setSelName(properties.get("ListShowName"));					//下拉控件绑定的字段显示名称
			this.setSelInputCode(properties.get("ListInputName"));				//下拉控件绑定的字段输入码
			this.setSelFilter(properties.get("ListFilter"));					//下拉控件绑定的字段过滤条件
			this.setItems(properties.get("AdvancedFormListItems"));				//下拉控件自定义项
			this.setDateFormat(properties.get("DateFormat"));					//日期控件格式
			//this.setType(properties.get("FieldType"));						//控件明细类型
			if(properties.get("TabOrder") != null ){
				this.setTabOrder(properties.get("TabOrder"));
			}
			
		}
		return this;
	}
	
	public String toString() {
		/*return new StringBuilder("{top=").append(top).append(",left=")
			.append(left).append(",height=").append(height).append(",width=")
			.append(width).append(",caption=").append(caption).append(",visible=")
			.append(visible).append(",readonly=").append(readonly).append(",required=")
			.append(required).append(",tableName=").append(tableName).append(",fieldName=").append(fieldName)
			.append(",isPK=").append(isPK).append(",sequenceName=").append(sequenceName)
			.append(",selTableName=").append(selTableName).append(",selCode=").append(selCode)
			.append(",selName=").append(selName).append(",selInputCode=").append(selInputCode)
			.append(",selFilter=").append(selFilter).append(",type=").append(type).append(",name=")
			.append(name).append(",lable=").append(lable).append(",number=").append(number).append(",value=")
			.append(value).append(",items=").append(items).append(",valueType=").append(valueType).append("}").toString(); */ 
		return JSONObject.fromObject(this).toString();
	}
	
	public EFormComponent(String top, String left, String height, String width, String caption, boolean visible, boolean readonly, boolean required, String tableName, String fieldName,String fileNameField, boolean isPK, String tabOrder,String sequenceName, String selTableName, String selCode, String selName, String selInputCode, String selFilter, String type, String name, String lable, long number, String value) {
		this.top = top;
		this.left = left;
		this.height = height;
		this.width = width;
		this.caption = caption;
		this.visible = visible;
		this.readonly = readonly;
		this.required = required;
		this.tableName = tableName;
		this.fieldName = fieldName;
		this.fileNameField = fileNameField;
		this.isPK = isPK;
		this.tabOrder = tabOrder;
		this.sequenceName = sequenceName;
		this.selTableName = selTableName;
		this.selCode = selCode;
		this.selName = selName;
		this.selInputCode = selInputCode;
		this.selFilter = selFilter;
		this.type = type;
		this.name = name;
		this.lable = lable;
		this.number = number;
		this.value = value;
	}

	public EFormComponent(){
		
	}
	
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getFileNameField() {
		return fileNameField;
	}

	public void setFileNameField(String fileNameField) {
		this.fileNameField = fileNameField;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public boolean isPK() {
		return isPK;
	}

	public void setPK(String isPK) {
		if("Y".equals(isPK)) {
			this.isPK = true;
		}else{
			this.isPK = false;
		}
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(String required) {
		if("Y".equals(required)) {
			this.required = true;
		}else{
			this.required = false;
		}
	}

	public String getSelCode() {
		return selCode;
	}

	public void setSelCode(String selCode) {
		this.selCode = selCode;
	}

	public String getSelFilter() {
		return selFilter;
	}

	public void setSelFilter(String selFilter) {
		this.selFilter = selFilter;
	}

	public String getSelInputCode() {
		return selInputCode;
	}

	public void setSelInputCode(String selInputCode) {
		this.selInputCode = selInputCode;
	}

	public String getSelName() {
		return selName;
	}

	public void setSelName(String selName) {
		this.selName = selName;
	}

	public String getSelTableName() {
		return selTableName;
	}

	public void setSelTableName(String selTableName) {
		this.selTableName = selTableName;
	}

	public String getTabOrder() {
		return tabOrder;
	}

	public void setTabOrder(String tabOrder) {
		this.tabOrder = tabOrder;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getColumnClassName() {
		return columnClassName;
	}

	public void setColumnClassName(String columnClassName) {
		this.columnClassName = columnClassName;
	}

	public int getColumnType() {
		return columnType;
	}

	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}

	public String getColumnTypeName() {
		return columnTypeName;
	}

	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		if(dateFormat == null || "".equals(dateFormat)) {
			dateFormat = "yyyy-MM-dd";
		}
		this.dateFormat = dateFormat;
	}
	
}
