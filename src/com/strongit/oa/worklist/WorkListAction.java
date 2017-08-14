package com.strongit.oa.worklist;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.message.MessageFolderManager;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;


@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "workList.action", type = ServletActionRedirectResult.class) })
public class WorkListAction extends BaseActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7853415246088762497L;
	
	private String userName;			//当前用户名；
	private String password;
	private IWorkflowService manager;	//工作流；
	private MessageFolderManager msgForlderManager;
	private IWorkListManager workManager;
	
	private String type;
	private Page page=new Page(1000000,true);
	
	@Autowired IUserService userService;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Autowired
	public void setMsgForlderManager(MessageFolderManager msgForlderManager) {
		this.msgForlderManager = msgForlderManager;
	}
	
	@Autowired
	public void setManager(IWorkflowService manager) {
		this.manager = manager;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		try {
			List<Object[]> lst = manager.getAllProcessTypeList();
			StringBuffer innerHtml = new StringBuffer();
			int i = 0;
			Object[] obj = null;
			innerHtml.append("<table>");
			while(i < lst.size()){
				obj = lst.get(i);
				innerHtml.append("<tr>")
				 .append("<td class=\"biao_bg1\">")
				 .append("<span>")
				 .append("<input type=\"checkbox\" name=\"chname\"  value=\""+obj[0].toString()+"\" id=\""+obj[0].toString()+"\">")
				 .append(obj[1].toString())
				 .append("</input>")
				 .append("</span>")
				 .append("</td>");
				if((i + 1) < lst.size()){
					obj = lst.get(i + 1);
					innerHtml.append("<td class=\"biao_bg1\">")
					 .append("<span>")
					 .append("<input type=\"checkbox\" name=\"chname\" value=\""+obj[0].toString()+"\" id=\""+obj[0].toString()+"\">")
					 .append(obj[1].toString())
					 .append("</input>")
					 .append("</span>")
					 .append("</td>");
				}
				if((i + 2) < lst.size()){
					obj = lst.get(i + 2);
					innerHtml.append("<td class=\"biao_bg1\">")
					 .append("<span>")
					 .append("<input type=\"checkbox\" name=\"chname\" value=\""+obj[0].toString()+"\" id=\""+obj[0].toString()+"\">")
					 .append(obj[1].toString())
					 .append("</input>")
					 .append("</span>")
					 .append("</td>");
				}
				i+=3;
				obj = null;
				innerHtml.append("</tr>");
			}
			innerHtml.append("<tr class=\"biao_bg1\">");
			innerHtml.append("<td>");
			innerHtml.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			innerHtml.append("<input  type=\"button\"  class=\"input_bg\" value=\"确认\" onclick=\"javascript:addCookie()\"/>");
		    innerHtml.append("</td>");
		    innerHtml.append("<td>");
		    innerHtml.append("</td>");
		    innerHtml.append("<td>");
	        innerHtml.append("&nbsp;&nbsp;&nbsp;&nbsp;");
	        innerHtml.append("<input type=\"button\" class=\"input_bg\" value=\"取消\" onclick=\"window.close()\" />");
	        innerHtml.append("</td>");
			innerHtml.append("</tr>");
			innerHtml.append("</table>");
			logger.info(innerHtml.toString());
			return this.renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return null;
	}

	public void GenerateRandom() throws Exception {
		Random rand = new Random();
	    long randnum = Math.abs(rand.nextLong());
	    getRequest().getSession(true).setAttribute("RandNum", String.valueOf(randnum));
	    HttpServletResponse response = getResponse();
	    response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
		PrintWriter pw = response.getWriter();
		pw.println(String.valueOf(randnum));
		pw.close();
	}
	
	public void doCheckLogin() {
		String j_userName = getRequest().getParameter("j_username");
		String j_password = getRequest().getParameter("j_password");
	}
	
	@SuppressWarnings("unchecked")
	public void LoadToDoWork() {
		try{
			if(userName != null && !"".equals(userName)) {
				User user = userService.getUserInfoByLoginName(userName);
				String userId = user.getUserId();
				StringBuilder html = new StringBuilder();
				if(userId != null) {
					Object[] toSelectItems = {"taskId","processTypeId","processTypeName","processName"};
					List sItems = Arrays.asList(toSelectItems);
					Map<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put("taskType", "2");//取非办结任务
					paramsMap.put("processSuspend", "0");// 取非挂起任务
					paramsMap.put("handlerId", userId);
					Map orderMap = new HashMap<Object, Object>();
					orderMap.put("processTypeId", "0");
					List<Object[]> list = manager.getTaskInfosByConditionForList(sItems, paramsMap, orderMap, null, null, null, null);
					Map<String, List<Object[]>> map = new HashMap<String, List<Object[]>>();
					for(Object[] objs : list) {
						String workflowType = (String)objs[1];
						if(workflowType != null) {
							if(!map.containsKey(workflowType)) {
								List<Object[]> tempList = new ArrayList<Object[]>();
								tempList.add(objs);
								map.put(workflowType, tempList);
							} else {
								map.get(workflowType).add(objs);
							}
						}
					}
					String message = msgForlderManager.getUnreadCount("recv", userId);
					//生成HTML代码
					html.append("<span style=\"cursor:hand\" onclick=\"loginOA()\">");
					html.append("		您有");
					html.append("	<font color=\"red\">");
					html.append("		(").append(list.size()).append(")");
					html.append("	</font>");
					html.append("		项待办工作&nbsp;");
					html.append("	<font color=\"red\">");
					html.append("		(").append(Integer.parseInt(message)).append(")");
					html.append("	</font>").append("封未读内部邮件");
					html.append("</span>");
					//生成表格
					html.append("<table>");
					Set<String> set = map.keySet();
					List<String> typeList = new ArrayList<String>(set);
					Collections.sort(typeList,new Comparator<String>(){

						public int compare(String o1, String o2) {
							Long l1 = Long.parseLong(o1);
							return l1.compareTo(Long.parseLong(o2));
						}
						
					});
					String rootPath = getRequest().getContextPath();
					if(rootPath.endsWith("/")) rootPath="";
					int i = 0;
					while(i < typeList.size()){
						html.append("<tr>");
						//第一列
						List<Object[]> tempList = map.get(typeList.get(i));
						if(tempList != null && !tempList.isEmpty()) {
							String typeName = (String)tempList.get(0)[2];
							int count = map.get(typeList.get(i)).size();
							html.append("<td>")
								.append("<img width=12 height=12 src=\"").append(rootPath)
								.append("/oa/image/desktop/work.gif")
								.append("\"/>")
								.append("<a href=\"#\" onclick=\"loginOA()\">")
								.append(typeName)
								.append("<font color='red'>(")
								.append(count)
								.append(")</a>")
								.append("</td>");
								
							
						}
						//第二列
						if((i + 1) < typeList.size()){
							List<Object[]> tempList2 = map.get(typeList.get(i+1));
							if(tempList2 != null && !tempList2.isEmpty()) {
								String typeName = (String)tempList2.get(0)[2];
								int count = tempList2.size();
								html.append("<td>")
									.append("<img width=12 height=12 src=\"").append(rootPath)
									.append("/oa/image/desktop/work.gif")
									.append("\"/>")
									.append("<a href=\"#\" onclick=\"loginOA()\">")
									.append(typeName)
									.append("<font color='red'>(")
									.append(count)
									.append(")</a>")
									.append("</td>");
								
							}
						}
						html.append("</tr>");
						i+=2;
					}
					
					html.append("</table>");
				}
				this.renderText(html.toString());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public String list() throws Exception {
		try {
			if((userName != null && !" ".equals(userName)))
			{
				String userId = workManager.getUserIDByUserName(userName);
				String message = msgForlderManager.getUnreadCount("recv", userId);
				List<Object[]> list = manager.getAllProcessTypeList();
				page = manager.getTasksTodo(page, userId, "all", null, null, null, null, null,"4");
				Map<String,String> typeMap = workManager.getProccessTypeNameAndCount(page);
				List<String> typeList = new ArrayList<String>();
				StringBuffer innerHtml = new StringBuffer();
				String rootPath = getRequest().getContextPath();
				int count=page!=null?page.getTotalCount():0;
				Object[] obj = null;
				if(type != null && !"".equals(type))
				{
					String[] id = type.split(",");
					for(int i=1 ; i < list.size() ; i++)
					{
						    for(int j = 0;j < id.length;j++)
						    {
						        if(id[j].equals(String.valueOf(list.get(i)[0])))
						        {
						    	    obj = list.get(i);
						    	    int n = Integer.parseInt(typeMap.get(obj[1].toString()));
									typeList.add("<font size=\"1.5px\">"+obj[1].toString()+"</font>"+"<font size=\"1px\" color='red'>("+n+")</font>");
						        }
						    }
							
					}
					innerHtml.append("<span>");
					innerHtml.append("<font size=\"1px\">"+"您有"+"</font><font size=\"1px\" color=\"red\">(").append(count)
					 .append(")</font>").append("<font size=\"1px\">"+"项待办工作"+"</font>");
					innerHtml.append("&nbsp;<font size=\"1px\" color=\"red\">(").append(Integer.parseInt(message))
					 .append(")</font>").append("<font size=\"1px\">"+"封未读内部邮件"+"</font>");
					innerHtml.append("&nbsp;<a href=\"#\" onclick=\"javascript:reset()\">");
					innerHtml.append("<font size=\"1px\" color=\"red\">"+"重新选择"+"</font>");
					innerHtml.append("</a>");
					innerHtml.append("</span>");
					innerHtml.append("<table>");
					int i = 0;
					while(i < typeList.size()){
						innerHtml.append("<tr>")
								 .append("<td>")
								 .append("<img src=\"").append(rootPath)
											   .append("/oa/image/desktop/work.gif")
											   .append("\"/>")
								 .append("<a href=\"#\" onclick=\"loginOA()\">")
								 .append(typeList.get(i))
								 .append("</a>")
								 .append("</td>");
								 if((i + 1) < typeList.size()){
									 innerHtml.append("<td>&nbsp;")
									 		  .append("<img src=\"").append(rootPath)
											     	.append("/oa/image/desktop/work.gif")
											        .append("\"/>")
								              .append("<a href=\"#\" onclick=\"javascript:loginOA()\">")
								              .append(typeList.get(i+1))
								              .append("</a>");
								 }
					     i+=2;
					}
					innerHtml.append("</table>");
					LogPrintStackUtil.logInfo(innerHtml.toString());
					return this.renderHtml(innerHtml.toString());//用ren
				}else{
					for(int i=0 ; i < list.size() ; i++)
					{
							obj = list.get(i);
							int n = Integer.parseInt(typeMap.get(obj[1].toString()));
							typeList.add("<font size=\"1.5px\">"+obj[1].toString()+"</font>"+"<font size=\"1px\" color='red'>("+n+")</font>");
					}
					innerHtml.append("<span>");
					innerHtml.append("<font size=\"1px\">"+"您有"+"</font><font size=\"1px\" color=\"red\">(").append(count)
					 .append(")</font>").append("<font size=\"1px\">"+"项待办工作"+"</font>");
					innerHtml.append("&nbsp;<font size=\"1px\" color=\"red\">(").append(Integer.parseInt(message))
					 .append(")</font>").append("<font size=\"1px\">"+"封未读内部邮件"+"</font>");
					innerHtml.append("&nbsp;<a href=\"#\" onclick=\"javascript:reset()\">");
					innerHtml.append("<font size=\"1px\" color=\"red\">"+"重新选择"+"</font>");
					innerHtml.append("</a>");
					innerHtml.append("</span>");
					innerHtml.append("<table>");
					int i = 0;
					while(i < typeList.size()){
						innerHtml.append("<tr>")
								 .append("<td>")
								 .append("<img src=\"").append(rootPath)
											   .append("/oa/image/desktop/work.gif")
											   .append("\"/>")
								 .append("<a href=\"#\" onclick=\"loginOA()\">")
								 .append(typeList.get(i))
								 .append("</a>")
								 .append("</td>");
								 if((i + 1) < typeList.size()){
									 innerHtml.append("<td>&nbsp;")
									 		  .append("<img src=\"").append(rootPath)
											     	.append("/oa/image/desktop/work.gif")
											        .append("\"/>")
								              .append("<a href=\"#\" onclick=\"javascript:loginOA()\">")
								              .append(typeList.get(i+1))
								              .append("</a>");
								 }
					     i+=2;
					}
					innerHtml.append("</table>");
					logger.info(innerHtml.toString());
					return this.renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
				}
			}		
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return null;
	}
	public String process() throws Exception{
		return null;
	}
	@Override
	protected void prepareModel() throws Exception {
	}
	@Override
	public String save() throws Exception {
		try {
			if(type != null && !"".equals(type))
			{
				File file = new File("/usr/local/cookie.txt");
				FileWriter write = new FileWriter(file,true);
				BufferedWriter buffer = new BufferedWriter(write);
				buffer.write(userName+","+type);
				buffer.newLine();
				buffer.close();
				write.close(); 
				return this.renderText("true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.renderText("false");
	}

	public Object getModel() {
		return null;
	}

	@Autowired
	public void setWorkManger(IWorkListManager workManager) {
		this.workManager = workManager;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
