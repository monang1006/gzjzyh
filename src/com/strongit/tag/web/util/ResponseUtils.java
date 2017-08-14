// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 2008-11-06 23:41:24
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) definits fieldsfirst noinners nonlb lnc safe 
// Source File Name:   ResponseUtils.java

package com.strongit.tag.web.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


public final class ResponseUtils {

	private static Log log = null;

	private ResponseUtils() {
	}

	public static String filter(String value) {
		if (value == null)
			return null;
		char content[] = new char[value.length()];
		value.getChars(0, value.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);
		for (int i = 0; i < content.length; i++)
			switch (content[i]) {
			case 60: // '<'
				result.append("&lt;");
				break;

			case 62: // '>'
				result.append("&gt;");
				break;

			case 38: // '&'
				result.append("&amp;");
				break;

			case 34: // '"'
				result.append("&quot;");
				break;

			case 39: // '\''
				result.append("&#39;");
				break;

			default:
				result.append(content[i]);
				break;
			}

		return result.toString();

	}

	public static void write(PageContext pageContext, String text)
			throws JspException {
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(text);
		} catch (IOException e) {
			throw new JspException("");
		}
	}

	public static void writePrevious(PageContext pageContext, String text)
			throws JspException {
		JspWriter writer = pageContext.getOut();
		if (writer instanceof BodyContent)
			writer = ((BodyContent) writer).getEnclosingWriter();
		try {
			writer.print(text);
		} catch (IOException e) {
			throw new JspException("");
		}
	}

	public static void main(String args[]) {
		ResponseUtils
				.filter("<html><head><title>dddddd</title></head><body>大家�?</body></html>");
	}

    static  {
    	log=LogFactory.getLog(com.strongit.tag.web.util.ResponseUtils.class);
    }
}