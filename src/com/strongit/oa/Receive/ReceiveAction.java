package com.strongit.oa.Receive;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.Receive.bo.DocAttach;
import com.strongit.oa.bo.ToaRecvdoc;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 公文传输——收文部分
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 28, 2012 1:26:05 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.Receive.ReceiveAction
 */
@ParentPackage("default")
// @Results( {@Result(name = "query", value =
// "/WEB-INF/jsp/Receive/receive-query.jsp", type =
// ServletDispatcherResult.class) })
public class ReceiveAction extends BaseActionSupport {

	/**
	 * @field serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String SendId;// 公文业务ID

	private Page<ToaRecvdoc> page = new Page<ToaRecvdoc>(FlexTableTag.MAX_ROWS, true);

	private ToaRecvdoc model = new ToaRecvdoc();

	private ReceiveManage receiveManage;

	private int curpage;

	private int unitpage = 10;

	private String id;

	private Date searchDate; // 时间查询

	private DocAttach docAttach;

	public ReceiveAction() {

	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return INPUT;
	}

	@SuppressWarnings("unchecked")
	public String query() throws Exception {
		model.setRecvdocOfficialTime(searchDate);
		page = receiveManage.query(page, model);

		return "query";
	}

	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {

	}

	@Override
	public String save() throws Exception {
		String message = "1";
		try {
			boolean flag = this.receiveManage.saveDoc(id);
			if (flag) {
				this.receiveManage.recvedids(id);
			}
		} catch (Exception e) {
			message = "2";
		}

		return renderText(message);
		// return renderHtml("<script>alert('导入成功！');parent.location='"
		// + getRequest().getContextPath()
		// + "/Receive/receive!query.action';</script>");
		// return renderHtml("<script>alert('导入成功！');parent.location='"
		// + getRequest().getContextPath()
		// + "/Receive/receive!query.action';</script>");
	}

	/**
	 * 下载附件文件
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime May 25, 2012 9:52:45 AM
	 */
	public String downLoadAttach() throws Exception {
		HttpServletResponse response = getResponse();
		DocAttach docAttachTemp = receiveManage.getAttachByFileId(docAttach
				.getFileId()
				+ "");
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ URLEncoder.encode(docAttachTemp.getFileName(), "UTF-8"));
		ServletOutputStream out = response.getOutputStream();
		out.write(docAttachTemp.getFileContent());
		out.close();
		return null;
	}

	/**
	 * 附件列表
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime May 25, 2012 9:54:02 AM
	 */
	public String attachlist() throws Exception {
		List<DocAttach> lis1 = receiveManage.getSimpleAttachListByDocId(model
				.getRecvDocId());
		StringBuilder tableHtml = new StringBuilder();
		StringBuilder th = new StringBuilder();
		StringBuilder tbody = new StringBuilder();
		for (int i = 0; i < lis1.size(); i++) {
			DocAttach attach = lis1.get(i);
			String trclass = "";
			if (i % 2 == 0) {
				trclass = " class = a1 ";
			}
			tbody.append("<tr " + trclass + ">").append("<td>").append(
					attach.getFileName()).append("</td>").append("<td>")
					.append(attach.getFileState().equals("1") ? "附件" : "正文")
					.append("</td>").append("<td>").append(
							"<a href=\"#\" onclick=\"download('"
									+ attach.getFileId() + "')\">下载</a>")
					.append("</td>").append("</tr>");
		}
		StringBuilder tfoot = new StringBuilder();
		tfoot
				.append("<tr align=center><td colspan=3>")
				.append(
						"<input type=button onclick=window.close() class=Operation value=关闭 />&nbsp;&nbsp;")
				.append(
						"<input type=button onclick=back() class=Operation value=返回 />")
				.append("</td></tr>");
		th
				.append("<thead><th width=50%>附件名称</th><th width=30%>附件类型</th><th width=20%>操作</th></thead>");
		tableHtml.append("<table width=100% id=mytab border=1 class=t1>")
				.append(th).append(tbody).append(tfoot).append("</table>");
		getRequest().setAttribute("table", tableHtml.toString());
		return "attachlist";
	}

	public ToaRecvdoc getModel() {

		return model;
	}

	public String getSendId() {
		return SendId;
	}

	public void setSendId(String sendId) {
		SendId = sendId;
	}

	public ReceiveManage getReceiveManage() {
		return receiveManage;
	}

	@Autowired
	public void setReceiveManage(ReceiveManage receiveManage) {
		this.receiveManage = receiveManage;
	}

	public Page<ToaRecvdoc> getPage() {
		return page;
	}

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public int getUnitpage() {
		return unitpage;
	}

	public void setUnitpage(int unitpage) {
		this.unitpage = unitpage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DocAttach getDocAttach() {
		return docAttach;
	}

	public void setDocAttach(DocAttach docAttach) {
		this.docAttach = docAttach;
	}

	public Date getSearchDate() {
		if (searchDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String searchDateString = sdf.format(searchDate);
			getRequest().setAttribute("searchDateString", searchDateString);
		}
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

}
