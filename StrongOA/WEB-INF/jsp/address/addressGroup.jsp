<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<title>个人通讯录-分组</title>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="<%=path %>/oa/js/mymail/jquery.js"></script>
<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.contextmenu.r2.js"></script>
<script type="text/javascript" src="<%=root %>/oa/js/mymail/jquery.simple.tree.js"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<style type="text/css">
body{
margin: 0px; padding: 0px; 
}

</style>
<script type="text/javascript">
var simpleTreeCollection;
$(document).ready(function(){
	simpleTreeCollection = $('.simpleTree').simpleTree({
		drag:false,
		autoclose: false,
		afterClick:function(node){
			var groupId = node.get(0).id;
			parent.project_work_content.document.location="<%=root %>/address/address.action?groupId="+groupId;
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
			    var ret=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=address/addressGroup-add.jsp","400","170",window);
				
				if("suc"==ret && ret!=undefined){
					parent.project_work_tree.location="<%=root %>/address/addressGroup.action";
				}
			},

			'edit': function(t) {
				var text = node.text();
				var id = node.get(0).id;
				var plaintext = text.substring(0,text.indexOf("("));
				$.post(
					"<%=root%>/address/addressGroup!checkIsSysGroup.action",
					{groupName:encodeURI(plaintext)},
					function(data){
						if("error" == data){
							alert("对不起，出错了。请与管理员联系。");
						}else if("yes" == data){
							alert("联系组“"+plaintext+"”是系统为您分配的，不能重命名。");
						}else{
							var ret=OpenWindow("<%=root%>/address/addressGroup!input.action?id="+id,"400","170",window);
							if("suc"==ret && ret!=undefined){
								parent.project_work_tree.location="<%=root %>/address/addressGroup.action";
							}
						}	
					}
				);
				
			},

			'delete': function(t) {
				var text = node.text();
				var id = node.get(0).id;
				var count = text.substring(text.indexOf("(")+1,text.indexOf(")"));
				var plaintext = text.substring(0,text.indexOf("("));
				$.post(
					"<%=root%>/address/addressGroup!checkIsSysGroup.action",
					{groupName:encodeURI(plaintext)},
					function(data){
						if("error" == data){
							alert("对不起，出错了。请与管理员联系。");
						}else if("yes" == data){
							alert("联系组“"+plaintext+"”是系统为您分配的，不能删除。");
						}else if("no" == data){
							if(confirm("删除联系组“"+plaintext+"”，确定？")){
								if(count>0){
									if(confirm("联系组“"+plaintext+"”存有"+count+"个联系人，删除此组及其所有联系人，确定？")){
										$.ajax({
											type:"post",
											url:"<%=root%>/address/addressGroup!delete.action?id="+id,
											data:{groupId:id},
											success:function(data){
												parent.document.location="<%=root%>/fileNameRedirectAction.action?toPage=address/address-personal.jsp" ;
											}
										});
									}
								}else{
									$.ajax({
											type:"post",
											url:"<%=root%>/address/addressGroup!delete.action?id="+id,
											data:{groupId:id},
											success:function(data){
												parent.document.location="<%=root%>/fileNameRedirectAction.action?toPage=address/address-personal.jsp" ;
											}
									});
								}
							}
						
						}
					}
				);
				
				
				
				
				
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
		var ret=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=address/addressGroup-add.jsp","400","170",window);
		if("suc"==ret && ret!=undefined){
			parent.project_work_tree.location="<%=root %>/address/addressGroup.action";
		}
	}
	//使组内显示的人员数显示为蓝色
	/*$("li>span").each(function(){
		var txt = $(this).text();
		var number = txt.substring(txt.indexOf("(")+1,txt.lastIndexOf(")"));
		var plainTxt = txt.substring(0,txt.indexOf("("));
		$(this).html(plainTxt+"(<font color='blue'>"+number+"</font>)");
	});*/
	
});
</script>
</head>

<body scroll="auto" oncontextmenu="return false;" style="background-color: #eeeff3;"><!-- oncontextmenu="return false;" -->
<div >
			<div>
				</br>
			</div>
<!--<div id=contentborder style="overflow: auto;background-color: white; border-color: blue;height: 100%;">-->
	<ul class="simpleTree">
		<li class="root" id='1'><label style="cursor: hand;" id="rootSpan"> 个人通讯录</label>
			<ul id="groupTree">
				${group }
				<%--<s:iterator value="groupList">
					<li id="<s:property value='addrGroupId'/>">
						<span><s:property value='addrGroupName'/></span>
					</li>
				</s:iterator>
			--%></ul>
		</li>		
	</ul>

<div class="contextMenu" oncontextmenu="return false;" id="myMenu1" >
	<ul >
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" /><SPAN class="wz">&nbsp;新建联系组名</SPAN> </li>
		<li id="edit"><img src="<%=path %>/oa/image/mymail/folder_edit.png" /><SPAN class="wz">&nbsp;联系组重命名</SPAN> </li>
		<li id="delete"><img src="<%=path %>/oa/image/mymail/folder_delete.png" /><SPAN class="wz"> 删除联系组</SPAN></li>
	</ul>
</div>
<div class="contextMenu" oncontextmenu="return false;" id="myMenu2" style="width:100%">
	<ul class="wz">
		<li id="add"><img src="<%=path %>/oa/image/mymail/folder_add.png" /><SPAN class="wz"> 新建联系组</SPAN></li>
	</ul>
</div>
</div>
</body>

</html>
