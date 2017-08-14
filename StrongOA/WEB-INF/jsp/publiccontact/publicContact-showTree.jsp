<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@include file="/common/include/rootPath.jsp" %>
<html>
<head>
<title>公共联系人类别</title>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
<LINK href="<%=frameroot%>/css/treeview.css" type=text/css rel=stylesheet>
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
			var pccId = node.get(0).id;
			parent.publiccontact_content.location="<%=path%>/publiccontact/publicContact!showList.action?typeId="+pccId;
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
									if(confirm("联系组“"+plaintext+"”存有“"+count+"”个联系人，删除此组及其所有联系人，确定？")){
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
function PublicContact(){
	var url = "<%=root%>/publiccontact/publicContact!typeManage.action";
	var a = window.showModalDialog(url,window,'dialogWidth:450pt;dialogHeight:310pt;help:no;status:no;scroll:no');
	window.parent.location.reload();
}
</script>
</head>

<body scroll="auto" oncontextmenu="return false;" style="background-color: #eeeff3;overflow-x:no;overflow-y: auto;"><!-- oncontextmenu="return false;" -->
	<div style="height: 10px;">
	</div>
	<ul class="simpleTree">
		<li class="root" id='1'><label style="cursor: hand;" id="rootSpan"> 公共联系人类型</label>
			<ul id="groupTree">
				<%--<s:iterator value="#request.showList">
					<li id="<s:property value='pccId'/>" title="<s:property value='pccOther'/>">
						<span><s:property value='pccName'/></span>
					</li>
				</s:iterator>--%>
				${showList }
			</ul>
		</li>	
	</ul>
</body>

</html>
