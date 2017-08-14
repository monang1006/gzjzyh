package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 流程定义插件BO类.实现方式是通过和现有流程定义作弱关联.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2011-2-8 下午04:41:40
 * @version  3.0
 * @classpath com.strongit.oa.bo.ToaDefinitionPlugin
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Entity
@Table(name="T_OA_DEFINITIONPLUGIN")
public class ToaDefinitionPlugin {

	@Id
	@GeneratedValue
	private String definitionPluginId;						//插件id
	
	private String definitionPluginName;					//插件名称
	
	private String definitionPluginValue;					//插件值
	
	private String definitionPluginWorkflowId;				//插件对应的流程定义id
	
	private String definitionPluginWorkflowName;			//插件对应的流程定义名称

	public ToaDefinitionPlugin() {
		
	}
	
	public ToaDefinitionPlugin(String definitionPluginName,String definitionPluginValue,
							   String definitionPluginWorkflowId,String definitionPluginWorkflowName) {
		this.definitionPluginName = definitionPluginName;
		this.definitionPluginValue = definitionPluginValue;
		this.definitionPluginWorkflowId = definitionPluginWorkflowId;
		this.definitionPluginWorkflowName = definitionPluginWorkflowName;
	}
	
	@Id
	@Column(name="DEFINITIONPLUGIN_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getDefinitionPluginId() {
		return definitionPluginId;
	}

	public void setDefinitionPluginId(String definitionPluginId) {
		this.definitionPluginId = definitionPluginId;
	}

	@Column(name = "DEFINITONPLUGIN_NAME")
	public String getDefinitionPluginName() {
		return definitionPluginName;
	}

	public void setDefinitionPluginName(String definitionPluginName) {
		this.definitionPluginName = definitionPluginName;
	}

	@Lob
	@Column(name = "DEFINITIONPLUGIN_VALUE")
	public String getDefinitionPluginValue() {
		return definitionPluginValue;
	}

	public void setDefinitionPluginValue(String definitionPluginValue) {
		this.definitionPluginValue = definitionPluginValue;
	}

	@Column(name = "DEFINITIONPLUGIN_WORKFLOWID")
	public String getDefinitionPluginWorkflowId() {
		return definitionPluginWorkflowId;
	}

	public void setDefinitionPluginWorkflowId(String definitionPluginWorkflowId) {
		this.definitionPluginWorkflowId = definitionPluginWorkflowId;
	}

	@Column(name = "DEFINITIONPLUGIN_WORKFLOWNAME")
	public String getDefinitionPluginWorkflowName() {
		return definitionPluginWorkflowName;
	}

	public void setDefinitionPluginWorkflowName(String definitionPluginWorkflowName) {
		this.definitionPluginWorkflowName = definitionPluginWorkflowName;
	}
	
	public String toString() {
		return new StringBuilder("{definitionPluginName=")
			.append(this.getDefinitionPluginName())
			.append(",definitionPluginValue=")
			.append(this.getDefinitionPluginValue())
			.append(",definitionPluginWorkflowId=")
			.append(this.getDefinitionPluginWorkflowId())
			.append(",definitionPluginWorkflowName=")
			.append(this.getDefinitionPluginWorkflowName())
			.append("}") 
			.toString();
	}
	
}
