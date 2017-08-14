package com.strongit.doc.sends;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.db2.jcc.a.b;
import com.ibm.db2.jcc.a.s;
import com.informix.util.stringUtil;
import com.strongit.doc.bo.TdocSend;
import com.strongit.doc.bo.TdocSendPrintPassword;
import com.strongit.doc.bo.TtransArchive;
import com.strongit.doc.bo.TtransDoc;
import com.strongit.doc.bo.TtransDocAttach;
import com.strongit.doc.receives.archive.ArchiveDocTree;
import com.strongit.doc.sends.util.DESEncrypter;
import com.strongit.doc.sends.util.DocType;
import com.strongit.oa.autocode.flowhandler.FlowHandlerManager;
import com.strongit.oa.bo.ToaReportBean;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.dict.IDictService;
import com.strongit.doc.util.DataSource;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.PathUtil;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "docSend.action", type = ServletActionRedirectResult.class),
			@Result(name = "jfcharts", value = "/WEB-INF/jsp/sends/docTransceiverReportResult.jsp", type = ServletDispatcherResult.class),
	})
public class DocSendAction  extends BaseActionSupport {

	private Page<TdocSend> page = new Page<TdocSend>(10, true);
	
	private Page<Object[]> recpage = new Page(15, true);
	private Page<TtransArchive> page1=new Page<TtransArchive>(20, true);		//分页
	
	private String docSendId;	//分发ID
	
	private String docId;	//公文ID
	private String orgIds;	//公文ID
	private String orgIdss;	//公文ID //抄送
	private String defPrint;	//打印份数
	private String defPrints;	//打印份数  //抄送
	
	/** 报表格式*/
	private String exportType;
	/** 报表统计时间*/
	private String reportDate;
	
	private String reportEndDate;
	/** 当前页*/
	private int currentPage;
	/** 总页数*/
	private int totalNum;

	private TdocSend model = new TdocSend();
	private TtransArchive myModel=new TtransArchive();	//档案信息
	private TtransDoc docModel = new TtransDoc();

	private DocSendManager manager;
	@Autowired
    FlowHandlerManager flowHandlerManager;
	@Autowired private TransDocManager transDocManager;
	
	@Autowired private IUserService user;
	
	@Autowired IDictService dictService ;										//字典服务类
	
	private List<Organization> orgList;
	private List orgGroupList;
	
	private List<ToaSysmanageDictitem> mmdjItems ;								//秘密等级字典项
	
	private List<ToaSysmanageDictitem> jjcdItems ;								//紧急程度字典项
	
	private	Date startDate ;															//开始日期
	
	private Date endDate ;																//结束日期
	
	private String disLogo;													//标识
	private String treeValue;
	private List<ArchiveDocTree>treeList;					//年份显示树
    
    private List<String> yearList;
    private String year;									//文件年份f
    private List<String> monthList;
    private String month;									//文件发文月份

	private HashMap<String, String> typemap = new HashMap<String, String>();			//分发状态
	private HashMap<String, String> docstate = new HashMap<String, String>();			//分发状态
	private String unitORall;												   		//单位发文或所有发文标识
	private String reportTime;														//年报 月报 日报 控制时间
	private String Identification;						//发文与收文区分标识
	
	TdocSendPrintPassword tdocSendPrintPassword;
	String password;  //公文打印密码
	public String getIdentification() {
		return Identification;
	}

