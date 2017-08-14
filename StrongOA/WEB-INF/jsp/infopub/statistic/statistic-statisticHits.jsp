<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<TITLE>信息统计</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css" >
		<LINK href="<%=path%>/common/js/tabpane/css/luna/tab.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<LINK href="<%=path %>/common/frame/css/properties_windows.css" type=text/css rel=stylesheet>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<script type="text/javascript">
		function showinfo(value,name){
		      getSysConsole().navigate("<%=root%>/infopub/statistic/statistic!columnState.action?columnId="+value,name);
		}
		</script>
		
		  <style>
    .tabl2{
	
		border-left:1px #E1E1E1 solid;
		border-top:1px #E1E1E1 solid;

		}
	.tabl2 td{
		
		border-bottom:1px #E1E1E1 solid;
		border-right:1px #E1E1E1 solid;
		}	
  </style>
  </head>
  
  <body  class=contentbodymargin oncontextmenu="return false;" >
  <script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
  <DIV id=contentborder align=center>
   <table width="100%"><tr><td>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    
    	
      <tr>
        <td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
        <table width="100%" border="0" cellspacing="0" cellpadding="00">
          <tr>
           <td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>点击量统计</strong>
							</td>
						</tr>
					</table>
				</td>
          </tr>
        </table>
        </td>
      </tr>
    </table>			
  <hr><br> 
       <table width="100%">
   <tr><td width="50%" valign="top" >
   <img src="<%=root%>/infopub/statistic/statistic!statisticHits.action?jpgtype=bar"/>
   </td>
   <td valign="top" width="50%">
   <div style="font-weight:bolder; font-size:18px; text-align:center; width:80%;height:28px;">点击量统计表</div>
    <table cellpadding="1" cellspacing="1" width='80%' class="tabl2" >
    <tr height="25px;">
   <td align="center"  bgcolor="#E1E1E1">序号</td>
   <td align="center" bgcolor="#E1E1E1">文章名称</td>
   <td align="center" bgcolor="#E1E1E1">点击数</td>
   </tr>
    <c:forEach items="${list}"  var="vo" varStatus="index" begin="${(pagenow-1)*pagesize}" end="${(pagenow-1)*pagesize+pagesize}">
   <tr height="25px;">
   <td align="center" width="10%">${index.index+1 }</td>
   <td align="left" style="padding-left:10px;" width="70%"><a href="javascript:showinfo('${vo.columnId }','${vo.columnName }')">
   <font color="#3366cc">${vo.columnName }</font></a>
   </td>
   <td align="center" width="20%">${vo.pubCount }</td>
    </tr>
   </c:forEach>
    <c:if test="${hpsize>1}">
    <tr height="25px;">
   <td colspan="3">
	       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;第${pagenow }/${hpsize }页
  
		   <a href="<%=root%>/infopub/statistic/statistic!statisticHits.action?pagenow=1">首页</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<c:choose>
			<c:when test="${pagenow>1}">
				<a href="<%=root%>/infopub/statistic/statistic!statisticHits.action?pagenow=${pagenow-1 }">上一页</a>
			</c:when>
		
		</c:choose>&nbsp;&nbsp;&nbsp;&nbsp;
		<c:choose>
			<c:when test="${pagenow<hpsize}">
				<a href="<%=root%>/infopub/statistic/statistic!statisticHits.action?pagenow=${pagenow+1 }">下一页</a>
			</c:when>
				</c:choose>
	&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="<%=root%>/infopub/statistic/statistic!statisticHits.action?pagenow=${hpsize }">尾页</a>
	    </td>
   </tr>
	     </c:if>
	
   </table>
   </td>
   </tr>
   </table>
  </DIV>
  </body>
</html>
  