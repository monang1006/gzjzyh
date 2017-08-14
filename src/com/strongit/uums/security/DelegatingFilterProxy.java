package com.strongit.uums.security;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

/**
 * 排除对静态文件的过滤：js、css、和图片文件等.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-7-27 下午03:16:22
 * @version  2.0.2.3
 * @classpath com.strongit.uums.security.DelegatingFilterProxy
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class DelegatingFilterProxy extends
		org.springframework.web.filter.DelegatingFilterProxy {

	private static final String EXCLUDE_SUFFIXS_NAME = "excludeSuffixs";

	private static final String DEFAULT_EXCLUDE_SUFFIXS[] = { ".js", ".css",
			".jpg", ".gif" };

	private static final String DEFAULT_INCLUDE_SUFFIXS[] = { ".action", ".do" };


	private static String excludeSuffixs[] = null;
	
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		String as[];
		int k = (as = DEFAULT_INCLUDE_SUFFIXS).length;
		for (int i = 0; i < k; i++) {
			String suffix = as[i];
			if (path.endsWith(suffix))
				return false;
		}
		k = (as = excludeSuffixs).length;
		for (int j = 0; j < k; j++) {
			String suffix = as[j];
			if (path.toLowerCase().endsWith(suffix))
				return true;
		}

		return false;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Enumeration paramMap = request.getParameterNames();
		String method = httpRequest.getMethod();
		/*String url = httpRequest.getRequestURI();
		if(url.endsWith("jsp") || url.endsWith("action")) {
			logger.info("执行的方法为：" + method + ",url=" + url);
		}*/
		if(!"GET".equals(method) && !"POST".equals(method)) {
			logger.fatal("系统捕获的请求不是GET请求或者POST请求，请求者IP为：" + request.getRemoteHost() + "当前时间为：" + new Date());
			return ;
		}
		boolean isCreack = false;
		while(paramMap.hasMoreElements()){
			String param = (String)paramMap.nextElement();
			if(param.indexOf("\\u0023")!=-1){//视为系统攻击
				logger.info(param);
				HttpServletResponse httpResponse = (HttpServletResponse)response;
				logger.fatal("系统遭受攻击，攻击者IP地址为：" + request.getRemoteHost() + "当前时间为：" + new Date());
				httpResponse.sendError(500);
				isCreack = true;
				break ;
			}
		}
		if(!isCreack){
			if(shouldNotFilter(httpRequest)){
				filterChain.doFilter(new RequestWrapper(httpRequest), response);
			}else{
				super.doFilter(new RequestWrapper(httpRequest), response, filterChain);
			}			
		}
		
	}

	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		String excludeSuffixStr = getFilterConfig().getInitParameter(
				EXCLUDE_SUFFIXS_NAME);
		if (StringUtils.isNotBlank(excludeSuffixStr)) {
			excludeSuffixs = excludeSuffixStr.split(",");
			for (int i = 0; i < excludeSuffixs.length; i++)
				excludeSuffixs[i] = (new StringBuilder(".")).append(
						excludeSuffixs[i]).toString();

		} else {
			excludeSuffixs = DEFAULT_EXCLUDE_SUFFIXS;
		}
	}
}
