package com.strongit.oa.util;

import java.io.IOException;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;

import com.strongit.uums.security.UserInfo;

public class FilterJspPage implements Filter {

	public void destroy() {	
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain arg2) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.getContext();
		HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;
	    Authentication authentication = context.getAuthentication();
	    if(authentication == null){
	    	//System.out.println(request.getRealPath("/"));
	    	res.sendRedirect(req.getContextPath()+"/theme/theme!login.action");
	    }else{
	    	UserInfo userInfo = (UserInfo) authentication.getPrincipal();
	    	if(userInfo == null){
	    		res.sendRedirect(req.getContextPath()+"/theme/theme!login.action");
	    	}else{
	    		res.sendRedirect(req.getContextPath()+"/theme/theme!RefreshTop.action");
	    	}
	    	 
	    }
	   

	    
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
