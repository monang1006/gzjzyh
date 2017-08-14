<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<jsp:directive.page import="com.strongit.oa.bo.ToaView"/>
<jsp:directive.page import="com.strongit.oa.bo.ToaGroup"/>
<jsp:directive.page import="com.strongit.oa.bo.ToaBigAntUser"/>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<title>系统通讯录-人员列表</title>
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
				$.post(
					"<%=root%>/address/address!getUserDefaultEmail.action",
					function(data){
						if("" == data){
							alert("对不起，您未配置默认邮箱，不能发送邮件。请先配置默认邮箱！");
						}else if(data == "error"){
							alert("对不起，获取默认邮箱出错。请与管理员联系！");
						}else{
							var id = node.get(0).id;
							$.post(
								"<%=root%>/address/address!getUserEmail.action",
								{userId:id,type:"public"},
								function(ret){
									if("null" == ret || ""==ret){
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

			'sendphone': function(t) {
				var id = node.get(0).id;
				var ret = OpenWindow("<%=path%>/sms/sms!input.action?recvUserIds="+id, '450', '330', window);
				if("reload" == ret){
					alert("短信已提交服务器！");
				}
			},
			'sendIM':function(t){
				var id = node.attr("id");
				if(id == '${currentUserId}'){
					alert("不能对自己发送即时消息！");
					return ;
				}
				var ret=OpenWindow("<%=root%>/im/iM!input.action?receiveId="+id,"450","295",window);
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

<body ><!-- oncontextmenu="return false;" -->

<div class="contextMenu" oncontextmenu="return false;" id="myMenu1" style="width:100%">
	<!--<ul>
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" />  新建</li>
		<li id="delete"><img src="<%=path %>/oa/image/mymail/folder_delete.png" /> 删除</li>
		<li id="edit"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /> 修改</li>
	</ul>-->
</div>
	<ul class="simpleTree">
		<li class="root" id='100000000000000000'><label style="cursor: hand;" id="rootSpan">系统通讯录</label>
			<ul id="groupTree">
				<%
				List<ToaView> list = (List<ToaView>)request.getAttribute("orgList");
				String[] child=(String[])request.getAttribute("hasChild");
				for(int i=0;i<list.size();i++){
					ToaView org =list.get(i);
					String charge=child[i];
					if("1".equals(charge)){//存在子节点
						out.println("<li id="+org.getCol_ID()+""+new Date().getTime() +">");	
						out.println("<span>"+org.getCol_Name()+"</span>");	
						out.println("<label style='display:none;'>org</label>");	
						out.println("<ul class=ajax>");
						out.println("<li id="+org.getCol_ID()+""+new Date().getTime()+">{url:"+root+"/bigant/bigant!bigantAjaxTree.action?Col_HsItemID="+org.getCol_ID()+"&Col_HsItemType=4"+"}</li>");	
						out.println("</ul>");	
						out.println("</li>");	
					}else{
						out.println("<li id="+org.getCol_ID()+""+new Date().getTime()+" leafChange='folder-org-leaf'>");	//  picChange='folder-org'	
						out.println("<span>"+org.getCol_Name()+"</span>");
						out.println("<label style='display:none;'>org</label>");	
						out.println("</li>");
					}
				}	
			%>		
			</ul>
		</li>		
	</ul>
</body>

</html>
