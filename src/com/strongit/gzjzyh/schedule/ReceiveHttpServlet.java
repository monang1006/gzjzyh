package com.strongit.gzjzyh.schedule;

import com.strongmvc.exception.SystemException;
import com.strongmvc.service.ServiceLocator;
import com.alibaba.fastjson.JSONObject;
import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.tohandle.IToHandleManager;
import com.strongit.gzjzyh.util.ExceptionKit;
import com.strongit.gzjzyh.vo.Packet;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReceiveHttpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(ReceiveHttpServlet.class);

	// Initialize global variables
	public void init() throws ServletException {
	}

	// Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ServletInputStream inputStream = request.getInputStream();
			Exception exceptionProcess = null;
			if (GzjzyhConstants.GZJZYH_CONTENT_TYPE.equals(request.getContentType()) && inputStream != null) {
				InputStream is = request.getInputStream();
				BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = bufferedInputStream.read(buffer, 0, 1024)) > 0) {
					outputStream.write(buffer, 0, count);
				}
				String requestContent = new String(outputStream.toByteArray(), "utf-8");
				if (requestContent != null && !"".equals(requestContent)) {
					try {
						Packet requestPacket = JSONObject.parseObject(requestContent, Packet.class);
						IToHandleManager handleManager = (IToHandleManager) ServiceLocator
								.getService("toHandleManager");
						handleManager.handleMsg(requestPacket);
					} catch (SystemException ex) {
						exceptionProcess = ex;
					} catch (DAOException ex) {
						exceptionProcess = ex;
					} catch (ServiceException ex) {
						exceptionProcess = ex;
					} catch (Exception ex) {
						exceptionProcess = ex;
					}
					// 生成回复流
					response.setContentType(GzjzyhConstants.GZJZYH_CONTENT_TYPE);
					String responseMsg = GzjzyhConstants.HTTP_RESPONSE_SUCCESS;
					if (exceptionProcess != null) {
						responseMsg = ExceptionKit.getStackTrace(exceptionProcess);
						logger.error(ExceptionKit.getStackTrace(exceptionProcess));
					}
					ServletOutputStream os = response.getOutputStream();
					response.setCharacterEncoding("UTF-8");
					outputStream.write(responseMsg.getBytes("UTF-8"));
					os.close();
				}
			}
		} catch (Exception ex) {
			logger.warn(ex.getMessage(), ex);
			throw new ServletException(ex);
		}
	}

	// Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	// Clean up resources
	public void destroy() {
	}
}
