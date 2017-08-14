/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息附件 manager类
 */
package com.strongit.oa.message;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaMessageFolder;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 处理消息文件夹类
 * @Create Date: 2009-12-7
 * @author luosy
 * @version 1.0
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "messageFolder.action", type = ServletActionRedirectResult.class) })
public class MessageFolderAction extends BaseActionSupport {
	
	private java.util.List<ToaMessageFolder> folderList;

	private String msgFolderId;

	private ToaMessageFolder model = new ToaMessageFolder();
	
	private MessageFolderManager manager;
	
	private String msgType;//消息类别
	
	@Autowired IWorkflowService workflowService;

	public java.util.List getFolderList() {
		return folderList;
	}

	public String getMsgFolderId() {
		return msgFolderId;
	}

	public void setMsgFolderId(String aMsgFolderId) {
		msgFolderId = aMsgFolderId;
	}

	public ToaMessageFolder getModel() {
		return model;
	}
	
	@Autowired
	public void setManager(MessageFolderManager manager) {
		this.manager = manager;
	}

	public void setModel(ToaMessageFolder model) {
		this.model = model;
	}

	
	////////////////////////////// 以下为action跳转方法
	
	/**
	 * @roseuid 496AFCED000F
	 */
	public MessageFolderAction() {
		
	}

	/**
	 * author:luosy
	 * description:	跳转到添加页面
	 * modifyer:
	 * description:
	 * @return
	 */
	public String input() throws Exception {
		return "add";
	}

	/**
	 * author:luosy
	 * description:	得到消息文件夹
	 * modifyer:
	 * description:
	 * @return
	 */
	public String list() throws Exception {
		folderList = manager.getFolderListByUser("");
		return SUCCESS;
	}