	public void setIdentification(String identification) {
		Identification = identification;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String getUnitORall() {
		return unitORall;
	}

	public void setUnitORall(String unitORall) {
		this.unitORall = unitORall;
	}
	
		
	private DesktopSectionManager sectionManager;
	
	@Autowired
	public void setSectionManager(DesktopSectionManager sectionManager) {
		this.sectionManager = sectionManager;
	}
	
	public TdocSend getModel() {
		return this.model;
	}

	public String getDocSendId() {
		return docSendId;
	}

	public void setDocSendId(String docSendId) {
		this.docSendId = docSendId;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}
	
	public Page<TdocSend> getPage() {
		return page;
	}

	public void setModel(TdocSend model) {
		this.model = model;
	}
	
	public List<Organization> getOrgList() {
		return orgList;
	}

	public List getOrgGroupList() {
		return orgGroupList;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	public String getDefPrint() {
		return defPrint;
	}

	public void setDefPrint(String defPrint) {
		this.defPrint = defPrint;
	}

	public List<ToaSysmanageDictitem> getJjcdItems() {
		return jjcdItems;
	}

	public void setJjcdItems(List<ToaSysmanageDictitem> jjcdItems) {
		this.jjcdItems = jjcdItems;
	}

	public List<ToaSysmanageDictitem> getMmdjItems() {
		return mmdjItems;
	}

	public void setMmdjItems(List<ToaSysmanageDictitem> mmdjItems) {
		this.mmdjItems = mmdjItems;
	}
	
	public TtransDoc getDocModel() {
		return docModel;
	}

	public void setDocModel(TtransDoc docModel) {
		this.docModel = docModel;
	}

	@Autowired
	public void setManager(DocSendManager manager) {
		this.manager = manager;
	}
	
	public HashMap getTypemap() {
		return typemap;
	}
	
	public DocSendAction(){
		typemap.put("null", "未发送");
		typemap.put(null, "未发送");
		typemap.put(TdocSend.DOCSEND_WAITRECV, "待收");
		typemap.put(TdocSend.DOCSEND_NOTRECV, "已拒收");
		typemap.put(TdocSend.DOCSEND_RECVED, "已收");
		typemap.put(TdocSend.DOCSEND_RECYCLER, "已回收");
	
	}
	
	
	@Override
	public String delete() throws Exception {
		return null;
	}

	
	public String viewDoc() throws Exception {
		
		return "viewDoc";
	}
	/**
	 * @author:luosy
	 * @description:  根据分发对象回收及修改打印份数
	 * @date : 2010-8-17
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	@Override
	public String input() throws Exception {
		String userN = user.getCurrentUser().getUserName();
		
		String state = model.getRecvState();
		StringBuilder info = new StringBuilder();
		model = manager.getDocSendById(docSendId);
		if(!DocType.Sended.getValue().equals(model.getTtransDoc().getDocState())){		//  公文不是分发状态，不能回收或修改默认打印份数
			return renderText("公文不是分发状态，不能回收或修改默认打印份数！");
		}else if(model.getRecvState().equals(TdocSend.DOCSEND_RECYCLER)){		//  该单位已回收，不能重复回收或修改默认打印份数
			return renderText("该单位已回收，不能重复回收或修改默认打印份数！");
		}
//		else if(model.getRecvState().equals(TdocSend.DOCSEND_RECVED)){		//  该单位已回收，不能重复回收或修改默认打印份数
//			return renderText("该单位已签收，不能重复回收或修改默认打印份数！");
//		}
		
		
		if(null!=state&&!"".equals(state)){
			model.setRecvState(state);
			info.append("回收发给【").append(model.getDeptName()).append("】的公文《")
				.append(model.getTtransDoc().getDocTitle()).append("》");
		}
		if(null!=defPrint&&!"".equals(defPrint)){
			model.setDocHavePrintSum(defPrint);
			info.append("将分发给【").append(model.getDeptName()).append("】的公文《")
				.append(model.getTtransDoc().getDocTitle()).append("》的打印份数修改为：").append(defPrint);
		}
		
		return renderText(manager.saveDocSend(model, new OALogInfo(userN+" "+info.toString())));//成功返回：0
	}
	
	
	/*
	 * 判断该单位公文是否回收
	 */
	public String isHasRecycleDocByOrg() throws Exception{
		model = manager.getDocSendById(docSendId);
		if(model.getRecvState().equals(TdocSend.DOCSEND_RECYCLER)){
			return renderText("1");
		}else {
			return renderText("0");
		}
	}
	
	
	public String batchRecycle() throws Exception
	{
		String[] ids = getRequest().getParameter("ids").toString().split(",");
		if(!verifyState(ids))
		{
			return renderText("-1");
		}
		
		return  renderText(String.valueOf( manager.RecycleAll(ids)));
	}
	
	public String batchSetPrintNum() throws Exception
	{
		String[] ids = getRequest().getParameter("ids").toString().split(",");
		String num =  getRequest().getParameter("defPrint").toString(); 
		if(!verifyState2(ids))
		{
			return renderText("-1");
		}
		
		return  renderText(String.valueOf( manager.SetAll(ids, Integer.parseInt(num))));
	}
	
	
	
	
	private Boolean verifyState(String[] ids) throws Exception
	{
		
		for(String id : ids)
		{
			model = manager.getDocSendById(id);
			System.out.print(model.getTtransDoc().getDocState());
			System.out.print(model.getRecvState());
			if(!DocType.Sended.getValue().equals(model.getTtransDoc().getDocState()) 
					|| (!model.getRecvState().equals(TdocSend.DOCSEND_WAITRECV)))
			{
				return false;
			}
		}
		return true;
	}
	
	//判断是否签收或拒收
	private Boolean verifyState2(String[] ids) throws Exception
	{
		
		for(String id : ids)
		{
			model = manager.getDocSendById(id);
			if(!DocType.Sended.getValue().equals(model.getTtransDoc().getDocState()) || !model.getRecvState().equals(TdocSend.DOCSEND_WAITRECV ))
			{
				return false;
			}
		}
		return true;
	}
	
	
	//判断是否签收或拒收
	public String verifyState3() throws Exception
	{
		String[] ids = getRequest().getParameter("ids").toString().split(",");
		for(String id : ids)
		{
			model = manager.getDocSendById(id);
			if(!DocType.Sended.getValue().equals(model.getTtransDoc().getDocState()) || !model.getRecvState().equals(TdocSend.DOCSEND_WAITRECV ))
			{
				return renderText("-1");
			}
		}
		return null;
	}
	
	/**
	 * @author:luosy
	 * @description:  根据公文id回收公文
	 * @date : 2010-8-17
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String recycleDoc() throws Exception{
		String userN = user.getCurrentUser().getUserName();
		
		TtransDoc doc = transDocManager.getDocById(docId);
		if(DocType.Draft.getValue().equals(doc.getDocState())
				||DocType.Sign.getValue().equals(doc.getDocState())
//				||DocType.Send.getValue().equals(doc.getDocState())
				||DocType.Recycle.getValue().equals(doc.getDocState())
				||DocType.ReturnBack.getValue().equals(doc.getDocState())){		
			return renderText("该公文状态不能进行回收！");
		}
		
		return renderText(manager.recycleDoc(doc, new OALogInfo(userN+" 回收全部发出的公文《"+doc.getDocTitle()+"》")));
	}

	/*
	 * 判断该单位公文是否已经全部接收完、或已拒收、回收
	 */
	public String isHasRecycleDocByDocId() throws Exception{
		if("".equals(docId)||null==docId){
			return renderText("0");
		}else{
			List list = new ArrayList();
			list = manager.getSendsList(docId);
			for(int i=0;i<list.size();i++){
				TdocSend temp = (TdocSend) list.get(i);
				if("0".equals(temp.getRecvState())){
					return renderText("0");
				}
			}
			return renderText("1");
		}
	}
	/*
	 * 回收公文时  公文只在全部没签收的情况下  才能被回收
	 */
	public String isHasRecycleDocByDocIds() throws Exception{
		if("".equals(docId)||null==docId){
			return renderText("0");
		}else{
			List list = new ArrayList();
			list = manager.getSendsList(docId);
			for(int i=0;i<list.size();i++){
				TdocSend temp = (TdocSend) list.get(i);
				if((!"0".equals(temp.getRecvState()))&&(!"3".equals(temp.getRecvState()))){
					return renderText("0");
				}
			}
			return renderText("1");
		}
	}
	/*
	 * 把该公文是否已经 回收
	 */
	public String isHasRecycleDoc() throws Exception{
		TtransDoc doc = transDocManager.getDocById(docId);
		if(DocType.Recycle.getValue().equals(doc.getDocState())){    
			return renderText("1");
		}else {
			return renderText("0");
		}
	}
	
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}
	
	public String sendList() throws Exception {
		mmdjItems = dictService.getItemsByValue("MMDJ");//秘密等级
		jjcdItems = dictService.getItemsByValue("JJCD");//紧急程度
		if(disLogo!=null&&disLogo.equals("search")){
			page1=manager.search(page1, myModel ,startDate,endDate);			//根据条件查询档案中心文件列表
		}else{
			page1=manager.getPageArchive(page1,treeValue);			//获取所有的文件列表
		}
		if(page1.getResult()==null){
			page1.setTotalCount(0);
		}
		return "list";
	}
	/**
	 * 年月份显示树
	 * @return
	 * @throws Exception
	 */
	public String tree() throws Exception{
		try {
				treeList=new ArrayList<ArchiveDocTree>();
				yearList=new ArrayList<String>();//年份列表
				
				User user1=user.getCurrentUser();
				yearList=manager.getTempfileYear();
				for(String ye:yearList){
					if(ye!=null){
						try {
							
							ArchiveDocTree tree=new ArchiveDocTree();
							tree.setTreeid(ye);
							tree.setTreeName("年");
							tree.setTreetype("4");
							treeList.add(tree);
						} catch (Exception e) {
							e.printStackTrace();
						}
						monthList=manager.getTempfileMonth(ye);
						if(monthList!=null){  //月份列表
							for(String mon:monthList){
								ArchiveDocTree montree=new ArchiveDocTree();
								montree.setTreeid(mon);
								montree.setParentId(ye);
								montree.setTreeName("月份");
								montree.setTreetype("4");
								treeList.add(montree);
							}
						}
					}				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "tree";
	}

	
	/**
	 * @author:luosy
	 * @description: 公文分发到机构列表信息
	 * @date : 2010-8-17
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String sendslist() throws Exception {
		if("".equals(docId)||null==docId){
			return "definfo";
		}else{
			docModel = transDocManager.getDocById(docId);
			String title = docModel.getDocTitle();
			
			String userId = user.getCurrentUser().getUserId();
			String danwei = user.getUserDepartmentByUserId(userId).getOrgName();
			getRequest().setAttribute("danwei", danwei);
			getRequest().setAttribute("title", title);
			
			page = manager.getSendsPage(page, model,docId);
			return "sendslist";
		}
	}
		
	/**
	 * 桌面显示
	 * @description	
	 * @author  Jianggb
	 * @return
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public String showDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		
		String blockid = getRequest().getParameter("blockid");//获取blockid
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum"); 
		String showCreator =getRequest().getParameter("showCreator"); 
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockid != null){
			Map<String,String> map = sectionManager.getParam(blockid);//通过blockid获取映射对象
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			showCreator = map.get("showCreator");//是否显示作者
			showDate = map.get("showDate");//是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		if(sectionFontSize == null || "".equals("sectionFontSize") || "null".equals("sectionFontSize") ){
			sectionFontSize = "12";
		}
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		//		链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/sends/docSend!doclist.action")
			.append("', '待发公文'")
			.append(");");
		
//		获取公告列表list
		List<Object[]> list = null;
		recpage.setPageSize(num);
		//page = manager.getListForTable(user.getCurrentUser().getUserId(),page);
		Calendar rightNow = Calendar.getInstance();
		DocType docType = DocType.Send.setRule(true);
		recpage =manager.getDocPage(page, docModel, docType, false, startDate, endDate);
		List<TdocSend> result = new ArrayList();
		
		getRequest().setAttribute("dfgwlength", recpage.getTotalCount());
		list = recpage.getResult();
		
		if(list!=null){
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				Object[] re =(Object[]) list.get(i);
				//标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript: window.showModalDialog('").append(getRequest().getContextPath())
//				.append("/sends/docSend!viewDoc.action?docId="+re[0].toString())
//				.append("&showType=view', 'window','dialogWidth:1000 pt;dialogHeight:800 pt;status:no;help:no;scroll:no;minimize:yes;maximize:yes;'")
				.append("/receives/recvDoc!showDoc.action?docId="+re[0].toString())
				.append("&showType=view&tuiwen=true', 'window','dialogWidth:1000 pt;dialogHeight:800 pt;status:no;help:no;scroll:no;minimize:yes;maximize:yes;'")
				.append("); window.location.reload();");
				
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				
				String title = re[1].toString();
				if(title==null){
					title="";
				}
				
				StringBuilder tip = new StringBuilder();
				tip.append(re[1].toString())
					//.append("\n").append("发布人：" + notify.getAfficheAuthor() )
					.append("\n").append("发文文号：" +re[2].toString())
					.append("\n").append("创建日期：" + st.format(re[5]));
				//	.append("\n").append("有效期限：" + st.format(notify.getAfficheUsefulLife()));
				
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<a href=\"#\" onclick=\"").append(titleLink.toString()).append("\">")
				.append("<span style=\"font-size: "+sectionFontSize+"px;color:blue\" title=\"").append(tip).append("\">").append(title).append("</span></a>");
				innerHtml.append("</td>");
				
				/*innerHtml.append("<td width=\"100px\">");
				innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px;color:green\" title =\""+re[2].toString()+"\" class =\"linkgray10\">").append(re[2].toString()).append("</span>");
				innerHtml.append("</td>");*/
				
				//if("1".equals(showDate)){//如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"150px\">");
					innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" title =\""+st.format(re[5])+"\" class =\"linkgray10\">").append(st.format(re[5])).append("</span>");
					innerHtml.append("</td>");
				//}
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		innerHtml.append("<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ")
				 .append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>")
				 .append("<span style=\"display: none\">").append(recpage.getTotalCount()).append("</span>");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * @author:luosy
	 * @description:公文分发列表
	 * @date : 2010-8-30
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String doclist() throws Exception {
		mmdjItems = dictService.getItemsByValue("MMDJ");//秘密等级
		jjcdItems = dictService.getItemsByValue("JJCD");//紧急程度
		DocType docType = DocType.Send.setRule(true);
		page = manager.getDocPage(page, docModel, docType, false, startDate, endDate);
		return "doclist";
	}
	
	public String docReport() throws ServiceException, SQLException{
		DocType docType = DocType.Send;
		List<DocType> docTypeList = new ArrayList<DocType>(1);
		//docTypeList.add(DocType.Recycle);	//回收的公文
		docTypeList.add(DocType.Send);//待分发的公文
		docTypeList.add(DocType.Sended);//已分发的公文
		docTypeList.add(DocType.Sign);//签章的公文
		docType.setDocTypeList(docTypeList);
		List<ToaReportBean> list = manager.getPageAllListReport(page,docType,false,startDate,endDate);
		try {
			String borrowTitle="发送登记表";
			Map map = new HashMap();
			map.put("borrowTitle", startDate);
			map.put("endDate", endDate);
			totalNum=list.size();//总行数
			String jasper = "/reportfiles/tjxiha.jasper"; // 你的jasper文件地址 
			
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			if(exportType==null||"".equals(exportType)){
				//读取jasper   
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
				jasperPrint=JasperFillManager.fillReport(jasperReport, map,new DataSource(list));
				
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.generateHtmlDocReport(jasperPrint,request,response);
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//下载jdk1.6
	public void downLoad() throws Exception{ 
		String filePath = getRequest().getSession().getServletContext().getRealPath("/")+"doc\\sends\\jdk-6u12-windows-i586-p.exe";
		HttpServletResponse response = getResponse();
		boolean isOnLine = true;
		File f = new File(filePath);
		if(!f.exists()){ 
			response.sendError(404,"File not found!");
			return;
			} 
		BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
		byte[] buf = new byte[1024]; 
		int len = 0; 
		response.reset();
		//非常重要
		if(isOnLine){ //在线打开方式
			URL u = new URL("file:///"+filePath); 
			response.setContentType(u.openConnection().getContentType()); 
			response.setHeader("Content-Disposition", "inline; filename="+f.getName()); //文件名应该编码成UTF-8 
		} else{ //纯下载方式 
				response.setContentType("application/x-msdownload");  
				response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());  
			} 
		OutputStream out = response.getOutputStream();
		while((len = br.read(buf)) >0) out.write(buf,0,len);
		br.close(); 
		out.close(); 
	}
	
	
		//公文查询模块
	public void generateHtmlDocReport(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
		  int pages=0;//总页数   
		  StringBuffer htmlString = new StringBuffer();
		  StringBuffer sbuffer = new StringBuffer();
		  String contextPath = request.getContextPath();
		  if(jasperPrint.getPages().size()>0){//报表是否有数据
			  Object jaspersize=jasperPrint.getPages().get(jasperPrint.getPages().size()-1);//获取最后一页对象
			  int laftsize=((JRBasePrintPage)jaspersize).getElements().size();//获取最后一页数据量
			  if(laftsize>0){
				  pages=jasperPrint.getPages().size();
			  }else{
				  pages=jasperPrint.getPages().size()-1;
			  }
		  }
	  StringBuffer url=new StringBuffer(contextPath+"/attendance/report/attendReport!dateReport.action");
		  try {
			  PrintWriter out = response.getWriter();
			  JRHtmlExporter exporter = new JRHtmlExporter();
			  int pageIndex = currentPage;
			  if (pageIndex < 0) {
				  pageIndex = 0;
			  }
			  int lastPageIndex = 0;
			  if (pages!=0) {
				  lastPageIndex = pages-1 ;
			  }
			  if (pageIndex > lastPageIndex) {
				  pageIndex = lastPageIndex;
			  }
			  if (pages <=0) {
				  sbuffer.append("报表内容为空！");
			  }else{
				  exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
				  exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
				  exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
				  exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				  exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "utf-8");
				  exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));  
				  exporter.exportReport();
			  }
			  htmlString
			  .append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html>")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
			  .append("window.name=\"targetWindow\";\n")
				.append("function onsub(BeginDate,EndDate,serach,temp){\n")
				.append("document.getElementById(\"startDate\").value=BeginDate;\n")
				.append("document.getElementById(\"endDate\").value=EndDate;\n")
				.append("document.getElementById(\"Identification\").value=temp;\n")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='post'  target='targetWindow' action='"+contextPath+"/sends/docSend!docReport.action'>\n")
			     .append("<input type='hidden' id='startDate' name='startDate'  value=''/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='endDate' name='endDate' value=''/>\n")
			     .append("<input type='hidden' id='Identification' name='Identification' value=''/>\n")
			  	 .append("<input type='hidden' id='currentPage' name='currentPage' value='"+currentPage+"' />\n");
			  htmlString.append(" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
				 .append("<tr>\n")
				 .append("<td align='left' width=33%>\n")
				 .append("<table width='100%' cellpadding='0' cellspacing='0' border='0'><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
				 .append("<tr>\n");
			  
	            if (pageIndex > 0) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages(0)")
	                    .append("'><img src='"+contextPath+"/oa/image/query/first.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex-1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/previous.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/first_grey.GIF' border='0'></td>\n")
	                    .append("<td><img src='"+contextPath+"/oa/image/query/previous_grey.GIF' border='0'></td>\n");
	            }
	            if (pageIndex < lastPageIndex) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex+1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/next.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+lastPageIndex+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/last.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/next_grey.GIF' border='0'></td>\n")
	                    .append(" <td><img src='"+contextPath+"/oa/image/query/last_grey.GIF' border='0'></td>\n");
	            }
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
	                .append(" </tr>\n")
	                .append(" </table></td>\n")
	                .append("</tr></table>")
	                .append("</td>\n")
	                .append("</tr>\n")
	                .append("<tr>\n")
	                .append(" <td align='center'>\n")
	                .append(sbuffer)
	                .append(" </td>\n")
	                .append("</tr>\n")
	                .append("</table>\n")
	                .append("</form>")
					.append("<div></body></html>");            
	            out.println(htmlString.toString());
	            out.flush();
	            out.close();
		  } catch (Exception e) {
			  logger.error(e);
			  e.printStackTrace();
		  } finally {
			  sbuffer.setLength(0);
			  sbuffer = null;
			  htmlString.setLength(0);
			  htmlString = null;
			 url.setLength(0);
		     url = null;
		  }
	}
	
	/**
	 * @author:luosy
	 * @description: 公文查询列表
	 * @date : 2010-8-30
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String docsearch() throws Exception {
		mmdjItems = dictService.getItemsByValue("MMDJ");//秘密等级
		jjcdItems = dictService.getItemsByValue("JJCD");//紧急程度
		DocType docType = DocType.Send;
		docModel.setDocState("3");
		if(docModel.getDocState() == null || "".equals(docModel.getDocState())){//查询适合查询列表全部状态的公文
			List<DocType> docTypeList = new ArrayList<DocType>(1);
			//docTypeList.add(DocType.Recycle);	//回收的公文
			docTypeList.add(DocType.Send);//待分发的公文
			docTypeList.add(DocType.Sended);//已分发的公文
			docTypeList.add(DocType.Sign);//签章的公文
			docType.setDocTypeList(docTypeList);
		}else{
			DocType[] typeArray = DocType.values();
			for(DocType typeElement : typeArray){
				if(docModel.getDocState().equals(typeElement.getValue())){
					docType = typeElement ;
					break;
				}
			}
		}
		page = manager.getDocPage(page, docModel, docType, false, startDate, endDate);
		return "docsearch";
	}
	////公文查询列表
	public String docsearchs() throws Exception {
		mmdjItems = dictService.getItemsByValue("MMDJ");//秘密等级
		jjcdItems = dictService.getItemsByValue("JJCD");//紧急程度
		DocType docType = DocType.Send;
		docModel.setDocState("3");
		if(docModel.getDocState() == null || "".equals(docModel.getDocState())){//查询适合查询列表全部状态的公文
			List<DocType> docTypeList = new ArrayList<DocType>(1);
			//docTypeList.add(DocType.Recycle);	//回收的公文
			docTypeList.add(DocType.Send);//待分发的公文
			docTypeList.add(DocType.Sended);//已分发的公文
			docTypeList.add(DocType.Sign);//签章的公文
			docType.setDocTypeList(docTypeList);
		}else{
			DocType[] typeArray = DocType.values();
			for(DocType typeElement : typeArray){
				if(docModel.getDocState().equals(typeElement.getValue())){
					docType = typeElement ;
					break;
				}
			}
		}
		List<Object> pages=new ArrayList<Object>();
		pages = manager.getDocPageReport(docModel, docType, false, startDate, endDate);
		
		try {
			String userId = user.getCurrentUser().getUserId();
			Organization org= user.getUserDepartmentByUserId(userId);
			
			List<ToaReportBean> list=new ArrayList<ToaReportBean>();
			if(pages!=null&&pages.size()>0){
				list = manager.pageToToaReportBean(pages);    //对ToaReportBean  填数据
			}
			
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
//			List<ToaReportBean> list=manager.getBorrowReport(df.parse(reportDate),df.parse(reportEndDate),org.getOrgCode());
			String orgName=org.getOrgName();
			String borrowTitle="发送登记表";
			Map map = new HashMap();
			map.put("borrowTitle", borrowTitle);
			map.put("orgName", orgName);
			totalNum=list.size();//总行数
			String jasper = "/reportfiles/SendDOCReport.jasper"; // 你的jasper文件地址 
			
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			if(exportType==null||"".equals(exportType)){
				//读取jasper   
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
				jasperPrint=JasperFillManager.fillReport(jasperReport, map,new DataSource(list));
				
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.generateHtmlSearch(jasperPrint,request,response);
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
//		return "docsearch";
	}
	////统计报表
	public String docSearchReport() throws Exception {
		mmdjItems = dictService.getItemsByValue("MMDJ");//秘密等级
		jjcdItems = dictService.getItemsByValue("JJCD");//紧急程度
		DocType docType = DocType.Send;
		docModel.setDocState("3");
		if(docModel.getDocState() == null || "".equals(docModel.getDocState())){//查询适合查询列表全部状态的公文
			List<DocType> docTypeList = new ArrayList<DocType>(1);
			//docTypeList.add(DocType.Recycle);	//回收的公文
			docTypeList.add(DocType.Send);//待分发的公文
			docTypeList.add(DocType.Sended);//已分发的公文
			docTypeList.add(DocType.Sign);//签章的公文
			docType.setDocTypeList(docTypeList);
		}else{
			DocType[] typeArray = DocType.values();
			for(DocType typeElement : typeArray){
				if(docModel.getDocState().equals(typeElement.getValue())){
					docType = typeElement ;
					break;
				}
			}
		}
		// 对年报  月报  日报  进行控制
		if(reportTime!=null&&!"".equals(reportTime)){
			if(reportTime.length()==4){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				startDate = sdf.parse(reportTime);    //开始查询日期
				Calendar cd = Calendar.getInstance();   
                cd.setTime(startDate);   
                cd.add(Calendar.MONTH, 12);//增加12个月
                cd.add(Calendar.DATE, -1);//减少一天 
				endDate = cd.getTime();					//结束日期
			}else if(reportTime.length()==7){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				startDate = sdf.parse(reportTime);		//开始查询日期
				Calendar cd = Calendar.getInstance();   
                cd.setTime(startDate);
                cd.add(Calendar.MONTH, 1);//增加一个月 
                cd.add(Calendar.DATE, -1);//减少一天 
				endDate = cd.getTime();					//结束日期
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				startDate = sdf.parse(reportTime);		//开始查询日期
				Calendar cd = Calendar.getInstance();   
                cd.setTime(startDate);
                cd.add(Calendar.DATE, 1);//增加一天 
                endDate = cd.getTime();					//结束日期
			}
		}
		List<ToaReportBean> list=new ArrayList<ToaReportBean>();
		List<Object> pages=new ArrayList<Object>();
		if("all".equals(unitORall)){
			//  查询所有单位
			pages = manager.getDocPageReportAll(docModel, docType, false, startDate, endDate);    
		}else{
			docType=docType.dataInOrgRange(true);    //控制单位查询
			pages = manager.getDocPageReport(docModel, docType, false, startDate, endDate);
			
		}
		try {
			String userId = user.getCurrentUser().getUserId();
			Organization org= user.getUserDepartmentByUserId(userId);
			
			if(pages!=null&&pages.size()>0){
				list = manager.pageToToaReportBeanGroup(pages);
			}
			
//			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
//			List<ToaReportBean> list=manager.getBorrowReport(df.parse(reportDate),df.parse(reportEndDate),org.getOrgCode());
			String orgName=org.getOrgName();
			String borrowTitle="发送登记表";
			Map map = new HashMap();
			map.put("borrowTitle", borrowTitle);
			totalNum=list.size();//总行数
			String jasper;
			if("all".equals(unitORall)){
				jasper = "/reportfiles/SendDOCReportGroup.jasper"; // 你的jasper文件地址 
			}else{
				map.put("orgName", orgName);
				jasper = "/reportfiles/SendDOCReport.jasper"; // 你的jasper文件地址 
			}
			
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			if(exportType==null||"".equals(exportType)){
				//读取jasper   
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
				jasperPrint=JasperFillManager.fillReport(jasperReport, map,new DataSource(list));
				
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				if("all".equals(unitORall)){
					this.generateHtmlReportAll(jasperPrint,request,response);
				}else{
					this.generateHtmlReport(jasperPrint,request,response);
				}
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		
	}

	/**
	 * @author:luosy
	 * @description:   选择分发单位界面
	 * @date : 2010-8-18
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String orgTree() throws Exception {
		
		//公文号为docId的公文，状态为已收、已拒收、待收的分发机构（也就是说公文已经分发了）
		List selectedorgList = manager.getSendsorgList(docId,TdocSend.DOCSEND_RECYCLER,false);
		
		//公文号为docId的公文，状态为已回收的分发机构
		List recyorgList = manager.getSendsorgList(docId,TdocSend.DOCSEND_RECYCLER,true);
		
		StringBuilder selectedOrg = new StringBuilder();	
		for(int i=0;i<selectedorgList.size();i++){
			Object[] orgInfo = (Object[])selectedorgList.get(i);
			selectedOrg.append("<option value=").append(orgInfo[0]).append(">").append(orgInfo[1]).append("</option>");
		}
		
		StringBuilder recyorg = new StringBuilder();
		for(int i=0;i<recyorgList.size();i++){
			Object[] orgInfo = (Object[])recyorgList.get(i);
			recyorg.append("<option value=").append(orgInfo[0]).append("><span>").append(orgInfo[1]).append("</span></option>");
		}
		
		this.getRequest().setAttribute("selectedOrg",selectedOrg);
		this.getRequest().setAttribute("recyorg",recyorg);
		//获取所有单位（机构、部门）
		//orgList = user.getOrgs("0");	
		//获取所有的机构
		orgList = user.getOrgAgency("0", "1");
		orgGroupList = manager.getAgencyListByUserId();
		
		//orgList中加入一个特别的节点 
		List<Organization> orgList2 = new ArrayList<Organization>();
		Organization myorg = new Organization();
		myorg.setOrgId("001");
		myorg.setOrgGrade("0");
		myorg.setOrgIsdel("0");
		myorg.setIsOrg("1");
		myorg.setOrgName("南昌市政府办公厅");
		myorg.setOrgParentId("8a928a703bb11098013bb6756e9a004c");
		myorg.setOrgNature("0");
		myorg.setRest1("0");
		myorg.setOrgSyscode("001999000");
		myorg.setSupOrgCode("001999");
		myorg.setOrgSequence(36L);
		orgList2.add(myorg);
		
		orgList2.addAll(orgList);
		orgList = orgList2;
		
		TtransDoc doc = transDocManager.getDocById(docId);
		if(DocType.Draft.getValue().equals(doc.getDocState())
				||DocType.Sign.getValue().equals(doc.getDocState())
				||DocType.ReturnBack.getValue().equals(doc.getDocState())){		//  公文不是分发或回收状态，不能进行分发
			this.getRequest().setAttribute("flaginfo","该公文状态不能进行分发！");
			return "orgtree";
		}
		return "orgtree";
	}
	
	/**
	 * @author:xiaolj
	 * @description:   选择再次分发单位界面
	 * @date : 2013-3-29
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String orgTree2() throws Exception {
		
		//公文号为docId的公文，状态为已收、已拒收、待收的分发机构（也就是说公文已经分发了）
		List selectedorgList = manager.getSendsorgList(docId,TdocSend.DOCSEND_RECYCLER,false);
		
		//公文号为docId的公文，状态为已回收的分发机构
		List recyorgList = manager.getSendsorgList(docId,TdocSend.DOCSEND_RECYCLER,true);
		
		StringBuilder selectedOrg = new StringBuilder();	
		for(int i=0;i<selectedorgList.size();i++){
			Object[] orgInfo = (Object[])selectedorgList.get(i);
			selectedOrg.append("<option value=").append(orgInfo[0]).append(">").append(orgInfo[1]).append("</option>");
		}
		
		StringBuilder recyorg = new StringBuilder();
		for(int i=0;i<recyorgList.size();i++){
			Object[] orgInfo = (Object[])recyorgList.get(i);
			recyorg.append("<option value=").append(orgInfo[0]).append("><span>").append(orgInfo[1]).append("</span></option>");
		}
		
		this.getRequest().setAttribute("selectedOrg",selectedOrg);
		this.getRequest().setAttribute("recyorg",recyorg);
		//获取所有单位（机构、部门）
		//orgList = user.getOrgs("0");	
		//获取所有的机构
		orgList = user.getOrgAgency("0", "1");
		orgGroupList = manager.getAgencyListByUserId();
		
		//orgList中加入一个特别的节点 
		List<Organization> orgList2 = new ArrayList<Organization>();
		Organization myorg = new Organization();
		myorg.setOrgId("001");
		myorg.setOrgGrade("0");
		myorg.setOrgIsdel("0");
		myorg.setIsOrg("1");
		myorg.setOrgName("南昌市政府办公厅");
		myorg.setOrgParentId("8a928a703bb11098013bb6756e9a004c");
		myorg.setOrgNature("0");
		myorg.setRest1("0");
		myorg.setOrgSyscode("001999000");
		myorg.setSupOrgCode("001999");
		myorg.setOrgSequence(36L);
		orgList2.add(myorg);
		
		orgList2.addAll(orgList);
		orgList = orgList2;
		
		TtransDoc doc = transDocManager.getDocById(docId);
		if(DocType.Draft.getValue().equals(doc.getDocState())
				||DocType.Sign.getValue().equals(doc.getDocState())
				||DocType.ReturnBack.getValue().equals(doc.getDocState())){		//  公文不是分发或回收状态，不能进行分发
			this.getRequest().setAttribute("flaginfo","该公文状态不能进行分发！");
			return "orgtree2";
		}
		return "orgtree2";
	}
	
	/**
	 * @author:luosy
	 * @description:   选择分发单位界面
	 * @date : 2010-8-18
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String orgTrees() throws Exception {
		//获取所有的机构
		orgList = user.getOrgAgency("0", "1");
		if(orgList != null && !orgList.isEmpty()) {
			
			for(int i = 0;i<orgList.size();i++){
				
				Organization organization=orgList.get(i);
				if(organization.getOrgSyscode().equals("001999011")){
					orgList.remove(i);//过滤掉市委办公厅
					i--;					
				}
			}		
		}
		//orgList中加入一个特别的节点 
		List<Organization> orgList2 = new ArrayList<Organization>();
		Organization myorg = new Organization();
		myorg.setOrgId("001");
		myorg.setOrgGrade("0");
		myorg.setOrgIsdel("0");
		myorg.setIsOrg("1");
		myorg.setOrgName("南昌市政府办公厅");
		myorg.setOrgParentId("8a928a703bb11098013bb6756e9a004c");
		myorg.setOrgNature("0");
		myorg.setRest1("0");
		myorg.setOrgSyscode("001999000");
		myorg.setSupOrgCode("001999");
		myorg.setOrgSequence(36L);
		orgList2.add(myorg);
		orgList2.addAll(orgList);
		orgList = orgList2;
		
		return "orgTrees";
	}

	/**
	 * @author:xiaolj
	 * @description:   显示所有已经要分发的公文 
	 * @date : 2012-10-30
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String viewSendOrg() throws Exception {
		this.getRequest().setAttribute("docId", docId);
		
		TtransDoc ttransDoc = transDocManager.getDocById(docId);
		String myorgIds = ttransDoc.getDocSubmittoDepart_id();//主送单位
		String myorgIdes =ttransDoc.getDocCcDepart_id();//抄送单位

		//很玄幻的一段代码，暂时发现没啥用，先注释掉
//		if(myorgIds!=null&&!"".equals(myorgIds)){
//			myorgIds = myorgIds;
//		}else{
//			myorgIds = "";
//		}
//		if(ttransDoc.getDocCcDepart_id()!=null&&!"".equals(ttransDoc.getDocCcDepart_id())){
//			if("".endsWith(myorgIdes)){
//				myorgIdes = ttransDoc.getDocCcDepart_id();
//			}else{
//				myorgIdes = myorgIdes+ "," + ttransDoc.getDocCcDepart_id();
//			}
//		}
		//System.out.println("***********************************************************" + myorgIds);
		
		TUumsBaseOrg org;
		
		List<Object> selectedorgList = new ArrayList<Object>();
		List<Object> selectedorgLists = new ArrayList<Object>();
		if(myorgIds!=null&&!"".equals(myorgIds)){
			String[] orgId =null;
			if(myorgIds.indexOf(";")!=-1){
				orgId = myorgIds.split(";");
			}else{
				orgId = myorgIds.split(",");
			}
		
			for(int i=0;i<orgId.length;i++){
				Object[] obj = new Object[2];
				if(orgId[i].toString().equals("001999000")){
					obj[0] = "001999000";
					obj[1] = "南昌市政府办公厅";
				}else{
					org = user.getOrgInfoBySyscode(orgId[i].toString());
					
					obj[0] = (Object) org.getOrgSyscode();
					obj[1] = (Object) org.getOrgName();
				}
				selectedorgList.add(obj);
			}
			
		}
		if(myorgIdes!=null&&!"".equals(myorgIdes)){
			String[] orgId =null;
			if(myorgIdes.indexOf(";")!=-1){
				orgId = myorgIdes.split(";");
			}else{
				orgId = myorgIdes.split(",");
			}
		
			for(int i=0;i<orgId.length;i++){
				Object[] obj = new Object[2];
				if(orgId[i].toString().equals("001999000")){
					obj[0] = "001999000";
					obj[1] = "南昌市政府办公厅";
				}else{
					org = user.getOrgInfoBySyscode(orgId[i].toString());
					
					obj[0] = (Object) org.getOrgSyscode();
					obj[1] = (Object) org.getOrgName();
				}
				selectedorgLists.add(obj);
			}
			
		}
		this.getRequest().setAttribute("myselectedOrg", selectedorgList);
		this.getRequest().setAttribute("myselectedOrgs", selectedorgLists);
		this.getRequest().setAttribute("myPrint", 3);
		return "orgsend";
	}
	
	/**
	 * @author:xiaolj
	 * @description:   显示所有已经要再分发的公文 
	 * @date : 2013-3-29
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String viewSendOrg2() throws Exception {
		this.getRequest().setAttribute("docId", docId);
		
		
//		/TtransDoc ttransDoc = transDocManager.getDocById(docId);
		//String myorgIds = ttransDoc.getDocSubmittoDepart_id();//主送单位
		String myorgIds = "";//主送单位
		String myorgIdes = "";//抄送单位
		
		myorgIds = this.getRequest().getParameter("zs").toString();
		myorgIdes = this.getRequest().getParameter("cs").toString();
		
		/*if(myorgIds!=null&&!"".equals(myorgIds)){
			myorgIds = myorgIds;
		}else{
			myorgIds = "";
		}
		if(ttransDoc.getDocCcDepart_id()!=null&&!"".equals(ttransDoc.getDocCcDepart_id())){
			if("".endsWith(myorgIdes)){
				myorgIdes = ttransDoc.getDocCcDepart_id();
			}else{
				myorgIdes = myorgIdes+ "," + ttransDoc.getDocCcDepart_id();
			}
		}
		//System.out.println("***********************************************************" + myorgIds);
*/		
		TUumsBaseOrg org;
		
		List<Object> selectedorgList = new ArrayList<Object>();
		List<Object> selectedorgLists = new ArrayList<Object>();
		if(myorgIds!=null&&!"".equals(myorgIds)){
			String[] orgId =null;
			if(myorgIds.indexOf(";")!=-1){
				orgId = myorgIds.split(";");
			}else{
				orgId = myorgIds.split(",");
			}
		
			for(int i=0;i<orgId.length;i++){
				Object[] obj = new Object[2];
				if(orgId[i].toString().equals("001999000")){
					obj[0] = "001999000";
					obj[1] = "南昌市政府办公厅";
				}else{
					org = user.getOrgInfoBySyscode(orgId[i].toString());
					
					obj[0] = (Object) org.getOrgSyscode();
					obj[1] = (Object) org.getOrgName();
				}
				selectedorgList.add(obj);
			}
			
		}
		if(myorgIdes!=null&&!"".equals(myorgIdes)){
			String[] orgId =null;
			if(myorgIdes.indexOf(";")!=-1){
				orgId = myorgIdes.split(";");
			}else{
				orgId = myorgIdes.split(",");
			}
		
			for(int i=0;i<orgId.length;i++){
				Object[] obj = new Object[2];
				if(orgId[i].toString().equals("001999000")){
					obj[0] = "001999000";
					obj[1] = "南昌市政府办公厅";
				}else{
					org = user.getOrgInfoBySyscode(orgId[i].toString());
					
					obj[0] = (Object) org.getOrgSyscode();
					obj[1] = (Object) org.getOrgName();
				}
				selectedorgLists.add(obj);
			}
			
		}
		this.getRequest().setAttribute("myselectedOrg", selectedorgList);
		this.getRequest().setAttribute("myselectedOrgs", selectedorgLists);
		this.getRequest().setAttribute("myPrint", 3);
		return "orgsend";
	}
	
