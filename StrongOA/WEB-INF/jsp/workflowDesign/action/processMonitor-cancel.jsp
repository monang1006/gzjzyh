<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/tags/strongitJbpm" prefix="strongit"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>取消任务</title>
		<link href="<%=frameroot%>/css/windows.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
		<script type="text/javascript">

		
</script>

	</head>
	<body class="contentbodymargin">
		<div id="contentborder" align="center">
			<table width="98%" cellspacing="0" border="1"
				bordercolordark="#FFFFFF" bordercolorlight="#9ABEDA"
				bordercolor="#333300">
				<tr>
					<td colspan="2" align="center" valign="bottom">
						<h4>
							请选择要取消的并发任务
						</h4>
					</td>
				</tr>
				<tr>
					<td class="titleTD">
						&nbsp;选择
					</td>
					<td class="titleTD">
						&nbsp;名称
					</td>
				</tr>
				<c:forEach items="${lst}" var="task" varStatus="status">
					<tr>
						<td>
							&nbsp;
							<c:if test="${task[5] == '1'}">
								<input type="checkbox" name="checkb" value='<c:out value="${task[0] }"/>'/>
							</c:if>
							<c:if test="${task[5] == '0'}">
								<input type="checkbox" name="checkb" value='<c:out value="${task[0] }"/>' disabled/>
							</c:if>
						</td>
						<td>
							&nbsp;
							<c:out value="${task[4]}" />
						</td>
					</tr>
				</c:forEach>
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
					<td colspan="2" align="center" valign="bottom">
						<h4>
							请选择取消的任务要跳转到的节点
						</h4>
					</td>
				</tr>
				<tr>
					<td class="titleTD">
						&nbsp;选择
					</td>
					<td class="titleTD">
						&nbsp;节点名称
					</td>
				</tr>
				<c:forEach items="${nodeLst}" var="node" varStatus="status">
					<tr>
						<td>
							&nbsp;
							<input type="radio" name="ro" value='<c:out value="${node.nsNodeId }" />'></input>
						</td>
						<td>
							&nbsp;
							<c:out value="${node.nsNodeName }" />
						</td>
					</tr>
				</c:forEach>
			</table>
			<table>
				<tr>
					<td><input type="button" value="确定" onclick="ok()"></input><input type="button" value="取消" onclick="cancel()"></input></td>
				</tr>
			</table>
		</div>
	</body>
</html>
<script>
	function ok(){
		var chkValue = "";
		var radioValue = "";
		var chkboxes = document.getElementsByName("checkb");
		var radios = document.getElementsByName("ro");
		for(var i=0; i<chkboxes.length; i++){
			var chkbox = chkboxes[i];
			if(chkbox.checked == true){
				chkValue = chkValue + "," + chkbox.value;
			}
		}
		for(var i=0; i<radios.length; i++){
			var ro = radios[i];
			if(ro.checked == true){
				radioValue = ro.value;
			}
		}
		if(chkValue == "" || radioValue == ""){
			alert("请选择相应的信息!");
			return;
		}
		//取消并发任务
		$.ajax({
    		type:"post",
    		url:"<%=root%>/workflowDesign/action/processMonitor!cancelConcurrencyTask.action",
    		data:{
	    		tokenIds: chkValue,
	    		nodeId: radioValue
    		},
    		success:function(result){
    			window.dialogArguments.location = "<%=root%>/workflowDesign/action/processMonitor!refreshMonitorPage.action?taskId=${taskId}";
    			window.close();
    		},
    		error:function(){
    			alert("异步出错！");
    		}
    	});
	}

	function cancel(){
		window.close();
	}
</script>