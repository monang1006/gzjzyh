<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<% 
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
	%>
<HTML>
	<HEAD>
		<TITLE>已用编号列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		
		<script language="javascript">
			function viewInfo(id){
				if(id=="0"){
					return "已使用";
				}else if(id=="1"){
					return "<font color='red'>已预留</font>";
				}else if(id=="2"){
					return "<font color='red'>已回收</font>";
				}else{
					return "";
				}
			}
			function getUsedNum(){
				//var rule = window.showModalDialog("<%=path%>/autocode/autoCode!input.action", window, 'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:600px');
				//window.location.reload();
				var rule=window.showModalDialog("<%=path%>/autocode/autoCode!input.action", window, 'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:250px');
				$("#img_sousuo").click();
			}
			
			function delUsedNum(){
				var id=getValue();
				if(id == ""||id == "null"){
					alert("请选择要删除的文号。");
					return;
				}
				if(!confirm("确定要删除吗？")){
			        return;
			    }
      			$.ajax({
      					type:"POST",
	      				dataType:"text",
	      				url:"<%=path%>/autoencoder/codemanage!delete.action",
	      				data:"ids=" + id,
	      				success:function(msg){
	      					if(msg=="true"){
	      						// 刷新规则列表
 								//window.location.reload();
 								$("#img_sousuo").click();
	      					}else{
	      						alert("删除失败。");
	      					}	      						    				
						}
      			});
			}
	    	$(document).ready(function(){
		        $("#img_sousuo").click(function(){
		        	$("#codeName").val(encodeURI($("#codeNameTEXT").val()));
		        	$("#myTableForm").submit();
		        }); 
	    	});
		</script>
	</HEAD>
	<base target="_self"/>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<form id="myTableForm" action="<%=path%>/autoencoder/codemanage.action" method="get"> 
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
			  <tr>
			  	<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>已用编号列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="getUsedNum();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;增&nbsp;编&nbsp;号&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="delUsedNum();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;编&nbsp;号</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                </tr>
				            </table>
				            </td>
				          </tr>
				        </table>
				        </td>
				    <input type="hidden" name="codeName" id="codeName" value="${codeName}" />
     				<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="logId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">    
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							<tr>
								 <td>
							       		&nbsp;&nbsp;编号名称：&nbsp;<input name="codeNameTEXT" id="codeNameTEXT" type="text" class="search" style=" border:1px solid #b3bcc3;background-color:#ffffff;" title="请您输入标题" value="${codeName }">
							       		&nbsp;&nbsp;起始日期：&nbsp;<strong:newdate  name="starttime" id="starttime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入起始日期" dateform="yyyy-MM-dd" dateobj="${starttime}"/>
							       		&nbsp;&nbsp;结束日期：&nbsp;<strong:newdate  name="endtime" id="endtime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入结束日期" dateform="yyyy-MM-dd" dateobj="${endtime}"/>
							       		&nbsp;&nbsp;编号状态：&nbsp;<s:select name="state" style=" border:1px solid #b3bcc3;background-color:#ffffff;" list="#{'-1':'全部','0':'已使用','1':'已预留','2':'已回收'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
							      <!--  	<td width="5%" align="center" class="biao_bg1">
									<img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo" width="15" height="15" style="cursor: hand;" />
								</td>
								<td width="30%" align="center" class="biao_bg1">
									<input name="codeNameTEXT" id="codeNameTEXT" value="${codeName}" type="text" style="width: 100%" class="search" title="请您输入编号名称" />
								</td>
								<td width="20%" class="biao_bg1">
									<strong:newdate name="starttime" dateform="yyyy-MM-dd" id="starttime" width="100%" skin="whyGreen" isicon="true"  classtyle="search" dateobj="${starttime}" title="请输入起始日期"/></td>
								<td width="20%" class="biao_bg1">
									<strong:newdate name="endtime" dateform="yyyy-MM-dd" id="endtime" width="100%" skin="whyGreen" isicon="true" classtyle="search" dateobj="${endtime}" title="请输入结束日期"/>
								</td>
								<td width="25%" class="biao_bg1">
									<s:select name="state" cssStyle="width:100%" list="#{'-1':'全部','0':'已使用','1':'已预留','2':'已回收'}" listKey="key" listValue="value" />
								</td>-->
							</tr>
						</table>
						<webflex:flexCheckBoxCol caption="选择" property="codeId" showValue="codeInfo" width="3%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
						<webflex:flexTextCol caption="编号名称" property="codeInfo" showValue="codeInfo" width="40%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						<webflex:flexDateCol caption="创建时间" property="codeCreatetime" showValue="codeCreatetime" showsize="100" dateFormat="yyyy-MM-dd HH:mm:ss" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
						<webflex:flexTextCol caption="创建人员" property="codeUsername" showValue="codeUsername" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						<webflex:flexTextCol caption="编号状态" property="codeStatus" showValue="javascript:viewInfo(codeStatus)" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
					</webflex:flexTable>
			  </tr>
			</table>
		</form>
		</DIV>
		<script type="text/javascript">
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/operationbtn/add.png","新增编号","getUsedNum",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除编号","delUsedNum",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
		</script>
	</BODY>
</HTML>
