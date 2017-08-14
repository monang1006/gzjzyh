package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 会议类型实体类
* 类功能说明 
* 类修改者 修改日期 
* 修改说明 
* <p>Title: TSwConfersort.java</p> 
* <p>Description:省委会务系统</p> 
* <p>Copyright: Copyright (c) 2006</p> 
* <p>Company:思创数码科技股份有限公司</p> 
* @author Jianggb 
* @date 2013-3-21 下午05:14:43 
* @version V1.0
 */
@Entity
@Table(name = "T_SW_CONFERSORT", schema = "")
public class TSwConfersort implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String confersortId;
	private String confersortName;
	private String confersortDemo;
	private String rest1;
	private String rest2;
	private String rest3;

	// Constructors
	/** default constructor */
	public TSwConfersort() {}

	/** minimal constructor */
	public TSwConfersort(String confersortId) {
		this.confersortId = confersortId;
	}

	/** full constructor */
	public TSwConfersort(String confersortId, String confersortName, String confersortDemo, String rest1, String rest2,
		String rest3) {
		this.confersortId = confersortId;
		this.confersortName = confersortName;
		this.confersortDemo = confersortDemo;
		this.rest1 = rest1;
		this.rest2 = rest2;
		this.rest3 = rest3;
	
	}

	// Property accessors
	@Id
	@Column(name = "CONFERSORT_ID", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getConfersortId() {
		return this.confersortId;
	}

	public void setConfersortId(String confersortId) {
		this.confersortId = confersortId;
	}

	@Column(name = "CONFERSORT_NAME", length = 50)
	public String getConfersortName() {
		return this.confersortName;
	}

	public void setConfersortName(String confersortName) {
		this.confersortName = confersortName;
	}

	@Column(name = "CONFERSORT_DEMO", length = 500)
	public String getConfersortDemo() {
		return this.confersortDemo;
	}

	public void setConfersortDemo(String confersortDemo) {
		this.confersortDemo = confersortDemo;
	}

	@Column(name = "REST1", length = 10)
	public String getRest1() {
		return this.rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2", length = 10)
	public String getRest2() {
		return this.rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "REST3", length = 10)
	public String getRest3() {
		return this.rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}
		
	
}