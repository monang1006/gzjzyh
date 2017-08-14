/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2010-08-11
 * Autour: qint
 * Version: V1.0
 * Description：公文收文action类
 */
package com.strongit.doc.receives;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.di.util.MessageCode;
import com.strongit.doc.bo.TdocSend;
import com.strongit.doc.bo.TdocSendRet;
import com.strongit.doc.bo.TtransDoc;
import com.strongit.oa.autocode.flowhandler.FlowHandlerManager;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.manager.SendDocRetManager;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

// //0待收TYPE_TODO\1已收TYPE_RECEIVED\2已拒收TYPE_REJECTION\3回收
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "recvDoc.action", type = ServletActionRedirectResult.class) })
public class RecvDocAction extends BaseActionSupport<TdocSend> { 

	private static final long serialVersionUID = 547506867611566378L;
	private Page<TdocSend> page = new Page(20, true);
	
	private Page<Object[]> recpage = new Page(20, true);
	private RecvDocManager recvDocManager;

	private String senddocId;// ID
	private String docId;// ID

	//退文单bo
	private TdocSendRet modelDocRet = new TdocSendRet();
	private TtransDoc modelDoc = new TtransDoc();
	private TdocSend model = new TdocSend(); // 发送
	
	@Autowired
	SendDocManager sendDocManager;
	@Autowired
	FlowHandlerManager flowHandlerManager;
	@Autowired
	SendDocRetManager sendDocRetManager;
	
	private String taskId;
	/*
	 * 查询相关属性
	 */
	private Date startDate;// 开始时间
	private Date endDate;// 结束时间
	private Date sendStartDate;// 发文开始时间
	private Date sendEndDate;// 发文结束时间emergencyMap
	private List<ToaSysmanageDictitem> secretLvlMap ;// 秘密等级
	private List<ToaSysmanageDictitem> emergencyMap ;// 紧急程度
	/** 是否归档Map */
	private Map<String, String> filingMap = new HashMap<String, String>();
	private String docFiling;// 
	private String showType; 
	private String showClose;
	private String flag;
	private String laiwenDW;//来文单位
	private String laiwentitle;
	
	@Autowired IDictService dictService;
	
	private DesktopSectionManager sectionManager;
	
	@Autowired
	public void setSectionManager(DesktopSectionManager sectionManager) {
		this.sectionManager = sectionManager;
	}

	// @Autowired SendDocManager sendDocManager;

	/**
	 * author:lanlc description:构造方法 modifyer: description:
	 * 
	 * @return
	 */
	public RecvDocAction() {

		filingMap.put(TdocSend.HAS_FILIONG, "已归档");
		filingMap.put(TdocSend.HAS_NO_FILIONG, "未归档");

		final Calendar ts = Calendar.getInstance();
		sendEndDate = ts.getTime();

		ts.set(Calendar.MONTH, ts.getTime().getMonth() - 1);
		sendStartDate = ts.getTime();
	} 

	/**
	 * author:lanlc description:获取当前用户接收公文分页列表 modifyer:
	 * 
	 * @return
	 */
	@Override
	public String list() throws Exception {
	
		
		return SUCCESS;
	}

	/**
	 * author:lanlc description:接收公文 modifyer:
	 * 
	 * @return
	 */
	public String doRecvDoc() throws Exception {
		this.model.setRecvState("1");
		return "remark";
	}

	/**
	 * author:lanlc description:物理删除公文 modifyer:
	 * 
	 * @return
	 */
	@Override
	public String delete() throws Exception {

		try {
			recvDocManager.delete(senddocId);
			addActionMessage("公文销毁成功");
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return RELOAD;
	}

	/**
	 * author:lanlc description:逻辑删除公文 modifyer:
	 * 
	 * @return
	 */
	public String removeDoc() throws Exception {

		try {
			recvDocManager.removeDoc(senddocId);
			addActionMessage("公文删除成功");
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		return received();
	}

	/**
	 * author:lanlc description:编辑或来文登记获取实体 modifyer:
	 * 
	 * @return
	 */
	@Override
	public void prepareModel() {
	}

	/**
	 * author:lanlc description:判断一个字符串数组中包含某个字符或字符串 modifyer:
	 * 
	 * @param strs
	 *            查找的字符串数组
	 * @param s
	 *            字符或字符串
	 * @return
	 */
	public boolean isHave(String[] strs, String s) {
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].indexOf(s) != -1) {
				return true;// 查找到了就返回真，不在继续查询
			}
		}
		return false;// 没找到返回false
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
			.append("/receive/recvDoc!todo.action")
			.append("', '待收公文'")
			.append(");");
		
//		获取公告列表list
		List<Object[]> list = null;
		recpage.setPageSize(num);
		//page = manager.getListForTable(user.getCurrentUser().getUserId(),page);
		Calendar rightNow = Calendar.getInstance();
		recpage = recvDocManager.getTtransDocs(page, modelDoc, "0", sendStartDate,
				sendEndDate, startDate, endDate);
		List<TdocSend> result = new ArrayList();
		
