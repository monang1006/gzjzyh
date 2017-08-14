package com.strongit.tag.web.tree;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.strongit.tag.web.util.ResponseUtils;
/**
 * <p>Title: TreeTag.java</p>
 * <p>Description: 通用树形结构标签</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2009-10-28 16:35:16
 * @version  1.1(添加属性:rootcheck)
 */

public class TreeTag extends TagSupport{

	private static final long serialVersionUID = 6982808609998526604L;
	
	private String title;					//树形结构根节点名称
	private boolean showTitle=true;			//是否显示提示内容
	private String dealclass;
	private String target;					//触发链接显示框架
	private String iconpath;				//图片路径
	private boolean check;					//是否出现复选框
	private boolean titlecheck = false;		//标题是否可选
	private List data;						//前台传入数据
	private String roothref;				//设置根图片路径
	private String chooseType;				//复选框类型
	private String hascheckedvalues = null;
	IDealTreeNode dealNode;
	private boolean rootcheck=true;			//是否在父子级联不选中
	
	public void setCheck(boolean check) {
		this.check = check;
	}

	
	public void setTitlecheck(boolean titlecheck) {
		this.titlecheck = titlecheck;
	}


	public void setData(List data) {
		this.data = data;
	}

	public void setDealclass(String dealclass) {
		this.dealclass = dealclass;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setChooseType(String chooseType) {
		this.chooseType = chooseType;
	}

	public int  doStartTag(){
		Class cls = null;
		try {
			dealNode=(IDealTreeNode)cls.forName(dealclass).newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag()throws JspException{
		StringBuffer script=new StringBuffer();
		script=scriptBeforeNode();
		for(int i=0;i<data.size();i++){
			Object obj=data.get(i);
			dealNode.setObject(obj);
			if(dealNode.getTagParentId()==null||"".equals(dealNode.getTagParentId())||"0".equals(dealNode.getTagParentId())){
				script.append("tree.nodes[\"C0"+"_"+dealNode.getTagNodeId()+"\"] = 'text:"+dealNode.getTagNodeName()+";valueId:"+dealNode.getTagNodeId()+";");
			}else{
				script.append("tree.nodes[\""+dealNode.getTagParentId()+"_"+dealNode.getTagNodeId()+"\"] = 'text:"+dealNode.getTagNodeName()+";valueId:"+dealNode.getTagNodeId()+";");
			}
			if(check==false){
				script.append("isChecked:no;");
			}else if(hascheckedvalues!=null&&hascheckedvalues.length()>0){
				String strarr[] = hascheckedvalues.split(",");
				for(int k=0;k<strarr.length;k++){
					if(dealNode.getTagNodeId().equals(strarr[k])){
						script.append("isChecked:true;");
						break;
					}
				}
			}
			if("signle".equals(chooseType)){
				script.append("chooseType:signle;");
			}else if("chooseOne".equals(chooseType)){
				script.append("chooseType:chooseOne;");
			}
			if(showTitle==true){
				script.append("showTitle:true;");
			}else{
				script.append("showTitle:false;");
			}
			if(target!=null&&!"".equals(target)){
				script.append("target:"+target+";");
			}
			if(dealNode.getUrl()!=null&&!"".equals(dealNode.getUrl())){
				if(dealNode.getUrl().indexOf("javascript:")!=-1){
					script.append("url:"+dealNode.getUrl()+";");
				}else{
					script.append("url:"+getRootPath()+"/"+dealNode.getUrl()+";");
				}
			}
			if(dealNode.getClick()!=null&&!"".equals(dealNode.getClick())){
				script.append("onclick:"+dealNode.getClick()+";");
			}
			if(dealNode.checkAble()==true){
				script.append("checkAble:true;");
			}else{
				script.append("checkAble:false;");
			}
			if(dealNode.getNodeImg()!=null&&!"".equals(dealNode.getNodeImg())){
				HttpSession session=pageContext.getSession();
				if(session.getAttribute("frameroot")==null){
					script.append("icon:"+getRootPath()+"/frame/theme_blue/images/"+dealNode.getNodeImg()+";");
				}else{
					script.append("icon:"+getRootPath()+session.getAttribute("frameroot")+"/images"+dealNode.getNodeImg()+";");
				}
			}
			script.append("'\n");
			dealNode.setObject(null);
		}
		ResponseUtils.write(pageContext, script.toString()+scriptBehindNode());
		return EVAL_PAGE;
		
	}
	
	public void release(){
		
		super.release();	
		
	}
	
	public StringBuffer scriptBeforeNode(){
		StringBuffer str=new StringBuffer();
		str.append("<TABLE cellSpacing=0 cellPadding=2 width=\"100%\" border=0>\n")
		   .append("<TBODY>\n")
		   //.append("<TR>\n")
		   //.append("<TD width=5></TD>")
		   //.append("<TD></TD></TR>")
		   .append("<TR>")
		   .append("<TD width=5></TD>")
		   .append("<TD>");
		str.append("<DIV class=dtree id=treeviewarea></DIV>\n")
		   .append("</TD>")
		   .append("</TR>")
		   .append("</TBODY>")
		   .append("</TABLE>")
		   .append("<SCRIPT language=javascript charset=utf-8>\n")
		   .append("window.tree = new MzTreeView(\"tree\");\n");
		if(iconpath!=null&&!"".equals(iconpath)){
			str.append("tree.setIconPath(\""+getRootPath()+"/"+iconpath+"\");\n");
		}else{
			HttpSession session=pageContext.getSession();
			if(session.getAttribute("frameroot")==null){
				str.append("tree.setIconPath(\""+getRootPath()+"/frame/theme_blue/images/\");\n");
			}else{
				str.append("tree.setIconPath(\""+getRootPath()+session.getAttribute("frameroot")+"/images/\");\n");
			}
		}
		str.append("tree.icons[\"property\"] = \"property.gif\";\n")
		   .append("tree.icons[\"css\"] = \"collection.gif\";\n")
		   .append("tree.icons[\"book\"]  = \"book.gif\";\n")
		   .append("tree.iconsExpand[\"book\"] = \"bookopen.gif\";\n")
		   .append("tree.nodes[\"0_C0\"] = 'text:"+title+";");
		if(check==false||this.titlecheck==false){
			str.append("isChecked:no;");
		}
		if("signle".equals(chooseType)){
			str.append("chooseType:signle;");
		}else if("chooseOne".equals(chooseType)){
			str.append("chooseType:chooseOne;");
		}
		if(this.roothref!=null&&!"".equals(roothref)){
			str.append("url:"+getRootPath()+"/"+roothref+";");
			if(target!=null&&!"".equals(target)){
				str.append("target:"+target+";");
			}
		}
		str.append("';\n");
		return str;
	}
	
	public String scriptBehindNode(){
		StringBuffer str=new StringBuffer();
		str.append("document.getElementById('treeviewarea').innerHTML = tree.toString();\n");
		if(check==true){
			if("signle".equals(chooseType)){
				//单选树结构
			}else if("chooseOne".equals(chooseType)){
				//多选树结构（选父不选子）
			}else{
				str.append("tree.expandAll();\n")
				   .append("function checkNodeClick(checked,value,id){\n")
				   .append("for(var i=0;i<chkTreeNode.length;i++){\n")
				   .append("if(chkTreeNode[i].value==value){\n")
				   .append("chkTreeNode[i].checked=checked;\n")
				   .append("checkid=chkTreeNode[i].id.substring(4);\n")
				   .append("var checknode=tree.node[checkid];\n")
				   .append("var parentid=checknode.parentId;\n")
				   .append("if(checknode.hasChild){\n")
				   .append("for(var j=0;j<checknode.childNodes.length;j++){\n")
				   .append("var childnode=checknode.childNodes[j];\n")
				   .append("checkNodeClick(checked,childnode.valueId,\"chk_\"+childnode.id);\n")
				   .append("}\n")
				   .append("}\n");
				if(rootcheck){
				   str.append("if(checked==false){\n")
					  .append("var parentnode=tree.node[parentid];\n")
					  .append("for(var i=0;i<chkTreeNode.length;i++){\n")
					  .append("if(chkTreeNode[i].value==parentnode.valueId){\n")
					  .append("chkTreeNode[i].checked=false;")
					  .append("}\n")
					  .append("}\n")
					  .append("}\n");
				}
				str.append("}\n")
				   .append("}\n")
				   .append("}\n");
			}
		}
		str.append("</SCRIPT>");
		return str.toString();
		
	}
	
	public String getRootPath(){
		HttpServletRequest httpServletRequest = (HttpServletRequest)pageContext.getRequest();
		String rooturl=httpServletRequest.getContextPath();
		return rooturl;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	public void setIconpath(String iconpath) {
		this.iconpath = iconpath;
	}

	public void setRoothref(String roothref) {
		this.roothref = roothref;
	}


	public void setHascheckedvalues(String hascheckedvalues) {
		this.hascheckedvalues = hascheckedvalues;
	}


	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}


	public void setRootcheck(boolean rootcheck) {
		this.rootcheck = rootcheck;
	}

}