	/**
	 * @author:luosy
	 * @description:   分发公文 
	 * @date : 2010-8-18
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	@Override
	public String save() throws Exception {
		String userN = user.getCurrentUser().getUserName();
 
		TtransDoc doc = transDocManager.getDocById(docId);
		//如果当前公文状态是已发送，则说明当前操作是新增发送单位，因此将当前添加的发送单位保存
		if(DocType.Sended.getValue().equals( doc.getDocState())){
			String[] zSend = devideStr(orgIds);
			String[] cSend = devideStr(orgIdss);
			if(doc.getDocSubmittoDepart_id() != "" && doc.getDocSubmittoDepart_id() != null){
				doc.setDocSubmittoDepart_id(doc.getDocSubmittoDepart_id() + "," + zSend[0]);
				doc.setDocSubmittoDepart(doc.getDocSubmittoDepart() + "," + zSend[1]);
			}else {
				doc.setDocSubmittoDepart_id(zSend[0]);
				doc.setDocSubmittoDepart(zSend[1]);
			}
			
			if(doc.getDocCcDepart_id() != "" && doc.getDocCcDepart_id() != null){
				doc.setDocCcDepart_id(doc.getDocCcDepart_id() + "," + zSend[0]);
				doc.setDocCcDepart(doc.getDocCcDepart() + "," + cSend[1]);
			}else {
				doc.setDocCcDepart_id(cSend[0]);
				doc.setDocCcDepart(cSend[1]);
			}
			
		}
		manager.saveDocSends(doc, orgIds,defPrint,orgIdss,defPrints, new OALogInfo(userN+" 分发公文《"+doc.getDocTitle()+"》"));
		return renderText("0");
	}
    /**
     * 
      * @method devideStr
      * @param srcStr  源字符串
      * @return
      * @return String[] 
      * @description  拆分save方法中的字符串：单位ID和姓名
      * @author 彭欣(PengXin)
      * @created 2013-4-28
     */
	private String[] devideStr(String srcStr){
		String[] rtnVal = new String[2];
		rtnVal[0] = rtnVal[1] = "";
		if("".equals(srcStr) || srcStr == null)
			return rtnVal;
		String[] sendStr = srcStr.split(";");
		for(String str : sendStr){
			String[] tmp = str.split(",");
			if(rtnVal[0] != "" && rtnVal[0] != null )
			{
				rtnVal[0] += "," + tmp[0];
				rtnVal[1] += "," + tmp[1];
			}
			else
			{
				rtnVal[0] = tmp[0];
				rtnVal[1] = tmp[1];
			}
			
		}
		return rtnVal;
	}
	
