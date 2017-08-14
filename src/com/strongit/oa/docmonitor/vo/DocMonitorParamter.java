package com.strongit.oa.docmonitor.vo;

import com.strongit.oa.docmonitor.util.DocMonitorUtil;
import com.strongit.oa.senddoc.pojo.ProcessedParameter;

/**
 * 参数处理类
 * 
 * @author yanjian
 * 
 * Sep 25, 2012 11:36:08 AM
 */
public class DocMonitorParamter extends ProcessedParameter {

	/**
	 * @field grantCode 授权id
	 */
	private int grantCode = DocMonitorUtil.GRANT_CODE_PERSONAL;

	private String grantOrgId = "-1";

	public int getGrantCode() {
		return grantCode;
	}

	public void setGrantCode(int grantCode) {
		this.grantCode = grantCode;
	}

	public String getGrantOrgId() {
		return grantOrgId;
	}

	public void setGrantOrgId(String grantOrgId) {
		this.grantOrgId = grantOrgId;
	}
}
