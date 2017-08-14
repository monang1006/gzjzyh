package com.strongit.xxbs.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.strongmvc.service.ServiceLocator;

public class OAPublish extends HttpServlet{
	public void init() throws ServletException
	{
		super.init();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		HelloWorld helloWorld = (HelloWorld) ServiceLocator.getService("helloWorldImpl");
		String strDate = request.getParameter("strDate");
		String endDate = request.getParameter("endDate");
		String tXML = "";
		try {
			tXML = helloWorld.getPublishs(strDate,endDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType("text/xml; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.print(tXML);
	}
}
