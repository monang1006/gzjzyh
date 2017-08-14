package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/** 即时通讯软件配置表
 *        @hibernate.class
 *         table="T_OA_IMCONFIG"
 *     
*/
@Entity
@Table(name = "T_OA_IMCONFIG", catalog = "", schema = "")
public class ToaImConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2203719214321931872L;
	
	private String id ;							//主键id
	
	private String imconfigIp ;					//服务器Ip
	
	private String imconfigPort ;				//服务器端口
	
	private String imconfigUrl ;				//服务请求地址
	
	private String imconfigClientPort ;			//客户端登录端口
	
	private String imconfigClassName ;			//服务实现类路径
	
	private String imconfigState ;				//服务状态：0：未启用，1：启用
	
	private String rest1 ;						//扩展字段1 已用于保存即时通讯软件名称
	
	private String rest2 ;						//扩展字段2 已用于保存即时通讯软件轮询器是否启用标示
	
	private String rest3 ;						//扩展字段3 已用于保存轮询频率
	
	private String rest4 ;						//扩展字段4

	public ToaImConfig(){
		
	}
	
	public ToaImConfig(String imconfigIp,String imconfigPort,
					   String imconfigUrl,String imconfigClientPort,
					   String imconfigClassName,String imconfigState){
		this.imconfigIp = imconfigIp;
		this.imconfigPort = imconfigPort ;
		this.imconfigUrl = imconfigUrl;
		this.imconfigClientPort = imconfigClientPort ;
		this.imconfigClassName = imconfigClassName ;
		this.imconfigState = imconfigState ;
	}
	
    @Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "IMCONFIG_CLASSNAME")
	public String getImconfigClassName() {
		return imconfigClassName;
	}

	public void setImconfigClassName(String imconfigClassName) {
		this.imconfigClassName = imconfigClassName;
	}

	@Column(name = "IMCONFIG_CLIENTPORT")
	public String getImconfigClientPort() {
		return imconfigClientPort;
	}

	public void setImconfigClientPort(String imconfigClientPort) {
		this.imconfigClientPort = imconfigClientPort;
	}

	@Column(name = "IMCONFIG_IP")
	public String getImconfigIp() {
		return imconfigIp;
	}

	public void setImconfigIp(String imconfigIp) {
		this.imconfigIp = imconfigIp;
	}

	@Column(name = "IMCONFIG_PORT")
	public String getImconfigPort() {
		return imconfigPort;
	}

	public void setImconfigPort(String imconfigPort) {
		this.imconfigPort = imconfigPort;
	}

	@Column(name = "IMCONFIG_STATE")
	public String getImconfigState() {
		return imconfigState;
	}

	public void setImconfigState(String imconfigState) {
		this.imconfigState = imconfigState;
	}

	@Column(name = "IMCONFIG_URL")
	public String getImconfigUrl() {
		return imconfigUrl;
	}

	public void setImconfigUrl(String imconfigUrl) {
		this.imconfigUrl = imconfigUrl;
	}

	@Column(name = "REST1")
	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2")
	public String getRest2() {
		return rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "REST3")
	public String getRest3() {
		return rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}

	@Column(name = "REST4")
	public String getRest4() {
		return rest4;
	}

	public void setRest4(String rest4) {
		this.rest4 = rest4;
	}

}
