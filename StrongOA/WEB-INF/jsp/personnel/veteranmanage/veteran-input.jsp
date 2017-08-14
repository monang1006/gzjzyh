<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
	<base target="_self">
		<%@include file="/common/include/meta.jsp" %>
		<title>添加老干部基本信息</title>
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
		function checkCommpany(){
		   var result=window.showModalDialog("<%=path%>/personnel/veteranmanage/veteran!tree.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
		}
		
</script>
		<s:head />
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		style="overflow: auto;">
				<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<!-- 增加form    -->
		<DIV id=contentborder align=center>
		<s:form action="/personnel/veteranmanage/veteran!save.action" id="veteranform" name="veteranform" method="post" enctype="multipart/form-data">
		<input type="hidden" id="personId" name="model.personId" value="${model.personId}">
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
				    
					<td>&nbsp;</td>
					<td width="50%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						编辑老干部基本信息
					</td>
					<td width="50%">&nbsp;
						
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
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">姓名(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
						<input id="personName" name="model.personName" type="text" style="width:80%;" value="${model.personName}">
					</td>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">身份证号(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
					<input id="personCardId"  name="model.personCardId" value="${model.personCardId}" style="width:80%;" class="required number">
					</td>
			    	
					
				</tr>
				<tr>
				<td  height="21" class="biao_bg1" align="right">
						<span class="wz">性别(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td   class="td1" align="left">
				<!--  	<s:select name="model.personSax" list="#{'':'性别','男':'男','女':'女'}" listKey="key" listValue="value"  style="width:80%"/>-->
					
					<s:select list="saxList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="性 别" 
							id="personSax" name="model.personSax" style="width:80%"/>
					</td>
				   
					<td width="20%" colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">出生日期(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td  width="30%" colspan="1" class="td1">
				 <strong:newdate name="model.personBorn" id="personBorn"  width="80%"
                      skin="whyGreen" isicon="true" dateobj="${model.personBorn}" dateform="yyyy-MM-dd"></strong:newdate>
					</td>
					
				</tr>
				<tr>
						
					 <td colspan="1" height="21" class="biao_bg1" align="right" >
						<span class="wz">工号：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left" id="tt1">
						<input id="personLabourno"  name="model.personLabourno" value="${model.personLabourno}"  type="text" style="width:80%;">
						
					</td>
				    <td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">健康状态(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
	<s:select list="healthList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="健康状况" 
							id="personHealthState" name="model.personHealthState" style="width:80%"/>
					</td>			    	
				</tr>
				<tr>
				    <td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">职位：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
						<input id="personPset"  name="model.personPset" value="${model.personPset}"  type="text" style="width:80%;">
					</td>
					<td width="20%" colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">离退日期(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td  width="30%" colspan="1" class="td1">
			<strong:newdate name="model.personRetireTime" id="personRetireTime"  width="80%"
                      skin="whyGreen" isicon="true" dateobj="${model.personRetireTime}" dateform="yyyy-MM-dd"></strong:newdate>
					</td>
					
				</tr>
				<tr>
				   
				   <td  height="21" class="biao_bg1" align="right">
						<span class="wz">民族(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td  width="30%" class="td1" align="left">
					<s:select list="nationList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="请选择民族"  
							id="personNation" name="model.personNation" style="width:80%"/>

					</td>
				<td  height="21" class="biao_bg1" align="right">
						<span class="wz">待遇级别(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td   class="td1" align="left">
<s:select list="levelList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="请选择待遇级别"  
							id="personTreatmentLevel" name="model.personTreatmentLevel" style="width:80%"/>
					</td>	 
					
				</tr>
				
				<tr>
				<td  height="21" class="biao_bg1" align="right">
						<span class="wz">籍贯：&nbsp;</span>
					</td>
					<td  width="30%" class="td1" align="left">
					<input id="personNativeplace"  name="model.personNativeplace" value="${model.personNativeplace}" style="width:80%;" class="required number">
					</td>
				   
			    	<td  height="21" class="biao_bg1" align="right">
						<span class="wz">原部门(<font color="#FF6600">*</font>)：&nbsp;</span>
					</td>
					<td   class="td1" align="left">
					<input id="orgName" name="orgName" value="${model.baseOrg.orgName}" style="width:70%;">
					<input type="hidden" id="baseOrg" name="model.baseOrg.orgid" value="${model.baseOrg.orgid}">
						<input type="button" class="input_bg" value="选择"
											onclick="checkCommpany();">
					</td>
				</tr>
								
			</table>
			<table width="100%">
			<tr>
			<td>&nbsp;</td>
			</tr>
			<tr>
					<td class="td1" colspan="4" align="center" height="21">
					    <input name="bmit" type="button" class="input_bg" value="保  存" onclick="save();">
					    &nbsp; &nbsp; &nbsp; 
						<input name="Submit2" type="button" class="input_bg" value="关 闭"
							onclick="window.close();">
					</td>
				</tr>
			</table>
		</s:form>
		</DIV>
		<SCRIPT type="text/javascript">
//函数名：fucCheckLength
//功能介绍：检查字符串的长度
//参数说明：要检查的字符串
//返回值：长度值
function fucCheckLength(strTemp)
{
 var i,sum;
 sum=0;
 for(i=0;i<strTemp.length;i++)
 {
  if ((strTemp.charCodeAt(i)>=0) && (strTemp.charCodeAt(i)<=255))
   sum=sum+1;
  else
   sum=sum+2;
 }
 return sum;
}

			function save(){
			 var inputDocument=document;
			  if(inputDocument.getElementById("personName").value==""){
    	              alert("姓名不能为空，请输入。");
    	            inputDocument.getElementById("personName").focus();
    	                return false;           
              } 
              if(inputDocument.getElementById("personName").value.length>25){
    	                alert("姓名长度过长，请重新输入！");
    	             inputDocument.getElementById("personName").focus();
    	                         return false;
    	                       }
    	     if(inputDocument.getElementById("personLabourno").value.length>10){
    	               alert("人员工号长度过长，请重新输入！");
    	             inputDocument.getElementById("personLabourno").focus();
    	                         return false;
    	                    }
    	                    
    	     if(inputDocument.getElementById("personPset").value.length>25){
    	               alert("职位长度过长，请重新输入！");
    	             inputDocument.getElementById("personPset").focus();
    	                         return false;
    	                    }
    	                    
 
             if(inputDocument.getElementById("personCardId").value==""){
    	       alert("身份证号码不能为空，请输入。");
    	      inputDocument.getElementById("personCardId").focus();
    	          return false;
             } 
             var card=inputDocument.getElementById("personCardId").value;
             if(fucCheckLength(card)>18){
             alert("身份证号码不符合！");
             return false;
             }
               if(inputDocument.getElementById("personSax").value==""){
    	       alert("性别不能为空，请选择。");
    	      inputDocument.getElementById("personSax").focus();
    	          return false;
             } 
             if(inputDocument.getElementById("personBorn").value==""){
    	       alert("出生日期不能为空，请选择。");
    	      inputDocument.getElementById("personBorn").focus();
    	          return false;
             } 
             if(inputDocument.getElementById("personHealthState").value==""){
    	       alert("健康状况不能为空，请选择。");
    	      inputDocument.getElementById("personHealthState").focus();
    	          return false;
             }    
              if(inputDocument.getElementById("personRetireTime").value==""){
    	       alert("离退日期不能为空，请输入。");
    	      inputDocument.getElementById("personRetireTime").focus();
    	          return false;
             } 
               if(inputDocument.getElementById("personNation").value==""){
    	       alert("民族不能为空，请选择。");
    	      inputDocument.getElementById("personNation").focus();
    	          return false;
             } 
             if(inputDocument.getElementById("personTreatmentLevel").value==""){
    	       alert("待遇级别不能为空，请选择。");
    	      inputDocument.getElementById("personTreatmentLevel").focus();
    	          return false;
             }        
            
              if(inputDocument.getElementById("orgName").value==""){
    	       alert("原部门不能为空，请选择。");
    	      inputDocument.getElementById("orgName").focus();
    	          return false;
             } 
   				veteranform.submit();
			}
		</SCRIPT>
	</body>
</html>
