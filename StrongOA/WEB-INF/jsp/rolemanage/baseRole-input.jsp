<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>角色管理</title>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
		<script type="text/javascript" src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
	var numtest = /^\d+$/; 
	function testMessage(){
	   if($.trim(document.getElementById("roleSyscode").value) == ""||document.getElementById("roleSyscode").value==null){
			alert('角色编号不能为空。');
	        	document.getElementById("roleSyscode").focus();
	        	return;
	    }
	    else
	    {
	    if(document.getElementById("iscode").value == ""||document.getElementById("iscode").value==null){
	       	 comparecode(document.getElementById('roleSyscode'),'1');	
	       }
	     else{
	       var flag=document.getElementById("iscode").value;
	        if(flag==$.trim(document.getElementById("roleSyscode").value)){
	       		 alert("合法编码。");
	           document.getElementById("roleSyscode").focus();
	      	 }
	       	else{
	         comparecode(document.getElementById('roleSyscode'),'1');
	       }	    	
	    }
	  }
	 }
	function testcode(){
	   if($.trim(document.getElementById("roleSyscode").value) == ""||document.getElementById("roleSyscode").value==null){
			alert('角色编号不能为空。');
	        	document.getElementById("roleSyscode").focus();
	        	return;
	    }
		if(document.getElementById("iscode").value == ""||document.getElementById("iscode").value==null){
	       	 comparecode(document.getElementById('roleSyscode'),'0');
	       }
	     else{
	       var flag=document.getElementById("iscode").value;
	       if(flag==document.getElementById("roleSyscode").value){
	           formsubmit(); 
	       }
	       else{
	         comparecode(document.getElementById('roleSyscode'),'0');
	       }	        
	        }
	}
	function comparecode(obj,flag){				
				 $.ajax({
					url:obj.url,
					type:"post",
					data:{orgcode:obj.value},
					success:function(message){
						if(message!="111"){
							alert("编码不唯一。");
							obj.focus();		
						}
						else{
						if(flag=='0')
						formsubmit();
						else 
						alert("合法编码。");
						}
					},
					error:function(message){
						alert("异步出错。");
					}				
				});			
			}
			
		function formsubmit(){
		 var inputDocument=document;
	 
    if(inputDocument.getElementById("roleSyscode").value==""){
    	alert("角色编号不能为空，请输入。");
    	inputDocument.getElementById("roleSyscode").focus();
    	return false;
    }
    if($.trim(inputDocument.getElementById("roleName").value)==""){
    	alert("角色名称不能为空，请输入。");
    	inputDocument.getElementById("roleName").focus();
    	return false;
    }else if($.trim(inputDocument.getElementById("roleName").value)=="null"){
        alert("角色名称不能null。");
    	inputDocument.getElementById("roleName").focus();
    	return false;
    }
    if(inputDocument.getElementById("roleSequence").value==""){
    	alert("角色排序序号不能为空，请输入。");
    	inputDocument.getElementById("roleSequence").focus();
    	return false;
    }
    var sequence=inputDocument.getElementById("roleSequence").value;
    if(!numtest.test(sequence)){
		 alert('排序号必须为整数');
		 inputDocument.getElementById("roleSequence").focus();
		 return;
	}
	 if(sequence.length>10){
	   	   alert('排序序号不能超过10位数！！！');
		      inputDocument.getElementById("roleSequence").focus();
		        return;
	   }
	if(inputDocument.getElementById("roleDescription").value.length > 200){
		alert("角色描述字数不能大于200!");
		return false;
	}
			document.forms[0].submit();			 
		  //window.returnValue="success";	   
		   //   window.close();			
		}
	</script>
		<base target="_self">
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;" >
		<DIV id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="3" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
												<strong>角色管理</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
									                <tr>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
									                 	<td class="Operation_input" onclick="testcode();">&nbsp;保&nbsp;存&nbsp;</td>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
								                  		<td width="5"></td>
									                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
									                 	<td class="Operation_input1" onclick="cancel();">&nbsp;取&nbsp;消&nbsp;</td>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
								                  		<td width="6"></td>
									                </tr>
									            </table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<s:form action="/rolemanage/baseRole!save.action" method="post"
							id="22" theme="simple">
							<input type="hidden" id="roleId" name="roleId1" value="${model.roleId}">
							<input type="hidden" id="iscode" name="iscode"
								value="${model.roleSyscode}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;角色编号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="roleSyscode" name="model.roleSyscode"
											url="<%=path%>/rolemanage/baseRole!compareCode.action"
											type="text" maxLength="21" size="22"
											value="${model.roleSyscode}">
											<a  href="#" class="button" onclick="testMessage()">检测合法性</a>
										
										&nbsp;
										<font color="#C0C0C0">用户自定义编码规则&nbsp;</font>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;角色名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="roleName" name="model.roleName" type="text"
											maxLength="50" size="22" value="${model.roleName}">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;是否启用：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.roleIsactive" list="#{'1':'是','0':'否'}"
											listKey="key" listValue="value" style="width:11.5em" />
									</td>
								</tr>								
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;排序序号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="roleSequence" name="model.roleSequence" type="text"
											maxlength="10" size="22" value="${model.roleSequence}">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">角色描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="6" cols="50" id="roleDescription"
											name="model.roleDescription">${model.roleDescription}</textarea>
									</td>
								</tr>
								<tr>
									<td class="table1_td"></td>
									<td></td>
								</tr>
							</table>
						</s:form>
						<table id="annex" width="90%" height="10%" border="0"
							cellpadding="0" cellspacing="1" align="center" class="table1">
						</table>
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
<script type="text/javascript">
	function setprivil(){
		window.showModalDialog("userClearance.jsp",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:450px');		
	}
	function cancel(){
		window.close();
	}
</script>
