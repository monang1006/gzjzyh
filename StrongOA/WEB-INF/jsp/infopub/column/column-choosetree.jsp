<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaInfopublishColumn"/>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<%@include file="/common/include/meta.jsp"%>
<title>栏目管理</title>


<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css" >
<SCRIPT type="text/javascript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
<script type="text/javascript" src="<%=root%>/oa/js/mymail/jquery.js"></script>
<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.contextmenu.r2.js"></script>
<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.simple.tree.js"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
<style>
body
{
	font: normal 12px arial, tahoma, helvetica, sans-serif;
	margin:0;
	padding:5px 0;
}
.simpleTree
{
	
	margin:0;
	padding:5px;
	/*
	overflow:auto;
	width: 250px;
	height:350px;
	overflow:auto;
	border: 1px solid #444444;
	*/
}
.simpleTree li
{
	list-style: none;
	margin:0;
	padding:0 0 0 34px;
	line-height: 14px;
}
.simpleTree li span
{
	display:inline;
	clear: left;
	white-space: nowrap;
}
.simpleTree ul
{
	margin:0; 
	padding:0;
}
.simpleTree .root
{  
   font-size:14px;
   font-family:"宋体";
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/root.gif) no-repeat 16px 0  ;
}
.simpleTree .line
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/line_bg.gif) 0 0 no-repeat transparent;
}
.simpleTree .line-last
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/spacer.gif) 0 0 no-repeat transparent;
}
.simpleTree .line-over
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/line_bg_over.gif) 0 0 no-repeat transparent;
}
.simpleTree .line-over-last
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/line_bg_over_last.gif) 0 0 no-repeat transparent;
}
.simpleTree .folder-open
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/collapsable.gif) 0 -2px no-repeat  ;
}
.simpleTree .folder-open-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/collapsable-last.gif) 0 -2px no-repeat  ;
}
.simpleTree .folder-close
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/expandable.gif) 0 -2px no-repeat  ;
}
.simpleTree .folder-close-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/expandable-last.gif) 0 -2px no-repeat  ;
}
.simpleTree .doc
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/leaf.gif) 0 -1px no-repeat  ;
}
.simpleTree .doc-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/leaf-last.gif) 0 -1px no-repeat  ;
}
.simpleTree .ajax
{
	background: url(<%=path %>/oa/image/mymail/spinner.gif) no-repeat 0 0  ;
	height: 16px;
	display:none;
}
/*查看或者添加页面上的顶部按钮样式——强调*/
.Operation_input{
	background:url(<%=frameroot%>/images/ch_h_m.gif) repeat-x;
	font-weight: bold;
	color:white;
	cursor: pointer;
}
/*查看或者添加页面上的顶部按钮样式——非强调*/
.Operation_input1{
	background:url(<%=frameroot%>/images/ch_z_m.gif) repeat-x;
	font-weight: bold;
	cursor: pointer;
	color:#454953;
}
.simpleTree .ajax li
{
	display:none;
	margin:0; 
	padding:0;
}
.simpleTree .trigger
{
	display:inline;
	margin-left:-32px;
	width: 28px;
	height: 11px;
	cursor:pointer;
}
.simpleTree .text
{
	cursor: default;
}
.simpleTree .active
{
	//cursor:hand;
	background-color:#F7BE77;

}
#drag_container
{
	background: ;
	color:#000;
	font: normal 11px arial, tahoma, helvetica, sans-serif;
	border: 1px dashed #767676;
}
#drag_container ul
{
	list-style: none;
	padding:0;
	margin:0;
}

#drag_container li
{
	list-style: none;
	background-color: ;
	line-height:18px;
	white-space: nowrap;
	padding:1px 1px 0px 16px;
	margin:0;
}
#drag_container li span
{
	padding:0;
}
#drag_container li.doc, #drag_container li.doc-last
{
	background: url(<%=path %>/oa/image/mymail/leaf.gif) no-repeat -17px 0  ;
}
#drag_container .folder-close, #drag_container .folder-close-last
{
	background: url(<%=path %>/oa/image/mymail/expandable.gif) no-repeat -17px 0  ;
}

#drag_container .folder-open, #drag_container .folder-open-last
{
	background: url(<%=path %>/oa/image/mymail/collapsable.gif) no-repeat -17px 0  ;
}
#contentborder {
	OVERFLOW: auto; HEIGHT: 100%; BACKGROUND: url(<%=frameroot%>/images/blueprint.png) #fff; POSITION: absolute; WIDTH: 100%
}
.contextMenu
{
	display:none;
}
.node{
	cursor: hand;
}

