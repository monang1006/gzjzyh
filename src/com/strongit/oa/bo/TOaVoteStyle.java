package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_OA_STYLE")
public class TOaVoteStyle implements Serializable {
	
	  @Id
	  @GeneratedValue	
	private String styleId;
	
	private String styleName;
	
	private String styleContent;

	public TOaVoteStyle(String styleId, String styleName,
			String styleContent) {
		super();
		this.styleId = styleId;
		this.styleName = styleName;
		this.styleContent = styleContent;

	}

	public TOaVoteStyle() {
	}
	  @Id
	  @Column(name="STYLE_ID", nullable=false, length=32)
	  @GeneratedValue(generator="hibernate-uuid")
	  @GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	  @Column(name="STYLE_NAME", nullable=false)
	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	 @Column(name="STYLE_CONTENT", nullable=true)		
	public String getStyleContent() {
		return styleContent;
	}

	public void setStyleContent(String styleContent) {
		this.styleContent = styleContent;
	}


}
