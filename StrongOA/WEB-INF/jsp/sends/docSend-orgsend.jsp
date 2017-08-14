<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@include file="/common/include/rootPath.jsp"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<HTML>
	<HEAD>
		<TITLE>选择分发机构</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<link href="<%=root%>/doc/sends/dtree.css" type="text/css" rel="stylesheet">
		<link href="<%=root%>/doc/sends/windows.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="<%=root%>/doc/sends/dtree_checkbox.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<style type="text/css">
		.Operation{
			height:19px;
			line-height:19px;
			text-align:center;
			text-decoration:none;
			display:block;
			FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#fbf9f6,endColorStr=#d5d2ca);
			border:1px solid #848285;
			CURSOR: hand;
		}
		</style>
		
		<script type="text/javascript">
		function selectTreeNode(checkFlag, nodeValue) {
			alert("selectTreeNode");
		}
		function SelectNodeItem(checkFlag,nodeValue) {
			alert("SelectNodeItem:");
    	}
		
		function setNodeItem(checkFlag, id, name) {
			alert("setNodeItem");
		}
		
		function  addOption(alias, strName, strValue){
			alert("addOption");
		}
		
		function  removeOption(alias, strName, strValue){ 
			alert("removeOption");
		}
		
		function initChecked() {
			alert("initChecked");
		}

		$(document).ready(function(){
			var pnum = "";
			if (typeof(setPnum) != "undefined") {
		       	 pnum = setPnum(pnum);
		        }
			 $("input[name^='printNum']").val(pnum);
			if("${flaginfo}"!=""){
				alert("${flaginfo}");
				window.close();
			}

			//发送公文
			var defPrintNum= /^((1|2|3|4|5|6|7|8|9)\d{0,3})$/;
			$("#btnOK").click(function(){
				var docId = "${docId}";
				var orgIds = "";	//主送单位
				var docprint = "";
				var orgIdss = "";	//抄送单位
				var docprints = "";
				//document.getElementById("btnOK").disabled = true;
				<c:forEach items="${requestScope.myselectedOrg}" var="selectedOrg" varStatus="status">
					var mySyscode = "${selectedOrg[0]}";
					if(document.getElementById("${selectedOrg[0]}").checked == true){
						orgIds += "${selectedOrg[0]}" + "," + "${selectedOrg[1]}" + ";";
						tempprint = $(".${selectedOrg[0]}").val();
						if(!defPrintNum.test(tempprint)){
							alert("输入的默认打印份数格式不正确，要求:\n1、只能为数字；\n2、不以0开头；\n3、不能超过四位数字；");
							return false;
						}
						docprint += tempprint + ";";
					}
				</c:forEach>
				<c:forEach items="${requestScope.myselectedOrgs}" var="selectedOrgs" varStatus="statuss">
					var mySyscode = "${selectedOrgs[0]}";
					if(document.getElementById("${selectedOrgs[0]}").checked == true){
						orgIdss += "${selectedOrgs[0]}" + "," + "${selectedOrgs[1]}" + ";";
						tempprint = $(".${selectedOrgs[0]}").val();
						if(!defPrintNum.test(tempprint)){
							alert("输入的默认打印份数格式不正确，要求:\n1、只能为数字；\n2、不以0开头；\n3、不能超过四位数字；");
							return false;
						}
						docprints += tempprint + ";";
					}
				</c:forEach>
				
				if(""==orgIds&""==orgIdss){
					alert("请选择要分发的单位！");
					return false;
				}
				
				orgIds = orgIds.substring(0,orgIds.length-1);
				orgIdss = orgIdss.substring(0,orgIdss.length-1);
				docprint = docprint.substring(0,docprint.length-1);
				docprints = docprints.substring(0,docprints.length-1);
				
				$("#btnOK").attr("disabled","disabled");   //防止多次提交表单
				
				$.post(scriptroot + "/sends/docSend!save.action",
    			  {"docId":docId,"orgIds":orgIds,"orgIdss":orgIdss,"defPrint":docprint,"defPrints":docprints},
    			  function(ret){
    				if(ret == "0"){
    					//alert("公文分发成功！");
    					returnValue = "reload";
	    				window.close();
    				}else{
    					alert("对不起，操作失败，请与管理员联系。");
    					return ;
    				}
    			  });
			});
			
			//关闭窗口
			$("#btnNO").click(function(){
				returnValue="none";
				window.close();
			});
			
			//初始化数据
			if("${recyorg}"!=""){
				$("#selectOrg").html("${recyorg}");
				$("#selectOrg > option").each(function(){
					$("input[name='rightCheckBox'][value='"+$(this).val()+"']").attr("checked",true);
				});
			}
			if("${selectedOrg}"!=""){
				$("#selectedOrg").html("${selectedOrg}");
				$("#selectedOrg > option").each(function(){
					$("input[name='rightCheckBox'][value='"+$(this).val()+"']").attr("disabled","disabled");
				});
			}
		});
		
		function OpenWindow(Url, Width, Height, WindowObj) {
			var ReturnStr = showModalDialog(Url,
											WindowObj,
											"dialogWidth:" + Width + "pt;dialogHeight:" + Height + "pt;"+
											"status:no;help:no;scroll:no;");
			return ReturnStr;
		}	

		//复选框全选
		function checkAll(){
 			var checked = document.getElementById("allselect").checked;
 			 
 			<c:forEach items="${requestScope.myselectedOrg}" var="selectedOrg" varStatus="status">
 				var object = document.getElementById("${selectedOrg[0]}");
 				object.checked=checked;
 			</c:forEach>
 			<c:forEach items="${requestScope.myselectedOrgs}" var="selectedOrgs" varStatus="statuss">
 				var object = document.getElementById("${selectedOrgs[0]}");
 				object.checked=checked;
 			</c:forEach>
		}

		</script>
	</HEAD>
	<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;" topmargin="0" leftmargin="0" style="background-color:#FFFFFF">
	<div id="forviews" style="overflow:hidden;width:100%;height:100%" >
		<div>
			<table border="0" cellpadding="2" cellspacing="0" width="100%">
				<tr>
					<td height="50"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table height="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
								            <td width="5%" align="center"><img src="<%=frameroot%>/images/ico.gif" width="9" height="9"></td>
								            <td width="75%" align="left">公文发送</td>
											<td width="15%" align="left">
												<input type="button" id="btnOK"  class="input_bg"  value="发送公文">
											</td>
											<td width="5%" align="right">
												<input type="button" id="btnNO"  class="input_ bg"  value="关闭">
											</td>
										</tr>
									</table>
								</td>
				</tr>
			</table>
			
			<select style="display: none ;" id="select_person"></select>
	</div>
	<div id="forview" style="overflow:auto;width:100%;height:91%;" >
			<table width="100%" height="90%" border="1" rules="none" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
				<tr height="10%" bgcolor="#EAEAEA">
					<td width="10%" align="left">全选：</td>
					<td><input name="allselect" checked type="checkbox" onclick="checkAll()"></td>
					<td width="50%">收文单位</td>
					<td width="25%" align="center">可打印份数</td>
				</tr>
				<tr height="15"><td>
					主送单位：
				</td></tr>
				
				<c:forEach items="${requestScope.myselectedOrg}" var="selectedOrg" varStatus="status" >
						<tr <c:if test="${status.count%2==0}">bgcolor="#EAEAEA"</c:if> height="8%"> 
							<td></td>
							<td align="left">
								<div>
									<input name="${selectedOrg[0]}" type="checkbox" checked value="${selectedOrg[0]}">
								</div>
							</td>
							<td>
								${selectedOrg[1]}
							</td>
							<td align="center">
								<input name="printNum" type="text" class="${selectedOrg[0]}" value="<%=request.getAttribute("myPrint") %>" style="width: 30%">份
							</td>
						</tr>
				</c:forEach>
				<tr height="15"><td>
					抄送单位：
				</td></tr>
				
				<c:forEach items="${requestScope.myselectedOrgs}" var="selectedOrgs" varStatus="statuss" >
						<tr <c:if test="${statuss.count%2==0}">bgcolor="#EAEAEA"</c:if> height="8%"> 
						    <td></td>
							<td align="left">
								<div>
									<input name="${selectedOrgs[0]}" type="checkbox" checked value="${selectedOrgs[0]}">
								</div>
							</td>
							<td>
								${selectedOrgs[1]}
							</td>
							<td align="center">
								<input name="printNum" type="text" class="${selectedOrgs[0]}" value="<%=request.getAttribute("myPrint") %>" style="width: 30%">份
							</td>
						</tr>
				</c:forEach>
				<tr height="15"><td>
				
				</tr>
			</table>
		</div>
		</div>
	</body>
</HTML>