package com.strongit.oa.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_OA_TRACE_DOC")
public class ToaTraceDoc {

	private static final long serialVersionUID = 6847769209111277965L;

	private String traceId; // TRACE_ID

	private String traceIntenceId;// TRACE_INTENCEID

	private String traceProcessName;// TRACE_PROCESSNAME

	private String traceStartUserName;// TRACE_STARTUSERNAME

	private String traceProcessTitle;// TRACE_PROCESSTITLE

	private String traceUserId;// TRACE_USERID

	private Date traceDate;// TRACE_DATE

	private String rest1;// REST1

	private String rest2;// REST2

	public ToaTraceDoc() {

	}

	@Id
	@Column(name = "TRACE_ID", nullable = true, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
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

	@Column(name = "TRACE_DATE")
	public Date getTraceDate() {
		return traceDate;
	}

	public void setTraceDate(Date traceDate) {
		this.traceDate = traceDate;
	}

	@Column(name = "TRACE_INTENCEID")
	public String getTraceIntenceId() {
		return traceIntenceId;
	}

	public void setTraceIntenceId(String traceIntenceId) {
		this.traceIntenceId = traceIntenceId;
	}

	@Column(name = "TRACE_PROCESSNAME")
	public String getTraceProcessName() {
		return traceProcessName;
	}

	public void setTraceProcessName(String traceProcessName) {
		this.traceProcessName = traceProcessName;
	}

	@Column(name = "TRACE_PROCESSTITLE")
	public String getTraceProcessTitle() {
		return traceProcessTitle;
	}

	public void setTraceProcessTitle(String traceProcessTitle) {
		this.traceProcessTitle = traceProcessTitle;
	}

	@Column(name = "TRACE_STARTUSERNAME")
	public String getTraceStartUserName() {
		return traceStartUserName;
	}

	public void setTraceStartUserName(String traceStartUserName) {
		this.traceStartUserName = traceStartUserName;
	}

	@Column(name = "TRACE_USERID")
	public String getTraceUserId() {
		return traceUserId;
	}

	public void setTraceUserId(String traceUserId) {
		this.traceUserId = traceUserId;
	}

}
