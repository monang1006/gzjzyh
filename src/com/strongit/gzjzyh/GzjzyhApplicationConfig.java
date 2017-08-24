package com.strongit.gzjzyh;

public class GzjzyhApplicationConfig {
	
	private static String deployType;
	private static String syncUrl;

	public static String getDeployType() {
		return deployType;
	}

	public static void setDeployType(String deployType) {
		GzjzyhApplicationConfig.deployType = deployType;
	}
	
	public static boolean isDistributedDeployed(){
		return "1".equals(GzjzyhApplicationConfig.deployType);
	}

	public static String getSyncUrl() {
		return syncUrl;
	}

	public static void setSyncUrl(String syncUrl) {
		GzjzyhApplicationConfig.syncUrl = syncUrl;
	}

}
