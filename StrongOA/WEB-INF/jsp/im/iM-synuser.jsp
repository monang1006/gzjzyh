<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaAddressGroup"/>
<jsp:directive.page import="com.strongit.oa.common.user.model.Organization"/>
<jsp:directive.page import="com.strongit.oa.im.rtx.RtxOrgBean"/>
<jsp:directive.page import="com.strongit.oa.im.cache.Cache"/>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<title>IM软件-人员列表</title>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
<style>
body
{
	font: normal 12px arial, tahoma, helvetica, sans-serif;
	margin:0;
	padding:5px;
	background-color:#ffffff;
}

#rootSpan{
font-size:14px;
font-family:'宋体';
}

.text{
font-size:14px;
font-family:'宋体';
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
	background: url(<%=path %>/oa/image/mymail/person-leaf.gif) 0 -1px no-repeat #fff;
}
.simpleTree .doc-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/person-leaf-last.gif) 0 -1px no-repeat #fff;
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
.simpleTree .folder-org-close
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/shareexpandable.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-org-close-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/shareexpandable-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-org-open
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/share-collapsable.gif) 0 -2px no-repeat #fff;
}

.simpleTree .folder-org-open-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/share-collapsable-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-org-leaf-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/org-leaf-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-org-leaf
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/folder-org-leaf.gif) 0 -1px no-repeat #fff;
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
		},
		afterDblClick:function(node){
		},
		afterMove:function(destination, source, pos){
		},
		afterAjax:function()
		{
		},
		afterContextMenu:function(node){
			$($('span:first',node)).contextMenu('myMenu2', {
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
		$($('span:first',node)).contextMenu(getDiv($('label:first',node).text()), {

		  bindings: {

			'sendmail': function(t) {
				
			},

			'semdmsg': function(t) {
			},

			'sendphone': function(t) {
			},
			'sendIM':function(t){
			}

		  }

		});
		},
		animate:false
	});
	
	
});
function getDiv(type){
	if(type == "org"){//说明当前节点是组节点
		return 'myMenu1';
	}else{
		return 'myMenu2';//当前节点是人员
	}
}
</script>
</head>

<body oncontextmenu="return false;"><!-- oncontextmenu="return false;" -->

<div class="contextMenu" oncontextmenu="return false;" id="myMenu1" style="width:100%">
	<!--<ul>
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" />  新建</li>
		<li id="delete"><img src="<%=path %>/oa/image/mymail/folder_delete.png" /> 删除</li>
		<li id="edit"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /> 修改</li>
	</ul>-->
</div>
<div class="contextMenu" oncontextmenu="return false;" id="myMenu2" style="width:100%">
	<%--<ul>
		<li id="sendmail"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 发送邮件</li>
		<li id="semdmsg"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 发送消息</li>
		<li id="sendphone"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 发送短信</li>
		<li id="sendIM"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 发送即时消息</li>
	</ul>
--%></div>
	<ul class="simpleTree">
		<li class="root" id='1'><label style="cursor: hand;" id="rootSpan"><%=Cache.getIMName() %>组织结构</label>
			<ul id="groupTree">
				<%
				List<Object[]> list=(List<Object[]>)request.getAttribute("orgList");
				String[] child=(String[])request.getAttribute("hasChild");
				if(list!=null){
					for(int i=0;i<list.size();i++){
						Object[] org =list.get(i);
						String charge=child[i];
						if("1".equals(charge)){//存在子节点
							out.println("<li id="+org[0] +">");	
							out.println("<span>"+org[1]+"</span>");	
							out.println("<label style='display:none;'>org</label>");	
							out.println("<ul class=ajax>");
							out.println("<li id="+org[0]+1 +">{url:"+root+"/im/iM!synajaxtree.action?deptId="+org[0]+"&type=4" +"}</li>");	
							out.println("</ul>");	
							out.println("</li>");	
						}else{
							out.println("<li id="+org[1]+" leafChange='folder-org-leaf'>");		
							out.println("<span>"+org[1]+"</span>");
							out.println("<label style='display:none;'>org</label>");	
							out.println("</li>");
						}
					}	
				}
			%>		
			</ul>
		</li>		
	</ul>
</body>

</html>
