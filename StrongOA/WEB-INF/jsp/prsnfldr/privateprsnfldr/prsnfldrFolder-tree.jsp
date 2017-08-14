<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaPrivatePrsnfldrFolder"/>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<title>个人文件柜</title>
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
	
	/*overflow:auto;
	width: 100px;
	height:80%;
	border: 0px solid #444444;*/
	
}
.simpleTree li
{
	list-style: none;
	margin:0;
	padding:0 0 0 34px;
	line-height: 14px;
	background-color:#eeeff3;
}


.active{
font-weight:700;

}

.simpleTree .active{
color: #000000;
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
	background-color:#eeeff3;
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
	background: url(<%=path %>/oa/image/mymail/folder-org-leaf.gif) no-repeat -17px 0 #eeeff3;
}
#drag_container .folder-close, #drag_container .folder-close-last
{
	background: url(<%=path %>/oa/image/mymail/expandable.gif) no-repeat -17px 0 #eeeff3;
}

#drag_container .folder-open, #drag_container .folder-open-last
{
	background: url(<%=path %>/oa/image/mymail/collapsable.gif) no-repeat -17px 0 #eeeff3;
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
	background: url(<%=path %>/oa/image/mymail/shareexpandable.gif) 0 -2px no-repeat #eeeff3;
}
.simpleTree .folder-share-close-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/shareexpandable-last.gif) 0 -2px no-repeat #eeeff3;
}
.simpleTree .folder-share-open
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/share-collapsable.gif) 0 -2px no-repeat #eeeff3;
}

.simpleTree .folder-share-open-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/share-collapsable-last.gif) 0 -2px no-repeat #eeeff3;
}
.simpleTree .folder-share-leaf-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/share-leaf-last.gif) 0 -2px no-repeat #eeeff3;
}
.simpleTree .folder-share-leaf
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/folder-share-leaf.gif) 0 -1px no-repeat #eeeff3;
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
			//$("#myMenu1").hide();
			var folderId = node.get(0).id;
			parent.project_work_content.document.location="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile.action?folderId="+folderId;
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
			    var ret=OpenWindow("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!init.action?folderId="+folderId,"400","100",window);
				if(ret!="None" && ret!=undefined){
					parent.project_work_tree.document.location = "<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!tree.action";
				}
			},

			'edit': function(t) {

			  var folderId = node.get(0).id;
			    var ret=OpenWindow("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!initEdit.action?folderId="+folderId,"400","101",window);
				if(ret!="None" && ret!=undefined){
					parent.project_work_tree.document.location = "<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!tree.action";
				}

			},

			'share':function(t){
				var folderId = node.get(0).id;
				var nodeClass = node.get(0).className;
			    var ret=OpenWindow("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!initShare.action?folderId="+folderId,"350","200",window);
				var newNodeClass = "";
				if(ret!=undefined){
					if(ret == "share"){//改变文件夹状态为共享:文件夹-->共享(原来的文件夹样式只能是非共享文件夹:{1:)

						if(nodeClass){
							if(nodeClass == "folder-open"){
								newNodeClass = "folder-share-open";
							}
							if(nodeClass == "folder-open-last"){
								newNodeClass = "folder-share-open-last";
							}
							if(nodeClass == "folder-close"){
								newNodeClass = "folder-share-close";
							}
							if(nodeClass == "folder-close-last"){
								newNodeClass = "folder-share-close-last";
							}
							if(nodeClass == "doc"){
								newNodeClass = "folder-share-leaf";
							}
							if(nodeClass == "doc-last"){
								newNodeClass = "folder-share-leaf-last";
							}
							node.removeClass();
							node.addClass(newNodeClass);
						}
						
					}else if(ret == "cancelShare"){//取消共享：共享-->文件夹
						if(nodeClass){
							if(nodeClass == "folder-share-open"){
								newNodeClass = "folder-open";
							}
							if(nodeClass == "folder-share-open-last"){
								newNodeClass = "folder-open-last";
							}
							if(nodeClass == "folder-share-close"){
								newNodeClass = "folder-close";
							}
							if(nodeClass == "folder-share-close-last"){
								newNodeClass = "folder-close-last";
							}
							if(nodeClass == "folder-share-leaf"){
								newNodeClass = "doc";
							}
							if(nodeClass == "folder-share-leaf-last"){
								newNodeClass = "doc-last";
							}
							node.removeClass();
							node.addClass(newNodeClass);
						}	
					}else{
						//nothing
					}	
					//parent.project_work_tree.document.location.reload() ;
				}
			},

			'delete': function(t) {
				var folderName = $('span:first',node).text();	
				var id = node.get(0).id;
			 	if(confirm("删除文件夹“"+folderName+"”，确定？")){
			 		$.post(
			 			"<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!initDelete.action",
			 			{folderId:id},
			 			function(data){
			 				if(data == "1"){//此文件夹下存在文件
			 					if(confirm("文件夹“"+folderName+"”存有文件,删除此文件夹及所有文件，确定？")){
			 						$.post(
			 							"<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!delete.action",
			 							{folderId:id},
			 							function(msg){
			 								//alert(msg);
			 								parent.location = "<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!content.action";
			 							}
			 						);
			 					}
			 				}else{//此文件夹下不存在文件
			 						$.post(
			 							"<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!delete.action",
			 							{folderId:id},
			 							function(msg){
			 								//alert(msg);
			 								parent.location = "<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!content.action";
			 							}
			 						);
			 				}
			 			}
			 		);
			 	}

			}

		  }

		});

			//$("#myMenu1").css({position:'absolute', "left" : (event.clientX ),"top":(event.clientY+5)}).show();

		},
		animate:false
	});

	$("#rootSpan").contextMenu("myMenu2",{
		bindings:{
			'add':function(){
				addFolder();
			},
			'prop':function(){
				prop();
			}
		}
	});
	//在BODY上绑定右键功能
	$("body").contextMenu("myMenu2",{
		bindings:{
			'add':function(){
				addFolder();
			},
			'prop':function(){
				prop();
			}
		}
	});

	//添加文件夹
	function addFolder(){
		var ret=OpenWindow("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!init.action","400","100",window);
		if(ret!="None" && ret!=undefined){
			parent.project_work_tree.document.location = "<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!tree.action";
		}
	}
	//查看文件柜属性
	function prop(){
		var ret=OpenWindow("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!prop.action","280","120",window);
	}
});



