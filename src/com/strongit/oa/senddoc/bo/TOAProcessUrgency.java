package com.strongit.oa.senddoc.bo;

import com.strongit.oa.bo.TProcessUrgency;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         May 18, 2012 4:18:05 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.senddoc.bo.TOAProcessUrgency
 */
public class TOAProcessUrgency extends TProcessUrgency {
	private String showUrgencyDate;

	private String urgencyerName;

	private String urgencyederName;

	public String getShowUrgencyDate() {
		return showUrgencyDate;
	}

	public void setShowUrgencyDate(String showUrgencyDate) {
		this.showUrgencyDate = showUrgencyDate;
	}

	public String getUrgencyederName() {
		return urgencyederName;
	}

	public void setUrgencyederName(String urgencyederName) {
		this.urgencyederName = urgencyederName;
	}

	public String getUrgencyerName() {
		return urgencyerName;
	}

	public void setUrgencyerName(String urgencyerName) {
		this.urgencyerName = urgencyerName;
	}

}
