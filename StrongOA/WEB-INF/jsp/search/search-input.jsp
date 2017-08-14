<%@ page contentType="text/html; charset=utf-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<html>
	<head>
		<title>全文检索</title>
		<link rel="stylesheet" type="text/css"
			href="<%=root%>/oa/css/work/style.css"></link>
	</head>
	<body>
		<jsp:include flush="true" page="/frame/navigation.jsp"></jsp:include>
			<div class=list>
				<div class="sec">
				<form action="<%=root%>/search/search!searchContent.action" method="post" id="mytable">
					<table style="width: 100%">
						<tr>
							<td>
								
								<select name="mode">
								<c:if test="${mode=='0' || mode==null}">
								<option value="0" selected="selected">全部</option>
								<option value="1">待办事宜</option>
								<option value="2" >通讯录</option>
								<option value="3" >手机邮件</option>
								<option value="4" >信息发布</option>
							
								</c:if>
								
								<c:if test="${mode=='1'}">
								<option value="0">全部</option>
								<option value="1" selected="selected">待办事宜</option>
								<option value="2" >通讯录</option>
								<option value="3" >手机邮件</option>
								<option value="4" >信息发布</option>
							
								</c:if>
								
								<c:if test="${mode=='2'}">
								<option value="0">全部</option>
								<option value="1" >待办事宜</option>
								<option value="2" selected="selected">通讯录</option>
								<option value="3" >手机邮件</option>
								<option value="4" >信息发布</option>
							
								</c:if>
								
								<c:if test="${mode=='3'}">
								<option value="0">全部</option>
								<option value="1" >待办事宜</option>
								<option value="2" >通讯录</option>
								<option value="3" selected="selected">手机邮件</option>
								<option value="4" >信息发布</option>
							
								</c:if>
								
								<c:if test="${mode=='4'}">
								<option value="0">全部</option>
								<option value="1" >待办事宜</option>
								<option value="2" >通讯录</option>
								<option value="3" >手机邮件</option>
								<option value="4" selected="selected">信息发布</option>
								</c:if>
								</select>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" id="searchContent" name="searchContent"
									value="${searchContent}" size="12"/>
								<input type="submit" value="查询" class="button" />
							</td>
						</tr>
					</table>
					</form>
				</div>
			
				<c:if test="${pageWorkflow.result!=null}">
				<div class="sec">
				待办事宜
				</div>
				<c:forEach items="${pageWorkflow.result}" var="dataRow" varStatus="status">
					<div class="sec">
						<c:out value="${status.index+1}"/>.
						<strong><c:out value="${dataRow[8]}"/></strong> - 
						<c:out value="${dataRow[6]}"/>（<c:out value="${dataRow[1]}"/>）
						<br/>
						<c:out value="${dataRow[9]}"/>  <c:out value="${dataRow[7]}"/>主办&nbsp;&nbsp;    
						<a href="<%=root%>/work/work!wapViewForm.action?taskId=${dataRow[0]}&listMode=1&businessTitle=${businessTitle}&userName=${userName}&instanceId=${dataRow[3]}&currentPage=${currentPage}&disLogo=view">表单</a>&nbsp;
						<a href="<%=root%>/work/work!wapViewForm.action?taskId=${dataRow[0]}&listMode=1&businessTitle=${businessTitle}&userName=${userName}&instanceId=${dataRow[3]}&currentPage=${currentPage}">办理</a>&nbsp;
						<a href="<%=root%>/work/work!wapChooseRP.action?nodeId=0&taskId=${dataRow[0]}&taskActors=''&currentPage=${currentPage}&businessTitle=${businessTitle}&userName=${userName}">指派</a>&nbsp;
						<a href="<%=root%>/work/work!annallist.action?taskId=${dataRow[0]}&currentPage=${currentPage}&businessTitle=${businessTitle}&userName=${userName}">办理记录</a>
					</div>
				</c:forEach>
				</c:if>
				
				
				
				
					<c:forEach items="${addresslist}" var="dataRow" varStatus="status">
					<c:if test="${status.count==1}">
						<div class="sec">
						通讯录
					</div>
					</c:if>
					<div class=sec>
						<img src="<%=root%>/oa/image/address/company.bmp"
							style="vertical-align: middle" />
						
							<a href="<%=root%>/address/addressOrg!userDetail.action?userId=<c:out value="${dataRow.userId}"/>&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}"
								title="查看">
						</security:authorize>
						<c:out value="${dataRow.userName}" />
						<security:authorize ifAnyGranted="001-0001000400020005">
							</a>
						</security:authorize>
						<c:out value="${dataRow.rest2}" />
						&nbsp;&nbsp;
						<security:authorize ifAnyGranted="001-0001000400020002">
							<a href="<%=root%>/sms/sms!wapInput.action?recvUserIds=<c:out value="${dataRow.userId}"/>&moduleCode=<%=GlobalBaseData.SMSCODE_ADDRESS%>&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}">发短信</a>
						</security:authorize>
						<security:authorize ifAnyGranted="001-0001000400020003">
							<a href="<%=root%>/message/message!view.action?forward=write&moduleCode=<%=GlobalBaseData.SMSCODE_ADDRESS%>&msgReceiverIds=<c:out value="${dataRow.userId}"/>&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}">发消息</a>
						</security:authorize>
					</div>
				</c:forEach>
				
				
				
				<c:if test="${androidpage.result!=null}">
				<div class="sec">
					手机邮件
				</div>
				<c:forEach items="${androidpage.result}" var="dataRow" varStatus="status">
				<div class="sec">
						<c:if test="${dataRow[2]==0}"><img alt="未读" src="<%=root%>/oa/image/message/unread.gif"/></c:if>
						<c:if test="${dataRow[2]==1}"><img alt="已读" src="<%=root%>/oa/image/message/read.gif"/></c:if>
						
						<c:set var="s" value="${dataRow[1]}" scope="request"/>
						<%
						String str=(String)request.getAttribute("s");
						String links;
						if(str.length()>12){
							links=str.substring(0,12)+"...";
						}else{
							links=str;
						}
						 %>
						<a href="<%=root%>/message/message!view.action?forward=wapview&currentPage=<c:out value='${currentPage}'/>&msgId=<c:out value="${dataRow[0]}"/>"><%=links %></a>
						<c:out value="${dataRow[5]}"/>						
						<c:out value="${dataRow[3]}"/>
						
					
					
					</div>
				</c:forEach>
				</c:if>
				
				
			${articleHtml }
			</div>
		    <jsp:include flush="true" page="/frame/MyLinuxbottom.jsp"></jsp:include>
	</body>
</html>
