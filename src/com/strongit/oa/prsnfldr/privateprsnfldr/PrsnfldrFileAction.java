//Source file: F:\\workspace\\StrongOA2.0_DEV\\src\\com\\strongit\\oa\\prsnfldr\\privateprsnfldr\\PrsnfldrFileAction.java

package com.strongit.oa.prsnfldr.privateprsnfldr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.bo.ToaAffiche;
import com.strongit.oa.bo.ToaAgencyPrsnfldrFolder;
import com.strongit.oa.bo.ToaDepartmentPrsnfldrFolder;
import com.strongit.oa.bo.ToaPrivatePrsnfldrFolder;
import com.strongit.oa.bo.ToaPrsnfldrFile;
import com.strongit.oa.bo.ToaPrsnfldrFolder;
import com.strongit.oa.bo.ToaPrsnfldrShare;
import com.strongit.oa.bo.ToaPublicPrsnfldrFolder;
import com.strongit.oa.prsnfldr.agencyprsnfldr.AgencyPrsnfldrFolderManager;
import com.strongit.oa.prsnfldr.departmentprsnfldr.DepartmentPrsnfldrFolderManager;
import com.strongit.oa.prsnfldr.publicprsnfldr.PublicPrsnfldrFolderManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.prsnfldr.util.Round;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date 2009年2月12日10:31:52
 * @version 1.0.0.0
 * @comment 文件管理Action
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "prsnfldrFile.action") })
public class PrsnfldrFileAction extends BaseActionSupport {

	private static final long serialVersionUID = -6300760804054820921L;
	private Page<ToaPrsnfldrFile> page = new Page<ToaPrsnfldrFile>(FlexTableTag.MAX_ROWS, true);// 分页对象,每页20条,支持自动获取总记录数
	private String folderId;// 文件夹ID,需要传递到文件列表页面
	private String folderName;// 文件夹名称,需要传递到文件列表页面
	private PrsnfldrFileManager manager;// 注入文件管理对象
	private PrsnfldrFolderManager folderManager;// 注入文件夹管理对象
	private PublicPrsnfldrFolderManager publicFolderManager;// 注入公共文件夹管理对象
	private DepartmentPrsnfldrFolderManager departmentFolderManager;// 注入部门文件夹管理对象
	private AgencyPrsnfldrFolderManager agencyFolderManager;
	private ToaPrsnfldrFile model = new ToaPrsnfldrFile();// VO对象
	private String id;// 在文件列表中删除文件，传递一个ID数组
	private String searchFileName;// 文件搜索名
	private String fileType;// 标识是公共文件柜还是个人文件柜中的文件
	private Long prsnfldAttSize ;	//默认附件大小限制
	
	
	/**
	 * 上传属性设置
	 */
	private File[] file;
	private String[] fileName;

	// 标题
	private String fileTitle;
	// 备注
	private String content;
	// 当前用户
	private String userName;
	// 操作日期
	private Date optDate;

	/**
	 * 构造方法
	 */
	public PrsnfldrFileAction() {
	}

	/**
	 * 注入文件管理对象
	 */
	@Autowired
	public void setManager(PrsnfldrFileManager aManager) {
		manager = aManager;
	}

	/**
	 * 返回VO,用于在和页面交互数据
	 */
	public ToaPrsnfldrFile getModel() {
		return model;
	}

