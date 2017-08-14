/**
 * 设定匿名访问权限
 * @author 曹钦
 * @company Strongit Ltd. (c) copyright
 * @date Oct 12, 2012 22:13:48 
 * @version 1.0
 * @classpath com.strongit.platform.internationlization.InternationlizationFilter
 */
package com.strongit.oa.monitor;


import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;

import com.strongit.uums.security.UserInfo;

public class UserVisit implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		Date time = new Date();
		String urlPath = ((HttpServletRequest)request).getQueryString();
	    if ((urlPath == null) || ("".equals(urlPath)))
	      urlPath = ((HttpServletRequest)request).getServletPath();
	    else {
	      urlPath = ((HttpServletRequest)request).getServletPath() + "?" + urlPath;
	    }
	    Logger logger = LoggerFactory.getLogger(this.getClass());
	    logger.info("(begin)" + urlPath);
	    
		String root=((HttpServletRequest)request).getContextPath();
        if(root.equals("/")) root="";
		String visitUrl = ((HttpServletRequest)request).getServletPath();
		visitUrl = root+visitUrl;
		
		//访问控制
		String userName = null;
		try{
			HttpSession httpSession = ((HttpServletRequest)request).getSession();
			SecurityContext context = (SecurityContext)httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
			if(context!=null){
				Authentication authentication = context.getAuthentication();
				UserInfo userInfo = (UserInfo) authentication.getPrincipal();
				if(userInfo!=null){
					userName = userInfo.getUsername();
				}
			}
		}catch (Exception e) {}
		if(userName == null || "".equals(userName)){
			//非后台用户访问
			if(visitUrl.matches(root+"/index.htm") 
			   //后台登入
			   ||visitUrl.matches(root+"/login.jsp")
			   ||visitUrl.matches(root+"/oamain.jsp")
			   ||visitUrl.matches(root+"/index.jsp")
			   ||visitUrl.matches(root+"/common/css/css.css")
			   ||visitUrl.matches(root+"/common/images/welcome/.*?")
			    ||visitUrl.matches(root+"/common/js/commontab/workservice.js")
			    ||visitUrl.matches(root+"/common/js/commontab/service.js")
			    ||visitUrl.matches(root+"/theme/theme!login.action")
			    ||visitUrl.matches(root+"/message.html")
			    ||visitUrl.matches(root+"/im/iM!checkImStart.action")
			    ||visitUrl.matches(root+"/common/error/images/.*?")
			   ||visitUrl.matches(root+"/ipproot/.*?")
			   ||visitUrl.matches(root+"/captcha/captchaImage.action")
			   ||visitUrl.matches(root+"/j_spring_security_check")
			   ||visitUrl.matches(root+"/security/index.jsp")
			   ||visitUrl.matches(root+"/j_spring_security_logout")
			   //前台访问
			   ||visitUrl.matches(root+"/webstat")
			   ||visitUrl.matches(root+"/captcha.jpg")
			   ||visitUrl.matches(root+"/.*?.shtml.*?")
			   ||visitUrl.matches(root+"/resource/.*?")
			   ||visitUrl.matches(root+"/scheme/.*?")
			   ||visitUrl.matches(root+"/eWebEditor/plugin/.*?")
			   ||visitUrl.matches(root+"/eWebEditor/sysimage/.*?")
			   //资源不存在 404错误页面
			   ||visitUrl.matches(root+"/common/error/error.css")
			   ||visitUrl.matches(root+"/common/script/jquery-1.4.1.min.js")
			   //500错误页面
			   ||visitUrl.matches(root+"/common/error/images/error.jpg")
			   ||visitUrl.matches(root+"/common/error/images/error_bg_03.jpg")
			   //电子表单
			   //添加401页面
			     || (visitUrl.matches(root + "/common/error/401.jsp.*?"))
			   ||visitUrl.matches(root+"/FormDesigner.*?")
			   ||visitUrl.matches(root+"/FormReader.*?")
			   //首页
			   ||visitUrl.matches(root+"/ncbgt/.*?")
			   ||visitUrl.matches(root+"/resource/.*?")
			   ||visitUrl.matches(root+"/services.*?")
			   ||visitUrl.matches(root+"/wap/work.*?")
			   ||visitUrl.matches(root+"/servlet/androidDownload.*?")
			   ||visitUrl.matches(root+"/webservice/mobile.*?")
			   ||visitUrl.matches(root+"/message.jsp")
			   ||visitUrl.matches(root+"/frame/theme_gray/images.*?")
			   
			){
				chain.doFilter((HttpServletRequest)request, (HttpServletResponse)response);
			}
			else{
				boolean visit = false;
//					System.out.println("visitUrl:"+visitUrl);
					//受访问控制的页面
					if( visitUrl.matches(root+"/ipp/info/.*?")
						||visitUrl.matches(root+"/.*?/.*?action.*?")	
						||visitUrl.matches(root+"/fileNameRedirectAction.action.*?")	
						||visitUrl.matches(root+"/plugin/.*?")
						||visitUrl.matches(root+"/uums/.*?")
						||visitUrl.matches(root+"/workflow/.*?")
						||visitUrl.matches(root+"/tag/.*?")
						||visitUrl.matches(root+"/oa/message/.*?")
						||visitUrl.matches(root+"/security/.*?")
						||visitUrl.matches(root+"/upload.*?")
						||visitUrl.matches(root+"/upload-progress.*?")
						
						){
							RequestDispatcher dis = request.getRequestDispatcher(root+"/ipproot/isnotlogin.jsp");
							dis.forward(request, response);
					    }else{
					    	((HttpServletResponse)response).sendError(404);
					    }
				}
		}else{
			chain.doFilter((HttpServletRequest)request, (HttpServletResponse)response);
		}
		logger.info("(end"+((new Date()).getTime()-time.getTime())+")" + urlPath);
	}
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
