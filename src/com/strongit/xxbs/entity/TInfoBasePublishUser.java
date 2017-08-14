package com.strongit.xxbs.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the T_INFO_BASE_BULLETIN_MARKED database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_PUBLISH_USER")
public class TInfoBasePublishUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="ID")
	private String id;

	@Column(name="PUB_ID")
	private String pubId;

	@Column(name="USER_ID")
	private String userId;

    public TInfoBasePublishUser() {
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getPubId() {
		return pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}