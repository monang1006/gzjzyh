<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>编制编辑</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript" language="javascript"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT>
			
		//关闭窗体
		function col(){
		window.close();
		}
		
		function onSub(){
		var orgid=$("#orgId").val();
		var strucType=$("#strucType").val();
		var strucNumber=$("#strucNumber").val();
		var strucId=$("#strucId").val();
		if(orgid==""){
		alert("请选择所属机构！");
		return;
		}else if(strucType==""){
		alert("请输入编制类型！");
		return ;
		}else if(strucNumber==""){
		  alert("请输入编制人数！");
		  return;
		}else if(strucNumber.search(/^\d+$/)==-1){
		alert("编制人数只能输入整数！");
		return;
		}else if(strucNumber=="0"){
		  alert("编制人数不可以为零！");
		  return;
		}
		
		 $.post(
              "<%=root%>/personnel/structure/personStructure!strucType.action",
                {"orgId":orgid,
                  "strucType":strucType,
                  "structureId":strucId},
                function(date){
               
                   if(date!="0"){
                   alert("该编制类型已经添加，不可以重复！");
                   return false;
                   }else{
                   $("#structureForm").submit();
                   }
               
                });
		  
		}
		function radioOrg(){
		    var audit= window.showModalDialog("<%=root%>/personnel/personorg/personOrg!radiotree.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
		    if(audit==undefined||audit==null){
		      return false;
		    }else{
              $("#orgName").val(audit[1]);	
              $("#orgId").val(audit[0]);    
		    }
		}
		
		$(document).ready(function(){
		var dictid='${model.strucType}';
		    $("#"+dictid).attr("selected",true);
		})
 		</SCRIPT>
	</head>
	<base target="_self" />
	<body class=contentbodymargin onload="" oncontextmenu="return false;">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js"
			type="text/javascript"></script>
		<DIV id=contentborder align=center>

			<s:form id="structureForm" theme="simple"
				action="/personnel/structure/personStructure!save.action"
				modth="post">
				<input type="hidden" name="model.strucId" id="strucId"
					value="${structureId }">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">

							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="50%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													保存编制
												</td>
												<td width="10%">
													&nbsp;

												</td>
												<td width="40%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">

								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">所属机构(<font color="red">*</font>)：</span>
									</td>
									<td width="79%" colspan="3" align="left" class="td1">
										<s:if test="orgId!=null">
											<input id="orgName" maxlength="25" onkeydown="return false;"
												name="org.orgName" type="text" style="width: 150"
												value="${org.orgName }" disabled>&nbsp;
									</s:if>
										<s:else>
											<input id="orgName" maxlength="25" onkeydown="return false;"
												name="org.orgName" onclick="radioOrg();" type="text"
												style="width: 150" value="${org.orgName }" disabled>
											<input type="button" name="button" value="机构"
												onclick="radioOrg();">
									&nbsp;
									</s:else>
										<input type="hidden" id="orgId" name="org.orgid"
											value="${orgId }">

									</td>
								</tr>

								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">编制类型(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
                                       <s:if test="structureId==null||structureId==''">
										<select id="strucType" name="model.strucType"
											style="width: 150">
											<s:iterator id="vo" value="dictlist">
												<option id="${vo.dictItemCode }" value="${vo.dictItemCode }">
													${vo.dictItemName }
												</option>
											</s:iterator>
										</select>
										</s:if>
										<s:else>
										<input type="text" name="model.strucTypeName" readonly="readonly" id="strucTypeName" value="${model.strucTypeName }" >
										<input type="hidden" name="model.strucType" id="strucType" value="${model.strucType }" >
										</s:else>
									</td>
								</tr>
								<tr>
									<td width="21%" height="21" class="biao_bg1" align="right">
										<span class="wz">编制数量(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">

										<input id="strucNumber" maxlength="4" name="model.strucNumber"
											style="width: 150" value="${model.strucNumber}">

									</td>
								</tr>


								<tr>
									<td height="21" class="biao_bg1" valign="top" align="right">
										<span class="wz">描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">

										<textarea id="strucDemo" rows="5" cols="35" onkeydown=""
											name="model.strucDemo" maxlength="200">${model.strucDemo}</textarea>


									</td>
								</tr>

							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td></td>
								</tr>
								<tr>
									<td align="center" valign="middle">

										<input name="Submit" type="button" class="input_bg"
											value="保  存" onClick="onSub()">
										&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
										<input name="Submit2" type="button" class="input_bg"
											value="取  消" onClick="col();">

									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>
