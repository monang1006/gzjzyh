
package com.strongit.oa.docafterflow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.ToaDocafterflow;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.docafterflow.util.FlowNode;
import com.strongit.oa.docafterflow.util.Node;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * <p>Title: DocafterflowAction.java</p>
 * <p>Description: </p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @des		 对应公文签收列表Action
 * @date 	 2009-11-12 16:09:02
 * @version  1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/docafterflow.action") })
public class DocafterflowAction extends BaseActionSupport{

	private static final long serialVersionUID = -2758029221586601895L;
	
	private Page<ToaDocafterflow> page = new Page<ToaDocafterflow>(FlexTableTag.MAX_ROWS,true);
	
	private DocafterflowManager afterflowManager;
	
	private String blockid;							//个人桌面传入
	
	@Autowired private DesktopSectionManager desktopSectionManager;
	
	@Autowired private IWorkflowService workflowService;
	
	private String type;
	
	private String title;
	
	private Date startDate;

	private Date endDate;
	
	private String mydocId;						//文档Id
	
	private String sendDocId;					//已经关联文档ID;
	
	private String workFlowName;
	
	
	


	@Autowired
	public void setAfterflowManager(DocafterflowManager afterflowManager) {
		this.afterflowManager = afterflowManager;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {//phj:获取已经签收的公文
		// TODO Auto-generated method stub
		page = afterflowManager.getNotGetPageList(page, title, startDate, endDate);
		return SUCCESS;
	}
	
	
	
	public String showGetList() throws Exception {//phj:获取已经签收的公文
		// TODO Auto-generated method stub
		page = afterflowManager.getGetPageList(page, title, startDate, endDate);
		return "showGetList";
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-12 20:35:41
	 * @des 	文档查看入口    
	 * @return  String
	 */
	public String view() throws Exception{
		ToaDocafterflow doc = afterflowManager.getObjById(mydocId);
		if(doc!=null){
			sendDocId = doc.getToaDocDis().getSenddocId();
		}
		return "view";
	}
	
	public String chooseWorkFlow() throws Exception{
		List workList = workflowService.getStartWorkflow("3");
		List flowNodeList = new ArrayList<FlowNode>();
		for(int i=0;i<workList.size();i++){
			Node node = new Node();
			node.setId(String.valueOf(i));
			Object[] obj = (Object[]) workList.get(i);
			node.setName(obj[0].toString());
			flowNodeList.add(node);
		}
		getRequest().setAttribute("list", flowNodeList);
		return "chooseflow";
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-12 22:13:37
	 * @des     前台确定签收Ajax入口  true:签收成功过  false:签收失败  refresh:本页面刷新
	 * @return  String
	 */
	public String sureGet() throws Exception{
		System.out.println(this.workFlowName);
		if(afterflowManager.getDocToDraft(mydocId,workFlowName)){
			if("no".equals(type)){						   //个人桌面入口则自我刷新
				return renderText("refresh");
			}else{
				return renderText("true");
			}
		}else{
			return renderText("false");
		}
	}
	
	public String sureGetCheck() throws Exception{
 		if(afterflowManager.getDocCheck(mydocId)){
			if("no".equals(type)){						   //个人桌面入口则自我刷新
				return renderText("refresh");
			}else{
				return renderText("true");
			}
		}else{
			return renderText("false");
		}
	}
	
	public String showDesktop() throws Exception{
		StringBuffer innerHtml = new StringBuffer();								//页面HTML语句对象
		Map<String,String> map = desktopSectionManager.getParam(blockid);
		String showNum = map.get("showNum");										//显示条数
		String subLength = map.get("subLength");									//主题长度
		String showCreator = map.get("showCreator");								//是否显示作者
		String showDate = map.get("showDate");										//是否显示日期
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		page.setPageSize(num);
		List<ToaDocafterflow> list = afterflowManager.getOrgNotReadDoc(page);
		if(list!=null){
			for(int i=0;i<list.size();i++){
				ToaDocafterflow temp = list.get(i);
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<div class=\"linkdiv\" title=\"\">");
				innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/mymail/read.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				String title = temp.getToaDocDis().getSenddocTitle();
				title=title==null?"无标题":title;
				if(title.length()>length)								//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<span title=\"").append(title).append("\">")
						 //.append("	<a href=\"#\" onclick=\"javascript:window.parent.refreshWorkByTitle('"+getRequest().getContextPath()+"/recvdoc/recvDoc.action?tableName=T_OARECVDOC','来文登记')\"> ").append(title).append("</a></span>");
						 .append("	<a href=\"#\" onclick=\"javascript:window.parent.refreshWorkByTitle('"+getRequest().getContextPath()+"/docafterflow/docafterflow.action?type=no','公文签收')\"> ").append(title).append("</a></span>");
				if("1".equals(showCreator)){
					innerHtml.append("	<span class =\"linkgray\">").append(temp.getToaDocDis().getSenddocUser()).append("</span>");
				}
				
				if("1".equals(showDate)){								//如果设置为显示日期，则显示日期信息
					String dateStr="";
					if(temp.getGetDocDate()==null){
						
					}else{
						dateStr=st.format(temp.getGetDocDate());
					}
					innerHtml.append("	<span class =\"linkgray10\">").append(dateStr).append("</span>");
				}
				innerHtml.append("	</div>");;
			}
			innerHtml.append("<div align=\"center\" class=\"linkdiv\" title=\"\">");
			innerHtml.append("待签收文件(<font color=\"red\">");
			innerHtml.append(page.getTotalCount());
			innerHtml.append("</font>)");
			innerHtml.append("</div>");
		}else{
			innerHtml.append("<div align=\"center\" class=\"linkdiv\" title=\"\">");
			innerHtml.append("待签收文件(<font color=\"red\">");
			innerHtml.append(page.getTotalCount());
			innerHtml.append("</font>)");
			innerHtml.append("</div>");
		}
		return renderHtml(innerHtml.toString());
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<ToaDocafterflow> getPage() {
		return page;
	}

	public void setPage(Page<ToaDocafterflow> page) {
		this.page = page;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMydocId() {
		return mydocId;
	}

	public void setMydocId(String mydocId) {
		this.mydocId = mydocId;
	}
	
	public String getSendDocId() {
		return sendDocId;
	}

	public void setSendDocId(String sendDocId) {
		this.sendDocId = sendDocId;
	}

	public String getBlockid() {
		return blockid;
	}

	public void setBlockid(String blockid) {
		this.blockid = blockid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}
}
