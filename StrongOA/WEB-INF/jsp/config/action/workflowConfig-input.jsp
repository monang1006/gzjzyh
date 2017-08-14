<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@	include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>
			<s:if test="model.asId==null">
				新建人员选择配置信息
			</s:if>
			<s:else>
				编辑人员选择配置信息
			</s:else>
		</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<base target="_self" />
		<script type="text/javascript">
			//拦截特殊字符  
			function CheckCode(t,tp) {  
			    var Error = "";  
			    var re = /^[a-z\d\u4E00-\u9FA5]+$/i;  
			    if(tp=="1"){
			    	re=/^[a-z\d\?\/\.\=\!]+$/i;
			    }
			    if (!re.test(t)) {  
			        Error = "中含有特殊字符，拒绝输入。";  
			    }  
			    return Error;  
			}
		   var numtest = /^\d+$/; 
		   function submitForm(){
		   	  var asName = $.trim($("#asName").val());
		   	  var asAlias = $.trim($("#asAlias").val());
		   	  var oldAsAlias = $.trim($("#oldAsAlias").val());
		   	  var asActionUrl = $.trim($("#asActionUrl").val());
		   	  var asHandlerClass = $.trim($("#asHandlerClass").val());
		   	  var asSequence = $.trim($("#asSequence").val());
		   	  var asPrefix = $.trim($("#asPrefix").val());
		   	  var oldAsPrefix = $.trim($("#oldAsPrefix").val());
		   	  if(asName == null || asName == ""){
		   	  	alert("配置名称不能为空。");
		   	  	$("#asName").focus();
		   	  	return false;
		   	  }
		   	  if(asName.length > 25){
		   	  	alert("配置名称长度不能大于25。");
		   	  	$("#asName").focus();
		   	  	return false;
		   	  }
		   	  if(asAlias == null || asAlias == ""){
		   	  	alert("配置别名不能为空。");
		   	  	$("#asAlias").focus();
		   	  	return false;
		   	  }
		   	  if(asAlias.length > 25){
		   	  	alert("配置别名长度不能大于25。");
		   	  	$("#asAlias").focus();
		   	  	return false;
		   	  }
		   	  if((oldAsAlias == null || oldAsAlias == "") || oldAsAlias != asAlias){
		   	  	var sameAlias = checkAlias(asAlias);
		   	  	if(sameAlias){
			   	  	alert("配置别名[" + asAlias + "]已存在。");
			   	  	$("#asAlias").focus();
			   	  	return false;
		   	  	}
		   	  }
			  if(asPrefix == null || asPrefix == ""){
		   	  	alert("配置前缀不能为空。");
		   	  	$("#asPrefix").focus();
		   	  	return false;
		   	  }
		   	  /**
		   	  	确保前缀不能包含中文
		   	  */
		   	  var reg=/[\u4e00-\u9fa5]+/;
		   	  if(reg.test(asPrefix)){
		   	  	 alert("配置前缀不能包含中文!");
		   	  	 return;
		   	  }
		   	  if((oldAsPrefix == null || oldAsPrefix == "") || oldAsPrefix != asPrefix){
		   	  	var samePrefix = checkPrefix(asPrefix);
		   	  	if(samePrefix){
			   	  	alert("配置前缀[" + asPrefix + "]已存在。");
			   	  	$("#asPrefix").focus();
			   	  	return false;
		   	  	}
		   	  }
		   	  if(asPrefix.length > 10){
		   	  	alert("配置前缀长度不能大于25。");
		   	  	$("#asPrefix").focus();
		   	  	return false;
		   	  }
		   	  if(asActionUrl == null || asActionUrl == ""){
		   	  	alert("展现URL不能为空。");
		   	  	$("#asActionUrl").focus();
		   	  	return false;
		   	  }
			  if(asActionUrl.length > 100){
		   	  	alert("展现URL长度不能大于100。");
		   	  	$("#asActionUrl").focus();
		   	  	return false;
		   	  }	
		   	  if(asHandlerClass == null || asHandlerClass == ""){
		   	  	alert("配置解析类不能为空。");
		   	  	$("#asHandlerClass").focus();
		   	  	return false;
		   	  }
		   	  if(asHandlerClass.length > 100){
		   	  	alert("配置解析类长度不能大于100。");
		   	  	$("#asHandlerClass").focus();
		   	  	return false;
		   	  }
		   	  if(asSequence != null && asSequence != "" && asSequence.length > 10){
		   	  	alert("配置排序号长度不能大于10。");
		   	  	$("#asSequence").focus();
		   	  	return false;
		   	  }
		   	  if(asSequence != null && asSequence != "" && !numtest.test(asSequence)){
		   	  	alert("配置排序号必须为数字。");
		   	  	$("#asSequence").focus();
		   	  	return false;
		   	  }
		   	  var isPass=true;
		   	  $("input:text").each(function(){
		   	  		var symbolName=$(this).attr("name");
		   	  		var tipName="";
		   	  		var rtn=CheckCode($(this).val());
		   	  		if(symbolName=="model.asActionUrl"){
		   	  			rtn=CheckCode($(this).val(),"1");
		   	  			tipName="展现URL";
		   	  		}
		   	  		if(rtn!=""){
		   	  			if(symbolName=="model.asName"){
		   	  				tipName="配置名称";
		   	  			}else if(symbolName=="model.asAlias"){
		   	  				tipName="配置别名";
		   	  			}else if(symbolName=="model.asPrefix"){
		   	  				tipName="配置前缀";
		   	  			}else if(symbolName=="model.asHandlerClass"){
		   	  				tipName="配置解析类";
		   	  			}
		   	  			if(tipName!=""){
			   	  			alert(tipName+rtn);
			   	  			isPass=false;
			   	  			return false;
		   	  			}
		   	  		}
		   	  });
		   	  if(isPass){
			     $.ajax( {
							type : "post",
							dataType : "text",
							url : "<%=root%>/config/action/workflowConfig!isExistHandlerClass.action",
							data : "model.asHandlerClass=" + asHandlerClass,
							success : function(msg) {
								if (msg == "false") {
								 alert("配置解析类不存在，不能保存。");
								   return;
								} else {
								  document.forms[0].submit();
								}
					}
						});
		   	  }
		   }
	  
		   function cancel(){
			 window.close();
		   }
		   
		   function checkAlias(asAlias){
		   		 var sameAlias = false;
				 $.ajax({
					url: "<%=root%>/config/action/workflowConfig!checkSameAlias.action",
					type:"post",
					async: false,
					data:{asAlias:asAlias},
					success:function(message){
						if(message == "true"){
							sameAlias = true;
						}
					},
					error:function(message){
						alert("异步出错!");
					}
				});
				return sameAlias;
			}
			
			function checkPrefix(asPrefix){
		   		 var samePrefix = false;
				 $.ajax({
					url: "<%=root%>/config/action/workflowConfig!checkSamePrefix.action",
					type:"post",
					async: false,
					data:{asPrefix:asPrefix},
					success:function(message){
						if(message == "true"){
							samePrefix = true;
						}
					},
					error:function(message){
						alert("异步出错!");
					}
				});
				return samePrefix;
			}	
