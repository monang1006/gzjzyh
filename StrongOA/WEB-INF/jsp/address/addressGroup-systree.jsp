<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<jsp:directive.page import="com.strongit.oa.bo.ToaAddressGroup"/>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<title>个人通讯录-联系人列表</title>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
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
.simpleTree .folder-share-leaf-last
{
	margin-left:-16px;
	background: url(<%=path %>/oa/image/mymail/org-leaf-last.gif) 0 -2px no-repeat #fff;
}
.simpleTree .folder-share-leaf
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
		$($('span:first',node)).contextMenu(getDiv($('label:first',node).text()), {

		  bindings: {

			'add': function(t) {
			    var ret=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=address/addressGroup-add.jsp","300","170",window);
				
				if("suc"==ret && ret!=undefined){
					this.location = "<%=root %>/address/addressGroup.action" ;
				}
			},

			'edit': function(t) {
				var id = node.get(0).id;
				var ret=OpenWindow("<%=root%>/address/addressGroup!input.action?id="+id,"300","170",window);
				if("suc"==ret && ret!=undefined){
					this.location = "<%=root %>/address/addressGroup!systree.action" ;
				}
			},

			'delete': function(t) {
				var text = $('span:first',node).text();
				var id = node.get(0).id;
				var count = text.substring(text.indexOf("(")+1,text.indexOf(")"));
				if(confirm("确定要删除分组“"+text+"”吗？")){
						$.ajax({
								type:"post",
								url:"<%=root%>/address/addressGroup!delete.action?id="+id,
								data:{groupId:id},
								success:function(data){
									alert(data);
									this.location = "<%=root %>/address/addressGroup!systree.action" ;
								},
								error:function(){
									alert("对不起，操作异常！请重试或与管理员联系！");
								}
						});
					}
				
			},
			'sendmail': function(t) {
			    $.post(
					"<%=root%>/address/address!getUserDefaultEmail.action",
					function(data){
						if("" == data){
							alert("对不起，您未配置默认邮箱，不能发送邮件！请先配置默认邮箱。");
						}else if(data == "error"){
							alert("对不起，获取默认邮箱出错。请与管理员联系！");
						}else{
							var id = node.get(0).id;
							$.post(
								"<%=root%>/address/address!getUserEmail.action",
								{userId:id,type:"personal"},
								function(ret){
									if("" == ret || "null" == ret){
										alert("提示:您所选择的人员未设置邮箱，无法发送邮件！");
									}else if(ret == "error"){
										alert("对不起，获取用户邮箱出错。请与管理员联系！");
									}else{
										OpenWindow('<%=root%>/mymail/mail!otherModel.action?receiver='+encodeURI(encodeURI(ret)), '700', '400', window);
									}
								}
							);
						}
					}
				);
			},

			'semdmsg': function(t) {
				var id = node.get(0).id;
				
				OpenWindow("<%=root%>/message/message!view.action?forward=write&msgReceiverIds="+id+"&moduleCode=<%=GlobalBaseData.SMSCODE_ADDRESS %>", '700', '400', window);
			},

			'prop': function(t) {
				var id = node.get(0).id;
				var ret=OpenWindow("<%=root%>/address/address!initEdit.action?id="+id,"450","295",window);
				/*if("sucess"==ret && ret!=undefined){
					this.location = "<%=root %>/address/addressGroup.action" ;
				}*/
			}
		  }

		});
		},
		animate:false
	});
	
	
});
function getDiv(type){
	if(type == "group"){//说明当前节点是组节点
		return 'myMenu1';
	}else{
		return 'myMenu2';//当前节点是人员
	}
}
</script>
</head>

<body oncontextmenu="return false;"><!-- oncontextmenu="return false;" -->

<div class="contextMenu" oncontextmenu="return false;" id="myMenu1" style="width:100%">
	<!-- <ul>
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" />  新建</li>
		<li id="delete"><img src="<%=path %>/oa/image/mymail/folder_delete.png" /> 删除</li>
		<li id="edit"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /> 修改</li>
	</ul>-->
</div>
<div class="contextMenu" oncontextmenu="return false;" id="myMenu2" style="width:100%">
	<ul>
		<li id="sendmail"><img src="<%=root%>/images/ico/fasong.gif" width="15" height="15" /> 发送邮件</li>
		<!--<li id="semdmsg"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 发送消息</li>
		<li id="sendphone"><img src="<%=path %>/oa/image/mymail/folder_add.png" /> 发送短信</li>-->
		<li id="prop"><img src="<%=root%>/images/ico/chakan.gif" width="15" height="15" /> 查看属性</li>
	</ul>
</div>
	<ul class="simpleTree">
		<li class="root" id='1'><label style="cursor: hand;" id="rootSpan">个人通讯录</label>
			<ul id="groupTree">
				<%
				List<ToaAddressGroup> list=(List<ToaAddressGroup>)request.getAttribute("groupList");
				String[] child=(String[])request.getAttribute("hasChild");
				for(int i=0;i<list.size();i++){
					ToaAddressGroup group =list.get(i);
					String charge=child[i];
					if("1".equals(charge)){//存在子节点
						out.println("<li id="+group.getAddrGroupId() +">");	
						out.println("<span>"+group.getAddrGroupName()+"</span>");	
						out.println("<label style='display:none;'>group</label>");	
						out.println("<ul class=ajax>");
						out.println("<li class=\"person\" id="+group.getAddrGroupId()+1 +">{url:"+root+"/address/addressGroup!synajaxtree.action?groupId="+group.getAddrGroupId() +"}</li>");	
						out.println("</ul>");	
						out.println("</li>");	
					}else{
						out.println("<li id="+group.getAddrGroupId()+" leafChange='folder-share-leaf'>");		
						out.println("<span>"+group.getAddrGroupName()+"</span>");
						out.println("<label style='display:none;'>group</label>");	
						out.println("</li>");
					}
				}	
			%>		
			</ul>
		</li>		
	</ul>
</body>

</html>
