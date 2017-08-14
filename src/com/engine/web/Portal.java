package com.engine.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.engine.servlet.IEngineService;
import com.engine.util.Define;
import com.engine.util.ParameterHandle;
import com.strongit.doc.sends.util.IPConfiguration;
import com.strongmvc.service.ServiceLocator;

public class Portal extends HttpServlet {
	private String CONTENT_TYPE = "text/html; charset=UTF-8";
	public void init() throws ServletException {
		super.init();
	} 

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		ParameterHandle parameterHandle = new ParameterHandle(request);
		//获取提交参数
		Map paras = parameterHandle.getParameters();
		parameterHandle = null;
		//分析访问页面类型
		String shtmltype = request.getRequestURL().toString();
		shtmltype = replaceSlash(shtmltype);
		//截取模板类型
		if(shtmltype.lastIndexOf("/")!=-1)
			shtmltype = shtmltype.substring(shtmltype.lastIndexOf("/")+1);
        if(shtmltype.equals(""))
        	shtmltype = "index";
        else
            shtmltype = shtmltype.substring(0,shtmltype.lastIndexOf("."));
        if(shtmltype.equals("indexs"))
        {
        	String ip = request.getRemoteAddr();
        	IPConfiguration ips = new IPConfiguration();
			if(!(ip.trim().contains((ips.getValue("IP")).trim())
					))
			{
				shtmltype = "index";
			}
        }
        paras.put(Define.WEB_PAGE_TYPE,shtmltype);//页面类型
        
        paras.put(Define.WEB_SESSION, request.getSession());//访问者SESSION
        paras.put(Define.WEB_SITE_NAME, request.getServerName());//访问域名
        paras.put(Define.WEB_VISITOR_IP, request.getRemoteAddr());//访问者IP
        paras.put(Define.WEB_CONTEXT_PATH, request.getContextPath());//访问上下文
		//分析访问页码
		//获取解析Service
		IEngineService engineService = (IEngineService)ServiceLocator.getService("engineService");
		out.print(engineService.getParse(paras));
		out.close();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		//验证码验证
		IEngineService engineService = (IEngineService)ServiceLocator.getService("engineService");
		out.print(engineService.postParse());
		out.close();
	}	
	
	public String replaceSlash(String str){
		  String temp="";
		  int n=0;
		  for(int i = 0; i < str.length(); i ++){ 
			    if(str.charAt(i) == '\\'){
			    	temp = temp + str.substring(n,i)+"/";
			    	n = i+1;
			    }
			}
		  if(n<str.length())
			  temp = temp + str.substring(n);
		  return temp;
	}
}