	public HashMap<String, String> getDocstate() {
		return docstate;
	}
	
	
	/**
	 * 统计收文登记情况
	 * @return
	 */
	public String getBorrowReport(){
		try {
			String userId = user.getCurrentUser().getUserId();
			Organization org= user.getUserDepartmentByUserId(userId);
			
			String orgcode;
			if(org.getOrgCode()!=null&&!"".equals(org.getOrgCode())){
				orgcode = org.getOrgCode();
			}else orgcode = org.getSupOrgCode();
			
			// 查询一天的日期
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			Date date = df.parse(reportEndDate);		//开始查询日期
			Calendar cd = Calendar.getInstance();   
            cd.setTime(date);
            cd.add(Calendar.DATE, 1);//增加一天 
            endDate = cd.getTime();					//结束日期
            
			List<ToaReportBean> list=manager.getBorrowReport(df.parse(reportDate),endDate,orgcode,docModel);
			String borrow=manager.getDateToString(df.parse(reportDate), "yyyy-MM-dd");//报表参数
			String endDate=manager.getDateToString(df.parse(reportEndDate), "yyyy-MM-dd");//报表参数
			String orgName=org.getOrgName();
			String borrowTitle="收文登记表";
			Map map = new HashMap();
			map.put("borrowTitle", borrowTitle);
			map.put("borrowdate", borrow);
			map.put("endDate", endDate);
			map.put("orgName", orgName);
			totalNum=list.size();//总行数
			String jasper = "/reportfiles/AcceptDOC.jasper"; // 你的jasper文件地址 
			
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			if(exportType==null||"".equals(exportType)){
				//读取jasper   
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
				jasperPrint=JasperFillManager.fillReport(jasperReport, map,new DataSource(list));
				
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.generateHtml(jasperPrint,request,response);
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//////////报表部分__ 公文查询 __  收文查询
	public String getBorrowReports(){
		try {
			String userId = user.getCurrentUser().getUserId();
			Organization org= user.getUserDepartmentByUserId(userId);
			
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			List<ToaReportBean> list = new ArrayList<ToaReportBean>();
			
			String orgcode;
			if(org.getOrgCode()!=null&&!"".equals(org.getOrgCode())){
				orgcode = org.getOrgCode();
			}else orgcode = org.getSupOrgCode();
			
			list=manager.getBorrowReports(docModel,orgcode);
//			String borrow=manager.getDateToString(df.parse(reportDate), "yyyy-MM-dd");//报表参数
//			String endDate=manager.getDateToString(df.parse(reportEndDate), "yyyy-MM-dd");//报表参数
			String orgName=org.getOrgName();
			String borrowTitle="收文登记表";
			Map map = new HashMap();
			map.put("borrowTitle", borrowTitle);
//			map.put("borrowdate", borrow);
//			map.put("endDate", endDate);
			map.put("orgName", orgName);
			totalNum=list.size();//总行数
			String jasper = "/reportfiles/AcceptDOCReport.jasper"; // 你的jasper文件地址 
			
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			if(exportType==null||"".equals(exportType)){
				//读取jasper   
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
				jasperPrint=JasperFillManager.fillReport(jasperReport, map,new DataSource(list));
				
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.generateHtmlReports(jasperPrint,request,response);
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 统计发文登记情况
	 * @return
	 */
	public String getBorrowReportSend(){
		try {
			String userId = user.getCurrentUser().getUserId();
			Organization org= user.getUserDepartmentByUserId(userId);
			String orgcode;
			if(org.getOrgCode()!=null&&!"".equals(org.getOrgCode())){
				orgcode = org.getOrgCode();
			}else orgcode = org.getSupOrgCode();
			
			// 查询一天的日期
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			Date date = df.parse(reportEndDate);		//开始查询日期
			Calendar cd = Calendar.getInstance();   
            cd.setTime(date);
            cd.add(Calendar.DATE, 1);//增加一天 
            endDate = cd.getTime();					//结束日期
            
			List<ToaReportBean> list=manager.getBorrowReportSend(df.parse(reportDate),endDate,orgcode,docModel);
			String borrow=manager.getDateToString(df.parse(reportDate), "yyyy-MM-dd");//报表参数
			String endDate=manager.getDateToString(df.parse(reportEndDate), "yyyy-MM-dd");//报表参数
			String orgName=org.getOrgName();
//			SimpleDateFormat dfs=new SimpleDateFormat("yyyy");
//			String dates = dfs.format(new Date());
			String borrowTitle="发文登记表";
			Map map = new HashMap();
			map.put("borrowTitle", borrowTitle);
			map.put("borrowdate", borrow);
			map.put("endDate", endDate);
			map.put("orgName", orgName);
			totalNum=list.size();//总行数
			String jasper = "/reportfiles/SendDOC.jasper"; // 你的jasper文件地址 
			
			HttpSession session = this.getSession();
			HttpServletRequest request=this.getRequest();
			HttpServletResponse response=this.getResponse();
			JasperPrint jasperPrint=(JasperPrint) session.getAttribute("mysessionkey");
			if(exportType==null||"".equals(exportType)){
				//读取jasper   
				JasperReport jasperReport = null;
				File exe_rpt = new File(this.getRequest().getRealPath(jasper));
				jasperReport = (JasperReport) JRLoader.loadObject(exe_rpt.getPath());
				jasperPrint=JasperFillManager.fillReport(jasperReport, map,new DataSource(list));
				
			}
			session.setAttribute("mysessionkey", jasperPrint);
			if(exportType!=null&&"excel".equals(exportType)){
				this.printToExcel(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"pdf".equals(exportType)){
				this.printToPDF(jasperPrint, request, response,borrowTitle);
			}else if(exportType!=null&&"print".equals(exportType)){
				this.printToPrinter(jasperPrint, request, response);
			}else{
				this.generateHtmlSend(jasperPrint,request,response);
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void generateHtml(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
		  int pages=0;//总页数   
		  StringBuffer htmlString = new StringBuffer();
		  StringBuffer sbuffer = new StringBuffer();
		  String contextPath = request.getContextPath();
		  if(jasperPrint.getPages().size()>0){//报表是否有数据
			  Object jaspersize=jasperPrint.getPages().get(jasperPrint.getPages().size()-1);//获取最后一页对象
			  int laftsize=((JRBasePrintPage)jaspersize).getElements().size();//获取最后一页数据量
			  if(laftsize>0){
				  pages=jasperPrint.getPages().size();
			  }else{
				  pages=jasperPrint.getPages().size()-1;
			  }
		  }
	  StringBuffer url=new StringBuffer(contextPath+"/attendance/report/attendReport!dateReport.action");
		  try {
			  PrintWriter out = response.getWriter();
			  JRHtmlExporter exporter = new JRHtmlExporter();
			  int pageIndex = currentPage;
			  if (pageIndex < 0) {
				  pageIndex = 0;
			  }
			  int lastPageIndex = 0;
			  if (pages!=0) {
				  lastPageIndex = pages-1 ;
			  }
			  if (pageIndex > lastPageIndex) {
				  pageIndex = lastPageIndex;
			  }
			  if (pages <=0) {
				  sbuffer.append("报表内容为空！");
			  }else{
				  exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
				  exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
				  exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
				  exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				  exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "utf-8");
				  exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));  
				  exporter.exportReport();
			  }
			  htmlString
			  .append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html>")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
				.append("function onsub(temp,temp1,date,endDate){\n")
				.append("if(temp==\"title\"){\n")
				.append("document.getElementById(\"docModel.docTitle\").value=temp1;\n")
				.append("document.getElementById(\"docModel.docCode\").value=\"\";\n")
				.append("}else{\n")
				.append("document.getElementById(\"docModel.docCode\").value=temp1;\n")
				.append("document.getElementById(\"docModel.docTitle\").value=\"\";\n")
				.append("}")
				.append("$('#reportDate').val(date);\n")
				.append("$('#reportEndDate').val(endDate);\n")
				.append("document.forms[0].action='"+contextPath+"/sends/docSend!getBorrowReport.action';")
			    .append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='get'  target='targetWindow' action='"+contextPath+"/sends/docSend!getBorrowReport.action'>\n")
			     .append("<input type='hidden' id='docModel.docTitle' name='docModel.docTitle'  value='"+docModel.getDocTitle()+"'/>\n")
			     .append("<input type='hidden' id='docModel.docCode' name='docModel.docCode'  value='"+docModel.getDocCode()+"'/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='reportDate' name='reportDate' value='"+reportDate+"'/>\n")
			       .append("<input type='hidden' id='reportEndDate' name='reportEndDate' value='"+reportEndDate+"'/>\n")
			  	 .append("<input type='hidden' id='currentPage' name='currentPage' value='"+currentPage+"' />\n");
			  htmlString.append(" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
				 .append("<tr>\n")
				 .append("<td align='left' width=33%>\n")
				 .append("<table width='100%' cellpadding='0' cellspacing='0' border='0'><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
				 .append("<tr>\n");
			  
	            if (pageIndex > 0) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages(0)")
	                    .append("'><img src='"+contextPath+"/oa/image/query/first.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex-1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/previous.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/first_grey.GIF' border='0'></td>\n")
	                    .append("<td><img src='"+contextPath+"/oa/image/query/previous_grey.GIF' border='0'></td>\n");
	            }
	            if (pageIndex < lastPageIndex) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex+1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/next.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+lastPageIndex+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/last.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/next_grey.GIF' border='0'></td>\n")
	                    .append(" <td><img src='"+contextPath+"/oa/image/query/last_grey.GIF' border='0'></td>\n");
	            }
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
	                .append(" </tr>\n")
	                .append(" </table></td>\n")
	                .append("</tr></table>")
	                .append("</td>\n")
	                .append("</tr>\n")
	                .append("<tr>\n")
	                .append(" <td align='center'>\n")
	                .append(sbuffer)
	                .append(" </td>\n")
	                .append("</tr>\n")
	                .append("</table>\n")
	                .append("</form>")
					.append("<div></body></html>");            
	            out.println(htmlString.toString());
	            out.flush();
	            out.close();
		  } catch (Exception e) {
			  logger.error(e);
			  e.printStackTrace();
		  } finally {
			  sbuffer.setLength(0);
			  sbuffer = null;
			  htmlString.setLength(0);
			  htmlString = null;
			 url.setLength(0);
		     url = null;
		  }
	}

	public void generateHtmlReports(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
		  int pages=0;//总页数   
		  StringBuffer htmlString = new StringBuffer();
		  StringBuffer sbuffer = new StringBuffer();
		  String contextPath = request.getContextPath();
		  if(jasperPrint.getPages().size()>0){//报表是否有数据
			  Object jaspersize=jasperPrint.getPages().get(jasperPrint.getPages().size()-1);//获取最后一页对象
			  int laftsize=((JRBasePrintPage)jaspersize).getElements().size();//获取最后一页数据量
			  if(laftsize>0){
				  pages=jasperPrint.getPages().size();
			  }else{
				  pages=jasperPrint.getPages().size()-1;
			  }
		  }
	  StringBuffer url=new StringBuffer(contextPath+"/attendance/report/attendReport!dateReport.action");
		  try {
			  PrintWriter out = response.getWriter();
			  JRHtmlExporter exporter = new JRHtmlExporter();
			  int pageIndex = currentPage;
			  if (pageIndex < 0) {
				  pageIndex = 0;
			  }
			  int lastPageIndex = 0;
			  if (pages!=0) {
				  lastPageIndex = pages-1 ;
			  }
			  if (pageIndex > lastPageIndex) {
				  pageIndex = lastPageIndex;
			  }
			  if (pages <=0) {
				  sbuffer.append("报表内容为空！");
			  }else{
				  exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
				  exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
				  exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
				  exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				  exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "utf-8");
				  exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));  
				  exporter.exportReport();
			  }
			  htmlString
			  .append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html>")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
			  .append("window.name=\"targetWindow\";\n")
				.append("function onsub(serach,temp,sumer){\n")
				.append("if(serach==\"fasong\"){\n")
				.append("document.getElementById(\"myTableForm\").action =\""+contextPath+"/sends/docSend!docsearchs.action\"; \n")
				.append("}else{\n")
				.append("document.getElementById(\"myTableForm\").action =\""+contextPath+"/sends/docSend!getBorrowReports.action\";\n")
				.append("}\n")
				.append("if(temp==\"title\"){\n")
				.append("document.getElementById(\"docModel.docTitle\").value=sumer;\n")
				.append("document.getElementById(\"docModel.docCode\").value='';\n")
				.append("}else{\n")
				.append("document.getElementById(\"docModel.docCode\").value=sumer;\n")
				.append("document.getElementById(\"docModel.docTitle\").value='';\n")
				.append("}\n")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='post'  target='targetWindow' action='"+contextPath+"/sends/docSend!getBorrowReports.action'>\n")
			     .append("<input type='hidden' id='docModel.docTitle' name='docModel.docTitle'  value='"+docModel.getDocTitle()+"'/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='docModel.docCode' name='docModel.docCode' value='"+docModel.getDocCode()+"'/>\n")
			  	 .append("<input type='hidden' id='currentPage' name='currentPage' value='"+currentPage+"' />\n");
			  htmlString.append(" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
				 .append("<tr>\n")
				 .append("<td align='left' width=33%>\n")
				 .append("<table width='100%' cellpadding='0' cellspacing='0' border='0'><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
				 .append("<tr>\n");
			  
	            if (pageIndex > 0) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages(0)")
	                    .append("'><img src='"+contextPath+"/oa/image/query/first.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex-1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/previous.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/first_grey.GIF' border='0'></td>\n")
	                    .append("<td><img src='"+contextPath+"/oa/image/query/previous_grey.GIF' border='0'></td>\n");
	            }
	            if (pageIndex < lastPageIndex) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex+1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/next.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+lastPageIndex+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/last.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/next_grey.GIF' border='0'></td>\n")
	                    .append(" <td><img src='"+contextPath+"/oa/image/query/last_grey.GIF' border='0'></td>\n");
	            }
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
	                .append(" </tr>\n")
	                .append(" </table></td>\n")
	                .append("</tr></table>")
	                .append("</td>\n")
	                .append("</tr>\n")
	                .append("<tr>\n")
	                .append(" <td align='center'>\n")
	                .append(sbuffer)
	                .append(" </td>\n")
	                .append("</tr>\n")
	                .append("</table>\n")
	                .append("</form>")
					.append("<div></body></html>");            
	            out.println(htmlString.toString());
	            out.flush();
	            out.close();
		  } catch (Exception e) {
			  logger.error(e);
			  e.printStackTrace();
		  } finally {
			  sbuffer.setLength(0);
			  sbuffer = null;
			  htmlString.setLength(0);
			  htmlString = null;
			 url.setLength(0);
		     url = null;
		  }
	}
	//公文查询模块
	public void generateHtmlSearch(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
		  int pages=0;//总页数   
		  StringBuffer htmlString = new StringBuffer();
		  StringBuffer sbuffer = new StringBuffer();
		  String contextPath = request.getContextPath();
		  if(jasperPrint.getPages().size()>0){//报表是否有数据
			  Object jaspersize=jasperPrint.getPages().get(jasperPrint.getPages().size()-1);//获取最后一页对象
			  int laftsize=((JRBasePrintPage)jaspersize).getElements().size();//获取最后一页数据量
			  if(laftsize>0){
				  pages=jasperPrint.getPages().size();
			  }else{
				  pages=jasperPrint.getPages().size()-1;
			  }
		  }
	  StringBuffer url=new StringBuffer(contextPath+"/attendance/report/attendReport!dateReport.action");
		  try {
			  PrintWriter out = response.getWriter();
			  JRHtmlExporter exporter = new JRHtmlExporter();
			  int pageIndex = currentPage;
			  if (pageIndex < 0) {
				  pageIndex = 0;
			  }
			  int lastPageIndex = 0;
			  if (pages!=0) {
				  lastPageIndex = pages-1 ;
			  }
			  if (pageIndex > lastPageIndex) {
				  pageIndex = lastPageIndex;
			  }
			  if (pages <=0) {
				  sbuffer.append("报表内容为空！");
			  }else{
				  exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
				  exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
				  exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
				  exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				  exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "utf-8");
				  exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));  
				  exporter.exportReport();
			  }
			  htmlString
			  .append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html>")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
			  .append("window.name=\"targetWindow\";\n")
				.append("function onsub(serach,temp,sumer){\n")
				.append("if(serach==\"fasong\"){\n")
				.append("document.getElementById(\"myTableForm\").action =\""+contextPath+"/sends/docSend!docsearchs.action\"; \n")
				.append("}else{\n")
				.append("document.getElementById(\"myTableForm\").action =\""+contextPath+"/sends/docSend!getBorrowReports.action\";\n")
				.append("}\n")
				.append("if(temp==\"title\"){\n")
				.append("document.getElementById(\"docModel.docTitle\").value=sumer;\n")
				.append("document.getElementById(\"docModel.docCode\").value='';\n")
				.append("}else{\n")
				.append("document.getElementById(\"docModel.docCode\").value=sumer;\n")
				.append("document.getElementById(\"docModel.docTitle\").value='';\n")
				.append("}\n")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='post'  target='targetWindow' action='"+contextPath+"/sends/docSend!docsearchs.action'>\n")
			     .append("<input type='hidden' id='docModel.docTitle' name='docModel.docTitle'  value='"+docModel.getDocTitle()+"'/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='docModel.docCode' name='docModel.docCode' value='"+docModel.getDocCode()+"'/>\n")
			  	 .append("<input type='hidden' id='currentPage' name='currentPage' value='"+currentPage+"' />\n");
			  htmlString.append(" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
				 .append("<tr>\n")
				 .append("<td align='left' width=33%>\n")
				 .append("<table width='100%' cellpadding='0' cellspacing='0' border='0'><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
				 .append("<tr>\n");
			  
	            if (pageIndex > 0) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages(0)")
	                    .append("'><img src='"+contextPath+"/oa/image/query/first.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex-1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/previous.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/first_grey.GIF' border='0'></td>\n")
	                    .append("<td><img src='"+contextPath+"/oa/image/query/previous_grey.GIF' border='0'></td>\n");
	            }
	            if (pageIndex < lastPageIndex) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex+1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/next.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+lastPageIndex+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/last.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/next_grey.GIF' border='0'></td>\n")
	                    .append(" <td><img src='"+contextPath+"/oa/image/query/last_grey.GIF' border='0'></td>\n");
	            }
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
	                .append(" </tr>\n")
	                .append(" </table></td>\n")
	                .append("</tr></table>")
	                .append("</td>\n")
	                .append("</tr>\n")
	                .append("<tr>\n")
	                .append(" <td align='center'>\n")
	                .append(sbuffer)
	                .append(" </td>\n")
	                .append("</tr>\n")
	                .append("</table>\n")
	                .append("</form>")
					.append("<div></body></html>");            
	            out.println(htmlString.toString());
	            out.flush();
	            out.close();
		  } catch (Exception e) {
			  logger.error(e);
			  e.printStackTrace();
		  } finally {
			  sbuffer.setLength(0);
			  sbuffer = null;
			  htmlString.setLength(0);
			  htmlString = null;
			 url.setLength(0);
		     url = null;
		  }
	}
	//报表模块
	public void generateHtmlReport(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
		  int pages=0;//总页数   
		  StringBuffer htmlString = new StringBuffer();
		  StringBuffer sbuffer = new StringBuffer();
		  String contextPath = request.getContextPath();
		  if(jasperPrint.getPages().size()>0){//报表是否有数据
			  Object jaspersize=jasperPrint.getPages().get(jasperPrint.getPages().size()-1);//获取最后一页对象
			  int laftsize=((JRBasePrintPage)jaspersize).getElements().size();//获取最后一页数据量
			  if(laftsize>0){
				  pages=jasperPrint.getPages().size();
			  }else{
				  pages=jasperPrint.getPages().size()-1;
			  }
		  }
	  StringBuffer url=new StringBuffer(contextPath+"/attendance/report/attendReport!dateReport.action");
		  try {
			  PrintWriter out = response.getWriter();
			  JRHtmlExporter exporter = new JRHtmlExporter();
			  int pageIndex = currentPage;
			  if (pageIndex < 0) {
				  pageIndex = 0;
			  }
			  int lastPageIndex = 0;
			  if (pages!=0) {
				  lastPageIndex = pages-1 ;
			  }
			  if (pageIndex > lastPageIndex) {
				  pageIndex = lastPageIndex;
			  }
			  if (pages <=0) {
				  sbuffer.append("报表内容为空！");
			  }else{
				  exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
				  exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
				  exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
				  exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				  exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "utf-8");
				  exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));  
				  exporter.exportReport();
			  }
			  htmlString
			  .append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html>")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
			  .append("window.name=\"targetWindow\";\n")
				.append("function onsub(date,temp){\n")
				.append("document.getElementById(\"reportTime\").value=date;\n")
				.append("document.getElementById(\"unitORall\").value=temp; \n")
				.append("document.getElementById(\"myTableForm\").submit(); \n")		
				.append("}\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='post'  target='targetWindow' action='"+contextPath+"/sends/docSend!docSearchReport.action'>\n")
			     .append("<input type='hidden' id='reportTime' name='reportTime'  value=''/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='unitORall' name='unitORall' value=''/>\n")
			  	 .append("<input type='hidden' id='currentPage' name='currentPage' value='"+currentPage+"' />\n");
			  htmlString.append(" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
				 .append("<tr>\n")
				 .append("<td align='left' width=33%>\n")
				 .append("<table width='100%' cellpadding='0' cellspacing='0' border='0'><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
				 .append("<tr>\n");
			  
	            if (pageIndex > 0) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages(0)")
	                    .append("'><img src='"+contextPath+"/oa/image/query/first.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex-1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/previous.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/first_grey.GIF' border='0'></td>\n")
	                    .append("<td><img src='"+contextPath+"/oa/image/query/previous_grey.GIF' border='0'></td>\n");
	            }
	            if (pageIndex < lastPageIndex) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex+1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/next.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+lastPageIndex+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/last.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/next_grey.GIF' border='0'></td>\n")
	                    .append(" <td><img src='"+contextPath+"/oa/image/query/last_grey.GIF' border='0'></td>\n");
	            }
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
	                .append(" </tr>\n")
	                .append(" </table></td>\n")
	                .append("</tr></table>")
	                .append("</td>\n")
	                .append("</tr>\n")
	                .append("<tr>\n")
	                .append(" <td align='center'>\n")
	                .append(sbuffer)
	                .append(" </td>\n")
	                .append("</tr>\n")
	                .append("</table>\n")
	                .append("</form>")
					.append("<div></body></html>");            
	            out.println(htmlString.toString());
	            out.flush();
	            out.close();
		  } catch (Exception e) {
			  logger.error(e);
			  e.printStackTrace();
		  } finally {
			  sbuffer.setLength(0);
			  sbuffer = null;
			  htmlString.setLength(0);
			  htmlString = null;
			 url.setLength(0);
		     url = null;
		  }
	}
	//报表模块  all
	public void generateHtmlReportAll(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
		  int pages=0;//总页数   
		  StringBuffer htmlString = new StringBuffer();
		  StringBuffer sbuffer = new StringBuffer();
		  String contextPath = request.getContextPath();
		  if(jasperPrint.getPages().size()>0){//报表是否有数据
			  Object jaspersize=jasperPrint.getPages().get(jasperPrint.getPages().size()-1);//获取最后一页对象
			  int laftsize=((JRBasePrintPage)jaspersize).getElements().size();//获取最后一页数据量
			  if(laftsize>0){
				  pages=jasperPrint.getPages().size();
			  }else{
				  pages=jasperPrint.getPages().size()-1;
			  }
		  }
	  StringBuffer url=new StringBuffer(contextPath+"/attendance/report/attendReport!dateReport.action");
		  try {
			  PrintWriter out = response.getWriter();
			  JRHtmlExporter exporter = new JRHtmlExporter();
			  int pageIndex = currentPage;
			  if (pageIndex < 0) {
				  pageIndex = 0;
			  }
			  int lastPageIndex = 0;
			  if (pages!=0) {
				  lastPageIndex = pages-1 ;
			  }
			  if (pageIndex > lastPageIndex) {
				  pageIndex = lastPageIndex;
			  }
			  if (pages <=0) {
				  sbuffer.append("报表内容为空！");
			  }else{
				  exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
				  exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
				  exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
				  exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				  exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "utf-8");
				  exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));  
				  exporter.exportReport();
			  }
			  htmlString
			  .append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html>")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
			  .append("window.name=\"targetWindow\";\n")
				.append("function onsub(date,temp){\n")
				.append("document.getElementById(\"reportTime\").value=date;\n")
				.append("document.getElementById(\"unitORall\").value=temp; \n")
				.append("document.getElementById(\"myTableForm\").submit(); \n")		
				.append("}\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#unitORall').val(\"all\");")  //控制
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='post'  target='targetWindow' action='"+contextPath+"/sends/docSend!docSearchReport.action'>\n")
			     .append("<input type='hidden' id='reportTime' name='reportTime'  value=''/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='unitORall' name='unitORall' value=''/>\n")
			  	 .append("<input type='hidden' id='currentPage' name='currentPage' value='"+currentPage+"' />\n");
			  htmlString.append(" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
				 .append("<tr>\n")
				 .append("<td align='left' width=33%>\n")
				 .append("<table width='100%' cellpadding='0' cellspacing='0' border='0'><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
				 .append("<tr>\n");
			  
	            if (pageIndex > 0) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages(0)")
	                    .append("'><img src='"+contextPath+"/oa/image/query/first.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex-1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/previous.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/first_grey.GIF' border='0'></td>\n")
	                    .append("<td><img src='"+contextPath+"/oa/image/query/previous_grey.GIF' border='0'></td>\n");
	            }
	            if (pageIndex < lastPageIndex) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex+1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/next.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+lastPageIndex+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/last.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/next_grey.GIF' border='0'></td>\n")
	                    .append(" <td><img src='"+contextPath+"/oa/image/query/last_grey.GIF' border='0'></td>\n");
	            }
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
	                .append(" </tr>\n")
	                .append(" </table></td>\n")
	                .append("</tr></table>")
	                .append("</td>\n")
	                .append("</tr>\n")
	                .append("<tr>\n")
	                .append(" <td align='center'>\n")
	                .append(sbuffer)
	                .append(" </td>\n")
	                .append("</tr>\n")
	                .append("</table>\n")
	                .append("</form>")
					.append("<div></body></html>");            
	            out.println(htmlString.toString());
	            out.flush();
	            out.close();
		  } catch (Exception e) {
			  logger.error(e);
			  e.printStackTrace();
		  } finally {
			  sbuffer.setLength(0);
			  sbuffer = null;
			  htmlString.setLength(0);
			  htmlString = null;
			 url.setLength(0);
		     url = null;
		  }
	}
	public void generateHtmlSend(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response) throws Exception{
		  int pages=0;//总页数   
		  StringBuffer htmlString = new StringBuffer();
		  StringBuffer sbuffer = new StringBuffer();
		  String contextPath = request.getContextPath();
		  if(jasperPrint.getPages().size()>0){//报表是否有数据
			  Object jaspersize=jasperPrint.getPages().get(jasperPrint.getPages().size()-1);//获取最后一页对象
			  int laftsize=((JRBasePrintPage)jaspersize).getElements().size();//获取最后一页数据量
			  if(laftsize>0){
				  pages=jasperPrint.getPages().size();
			  }else{
				  pages=jasperPrint.getPages().size()-1;
			  }
		  }
	  StringBuffer url=new StringBuffer(contextPath+"/attendance/report/attendReport!dateReport.action");
		  try {
			  PrintWriter out = response.getWriter();
			  JRHtmlExporter exporter = new JRHtmlExporter();
			  int pageIndex = currentPage;
			  if (pageIndex < 0) {
				  pageIndex = 0;
			  }
			  int lastPageIndex = 0;
			  if (pages!=0) {
				  lastPageIndex = pages-1 ;
			  }
			  if (pageIndex > lastPageIndex) {
				  pageIndex = lastPageIndex;
			  }
			  if (pages <=0) {
				  sbuffer.append("报表内容为空！");
			  }else{
				  exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,Boolean.FALSE);
				  exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
				  exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER,sbuffer);
				  exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				  exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "utf-8");
				  exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));  
				  exporter.exportReport();
			  }
			  htmlString
			  .append("<%@ page contentType=\"text/html; charset=UTF-8\" %>")
				.append("<html>")
				.append(
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<head>")
				.append("<script src='"+contextPath+"/common/js/jquery/jquery-1.2.6.js' type='text/javascript'></script>\n")
				.append("<script language='javascript'>\n")
				.append("function onsub(temp,temp1,date,endDate){\n")
				.append("if(temp==\"title\"){\n")
				.append("document.getElementById(\"docModel.docTitle\").value=temp1;\n")
				.append("document.getElementById(\"docModel.docCode\").value=\"\";\n")
				.append("}else{\n")
				.append("document.getElementById(\"docModel.docCode\").value=temp1;\n")
				.append("document.getElementById(\"docModel.docTitle\").value=\"\";\n")
				.append("}")
				.append("$('#reportDate').val(date);\n")
				.append("$('#reportEndDate').val(endDate);\n")
				.append("document.forms[0].action='"+contextPath+"/sends/docSend!getBorrowReportSend.action';")
			    .append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("function goPages(page){\n")
				.append("$('#currentPage').val(page);")
				.append("$('#myTableForm').submit();")
				.append(" }\n")
				.append("</script>")
				.append("</head>")
				.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\" oncontextmenu=\"\">")
			    .append(" <div id=\"contentborder\" align=\"center\">");
			  htmlString.append("<form id='myTableForm'  method='get'  target='targetWindow' action='"+contextPath+"/sends/docSend!getBorrowReportSend.action'>\n")
			     .append("<input type='hidden' id='docModel.docTitle' name='docModel.docTitle'  value='"+docModel.getDocTitle()+"'/>\n")
			     .append("<input type='hidden' id='docModel.docCode' name='docModel.docCode'  value='"+docModel.getDocCode()+"'/>\n")
			     .append("<input type='hidden' id='totalNum' name='totalNum' value='"+totalNum+"'/>\n")
			     .append("<input type='hidden' id='reportDate' name='reportDate' value='"+reportDate+"'/>\n")
			       .append("<input type='hidden' id='reportEndDate' name='reportEndDate' value='"+reportEndDate+"'/>\n")
			  	 .append("<input type='hidden' id='currentPage' name='currentPage' value='"+currentPage+"' />\n");
			  htmlString.append(" <table cellpadding='0' cellspacing='0' border='0' width='778' align=center>")
				 .append("<tr>\n")
				 .append("<td align='left' width=33%>\n")
				 .append("<table width='100%' cellpadding='0' cellspacing='0' border='0'><tr><td><table width='100%' cellpadding='0' cellspacing='0' border='0'>\n")
				 .append("<tr>\n");
			  
	            if (pageIndex > 0) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages(0)")
	                    .append("'><img src='"+contextPath+"/oa/image/query/first.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex-1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/previous.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/first_grey.GIF' border='0'></td>\n")
	                    .append("<td><img src='"+contextPath+"/oa/image/query/previous_grey.GIF' border='0'></td>\n");
	            }
	            if (pageIndex < lastPageIndex) {
	                htmlString.append(" <td><a href='")
	                    .append("javascript:goPages("+(pageIndex+1)+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/next.GIF' border='0'></a></td>\n")
	                    .append(" <td><a href='")
	                    .append("javascript:goPages("+lastPageIndex+")")
	                    .append("'><img src='"+contextPath+"/oa/image/query/last.GIF' border='0'></a></td>\n");
	            }else {
	                htmlString.append(" <td><img src='"+contextPath+"/oa/image/query/next_grey.GIF' border='0'></td>\n")
	                    .append(" <td><img src='"+contextPath+"/oa/image/query/last_grey.GIF' border='0'></td>\n");
	            }
	            
	            htmlString.append(" <td width='100%'>&nbsp;&nbsp;"+(pageIndex+1)+"/"+pages+"&nbsp;&nbsp;共"+totalNum+"条记录</td>\n")
	                .append(" </tr>\n")
	                .append(" </table></td>\n")
	                .append("</tr></table>")
	                .append("</td>\n")
	                .append("</tr>\n")
	                .append("<tr>\n")
	                .append(" <td align='center'>\n")
	                .append(sbuffer)
	                .append(" </td>\n")
	                .append("</tr>\n")
	                .append("</table>\n")
	                .append("</form>")
					.append("<div></body></html>");            
	            out.println(htmlString.toString());
	            out.flush();
	            out.close();
		  } catch (Exception e) {
			  logger.error(e);
			  e.printStackTrace();
		  } finally {
			  sbuffer.setLength(0);
			  sbuffer = null;
			  htmlString.setLength(0);
			  htmlString = null;
			 url.setLength(0);
		     url = null;
		  }
	}

	public void printToPrinter(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response)throws Exception{
		try{
			ServletOutputStream ouputStream = response.getOutputStream();	
		//	JasperPrintManager.printReport(jasperPrint, true);//直接打印,不用预览PDF直接打印  true为弹出打印机选择.false为直接打印
			response.setContentType("application/octet-stream");
			ObjectOutputStream oos = new ObjectOutputStream(ouputStream);
			oos.writeObject(jasperPrint);//将JasperPrint对象写入对象输出流中 
			oos.flush();
			oos.close();  
			ouputStream.flush();
			ouputStream.close();	
		}catch (Exception e) {
			logger.error(e);
			throw e;
		}
    }


    public void printToExcel(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response,String struct) throws Exception {
    	ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		JRXlsExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,Boolean.TRUE);
		exporter.exportReport();
		byte[] bytes = oStream.toByteArray();
		if (bytes != null && bytes.length > 0) {
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment; filename="+ java.net.URLEncoder.encode(struct+".xls","utf-8"));
			response.setContentLength(bytes.length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(bytes, 0, bytes.length);
			outputStream.flush();
			outputStream.close();
		}
		oStream.close();

    }
    
  
    public void printToPDF(JasperPrint jasperPrint, HttpServletRequest request,HttpServletResponse response,String struct){
    	ServletOutputStream ouputStream;
		try{
			ouputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(struct+".pdf", "utf-8"));
			JasperExportManager.exportReportToPdfStream(jasperPrint,
					ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    /**
     * 发文柱状图形显示
     * @author: qibh
     *@return
     *@throws Exception
     * @created: 2012-10-31 上午03:08:36
     * @version :5.0
     */
    public String reportchart() throws Exception {
    	DocType docType = DocType.Send;
    	List<DocType> docTypeList = new ArrayList<DocType>(1);
		//docTypeList.add(DocType.Recycle);	//回收的公文
		docTypeList.add(DocType.Send);//待分发的公文
		docTypeList.add(DocType.Sended);//已分发的公文
		docTypeList.add(DocType.Sign);//签章的公文
		docType.setDocTypeList(docTypeList);
    	String realPath = this.getRequest().getSession().getServletContext().getRealPath("/");
		manager.printBarChart(page,realPath,docType,Identification,startDate,endDate);
		return "jfcharts";
    }
    //打印密码
    public String printPassword(){
    	tdocSendPrintPassword = manager.getPrintPassword(user.getCurrentUser().getUserId());
    	if(tdocSendPrintPassword.getId()!=null)
    		return "printPassword";
    	return "newPrintPassword";
    }
    //保存打印密码
    public String printPasswordSave() throws IOException{
    	if(tdocSendPrintPassword==null||tdocSendPrintPassword.getId()==null||"".equals(tdocSendPrintPassword.getId())){
    		tdocSendPrintPassword = new TdocSendPrintPassword();
    		tdocSendPrintPassword.setPassword(password);
        	tdocSendPrintPassword.setUserId(user.getCurrentUser().getUserId());
    	}else{
    		tdocSendPrintPassword.setPassword(password);
        	tdocSendPrintPassword.setUserId(user.getCurrentUser().getUserId());
    	}
    	manager.PrintPasswordSaveOrUpdat(tdocSendPrintPassword);
    	//公文打印出设置密码
    	if("temp".equals((String) getRequest().getParameter("temp"))){
    		return renderText("true");
    	}
    	
    	return "temp";
    }
    public String printPasswords(){
    	//打印时输入密码
    	if("password".equals(password)){
        	tdocSendPrintPassword = manager.getPrintPassword(user.getCurrentUser().getUserId());
        	return "inputPassword";
    	}
    	//查询该用户是否有打印密码
    	tdocSendPrintPassword = manager.getPrintPassword(user.getCurrentUser().getUserId());
    	if(tdocSendPrintPassword.getId()!=null)
    		return renderText("true");
    	return renderText("false");
    }
    //验证打印密码
    public String isPassword(){
    	tdocSendPrintPassword = manager.getPrintPassword(user.getCurrentUser().getUserId());
    	if(password.equals(tdocSendPrintPassword.getPassword())){
    		return renderText("true");
    	}
    	return renderText("false");
    }
    /**
     * 退回到公文草稿 
     * 公文分发  公文签章 下
     * author  taoji
     * @return
     * @throws Exception 
     * @throws ServiceException 
     * @date 2013-2-21 下午01:06:25
     */
    public String tuiwen() throws ServiceException, Exception{
    	if(docModel.getDocId()!=null&&!"".equals(docModel.getDocId())){
    		docModel = transDocManager.getDocById(docModel.getDocId());
    		docModel.setDocState("0");
    		 flowHandlerManager.saveToaReportBean1(docModel, null);
    		transDocManager.save(docModel);
    	}
    	return renderText("0");
    }
    
public int getCurrentPage() {
	return currentPage;
}

public void setCurrentPage(int currentPage) {
	this.currentPage = currentPage;
}

public String getExportType() {
	return exportType;
}

public void setExportType(String exportType) {
	this.exportType = exportType;
}

public String getReportDate() {
	return reportDate;
}

public void setReportDate(String reportDate) {
	this.reportDate = reportDate;
}

public int getTotalNum() {
	return totalNum;
}

public void setTotalNum(int totalNum) {
	this.totalNum = totalNum;
}

public String getReportEndDate() {
	return reportEndDate;
}

public void setReportEndDate(String reportEndDate) {
	this.reportEndDate = reportEndDate;
}
/**
 * @return the disLogo
 */
public String getDisLogo() {
	return disLogo;
}

/**
 * @param disLogo the disLogo to set
 */
public void setDisLogo(String disLogo) {
	this.disLogo = disLogo;
}

public TtransArchive getMyModel() {
	return myModel;
}

public void setMyModel(TtransArchive myModel) {
	this.myModel = myModel;
}

public String getTreeValue() {
	return treeValue;
}

public void setTreeValue(String treeValue) {
	this.treeValue = treeValue;
}

public Page<TtransArchive> getPage1() {
	return page1;
}

public void setPage1(Page<TtransArchive> page1) {
	this.page1 = page1;
}

public List<String> getYearList() {
	return yearList;
}

public void setYearList(List<String> yearList) {
	this.yearList = yearList;
}

public String getYear() {
	return year;
}

public void setYear(String year) {
	this.year = year;
}

public List<String> getMonthList() {
	return monthList;
}

public void setMonthList(List<String> monthList) {
	this.monthList = monthList;
}

public String getMonth() {
	return month;
}

public void setMonth(String month) {
	this.month = month;
}

public List<ArchiveDocTree> getTreeList() {
	return treeList;
}

public void setTreeList(List<ArchiveDocTree> treeList) {
	this.treeList = treeList;
}

public TdocSendPrintPassword getTdocSendPrintPassword() {
	return tdocSendPrintPassword;
}

public void setTdocSendPrintPassword(TdocSendPrintPassword tdocSendPrintPassword) {
	this.tdocSendPrintPassword = tdocSendPrintPassword;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getOrgIdss() {
	return orgIdss;
}

public void setOrgIdss(String orgIdss) {
	this.orgIdss = orgIdss;
}

public String getDefPrints() {
	return defPrints;
}

public void setDefPrints(String defPrints) {
	this.defPrints = defPrints;
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
			sb = manager.searchByAutoComplete(q);
			renderText(sb.toString());
		}
	} catch (Exception e) {
		LogPrintStackUtil.printErrorStack(logger, e);
		renderText(LogPrintStackUtil.errorMessage);
	}
	return null;
}

}
