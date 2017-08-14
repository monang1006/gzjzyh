<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>验证二维码</title>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link type="text/css" rel="stylesheet"
			href="<%=path%>/common/js/tabpane/css/luna/tab.css" />

		<SCRIPT language="javascript" event="OnReceiveMsg()" for="PDF417Reader">
			//PDF417条码读取中间件响应事件  
 			alert("收到消息");
   			alert(PDF417Reader.GetBarCodeBuff());
   			alert(PDF417Reader.GetBarCodeXml());
		</SCRIPT> 

		<SCRIPT language="javascript">	
			//作用：打开条码侦听
			function OnOpen()
			{
  				//设置正常的用户授权信息，否则将不能正常使用该产品
  				PDF417Reader.CopyRight = "金格公文二维条码中间件[演示版]";

  				//设置并打开条码设备安装端口号
  				PDF417Reader.SetComPortId(1);   //设置扫描设备端口为COM1，控件默认为COM1

  				//打开条码识别设备连接的COM端口及侦听
  				if(PDF417Reader.OpenComPort()){
  					alert("打开条码侦听失败"); 
  				}else{
  					alert("打开条码侦听成功");
  				}
			}

			//作用：关闭条码侦听
			function OnClose()
			{
 				if(PDF417Reader.CloseComPort()){
			 	 	alert("关闭条码侦听失败"); 
				}else{
  					alert("关闭条码侦听成功");
  				}
			}

			//作用：获取条码数据
			function OnGetDate()
			{
   				alert(PDF417Reader.GetBarCodeXml());   //以XML格式获取数据
   				alert(PDF417Reader.GetDispCompany());  //获取<发文单位>元素
			}
			
			//读取数据库数据
			function Getxxx(){
   				$.post("<%=path%>/senddoc/sendDoc!getBar.action",
					{tableName:$("#tableName").val(),pkFieldName:$("#pkFieldName").val(),pkFieldValue:$("#pkFieldValue").val()},
					function(ret){
						var htmlStr = "";
						if(ret == "-1"){
						  	show("对不起，加载数据异常！");
						}else{
							alert(ret);
							htmlStr=htmlStr+"<tr>";
							htmlStr=htmlStr+"<td align='left'>" + ret + "</td>";
							htmlStr=htmlStr+"</tr>";
							$("#showItem").html(htmlStr);
						}
					});
			}
			
		</SCRIPT>
	</head>

	<body>
	<!-- 业务表名称 -->
	  <s:hidden id="tableName" name="tableName"></s:hidden>
	  <!-- 业务表主键名称 -->
	  <s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
	  <!-- 业务表主键值 -->
	  <s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
	  <table width="100%" border="0" cellpadding="0" cellspacing="1">
			<tr>
				<td>
					<h2><font color="red">读取二维码信息</font></h2>
  					<h4><font color="red">扫描前请先确认打开侦听器</font></h4>			
				</td>
				<td>
					<h2><font color="red">条码原数据</font></h2>
				</td>
			</tr>
  		</table>
  		<table width="100%" border="0" cellpadding="0" cellspacing="1">
  			<tr>
  				<td>&nbsp;
  					<OBJECT id="PDF417Reader" 
					codeBase="<%=root%>/common/goldgridOCX/PDF417Reader.ocx#version=6,0,0,0" width="400" height="300" 
					classid="CLSID:AD650675-9B4A-43D2-A8CA-F49B00A9BD92">
					</OBJECT>
  				</td>
  				<td>
  					<table id="showItem">
						
  					</table>
  				</td>
  			</tr>
  		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="1">
			<tr>
				<td>
					<input type=button value="打开条码侦听" onclick="OnOpen()">
					<input type=button value="关闭条码侦听" onclick="OnClose()">
					<input type=button value="获取条码数据" onclick="OnGetDate()">
				</td>
				<td>
					<input type=button value="验证二维码" onclick="Getxxx()">
				</td>
			</tr>
		</table>
  </body>
</html>
