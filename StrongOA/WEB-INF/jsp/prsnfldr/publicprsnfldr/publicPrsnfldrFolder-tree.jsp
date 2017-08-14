<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaPublicPrsnfldrFolder"/>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<title>公共文件柜</title>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=frameroot%>/css/treeview.css" type=text/css  rel=stylesheet>
<style>
body
{
	font: normal 12px arial, tahoma, helvetica, sans-serif;
	margin:0;
	padding:5px;
	background-color:#eeeff3;
}
.simpleTree
{
	
	margin:0;
	padding:0;
	background-color:#eeeff3;
	/*
	overflow:auto;
	width: 250px;
	height:350px;
	overflow:auto;
	border: 1px solid #444444;
	*/
}


.active{
font-weight:700;

}

.simpleTree .active{
color: #000000;
}
.simpleTree li
{
	list-style: none;
	margin:0;
	padding:0 0 0 34px;
	line-height: 14px;
	background-color:#eeeff3;
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
	background-color:#eeeff3;
}
.simpleTree .root
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/root.gif) no-repeat 16px 0 #eeeff3;
}
.simpleTree .line
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/line_bg.gif) 0 0 no-repeat #eeeff3;
}
.simpleTree .line-last
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/spacer.gif) 0 0 no-repeat #eeeff3;
}
.simpleTree .line-over
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/line_bg_over.gif) 0 0 no-repeat #eeeff3;
}
.simpleTree .line-over-last
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/oa/image/mymail/line_bg_over_last.gif) 0 0 no-repeat #eeeff3;
}
.simpleTree .folder-open
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/collapsable.gif) 0 -2px no-repeat #eeeff3;
}
.simpleTree .folder-open-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/collapsable-last.gif) 0 -2px no-repeat #eeeff3;
}
.simpleTree .folder-close
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/expandable.gif) 0 -2px no-repeat #eeeff3;
}
.simpleTree .folder-close-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/expandable-last.gif) 0 -2px no-repeat #eeeff3;
}
.simpleTree .doc
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/folder-org-leaf.gif) 0 -1px no-repeat #eeeff3;
}
.simpleTree .doc-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/org-leaf-last.gif) 0 -1px no-repeat #eeeff3;
}
.simpleTree .ajax
{
	background: url(<%=path %>/oa/image/mymail/spinner.gif) no-repeat 0 0 #eeeff3;
	height: 16px;
	display:none;
}
.simpleTree .ajax li
{
	display:none;
	margin:0; 
	padding:0;
	background-color:#eeeff3;
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
	background: url(<%=path %>/oa/image/mymail/folder-org-leaf.gif) no-repeat -17px 0 #ffffff;
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
.simpleTree .root{ background-color:#eeeff3 !important;}
.simpleTree .root ul li{ background-color:#eeeff3 !important;}
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
			//$("#myMenu1").hide();
			var folderId = node.get(0).id;
			parent.project_work_content.document.location="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!publicFileList.action?folderId="+folderId;
		},
		afterDblClick:function(node){
			//alert("text-"+$('span:first',node).text());
		},
		afterMove:function(destination, source, pos){
			//alert("destination-"+destination.attr('id')+" source-"+source.attr('id')+" pos-"+pos);
		},
		afterAjax:function()
		{
			//alert('Loaded');
		},
		afterContextMenu:function(node){


			

			$($('span:first',node)).contextMenu('myMenu1', {

			  menuStyle: {

				border: '2px solid #000'

			  },

			  itemStyle: {

				fontFamily : 'verdana',

				backgroundColor : '#666',

				color: 'white',

				border: 'none',

				padding: '1px'

			  },

			  itemHoverStyle: {

				color: '#fff',

				backgroundColor: '#0f0',

				border: 'none'

			  }

			});

		$($('span:first',node)).contextMenu('myMenu1', {

		  bindings: {

			'add': function(t) {
				var folderId = node.get(0).id;
			    var ret=OpenWindow("<%=root%>/prsnfldr/publicprsnfldr/publicPrsnfldrFolder!init.action?folderId="+folderId,"400","100",window);
				if(ret!=undefined && ret!="None"){
					parent.project_work_tree.location.reload() ;
				}
			},

			'edit': function(t) {

			   var folderId = node.get(0).id;
			    var ret=OpenWindow("<%=root%>/prsnfldr/publicprsnfldr/publicPrsnfldrFolder!initEdit.action?folderId="+folderId,"400","101",window);
				if(ret!=undefined && ret!="None"){
					parent.project_work_tree.document.location.reload() ;
				}

			},

			'delete': function(t) {
				var folderName = $('span:first',node).text();	
				var id = node.get(0).id;
			 	if(confirm("删除文件夹“"+folderName+"”，确定？")){
			 		$.ajax({
			 			type:"post",
			 			url:"<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!initDelete.action",
			 			data:{folderId:id},
			 			success:function(data){
			 				if(data == "1"){
			 					if(confirm("文件夹“"+folderName+"”存有文件，删除此文件夹及所有文件，确定？")){
			 						$.ajax({
			 							type:"post",
			 							url:"<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!delete.action",
			 							data:{folderId:id},
			 							success:function(data){
			 							//	alert(data);
			 								parent.location.reload() ;
			 							},
			 							error:function(){
			 								alert("对不起,操作异常!");
			 							}
			 						});
			 					}
			 				}else{
			 					$.ajax({
			 							type:"post",
			 							url:"<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!delete.action",
			 							data:{folderId:id},
			 							success:function(data){
			 							//	alert(data);
			 								parent.location.reload() ;
			 							},
			 							error:function(){alert("对不起,操作异常!");}
			 						});
			 				}
			 			},
			 			error:function(data){
			 				alert("对不起,操作异常!");
			 			}
			 		});
			 		
			 	}
			 

			}

		  }

		});

		},
		animate:false
	});

	//新建根目录
	$("#rootSpan").contextMenu("myMenu2",{
		bindings:{
			'add':function(){
				addFolder();
			}
		}
	});
	
	//在BODY上绑定右键事件
	$("body").contextMenu("myMenu2",{
		bindings:{
			'add':function(){
				addFolder();
			}
		}
	});

	//添加文件夹
	function addFolder(){
		var ret=OpenWindow("<%=root%>/prsnfldr/publicprsnfldr/publicPrsnfldrFolder!init.action","400","100",window);
		if(ret!=undefined && ret!="None"){
			parent.document.location.reload() ;
		}
	}
});