</script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;">
		<div id="contentborder" align="center">
			<form id="meetform" action="<%=path%>/config/action/workflowConfig!save.action" method="POST">
				<input type="hidden" id="asId" name="asId" value="${model.asId }" />
				<input type="hidden" id="oldAsAlias" name="oldAsAlias" value="${model.asAlias }" />
				<input type="hidden" id="oldAsPrefix" name="oldAsPrefix" value="${model.asPrefix }" />
				<input type="hidden" id="asFlag" name="model.asFlag" value="${model.asFlag }" />
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td class="table_headtd_img" >
										<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
									</td>
									<td align="left">
										<strong>人员选择配置</strong>
									</td>
									<td align="right">
										<table border="0" align="right" cellpadding="00" cellspacing="0">
							                <tr>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="submitForm();">&nbsp;保&nbsp;存&nbsp;</td>
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
				<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
					<tr>
						<td width="35%" height="21" class="biao_bg1" align="right">
							<span class="wz"><font color='red'>*</font>&nbsp;配置名称：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="asName" name="model.asName" type="text" maxLength="50"
								size="30" value="${model.asName }">
						</td>
					</tr>
					<tr>
						<td width="25%" height="21" class="biao_bg1" align="right">
							<span class="wz"><font color='red'>*</font>&nbsp;配置别名：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="asAlias" name="model.asAlias" type="text" maxLength="50"
								size="30" value="${model.asAlias }">
						</td>
					</tr>
					<tr>
						<td width="25%" height="21" class="biao_bg1" align="right">
							<span class="wz"><font color='red'>*</font>&nbsp;配置前缀：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="asPrefix" name="model.asPrefix" type="text" maxLength="50"
								size="30" value="${model.asPrefix }">
						</td>
					</tr>					
					<tr>
						<td width="25%" height="21" class="biao_bg1" align="right">
							<span class="wz"><font color='red'>*</font>&nbsp;是否启用：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:select name="model.asIsActive" list="#{'1':'启用','0':'停用'}"
											listKey="key" listValue="value" />
						</td>
					</tr>
					<tr>
						<td width="25%" height="21" class="biao_bg1" align="right">
							<span class="wz"><font color='red'>*</font>&nbsp;展现URL：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="asActionUrl" name="model.asActionUrl" type="text" style="width: 300"
								 value="${model.asActionUrl }">
						</td>
					</tr>
					<tr>
						<td width="25%" height="21" class="biao_bg1" align="right">
							<span class="wz"><font color='red'>*</font>&nbsp;配置解析类：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="asHandlerClass" name="model.asHandlerClass" type="text" style="width: 208"
								value="${model.asHandlerClass }">
						</td>
					</tr>
					<tr>
						<td width="25%" height="21" class="biao_bg1" align="right">
							<span class="wz">配置排序号：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="asSequence" name="model.asSequence" type="text"
								value="${model.asSequence }" maxLength="50" size="24" >
							<font color="#999999">(排序号越小排在越前面 )</font>
						</td>
					</tr>
					<tr>
						<td class="table1_td"></td>
						<td></td>
					</tr>
				</table>
			</form>
			<table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
