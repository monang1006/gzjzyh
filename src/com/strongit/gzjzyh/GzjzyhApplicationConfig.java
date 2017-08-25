package com.strongit.gzjzyh;

public class GzjzyhApplicationConfig {
	
	public static String deployType;
	public static String syncUrl;
	public static String syncflag;
	public static String handleflag;

	public static String getDeployType() {
		return deployType;
	}

	public void setDeployType(String deployType) {
		GzjzyhApplicationConfig.deployType = deployType;
	}
	
	public static boolean isDistributedDeployed(){
		return "1".equals(GzjzyhApplicationConfig.deployType);
	}

	public static String getSyncUrl() {
		return syncUrl;
	}

	public void setSyncUrl(String syncUrl) {
		GzjzyhApplicationConfig.syncUrl = syncUrl;
	}

	public static String getSyncflag() {
		return syncflag;
	}

	public void setSyncflag(String syncflag) {
		GzjzyhApplicationConfig.syncflag = syncflag;
	}

	public static String getHandleflag() {
		return handleflag;
	}

	public void setHandleflag(String handleflag) {
		GzjzyhApplicationConfig.handleflag = handleflag;
	}

}
