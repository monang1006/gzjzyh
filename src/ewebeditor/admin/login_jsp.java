package ewebeditor.admin;

import java.util.*;
import java.util.regex.*;
import java.text.*;
import java.io.*;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;


public class login_jsp
{
		
	private HttpServletRequest m_request;
	private HttpServletResponse m_response;
	private HttpSession m_session;
	private ServletContext m_application;
	private PageContext m_pagecontext;
	private PrintWriter out; 

	private ewebeditor.admin.util myUtil;


	public login_jsp(){
		myUtil = new ewebeditor.admin.util();
	}



	public final void Load(PageContext pagecontext) throws ServletException,IOException{
		m_pagecontext = pagecontext;
		m_application = pagecontext.getServletContext();
		m_request = (HttpServletRequest)pagecontext.getRequest();
		m_response = (HttpServletResponse)pagecontext.getResponse();
		m_session = (HttpSession)pagecontext.getSession();
		out = m_response.getWriter();

		
		boolean b = myUtil.InitAdmin(pagecontext, false);

		if (myUtil.sAction.equals("LOGIN")){

			String s_Usr = myUtil.dealNull(m_request.getParameter("usr"));
			String s_Pwd = myUtil.dealNull(m_request.getParameter("pwd"));
			if (s_Usr.equals(myUtil.sUsername) && s_Pwd.equals(myUtil.sPassword)) {
				m_session.setAttribute("eWebEditor_User", "OK");
				m_response.sendRedirect("default.jsp");
				return;
			}
		} else if (myUtil.sAction.equals("OUT")){
			m_session.setAttribute("eWebEditor_User", "");
		}


	}



}
