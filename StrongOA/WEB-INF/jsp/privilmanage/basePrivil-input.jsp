<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>编辑资源</title>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>
     	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
		 var numtest = /^\d+$/; 
		function testMessage(){
	      if(document.getElementById("privilSyscode").value == ""||document.getElementById("privilSyscode").value==null){
			alert('资源编码不能为空！！！');
	        	document.getElementById("privilSyscode").focus();
	        	return;
	    }
	    else
	    {
	    if(document.getElementById("iscode").value != ""||document.getElementById("iscode").value!=null){
	       if(!numtest.test(document.getElementById("privilSyscode").value)){
		    alert('资源编码必须为数字！！！');
		    document.getElementById("privilSyscode").focus();
		        return false;
	   }
	       var flag=document.getElementById("iscode").value;
	        if(flag==document.getElementById("privilSyscode").value){
	           alert("合法编码。");
	           document.getElementById("privilSyscode").focus();
	       }
	       else{
	         comparecode(document.getElementById('privilSyscode'),'1');
	       }
	       }
	       }
	 }
	function testcode(){
	   if(document.getElementById("privilSyscode").value == ""||document.getElementById("privilSyscode").value==null){
			alert('资源编码不能为空！！！');
	        	document.getElementById("privilSyscode").focus();
	        	return;
	    }
		if(document.getElementById("iscode").value != ""||document.getElementById("iscode").value!=null){	       	
	       var flag=document.getElementById("iscode").value;
	       if(flag==document.getElementById("privilSyscode").value){
	           formsubmit(); 
	       }
	       else{
	         comparecode(document.getElementById('privilSyscode'),'0');
	       }
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
		var oldIsactive = inputDocument.getElementById("oldIsactive").value;
		var privilIsactive = inputDocument.getElementById("privilIsactive").value;
		if(oldIsactive != null && oldIsactive != "" && oldIsactive != privilIsactive){
			if(privilIsactive == "0"){
				if(!confirm("设定该资源为未启用将导致其下级资源都变为未启用状态，是否继续操作?")){
					return false;
				}
			}else if(privilIsactive == "1"){
				if(confirm("是否同时将该资源的下级资源都设置为启用状态?")){
					document.getElementById("childTogether").value = "1";
				}
			}
		}
	 
     if(inputDocument.getElementById("privilSyscode").value==""){
    	alert("资源编码不能为空，请输入。");
    	inputDocument.getElementById("privilSyscode").focus();
    	return false;
    }
    
    if(!numtest.test(inputDocument.getElementById("privilSyscode").value)){
		    alert('资源编码必须为数字！！！');
		    inputDocument.getElementById("privilSyscode").focus();
		        return false;
	   }
    if(inputDocument.getElementById("privilName").value==""){
    	alert("资源名称不能为空，请输入。");
    	inputDocument.getElementById("privilName").focus();
    	return false;
    }
  	//if(inputDocument.getElementById("privilAttribute").value.length > 200){
  	//	alert("资源属性字数不能大于200!");
  	//	return;
  	//}
    if(inputDocument.getElementById("privilCode").value==""){
  		alert("资源代码不能为空，请输入。");
  		inputDocument.getElementById("privilCode").focus();
  		return;
  	}
  	if(inputDocument.getElementById("privilCode").value.length > 4000){
  		alert("资源代码字数不能大于4000!");
  		return;
  	}
    
     if(inputDocument.getElementById("typeId").value==""){
      alert("请选择所属资源类型");
   	inputDocument.getElementById("typeId").focus();
   	return false;
   }
   if(inputDocument.getElementById("privilSequence").value==""){
    	alert("资源排序序号不能为空，请输入。");
    	inputDocument.getElementById("privilSequence").focus();
    	return false;
    }
     var sequence=inputDocument.getElementById("privilSequence").value;
     if(!numtest.test(sequence)){
		       alert('排序必须为数字！');
		      inputDocument.getElementById("privilSequence").focus();
		        return;
	        }
      if(sequence.length>10){
	   	       alert('排序序号不能超过10位数！！！');
		       document.getElementById("privilSequence").focus();
		        return;
	           }
    if(inputDocument.getElementById("privilDescription").value.length > 200){
    	alert("资源描述字数不能大于200!");
    	return false;
    }
     
        	//若状态是未启用则不需要进行验证
    	if(document.getElementById("privilIsactive").value == "1"){
    	        //验证父级元素是否是被删除状态
	        $.ajax({
					url:"<%=path%>/privilmanage/basePrivil!checkFatherIsdel.action",
					type:"post",
					data:{privilSyscode:document.getElementById("privilSyscode").value},
					success:function(message){
						if(message=="111"){
							alert("该资源的父级资源已被停用，该资源无法设置为启用状态！");
							return false;
						}else if(message == "222"){
							alert("该资源所属系统已被停用，该资源无法设置为启用状态！");
							return false;
						}else if(message == "false"){
							document.getElementById("viewChangeFlag").value = viewChangeFlag;
							document.forms[0].submit();
						}
					},
					error:function(message){
						alert("异步错误!");
					}
				});
		}else{
			document.getElementById("viewChangeFlag").value = viewChangeFlag;
			document.forms[0].submit();
		}
}
		function moveUp(){
			var id=document.getElementById("privilId").value;
			if(id==null || id==""){
			alert("请选择需要移动的资源。");
			return;
			}
			parent.PrivilTree.moveUp1(id);
		}
		
		function moveDown(){
			var id=document.getElementById("privilId").value;
			if(id==null || id==""){
			alert("请选择需要移动的资源。");
			return;
			}
			parent.PrivilTree.moveDown1(id);
		}
	</script>
	<style>
	.Operation_list{
	cursor: pointer;
	height:24px;
	line-height:24px;
	text-align:center;
	text-decoration:none;
	background:url(<%=frameroot%>/images/bt_m.jpg) repeat-x;
}
 input,select,textarea{border:1px solid #b3bcc3;background-color:#ffffff;}
   .table_headtd{background:none;}
	</style>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
												<strong>资源管理</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
													<tr>
														<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="addTempFile();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新建&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="editTempFile();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编辑&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="delTempFile();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删除&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<%--<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="changPrivilTypeView();"><img src="<%=root%>/images/operationbtn/Switching.png"/>&nbsp;切换视图&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		--%><td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="moveUp();"><img src="<%=root%>/images/operationbtn/Move.png"/>&nbsp;上&nbsp;移&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
								                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
									                 	<td class="Operation_list" onclick="moveDown();"><img src="<%=root%>/images/operationbtn/Down.png"/>&nbsp;下&nbsp;移&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
								                  		<td width="5"></td>
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
						</table>
						<s:form id="submitForm"
							action="/privilmanage/basePrivil!save.action" method="post"
							theme="simple">
							<input  type="reset" id="ret" style="display: none;"/>
							<input type="hidden" id="childTogether" name="childTogether"
								value="0" />
							<input type="hidden" id="oldIsactive" name="oldIsactive"
								value="${model.privilIsactive }" />
							<input type="hidden" id="privilId" name="privilId"
								value="${model.privilId}">
							<input type="hidden" id="iscode" name="iscode"
								value="${model.privilSyscode}">
							<input type="hidden" id="viewChangeFlag" name="viewChangeFlag"
								value="${viewChangeFlag}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;资源编码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="privilSyscode" name="model.privilSyscode"
											maxLength="21" type="text" size="22"
											url="<%=path%>/privilmanage/basePrivil!compareCode.action"
											style="width:11em" value="${model.privilSyscode}">
											<a  href="#" class="button" onclick="testMessage()">检测合法性</a>
										
										&nbsp;
										<font color="#999999">编码规则:&nbsp;4444444444</font>
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;资源名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="privilName" name="model.privilName" type="text"
											maxLength="50" size="22" style="width:11em"
											value="${model.privilName}">
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz"><font color="red">*</font>&nbsp;资源代码：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="4" id="privilCode" name="model.privilCode"
											style="width:662px">${model.privilCode}</textarea>
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz"><font color="red">*</font>&nbsp;资源属性：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="4" id="privilAttribute"
											name="model.privilAttribute" style="width:80%">${model.privilAttribute}</textarea>

									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">资源图标：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="2" id="rest1"
											name="model.rest1" style="width:80%">${model.rest1}</textarea>

									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;资源类型：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select list="typeLst" listKey="itemId" listValue="itemName"
											headerKey="" headerValue="请选择资源类型" id="typeId" name="typeId" />
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">外部标识：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="privilOuterCode" name="model.privilOuterCode"
											type="text" size="22" style="width:11em"
											value="${model.privilOuterCode}">
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;功能类别：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.rest4"
											list="#{'0':'模块权限','1':'功能权限'}" listKey="key" listValue="value"
											id="rest4" style="width:11em" />

									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;是否启用：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select name="model.privilIsactive"
											list="#{'1':'是','0':'否'}" listKey="key" listValue="value"
											id="privilIsactive" style="width:11em" />

									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;排序序号：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="privilSequence" name="model.privilSequence"
											type="text" size="22" maxlength="10" style="width:11em"
											value="${model.privilSequence}">
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">资源描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="3" id="privilDescription"
											name="model.privilDescription" style="width:80%">${model.privilDescription}</textarea>
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
							<td colspan="3" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td align="center" valign="middle">
												<table border="0" align="center" cellpadding="00" cellspacing="0">
													<tr>
														<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
									                 	<td class="Operation_input" onclick="testcode();">&nbsp;保&nbsp;存&nbsp;</td>
									                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
								                  		<td width="5"></td>
									                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
									                 	<td class="Operation_input1" onclick="$('#ret').click();">&nbsp;重&nbsp;置&nbsp;</td>
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
					</td>
				</tr>
			</table>

		</DIV>
	</body>
</html>
<script language="javascript">

	function addTempFile(){
	  var id=document.getElementById("privilSyscode").value;
	   var yy=document.getElementById("typeId").value; 
		location="<%=path%>/privilmanage/basePrivil!insert.action?viewChangeFlag=" + viewChangeFlag + "&code="+id+"&typeId="+yy;
	}
	function editTempFile(){
		var id=document.getElementById("privilId").value;
		if(id==null || id==""){
		alert("请选择需要编辑的资源。");
		return;
		}
		 location="<%=path%>/privilmanage/basePrivil!input.action?viewChangeFlag=" + viewChangeFlag + "&privilId="+id;
	}
	function delTempFile(){
	   var id=document.getElementById("privilId").value;
		if(id==null || id==""){
		    alert("请选择需要删除的资源。");
		    return;
		}
		if(confirm("执行此操作将要把他的子节点一块删除，您确定要删除吗？")){
	    parent.PrivilTree.location="<%=path%>/privilmanage/basePrivil!delete.action?viewChangeFlag=" + viewChangeFlag + "&privilId="+id;
		location="<%=path%>/privilmanage/basePrivil!input.action?viewChangeFlag=" + viewChangeFlag;
		}
	}
	
	//将当前视图切换到资源权限类型视图
	function changPrivilTypeView(){
		parent.PrivilTree.location = "<%=path%>/privilmanage/basePrivil!priviltree.action?viewChangeFlag=" + reverseViewChangeFlag;
		var form = document.getElementById("submitForm");
		form.action = "<%=path%>/privilmanage/basePrivil!changeInsertView.action";
		document.getElementById("viewChangeFlag").value = reverseViewChangeFlag;
		form.submit();
	}

	var viewChangeFlag = "${viewChangeFlag}"; 
	var reverseViewChangeFlag;
	//视图标识
	if(viewChangeFlag == "typeTree"){
		reverseViewChangeFlag = "systemTree";
	}else{
		viewChangeFlag = "systemTree";
		reverseViewChangeFlag = "typeTree";
	}
	document.getElementById("viewChangeFlag").value = viewChangeFlag;
</script>

