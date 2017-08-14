<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>代理人设置</title>
		<link href="<%=root%>/workflow/css/dtree.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="<%=root%>/workflow/js/dtree_checkbox.js"></script>
		<script>
			var imageRootPath='<%=root%>/workflow';
			 var username = new Array();
			 //得到选择用户的Id和用户名,
		    function getAllCheckValue(){
				var allvalue=",";
				for(var i=0;i<document.getElementsByName("rightCheckBox").length;i++){
					if(document.getElementsByName("rightCheckBox")[i].checked){
						var obj = document.getElementsByName("rightCheckBox")[i];
						if (obj.value.substring(0,1) == "u"){
							allvalue =obj.value.substring(1)+","+username[obj.value.substring(1)] ;
							break;
						}
					}
				}
			if(allvalue==","){
				allvalue = "";
			}			
			
			//窗口关闭时返回值  
			window.returnValue = allvalue;
			window.close();
		}
		
		function gotoSelectCheck(checkFlag,nodeValue){}
	</script>
	</head>
	<base target="_self">
	<body topmargin="0" leftmargin="0" class=contentbodymargin>
		<DIV id=contentborder cellpadding="0">
			<form>
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr><td height="5"></td>
					</tr>
					<tr>
						<td valign=top>
							<table align="center" border="0" cellpadding="2" cellspacing="0">
								<tr>
									<td height="10px"></td>
									<td>
										<div class="dtree">
											<script type="text/javascript">
												d = new dTree('d');	
												d.add('g0','-1','选择人员','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
												<c:forEach items="${userList}" var="tree" varStatus="status">
												  <c:choose>
												    <c:when test="${tree[1] == '0'}">
												      d.add('g<c:out value="${tree[0]}"/>','g0','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
												      <c:forEach items="${tree[3]}" var="user" varStatus="status">
												          d.add('u<c:out value="${user[0]}"/>','g<c:out value="${tree[0]}"/>','<c:out value="${user[1]}"/>','','','','<%=root%>/workflow/images/tree/renyuan.gif','3');
												          username["<c:out value="${user[0]}"/>"] = "<c:out value="${user[1]}"/>";
												      </c:forEach>
												    </c:when>
												    <c:otherwise>
												      d.add('g<c:out value="${tree[0]}"/>','g<c:out value="${tree[1]}"/>','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
												      <c:forEach items="${tree[3]}" var="user" varStatus="status">
												          d.add('u<c:out value="${user[0]}"/>','g<c:out value="${tree[0]}"/>','<c:out value="${user[1]}"/>','','','','<%=root%>/workflow/images/tree/renyuan.gif','3');
												          username['<c:out value="${user[0]}"/>'] = "<c:out value="${user[1]}"/>";
												      </c:forEach>
												    </c:otherwise>
												  </c:choose>						  
												  </c:forEach>
												document.write(d);
												//d.closeAll();
												</script>
										</div>
									</td>
								</tr>
							</table>
<%--							<input name="rightStr" type="hidden" value="">--%>
<%--							<input name="rightType" type="hidden" value="1">--%>
<%--							<input name="id" type="hidden" value="000">--%>
						</td>
					</tr>
				</table>
				<table align="center">
					<tr>
						<td>
<%--							<INPUT type="hidden" name="Indent_input">--%>
<%--							<input type="hidden" name="const_value">--%>
							<input type="button" value="确定" class='input_bg' onclick="getAllCheckValue();">
							<input type="button" value="取消" class='input_bg' onclick="javasctipr:window.close();">
						</td>
					</tr>
				</table>
		
			</form>
		</div>
	</body>
</html>