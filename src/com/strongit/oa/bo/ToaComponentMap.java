/**
 * 
 */
package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.workflow.bo.TwfBaseProcessfile;

/**
 * @description 列表控件映射实体类
 * @className ToaComponentMap
 * @company Strongit Ltd. (C) copyright
 * @author 申仪玲
 * @email shenyl@strongit.com.cn
 * @created 2012-2-8 上午11:17:43
 * @version 3.0
 */

@Entity
@Table(name = "T_OA_COMPONENT_MAP", catalog = "", schema = "")
public class ToaComponentMap implements Serializable{
	/**
	* @fields serialVersionUID TODO()
	*/
	
	private static final long serialVersionUID = -1484159007454035666L;

	//主键
	private String mapId;
	
	private com.strongit.uums.bo.TUumsBasePrivil mapPrivil;  //系统资源(系统功能模块ID)

	private com.strongit.workflow.bo.TwfBaseProcessfile mapProcess; //流程
	
	private com.strongit.oa.bo.TEFormTemplate mapForm;     //列表控件表单(展现类型表单)
	
	private String mapType;        //列表控件用途类型(是查询列表还是展现列表)
	
	
	
	//空的构造函数
	public ToaComponentMap() {
		
	}
		
	/**
	* @构造函数
	* @description 描述
	* @param mapId
	* @param mapPrivil
	* @param mapProcess
	* @param mapForm
	* @param mapType
	*/
	public ToaComponentMap(String mapId, TUumsBasePrivil mapPrivil,
			TwfBaseProcessfile mapProcess, TEFormTemplate mapForm, String mapType) {
		this.mapId = mapId;
		this.mapPrivil = mapPrivil;
		this.mapProcess = mapProcess;
		this.mapForm = mapForm;
		this.mapType = mapType;
	}



	@Id
	@Column(name="MAP_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MAP_PRIVILID")
	public com.strongit.uums.bo.TUumsBasePrivil getMapPrivil() {
		return mapPrivil;
	}
	
	public void setMapPrivil(com.strongit.uums.bo.TUumsBasePrivil mapPrivil) {
		this.mapPrivil = mapPrivil;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MAP_PROCESSID")
	public com.strongit.workflow.bo.TwfBaseProcessfile getMapProcess() {
		return mapProcess;
	}

	public void setMapProcess(com.strongit.workflow.bo.TwfBaseProcessfile mapProcess) {
		this.mapProcess = mapProcess;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MAP_FORMID")
	public com.strongit.oa.bo.TEFormTemplate getMapForm() {
		return mapForm;
	}
	
	public void setMapForm(com.strongit.oa.bo.TEFormTemplate mapForm) {
		this.mapForm = mapForm;
	}
	
	@Column(name="MAP_TYPE")
	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}	

}
