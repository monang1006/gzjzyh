/**  
* @title: DownloadServlet.java
* @package com.strongit.oa.webservice.iphone.server.servletExtend
* @description: TODO
* @author  hecj
* @date Apr 14, 2014 10:48:17 AM
*/


package com.strongit.oa.webservice.iphone.server.servletExtend;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.strongit.uums.webservice.AuthenticationHandler;
import com.strongmvc.service.ServiceLocator;

/**
 * @classname: DownloadServlet	
 * @author hecj
 * @date Apr 14, 2014 10:48:17 AM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.webservice.iphone.server.servletExtend
 * @update
 */
public class DownloadServlet extends HttpServlet{
	/**
	 * 
	 * moduleNo
	 * 		1:待办 2:公文 3:文件柜 4:领导日程 5:邮件 6:通知公告
	 * Overriding method
	 * @description
	 *
	 * @author  hecj
	 * @date    Apr 14, 2014 3:25:21 PM
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @return  
	 * @throws
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException{
		ServletUtils utils=(ServletUtils)ServiceLocator.getService("DBUtil");
		String sessionId=request.getParameter("sessionId");
		//验证用户
		if(AuthenticationHandler.getUserInfo(sessionId)==null){
			//return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
		} 
		/**
		 * moduleNo
		 * 1:待办 2:公文 3:文件柜 4:领导日程 5:邮件 6:通知公告
		 */
		String moduleNo=request.getParameter("moduleNo");
		if(moduleNo!=null&&!"".equals(moduleNo)){
			String attId=request.getParameter("id");
			if("1".equals(moduleNo)){
				utils.getWorkAttachInfo(request,response, attId);
			}else if("2".equals(moduleNo)){
				utils.getWorkAttachInfo(request,response, attId);
			}else if("3".equals(moduleNo)){
				utils.getDocAttachInfo(request,response, attId);
			}else if("4".equals(moduleNo)){
				utils.getCalendarAttachInfo(request,response, attId);
			}else if("5".equals(moduleNo)){
				utils.getMessageAttachInfo(request,response, attId);
			}else if("6".equals(moduleNo)){
				//暂时没有附件
			}
		}
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException{
		ServletUtils utils=(ServletUtils)ServiceLocator.getService("DBUtil");
		String sessionId=request.getParameter("sessionId");
		//验证用户
		if(AuthenticationHandler.getUserInfo(sessionId)==null){
			//return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
		} 
		/**
		 * moduleNo
		 * 1:待办 2:公文 3:文件柜 4:领导日程 5:邮件 6:通知公告
		 */
		String moduleNo=request.getParameter("moduleNo");
		if(moduleNo!=null&&!"".equals(moduleNo)){
			String attId=request.getParameter("id");
			if("1".equals(moduleNo)){
				utils.getWorkAttachInfo(request,response, attId);
			}else if("2".equals(moduleNo)){
				utils.getWorkAttachInfo(request,response, attId);
			}else if("3".equals(moduleNo)){
				utils.getDocAttachInfo(request,response, attId);
			}else if("4".equals(moduleNo)){
				utils.getCalendarAttachInfo(request,response, attId);
			}else if("5".equals(moduleNo)){
				utils.getMessageAttachInfo(request,response, attId);
			}else if("6".equals(moduleNo)){
				//暂时没有附件
			}
		}
	}
}