</script>
</head>

<body class=contentbodymargin oncontextmenu="return false;">
<DIV id=contentborder align=left style="border:0px;background-color:#eeeff3;" >
<div class="contextMenu" id="myMenu1" style="width:100%">
	<ul>
		<security:authorize ifAnyGranted="001-0001000700030003">
			<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" />&nbsp;&nbsp;<SPAN class="wz">新建</SPAN> </li>
		</security:authorize>
		<security:authorize ifAnyGranted="001-0001000700030002">	
			<li id="delete"><img src="<%=path %>/oa/image/mymail/folder_delete.png" />&nbsp;&nbsp;<SPAN class="wz">删除</SPAN> </li>
		</security:authorize>	
		<security:authorize ifAnyGranted="001-0001000700030001">	
			<li id="edit"><img src="<%=path %>/oa/image/mymail/folder_edit.png" />&nbsp;&nbsp;<SPAN class="wz">重命名</SPAN> </li>
		</security:authorize>	
	</ul>
</div>
<div class="contextMenu" oncontextmenu="return false;" id="myMenu2" style="width:100%">
	<ul>
		<security:authorize ifAnyGranted="001-0001000700030003">
			<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" />&nbsp;&nbsp;<SPAN class="wz">新建</SPAN> </li>
		</security:authorize>	
	</ul>
</div>
<ul class="simpleTree">
	<li class="root" id='1'><span style="cursor: hand;" id="rootSpan">公共文件柜</span>
		<ul>
			<%
				List<ToaPublicPrsnfldrFolder> list=(List<ToaPublicPrsnfldrFolder>)request.getAttribute("folderList");
				String[] child=(String[])request.getAttribute("hasChild");
				for(int i=0;i<list.size();i++){
					ToaPublicPrsnfldrFolder folder=list.get(i);
					String charge=child[i];
					if("1".equals(charge)){//存在子节点
						String id=folder.getFolderId()+i;
						out.println("<li id="+folder.getFolderId()+">");					
						out.println("<span title=\""+folder.getFolderCreatePerson()+"\">"+folder.getFolderName()+"</span>");	
						out.println("<ul class=ajax>");
						out.println("<li id="+id +">{url:"+root+"/prsnfldr/publicprsnfldr/publicPrsnfldrFolder!syntree.action?folderId="+folder.getFolderId() +"}</li>");	
						out.println("</ul>");	
						out.println("</li>");	
					}else{
						out.println("<li id="+folder.getFolderId()+">");		
						out.println("<span title=\""+folder.getFolderCreatePerson()+"\">"+folder.getFolderName()+"");
						out.println("</span>");
						out.println("</li>");
					}
				}
			%>
		</ul>
	</li>		
</ul>
</DIV>
</body>

</html>
