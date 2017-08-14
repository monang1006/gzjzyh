<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaTrainColumn"/>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<%@include file="/common/include/meta.jsp"%>
<title>培训栏目类别</title>
<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
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
function showname(id){
     $.post("<%=path%>/infopub/column/column!show.action",
            {clumnId:id},
            function(date){
            return date;
            });
}
$(document).ready(function(){
	simpleTreeCollection = $('.simpleTree').simpleTree({
		drag:false,
		autoclose: false,
		afterClick:function(node){
			var folderId = node.get(0).id;
			parent.project_work_content.document.location="<%=root%>/personnel/trainingmanage/training.action?clumnId="+folderId;
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
			   	parent.project_work_content.document.location="<%=root%>/personnel/trainingmanage/training!input.action?clumnId="+folderId;
				//parent.location.reload() ;
			},

           
				
			'setpcol':function(t){
				var folderName = $('span:first',node).text();	
				var id = node.get(0).id;
				var parent=node.get(0).name;
				
				//confirm(folderName+"已经是顶级栏目！");
				//return false;
			 if(confirm("确定要将栏目“"+folderName+"”置为顶级栏目吗?")){
			 		$.post(
			 		"<%=path%>/infopub/column/column!setParentColumn.action",
			 		{clumnId:id},
			 		function(data){
			 			if(data == "settrue"){	 								 			
			 			window.parent.location.reload();
			 			}else{
				 			alert("该栏目已经是顶级栏目，不需要再设置");
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

<body class=contentbodymargin oncontextmenu="return false;">

<div class="contextMenu" oncontextmenu="return false;" id="myMenu1" style="width:100%">
	<ul>
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 添加培训信息</li>
		
	</ul>
</div>
<div id=contentborder>
<ul class="simpleTree">
	<li class="root" id='1'><span>栏目管理</span>
		<ul>
			<%
				List<ToaTrainColumn> list=(List<ToaTrainColumn>)request.getAttribute("columnList");
				String[] child=(String[])request.getAttribute("hasChild");
				for(int i=0;i<list.size();i++){
					ToaTrainColumn folder=list.get(i);
					String charge=child[i];
					String clumnparent=folder.getClumnParent();//判断是否是顶级栏目
					if("1".equals(charge)){//存在子节点
						String id=folder.getClumnId()+i;
						
						out.println("<li name="+clumnparent+" id="+folder.getClumnId() +">");
						out.println("<span>"+folder.getClumnName()+"</span>");	
						out.println("<ul class=ajax>");
						out.println("<li name="+clumnparent+"  id="+id +">{url:"+root+"/personnel/trainingmanage/training!syntree.action?clumnId="+folder.getClumnId()+"}</li>");	
						out.println("</ul>");	
						out.println("</li>");	
					}else{
						out.println("<li name="+clumnparent+"  id="+folder.getClumnId() +">");
						out.println("<span>"+folder.getClumnName());
						out.println("</span>");
						out.println("</li>");
					}
				}	
			%>		
		</ul>
	</li>		
</ul>
</div>
</body>

</html>
