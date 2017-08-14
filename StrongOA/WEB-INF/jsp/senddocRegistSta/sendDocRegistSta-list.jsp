<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>   
    <title>发文登记统计</title>
    <%@include file="/common/include/meta.jsp"%>
    <LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
    <LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
    <script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
    <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
    <script type="text/javascript"> 	
    	function detail(param){
    		location = "<%=path%>/fileNameRedirectAction.action?toPage=senddocRegistSta/sendDocRegistSta-Frame.jsp?selectType="+param;
    	}
    </script>
    <style type="text/css">
    	a:link {color: blue; text-decoration:none;}
    </style>
    <%
    	String numTotal = (String)request.getAttribute("numTotal");
    	String[] str = numTotal.split(";");
     %>
  </head>  
  <body>
  	<DIV id=contentborder align=center>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		      <tr>
		        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
			        <table width="100%" border="0" cellspacing="0" cellpadding="00">
			          <tr>
			            <td >&nbsp;</td>
			            <td width="70%">
			            	<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
			            	发文登记统计列表:
			            </td>
			            <td width="30%">
			            </td>
			          </tr>
			        </table>
		        </td>
		      </tr>
		    </table>
		    <table width="70%" border="1" cellspacing="0" cellpadding="0">
		    	<tr>
		    		<td width="20%" height="50px">&nbsp;</td>
		    		<td width="20%" align="middle">本日发文数</td>
		    		<td width="20%" align="middle">本周发文数</td>
		    		<td width="20%" align="middle">本月发文数</td>
		    		<td width="20%" align="middle">本年发文数</td>
		    	</tr>
		    	<tr>
		    		<td height="100px" align="middle">发文数</td>
		    		<td align="middle">
		    			<%if(str[0]!=null && !"0".equals(str[0])){
		    				%>
		    				<a href="javascript:detail('0');"/>
		    				<%
		    			}%>
		    			<%=str[0]%>
		    		</td>
		    		<td align="middle">
		    			<%if(str[1]!=null && !"0".equals(str[1])){
		    				%>
		    				<a href="javascript:detail('1');"/>
		    				<%
		    			}%>
		    			<%=str[1]%>
		    		</td>
		    		<td align="middle">
		    			<%if(str[2]!=null && !"0".equals(str[2])){
		    				%>
		    				<a href="javascript:detail('2');"/>
		    				<%
		    			}%>
		    			<%=str[2]%>
		    		</td>
		    		<td align="middle">
		    			<%if(str[3]!=null && !"0".equals(str[3])){
		    				%>
		    				<a href="javascript:detail('3');"/>
		    				<%
		    			}%>
		    			<%=str[3]%>
		    		</td>
		    	</tr>
		    </table>
	    </table>
    </DIV>
  </body>
</html>
