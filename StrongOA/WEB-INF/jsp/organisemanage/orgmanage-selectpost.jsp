<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>添加岗位信息</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_add.css">
		<script type="text/javascript" src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript">
		var numtest = /^\d+$/; 
		function setpost(){
			//var retValue = window.showModalDialog("<%=path%>/organisemanage/orgmanage!setPost.action",window,'help:no;status:no;scroll:no;dialogWidth:700px; dialogHeight:500px');
			var id = document.getElementById("orgPostId").value;
			var retValue = window.showModalDialog("<%=path%>/organisemanage/orgmanage!getWholePostList.action?postId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
		}
		
		function onsubmit1(){
			document.forms("mytable").submit();
			window.close();
		}
		
		function setPostValue(id,name){
			document.getElementById("orgPostId").value='';
			document.getElementById("orgPostId").value= id;
			document.getElementById("postName").value='';
			document.getElementById("postName").value= name;
		}

	</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<iframe name="hiddenFrame" id="hiddenFrame" style="width:0;height:0;display:none"></iframe>
		<DIV id=contentborder align=center>
		<s:form id="mytable"  action="/organisemanage/orgmanage!saveOrgPost.action" theme="simple" target="hiddenFrame">
		<input type="hidden" id="orgId" name="orgId" value="${model.orgId}">
		<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>添加组织机构岗位</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="onsubmit1();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
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
				<tr>
					<td>
						<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
							<tr>
								<td class="biao_bg1" align="right" valign="top">
									<span class="wz">岗位：</span>
								</td>
								<td class="td1" colspan="3" align="left">
									<textarea id="postName" name="postName" rows="4" cols="35" readonly="readonly" onclick="setpost();">${postName}</textarea>
									<input id="orgPostId" name="orgPostId" value="${orgPostId}" type="hidden" size="30"><br>
									<a href="#" class="button" onclick="setpost();">设置</a>
								</td>
							</tr>
						</table>
						<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
						</table>
					</td>
				</tr>
			</table>
		</s:form>
		</DIV>
	</body>
</html>