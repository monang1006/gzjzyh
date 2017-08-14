package com.strongit.doc.sends;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.informix.util.stringUtil;
import com.strongit.doc.agencygroup.GroupDetManager;
import com.strongit.doc.bo.TdocSend;
import com.strongit.doc.bo.TdocSendRet;
import com.strongit.doc.bo.TdocStampLog;
import com.strongit.doc.bo.TtransDoc;
import com.strongit.doc.bo.TtransDocAttach;
import com.strongit.doc.monidoc.MoniDocManager;
import com.strongit.doc.sends.util.DESEncrypter;
import com.strongit.doc.sends.util.DocType;
import com.strongit.oa.bo.ToaDoctemplateGroup;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.bookmark.BookMarkManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.doctemplate.doctempItem.DocTempItemManager;
import com.strongit.oa.doctemplate.doctempType.DocTempTypeManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.manager.SendDocRetManager;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.PathUtil;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.util.UUIDGenerator;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 发文管理Action.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-8-10 上午11:09:07
 * @version  2.0.7
 * @classpath com.strongit.doc.sends.TransDocAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
@ParentPackage("default")
@Results({ 
			@Result(name = BaseActionSupport.RELOAD, value = "transDoc.action", type = ServletActionRedirectResult.class),
			@Result(name = "signReload", value = "transDoc!sign.action", type = ServletActionRedirectResult.class)
		})
