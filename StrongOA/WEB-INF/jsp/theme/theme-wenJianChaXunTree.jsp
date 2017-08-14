<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>导航器内容</title>
		<script type="text/javascript">
			var imageRootPath='<%=frameroot%>';
		</script>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/navigator_windows.css" type=text/css rel=stylesheet />
		<link href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet />
		<script src="<%=jsroot%>/mztree_check/mztreeview_check.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script type="text/javascript">
		var fontsize="14";
		if(fontsize!=null&&fontsize!=""){
			fontsize=fontsize+"px";
		}else{
			fontsize="12px";
		}
		function navigates(url,title){
			window.parent.parent.actions_container.personal_properties_toolbar.navigate("<%=path%>"+url,title);
		}
		
		function getPageModelId(){
			return document.getElementById("pageModelId").value;
		}
		
		function showMenu(){
			try{
			if(window.top.perspective_toolbar!=undefined){
				window.top.perspective_toolbar.showMenu("nav_${priviparent}");
			}
			}catch(e){
			}
		}
		
		function gotoPrivil(url,title){
			window.parent.privil_content.location = "<%=path%>"+url;
		}

		var getDoingDocCout = 0;
		var getTodoDocCount = 0;
		var getDoneDocCount = 0;
		var getCount = function(){
			$.ajax({ type:"post",
					url:'<%=path%>/bgt/documentview/documentView!getDoingDocCout.action?'+new Date(),
					data:{ mytype:'<%=request.getParameter("mytype")%>',excludeWorkflowType:"370020"},
					async:false,
					success:function(response){
						if(response==""){
						}else{
							getDoingDocCout = response;
						}
					},
					error:function(data){
						//alert("对不起，操作异常"+data);
					}
			   });
			$.ajax({ type:"post",
				url:'<%=path%>/bgt/documentview/documentView!getTodoDocCount.action?'+new Date(),
				data:{ mytype:'<%=request.getParameter("mytype")%>'},
				async:false,
				success:function(response){
					if(response==""){
					}else{
						getTodoDocCount = response;
					}
				},
				error:function(data){
					//alert("对不起，操作异常"+data);
				}
		   });
			$.ajax({ type:"post",
				url:'<%=path%>/bgt/documentview/documentView!getDoneDocCount.action?'+new Date(),
				data:{ mytype:'<%=request.getParameter("mytype")%>',excludeWorkflowType:"370020",doneYear:"-1"},
				async:false,
				success:function(response){
					if(response==""){
					}else{
						getDoneDocCount = response;
					}
				},
				error:function(data){
					//alert("对不起，操作异常"+data);
				}
		   });
		}
		var getManaMenuFontSize = function(){
			$.ajax({ type:"post",
				url:'<%=path%>/theme/theme!gotoBgtMenutree.action',
				async:false,
				success:function(response){
					if(response==""){
					}else{
						var ManaMenuFontSize = response;
					}
				},
				error:function(data){
					//alert("对不起，操作异常"+data);
				}
		   });
		}
	</script>
		<style type="text/css">
a:link,a:visited,a:hover,a:active {
	font-family: "宋体";
	font-size: expression(fontsize);
	color: #000000;
	text-decoration: none;
}
</style>
	</head>
	<body class="contentbodymargin" onload="setTimeout('showMenu()',1)">
		<s:hidden id="pageModelId" name="pageModelId"></s:hidden>
		<div id=treecontentborder>
			<table border="0" cellpadding="0" cellspacing="0" width="95%" style="height: 100%;">
				<tr>
					<td valign=top>
						<table cellspacing=0 cellpadding=2 width="100%" border=0>
							<tbody>
								<tr>
									<td style="width: 5;"></td>
									<td></td>
								</tr>
								<tr>
									<td style="width: 5;"></td>
									<td>
										<div class=dtree id=treeviewarea></div>
									</td>
								</tr>
							</tbody>
						</table>
						<script type="text/javascript" charset="utf-8">
						function reLoadDate(){
							getCount();
							window.tree = new MzTreeView("tree");
							tree.setIconPath("<%=path%>/frame/theme_blue/images/");
							tree.icons["property"] = "property.gif";
							tree.icons["css"] = "collection.gif";
							tree.icons["book"] = "book.gif";
							tree.iconsExpand["book"] = "bookopen.gif";
							
							<%if ("dept".equals((String) request.getParameter("mytype"))) {%>
							tree.nodes["0_C0"] = 'text:处内文件;isChecked:no;';
							<%}%>
							<%if ("org".equals((String) request.getParameter("mytype"))) {%>
							tree.nodes["0_C0"] = 'text:厅内文件;isChecked:no;';
							<%}%>
							
							
							tree.nodes["C0_000200140001"] = 'text:在办文件(<strong style="color:red">' + getDoingDocCout + '</strong>);valueId:000200140001;isChecked:no;showTitle:false;target:project_work_content;onclick:gotoPrivil("/workflowDesign/action/processMonitor!mainFrameOrg.action?model=person&mytype=<%=request.getParameter("mytype")%>&state=0&privilSyscode=0001000100220001","在办文件");checkAble:true;'
							tree.nodes["C0_000200140005"] = 'text:待签收文件(<strong style="color:red">' + getTodoDocCount + '</strong>);valueId:000200140005;isChecked:no;showTitle:false;target:project_work_content;onclick:gotoPrivil("/bgt/documentview/documentView!mainFrameOrg.action?mytype=<%=request.getParameter("mytype")%>","待签收文件");checkAble:true;'
							tree.nodes["C0_000200140002"] = 'text:办结文件(<strong style="color:red">' + getDoneDocCount + '</strong>);valueId:000200140002;isChecked:no;showTitle:false;target:project_work_content;onclick:gotoPrivil("/workflowDesign/action/processMonitor!mainFrameOrg.action?model=person&mytype=<%=request.getParameter("mytype")%>&state=1&doneYear=-1&privilSyscode=0001000100220002","办结文件");checkAble:true;'
							/*
							<%if ("org".equals((String) request.getParameter("mytype"))) {%>
							tree.nodes["C0_000200100001"] = 'text:公文限时办理情况;valueId:000200100001;isChecked:no;showTitle:true;target:project_work_content;onclick:gotoPrivil("/workflowDesign/action/documentHandle!limitTime.action","公文限时办理情况");checkAble:true;'
							<%}%>
							*/
							document.getElementById('treeviewarea').innerHTML = tree.toString();
							}
							reLoadDate();
						</script>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
