<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData" />
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/meta.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> <meta content="text/html; charset=utf-8" http-equiv="Content-Type" /> 
<title>添加人员</title> <meta content="m.taobao.com" name="author" /> 
<meta content="m.taobao.com" name="author" /> 
<meta content="1 days" name="revisit-after" /> 
<link href="http://m.taobao.com/channel/chn/wap/index_v6.xhtml#nocheck" rel="canonical" />
<meta content="" name="keywords" /> 
<meta content="" name="description" />
<meta content="width=device-width; initial-scale=1.2; minimum-scale=1.0; maximum-scale=2.0" name="viewport" />
<link href="http://a.tbcdn.cn/mw/s/hi/tbtouch/images/touch-icon.png" rel="apple-touch-icon-precomposed" /> 
<meta content="1 days" name="revisit-after" /> 
<meta content="" name="keywords" /> 
<meta content="" name="description" />
<style type="text/css">
body,h1,h2,h3,h4,h5,h6,hr,p,blockquote,dl,dt,dd,ul,ol,li,pre,form,fieldset,legend,button,input,textarea,th,td{margin:0;padding:0;}body,button,input,select,textarea{font:12px/1.5 tahoma,arial,simsun,sans-serif;}h1,h2,h3,h4,h5,h6{font-size:100%;}address,cite,dfn,em,var{font-style:normal;}code,kbd,pre,samp{font-family:courier new,courier,monospace;}small{font-size:12px;}ul,ol,li{list-style:none;}a{text-decoration:none;}a:hover{text-decoration:underline;}sup{vertical-align:text-top;}sub{vertical-align:text-bottom;}legend{color:#000;}fieldset,img{border:0;}button,input,select,textarea{font-size:100%;}table{border-collapse:collapse;border-spacing:0;}
/* common */
html{ color: #000; background-color:#edf6fc; }
body{ font-family: "微软雅黑","Microsoft YaHei","Microsoft JhengHei",STHeiti,Georgia,"Times New Roman",Times,serif; font-size:14px; background:url(resource/images/bgimg.png) no-repeat center top; }
a{ color: #333; cursor:pointer; }
a:hover{ color:#000; }
.inline-block{ display: inline-block; zoom: 1; *display: inline; vertical-align: middle; }
.clearfix:after{ content: '\0020'; display: block; height: 0; clear: both; }
.clearfix{ *zoom: 1; }
.clear{ clear:both; height:0; line-height:0; font-size:0; width:100%; overflow:hidden; }
.fri{ float: right; }
.fle{ float: left; }
.cred{ color:#f00; }
a.ered{ color: #d71304; }
a.ered:hover{ color:#000; }
.tecen{ text-align:center; }
.terig{ text-align:right; }
.tit{ font-size:14px; font-weight:bold; }
.dtmore{ font-size:12px; font-weight:normal; float:right; }
/* container */
#c{ position:relativee; width:540px; margin:0 auto; height:960px; background:url(<%=root%>/oa/image/login/bgimg.png) no-repeat center center; }
.login{ width:540px; margin:0 auto; height:473px; background:url(<%=root%>/oa/image/login/logbg.png) repeat-x center center; position:fixed; top:50%; left:50%; margin-top:-236px; margin-left:-270px; }
.logo{ padding-top:66px; text-align:center; }
.loginbar{ background:url(<%=root%>/oa/image/login/syhximg.png) no-repeat center 20px; padding-top:30px; }
.loginbar p{ margin:20px; }
p.logbarpa{ height:37px; padding-left:160px; background:url(<%=root%>/oa/image/login/yhmbg.png) no-repeat center top; }
p.logbarpa input{ width:205px; height:32px; line-height:32px; background:none; border:none; padding:3px; }
p.logbarpb{ height:37px; padding-left:160px; background:url(<%=root%>/oa/image/login/mimabg.png) no-repeat center top; }
p.logbarpb input{ width:205px; height:32px; line-height:32px; background:none; border:none; padding:3px; }
p.logbarpc{ padding-top:28px; padding-left:94px; margin:0; height:62px; background:url(<%=root%>/oa/image/login/logkimg.png) no-repeat center 21px; }
p.logbarpc input{ width:253px; height:47px; background:none; border:none; }
/* nav */
.nav{ width:100%; height:83px; background:url(<%=root%>/oa/image/login/navtopbg.png) repeat-x center top; position: fixed; top:0; }
.con{ padding-top:83px; }
.dhjm{ text-align:center; }
.dhjm td{ height:119px; padding-top:40px; }
.dhjm td a{ display:block; width:134px; height:119px; margin:0 auto; background:url(<%=root%>/oa/image/login/navlibg.png) no-repeat center center; text-align:center; }
p.dhjmpa{ padding-top:12px; padding-bottom:9px; height:72px; }
p.dhjmpb{ height:22px; background:url(<%=root%>/oa/image/login/dhjpbg.png) no-repeat center center; }
/* lb */
.lby,.xqy{ color:#214b88; background-color:#edf6fc; padding:6%; margin:10px 0; }
.lby th{ height:37px; line-height:37px; background-color:#bfd6e6; }
.lby td{ height:45px; line-height:45px; border-bottom:1px solid #bed5e6; }
.xqy th{ height:37px; line-height:37px; background-color:#bfd6e6; }
td.xqytdta{ background-color:#d8e7f2; padding:4%; }
td.xqytdta td{ padding-top:22px; }
input.xqbtdinptext{ border:1px solid #85b7da; padding:3px; width:125px; height:15px; line-height:15px; background:url(<%=root%>/oa/image/login/inputextbg.png) #fff repeat-x center top; }
input.xqbtdinpbut{ vertical-align:middle; width:57px; height:22px; background:url(resource/images/songsimg.png) no-repeat center center; border:none; }
.xqbtdimg{ vertical-align:middle; }
.ntoplogo{ height:36px; background:url() no-repeat center center; }
.ntopcenter{height:36px; font-size:20px; color:#ffffff;width:100%;align:center }
.ntopbot{ padding:4px 4px 0; color:#aeb9c9; }
</style>
</head>
	<body>
	<div id="container">
  		<div class="nav" >
       		<div class="ntopcenter">添加人员</div>
     		<div class="ntopbot clearfix">
				
					<s:if test="chooseType==\"sms\"">
						<a href="<%=root%>/wap/sms!wapInput.action?moduleCode=${moduleCode}&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}&recvUserIds=${currentUserId}"><img src="<%=root%>/oa/image/login/sytctop2.png" /></a>
						<a href="<%=root%>/wap/login!getMainInfo.action"><img src="<%=root%>/oa/image/login/sytctop1.png" /></a>
						<a href="<%=root%>/j_spring_security_logout"><img src="<%=root%>/oa/image/login/sytctop3.png" /></a>
					</s:if>
					<s:elseif test="chooseType==\"import\"">
						<a href="<%=root%>/wap/addressGroup!getAddressList.action?groupId=${groupId}"><img src="<%=root%>/oa/image/login/sytctop2.png" /></a><a href="<%=root%>/wap/login!getMainInfo.action"><img src="<%=root%>/oa/image/login/sytctop1.png" /></a>
					</s:elseif>
					<s:elseif test="chooseType==\"message\"">
						<a href="<%=root%>/wap/message!wapnew.action?forward=${forward}&msgId=${orgId}"><img src="<%=root%>/oa/image/login/sytctop2.png" /></a><a href="<%=root%>/wap/login!getMainInfo.action"><img src="<%=root%>/oa/image/login/sytctop1.png" /></a>
					</s:elseif>
					<s:else>
						<a href="<%=root%>/wap/message!wapview.action?forward=write&moduleCode=${moduleCode}&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}&msgReceiverIds=${currentUserId}"><img src="<%=root%>/oa/image/login/sytctop2.png" /></a>
						<a href="<%=root%>/wap/login!getMainInfo.action"><img src="<%=root%>/oa/image/login/sytctop1.png" /></a>
						<a href="<%=root%>/j_spring_security_logout"><img src="<%=root%>/oa/image/login/sytctop3.png" /></a>
					</s:else>
				
			</div>
    	</div>
  	<div class="con">
    	<div class="lby clearfix">
	      <table width="100%">
	    	<tr>
			<form action="<%=root%>/wap/addressOrg!toChooseUsers.action"  method="post">
					<table style="width: 100%">
						<tr>
							<td>
								<s:select list="#request.orgList" id="searchOrgId" name="searchOrgId"
									listKey="codeId" listValue="name" headerKey=""
									headerValue="请选择组织机构" cssStyle="width:80%"></s:select>
								<input type="hidden" id="chooseType" name="chooseType" value="${chooseType}"/>
								<input type="hidden" id="groupId" name="groupId" value="${groupId}"/>
								<input type="hidden" id="forward" name="forward" value="${forward}"/>
								<input type="hidden" id="moduleCode" name="moduleCode" value="${moduleCode}"/>
								<input type="hidden" id="orgId" name="orgId" value="${orgId}"/>
								<input type="hidden" id="userName" name="userName" value="${userName}"/>
								<input type="hidden" id="currentPage" name="currentPage" value="${currentPage}"/>
								<input type="hidden" id="currentUserId" name="currentUserId" value="${currentUserId}"/>
							</td>
						</tr>
						<tr>
							<td>
								<input type="text" id="searchUserName" name="searchUserName" title="输入姓名" value="${searchUserName}" style="width:58%"/>
								<input type="submit" value="查询" class="button" />
							</td>
						</tr>
					</table>
			</form>
									<c:if test="${page.result!=null}">
										<s:if test="chooseType==\"sms\"">
											<!-- 发送短信 -->
											<form action="<%=root%>/wap/sms!wapInput.action"
												method="post">
										</s:if>
										<s:if test="chooseType==\"message\"">
											<!-- 发送短信 -->
											<form action="<%=root%>/wap/message!wapSelectPerson.action"
												method="post">
										</s:if>
										<!-- 发送消息 -->
										<s:elseif test="chooseType==\"import\"">
											<!-- 导入系统人员 -->
											<form action="<%=root%>/wap/addressGroup!wapImport.action"
												method="post">
										</s:elseif>
										<s:else>
											<!-- 发送消息 -->
											<form action="<%=root%>/wap/message!wapview.action"
												method="post">
										</s:else>
										<input type="hidden" id="forward" name="forward"
											value="${forward}" />
										<input type="hidden" id="moduleCode" name="moduleCode"
											value="${moduleCode}" />
										<input type="hidden" id="msgId" name="msgId" value="${orgId}" />
										<input type="hidden" id="waporgId" name="waporgId"
											value="${orgId}" />
										<input type="hidden" id="userName" name="userName"
											value="${userName}" />
										<input type="hidden" id="currentPage" name="currentPage"
											value="${currentPage}" />
											<s:if test="chooseType==\"import\"">
												<!-- 导入系统人员 -->
												<%--
						<s:select list="#request.list" id="groupId" name="groupId"
								listKey="addrGroupId" listValue="addrGroupName" headerKey=""
								headerValue="请选择目标组"></s:select>	
						--%>
												<input type="hidden" id="chooseType" name="chooseType"
													value="${chooseType}" />
												<input type="hidden" id="groupId" name="groupId"
													value="${groupId}" />
												<input type="hidden" id="searchOrgId" name="searchOrgId"
													value="${searchOrgId}" />
												<input type="hidden" id="searchUserName"
													name="searchUserName" value="${searchUserName}" />
												<input type="hidden" id="searchCurrentPage"
													name="searchCurrentPage" value="${searchCurrentPage}" />
											</s:if>
											<%--<input type="submit" value="确定" class="button" />
											<s:if test="chooseType==\"sms\"">
												<!-- 发送短信 -->
												<a class="button1"
													href="<%=root%>/wap/sms!wapInput.action?moduleCode=${moduleCode}&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}&recvUserIds=${currentUserId}">返回</a>
											</s:if>
											<s:elseif test="chooseType==\"import\"">
												<!-- 导入系统人员 -->
												<a class="button1"
													href="<%=root%>/wap/addressGroup!getAddressList.action?groupId=${groupId}">返回</a>
											</s:elseif>
											<s:elseif test="chooseType==\"message\"">
												<a class="button1"
													href="<%=root%>/wap/message!wapnew.action?forward=${forward}&msgId=${orgId}">返回</a>
											</s:elseif>
											<s:else>
												<!-- 发送消息 -->
												<a class="button1"
													href="<%=root%>/wap/message!wapview.action?forward=${forward}&moduleCode=${moduleCode}&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}&msgReceiverIds=${currentUserId}">返回</a>
											</s:else> --%>
											<!-- 分页 -->
											<span class="gray"><jsp:include flush="true"
													page="/oa/include/search-splitPage.jsp"></jsp:include></span>
										<c:forEach items="${page.result}" var="dataRow"
											varStatus="status">
											<div class="sec">
												<s:if test="chooseType==\"sms\"">
													<!-- 发送短信 -->
													<input type="checkbox" name="recvUserIds"
														value="<c:out value="${dataRow.userId}"/>" />
												</s:if>
												<s:elseif test="chooseType==\"import\"">
													<!-- 导入系统人员 -->
													<input type="checkbox" name="users"
														value='{userName:"<c:out value="${dataRow.userName}"/>",userTel:"<c:out value="${dataRow.userTel}"/>",userPhone:"<c:out value="${dataRow.rest2}"/>",userEmail:"<c:out value="${dataRow.userEmail}"/>",userId:"<c:out value="${dataRow.userId}"/>"}' />
												</s:elseif>
												<s:else>
													<!-- 发送消息 -->
													<input type="checkbox" name="msgReceiverIds"
														value="<c:out value="${dataRow.userId}"/>,${currentUserId}"/>
												</s:else>
												<c:out value="${dataRow.userName}" />
											</c:forEach><br/>
											<input type="submit" value="确定" class="button" />
											<%--<s:if test="chooseType==\"sms\"">
												<!-- 发送短信 -->
												<a class="button1"
													href="<%=root%>/wap/sms!wapInput.action?moduleCode=${moduleCode}&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}&recvUserIds=${currentUserId}">返回</a>
											</s:if>
											<s:elseif test="chooseType==\"import\"">
												<!-- 导入系统人员 -->
												<a class="button1"
													href="<%=root%>/wap/addressGroup!getAddressList.action?groupId=${groupId}">返回</a>
											</s:elseif>
											<s:elseif test="chooseType==\"message\"">
												<a class="button1"
													href="<%=root%>/wap/message!wapnew.action?forward=${forward}&msgId=${orgId}">返回</a>
											</s:elseif>
											<s:else>
												<!-- 发送消息 -->
												<!--<a class="button1"
													href="<%=root%>/wap/message!wapview.action?forward=write&moduleCode=${moduleCode}&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}&msgReceiverIds=${currentUserId}">返回</a>--!>
											</s:else>--%>
										</form>
									</c:if>
		
        </tr>
		</table>
    </div> 
  </div>
</div>
	</body>
</html>
