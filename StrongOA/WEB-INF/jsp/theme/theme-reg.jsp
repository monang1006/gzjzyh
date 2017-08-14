<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		<title>注册信息</title>
		<link href="<%=root %>/common/error/error.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path %>/common/js/upload/myjquery.MultiFile.js"></script>
		<script language="javascript" src="<%=path %>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" src="<%=path %>/common/js/common/common.js"></script>
		<script type="text/javascript">
			function submitForm(){
				 var isSelectFile = false;
			     $("input:file.multi").each(function(){
			     	if($(this).val()!=""){
			     		isSelectFile = true;
			     	}
			     });
			     if(!isSelectFile){
			     	alert("对不起，请您选择对应的License文件");
			     	return ;
			     }
				document.forms[0].submit();
			}
			function myAlert(rev){
				if(rev=="success"){
					if(confirm("License文件上传成功！是否重新登录")){
						window.location.reload();
					}
				}else if(rev=="empty"){
					alert("License文件流异常，请您重新上传");
				}else if(rev=="uperror"){
					alert("License文件上传异常，请您重新上传");
				}else{
					alert("未知异常，请您重新上传");
				}
			}
		</script>
	</head>
	<body>
		<DIV id=error>
			<h1>请您使用正版思创数码科技股份有限公司协同办公系统</h1>
			<h3>License错误信息：${error }<a href="#" onclick="history.go(-1);">返回</a></h3>
			<h3>服务器识别码信息：${code }</h3>
			<ul>
				<h4>如何注册思创协同办公系统？</h4>
				<li>查看License错误信息</li>
				<li>将服务器识别码信息发送给系统生产厂商或系统管理员获取License文件</li>
				<li>利用本页面上传功能上传至服务器<br />
				  <form action="<%=path %>/theme/theme!uploadLicense.action" method="post" encType="multipart/form-data" target="hidden_frame">
				  	<input type="file" onkeydown="return false;" id="file" name="file" class="multi max-1" accept="license" />
				  	<input type="button" name="sublicense" value="上传" onclick="submitForm()">
				  </form>
				</li>
			</ul>
		</DIV>
		<iframe name='hidden_frame' id="hidden_frame" style='display:none'></iframe>
	</body>
</html>

