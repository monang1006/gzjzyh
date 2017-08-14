<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaPrivatePrsnfldrFolder"/>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<title>移动文件到个人文件柜</title>
<%@include file="/common/include/meta.jsp" %>
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
	background: url(<%=path %>/personal_office/mymail/images/root.gif) no-repeat 16px 0 #ffffff;
}
.simpleTree .line
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/personal_office/mymail/images/line_bg.gif) 0 0 no-repeat transparent;
}
.simpleTree .line-last
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/personal_office/mymail/images/spacer.gif) 0 0 no-repeat transparent;
}
.simpleTree .line-over
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/personal_office/mymail/images/line_bg_over.gif) 0 0 no-repeat transparent;
}
.simpleTree .line-over-last
{
	margin:0 0 0 -16px;
	padding:0;
	line-height: 3px;
	height:3px;
	font-size:3px;
	background: url(<%=path %>/personal_office/mymail/images/line_bg_over_last.gif) 0 0 no-repeat transparent;
}
.simpleTree .folder-open
{
	margin-left:-16px;
	background: url(<%=path %>/personal_office/mymail/images/collapsable.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-open-last
{
	margin-left:-16px;
	background: url(<%=path %>/personal_office/mymail/images/collapsable-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-close
{
	margin-left:-16px;
	background: url(<%=path %>/personal_office/mymail/images/expandable.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-close-last
{
	margin-left:-16px;
	background: url(<%=path %>/personal_office/mymail/images/expandable-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .doc
{
	margin-left:-16px;
	background: url(<%=path %>/personal_office/mymail/images/leaf.gif) 0 -1px no-repeat #fff;
}
.simpleTree .doc-last
{
	margin-left:-16px;
	background: url(<%=path %>/personal_office/mymail/images/leaf-last.gif) 0 -1px no-repeat #fff;
}
.simpleTree .ajax
{
	background: url(<%=path %>/personal_office/mymail/images/spinner.gif) no-repeat 0 0 #ffffff;
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
	background: url(<%=path %>/personal_office/mymail/images/leaf.gif) no-repeat -17px 0 #ffffff;
}
#drag_container .folder-close, #drag_container .folder-close-last
{
	background: url(<%=path %>/personal_office/mymail/images/expandable.gif) no-repeat -17px 0 #ffffff;
}

#drag_container .folder-open, #drag_container .folder-open-last
{
	background: url(<%=path %>/personal_office/mymail/images/collapsable.gif) no-repeat -17px 0 #ffffff;
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
	background: url(<%=path %>/personal_office/mymail/images/shareexpandable.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-share-close-last
{
	margin-left:-16px;
	background: url(<%=path %>/personal_office/mymail/images/shareexpandable-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-share-open
{
	margin-left:-16px;
	background: url(<%=path %>/personal_office/mymail/images/share-collapsable.gif) 0 -2px no-repeat #fff;
}

.simpleTree .folder-share-open-last
{
	margin-left:-16px;
	background: url(<%=path %>/personal_office/mymail/images/share-collapsable-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-share-leaf-last
{
	margin-left:-16px;
	background: url(<%=path %>/personal_office/mymail/images/share-leaf-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-share-leaf
{
	margin-left:-16px;
	background: url(<%=path %>/personal_office/mymail/images/folder-share-leaf.gif) 0 -1px no-repeat #fff;
}
#contentborder {
	BORDER-RIGHT: #848284 1px solid;
	PADDING-RIGHT: 3px;
	BORDER-TOP: #848284 1px solid;
	PADDING-LEFT: 3px;
	BACKGROUND: white;
	PADDING-BOTTOM: 10px;
	OVERFLOW: auto;
	BORDER-LEFT: #848284 1px solid;
	WIDTH: 100%;
	PADDING-TOP: 0px;
	BORDER-BOTTOM: #848284 1px solid;
	
	HEIGHT: 60%
}
</style>
<script type="text/javascript" src="<%=path %>/personal_office/mymail/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/personal_office/mymail/js/jquery.contextmenu.r2.js"></script>
<script type="text/javascript" src="<%=path %>/personal_office/mymail/js/jquery.simple.tree.js"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>

<script type="text/javascript">
var simpleTreeCollection;
$(document).ready(function(){
	simpleTreeCollection = $('.simpleTree').simpleTree({
		drag:false,
		autoclose: false,
		afterClick:function(node){
			var folderId = node.get(0).id;
			var folderName = $('span:first',node).text()
			$("#outputFolder").val(folderName);
			$("#outputFolderId").val(folderId);
		},
		afterDblClick:function(node){
		},
		afterMove:function(destination, source, pos){
		},
		afterAjax:function()
		{
		},
		animate:false
	});


});

</script>
</head>

<body oncontextmenu="return false;"><!-- oncontextmenu="return false;" -->
<div style="height:100%;width:100%;overflow: auto;">
	<form action="">
				输入或选择文件夹：<br/>
				<input id="outputFolder" type="text" value="" style="width: 100%;height: 20px;"><br/>
				<input id="outputFolderId" type="hidden" name="folderId">
				<div id="contentborder">
					<ul class="simpleTree">
						<li class="root" id='1'><span style="cursor: hand;" id="rootSpan">个人文件柜</span>
							<ul>
								<%
									List<ToaPrivatePrsnfldrFolder> list=(List<ToaPrivatePrsnfldrFolder>)request.getAttribute("folderList");
									String[] child=(String[])request.getAttribute("hasChild");
									for(int i=0;i<list.size();i++){
										ToaPrivatePrsnfldrFolder folder=list.get(i);
										String charge=child[i];
										if("1".equals(charge)){//存在子节点
											String id=folder.getFolderId()+i;
											out.println("<li id="+folder.getFolderId()+">");					
											out.println("<span>"+folder.getFolderName()+"</span>");	
											out.println("<ul class=ajax>");
											out.println("<li id="+id +">{url:"+root+"/prsnfldr/privateprsnfldr/prsnfldrFolder!syntree.action?folderId="+folder.getFolderId() +"}</li>");	
											out.println("</ul>");	
											out.println("</li>");	
										}else{
											out.println("<li id="+folder.getFolderId()+">");		
											out.println("<span>"+folder.getFolderName());
											out.println("</span>");
											out.println("</li>");
										}
									}	
								%>		
							</ul>
						</li>		
					</ul>
					</div>
				 	文件名：<input id="outputFile" type="text" value="" style="width: 100%;height: 20px;"/>
				 		<input type="button" value="确定"/>&nbsp;
							<input type="button" value="取消"/>
	</form>						
</div>
</body>

</html>
