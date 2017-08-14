package com.strongit.oa.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {

	public static boolean isHttpEnabled(String httpUrl) {
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			int code = con.getResponseCode();
			if(code == 200) {
				return true;
			}
		} catch (java.net.ConnectException e) {
			System.err.println(e);
		} catch (MalformedURLException e) {
			System.err.println(e);
		} catch (IOException e) { 
			System.err.println(e);
		}
		return false;
	}
}
