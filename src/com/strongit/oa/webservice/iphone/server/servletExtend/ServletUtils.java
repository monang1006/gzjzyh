/**  
* @title: servletUtils.java
* @package com.strongit.oa.webservice.iphone.server.servletExtend
* @description: TODO
* @author  hecj
* @date Apr 14, 2014 11:13:08 AM
*/


package com.strongit.oa.webservice.iphone.server.servletExtend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaPrsnfldrFile;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.calendar.CalendarManager;
import com.strongit.oa.prsnfldr.privateprsnfldr.PrsnfldrFolderManager;
import com.strongit.oa.util.PathUtil;
import com.strongit.uums.webservice.AuthenticationHandler;
import com.strongmvc.exception.SystemException;

/**
 * @classname: servletUtils	
 * @author hecj
 * @date Apr 14, 2014 11:13:08 AM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.webservice.iphone.server.servletExtend
 * @update
 */
@Service("DBUtil")
public class ServletUtils {
	//个人文件柜
	@Autowired
	private PrsnfldrFolderManager prsnfldrFolderManager;
	/**
	 * 公文附件服务类
	 */
	@Autowired
	private IWorkflowAttachService workAttachService;
	
	@Autowired
	private IAttachmentService attachmentService;
	
	public ServletUtils(){
		
	}
	/**
	 * 文件柜附件获取
	 * @description
	 *
	 * @author  hecj
	 * @date    Apr 15, 2014 2:13:40 PM
	 * @param   
	 * @return  void
	 * @throws
	 */
	public void getDocAttachInfo(HttpServletRequest request,HttpServletResponse response, String attId){
		ToaPrsnfldrFile t = prsnfldrFolderManager.getFile(attId);
		try {
			response.setContentType("application/x-msdownload");
			String fileName=new String(t.getFileName().getBytes("gb2312"),"ISO8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
			response.getOutputStream().write(t.getFileContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取公文的附件
	 * @description
	 *
	 * @author  hecj
	 * @date    Apr 15, 2014 2:15:10 PM
	 * @param   
	 * @return  void
	 * @throws
	 */
	public void getWorkAttachInfo(HttpServletRequest request,HttpServletResponse response, String attId){
		WorkflowAttach attach = workAttachService.get(attId);
		try {
			response.setContentType("application/x-msdownload");
			String fileName=new String(attach.getAttachName().getBytes("gb2312"),"ISO8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=\""+fileName+"\"");
			response.getOutputStream().write(attach.getAttachContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取领导日程的附件
	 * @description
	 *
	 * @author  hecj
	 * @date    Apr 15, 2014 2:30:06 PM
	 * @param   
	 * @return  void
	 * @throws
	 */
	public void getCalendarAttachInfo(HttpServletRequest request,HttpServletResponse response, String attId){
		ToaAttachment attach = attachmentService.getAttachmentById(attId);
		try {
			response.setContentType("application/x-msdownload");
			String fileName=new String(attach.getAttachName().getBytes("gb2312"),"ISO8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=\""+fileName+"\"");
			response.getOutputStream().write(attach.getAttachCon());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取内部邮件的附件
	 * @description
	 *
	 * @author  hecj
	 * @date    Apr 15, 2014 2:30:06 PM
	 * @param   
	 * @return  void
	 * @throws
	 */
	public void getMessageAttachInfo(HttpServletRequest request,HttpServletResponse response, String attId){
		ToaAttachment attach = attachmentService.getAttachmentById(attId);
		try {
			response.setContentType("application/x-msdownload");
			String fileName=new String(attach.getAttachName().getBytes("gb2312"),"ISO8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=\""+fileName+"\"");
			response.getOutputStream().write(attach.getAttachCon());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
