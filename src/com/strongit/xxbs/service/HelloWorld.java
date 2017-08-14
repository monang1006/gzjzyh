package com.strongit.xxbs.service;
import java.util.List;

import javax.jws.WebService;


public interface HelloWorld {
	public String getPublishs(String strDate ,String endDate)
			throws Exception;
	
	public String[] sayHi();
}