.simpleTree .folder-share-close
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/shareexpandable.gif) 0 -2px no-repeat  ;
}
.simpleTree .folder-share-close-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/shareexpandable-last.gif) 0 -2px no-repeat  ;
}
.simpleTree .folder-share-open
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/share-collapsable.gif) 0 -2px no-repeat  ;
}

.simpleTree .folder-share-open-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/share-collapsable-last.gif) 0 -2px no-repeat  ;
}
.simpleTree .folder-share-leaf-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/share-leaf-last.gif) 0 -2px no-repeat  ;
}
.simpleTree .folder-share-leaf
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/folder-share-leaf.gif) 0 -1px no-repeat  ;
}
</style>

<script type="text/javascript">
var simpleTreeCollection;
var folderId;
$(document).ready(function(){
	simpleTreeCollection = $('.simpleTree').simpleTree({
		drag:false,
		autoclose: false,
		afterClick:function(node){
			folderId = node.get(0).id;
			//parent.project_work_content.document.location="<%=root%>/infopub/column/column!input.action?clumnId="+folderId;
		},
		afterDblClick:function(node){
		},
		afterMove:function(destination, source, pos){
		},
		afterAjax:function()
		{
		},
		afterContextMenu:function(node){

			$($('span:first',node)).contextMenu('myMenu1', {

			  menuStyle: {

				border: '2px solid #000'

			  },

			  itemStyle: {

				fontFamily : 'verdana',

			

				color: 'white',

				border: 'none',

				padding: '1px'

			  },

			  itemHoverStyle: {

				color: '#fff',


				border: 'none'

			  }

			});

		$($('span:first',node)).contextMenu('myMenu1', {

		  bindings: {
			
		  }

		});


		},
		animate:false
	});


});

function getid(){
	if(folderId!=null && folderId!=''){
	var folderName;
	   folderName= $("#"+folderId).attr("name"); 
		window.returnValue=folderId+","+folderName;
		window.close();
	}else{
		alert("请选择一个栏目再提交");
	}
}
function closewin(){
	window.returnValue=null;
	window.close();
}


</script>
</head>

<body class=contentbodymargin oncontextmenu="return false;">
<div id="contentborder">
             <table width="100%" border="0" cellspacing="0" cellpadding="00">
               <tr>
                 <td class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>栏目管理</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onClick="getid();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onClick="closewin();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
						
					</table>
                 </td>
                 </tr>
                </table>    
	<ul class="simpleTree">
	<li class="root" id='1'><span>栏目管理</span>
		<ul>
			<%
				List<ToaInfopublishColumn> list=(List<ToaInfopublishColumn>)request.getAttribute("columnList");
				String[] child=(String[])request.getAttribute("hasChild");
				for(int i=0;i<list.size();i++){
					ToaInfopublishColumn folder=list.get(i);
					String charge=child[i];
					if("1".equals(charge)){//存在子节点
						String id=folder.getClumnId()+i;
						out.println("<li name="+folder.getClumnName()+" id="+folder.getClumnId() +">");
						out.println("<span>"+folder.getClumnName()+"</span>");	
						out.println("<ul class=ajax>");
						out.println("<li  name="+folder.getClumnName()+"  id="+id +">{url:"+root+"/infopub/column/column!syntree.action?clumnId="+folder.getClumnId()+"}</li>");	
						out.println("</ul>");	
						out.println("</li>");	
					}else{
						out.println("<li  name="+folder.getClumnName()+"  id="+folder.getClumnId() +">");
						out.println("<span>"+folder.getClumnName());
						out.println("</span>");
						out.println("</li>");
					}
				}	
			%>		
		</ul>
	</li>		
</ul>
<table id="annex" width="90%" height="10%" border="0"
	cellpadding="0" cellspacing="1" align="center" class="table1">
</table>

<%--<table width="90%" border="0" cellspacing="0" cellpadding="00">--%>
<%--	<tr>--%>
<%--		<td align="center" valign="middle">--%>
<%--			<table width="50%" border="0" cellspacing="0" cellpadding="00">--%>
<%--				<tr>--%>
<%--					<td width="33%">--%>
<%--						<input id="sb" type="button" class="input_bg" value="确  定" onclick="getid();">--%>
<%--					</td>--%>
<%--					<td width="33%">--%>
<%--						&nbsp;&nbsp;&nbsp;--%>
<%--						<input name="Submit2" type="button" class="input_bg" value="关 闭" onclick="closewin();">--%>
<%--					</td>--%>
<%--				</tr>--%>
<%--			</table>--%>
<%--		</td>--%>
<%--	</tr>--%>
<%--</table>--%>
</div>
</body>

</html>
