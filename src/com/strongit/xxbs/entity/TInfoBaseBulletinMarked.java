package com.strongit.xxbs.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the T_INFO_BASE_BULLETIN_MARKED database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_BULLETIN_MARKED")
public class TInfoBaseBulletinMarked implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="MK_ID")
	private String mkId;

	@Column(name="BL_ID")
	private String blId;

	@Column(name="USER_ID")
	private String userId;

    public TInfoBaseBulletinMarked() {
    }

	public String getMkId() {
		return this.mkId;
	}

	public void setMkId(String mkId) {
		this.mkId = mkId;
	}

	public String getBlId() {
		return this.blId;
	}

	public void setBlId(String blId) {
		this.blId = blId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}