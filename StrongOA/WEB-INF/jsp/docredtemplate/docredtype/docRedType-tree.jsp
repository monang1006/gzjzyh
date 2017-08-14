<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaDocredGroup"/>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<title>公文套红类别</title>
<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
<SCRIPT type="text/javascript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
<script type="text/javascript" src="<%=root%>/oa/js/mymail/jquery.js"></script>
<script type="text/javascript" src="<%=root%>/oa/js/mymail/jquery.contextmenu.r2.js"></script>
<script type="text/javascript" src="<%=root%>/oa/js/mymail/jquery.simple.tree.js"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
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
	background: url(<%=path %>/oa/image/mymail/leaf.gif) 0 -1px no-repeat #fff;
}
.simpleTree .doc-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/leaf-last.gif) 0 -1px no-repeat #fff;
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

.simpleTree .folder-share-close
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/shareexpandable.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-share-close-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/shareexpandable-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-share-open
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/share-collapsable.gif) 0 -2px no-repeat #fff;
}

.simpleTree .folder-share-open-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/share-collapsable-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-share-leaf-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/share-leaf-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-share-leaf
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/folder-share-leaf.gif) 0 -1px no-repeat #fff;
}
</style>

<script type="text/javascript">
var simpleTreeCollection;
$(document).ready(function(){
	simpleTreeCollection = $('.simpleTree').simpleTree({
		drag:false,
		autoclose: false,
		afterClick:function(node){
			var folderId = node.get(0).id;
			parent.project_work_content.document.location="<%=root%>/docredtemplate/docreditem/docRedItem.action?redtempGroupId="+folderId;
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
			    var ReturnStr=OpenWindow("<%=root%>/docredtemplate/docredtype/docRedType!init.action?redtempGroupId="+folderId, 
                                   "400px", "250px", window);
				if(ReturnStr=="OK"){
					location.reload() ;
				}
			},

			'edit': function(t) {

			  var folderId = node.get(0).id;
			    var ReturnStr=OpenWindow("<%=root%>/docredtemplate/docredtype/docRedType!input.action?redtempGroupId="+folderId,"400px", "250px", window);
				if(ReturnStr=="OK"){
					parent.location.reload() ;
				}

			},

			'delete': function(t) {
				var folderName = $('span:first',node).text();	
				var id = node.get(0).id;
			 	if(confirm("确定要删除套红类别“"+folderName+"”吗?")){
			 		$.post(
			 		"<%=path%>/docredtemplate/docredtype/docRedType!delete.action",
			 		{redtempGroupId:id},
			 		function(data){
			 			if(data == "delfalse"){//此模板类别下已存在模板			 								 			
			 				alert("该套红类别下已存在套红项，不允许删除");
			 			}else{
				 			alert("删除成功");
				 			parent.location.reload();
			 			}
			 		 }
			 		);
			 	}

			}

		  }

		});


		},
		animate:false
	});


});


</script>
</head>

<body class=contentbodymargin oncontextmenu="return false;"><!-- oncontextmenu="return false;" -->

<div class="contextMenu" oncontextmenu="return false;" id="myMenu1" style="width:100%">
	<ul>
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 添加</li>
		<li id="edit"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /> 修改</li>
		<li id="delete"><img src="<%=path %>/oa/image/mymail/folder_delete.png" /> 删除</li>
	</ul>
</div>
<ul class="simpleTree">
	<li class="root" id='1'><span>公文套红类别</span>
		<ul>
			<%
				List<ToaDocredGroup> list=(List<ToaDocredGroup>)request.getAttribute("docRedTypeList");
				String[] child=(String[])request.getAttribute("hasChild");
				for(int i=0;i<list.size();i++){
					ToaDocredGroup folder=list.get(i);
					String charge=child[i];
					if("1".equals(charge)){//存在子节点
						String id=folder.getRedtempGroupId()+i;
						out.println("<li id="+folder.getRedtempGroupId() +">");
						out.println("<span>"+folder.getRedtempGroupName()+"</span>");	
						out.println("<ul class=ajax>");
						out.println("<li id="+id +">{url:"+root+"/docredtemplate/docredtype/docRedType!syntree.action?redtempGroupId="+folder.getRedtempGroupId()+"}</li>");	
						out.println("</ul>");	
						out.println("</li>");	
					}else{
						out.println("<li id="+folder.getRedtempGroupId()+">");
						out.println("<span>"+folder.getRedtempGroupName());
						out.println("</span>");
						out.println("</li>");
					}
				}	
			%>		
		</ul>
	</li>		
</ul>
</body>

</html>
