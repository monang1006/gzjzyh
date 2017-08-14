<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaPrivatePrsnfldrFolder"/>
<jsp:directive.page import="com.strongit.oa.util.UUIDGenerator"/>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<title>共享文件柜</title>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
<style>
body
{
	font: normal 12px arial, tahoma, helvetica, sans-serif;
	margin:0;
	padding:5px;
	background-color:#eeeff3;
}


.active{
font-weight:700;

}

.simpleTree .active{
color: #000000;
}


.wz{
text-indent:0px;
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
			var folderId = node.attr("folderId");
			parent.project_work_content.document.location="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFile!shareFileList.action?folderId="+folderId;
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
				var folderId = node.attr("folderId");
			    var ret=OpenWindow("<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!init.action?folderId="+folderId,"300","100",window);
				if(ret!="None" && ret!=undefined){
					parent.project_work_tree.document.location.reload() ;
				}
			},

			'edit': function(t) {

			  alert('Trigger was '+t.id+'\nAction was Save');

			},

			'delete': function(t) {

			 

			}

		  }

		});

			//$("#myMenu1").css({position:'absolute', "left" : (event.clientX ),"top":(event.clientY+5)}).show();

		},
		animate:false
	});
//	$("#myMenu1").blur(function(){
//		$("#myMenu1").hide();
//	});

});
//$(document).click(function(){
//	$("#myMenu1").hide();
//});


</script>
</head>

<body class=contentbodymargin oncontextmenu="return false;">
<DIV id=contentborder align=left style="width:100%;height: 98%; border:0px;background-color:#eeeff3;" >

<!-- <div class="contextMenu" id="myMenu1" style="width:100%">
	<ul>
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 添加</li>
		<li id="delete"><img src="<%=path %>/oa/image/mymail/folder_delete.png" /> 删除</li>
		<li id="edit"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /> 修改</li>
	</ul>
</div>-->
<ul class="simpleTree">
	<li class="root" id='1'><span class="wz">共享文件柜</span>
		<ul class="text">
			<%
				List<ToaPrivatePrsnfldrFolder> list=(List<ToaPrivatePrsnfldrFolder>)request.getAttribute("folderList");
				String[] child=(String[])request.getAttribute("hasChild");
				UUIDGenerator generator = new UUIDGenerator();
				for(int i=0;i<list.size();i++){
					ToaPrivatePrsnfldrFolder folder=list.get(i);
					String charge=child[i];
					if("1".equals(charge)){//存在子节点
						String id=folder.getFolderId()+i;
						out.println("<li folderId="+folder.getFolderId()+" id="+folder.getFolderId()+generator.generate() +" picChange='folder-share'>");					
						out.println("<span>"+folder.getFolderName()+"("+folder.getFolderCreatePerson()+")</span>");	
						out.println("<ul class=ajax>");
						out.println("<li folderId="+folder.getFolderId()+" id="+generator.generate() +">{url:"+root+"/prsnfldr/privateprsnfldr/prsnfldrFolder!synShareTree.action?folderId="+folder.getFolderId() +"}</li>");	
						out.println("</ul>");	
						out.println("</li>");	
					}else{
						out.println("<li folderId="+folder.getFolderId()+" id="+folder.getFolderId()+generator.generate()+" leafChange='folder-share-leaf'>");		
						out.println("<span>"+folder.getFolderName()+"("+folder.getFolderCreatePerson()+")");
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
