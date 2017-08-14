<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> <meta content="text/html; charset=utf-8" http-equiv="Content-Type" /> 
<title>我的消息</title> <meta content="m.taobao.com" name="author" /> 
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
.eblue{color:#CC6600;font-size:12px;}
.eblue a{color:red;}
.eblue a:hover{color:red;}
.titlespan{ height:37px; line-height:37px; background-color:#bfd6e6; }
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

		<%--
				<table width="100%" border=1>
				<tr class=sec>
				<td width="5%" align="center">状态</td>
				<td width="10%" align="center">发件人</td>
				<td width="65%" align="center">标题</td>
				<td width="20%" align="center">日期</td>
				</tr>
				--%>
		<div id="container">
			<div class="nav" style="width: 540px;">
			<div class="ntopcenter">邮件列表</div>
					<div class="ntopbot clearfix">
						<div >
						<a href="<%=root%>/wap/login!getMainInfo.action"><img src="<%=root%>/oa/image/login/sytctop2.png" /></a>
						<a href="<%=root%>/j_spring_security_logout"><img src="<%=root%>/oa/image/login/sytctop3.png" /></a>
   	 					</div>
					</div>
			
			</div>
			<div class="con">
				<div class="lby clearfix">
					<table width="100%">
						<tr>
						<td align="left">
							<form action="<%=root%>/wap/message!waprecvlist.action"
										method="post">
										<input type="text" id="searchTitle" name="searchTitle"
											value="${searchTitle}" size="12" />
										<input type="submit" value="查询" class="button" />
									</form>
						</td>
						<td align="left">
						<form action="<%=root%>/wap/message!wapnew.action?forward=wapnew" method="post">
										<input type="submit" value="新增" class="button">
										</form>
						</td>
						</tr>
						<tr>
							<td class="titlespan" colspan="2">
								
									<span class="gray"> <jsp:include flush="true"
											page="/oa/include/splitMessagePage.jsp"></jsp:include> </span>
								
							</td>
						</tr>
					</table>
					<table width="100%">


						<c:forEach items="${androidpage.result}" var="dataRow"
							varStatus="status">
							<tr>
								<td>
									<div>
												<c:if test="${dataRow[2]==0}">
													<img alt="未读" src="<%=root%>/oa/image/message/unread.gif" />
												</c:if>
												<c:if test="${dataRow[2]==1}">
													<img alt="已读" src="<%=root%>/oa/image/message/read.gif" />
												</c:if>
												<c:set var="s" value="${dataRow[1]}" scope="request" />
												<%
													String str = (String) request.getAttribute("s");
														String links;
														if (str.length() > 12) {
															links = str.substring(0, 12) + "...";
														} else {
															links = str;
														}
												%>
												<a href="<%=root%>/wap/message!wapview.action?forward=wapview&currentPage=<c:out value='${currentPage}'/>&msgId=<c:out value="${dataRow[0]}"/>"><%=links%></a>
												<br/><c:out value="${dataRow[5]}" />
												<%--<c:out value="${dataRow[3]}" />--%>
												
												<c:set var="s1" value="${dataRow[3]}" scope="request" />
												<%
													Date str1 = (Date) request.getAttribute("s1");
													String viewTime=str1.toString();
													if(viewTime.indexOf(".")!=-1){
														viewTime=viewTime.substring(0,viewTime.indexOf("."));
													}
												%>
												<%=viewTime%>
												</div>
									
								</td>
								
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>
