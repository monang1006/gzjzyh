package com.strongit.tag.web.tree;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.strongit.tag.web.util.ResponseUtils;

public class BigTreeTag  extends TagSupport{

	private static final long serialVersionUID = 5571531129086771724L;
	
	private String dealclass;						//对应树形节点辅助类
	
	IDealTreeNode dealNode;
	
	private boolean check;							//判断当前是否采用复选框 
	
	private List data;								//前台传入数据
	
	private String oncheckboxclick;
	
	private String title;
	
	private boolean parentCascadecheck = true;     //节点选中时向父节点回溯 added by dengzc 2011年1月13日18:21:13
	
	private boolean cascadecheck	= true;			//节点选中时级联选中父节点和子节点 added by dengzc 2011年1月13日18:21:13
	
	public void setDealclass(String dealclass) {
		this.dealclass = dealclass;
	}

	public void setDealNode(IDealTreeNode dealNode) {
		this.dealNode = dealNode;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public void setData(List data) {
		this.data = data;
	}

	public void setOncheckboxclick(String oncheckboxclick) {
		this.oncheckboxclick = oncheckboxclick;
	}
	
	public int  doStartTag(){
		try {
			dealNode=(IDealTreeNode)Class.forName(dealclass).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag()throws JspException{
		StringBuffer script=new StringBuffer();
		script=scriptBeforeNode();
		script.append("<script type=\"text/javascript\">");
		script.append("		var demoData = [");
		//{"id":"1","text":"节点1","value":"1","showcheck":true,"complete":true,"isexpand":false,"checkstate":"null","hasChildren":true,"pid":"-1","ChildNodes":[]}
		for(int i=0;i<data.size();i++){
			Object obj=data.get(i);
			dealNode.setObject(obj);
			script.append("{'id':'"+dealNode.getTagNodeId()+"','checkAble':'"+dealNode.checkAble()+"','text':'"+dealNode.getTagNodeName()+"','value':'"+dealNode.getTagNodeId()+"',");
			//增加附加属性 added by dengzc 2011年1月13日18:11:22
			script.append("'url':'").append(dealNode.getUrl()).append("',"); 
			script.append("'click':'").append(dealNode.getClick()).append("',"); 
			script.append("'nodeImg':'").append(dealNode.getNodeImg()).append("',");
			//--END----
			if(check==true){
				script.append("'showcheck':true,");
			}else{
				script.append("'showcheck':false,");
			}
			if(dealNode.getTagParentId()==null||"".equals(dealNode.getTagParentId())||"0".equals(dealNode.getTagParentId())){
				script.append("'complete':true,'isexpand':true,'checkstate':'0','hasChildren':false,'pid':'-1','ChildNodes':[]}");
			}else{
				script.append("'complete':true,'isexpand':false,'checkstate':'0','hasChildren':false,'pid':'"+dealNode.getTagParentId()+"','ChildNodes':[]}");
			}
			if(i<data.size()-1){
				script.append(",");
			}
		}
		script.append("];");
		
		script.append("</script>");
		script.append("<script type=\"text/javascript\">");
		script.append("$(document).ready(function(){");
		script.append("		var o = {");
		script.append("			cbiconpath: '"+getRootPath()+"/common/images/tree/',");
		if(title!=null){
			script.append("			title:'"+title+"',");
		}
		if(check==true){
			script.append("			showcheckbox : true,");
			script.append("         showcheck : true,");
			//script.append("         cascadecheck : true,");

		}else{
			script.append("			showcheckbox : false,");
			script.append("         showcheck : false,");
			//script.append("         cascadecheck : false,");
		}
		//增加选择子节点是否选中父节点的控制
		script.append("				parentCascadecheck:").append(this.isParentCascadecheck()).append(","); 
		script.append("				cascadecheck:").append(this.isCascadecheck()).append(","); 
		//--------END
		if(oncheckboxclick==null||"false".equals(oncheckboxclick)){
			script.append("         oncheckboxclick : false,");
		}else{
			script.append("         oncheckboxclick : "+oncheckboxclick+",");
		}
		script.append("			theme : \"bbit-tree-lines\",");
		script.append("			data: demoData");
		script.append("};");
		script.append(" init(o);");
		script.append("$(\"#bigTreeDiv\").treeview(o);");
		script.append("});");
		script.append("</script>");
		System.out.println(script.toString());
		ResponseUtils.write(pageContext, script.toString());
		return EVAL_PAGE;
	}
	
	public StringBuffer scriptBeforeNode(){
		StringBuffer beforeNode=new StringBuffer();
		beforeNode.append("<div id=\"bigTreeDiv\"></div>");
		return beforeNode;
	}
	
	public String getRootPath(){
		HttpServletRequest httpServletRequest = (HttpServletRequest)pageContext.getRequest();
		String rooturl=httpServletRequest.getContextPath();
		return rooturl;
	}
	
	public void release(){
		
		super.release();	
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isParentCascadecheck() {
		return parentCascadecheck;
	}

	public void setParentCascadecheck(boolean parentCascadecheck) {
		this.parentCascadecheck = parentCascadecheck;
	}

	public boolean isCascadecheck() {
		return cascadecheck;
	}

	public void setCascadecheck(boolean cascadecheck) {
		this.cascadecheck = cascadecheck;
	}
	
}