	/**
	 * author:luosy
	 * description:	保存
	 * modifyer:
	 * description:
	 * @return
	 */
	public String save() throws Exception {
		
		String folderName = model.getMsgFolderName();
		try {
			if(!"".equals(folderName)&&null!=folderName){
				folderName = java.net.URLDecoder.decode(folderName, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(manager.isExistFolder(folderName)){
			return renderText("exist");
		}else{
			model.setMsgFolderName(folderName);
			manager.saveFolder(model, new OALogInfo("我的消息-自定义文件夹-『保存』："+model.getMsgFolderName()));
			return renderText("succ");
		}
	}

	/**
	 * author:luosy
	 * description:	删除
	 * modifyer:
	 * description:
	 * @return
	 */
	public String delete() throws Exception {
		if(msgFolderId!=null&&!"".equals(msgFolderId)){
			if(msgFolderId.length()>30){
				ToaMessageFolder delObj=manager.getMsgFolderById(msgFolderId);
				if(delObj==null){
					return renderText("notexits");
				}else{
					try{
						manager.delFolder(delObj, new OALogInfo("我的消息-自定义文件夹-『删除』："+delObj.getMsgFolderName()));
						return renderText("true");
					}catch(Exception e){
						logger.error(this, e);
						return renderText("false");
					}
				}
			}else{
				return renderText("baseFolder");
			}
		}else{
			return renderText("noid");
		}
	}
	
	/**
	 * author:luosy
	 * description: 文件夹是否为空
	 * modifyer:
	 * description:
	 * @return
	 */
	public String isNullFolder(){
		if(msgFolderId!=null&&!"".equals(msgFolderId)){
			if(msgFolderId.length()>30){
				if(manager.isNullFolder(msgFolderId)){
					return renderText("nullFolder");
				}else{
					return renderText("notNull");
				}
			}else{
				return renderText("baseFolder");
			}
		}else{
			return renderText("noid");
		}
	}
	
	public String getUnreadCount() throws Exception {
		String count = manager.getUnreadCount(msgFolderId,"");
		renderText(msgFolderId+","+count);
		return null;
	}
	
	public String getAll() throws Exception{
		String readed=manager.getReadedCount(msgFolderId, "");
		String unread=manager.getUnreadCount(msgFolderId,"");
		int u=Integer.parseInt(unread);
		int r=Integer.parseInt(readed);
		int a=u+r;
		String all=a+"";
		renderText(msgFolderId+","+unread+","+all);
		return null;
	}

	/**
	 * 弹出消息
	 * <P>对消息作了归类处理,类别的获取是通过注解和反射方式实现,如果以后需要
	 * 引入新的类型，只需要在定义类型的类中加入,然后注解就可以实现动态扩展</P>
	 * @author:邓志城
	 * @date:2009-7-13 上午10:53:37
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getPopMsg() throws Exception{
		StringBuffer html = new StringBuffer("&nbsp;&nbsp;");
		html.append("<table align=\"center\">");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(this.getSession().getCreationTime());
		List<String> lst = manager.getNewMsgForWork(cal.getTime());
		
		/*if(lst==null || lst.size()==0){
			return renderText("0");
		}*/

		Integer unread =Integer.valueOf(manager.getUnreadForMessage(cal.getTime()));
		if(unread == 0){
			return renderText("0");
		}
		
		int msgcount = unread - Integer.valueOf(lst.size());
		if(msgcount>0){
			html.append("<tr><td>");
			html.append("<A href=\"javascript:void(0)\" style=\"text-decoration: none;FONT-WEIGHT: normal; FONT-SIZE: 12px; COLOR: #1f336b; PADDING-TOP: 4px\" msgType='' url=\""+getRequest().getContextPath()+"/message/messageFolder!msgRedirectByMsgType.action\" hidefocus=\"false\" name=\"btCommand\">");
			html.append("最新消息");
			html.append("（<font color=\"red\">"+msgcount+"</font>）");
			html.append("</A>");
			html.append("</td></tr>");	
		}
		
		//每个类型对应的注解名称
		String name = "待办事宜";
		//每个类型对应的类型值
		String value = null;
		List<Object[]> typeList = workflowService.getAllProcessTypeList();
		if(!lst.isEmpty()){
			for(Object[] objs : typeList){
				value = objs[0].toString();
				name = objs[1].toString();
				int count = 0;
				for(String msgType:lst){
					if(value.equals(msgType)){
						count = count +1;
					}
				}
				if(count>0){
					html.append("<tr><td>");
					html.append("<A href=\"javascript:void(0)\" style=\"text-decoration: none;FONT-WEIGHT: normal; FONT-SIZE: 12px; COLOR: #1f336b; PADDING-TOP: 4px\" msgType="+value+" url=\""+getRequest().getContextPath()+"/message/messageFolder!msgRedirectByMsgType.action?msgType="+value+"\" hidefocus=\"false\" name=\"btCommand\">");
					html.append(name);
					html.append("（<font color=\"red\">"+count+"</font>）");
					html.append("</A>");
					html.append("</td></tr>");				
				}
			}
		}
		html.append("</table>");
		LogPrintStackUtil.logInfo("*消息内容：*" + html.toString());
		return renderText(html.toString()); 
	}

	/**
	 * 根据消息类别重新定位到对应的模块
	 * <p>如果是工作流相关则跳转到待办任务列表,同时需要更新消息状态,以避免下次再显示此类消息</p>
	 * @author:邓志城
	 * @date:2009-7-13 上午11:02:26
	 * @return
	 * @throws Exception
	 */
	public void msgRedirectByMsgType()throws Exception{
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		String url = "";
		String path = request.getContextPath();
		//判断消息类型
		if(msgType!= null && !"".equals(msgType)) {
			url = path + "/senddoc/sendDoc!todoWorkflow.action?workflowType="+msgType;
		} else {
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}
		/*if(msgType.equals(String.valueOf(WorkFlowTypeConst.COLUMN))){//栏目类型
			url = path + "/infopub/articlesappro/articlesAppro!todo.action?workflowType=1";
		}else if(msgType.equals(String.valueOf(WorkFlowTypeConst.SENDDOC))){//发文处理
			url = path + "/senddoc/sendDoc!todo.action?workflowType=2";
		}else if(msgType.equals(String.valueOf(WorkFlowTypeConst.RECEDOC))){//收文处理
			url = path + "/recvdoc/recvDoc!todo.action?workflowType=3";
		}else if(msgType.equals(String.valueOf(WorkFlowTypeConst.INSPECT))){//督察督办
			url = path + "/inspect/inspect!todo.action?workflowType=4";
		}else if(msgType.equals(String.valueOf(WorkFlowTypeConst.MEETING))){//会议管理
			url = path + "/meetingmanage/meetingaudit/meetingaudit.action";
		}else if(msgType.equals(String.valueOf(WorkFlowTypeConst.TIANJIANYI))){//提案建议
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}else if(msgType.equals(String.valueOf(WorkFlowTypeConst.XINFANGGUANLI))){//信访管理
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}else if(msgType.equals(String.valueOf(WorkFlowTypeConst.ZHIBANGUANLI))){//值班管理
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}else if(msgType.equals(String.valueOf(WorkFlowTypeConst.XINXICHULI))){//信息处理
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}else if(msgType.equals(String.valueOf(WorkFlowTypeConst.XINWENGUANLI))){//新闻管理
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}else if(msgType.equals(String.valueOf(WorkFlowTypeConst.QIANBAOGUANLI))){//签报管理
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}else if(msgType.equals(String.valueOf(WorkFlowTypeConst.DANGDANGUANLI))){//档案管理
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}else if(msgType.equals(GlobalBaseData.MSG_GZCL)){//工作处理
			url = path + "/fileNameRedirectAction.action?toPage=work/work-todomain.jsp";
		}else if(msgType.equals(GlobalBaseData.MSG_CALENDAR)){//日程【跳到消息列表】
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}else if(msgType.equals(GlobalBaseData.MSG_KNOWLEDGE)){//知识库【跳到消息列表】
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}else if(msgType.equals(GlobalBaseData.MSG_MEETING)){//会议通知【跳到消息列表】
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}else{//如果没找到则转到消息列表
			url = path + "/fileNameRedirectAction.action?toPage=message/message.jsp";
		}*/
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(this.getSession().getCreationTime());
		manager.setReadStateByType(msgType,cal.getTime());
		response.sendRedirect(url);
	}
	
	protected void prepareModel() {
		if(!"".equals(msgFolderId)&&null!=msgFolderId&&msgFolderId.length()>30){
			model = manager.getMsgFolderById(msgFolderId);
		}

	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}


}
