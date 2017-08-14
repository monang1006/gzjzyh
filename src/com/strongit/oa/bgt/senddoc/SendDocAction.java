package com.strongit.oa.bgt.senddoc;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.attachment.AttachmentHelper;
import com.strongit.oa.bgt.model.ToaYjzx;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.bo.ParamBean;
import com.strongit.oa.senddoc.manager.SendDocLinkManager;
import com.strongit.oa.senddoc.manager.service.ISendDocIcoService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Jun 19, 2012 3:29:10 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.bgt.senddoc.SendDocAction
 */
@ParentPackage("default")
@Results( 
			{ 
				@Result(name = "yjzxfk", value = "/WEB-INF/jsp/senddoc/sendDoc-writeyjfk.jsp", type = ServletDispatcherResult.class),
				@Result(name = "viewyjzx", value = "/WEB-INF/jsp/senddoc/sendDoc-viewyjzx.jsp", type = ServletDispatcherResult.class),
				@Result(name = BaseActionSupport.SUCCESS, value = "/WEB-INF/jsp/senddoc/sendDoc-yjfklist.jsp", type = ServletDispatcherResult.class) 
			}
		)
public class SendDocAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 51790121413969983L;

	@Autowired
	private DesktopSectionManager desktop;// 个人桌面服务
	
	@Autowired
	private DocManager manager;
	
	@Autowired
	private ISendDocIcoService sendDocIcoManager;
	
	@Autowired
	private SendDocLinkManager sendDocLinkManager;
	
	private String processStatus; // 流程状态（“0”：待办；“1”：办毕)
	
	private String sortType = ""; // 已办事宜排序方式
	
	private String filterSign; // 是否要过滤签收数据 0：否，1：是
	
	private File[] file;		//附件
	
	private String[] fileFileName;	//附件名称
	
	public ToaYjzx model = new ToaYjzx();
	
	public String showDesktopDoneWork() throws Exception {

		// 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = getRequest().getParameter("showCreator");
		String showDate = getRequest().getParameter("showDate");
		if (blockId != null) {
			Map<String, String> map = desktop.getParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
		}

		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}

		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		Page<TaskBusinessBean> page = manager
				.getProcessedWorkflowForDesktop(processStatus, num, sortType,
						filterSign);
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		if (page.getResult() != null) {
			ParamBean parambean = new ParamBean();
			parambean.setNum(num);
			parambean.setLength(length);
			parambean.setRootPath(rootPath);
			parambean.setShowCreator(showCreator);
			parambean.setProcessStatus(processStatus);
			parambean.setShowDate(showDate);
			parambean.setFilterSign(filterSign);
			parambean.setSortType(sortType);
			innerHtml
					.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");

			for (Iterator iterator = page.getResult().iterator(); iterator
					.hasNext();) {
				innerHtml.append("<table class=\"linkdiv\" width=\"100%\" title=\"\">");
				TaskBusinessBean taskbusinessbean = (TaskBusinessBean) iterator
						.next();
				taskbusinessbean.setWorkflowStatus(processStatus);
				
				innerHtml.append("<tr>");
				// 图标
				innerHtml.append("<td>");
				if (taskbusinessbean.getWorkflowStatus().equals("1")) {// 办结文件
					// object[14] = "";
					innerHtml.append("<img src=\"").append(rootPath).append(
							"/oa/image/desktop/littlegif/news_bullet.gif")
							.append("\"/> ");
				} else {// 已办文件
					sendDocIcoManager.loadProcessedIco(innerHtml,
							taskbusinessbean, rootPath);
				}

				sendDocLinkManager.genProcessedTitle(innerHtml,
						taskbusinessbean, parambean);
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}

		}

		// 跳转连接
		sendDocLinkManager.genProcessedClickMoreLink(innerHtml, rootPath,
				processStatus, blockId, sortType);

		innerHtml.append("<input type=\"hidden\" class=\"listsize\" value=\"")
				.append(page == null ? 0 : (page.getTotalCount())).append(
						"\" />");
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查看意见征询登记信息
	 */
	@Override
	public String input() throws Exception {
		model = manager.getYjzx(model.getId());
		return "viewyjzx";
	}

	/**
	 * 更新意见征询登记内容
	 * @return
	 */
	public String updateYjzx() {
		String ret = "0";
		try {
			manager.updateToYjzx(model);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			ret = "-1";
		}
		return this.renderText(ret);
	}
	
	/**
	 * 加载意见征询反馈列表。
	 * 生成图片临时文件
	 */
	@Override
	public String list() throws Exception {
		List<ToaAttachment> attachments = manager.getAttachments(model.getId());
		String[] paths = new String[attachments.size()];//存储附件路径
		if(!attachments.isEmpty()) {
			for(int i=0;i<attachments.size();i++) {
				ToaAttachment attachment = attachments.get(i);
				byte[] buf = attachment.getAttachCon();
				if(buf != null) {
					InputStream is = FileUtil.ByteArray2InputStream(buf);
					String path = AttachmentHelper.saveFile(getRequest().getContextPath(), is, "temp.jpg");
					logger.info("生成附件：" + path);
					paths[i] = path; 			
				}
				
			}
			
		}
		getRequest().setAttribute("path", paths);
		return SUCCESS;
	}

	/**
	 * 转到意见反馈页面,可上传附件、修改附件
	 * @return
	 * @throws Exception
	 */
	public String yjzxfk() throws Exception {
		return "yjzxfk";
	}
	
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 保存意见征询数据
	 */
	@Override
	public String save() throws Exception {
		String ret = "0";
		try {
			manager.saveToYjzx(model);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			ret = "-1";
		}
		return this.renderText(ret);
	}

	/**
	 * 保存意见征询反馈信息（附件信息）
	 * @throws Exception
	 */
	public void saveYjfk() throws Exception {
		String callback = "true";//返回页面后的操作
		HttpServletResponse response=this.getResponse();
		PrintWriter out = response.getWriter();
		try {
			String[] delAttachId = getRequest().getParameterValues("dbobj");
			manager.saveYjzxHz(model, file, fileFileName,delAttachId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			callback = "false";
		}
		out.println("<script>parent.callback('"+callback+"')</script>");
	
	}
	
	public Object getModel() {
		return model;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getFilterSign() {
		return filterSign;
	}

	public void setFilterSign(String filterSign) {
		this.filterSign = filterSign;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

}
