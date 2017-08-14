<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>关于思创数码科技股份有限公司协同办公软件</title>
<style>
body,img,h1,hr{
	margin:0;padding:0;border:0;
	}
body{
	background:#e8e8e8;
	text-align:center;
	font-size:13px;
	color:#000;
	font-family:"宋体", "微软雅黑";
	}
#about{
	width:413px;
	margin:0 auto;
	}
#about h1{
	background:url(<%=frameroot%>/images/about_03.jpg);
	width:413px;
	height:77px;
	}
#about ul{
	width:360px;
	margin:20px auto;
	text-align:left;
	}
#about ul li{
	line-height:20px;
	list-style:none;
	}
hr{
	border-bottom:1px solid #fff;
	border-top:1px solid #bfc0bb;
	width:360px;
	margin:0 auto;
	}
.btn{
	margin:5px auto;
	width:73px;
	}
.btn a:link,.btn a:visited{
	background:url(<%=frameroot%>/images/btn_o.jpg);
	}
.btn a:hover{
	background:url(<%=frameroot%>/images/btn_r.jpg);
	}
.btn a:link,.btn a:visited,.btn a:hover{
	display:block;
	width:73px;
	height:22px;
	line-height:22px;
	color:#000;
	text-decoration:none;
	}
.copylight{margin:10px 0;}
</style>
<script type="text/javascript">
<!--
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}
//-->
</script>
</head>

<body>
<div id="about">
<h1></h1>
<ul>
<li>思创数码科技股份有限公司思创数码科技股份有限公司版本：<%=version%></li>
<li>密钥长度：</li>
<li>产品ID：</li>
<li>最佳分辨率：1280*800</li>
<li class="copylight">版权所有(C)2007-2012 Strong Corp</li>
<li>本产品符合<a href="#">最终用户许可协议</a></li>
</ul>
<hr size="2" noshade="noshade" />
<p class="btn"><a href="#" onclick="window.close()">确定</a></p> 
</div>
</body>
</html>
