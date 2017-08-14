<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaPagemodel"/>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
<html>
	<head>
		<title>页面模板关系树</title>
		<style>
			body
			{
				font: normal 12px arial, tahoma, helvetica, sans-serif;
				margin:0;
				padding:0px;
			}
			<%
				String theme = path+"/frame/theme_blue";
				String redtheme=path+"/frame/theme_red";
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
			 }else if(redtheme.equals(frameroot)){
			%>
				.contentbodymargin {
					BACKGROUND: #f3f3f3;
					MARGIN: 0 2px 2px 0px;
				}
				
				#contentborder {
					BORDER-RIGHT: #DBDBDB 1px solid;
					PADDING-RIGHT: 3px;
					PADDING-LEFT: 3px;
					BACKGROUND: white;
					PADDING-BOTTOM: 10px;
					OVERFLOW: auto;
					BORDER-LEFT: #DBDBDB 1px solid;
					WIDTH: 100%;
					BORDER-BOTTOM: #DBDBDB 1px solid;
					HEIGHT: 100%;
					margin-left: 2px;
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
				margin-left:-10px;
				background: url(<%=path %>/oa/image/mymail/root.gif) no-repeat 16px 0 #ffffff;
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
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
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
						//alert(node.get(0).id);
						window.parent.project_work_content.location="<%=root%>/viewmodel/viewModelPage!edit.action?id="+node.get(0).id+"&modelId=${modelId}";
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
						$($('span:first',node)).contextMenu('myMenu1',{
							bindings:{
								'newmodel':function(){
									window.parent.project_work_content.location="<%=root%>/viewmodel/viewModelPage!input.action?modelId=${modelId}&parentId="+node.get(0).id;
								},
								'editmodel':function(){
									alert("编辑模型");
								},
								'deletemodel':function(){
									if(confirm("您确认要删除该页面模板么？")==true){
										$.ajax({
									  		type:"post",
									  		dataType:"text",
									  		url:"<%=root%>/viewmodel/viewModelPage!delete.action",
									  		data:"modelId=${modelId}&id="+node.get(0).id,
									  		success:function(msg){
									  			if(msg=="true"){
													window.parent.location.reload();
												}else if(msg="has"){
													alert("该页面模型存在儿子节点，请将所有子节点删除后再进行删除操作");
												}else{
									  				alert("删除失败，请您重新操作");
												}
									  		}
									  	});
									  }
								}
							}
						});
					},
					animate:false
				});
				$("#rootSpan").contextMenu("rootMenu",{
					bindings:{
						'addroot':function(){
							$.ajax({
						  		type:"post",
						  		dataType:"text",
						  		url:"<%=root%>/viewmodel/viewModelPage!chargeRoot.action",
						  		data:"modelId=${modelId}",
						  		success:function(msg){
						  			if(msg=="true"){
										window.parent.project_work_content.location="<%=root%>/viewmodel/viewModelPage!input.action?modelId=${modelId}&parentId=0";
									}else{
						  				alert("在一个视图模型中只能存在一个主页面（即树形结构根节点）");
									}
						  		}
						  	});
						},
						'createJSP':function(){
							$.blockUI({ message: '' });
							window.parent.project_work_content.show("正在进行页面生成请勿进行其他操作...");
							$.ajax({
								type:"post",
								dataType:"json",
								url:"<%=root%>/viewmodel/viewModelPage!createJSP.action",
								data:"modelId=${modelId}",
								success:function(msg){
									alert("页面生成成功"+msg.sucn+"个"+",失败"+msg.faln+"个");
									$.unblockUI();
									window.parent.project_work_content.hidden();
								}
							});
						}
					}
				});
			});
		</script>
	</head>

	<body class=contentbodymargin oncontextmenu="return false;">
		<input type="hidden" id="modelId" name="modelId" value="${modelId }">
		<DIV id=contentborder align=left>
			<ul class="simpleTree" style="width:100%">
				<li class="root" id='root'><span style="cursor: hand;" id="rootSpan">${modelName}</span>
					<ul id="modelTree">
						<%
							List<ToaPagemodel> list=(List<ToaPagemodel>)request.getAttribute("list");
							String[] child=(String[])request.getAttribute("charge");
							if(list!=null){
								for(int i=0;i<list.size();i++){
									ToaPagemodel temp =list.get(i);
									String charge=child[i];
									if("1".equals(charge)){//存在子节点
										out.println("<li id="+temp.getPagemodelId() +">");	
										out.println("<span>"+temp.getPagemodelDes()+"</span>");	
										out.println("<label style='display:none;'>group</label>");	
										out.println("<ul class=ajax>");
										out.println("<li class=\"person\" id=ajax"+temp.getPagemodelId()+">{url:"+root+"/viewmodel/viewModelPage!getPageRootChild.action?parentId="+temp.getPagemodelId()+"&modelId="+temp.getToaForamula().getForamulaId()+"}</li>");	
										out.println("</ul>");	
										out.println("</li>");	
									}else{
										out.println("<li id="+temp.getPagemodelId()+">");		
										out.println("<span>"+temp.getPagemodelDes()+"</span>");
										out.println("<label style='display:none;'>group</label>");	
										out.println("</li>");
									}
								}
							}
						%>		
					</ul>
				</li>
			</ul>
			<div class="contextMenu" id="myMenu1">
				<ul>
					<li id="newmodel"><img src="<%=root %>/oa/image/mymail/arrow_refresh.png" /> 新建页面模板</li>
					<%--<li id="editmodel"><img src="<%=root %>/oa/image/mymail/folder_add.png" /> 修改页面模板</li>--%>
					<li id="deletemodel"><img src="<%=root %>/oa/image/mymail/folder_delete.png" /> 删除页面模板</li>
				</ul>
			</div>
			<div class="contextMenu" id="rootMenu">
				<ul>
					<li id="addroot"><img src="<%=root %>/oa/image/mymail/folder_add.png" /> 新建模板</li>
					<li id="createJSP"><img src="<%=root %>/oa/image/mymail/folder_add.png" />生成页面</li>
				</ul>
			</div>
		</DIV>
	</body>
</html>
