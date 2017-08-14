package com.engine.tag.gettag;

import javax.servlet.http.HttpSession;

import com.engine.tag.Tag;
import com.engine.util.Define;


public class ContextPath extends Tag { 
	//<GET ContextPath()>@上下文@ @用户名@</GET>
	public String parse() {
		StringBuffer returnHtml=new StringBuffer();
		String temp = "";
		String contextPath = (String)requestMap.get(Define.WEB_CONTEXT_PATH);
		
		HttpSession session = (HttpSession) requestMap.get(Define.WEB_SESSION);
		String userName = "";
		//认证原文
		String randNum = generateRandomNum();
		session.setAttribute("original_data", randNum);
		
		if(session != null){
			try{	
				if(session.getAttribute("userName") != null){				
					userName = session.getAttribute("userName").toString();				
				}			
			}catch(Exception e){
				System.out.println("已经创建新的session!");
			}
		}
		temp = this.htmlContent;
		temp = temp.replaceAll(quote("@上下文@"),quoteReplacement(contextPath));
		temp = temp.replaceAll(quote("@用户名@"),quoteReplacement(userName));
		temp = temp.replaceAll(quote("@认证原文@"),quoteReplacement(randNum));
		returnHtml.append(temp);
		return returnHtml.toString();
	}
	
	/**
	 * 产生认证原文
	 */
	private String generateRandomNum() {
		String num = "1234567890abcdefghijklmnopqrstopqrstuvwxyz";
		int size = 6;
		char[] charArray = num.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb
					.append(charArray[((int) (Math.random() * 10000) % charArray.length)]);
		}
		return sb.toString();
	}
}
