<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData" />
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/meta.jsp"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head> <meta content="text/html; charset=utf-8" http-equiv="Content-Type" /> 
<title>新消息</title> <meta content="m.taobao.com" name="author" /> 
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
input.xqbtdinpbut{ vertical-align:middle; width:57px; height:22px; background:url(<%=root%>/oa/image/login/songsimg.png) no-repeat center center; border:none; }
.xqbtdimg{ vertical-align:middle; }
.ntoplogo{ height:36px; background:url() no-repeat center center; }
.ntopcenter{height:36px; font-size:20px; color:#ffffff;width:100%;align:center }
.ntopbot{ padding:4px 4px 0; color:#aeb9c9; }
</style>
</head>

<body>
<div id="container">
  <div class="nav" style="width:540px;">
  <div class="ntopcenter">新消息</div>
    <div class="ntopbot clearfix">
       
      <div ><a href="<%=root%>/wap/addressOrg!getOrguserlist.action?orgId=${orgId}&userName=${userName}&currentPage=${currentPage}"><img src="<%=root%>/oa/image/login/sytctop2.png" /></a>
      		<a href="<%=root%>/wap/login!getMainInfo.action"><img src="<%=root%>/oa/image/login/sytctop1.png" /></a>
      		<a href="<%=root%>/j_spring_security_logout"><img src="<%=root%>/oa/image/login/sytctop3.png" /></a>
      		</div>
    	</div>
    
  </div>
  <div class="con">
    <div class="xqy clearfix">
      <table width="100%">
       
      	<tr>
      	  <td class="xqytdta">
            <table width="100%">
            	<tr>
				    <td>
					<s:form id="msgForm" name="msgForm" action="/wap/message!sendMessage.action" method="post">
					<input type="hidden" id="moduleCode" name="moduleCode" value="<%=GlobalBaseData.SMSCODE_ADDRESS%>"/>
					<input type="hidden" id="disLogo" name="disLogo" value="wapSend"/>
					<input type="hidden" id="forward" name="forward" value="${forward}"/>
					<input type="hidden" id="orgId" name="orgId" value="${orgId}"/>
					<input type="hidden" id="userName" name="userName" value=message{userName}"/>
					<input type="hidden" id="currentPage" name="currentPage" value="${currentPage}"/>
					<font color="red"><s:property value="message"/></font>
					<div class=list>
						<%-- <div class="sec">
							<table style="width: 100%">
								<tr>
									<td align="left">
										
									</td>
									<td align="right">
									
										<input type="submit" name="fsbutton" id="fsbutton" class="button"
											value="发送" />
										<!--  <a href="<%=root%>/wap/addressOrg!getOrguserlist.action?orgId=${orgId}&userName=${userName}&currentPage=${currentPage}">返回</a>-->
									</td>
								</tr>
							</table>
						</div>--%>
						<table border="1" padding="1px" style="width: 98%;" align="center">
							<tr>
								<td width="25%" align="right" nowrap="nowrap">
									收件人(<font color=red>*</font>)：
								</td>
								<td width="75%" align="left">
									<s:textarea id="orgusername"
										name="msgReceiverNames" style="width:95% " rows="4"
										readonly="true"></s:textarea>
									<input type="hidden" id="orguserid" name="msgReceiverIds"
										value="${msgReceiverIds}"></input>
									<table width="100%">
										<tr width="100%">
											<td align="left" width="100%">
												<%--<s:if test="isAllUser==\"1\"">
													<input type="checkbox" name="isAllUser" id="isAllUser" value="1" checked="checked"/>
												</s:if>
												<s:else>
													<input type="checkbox" name="isAllUser" id="isAllUser" value="1"/>
												</s:else>
													所有人&nbsp; --%>
												<a style="color:red" href="<%=root%>/wap/addressOrg!toChooseUsers.action?forward=${forward}&currentUserId=${msgReceiverIds}&orgId=${orgId}&userName=${userName}&currentPage=${currentPage}&moduleCode=<%=GlobalBaseData.SMSCODE_ADDRESS%>">添加</a>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td align="right">
									<span class="wz">标&nbsp;&nbsp;题：</span>
								</td>
								<td align="left">
									<input type="text" name="model.msgTitle" id="msgTitle"
										value="${model.msgTitle}" maxlength="250" style="width:95% "/>
								</td>
							</tr>
							<tr>
								<td align="right" width="25%">
									<span class="wz">内&nbsp;&nbsp;容：</span>
								</td>
								<td align="left" width="75%">
									<s:textarea id="msgContent"
										name="model.msgContent" style="width:95% " rows="4"></s:textarea>
								</td>
							</tr>
							
						</table>
					</div>
					<div  style="margin:10">
							<s:if test="sfxyhz==\"1\"">
								<input type="checkbox" name="sfxyhz" id="sfxyhz" value="1" checked="checked"/>
							</s:if>
							<s:else>
								<input type="checkbox" name="sfxyhz" id="sfxyhz" value="1" />
							</s:else>
							需要回执&nbsp;
					</div>
					<div align="left">
						<input type="submit" name="fsbutton" id="fsbutton" class="button" value="发送" />
						<%-- <a class="button1" href="<%=root%>/wap/addressOrg!getOrguserlist.action?orgId=${orgId}&userName=${userName}&currentPage=${currentPage}">返回</a>--%>
					</div>
					</s:form>
					</td>
            	</tr>
            </table>
          </td>
        </tr>
      </table>
    </div> 
  </div>
</div>
</body>
</html>
