<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<title>消息模块导航</title>
		<style>
			#contentborder {
				BORDER-RIGHT: #848284 1px solid;
				PADDING-RIGHT: 3px;
				BORDER-TOP: #848284 0px solid;
				PADDING-LEFT: 13px;
				BACKGROUND: white;
				PADDING-BOTTOM: 10px;
				OVERFLOW: auto;
				BORDER-LEFT: #848284 1px solid;
				WIDTH: 100%;
				PADDING-TOP: 13px;
				BORDER-BOTTOM: #848284 1px solid;
				POSITION: absolute;
				HEIGHT: 100%
			}
			body
			{
				font: normal 12px arial, tahoma, helvetica, sans-serif;
				margin:0;
				padding:0px;
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
				cursor: pointer;
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
	<script type="text/javascript" src="<%=root %>/common/js/jquery/jquery-1.2.6.js"></script>
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
			var groupId = node.get(0).id; 
			document.getElementById("groupId").value = groupId;
			parent.project_work_content.document.location="<%=root %>/agencygroup/groupDet.action?groupId="+groupId;
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

			'edit': function(t) {
				var text = node.text();
				var id = node.get(0).id;
				var groupId = $("#groupId").val();
				var plaintext = text.substring(0,text.indexOf("("));
				var ret=OpenWindow("<%=root%>/agencygroup/agencyGroup!input.action?groupId="+id,"400","170",window);
				if("suc"==ret && ret!=undefined){
					parent.project_work_tree.location="<%=root %>/agencygroup/agencyGroup.action?groupId="+groupId;
				}				
			},

			'delete': function(t) {
				var text = node.text();
				var id = node.get(0).id; 
				var count = text.substring(text.indexOf("(")+1,text.indexOf(")"));
				var plaintext = text.substring(0,text.indexOf("("));
				var groupId = $("#groupId").val();
				if(confirm("删除发文组“"+plaintext+"”，确定？")){
					
					if(count==0 ||(count > 0 && confirm("发文组“"+plaintext+"”存有“"+count+"”个机构，删除此组，确定？"))){
						$.ajax({
							type:"post",
							url:"<%=root%>/agencygroup/agencyGroup!delete.action",
							data:{groupId:id},
							success:function(data){
								parent.document.location="<%=root%>/fileNameRedirectAction.action?toPage=agencygroup/agencyGroup-personal.jsp" ;
							}
						});
					}
				}
			},
			'impt': function(t) {
				var id = node.get(0).id;
				var ret=OpenWindow("<%=root%>/agencygroup/agencyGroup!importOrgList.action?groupId="+id,"600","400",window);
				var groupId = $("#groupId").val();
			   
				if("yes"==ret && ret!=undefined){
					//parent.project_work_tree.location="<%=root %>/agencygroup/agencyGroup.action";
					parent.document.location="<%=root%>/fileNameRedirectAction.action?toPage=agencygroup/agencyGroup-personal.jsp?groupId="+groupId ;
				}
			}

		  }

		});
		},
		animate:false
	});
	//新建组
	$("#rootSpan").contextMenu("myMenu2",{
		bindings:{
			'add':function(){
				addGroup();
			}
		}
	});
	
	//在BODY上绑定右键功能
	$("body").contextMenu("myMenu2",{
		bindings:{
			'add':function(){
				addGroup();
			}
		}
	});
	
	//添加组
	function addGroup(){
		var ret=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/agencygroup/agencyGroup-add.jsp","400","170",window);
		if("suc"==ret && ret!=undefined){
			parent.project_work_tree.location="<%=root %>/agencygroup/agencyGroup.action";
		}
	}
	
});

</script>
</head>

<body >
<DIV id=contentborder >
		<input id="groupId" type="hidden" name="groupId" value="${groupId }"/>
	<div class="contextMenu" id="myMenu1" >
		<ul>
			<li id="edit"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /> 发文组重命名</li>
			<li id="delete"><img src="<%=path %>/oa/image/mymail/folder_delete.png" /> 删除发文组</li>
			<li id="impt"><img src="<%=path %>/oa/image/mymail/folder_add.png" />  导入机构</li>
		</ul>
	</div>
	<div class="contextMenu"  id="myMenu2" >
		<ul>
			<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 新建发文组</li>
		</ul>
	</div>
		<ul class="simpleTree" style="width:100%">
			<li class="root" id='1'><label style="cursor: hand;" id="rootSpan">发文组</label>
				<ul id="groupTree">
					${group }
				</ul>
			</li>		
		</ul>
</DIV>
</body>

</html>