</script>
</head>

<body class=contentbodymargin oncontextmenu="return false;"><!-- oncontextmenu="return false;" -->
<DIV id=contentborder align=left style="border:0px;background-color:#eeeff3;" >
<div class="contextMenu" oncontextmenu="return false;" id="myMenu1" style="width:100%">
	<ul>
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" />&nbsp;&nbsp;<SPAN class="wz">新建</SPAN> </li>
		<li id="edit"><img src="<%=path %>/oa/image/mymail/folder_edit.png" />&nbsp;&nbsp;<SPAN class="wz">重命名</SPAN> </li>
		<li id="delete"><img src="<%=path %>/oa/image/mymail/folder_delete.png" />&nbsp;&nbsp;<SPAN class="wz">删除</SPAN> </li>
		<li id="share"><img src="<%=path %>/oa/image/mymail/share.bmp" />&nbsp;&nbsp;<SPAN class="wz">共享</SPAN> </li>
	</ul>
</div>
<div class="contextMenu" oncontextmenu="return false;" id="myMenu2" style="width:100%">
	<ul>
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" />&nbsp;&nbsp;<SPAN class="wz">新建</SPAN> </li>
		<li id="prop"><img src="<%=root%>/images/ico/chakan.gif" />&nbsp;&nbsp;<SPAN class="wz">属性</SPAN> </li>
	</ul>
</div>
<ul class="simpleTree">
	<li class="root" id='1'><span style="cursor: hand;" id="rootSpan">个人文件柜</span>
		<ul>
			<%
				List<ToaPrivatePrsnfldrFolder> list=(List<ToaPrivatePrsnfldrFolder>)request.getAttribute("folderList");
				String[] child=(String[])request.getAttribute("hasChild");
				/*if(list.size()==0){//无节点
					out.println("<script language=\"javascript\">");
					out.println("$(document).ready(function(){");
					out.println("$(\"#non\").contextMenu(\"myMenu2\",{bindings:{'add':function(){");
					out.println("var ret=OpenWindow(\""+root+"/prsnfldr/privateprsnfldr/prsnfldrFolder!init.action\",\"300\",\"100\",window);");
					out.println("if(ret!=\"None\" && ret!=undefined){");
					out.println("parent.project_work_tree.document.location.reload() ;}");
						
						
					out.println("}}});");	
					out.println("});");
					out.println("</script>");
				}*/
				for(int i=0;i<list.size();i++){
					ToaPrivatePrsnfldrFolder folder=list.get(i);
					String charge=child[i];
					if("1".equals(charge)){//存在子节点
						String id=folder.getFolderId()+i;
						if("YES".equals(folder.getIsShare())){//共享状态
							out.println("<li id="+folder.getFolderId() +" picChange='folder-share'>");					
						}else{
							out.println("<li id="+folder.getFolderId() +">");	
						}
						out.println("<span>"+folder.getFolderName()+"</span>");	
						out.println("<ul class=ajax>");
						out.println("<li id="+id +">{url:"+root+"/prsnfldr/privateprsnfldr/prsnfldrFolder!syntree.action?folderId="+folder.getFolderId() +"}</li>");	
						out.println("</ul>");	
						out.println("</li>");	
					}else{
						if("YES".equals(folder.getIsShare())){
							out.println("<li id="+folder.getFolderId()+" leafChange='folder-share-leaf'>");		
						}else{
							out.println("<li id="+folder.getFolderId()+" >");		
						}
						out.println("<span>"+folder.getFolderName());
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