	/**
	 * 显示个人文件柜下的文件列表，如果是第一次加载，默认加载第一个文件夹下的文件列表
	 * 
	 * @return java.lang.String
	 * @roseuid 493DD6E1004E
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back()");
		if (folderId == null || "".equals(folderId)) {
			if (logger.isInfoEnabled()) {
				logger.info("load the first folder's files." + folderId);
			}
			List<ToaPrivatePrsnfldrFolder> folderlist = folderManager
					.getAllFolders();
			if (folderlist.size() > 0) {
				ToaPrivatePrsnfldrFolder folder = folderlist.get(0);
				folderId = folder.getFolderId();
				folderName = folder.getFolderName();
			} else {
				folderName = "<a title='您的文件柜中还没有文件夹，点击此处创建' href='#' onclick='addFolder();'><nobr>创建文件夹<font color='red'>(您的文件柜中还没有文件夹，点击此处创建)</nobr></a>";
			}
		} else {
			setFolderName(folderManager.getWholeFolderById(folderId)
					.getFolderName());
		}
		page = manager.getFiles(page, folderId, searchFileName);
		return SUCCESS;
	}

	/**
	 * 显示共享文件柜下的文件列表
	 */
	public String shareFileList() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back()");
		if (folderId == null || "".equals(folderId)) {
			if (logger.isInfoEnabled()) {
				logger.info("load the first folder's files." + folderId);
			}
			List<ToaPrivatePrsnfldrFolder> folderlist = folderManager
					.getAllShareFolders();
			if (folderlist.size() > 0) {
				ToaPrivatePrsnfldrFolder folder = folderlist.get(0);
				folderId = folder.getFolderId();
				folderName = folder.getFolderName();
			} else {
				folderName = "无共享文件";
			}
		} else {
			setFolderName(folderManager.getWholeFolderById(folderId)
					.getFolderName());
		}
		page = manager.getFiles(page, folderId, searchFileName);
		return "sharefilelist";
	}

	/**
	 * 显示公共文件柜下的文件列表
	 */
	public String publicFileList() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back()");
		if (folderId == null || "".equals(folderId)) {
			if (logger.isInfoEnabled()) {
				logger.info("load the first folder's files." + folderId);
			}
			List<ToaPublicPrsnfldrFolder> folderlist = publicFolderManager
					.getAllFolders();
			if (folderlist.size() > 0) {
				ToaPublicPrsnfldrFolder folder = folderlist.get(0);
				folderId = folder.getFolderId();
				folderName = folder.getFolderName();
			} else {
				folderName = "<a title='您的文件柜中还没有文件夹，点击此处创建' href='#' onclick='addFolder();'><nobr>创建文件夹<font color='red'>(您的文件柜中还没有文件夹，点击此处创建)</nobr></a>";
			}
		} else {
			ToaPrsnfldrFolder folder = folderManager
					.getWholeFolderById(folderId);
			setFolderName(folder.getFolderName());
		}
		page = manager.getFiles(page, folderId, searchFileName);
		return "publicfilelist";
	}

	/**
	 * 显示部门文件柜下的文件列表
	 */
	public String departmentFileList() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back()");
		if (folderId == null || "".equals(folderId)) {
			if (logger.isInfoEnabled()) {
				logger.info("load the first folder's files." + folderId);
			}
			List<ToaDepartmentPrsnfldrFolder> folderlist = departmentFolderManager
					.getAllFolders();
			if (folderlist.size() > 0) {
				ToaDepartmentPrsnfldrFolder folder = folderlist.get(0);
				folderId = folder.getFolderId();
				folderName = folder.getFolderName();
			} else {
				folderName = "<a title='您的文件柜中还没有文件夹，点击此处创建' href='#' onclick='addFolder();'><nobr>创建文件夹<font color='red'>(您的文件柜中还没有文件夹，点击此处创建)</nobr></a>";
			}
		} else {
			ToaPrsnfldrFolder folder = folderManager
					.getWholeFolderById(folderId);
			setFolderName(folder.getFolderName());
		}

		page = manager.getFiles(page, folderId, searchFileName);
		return "departmentfilelist";
	}

	/**
	 * 部门文件柜下的文件列表桌面显示
	 * 
	 * @author:申仪玲
	 * @return
	 */
	public String showDepartmentDesktop() throws Exception {
		
		StringBuffer innerHtml = new StringBuffer();

		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}

		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}

		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "12";
		}
		// 链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(
				getRequest().getContextPath()).append(
				"/fileNameRedirectAction.action?toPage=prsnfldr/departmentprsnfldr/departmentPrsnfldrFolder-content.jsp").append(
				"', '部门文件柜'").append(");");

		// 获取部门文件柜的文件列表
		List<ToaPrsnfldrFile> list =  new ArrayList<ToaPrsnfldrFile>();
		List<ToaDepartmentPrsnfldrFolder> folderlist = departmentFolderManager.getAllFolders();
		if(folderlist !=null){
			for (int i = 0; i < folderlist.size(); i++) {
				ToaDepartmentPrsnfldrFolder folder = folderlist.get(i);
				folderId = folder.getFolderId();
				List<ToaPrsnfldrFile> filelist = manager.getFilesByFolderId(folderId);
				list.addAll(filelist);				
			}
		}

		if (list != null) {
			for (int i = 0; i < num && i < list.size(); i++) {// 获取在条数范围内的列表
				ToaPrsnfldrFile file = list.get(i);
				// 标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append(
						"javascript:window.parent.refreshWorkByTitle('")
						.append(getRequest().getContextPath()).append(
								"/prsnfldr/privateprsnfldr/prsnfldrFile!viewFile.action?id="
										+ file.getFileId()).append("', '查看部门文件'")
						.append(");");

				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td width=\"64%\">");
				String title = file.getFileTitle();
				if (title == null) {
					title = "";
				}
				if (title.length() > length)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0, length) + "...";
				innerHtml.append("	<span title=\"").append(title).append("\">")
						.append("	<a style=\"font-size:"+sectionFontSize+"px\" href=\"#\" onclick=\"").append(
								titleLink.toString()).append("\"> ").append(
								title).append("</a></span>");
				innerHtml.append("</td>");
				innerHtml.append("<td width=\"20%\">");
				if ("1".equals(showCreator)) {// 如果设置为显示作者，则显示作者信息
					if (file.getFileCreatePerson() == null) {
						innerHtml.append("	<span class =\"linkgray\">").append(
								"").append("</span>");
					} else {
						innerHtml.append("	<span class =\"linkgray\">").append(
								file.getFileCreatePerson()).append("</span>");
					}
				}
				innerHtml.append("</td>");
				innerHtml.append("<td width=\"16%\" align=\"right\">");
				if ("1".equals(showDate)) {// 如果设置为显示日期，则显示日期信息
					innerHtml.append("	<span class =\"linkgray10\">").append(
							st.format(file.getFileCreateTime())).append(
							"</span>");
				}
				innerHtml.append("	</td>");
				innerHtml.append("	</tr>");
				innerHtml.append("	</table>");
			}
		}
		innerHtml
				.append(
						"<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"")
				.append(link.toString()).append("\"> ")
				.append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");

		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * 显示机构文件柜下的文件列表
	 */
	public String agencyFileList() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back()");
		if (folderId == null || "".equals(folderId)) {
			if (logger.isInfoEnabled()) {
				logger.info("load the first folder's files." + folderId);
			}
			List<ToaAgencyPrsnfldrFolder> folderlist = agencyFolderManager
					.getAllFolders();
			if (folderlist.size() > 0) {
				ToaAgencyPrsnfldrFolder folder = folderlist.get(0);
				folderId = folder.getFolderId();
				folderName = folder.getFolderName();
			} else {
				folderName = "<a title='您的文件柜中还没有文件夹，点击此处创建' href='#' onclick='addFolder();'><nobr>创建文件夹<font color='red'>(您的文件柜中还没有文件夹，点击此处创建)</nobr></a>";
			}
		} else {
			ToaPrsnfldrFolder folder = folderManager
					.getWholeFolderById(folderId);
			setFolderName(folder.getFolderName());
		}

		page = manager.getFiles(page, folderId, searchFileName);
		return "agencyfilelist";
	}

	/**
	 * 机构文件柜下的文件列表桌面显示
	 * 
	 * @author:申仪玲
	 * @return
	 */

	public String showAgencyDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();

		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}

		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}

		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "12";
		}
		// 链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(
				getRequest().getContextPath()).append(
				"/fileNameRedirectAction.action?toPage=prsnfldr/agencyprsnfldr/agencyPrsnfldrFolder-content.jsp").append(
				"', '机构文件柜'").append(");");

		// 获取机构文件柜的文件列表
		List<ToaPrsnfldrFile> list =  new ArrayList<ToaPrsnfldrFile>();
		List<ToaAgencyPrsnfldrFolder> folderlist = agencyFolderManager.getAllFolders();
		if(folderlist !=null){
			for (int i = 0; i < folderlist.size(); i++) {
				ToaAgencyPrsnfldrFolder folder = folderlist.get(i);
				folderId = folder.getFolderId();
				List<ToaPrsnfldrFile> filelist = manager.getFilesByFolderId(folderId);
				list.addAll(filelist);				
			}
		}

		if (list != null) {
			for (int i = 0; i < num && i < list.size(); i++) {// 获取在条数范围内的列表
				ToaPrsnfldrFile file = list.get(i);
				// 标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append(
						"javascript:window.parent.refreshWorkByTitle('")
						.append(getRequest().getContextPath()).append(
								"/prsnfldr/privateprsnfldr/prsnfldrFile!viewFile.action?id="
										+ file.getFileId()).append("', '查看机构文件'")
						.append(");");

				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td width=\"64%\">");
				String title = file.getFileTitle();
				if (title == null) {
					title = "";
				}
				if (title.length() > length)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0, length) + "...";
				innerHtml.append("	<span title=\"").append(title).append("\">")
						.append("	<a style=\"font-size:"+sectionFontSize+"px\" href=\"#\" onclick=\"").append(
								titleLink.toString()).append("\"> ").append(
								title).append("</a></span>");
				innerHtml.append("</td>");
				innerHtml.append("<td width=\"20%\">");
				if ("1".equals(showCreator)) {// 如果设置为显示作者，则显示作者信息
					if (file.getFileCreatePerson() == null) {
						innerHtml.append("	<span class =\"linkgray\">").append(
								"").append("</span>");
					} else {
						innerHtml.append("	<span class =\"linkgray\">").append(
								file.getFileCreatePerson()).append("</span>");
					}
				}
				innerHtml.append("</td>");
				innerHtml.append("<td width=\"16%\" align=\"right\">");
				if ("1".equals(showDate)) {// 如果设置为显示日期，则显示日期信息
					innerHtml.append("	<span class =\"linkgray10\">").append(
							st.format(file.getFileCreateTime())).append(
							"</span>");
				}
				innerHtml.append("	</td>");
				innerHtml.append("	</tr>");
				innerHtml.append("	</table>");
			}
		}
		innerHtml
				.append(
						"<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"")
				.append(link.toString()).append("\"> ")
				.append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");

		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * author:dengzc description:搜索-自动完成功能 modifyer: description:
	 * 
	 * @return
	 */
	public String autoComplete() throws Exception {
		try {
			String q = getRequest().getParameter("q");
			StringBuffer sb = null;
			;
			if (null != q && !"".equals(q)) {
				q = URLDecoder.decode(q, "utf-8");
				sb = manager.searchByAutoComplete(folderId, q);
				renderText(sb.toString());
			}
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * 上传文件
	 * 
	 * @return java.lang.String
	 * @roseuid 493DD6E1005D
	 */
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		boolean isPublicFolder = true;
		ToaPrsnfldrFolder folder = folderManager.getWholeFolderById(folderId);
		if (folder == null || "".equals(folder)) {
			addActionMessage("对不起,不能在根路径下放置文件。请先建立文件夹。");
			userName = manager.getCurrentUser().getUserName();
			optDate = new Date();
			return "init";
		}
		if (file == null) {// 未上传文件就提交了
			addActionMessage("对不起,请选择附件。");
			userName = manager.getCurrentUser().getUserName();
			optDate = new Date();
			return "init";
		}
		if (FileUtil.getTotalSize(file) > FileUtil.getIntFileUploadMaxSize()) {
			addActionMessage("对不起,每次上传的文件大小不能超过"
					+ FileUtil.getStringFileUploadMaxSize() + "。");
			userName = manager.getCurrentUser().getUserName();
			optDate = new Date();
			return "init";
		}
		if (folder instanceof ToaPrivatePrsnfldrFolder) {// 个人文件柜需要验证
			double used = manager.getTotalSize();
			double uploadSize = used + FileUtil.getTotalSize(file);
			double total = folderManager.getUserSpace();// 获取分配的空间大小
			if (total == 0.0) {
				addActionMessage("对不起,系统未给您分配空间,请与管理员联系。");
				userName = manager.getCurrentUser().getUserName();
				optDate = new Date();
				return "init";
			}
			if (uploadSize > total) {
				addActionMessage("对不起,您的空间不足,请删除一些文件释放空间。");
				userName = manager.getCurrentUser().getUserName();
				optDate = new Date();
				return "init";
			}
			isPublicFolder = false;
		}

		for (int i = 0; i < file.length; i++) {
			
			if (FileUtil.isEmptyFile(file[i].length())) {
				addActionMessage("对不起,您上传的文件至少有一个空文件.本次上传失败。");
				userName = manager.getCurrentUser().getUserName();
				optDate = new Date();
				return "init";
			}
			
		}
		
		for (int i = 0, j = 1; i < file.length; i++, j++) {
			/**
			if (FileUtil.isEmptyFile(file[i].length())) {
				addActionMessage("对不起,您上传的文件至少有一个空文件.本次上传失败。");
				userName = manager.getCurrentUser().getUserName();
				optDate = new Date();
				return "init";
			}
			*/
			ToaPrsnfldrFile model = new ToaPrsnfldrFile();
			model.setFileCreateTime(new Date());
			model.setFileSortNo("0");
			model.setFileCreatePerson(folderManager.getUserName());// 发布人

			FileInputStream fis = null;
			FileInputStream indexfis = null;// 全文检索用到
			try {
				fis = new FileInputStream(file[i]);
				File newflie = file[i];// 全文检索用到
				indexfis = new FileInputStream(newflie);// 全文检索用到
				byte[] buf = new byte[(int) file[i].length()];
				fis.read(buf);
				long length_byte = file[i].length();
				String size = "";
				if (length_byte >= 1024) {
					double length_k = ((double) length_byte) / 1024;
					if (length_k >= 1024) {
						size = Round.round(length_k / 1024, 2) + "MB";
					} else {
						size = length_byte / 1024 + "k";
					}
				} else {
					size = length_byte + "字节";
				}
				model.setFileContent(buf);
				String ext = fileName[i]
						.substring(fileName[i].lastIndexOf(".") + 1);
				model.setFileExt(ext);
				model.setFileName(fileName[i]);
				if (file.length > 1) {
					model.setFileTitle(fileTitle + "(" + j + ")");
				} else {
					model.setFileTitle(fileTitle);
				}
				model.setFileBeiZhu(getRequest().getParameter("content"));
				model.setFileSize(size);
				model.setToaPrsnfldrFolder(folder);
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("上传失败。" + e);
				}
				throw e;
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					if (logger.isErrorEnabled()) {
						logger.error("文件关闭失败。" + e);
					}
					throw e;
				}
			}
			manager.addFile(model, indexfis, isPublicFolder);
		}
		// addActionMessage("文件已经上传至“"+folder.getFolderName()+"“目录");
		//addActionMessage("ok");
		getRequest().setAttribute("isTrue", 1);
		return "init";
	}

	/**
	 * @return java.lang.String
	 * @roseuid 493DD6E1007D
	 */
	public String delete() throws Exception {
		try {
			manager.deleteFile(id);
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * 下载文件
	 */
	public String download() throws Exception {
		HttpServletResponse response = super.getResponse();
		ToaPrsnfldrFile file = manager.getFileById(id);
		response.reset();
		response.setContentType("application/x-download"); // windows
		OutputStream output = null;
		try {
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(file.getFileName().getBytes("gb2312"),
							"iso8859-1"));
			output = response.getOutputStream();
			output.write(file.getFileContent());
			output.flush();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage(), e);
			}
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
				}
				output = null;
			}
		}
		return null;
	}

	/**
	 * 文件复制 这里采用session来模拟内存地址。存储需要复制的文件ID。
	 */
	@SuppressWarnings("unchecked")
	public String copy() throws Exception {
		try {
			ActionContext context = ActionContext.getContext();
			Map<String, Object> ids = context.getSession();
			ids.put("file_id", id);// 将文件ID保存在session中
			ids.put("operation", "copy");// 标识用户是在复制还是在剪切
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * 粘贴文件 取出session中保存的文件ID，然后获得文件对象，存储到对应的文件夹中 文件的复制采用的对象的克隆技术。
	 */
	@SuppressWarnings("unchecked")
	public String parse() throws Exception {
		try {
			ActionContext context = ActionContext.getContext();
			Map<String, Object> ids = context.getSession();
			String file_id = (String)ids.get("file_id");
			String message = "";// 传回客户端的消息提醒
			ToaPrsnfldrFolder privatefolder = folderManager
					.getWholeFolderById(folderId);
			boolean isPublicFolder = true;
			String operation = (String)ids.get("operation");
			if (privatefolder == null || "".equals(privatefolder)) {
				if (file_id == null || file_id.equals("")) {
					message = "对不起,粘贴板内无文件,请先复制或剪切文件。";
				} else {
					message = "对不起，不能在根路径下放置文件。请先建立文件夹";
				}
			} else {
				if (file_id != null && !"".equals(file_id)) {
					String[] files = file_id.split(",");
					if (privatefolder instanceof ToaPrivatePrsnfldrFolder) {// 个人文件柜需要验证
						double fileSize = manager.getTheCopyFileSize(files);
						double used = manager.getTotalSize();
						double uploadSize = used + fileSize;
						double total = folderManager.getUserSpace();// 获取分配的空间大小
						if (total == 0.0) {
							message = "对不起,系统未给您分配空间,请与管理员联系。";
							renderText(message);
							return null;
						}
						if (uploadSize > total) {
							message = "对不起,您的空间不足,请删除一些文件释放空间。";
							renderText(message);
							return null;
						}
						isPublicFolder = false;
					}
					for (int i = 0; i < files.length; i++) {
						ToaPrsnfldrFile file = manager.getFileById(files[i]);
						ToaPrsnfldrFile file_copy = (ToaPrsnfldrFile) file
								.clone();
						if (file_copy != null) {
							file_copy.setToaPrsnfldrFolder(privatefolder);
							file_copy.setFileId(null);
							// 添加时间为当前时间
							file_copy.setFileCreateTime(new Date());
							manager.addFile(file_copy, isPublicFolder);
							// 如果是剪切将删除原文件
							if ("cut".equals(operation)) {
								manager.deleteFile(file);// 同时清空session，防止剪切后多次粘贴,对于复制的情况，这里允许多次粘贴
								ids.put("file_id", null);
							}
						}
					}
					// message = "文件已经复制到“"+privatefolder.getFolderName()+"”目录";
				} else {
					message = "对不起,粘贴板内无文件,请先复制或剪切文件。";
				}
			}
			renderText(message);
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * 文件剪切 每次执行剪切或复制时，上一次的操作都被覆盖
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String cut() throws Exception {
		try {
			ActionContext context = ActionContext.getContext();
			Map<String, Object> ids = context.getSession();
			ids.put("file_id", id);// 将文件ID保存在session中
			ids.put("operation", "cut");// 标识操作为剪切
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * @roseuid 493DD6E1008C
	 */
	protected void prepareModel() throws Exception {
		if (id != null) {
			model = manager.getFileById(id);
			content = model.getFileBeiZhu();
			userName = model.getFileCreatePerson();
			optDate = model.getFileCreateTime();
			// 查阅数加1
			int no = 0;
			if (model.getFileSortNo() == null
					|| "".equals(model.getFileSortNo())) {
				no = 1;
			} else {
				no = Integer.parseInt(model.getFileSortNo()) + 1;
			}
			model.setFileSortNo(String.valueOf(no));
			manager.editFile(model);
		}
	}

	/**
	 * 转到文件上传或编辑页面 在文件上传时，传递了folderId（用于在后台通知文件存放位置）
	 * 和folderName（用于在页面通知用户文件保存位置）两个参数
	 * 
	 * @return
	 */
	public String init() {
		userName = manager.getCurrentUser().getUserName();
		optDate = new Date();
		prsnfldAttSize = manager.getDefAttSize();
		return "init";
	}

	/**
	 * 转到编辑文件页面
	 */
	public String initEditFile() throws Exception {
		prepareModel();
		String userId = manager.getCurrentUser().getUserId();// 当前用户ID
		String privlProp = "readonly";
		ToaPrsnfldrFolder folder = model.getToaPrsnfldrFolder();
		if (userId.equals(folder.getUserId())) {// 当前用户是文件所属文件夹的创建人
			privlProp = "readwrite";
		} else {
			Set<ToaPrsnfldrShare> shares = folder.getToaPrsnfldrShares();
			if (shares.size() > 0) {// 此文件所属文件夹被共享了。
				for (ToaPrsnfldrShare share : shares) {
					if (userId.equals(share.getUserId())||"allPeople".equals(share.getUserId())) {
						privlProp = share.getSharePrivilege();
						break;
					}
				}
			}

		}

		ActionContext.getContext().put("privlprop", privlProp);
		return "editfile";
	}

	/**
	 * 查看文件.
	 * 
	 * @author:邓志城
	 * @date:2010-7-12 下午05:05:17
	 * @return
	 * @throws Exception
	 */
	public String viewFile() throws Exception {
		prepareModel();
		return "viewfile";
	}

	/**
	 * author:dengzc description:修改新建的文件 modifyer: description:
	 * 
	 * @return
	 */
	public String editFile() throws Exception {
		ToaPrsnfldrFile file = manager.getFileById(getModel().getFileId());
		file.setFileTitle(model.getFileTitle());
		file.setFileBeiZhu(getRequest().getParameter("content"));
		manager.editFile(file);
		//addActionMessage("ok");
		getRequest().setAttribute("isTrue", 1);
		return "init";
	}

	/**
	 * 获取文件后缀,用于判断是查看新建的文件还是上传的文件
	 * 
	 * @param folderManager
	 */
	public String getExt() throws Exception {
		try {
			model = manager.getFileById(id);
			String ext = model.getFileExt();
			String data = "";
			if (null == ext || "".equals(ext)) {
				data = "0";
			} else {
				data = "1";
			}
			renderText(data);
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return null;
	}

	/**
	 * author:dengzc description:查看个人文件柜属性 modifyer: description:
	 * 
	 * @param folderManager
	 */
	public String prop() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		double totalSize = manager.getTotalSize();
		String stringTotalSize = Round.round(totalSize / (1024 * 1024), 2)
				+ "MB"; // 已经用掉的空间大小totalSize/(1024*1024)+"MB";
		double all = folderManager.getUserSpace();// 获取分配的空间大小
		String restSize = String.valueOf(Round.round((all - totalSize)
				/ (1024 * 1024), 2))
				+ "MB";// 剩余
		ActionContext cxt = ActionContext.getContext();
		cxt.put("allSize", folderManager.getUserSpaceStr());
		cxt.put("used", stringTotalSize);
		cxt.put("rest", restSize);

		return "prop";
	}

	/**
	 * 校验文件标题是否已存在.
	 * 
	 * @author:邓志城
	 * @date:2009-11-5 下午04:26:09
	 * @return 0：不存在；1：存在.-1:发生异常
	 * @throws Exception
	 */
	public String checkTitle() throws Exception {
		String ret = "";
		try {
			boolean boolRet = manager.isTitleAlreadyExist(fileTitle, folderId);
			if (boolRet) {
				ret = "1";
			} else {
				ret = "0";
			}
		} catch (Exception e) {
			LogPrintStackUtil.error("校验文件标题是否存在异常。" + e);
			ret = "-1";
		}
		return this.renderText(ret);
	}

	@Autowired
	public void setFolderManager(PrsnfldrFolderManager folderManager) {
		this.folderManager = folderManager;
	}

	public Page<ToaPrsnfldrFile> getPage() {
		return page;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getFolderId() {
		return folderId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setUpload(File[] file) {
		this.file = file;
	}

	public void setUploadFileName(String[] fileName) {
		this.fileName = fileName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Autowired
	public void setPublicFolderManager(
			PublicPrsnfldrFolderManager publicFolderManager) {
		this.publicFolderManager = publicFolderManager;
	}

	@Autowired
	public void setAgencyFolderManager(
			AgencyPrsnfldrFolderManager agencyFolderManager) {
		this.agencyFolderManager = agencyFolderManager;
	}

	@Autowired
	public void setDepartmentFolderManager(
			DepartmentPrsnfldrFolderManager departmentFolderManger) {
		this.departmentFolderManager = departmentFolderManger;
	}

	public void setSearchFileName(String searchFileName) {
		this.searchFileName = searchFileName;
	}

	public String getSearchFileName() {
		return searchFileName;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getOptDate() {
		return optDate;
	}

	public void setOptDate(Date optDate) {
		this.optDate = optDate;
	}

	public String getContent() {
		return content;
	}

	public Long getPrsnfldAttSize() {
		return prsnfldAttSize;
	}

	public void setPrsnfldAttSize(Long prsnfldAttSize) {
		this.prsnfldAttSize = prsnfldAttSize;
	}

}
