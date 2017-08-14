<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>增加权限</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script type="text/javascript"
			src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript">
			var numtest = /^\d+$/; 
			function testMessage(){
	   if(document.getElementById("privilSyscode").value == ""||document.getElementById("privilSyscode").value==null){
			alert('权限编码不能为空！');
	        	document.getElementById("privilSyscode").focus();
	        	return;
	    }
	    else if(!numtest.test(document.getElementById("privilSyscode").value)){
		    alert('权限编码必须为数字！');
		    document.getElementById("privilSyscode").focus();
		        return false;
	   }
	   else {
	    
	     comparecode(document.getElementById('privilSyscode'),'1');
	    }
	    	  
	 }
	function testcode(){
	   if(document.getElementById("privilSyscode").value == ""||document.getElementById("privilSyscode").value==null){
			alert('权限编码不能为空！');
	        	document.getElementById("privilSyscode").focus();
	        	return;
	    }
		else{
	   comparecode(document.getElementById('privilSyscode'),'0');
	   }      
	}
	function comparecode(obj,flag){	
			
				 $.ajax({
					url:obj.url,
					type:"post",
					data:{orgcode:obj.value, privilId:document.getElementById("privilId").value},
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
	 	
    if(inputDocument.getElementById("privilSyscode").value==""){
    	alert("权限编码不能为空，请输入。");
    	inputDocument.getElementById("privilSyscode").focus();
    	return false;
    }
    if(!numtest.test(inputDocument.getElementById("privilSyscode").value)){
		       alert('权限编码必须为数字！');
		      inputDocument.getElementById("privilSyscode").focus();
		        return;
	      }
   if(inputDocument.getElementById("privilName").value==""){
    	alert("权限名称不能为空，请输入。");
    	inputDocument.getElementById("privilName").focus();
    	return false;
    }
     if(inputDocument.getElementById("privilAttribute").value==""){
    	alert("权限属性不能为空，请输入。");
    	inputDocument.getElementById("privilAttribute").focus();
    	return false;
    }
    
  	if(inputDocument.getElementById("privilAttribute").value.length > 200){
  		alert("权限属性字数不能大于200!");
  		return;
  	}
    
    if(inputDocument.getElementById("privilSequence").value==""){
    	alert("权限排序序号不能为空，请输入。");
    	inputDocument.getElementById("privilSequence").focus();
    	return false;
    }
     var sequence=inputDocument.getElementById("privilSequence").value;
      if(!numtest.test(sequence)){
		       alert('排序必须为数字！');
		      inputDocument.getElementById("privilSequence").focus();
		        return;
	        }
      if(sequence.length!=10){
    	alert("权限排序序号必须为长度为10的数字列，请检查。");
    	inputDocument.getElementById("privilSequence").focus();
    	return false;
    	}	
    	
   	if(inputDocument.getElementById("privilDescription").value.length > 200){
    	alert("权限描述字数不能大于200!");
    	return false;
    }
			
        	//若状态是未启用则不需要进行验证
    	if(document.getElementById("privilIsactive").value == "1"){
    	        //验证父级元素是否是被删除状态
	        $.ajax({
					url:"<%=path%>/optprivilmanage/baseOptPrivil!checkFatherIsdel.action",
					type:"post",
					data:{privilSyscode:document.getElementById("privilSyscode").value},
					success:function(message){
						if(message=="111"){
							alert("该权限的父级权限已被停用，该权限无法设置为启用状态！");
							return false;
						}else if(message == "222"){
							alert("该权限所属系统已被停用，该权限无法设置为启用状态！");
							return false;
						}else if(message == "false"){
							document.forms[0].submit();
						}
					},
					error:function(message){
						alert("异步错误!");
					}
				});
		}else{
			document.forms[0].submit();
		}
}
	</script>

	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="35%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												操作权限
											</td>
											<td width="10%">
												&nbsp;
											</td>
											<td width="50%">
												<table width="100%" border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<td width="*">
															&nbsp;
														</td>
														<td >
															<a class="Operation" href="javascript:addTempFile();">
																<img src="<%=root%>/images/ico/tianjia.gif" width="10"
																	height="10" class="img_s">添加&nbsp;</a>
														</td>
														<td width="5">&nbsp;</td>
														<td >
															<a class="Operation" href="javascript:editTempFile();">
																<img src="<%=root%>/images/ico/bianji.gif" width="10"
																	height="10" class="img_s">修改&nbsp;</a>
														</td>
														<td width="5">&nbsp;</td>
														<td >
															<a class="Operation" href="javascript:delTempFile();">
																<img src="<%=root%>/images/ico/shanchu.gif" width="10"
																	height="10" class="img_s">删除&nbsp;</a>
														</td>
														<%--<td width="5">&nbsp;</td>
														<td width="86"><a class="Operation" href="#">
															<img src="<%=root%>/images/ico/bianji.gif" width="12"
																height="12" class="img_s">
															LDAP同步
														</td>--%>
														
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<s:form action="/optprivilmanage/baseOptPrivil!save.action"
							method="post" id="12" theme="simple">
							<input type="hidden" id="privilId" name="model.privilId"
								value="${model.privilId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">权限编码(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="privilSyscode" name="model.privilSyscode"
											url="<%=path%>/optprivilmanage/baseOptPrivil!compareCode.action"
											type="text" size="22" style="width:11em"
											value="${model.privilSyscode}">
										<input type="button" class="input_bg" value="检测合法性"
											onclick="testMessage();">
										&nbsp;
										<font color="#C0C0C0">编码规则:&nbsp;</font><font color="#FF6600">4444444444</font>
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">权限名称(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="privilName" name="model.privilName" type="text" maxLength="50"
											size="22" style="width:11em" value="${model.privilName}">
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">权限属性(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="4" id="privilAttribute"
											name="model.privilAttribute" style="width:80%">${model.privilAttribute}</textarea>

									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">权限图标：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="4" id="rest1 "
											name="model.rest1 " style="width:80%">${model.rest1}</textarea>
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">权限类型(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.privilType"
											list="#{'1':'模块权限','2':'功能权限'}" listKey="key"
											listValue="value" style="width:11em" />
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否启用(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.privilIsactive"
											list="#{'1':'是','0':'否'}" listKey="key" listValue="value"
											id="privilIsactive" style="width:11em" />

									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">排序序号(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="privilSequence" name="model.privilSequence"
											type="text" size="22" style="width:11em"
											value="${model.privilSequence}">
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">权限描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="3" id="privilDescription"
											name="model.privilDescription" style="width:80%">${model.privilDescription}
</textarea>
									</td>
								</tr>
							</table>
						</s:form>
						<table id="annex" width="90%" height="10%" border="0"
							cellpadding="0" cellspacing="1" align="center" class="table1">
						</table>

						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="100%" align="center" valign="middle">
												<input name="Submit2" type="button" class="input_bg"
													value="保  存" onclick="testcode();">
												&nbsp;
												<input name="Submit2" type="button" class="input_bg"
													value="取  消" onclick="">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">

function addTempFile(){
  var id=document.getElementById("privilSyscode").value;
	location="<%=path%>/optprivilmanage/baseOptPrivil!insert.action?code="+id;
}
function editTempFile(){
	var id=document.getElementById("privilId").value;
	if(id==null || id==""){
	alert("请选择需要修改的权限。");
	return;
	}
	 location="<%=path%>/optprivilmanage/baseOptPrivil!input.action?privilId="+id;
}
function delTempFile(){
   var id=document.getElementById("privilId").value;
	if(id==null || id==""){
	    alert("请选择需要删除的权限。");
	    return;
	}
	
   parent.PrivilTree.location="<%=path%>/optprivilmanage/baseOptPrivil!delete.action?privilId="+id;
	location="<%=path%>/optprivilmanage/baseOptPrivil!input.action";
 
}

</script>
	</body>
</html>
