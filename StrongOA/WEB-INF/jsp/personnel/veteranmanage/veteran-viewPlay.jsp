<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ page import="java.util.*"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
	<base target="_self">
		<%@include file="/common/include/meta.jsp" %>
		<title>老干部基本信息列表</title>		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<!--右键菜单脚本 -->
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
	</HEAD>
	<body class=contentbodymargin oncontextmenu="return false;"
		style="overflow: auto;">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<!-- 增加form    -->
		<DIV id=contentborder align=center>
		<s:form  id="regardForm" action="/personnel/veteran!view.action">
		
		<br>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td>&nbsp;</td>
					<td width="25%">
						<img src="<%=frameroot%>/images/perspective_leftside/ico.gif" width="9" height="9">&nbsp;
						老干部基本信息
					</td>
					<td width="*">&nbsp;
												
					</td>
					<td width="50">
						
					</td>
					<td width="5"></td>
					<td width="50">
						
					</td>
					<td width="5"></td>
					<td width="50" >
						
					</td>
					
					<td width="5"></td>
											
				</tr>
											
				</tr>
			</table>
            <table width="100%">
				<tr>
					<td height="10">	
					  
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
				
							<tr>
								<td colspan="1" height="21" class="biao_bg1" align="right">
									<span class="wz">姓名：&nbsp;</span>
								</td>
								<td colspan="1" class="biao_bg1" align="left">
									${model.personName}
								</td>
								<td colspan="1" height="21" class="biao_bg1" align="right">
									<span class="wz">身份证号：&nbsp;</span>
								</td>
								<td colspan="1" class="biao_bg1" align="left">
									${model.personCardId }
								</td>
							
							</tr>
							<tr>
							 <td  height="21" class="biao_bg1" align="right">
									<span class="wz">性别：&nbsp;</span>
								</td>
								<td   class="biao_bg1" align="left">
									<s:select list="saxList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="性 别" 
							id="personSax" name="model.personSax" style="width:80%" disabled="true"/>
								</td>
								
				    		
								<td width="20%" colspan="1" height="21" class="biao_bg1" align="right">
									<span class="wz">出生日期：&nbsp;</span>
								</td>
								<td  width="30%" colspan="1" class="biao_bg1">
								<!-- 	${model.personBorn} -->
							
						 <strong:newdate name="model.personBorn" id="personBorn"  width="80%"
                      skin="whyGreen" isicon="true" dateobj="${model.personBorn}" dateform="yyyy-MM-dd"></strong:newdate>
                      
								</td>
					
							</tr>
							<tr>
								<td colspan="1" height="21" class="biao_bg1" align="right">
									<span class="wz">工号：&nbsp;</span>
								</td>
								<td colspan="1" class="biao_bg1" align="left">
									${model.personLabourno }
								</td>
								
							
				    			<td colspan="1" height="21" class="biao_bg1" align="right">
									<span class="wz">健康状态：&nbsp;</span>
								</td>
								<td colspan="1" class="biao_bg1" align="left">
								<s:select list="healthList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="" 
							id="personHealthState" name="model.personHealthState" style="width:80%" disabled="true"/>
								</td>
							</tr>
							<tr>
				    			<td colspan="1" height="21" class="biao_bg1" align="right">
									<span class="wz">职位：&nbsp;</span>
								</td>
								<td colspan="1" class="biao_bg1" align="left">
									${model.personPset }
								</td>
								<td width="20%" colspan="1" height="21" class="biao_bg1" align="right">
									<span class="wz">离退日期：&nbsp;</span>
								</td>
								<td  width="30%" colspan="1" class="biao_bg1">
								<!-- ${model.personRetireTime}  -->
								 
											<strong:newdate name="model.personRetireTime" id="personRetireTime"  width="80%"
                      skin="whyGreen" isicon="true" dateobj="${model.personRetireTime}" dateform="yyyy-MM-dd"></strong:newdate>
                    
								</td>
					
							</tr>
							<tr>				   
				   				<td  height="21" class="biao_bg1" align="right">
									<span class="wz">民族：&nbsp;</span>
								</td>
								<td  width="30%" class="biao_bg1" align="left">
									<s:select list="nationList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="请选择民族"  
							id="personNation" name="model.personNation" style="width:80%" disabled="true"/>
								</td>
								
									<td  height="21" class="biao_bg1" align="right">
									<span class="wz">待遇级别：&nbsp;</span>
								</td>
								<td   class="biao_bg1" align="left">
								<s:select list="levelList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue=""  
							id="personTreatmentLevel" name="model.personTreatmentLevel" style="width:80%" disabled="true"/>
								</td>
								
							</tr>				
            				<tr>
            				<td  height="21" class="biao_bg1" align="right">
									<span class="wz">籍贯：&nbsp;</span>
								</td>
								<td  width="30%" class="biao_bg1" align="left">
									${model.personNativeplace }
								</td>					
				    			
			    				<td  height="21" class="biao_bg1" align="right">
									<span class="wz">原部门：&nbsp;</span>
								</td>
								<td   class="biao_bg1" align="left">
									${model.baseOrg.orgName}
								</td>
							</tr>				
						</table>											
		</td>
	</tr>
</table>
    <table width="100%">
			<tr>
			<td>&nbsp;</td>
			</tr>
			<tr>
					<td class="td1" colspan="4" align="center" height="21">
					   
						<input name="Submit2" type="button" class="input_bg" value="关 闭"
							onclick="window.close();">
					</td>
				</tr>
			</table>
</s:form>
</DIV>
	</body>
</html>
