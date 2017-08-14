<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/meta.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head> <meta content="text/html; charset=utf-8" http-equiv="Content-Type" /> 
		<title>提交下一步流程</title>
		<link rel="stylesheet" type="text/css"
			href="<%=root%>/oa/css/wap/style.css"></link>
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
html{ color: #000; background-color:#062a55; }
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
			<div class="nav" style="width: 540px;">
			<div class="ntopcenter">下一步处理</div>
				<div class="ntopbot clearfix">
					<div ><a href="<%=root%>/wap/work!handleTask.action?taskId=${taskId}&businessTitle=${businessTitle}&userName=${userName}&currentPage=${currentPage}"><img src="<%=root%>/oa/image/login/sytctop2.png" /></a><a href="<%=root%>/wap/login!getMainInfo.action"><img src="<%=root%>/oa/image/login/sytctop1.png" /></a>
    				</div>
				</div>

			</div>
			<div class="con">
				<div class="xqy clearfix">
					<table width="100%">

						<s:if test="message!=null&&message!=\"\"">
							<td class="xqytdta">
								<table width="100%">
									<tr>
										<td>
											${message}
											<s:if test="disLogo!=\"success\"">
												<form id="form3"
													action="<%=root%>/wap/work!getWapNextTransitions.action"
													method="post">
											</s:if>
											<s:else>
												<form id="form3" action="<%=root%>/wap/work.action"
													method="post">
													<s:hidden id="listMode" name="listMode" value="10"></s:hidden>
											</s:else>
											<s:hidden id="instanceId" name="instanceId"></s:hidden>
											<s:hidden id="formId" name="formId"></s:hidden>
											<!-- 任务id -->
											<s:hidden id="taskId" name="taskId"></s:hidden>
											<s:hidden id="currentPage" name="currentPage"></s:hidden>
											<s:hidden id="businessTitle" name="businessTitle"></s:hidden>
											<s:hidden id="userName" name="userName"></s:hidden>
											<s:hidden id="suggestion" name="suggestion"></s:hidden>
											<s:hidden id="updateSql" name="updateSql"></s:hidden>
											<input type="submit" value="返回" class="button" />
											</form>
										</td>
									</tr>
								</table>
							</td>
						</s:if>
						<s:else>
							<tr>
								<th>
									<%--第一步：下一步处理--%>
								</th>
							</tr>
							<tr>
								<td class="xqytdta">
									<table width="100%">
										<tr>
											<td>
												选择步骤
											</td>
										</tr>
										<tr>
											<td>
												<form id="form"
													action="<%=root%>/wap/work!getWapNextTransitions.action"
													method="post">
													<s:hidden id="instanceId" name="instanceId"></s:hidden>
													<s:hidden id="formId" name="formId"></s:hidden>
													<!-- 任务id -->
													<s:hidden id="taskId" name="taskId"></s:hidden>
													<s:hidden id="updateSql" name="updateSql"></s:hidden>
													<s:hidden id="businessTitle" name="businessTitle"></s:hidden>
													<s:hidden id="userName" name="userName"></s:hidden>
													<s:hidden id="currentPage" name="currentPage"></s:hidden>
													${transHtmls}
												</form>

												<form id="form2"
													action="<%=root%>/wap/work!wapHandleNextStep.action"
													method="post">
													<input type="hidden" name="worktype" value="${worktype}" />
													<s:hidden id="instanceId" name="instanceId"></s:hidden>
													<s:hidden id="formId" name="formId"></s:hidden>
													<s:hidden id="taskId" name="taskId"></s:hidden>
													<s:hidden id="currentPage" name="currentPage"></s:hidden>
													<s:hidden id="businessTitle" name="businessTitle"></s:hidden>
													<s:hidden id="userName" name="userName"></s:hidden>
													<s:hidden id="selectNodesInfo" name="selectNodesInfo"></s:hidden>
													<s:hidden id="transitionName" name="transitionName" />
													<input type="hidden" id="tranIds" name="tranIds"
														value="${transitionId}" />
													<s:hidden id="updateSql" name="updateSql"></s:hidden>
											</td>
										</tr>
										<tr>
											<td>
												选择人员
											</td>
										</tr>
										<tr>
											<td>
												<c:choose>
													<c:when test="${userList!=null&&userList!='[]'}">
														<%
															String[] nodesInfo = new String[3];
																		for (int i = 0; i < 3; i++) {
																			nodesInfo[i] = "";
																		}
																		String selectNodesInfo = (String) request
																				.getAttribute("selectNodesInfo");
																		if (selectNodesInfo != null)
																			nodesInfo = selectNodesInfo.split("\\|");
														%>
														<div class="sec"><%=nodesInfo[1]%>处理人：
														</div>
														<c:if test="${onlyone=='1'}">
															<c:forEach items="${userList}" var="dataRow"
																varStatus="status">
																<div class="sec">
																	<input type="radio" name="strTaskActors"
																		checked="checked"
																		value="<c:out value='${dataRow[0]}'/>" />
																	<c:out value="${dataRow[1]}" />
																</div>
															</c:forEach>
														</c:if>
														<c:if test="${onlyone=='0'}">
															<c:forEach items="${userList}" var="dataRow"
																varStatus="status">
																<div class="sec">
																	<input type="checkbox" name="strTaskActors"
																		value="<c:out value='${dataRow[0]}'/>" />
																	<c:out value="${dataRow[1]}" />
																</div>
															</c:forEach>
														</c:if>
													</c:when>
													<c:otherwise>
											${usersHtmls}
									</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<td>
												填写意见
												<textarea id="suggestion" name="suggestion"
													style="width: 80%; height: 80px" wrap="on">${suggestion}</textarea>
											</td>
										</tr>
										<tr>
											<td align="left">
												<input type="submit" id="next" value="完成" />
												<%--<a
													href="<%=root%>/wap/work!handleTask.action?taskId=${taskId}&businessTitle=${businessTitle}&userName=${userName}&currentPage=${currentPage}">返回</a>--%>
											</td>
										</tr>
										</form>
									</table>
								</td>
						</s:else>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>