public class TransDocAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3998402740471941374L;
	
	TtransDoc model = new TtransDoc();											//定义Model对象,方便传递参数

	Page<Object[]> page = new Page<Object[]>(20,true);							//定义分页列表对象
	
	@Autowired TransDocManager manager ;										//注入服务层对象
	
	Date startDate ;															//开始日期
	
	Date endDate ;																//结束日期	
	
	private String docType;														//打开的OFFICE类型，参见千航控件说明：0:  没有文档； 100 =其他文档类型；
																				//1=word；2=Excel.Sheet或者 Excel.Chart ；
																				//3=PowerPoint.Show； 4= Visio.Drawing； 
																				//5=MSProject.Project； 6= WPS Doc；
																				//7:Kingsoft Sheet
	
	private File wordDoc ;														//正文
	
	private File[] attachMent ;													//附件；支持一次上传多个附件
	
	private String[] attachMentName ;											//附件名称	
	
	private String formId ;														//如果是JSp存的就是JSP路径
	
	static String UPLOADDIR = "transdoc.upload.dir";							//上传文件路径,定义在资源文件中
	
	@Autowired IUserService userService ;										//统一用户服务类
	
	@Autowired TransDocAttachManager attachManager ;							//公文附件服务类
	
	@Autowired DocTempTypeManager tempTypeManager ; 							//模板类别服务类.
	
	@Autowired DocTempItemManager tempItemManager; 								//模板服务类
	
	@Autowired BookMarkManager bookMarkService ;								//标签服务类
	
	@Autowired IDictService dictService ;										//字典服务类
	
	@Autowired MoniDocManager moniDocManager ;									//盖章日志服务类
	
	@Autowired GroupDetManager groupManager ;									//机构分组服务类
	
	@Autowired DocSendManager docSendManager;
	
	private String actionName = BaseActionSupport.RELOAD ;						//目标路径名称，如reload对应
																				//transDoc.action;signReload对应transDoc!sign.action
	
	private String userName ;													//用户名称,痕迹保留用到
	
	private List<ToaSysmanageDictitem> mmdjItems ;								//秘密等级字典项
	
	private List<ToaSysmanageDictitem> jjcdItems ;								//紧急程度字典项
	
	private String signInfo ;													//签章信息,采用JSON格式传输
	
	private String oldSignInfo ;												//开始的签章信息
	
	private List<Organization> orgList ;														//机构列表
	
	private List orgGroupList ;													//机构分组列表
	
	private String type ;														//操作类别；0或空：保存；1：提交
	
	private TdocSendRet modelDocRet = new TdocSendRet();                        //退文单
	
	@Autowired
	SendDocRetManager sendDocRetManager;
	
    private DesktopSectionManager sectionManager;
	
	@Autowired
	public void setSectionManager(DesktopSectionManager sectionManager) {
		this.sectionManager = sectionManager;
	}
	/**
	 * 删除公文,只是做了逻辑删除.
	 */
	@Override
	public String delete() throws Exception {
		if(model.getDocId() != null){
			String[] ids = model.getDocId().split(",");
			/*for(String id : ids){
				manager.delete(id);
			}*/
			User user = userService.getCurrentUser();
			if(ids != null){
				for(String id : ids){
					TtransDoc doc = manager.getDocById(id);
					if(doc != null){
						OALogInfo info = new OALogInfo(getText(GlobalBaseData.SENDDOC_DELETE_DOC,new String[]{user.getUserName(),doc.getDocTitle()}));
						manager.jdbcDelete(id,info);
					}
				}
			}
		}
		return BaseActionSupport.RELOAD;
	}

	/**
	 * 提交公文,只是修改公文的状态为待签章状态或待分发状态
	 * @author:邓志城
	 * @date:2010-8-17 下午01:25:16
	 * @return
	 * @throws Exception
	 */
	public String submit() throws Exception {
		if(model.getDocId() != null){
			String[] ids = model.getDocId().split(",");
			/*for(String id : ids){
				manager.submit(id);
			}*/
			User user = userService.getCurrentUser();
			if(ids != null){
				for(String id : ids){
					TtransDoc doc = manager.getDocById(id);
					if(doc != null){
						String docSealIs = doc.getDocSealIs();
						String docState = doc.getDocState();
						if(docSealIs != null && !"".equals(docSealIs)){
							if("0".equals(docState)){//在草稿列表中提交
								docState = getText("transdoc.state.sign");
							}
							if("1".equals(docState)){
								docState = getText("transdoc.state.send");
							}
						}else{
							docState = getText("transdoc.state.send");
						}
						OALogInfo info = new OALogInfo(getText(GlobalBaseData.SENDDOC_SUBMIT_DOC,new String[]{user.getUserName(),doc.getDocTitle(),docState}));
						manager.jdbcSubmit(doc,info);
					}
				}
			}
		}
		return actionName;
	}
	
	/**
	 * 转到新建或编辑公文界面
	 */
	@Override
	public String input() throws Exception {
		User user = userService.getCurrentUser();
		userName = user.getUserName();
		mmdjItems = dictService.getItemsByValue("MMDJ");//秘密等级
		jjcdItems = dictService.getItemsByValue("JJCD");//紧急程度
		if(model.getDocId() != null && !"".equals(model.getDocId())){
			model = manager.getDocById(model.getDocId());
			/*if(model.getDocOfficialTime() != null && !"".equals(model.getDocOfficialTime())){
				model.setDocOfficialTime(null);//如果有成文日期,说明非草稿中添加过来的
			}*/
		}else{
			model.setDocEntryTime(new Date());
			model.setDdocEntryPeople(user.getUserId());	//当前用户
			model.setIsdelete(DocType.NOTDELETE);
			Organization organization = userService.getUserDepartmentByUserId(user.getUserId());
			
			if(organization != null){
				if(organization.getSupOrgCode()!=null && organization.getSupOrgCode().trim().equals("001"))
				{
					model.setDocIssueDepartSigned("南昌市人民政府");
					model.setRest3(userService.getSupOrgByUserIdByHa(user.getUserId()).getOrgSyscode());//获取顶级机构的ID
				}else
				{
					model.setDocIssueDepartSigned(organization.getOrgName());
					
					model.setRest3(organization.getOrgSyscode());//获取顶级机构的ID
				}
				
				
				
				
			}
		}
		return INPUT;
	}

	/**
	 * 转到公文签章页面
	 * @author:邓志城
	 * @date:2010-8-18 下午03:48:54
	 * @return
	 * @throws Exception
	 */
	public String initSign() throws Exception {
		userName = userService.getCurrentUser().getUserName();
		long l1 = System.currentTimeMillis();
		mmdjItems = dictService.getItemsByValue("MMDJ");//秘密等级
		jjcdItems = dictService.getItemsByValue("JJCD");//紧急程度
		long l2 = System.currentTimeMillis();
		logger.info("查询数据字典耗时为：" + (l2-l1)+"ms");
		if(model.getDocId() != null && !"".equals(model.getDocId())){
			model = manager.getDocById(model.getDocId());
		}
		return "initsign2";
	}
	
	/**
	 * 公文草稿列表
	 */
	@Override
	public String list() throws Exception {
		mmdjItems = dictService.getItemsByValue("MMDJ");//秘密等级
		jjcdItems = dictService.getItemsByValue("JJCD");//紧急程度
		
		DocType type = DocType.Draft;
		if(model.getDocState() == null || "".equals(model.getDocState())){//查询适合草稿全部状态的公文
			List<DocType> docTypeList = new ArrayList<DocType>(1);
			docTypeList.add(type);				//草稿公文
			docTypeList.add(DocType.Recycle);	//回收的公文
			docTypeList.add(DocType.ReturnBack);//退回的公文
			type.setDocTypeList(docTypeList);
		}else{
			DocType[] typeArray = DocType.values();
			for(DocType typeElement : typeArray){
				if(model.getDocState().equals(typeElement.getValue())){
					type = typeElement ;
					break;
				}
			}
		}
		
		page = manager.getDocDraft(page,type, model, startDate, endDate);
		String startStr = "";
		String endStr = "";
		if(startDate != null)
		{
			startStr = (new SimpleDateFormat("yyyy-MM-dd")).format(startDate);
			getRequest().setAttribute("startStr", startStr);
		}
		if(endDate != null)
		{
		    endStr = (new SimpleDateFormat("yyyy-MM-dd")).format(endDate);
			getRequest().setAttribute("endStr", endStr);
		}
		return SUCCESS;
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
			.append("/sends/transDoc!sign.action")
			.append("', '待签章公文'")
			.append(");");
		
//		获取公告列表list
		List<Object[]> list = null;
		page.setPageSize(num);
		//page = manager.getListForTable(user.getCurrentUser().getUserId(),page);
		Calendar rightNow = Calendar.getInstance();
		DocType docType = DocType.Send.setRule(true);
		page = manager.getDocSign(page, model, startDate, endDate);
		List<TdocSend> result = new ArrayList();
		
		list = page.getResult();
		
		if(list!=null){
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				Object[] re =(Object[]) list.get(i);
				//标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript: window.showModalDialog('").append(getRequest().getContextPath())
				.append("/sends/transDoc!initSign.action?model.docId="+re[0].toString())
				.append("', 'window','dialogWidth:1000 pt;dialogHeight:800 pt;status:no;help:no;scroll:no;minimize:yes;maximize:yes;'")
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
				 .append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * 得到未签章的公文列表
	 * @author:邓志城
	 * @date:2010-8-12 上午10:29:20
	 * @return
	 * @throws Exception
	 */
	public String sign() throws Exception {
		mmdjItems = dictService.getItemsByValue("MMDJ");	//秘密等级
		jjcdItems = dictService.getItemsByValue("JJCD");	//紧急程度
		page = manager.getDocSign(page, model, startDate, endDate);
		return "sign";
	}

	/**
	 * 得到未分发的公文列表
	 * @author:邓志城
	 * @date:2010-8-12 上午10:30:15
	 * @return
	 * @throws Exception
	 */
	public String send() throws Exception {
		mmdjItems = dictService.getItemsByValue("MMDJ");//秘密等级
		jjcdItems = dictService.getItemsByValue("JJCD");//紧急程度
		page = manager.getDocSend(page, model, startDate, endDate);
		return "send";
	}
	
	@Override
	protected void prepareModel() throws Exception {

	}

	@Override
	public String save() throws Exception {
		
		
		InputStream is = null;
		OutputStream os  = null;
		byte[] buf = null;
		ByteArrayOutputStream bos = null;
		String ret = "";
		if(wordDoc == null){//获取正文数据失败
			
			ret = "-1";
			return this.renderText(ret);
		}
		try{
			
			is = new FileInputStream(wordDoc);
			
			byte[] b = new byte[8192];
			int read = 0;
			bos = new ByteArrayOutputStream();
			while((read=is.read(b))!=-1){
				bos.write(b, 0, read);
			}
			
			buf = bos.toByteArray();	//得到正文的字节流数据
			model.setDocContent(buf);
			
			if(attachMent != null){
				Set<TtransDocAttach> set = new HashSet<TtransDocAttach>();
				String rootPath = PathUtil.getRootPath();//得到工程根路径
				String uploadDir = getText(UPLOADDIR); 
				String dir = rootPath + uploadDir; 
				File file = new File(dir);
				if(!file.exists()){
					file.mkdir();
				}
				
				for(int i=0;i<attachMent.length;i++){
					UUIDGenerator ID = new UUIDGenerator();
					String ext = attachMentName[i].substring(attachMentName[i].lastIndexOf(".")+1);
					String fileName = uploadDir + File.separator + ID.generate() + "." + ext ;
					TtransDocAttach attach = new TtransDocAttach();
					attach.setAttachFileData(new Date());
					attach.setAttachFileName(attachMentName[i]);
					attach.setAttachFilePath(fileName);
					attach.setAttachFileType(ext);
					attach.setFileServer(getRequest().getLocalAddr());
					attach.setTtransDoc(model);
					if(attachMent[i].exists()){
						attach.setIs(new FileInputStream(attachMent[i]));
					}
					set.add(attach);
				}
				model.setObj(set);
			}
			User user = userService.getCurrentUser();
			Organization organization = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JSONArray jsonArray = null;
			//处理签章信息
			if(signInfo != null && !"".equals(signInfo) && !"[]".equals(signInfo)){ 
				jsonArray = JSONArray.fromObject(signInfo);
				if(organization == null){
					organization = userService.getUserDepartmentByUserId(user.getUserId());//得到当前用户所在机构
				}
				model.setRest1("false");			
				for(int i=0;i<jsonArray.size();i++){
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					if(jsonObj != null){
						String isLockDoc = jsonObj.getString("isLockDoc");	//签章是否锁定
						//isLockDoc.toLowerCase();
						String signer    = jsonObj.getString("signer");		//签章持有人
						String signtime  = jsonObj.getString("signtime");	//签章时间	
						String signname  = jsonObj.getString("signname");	//签章名称
						if(signer == null || "".equals(signer)){
							signer = user.getUserName();					//签章持有人为空时,取当前用户姓名
						}
						if(i == 0){//记录第一个签章到公文信息中
							model.setDocSealPeople(signer);
							model.setDocSealTime(sdf.parse(signtime));
						}
						if("true".equals(isLockDoc)){
							model.setRest1("true");
						}
						TdocStampLog stampLog = new TdocStampLog();
						stampLog.setDepartName(organization.getOrgName());  //盖章人所在机构名称
						stampLog.setDocCode(model.getDocCode());			//发文文号
						stampLog.setOperateIp(getRequest().getRemoteAddr());//IP
						stampLog.setOperateTime(sdf.parse(signtime)); 		
						stampLog.setStampName(signname);
						stampLog.setStampUser(signer);						//签章持有人
						stampLog.setOperateUser(user.getUserName());		//操作人
						stampLog.setStampType(getText("transdoc.sign.add"));					//盖章日志类型
						moniDocManager.save(stampLog);
						logger.info(new StringBuilder("保存签章信息:").append("签章持有人=")
								.append(signer).append(";签章名称=").append(signname)
								.append(";签章时间=").append(signtime).append(";签章是否锁定=")
								.append(isLockDoc).append(";签章人IP=").append(stampLog.getOperateIp()));
						
						if("true".equals(isLockDoc)){
							TdocStampLog stampLogLock = new TdocStampLog();
							stampLogLock.setDepartName(organization.getOrgName());  //盖章人所在机构名称
							stampLogLock.setDocCode(model.getDocCode());			//发文文号
							stampLogLock.setOperateIp(getRequest().getRemoteAddr());//IP
							stampLogLock.setOperateTime(new Date()); 		
							stampLogLock.setStampName(signname);
							stampLogLock.setStampUser(signer);						//签章持有人
							stampLogLock.setOperateUser(user.getUserName());		//操作人
							stampLogLock.setStampType(getText("transdoc.sign.lock"));					//盖章日志类型
							moniDocManager.save(stampLogLock);
						}
						
				}
			}
		}		
			logger.error("原签章信息:" + oldSignInfo);
			logger.error("新签章信息:" + signInfo);
			//处理签章信息变更的情况,主要对比oldSignInfo,signInfo
			if(oldSignInfo != null && !"".equals(oldSignInfo) && !"[]".equals(oldSignInfo)){ 
				JSONArray jsonArrayOld = JSONArray.fromObject(oldSignInfo);
				if(organization == null){
					organization = userService.getUserDepartmentByUserId(user.getUserId());//得到当前用户所在机构
				}
				if(signInfo != null && !"".equals(signInfo) && !"[]".equals(signInfo)){ 
					if(!oldSignInfo.toString().equals(signInfo.toString())){//两者信息不一致
						for(int i=0;i<jsonArrayOld.size();i++){
							JSONObject jsonObjOld = jsonArrayOld.getJSONObject(i);
							if(jsonObjOld != null){
								String isLockDocOld = jsonObjOld.getString("isLockDoc");	//签章是否锁定
								String signerOld    = jsonObjOld.getString("signer");		//签章持有人
								String signtimeOld  = jsonObjOld.getString("signtime");		//签章时间	
								String signnameOld  = jsonObjOld.getString("signname");		//签章名称
								boolean isExist = false;
							
								for(int j=0;j<jsonArray.size();j++){
									JSONObject jsonObj = jsonArray.getJSONObject(j);
									if(jsonObj != null){
										String isLockDoc = jsonObj.getString("isLockDoc");	//签章是否锁定
										String signtime  = jsonObj.getString("signtime");		//签章时间	
										String signname  = jsonObj.getString("signname");		//签章名称
										String signer    = jsonObj.getString("signer");		//签章持有人
										if(signtimeOld.equals(signtime)&&signnameOld.equals(signname)&&signerOld.equals(signer)){     
											isExist = true ;								//签章仍然存在
											if(!isLockDocOld.equals(isLockDoc)){			//
												if(isLockDoc.equals("true")){
													TdocStampLog stampLog = new TdocStampLog();
													stampLog.setDepartName(organization.getOrgName());  //盖章人所在机构名称
													stampLog.setDocCode(model.getDocCode());			//发文文号
													stampLog.setOperateIp(getRequest().getRemoteAddr());//IP
													stampLog.setOperateTime(new Date()); 		
													stampLog.setStampName(signnameOld);
													stampLog.setStampUser(signerOld);					//签章持有人
													stampLog.setOperateUser(user.getUserName());		//操作人
													stampLog.setStampType(getText("transdoc.sign.lock"));					//盖章日志类型
													moniDocManager.save(stampLog);
												}else{
													TdocStampLog stampLog = new TdocStampLog();
													stampLog.setDepartName(organization.getOrgName());  //盖章人所在机构名称
													stampLog.setDocCode(model.getDocCode());			//发文文号
													stampLog.setOperateIp(getRequest().getRemoteAddr());//IP
													stampLog.setOperateTime(new Date()); 
													stampLog.setStampName(signnameOld);
													stampLog.setStampUser(signerOld);					//签章持有人
													stampLog.setOperateUser(user.getUserName());		//操作人
													stampLog.setStampType(getText("transdoc.sign.unlock"));					//盖章日志类型
													moniDocManager.save(stampLog);
												}
											}
											break;
										}
									}	
								}	
								if(!isExist){//删除了签章
									TdocStampLog stampLog = new TdocStampLog();
									stampLog.setDepartName(organization.getOrgName());  //盖章人所在机构名称
									stampLog.setDocCode(model.getDocCode());			//发文文号
									stampLog.setOperateIp(getRequest().getRemoteAddr());//IP
									stampLog.setOperateTime(new Date()); 	 
									stampLog.setStampName(signnameOld);
									stampLog.setStampUser(signerOld);					//签章持有人
									stampLog.setOperateUser(user.getUserName());		//操作人
									stampLog.setStampType(getText("transdoc.sign.delete"));					//盖章日志类型
									moniDocManager.save(stampLog);
								}
							}	
						}	
					}
				}else{
					for(int i=0;i<jsonArrayOld.size();i++){
						JSONObject jsonObjOld = jsonArrayOld.getJSONObject(i);
						if(jsonObjOld != null){
							String signerOld    = jsonObjOld.getString("signer");		//签章持有人
							String signnameOld  = jsonObjOld.getString("signname");		//签章名称
							TdocStampLog stampLog = new TdocStampLog();
							stampLog.setDepartName(organization.getOrgName());  //盖章人所在机构名称
							stampLog.setDocCode(model.getDocCode());			//发文文号
							stampLog.setOperateIp(getRequest().getRemoteAddr());//IP
							stampLog.setOperateTime(new Date()); 	 
							stampLog.setStampName(signnameOld);
							stampLog.setStampUser(signerOld);					//签章持有人
							stampLog.setOperateUser(user.getUserName());		//操作人
							stampLog.setStampType(getText("transdoc.sign.delete"));					//盖章日志类型
							moniDocManager.save(stampLog);
						}
					}	
				}
			}
			if(type != null && "1".equals(type)){//提交分发或提交签章才记录到日志
				String docState = model.getDocState();
				if(docState != null && "1".equals(docState)){
					docState = getText("transdoc.state.sign");
				}
				if(docState != null && "2".equals(docState)){
					docState = getText("transdoc.state.send");
				}		
				OALogInfo info = new OALogInfo(getText(GlobalBaseData.SENDDOC_SUBMIT_DOC,new String[]{user.getUserName(),model.getDocTitle(),docState}));
				manager.save(model,info);
			}else{//保存的动作不记录到日志
				manager.save(model);				
			}
			ret = "0,"+model.getDocId();
		}catch(Exception e){
			logger.error("保存公文数据失败",e);
			ret = "-2";
		}finally{
			if(bos != null){
				bos.close();
			}
			if(is != null){
				is.close();
			}
			if(os != null){
				os.close();
			}
		}
		return renderText(ret);
	}


	public TtransDoc getModel() {
		return model;
	}

	/**
	 * 兼容2003 - 2007 打开空文档。
	 * @author:邓志城
	 * @date:2010-4-13 上午11:49:00
	 */
	public void openEmptyDocFromUrl() {
		HttpServletResponse response = this.getResponse();
		response.reset();
		response.setContentType("application/octet-stream");
		OutputStream output = null;
		String type = ".doc";
		logger.info("文档类型：" + docType);
		try{
			if("1".equals(docType)){
				type = ".doc";
			}else if("2".equals(docType)){
				type = ".xls";
			}else if("3".equals(docType)){
				type = ".ppt";
			}else if("4".equals(docType)){
				type = ".vsd";
			}else if("5".equals(docType)){
				type = ".mpp";
			}else if("6".equals(docType)){
				type = ".wps";
			}
			String rootPath = getRequest().getSession().getServletContext().getRealPath("/empty"+type);
			output = response.getOutputStream();
			File file = new File(rootPath);
			if(!file.exists()){
				rootPath = getRequest().getSession().getServletContext().getRealPath("/empty.doc");
				file = new File(rootPath);
			}
			logger.info("打开文档:"+rootPath);
			byte[] buf = FileUtil.inputstream2ByteArray(new FileInputStream(file));
			output.write(buf);
		}catch(Exception e){
			logger.error("打开文档出错了。",e);
		}finally{
			if(output!=null){
				try {
					output.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}

	/**
	 * 打开公文正文
	 * @author:邓志城
	 * @date:2010-8-13 下午02:52:32
	 * @throws Exception
	 */
	public void openWordDocFromUrl() throws Exception {
		if(model.getDocId() != null && !"".equals(model.getDocId())){
			model = manager.getDocById(model.getDocId());
			HttpServletResponse response = getResponse();
			response.reset();
			response.setContentType("application/octet-stream");
			OutputStream output = null;
			try {
				output = response.getOutputStream();
				output.write(model.getDocContent());
			} catch (Exception e) {
				logger.error("打开公文正文内容异常。",e);
			}finally{
				if(output != null){
					output.close();
				}
			}
		}
	}

	/**
	 * 下载附件
	 * @author:邓志城
	 * @date:2010-8-13 下午05:41:37
	 * @throws Exception
	 */
	public void downLoad() throws Exception {
		String id  = model.getDeledAttachId();//得到要下载的附件
		HttpServletResponse response = super.getResponse();
		TtransDocAttach file = attachManager.getAttach(id);
		response.reset();
		response.setContentType("application/x-download");         //windows
		OutputStream output = null;
		ByteArrayOutputStream bos = null;
		InputStream is = null;
		try{
			response.addHeader("Content-Disposition", "attachment;filename=" +
			         new String(file.getAttachFileName().getBytes("gb2312"),"iso8859-1"));
		    output = response.getOutputStream();
		    String filePath = file.getAttachFilePath();
		    String rootPath = PathUtil.getRootPath();
		    File attchFile = new File(rootPath + filePath);
		    if(attchFile.exists()){
		    	is = new FileInputStream(attchFile);
		    	byte[] buf = null;
				byte[] b = new byte[8192];
				int read = 0;
				bos = new ByteArrayOutputStream();
				while((read=is.read(b))!=-1){
					
					bos.write(b, 0, read);
				}
				buf = bos.toByteArray();
				output.write(buf);
				output.flush();
		    }
		} catch(Exception e) {
			if(logger.isErrorEnabled()){
				logger.error(e.getMessage(),e);
			}
		} finally {	
			if(bos != null){
				bos.close();
			}
		    if(output != null){
		      try {
				output.close();
			} catch (IOException e) {
				if(logger.isErrorEnabled()){
					logger.error(e.getMessage(),e);
				}
			}
			if(is != null){
				is.close();
			}
		   }
		}
	}

	/**
	 * 插入模板时显示树形结构.
	 * @author:邓志城
	 * @date:2010-7-9 下午07:54:47
	 * @return	WORD模板类别树
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String templateTree() throws Exception {
		List<ToaDoctemplateGroup> typeLst = tempTypeManager.getAllTypeTemplate();//得到模板所有类别
		Map<Object, List<Object[]>> map = tempItemManager.getTemplateMap();
		List<TempPo> tempPoList = new ArrayList<TempPo>();//树节点列表
		if(typeLst != null && !typeLst.isEmpty()){
			for(ToaDoctemplateGroup type : typeLst){
				if(!"1".equals(type.getDocgroupType())){
					TempPo po = new TempPo();
					po.setType("type");//类别节点
					po.setId(type.getDocgroupId());
					po.setName(type.getDocgroupName());
					po.setParentId(type.getDocgroupParentId());
					tempPoList.add(po);
					List<Object[]> tempLst = map.get(po.getId());
					if(tempLst != null && !tempLst.isEmpty()){
						for(Object[] obj : tempLst){
							TempPo templatePo = new TempPo();
							templatePo.setId(obj[0].toString());
							templatePo.setName((String)obj[1]);
							templatePo.setParentId(po.getId());
							templatePo.setType("item");//模板节点
							tempPoList.add(templatePo);
						}
					}
				}
			}
		}
		getRequest().setAttribute("typeList", tempPoList);
		return "templatetree";
	}

	/**
	 * 读取标签与表单模板的映射关系.
	 * @author:邓志城
	 * @date:2010-7-11 下午10:49:12
	 * @return
	 */
	public String readBookMarkInfo() {
		try{
			Assert.notNull(formId,"表单id不能为空。");
			List list = bookMarkService.getBookMarkList(formId);
			JSONArray array = new JSONArray();
			if(list != null && !list.isEmpty()){
				for(int i=0;i<list.size();i++){
					Object[] objs = (Object[])list.get(i);
					JSONObject obj = new JSONObject();
					obj.put("bookMarkName", objs[1]);	//标签名称
					obj.put("componentName", objs[2]);	//控件名称
					obj.put("fieldName", objs[3]);		//控件绑定的字段名称
					obj.put("tableName", objs[4]);		//控件绑定的表名称
					array.add(obj);
				}
			}
			logger.info(array.toString());
			return this.renderText(array.toString());
		}catch(Exception e){
			logger.error("读取标签与电子表单映射时异常", e);
			return this.renderText("-1");
		}
	}

	/**
	 * 退回公文
	 * 	返回状态为0成功；返回-1退回失败
	 * @author:邓志城
	 * @date:2010-8-23 上午09:16:40
	 */
	public void doBack() {
		String ret= "";
		try{
			String id = model.getDocId();
			String content = model.getRest2();
			model = manager.getDocById(id);
			model.setRest2(content);						 //退回意见
			model.setDocState(DocType.ReturnBack.getValue());//退回状态
			
			User user = userService.getCurrentUser();
			OALogInfo info = new OALogInfo(getText(GlobalBaseData.SENDDOC_BACK_DOC,new String[]{user.getUserName(),model.getDocTitle(),content}));
			manager.update(model,info);
			ret = "0";
		}catch(Exception e){
			logger.error("退回公文时发生异常", e);
			ret = "-1";
		}
		this.renderText(ret);
	}

	/**
	 * 查看公文退回原因
	 * @author:邓志城
	 * @date:2010-8-24 上午10:56:53
	 * @return
	 * @throws Exception
	 */
	public String viewBackReason() throws Exception {
		if(model.getDocId() != null){
			model = manager.getDocById(model.getDocId());
		}else{
			logger.error("id为" + model.getDocId() + "的公文不存在或已删除。");
		}
		return "viewbackreason";
	}

	/**
	 * 得到机构列表以及机构分组列表
	 * @author:邓志城
	 * @date:2010-8-25 下午03:53:16
	 * @return
	 * @throws Exception
	 */
	public String chooseDept() throws Exception {
//		String userId = userService.getCurrentUser().getUserId();
//		orgList = userService.getAllDeparments();
		
		//获取所有的机构
		orgList = userService.getOrgAgency("0", "1");

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
		
		/*for(int i=0;i < orgList.size();i++){
			System.out.println("单位>>：" + orgList.get(i).getOrgName());
			System.out.println("编码>>：" + orgList.get(i).getOrgSyscode());
			System.out.println("xx:" + orgList.get(i).getSupOrgCode());
			System.out.println("排序:" + i);
			System.out.println("\n");
			
			if(orgList.get(i).getOrgSyscode().length() == 3){
				System.out.println("就是他：***************************************************" + orgList.get(i).getOrgSyscode()+":" + orgList.get(i).getOrgName());
			}
		}*/
		
		orgGroupList = groupManager.getAgencyListByUserId(userService.getCurrentUser().getUserId());
		return "choosedept2";
	}
	public String listValue(){
		//借用formid  传参数
		String[] ids = formId.split(",");
		for(int i=0;i<ids.length;i++){
			TtransDoc t = manager.getDocById(ids[i]);
			if(null==t.getDocOfficialTime()||"".equals(t.getDocOfficialTime())){
				return renderText(t.getDocTitle());
			}else{
				return renderText("false");
			}
		}
		return renderText("false");
	}
	
	/**
	 * 到退到公文传输的页面  --- 之前的
	 * 
	 * @author 肖利建
	 * @return
	 * @throws Exception
	 * @createTime 2013-01-24 10:51:23
	 */
	public String retdoctrans(){
		String docSendId = getRequest().getParameter("docSendId");
		if(docSendId != null && !"".equals(docSendId)){
			model = docSendManager.getTtransDocBysenddocId(docSendId);
		}
		
		getRequest().setAttribute("laiwenDW", model.getDocIssueDepartSigned());
		getRequest().setAttribute("laiwentitle", model.getDocTitle());
		getRequest().setAttribute("docSendId", docSendId);
		//System.out.println("dw:" + model.getDocIssueDepartSigned());
		//System.out.println("title:" + model.getDocTitle());
		return "rejectRmk";
	}
	
	/**
	 * 到退到公文传输的页面
	 * 
	 * @author 肖利建
	 * @return
	 * @throws Exception
	 * @createTime 2013-02-04 15:03:59
	 */
	public String tuiwenret(){
		String bussinessId = getRequest().getParameter("bussinessId");
		modelDocRet = sendDocRetManager.getDocRetBybusId(bussinessId);
		return "retrecvRmk";
	}
	
	/**
	 * 到退到公文传输的页面
	 * 
	 * @author 肖利建
	 * @return
	 * @throws Exception
	 * @createTime 2013-02-04 15:03:59
	 */
	public String firsttuiwenret(){
		return "firstretrecvRmk";
	}
	
	/**
	 * 收文办件登记退到公文公文分面结点
	 * 
	 * @author 肖利建
	 * @return
	 * @throws Exception
	 * @createTime 2013-01-30 19:11:53
	 */
	public String tuiwen(){
		return "retrecv";
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	@SuppressWarnings("deprecation")
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

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public File getWordDoc() {
		return wordDoc;
	}

	public void setWordDoc(File wordDoc) {
		this.wordDoc = wordDoc;
	}

	public File[] getAttachMent() {
		return attachMent;
	}

	public void setAttachMent(File[] attachMent) {
		this.attachMent = attachMent;
	}

	public String[] getAttachMentName() {
		return attachMentName;
	}

	public void setAttachMentFileName(String[] attachMentName) {
		this.attachMentName = attachMentName;
	}

	public Page<Object[]> getPage() {
		return page;
	}

	public void setPage(Page<Object[]> page) {
		this.page = page;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(String signInfo) {
		this.signInfo = signInfo;
	}

	public List getOrgGroupList() {
		return orgGroupList;
	}

	public List<Organization> getOrgList() {
		return orgList;
	}

	public String getOldSignInfo() {
		return oldSignInfo;
	}

	public void setOldSignInfo(String oldSignInfo) {
		this.oldSignInfo = oldSignInfo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public void setAttachMentName(String[] attachMentName) {
		this.attachMentName = attachMentName;
	}
	public TdocSendRet getModelDocRet() {
		return modelDocRet;
	}
	public void setModelDocRet(TdocSendRet modelDocRet) {
		this.modelDocRet = modelDocRet;
	}

}
