// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 2009-04-28 下午 04:28:44
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AuthorizeTag.java

package com.strongit.tag.security;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;

public class AuthorizeTag extends TagSupport
{

	private BaseOptPrivilManager manager;
    private String ifAllGranted;
    private String ifAnyGranted;
    private String ifNotGranted;
	
    public void setManager(BaseOptPrivilManager manager) {
		this.manager = manager;
	}

	public AuthorizeTag()
    {
        ifAllGranted = "";
        ifAnyGranted = "";
        ifNotGranted = "";      
    }

    public int doStartTag()
        throws JspException
    {
    	  ServletContext servletContext = pageContext.getServletContext();
          ApplicationContext appcontext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
          manager = (BaseOptPrivilManager)appcontext.getBean("baseOptPrivilManager");
    	/**如果未设置任何权限，则返回不通过*/
        if((null == ifAllGranted || "".equals(ifAllGranted)) && (null == ifAnyGranted || "".equals(ifAnyGranted)) && (null == ifNotGranted || "".equals(ifNotGranted)))
            return 0;
       
        /**如果有一个模块有权限，则返回不通过，否则通过*/
        if(null != ifNotGranted && !"".equals(ifNotGranted) && ifNotGranted.length()>0)
        {
            /**获取设置的否权限*/
            String[] evaledIfNotGranted = ifNotGranted.split(",");
        	for(int i=0;i<evaledIfNotGranted.length;i++){
        		boolean flag = manager.checkPrivilBySysCode(filterSpace(evaledIfNotGranted[i]));
        		if(flag)
        			return 0;
        	}
        }
        
        
        /**如果有一个模块没有权限，则返回不通过，否则通过*/
        if(null != ifAllGranted && !"".equals(ifAllGranted) && ifAllGranted.length()>0)
        {
        	/**获取设置的与权限*/
            String[] evaledIfAllGranted = ifAllGranted.split(",");
        	for(int i=0;i<evaledIfAllGranted.length;i++){
        		boolean flag = manager.checkPrivilBySysCode(filterSpace(evaledIfAllGranted[i]));
        		if(!flag)
        			return 0;
        	}
        }
        
        /**如果有一个模块有权限，则返回通过，否则不通过*/
        if(null != ifAnyGranted && !"".equals(ifAnyGranted) && ifAnyGranted.length()>0)
        {
            /**获取设置的或权限*/
            String[] evaledIfAnyGranted = ifAnyGranted.split(",");
        	for(int i=0;i<evaledIfAnyGranted.length;i++){
        		boolean flag = manager.checkPrivilBySysCode(filterSpace(evaledIfAnyGranted[i]));
        		if(flag)
        			return 1;
        	}
        	return 0;
        }
        return 1;
    }
    
    private String filterSpace(String strToFilter) {
        if (strToFilter == null || strToFilter.trim().length() == 0) {
          return "";
        }
        String returnString = "";
        returnString = strToFilter.replaceAll(" ", "");
        returnString = returnString.replaceAll("&nbsp;", "");
        return returnString;
      }

    public String getIfAllGranted()
    {
        return ifAllGranted;
    }

    public String getIfAnyGranted()
    {
        return ifAnyGranted;
    }

    public String getIfNotGranted()
    {
        return ifNotGranted;
    }

    public void setIfAllGranted(String ifAllGranted)
        throws JspException
    {
        this.ifAllGranted = ifAllGranted;
    }

    public void setIfAnyGranted(String ifAnyGranted)
        throws JspException
    {
        this.ifAnyGranted = ifAnyGranted;
    }

    public void setIfNotGranted(String ifNotGranted)
        throws JspException
    {
        this.ifNotGranted = ifNotGranted;
    }
}