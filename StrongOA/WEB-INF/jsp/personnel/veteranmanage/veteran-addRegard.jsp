<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<base target="_self">
		<title>老干部慰问信息管理</title>
		<%@include file="/common/include/meta.jsp" %>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>

		<!--右键菜单样式 -->
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
			
		<script>
		
</script>
		<s:head />
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		style="overflow: auto;">
				<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<!-- 增加form    -->
		<DIV id=contentborder align=center>
		<s:form action="/personnel/veteranmanage/veteran!saveRegard.action" id="regardform" name="regardform" method="post" enctype="multipart/form-data">
		<input type="hidden" id="regardId" name="regard.vereId" value="${regard.vereId}">
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>&nbsp;</td>
					<td width="20%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						管理老干部慰问信息
					</td>
					<td width="*">&nbsp;
						
					</td>
				</tr>
			</table>
            <table width="100%">
				<tr>
					<td height="10">	
					  
					</td>
				</tr>
			</table>
			<table align="center" width="100%" border="0" cellpadding="0" 
				cellspacing="1" class="table1">
				
				<tr>
					<td colspan="1" height="21" width="20%" class="biao_bg1" align="right">
						<span class="wz">慰问主题(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left" width="30%">
						<input id="vereTopic" name="regard.vereTopic" type="text" style="width:80%;" value="${regard.vereTopic}">
					</td>
					
					<td colspan="1" height="21" width="20%" class="biao_bg1" align="right">
						<span class="wz">老干部：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left" width="30%">
			<input id="baseVeteran" name="personName" type="text" style="width:80%;" value="${model.personName}" readonly>
			<input type="hidden" id="vpersonId" name="regard.toaBaseVeteran.personId" value="${model.personId}"/>
					</td>
			    	
					
				</tr>
				<tr>
				<td width="20%" colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">慰问日期(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td  width="30%" colspan="1" class="td1">
										 <strong:newdate name="regard.vereTime" id="vereTime"  width="80%"
                      skin="whyGreen" isicon="true" dateobj="${regard.vereTime}" dateform="yyyy-MM-dd"></strong:newdate>
					</td>
					
					
					<td colspan="1" height="21" width="20%" class="biao_bg1" align="right">
						<span class="wz">带队领导：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left" width="30%">
	         <input id="verePersons" name="regard.verePersons" type="text" style="width:80%;" value="${regard.verePersons}">
					</td>
					
				</tr>
				<tr>
				    <td colspan="1" height="21" class="biao_bg1" align="right" width="20%">
						<span class="wz">活动内容：&nbsp;</span><br>
					</td>
					<td colspan="3" class="td1" align="left" width="80%">
						<textarea  id="vereInfo" name="regard.vereInfo" 
							             style="width:92%;height:150px;" >${regard.vereInfo}</textarea>
					</td>										
				</tr>				
			</table>
			<table width="100%">
				<tr>
					<td class="td1" colspan="4" align="center" height="21">
					    <input name="Submit" type="button" class="input_bg" value="保 存" onClick="save();">
					    &nbsp; &nbsp; &nbsp; &nbsp;
						<input name="Submit2" type="button" class="input_bg" value="关 闭"
							onclick="goBack();">
					</td>
				</tr>
			</table>
		</s:form>
		</DIV>
		<SCRIPT type="text/javascript">
			function goBack(){
				window.close();
			}
			
			function save(){  
			
			 var inputDocument=document;
			  if(inputDocument.getElementById("vereTopic").value==""){
    	       alert("慰问主题不能为空，请输入。");
    	      inputDocument.getElementById("vereTopic").focus();
    	          return false;
             } 
             
         if(inputDocument.getElementById("vereTopic").value.length>60){
    	               alert("慰问主题长度过长，请重新输入！");
    	             inputDocument.getElementById("vereTopic").focus();
    	                         return false;
    	                    }
    	  if(inputDocument.getElementById("verePersons").value.length>60){
    	              alert("带队领导长度过长，请重新输入！");
    	             inputDocument.getElementById("verePersons").focus();
    	                         return false;
    	                    }             
    	   if(inputDocument.getElementById("vereInfo").value.length>100){
    	              alert("慰问内容长度过长，请重新输入！");
    	             inputDocument.getElementById("vereInfo").focus();
    	                         return false;
    	                    }
               if(inputDocument.getElementById("vereTime").value==""){
    	       alert("慰问日期不能为空，请选择。");
    	      inputDocument.getElementById("vereTime").focus();
    	          return false;
             }  
   				regardform.submit();
			}
		</SCRIPT>
	</body>
</html>
