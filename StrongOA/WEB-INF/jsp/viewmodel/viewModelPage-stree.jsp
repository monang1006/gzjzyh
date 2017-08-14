<%@ page language="java" import="java.util.*,java.io.File" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		<title>文件目录树</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<style>
		body
		{
			font: normal 12px arial, tahoma, helvetica, sans-serif;
			margin:0;
			padding:5px;
		}
		.simpleTree
		{
			
			margin:0;
			padding:0;
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
			margin-left:-16px;
			background: url(<%=path %>/oa/image/mymail/root.gif) no-repeat 16px 0 #ffffff;
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
			background: url(<%=path %>/oa/image/mymail/collapsable.gif) 0 -2px no-repeat #fff;
		}
		.simpleTree .folder-open-last
		{
			margin-left:-16px;
			background: url(<%=path %>/oa/image/mymail/collapsable-last.gif) 0 -2px no-repeat #fff;
		}
		.simpleTree .folder-close
		{
			margin-left:-16px;
			background: url(<%=path %>/oa/image/mymail/expandable.gif) 0 -2px no-repeat #fff;
		}
		.simpleTree .folder-close-last
		{
			margin-left:-16px;
			background: url(<%=path %>/oa/image/mymail/expandable-last.gif) 0 -2px no-repeat #fff;
		}
		.simpleTree .doc
		{
			margin-left:-16px;
			background: url(<%=path %>/oa/image/mymail/person-leaf.gif) 0 -1px no-repeat #fff;
		}
		.simpleTree .doc-last
		{
			margin-left:-16px;
			background: url(<%=path %>/oa/image/mymail/person-leaf-last.gif) 0 -1px no-repeat #fff;
		}
		.simpleTree .ajax
		{
			background: url(<%=path %>/oa/image/mymail/spinner.gif) no-repeat 0 0 #ffffff;
			height: 16px;
			display:none;
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
			background:#ffffff;
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
			background-color:#ffffff;
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
			background: url(<%=path %>/oa/image/mymail/leaf.gif) no-repeat -17px 0 #ffffff;
		}
		#drag_container .folder-close, #drag_container .folder-close-last
		{
			background: url(<%=path %>/oa/image/mymail/expandable.gif) no-repeat -17px 0 #ffffff;
		}
		
		#drag_container .folder-open, #drag_container .folder-open-last
		{
			background: url(<%=path %>/oa/image/mymail/collapsable.gif) no-repeat -17px 0 #ffffff;
		}
		.contextMenu
		{
			display:none;
		}
		.node{
			cursor: hand;
		}
		.simpleTree .folder-share-leaf-last
		{
			margin-left:-16px;
			background: url(<%=path %>/oa/image/mymail/org-leaf-last.gif) 0 -2px no-repeat #fff;
		}
		.simpleTree .folder-share-leaf
		{
			margin-left:-16px;
			background: url(<%=path %>/oa/image/mymail/folder-org-leaf.gif) 0 -1px no-repeat #fff;
		}
		</style>
		<script type="text/javascript" src="<%=path %>/oa/js/mymail/jquery.js"></script>
		<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.contextmenu.r2.js"></script>
		<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.simple.tree.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
		var simpleTreeCollection;
		$(document).ready(function(){
			simpleTreeCollection = $('.simpleTree').simpleTree({
				drag:false,
				autoclose: false,
				afterClick:function(node){
					
				},
				afterDblClick:function(node){
					window.returnValue=node.get(0).id;
					window.close();
				},
				afterMove:function(destination, source, pos){
				},
				afterAjax:function()
				{
				},
				afterContextMenu:function(node){
				},
				animate:false
			});
			
			
		});
		function getDiv(type){
			if(type == "group"){//说明当前节点是组节点
				return 'myMenu1';
			}else{
				return 'myMenu2';//当前节点是人员
			}
		}
		</script>
	</head>

	<body oncontextmenu="return false;">

	<div class="contextMenu" oncontextmenu="return false;" id="myMenu1" style="width:100%">
	
	</div>
	<div class="contextMenu" oncontextmenu="return false;" id="myMenu2" style="width:100%">
		<ul>
			<li id="sendmail"><img src="<%=root%>/images/ico/tijiao.gif" width="15" height="15" /> 发送邮件</li>
			<!--<li id="semdmsg"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 发送消息</li>
			<li id="sendphone"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 发送短信</li>-->
			<li id="prop"><img src="<%=root%>/images/ico/chakan.gif" /> 查看属性</li>
		</ul>
	</div>
	<ul class="simpleTree">
		<li class="root" id='1'><label style="cursor: hand;" id="rootSpan">系统文件目录</label>
			<ul id="systemFileTree">
				<%
				List<File> list=(List<File>)request.getAttribute("list");
				String realPath = (String)request.getAttribute("realPath");
				String[] child=(String[])request.getAttribute("charge");
				for(int i=0;i<list.size();i++){
					File file =list.get(i);
					String charge=child[i];
					String nowPath = file.getPath();
					String tempId = nowPath.substring(realPath.length()+1,nowPath.length());
					System.out.println(tempId);
					if("1".equals(charge)){//存在子节点
						out.println("<li id="+tempId +">");	
						out.println("<span>"+file.getName()+"</span>");	
						out.println("<label style='display:none;'>group</label>");	
						out.println("<ul class=ajax>");
						out.println("<li class=\"person\" id="+tempId +1 +">{url:"+root+"/viewmodel/viewModelPage!getRootChild.action?nowNode="+file.getPath()+"}</li>");	
						out.println("</ul>");	
						out.println("</li>");	
					}else{
						out.println("<li id="+tempId+" leafChange='folder-share-leaf'>");		
						out.println("<span>"+file.getName()+"</span>");
						out.println("<label style='display:none;'>group</label>");	
						out.println("</li>");
					}
				}	
			%>		
			</ul>
		</li>		
	</ul>
</body>

</html>
