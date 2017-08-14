<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head> 
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" /> 
		<c:if test="${worktype=='1'}">
			<title>公文管理</title>
		</c:if>
		<c:if test="${worktype!='1'}">
			<title>待办事宜</title>
		</c:if>
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
.eblue{color:#CC6600;font-size:12px;}
.eblue a{color:red;}
.eblue a:hover{color:red;}
.titlespan{ height:37px; line-height:37px; background-color:#bfd6e6; }
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
  				<div class="nav">
  					<div class="ntopcenter">
  						<c:if test="${worktype=='1'}">
								公文管理
						</c:if>
						<c:if test="${worktype!='1'}">
								待办事宜
						</c:if>
					</div>
					<div class="ntopbot clearfix">
						<a href="<%=root%>/wap/login!getMainInfo.action"><img src="<%=root%>/oa/image/login/sytctop2.png" /></a>
						<a href="<%=root%>/j_spring_security_logout"><img src="<%=root%>/oa/image/login/sytctop3.png" /></a>
					</div>
				</div>
  				<div class="con">
  			  		<div class="lby clearfix">
	     				 <table width="100%">
								<form action="<%=root%>/wap/work.action" method="post" id="mytable">
							<tr>
								<td>
									标&nbsp;&nbsp;&nbsp;&nbsp;题：<input type="text" style="width:40%" name="businessTitle" value="${businessTitle}" size="10"/>
								</td>
							</tr>
							<tr>
								<td>
									主办人：<input type="text" style="width:40%" name="wapUserName" value="${wapUserName}" size="10"/>&nbsp;&nbsp;<input type="submit" value="查询" class="button" /><font color="red">${rtnContent}</font>
								</td>				
							</tr>
							<tr><td class="titlespan">
								<input type="hidden" name="worktype" value="${worktype}"/>
								<input type="hidden" name="currentPage" value="1"/>
								<input type="hidden" name="listMode" value="10"/>
								<jsp:include flush="true" page="/oa/include/splitPage.jsp"></jsp:include>
								</td>
							</tr>
								</form>
								<c:if test="${pageWorkflow.result!=null}">
								<c:forEach items="${pageWorkflow.result}" var="dataRow" varStatus="status">
							<tr><td>
									<%--<c:out value="${status.index+1}"/>.
									<strong><c:out value="${dataRow[8]}"/></strong> - --%>
								<c:out value="${dataRow[6]}"/>（<c:out value="${dataRow[1]}"/>）
								<br/>
								<c:out value="${dataRow[7]}"/>主办&nbsp;&nbsp;    
						
								<a href="<%=root%>/wap/work!wapViewForm.action?taskId=${dataRow[0]}&listMode=1&businessTitle=${businessTitle}&userName=${userName}&instanceId=${dataRow[3]}&currentPage=${currentPage}&disLogo=view&worktype=${worktype}">查看</a>&nbsp;
								<a href="<%=root%>/wap/work!wapViewForm.action?taskId=${dataRow[0]}&listMode=1&businessTitle=${businessTitle}&userName=${userName}&instanceId=${dataRow[3]}&currentPage=${currentPage}&worktype=${worktype}">办理</a>&nbsp;
								<c:if test="${dataRow[10]!='0'}">
									<%--<a href="<%=root%>/wap/work!getDocContent.action?taskId=${dataRow[0]}&worktype=${worktype}">正文</a>&nbsp;--%>
									<a href="${dataRow[10]}">正文</a>&nbsp;
								</c:if>
								<a href="<%=root%>/wap/work!wapannallist.action?taskId=${dataRow[0]}&currentPage=${currentPage}&businessTitle=${businessTitle}&userName=${userName}&worktype=${worktype}">办理记录</a>&nbsp;&nbsp;
								</td>
							</tr>
								</c:forEach>
								</c:if>
						</table>
					</div><!-- lby clearfix -->
				</div><!-- con -->
			</div><!-- container -->
	</body>
</html>
