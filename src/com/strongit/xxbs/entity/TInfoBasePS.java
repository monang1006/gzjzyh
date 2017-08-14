package com.strongit.xxbs.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the T_INFO_BASE_P_S database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_P_S")
public class TInfoBasePS implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="PS_ID")
	private String psId;

	@Column(name="SC_ID")
	private String scId;

	//bi-directional many-to-one association to TInfoBasePublish
    @ManyToOne
	@JoinColumn(name="PUB_ID")
	private TInfoBasePublish TInfoBasePublish;

    public TInfoBasePS() {
    }

	public String getPsId() {
		return this.psId;
	}

	public void setPsId(String psId) {
		this.psId = psId;
	}

	public String getScId() {
		return this.scId;
	}

	public void setScId(String scId) {
		this.scId = scId;
	}

	public TInfoBasePublish getTInfoBasePublish() {
		return this.TInfoBasePublish;
	}

	public void setTInfoBasePublish(TInfoBasePublish TInfoBasePublish) {
		this.TInfoBasePublish = TInfoBasePublish;
	}
	
}