		list = recpage.getResult();
		
		if(list!=null){
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				Object[] re =(Object[]) list.get(i);
				//标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript: window.showModalDialog('").append(getRequest().getContextPath())
				.append("/receive/recvDoc!showDoc.action?senddocId="+re[11].toString())
				.append("&showType=todo&showClose=1', 'window','dialogWidth:1000 pt;dialogHeight:800 pt;status:no;help:no;scroll:no;minimize:yes;maximize:yes;'")
				.append("); window.location.reload();");
				
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
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
					.append("\n").append("发送单位：" +re[2].toString())
					.append("\n").append("发送时间：" + st.format(re[3]));
				//	.append("\n").append("有效期限：" + st.format(notify.getAfficheUsefulLife()));
				
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<a href=\"#\" onclick=\"").append(titleLink.toString()).append("\">")
				.append("<span style=\"font-size: "+sectionFontSize+"px;color:blue\" title=\"").append(tip).append("\">").append(title).append("</span></a>");
				innerHtml.append("</td>");
				
				innerHtml.append("<td width=\"200px\">");
				innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px;color:green\" title =\""+re[2].toString()+"\" class =\"linkgray10\">").append(re[2].toString()).append("</span>");
				innerHtml.append("</td>");
				
				//if("1".equals(showDate)){//如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" title =\""+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(re[3])+"\" class =\"linkgray10\">").append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(re[3])).append("</span>");
					innerHtml.append("</td>");
				//}
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		innerHtml.append("<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ")
				 .append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * 桌面显示 待处理文件
	 * @description	
	 * @author  xiaolj
	 * @return
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public String showDesktopDcl() throws Exception {
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
			.append("/receive/recvDoc!todo.action")
			.append("', '待收公文'")
			.append(");");
		
//		获取公告列表list
		List<Object[]> list = null;
		recpage.setPageSize(num);
		//page = manager.getListForTable(user.getCurrentUser().getUserId(),page);
		Calendar rightNow = Calendar.getInstance();
		recpage = recvDocManager.getTtransDocs(page, modelDoc, "0", sendStartDate,
				sendEndDate, startDate, endDate);
		List<TdocSend> result = new ArrayList();
		
		list = recpage.getResult();
		
		if(list!=null){
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				Object[] re =(Object[]) list.get(i);
				//标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript: window.showModalDialog('").append(getRequest().getContextPath())
				.append("/receives/recvDoc!showDoc.action?senddocId="+re[11].toString())
				.append("&showType=todo&showClose=1', 'window','dialogWidth:1000 pt;dialogHeight:800 pt;status:no;help:no;scroll:no;minimize:yes;maximize:yes;'")
				.append("); window.location.reload();");
				
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				
				String title = re[1].toString();
				if(title==null){
					title="";
				}
				
				StringBuilder tip = new StringBuilder();
				tip.append(re[1].toString())
					.append("\n").append("发文文号：" + re[0].toString() )//添加发文文号
					.append("\n").append("发送单位：" +re[2].toString())
					.append("\n").append("发送时间：" + st.format(re[3]));
				//	.append("\n").append("有效期限：" + st.format(notify.getAfficheUsefulLife()));
				
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				//title = "[待收公文]" + title;
				String clickProcessType = "window.parent.refreshWorkByTitle('"+getRequest().getContextPath()+"/receives/recvDoc!todo.action','待处理文件');";
				innerHtml.append("<a href=\"#\" onclick=\"").append(clickProcessType).append("\"").append("title=\"").append("待收公文").append("\">[")
				.append("待收公文")
				.append("]</a>").append("");
				innerHtml.append("<a href=\"#\" onclick=\"").append(titleLink.toString()).append("\">")
				.append("<span style=\"font-size: "+sectionFontSize+"px;color:black\" title=\"").append(tip).append("\">").append(title).append("</span></a>");
				innerHtml.append("</td>");
				
				innerHtml.append("<td width=\"150px\">");
				innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px;color:green\" title =\""+re[2].toString()+"\" class =\"linkgray10\">").append(re[2].toString()).append("</span>");
				innerHtml.append("</td>");
				
				//if("1".equals(showDate)){//如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"150px\">");
					innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" title =\""+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(re[3])+"\" class =\"linkgray10\">").append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(re[3])).append("</span>");
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
	 * author:lanlc description:待收公文列表 modifyer:
	 * 
	 * @return
	 */
	public String todo() throws Exception {
		User user = userService.getCurrentUser();
		Organization organization = userService.getUserDepartmentByUserId(user.getUserId());
		getRequest().setAttribute("rest3", organization.getSupOrgCode());
		// //0待收TYPE_TODO\1已收TYPE_RECEIVED\2已拒收TYPE_REJECTION\3回收
		
		secretLvlMap = dictService.getItemsByValue("MMDJ");//秘密等级
		emergencyMap = dictService.getItemsByValue("JJCD");//紧急程度
//		sendStartDate = null;
//		sendEndDate = null;
		page = recvDocManager.getTtransDocs(page, modelDoc, "0", sendStartDate,
				sendEndDate, startDate, endDate);
		return "todo";
	}
	public String todoReport () throws Exception {
		// //0待收TYPE_TODO\1已收TYPE_RECEIVED\2已拒收TYPE_REJECTION\3回收

		secretLvlMap = dictService.getItemsByValue("MMDJ");//秘密等级
		emergencyMap = dictService.getItemsByValue("JJCD");//紧急程度
//		sendStartDate = null;
//		sendEndDate = null;
		page = recvDocManager.getTtransDocs(page, modelDoc, "0", sendStartDate,
				sendEndDate, startDate, endDate);
		return "todo";
	}

	public List<ToaSysmanageDictitem> getSecretLvlMap() {
		return secretLvlMap;
	}

	public List<ToaSysmanageDictitem> getEmergencyMap() {
		return emergencyMap;
	}

	/**
	 * author:lanlc description:已收公文列表 modifyer:
	 * 
	 * @return
	 */
	public String received() throws Exception {
		// //0待收TYPE_TODO\1已收TYPE_RECEIVED\2已拒收TYPE_REJECTION\3回收
		page = recvDocManager.getTtransDocs(page, modelDoc, "1", sendStartDate,
				sendEndDate, startDate, endDate, docFiling);
		return "received";
	}
	/**公文传输转来问草稿（已收公文列表）(未转入列表)
	 * author:lanlc description:已收公文列表 modifyer:
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String docTurnDraft() throws Exception {
		// //0待收TYPE_TODO\1已收TYPE_RECEIVED\2已拒收TYPE_REJECTION\3回收
		page = recvDocManager.getTtransDocsTurnDraft(page, modelDoc, "1", sendStartDate,
				sendEndDate, startDate, endDate, docFiling);
		return "docTurnDraft";
	}
	/**公文传输转来问草稿（已收公文列表）(已转入列表)
	 * author:lanlc description:已收公文列表 modifyer:
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String docTurnDrafts() throws Exception {
		// //0待收TYPE_TODO\1已收TYPE_RECEIVED\2已拒收TYPE_REJECTION\3回收
		page = recvDocManager.getTtransDocsTurnDrafts(page, modelDoc, "1", sendStartDate,
				sendEndDate, startDate, endDate, docFiling);
		return "docTurnDrafts";
	}
	
	/**
	 * author:
	 * description:
	 * modifyer: 	luosy 2013-5-18
	 * description: 修改为批量操作“转移到来文草稿”
	 * @return
	 * @throws HibernateException
	 * @throws SQLException
	 * @throws IOException
	 */
	public String docTurnDraftHandle() throws HibernateException, SQLException, IOException{
		
		if(!"".equals(senddocId)&&null!=senddocId){
			
			String[] senddocIds = senddocId.split(",");
			String ret = "";
			String returnString = "false";
			for(String docId : senddocIds){
				
				ret = recvDocManager.docTurnDraftHandle(docId);

				if(!"true".equals(ret) && ret.indexOf("false")>-1){
					ret.replace("false", "");
					returnString += ret+"\n";
				}
			}
			return renderText(ret);
		}
		return "error";
	}

	/**
	 * author:lanlc description:已拒收公文列表 modifyer:
	 * 
	 * @return
	 */
	public String rejected() throws Exception {
		// //0待收TYPE_TODO\1已收TYPE_RECEIVED\2已拒收TYPE_REJECTION\3回收
		page = recvDocManager.getTtransDocs(page, modelDoc, "2", sendStartDate,
				sendEndDate, startDate, endDate);
		return "rejected";
	}
	
	/**
	 * author:xiaolj description:已发公文---已拒收公文列表 modifyer:
	 * 
	 * @return
	 */
	public String sendRejected() throws Exception {
		// //0待收TYPE_TODO\1已收TYPE_RECEIVED\2已拒收TYPE_REJECTION\3回收
		page = recvDocManager.getTtransSendDocs(page, modelDoc, "2", sendStartDate,
				sendEndDate, startDate, endDate);
		return "sendRejected";
	}
	
	/**
	 * 判断拒收公文代码
	 */
	@Autowired IUserService userService ;										//统一用户服务类
	@Autowired
	RecvTDocManager manager; // 注入服务类对象
	private String flag2 = null;
	
	public String getFlag2() {
		return flag2;
	}

	public void setFlag2(String flag2) {
		this.flag2 = flag2;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * author:lanlc \接收公文\显示公文:
	 * 
	 * @return
	 */
	public String showDoc() throws Exception {
		//标识公文传输是否已转来文草稿
		String docTurnDraft = null;
		if (senddocId != null && !"".equals(senddocId)
				&& !"null".equals(senddocId)) {
			TdocSend docSend = recvDocManager.getSendDocById(senddocId);
			TtransDoc ttransDoc = (TtransDoc) docSend.getTtransDoc();
		
			docId = ttransDoc.getDocId();
			TtransDoc model = manager.getDocById(docId);
			if(model.getDocIssueDepartSigned()!=null&&model.getDocIssueDepartSigned().trim().equals("南昌市人民政府"))
			{
				flag = null;
			}else
			{
				flag = "0";
			}
			laiwenDW = model.getDocIssueDepartSigned();
			laiwentitle = model.getDocTitle();
			docTurnDraft = docSend.getDocTurnDraft();
			User user = userService.getCurrentUser();
			String code = user.getSupOrgCode();
			if(code!=null&&code.trim().equals("001"))
			{
				flag2 = "0";
			}else
			{
				flag2 = null;
			}
		}
		getRequest().setAttribute("docTurnDraft",docTurnDraft);
		//by tj
		String tuiwen = getRequest().getParameter("tuiwen");
		if("true".equals(tuiwen)){
			getRequest().setAttribute("tuiwen", tuiwen);
		}
		TtransDoc t = manager.getDocById(docId);
		if("2".equals(t.getDocState())){
			return "showview";
		}
		getRequest().setAttribute("docId", docId);    //docId上面就有，可以不用伟的，不过，不知道为什么后面就是得不到，现在只能加这一句
		return "view";
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public TtransDoc getModelDoc() {
		return modelDoc;
	}

	public void setModelDoc(TtransDoc modelDoc) {
		this.modelDoc = modelDoc;
	}

	@Autowired
	public void setRecvDocManager(RecvDocManager aRecvDocManager) {
		recvDocManager = aRecvDocManager;
	}

	// public void setSuggestion(String suggestion) {
	// try{
	// suggestion = URLDecoder.decode(suggestion, "utf-8");
	// }catch(Exception e){
	// LogPrintStackUtil.error("处理意见转码异常！");
	// }
	// this.suggestion = suggestion;
	// }

	/**
	 * 转到回退输入处理意见页面
	 * 
	 * @author:
	 * @date:
	 * @return
	 * @throws Exception
	 */
	public String initBack() throws Exception {
		return "initback";
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		endDate.setHours(23);
		endDate.setMinutes(59);
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public String input() throws Exception { 
		return "view";
	}

	
	/**
	 * @author:xiaolj
	 * @date:
	 * @return
	 * @throws Exception
	 */
	public String saveret() throws Exception{
		String bussinessId = getRequest().getParameter("bussinessId");
		modelDocRet.setBusinessId(bussinessId);
		
		String qita = this.getRequest().getParameter("qita");
		modelDocRet.setQita(qita);
		
		String liyou[] = this.getRequest().getParameterValues("liyou");
		 String liyous = "";
		 if(liyou !=null && liyou.length>0)
		 {
			 for(int i =0;i<liyou.length;)
			 {
				 liyous =liyous+ liyou[i];
				 i++;
				 if(i<liyou.length)
				 {
					 liyous = liyous+",";
				 }
			 }
			 System.out.println("liyous:" + liyous);
			 modelDocRet.setLiyou(liyous);
		 }
		 
		 String docRecvRemark = this.getRequest().getParameter("docRecvRemark");
		 modelDocRet.setDocrecvRemark(docRecvRemark);
		 
		 sendDocRetManager.saveDocRet(modelDocRet);
		 
		return renderText("true");
	}
	
	/**
	 * 保存退文，并做处理
	 * 
	 * @author:xiaolj
	 * @date:
	 * @return
	 * @throws Exception
	 */
	public String firstsaveretRmk() throws Exception{
		//收文文编号
		String recNum = null;
		String docSendId = "";
		TdocSend modelSend = new TdocSend();
		
		String userN = recvDocManager.getCurrentUser().getUserName();
		//传过来的T_OARECVDOC 表的  id   by tj
		String pkFieldValue = getRequest().getParameter("pkFieldValue");
		if(pkFieldValue!=null&&!"".equals(pkFieldValue)&&!"null".equals(pkFieldValue)){
			docSendId = recvDocManager.getDocSendIdByRecId(pkFieldValue);
			
			recvDocManager.deltetT_OARECVDOCFlag(pkFieldValue);
			
			recNum = recvDocManager.getRecNum(pkFieldValue);
		}else{
			String[] infos = sendDocManager.getFormIdAndBussinessIdByTaskId(taskId);
			String[] t = infos[0].split(";");
			recvDocManager.setT_OARECVDOCFlag(t[2]);
			
			docSendId = recvDocManager.getDocSendIdByRecId(t[2]);
			
			recNum = recvDocManager.getRecNum(t[2]);
		}
		
		if(docSendId!=null&&!"".equals(docSendId)){
			modelSend = recvDocManager.getSendDocById(docSendId);
		}
		modelSend.setRecvState("2");//退回到拒签状态
		modelSend.setSwbh(recNum);//收文编号
		/*modelSend.setDocRecvRemark(java.net.URLDecoder.decode(model
				.getDocRecvRemark(), "utf-8"));*/
		
		String qita = this.getRequest().getParameter("qita");
		String docrecvRemark = this.getRequest().getParameter("this.getRequest()");
		
		modelSend.setDocRecvRemark(docrecvRemark);
		modelSend.setQita(qita);
		 modelSend.setDocRecvUser(userN);
		 String liyou[] = this.getRequest().getParameterValues("liyou");
		 String liyous = "";
		 if(liyou !=null && liyou.length>0)
		 {
			 for(int i =0;i<liyou.length;)
			 {
				 liyous =liyous+ liyou[i];
				 i++;
				 if(i<liyou.length)
				 {
					 liyous = liyous+",";
				 }
			 }
			 modelSend.setLiyous(liyous);
		 }
		 String ip = this.getRequest().getHeader("x-forwarded-for");
		 if(ip == null || ip.length()==0 || "unknow".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("Proxy-Client-IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("WL-Proxy-Client-IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("HTTP_CLIENT_IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("HTTP_X_FORWARDED_FOR");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getRemoteAddr();
		 }
 
		modelSend.setDocRecvTime(Calendar.getInstance().getTime());
		modelSend.setOperateIp(ip);

		String msg;OALogInfo info ;
		if (modelSend.getRecvState().equals("1")) {
			msg = "公文接收成功！";
			info = new OALogInfo(userN
					+ " 成功接收公文《" + modelSend.getTtransDoc().getDocTitle()+"》");
		} else {
			msg = "";
			info = new OALogInfo(userN
					+ " 拒收公文《" + modelSend.getTtransDoc().getDocTitle()+"》,拒收原因："+modelSend.getDocRecvRemark());
		}
		recvDocManager.saveSends(modelSend,info);
		
		//StringBuilder sb = new StringBuilder();
		//sb.append("<script type=\"text/javascript\">window.returnValue=\"true\";" +
		//		"window.close();</script>");

//		if("1".equals(showClose)){
//		    return renderHtml(MessageCode.closeAndRemandHtml(getRequest()
//					 .getContextPath(), "", msg));
//		}else {
//			return renderHtml(MessageCode.closeAndRemandHtml(getRequest()
//					 .getContextPath(), "", msg));//submitForm
//		}
		return renderText("true");
	}
	
	/**
	 * 保存退文，并做处理
	 * 
	 * @author:xiaolj
	 * @date:
	 * @return
	 * @throws Exception
	 */
	public String saveretRmk() throws Exception{
		//收文文编号
		String recNum = null;
		String docSendId = "";
		TdocSend modelSend = new TdocSend();
		
		String userN = recvDocManager.getCurrentUser().getUserName();
		
		String[] infos = sendDocManager.getFormIdAndBussinessIdByTaskId(taskId);
		String[] t = infos[0].split(";");
		recvDocManager.setT_OARECVDOCFlag(t[2]);
		
		docSendId = recvDocManager.getDocSendIdByRecId(t[2]);
		recNum = recvDocManager.getRecNum(t[2]);
		if(docSendId!=null&&!"".equals(docSendId)){
			modelSend = recvDocManager.getSendDocById(docSendId);
		}
		modelSend.setRecvState("2");
		modelSend.setSwbh(recNum);
		/*modelSend.setDocRecvRemark(java.net.URLDecoder.decode(model
				.getDocRecvRemark(), "utf-8"));*/
		modelSend.setDocRecvRemark(modelDocRet.getDocrecvRemark());
		modelSend.setQita(modelDocRet.getQita());
		 modelSend.setDocRecvUser(userN);
		 String liyou[] = this.getRequest().getParameterValues("liyou");
		 String liyous = "";
		 if(liyou !=null && liyou.length>0)
		 {
			 for(int i =0;i<liyou.length;)
			 {
				 liyous =liyous+ liyou[i];
				 i++;
				 if(i<liyou.length)
				 {
					 liyous = liyous+",";
				 }
			 }
			 modelSend.setLiyous(liyous);
		 }
		 modelDocRet.setLiyou(liyous);
		 String ip = this.getRequest().getHeader("x-forwarded-for");
		 if(ip == null || ip.length()==0 || "unknow".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("Proxy-Client-IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("WL-Proxy-Client-IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("HTTP_CLIENT_IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("HTTP_X_FORWARDED_FOR");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getRemoteAddr();
		 }
 
		modelSend.setDocRecvTime(Calendar.getInstance().getTime());
		modelSend.setOperateIp(ip);

		String msg;OALogInfo info ;
		if (modelSend.getRecvState().equals("1")) {
			msg = "公文接收成功！";
			info = new OALogInfo(userN
					+ " 成功接收公文《" + modelSend.getTtransDoc().getDocTitle()+"》");
		} else {
			msg = "";
			info = new OALogInfo(userN
					+ " 拒收公文《》,拒收原因："+modelSend.getDocRecvRemark());
		}
		recvDocManager.saveSends(modelSend,info);
		sendDocRetManager.updateDocRet(modelDocRet);
		
		//StringBuilder sb = new StringBuilder();
		//sb.append("<script type=\"text/javascript\">window.returnValue=\"true\";" +
		//		"window.close();</script>");

//		if("1".equals(showClose)){
//		    return renderHtml(MessageCode.closeAndRemandHtml(getRequest()
//					 .getContextPath(), "", msg));
//		}else {
//			return renderHtml(MessageCode.closeAndRemandHtml(getRequest()
//					 .getContextPath(), "", msg));//submitForm
//		}
		return renderText("true");
	}
	
	/**
	 * 保存退文，并做处理
	 * 
	 * @author:xiaolj
	 * @date:
	 * @return
	 * @throws Exception
	 */
	public String mysave() throws Exception{
		//收文文编号
		String recNum = null;
		//docId公文Id   docSendId 分发id
		//System.out.println("docId:" + docId);
		String docSendId = getRequest().getParameter("docSendId");
		TdocSend modelSend = new TdocSend();
		if(docSendId!=null&&!"".equals(docSendId)){
			modelSend = recvDocManager.getSendDocById(docSendId);
		}

		String userN = recvDocManager.getCurrentUser().getUserName();
		
		modelSend.setRecvState("2");
		
		String[] infos = sendDocManager.getFormIdAndBussinessIdByTaskId(taskId);
		String[] t = infos[0].split(";");
		recvDocManager.setT_OARECVDOCFlag(t[2]);
		
		recNum = recvDocManager.getRecNum(t[2]);
		modelSend.setSwbh(recNum);
		/*modelSend.setDocRecvRemark(java.net.URLDecoder.decode(model
				.getDocRecvRemark(), "utf-8"));*/
		modelSend.setDocRecvRemark(model.getDocRecvRemark());
		modelSend.setQita(model.getQita());
		 modelSend.setDocRecvUser(userN);
		 String liyou[] = this.getRequest().getParameterValues("liyou");
		 String liyous = "";
		 if(liyou !=null && liyou.length>0)
		 {
			 for(int i =0;i<liyou.length;)
			 {
				 liyous =liyous+ liyou[i];
				 i++;
				 if(i<liyou.length)
				 {
					 liyous = liyous+",";
				 }
			 }
			 modelSend.setLiyous(liyous);
		 }
		 String ip = this.getRequest().getHeader("x-forwarded-for");
		 if(ip == null || ip.length()==0 || "unknow".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("Proxy-Client-IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("WL-Proxy-Client-IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("HTTP_CLIENT_IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("HTTP_X_FORWARDED_FOR");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getRemoteAddr();
		 }
 
		modelSend.setDocRecvTime(Calendar.getInstance().getTime());
		modelSend.setOperateIp(ip);

		String msg;OALogInfo info ;
		if (model.getRecvState().equals("1")) {
			msg = "公文接收成功！";
			info = new OALogInfo(userN
					+ " 成功接收公文《" + modelSend.getTtransDoc().getDocTitle()+"》");
		} else {
			msg = "";
			info = new OALogInfo(userN
					+ " 拒收公文《" + modelSend.getTtransDoc().getDocTitle()+"》,拒收原因："+modelSend.getDocRecvRemark());
		}
		recvDocManager.saveSends(modelSend,info);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<script type=\"text/javascript\">window.returnValue=\"true\";" +
				"window.close();</script>");

//		if("1".equals(showClose)){
//		    return renderHtml(MessageCode.closeAndRemandHtml(getRequest()
//					 .getContextPath(), "", msg));
//		}else {
//			return renderHtml(MessageCode.closeAndRemandHtml(getRequest()
//					 .getContextPath(), "", msg));//submitForm
//		}
		return renderHtml(sb.toString());
	}
	
	@Override
	public String save() throws Exception {
		TdocSend modelSend = recvDocManager.getSendDocById(senddocId); // 根据id获取对象
		TtransDoc ttransDoc = recvDocManager.getTtransDocById(modelSend.getDocId());
		String userN = recvDocManager.getCurrentUser().getUserName();
		modelSend.setRecvState(model.getRecvState());
		/*modelSend.setDocRecvRemark(java.net.URLDecoder.decode(model
				.getDocRecvRemark(), "utf-8"));*/
		modelSend.setDocRecvRemark(model.getDocRecvRemark());
		modelSend.setQita(model.getQita());
		 modelSend.setDocRecvUser(userN);
		 String liyou[] = this.getRequest().getParameterValues("liyou");
		 String liyous = "";
		 if(liyou !=null && liyou.length>0)
		 {
			 for(int i =0;i<liyou.length;)
			 {
				 liyous =liyous+ liyou[i];
				 i++;
				 if(i<liyou.length)
				 {
					 liyous = liyous+",";
				 }
			 }
			 modelSend.setLiyous(liyous);
		 }
		 if(liyous!=null&&!"".equals(liyous)){
		 flowHandlerManager.saveToaReportBean1(ttransDoc, modelSend);}
		 String ip = this.getRequest().getHeader("x-forwarded-for");
		 if(ip == null || ip.length()==0 || "unknow".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("Proxy-Client-IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("WL-Proxy-Client-IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("HTTP_CLIENT_IP");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getHeader("HTTP_X_FORWARDED_FOR");
		 }
		 if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			 ip = this.getRequest().getRemoteAddr();
		 }
 
		modelSend.setDocRecvTime(Calendar.getInstance().getTime());
		modelSend.setOperateIp(ip);

		String msg;OALogInfo info ;
		if (model.getRecvState().equals("1")) {
			msg = "公文接收成功！";
			info = new OALogInfo(userN
					+ " 成功接收公文《" + modelSend.getTtransDoc().getDocTitle()+"》");
		} else {
			msg = "";
			info = new OALogInfo(userN
					+ " 拒收公文《" + modelSend.getTtransDoc().getDocTitle()+"》,拒收原因："+modelSend.getDocRecvRemark());
		}
		/*else {
			msg = "公文已拒收！";
			info = new OALogInfo(userN
					+ " 拒收公文《" + modelSend.getTtransDoc().getDocTitle()+"》,拒收原因："+modelSend.getDocRecvRemark());
		}*/
		recvDocManager.saveSends(modelSend,info);

		// return renderHtml("<script> var scriptroot =
		// '/StrongOA_DT'</script><SCRIPT
		// src='/StrongOA_DT/common/js/commontab/workservice.js'>" +
		// "</SCRIPT><SCRIPT
		// src='/StrongOA_DT/common/js/commontab/service.js'></SCRIPT><script>alert('公文接收成功！');"
		// +
		// "window.dialogArguments.tob.submitForm();window.returnValue
		// ='reload';window.close();</script>");
		//if(showClose.equals("1")){
		if("1".equals(showClose)){
		    return renderHtml(MessageCode.closeAndHtml(getRequest()
				 .getContextPath(), "", msg));
		}else {
			return renderHtml(MessageCode.closeAndRemandHtml(getRequest()
					 .getContextPath(), "", msg));//submitForm
		}
		
	}
	/**
	 * 判断此公文是否是公文传输转入
	 * 用来退文  
	 * author  taoji
	 * @return
	 * @throws SQLException 
	 * @throws HibernateException 
	 * @date 2013-3-15 下午02:11:05
	 */
	public String listGwcs() throws HibernateException, SQLException{
		//docid  临时传输数据  
		if(recvDocManager.getGwcs(docId)){
			return renderText("true");
		}else{
			return renderText("false");
		}
	}
	public TdocSend getModel() {
		return model;
	}

	public void setModel(TdocSend model) {
		this.model = model;
	}

	public Date getSendStartDate() {
		return sendStartDate;
	}

	public void setSendStartDate(Date sendStartDate) {
		this.sendStartDate = sendStartDate;
	}

	public Date getSendEndDate() {
		return sendEndDate;
	}

	public void setSendEndDate(Date sendEndDate) {
		this.sendEndDate = sendEndDate;
	}

	public Map<String, String> getFilingMap() {
		return filingMap;
	}

	public String getDocFiling() {
		return docFiling;
	}

	public void setDocFiling(String docFiling) {
		this.docFiling = docFiling;
	} 

	public String getDocSendId() {
		return senddocId;
	} 

	public String getSenddocId() {
		return senddocId;
	}

	public void setSenddocId(String senddocId) {
		this.senddocId = senddocId;
	}

	public void setSecretLvlMap(List<ToaSysmanageDictitem> secretLvlMap) {
		this.secretLvlMap = secretLvlMap;
	}

	public void setEmergencyMap(List<ToaSysmanageDictitem> emergencyMap) {
		this.emergencyMap = emergencyMap;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getShowClose() {
		return showClose;
	}

	public void setShowClose(String showClose) {
		this.showClose = showClose;
	}

	public String getLaiwenDW() {
		return laiwenDW;
	}

	public void setLaiwenDW(String laiwenDW) {
		this.laiwenDW = laiwenDW;
	}

	public String getLaiwentitle() {
		return laiwentitle;
	}

	public void setLaiwentitle(String laiwentitle) {
		this.laiwentitle = laiwentitle;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public TdocSendRet getModelDocRet() {
		return modelDocRet;
	}

	public void setModelDocRet(TdocSendRet modelDocRet) {
		this.modelDocRet = modelDocRet;
	}

}
