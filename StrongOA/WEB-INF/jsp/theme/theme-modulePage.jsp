<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>协同办公软件</TITLE>
		<%@include file="/common/include/meta.jsp"%>
	</HEAD>
	<style type="text/css">
a {
	color: #000;
	cursor: pointer;
	text-decoration: none;
}

a:hover {
	color: #00457d;
	text-decoration: underline;
}

.main {
	padding: 30px 20px;
}

.main li {
	width: 160px;
	height: 150px;
	float: left;
	text-align: center;
}

.main li p {
	overflow: hidden;
	width: 120px;
	height: 20px;
	margin: 0 auto;
}
</style>
<script type="text/javascript">
<!--
	function showNewWindow(url,title)
	{
		try
		{

			window.parent.navigate(url,title);
		}
		catch (e)
		{
			alert(e.message);
		}
	}
//-->
</script>
	<body>
		<s:form action="" id="mytable" theme="simple">
			<div class="main" style="height: 500px; overflow: auto;">
				<ul>
					<s:iterator value="privilList" id="privil">
						<li>
							<a  href="javascript:void(0);" onclick="showNewWindow('<%=path%>/<s:property value="#privil.privilAttribute"/>','<s:property value="#privil.privilName" />')"><img
									src="<%=path%>/common/images/b_xiugai.gif" border="0" /> </a>
							<p>
								<s:property value="#privil.privilName" />
							</p>
						</li>
					</s:iterator>
				</ul>
			</div>
		</s:form>
	</body>
</HTML>
