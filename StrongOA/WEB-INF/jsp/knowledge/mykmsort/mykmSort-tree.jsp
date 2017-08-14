<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<title>收藏模块导航</title>
		<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<style>
			body
			{
				font: normal 12px arial, tahoma, helvetica, sans-serif;
				margin:0;
				padding:0px;
			}
			<%
				String theme = path+"/frame/theme_blue";
				if(theme.equals(frameroot)){
			%>
			.contentbodymargin {
				BACKGROUND: #dae6f2; MARGIN: 0 0 0 0;
			}
			#contentborder {
				BORDER-RIGHT: #506eaa 1px solid;
				BACKGROUND: white;
				PADDING-BOTTOM: 5px;
				OVERFLOW: auto;
				BORDER-LEFT: #506eaa 1px solid;
				WIDTH: 100%;
				BORDER-BOTTOM: #506eaa 1px solid;
				HEIGHT: 100%;
    			margin-left:0px;
			}
			<%
				}else{
			%>
			.contentbodymargin {
				BACKGROUND: #dae6f2; MARGIN: 0 0 0 0;
			}
			#contentborder {
				BORDER-RIGHT: #848284 1px solid;
				PADDING-RIGHT: 3px;
				BORDER-TOP: #848284 0px solid;
				PADDING-LEFT: 3px;
				BACKGROUND: white;
				PADDING-BOTTOM: 10px;
				OVERFLOW: auto;
				BORDER-LEFT: #848284 1px solid;
				WIDTH: 100%;
				PADDING-TOP: 0px;
				BORDER-BOTTOM: #848284 1px solid;
				POSITION: absolute;
				HEIGHT: 100%
			}
			<%
				}
			%>
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
				margin-left:0px;
				background: url(<%=path %>/frame/theme_gray/images/base.gif) no-repeat 16px 0 #ffffff;
			}
			.simpleTree .line
			{
				margin:0 0 0 -10px;
				padding:0;
				line-height: 3px;
				height:3px;
				font-size:3px;
				background: url(<%=path %>/oa/image/mymail/line_bg.gif) 0 0 no-repeat transparent;
			}
			.simpleTree .line-last
			{
				margin:0 0 0 -10px;
				padding:0;
				line-height: 3px;
				height:3px;
				font-size:3px;
				background: url(<%=path %>/oa/image/mymail/spacer.gif) 0 0 no-repeat transparent;
			}
			.simpleTree .line-over
			{
				margin:0 0 0 -10px;
				padding:0;
				line-height: 3px;
				height:3px;
				font-size:3px;
				background: url(<%=path %>/oa/image/mymail/line_bg_over.gif) 0 0 no-repeat transparent;
			}
			.simpleTree .line-over-last
			{
				margin:0 0 0 -10px;
				padding:0;
				line-height: 3px;
				height:3px;
				font-size:3px;
				background: url(<%=path %>/oa/image/mymail/line_bg_over_last.gif) 0 0 no-repeat transparent;
			}
			.simpleTree .folder-open
			{
				margin-left:-10px;
				background: url(<%=path %>/oa/image/mymail/collapsable.gif) 0 -2px no-repeat #fff;
			}
			.simpleTree .folder-open-last
			{
				margin-left:-10px;
				background: url(<%=path %>/oa/image/mymail/collapsable-last.gif) 0 -2px no-repeat #fff;
			}
			.simpleTree .folder-close
			{
				margin-left:-10px;
				background: url(<%=path %>/oa/image/mymail/expandable.gif) 0 -2px no-repeat #fff;
			}
			.simpleTree .folder-close-last
			{
				margin-left:-10px;
				background: url(<%=path %>/oa/image/mymail/expandable-last.gif) 0 -2px no-repeat #fff;
			}
			.simpleTree .doc
			{
				margin-left:-10px;
				background: url(<%=path %>/oa/image/mymail/leaf.gif) 0 -1px no-repeat #fff;
			}
			.simpleTree .doc-last
			{
				margin-left:-10px;
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
				cursor: hand;
			}
			.simpleTree .active
			{
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

		</style>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.contextmenu.r2.js"></script>
		<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.simple.tree.js"></script>
		<script type="text/javascript" src="<%=root %>/common/js/common/common.js"></script>
			<script type="text/javascript">
			var simpleTreeCollection;
			$(document).ready(function(){
				simpleTreeCollection = $('.simpleTree').simpleTree({
					drag:false,
					autoclose: false,
					afterClick:function(node){
						//var Id = node.get(0).id;
						var Id=$('span:first',node).parent().attr("id");
						
							parent.mykmlist.document.location="<%=root%>/knowledge/mykm/mykm.action?mykmSortId="+Id;
					
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
						if($('span:first',node).attr("id")=='son'){
							
						}else{
							$($('span:first',node)).contextMenu('myMenu1', {
							  bindings: {
								
								'add': function(t) {
									var boo=OpenWindow('<%=root%>/knowledge/mykmsort/mykmSort!input.action', '400', '260', window);
									if(boo!=undefined && boo!=null && boo!=""){
										parent.sortlist.document.location.reload() ;
									}
								},
								'edit': function(t) {
								var id=$('span:first',node).parent().attr("id");
								if(id==null||id==""){
								    alert("默认类型不可以编辑！");
								    return;
								}
									var boo=OpenWindow('<%=root%>/knowledge/mykmsort/mykmSort!input.action?mykmSortId='+$('span:first',node).parent().attr("id"), '400', '260', window);
									if(boo!=undefined && boo!=null && boo!=""){
									
										parent.sortlist.document.location.reload() ;
									}
								},
								'delete': function(t) {
									if(confirm("您是否确定删除？")==true){
									
									parent.sortlist.document.location="<%=root%>/knowledge/mykmsort/mykmSort!delete.action?mykmSortId="+$('span:first',node).parent().attr("id");
									
									}
								}
							  }
							});
						}
					},
					animate:false
				});
				
			});
			
			
		</script>
	</head>
	 
	<body>
	<br>
	<DIV id=contentborder  class=contentbodymargin oncontextmenu="return false;" align="left">
			<ul class="simpleTree" style="width:100%">
				<li class="root" id='root'><span style="cursor: hand;" id="rootSpan">类型管理</span>
					<ul>
					<s:if test="sortList!=null">
					<s:iterator value="sortList" id="sort">
					<li id='${sort.mykmSortId}'>
					<span>${sort.mykmSortName}</span>
					
					</li>
					</s:iterator>
					
					</s:if>
					<s:else>
					<li>
					<span>默认类型</span>
					</s:else>
					</ul>
				</li>
			</ul>
		<div class="contextMenu" id="myMenu1">
			<ul>
			
				<li id="add"><img src="<%=root %>/oa/image/mymail/arrow_refresh.png" /> 添加类型</li>
				<li id="edit"><img src="<%=root %>/oa/image/mymail/folder_edit.png" /> 编辑类型</li>
			
				<li id="delete"><img src="<%=root %>/oa/image/mymail/folder_delete.png" /> 删除类型</li>
			</ul>
		</div>
		
	
		</DIV>
	</body>
</html>
