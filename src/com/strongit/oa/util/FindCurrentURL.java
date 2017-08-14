package com.strongit.oa.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public final class FindCurrentURL {
	public String getCurrentURL(){
        
    	Properties properties = new Properties(); 
    	URL in = this.getClass().getClassLoader().getResource("appconfig.properties");
    	try {
			properties.load(new FileInputStream(in.getFile()));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		String URL =properties.getProperty("jndi.name");//获取jdbc.properties文件中hibernate.connection.url的值
		if(URL==null||URL.equals("")||URL.equals("null"))
			URL =properties.getProperty("jdbc.url");
		return URL;
    }
}
