package com.strongit.oa.wap;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.attachment.AttachmentHelper;
import com.strongit.oa.attachment.IWorkflowAttachService;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.wap.util.Data;
import com.strongit.oa.wap.util.Item;
import com.strongit.oa.wap.util.Row;
import com.strongit.oa.wap.util.Status;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 处理手机OA的Action.提供Http请求返回数据的调用方式.
 * 返回数据格式为Json格式.
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Nov 23, 2011
 * @classpath	com.strongit.oa.mobileoa.MobileAction
 * @version  	5.0
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
@ParentPackage("default")
public class MobileAction extends BaseActionSupport {

	/**
	 * 
	 */
	
	@Autowired
	 IWorkflowAttachService workflowAttachManager;
	
	@Autowired
	 IUserService userService;//注入统一用户服务
	
	@Autowired
	 IWorkflowService workflow; // 工作流服务
	
	/**
	 * 手机短信提醒服务
	 */

	private String taskId;
	private String attachId;
	
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
	public String list() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查看单个附件
	 * @return
	 */
	public String viewAttach() {
		HttpServletRequest request = getRequest();
		HttpServletResponse response=getResponse();
		OutputStream outputStream=null;
		BufferedInputStream br=null;
		InputStream is =null;
		String rtn="";
		try {
			String root = request.getContextPath();
			//String data = workflowService.getForm(taskId, root);
			String strNodeInfo = workflow.getNodeInfo(taskId);
			String[] arrNodeInfo = strNodeInfo.split(",");
			
			String formId =arrNodeInfo[3];
			String bussinessId = arrNodeInfo[2];
			String[] args = bussinessId.split(";");
			String tableName = args[0];
			String pkFieldName = args[1];
			
			
			String pkFieldValue = args[2];
			List<WorkflowAttach> workflowAttachs = workflowAttachManager.getWorkflowAttachsByDocId(pkFieldValue);
			if(workflowAttachs != null && !workflowAttachs.isEmpty()) {
				for(WorkflowAttach attach : workflowAttachs) {
					if(attachId.equals(attach.getDocattachid())){
						is = FileUtil.ByteArray2InputStream(attach.getAttachContent());
						String fileName=attach.getAttachName();;
						String fileType=fileName.substring(fileName.indexOf(".")+1,fileName.length());
						
						response.reset();
						response.setCharacterEncoding("utf-8");
						response.setContentType("application/x-msdownload");
						response.setHeader("Content-Type",fileType);
						response.addHeader("Content-Disposition", "attachment;filename=" +
							    new String(fileName.getBytes("gb2312"),"iso8859-1"));
						outputStream = new BufferedOutputStream(response.getOutputStream());
						br = new BufferedInputStream(is);
						byte[] buf = new byte[1024];
						int len = 0;
						while((len = br.read(buf)) >0){
							outputStream.write(buf,0,len);
						}
					}
				}
			}
			/*if(!"".equals(rtn)){
				rtn=rtn.substring(0,rtn.length()-1);
			}*/
			//request.setAttribute("result", data);
		} catch (Exception ex) {
			logger.error("读取附件数据发生异常", ex);
			String msg = ex.getMessage();
		}finally{
			try {
				if(outputStream!=null){
					outputStream.close();
				}
				if(br!=null){
					br.close();
				}
				if(is!=null){
					is.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取附件，并返回临时附件文件的访问路径
	 * @return
	 */
	public String viewForm() {
		HttpServletRequest request = getRequest();
		HttpServletResponse response=getResponse();
		OutputStream outputStream=null;
		BufferedInputStream br=null;
		InputStream is =null;
		String rtn="";
		try {
			String root = request.getContextPath();
			//String data = workflowService.getForm(taskId, root);
			String strNodeInfo = workflow.getNodeInfo(taskId);
			String[] arrNodeInfo = strNodeInfo.split(",");
			
			String bussinessId = arrNodeInfo[2];
			String[] args = bussinessId.split(";");
			String pkFieldValue = args[2];
			
			List<WorkflowAttach> workflowAttachs = workflowAttachManager.getWorkflowAttachsByDocId(pkFieldValue);
			if(workflowAttachs != null && !workflowAttachs.isEmpty()&&workflowAttachs.size()>0) {
				for(WorkflowAttach attach : workflowAttachs) {
					if(attach.getAttachContent()!=null){
						is = FileUtil.ByteArray2InputStream(attach.getAttachContent());
						String fileName=attach.getAttachName();;
						String fileType=fileName.substring(fileName.indexOf(".")+1,fileName.length());
						String path = AttachmentHelper.saveFile(root, is, fileName);
						rtn=rtn+attach.getDocattachid()+","+fileName+","+path+";";
					}
				}
			}
			if(!"".equals(rtn)){
				rtn=rtn.substring(0,rtn.length()-1);
			}
			//request.setAttribute("result", data);
		} catch (Exception ex) {
			logger.error("读取附件数据发生异常", ex);
			String msg = ex.getMessage();
		}finally{
			try {
				if(outputStream!=null){
					outputStream.close();
				}
				if(br!=null){
					br.close();
				}
				if(is!=null){
					is.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ActionContext.getContext().put("androidsearch", rtn);
		return "android";
	}
	
	/**
	 * 获取附件，并返回临时附件文件的访问路径
	 * @return
	 */
	public String androidViewAttach() {
		HttpServletRequest request = getRequest();
		HttpServletResponse response=getResponse();
		OutputStream outputStream=null;
		BufferedInputStream br=null;
		InputStream is =null;
		String rtn="";
		Data data=new Data();
		try {
			String root = request.getContextPath();
			//String data = workflowService.getForm(taskId, root);
			String strNodeInfo = workflow.getNodeInfo(taskId);
			String[] arrNodeInfo = strNodeInfo.split(",");
			
			String bussinessId = arrNodeInfo[2];
			String[] args = bussinessId.split(";");
			String pkFieldValue = args[2];
			List<Row> rows=new ArrayList<Row>();
			List<WorkflowAttach> workflowAttachs = workflowAttachManager.getWorkflowAttachsByDocId(pkFieldValue);
			if(workflowAttachs != null && !workflowAttachs.isEmpty()&&workflowAttachs.size()>0) {
				for(WorkflowAttach attach : workflowAttachs) {
					if(attach.getAttachContent()!=null){
						is = FileUtil.ByteArray2InputStream(attach.getAttachContent());
						String fileName=attach.getAttachName();;
						String fileType=fileName.substring(fileName.indexOf(".")+1,fileName.length());
						String path = AttachmentHelper.saveFile(root, is, fileName);
						
						Row row=new Row();
						List<Item> items = new ArrayList<Item>();
						Item item=new Item();
						item.setType("attachId");
						item.setValue(attach.getDocattachid());
						items.add(item);
						
						item=new Item();
						item.setType("filename");
						item.setValue(fileName);
						items.add(item);
						
						item=new Item();
						item.setType("filepath");
						item.setValue(path);
						items.add(item);
						
						row.setItems(items);
						rows.add(row);
						//rtn=rtn+attach.getDocattachid()+","+fileName+","+path+";";
					}
				}
			}
			data.setRows(rows);
			Status status=new Status("0","success");
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
			
		}catch(Exception e){
			logger.error("读取附件数据发生异常", e);
			String msg = e.getMessage();
			String err=e.getMessage();
			Status status=new Status("1",err);
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
		}finally{
			try {
				if(outputStream!=null){
					outputStream.close();
				}
				if(br!=null){
					br.close();
				}
				if(is!=null){
					is.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*	if(!"".equals(rtn)){
				rtn=rtn.substring(0,rtn.length()-1);
			}
			//request.setAttribute("result", data);
			
		} catch (Exception ex) {
			logger.error("读取附件数据发生异常", ex);
			String msg = ex.getMessage();
		}
		ActionContext.getContext().put("androidsearch", rtn);*/
		return "androidview";
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
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}
}
