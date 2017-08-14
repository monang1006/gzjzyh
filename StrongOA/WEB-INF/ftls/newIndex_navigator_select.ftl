<!--中间分隔区域的-->
<!--newIndex_navigator_select.ftl-->
<!--首页/主框架工作区/中间分隔区域-->
<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD><TITLE></TITLE>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="<%=frameroot%>/css/toolbar.css" rel="stylesheet" type="text/css" />
</head>
  <script>
  var state = 0 ;
  function hidemenu(){
  	if(state == 0){
		parent.title.cols='*,6,100%';
		state = 1;
		document.pic.src="<%=frameroot%>/images/jiantou_2.jpg";
		document.pic.title="点击打开菜单栏";
	}else if(state == 1){
		parent.title.cols='15%,6,*';
		state = 0;
		document.pic.src="<%=frameroot%>/images/jiantou.jpg";
		document.pic.title="点击隐藏菜单栏";
	}
  }
  </script>
<BODY>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="100%" height="100%"><a href="#" onclick="hidemenu();"><img name=pic src="<%=frameroot%>/images/jiantou.jpg" width="6" height="56" border="0"  title="点击隐藏菜单栏"/></a></td>
  </tr>
</table>
</body>

</html>