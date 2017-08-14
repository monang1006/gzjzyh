<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaMessage"/>
<%
	ToaMessage msg=(ToaMessage)request.getAttribute("model");
	String con="";
	con=msg.getMsgContent();
	int rows=4;
	if(con!=null&&!"".equals(con)&&con.length()>30){
		rows=(con.length()/10+1);
	}
 	String rcvNames=(String)request.getAttribute("msgReceiverNames");
	int rcvRows=1;
	if(rcvNames!=null&&!"".equals(rcvNames)&&rcvNames.length()>10){
		rcvRows=(rcvNames.length()/10+1);
	}
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> <meta content="text/html; charset=utf-8" http-equiv="Content-Type" /> 
<title>编辑消息</title> <meta content="m.taobao.com" name="author" /> 
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
			<div class="nav" style="width: 540px;">
			<div class="ntopcenter">编辑邮件</div>
				<div class="ntopbot clearfix">
					<div>
						<c:if test="${forward=='reply'}">
							<a
								href="<%=root%>/wap/message!wapview.action?forward=wapview&msgId=${model.msgId}"
								class="button"><img
									src="<%=root%>/oa/image/login/sytctop2.png" />
							</a>
							<a href="<%=root%>/wap/login!getMainInfo.action"><img src="<%=root%>/oa/image/login/sytctop1.png" /></a>
							<a href="<%=root%>/j_spring_security_logout"><img src="<%=root%>/oa/image/login/sytctop3.png" /></a>
						</c:if>
						<c:if test="${forward=='forward'}">
							<a
								href="<%=root%>/wap/message!wapview.action?forward=wapview&msgId=${model.msgId}"
								class="button"><img
									src="<%=root%>/oa/image/login/sytctop2.png" />
							</a>
							<a href="<%=root%>/wap/login!getMainInfo.action"><img src="<%=root%>/oa/image/login/sytctop1.png" /></a>
							<a href="<%=root%>/j_spring_security_logout"><img src="<%=root%>/oa/image/login/sytctop3.png" /></a>
						</c:if>
						<c:if test="${forward!='reply'&&forward!='forward'}">
							<a href="<%=root%>/wap/message!waprecvlist.action" class="button"><img
									src="<%=root%>/oa/image/login/sytctop2.png" />
							</a>
							<a href="<%=root%>/wap/login!getMainInfo.action"><img src="<%=root%>/oa/image/login/sytctop1.png" /></a>
							<a href="<%=root%>/j_spring_security_logout"><img src="<%=root%>/oa/image/login/sytctop3.png" /></a>
						</c:if>
					</div>
				</div>

			</div>
			<div class="con">
				<div class="xqy clearfix">
					<table width="100%">
						<tr>
							<th>
							</th>
						</tr>
						<tr>
							<td class="xqytdta">
								
									<table width="100%">
										<c:if test="${forward!='reply'}">
											<tr align="center">
												<td></td>
												<td align="center">
													<form action="<%=root%>/wap/addressOrg!toChooseUsers.action?chooseType=message&forward=${forward}&currentPage=${currentPage}&orgId=${msgId}" method="post">
															<input type="submit" value="选择人员" >
													</form>
													<%--<a
														href="<%=root%>/wap/addressOrg!toChooseUsers.action?chooseType=message&forward=${forward}&currentPage=${currentPage}&orgId=${msgId}"
														class="button">选择人员</a>--%>
												</td>
											</tr>
										</c:if>
										<form action="<%=root%>/wap/message!wapSendMessage.action"
												method="post">
										<input type="hidden" name="disLogo" value="${forward}" />
										<input type="hidden" name="msgId" value="${msgId}" />
										<tr>
											<td  nowrap="nowrap" align="center" valign="top">
												收件人：
											</td>
											<td valign="top">	
												<textArea name="msgReceiverNames" rows="<%=rcvRows%>" readonly='readonly'/>${msgReceiverNames}</textarea>
												<input type="hidden" name="msgReceiverIds"
													value="${msgReceiverIds}" />
												<font color="red">*${info3 } </font>
											</td>
										</tr>
										<tr>
											<tr>
												<td align="center" valign="top">
													标&nbsp;&nbsp;题：
												</td>
												<td valign="top">	
													<c:if test="${forward=='forward'}">
														<c:if test="${model.msgTitle!=null&&model.msgTitle!=''}">
															<input type="text" name="model.msgTitle"
															value="FW:${model.msgTitle}" />
														</c:if>
														<c:if test="${model.msgTitle==null||model.msgTitle==''}">
															<input type="text" name="model.msgTitle"
															value="${model.msgTitle}" />
														</c:if>
													</c:if>
													<c:if test="${forward!='forward'&&forward!='reply'}">
															<input type="text" name="model.msgTitle"
															value="${model.msgTitle}" />
													</c:if>
													<c:if test="${forward=='reply'}">
														<input type="text" name="model.msgTitle" value="RE:${model.msgTitle}"/>
													</c:if>
													<font color="red">*${info2 } </font>
												</td>
											</tr>
											<tr>
												<td align="center" valign="top">
													内&nbsp;&nbsp;容:
												</td>
												<td valign="top">	
													<c:if test="${forward!='reply'}">
														<textArea rows="<%=rows%>" name="model.msgContent"><c:if test="${model.msgContent!=''&&model.msgContent!=null}">${model.msgContent}</c:if></textArea>
													</c:if>
													<c:if test="${forward=='reply'}"><textArea rows="4"  name="model.msgContent"></textArea></c:if>
													<font color="red">*${info1 } </font>
												</td>
											</tr>
											<tr>
												<td>
												</td>
												<td align="left">
													<input type="submit" class="button" value="发送" />
													<%--<c:if test="${forward=='reply'}">
														<a
															href="<%=root%>/wap/message!wapview.action?forward=wapview&msgId=${model.msgId}"
															class="button">返回</a>
													</c:if>
													<c:if test="${forward=='forward'}">
														<a
															href="<%=root%>/wap/message!wapview.action?forward=wapview&msgId=${model.msgId}"
															class="button">返回</a>
													</c:if>
													<c:if test="${forward!='reply'&&forward!='forward'}">
														<a href="<%=root%>/wap/message!waprecvlist.action"
															class="button">返回</a>
													</c:if>--%>
												</td>
											</tr>
											</form>
									</table>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>
