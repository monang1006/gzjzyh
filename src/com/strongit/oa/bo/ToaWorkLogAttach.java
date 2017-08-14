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
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
/**
 * @hibernate.class table="T_OA_WORK_LOG_ATTACH"
 * 
 */
@Entity
@Table(name = "T_OA_WORK_LOG_ATTACH")
public class ToaWorkLogAttach implements Serializable {

	private String workLogAttachId;

	private String attachId;

	private ToaWorkLog toaWorkLog;

	public ToaWorkLogAttach(String workLogAttachId, String attachId,
			ToaWorkLog toaWorkLog) {
		this.workLogAttachId = workLogAttachId;
		this.attachId = attachId;
		this.toaWorkLog = toaWorkLog;
	}

	public ToaWorkLogAttach() {
		
	}

	public ToaWorkLogAttach(String workLogAttachId, ToaWorkLog toaWorkLog) {
		this.workLogAttachId = workLogAttachId;
		this.toaWorkLog = toaWorkLog;
	}

	@Id
	@Column(name = "WORK_LOG_ATTACH_ID", nullable = false, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getWorkLogAttachId() {
		return this.workLogAttachId;
	}

	public void setWorkLogAttachId(String workLogAttachId) {
		this.workLogAttachId = workLogAttachId;
	}

	@Column(name = "ATTACH_ID", nullable = true)
	public String getAttachId() {
		return this.attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WORK_LOG_ID")
	public ToaWorkLog getToaWorkLog() {
		return this.toaWorkLog;
	}

	public void setToaWorkLog(ToaWorkLog toaWorkLog) {
		this.toaWorkLog = toaWorkLog;
	}

	public String toString() {
		return new ToStringBuilder(this).append("workLogAttachId", getAttachId())
				.toString();
	}

}
