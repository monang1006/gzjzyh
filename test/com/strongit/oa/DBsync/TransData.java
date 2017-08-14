package com.strongit.oa.DBsync;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TransData {
	private String url;

	private String userName;

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public TransData() {
//		Properties properties = new Properties();
//		try {
//			properties.load(new FileInputStream("setting.properties"));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		url = properties.getProperty("jdbc.url");
//		userName = properties.getProperty("jdbc.username");
//		password = properties.getProperty("jdbc.password");
		url = "jdbc:oracle:thin:@14.0.0.10:1521:orcl";
		userName = "oa_bgt";
		password = "password";
	}
}
