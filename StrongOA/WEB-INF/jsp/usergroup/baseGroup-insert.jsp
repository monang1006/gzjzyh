<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>增加用户组</title>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" src="<%=root %>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<base target="_self">
	</head>
	<script type="text/javascript">
	var numtest = /^\d+$/; 
	function testMessage(){
	   if(document.getElementById("groupSyscode").value == ""||document.getElementById("groupSyscode").value==null){
			alert('用户组编码不能为空！！！');
	        	document.getElementById("groupSyscode").focus();
	        	return;
	    }
	    else if(!numtest.test(document.getElementById("groupSyscode").value)){
		    alert('用户组编号必须为数字！！！');
		    document.getElementById("groupSyscode").focus();
		        return false;
	       }
	    else
	    {
	     comparecode(document.getElementById('groupSyscode'),'1');
	    }	    	  
	 }
	function testcode(){
	   if(document.getElementById("groupSyscode").value == ""||document.getElementById("groupSyscode").value==null){
			alert('用户组编码不能为空！！！');
	        	document.getElementById("groupSyscode").focus();
	        	return;
	    }
		else{
	   		comparecode(document.getElementById('groupSyscode'),'0');
	   }      
	}
	
	function comparecode(obj,flag){				
				 $.ajax({
					url:obj.url,
					type:"post",
					data:{orgcode:obj.value, groupId:document.getElementById("groupId").value},
					success:function(message){					
						if(message=="000"){
							alert("编码不唯一！");
							obj.focus();		
						}else if(message=="333"){
							alert("编码不符合规则！");
							obj.focus();		
						}else if(message == "222"){
							alert("该系统编码的父级不存在！");
							obj.focus();
						}else{
							if(flag=='0')
							formsubmit();
							else if(message=="111")
							alert("合法编码。");
						}					
					},
					error:function(message){
						alert("异步出错!");
					}					
				});				
			}
			
		function formsubmit(){
		 var inputDocument=document;
	 
  if(inputDocument.getElementById("groupSyscode").value==""){
    	alert("用户组编号不能为空，请输入。");
    	inputDocument.getElementById("groupSyscode").focus();
    	return false;
    }
    
     if(!numtest.test(inputDocument.getElementById("groupSyscode").value)){
		    alert('用户组编号必须为数字！！！');
		    inputDocument.getElementById("groupSyscode").focus();
		        return false;
	       }
    if(inputDocument.getElementById("groupName").value==""){
    	alert("用户组名称不能为空，请输入。");
    	inputDocument.getElementById("groupName").focus();
    	return false;
    }
    if(inputDocument.getElementById("groupSequence").value==""){
    	alert("用户组排序序号不能为空，请输入。");
    	inputDocument.getElementById("groupSequence").focus();
    	return false;
    }
     var sequence=inputDocument.getElementById("groupSequence").value;
      if(!numtest.test(sequence)){
		       alert('排序必须为数字！！！');
		      inputDocument.getElementById("groupSequence").focus();
		        return;
	        }	        
	     if(sequence.length>10){
	   	   alert('排序序号字符不能超过10位数！！！');
		      inputDocument.getElementById("groupSequence").focus();
		        return;
	   }
    if(inputDocument.getElementById("groupDescription").value.length > 200){
    	alert("用户组描述字数不能大于200!");
    	return false;
    }
    	
    	//若状态是未启用则不需要进行验证
    	if(document.getElementById("groupIsactive").value == "1"){
    	        //验证父级元素是否是被删除状态
	        $.ajax({
					url:"<%=path%>/usergroup/baseGroup!checkFatherIsdel.action",
					type:"post",
					data:{groupSyscode:document.getElementById("groupSyscode").value},
					success:function(message){
						if(message=="true"){
							alert("该用户组的上级用户组已被停用，该用户组无法设置为启用状态！");
							return false;
						}else if(message == "false"){
							document.forms[0].submit();
						}
					},
					error:function(message){
						alert("异步错误!");
					}
				}); 
		}else {
			document.forms[0].submit();
		}			   
}
	</script>
	
	<body class="contentbodymargin" oncontextmenu="return false;" s>
		<DIV id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td class="table_headtd_img">
												<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
											</td>
											<td align="left">
												<strong>用户组管理</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
													<tr>
														<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
														<td class="Operation_input" onclick="testcode();">&nbsp;保&nbsp;存&nbsp;</td>
														<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
								                  		<td width="5"></td>
														<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
														<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
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
						<s:form action="/usergroup/baseGroup!save.action" method="post"
							id="22" theme="simple">
							<input type="hidden" id="groupId" name="groupId"
								value="${model.groupId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;用户组编号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="groupSyscode" name="model.groupSyscode"
											url="<%=path%>/usergroup/baseGroup!compareCode.action"
											type="text" size="22" value="${model.groupSyscode}">
										<a  href="#" class="button" onclick="testMessage()">检测合法性</a>
										<font color="#C0C0C0">编码规则:33333443333</font><br>
										<font color="red">随意修改用户组编号会导致用户组数据无法展现。请使用系统提供默认的编码，如需调整请在管理员指导下修改。</font>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;用户组名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="groupName" name="model.groupName" type="text" maxLength="50"
											size="22" value="${model.groupName}">
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;是否启用：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select id="groupIsactive" name="model.groupIsactive" list="#{'1':'是','0':'否'}"
											listKey="key" listValue="value" style="width:11.6em"/>
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;排序序号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="groupSequence" name="groupSequence" type="text" maxlength="10"
											size="22" style="width:11.6em" value="${model.groupSequence}">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">用户组描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="6" cols="35" id="groupDescription" name="model.groupDescription">${model.groupDescription}</textarea>
									</td>
								</tr>
								<tr>
									<td class="table1_td"></td>
									<td></td>
								</tr>
							</table>
						</s:form>
						<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
						</table>
						<table width="90%" border="0" cellspacing="0" cellpadding="00">
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