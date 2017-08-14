<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存信息集</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/commontab/service.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/commontab/workservice.js" type="text/javascript" language="javascript"></script>
		<SCRIPT>
		//拦截特殊字符  
			function CheckCode(t) {  
			    var Error = "";  
			     var re = /^[a-z\d\u4E00-\u9FA5]+$/i; 
			    if (!re.test(t)) {  
			        Error = "中含有特殊字符，拒绝输入。";  
			    }  
			    return Error;  
			}  
		setPageListenerEnabled(true);//设置页面数据发生改变是否监听
		
		function gosubmit(){
			//信息集值
			var infoSetValue = document.getElementById("infoSetValue").value;
			//信息集名称
			var infoSetName = document.getElementById("infoSetName").value;
			//alert(infoSetName);
			//信息集简称
			var infoSetShort = document.getElementById("infoSetShort").value;
			//信息集排序号
			var infoSetOrderno = document.getElementById("infoSetOrderno").value;
			var msg="";
			if($.trim( infoSetValue)==""){
				if(msg==""){
				  document.getElementById("infoSetValue").focus();
				}
				msg="信息集值不能为空。\n";
				
			}else{
				if(!/^[A-Z]+([_A-Z])*$/.test(infoSetValue)){
					 document.getElementById("infoSetValue").value = "";
					 if(msg==""){
						  document.getElementById("infoSetValue").focus();
						}
					 	//alert("信息集值只能包含大写字母或_符号,并且只能以大写字母开头");
					 	
					 	msg+="信息集值只能包含大写字母或_符号,并且只能以大写字母开头。\n"
					 	
					 }
			}
			
			 
			if($.trim(infoSetName)==""){
				if(msg==""){
				  document.getElementById("infoSetName").focus();
				}
				msg+="信息集名称不能为空。\n";
				
			}else{
              var ret=CheckCode(infoSetName);
	             if(ret!=null && ret!=""){
	                 alert("信息集名称"+ret);
	                 return;
	             }
			}
			if($.trim(infoSetShort)==""){
				if(msg==""){
				  document.getElementById("infoSetShort").focus();
				}
				msg+="信息集简称不能为空。\n";
				
			}else{
			   var ret=CheckCode(infoSetShort);
	             if(ret!=null && ret!=""){
	                 alert("信息集简称"+ret);
	                 return;
	             }
			}
			if($.trim(infoSetOrderno)==""){
				if(msg==""){
				  document.getElementById("infoSetOrderno").focus();
				}
				msg+="信息集排序号不能为空。\n";
				
			}
			if(msg!=""){
				alert(msg);
				return false;
			}
			 $("#infosetForm").submit();
		}
	</SCRIPT>
		<s:head />
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="infosetForm" theme="simple" action="/infotable/infoset/infoSet!save.action">
				<input id="infoSetCode" name="model.infoSetCode" type="hidden" size="32" value="${model.infoSetCode}">
				<input name="infoSetParentid" type="hidden" size="32" value="${infoSetParentid}">
				<input name="infoSetState" type="hidden" size="32" value="${model.infoSetState}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							   <s:if test="infoSetCode==null">
										<strong>新建信息集</strong>
									</s:if>
									<s:else>
										<strong>编辑信息集</strong>
									</s:else>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="gosubmit();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="history.go(-1);">&nbsp;返&nbsp;回&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>
								
				<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
					<tr>
						<td  class="biao_bg1" align="right">
							<span class="wz">父信息集：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="infoSetParentid" name="model.infoSetParentid" type="hidden" size="32" value="${infoSetParentid}" onchange="notifyChange(true);">
							<span class="wz">${infoParentName}</span>
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" align="right">
							<span class="wz"><FONT color="red">*</FONT>&nbsp;信息集值：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:if test="model.infoSetState==1">
							
								<input id="infoSetValue" name="model.infoSetValue" type="text" size="32" value="${model.infoSetValue}" onchange="notifyChange(true);" style="color:#999;"  maxlength="20" readonly="readonly" >
							</s:if>
							<s:else>
								<input id="infoSetValue" name="model.infoSetValue" type="text" size="32" value="${model.infoSetValue}"  onchange="notifyChange(true);"  maxlength="20">
							</s:else>
						</td>
					</tr>
					<tr>
						<td  class="biao_bg1" align="right">
							<span class="wz"><FONT color="red">*</FONT>&nbsp;信息集名称：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="infoSetName" name="model.infoSetName" type="text" size="32" value="${model.infoSetName}" onchange="notifyChange(true);"   maxlength="50">
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" align="right">
							<span class="wz"><FONT color="red">*</FONT>&nbsp;信息集简称：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="infoSetShort" name="model.infoSetShort" type="text" size="32" value="${model.infoSetShort}" onchange="notifyChange(true);"   maxlength="50">
						</td>
					</tr>
					<tr>
						<td  class="biao_bg1" align="right">
							<span class="wz"><FONT color="red">*</FONT>&nbsp;信息集排序号：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="infoSetOrderno" name="model.infoSetOrderno" type="text" size="32" value="${model.infoSetOrderno}" onchange="notifyChange(true);" class="number"   maxlength="10">
						   <span style="color:#999">信息集排序序号达到最大值9999999999，排序号就为9999999999</span>
						</td>
					</tr>
					<tr>
						<td  class="biao_bg1" align="right">
							<span class="wz">信息集类型：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:select id="infoSetType" name="model.infoSetType"  list="#{'0':'需构建信息集','1':'不构建分类信息集'}" onchange="notifyChange(true);" style='width:219px;'/>
						</td>
					</tr>
					<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
				</table>
				<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
				</table>

			</s:form>
		</DIV>
	</body>
</html>
