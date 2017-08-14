<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML>
	<HEAD>
		<TITLE>门户管理</TITLE>
		<%@include file="/common/include/meta.jsp"%>


		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">

		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<!--<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>-->
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			language="javascript"></script>
		<script language='javascript'
			src='<%=root%>/common/js/grid/ChangeWidthTable.js'></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js"
			type="text/javascript"></script>
		<script src="js/work.js" type="text/javascript"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->


		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script type="text/javascript">
   
   	
		 function showimg(topicStatus){

      
			var rv = '' ;
			if(topicStatus == '0'){
				rv = "<font>不可编辑</font>&nbsp&nbsp";
			}
			if(topicStatus == '1'){
				rv = "<font>可编辑</font>&nbsp&nbsp";
			}
			
			
			return rv;
		}
		 function showimg1(topicStatus){
			var rv = '' ;
			if(topicStatus == '0'){
				rv = "<font>否</font>&nbsp&nbsp";
			}
			if(topicStatus == '1'){
				rv = "<font>是</font>&nbsp&nbsp";
			}
			return rv;
		}
		
		function reloadPage() {
				window.location.reload();
			}
</script>

	</HEAD>


	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
								<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								                <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                   </td>
					                            <td align="left">
													<strong>门户列表</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
													 <tr>
													<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                        	<td class="Operation_list" onclick="portalAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
													<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		        <td width="5"></td> 
													<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
								                 	<td class="Operation_list" onclick="portalEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
								                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
							                  		<td width="5"></td>
													<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
								                 	<td class="Operation_list" onclick="portalDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
								                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
							                  		<td width="5"></td>
													<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                        	<td class="Operation_list" onclick="portalSet();"><img src="<%=root%>/images/operationbtn/install.png"/>设置门户桌面</td>
													<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		        <td width="5"></td> 
	                                                <td width="5"></td> 
													<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
								                 	<td class="Operation_list" onclick="createPrival();"><img src="<%=root%>/images/operationbtn/Permissions_settings.png"/>权限设置</td>
								                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
							                  		<td width="5"></td>
										            <td width="2%"></td>
													</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					 </tr>
					 <tr>
					  <td>
                    <s:form theme="simple" id="myTableForm" action="/desktop/desktopPortal.action">
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="portalId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table1_search">
									<tr>
										<td>
							       		&nbsp;&nbsp;门户名称：&nbsp;<input name="portalName" id="portalName" type="text"  class="search" title="请您输入门户名称 " value="${portalName}">
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
									</tr>	
								</table>

								<webflex:flexCheckBoxCol caption="选择" property="portalId"
									showValue="portalName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>

								<webflex:flexEnumCol caption="编辑状态" mapobj="${editMap}"
									property="isEdit" showValue="portalId" width="0"
									isCanDrag="true" isCanSort="true" align="center"></webflex:flexEnumCol>

								<webflex:flexTextCol caption="门户名称" property="portalName"
									showValue="portalName" width="45%" isCanDrag="true"
									isCanSort="true" showsize="15"  align="center"></webflex:flexTextCol>
								<webflex:flexTextCol caption="编辑状态" property="portalId"
									showValue="javascript:showimg(isEdit)" width="15%"
									isCanDrag="true" isCanSort="true"  align="center"></webflex:flexTextCol>
								<webflex:flexTextCol caption="是否为默认门户" property="portalId"
									showValue="javascript:showimg1(isMoren)" width="15%"
									isCanDrag="true" isCanSort="true"  align="center"></webflex:flexTextCol>
								<webflex:flexDateCol caption="更新时间" property="setTime"
									showValue="setTime" dateFormat="yyyy-MM-dd" width="25%"
									isCanDrag="true" isCanSort="true" ></webflex:flexDateCol>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
 
function initMenuT(){
$("input:checkbox").parent().next().hide(); //隐藏第二列
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","portalAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","portalEdit",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","portalDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	
	item = new MenuItem("<%=root%>/images/operationbtn/install.png","设置门户桌面","portalSet",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/operationbtn/Permissions_settings.png","权限设置","createPrival",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item); 
	
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}



function portalAdd(){//增加门户
	var audit= window.showModalDialog("<%=root%>/desktop/desktopPortal!input.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:250px');
}
function portalEdit(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要编辑的记录。');
		return;
	}
	if(id.length >32){
		alert('只可以编辑一条记录。');
		return;
	}
	var audit = window.showModalDialog("<%=path%>/desktop/desktopPortal!input.action?portalId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:250px');
}
function portalDel(){//删除门户
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要删除的记录。');
		return;
	}
	if(confirm("确定要删除吗？")) 
	{ 
	location = '<%=path%>/desktop/desktopPortal!delete.action?portalId='+id;
	} 	
}

function portalSet(){//设置门户内容
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要设置的记录。');
		return;
	}
	if(id.length >32){
		alert('只可以设置一条记录。');
		return;
	}
	
    location = "<%=path%>/desktop/desktopWhole!edited.action?defaultType=2&portalId="+id;
}


		function sub(){
				myTableForm.submit();
			}
	function createPrival(){				
				var id = getValue();
				if(id==null||id==""){
	    			alert("请选择要设置的记录。");
	    			return ;
	    		}else{
	    			if(id.indexOf(",")!=-1){
	    				alert("只可以设置一条记录。");
	    				return ;
	    			}else{
	    				var reValue = OpenWindow("<%=root%>/desktop/protalPrival!selectPerson.action?portalId="+ id, "400", "520", window);
	    				if(reValue=="suc"){
							window.location.reload();
						}
	    			}
	    		}
			}
			
		$(document).ready(function(){
	        $("#img_sousuo").click(function(){
	        	$("form").submit();
	        });     
	    });

</script>


	</BODY>
</HTML>
