<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaSysmanageBase"/>
<jsp:directive.page import="com.strongit.oa.bo.ToaSystemmanageSystemLink"/>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<title>无标题文档</title>
<link href="<%=root %>/common/temp/css.css" rel="stylesheet" type="text/css" />
<link href="<%=frameroot%>/css/toolbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=frameroot%>/css/navigator_windows.css" type=text/css rel=stylesheet>
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
<script type="text/javascript" src="<%=root %>/common/js/common/common.js"></script>

</head>
<%
	String userName = (String) request.getSession().getAttribute(
			"userName");
	ToaSysmanageBase sysTheme = request.getSession()
		.getAttribute("sysTheme")==null?null:(ToaSysmanageBase)request.getSession()
				.getAttribute("sysTheme");
	String baseTitle="";
	String baseLogoPic="";
	int baseLogoWidth=0;
	int baseLogoHeight=0;
	if(sysTheme!=null){
		baseTitle = sysTheme.getBaseTitle();
		baseLogoPic = sysTheme.getBaseLogoPic();
		baseLogoWidth=sysTheme.getBaseLogoWidth()==null?0:sysTheme.getBaseLogoWidth();
		baseLogoHeight=sysTheme.getBaseLogoHeight()==null?0:sysTheme.getBaseLogoHeight();
	}
%>
<body>
	<table width="100%" height="100%" border="0" align="center"
		cellpadding="0" cellspacing="0" bgcolor="#8d2001">
		<tr>
			<td height="100%" bgcolor="#8d2001" class="topbg">
				<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="10%">
							&nbsp;
						</td>
						<td width="5%" align="right">
							<%
							if (baseLogoPic != null && !"".equals(baseLogoPic)
								&& !"null".equals(baseLogoPic) && !"0".equals(baseLogoPic)) {
							%>
							<img id="logos"
								src="<%=frameroot%>/images/perspective_toolbar/<%=baseLogoPic%>"
								width="36" height="39" />
							<%
							}
							%>
						</td>
						<td width="50%" class="titleword">
							<%
							if (baseTitle == null) {
							%>
							江西省财政厅协同办公软件
							<%
							} else if (baseTitle != null && baseTitle.indexOf(".") != -1) {
							%>
							<img src="<%=frameroot%>/images/<%=baseTitle%>" width="264"
								height="39" align="middle" />
							<%
							} else {
							%>
							<%=baseTitle%>
							<%
							}
							%>
						</td>
						<td width="35%" class="titleword" align="center" valign="bottom">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td width="50" height="37" class="help">
										用户：
									</td>
									<td width="100" class="help">
										<%=userName%>
									</td>
									<td width="29" align="center" class="help">
										<img src="<%=frameroot%>/images/perspective_toolbar/help.gif"
											width="14" height="14" />
									</td>
									<td width="42" class="help">
										<a href="<%=path %>/common/helpfiles/simpleHelp.doc">帮助</a>
									</td>
									<!-- 
									<td width="28" align="center" class="help"
										style="cursor: hand;">
										<img
											src="<%=frameroot%>/images/perspective_toolbar/about.gif"
											width="14" height="14" onclick="showCompanyDetail()" />
									</td>
									<td width="39" class="help" onclick="showCompanyDetail()"
										style="cursor: hand;">
										关于
									</td>
									-->
									<td width="28" align="center" class="help"
										style="cursor: hand;">
										<img
											src="<%=frameroot%>/images/perspective_toolbar/about.gif"
											width="14" height="14" onclick="showCompanyDetail()" />
									</td>
									<td width="90" class="help" onclick="changePassword()"
										style="cursor: hand;">
										修改密码
									</td>
									<td width="28" align="center" class="help"
										style="cursor: hand;">
										<img src="<%=frameroot%>/images/perspective_toolbar/exit.gif"
											width="14" height="14" onclick="gotologin()" />
									</td>
									<td width="50" class="help" onclick="gotologin()"
										style="cursor: hand;">
										退出
									</td>
									<td width="120">
										<s:if test="#session.linkList.size()>0">
											<s:select list="#session.linkList" listKey="linkUrl"
												listValue="systemName" id="systemUrl" name="systemLink"
												headerKey="" headerValue="请选择"
												onchange="jumpMenu(this.value)" />
										</s:if>
										<s:else>
											<!-- 
											<select id="systemUrl" name="systemLink">
												<option value="">
													请选择
												</option>
											</select>
											-->
										</s:else>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<script type="text/javascript">
		$(document).ready(function(){
			var obj=document.getElementById("logos");	//顶部图标对象
			if(obj!=null&&obj!=undefined){
				obj.align="middle";
				var imageWidth="<%=baseLogoWidth%>"; 		//顶部图标高度
				var imageHeight="<%=baseLogoHeight%>";	//顶部图标高度
				if(imageWidth!=null&&imageWidth!=""&&imageWidth!="0"){
					obj.width=imageWidth;
				}
				if(imageHeight!=null&&imageHeight!=""&&imageHeight!="0"){
						obj.height=imageHeight;
				}
			}	
		});	
		
		function showCompanyDetail(){
			window.showModalDialog("<%=path%>/about.jsp",window,'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:350px');
		}
		function changePassword(){
			var rValue= OpenWindow("<%=basePath%>/fileNameRedirectAction.action?toPage=myinfo/myInfo-password.jsp", '350', '150', window);
		}

		function gotologin(){
			window.top.location="<%=path%>/j_spring_security_logout";
		}

		function jumpMenu(url){
		  var xx="<%=path%>/theme/theme!jump.action?url="+url;
		  window.parent.location=xx;
		}
	</script>
</body>
</html>