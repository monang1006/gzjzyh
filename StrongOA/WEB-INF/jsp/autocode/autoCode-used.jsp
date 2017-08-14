<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<% 
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
		
	%>
<html>
	<head>
		<title>已用文号列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
			var fparent = this.parent.window.document;
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
			function refreshIframe(){
				$("#myTableForm").submit();
			}
			function getMyids(chooseTr){
				var id = getValue();
				//var id = chooseTr.cells[1].value;
				var codename = chooseTr.cells[2].value;
				//alert(id);
				//alert(codename);
				if(id==null || id == ""){
					alert("请选择文号");
					return ;
				}
				if(confirm("您是要回收选中的已使用文号："+codename+"?")){
					$.ajax({
						type:"post",
						dataType:"text",
						url:"<%=root%>/autocode/autoCode!recCode.action",
						data:"id="+id,
						success:function(msg){
							if(msg=="true"){
								alert("文号回收成功！");
								refreshIframe();
							}else{
								alert("更新文号状态出错,请您重新操作");
								return;
							}
						},
						error:function(){
							alert("Ajax调用异常");
						}
					});
				}
			}
			
			function recCode(){
				var id = getValue();
				if(id==null || id == ""){
					alert("请选择文号");
					return ;
				}
				if(confirm("您是要回收选中的已使用文号?")){
					$.ajax({
						type:"post",
						dataType:"text",
						url:"<%=root%>/autocode/autoCode!recCode.action",
						data:"id="+id,
						success:function(msg){
							if(msg=="true"){
								alert("文号回收成功！");
								refreshIframe();
							}else{
								alert("更新文号状态出错,请您重新操作");
								return;
							}
						},
						error:function(){
							alert("Ajax调用异常");
						}
					});
				}
			}
			
	    	$(document).ready(function(){
		        $("#img_sousuo").click(function(){
		        	$("#myTableForm").submit();
		        });
		        $("#selCode").attr("value",fparent.getElementById("tmptxt").value)
	    	});
	    	
		</script>
	</head>
	  
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT()">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<form id="myTableForm" action="<%=path %>/autocode/autoCode!getUsedCode.action" method="post"> 
			<%--<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
			  <tr>
					<td height="30" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td>&nbsp;</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" />&nbsp;
									已使用文号列表
								</td>
								<td width="70%">
									<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
										<tr>
											<td width="*">
												&nbsp;
											</td>
											<td width="100">
												<a class="Operation" href="javascript:recCode();"><img src="<%=root%>/images/ico/fankuihuizhi.gif" width="15" height="15" class="img_s" />回收文号&nbsp;</a>
											</td>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			  --%>
			  
			  <table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									
											<table width="100%" border="0" cellspacing="0" cellpadding="00">
						                       <tr>
							                    <td class="table_headtd_img" >
								                  <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                    </td>
							                    <td align="left">
								                    <strong>已使用文号列表</strong>
							                    </td>
											
											<td width="20%">
											<table border="0" align="center" cellpadding="00" cellspacing="0">
								                <tr>
								                <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	        <td class="Operation_list" onclick="recCode();"><img src="<%=root%>/images/operationbtn/Has_the_list.png"/>&nbsp;回&nbsp;收&nbsp;文&nbsp;号&nbsp;</td>
					                 	        <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		        <td width="5"></td>
								                 	
								               
				                  		            
								                 	<%--<td ><a class="Operation" href="javascript:gotoRemove();"><img src="<%=root%>/images/operationbtn/del.png" width="15" height="15" class="img_s">删除&nbsp;</a></td>
								                 	<td width="5"></td>
								                --%>
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
     				<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="codeId" ondblclick="getMyids(this)" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">    
						<%--<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
							<tr>
								<td width="6%" align="center" class="biao_bg1">
									<img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo" width="15" height="15" style="cursor: hand;" />
								</td>
								<td width="94%" align="center" class="biao_bg1">
									<input name="codeName" value="${codeName}" type="text" style="width: 100%" class="search" title="请您输入编号内容" />
								</td>
								 
								<td width="30%" class="biao_bg1">
									<strong:newdate name="starttime" dateform="yyyy-MM-dd" id="starttime" width="100%" skin="whyGreen" isicon="true"  classtyle="search" dateobj="${starttime}" title="请输入起始日期"/></td>
								</td>
								<td width="30%" class="biao_bg1">
									<strong:newdate name="endtime" dateform="yyyy-MM-dd" id="endtime" width="100%" skin="whyGreen" isicon="true" classtyle="search" dateobj="${endtime}" title="请输入结束日期"/>
								</td>
								<input type="hidden" id="selCode" name="selCode">
						</table>
						--%>
						
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
						<input type="hidden" id="selCode" name="selCode">
							     <tr>
							       <td >
							       		&nbsp;&nbsp;编号内容：&nbsp;<input name="codeName" value="${codeName}" type="text" class="search" title="请您输入编号内容" />
							     
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
							     </tr>
							     
							</table>
						
						<webflex:flexCheckBoxCol caption="选择" property="codeId" showValue="codeInfo" width="8%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
						<webflex:flexTextCol caption="编号内容" property="codeInfo" showValue="codeInfo" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						<webflex:flexDateCol caption="创建时间" property="codeCreatetime" showValue="codeCreatetime" showsize="100" dateFormat="yyyy-MM-dd HH:mm:ss" width="27%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
						<webflex:flexTextCol caption="创建人员" align="center" property="codeUsername" showValue="codeUsername" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						<webflex:flexTextCol caption="编号状态"  align="center" property="codeStatus" showValue="javascript:viewInfo(codeStatus)" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
					</webflex:flexTable>
			      </td>
			  </tr>
			</table>
		</form>
		</DIV>
		<script type="text/javascript">
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
			    registerMenu(sMenu);
			}
		</script>
	</body>
</html>
