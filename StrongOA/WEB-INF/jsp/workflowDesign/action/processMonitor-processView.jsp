<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/tags/strongitJbpm" prefix="strongit"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程监控</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
      <script src="<%=path%>/common/js/common/common.js"
      	type="text/javascript"></script>
		<script type="text/javascript">
		
		//返回列表
		function goback(){
			var processId="${processId}";
			if(window.parent.document.getElementById("isShowparentBtn") != undefined){
				window.parent.document.getElementById("isShowparentBtn").value = "true";
			}
			if(processId!=""){
				location = "<%=path%>/workflowDesign/action/processMonitor.action?processId=${processId}&proName="+encodeURI(encodeURI("${ProName}")) + "&workflowName="+encodeURI(encodeURI("${workflowName}"));
			}else{
				window.history.go(-1);
			}
		}
		
		
</script>

	</head>
	<body class="contentbodymargin">
		<!--  oncontextmenu="return false;"> -->
		<div id="contentborder" align="center">

			<table width="100%">
				<tr>
					<td height="40">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td>
								&nbsp;
								</td>
								<td>
									<!-- 
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
										height="9" alt="">&nbsp;
										流程运行情况：${proName}
									 -->
								</td>
								<td width="25%">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
											<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td style="background:url(<%=frameroot%>/images/ch_h_m.gif) repeat-x;font-weight: bold;color:white;cursor: pointer;" onclick="goback();">&nbsp;返&nbsp;回&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
											<td width="5">
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>

			<table width="98%" cellspacing="0" border="1"
				bordercolordark="#FFFFFF" bordercolorlight="#9ABEDA"
				bordercolor="#333300">
				<tr>
					<td colspan="6" align="center" valign="bottom">
						<h4>
							流程运行情况
						</h4>
					</td>
				</tr>
				<tr>
					<td class="titleTD">
						&nbsp;流程号
					</td>
					<td class="titleTD">
						&nbsp;流程名称
					</td>
					<td class="titleTD">
						&nbsp;流程业务名称
					</td>
					<td class="titleTD">
						&nbsp;流程发起人
					</td>
				</tr>
				
				<tr>
					<td width="25%">
						&nbsp;${model[0]}
					</td>
					<td width="25%">
						&nbsp;${model[1]}
					</td>
					<td width="25%">
						&nbsp;${model[3]}
					</td>
					<td width="25%">
						&nbsp;${model[5]}
					</td>
					
				</tr>
			</table>
			
			<table>
				<tr>
					<td height="15"></td>
				</tr>
			</table>
				<table width="98%" cellspacing="0" border="1"
				bordercolordark="#FFFFFF" bordercolorlight="#9ABEDA"
				bordercolor="#333300">
				<tr>
					<td colspan="5" align="center" valign="bottom">
						<h4>
							任&nbsp;&nbsp;&nbsp;&nbsp;务
						</h4>
					</td>
				</tr>
				<tr>
					<td class="titleTD">
						&nbsp;任务标志
					</td>
					<td class="titleTD">
						&nbsp;节点名称
					</td>
					<td class="titleTD">
						&nbsp;进入时间
					</td>
					<td class="titleTD">
						&nbsp;处理人
					</td>
				</tr>
				
				<c:forEach items="${model[6]}" var="variour" varStatus="status">
					<tr>
						<td width="25%">
							&nbsp;
							${variour[0]}		
						</td>
						<td width="25%">
							&nbsp;
							${variour[1]}		
						</td>
						<td width="25%">
							&nbsp;
							${variour[2]}		
						</td>
						<td width="25%">
							<input type="hidden" name="taskId"
							value="${variour[4]}">
							&nbsp;
							${variour[4]}		
						</td>
					</tr>
				</c:forEach>
			</table>
			
			<table>
				<tr>
					<td height="15"></td>
				</tr>
			</table>
		</div>
	</body>
</html>